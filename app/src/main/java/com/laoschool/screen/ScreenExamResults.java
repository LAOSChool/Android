package com.laoschool.screen;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.adapter.ExamResultsByTermPagerAdapter;
import com.laoschool.adapter.RecylerViewScreenExamResultsAdapter;
import com.laoschool.adapter.ExamResultsStudentSemesterAdapter;
import com.laoschool.entities.ExamResult;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenExamResults extends Fragment implements FragmentLifecycle {
    public static final String TAG = "ScreenExamResults";
    private static FragmentManager fr;
    private ScreenExamResults screenExamResults;
    private int containerId;
    private String data;
    private String currentRole;
    private static Context context;
    private static boolean alreadyExecuted = false;

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

    //
    RelativeLayout mFillerDescription;
    LinearLayout mFilterTerm;

    Spinner cbxTerm;
    Spinner cbxClass;
    Spinner cbxSubject;

    TextView txtTerm;
    TextView txtSubject;
    TextView txtClass;
    static ViewpagerDisableSwipeLeft mViewPageStudent;
    static PagerSlidingTabStrip tabsStrip;
    static LinearLayout mExamResultsStudent;
    static ProgressBar mProgressStudent;
    static FrameLayout mError;
    static FrameLayout mNoData;


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
        mProgressStudent = (ProgressBar) view.findViewById(R.id.mProgressExamResultsStudent);
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
                _showProgressLoadingStudent(true);
                _getMyExamResult();
            }
        });
    }

    private void _handlerOnclickNodata() {
        mNoData.findViewById(R.id.mReloadData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _showProgressLoadingStudent(true);
                _getMyExamResult();
            }
        });
    }

    private void _getExamResult() {
        LaoSchoolSingleton.getInstance().getDataAccessService().getExamResults(-1, -1, new AsyncCallback<List<ExamResult>>() {
            @Override
            public void onSuccess(List<ExamResult> result) {
                if (result != null) {
                    _setDataforPageSemester(result, -1);
                } else {
                    Log.d(TAG, "_definePageSemesterStudent()/getExamResults()/onSuccess() message:NUll");
                }
                _showProgressLoadingStudent(false);
            }

            @Override
            public void onFailure(String message) {
                Log.e(TAG, "_definePageSemesterStudent()/getExamResults()/onFailure() message:" + message);
                _showProgressLoadingStudent(false);
                _showErrorStudent();
            }

            @Override
            public void onAuthFail(String message) {
                Log.e(TAG, "_definePageSemesterStudent()/getExamResults()/onAuthFail() message:" + message);
                LaoSchoolShared.goBackToLoginPage(context);
                _showProgressLoadingStudent(false);
            }
        });
    }

    private void _getMyExamResult() {
        _getMyExamResult(-1);
    }

    private void _getMyExamResult(final int positon) {
        if (positon == -1)
            _showProgressLoadingStudent(true);

        int filter_class_id = LaoSchoolShared.myProfile.getEclass().getId();
        LaoSchoolSingleton.getInstance().getDataAccessService().getMyExamResults(filter_class_id, new AsyncCallback<List<ExamResult>>() {
            @Override
            public void onSuccess(List<ExamResult> result) {
                if (result.size() > 0) {
                    if (result != null) {
                        _setDataforPageSemester(result, positon);
                        _showProgressLoadingStudent(false);
                        alreadyExecuted = true;

                    } else {
                        Log.d(TAG, "getMyExamResults(" + positon + ")/onAuthFail() message:NUll");
                        _showProgressLoadingStudent(false);
                        _showNoDataStudent();
                        alreadyExecuted = true;
                    }
                } else {
                    Log.d(TAG, "getMyExamResults(" + positon + ")/onAuthFail() message:Size==0");
                    _showProgressLoadingStudent(false);
                    _showNoDataStudent();
                    alreadyExecuted = true;
                }

            }

            @Override
            public void onFailure(String message) {
                Log.e(TAG, "getMyExamResults(" + positon + ")/onFailure() message:" + message);
                _showProgressLoadingStudent(false);
                _showErrorStudent();
                alreadyExecuted = true;
            }

            @Override
            public void onAuthFail(String message) {
                Log.e(TAG, "getMyExamResults(" + positon + ")/onAuthFail() message:" + message);
                LaoSchoolShared.goBackToLoginPage(context);
                _showProgressLoadingStudent(false);
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

    private static void _showErrorStudent() {
        mError.setVisibility(View.VISIBLE);
        mProgressStudent.setVisibility(View.GONE);
        mExamResultsStudent.setVisibility(View.GONE);
        mNoData.setVisibility(View.GONE);
    }

    private static void _showNoDataStudent() {
        mNoData.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
        mProgressStudent.setVisibility(View.GONE);
        mExamResultsStudent.setVisibility(View.GONE);
    }


    private static void _showProgressLoadingStudent(boolean b) {
        try {
            if (b) {
                mProgressStudent.setVisibility(View.VISIBLE);
                mExamResultsStudent.setVisibility(View.GONE);
            } else {
                mProgressStudent.setVisibility(View.GONE);
                mExamResultsStudent.setVisibility(View.VISIBLE);
            }
            mError.setVisibility(View.GONE);
            mNoData.setVisibility(View.GONE);
        } catch (Exception e) {
        }
    }

    private View _defineScreenExamResultsTeacher(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.screen_exam_results_tearcher, container, false);
        view.findViewById(R.id.btnGotoMarkScore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_MARK_SCORE_STUDENT_11);
                Log.d(getString(R.string.title_screen_exam_results), tag);
                setData("Hello " + getString(R.string.title_screen_mark_score_student));
                iScreenExamResults.sendData("Hello " + getString(R.string.title_screen_mark_score_student));
            }
        });

        //Define item
        mFillerDescription = (RelativeLayout) view.findViewById(R.id.mShowFitler);
        mFilterTerm = (LinearLayout) view.findViewById(R.id.mFitlerTerm);

        cbxTerm = (Spinner) view.findViewById(R.id.cbxTerm);
        cbxClass = (Spinner) view.findViewById(R.id.cbxClass);
        cbxSubject = (Spinner) view.findViewById(R.id.cbxSubject);

        Button btnFilterSubmit = (Button) view.findViewById(R.id.btnFilterSubmit);
        SearchView searchStudent = (SearchView) view.findViewById(R.id.mSearchExamResults);
        final LinearLayout mSearch = (LinearLayout) view.findViewById(R.id.mSearch);

        txtTerm = (TextView) view.findViewById(R.id.txtTermScreenExamResults);
        txtSubject = (TextView) view.findViewById(R.id.txtSubjectScreenExamResults);
        txtClass = (TextView) view.findViewById(R.id.lbTotalTerm);

        //
        searchStudent.setIconifiedByDefault(false);
        searchStudent.setIconified(false);
        searchStudent.clearFocus();
        searchStudent.setQueryHint(getString(R.string.hint_search_exam_resutls));


        //set search plate color..
        ViewGroup search_plate = (ViewGroup) searchStudent.findViewById(R.id.search_plate);
        if (search_plate != null) {
            search_plate.setBackgroundColor(Color.parseColor("#ebebeb"));
        }

        //
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.mRecylerViewExamResults);

        //


        // Creating adapter for spinner Term
        final List<String> terms = Arrays.asList(getResources().getStringArray(R.array.termTest));
        _fillDataForSpinerFilter(cbxTerm, terms);
        cbxTerm.setSelection(1);

        //Handler on item Selected
        cbxTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    List<String> classTest = Arrays.asList(getResources().getStringArray(R.array.classTest));
                    _fillDataForSpinerFilter(cbxClass, classTest);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cbxClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    List<String> classTest = Arrays.asList(getResources().getStringArray(R.array.subjectTest));
                    _fillDataForSpinerFilter(cbxSubject, classTest);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final boolean[] flagSummit = {false};
        //Handler on submit filter
        btnFilterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _term = cbxTerm.getSelectedItem().toString();
                if (!_term.equals(getString(R.string.selected))) {
                    String _class = cbxClass.getSelectedItem().toString();
                    if (!_class.equals(getString(R.string.selected))) {
                        String _subject = cbxSubject.getSelectedItem().toString();
                        if (!_subject.equals(getString(R.string.selected))) {
                            flagSummit[0] = true;
                            Toast.makeText(context, "Term:" + _term + "-Class:" + _class + "-Subject:" + _subject, Toast.LENGTH_SHORT).show();
                            txtTerm.setText(_term);
                            txtClass.setText(_class);
                            txtSubject.setText(_subject);
                            _fillDataForListResultFilter(recyclerView);
                            mSearch.setVisibility(View.VISIBLE);
                            _showFilterDescription();
                        } else {
                            flagSummit[0] = false;
                            cbxSubject.performClick();
                        }
                    } else {
                        flagSummit[0] = false;
                        cbxClass.performClick();
                    }
                } else {
                    //Show dropdown view
                    flagSummit[0] = false;
                    cbxTerm.performClick();
                }
            }
        });
        View.OnClickListener showHideFilter = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFilterTerm.isShown()) {
                    Log.d(TAG, "Show description");
                    _showFilterDescription();
                } else {
                    Log.d(TAG, "Show filter");
                    _showFilterTerm();
                }
            }
        };
        mFillerDescription.setOnClickListener(showHideFilter);
