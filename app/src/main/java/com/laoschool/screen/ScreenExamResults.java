package com.laoschool.screen;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.adapter.ExamResultsAdapter;
import com.laoschool.adapter.MyExamResultsAdapter;
import com.laoschool.adapter.MyExamResultsPagerAdapter;
import com.laoschool.entities.ExamResult;
import com.laoschool.entities.Master;
import com.laoschool.listener.AppBarStateChangeListener;
import com.laoschool.model.AsyncCallback;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;
import com.laoschool.view.ViewpagerDisableSwipeLeft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenExamResults extends Fragment
        implements FragmentLifecycle {
    public static final String TAG = ScreenExamResults.class.getSimpleName();
    private static FragmentManager fr;
    private ScreenExamResults screenExamResults;
    private int containerId;
    private String data;
    private static String currentRole;
    private static Context context;
    private static boolean alreadyExecuted = false;
    private static LinearLayout mContainer;
    private LinearLayout mFilter;
    private ActionBar mActionBar;
    private ImageView btnShowSearch;
    private TextView lbSubjectSeleted;
    private View mSelectedSubject;
    private TextView txtClassAndTermName;
    public List<Master> listSubject;
    public int selectedSubjectId = -1;
    Map<Integer, String> mapSubject;
    private View mSugesstionSelectedSubject;
    private MenuItem itemSearch;
    private SearchView mSearch;
    private ExamResultsAdapter examResultsAdapter;
    private SearchView mSearchExamResults;
    private AppBarLayout appFilterBar;
    private CollapsingToolbarLayout collapsing;
    private CoordinatorLayout.Behavior<AppBarLayout> behavior;
    private View mListExam;
    private View mExpanSearch;
    //   private EditText txtSearch;

    private AppBarStateChangeListener.State appBarState = AppBarStateChangeListener.State.EXPANDED;


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
        void gotoScreenInputExamResults();
    }

    public IScreenExamResults iScreenExamResults;
    HomeActivity activity;


    static ViewpagerDisableSwipeLeft mViewPageStudent;
    static PagerSlidingTabStrip tabsStrip;
    static LinearLayout mExamResultsStudent;
    static View mProgress;
    static FrameLayout mError;
    static FrameLayout mNoData;
    private ObservableRecyclerView mResultListStudentBySuject;
    private int subjectIdSeleted = 0;
    private Dialog dialogSelectdSubject;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_input_exam_results:
                iScreenExamResults.gotoScreenInputExamResults();
                return true;

            case R.id.search:
                onSearchClick();

        }
        return super.onOptionsItemSelected(item);
    }

    private void onSearchClick() {
        if (selectedSubjectId == -1) {
            if (dialogSelectdSubject != null) {
                dialogSelectdSubject.show();
            } else {
                Log.d(TAG, "dialog null");
            }
        }
        setExpandedFilterBar(false, true);

        activity.hideBottomBar();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        try {
            if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
                menu.clear();
                inflater.inflate(R.menu.menu_screen_exam_results_teacher, menu);
                itemSearch = menu.findItem(R.id.search);
                mSearchExamResults = (SearchView) itemSearch.getActionView();
                mSearchExamResults.setQueryHint(context.getString(R.string.SCCommon_Search));
                onExpanCollapseSearch();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onExpanCollapseSearch() {
        MenuItemCompat.setOnActionExpandListener(itemSearch, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                itemSearch.setVisible(true);
                setExpandedAppBar(false, true);
                mExpanSearch.setVisibility(View.GONE);

                ((HomeActivity) getActivity()).displaySearch();
                ((HomeActivity) getActivity()).hideBottomBar();

                expanSearch();
                return true;  // Return true to expand action view
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                itemSearch.setVisible(false);
                setExpandedAppBar(true, true);
                mExpanSearch.setVisibility(View.VISIBLE);

                ((HomeActivity) getActivity()).cancelSearch();
                ((HomeActivity) getActivity()).showBottomBar();
                return true;  // Return true to collapse action view
            }


        });
    }

    private void expanSearch() {
        mSearchExamResults.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.d(TAG, "query:" + query);
                if (examResultsAdapter != null) {
                    examResultsAdapter.filter(query);
                }
                return true;
            }
        });
    }

    @Override
    public void onPauseFragment() {
        Log.d(TAG, "onPauseFragment()");
        if (!mActionBar.isShowing())
            mActionBar.show();

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
            } else _definePageSemesterStudent();

        }
        reloadDatAfterInputSucessfuly();
    }

    private void reloadDatAfterInputSucessfuly() {
        if (getUserVisibleHint()) {
            String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_MARK_SCORE_STUDENT_11);
            ScreenInputExamResultsStudent inputExamResultsStudent = (ScreenInputExamResultsStudent) getFragmentManager().findFragmentByTag(tag);
            if (inputExamResultsStudent != null)
                if (inputExamResultsStudent.onchange) {
                    int subjectId = inputExamResultsStudent.selectedSubjectId;
                    String subjectName = mapSubject.get(subjectId);
                    lbSubjectSeleted.setText(subjectName);
                    lbSubjectSeleted.setTextColor(Color.WHITE);
                    getExamResultsbySubject(true, subjectId);
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
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);

        mExamResultsStudent = (LinearLayout) view.findViewById(R.id.mExamResultsStudent);
        mProgress = view.findViewById(R.id.mProgressExamResultsStudent);
        mError = (FrameLayout) view.findViewById(R.id.mError);
        mNoData = (FrameLayout) view.findViewById(R.id.mNoData);

        mViewPageStudent = (ViewpagerDisableSwipeLeft) view.findViewById(R.id.mViewPageScreenExamResultsStudent);
        tabsStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        mViewPageStudent.setAllowedSwipeDirection(HomeActivity.SwipeDirection.all);

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
                        Log.d(TAG, "getMyExamResults(" + positon + ")/onSuccess() -results size:" + result.size());
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
        Log.d(TAG, "_setDataforPageSemester() -term size:=" + hashterms.size() + ",mapTermExam:" + mapTermExam.size());
        //difine pager
        MyExamResultsPagerAdapter myExamResultsAdapter = new MyExamResultsPagerAdapter(fr, context, result);
        mViewPageStudent.setAdapter(myExamResultsAdapter);

        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(mViewPageStudent);

        if (positon > -1)
            mViewPageStudent.setCurrentItem(positon);
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
                    mExamResultsStudent.setVisibility(View.VISIBLE);
                if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
                    mContainer.setVisibility(View.VISIBLE);
                }
            }
            mError.setVisibility(View.GONE);
            mNoData.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View _defineScreenExamResultsTeacher(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.screen_exam_results_tearcher, container, false);
        //difine container
        appFilterBar = (AppBarLayout) view.findViewById(R.id.examresuts_appbar);
        mContainer = (LinearLayout) view.findViewById(R.id.mContainer);
        mFilter = (LinearLayout) view.findViewById(R.id.mFilter);
        lbSubjectSeleted = (TextView) view.findViewById(R.id.lbSubjectSeleted);
        mSelectedSubject = view.findViewById(R.id.mSelectedSubject);
        //difine progee,erorr,nodata
        mProgress = view.findViewById(R.id.mProgress);
        mError = (FrameLayout) view.findViewById(R.id.mError);
        mNoData = (FrameLayout) view.findViewById(R.id.mNoData);

        //define seach box
        mExpanSearch = view.findViewById(R.id.mSearchBox);
