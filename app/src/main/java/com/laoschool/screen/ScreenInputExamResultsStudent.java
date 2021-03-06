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
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.adapter.InputExamResultsbyTypeAdapter;
import com.laoschool.entities.ExamResult;
import com.laoschool.entities.ExamType;
import com.laoschool.entities.Master;
import com.laoschool.listener.AppBarStateChangeListener;
import com.laoschool.model.AsyncCallback;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenInputExamResultsStudent extends Fragment implements FragmentLifecycle {
    private static final String TAG = ScreenInputExamResultsStudent.class.getSimpleName();
    private Context context;
    private int containerId;

    private TextView lbTermName;
    private TextView lbClassName;
    private View mSelectedSubject;
    private View mSelectedInputExamType;
    private TextView lbInputDate;
    private TextView lbSubjectSeleted;
    private RecyclerView listExamByStudent;
    private View mContainer;

    private ProgressDialog progressDialog;
    private Dialog dialogSelectedSubject;

    private List<String> exam_months;
    private Dialog dialogSelectedInputTypeDate;

    //InputExamResultsAdapter resultsforClassbySubjectAdapter;
    private InputExamResultsbyTypeAdapter mAdapter;
    public int selectedSubjectId = -1;
    public int selectedExamTypeId = -1;
    public boolean onchange = false;

    private List<ExamType> mExamTypes = new ArrayList<>();
    private List<ExamResult> examListBySubjectId = new ArrayList<>();

    AppBarLayout input_exam_appbar;
    // private EditText txtSearch;
    private AppBarStateChangeListener.State appBarState = AppBarStateChangeListener.State.EXPANDED;
    private View mExpanSearch;
    private View mSugesstionSelectedSubject;
    private View mInput;
    private View viewMain;
    private View mSugesstionSelectedExamType;
    private LinearLayoutManager layoutManager;
    private MenuItem itemSearch;
    private SearchView mSearch;
    private MenuItem itemSubmit;
    private ExamType mTypeSelected;


    interface IScreenInputExamResults {
        void cancelInputExamResults();
    }

    public IScreenInputExamResults iScreenInputExamResults;

    public ScreenInputExamResultsStudent() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.context = getActivity();
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            Log.d(TAG, "-Container Id:" + containerId);
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewMain = inflater.inflate(R.layout.screen_input_exam_results_student, container, false);
        HomeActivity activity = (HomeActivity) getActivity();

        // activity.hideBottomBar();//hide bottom bar

        // activity.getSupportActionBar().setTitle(R.string.title_screen_input_exam_resuls);

        mContainer = viewMain.findViewById(R.id.coordinatorLayout);

        input_exam_appbar = (AppBarLayout) viewMain.findViewById(R.id.input_exam_appbar);
        onAppBarExpanded();

        mSelectedSubject = viewMain.findViewById(R.id.mSelectedSubject);
        lbSubjectSeleted = (TextView) mSelectedSubject.findViewById(R.id.lbSubjectSeleted);

        //
        mSugesstionSelectedSubject = viewMain.findViewById(R.id.mSugesstionSelectedSubject);
        mSugesstionSelectedExamType = viewMain.findViewById(R.id.mSugesstionSelectedExamType);
        mInput = viewMain.findViewById(R.id.mInput);
        //define toolbox
        mSelectedInputExamType = viewMain.findViewById(R.id.mToolBox);
        lbInputDate = (TextView) mSelectedInputExamType.findViewById(R.id.lbInputDate);

        listExamByStudent = (RecyclerView) viewMain.findViewById(R.id.listExamByStudent);

        layoutManager = new LinearLayoutManager(context);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //linearLayoutManager.setStackFromEnd(true);
        listExamByStudent.setLayoutManager(layoutManager);

        mExpanSearch = viewMain.findViewById(R.id.mSearchBox);
        mExpanSearch.setVisibility(View.GONE);

        onExpanSearch();

        // txtSearch = (EditText) viewMain.findViewById(R.id.txtSearch);
        onOnFocusChangeBoxSearch();
//        txtSearch.setEnabled(false);
//        txtSearch.clearFocus();


        //
        String className = LaoSchoolShared.myProfile.getEclass().getTitle();
        String termName = String.valueOf(context.getString(R.string.SCCommon_Term) + " " + LaoSchoolShared.myProfile.getEclass().getTerm());
        String year = String.valueOf(LaoSchoolShared.myProfile.getEclass().getYears());

        TextView txtClassAndTermName = (TextView) viewMain.findViewById(R.id.txtClassAndTermName);
        txtClassAndTermName.setText(className + " | " + year + "   " + termName);

        return viewMain;
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
        input_exam_appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d(TAG, "STATE:" + state.name());
                appBarState = state;
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_screen_input_exam_results_student, menu);
        itemSubmit = menu.findItem(R.id.action_submit_input_exam_results);
        itemSubmit.setEnabled(false);
        itemSearch = menu.findItem(R.id.search);
        itemSearch.setVisible(false);
        mSearch = (SearchView) itemSearch.getActionView();
        mSearch.setQueryHint(context.getString(R.string.SCCommon_Search));
        onExpandCollapseSearch();
    }

    private void onExpandCollapseSearch() {
        MenuItemCompat.setOnActionExpandListener(itemSearch, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                itemSearch.setVisible(true);
                mExpanSearch.setVisibility(View.GONE);
                setExpandedAppBar(false, true);

                ((HomeActivity) getActivity()).displaySearch();
                expanSearch();
                return true;
            }


            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                itemSearch.setVisible(false);
                mExpanSearch.setVisibility(View.VISIBLE);
                setExpandedAppBar(true, true);
                //resultsforClassbySubjectAdapter.filter("");
                mAdapter.filter("");
                ((HomeActivity) getActivity()).cancelSearch();
                return true;
            }
        });
    }

    private void expanSearch() {
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!query.trim().isEmpty()) {
                    //resultsforClassbySubjectAdapter.filter(query);
                    mAdapter.filter(query);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_submit_input_exam_results:
                submitInputExamResults();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPauseFragment() {
        selectedSubjectId = -1;
        selectedExamTypeId = -1;
        input_exam_appbar.setExpanded(true, false);
        examListBySubjectId.clear();
    }

    @Override
    public void onResumeFragment() {
        try {
            onchange = false;
            input_exam_appbar.setExpanded(true, true);
            String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_EXAM_RESULTS_2);
            ScreenExamResults screenExamResults = (ScreenExamResults) getFragmentManager().findFragmentByTag(tag);

            List<Master> subjectList = screenExamResults.listSubject;
            if (subjectList != null) {
                fillDataSubject(screenExamResults.listSubject, screenExamResults.selectedSubjectId);
            }
            getExamType();

            onSuggesstionSelectedSubjectCLick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSuggesstionSelectedSubjectCLick() {
        mSugesstionSelectedSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogSelectedSubject != null) {
                    dialogSelectedSubject.show();
                }
            }
        });
    }

    private void getExamType() {
        int classId = LaoSchoolShared.myProfile.getEclass().getId();
        LaoSchoolSingleton.getInstance().getDataAccessService().getExamType(classId, new AsyncCallback<List<ExamType>>() {
            @Override
            public void onSuccess(List<ExamType> result) {
                //mExamTypes.addAll(result);
                exam_months = new ArrayList<>();
                List<Integer> integers = new ArrayList<>();
                for (ExamType examType : result) {
                    if (examType.getEx_type() <= 2) {
                        //translate
                        if ((examType.getEx_name().equals("Sep"))) {
                            exam_months.add(getString(R.string.SCExanResults_month_9));
                        } else if ((examType.getEx_name().equals("Oct"))) {
                            exam_months.add(getString(R.string.SCExanResults_month_10));
                        } else if ((examType.getEx_name().equals("Nov"))) {
                            exam_months.add(getString(R.string.SCExanResults_month_11));
                        } else if ((examType.getEx_name().equals("Dec"))) {
                            exam_months.add(getString(R.string.SCExanResults_month_12));
                        } else if ((examType.getEx_name().equals("Test term1"))) {
                            exam_months.add(getString(R.string.SCExanResults_test_term1));
                        } else if ((examType.getEx_name().equals("Feb"))) {
                            exam_months.add(getString(R.string.SCExanResults_month_2));
                        } else if ((examType.getEx_name().equals("Mar"))) {
                            exam_months.add(getString(R.string.SCExanResults_month_3));
                        } else if ((examType.getEx_name().equals("Apr"))) {
                            exam_months.add(getString(R.string.SCExanResults_month_4));
                        } else if ((examType.getEx_name().equals("Apr"))) {
                            exam_months.add(getString(R.string.SCExanResults_month_5));
                        } else if ((examType.getEx_name().equals("Test term2"))) {
                            exam_months.add(getString(R.string.SCExanResults_test_term2));
                        }

                        integers.add(examType.getId());
                        mExamTypes.add(examType);
                    }
                }
                if (exam_months.size() > 0) {
                    dialogSelectedInputTypeDate = makeDialogSelectdInputExamType(exam_months, integers, mExamTypes);
                    onSelectedInputExamType();
                } else {

                }
            }

            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onAuthFail(String message) {

            }
        });
    }

    private void onSelectedInputExamType() {
        mSelectedInputExamType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getView().requestFocus();
                dialogSelectedInputTypeDate.show();
            }
        });
    }

    private void onSelectedSubject() {
        mSelectedSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getView().requestFocus();
                dialogSelectedSubject.show();
            }
        });
    }

    private void fillDataSubject(List<Master> listSubject, int selectedSubjectId) {
        final List<String> subjectNames = new ArrayList<>();
        Map<Integer, String> subs = new HashMap<>();
        for (Master master : listSubject) {
            subjectNames.add(master.getSval());
            subs.put(master.getId(), master.getSval());
        }
        dialogSelectedSubject = makeDialogSelectdSubject(listSubject, subjectNames);
        onSelectedSubject();
    }


    private Dialog makeDialogSelectdSubject(final List<Master> result, final List<String> subjectNames) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.SCExamResults_SelectScoreType);
        View header = View.inflate(context, R.layout.custom_hearder_dialog, null);
        ImageView imgIcon = ((ImageView) header.findViewById(R.id.imgIcon));
        Drawable drawable = LaoSchoolShared.getDraweble(context, R.drawable.ic_library_books_black_24dp);
