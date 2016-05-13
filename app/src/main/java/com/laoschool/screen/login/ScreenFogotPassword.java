package com.laoschool.screen.login;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.InputType;
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
import com.laoschool.shared.LaoSchoolShared;

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
        txbPhoneNumber.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_CLASS_PHONE);
        this.scrollView = scrollView;

        int color = Color.parseColor("#ffffff");
        btnBack.setColorFilter(color);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerz.switchToScreenLoginMain();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textbBox.requestFocus();
                InputMethodManager imm = (InputMethodManager) thiz.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                String sso_id = txbUserName.getText().toString();
                String phone = txbPhoneNumber.getText().toString();

                final ProgressDialog ringProgressDialog = ProgressDialog.show(thiz.getActivity(), "Please wait ...", "Sending your request ...", true);

//                AlertDialog.Builder builder1 = new AlertDialog.Builder(thiz.getActivity());
//                builder1.setMessage("Your password has been reset !");
//                builder1.setCancelable(true);
//                final AlertDialog alert11 = builder1.create();

                service.forgotPass(sso_id, phone, new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ringProgressDialog.dismiss();
                        Toast.makeText(thiz.getActivity(), "Your password has been reset !", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        ringProgressDialog.dismiss();
                        if(message.contains("sso_id"))
                            Toast.makeText(thiz.getActivity(), "Username not exist !", Toast.LENGTH_SHORT).show();
                        else if(message.contains("phone"))
                            Toast.makeText(thiz.getActivity(), "Phone number not match with username !", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(thiz.getActivity(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthFail(String message) {
                        LaoSchoolShared.goBackToLoginPage(thiz.getContext());
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

    private void sendScroll() {
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
