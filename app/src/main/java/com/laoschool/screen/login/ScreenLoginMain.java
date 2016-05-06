package com.laoschool.screen.login;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.screen.HomeActivity;
import com.laoschool.shared.LaoSchoolShared;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenLoginMain extends Fragment {

    private DataAccessInterface service;
    private ScreenLoginMain thiz = this;

    ScreenLogin container;

    public ScreenLoginMain() {
    }

    public void setContainer(ScreenLogin container) {
        this.container = container;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        service = DataAccessImpl.getInstance(this.getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.screen_login_conten, container, false);
        LaoSchoolShared.myProfile = null;

        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.container);
        final LinearLayout layoutContent = (LinearLayout) view.findViewById(R.id.layoutContent);
        final TextView logo = (TextView) view.findViewById(R.id.logo);
        final EditText txbUserName = (EditText) view.findViewById(R.id.txbUserName);
        final EditText txbPassword = (EditText) view.findViewById(R.id.txbPassword);
        final Button buttonLogin = (Button) view.findViewById(R.id.btnLogin);
        final ImageButton btnQuestion = (ImageButton) view.findViewById(R.id.btnQuestion);
        final TextView btnFogetPass = (TextView) view.findViewById(R.id.btnFogetPass);
//      final ImageView imgForgotPass = (ImageView) view.findViewById(R.id.imgForgotPass);

        int color = Color.parseColor("#ffffff"); //The color u want
        btnQuestion.setColorFilter(color);
//      imgForgotPass.setColorFilter(color);

//      btnFogetPass.setPaintFlags(btnFogetPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//      btnFogetPass.setText("Forgot password?");
        SharedPreferences prefs = this.getActivity().getSharedPreferences(
                LaoSchoolShared.SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
        String userName = prefs.getString("userName", null);
        if (userName != null)
            txbUserName.setText(userName);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String userName = txbUserName.getText().toString();
                String password = txbPassword.getText().toString();
                service.login(userName, password, new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {

                        goToHomeScreen();
                        //save last username
                        SharedPreferences prefs = thiz.getActivity().getSharedPreferences(
                                LaoSchoolShared.SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
                        prefs.edit().putString("userName", userName).apply();
                    }

                    @Override
                    public void onFailure(String message) {
                        if (thiz.getActivity() != null) {
                            if (message.contains("TimeoutError"))
                                Toast.makeText(thiz.getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(thiz.getActivity(), "Wrong username or password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onAuthFail(String message) {
                        LaoSchoolShared.goBackToLoginPage(thiz.getContext());
                    }
                });
            }
        });

        btnFogetPass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToForgetPassScreen();
            }
        });

        btnQuestion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) thiz.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                goToHelpScreen();
            }
        });

        frameLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
                                       int oldBottom) {
                // its possible that the layout is not complete in which case
                // we will get all zero values for the positions, so ignore the event
                if (left == 0 && top == 0 && right == 0 && bottom == 0) {
                    return;
                }

                // Do what you need to do with the height/width since they are now set
                if (bottom < oldBottom) {
                    layoutContent.setGravity(Gravity.START);
                    layoutContent.setPadding(0, 30, 0, 0);
                    logo.setPadding(0, 0, 0, 0);
                    btnFogetPass.setVisibility(View.INVISIBLE);
                    return;
                } else if (bottom > oldBottom) {
                    layoutContent.setGravity(Gravity.CENTER);
                    layoutContent.setPadding(0, 0, 0, 90);
                    logo.setPadding(0, 0, 0, 70);
                    btnFogetPass.setVisibility(View.VISIBLE);
                    return;
                }
            }
        });

        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    frameLayout.requestFocus();
                    InputMethodManager imm = (InputMethodManager) thiz.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });

        return view;
    }

    public void goToForgetPassScreen() {
        container.switchToScreenFogotPassword();
    }

    public void goToHelpScreen() {
        container.switchToScreenLoginHelp();
    }

    public void goToHomeScreen() {
        Intent intent = new Intent(this.getActivity(), HomeActivity.class);
        startActivity(intent);
        this.getActivity().finish();
    }

}
