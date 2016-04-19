package com.laoschool.screen;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.entities.User;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.screen.login.ScreenLogin;
import com.laoschool.shared.LaoSchoolShared;

import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Calendar;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SplashScreen extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 1000;
    private DataAccessInterface service;
    private SplashScreen thiz;
    private KeyStore keyStore;

    public final String TAG = "SplashScreen";

//    private void unLockCredentialStorage() {
//        try {
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//                startActivity(new Intent("android.credentials.UNLOCK"));
//            } else {
//                startActivity(new Intent("com.android.credentials.UNLOCK"));
//            }
//        } catch (ActivityNotFoundException e) {
//            Log.e(TAG, "No UNLOCK activity: " + e.getMessage(), e);
//        }
//    }

    public X509Certificate generateCertificate(KeyPair keyPair){
        try {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            X509V3CertificateGenerator cert = new X509V3CertificateGenerator();
            cert.setSerialNumber(BigInteger.valueOf(1));   //or generate a random number
            cert.setSubjectDN(new X509Principal("CN=localhost"));  //see examples to add O,OU etc
            cert.setIssuerDN(new X509Principal("CN=localhost")); //same since it is self-signed
            cert.setPublicKey(keyPair.getPublic());
            cert.setNotBefore(start.getTime());
            cert.setNotAfter(end.getTime());
            cert.setSignatureAlgorithm("SHA1WithRSAEncryption");
            PrivateKey signingKey = keyPair.getPrivate();
            return cert.generate(signingKey, "BC");
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createNewKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            if(!keyStore.containsAlias(LaoSchoolShared.KEY_STORE_ALIAS)) {
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(1024);
                KeyPair kp = kpg.genKeyPair();
                X509Certificate certificate = generateCertificate(kp);
                Certificate[] certChain = new Certificate[1];
                certChain[0] = certificate;
                keyStore.setKeyEntry(LaoSchoolShared.KEY_STORE_ALIAS, (Key)kp.getPrivate(), null, certChain);
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        service = DataAccessImpl.getInstance(this.getApplicationContext());
        createNewKey();

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                checkAuth();
            }
        }, SPLASH_TIME_OUT);
    }

    private void checkAuth() {
        SharedPreferences prefs = this.getSharedPreferences(
                LaoSchoolShared.SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
        String auth_key = prefs.getString("auth_key", null);

        if(auth_key == null) {
            startLogin();
        }
        else {
           getUserProfile();
        }
    }

    private void startLogin() {
        Intent intent = new Intent(SplashScreen.this, ScreenLogin.class);
        startActivity(intent);
        this.finish();
    }

    private void getUserProfile() {
        service.getUserProfile(new AsyncCallback<User>() {
            @Override
            public void onSuccess(User result) {
                LaoSchoolShared.myProfile = result;
                Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                intent.setAction(result.getRoles());
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String message) {
                startLogin();
            }
        });
    }
}
