package com.laoschool.screen.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laoschool.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenLoginMain extends Fragment {


    public ScreenLoginMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.screen_login_conten, container, false);
    }

}
