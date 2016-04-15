package com.laoschool.screen.login;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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

                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_screen_message, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
