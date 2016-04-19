package com.laoschool.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.NetworkResponse;
import com.google.gson.JsonObject;
import com.laoschool.entities.*;
import com.laoschool.entities.Class;

import org.json.JSONException;
import org.json.JSONObject;

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
    private static String auth_key = "";

    final String LOGIN_HOST = "https://192.168.0.202:9443/laoschoolws/";
    final String HOST = "https://192.168.0.202:9443/laoschoolws/api/";

    private DataAccessImpl(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        HttpsTrustManager.allowAllSSL();
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public static synchronized DataAccessImpl getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataAccessImpl(context);
        }
        return mInstance;
    }

    public static void setAuthKey(String auth_key) {
        DataAccessImpl.auth_key = auth_key;
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
                auth_key = key;
                callback.onSuccess(key);
                Log.d("Service/login()", "auth_key = " + key);
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Service/forgotPass()", error.toString());
                        callback.onFailure(error.toString());
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

        mRequestQueue.add(stringRequest);
    }

    @Override
    public void getUsers(int filter_class_id, int filter_user_role, int filter_sts, AsyncCallback<List<User>> callback) {

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
                        Log.e("Service/gUserProfile()", error.toString());
                        callback.onFailure(error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", auth_key);
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
                        Log.e("Service/getUserById()", error.toString());
                        callback.onFailure(error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", auth_key);
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
    }

    @Override
    public void getAttendances(AsyncCallback<List<Attendance>> callback) {

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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Service/getMessage()", response);
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
                        Log.e("Service/getMessage()", error.toString());
                        callback.onFailure(error.toString());
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", auth_key);
                return params;
            }


        };
        Log.d("Service/getMessage()", "URL:" + stringRequest.getUrl());
        mRequestQueue.add(stringRequest);

    }

    private String _makeUrlgetMessages(String filter_class_id, String filter_from_user_id, String filter_from_dt, String filter_to_dt, String filter_to_user_id, String filter_channel, String filter_sts, String filter_from_id) {
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Service/createMessage()", response);
                        Message m = Message.parsefromJson(response);
                        callback.onSuccess(m);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Service/createMessage()", error.toString());
                        callback.onFailure(error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", api_key);
                params.put("auth_key", auth_key);
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String httpPostBody = message.toJson();
                return httpPostBody.getBytes();
            }
        };

        mRequestQueue.add(stringRequest);
    }

    @Override
    public void updateMessage(Message message, AsyncCallback<Message> callback) {

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

}
