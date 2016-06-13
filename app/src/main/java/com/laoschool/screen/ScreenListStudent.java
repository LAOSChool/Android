package com.laoschool.screen;

import android.app.Activity;
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
import com.laoschool.adapter.RecylerViewScreenListTeacherAdapter;
import com.laoschool.view.FragmentLifecycle;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Hue on 6/13/2016.
 */
public class ScreenListStudent extends Fragment implements FragmentLifecycle {


    private String seletedTearcher;

    public ScreenListStudent() {
        // Required empty public constructor
    }

    public void setSeletedTearcher(String seletedTearcher) {
        this.seletedTearcher = seletedTearcher;
    }

    public String getSeletedTearcher() {
        return seletedTearcher;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.screen_list_student, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.mListStudent);

        //init adapter
        final List<String> strings = Arrays.asList(getResources().getStringArray(R.array.listTeacher));
        //RecylerViewScreenListTeacherAdapter adapter = new RecylerViewScreenListTeacherAdapter(this, strings);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);

        //set adapter
        recyclerView.setLayoutManager(gridLayoutManager);
        //recyclerView.setAdapter(adapter);
        return view;
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

    public static Fragment instantiate(int containerId, String currentRole) {
        return new ScreenListStudent();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}

