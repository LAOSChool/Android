package com.laoschool.screen;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.laoschool.R;
import com.laoschool.entities.Attendance;
import com.laoschool.entities.User;
import com.laoschool.entities.UserDetail;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;

public class ScreenLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_login);

        final DataAccessInterface service = DataAccessImpl.getInstance(this.getApplicationContext());

        final Button button = (Button) findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                service.login("khiemph", "khiemphpass", new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        System.out.println("AUTH_KEY = " + result);
                        goToHomeScreen();
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        System.out.println(caught.getMessage());
                    }
                });
            }
        });
    }

    public void goToHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        finish();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
