package com.laoschool.screen.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.screen.HomeActivity;
import com.laoschool.shared.LaoSchoolShared;

public class ScreenLogin extends AppCompatActivity {

    private DataAccessInterface service;
    private ScreenLogin thiz = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_login);

        service = DataAccessImpl.getInstance(this.getApplicationContext());

        final EditText txbUserName = (EditText) findViewById(R.id.txbUserName);
        final EditText txbPassword = (EditText) findViewById(R.id.txbPassword);

        final Button button = (Button) findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userName = txbUserName.getText().toString();
                String password = txbPassword.getText().toString();
                service.login(userName, password, new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        goToHomeScreen();
                    }

                    @Override
                    public void onFailure(String message) {
                        System.out.println(message);
                        if (message.contains("TimeoutError"))
                            Toast.makeText(thiz, "No internet connection", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(thiz, "Wrong username or password", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
//        service.login("khiemph", "1234567890", new AsyncCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                goToHomeScreen();
//            }
//
//            @Override
//            public void onFailure(String message) {
//                goToHomeScreen();
//                System.out.println(message);
//                if (message.contains("TimeoutError"))
//                    Toast.makeText(thiz, "No internet connection", Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(thiz, "Wrong username or password", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public void goToHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setAction(LaoSchoolShared.ROLE_STUDENT);
        startActivity(intent);
    }

    public void login(View view) {
        finish();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
