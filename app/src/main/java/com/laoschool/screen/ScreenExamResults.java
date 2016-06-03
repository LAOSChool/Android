package com.laoschool.screen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.adapter.ExamResultsByTermPagerAdapter;
import com.laoschool.adapter.ExamResultsforClassbySubjectAdapter;
import com.laoschool.adapter.ExamResultsStudentSemesterAdapter;
import com.laoschool.adapter.SubjectDropDownAdapter;
import com.laoschool.entities.ExamResult;
import com.laoschool.entities.Master;
import com.laoschool.model.AsyncCallback;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;
import com.laoschool.view.ViewpagerDisableSwipeLeft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenExamResults extends Fragment implements FragmentLifecycle {
    public static final String TAG = "ScreenExamResults";
    private static FragmentManager fr;
    private ScreenExamResults screenExamResults;
    private int containerId;
    private String data;
    private static String currentRole;
    private static Context context;
    private static boolean alreadyExecuted = false;
    private static LinearLayout mContainer;
    private LinearLayout mFilter;
    private LinearLayout mData;
    private ActionBar mActionBar;
    private RelativeLayout mDataInfomation;
    private LinearLayout btnSelectedDateInput;
    private ImageView btnShowSearch;
    private LinearLayout mActionSubmitCancel;
    private TextView btnCancelInputExamResult;
    private TextView btnSubmitInputExamResult;
    private Long longDateInputExamResult;
    private boolean typeInputExam = false;

    public ScreenExamResults() {
        alreadyExecuted = false;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public interface IScreenExamResults {
        void sendData(String message);

        void gotoScreenMarkScoreStudentFromExamResults(String student);
    }

    public IScreenExamResults iScreenExamResults;
    HomeActivity activity;

    LinearLayout mFilterTerm;

    Spinner cbxTerm;
    Spinner cbxClass;
    Spinner cbxSubject;
    Button btnFilterSubmitTeacher;
    RelativeLayout mToolBox;


    TextView txtClass;
    static ViewpagerDisableSwipeLeft mViewPageStudent;
    static PagerSlidingTabStrip tabsStrip;
    static LinearLayout mExamResultsStudent;
    static ProgressBar mProgress;
    static FrameLayout mError;
    static FrameLayout mNoData;
    private ObservableRecyclerView mResultListStudentBySuject;
    private int subjectIdSeleted = 0;

    private ExamResultsforClassbySubjectAdapter resultsforClassbySubjectAdapter;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        if (currentRole == null)
            return inflater.inflate(R.layout.screen_error_application, container, false);
        else
            return _defineScreenExamResultsbyRole(inflater, container);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            currentRole = getArguments().getString(LaoSchoolShared.ROLE);
            Log.d(TAG, "-Container Id:" + containerId);
        }
        this.context = getActivity();
        this.screenExamResults = this;
        this.fr = getActivity().getSupportFragmentManager();
        activity = (HomeActivity) getActivity();
        mActionBar = activity.getSupportActionBar();
        progressDialog = new ProgressDialog(context);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //set display menu item
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPauseFragment() {
        Log.d(TAG, "onPauseFragment()");
        if (!mActionBar.isShowing())
            mActionBar.show();
        if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
            cancelInputExamResults(false);
        }

    }

    @Override
    public void onResumeFragment() {
        Log.d(TAG, "onResumeFragment()/getUserVisibleHint()=" + getUserVisibleHint() + ",alreadyExecuted=" + alreadyExecuted);
        if (!alreadyExecuted && getUserVisibleHint()) {
            if (currentRole == null) {
                Log.d(TAG, "onResumeFragment() - current role null");
                currentRole = LaoSchoolShared.ROLE_STUDENT;
            }
            if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
                fillDataForTeacher();
            } else {
                _definePageSemesterStudent();
            }

        }
    }

    @Override
    public void onAttach(Context activity) {
        Log.d(TAG, "onAttach()");
        super.onAttach(activity);
        iScreenExamResults = (IScreenExamResults) activity;
    }

    public static Fragment instantiate(int containerId, String currentRole) {
        Log.d(TAG, "instantiate()");
        ScreenExamResults fragment = new ScreenExamResults();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        args.putString(LaoSchoolShared.ROLE, currentRole);
        fragment.setArguments(args);
        return fragment;
    }

    private View _defineScreenExamResultsbyRole(LayoutInflater inflater, ViewGroup container) {
        if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
            return _defineScreenExamResultsTeacher(inflater, container);
        } else {
            return _defineScreenExamResultsDetailForStudent(inflater, container);
        }

    }

    private View _defineScreenExamResultsDetailForStudent(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.screen_exam_results_student, container, false);

        mExamResultsStudent = (LinearLayout) view.findViewById(R.id.mExamResultsStudent);
        mProgress = (ProgressBar) view.findViewById(R.id.mProgressExamResultsStudent);
        mError = (FrameLayout) view.findViewById(R.id.mError);
        mNoData = (FrameLayout) view.findViewById(R.id.mNoData);

        mViewPageStudent = (ViewpagerDisableSwipeLeft) view.findViewById(R.id.mViewPageScreenExamResultsStudent);
        tabsStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        mViewPageStudent.setAllowedSwipeDirection(HomeActivity.SwipeDirection.none);

        _handlerOnclickError();

        _handlerOnclickNodata();

        if (!alreadyExecuted && getUserVisibleHint()) {
            if (currentRole == null) {
                Log.d(TAG, "onResumeFragment() - current role null");
                currentRole = LaoSchoolShared.ROLE_STUDENT;
            }
            if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
            } else {
                _definePageSemesterStudent();
            }

        }

        return view;
    }

    private void _handlerOnclickError() {
        mError.findViewById(R.id.mReloadData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressLoading(true);
                _getMyExamResult();
            }
        });
    }

    private void _handlerOnclickNodata() {
        mNoData.findViewById(R.id.mReloadData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressLoading(true);
                _getMyExamResult();
            }
        });
    }

    private void _getExamResult() {
        LaoSchoolSingleton.getInstance().getDataAccessService().getExamResults(-1, -1, -1, new AsyncCallback<List<ExamResult>>() {
            @Override
            public void onSuccess(List<ExamResult> result) {
                if (result != null) {
                    _setDataforPageSemester(result, -1);
                } else {
                    Log.d(TAG, "_definePageSemesterStudent()/getExamResults()/onSuccess() message:NUll");
                }
                showProgressLoading(false);
            }

            @Override
            public void onFailure(String message) {
                Log.e(TAG, "_definePageSemesterStudent()/getExamResults()/onFailure() message:" + message);
                showProgressLoading(false);
                showError();
            }

            @Override
            public void onAuthFail(String message) {
                Log.e(TAG, "_definePageSemesterStudent()/getExamResults()/onAuthFail() message:" + message);
                LaoSchoolShared.goBackToLoginPage(context);
                showProgressLoading(false);
            }
        });
    }

    private void _getMyExamResult() {
        _getMyExamResult(-1);
    }

    private void _getMyExamResult(final int positon) {
        if (positon == -1)
            showProgressLoading(true);

        int filter_class_id = LaoSchoolShared.myProfile.getEclass().getId();
        LaoSchoolSingleton.getInstance().getDataAccessService().getMyExamResults(filter_class_id, new AsyncCallback<List<ExamResult>>() {
            @Override
            public void onSuccess(List<ExamResult> result) {
                if (result.size() > 0) {
                    if (result != null) {
                        _setDataforPageSemester(result, positon);
                        showProgressLoading(false);
                        alreadyExecuted = true;

                    } else {
                        Log.d(TAG, "getMyExamResults(" + positon + ")/onAuthFail() message:NUll");
                        showProgressLoading(false);
                        showNoData();
                        alreadyExecuted = true;
                    }
                } else {
                    Log.d(TAG, "getMyExamResults(" + positon + ")/onAuthFail() message:Size==0");
                    showProgressLoading(false);
                    showNoData();
                    alreadyExecuted = true;
                }

            }

            @Override
            public void onFailure(String message) {
                Log.e(TAG, "getMyExamResults(" + positon + ")/onFailure() message:" + message);
                showProgressLoading(false);
                showError();
                alreadyExecuted = true;
            }

            @Override
            public void onAuthFail(String message) {
                Log.e(TAG, "getMyExamResults(" + positon + ")/onAuthFail() message:" + message);
                LaoSchoolShared.goBackToLoginPage(context);
                showProgressLoading(false);
                alreadyExecuted = true;
            }
        });
    }


    private static void _setDataforPageSemester(List<ExamResult> result, int positon) {
        Log.d(TAG, "_setDataforPageSemester() positon=" + positon);
        HashMap<Integer, String> hashterms = new LinkedHashMap<Integer, String>();
        Map<Integer, ArrayList<ExamResult>> mapTermExam = new HashMap<>();
        for (ExamResult examResult : result) {
            int termId = examResult.getTerm_id();
            ArrayList<ExamResult> examResultList = null;
            if (examResult.getTermName() != null) {
                //Put term List
                hashterms.put(examResult.getTerm_id(), examResult.getTermName());

                //Add exam by Term
                if (mapTermExam.containsKey(termId)) {
                    examResultList = mapTermExam.get(termId);
                    if (examResultList == null) {
                        examResultList = new ArrayList<>();
                    }
                    examResultList.add(examResult);
                } else {
                    examResultList = new ArrayList<>();
                    examResultList.add(examResult);
                }

            }
            mapTermExam.put(termId, examResultList);
        }

        //difine pager
        ExamResultsByTermPagerAdapter sampleFragmentPagerAdapter = new ExamResultsByTermPagerAdapter(fr, hashterms, mapTermExam);
        mViewPageStudent.setAdapter(sampleFragmentPagerAdapter);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(mViewPageStudent);

        if (positon > -1)
            mViewPageStudent.setCurrentItem(positon);
    }

    private Map<Integer, List<ExamResult>> _hashExamResultsbyTerm(List<ExamResult> result) {

        return null;
    }

    private void _definePageSemesterStudent() {
        Log.d(TAG, "_definePageSemesterStudent()");
        _getMyExamResult();
    }

    private static void showError() {
        mError.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
        mNoData.setVisibility(View.GONE);

        if (currentRole.equals(LaoSchoolShared.ROLE_STUDENT)) {
            mExamResultsStudent.setVisibility(View.GONE);
        }
        if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
            mContainer.setVisibility(View.GONE);
        }
    }

    private static void showNoData() {
        mNoData.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
        mProgress.setVisibility(View.GONE);

        if (currentRole.equals(LaoSchoolShared.ROLE_STUDENT)) {
            mExamResultsStudent.setVisibility(View.GONE);
        }
        if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
            mContainer.setVisibility(View.GONE);
        }


    }


    public static void showProgressLoading(boolean show) {
        try {
            if (show) {
                mProgress.setVisibility(View.VISIBLE);
                if (currentRole.equals(LaoSchoolShared.ROLE_STUDENT)) {
                    mExamResultsStudent.setVisibility(View.GONE);
                }
                if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
                    mContainer.setVisibility(View.GONE);
                }
            } else {
                mProgress.setVisibility(View.GONE);
                if (currentRole.equals(LaoSchoolShared.ROLE_STUDENT))
                    mExamResultsStudent.setVisibility(View.GONE);
                if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
                    mContainer.setVisibility(View.VISIBLE);
                }
            }
            mError.setVisibility(View.GONE);
            mNoData.setVisibility(View.GONE);
        } catch (Exception e) {
        }
    }

    private View _defineScreenExamResultsTeacher(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.screen_exam_results_tearcher, container, false);
        //difine container
        mContainer = (LinearLayout) view.findViewById(R.id.mContainer);
        mFilter = (LinearLayout) view.findViewById(R.id.mFilter);
        mData = (LinearLayout) view.findViewById(R.id.mData);
        mFilterTerm = (LinearLayout) view.findViewById(R.id.mFitlerTerm);
        mDataInfomation = (RelativeLayout) view.findViewById(R.id.mDataInfomation);
        mDataInfomation.setVisibility(View.GONE);
        cbxTerm = (Spinner) view.findViewById(R.id.cbxTerm);
        cbxClass = (Spinner) view.findViewById(R.id.cbxClass);
        cbxSubject = (Spinner) view.findViewById(R.id.cbxSubject);

        //difine progee,erorr,nodata
        mProgress = (ProgressBar) view.findViewById(R.id.mProgress);
        mError = (FrameLayout) view.findViewById(R.id.mError);
        mNoData = (FrameLayout) view.findViewById(R.id.mNoData);


        btnFilterSubmitTeacher = (Button) view.findViewById(R.id.btnFilterSubmit);
        txtClass = (TextView) view.findViewById(R.id.lbTotalTerm);

        //define toolbox
        mToolBox = (RelativeLayout) view.findViewById(R.id.mToolBox);
        btnShowSearch = (ImageView) view.findViewById(R.id.btnShowSearch);
        SearchView searchStudent = (SearchView) view.findViewById(R.id.mSearchExamResults);
        btnSelectedDateInput = (LinearLayout) view.findViewById(R.id.btnMarkScoreAll);
        mActionSubmitCancel = (LinearLayout) view.findViewById(R.id.mActionSubmitCancel);
        btnCancelInputExamResult = (TextView) view.findViewById(R.id.btnCancelInputExamResult);
        btnSubmitInputExamResult = (TextView) view.findViewById(R.id.btnSubmitInputExamResult);
        handlerSubmitInputExam();

        searchStudent.setQueryHint(getString(R.string.hint_search_exam_resutls));
        searchStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFilterShowData();
            }
        });

        //set search plate color..
        ViewGroup search_plate = (ViewGroup) searchStudent.findViewById(R.id.search_plate);
        if (search_plate != null) {
            search_plate.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        mResultListStudentBySuject = (ObservableRecyclerView) view.findViewById(R.id.mRecylerViewExamResults);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        mResultListStudentBySuject.setLayoutManager(gridLayoutManager);

        //Handler on submit filter
//        handlerSubmitFilterMarkScoreForTeacher();

        if (!alreadyExecuted && getUserVisibleHint()) {
            if (currentRole == null) {
                Log.d(TAG, "onResumeFragment() - current role null");
                currentRole = LaoSchoolShared.ROLE_STUDENT;
            }
            if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
                fillDataForTeacher();
            }

        }
        return view;
    }

    private void fillDataForTeacher() {
        List<String> classTest = Arrays.asList(LaoSchoolShared.myProfile.getEclass().getTitle());
        _fillDataForSpinerFilter(cbxClass, classTest);
        cbxClass.setEnabled(false);
        getFilterSubject();
    }

    private void handlerSubmitInputExam() {
        View.OnClickListener actionSubmitInput = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaoSchoolShared.hideSoftKeyboard(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.title_msg_comfirm_submit_input_exam_results);
                builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        cancelInputExamResults(true);
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (resultsforClassbySubjectAdapter.getInputExamResults().size() > 0) {
                            Log.d(TAG, "-input type exam size:" + resultsforClassbySubjectAdapter.getInputExamResults().size());
                            List<ExamResult> examResults = resultsforClassbySubjectAdapter.getInputExamResults();
                            inputExamResults(examResults);
                        }
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        };
        View.OnClickListener actionCancelInput = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaoSchoolShared.hideSoftKeyboard(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.title_msg_comfirm_submit_input_exam_results);
                builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cancelInputExamResults(true);
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        };

        btnSubmitInputExamResult.setOnClickListener(actionSubmitInput);
        btnCancelInputExamResult.setOnClickListener(actionCancelInput);
    }

    private void cancelInputExamResults(final boolean show) {
        if (show)
            progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mActionSubmitCancel.setVisibility(View.GONE);
                btnSelectedDateInput.setVisibility(View.VISIBLE);
                btnShowSearch.setVisibility(View.VISIBLE);
                resultsforClassbySubjectAdapter.setTYPE_DIPSLAY(ExamResultsforClassbySubjectAdapter.DISPLAY_LIST_0);
                typeInputExam = false;
                LaoSchoolShared.hideSoftKeyboard(getActivity());
                if (show)
                    progressDialog.dismiss();

            }
        }, LaoSchoolShared.LOADING_TIME);

    }

    private void inputExamResults(final List<ExamResult> examResults) {
        int monthEx = 0;
        int yearEx = 0;
        int teacherId = LaoSchoolShared.myProfile.getId();
        if (longDateInputExamResult != null) {
            monthEx = LaoSchoolShared.getMonth(longDateInputExamResult);
            yearEx = LaoSchoolShared.getYear(longDateInputExamResult);
        }
        Log.d(TAG, "inputExamResults() -month:" + monthEx + ",year:" + yearEx);
        progressDialog.show();
        if (monthEx > 0 && yearEx > 0) {
            final int size = examResults.size();
            for (int i = 0; i < size; i++) {
                final ExamResult examResult = examResults.get(i);
                examResult.setExam_month(monthEx);
                examResult.setTeacher_id(teacherId);
                examResult.setExam_year(yearEx);

                Log.d(TAG, "inputExamResults() -exam:" + examResult.toString());

                callInputExamResults(size, i, examResult);
            }
        } else {
            Log.d(TAG, "inputExamResults() -parse date error.");
        }

    }

    private void callInputExamResults(final int size, final int index, ExamResult examResult) {
        LaoSchoolSingleton.getInstance().getDataAccessService().inputExamResults(examResult, new AsyncCallback<ExamResult>() {
            @Override
            public void onSuccess(ExamResult result) {
                if (index == (size - 1)) {
                    Log.d(TAG, "callInputExamResults().onSuccess() -input " + size + " ok");
                    finishInputExamResults();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "callInputExamResults().onFailure() -message" + message);
                progressDialog.dismiss();
            }

            @Override
            public void onAuthFail(String message) {
                Log.d(TAG, "callInputExamResults().onAuthFail() -message" + message);
                progressDialog.dismiss();
            }
        });
    }

    private void finishInputExamResults() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mActionSubmitCancel.setVisibility(View.GONE);
                btnSelectedDateInput.setVisibility(View.VISIBLE);
                btnShowSearch.setVisibility(View.VISIBLE);
                resultsforClassbySubjectAdapter.setTYPE_DIPSLAY(ExamResultsforClassbySubjectAdapter.DISPLAY_LIST_0);
                LaoSchoolShared.hideSoftKeyboard(getActivity());
                typeInputExam = false;
                getExamResultsbySubject(true, subjectIdSeleted);
            }
        }, LaoSchoolShared.LOADING_TIME);


    }

    //get subject for filter mard score
    private void getFilterSubject() {
        Log.d(TAG, "getFilterSubject()");
        showProgressLoading(true);
        LaoSchoolSingleton.getInstance().getDataAccessService().getMasterTablebyName("m_subject", new AsyncCallback<List<Master>>() {
            @Override
            public void onSuccess(List<Master> result) {
                Log.d(TAG, "getFilterSubject().onSuccess() -results size:" + result.size());
                SubjectDropDownAdapter subjectDropDownAdapter = new SubjectDropDownAdapter(context, android.R.layout.simple_spinner_item, result);
                cbxSubject.setAdapter(subjectDropDownAdapter);
                handlerSelectedSubject();
                showProgressLoading(false);
            }


            @Override
            public void onFailure(String message) {
                Log.e(ExamResultsByTermPagerAdapter.TAG, "getFilterSubject().onFailure() -message:" + message);
            }

            @Override
            public void onAuthFail(String message) {
                Log.e(ExamResultsByTermPagerAdapter.TAG, "getFilterSubject().onAuthFail() -message:" + message);
                LaoSchoolShared.goBackToLoginPage(context);
            }

        });

    }


    private void handlerSelectedSubject() {
        Log.d(TAG, "handlerSelectedSubject()");
        cbxSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "handlerSelectedSubject().onItemSelected()");
                subjectIdSeleted = view.getId();
                Master subjects = (Master) cbxSubject.getSelectedItem();
                getExamResultsbySubject(true, subjectIdSeleted);
                fillInformationClass(subjects.getSval());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void handlerSelectedDateInputExamResults(final Map<Long, String> groupMonth) {
        final List<String> exam_months = new ArrayList<>();
        final List<Long> exam_dates = new ArrayList<>();
        for (Long date : (new TreeMap<Long, String>(groupMonth)).keySet()) {
            String monthStr = groupMonth.get(date);
            exam_months.add(monthStr);
            exam_dates.add(date);
        }
        exam_months.remove(exam_months.size() - 1);
        exam_months.add("Final");
        btnSelectedDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSelectDateInputExam(exam_months, exam_dates);
            }


        });
    }

    private void showDialogSelectDateInputExam(final List<String> month, final List<Long> dates) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.title_selected_type_input_exam_results);
        builder.setItems(month.toArray(new String[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {

                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String m = month.get(i);
                        longDateInputExamResult = dates.get(i);
                        showInputExamResultsMode(m, longDateInputExamResult);
                        dialogInterface.dismiss();
                    }
                }, LaoSchoolShared.LOADING_TIME);
            }
        });
        builder.show();
    }

    private void showInputExamResultsMode(String month, Long longDateInputExamResult) {
        final TextView lbInputSubmit = (TextView) mActionSubmitCancel.findViewById(R.id.lbInputSubmit);
        hideFilterShowData();
        btnSelectedDateInput.setVisibility(View.GONE);
        btnShowSearch.setVisibility(View.GONE);
        mActionSubmitCancel.setVisibility(View.VISIBLE);
        lbInputSubmit.setText("Date input exam : " + month);
        resultsforClassbySubjectAdapter.setEditDisplay(longDateInputExamResult);
        Log.d(TAG, "showInputExamResultsMode() -date seleted:" + longDateInputExamResult);
        typeInputExam = true;
        progressDialog.dismiss();
    }


    private void fillInformationClass(String subjectName) {
        ((TextView) mDataInfomation.findViewById(R.id.lbClassName)).setText(LaoSchoolShared.myProfile.getEclass().getTitle() + " - Term:" + LaoSchoolShared.myProfile.getEclass().getTerm());
        ((TextView) mDataInfomation.findViewById(R.id.lbSubject)).setText("Subject:" + subjectName);
        mDataInfomation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeInputExam) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.title_msg_comfirm_submit_input_exam_results);
                    builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showFilterExamResults();
                            finishInputExamResults();
                            dialogInterface.dismiss();
                        }
                    });
                    builder.create().show();
                } else {
                    showFilterExamResults();
                }

            }
        });
    }


    private void getExamResultsbySubject(boolean showProgress, int subjectId) {
        Log.d(TAG, "getExamResultsbySubject()");
        if (showProgress) {
            progressDialog.show();
        }
        final ProgressDialog finalProgressDialog = progressDialog;
        LaoSchoolSingleton.getInstance().getDataAccessService().getExamResults(LaoSchoolShared.myProfile.getEclass().getId(), -1, subjectId, new AsyncCallback<List<ExamResult>>() {
            @Override
            public void onSuccess(List<ExamResult> result) {
                if (result != null) {
                    //Group data
                    Map<Integer, List<ExamResult>> groupStudentMap = groupExamResultbyStudentId(result);
                    Map<Long, String> groupMonth = groupMonth(result);

                    //
                    _fillDataForListResultFilter(mResultListStudentBySuject, groupStudentMap);
                    handlerSelectedDateInputExamResults(groupMonth);
                    handlerScroll();
                } else {
                    Log.d(TAG, "getExamResultsbySubject().onSuccess() message:NUll");
                }
                if (finalProgressDialog != null)
                    finalProgressDialog.dismiss();
            }

            @Override
            public void onFailure(String message) {
                Log.e(TAG, "getExamResultsbySubject().onFailure() message:" + message);
                if (finalProgressDialog != null)
                    finalProgressDialog.dismiss();
                showProgressLoading(false);
                showError();
            }

            @Override
            public void onAuthFail(String message) {
                Log.e(TAG, "getExamResultsbySubject().onAuthFail() message:" + message);
                if (finalProgressDialog != null)
                    finalProgressDialog.dismiss();
                LaoSchoolShared.goBackToLoginPage(context);
                showProgressLoading(false);
            }
        });
    }

    private Map<Long, String> groupMonth(List<ExamResult> result) {
        Map<Long, String> groupMonth = new HashMap<Long, String>();
        for (ExamResult examResult : result) {
            int exam_month = examResult.getExam_month();
            int exam_year = examResult.getExam_year();
            if (examResult.getTerm_id() == LaoSchoolShared.myProfile.getEclass().getTerm()) {
                long longDate = LaoSchoolShared.getLongDate(exam_month, exam_year);
                if (!groupMonth.containsKey(longDate)) {
                    groupMonth.put(longDate, LaoSchoolShared.getMonthString(exam_month));
                }
            }
        }
        return groupMonth;
    }

    private Map<Integer, List<ExamResult>> groupExamResultbyStudentId(List<ExamResult> result) {
        Map<Integer, List<ExamResult>> groupStudentMap = new HashMap<Integer, List<ExamResult>>();
        for (ExamResult examResult : result) {
            List<ExamResult> temp;
            Integer studentId = examResult.getStudent_id();
            if (groupStudentMap.containsKey(studentId)) {
                temp = groupStudentMap.get(studentId);
                temp.add(examResult);
            } else {
                temp = new ArrayList<>();
                temp.add(examResult);
            }
            groupStudentMap.put(studentId, temp);
        }
        return groupStudentMap;
    }

