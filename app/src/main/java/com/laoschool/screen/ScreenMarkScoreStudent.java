package com.laoschool.screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenMarkScoreStudent extends Fragment implements FragmentLifecycle {


    public String getDataMessage() {
        return dataMessage;
    }

    public void setDataMessage(String dataMessage) {
        this.dataMessage = dataMessage;
    }

    private String dataMessage;
    private int containerId;

    public ScreenMarkScoreStudent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.screen_mark_score_student, container, false);
        Log.d(getString(R.string.title_screen_mark_score_student), "-Tag:" + getTag());
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            Log.d(getString(R.string.title_screen_mark_score_student), "-Container Id:" + containerId);

//            String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_EXAM_RESULTS_5);
//            Log.d(getString(R.string.title_screen_select_list_student), "-TAG Screen Exam Results:" + tag);


//            HomeActivity homeActivity = (HomeActivity) getActivity();
//            ScreenExamResults screenExamResults = (ScreenExamResults) homeActivity.getSupportFragmentManager().findFragmentByTag(tag);
//            if (screenExamResults != null) {
//                Log.d(getString(R.string.title_screen_mark_score_student), "Data:" + screenExamResults.getData());
//                this.setDataMessage(dataMessage);
//                Toast.makeText(homeActivity, "Data=" + dataMessage, Toast.LENGTH_SHORT).show();
//            } else {
//                Log.d(getString(R.string.title_screen_mark_score_student), "Data is null");
//                Toast.makeText(homeActivity, "Data is null", Toast.LENGTH_SHORT).show();
//            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public void onPauseFragment() {
    }

    @Override
    public void onResumeFragment() {
        String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_EXAM_RESULTS_5);
        ScreenExamResults screenExamResults = (ScreenExamResults) ((HomeActivity) getActivity()).getSupportFragmentManager().findFragmentByTag(tag);
        if (screenExamResults != null)
            setDataMessage(screenExamResults.getData());

        if (getDataMessage() != null) {
            Log.d(getString(R.string.title_screen_mark_score_student), "data:" + getDataMessage());
            Toast.makeText(getActivity(), "Data =" + getDataMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Data is Null", Toast.LENGTH_SHORT).show();
        }

    }


    public static Fragment instantiate(int containerId, String currentRole) {
        ScreenMarkScoreStudent fragment = new ScreenMarkScoreStudent();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        fragment.setArguments(args);
        return fragment;
    }
}
