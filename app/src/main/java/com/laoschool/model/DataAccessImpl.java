package com.laoschool.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.NetworkResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.*;
import com.laoschool.entities.Class;
import com.laoschool.shared.LaoSchoolShared;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tran An on 14/03/2016.
 */
public class DataAccessImpl implements DataAccessInterface {

    private static DataAccessImpl mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private static String api_key = "TEST_API_KEY";

    final String LOGIN_HOST = "https://192.168.0.202:9443/laoschoolws/";
    final String HOST = "https://192.168.0.202:9443/laoschoolws/api/";

    //VDC:https://222.255.29.25:8443/laoschoolws/api
    //192.168.0.202:9443
    private DataAccessImpl(Context context) {
        mCtx = context;
        mRequestQueue = LaoSchoolSingleton.getInstance().getRequestQueue();
        HttpsTrustManager.allowAllSSL();
    }

    public static synchronized DataAccessImpl getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataAccessImpl(context);
        }
//        ImageLoader mImageLoader = new ImageLoader(LaoSchoolSingleton.getInstance().getRequestQueue(),
//                new ImageLoader.ImageCache() {
//                    private final android.support.v4.util.LruCache<String, Bitmap>
//                            cache = new android.support.v4.util.LruCache<String, Bitmap>(20);
//
//                    @Override
//                    public Bitmap getBitmap(String url) {
//                        return cache.get(url);
//                    }
//
//                    @Override
//                    public void putBitmap(String url, Bitmap bitmap) {
//                        cache.put(url, bitmap);
//                    }
//                });
//        LaoSchoolSingleton.getInstance().setImageLoader(mImageLoader);
        return mInstance;
    }

    private String getAuthKey() {
        SharedPreferences prefs = mCtx.getSharedPreferences(
                LaoSchoolShared.SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
        String auth_key = prefs.getString("auth_key", null);
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(LaoSchoolShared.KEY_STORE_ALIAS, null);
            RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();
            String decode_auth_key = LaoSchoolShared.decrypt(auth_key, privateKey);

            return decode_auth_key;
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return auth_key;
    }

    private void saveAuthKey(String auth_key) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(LaoSchoolShared.KEY_STORE_ALIAS, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
            String encrypt_auth_key = LaoSchoolShared.encrypt(auth_key, publicKey);

            SharedPreferences prefs = mCtx.getSharedPreferences(
                    LaoSchoolShared.SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
            prefs.edit().putString("auth_key", encrypt_auth_key).apply();
            return;
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences prefs = mCtx.getSharedPreferences(
                LaoSchoolShared.SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
        prefs.edit().putString("auth_key", auth_key).apply();
    }

    @Override
    public void login(final String sso_id, final String password, final AsyncCallback<String> callback) {
        // Request a string response from the provided URL.
        String url = LOGIN_HOST + "login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/login()", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Service/login()", error.toString());
                        callback.onFailure(error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("sso_id", sso_id);
                params.put("password", password);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String key = response.headers.get("auth_key");
                saveAuthKey(key);
                callback.onSuccess("the key has been encrypt");
                Log.d("Service/login()", "auth_key = the key has been encrypt");
                return super.parseNetworkResponse(response);
            }

        };

        mRequestQueue.add(stringRequest);
    }

    @Override
    public void forgotPass(final String sso_id, final String phone, final AsyncCallback<String> callback) {
        // Request a string response from the provided URL.
        String url = LOGIN_HOST + "forgot_pass";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/forgotPass()", response);
                        callback.onSuccess(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Service/forgotPass()", error.toString());
                        try {
                            if(error.networkResponse != null) {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String developerMessage = jsonObject.getString("developerMessage");
                                callback.onFailure(developerMessage);
                            }
                            else
                                callback.onFailure(error.toString());
                        } catch ( JSONException e ) {
                            callback.onFailure(error.toString());
                        } catch (UnsupportedEncodingException e){}
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sso_id", sso_id);
                params.put("phone", phone);
                return params;
            }
        };

        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions.
        //Volley does retry for you if you have specified the policy.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void getUsers(int filter_class_id, int filter_user_role, int filter_sts, final AsyncCallback<List<User>> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "users";
        url += _makeUrlgetUsers(filter_class_id, filter_user_role, filter_sts);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/getUsers()", response);
                        ListUser listUser = ListUser.fromJson(response);
                        callback.onSuccess(listUser.getList());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/getUsers()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        }
                        else {
                            Log.e("Service/getUsers()", error.toString());
                            callback.onFailure(error.toString());
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
    }

    private String _makeUrlgetUsers(int filter_class_id, int filter_user_role, int filter_sts) {
        StringBuilder stringBuilder = new StringBuilder();
        int _filter_class_id = 0, _filter_user_role = 0;

        if (filter_class_id > -1) {
            stringBuilder.append("?filter_class_id=" + filter_class_id);
            _filter_class_id = 1;
        }
        if (filter_class_id > -1 && _filter_class_id == 0) {
            stringBuilder.append("?filter_user_role=" + filter_user_role);
            _filter_user_role = 1;
        } else {
            stringBuilder.append("&filter_user_role=" + filter_user_role);
        }
        if (filter_user_role > -1 && _filter_user_role == 0) {
            stringBuilder.append("?filter_sts=" + filter_sts);
        } else {
            stringBuilder.append("&filter_sts=" + filter_sts);
        }
        return stringBuilder.toString();
    }

    @Override
    public void getUserProfile(final AsyncCallback<User> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "users/myprofile";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/gUserProfile()", response);
                        User user = User.parseFromJson(response);
                        callback.onSuccess(user);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/gUserProfile()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        }
                        else {
                            Log.e("Service/gUserProfile()", error.toString());
                            callback.onFailure(error.toString());
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
    }

    @Override
    public void getUserById(int user_id, final AsyncCallback<User> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "users/" + user_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/getUserById()", response);
                        User user = User.parseFromJson(response);
                        callback.onSuccess(user);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/getUserById()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        }
                        else {
                            Log.e("Service/getUserById()", error.toString());
                            callback.onFailure(error.toString());
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
    }

    @Override
    public void getAttendances(String filter_class_id, String filter_user_id, final AsyncCallback<List<Attendance>> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "attendances";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Service/getAttendance()", response);
                            ListAttendance attendanceList = ListAttendance.fromJson(response);
                            callback.onSuccess(attendanceList.getList());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/getAttendance()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        }
                        else {
                            Log.e("Service/getAttendance()", error.toString());
                            callback.onFailure(error.toString());
                        }
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
    }

    @Override
    public void getAttendanceById(int attendance_id, AsyncCallback<Attendance> callback) {

    }

    @Override
    public void updateAttendance(Attendance attendance, AsyncCallback<Attendance> callback) {

    }

    @Override
    public void getExamResults(int filter_class_id, int filter_user_id, AsyncCallback<List<ExamResult>> callback) {

    }

    @Override
    public void getExamResultById(int exam_id, AsyncCallback<ExamResult> callback) {

    }

    @Override
    public void getFinalResults(int filter_class_id, int filter_user_id, AsyncCallback<List<FinalResult>> callback) {

    }

    @Override
    public void getFinalResultById(int final_id, AsyncCallback<FinalResult> callback) {

    }

    @Override
    public void getTimeTables(int filter_class_id, AsyncCallback<List<TimeTable>> callback) {

    }

    @Override
    public void updateTimeTable(TimeTable timetable, AsyncCallback<TimeTable> callback) {

    }

    @Override
    public void getMessages(final String filter_class_id, final String filter_from_user_id, final String filter_from_dt,
                            final String filter_to_dt, final String filter_to_user_id, final String filter_channel,
                            final String filter_sts, final String filter_from_id, final AsyncCallback<List<Message>> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "messages";
        String makeUrl = _makeUrlgetMessages(filter_class_id, filter_from_user_id, filter_from_dt,
                filter_to_dt, filter_to_user_id, filter_channel,
                filter_sts, filter_from_id);
        url += makeUrl;
        Log.d("Service/getMessages()", "url:" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/getMessages()", response);
                        try {
                            JSONObject mainObject = new JSONObject(response);

                            int total_count = mainObject.getInt("total_count");
                            if (total_count > 0) {
                                ListMessages messages = ListMessages.fromJson(response);
                                callback.onSuccess(messages.getList());
                            } else {
                                callback.onSuccess(new ArrayList<Message>());
                            }
                        } catch (JSONException e) {
                            callback.onSuccess(new ArrayList<Message>());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/getMessages()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        }
                        else {
                            Log.e("Service/getMessages()", error.toString());
                            callback.onFailure(error.toString());
                        }
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }
        };

