package com.laoschool.screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laoschool.R;
import com.laoschool.adapter.RecyclerViewScreenMoreAdapter;
import com.laoschool.adapter.RecylerViewScreenSettingAdapter;
import com.laoschool.view.FragmentLifecycle;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenSetting extends Fragment implements FragmentLifecycle {


    public ScreenSetting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//
        View view = inflater.inflate(R.layout.screen_setting, container, false);
        RecyclerView mRecylerViewScreenSetting = (RecyclerView) view.findViewById(R.id.mRecylerViewScreenSetting);

        //init adapter
        final List<String> settings = Arrays.asList(getResources().getStringArray(R.array.settings));
        RecylerViewScreenSettingAdapter adapter = new RecylerViewScreenSettingAdapter(this, settings);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);

        //set adapter
        mRecylerViewScreenSetting.setLayoutManager(gridLayoutManager);
        mRecylerViewScreenSetting.setAdapter(adapter);

        return view;
    }

    public static Fragment instantiate(int containerId, String currentRole) {
        return new ScreenSetting();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }
}
