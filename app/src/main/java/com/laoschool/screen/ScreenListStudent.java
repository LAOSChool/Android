package com.laoschool.screen;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.adapter.ExamResultsByTermPagerAdapter;
import com.laoschool.adapter.ListStudentOfClassAdapter;
import com.laoschool.adapter.RecylerViewScreenListTeacherAdapter;
import com.laoschool.entities.User;
import com.laoschool.model.AsyncCallback;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

import java.util.List;

/**
 * Created by Hue on 6/13/2016.
 */
public class ScreenListStudent extends Fragment implements FragmentLifecycle {
    private static final String TAG = ScreenListStudent.class.getSimpleName();
    private ScreenListStudent listStudent;
    RecyclerView mListStudent;
    HomeActivity activity;
    private Context context;

    View mError;
    View mProgress;
    View mNoData;

    public interface IScreenListStudentOfClass {
        void gotoDetailsStudent(User user);
    }

    public IScreenListStudentOfClass iScreenListStudentOfClass;


    public ScreenListStudent() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.listStudent = this;
        activity = (HomeActivity) getActivity();
        this.context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.screen_list_student, container, false);
        mListStudent = (RecyclerView) view.findViewById(R.id.mListStudent);
        mListStudent.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        mError = view.findViewById(R.id.mError);
        mProgress = view.findViewById(R.id.mProgress);
        mNoData = view.findViewById(R.id.mNoData);


        if (getUserVisibleHint()) {
            int classId = LaoSchoolShared.myProfile.getEclass().getId();
            getStudentOfClass(classId);
        }
        handlerErrorAndNodata();

        return view;
    }

    private void handlerErrorAndNodata() {
        View.OnClickListener reloadDataClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int classId = LaoSchoolShared.myProfile.getEclass().getId();
                getStudentOfClass(classId);
            }
        };
        mError.findViewById(R.id.mReloadData).setOnClickListener(reloadDataClick);
        mNoData.findViewById(R.id.mReloadData).setOnClickListener(reloadDataClick);
    }

    private void getStudentOfClass(int classId) {
        showProcessLoading(true);
        LaoSchoolSingleton.getInstance().getDataAccessService().getUsers(classId, LaoSchoolShared.ROLE_STUDENT, "", -1, new AsyncCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                if (result.size() > 0) {
                    fillData(result);
                    showProcessLoading(false);
                } else {
                    showNoData();
                }
            }


            @Override
            public void onFailure(String message) {
                showError();
            }

            @Override
            public void onAuthFail(String message) {
                Log.e(ExamResultsByTermPagerAdapter.TAG, "getFilterSubject().onAuthFail() -message:" + message);
                LaoSchoolShared.goBackToLoginPage(context);
            }
        });
    }

    private void showError() {
        mProgress.setVisibility(View.GONE);
        mListStudent.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
        mNoData.setVisibility(View.GONE);
    }

    private void showNoData() {
        mProgress.setVisibility(View.GONE);
        mListStudent.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mNoData.setVisibility(View.VISIBLE);
    }

    private void showProcessLoading(boolean show) {
        if (show) {
            mProgress.setVisibility(View.VISIBLE);
            mListStudent.setVisibility(View.GONE);
            mError.setVisibility(View.GONE);
            mNoData.setVisibility(View.GONE);
        } else {
            mProgress.setVisibility(View.GONE);
            mListStudent.setVisibility(View.VISIBLE);
            mError.setVisibility(View.GONE);
            mNoData.setVisibility(View.GONE);
        }
    }

    private void fillData(List<User> result) {
        ListStudentOfClassAdapter listStudentOfClassAdapter = new ListStudentOfClassAdapter(listStudent, result);
        mListStudent.setAdapter(listStudentOfClassAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_screen_list_student_of_class, menu);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        iScreenListStudentOfClass = (IScreenListStudentOfClass) context;
    }
}