//      Log.d("Service/getMessage()", "URL:" + stringRequest.getUrl());
        mRequestQueue.add(stringRequest);
    }

    private String _makeUrlgetMessages(String filter_class_id, String filter_from_user_id, String filter_from_dt,
                                       String filter_to_dt, String filter_to_user_id, String filter_channel,
                                       String filter_sts, String filter_from_id) {
        int _filter_class_id = 0, _filter_from_user_id = 0, _filter_from_dt = 0,
                _filter_to_dt = 0, _filter_to_user_id = 0, _filter_channel = 0,
                _filter_sts = 0, _filter_from_id = 0;
        StringBuilder stringBuilder = new StringBuilder();
        if (!filter_class_id.trim().isEmpty()) {
            stringBuilder.append("?filter_class_id=" + filter_class_id);
            _filter_class_id = 1;
        }
        if (!filter_from_user_id.trim().isEmpty()) {
            if (_filter_class_id == 1)
                stringBuilder.append("&filter_from_user_id=" + filter_from_user_id);
            else {
                stringBuilder.append("?filter_from_user_id=" + filter_from_user_id);
                _filter_from_user_id = 1;
            }
        }
        if (!filter_from_dt.trim().isEmpty()) {
            if (_filter_class_id == 1 || _filter_from_user_id == 1)
                stringBuilder.append("&filter_from_dt=" + filter_from_dt);
            else {
                stringBuilder.append("?filter_from_dt=" + filter_from_dt);
                _filter_from_dt = 1;
            }
        }
        if (!filter_to_dt.trim().isEmpty()) {
            if (_filter_class_id == 1 || _filter_from_user_id == 1 || _filter_from_dt == 1)
                stringBuilder.append("&filter_to_dt=" + filter_to_dt);
            else {
                stringBuilder.append("?filter_to_dt=" + filter_to_dt);
                _filter_to_dt = 1;
            }
        }
        if (!filter_to_user_id.trim().isEmpty()) {
            if (_filter_class_id == 1 || _filter_from_user_id == 1 || _filter_from_dt == 1 || _filter_to_dt == 1)
                stringBuilder.append("&filter_to_user_id=" + filter_to_user_id);
            else {
                stringBuilder.append("?filter_to_user_id=" + filter_to_user_id);
                _filter_to_user_id = 1;
            }
        }
        if (!filter_channel.trim().isEmpty()) {
            if (_filter_class_id == 1 || _filter_from_user_id == 1 || _filter_from_dt == 1 || _filter_to_dt == 1 || _filter_to_user_id == 1)
                stringBuilder.append("&filter_channel=" + filter_channel);
            else {
                stringBuilder.append("?filter_channel=" + filter_channel);
                _filter_channel = 1;
            }
        }
        if (!filter_sts.trim().isEmpty()) {

            if (_filter_class_id == 1 || _filter_from_user_id == 1 || _filter_from_dt == 1 || _filter_to_dt == 1 || _filter_to_user_id == 1 || _filter_channel == 1)
                stringBuilder.append("&filter_sts=" + filter_sts);
            else {
                stringBuilder.append("?filter_sts=" + filter_sts);
                _filter_sts = 1;
            }
        }
        if (!filter_from_id.trim().isEmpty()) {
            if (_filter_class_id == 1 || _filter_from_user_id == 1 || _filter_from_dt == 1 || _filter_to_dt == 1 || _filter_to_user_id == 1 || _filter_sts == 1 || _filter_channel == 1)
                stringBuilder.append("&filter_from_id=" + filter_from_id);
            else {
                stringBuilder.append("?filter_from_id=" + filter_from_id);
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public void getMessageById(int message_id, AsyncCallback<Message> callback) {

    }

    @Override
    public void createMessage(final Message message, final AsyncCallback<Message> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "messages/create";
        //Log.d("Service/createMessage()", message.messageCreatetoString());
        final String httpPostBody = makeMessagetoJson(message);
        Log.d("Service/createMessage()", "makeMessagetoJson():" + httpPostBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/createMessage()", response);
                        Message m = Message.messageParsefromJson(response);
                        callback.onSuccess(m);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/createMessage()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        }
                        else {
                            Log.e("Service/createMessage()", error.toString());
                            callback.onFailure(error.toString());
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return httpPostBody.getBytes();
            }
        };

        mRequestQueue.add(stringRequest);
    }

    private String makeMessagetoJson(Message message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("school_id", message.getSchool_id());
        jsonObject.addProperty("class_id", message.getClass_id());
        //jsonObject.addProperty("title", message.getTitle());
        jsonObject.addProperty("content", message.getContent());
        jsonObject.addProperty("from_usr_id", message.getFrom_usr_id());
        jsonObject.addProperty("to_usr_id", message.getTo_usr_id());
        Gson gson = new Gson();
        String jsonString = gson.toJson(jsonObject);
        Log.d("", "makeMessagetoJson():" + jsonString);
        return jsonString;
    }

    @Override
    public void updateMessage(Message message, final AsyncCallback<Message> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "messages/update/" + message.getId() + "?is_read=1";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/updateMessage()", response);
                        Message m = Message.messageParsefromJson(response);
                        callback.onSuccess(m);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/updateMessage()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        }
                        else {
                            Log.e("Service/updateMessage()", error.toString());
                            callback.onFailure(error.toString());
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
    }

    @Override
    public void deleteMessage(int message_id, AsyncCallback<String> callback) {

    }

    @Override
    public void getSchoolById(int school_id, AsyncCallback<School> callback) {

    }

    @Override
    public void getClassById(int class_id, AsyncCallback<Class> callback) {

    }

    @Override
    public void getNotification(final AsyncCallback<List<Message>> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "notifies?filter_class_id=" + LaoSchoolShared.myProfile.getEclass().getId() + "&filter_to_user_id" + LaoSchoolShared.myProfile.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Service/gNotification()", response);
                            ListMessages messages = ListMessages.fromJson(response);
                            callback.onSuccess(messages.getList());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/gNotification()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        }
                        else {
                            Log.e("Service/gNotification()", error.toString());
                            callback.onFailure(error.toString());
                        }
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }


        };

        mRequestQueue.add(stringRequest);
    }

    @Override
    public void getNotification(int filter_from_id, final AsyncCallback<List<Message>> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "notifies?filter_class_id=" + LaoSchoolShared.myProfile.getEclass().getId() + "&filter_to_user_id" + LaoSchoolShared.myProfile.getId() + "&filter_from_id=" + filter_from_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Service/gNotification()", response);
                            JSONObject mainObject = new JSONObject(response);
                            int total_count = mainObject.getInt("total_count");
                            if (total_count > 0) {
                                ListMessages messages = ListMessages.fromJson(response);
                                callback.onSuccess(messages.getList());
                            } else {
                                callback.onSuccess(new ArrayList<Message>());
                            }
                        } catch (JSONException e) {
                            callback.onSuccess(new ArrayList<Message>());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/gNotification()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        }
                        else {
                            Log.e("Service/gNotification()", error.toString());
                            callback.onFailure(error.toString());
                        }
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }


        };

        mRequestQueue.add(stringRequest);
    }

    @Override
    public void getImageBitmap(String url, final AsyncCallback<Bitmap> callback) {
        ImageLoader imageLoader = LaoSchoolSingleton.getInstance().getImageLoader();

        // If you are using normal ImageView
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                callback.onSuccess(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == 409) {
                    // HTTP Status Code: 409 Unauthorized Oo
                    Log.e("Service/getImage()", "error status code " + networkResponse.statusCode);
                    callback.onAuthFail(error.toString());
                }
                else {
                    Log.e("Service/getImage()", error.toString());
                    callback.onFailure(error.toString());
                }
            }
        });

    }

    @Override
    public void createNotification(final Message message, final AsyncCallback<Message> callback) {
        String url = HOST + "notifies/create";
        Log.d("Service/cNotification()", "message=" + message.toString());
        try {
            HttpEntity entity = null;
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            for (int i = 0; i < message.getNotifyImages().size(); i++) {
                Image image = message.getNotifyImages().get(i);
                entityBuilder.addBinaryBody("file", (resizedFilebyPath(image.getLocal_file_url())));

                if (image.getCaption() != null) {
                    byte ptext[] = image.getCaption().getBytes();
                    String caption_utf8 = new String(ptext, "UTF-8");
                    entityBuilder.addPart("caption", new StringBody(caption_utf8, ContentType.TEXT_PLAIN));
                } else {
                    entityBuilder.addPart("caption", new StringBody("No caption", ContentType.TEXT_PLAIN));
                }

                entityBuilder.addPart("order", new StringBody(String.valueOf(i + 1), ContentType.TEXT_PLAIN));
            }
            String content = createNotificationJson(message);

            entityBuilder.addPart("json_in_string", new StringBody(content, ContentType.TEXT_PLAIN));
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entity = entityBuilder.build();

            final HttpEntity finalEntity = entity;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Service/cNotification()", response);
                            //Message m = Message.messageParsefromJson(response);
                            callback.onSuccess(new Message());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.statusCode == 409) {
                                // HTTP Status Code: 409 Unauthorized Oo
                                Log.e("Service/cNotification()", "error status code " + networkResponse.statusCode);
                                callback.onAuthFail(error.toString());
                            }
                            else {
                                Log.e("Service/cNotification()", error.toString());
                                callback.onFailure(error.toString());
                            }
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", api_key);
                    params.put("auth_key", getAuthKey());
                    return params;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    if (finalEntity != null) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        try {
                            finalEntity.writeTo(bos);
                        } catch (IOException e) {
                            VolleyLog.e("IOException writing to ByteArrayOutputStream");
                        }
                        return bos.toByteArray();
                    }
                    return null;
                }

                @Override
                public String getBodyContentType() {
                    return finalEntity.getContentType().getValue();
                }
            };
            mRequestQueue.add(stringRequest);
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void updateNotification(Message message, final AsyncCallback<Message> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "notifies/update/" + message.getId() + "?is_read=1";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/uNotification()", response);
                        Message m = Message.messageParsefromJson(response);
                        callback.onSuccess(m);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/uNotification()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        }
                        else {
                            Log.e("Service/uNotification()", error.toString());
                            callback.onFailure(error.toString());
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
    }

    public String createNotificationJson(Message message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("school_id", message.getSchool_id());
        jsonObject.addProperty("class_id", message.getClass_id());
        jsonObject.addProperty("title", String.valueOf(message.getTitle()));
        jsonObject.addProperty("content", String.valueOf(message.getContent()));
        jsonObject.addProperty("imp_flg", message.getImp_flg());
        jsonObject.addProperty("dest_type", 1);
        Gson gson = new Gson();
        String jsonString = gson.toJson(jsonObject);

        return jsonString;
    }

    private File resizedFilebyPath(String picturePath) {
        Bitmap bMap = BitmapFactory.decodeFile(picturePath);
        final int maxSize = 960;
        int outWidth;
        int outHeight;
        int inWidth = bMap.getWidth();
        int inHeight = bMap.getHeight();
        if (inWidth > inHeight) {
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }
        Bitmap out = Bitmap.createScaledBitmap(bMap, outWidth, outHeight, false);
        File resizedFile = new File(picturePath);
        OutputStream fOut = null;
        try {
            fOut = new BufferedOutputStream(new FileOutputStream(resizedFile));
            out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            bMap.recycle();
            out.recycle();

        } catch (Exception e) { // TODO
            e.printStackTrace();
        }
        return resizedFile;
    }

    @Override
    public void updateMessageIsRead(Message message, final AsyncCallback<Message> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "messages/update/" + message.getId() + "?is_read=1";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/updateMessage()", response);
                        Message m = Message.messageParsefromJson(response);
                        callback.onSuccess(m);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Service/updateMessage()", error.toString());
                        callback.onFailure(error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
    }

    @Override
    public void updateMessageIsFlag(Message message, final AsyncCallback<Message> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "messages/update/" + message.getId() + "?imp_flg="+message.getImp_flg();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/updateMessage()", response);
                        Message m = Message.messageParsefromJson(response);
                        callback.onSuccess(m);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Service/updateMessage()", error.toString());
                        callback.onFailure(error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
    }
}
