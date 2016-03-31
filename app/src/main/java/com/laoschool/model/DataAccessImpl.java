package com.laoschool.model;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.NetworkResponse;
import com.laoschool.entities.*;
import com.laoschool.entities.Class;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tran An on 14/03/2016.
 */
public class DataAccessImpl implements DataAccessInterface{

    private static DataAccessImpl mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private static String api_key = "TEST_API_KEY";
    private static String auth_key;

    final String HOST = "https://192.168.0.202:9443/laoschoolws/api/";

    private DataAccessImpl(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
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

    @Override
    public void login(final String sso_id, final String password, final AsyncCallback<String> callback) {
        // Request a string response from the provided URL.
        String url = HOST + "login";
        HttpsTrustManager.allowAllSSL();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure(error);
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
                    return super.parseNetworkResponse(response);
                }
        };

        mRequestQueue.add(stringRequest);
    }

    @Override
    public void getUsers(int filter_class_id, int filter_user_role, int filter_sts, AsyncCallback<List<User>> callback) {

    }

    @Override
    public void getUserProfile(AsyncCallback<User> callback) {

    }

    @Override
    public void getUserById(int user_id, AsyncCallback<User> callback) {

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
    public void getMessages(int filter_class_id, int filter_from_user_id, String filter_from_dt, String filter_to_dt, int filter_to_user_id, int filter_channel, int filter_sts, AsyncCallback<List<Message>> callback) {

    }

    @Override
    public void getMessageById(int message_id, AsyncCallback<Message> callback) {

    }

    @Override
    public void createMessage(Message message, AsyncCallback<Message> callback) {

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
