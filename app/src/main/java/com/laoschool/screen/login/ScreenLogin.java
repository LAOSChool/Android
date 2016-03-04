package com.laoschool.screen.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.laoschool.R;
import com.laoschool.screen.HomeActivity;
import com.laoschool.shared.LaoSchoolShared;

public class ScreenLogin extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_login);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ScreenLoginMain()).commit();
    }

    public void login(View view) {
        _loginSucess(LaoSchoolShared.ROLE_TEARCHER);
    }

    public void _loginHelp(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ScreenLoginHelp()).addToBackStack(null).commit();
    }

    public void _loginForgotPassword(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ScreenFogotPassword()).addToBackStack(null).commit();
    }

    private void _loginSucess(String role) {
        finish();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(role);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