//    private void handlerSubmitFilterMarkScoreForTeacher() {
//        btnFilterSubmitTeacher.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "handlerSubmitFilterMarkScoreForTeacher() -subject seleted:" + subjectIdSeleted);
//                if (subjectIdSeleted > 0) {
//                    getExamResultsbySubject(false, subjectIdSeleted);
//                }
//            }
//        });
//    }


    private void _fillDataForListResultFilter(RecyclerView recyclerView, Map<Integer, List<ExamResult>> result) {
        resultsforClassbySubjectAdapter = new ExamResultsforClassbySubjectAdapter(this, result);
        recyclerView.setAdapter(resultsforClassbySubjectAdapter);
        //hide loading
        showProgressLoading(false);
    }

    private void _fillDataForSpinerFilter(Spinner cbx, List<String> classTest) {
        ArrayAdapter<String> dataAdapterclassTest = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, classTest);
        // Drop down layout style - list view with radio button
        dataAdapterclassTest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        cbx.setAdapter(dataAdapterclassTest);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.d(TAG, "setUserVisibleHint(" + isVisibleToUser + ")");
        super.setUserVisibleHint(isVisibleToUser);
    }

    public static class ExamResultsByTermPager extends Fragment {
        public static final String ARG_PAGE = "ARG_PAGE";
        private static final String ARG_LIST = "list";
        private static final String ARG_SEMESTER_ID = "semesterId";
        private static final String TAG = ExamResultsByTermPager.class.getSimpleName();
        private int position;
        ExamResultsByTermPager fragment;
        private Context context;
        private RecyclerView recyclerView;
        private ExamResultsStudentSemesterAdapter studentSemesterAdapter;
        private List<ExamResult> f_results;
        int semesterId;

        public ExamResultsByTermPager() {

        }

        public static ExamResultsByTermPager newInstance(int page, int semesterId, ArrayList<ExamResult> results) {
            Bundle args = new Bundle();
            args.putInt(ARG_PAGE, page);
            args.putParcelableArrayList(ARG_LIST, results);
            args.putInt(ARG_SEMESTER_ID, semesterId);
            ExamResultsByTermPager fragment = new ExamResultsByTermPager();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                position = getArguments().getInt(ARG_PAGE);
                f_results = getArguments().getParcelableArrayList(ARG_LIST);
                semesterId = getArguments().getInt(ARG_SEMESTER_ID);
            }

            this.fragment = this;
            this.context = getActivity();
        }

        // Inflate the fragment layout we defined above for this fragment
        // Set the associated text for the title
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
            Log.d(ExamResultsByTermPagerAdapter.TAG, TAG + ".onCreateView() position: " + position);
            View view = inflater.inflate(R.layout.view_exam_resluts_student_tab, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerViewExamResultsStudentTab);
            final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);

            studentSemesterAdapter = new ExamResultsStudentSemesterAdapter(fragment, f_results);
            recyclerView.setAdapter(studentSemesterAdapter);

            //handler Scroll list and swipe refersh
            _handlerScrollList(mSwipeRefreshLayout);
            _handlerSwipeRefesh(mSwipeRefreshLayout);
            return view;
        }

        private void _handlerScrollList(final SwipeRefreshLayout mSwipeRefreshLayout) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    int topRowVerticalPosition =
                            (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                    mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);

                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });

        }


        private void _handlerSwipeRefesh(final SwipeRefreshLayout mSwipeRefreshLayout) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getMyExamResult(position);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }

        private void getMyExamResult(final int position) {
            int filter_class_id = LaoSchoolShared.myProfile.getEclass().getId();
            LaoSchoolSingleton.getInstance().getDataAccessService().getMyExamResults(filter_class_id, new AsyncCallback<List<ExamResult>>() {
                @Override
                public void onSuccess(List<ExamResult> result) {
                    if (result.size() > 0) {
                        if (result != null) {
                            _refeshData(result);
                            showProgressLoading(false);
                            alreadyExecuted = true;

                        } else {
                            Log.d(ExamResultsByTermPagerAdapter.TAG, TAG + ".getMyExamResult(" + position + ").onAuthFail() message:NUll");
                            showProgressLoading(false);
                            showNoData();
                            alreadyExecuted = true;
                        }
                    } else {
                        Log.d(ExamResultsByTermPagerAdapter.TAG, TAG + ".getMyExamResult(" + position + ").onAuthFail() message:Size==0");
                        showProgressLoading(false);
                        showNoData();
                        alreadyExecuted = true;
                    }

                }

                @Override
                public void onFailure(String message) {
                    Log.e(ExamResultsByTermPagerAdapter.TAG, TAG + ".getMyExamResult(" + position + ").onFailure() message:" + message);
                    showProgressLoading(false);
                    showError();
                    alreadyExecuted = true;
                }

                @Override
                public void onAuthFail(String message) {
                    Log.e(ExamResultsByTermPagerAdapter.TAG, TAG + "._getMyExamResult(" + position + ").onAuthFail() message:" + message);
                    LaoSchoolShared.goBackToLoginPage(context);
                    showProgressLoading(false);
                    alreadyExecuted = true;
                }
            });
        }

        private void _refeshData(List<ExamResult> result) {
            recyclerView.swapAdapter(new ExamResultsStudentSemesterAdapter(this, hashDatafromSemestes(result)), true);

        }

        private ArrayList<ExamResult> hashDatafromSemestes(List<ExamResult> result) {
            ArrayList<ExamResult> examResults = new ArrayList<>();
            for (ExamResult examResult : result) {
                if (examResult.getTerm_id() == semesterId) {
                    examResults.add(examResult);
                }
            }
            Log.d(TAG, "hashDatafromSemestes() -semesterId=" + semesterId + " ,size=" + examResults.size());
            return examResults;
        }
    }

    private void handlerScroll() {
        mResultListStudentBySuject.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
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
                        if (mActionBar.isShowing()) {
                            hideFilterShowData();
                        }
                    } else if (scrollState == ScrollState.DOWN) {
//                        if (!mActionBar.isShowing()) {
//                            mActionBar.show();
//                            mFilter.setVisibility(View.VISIBLE);
//                            mDataInfomation.setVisibility(View.GONE);
//                            activity.showBottomBar();
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                Window window = getActivity().getWindow();
//                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
//                            }
//                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "handlerScroll().onUpOrCancelMotionEvent() -exception : " + e.getMessage());
                }
            }
        });

    }

    private void hideFilterShowData() {
        mActionBar.hide();
        mFilter.setVisibility(View.GONE);
        mDataInfomation.setVisibility(View.VISIBLE);
        activity.hideBottomBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPriorityHigh));
        }
    }

    private void showFilterExamResults() {
        mActionBar.show();
        mFilter.setVisibility(View.VISIBLE);
        mDataInfomation.setVisibility(View.GONE);
        activity.showBottomBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

}
