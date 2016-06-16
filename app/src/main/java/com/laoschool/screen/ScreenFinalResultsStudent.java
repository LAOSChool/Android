package com.laoschool.screen;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;


import com.laoschool.adapter.FinalResultsPagerAdapter;
import com.laoschool.adapter.SelectedSchoolYearsAdapter;
import com.laoschool.entities.ExamResult;
import com.laoschool.entities.FinalResult;
import com.laoschool.entities.Master;
import com.laoschool.entities.SchoolYears;
import com.laoschool.model.AsyncCallback;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;
import com.laoschool.view.ScrollableViewPager;


import java.util.ArrayList;
import java.util.List;

import it.carlom.stikkyheader.core.StikkyHeaderBuilder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenFinalResultsStudent extends Fragment implements FragmentLifecycle {


    private static final String TAG = ScreenFinalResultsStudent.class.getSimpleName();
    private Context context;
    private String currentRole;
    private int containerId;
    private RelativeLayout mFilterYear;
    TextView lbSelectedSchoolYear;


    private ProgressBar mProgress;
    private FrameLayout mError;
    private View mNoDataFinal;

    private LinearLayout mListData;

    private ScreenFinalResultsStudent fragment;
    private View mContainer;
    private ActionBar mActionBar;
    ScrollableViewPager mPagerFinalResults;
    PagerSlidingTabStrip mTab;
    private SearchView mSearch;
    private List<SchoolYears> schoolYears;
    private View mDataFinal;
    private View mSucgestionSelectedYear;
    private View mProgressLoadingFinal;
    ScrollView mScroll;
    private Dialog dialogSelectedYear;

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
            //error,process of screen
            mProgress = (ProgressBar) view.findViewById(R.id.mProgressFinalResults);
            mError = (FrameLayout) view.findViewById(R.id.mError);

            //Filter year
            mFilterYear = (RelativeLayout) view.findViewById(R.id.mListBox);
            lbSelectedSchoolYear = (TextView) view.findViewById(R.id.lbSelectedSchoolYear);

            //Filter content
            mScroll = (ScrollView) view.findViewById(R.id.mScroll);

            //Header infor final results
            mDataFinal = view.findViewById(R.id.mDataFinal);

            //no data ,sucgetion,loading final
            mNoDataFinal = view.findViewById(R.id.mNoDataFinal);
            mSucgestionSelectedYear = view.findViewById(R.id.mSucgetionSelectedYear);
            mProgressLoadingFinal = view.findViewById(R.id.mProgressLoadingFinal);

            //Pager
            mPagerFinalResults = (ScrollableViewPager) view.findViewById(R.id.mPagerFinalResults);
            mTab = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);

            //Fill data
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StikkyHeaderBuilder.stickTo(mScroll)
                .setHeader(R.id.header, (FrameLayout) getView())
                .minHeightHeader(85)
                .build();

    }

    private void defineAvgFinalTotal(FinalResult result) {
        ((TextView) mDataFinal.findViewById(R.id.lbClassNameAndLocation)).setText(result.getClassName() + " | " + result.getCls_location());
        ((TextView) mDataFinal.findViewById(R.id.lbTeacherName)).setText(result.getTeacher_name());
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

            ((TextView) mDataFinal.findViewById(R.id.lbAvgTerm1)).setText(String.valueOf(avgTerm1));
            ((TextView) mDataFinal.findViewById(R.id.lbAvgTerm2)).setText(String.valueOf(avgTerm2));
            ((TextView) mDataFinal.findViewById(R.id.lbAvgofYear)).setText(String.valueOf(total));
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

        List<String> yearNames = new ArrayList<>();
        for (SchoolYears year : result) {
            yearNames.add(year.getYears());
        }

        dialogSelectedYear = makeDialogSelectdYear(result, yearNames);
        mFilterYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSelectedYear.show();
            }
        });
        this.schoolYears = schoolYearsList;

        mSucgestionSelectedYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFilterYear.performClick();
            }
        });
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
        mContainer.setVisibility(View.GONE);
    }

    private void showProgressLoading(boolean b) {
        try {
            if (b) {
                mProgress.setVisibility(View.VISIBLE);
                mContainer.setVisibility(View.GONE);
            } else {
                mProgress.setVisibility(View.GONE);
                mContainer.setVisibility(View.VISIBLE);
            }
            mError.setVisibility(View.GONE);
        } catch (Exception e) {
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
            mProgressLoadingFinal.setVisibility(View.VISIBLE);
            mSucgestionSelectedYear.setVisibility(View.GONE);
            mDataFinal.setVisibility(View.GONE);
            mNoDataFinal.setVisibility(View.GONE);
        } else {
            mDataFinal.setVisibility(View.VISIBLE);
            mSucgestionSelectedYear.setVisibility(View.GONE);
            mNoDataFinal.setVisibility(View.GONE);
            mProgressLoadingFinal.setVisibility(View.GONE);
        }
    }

    private void showSugesstionFinal() {
        mSucgestionSelectedYear.setVisibility(View.VISIBLE);
        mDataFinal.setVisibility(View.GONE);
        mNoDataFinal.setVisibility(View.GONE);
        mProgressLoadingFinal.setVisibility(View.GONE);

    }

    private void showNodataFinal() {
        mNoDataFinal.setVisibility(View.VISIBLE);
        mSucgestionSelectedYear.setVisibility(View.GONE);
        mProgressLoadingFinal.setVisibility(View.GONE);

    }


    private Dialog makeDialogSelectdYear(final List<SchoolYears> result, final List<String> yearsNames) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.title_selected_type_input_exam_results);
        View header = View.inflate(context, R.layout.custom_hearder_dialog, null);
        ImageView imgIcon = ((ImageView) header.findViewById(R.id.imgIcon));
        Drawable drawable = LaoSchoolShared.getDraweble(context, R.drawable.ic_timer_black_24dp);
        int color = Color.parseColor("#ffffff");
        drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        imgIcon.setImageDrawable(drawable);

        ((TextView) header.findViewById(R.id.txbTitleDialog)).setText(R.string.selected_year);

        builder.setCustomTitle(header);
        final ListAdapter subjectListAdapter = new ArrayAdapter<>(context, R.layout.row_selected_subject, yearsNames);

        builder.setAdapter(subjectListAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {
                String yearName = yearsNames.get(i);
                int yearId = result.get(i).getId();
                lbSelectedSchoolYear.setText(yearName);
                getMyFinalResultsByYear(yearId);

            }
        });
        final AlertDialog dialog = builder.create();
        (header.findViewById(R.id.imgCloseDialog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return dialog;
    }
}
