package com.laoschool.shared;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.Image;
import com.laoschool.entities.Message;
import com.laoschool.entities.User;
import com.laoschool.screen.login.ScreenLogin;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;


/**
 * Created by Hue on 3/1/2016.
 */
public class LaoSchoolShared {

    public static final int SELECT_PHOTO = 100;
    public static final int SELECT_CAMERA = 101;
    public static final long LOADING_TIME = 1500;
    public static User myProfile;
    public static final String SHARED_PREFERENCES_TAG = "com.laoshcool.app";
    public static final String KEY_STORE_ALIAS = "LAOSCHOOL_KEY";

    public static final String CONTAINER_ID = "container_id";
    public static final String CURRENT_ROLE = "current_role";
    public static final int POSITION_SCREEN_MESSAGE_0 = 0;
    public static final int POSITION_SCREEN_ANNOUNCEMENTS_1 = 1;
    public static final int POSITION_SCREEN_EXAM_RESULTS_2 = 2;
    public static final int POSITION_SCREEN_ATTENDED_3 = 3;
    public static final int POSITION_SCREEN_MORE_4 = 4;

    public static final int POSITION_SCREEN_CREATE_MESSAGE_5 = 5;
    public static final int POSITION_SCREEN_SCHEDULE_6 = 6;
    public static final int POSITION_SCREEN_SCHOOL_RECORD_YEAR_7 = 7;
    public static final int POSITION_SCREEN_SCHOOL_INFORMATION_8 = 8;
    public static final int POSITION_SCREEN_LIST_TEACHER_9 = 9;

    public static final int POSITION_SCREEN_LIST_STUDENT_10 = 10;
    public static final int POSITION_SCREEN_MARK_SCORE_STUDENT_11 = 11;
    public static final int POSITION_SCREEN_SETTING_12 = 12;
    public static final int POSITION_SCREEN_PROFILE_13 = 13;
    public static final int POSITION_SCREEN_MESSAGE_DETAILS_14 = 14;
    public static final int POSITION_SCREEN_ANNOUNCEMENT_DETAILS_15 = 15;
    public static final int POSITION_SCREEN_CREATE_ANNOUNCEMENT_16 = 16;
    public static final int POSITION_SCREEN_REQUEST_ATTENDANCE_17 = 17;

    public static final String ROLE_TEARCHER = "TEACHER";
    public static final String ROLE_STUDENT = "STUDENT";
    public static final String ROLE = "role";
    private static final String TAG = LaoSchoolShared.class.getSimpleName();

    public static String makeFragmentTag(int containerViewId, long id) {
        return "android:switcher:" + containerViewId + ":" + id;
    }

    public static View createTabIndicator(LayoutInflater inflater, TabHost tabHost, int textResource, int iconResource) {
        View tabIndicator = inflater.inflate(R.layout.view_tab_indicator, tabHost.getTabWidget(), false);
        ((TextView) tabIndicator.findViewById(R.id.tab_indicator_title)).setText(textResource);
        ((ImageView) tabIndicator.findViewById(R.id.tab_indicator_icon)).setImageResource(iconResource);
        return tabIndicator;
    }

