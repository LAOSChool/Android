package com.laoschool.screen.login;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.laoschool.R;
import com.laoschool.model.DataAccessImpl;

public class ScreenLogin extends AppCompatActivity {

    ScreenLoginMain screenLoginMain = new ScreenLoginMain();
    //  ScreenFogotPassword screenFogotPassword = new ScreenFogotPassword();
    ScreenLoginHelp screenLoginHelp = new ScreenLoginHelp();
    private Context thiz;
    private DataAccessImpl service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_login);
        this.thiz = this;
        service = DataAccessImpl.getInstance(this.getApplicationContext());
        screenLoginMain.setContainer(this);
//      screenFogotPassword.setContainer(this);
        screenLoginHelp.setContainer(this);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, screenLoginMain);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void switchToScreenLoginMain() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.container, screenLoginMain);
        fragmentTransaction.commit();
    }

    public void switchToScreenFogotPassword() {
        ScreenFogotPassword screenFogotPassword = new ScreenFogotPassword();
        screenFogotPassword.setContainer(this);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.container, screenFogotPassword);
        fragmentTransaction.commit();
    }

    public void switchToScreenLoginHelp() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.container, screenLoginHelp);
        fragmentTransaction.commit();
    }
}
