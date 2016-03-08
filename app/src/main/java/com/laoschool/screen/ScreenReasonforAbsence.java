package com.laoschool.screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laoschool.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenReasonforAbsence extends Fragment {


    public ScreenReasonforAbsence() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.screen_reason_for_absence, container, false);
    }

}
