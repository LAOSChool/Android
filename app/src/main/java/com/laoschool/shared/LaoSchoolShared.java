package com.laoschool.shared;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.entities.User;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;


/**
 * Created by Hue on 3/1/2016.
 */
public class LaoSchoolShared {

    public static User myProfile;
    public static final String SHARED_PREFERENCES_TAG = "com.laoshcool.app";
    public static final String KEY_STORE_ALIAS = "LAOSCHOOL_KEY";

    public static final String CONTAINER_ID = "container_id";
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

    public static final String ROLE_TEARCHER = "TEACHER";
    public static final String ROLE_STUDENT = "STUDENT";
    public static final String ROLE = "role";
    private static final String TAG = "LaoSchoolShared";

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

    public static void showDialogConnectionInternetFails(Context context){
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

            byte [] vals = outputStream.toByteArray();
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
                values.add((byte)nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for(int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            String finalText = new String(bytes, 0, bytes.length, "UTF-8");
            return (finalText);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return "";
        }
    }

}
