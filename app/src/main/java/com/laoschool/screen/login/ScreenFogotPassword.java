package com.laoschool.screen.login;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenFogotPassword extends Fragment {

    private DataAccessInterface service;
    ScreenLogin containerz;
    ScreenFogotPassword thiz = this;
    ScrollView scrollView;

    public void setContainer(ScreenLogin container) {
        this.containerz = container;
    }

    public ScreenFogotPassword() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        service = DataAccessImpl.getInstance(this.getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.screen_fogot_password, container, false);

        RelativeLayout header = (RelativeLayout) view.findViewById(R.id.header);
        ImageButton btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        TextView btnSubmit = (TextView) view.findViewById(R.id.btnSubmit);
        final EditText txbUserName = (EditText) view.findViewById(R.id.txbUserName);
        final EditText txbPhoneNumber = (EditText) view.findViewById(R.id.txbPhoneNumber);
        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        final LinearLayout textbBox = (LinearLayout) view.findViewById(R.id.textBox);

        this.scrollView = scrollView;

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerz.switchToScreenLoginMain();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sso_id = txbUserName.getText().toString();
                String phone = txbPhoneNumber.getText().toString();

                Toast.makeText(thiz.getActivity(), "Your request has been send", Toast.LENGTH_SHORT).show();

                service.forgotPass(sso_id, phone, new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(thiz.getActivity(), "Your password has been reset", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(thiz.getActivity(), "Wrong username or phone number", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        txbUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    sendScroll();
                }
            }
        });

        txbPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    sendScroll();
                }
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textbBox.requestFocus();
                    InputMethodManager imm = (InputMethodManager) thiz.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });

        return view;
    }

    private void sendScroll(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0, scrollView.getBottom());
                    }
                });
            }
        }).start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_screen_message, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
