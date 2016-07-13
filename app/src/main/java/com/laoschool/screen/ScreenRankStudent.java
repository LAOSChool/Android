package com.laoschool.screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.adapter.RankStudentAdapter;
import com.laoschool.entities.StudentRanking;
import com.laoschool.model.AsyncCallback;
import com.laoschool.shared.LaoSchoolShared;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenRankStudent extends Fragment {


    RecyclerView mRanksStudent;
    private SwipeRefreshLayout mRefreshRanksStudent;

    public ScreenRankStudent() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_rank_student, container, false);
        mRefreshRanksStudent = (SwipeRefreshLayout) view.findViewById(R.id.mRefreshRanksStudent);
        mRanksStudent = (RecyclerView) view.findViewById(R.id.mRanksStudent);
        mRanksStudent.setLayoutManager(new LinearLayoutManager(getActivity()));
        getRanking();
        mRefreshRanksStudent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRanking();
                mRefreshRanksStudent.setRefreshing(false);
            }
        });
        return view;
    }

    private void getRanking() {
        LaoSchoolSingleton.getInstance().getDataAccessService().getMyRanking(new AsyncCallback<StudentRanking>() {
            @Override
            public void onSuccess(StudentRanking result) {
                if (result != null) {
                    mRanksStudent.setAdapter(new RankStudentAdapter(getActivity(), result));
                }
            }

            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onAuthFail(String message) {
                LaoSchoolShared.goBackToLoginPage(getActivity());
            }
        });
    }

}
