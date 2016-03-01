package com.laoschool.screen;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laoschool.R;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenExamResults extends Fragment implements FragmentLifecycle {


    private int containerId;
    private String data;

    public ScreenExamResults() {
        // Required empty public constructor
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    interface IScreenExamResults {
        void sendData(String message);
    }

    private IScreenExamResults iScreenExamResults;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.screen_exam_results, container, false);
        view.findViewById(R.id.btnGotoMarkScore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_MARK_SCORE_STUDENT_10);
                Log.d(getString(R.string.title_screen_exam_results), tag);
                setData("Hello " + getString(R.string.title_screen_mark_score_student));
                iScreenExamResults.sendData("Hello " + getString(R.string.title_screen_mark_score_student));
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            Log.d(getString(R.string.title_screen_exam_results), "-Container Id:" + containerId);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //set display menu item
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iScreenExamResults = (IScreenExamResults) activity;
    }

    public static Fragment instantiate(int containerId) {
        ScreenExamResults fragment = new ScreenExamResults();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        fragment.setArguments(args);
        return fragment;
    }
}
