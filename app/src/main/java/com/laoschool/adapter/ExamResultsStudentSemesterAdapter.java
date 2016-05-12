package com.laoschool.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laoschool.R;
import com.laoschool.entities.ExamResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Hue on 3/11/2016.
 */
public class ExamResultsStudentSemesterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = ExamResultsStudentSemesterAdapter.class.getSimpleName();
    private Fragment screen;
    private Context context;
    private List<ExamResult> examResults;
    private List<String> subjects = new ArrayList<>();
    private List<Integer> subjectIds = new ArrayList<>();

    private int TYPE_SUB_HEADER = 0;
    private int TYPE_TITLE = 1;
    private int TYPE_LINE = 2;
    Map<Integer, Map<Integer, ArrayList<ExamResult>>> listExamMap = new HashMap<>();

    public ExamResultsStudentSemesterAdapter(Fragment screen, List<ExamResult> examResults) {
        this.screen = screen;
        this.context = screen.getActivity();
        if (examResults != null) {
            this.examResults = examResults;
            //define linker subject
            HashMap<Integer, String> hashSubject = defineSubjectMap(examResults);
            //sort subject
            Map<Integer, String> treeSubject = sortMap(hashSubject);
            for (Integer subId : treeSubject.keySet()) {
                String subjectName = treeSubject.get(subId);
                addSubject(subId, subjectName);
                addExambySubjectId(subId);
            }
        } else {
            this.examResults = new ArrayList<>();
        }
    }

    private void addSubject(Integer subId, String subjectName) {
        subjectIds.add(subId);
        subjects.add(subjectName);
    }

    private void addExambySubjectId(Integer subId) {
        Map<Integer, ArrayList<ExamResult>> examByMonthList = new HashMap<>();
        ArrayList<ExamResult> end_exam_semester = new ArrayList<>();
        for (int i = 0; i < examResults.size(); i++) {
            ExamResult examResult = examResults.get(i);
            int exam_month = examResult.getExam_month();
            int exam_type = examResult.getExam_type();
            if (examResult.getSubject_id() == subId) {
                ArrayList<ExamResult> examTermList = null;

                if (examByMonthList.containsKey(exam_month)) {
                    examTermList = examByMonthList.get(exam_month);
                    if (examTermList == null)
                        examTermList = new ArrayList();
                    if (exam_type == 1)
                        examTermList.add(examResult);
                    else if (exam_type == 2) {
                        end_exam_semester.add(examResult);
                    }
                } else {
                    examTermList = new ArrayList();
                    if (exam_type == 1)
                        examTermList.add(examResult);
                    else if (exam_type == 2) {
                        end_exam_semester.add(examResult);
                    }
                }
                examByMonthList.put(exam_month, examTermList);
            }
        }
        examByMonthList.put(100, end_exam_semester);
        listExamMap.put(subId, examByMonthList);
    }

    private HashMap<Integer, String> defineSubjectMap(List<ExamResult> examResults) {
        HashMap<Integer, String> hashSubject = new LinkedHashMap<Integer, String>();
        for (ExamResult examResult : examResults) {
            hashSubject.put(examResult.getSubject_id(), examResult.getSubjectName());
        }
        return hashSubject;
    }

    private Map sortMap(HashMap<Integer, String> hashSubject) {
        return new TreeMap(hashSubject);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_LINE)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_line, parent, false); //Inflating the layout
        else if (viewType == TYPE_TITLE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exam_results_student, parent, false); //Inflating the layout
        } else if (viewType == TYPE_SUB_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_only_title, parent, false); //Inflating the layout
        }
        ExamResultsStudentSemesterAdapterViewHolder viewHolder = new ExamResultsStudentSemesterAdapterViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof ExamResultsStudentSemesterAdapterViewHolder) {
                ExamResultsStudentSemesterAdapterViewHolder semesterHolder = (ExamResultsStudentSemesterAdapterViewHolder) holder;
                View view = semesterHolder.view;
                if (semesterHolder.viewType == TYPE_TITLE) {
                    final String title = subjects.get(position);
                    Map<Integer, ArrayList<ExamResult>> examsMap = listExamMap.get(subjectIds.get(position));
                    Map<Integer, ArrayList<ExamResult>> examsTree = new TreeMap<>(examsMap);

//                //Define and set data
                    TextView txtSubjectScreenResultsStudent = (TextView) view.findViewById(R.id.txtSubjectScreenResultsStudent);
                    RecyclerView mListScoreBySemester = (RecyclerView) view.findViewById(R.id.mListScoreBySemester);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    mListScoreBySemester.setLayoutManager(linearLayoutManager);

                    txtSubjectScreenResultsStudent.setText(title);

                    ScoreStudentSemesterAdapter scoreStudentSemesterAdapter = new ScoreStudentSemesterAdapter(context, examsTree);
                    mListScoreBySemester.setAdapter(scoreStudentSemesterAdapter);
                } else {

                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder() - exception messages:" + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    @Override
    public int getItemViewType(int position) {
//        String item = strings.get(position);
//        if (item.equals(context.getString(R.string.row_sub_header)))
//            return TYPE_SUB_HEADER;
//        else if (!item.equals(context.getString(R.string.row_line)))
        return TYPE_TITLE;
//        else
//            return TYPE_LINE;

    }

    public class ExamResultsStudentSemesterAdapterViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public ExamResultsStudentSemesterAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }
}

