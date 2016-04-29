package com.laoschool.screen;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.adapter.RecyclerViewScreenMoreAdapter;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenMore extends Fragment implements FragmentLifecycle {


    private static final String CURRENT_ROLE = "current_role";
    private static final String TAG = "ScreenMore";
    private Context context;
    private int containerId;
    private String currentRole;
    private boolean checkConn;

    public ScreenMore() {
        // Required empty public constructor
    }

    public interface IScreenMore {
        void gotoSchoolInformationformMore();

        void gotoListTearcherformMore();

        void gotoSettingformMore();

        void logoutApplication();

        void gotoDetailsUser();

        void gotoExamResultsformMore();

        void gotoSchoolRecordbyYearformMore();

        void gotoScheduleformMore();
    }

    public IScreenMore iScreenMore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return _defineScreenMorebyRole(inflater, container);
    }

    private View _defineScreenMorebyRole(LayoutInflater inflater, ViewGroup container) {
//        boolean checkConn = LaoSchoolShared.checkConn(context);
//        Log.d(TAG, "-checkConn:" + checkConn);
        if (currentRole != null) {
            if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
                return _defineSrceenMoreTeacher(inflater, container);
            } else {
                return _defineSrceenMoreStudent(inflater, container);
            }
        } else {
            return inflater.inflate(R.layout.screen_error_application, container, false);
        }


//        if (checkConn) {
//        } else {
//            return inflater.inflate(R.layout.view_connection_error, container, false);
//        }

    }

    private View _defineSrceenMoreStudent(LayoutInflater inflater, ViewGroup container) {
        try {

            View view = inflater.inflate(R.layout.screen_more_student, container, false);
            //
            LinearLayout mDetaislUser = (LinearLayout) view.findViewById(R.id.mDetaislUser);
            TextView txtStudentName = (TextView) view.findViewById(R.id.txtUserNameScreenMoreStudent);
            TextView txtSchoolName = (TextView) view.findViewById(R.id.txtUserNameScreenMoreStudent);
            TextView txtTerm = (TextView) view.findViewById(R.id.txtTermScreenMoreStudent);
            RecyclerView mRecylerViewFunctionMore = (RecyclerView) view.findViewById(R.id.mRecylerViewFunctionMore);

            //Handler goto detaisl
            mDetaislUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iScreenMore.gotoDetailsUser();
                }
            });

            //init adapte
            List<String> more_student = Arrays.asList(getResources().getStringArray(R.array.more_student));
            RecyclerViewScreenMoreAdapter adapter = new RecyclerViewScreenMoreAdapter(this, more_student);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);

            //set adapter
            mRecylerViewFunctionMore.setLayoutManager(gridLayoutManager);
            mRecylerViewFunctionMore.setAdapter(adapter);
            return view;
        } catch (Exception e) {
            return null;
        }
    }

    private View _defineSrceenMoreTeacher(LayoutInflater inflater, ViewGroup container) {
        try {
            View view = inflater.inflate(R.layout.screen_more_teacher, container, false);
            //
            LinearLayout mDetaislUser = (LinearLayout) view.findViewById(R.id.mDetaislUser);
            TextView txtUserName = (TextView) view.findViewById(R.id.txtUserNameScreenMoreTeacher);
            TextView txtSchoolName = (TextView) view.findViewById(R.id.txtSchoolNameScreenMoreTeacher);
            TextView txtContactPhone = (TextView) view.findViewById(R.id.txtContactPhoneScreenMoreTeacher);
            RecyclerView mRecylerViewFunctionMore = (RecyclerView) view.findViewById(R.id.mRecylerViewFunctionMore);

            Typeface roboto = Typeface.createFromAsset(context.getAssets(),
                    "font/Roboto-Regular.ttf"); //use this.getAssets if you are calling from an Activity
            txtUserName.setTypeface(roboto);


            //Handler goto detaisl
            mDetaislUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iScreenMore.gotoDetailsUser();
                }
            });

            //init adapte
            final List<String> more_teacher = Arrays.asList(getResources().getStringArray(R.array.more_teacher));
            RecyclerViewScreenMoreAdapter adapter = new RecyclerViewScreenMoreAdapter(this, more_teacher);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);

            //set adapter
            mRecylerViewFunctionMore.setLayoutManager(gridLayoutManager);
            mRecylerViewFunctionMore.setAdapter(adapter);
            return view;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.context = getActivity();
        if (getArguments() != null) {
            checkConn = LaoSchoolShared.checkConn(context);
            Log.d(TAG, "-checkConn:" + checkConn);
//            if (checkConn) {
//
//            }
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            currentRole = getArguments().getString(CURRENT_ROLE);
            Log.d(TAG, "-Container Id:" + containerId);
            Log.d(TAG, "-Role:" + currentRole);
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

    public static Fragment instantiate(int containerId, String currentRole) {
        ScreenMore fragment = new ScreenMore();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        args.putString(CURRENT_ROLE, currentRole);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LaoSchoolShared.myProfile == null) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.logoutApplication();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iScreenMore = (IScreenMore) activity;
    }
}