//        txtTerm.setOnClickListener(showHideFilter);
//        txtClass.setOnClickListener(showHideFilter);
//        txtSubject.setOnClickListener(showHideFilter);
//        mFilterTerm.setOnClickListener(showHideFilter);

        return view;
    }

    public void _showFilterDescription() {
        //
        TranslateAnimation hideFilter = new TranslateAnimation(0, 0, 0, mFilterTerm.getHeight());
        hideFilter.setDuration(200);
        hideFilter.setFillAfter(true);
        TranslateAnimation showFilterDescription = new TranslateAnimation(0, 0, mFillerDescription.getHeight(), 0);
        showFilterDescription.setDuration(200);
        showFilterDescription.setFillAfter(true);

        //mFilterTerm.startAnimation(hideFilter);
        mFilterTerm.setVisibility(View.GONE);

        //mFillerDescription.startAnimation(showFilterDescription);
        mFillerDescription.setVisibility(View.VISIBLE);
        ///
        //
        txtTerm.setVisibility(View.VISIBLE);
        txtClass.setVisibility(View.VISIBLE);
        txtSubject.setVisibility(View.VISIBLE);

        cbxClass.setVisibility(View.GONE);
        cbxSubject.setVisibility(View.GONE);
        cbxTerm.setVisibility(View.GONE);
    }

    public void _showFilterTerm() {

        ///
//        TranslateAnimation showFilterDescription = new TranslateAnimation(0, 0, 0, mFillerDescription.getHeight());
//        showFilterDescription.setDuration(200);
//        showFilterDescription.setFillAfter(true);
//        mFillerDescription.startAnimation(showFilterDescription);


        //
//        TranslateAnimation hideFilter = new TranslateAnimation(0, 0, 0, mFilterTerm.getHeight());
//        hideFilter.setDuration(200);
//        hideFilter.setFillAfter(true);
//        mFilterTerm.startAnimation(hideFilter);
        mFilterTerm.setVisibility(View.VISIBLE);
        mFillerDescription.setVisibility(View.GONE);

        txtTerm.setVisibility(View.GONE);
        txtClass.setVisibility(View.GONE);
        txtSubject.setVisibility(View.GONE);

        cbxClass.setVisibility(View.VISIBLE);
        cbxSubject.setVisibility(View.VISIBLE);
        cbxTerm.setVisibility(View.VISIBLE);
    }

    private void _fillDataForListResultFilter(RecyclerView recyclerView) {
        //init adapter
        final List<String> strings = new ArrayList<String>();
        strings.add(context.getString(R.string.row_sub_header));
        strings.addAll(Arrays.asList(getResources().getStringArray(R.array.listTeacher)));
        RecylerViewScreenExamResultsAdapter adapter = new RecylerViewScreenExamResultsAdapter(this, strings);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);

        //set adapter
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
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
                            _showProgressLoadingStudent(false);
                            alreadyExecuted = true;

                        } else {
                            Log.d(ExamResultsByTermPagerAdapter.TAG, TAG + ".getMyExamResult(" + position + ").onAuthFail() message:NUll");
                            _showProgressLoadingStudent(false);
                            _showNoDataStudent();
                            alreadyExecuted = true;
                        }
                    } else {
                        Log.d(ExamResultsByTermPagerAdapter.TAG, TAG + ".getMyExamResult(" + position + ").onAuthFail() message:Size==0");
                        _showProgressLoadingStudent(false);
                        _showNoDataStudent();
                        alreadyExecuted = true;
                    }

                }

                @Override
                public void onFailure(String message) {
                    Log.e(ExamResultsByTermPagerAdapter.TAG, TAG + ".getMyExamResult(" + position + ").onFailure() message:" + message);
                    _showProgressLoadingStudent(false);
                    _showErrorStudent();
                    alreadyExecuted = true;
                }

                @Override
                public void onAuthFail(String message) {
                    Log.e(ExamResultsByTermPagerAdapter.TAG, TAG + "._getMyExamResult(" + position + ").onAuthFail() message:" + message);
                    LaoSchoolShared.goBackToLoginPage(context);
                    _showProgressLoadingStudent(false);
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
}
