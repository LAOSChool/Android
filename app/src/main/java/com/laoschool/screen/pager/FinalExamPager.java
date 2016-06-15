package com.laoschool.screen.pager;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.laoschool.R;
import com.laoschool.adapter.FinalResultsAdapter;
import com.laoschool.entities.FinalResult;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class FinalExamPager extends Fragment {

    private static final String TAG = FinalExamPager.class.getSimpleName();
    private final int position;
    FinalResult finalResult;
    FinalResultsAdapter examResultsStudentSemesterAdapter;
    ObservableRecyclerView mListExam;

    public FinalResultsAdapter getExamResultsStudentSemesterAdapter() {
        return examResultsStudentSemesterAdapter;
    }

    public ObservableRecyclerView getListExamView() {
        return mListExam;
    }

    public FinalExamPager(int position, FinalResult finalResult) {
        this.position = position;
        this.finalResult = finalResult;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.final_exam_pager, container, false);
        mListExam = (ObservableRecyclerView) view.findViewById(R.id.mListExam);
        mListExam.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (finalResult != null) {
            Log.d(TAG, "-size:" + finalResult.getExam_results().size());
            examResultsStudentSemesterAdapter = new FinalResultsAdapter(this, finalResult.getExam_results());
            mListExam.setAdapter(examResultsStudentSemesterAdapter);
        } else {
            Log.d(TAG, "Null");
        }
        return view;
    }

}
