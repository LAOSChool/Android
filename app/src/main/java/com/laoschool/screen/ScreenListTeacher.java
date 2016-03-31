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
import com.laoschool.adapter.RecylerViewScreenSettingAdapter;
import com.laoschool.view.FragmentLifecycle;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenListTeacher extends Fragment implements FragmentLifecycle {


    private String seletedTearcher;

    public ScreenListTeacher() {
        // Required empty public constructor
    }

    public void setSeletedTearcher(String seletedTearcher) {
        this.seletedTearcher = seletedTearcher;
    }

    public String getSeletedTearcher() {
        return seletedTearcher;
    }

    public interface IScreenListTeacher {
        void gotoScreenTeacherDetailsformScreenListTeacher(String teacherSelected);
    }

    public IScreenListTeacher iScreenListTeacher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.screen_list_teacher, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.mRecylerViewScreenListTeacher);

        //init adapter
        final List<String> strings = Arrays.asList(getResources().getStringArray(R.array.listTeacher));
        RecylerViewScreenListTeacherAdapter adapter = new RecylerViewScreenListTeacherAdapter(this, strings);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);

        //set adapter
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //set display menu item
        inflater.inflate(R.menu.menu_screen_list_teacher, menu);
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }

    public static Fragment instantiate(int containerId, String currentRole) {
        return new ScreenListTeacher();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iScreenListTeacher = (IScreenListTeacher) activity;
    }
}