//        int color = Color.parseColor("#ffffff");
//        drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        imgIcon.setImageDrawable(drawable);
        imgIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        ((TextView) header.findViewById(R.id.txbTitleDialog)).setText(R.string.SCExamResults_SelectSubject);

        builder.setCustomTitle(header);
        final ListAdapter subjectListAdapter = new ArrayAdapter<String>(context, R.layout.row_selected_subject, subjectNames);

        builder.setAdapter(subjectListAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {
                String subjectName = subjectNames.get(i);
                int subjectId = result.get(i).getId();
                lbSubjectSeleted.setText(subjectName);
                lbSubjectSeleted.setTextColor(Color.WHITE);
                getExamResultsbySubjectId(true, subjectId);
                selectedSubjectId = subjectId;
                mSugesstionSelectedSubject.setVisibility(View.GONE);
                mInput.setVisibility(View.VISIBLE);
                enabledSubmitInput();
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

    private void enabledSubmitInput() {
        if (selectedExamTypeId > 0 && selectedSubjectId > 0) {
            itemSubmit.setEnabled(true);
        }
    }

    private void submitInputExamResults() {
        LaoSchoolShared.hideSoftKeyboard(getActivity());
        if (getActivity().getCurrentFocus() != null) {
            getActivity().getCurrentFocus().clearFocus();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle(R.string.title_msg_are_you_sure);
        builder.setMessage(R.string.SCExamResults_msg_check_submit_score);
        builder.setNegativeButton(R.string.SCCommon_Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(R.string.SCCommon_Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //if (resultsforClassbySubjectAdapter.getInputExamResults().size() > 0) {
                if (mAdapter.getInputExamResults().size() > 0) {
                    Log.d(TAG, "-input type exam size:" + mAdapter.getInputExamResults().size());
                    // List<ExamResult> examResults = resultsforClassbySubjectAdapter.getInputExamResults();
                    List<ExamResult> examResults = mAdapter.getInputExamResults();
                    inputExamResults(examResults);
                } else {

                }
                dialogInterface.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();


    }

    private void inputExamResults(final List<ExamResult> examResults) {
        Log.d(TAG, "type:" + mTypeSelected.toString());
        int teacherId = LaoSchoolShared.myProfile.getId();
        Log.d(TAG, "teacherId:" + teacherId);
        progressDialog.show();
        final int size = examResults.size();
        List<ExamResult> listScore = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            final ExamResult examResult = examResults.get(i);
            ExamResult score = new ExamResult();

            score.setClass_id(examResult.getClass_id());
            score.setSchool_id(examResult.getSchool_id());
            score.setStudent_id(examResult.getStudent_id());
            score.setSubject_id(examResult.getSubject_id());

//            switch (selectedExamTypeId) {
//                case 1:
//                    score.setM1(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
//                    break;
//                case 2:
//                    score.setM2(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
//                    break;
//                case 3:
//                    score.setM3(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
//                    break;
//                case 4:
//                    score.setM4(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
//                    break;
//                case 6:
//                    score.setM6(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
//                    break;
//                case 7:
//                    score.setM7(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
//                    break;
//                case 8:
//                    score.setM8(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
//                    break;
//                case 9:
//                    score.setM9(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
//                    break;
//                case 10:
//                    score.setM10(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
//                    break;
//                case 12:
//                    score.setM12(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
//                    break;
//            }
            if (mTypeSelected.getEx_key().equals("m1")) {
                score.setM1(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
            } else if (mTypeSelected.getEx_key().equals("m2")) {
                score.setM2(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
            } else if (mTypeSelected.getEx_key().equals("m3")) {
                score.setM3(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
            } else if (mTypeSelected.getEx_key().equals("m4")) {

                score.setM4(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
            } else if (mTypeSelected.getEx_key().equals("m5")) {
                //score.setM5(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
            } else if (mTypeSelected.getEx_key().equals("m6")) {
                score.setM6(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
            } else if (mTypeSelected.getEx_key().equals("m7")) {
                score.setM7(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
            } else if (mTypeSelected.getEx_key().equals("m8")) {
                score.setM8(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
            } else if (mTypeSelected.getEx_key().equals("m9")) {
                score.setM9(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
            } else if (mTypeSelected.getEx_key().equals("m10")) {
                score.setM10(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
            } else if (mTypeSelected.getEx_key().equals("m11")) {
                score.setM11(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
            } else if (mTypeSelected.getEx_key().equals("m12")) {
                //score.setM12(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
            } else if (mTypeSelected.getEx_key().equals("m13")) {
                score.setM13(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
            } else if (mTypeSelected.getEx_key().equals("m14")) {
                //score.setM14(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
            } else if (mTypeSelected.getEx_key().equals("m15")) {
                score.setM15(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
            } else if (mTypeSelected.getEx_key().equals("m16")) {
                score.setM16(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
            } else if (mTypeSelected.getEx_key().equals("m17")) {
//                score.setM17(LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice()));
            }
            listScore.add(score);
        }
        callInputExamResults(listScore);
    }

    private void callInputExamResults(List<ExamResult> listScore) {

//        LaoSchoolSingleton.getInstance().getDataAccessService().inputExamResults(examResult, new AsyncCallback<ExamResult>() {
//            @Override
//            public void onSuccess(ExamResult result) {
//                Log.d(TAG, "callInputExamResults().onSuccess() -result: " + result.toJson());
//                if (index == (size - 1)) {
//                    Log.d(TAG, "callInputExamResults().onSuccess() -input " + size + " ok");
//                    finishInputExamResults();
//                }
//
//            }
//
//            @Override
//            public void onFailure(String message) {
//                Log.d(TAG, "callInputExamResults().onFailure() -message" + message);
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onAuthFail(String message) {
//                Log.d(TAG, "callInputExamResults().onAuthFail() -message" + message);
//                progressDialog.dismiss();
//            }
//        });


        LaoSchoolSingleton.getInstance().getDataAccessService().inputBatchExamResults(listScore, new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                finishInputExamResults();
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
        onchange = true;
        // resultsforClassbySubjectAdapter.clearData();
        mAdapter.clearData();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.SCCommon_Successfully);
        builder.setPositiveButton(R.string.SCCommon_Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getExamResultsbySubjectId(false, selectedSubjectId, selectedExamTypeId);
            }
        });
        Dialog dialog = builder.create();
        dialog.show();


    }

    private void getExamResultsbySubjectId(boolean showProgress, int selectedSubjectId, final int selectedExamTypeId) {
        Log.d(TAG, "getExamResultsbySubject()");
        if (showProgress) {
            progressDialog.show();
        }
        final ProgressDialog finalProgressDialog = progressDialog;
        LaoSchoolSingleton.getInstance().getDataAccessService().getExamResultsforMark(LaoSchoolShared.myProfile.getEclass().getId(), -1, selectedSubjectId, new AsyncCallback<List<ExamResult>>() {
            @Override
            public void onSuccess(List<ExamResult> result) {
                if (result != null) {
                    //Group data
                    //fillDataExamStudent(groupExamResultbyStudentId(result, selectedExamTypeId), selectedExamTypeId);
                    fillDataExamStudent2(result, mTypeSelected);
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
            }

            @Override
            public void onAuthFail(String message) {
                Log.e(TAG, "getExamResultsbySubject().onAuthFail() message:" + message);
                if (finalProgressDialog != null)
                    finalProgressDialog.dismiss();
                LaoSchoolShared.goBackToLoginPage(context);

            }
        });
    }

    private void fillDataExamStudent2(List<ExamResult> result, ExamType mTypeSelected) {
        //resultsforClassbySubjectAdapter = new InputExamResultsAdapter(getActivity(), context, groupStudentMap, selectedExamTypeId);
        Log.d(TAG, "Input exam to month: " + mTypeSelected);
        Collections.sort(result);
        mAdapter = new InputExamResultsbyTypeAdapter(getActivity(), context, result, mTypeSelected);
        listExamByStudent.setAdapter(mAdapter);
        listExamByStudent.setNestedScrollingEnabled(false);
        onTextChangeTextBoxSearch();
        mExpanSearch.setVisibility(View.VISIBLE);
        //  txtSearch.setEnabled(true);
        mSugesstionSelectedExamType.setVisibility(View.GONE);
    }

    private void cancelInputExamResults() {
        iScreenInputExamResults.cancelInputExamResults();
    }

    private void getExamResultsbySubjectId(boolean showProgress, final int subjectId) {
        Log.d(TAG, "getExamResultsbySubject()");
        if (showProgress) {
            progressDialog.show();
        }
        final ProgressDialog finalProgressDialog = progressDialog;
        LaoSchoolSingleton.getInstance().getDataAccessService().getExamResultsforMark(LaoSchoolShared.myProfile.getEclass().getId(), -1, subjectId, new AsyncCallback<List<ExamResult>>() {
            @Override
            public void onSuccess(List<ExamResult> result) {
                if (result != null) {
                    examListBySubjectId.clear();
                    examListBySubjectId.addAll(result);
                    if (selectedExamTypeId > 0) {
                        fillDataExamStudent2(examListBySubjectId, mTypeSelected);
                    } else {
                        showSugesstionSelectedExamType();
                    }
                    if (progressDialog != null || progressDialog.isShowing())
                        progressDialog.dismiss();
                } else {
                    Log.d(TAG, "getExamResultsbySubject().onSuccess() message:NUll");
                }
            }

            @Override
            public void onFailure(String message) {
                Log.e(TAG, "getExamResultsbySubject().onFailure() message:" + message);
                if (finalProgressDialog != null)
                    finalProgressDialog.dismiss();
            }

            @Override
            public void onAuthFail(String message) {
                Log.e(TAG, "getExamResultsbySubject().onAuthFail() message:" + message);
                if (finalProgressDialog != null)
                    finalProgressDialog.dismiss();
                LaoSchoolShared.goBackToLoginPage(context);

            }
        });
    }

    private void showSugesstionSelectedExamType() {
        mSugesstionSelectedExamType.setVisibility(View.VISIBLE);
        onSuggesstionSelectedExamTypeClick();
    }

    private void onSuggesstionSelectedExamTypeClick() {
        mSugesstionSelectedExamType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogSelectedInputTypeDate != null) {
                    dialogSelectedInputTypeDate.show();
                }
            }
        });
    }

//    private void fillDataDateInputExamResults(List<ExamResult> examResults, List<ExamType> examTypes) {
//        exam_months = new ArrayList<>();
//        List<Integer> exam_type = new ArrayList<>();
//        for (ExamType mExamTypes : examTypes) {
//            if (mExamTypes.getEx_type() <= 2) {
//                exam_months.add(mExamTypes.getEx_name());
//                Log.d(TAG, "exam_type:" + mExamTypes.toString());
//                exam_type.add(mExamTypes.getId());
//            }
//        }
//        if (exam_months.size() > 0) {
//            //
//            dialogSelectedInputTypeDate = makeDialogSelectdInputExamType(examResults, exam_months, exam_type);
//            mSelectedInputExamType.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //clearFocus();
//                    view.requestFocus();
//                    dialogSelectedInputTypeDate.show();
//                }
//            });
//        }
//    }

    private void clearFocus() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
        }
//        mSearchCardBox.clearFocus();
//        txtSearch.clearFocus();
//        mContainer.requestFocus();
        LaoSchoolShared.hideSoftKeyboard(getActivity());
    }

//    private Dialog makeDialogSelectdInputExamType(final List<ExamResult> examResults, final List<String> exam_months, final List<Integer> examTypeIds) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        // builder.setTitle(R.string.title_selected_type_input_exam_results);
//        View header = View.inflate(context, R.layout.custom_hearder_dialog, null);
//        ImageView imgIcon = ((ImageView) header.findViewById(R.id.imgIcon));
//        imgIcon.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_input_white_24dp));
//
//        ((TextView) header.findViewById(R.id.txbTitleDialog)).setText("Seleted date");
//
//        builder.setCustomTitle(header);
//        final ListAdapter subjectListAdapter = new ArrayAdapter<String>(context, R.layout.row_selected_subject, exam_months);
//
//        builder.setAdapter(subjectListAdapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(final DialogInterface dialogInterface, final int position) {
//                String selected = exam_months.get(position);
//                selectedExamTypeId = examTypeIds.get(position);
//                Log.d(TAG, "-selected " + position + " exam type Id:" + selectedExamTypeId);
//                lbInputDate.setText(selected);
//                lbInputDate.setTextColor(Color.WHITE);
//                if (examResults != null) {
//                    // fillDataExamStudent(groupExamResultbyStudentId(examResults, selectedExamTypeId), selectedExamTypeId);
//                    fillDataExamStudent2(examListBySubjectId, selectedExamTypeId);
//                }
//                enabledSubmitInput();
//
//
//            }
//        });
//        final AlertDialog dialog = builder.create();
//        (header.findViewById(R.id.imgCloseDialog)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        return dialog;
//    }


    private Dialog makeDialogSelectdInputExamType(final List<String> exam_months, final List<Integer> examTypeIds, List<ExamType> examType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // builder.setTitle(R.string.title_selected_type_input_exam_results);
        View header = View.inflate(context, R.layout.custom_hearder_dialog, null);
        ImageView imgIcon = ((ImageView) header.findViewById(R.id.imgIcon));
        imgIcon.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_input_white_24dp));

        ((TextView) header.findViewById(R.id.txbTitleDialog)).setText(R.string.SCExamResults_SelectScoreType);

        builder.setCustomTitle(header);
        final ListAdapter subjectListAdapter = new ArrayAdapter<String>(context, R.layout.row_selected_subject, exam_months);

        builder.setAdapter(subjectListAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int position) {
                String selected = exam_months.get(position);
                selectedExamTypeId = examTypeIds.get(position);
                mTypeSelected = mExamTypes.get(position);
                Log.d(TAG, "-selected " + position + " exam type Id:" + selectedExamTypeId);
                lbInputDate.setText(selected);
                lbInputDate.setTextColor(Color.WHITE);
                if (examListBySubjectId.size() > 0) {
                    //fillDataExamStudent(groupExamResultbyStudentId(examListBySubjectId, selectedExamTypeId), selectedExamTypeId);
                    fillDataExamStudent2(examListBySubjectId, mTypeSelected);
                }
                enabledSubmitInput();


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

    private void fillDataExamStudent(Map<Integer, ExamResult> groupStudentMap, int selectedExamTypeId) {
        // resultsforClassbySubjectAdapter = new InputExamResultsAdapter(getActivity(), context, groupStudentMap, selectedExamTypeId);
        //listExamByStudent.setAdapter(resultsforClassbySubjectAdapter);
        onTextChangeTextBoxSearch();
        mExpanSearch.setVisibility(View.VISIBLE);
        //  txtSearch.setEnabled(true);
        mSugesstionSelectedExamType.setVisibility(View.GONE);
    }

    private void onTextChangeTextBoxSearch() {
//        txtSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (appBarState == AppBarStateChangeListener.State.EXPANDED) {
//                    setExpandedAppBar(true, true);
//                }
//                resultsforClassbySubjectAdapter.filter(charSequence.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
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
////                    else {
////                        setExpandedAppBar(true, true);
////                    }
//                } else {
//                    //txtSearch.clearFocus();
//                }
//
//
//            }
//        });

    }

    private void setExpandedAppBar(boolean show, boolean show_animation) {
        input_exam_appbar.setExpanded(show, show_animation);
    }

    private Map<Integer, ExamResult> groupExamResultbyStudentId(List<ExamResult> result, int selectedExamTypeId) {
        Map<Integer, ExamResult> groupStudentMap = new HashMap<Integer, ExamResult>();
        for (ExamResult examResult : result) {
            if (examResult.getExam_id() == selectedExamTypeId) {
                Integer studentId = examResult.getStudent_id();
                groupStudentMap.put(studentId, examResult);
            }
        }
        return groupStudentMap;
    }


    public static Fragment instantiate(int containerId, String currentRole) {
        ScreenInputExamResultsStudent fragment = new ScreenInputExamResultsStudent();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iScreenInputExamResults = (IScreenInputExamResults) context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
