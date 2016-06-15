package com.laoschool.screen;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.AdapterView;
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
import com.laoschool.adapter.SelectedSchoolYearsAdapter;
import com.laoschool.entities.ExamResult;
import com.laoschool.entities.FinalResult;
import com.laoschool.entities.SchoolYears;
import com.laoschool.model.AsyncCallback;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenFinalResultsStudent extends Fragment implements FragmentLifecycle, SearchView.OnQueryTextListener, Spinner.OnItemSelectedListener {


    private static final String TAG = ScreenFinalResultsStudent.class.getSimpleName();
    private Context context;
    private String currentRole;
    private int containerId;
    private Spinner cbxTermScreenRecordStudent;
    private RelativeLayout mFilterYear;


    private ProgressBar mProgress;
    private FrameLayout mError;
    private View mNoDataFinal;

    private LinearLayout mListData;

    private ScreenFinalResultsStudent fragment;
    private View mContainer;
    private ActionBar mActionBar;
    ViewPager mPagerFinalResults;
    PagerSlidingTabStrip mTab;
    private SearchView mSearch;
    private List<SchoolYears> schoolYears;
    private View mDataFinal;
    private View mSucgetionSelectedYear;
    private View mProgressLoadingFinal;
    private View mDataTotalFinalResults;

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
            mContainer = view.findViewById(R.id.mContainer);
            mDataTotalFinalResults = view.findViewById(R.id.mDataTotalFinalResults);
            mFilterYear = (RelativeLayout) view.findViewById(R.id.mListBox);
            mProgress = (ProgressBar) view.findViewById(R.id.mProgressFinalResults);
            mError = (FrameLayout) view.findViewById(R.id.mError);

            ///
            mDataFinal = view.findViewById(R.id.mDataFinal);
            mNoDataFinal = view.findViewById(R.id.mNoDataFinal);
            mSucgetionSelectedYear = view.findViewById(R.id.mSucgetionSelectedYear);
            mProgressLoadingFinal = view.findViewById(R.id.mProgressLoadingFinal);


            mPagerFinalResults = (ViewPager) view.findViewById(R.id.mPagerFinalResults);
            mTab = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);

            cbxTermScreenRecordStudent = (Spinner) view.findViewById(R.id.cbxTermScreenFinalResultsStudent);
            //fill data for cbx
            getMySchoolYears();
            return view;
        }
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
        ((TextView) mDataTotalFinalResults.findViewById(R.id.lbClassNameAndLocation)).setText(result.getClassName() + " | " + result.getCls_location());
        ((TextView) mDataTotalFinalResults.findViewById(R.id.lbTeacherName)).setText(result.getTeacher_name());
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

            ((TextView) mDataTotalFinalResults.findViewById(R.id.lbAvgTerm1)).setText(String.valueOf(avgTerm1));
            ((TextView) mDataTotalFinalResults.findViewById(R.id.lbAvgTerm2)).setText(String.valueOf(avgTerm2));
            ((TextView) mDataTotalFinalResults.findViewById(R.id.lbAvgofYear)).setText(String.valueOf(total));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMySchoolYears() {
        showProgressLoading(true);
        LaoSchoolSingleton.getInstance().getDataAccessService().getMySchoolYears(new AsyncCallback<List<SchoolYears>>() {
            @Override
            public void onSuccess(List<SchoolYears> result) {
                fillDataToSeletedYear(result);
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

    private void fillDataToSeletedYear(List<SchoolYears> result) {

        SchoolYears schoolYears = new SchoolYears();
        schoolYears.setId(-1);
        schoolYears.setYears(context.getString(R.string.selected));

        List<SchoolYears> schoolYearsList = new ArrayList<>();
        schoolYearsList.add(schoolYears);
        schoolYearsList.addAll(result);

        SelectedSchoolYearsAdapter yearsAdapter = new SelectedSchoolYearsAdapter(context, schoolYearsList);
        //yearsAdapter.setDropDownViewResource(R.layout.row_selected_school_year);
        cbxTermScreenRecordStudent.setAdapter(yearsAdapter);
        cbxTermScreenRecordStudent.setOnItemSelectedListener(this);
        this.schoolYears = schoolYearsList;
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
    }

    private void showNoData() {
        mNoDataFinal.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
        mProgress.setVisibility(View.GONE);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position > 0) {
            int year = 0;
            getMyFinalResultsByYear(year);
        } else {
            showSugesstionFinal();
        }

    }

    private void getMyFinalResultsByYear(int year) {
        showProgressLoadingFinal(true);
        int classId = LaoSchoolShared.myProfile.getEclass().getId();
        LaoSchoolSingleton.getInstance().getDataAccessService().getMyFinalResultsByClassId(classId, year, new AsyncCallback<FinalResult>() {
            @Override
            public void onSuccess(final FinalResult result) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        defineFinalResults(result);
                        showProgressLoadingFinal(false);
                    }
                }, 500);
            }

            @Override
            public void onFailure(String message) {
                showErrorFinal();
            }

            @Override
            public void onAuthFail(String message) {
                LaoSchoolShared.goBackToLoginPage(context);
                showProgressLoadingFinal(false);
            }
        });
    }

    private void showErrorFinal() {

    }

    private void showProgressLoadingFinal(boolean show) {
        if (show) {
            mSucgetionSelectedYear.setVisibility(View.GONE);
            mDataFinal.setVisibility(View.GONE);
            mNoDataFinal.setVisibility(View.GONE);
            mProgressLoadingFinal.setVisibility(View.VISIBLE);
        } else {
            mDataFinal.setVisibility(View.VISIBLE);
            mNoDataFinal.setVisibility(View.GONE);
            mSucgetionSelectedYear.setVisibility(View.GONE);
            mProgressLoadingFinal.setVisibility(View.GONE);
        }
    }

    private void showSugesstionFinal() {
        mSucgetionSelectedYear.setVisibility(View.VISIBLE);
        mDataFinal.setVisibility(View.GONE);
        mNoDataFinal.setVisibility(View.GONE);
        mProgressLoadingFinal.setVisibility(View.GONE);

    }

    private void showNodataFinal() {
        mDataFinal.setVisibility(View.GONE);
        mNoDataFinal.setVisibility(View.VISIBLE);
        mSucgetionSelectedYear.setVisibility(View.GONE);
        mProgressLoadingFinal.setVisibility(View.GONE);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
