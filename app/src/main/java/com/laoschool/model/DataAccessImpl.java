package com.laoschool.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.entities.Attendance;
import com.laoschool.entities.MessageSample;
import com.laoschool.entities.AttendanceRollup;
import com.laoschool.entities.Class;
import com.laoschool.entities.ExamResult;
import com.laoschool.entities.ExamType;
import com.laoschool.entities.FinalResult;
import com.laoschool.entities.Image;
import com.laoschool.entities.ListAttendance;
import com.laoschool.entities.ListMessages;
import com.laoschool.entities.ListUser;
import com.laoschool.entities.Master;
import com.laoschool.entities.Message;
import com.laoschool.entities.School;
import com.laoschool.entities.SchoolYears;
import com.laoschool.entities.StudentRanking;
import com.laoschool.entities.TimeTable;
import com.laoschool.entities.User;
import com.laoschool.shared.LaoSchoolShared;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tran An on 14/03/2016.
 */
public class DataAccessImpl implements DataAccessInterface {
    private static final String TAG = DataAccessImpl.class.getSimpleName();
    private static DataAccessImpl mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    public static String api_key = "";

//    Lab02
//    final String LOGIN_HOST = "https://192.168.0.202:9443/laoschoolws/";
//    final String HOST = "https://192.168.0.202:9443/laoschoolws/api/";

    //    //VDC
    final String LOGIN_HOST = "https://222.255.29.25:8443/laoschoolws/";
    final String HOST = "https://222.255.29.25:8443/laoschoolws/api/";


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
//            RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();
            String decode_auth_key = LaoSchoolShared.decrypt(auth_key, privateKeyEntry.getPrivateKey());

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
//            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
            String encrypt_auth_key = LaoSchoolShared.encrypt(auth_key, privateKeyEntry.getCertificate().getPublicKey());

