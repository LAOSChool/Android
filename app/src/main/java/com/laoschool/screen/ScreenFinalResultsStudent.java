package com.laoschool.screen;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.adapter.FinalResultsPagerAdapter;
import com.laoschool.entities.ExamResult;
import com.laoschool.entities.FinalResult;
import com.laoschool.model.AsyncCallback;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;


import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenFinalResultsStudent extends Fragment implements FragmentLifecycle, SearchView.OnQueryTextListener {


    private static final String TAG = ScreenFinalResultsStudent.class.getSimpleName();
    private Context context;
    private String currentRole;
    private int containerId;
    private Spinner cbxTermScreenRecordStudent;
    private RelativeLayout mListBox;


    private ProgressBar mProgress;
    private FrameLayout mError;
    private FrameLayout mNoData;

    private LinearLayout mListData;

    private ScreenFinalResultsStudent fragment;
    private FrameLayout mContainer;
    private ActionBar mActionBar;
    private View mAvgTotalFinalResults;
    ViewPager mPagerFinalResults;
    PagerSlidingTabStrip mTab;
    private SearchView mSearch;

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
            mAvgTotalFinalResults = view.findViewById(R.id.mContenDataFinalResults);
            mListBox = (RelativeLayout) view.findViewById(R.id.mListBox);
            mListData = (LinearLayout) view.findViewById(R.id.fragment_root);
            mProgress = (ProgressBar) view.findViewById(R.id.mProgressFinalResults);
            mError = (FrameLayout) view.findViewById(R.id.mError);
            mNoData = (FrameLayout) view.findViewById(R.id.mNoData);

            mPagerFinalResults = (ViewPager) view.findViewById(R.id.mPagerFinalResults);
            mTab = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);

            cbxTermScreenRecordStudent = (Spinner) view.findViewById(R.id.cbxTermScreenFinalResultsStudent);
            //fill data for cbx
            fillDataForSpinerFilter(cbxTermScreenRecordStudent, Arrays.asList(getResources().getStringArray(R.array.termTest)));
            getMyFinalResults();
            return view;
        }
    }

    private void getMyFinalResults() {
        showProgressLoading(true);
        int classId = LaoSchoolShared.myProfile.getEclass().getId();
        LaoSchoolSingleton.getInstance().getDataAccessService().getMyFinalResultsByClassId(classId, new AsyncCallback<FinalResult>() {
            @Override
            public void onSuccess(FinalResult result) {
                defineFinalResults(result);
                showProgressLoading(false);
            }

            @Override
            public void onFailure(String message) {
                showError();
            }

            @Override
            public void onAuthFail(String message) {
                LaoSchoolShared.goBackToLoginPage(context);
                showProgressLoading(false);
            }
        });
    }

    private void defineFinalResults(FinalResult result) {
//        1  Normal
//        2 Thi Hoc Ky
//        3  Trung Binh 4 thang
//        4  Trung Binh Hoc ky
//        5 Trung Binh Ca Nam
//        6 Thi Lai Ca Nam
//        7 Thi Tot Nghiep Cap

        defineAvgFinalTotal(result);

        final FinalResultsPagerAdapter resultsPagerAdapter = new FinalResultsPagerAdapter(getFragmentManager(), result);
        mPagerFinalResults.setAdapter(resultsPagerAdapter);

        mTab.setViewPager(mPagerFinalResults);
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                resultsPagerAdapter.getCurrentFragment().getExamResultsStudentSemesterAdapter().filter(newText);
                return true;
            }
        });
    }

    private void defineAvgFinalTotal(FinalResult result) {
        ((TextView) mAvgTotalFinalResults.findViewById(R.id.lbClassNameAndLocation)).setText(result.getClassName() + " | " + result.getCls_location());
        ((TextView) mAvgTotalFinalResults.findViewById(R.id.lbTeacherName)).setText(result.getTeacher_name());
        try {
            //caculater avg
            float avg1 = 0;
            float avg2 = 0;
            int count = 1;
            for (ExamResult exam : result.getExam_results()) {
                count++;
                if (exam.getExam_type() == 5) {
                    if (exam.getSresult() != null)
                        if (!exam.getSresult().trim().isEmpty()) {
                            float s_results = Float.parseFloat(exam.getSresult());
                            if (exam.getTerm_id() == 1) {
                                avg1 += s_results;
                            }
                            if (exam.getTerm_id() == 2) {
                                avg2 += s_results;
                            }
                        }
                }
            }

            float avgTerm1 = avg1 / count;
            float avgTerm2 = avg2 / count;
            float total = (avgTerm1 + avgTerm2) / 2;

            ((TextView) mAvgTotalFinalResults.findViewById(R.id.lbAvgTerm1)).setText(String.valueOf(avgTerm1));
            ((TextView) mAvgTotalFinalResults.findViewById(R.id.lbAvgTerm2)).setText(String.valueOf(avgTerm2));
            ((TextView) mAvgTotalFinalResults.findViewById(R.id.lbAvgofYear)).setText(String.valueOf(total));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        menu.clear();
        inflater.inflate(R.menu.menu_screen_final_results, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearch = new SearchView(((HomeActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, mSearch);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
