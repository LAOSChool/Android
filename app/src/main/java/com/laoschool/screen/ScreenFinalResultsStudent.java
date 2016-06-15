package com.laoschool.screen;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.adapter.ExamResultsForStudentAdapter;
import com.laoschool.adapter.FinalResultsAdapter;
import com.laoschool.adapter.SessionAdapter;
import com.laoschool.entities.ExamResult;
import com.laoschool.entities.FinalResult;
import com.laoschool.entities.TimeTable;
import com.laoschool.model.AsyncCallback;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenFinalResultsStudent extends Fragment implements FragmentLifecycle {


    private static final String TAG = ScreenFinalResultsStudent.class.getSimpleName();
    private Context context;
    private String currentRole;
    private int containerId;
    private Spinner cbxTermScreenRecordStudent;
    private ObservableRecyclerView recyclerView;
    private RelativeLayout mListBox;
    private LinearLayoutManager layoutManager;
    private int firstVisibleInListview;
    private android.support.v7.widget.Toolbar mToolbar;

    private ProgressBar mProgress;
    private FrameLayout mError;
    private FrameLayout mNoData;

    private LinearLayout mListData;

    private ScreenFinalResultsStudent fragment;
    private FrameLayout mContainer;
    private ActionBar mActionBar;
    private View mContenDataFinalResults;

    public ScreenFinalResultsStudent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            currentRole = getArguments().getString(LaoSchoolShared.CURRENT_ROLE);
        }
        this.context = getActivity();
        fragment = this;
        HomeActivity activity = (HomeActivity) getActivity();
        mActionBar = activity.getSupportActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (currentRole == null)
            return inflater.inflate(R.layout.screen_error_application, container, false);
        else {
            // Inflate the layout for this fragment
            final View view = inflater.inflate(R.layout.screen_final_results_student, container, false);
            mContainer = (FrameLayout) view.findViewById(R.id.mContainer);
            mContenDataFinalResults = view.findViewById(R.id.mContenDataFinalResults);
            mListBox = (RelativeLayout) view.findViewById(R.id.mListBox);
            mListData = (LinearLayout) view.findViewById(R.id.fragment_root);
            mProgress = (ProgressBar) view.findViewById(R.id.mProgressFinalResults);
            mError = (FrameLayout) view.findViewById(R.id.mError);
            mNoData = (FrameLayout) view.findViewById(R.id.mNoData);

            cbxTermScreenRecordStudent = (Spinner) view.findViewById(R.id.cbxTermScreenFinalResultsStudent);
            recyclerView = (ObservableRecyclerView) view.findViewById(R.id.mRecyclerViewResultsDetailsScreenFinalResultsStudent);
            recyclerView.setHasFixedSize(false);
            recyclerView.setTouchInterceptionViewGroup((ViewGroup) view.findViewById(R.id.fragment_root));

            //set LayoutManager to recyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            layoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            layoutManager.scrollToPositionWithOffset(0, 0);

            firstVisibleInListview = layoutManager.findFirstVisibleItemPosition();

            //fill data for cbx
            fillDataForSpinerFilter(cbxTermScreenRecordStudent, Arrays.asList(getResources().getStringArray(R.array.termTest)));

            //getMyExamResult();
            getMyFinalResults();
            handlerScroll();
            return view;
        }
    }

    private void getMyFinalResults() {
        LaoSchoolSingleton.getInstance().getDataAccessService().getMyFinalResults(2015, new AsyncCallback<List<FinalResult>>() {
            @Override
            public void onSuccess(List<FinalResult> result) {
                Log.d(TAG, "getMyFinalResults().onSuccess() -size:" + result.size());
                FinalResultsAdapter finalResultAdapter = new FinalResultsAdapter(context);
                recyclerView.setAdapter(finalResultAdapter);
            }

            @Override
            public void onFailure(String message) {
                showError();
            }

            @Override
            public void onAuthFail(String message) {
                LaoSchoolShared.goBackToLoginPage(getActivity());
            }
        });
    }

    private void handlerScroll() {
        recyclerView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

            }

            @Override
            public void onDownMotionEvent() {

            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {

                try {
                    if (scrollState == ScrollState.UP) {
                        if (mListBox.getVisibility() == View.VISIBLE) {
                            mActionBar.hide();
                            mListBox.setVisibility(View.GONE);
                        }
                    } else if (scrollState == ScrollState.DOWN) {
                        if (mListBox.getVisibility() == View.GONE) {
                            mActionBar.show();
                            mListBox.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "handlerScroll().onUpOrCancelMotionEvent() -exception : " + e.getMessage());
                }
            }
        });

    }


    private void fillDataForSpinerFilter(Spinner cbx, List<String> classTest) {
        ArrayAdapter<String> dataAdapterclassTest = new ArrayAdapter<String>(getActivity(), R.layout.row_year_final_results_selected, classTest);
        // Drop down layout style - list view with radio button
        dataAdapterclassTest.setDropDownViewResource(R.layout.row_year_final_results);
        // attaching data adapter to spinner
        cbx.setAdapter(dataAdapterclassTest);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }

    public static Fragment instantiate(int containerId, String currentRole) {
        ScreenFinalResultsStudent fragment = new ScreenFinalResultsStudent();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        args.putString(LaoSchoolShared.CURRENT_ROLE, currentRole);
        fragment.setArguments(args);
        return fragment;
    }

    private void _refeshData(List<ExamResult> result) {
        recyclerView.swapAdapter(new ExamResultsForStudentAdapter(this, hashDatafromSemestes(result)), true);

    }

    private ArrayList<ExamResult> hashDatafromSemestes(List<ExamResult> result) {
        ArrayList<ExamResult> examResults = new ArrayList<>();
        for (ExamResult examResult : result) {
            if (examResult.getTerm_id() == 1) {
                examResults.add(examResult);
            }
        }
        return examResults;
    }

    private void getMyExamResult() {
        showProgressLoading(true);
//        int filter_class_id = LaoSchoolShared.myProfile.getEclass().getId();
//        LaoSchoolSingleton.getInstance().getDataAccessService().getMyExamResults(filter_class_id, new AsyncCallback<List<ExamResult>>() {
//            @Override
//            public void onSuccess(List<ExamResult> result) {
//                if (result.size() > 0) {
//                    if (result != null) {
//                        _refeshData(result);
//                    } else {
//                    }
//                } else {
//                }
//                showProgressLoading(false);
//            }
//
//            @Override
//            public void onFailure(String message) {
//                showProgressLoading(false);
//            }
//
//            @Override
//            public void onAuthFail(String message) {
//                LaoSchoolShared.goBackToLoginPage(context);
//            }
//        });

        int classId = LaoSchoolShared.myProfile.getEclass().getId();
        LaoSchoolSingleton.getInstance().getDataAccessService().getTimeTables(classId, new AsyncCallback<List<TimeTable>>() {
            @Override
            public void onSuccess(List<TimeTable> result) {
                if (result.size() > 0) {
                    showProgressLoading(false);
                    recyclerView.setAdapter(new SessionAdapter(context, 0, result));
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
                LaoSchoolShared.goBackToLoginPage(getActivity());
            }
        });

    }

    private void showToolbar() {
        moveToolbar(0);
    }

    private void hideToolbar() {
        moveToolbar(-mToolbar.getHeight());
    }

    private void moveToolbar(int toTranslationY) {

    }


    public int getScreenHeight() {
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        Log.d(TAG, "getScreenHeight() -height : " + screenHeight);
        return screenHeight;
    }

    private boolean toolbarIsShown() {
        // Toolbar is 0 in Y-axis, so we can say it's shown.
        return true;
    }

    private boolean toolbarIsHidden() {
        // Toolbar is outside of the screen and absolute Y matches the height of it.
        // So we can say it's hidden.
        return false;
    }

    private void showError() {
        mError.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
        mListData.setVisibility(View.GONE);
        mNoData.setVisibility(View.GONE);
    }

    private void showNoData() {
        mNoData.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
        mProgress.setVisibility(View.GONE);
        mListData.setVisibility(View.GONE);
    }


    private void showProgressLoading(boolean b) {
        try {
            if (b) {
                mProgress.setVisibility(View.VISIBLE);
                mListData.setVisibility(View.GONE);
            } else {
                mProgress.setVisibility(View.GONE);
                mListData.setVisibility(View.VISIBLE);
            }
            mError.setVisibility(View.GONE);
            mNoData.setVisibility(View.GONE);
        } catch (Exception e) {
        }
    }
}
