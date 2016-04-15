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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

    public ScreenLoginMain() {}

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

        final EditText txbUserName = (EditText) view.findViewById(R.id.txbUserName);
        final EditText txbPassword = (EditText) view.findViewById(R.id.txbPassword);
        final Button buttonLogin = (Button) view.findViewById(R.id.btnLogin);
        final ImageButton btnQuestion = (ImageButton) view.findViewById(R.id.btnQuestion);
        final TextView btnFogetPass = (TextView) view.findViewById(R.id.btnFogetPass);
        final ImageView imgForgotPass = (ImageView) view.findViewById(R.id.imgForgotPass);

        int color = Color.parseColor("#ffffff"); //The color u want
        btnQuestion.setColorFilter(color);
        imgForgotPass.setColorFilter(color);

//        btnFogetPass.setPaintFlags(btnFogetPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        btnFogetPass.setText("Forgot password?");

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userName = txbUserName.getText().toString();
                String password = txbPassword.getText().toString();
                service.login(userName, password, new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        cryptoAuthKey(result);
                        goToHomeScreen();
                    }

                    @Override
                    public void onFailure(String message) {
                        if (message.contains("TimeoutError"))
                            Toast.makeText(thiz.getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(thiz.getActivity(), "Wrong username or password", Toast.LENGTH_SHORT).show();
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
                goToHelpScreen();
            }
        });

        return view;
    }

    private void cryptoAuthKey(String auth_key) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(LaoSchoolShared.KEY_STORE_ALIAS, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
            String encrypt_auth_key = LaoSchoolShared.encrypt(auth_key, publicKey);

            SharedPreferences prefs = thiz.getActivity().getSharedPreferences(
                    LaoSchoolShared.SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
            prefs.edit().putString("auth_key", encrypt_auth_key).apply();

//          System.out.println(encrypt_auth_key);
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
    }

    protected void autoLogin() {
        service.login("tranan", "1234567890", new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                goToHomeScreen();
            }

            @Override
            public void onFailure(String message) {
                goToHomeScreen();
                System.out.println(message);
                if (message.contains("TimeoutError"))
                    Toast.makeText(thiz.getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(thiz.getActivity(), "Wrong username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToForgetPassScreen() {
        container.switchToScreenFogotPassword();
    }

    public void goToHelpScreen() {
        container.switchToScreenLoginHelp();
    }

    public void goToHomeScreen() {
        Intent intent = new Intent(this.getActivity(), HomeActivity.class);
        intent.setAction(LaoSchoolShared.ROLE_STUDENT);
        startActivity(intent);
    }

}
