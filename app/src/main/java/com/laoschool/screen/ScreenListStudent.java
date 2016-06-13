package com.laoschool.screen;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
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
    private boolean alreadyExecuted = false;
    private ScreenListStudent listStudent;
    RecyclerView mListStudent;

    public interface IScreenListStudentOfClass {
        void gotoDetailsStudent();
    }

    public IScreenListStudentOfClass iScreenListStudentOfClass;


    public ScreenListStudent() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.screen_list_student, container, false);
        mListStudent = (RecyclerView) view.findViewById(R.id.mListStudent);
        mListStudent.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        if (!alreadyExecuted && getUserVisibleHint()) {
            int classId = LaoSchoolShared.myProfile.getEclass().getId();
            getStudentOfClass(classId);
        }
        return view;
    }

    private void getStudentOfClass(int classId) {
        LaoSchoolSingleton.getInstance().getDataAccessService().getUsers(classId, LaoSchoolShared.ROLE_STUDENT, "", -1, new AsyncCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                ListStudentOfClassAdapter listStudentOfClassAdapter = new ListStudentOfClassAdapter(listStudent, result);
                mListStudent.setAdapter(listStudentOfClassAdapter);
            }

            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onAuthFail(String message) {

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.listStudent = this;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //set display menu item
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