//        txtSearch = (EditText) view.findViewById(R.id.txtSearch);

        //clear focus search
        // txtSearch.clearFocus();


        txtClassAndTermName = (TextView) view.findViewById(R.id.txtClassAndTermName);
        mSugesstionSelectedSubject = view.findViewById(R.id.mSugesstionSelectedSubject);
        mListExam = view.findViewById(R.id.mListExam);
        mResultListStudentBySuject = (ObservableRecyclerView) view.findViewById(R.id.mListExamResults);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        mResultListStudentBySuject.setLayoutManager(gridLayoutManager);


        //handler app bar
        onAppBarExpanded();
        //focus and collapse appbar
        onOnFocusChangeBoxSearch();

        onExpanSearch();
        return view;
    }

    private void onExpanSearch() {
        mExpanSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuItemCompat.expandActionView(itemSearch);
                itemSearch.setVisible(true);
            }
        });
    }

    private void onAppBarExpanded() {
        appFilterBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                appBarState = state;
            }
        });
    }

    private void onOnFocusChangeBoxSearch() {
//        txtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                Log.d(TAG, "onFocusChange:" + hasFocus);
//                if (hasFocus) {
//                    if (appBarState == AppBarStateChangeListener.State.EXPANDED || appBarState == AppBarStateChangeListener.State.IDLE) {
//                        setExpandedAppBar(false, true);
//                    }
//                }
//
//
//            }
//        });

    }

    private void setExpandedFilterBar(boolean expanded, boolean animate) {
        appFilterBar.setExpanded(expanded, animate);
    }

    private void fillDataForTeacher() {
        String className = LaoSchoolShared.myProfile.getEclass().getTitle();
        String termName = String.valueOf(context.getString(R.string.SCCommon_Term) + " " + LaoSchoolShared.myProfile.getEclass().getTerm());
        String year = String.valueOf(LaoSchoolShared.myProfile.getEclass().getYears());

        txtClassAndTermName.setText(className + " | " + year + "   " + termName);
        getFilterSubject();
    }


    //get subject for filter mard score
    private void getFilterSubject() {
        Log.d(TAG, "getFilterSubject()");
        showProgressLoading(true);
        int classId = LaoSchoolShared.myProfile.getEclass().getId();
        LaoSchoolSingleton.getInstance().getDataAccessService().getListSubjectbyClassId(classId, new AsyncCallback<List<Master>>() {
            @Override
            public void onSuccess(List<Master> result) {
                listSubject = result;
                Log.d(TAG, "getFilterSubject().onSuccess() -results size:" + result.size());
//                int subjectId = result.get(0).getId();
//                selectedSubjectId = subjectId;
//                String subjectName = result.get(0).getSval();
//                getExamResultsbySubject(false, subjectId);
//                lbSubjectSeleted.setText(subjectName);
//                fillInformationClass(subjectName);
                handlerSelectedSubject(result);
                alreadyExecuted = true;
                showProgressLoading(false);
            }


            @Override
            public void onFailure(String message) {
                Log.e(TAG, "getFilterSubject().onFailure() -message:" + message);
                showError();
            }

            @Override
            public void onAuthFail(String message) {
                Log.e(TAG, "getFilterSubject().onAuthFail() -message:" + message);
                LaoSchoolShared.goBackToLoginPage(context);
            }

        });

        mError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedSubjectId > -1) {
                    getExamResultsbySubject(true, selectedSubjectId);
                } else {
                    getFilterSubject();
                }
            }
        });
        mNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedSubjectId > -1) {
                    getExamResultsbySubject(true, selectedSubjectId);
                } else {
                    getFilterSubject();
                }
            }
        });

    }


    private void handlerSelectedSubject(final List<Master> result) {
        Log.d(TAG, "handlerSelectedSubject()");
        final List<String> subjectNames = new ArrayList<>();
        mapSubject = new HashMap<>();
        for (Master master : result) {
            subjectNames.add(master.getSval());
            mapSubject.put(master.getId(), master.getSval());

        }
        dialogSelectdSubject = makeDialogSelectdSubject(result, subjectNames);
        mSelectedSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSelectdSubject.show();
            }
        });
        mSugesstionSelectedSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSelectdSubject.show();
            }
        });

    }

    private Dialog makeDialogSelectdSubject(final List<Master> result, final List<String> subjectNames) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.title_selected_type_input_exam_results);
        View header = View.inflate(context, R.layout.custom_hearder_dialog, null);
        ImageView imgIcon = ((ImageView) header.findViewById(R.id.imgIcon));
        Drawable drawable = LaoSchoolShared.getDraweble(context, R.drawable.ic_library_books_black_24dp);
        int color = Color.parseColor("#ffffff");
        drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        imgIcon.setImageDrawable(drawable);

        ((TextView) header.findViewById(R.id.txbTitleDialog)).setText(R.string.SCExamResults_SelectSubject);


        builder.setCustomTitle(header);
        final ListAdapter subjectListAdapter = new ArrayAdapter<String>(context, R.layout.row_selected_subject, subjectNames);

        builder.setAdapter(subjectListAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {
                String subjectName = subjectNames.get(i);
                int subjectId = result.get(i).getId();
                selectedSubjectId = subjectId;
                lbSubjectSeleted.setText(subjectName);
                lbSubjectSeleted.setTextColor(Color.WHITE);
                getExamResultsbySubject(true, subjectId);
            }
        });
        final AlertDialog dialog = builder.create();
        ((ImageView) header.findViewById(R.id.imgCloseDialog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return dialog;
    }


    private void getExamResultsbySubject(boolean showProgress, final int subjectId) {
        Log.d(TAG, "getExamResultsbySubject()");
        if (showProgress) {
            progressDialog.show();
        }
        final ProgressDialog finalProgressDialog = progressDialog;
        LaoSchoolSingleton.getInstance().getDataAccessService().getExamResultsforMark(LaoSchoolShared.myProfile.getEclass().getId(), -1, subjectId, new AsyncCallback<List<ExamResult>>() {
            @Override
            public void onSuccess(List<ExamResult> result) {
                mSugesstionSelectedSubject.setVisibility(View.GONE);
                mResultListStudentBySuject.setVisibility(View.VISIBLE);
                mListExam.setVisibility(View.VISIBLE);
                if (result != null) {
                    //Group data
                    // Map<Integer, List<ExamResult>> groupStudentMap = groupExamResultbyStudentId(result);

                    //_fillDataForListResultFilter(subjectId, mResultListStudentBySuject, new TreeMap<>(groupStudentMap));
                    fillDataForListResultFilter(subjectId, mResultListStudentBySuject, result);
                } else {
                    Log.d(TAG, "getExamResultsbySubject().onSuccess() message:NUll");
                }
                if (finalProgressDialog != null) {
                    finalProgressDialog.dismiss();
                }
                showProgressLoading(false);
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

    private void fillDataForListResultFilter(int subjectId, ObservableRecyclerView mResultListStudentBySuject, List<ExamResult> result) {
        Collections.sort(result);
        examResultsAdapter = new ExamResultsAdapter(this, subjectId, result);
        mResultListStudentBySuject.setAdapter(examResultsAdapter);
        //hide loading
        showProgressLoading(false);

        mSearchExamResults.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.d(TAG, "query:" + query);
                examResultsAdapter.filter(query);
                return true;
            }
        });
    }

    private Map<Long, String> groupMonth(List<ExamResult> result) {
        Map<Long, String> groupMonth = new HashMap<Long, String>();
        for (ExamResult examResult : result) {
            int exam_month = examResult.getExam_month();
            int exam_year = examResult.getExam_year();
            if (examResult.getTerm_id() == 1) {
                long longDate = LaoSchoolShared.getLongDate(exam_month, exam_year);
                if (!groupMonth.containsKey(longDate)) {
                    groupMonth.put(longDate, LaoSchoolShared.getMonthString(exam_month) + "," + exam_year);
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


    private void setExpandedAppBar(boolean show, boolean show_animation) {
        appFilterBar.setExpanded(show, show_animation);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.d(TAG, "setUserVisibleHint(" + isVisibleToUser + ")");
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void reloadDataAfterInputSingleScore(int subjectId) {
        Log.d(TAG, "reloadDataAfterInputSingleScore() -subjectId:" + subjectId);
        getExamResultsbySubject(true, subjectId);
    }

    public static class MyExamResultsPager extends Fragment {
        private static final String TAG = MyExamResultsPager.class.getSimpleName();
        private static final String ARG_PAGE = "pager";
        private static final String ARG_LIST = "list";
        private static final String ARG_TERM_NAME = "term_name";
        private RecyclerView recyclerView;
        private MyExamResultsAdapter myExamResultsAdapter;
        private List<ExamResult> f_results;
        private int position;
        private Context context;

        public static MyExamResultsPager newInstance(int page, String termName, ArrayList<ExamResult> results) {
            Bundle args = new Bundle();
            args.putInt(ARG_PAGE, page);
            args.putString(ARG_TERM_NAME, termName);
            args.putParcelableArrayList(ARG_LIST, results);
            MyExamResultsPager fragment = new MyExamResultsPager();
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                position = getArguments().getInt(ARG_PAGE);
                f_results = getArguments().getParcelableArrayList(ARG_LIST);
            }
            this.context = getActivity();
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.view_exam_resluts_student_tab, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerViewExamResultsStudentTab);
            final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);

            //define adapter
            myExamResultsAdapter = new MyExamResultsAdapter(this, position, f_results);
            recyclerView.setAdapter(myExamResultsAdapter);

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
                            Log.d(TAG, "getMyExamResult(" + position + ").onAuthFail() message:NUll");
                            showProgressLoading(false);
                            showNoData();
                            alreadyExecuted = true;
                        }
                    } else {
                        Log.d(TAG, "getMyExamResult(" + position + ").onAuthFail() message:Size==0");
                        showProgressLoading(false);
                        showNoData();
                        alreadyExecuted = true;
                    }

                }

                @Override
                public void onFailure(String message) {
                    Log.e(TAG, "getMyExamResult(" + position + ").onFailure() message:" + message);
                    showProgressLoading(false);
                    showError();
                    alreadyExecuted = true;
                }

                @Override
                public void onAuthFail(String message) {
                    Log.e(TAG, "getMyExamResult(" + position + ").onAuthFail() message:" + message);
                    LaoSchoolShared.goBackToLoginPage(context);
                    showProgressLoading(false);
                    alreadyExecuted = true;
                }
            });
        }

        private void _refeshData(List<ExamResult> result) {
            recyclerView.swapAdapter(new MyExamResultsAdapter(this, position, result), true);

        }

    }
}
