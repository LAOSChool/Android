package com.laoschool.screen;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Toast;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.adapter.InputExamResultsAdapter;
import com.laoschool.entities.ExamResult;
import com.laoschool.entities.ExamType;
import com.laoschool.entities.Master;
import com.laoschool.model.AsyncCallback;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    private View mToolBox;
    private TextView lbInputDate;
    private TextView lbSubjectSeleted;
    private RecyclerView listExamByStudent;

    private ProgressDialog progressDialog;
    private Dialog dialogSelectedSubject;

    private List<String> exam_months;
    private Dialog dialogSelectedInputTypeDate;

    InputExamResultsAdapter resultsforClassbySubjectAdapter;
    public int selectedSubjectId;
    private int selectedExamTypeId;
    public boolean onchange = false;

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
        View view = inflater.inflate(R.layout.screen_input_exam_results_student, container, false);
        HomeActivity activity = (HomeActivity) getActivity();
        activity.hideBottomBar();

        View customActionBar = View.inflate(context, R.layout.view_custom_action_bar_input_exam_results, null);
        lbTermName = (TextView) customActionBar.findViewById(R.id.lbClassAndTermName);
        lbClassName = (TextView) customActionBar.findViewById(R.id.lbClassName);
        String className = LaoSchoolShared.myProfile.getEclass().getTitle();
        String termName = String.valueOf("Term " + LaoSchoolShared.myProfile.getEclass().getTerm());
        String year = String.valueOf(LaoSchoolShared.myProfile.getEclass().getYears());

        lbTermName.setText(termName + " / " + year);
        lbClassName.setText(className);
        activity.getSupportActionBar().setCustomView(customActionBar);


        mSelectedSubject = view.findViewById(R.id.mSelectedSubject);
        lbSubjectSeleted = (TextView) mSelectedSubject.findViewById(R.id.lbSubjectSeleted);

        //define toolbox
        mToolBox = view.findViewById(R.id.mToolBox);
        lbInputDate = (TextView) mToolBox.findViewById(R.id.lbInputDate);

        listExamByStudent = (RecyclerView) view.findViewById(R.id.listExamByStudent);
        listExamByStudent.setHasFixedSize(true);

        listExamByStudent.setLayoutManager(new LinearLayoutManager(context));

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_screen_input_exam_results_student, menu);
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
    }

    @Override
    public void onResumeFragment() {
        try {
            onchange = false;
            String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_EXAM_RESULTS_2);
            ScreenExamResults screenExamResults = (ScreenExamResults) getFragmentManager().findFragmentByTag(tag);

            List<Master> subjectList = screenExamResults.listSubject;
            if (subjectList != null) {
                fillDataSubject(screenExamResults.listSubject, screenExamResults.selectedSubjectId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getExamType(final List<ExamResult> examResults) {
        int classId = LaoSchoolShared.myProfile.getEclass().getId();
        LaoSchoolSingleton.getInstance().getDataAccessService().getExamType(classId, new AsyncCallback<List<ExamType>>() {
            @Override
            public void onSuccess(List<ExamType> result) {
                fillDataDateInputExamResults(examResults, result);
                if (progressDialog != null)
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(String message) {
                if (progressDialog != null)
                    progressDialog.dismiss();
            }

            @Override
            public void onAuthFail(String message) {
                if (progressDialog != null)
                    progressDialog.dismiss();
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
        //First load
        this.selectedSubjectId = selectedSubjectId;
        lbSubjectSeleted.setText(subs.get(selectedSubjectId));
        getExamResultsbySubjectId(true, selectedSubjectId);

        dialogSelectedSubject = makeDialogSelectdSubject(listSubject, subjectNames);
        mSelectedSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFocus();
                dialogSelectedSubject.show();
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

        ((TextView) header.findViewById(R.id.txbTitleDialog)).setText(R.string.selected_subject);


        builder.setCustomTitle(header);
        final ListAdapter subjectListAdapter = new ArrayAdapter<String>(context, R.layout.row_selected_subject, subjectNames);

        builder.setAdapter(subjectListAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {
                String subjectName = subjectNames.get(i);
                int subjectId = result.get(i).getId();
                lbSubjectSeleted.setText(subjectName);
                getExamResultsbySubjectId(true, subjectId);
                selectedSubjectId = subjectId;
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

    private void submitInputExamResults() {
        LaoSchoolShared.hideSoftKeyboard(getActivity());
        if (getActivity().getCurrentFocus() != null) {
            getActivity().getCurrentFocus().clearFocus();
        }
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
                if (resultsforClassbySubjectAdapter.getInputExamResults().size() > 0) {
                    Log.d(TAG, "-input type exam size:" + resultsforClassbySubjectAdapter.getInputExamResults().size());
                    List<ExamResult> examResults = resultsforClassbySubjectAdapter.getInputExamResults();
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

        int teacherId = LaoSchoolShared.myProfile.getId();
        Log.d(TAG, "teacherId:" + teacherId);
        progressDialog.show();
        final int size = examResults.size();
        for (int i = 0; i < size; i++) {
            final ExamResult examResult = examResults.get(i);
            ExamResult score = new ExamResult();
            score.setTeacher_id(teacherId);
            score.setSresult(examResult.getSresult());
            score.setClass_id(examResult.getClass_id());
            score.setExam_id(selectedExamTypeId);
            score.setExam_type(examResult.getExam_type());
            score.setSchool_id(examResult.getSchool_id());
            score.setStudent_id(examResult.getStudent_id());
            score.setTerm_id(examResult.getTerm_id());
            score.setSubject_id(examResult.getSubject_id());
            score.setNotice(examResult.getNotice());
            callInputExamResults(size, i, score);
            Log.d(TAG, "inputExamResults() -exam:" + score.toJson());

        }


    }

    private void callInputExamResults(final int size, final int index, ExamResult examResult) {
        LaoSchoolSingleton.getInstance().getDataAccessService().inputExamResults(examResult, new AsyncCallback<ExamResult>() {
            @Override
            public void onSuccess(ExamResult result) {
                Log.d(TAG, "callInputExamResults().onSuccess() -result: " + result.toJson());
                if (index == (size - 1)) {
                    Log.d(TAG, "callInputExamResults().onSuccess() -input " + size + " ok");
                    finishInputExamResults();
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
        onchange = true;
        resultsforClassbySubjectAdapter.clearData();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.msg_input_exam_results_successfully);
        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
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
                    fillDataExamStudent(groupExamResultbyStudentId(result, selectedExamTypeId), selectedExamTypeId);

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
                    //Group data
                    getExamType(result);

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

    private void fillDataDateInputExamResults(List<ExamResult> examResults, List<ExamType> examTypes) {
        exam_months = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();
        for (ExamType examType : examTypes) {
            if (examType.getEx_type() <= 2) {
                exam_months.add(examType.getEx_name());
                Log.d(TAG, "exam_type:" + examType.toString());
                integers.add(examType.getId());
            }
        }
        if (exam_months.size() > 0) {
            //first
            lbInputDate.setText(exam_months.get(0));
            selectedExamTypeId = examTypes.get(0).getId();
            fillDataExamStudent(groupExamResultbyStudentId(examResults, selectedExamTypeId), selectedExamTypeId);
            //
            dialogSelectedInputTypeDate = makeDialogSelectdInputExamType(examResults, exam_months, integers);
            mToolBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearFocus();
                    dialogSelectedInputTypeDate.show();
                }
            });
        }
    }

    private void clearFocus() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
        }
        LaoSchoolShared.hideSoftKeyboard(getActivity());
    }

    private Dialog makeDialogSelectdInputExamType(final List<ExamResult> examResults, final List<String> exam_months, final List<Integer> examTypeIds) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // builder.setTitle(R.string.title_selected_type_input_exam_results);
        View header = View.inflate(context, R.layout.custom_hearder_dialog, null);
        ImageView imgIcon = ((ImageView) header.findViewById(R.id.imgIcon));
        imgIcon.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_input_white_24dp));

        ((TextView) header.findViewById(R.id.txbTitleDialog)).setText("Seleted date");

        builder.setCustomTitle(header);
        final ListAdapter subjectListAdapter = new ArrayAdapter<String>(context, R.layout.row_selected_subject, exam_months);

        builder.setAdapter(subjectListAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int position) {
                String selected = exam_months.get(position);
                selectedExamTypeId = examTypeIds.get(position);
                Log.d(TAG, "-selected " + position + " exam type Id:" + selectedExamTypeId);
                lbInputDate.setText(selected);
                fillDataExamStudent(groupExamResultbyStudentId(examResults, selectedExamTypeId), selectedExamTypeId);


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
        resultsforClassbySubjectAdapter = new InputExamResultsAdapter(context, groupStudentMap, selectedExamTypeId);
        listExamByStudent.setAdapter(resultsforClassbySubjectAdapter);
    }

    private Map<Long, String> groupMonth(List<ExamResult> result) {
        Map<Long, String> groupMonth = new HashMap<Long, String>();
        for (ExamResult examResult : result) {
            if (examResult.getExam_type() <= 2) {
                int exam_month = examResult.getExam_month();
                groupMonth.put(Long.valueOf(exam_month), getMonthString(exam_month));
            }
        }
        return groupMonth;
    }

    private String getMonthString(int month) {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(Locale.UK);
        String monthParse = dateFormatSymbols.getMonths()[month - 1];
        return monthParse;
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

}
