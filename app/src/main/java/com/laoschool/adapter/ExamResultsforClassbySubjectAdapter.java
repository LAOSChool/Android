package com.laoschool.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.ExamResult;
import com.laoschool.screen.ScreenExamResults;
import com.laoschool.screen.view.DialogInputExamResultsForStudent;
import com.laoschool.shared.LaoSchoolShared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hue on 3/7/2016.
 */
public class ExamResultsforClassbySubjectAdapter extends RecyclerView.Adapter<ExamResultsforClassbySubjectAdapter.StudentInfomationViewHolder> {
    private static final String TAG = ExamResultsforClassbySubjectAdapter.class.getSimpleName();
    private ScreenExamResults screenExamResults;
    private Context context;
    private ScreenExamResults.IScreenExamResults iScreenExamResults;
    private List<Integer> studentIds = new ArrayList<>();
    private List<List<ExamResult>> listExam = new ArrayList<>();
    private Map<Integer, List<ExamResult>> groupExamByStudent;
    private static final int TYPE_SEARCH = 0;
    private int TYPE_EXAM = 1;
    private int subjectId;

    public ExamResultsforClassbySubjectAdapter(ScreenExamResults screenExamResults, int subjectId, Map<Integer, List<ExamResult>> groupExamByStudent) {
        this.screenExamResults = screenExamResults;
        this.context = screenExamResults.getActivity();
        this.iScreenExamResults = screenExamResults.iScreenExamResults;
        this.groupExamByStudent = groupExamByStudent;
        this.subjectId = subjectId;
        for (Integer studentId : groupExamByStudent.keySet()) {
            studentIds.add(studentId);
            listExam.add(groupExamByStudent.get(studentId));
        }
    }

    @Override
    public StudentInfomationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_SEARCH) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_exam_results, parent, false); //Inflating the layout
        } else if (viewType == TYPE_EXAM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exam_results, parent, false); //Inflating the layout
        }
        StudentInfomationViewHolder viewHolder = new StudentInfomationViewHolder(view, viewType);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(StudentInfomationViewHolder holder, int position) {
        try {
            if (holder instanceof StudentInfomationViewHolder) {
                StudentInfomationViewHolder infomationViewHolder = holder;
                if (holder.viewType == TYPE_SEARCH) {
                } else if (holder.viewType == TYPE_EXAM) {
                    View view = infomationViewHolder.view;
                    int studentId = studentIds.get(position);
                    final List<ExamResult> examResultByStudentId = groupExamByStudent.get(studentId);
                    ScoreCurrentSemesterAdapter examAdpter = new ScoreCurrentSemesterAdapter(screenExamResults, subjectId, context, iScreenExamResults, examResultByStudentId);
                    ExamResult examResult = examResultByStudentId.get(0);
                    String userName = examResult.getStudent_name();

                    TextView row_title = (TextView) view.findViewById(R.id.row_title);
                    TextView lbNickName = (TextView) view.findViewById(R.id.lbNickName);
                    NetworkImageView imgUserAvata = (NetworkImageView) view.findViewById(R.id.row_icon);
                    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.mListScoreBySemester);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(examAdpter);
                    recyclerView.setNestedScrollingEnabled(false);
                    //fill data exam results
                    lbNickName.setText(examResult.getStd_nickname());
                    row_title.setText(userName);


                    //Load photo student
                    String photo = examResult.getStd_photo();
                    if (photo != null) {
                        LaoSchoolSingleton.getInstance().getImageLoader().get(photo, ImageLoader.getImageListener(imgUserAvata,
                                R.drawable.ic_account_circle_black_36dp, R.drawable.ic_account_circle_black_36dp));
                        imgUserAvata.setImageUrl(photo, LaoSchoolSingleton.getInstance().getImageLoader());
                    } else {
                        imgUserAvata.setDefaultImageResId(R.drawable.ic_account_circle_black_36dp);
                    }

                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder() -exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private Map<Integer, ArrayList<ExamResult>> groupDataByTerm
            (List<ExamResult> examResultByStudentId) {
        Map<Integer, ArrayList<ExamResult>> groupExamByTerm = new HashMap<>();
        for (ExamResult examResult : examResultByStudentId) {
            int examTermId = examResult.getTerm_id();
            ArrayList<ExamResult> temp;
            if (groupExamByTerm.containsKey(examTermId)) {
                temp = groupExamByTerm.get(examTermId);
                temp.add(examResult);
            } else {
                temp = new ArrayList<>();
                temp.add(examResult);
            }
            groupExamByTerm.put(examTermId, temp);
        }
        return groupExamByTerm;
    }

    @Override
    public int getItemCount() {
        if (studentIds.size() > 0)
            return studentIds.size();
        else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int studentId = studentIds.get(position);
        if (studentId == 0) {
            return TYPE_SEARCH;
        } else
            return TYPE_EXAM;
    }

    public class StudentInfomationViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public StudentInfomationViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }
}