    public static Drawable getDraweble(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getDrawableGreater21(context, id);
        } else {
            return getDrawableUnder20(context, id);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Drawable getDrawableGreater21(Context context, int id) {
        return context.getDrawable(id);
    }

    private static Drawable getDrawableUnder20(Context context, int id) {
        return context.getResources().getDrawable(id);
    }


    public static boolean checkConn(Context ctx) {
        try {
            ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
//            // test for connection
//            if (cm.getActiveNetworkInfo() != null
//                    && cm.getActiveNetworkInfo().isAvailable()
//                    && cm.getActiveNetworkInfo().isConnected()) {
//                return true;
//            } else {
//                Log.d(TAG, "Internet Connection Not Present 1");
//                return false;
//            }
            NetworkInfo i = conMgr.getActiveNetworkInfo();
            if (i == null)
                return false;
            if (!i.isConnected())
                return false;
            if (!i.isAvailable())
                return false;
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Internet Connection Not Present 2");
            return false;
        }
    }


    public static void showErrorOccurred(Context context, String function, Exception e) {
        try {
            String messageError = "an_error_occurred"
                    + "\t" + context.getClass().getName() + " in function " + function + "():" + e.getMessage();
//            final List<String> devices = Arrays.asList(context.getResources().getStringArray(R.array.devices_dev_id));
//            String android_id = Settings.Secure.getString(context.getContentResolver(),
//                    Settings.Secure.ANDROID_ID);
//            if (devices.contains(android_id)) {
            Toast.makeText(context, messageError, Toast.LENGTH_SHORT).show();
            Log.e(TAG, messageError);
            e.printStackTrace();
//            }
        } catch (Exception ex) {
            Log.e(TAG, "showErrorOccurred Erorr:" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void showDialogConnectionInternetFails(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Connection error");

        builder.setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                .getWindowToken(), 0);
    }

    public static String encrypt(String plainText, RSAPublicKey publicKey)
            throws Exception {
        try {
            Cipher input = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            input.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, input);
            cipherOutputStream.write(plainText.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte[] vals = outputStream.toByteArray();
            return (Base64.encodeToString(vals, Base64.DEFAULT));
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return "";
        }
    }

    public static String decrypt(String encryptedText, RSAPrivateKey privateKey)
            throws Exception {
        try {
            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            output.init(Cipher.DECRYPT_MODE, privateKey);

            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(encryptedText, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            String finalText = new String(bytes, 0, bytes.length, "UTF-8");
            return (finalText);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return "";
        }
    }

    public static Message parseMessageFormSql(Cursor cursor) {
        Message message = new Message();

        message.setId(cursor.getInt(Message.MessageColumns.COLUMN_NAME_ID_INDEX_0));

        message.setSchool_id(cursor.getInt(Message.MessageColumns.COLUMN_NAME_SCHOOL_ID_INDEX_1));
        message.setClass_id(cursor.getInt(Message.MessageColumns.COLUMN_NAME_CLASS_ID_INDEX_2));
        message.setFrom_usr_id(cursor.getInt(Message.MessageColumns.COLUMN_NAME_FROM_USR_ID_INDEX_3));
        message.setFrom_user_name(cursor.getString(Message.MessageColumns.COLUMN_NAME_FROM_USER_NAME_INDEX_4));
        message.setTo_usr_id(cursor.getInt(Message.MessageColumns.COLUMN_NAME_TO_USR_ID_INDEX_5));
        message.setTo_user_name(cursor.getString(Message.MessageColumns.COLUMN_NAME_TO_USER_NAME_INDEX_6));
        message.setContent(cursor.getString(Message.MessageColumns.COLUMN_NAME_CONTENT_INDEX_7));
        message.setMsg_type_id(cursor.getInt(Message.MessageColumns.COLUMN_NAME_MSG_TYPE_ID_INDEX_8));
        message.setChannel(cursor.getInt(Message.MessageColumns.COLUMN_NAME_CHANNEL_INDEX_9));
        message.setIs_sent(cursor.getInt(Message.MessageColumns.COLUMN_NAME_IS_SENT_INDEX_10));
        message.setIs_read(cursor.getInt(Message.MessageColumns.COLUMN_NAME_IS_READ_INDEX_12));
        message.setSent_dt(cursor.getString(Message.MessageColumns.COLUMN_NAME_SENT_DT_INDEX_11));

        message.setRead_dt(cursor.getString(Message.MessageColumns.COLUMN_NAME_READ_DT_INDEX_13));

        message.setImp_flg(cursor.getInt(Message.MessageColumns.COLUMN_NAME_IMP_FLG_INDEX_14));
        message.setOther(cursor.getString(Message.MessageColumns.COLUMN_NAME_OTHER_INDEX_16));
        message.setTitle(cursor.getString(Message.MessageColumns.COLUMN_NAME_TITLE_INDEX_15));
        message.setCc_list(cursor.getString(Message.MessageColumns.COLUMN_NAME_CC_LIST_INDEX_17));
        message.setSchoolName(cursor.getString(Message.MessageColumns.COLUMN_NAME_SCHOOL_NAME_INDEX_18));
        message.setMessageType(cursor.getString(Message.MessageColumns.COLUMN_NAME_MESSAGE_TYPE_INDEX_19));
        message.setClienId(cursor.getInt(Message.MessageColumns.COLUMN_NAME_CLIENT_ID_INDEX_20));
        message.setType(cursor.getInt(Message.MessageColumns.COLUMN_NAME_TYPE_INDEX_21));
        message.setTask_id(cursor.getInt(Message.MessageColumns.COLUMN_NAME_TASK_ID_INDEX_22));
        message.setFrm_user_photo(cursor.getString(Message.MessageColumns.COLUMN_NAME_FRM_USER_PHOTO_INDEX_23));
        //message.setFile_url(cursor.getString(Message.MessageColumns.COLUMN_NAME_FILE_URL_INDEX_22));
        //Log.d(TAG,message.toString());
        return message;
    }

    public static String getPath(Context context, Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    public static Image parseImageFormSql(Cursor cursor) {
        Image image = new Image();
        image.setId(cursor.getInt(Image.ImageColumns.COLUMN_NAME_ID_INDEX_0));
        image.setNotify_id(cursor.getInt(Image.ImageColumns.COLUMN_NAME_NOTIFY_ID_INDEX_1));
        image.setUser_id(cursor.getInt(Image.ImageColumns.COLUMN_NAME_USER_ID_INDEX_2));
        image.setUpload_dt(cursor.getString(Image.ImageColumns.COLUMN_NAME_UPLOAD_DT_INDEX_3));
        image.setFile_name(cursor.getString(Image.ImageColumns.COLUMN_NAME_FILE_NAME_INDEX_4));
        image.setFile_path(cursor.getString(Image.ImageColumns.COLUMN_NAME_FILE_PATH_INDEX_5));
        image.setFile_url(cursor.getString(Image.ImageColumns.COLUMN_NAME_FILE_URL_INDEX_6));
        image.setCaption(cursor.getString(Image.ImageColumns.COLUMN_NAME_CAPTION_INDEX_7));
        image.setLocal_file_url(cursor.getString(Image.ImageColumns.COLUMN_NAME_LOCAL_FILE_URL_INDEX_8));
        return image;
    }

    public static void goBackToLoginPage(Context context) {
        if (context == null)
            context = LaoSchoolSingleton.context;
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
        preferences.edit().remove("auth_key").commit();

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(context.getString(R.string.err));
        builder1.setMessage(context.getString(R.string.msg_re_login_application));
        builder1.setCancelable(true);

        final Context finalContext = context;
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(finalContext, ScreenLogin.class);
                        finalContext.startActivity(intent);
                        ((Activity) finalContext).finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static String formatDate(String exam_dt, int type) {
        try {
            DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            DateFormat outputFormatter;
            if (type == 0) {
                outputFormatter = new SimpleDateFormat("dd-MMM");
            } else if (type == 1) {
                outputFormatter = new SimpleDateFormat("dd-MMM-yyyy");
            } else if (type == 2) {
                outputFormatter = new SimpleDateFormat("HH:mm dd-MMM-yyyy");
            } else {
                outputFormatter = new SimpleDateFormat("HH:mm:ss dd-MMM-yyyy");
            }

            Date date;
            if (exam_dt != null) {
                date = inputFormatter.parse(exam_dt);
            } else {
                date = new Date();
            }
            String output1 = outputFormatter.format(date);
            return output1;
        } catch (ParseException e) {
            Log.e(TAG, "formatDate() - parse exception: " + e.getMessage());
        }
        return exam_dt;
    }
}