            SharedPreferences prefs = mCtx.getSharedPreferences(
                    LaoSchoolShared.SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
//            Log.i("EncryptAuthKey", encrypt_auth_key);
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
                            if (error.networkResponse != null) {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String developerMessage = jsonObject.getString("developerMessage");
                                callback.onFailure(developerMessage);
                            } else
                                callback.onFailure(error.toString());
                        } catch (JSONException e) {
                            callback.onFailure("Server error statusCode = 500 but can not read response body");
                        } catch (UnsupportedEncodingException e) {
                            callback.onFailure("Server error statusCode = 500 but can not read response body");
                        }
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
    public void getUsers(int filter_class_id, String filter_user_role, String filter_sts, int filter_from_id, final AsyncCallback<List<User>> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "users";
        url += _makeUrlgetUsers(filter_class_id, filter_user_role, filter_sts, filter_from_id);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/getUsers()", response);
                        ListUser listUser = ListUser.fromJson(response);
                        if (listUser.getList() != null)
                            callback.onSuccess(listUser.getList());
                        else callback.onSuccess(null);
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
                        } else {
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

    private String _makeUrlgetUsers(int filter_class_id, String filter_user_role, String filter_sts, int filter_from_id) {
        StringBuilder stringBuilder = new StringBuilder();
        if (filter_class_id > -1) {
            stringBuilder.append("?filter_class_id=" + filter_class_id);
            if (filter_user_role.equals(User.USER_ROLE_STUDENT) || filter_user_role.equals(User.USER_ROLE_TEACHER))
                stringBuilder.append("&filter_user_role=" + filter_user_role);
            if (filter_from_id > -1)
                stringBuilder.append("&filter_from_id=" + filter_from_id);

            return stringBuilder.toString();
        } else
            return "";


//        int _filter_class_id = 0, _filter_user_role = 0;
//
//        if (filter_class_id > -1) {
//            stringBuilder.append("?filter_class_id=" + filter_class_id);
//            _filter_class_id = 1;
//        }
//        if (filter_class_id > -1 && _filter_class_id == 0) {
//            stringBuilder.append("?filter_user_role=" + filter_user_role);
//            _filter_user_role = 1;
//        } else {
//            stringBuilder.append("&filter_user_role=" + filter_user_role);
//        }
//        if (filter_user_role > -1 && _filter_user_role == 0) {
//            stringBuilder.append("?filter_sts=" + filter_sts);
//        } else {
//            stringBuilder.append("&filter_sts=" + filter_sts);
//        }
//        return stringBuilder.toString();
    }

    @Override
    public void getUserProfile(final AsyncCallback<User> callback) {
        // Request a string response from the provided URL.
        final String url = HOST + "users/myprofile";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/gUserProfile()", response);
                        if (response != null) {
                            try {
                                User user = User.parseFromJson(response);

                                LaoSchoolShared.selectedClass = user.getEclass();
                                callback.onSuccess(user);
                            } catch (Exception e) {
                                callback.onAuthFail("");
                                e.printStackTrace();
                            }
                        } else {
                            callback.onAuthFail("");
                        }
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
                        } else if (networkResponse != null) {
                            Log.e("Service/gUserProfile()", new String(networkResponse.data));
                            callback.onFailure(new String(networkResponse.data));
                        } else {
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
                        } else {
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
    public void userChangePassword(final String username, final String oldpass, final String newpass, final AsyncCallback<String> callback) {
        final String httpPostBody = "{\"username\":\"" + username + "\","
                + "\"old_pass\":\"" + oldpass + "\","
                + "\"new_pass\":\"" + newpass + "\"}";
        Log.i("Service/changePass()", httpPostBody);
        // Request a string response from the provided URL.
        String url = HOST + "users/change_pass";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/changePass()", response);
                        callback.onSuccess(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/changePass()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        } else if (networkResponse != null && networkResponse.statusCode == 500) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String developerMessage = jsonObject.getString("developerMessage");
                                Log.e("Service/changePass()", "error status code " + networkResponse.statusCode + ", " + developerMessage);
                                callback.onFailure(developerMessage);
                            } catch (JSONException e) {
                                Log.e("Service/changePass()", "Server error statusCode = 500 but can not read response body");
                                callback.onFailure(error.toString());
                            } catch (UnsupportedEncodingException e) {
                                Log.e("Service/changePass()", "Server error statusCode = 500 but can not read response body");
                                callback.onFailure(error.toString());
                            }
                        } else {
                            Log.e("Service/changePass()", error.toString());
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

        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions.
        //Volley does retry for you if you have specified the policy.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void getMyAttendances(String filter_class_id, String filter_user_id, final AsyncCallback<List<Attendance>> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "attendances/myprofile";
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
                        } else {
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
    public void createAttendance(Attendance attendance, final AsyncCallback<Attendance> callback) {
        final String httpPostBody = attendance.toJson();
        // Request a string response from the provided URL.
        String url = HOST + "attendances/create";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/creAttendance()", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String attendanceJson = jsonObject.getString("messageObject");
                            Attendance attendance1 = Attendance.fromJson(attendanceJson);
                            callback.onSuccess(attendance1);
                        } catch (JSONException e) {
                            Log.i("Service/creAttendance()", "Unable to parse attendance object");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/creAttendance()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        } else if (networkResponse != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String developerMessage = jsonObject.getString("developerMessage");
                                callback.onFailure(developerMessage);
                            } catch (JSONException e) {
                                callback.onFailure("Server error statusCode = 500 but can not read response body");
                            } catch (UnsupportedEncodingException e) {
                                callback.onFailure("Server error statusCode = 500 but can not read response body");
                            }
                            Log.e("Service/creAttendance()", new String(error.networkResponse.data));
                        } else {
                            Log.e("Service/creAttendance()", error.toString());
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

    @Override
    public void requestAttendance(Attendance attendance, String fromDt, String toDt, final AsyncCallback<String> callback) {
        final String httpPostBody = attendance.toJson();
        // Request a string response from the provided URL.
        String url = HOST + "attendances/request?from_dt=" + fromDt + "&to_dt=" + toDt;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/reqAttendance()", response);
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/reqAttendance()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        } else if (networkResponse != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String developerMessage = jsonObject.getString("developerMessage");
                                callback.onFailure(developerMessage);
                            } catch (JSONException e) {
                                callback.onFailure("Server error statusCode = 500 but can not read response body");
                            } catch (UnsupportedEncodingException e) {
                                callback.onFailure("Server error statusCode = 500 but can not read response body");
                            }
                            Log.e("Service/reqAttendance()", new String(error.networkResponse.data));
                        } else {
                            Log.e("Service/reqAttendance()", error.toString());
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

    @Override
    public void updateAttendance(Attendance attendance, AsyncCallback<Attendance> callback) {

    }

    @Override
    public void rollupAttendance(int filter_class_id, String filter_date, final AsyncCallback<AttendanceRollup> callback) {
        if (filter_class_id >= 0 && !filter_date.isEmpty()) {
            // Request a string response from the provided URL.
            String url = HOST + "attendances/rollup?filter_class_id=" + filter_class_id + "&filter_date=" + filter_date;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url.trim(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Service/rollupAtten()", response);
                            try {
                                JSONObject mainObject = new JSONObject(response);
                                JSONObject messageObject = mainObject.getJSONObject("messageObject");
                                AttendanceRollup attendanceRollup = AttendanceRollup.fromJson(messageObject.toString());
                                callback.onSuccess(attendanceRollup);
                            } catch (JSONException e) {
                                callback.onFailure("Can not parse json object data");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.statusCode == 409) {
                                // HTTP Status Code: 409 Unauthorized Oo
                                Log.e("Service/rollupAtten()", "error status code " + networkResponse.statusCode);
                                callback.onAuthFail(error.toString());
                            } else {
                                Log.e("Service/rollupAtten()", error.toString());
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
        } else {
            Log.d("Service/rollupAtten()", "filter_class_id or filter_date is empty.");
        }
    }

    @Override
    public void deleteAttendance(final String attendanceId, final AsyncCallback<String> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "attendances/delete/" + attendanceId;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/deleteAtten()", response);
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/deleteAtten()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        } else if (networkResponse != null) {
                            Log.e("Service/deleteAtten()", new String(networkResponse.data));
                        } else {
                            Log.e("Service/deleteAtten()", error.toString());
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
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("attendance_id", String.valueOf(attendanceId));
//                return params;
//            };
        };

        mRequestQueue.add(stringRequest);
    }

    @Override
    public void getAttendanceReason(final AsyncCallback<List<MessageSample>> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "sys/sys_late_reason";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/getAttReason()", response);
                        try {
                            JSONObject mainObject = new JSONObject(response);
                            JSONObject obj1 = mainObject.getJSONObject("messageObject");
                            JSONArray listAttReason = obj1.getJSONArray("list");
                            List<MessageSample> datas = new ArrayList<MessageSample>();
                            for (int i = 0; i < listAttReason.length(); i++) {
                                MessageSample messageSample =
                                        MessageSample.fromJson(listAttReason.getJSONObject(i).toString());
                                datas.add(messageSample);
                            }
                            callback.onSuccess(datas);
                        } catch (JSONException e) {
                            Log.d("Service/getAttReason()", "Can not parse json object data");
                            callback.onFailure(e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/getAttReason()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        } else if (networkResponse != null) {
                            Log.e("Service/getAttReason()", new String(networkResponse.data));
                        } else {
                            Log.e("Service/getAttReason()", error.toString());
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
    public void getAttMss(final AsyncCallback<List<MessageSample>> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "sys/sys_att_msg";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/getAttMss()", response);
                        try {
                            JSONObject mainObject = new JSONObject(response);
                            JSONObject obj1 = mainObject.getJSONObject("messageObject");
                            JSONArray listAttReason = obj1.getJSONArray("list");
                            List<MessageSample> datas = new ArrayList<MessageSample>();
                            for (int i = 0; i < listAttReason.length(); i++) {
                                MessageSample messageSample =
                                        MessageSample.fromJson(listAttReason.getJSONObject(i).toString());
                                datas.add(messageSample);
                            }
                            callback.onSuccess(datas);
                        } catch (JSONException e) {
                            Log.d("Service/getAttMss()", "Can not parse json object data");
                            callback.onFailure(e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/getAttMss()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        } else if (networkResponse != null) {
                            Log.e("Service/getAttMss()", new String(networkResponse.data));
                        } else {
                            Log.e("Service/getAttMss()", error.toString());
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
    public void getStdMss(final AsyncCallback<List<MessageSample>> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "sys/sys_std_msg";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/getStdMss()", response);
                        try {
                            JSONObject mainObject = new JSONObject(response);
                            JSONObject obj1 = mainObject.getJSONObject("messageObject");
                            JSONArray listAttReason = obj1.getJSONArray("list");
                            List<MessageSample> datas = new ArrayList<MessageSample>();
                            for (int i = 0; i < listAttReason.length(); i++) {
                                MessageSample messageSample =
                                        MessageSample.fromJson(listAttReason.getJSONObject(i).toString());
                                datas.add(messageSample);
                            }
                            callback.onSuccess(datas);
                        } catch (JSONException e) {
                            Log.d("Service/getStdMss()", "Can not parse json object data");
                            callback.onFailure(e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/getStdMss()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        } else if (networkResponse != null) {
                            Log.e("Service/getStdMss()", new String(networkResponse.data));
                        } else {
                            Log.e("Service/getStdMss()", error.toString());
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

    public void getExamResults(int filter_class_id, int filter_user_id, int filter_subject_id, final AsyncCallback<List<ExamResult>> callback) {
        String url = HOST + "exam_results";
        StringBuilder stringBuilder = new StringBuilder();
        int check = 0;
        if (filter_class_id > -1) {
            stringBuilder.append("?filter_class_id=" + filter_class_id);
            check = 1;
        }
        if (filter_user_id > -1) {
            if (check == 0) {
                stringBuilder.append("?filter_user_id=" + filter_user_id);
            } else if (check == 1) {
                stringBuilder.append("&filter_user_id=" + filter_user_id);
                check = 1;
            }
        }
        if (filter_subject_id > -1) {
            if (check == 0) {
                stringBuilder.append("?filter_subject_id=" + filter_subject_id);
            } else if (check == 1) {
                stringBuilder.append("&filter_subject_id=" + filter_subject_id);
            }
        }
        url += stringBuilder.toString();
        Log.d("S/getExamResults()", "url:" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("S/getExamResults()", response);
                        try {
                            JSONObject mainObject = new JSONObject(response);
                            JSONArray jsonArray = mainObject.getJSONArray("list");
                            if (jsonArray != null) {
                                List<ExamResult> resultExams = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objectExam = jsonArray.getJSONObject(i);
                                    resultExams.add(ExamResult.fromJson(objectExam.toString()));
                                }
                                callback.onSuccess(resultExams);
                            } else {

                                callback.onSuccess(new ArrayList<ExamResult>());
                            }
                        } catch (JSONException e) {
                            callback.onSuccess(new ArrayList<ExamResult>());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("S/getExamResults()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        } else {
                            Log.e("S/getExamResults()", error.toString());
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
    public void getMyExamResults(int filter_class_id, final AsyncCallback<List<ExamResult>> callback) {
        String url = HOST + "exam_results/myprofile?filter_class_id=" + filter_class_id;
        Log.d("Service", TAG + ".getMyExamResults() -url:" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject mainObject = new JSONObject(response);
                            JSONArray jsonArray = mainObject.getJSONArray("messageObject");
                            if (jsonArray != null) {
                                List<ExamResult> resultExams = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objectExam = jsonArray.getJSONObject(i);
                                    resultExams.add(ExamResult.fromJson(objectExam.toString()));
                                }
                                callback.onSuccess(resultExams);
                            } else {
                                callback.onSuccess(new ArrayList<ExamResult>());
                            }
                        } catch (JSONException e) {
                            callback.onSuccess(new ArrayList<ExamResult>());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        Log.e("Service", TAG + ".getMyExamResults() onErrorResponse() - code: " + networkResponse.statusCode + ",messages: " + error.toString());
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            callback.onAuthFail(error.toString());
                        } else {
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
    public void getExamResultById(int exam_id, AsyncCallback<ExamResult> callback) {

    }

    @Override
    public void getFinalResults(int filter_class_id, int filter_user_id, AsyncCallback<List<FinalResult>> callback) {
        // https://192.168.0.202:9443/laoschoolws/api/final_results/myprofile?filter_school_year=2015
    }

    @Override
    public void getFinalResultById(int final_id, AsyncCallback<FinalResult> callback) {

    }

    @Override
    public void getTimeTables(int filter_class_id, final AsyncCallback<List<TimeTable>> callback) {
//        {
//            "id": 1,
//                "school_id": 1,
//                "class_id": 1,
//                "teacher_id": 1,
//                "subject_id": 1,
//                "session_id": 1,
//                "weekday_id": 1,
//                "description": "test time table",
//                "term_id": 1,
//                "term": "Toan",
//                "subject": "Toan",
//                "session": "Tiet 1",
//                "weekday": "Thu 2"
//        }
        String url = HOST + "/timetables?filter_class_id=" + filter_class_id;
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response != null) {
                        int total_count = response.getInt("total_count");
                        List<TimeTable> timeTables = new ArrayList<>();
                        if (total_count > 0) {
                            JSONArray listTimeTable = response.getJSONArray("list");
                            for (int i = 0; i < listTimeTable.length(); i++) {
                                JSONObject json_timeTable = listTimeTable.getJSONObject(i);
                                timeTables.add(TimeTable.fromJson(json_timeTable.toString()));
                            }
                            callback.onSuccess(timeTables);
                        } else {
                            callback.onSuccess(timeTables);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == 409) {
                    // HTTP Status Code: 409 Unauthorized Oo
                    Log.e("Service/getMessages()", "error status code " + networkResponse.statusCode);
                    callback.onAuthFail(error.toString());
                } else {
                    Log.e("Service/getMessages()", error.toString());
                    callback.onFailure(error.toString());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }
        };
        mRequestQueue.add(jsonArrayRequest);
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
                        } else {
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
        final String httpPostBody = makeMessagetoJson(message);

//      Log.d("Service/createMessage()", "makeMessagetoJson():" + httpPostBody);

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
                        } else if (networkResponse != null) {
                            Log.e("Service/createMessage()", new String(networkResponse.data));
                            callback.onFailure(new String(networkResponse.data));
                        } else {
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
        String content = message.getContent();
        content = content.replaceAll("\n","ahdjhf9283298");
        content = StringEscapeUtils.escapeJava(content);
//        Log.d("Service/messageContent1", content);
        content = content.replaceAll("ahdjhf9283298", "\n");
//        Log.d("Service/messageContent2", content);
        jsonObject.addProperty("content", content);

        jsonObject.addProperty("from_user_id", message.getFrom_usr_id());
        jsonObject.addProperty("to_user_id", message.getTo_usr_id());
        if (message.getCc_list() != null && !message.getCc_list().isEmpty())
            jsonObject.addProperty("cc_list", message.getCc_list());
        Gson gson = new Gson();
        String jsonString = gson.toJson(jsonObject);
        Log.d("makeMessagetoJson()", "JSON:" + jsonString);
        return jsonString;
    }


//    private String makeExamResultsJson(ExamResult examResult) {
//
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("school_id", examResult.getSchool_id());
//        jsonObject.addProperty("class_id", examResult.getClass_id());
//        jsonObject.addProperty("sresult", examResult.getSresult());
//
//        jsonObject.addProperty("student_id", examResult.getStudent_id());
//        jsonObject.addProperty("subject_id", examResult.getSubject_id());
//
//        jsonObject.addProperty("exam_id", examResult.getExam_id());
//        jsonObject.addProperty("teacher_id", examResult.getTeacher_id());
//        jsonObject.addProperty("term_id", examResult.getTerm_id());
//
//        Gson gson = new Gson();
//        String jsonString = gson.toJson(jsonObject);
//        Log.d(TAG, "makeExamResultsJson():" + jsonString);
//        return jsonString;
//    }

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
                        } else {
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
        String url = HOST + "notifies?filter_to_user_id" + LaoSchoolShared.myProfile.getId();
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
                        } else {
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
        String url = HOST + "notifies?filter_to_user_id=" + LaoSchoolShared.myProfile.getId() + "&filter_from_id=" + filter_from_id;
        Log.d(TAG, "url:" + url);
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
                        } else {
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
        if (url != null) {
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
                    } else {
                        Log.e("Service/getImage()", error.toString());
                        callback.onFailure(error.toString());
                    }
                }
            });
        } else {
            Log.e("Service/getImage()", "image url is empty");
            callback.onFailure("image url is empty");
        }
    }

    @Override
    public void createNotification(final Message message, final AsyncCallback<Message> callback) {
        String url = HOST + "notifies/create";
//        Log.d("Service/cNotification()", "message=" + message.toString());
        try {
            HttpEntity entity = null;
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
            for (int i = 0; i < message.getNotifyImages().size(); i++) {
                Image image = message.getNotifyImages().get(i);
                entityBuilder.addBinaryBody("file", (resizedFilebyPath(image.getLocal_file_url())));

                if (image.getCaption() != null) {
                    // byte ptext[] = image.getCaption().getBytes();
                    // String caption_utf8 = new String(ptext, Charset.forName("UTF-8"));
                    // entityBuilder.addPart("caption", new StringBody(caption_utf8, ContentType.TEXT_PLAIN));
                    entityBuilder.addTextBody("caption", StringEscapeUtils.escapeJava(image.getCaption()), contentType);
                } else {
                    // entityBuilder.addPart("caption", new StringBody("No caption", ContentType.TEXT_PLAIN));
                    entityBuilder.addTextBody("caption", "No caption", contentType);
                }

                entityBuilder.addPart("order", new StringBody(String.valueOf(i + 1), ContentType.TEXT_PLAIN));
            }
            String content = createNotificationJson(message);
            //String content_utf8 = new String(content.getBytes(Charset.forName("ISO-8859-1")), Charset.forName("UTF-8"));
            //entityBuilder.addPart("json_in_string", new StringBody(content, ContentType.APPLICATION_JSON));

            entityBuilder.addTextBody("json_in_string", content, contentType);
            //Charset chars = Charset.forName("UTF-8");
            //entityBuilder.setCharset(chars);

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
                            } else {
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
                        } else {
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

        String title = message.getTitle();
        title = title.replaceAll("\n","ahdjhf9283298");
        title = StringEscapeUtils.escapeJava(title);
        title = title.replaceAll("ahdjhf9283298", "\n");
        jsonObject.addProperty("title", title);
        //jsonObject.addProperty("title", String.valueOf(StringEscapeUtils.escapeJava(message.getTitle())));

        String content = message.getContent();
        content = content.replaceAll("\n","ahdjhf9283298");
        content = StringEscapeUtils.escapeJava(content);
        content = content.replaceAll("ahdjhf9283298", "\n");
        jsonObject.addProperty("content", content);
        //jsonObject.addProperty("content", String.valueOf(StringEscapeUtils.escapeJava(message.getContent())));

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
        String url = HOST + "messages/update/" + message.getId() + "?imp_flg=" + message.getImp_flg();
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
    public void getMyFinalResults(int filter_school_year, final AsyncCallback<List<FinalResult>> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "final_results/myprofile?filter_school_year=" + filter_school_year;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service", "getMyFinalResults().onResponse() -response:" + response);
                        callback.onSuccess(new ArrayList<FinalResult>());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        Log.e("Service", TAG + ".getMyFinalResults().onErrorResponse() - code: " + networkResponse.statusCode + ",messages: " + error.toString());
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            callback.onAuthFail(error.toString());
                        } else {
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
    public void getMasterTablebyName(String tableName, final AsyncCallback<List<Master>> callback) {
        String url = HOST + "masters/" + tableName;
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response != null) {
                        int total_count = response.getInt("total_count");
                        List<Master> masters = new ArrayList<>();
                        if (total_count > 0) {
                            JSONArray listMaster = response.getJSONArray("list");
                            for (int i = 0; i < listMaster.length(); i++) {
                                JSONObject objMaster = listMaster.getJSONObject(i);
                                masters.add(Master.fromJson(objMaster.toString()));
                            }
                            callback.onSuccess(masters);
                        } else {
                            callback.onSuccess(masters);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == 409) {
                    // HTTP Status Code: 409 Unauthorized Oo
                    Log.e("Service", "getMasterTablebyName() -error status code " + networkResponse.statusCode);
                    callback.onAuthFail(error.toString());
                } else {
                    Log.e("Service", "getMasterTablebyName() " + error.toString());
                    callback.onFailure(error.toString());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }
        };
        mRequestQueue.add(jsonArrayRequest);
    }

    @Override
    public void inputExamResults(final ExamResult examResult, final AsyncCallback<ExamResult> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "exam_results/input";
        Log.d(TAG, "inputExamResults() -json input:" + examResult.toCreateJsonString());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ExamResult result = ExamResult.fromJson(response);
                        callback.onSuccess(result);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        Log.e("Service", "inputExamResults().onErrorResponse() -error status code " + networkResponse.statusCode + ", message:" + error.toString());
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            callback.onAuthFail(error.toString());
                        } else {
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
                return examResult.toCreateJsonString().getBytes();
            }
        };

        mRequestQueue.add(stringRequest);
    }


    @Override
    public void getListSubjectbyClassId(int classId, final AsyncCallback<List<Master>> callback) {
        String url = HOST + "timetables/subjects?filter_class_id=" + classId;
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response != null) {
                        List<Master> masters = new ArrayList<>();
                        JSONArray listMaster = response.getJSONArray("messageObject");
                        for (int i = 0; i < listMaster.length(); i++) {
                            JSONObject objMaster = listMaster.getJSONObject(i);
                            masters.add(Master.fromJson(objMaster.toString()));
                        }
                        callback.onSuccess(masters);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == 409) {
                    // HTTP Status Code: 409 Unauthorized Oo
                    Log.e("Service", "getMasterTablebyName() -error status code " + networkResponse.statusCode);
                    callback.onAuthFail(error.toString());
                } else {
                    Log.e("Service", "getMasterTablebyName() " + error.toString());
                    callback.onFailure(error.toString());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }
        };
        mRequestQueue.add(jsonArrayRequest);
    }

    @Override
    public void getExamResultsforMark(int filter_class_id, int filter_user_id, int filter_subject_id, final AsyncCallback<List<ExamResult>> callback) {
        String url = HOST + "exam_results";
        StringBuilder stringBuilder = new StringBuilder();
        int check = 0;
        if (filter_class_id > -1) {
            stringBuilder.append("?filter_class_id=" + filter_class_id);
            check = 1;
        }
        if (filter_user_id > -1) {
            if (check == 0) {
                stringBuilder.append("?filter_user_id=" + filter_user_id);
            } else if (check == 1) {
                stringBuilder.append("&filter_user_id=" + filter_user_id);
                check = 1;
            }
        }
        if (filter_subject_id > -1) {
            if (check == 0) {
                stringBuilder.append("?filter_subject_id=" + filter_subject_id);
            } else if (check == 1) {
                stringBuilder.append("&filter_subject_id=" + filter_subject_id);
            }
        }
        url += stringBuilder.toString();
        Log.d("S", "getExamResultsforMark()" + "-url:" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("S", "getExamResultsforMark() " + response);
                        try {
                            JSONObject mainObject = new JSONObject(response);
                            JSONArray jsonArray = mainObject.getJSONArray("messageObject");
                            if (jsonArray != null) {
                                List<ExamResult> resultExams = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objectExam = jsonArray.getJSONObject(i);
                                    resultExams.add(ExamResult.fromJson(objectExam.toString()));
                                }
                                callback.onSuccess(resultExams);
                            } else {
                                callback.onSuccess(new ArrayList<ExamResult>());
                            }
                        } catch (JSONException e) {
                            callback.onSuccess(new ArrayList<ExamResult>());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("S", "getExamResultsforMark() " + " error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        } else {
                            Log.e("S", "getExamResultsforMark() " + error.toString());
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
    public void getExamType(int filter_class_id, final AsyncCallback<List<ExamType>> callback) {
        String url = HOST + "schools/exams";
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response != null) {
                        List<ExamType> masters = new ArrayList<>();
                        JSONArray listMaster = response.getJSONArray("messageObject");
                        if (listMaster != null) {
                            int currentTerm = LaoSchoolShared.myProfile.getEclass().getTerm();
                            for (int i = 0; i < listMaster.length(); i++) {
                                JSONObject objMaster = listMaster.getJSONObject(i);
                                ExamType examType = ExamType.fromJson(objMaster.toString());
                                if (examType.getTerm_val() == currentTerm) {
                                    masters.add(examType);
                                }
                            }
                        }
                        callback.onSuccess(masters);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e("Service", "getExamType() -error status code " + networkResponse.statusCode + ",message:" + error.toString());
                if (networkResponse != null && networkResponse.statusCode == 409) {
                    // HTTP Status Code: 409 Unauthorized Oo
                    callback.onAuthFail(error.toString());
                } else {
                    callback.onFailure(error.toString());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }
        };
        mRequestQueue.add(jsonArrayRequest);
    }


    @Override
    public void getMyFinalResultsByYear(int filter_year_id, final AsyncCallback<FinalResult> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "edu_profiles/myprofile?filter_year_id=" + filter_year_id;
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response != null) {
                        JSONArray listMaster = response.getJSONArray("messageObject");
                        if (listMaster != null) {
                            JSONObject finalObj = listMaster.getJSONObject(0);
                            if (finalObj != null)
                                callback.onSuccess(FinalResult.fromJson(finalObj));
                            else {
                                Log.d("Service", "getMyFinalResultsByYear() -obj null");
                                callback.onSuccess(null);
                            }
                        } else {
                            callback.onSuccess(null);
                        }

                    } else {
                        callback.onSuccess(null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onSuccess(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e("Service", "getMyFinalResultsByYear() -error status code " + networkResponse.statusCode + ",message:" + error.toString());
                    if (networkResponse != null && networkResponse.statusCode == 409) {
                        // HTTP Status Code: 409 Unauthorized Oo
                        callback.onAuthFail(error.toString());
                    } else {
                        callback.onFailure(error.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure(e.toString());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }
        };
        mRequestQueue.add(jsonArrayRequest);
    }

    @Override
    public void getMySchoolYears(final AsyncCallback<List<SchoolYears>> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "schools/years";
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response != null) {
                        JSONArray listSchoolYears = response.getJSONArray("messageObject");
                        List<SchoolYears> yearsList = new ArrayList<>();
                        if (listSchoolYears != null) {
                            if (listSchoolYears.length() > 0) {
                                yearsList = SchoolYears.fromArray(listSchoolYears.toString());
                            }
                            callback.onSuccess(yearsList);
                        } else {
                            callback.onSuccess(yearsList);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onSuccess(new ArrayList<SchoolYears>());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e("Service", "getMySchoolYears() -error status code " + networkResponse.statusCode + ",message:" + error.toString());
                if (networkResponse != null && networkResponse.statusCode == 409) {
                    // HTTP Status Code: 409 Unauthorized Oo
                    callback.onAuthFail(error.toString());
                } else {
                    callback.onFailure(error.toString());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }
        };
        mRequestQueue.add(jsonArrayRequest);
    }

    @Override
    public void inputBatchExamResults(final List<ExamResult> examResults, final AsyncCallback<String> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "exam_results/input/batch";
        final String jsonData = createBatchExamResults(examResults);
        Log.d("Service", "inputBatchExamResults() -array:" + jsonData.toString());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        Log.e("Service", "inputBatchExamResults().onErrorResponse() -error status code " + networkResponse.statusCode + ", message:" + error.toString());
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            callback.onAuthFail(error.toString());
                        } else {
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
                return jsonData.getBytes();
            }
        };

        mRequestQueue.add(stringRequest);
    }

    private String createBatchExamResults(List<ExamResult> examResults) {
        JSONArray jsonArray = new JSONArray();
        for (ExamResult result : examResults) {
            jsonArray.put(result.toCreateJson());
        }

        return jsonArray.toString();
    }

    @Override
    public void getMyRanking(final AsyncCallback<StudentRanking> callback) {
        String url = HOST + "exam_results/ranks?filter_class_id=" + LaoSchoolShared.myProfile.getEclass().getId() + "&filter_year_id=" + LaoSchoolShared.myProfile.getEclass().getYear_id() + "&filter_student_id=" + LaoSchoolShared.myProfile.getId();
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response != null) {
                        JSONArray rankingStudentArray = response.getJSONArray("messageObject");
                        StudentRanking studentRanking = new StudentRanking();
                        if (rankingStudentArray != null) {
                            if (rankingStudentArray.length() > 0) {
                                //  yearsList = SchoolYears.fromArray(rankingStudentArray.toString());
                                studentRanking = StudentRanking.toStudentRanking(rankingStudentArray.getJSONObject(0));
                            }
                            callback.onSuccess(studentRanking);
                        } else {
                            callback.onSuccess(studentRanking);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onSuccess(new StudentRanking());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e("Service", "getMyRanking() -error status code " + networkResponse.statusCode + ",message:" + error.toString());
                if (networkResponse != null && networkResponse.statusCode == 409) {
                    // HTTP Status Code: 409 Unauthorized Oo
                    callback.onAuthFail(error.toString());
                } else {
                    callback.onFailure(error.toString());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", getAuthKey());
                return params;
            }
        };
        mRequestQueue.add(jsonArrayRequest);
    }

    @Override
    public void saveToken(final String token, final AsyncCallback<String> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "tokens/save";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/saveToken()", response);

                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            // HTTP Status Code: 409 Unauthorized Oo
                            Log.e("Service/saveToken()", "error status code " + networkResponse.statusCode);
                            callback.onAuthFail(error.toString());
                        } else if (networkResponse != null) {
                            Log.e("Service/saveToken()", new String(networkResponse.data));
                            callback.onFailure(new String(networkResponse.data));
                        } else {
                            Log.e("Service/saveToken()", error.toString());
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
                params.put("token", token);
                return params;
            }
        };

        mRequestQueue.add(stringRequest);

    }

    @Override
    public void createMessageOnlyStudent(Message message, final AsyncCallback<Message> callback) {
        // Request a string response from the provided URL.
        int classId = LaoSchoolShared.myProfile.getEclass().getId();
        String url = HOST + "messages/create_ext?filter_class_list=" + classId + "&filter_roles=STUDENT";
        final String httpPostBody = makeMessagetoJson(message);

//      Log.d("Service/createMessage()", "makeMessagetoJson():" + httpPostBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("S/createMessage()", response);
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
                        } else if (networkResponse != null) {
                            Log.e("Service/createMessage()", new String(networkResponse.data));
                            callback.onFailure(new String(networkResponse.data));
                        } else {
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
}
