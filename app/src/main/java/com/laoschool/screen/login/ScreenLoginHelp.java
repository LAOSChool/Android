package com.laoschool.screen.login;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laoschool.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenLoginHelp extends Fragment {

    ScreenLogin containerz;

    public void setContainer(ScreenLogin container) {
        this.containerz = container;
    }

    public ScreenLoginHelp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_login_help, container, false);

        RelativeLayout header = (RelativeLayout) view.findViewById(R.id.header);
        ImageButton btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        TextView txvTitle = (TextView) view.findViewById(R.id.txvTitle);

        txvTitle.setText(R.string.SCCommon_Help);

        int color = Color.parseColor("#ffffff");
        btnBack.setColorFilter(color);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerz.switchToScreenLoginMain();
            }
        });

        return view;
    }

}
