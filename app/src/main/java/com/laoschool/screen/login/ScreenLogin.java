package com.laoschool.screen.login;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.screen.HomeActivity;
import com.laoschool.shared.LaoSchoolShared;

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

    public void goToHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setAction(LaoSchoolShared.ROLE_STUDENT);
        startActivity(intent);
        finish();

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
