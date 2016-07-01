package com.laoschool.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laoschool.R;
import com.laoschool.entities.ExamResult;
import com.laoschool.shared.LaoSchoolShared;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Hue on 6/29/2016.
 */
public class MyExamResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = MyExamResultsAdapter.class.getSimpleName();
    private final int termId;
    private Fragment screen;
    private Context context;
    private List<ExamResult> examResults;

    private int TYPE_SUB_HEADER = 0;
    private int TYPE_TITLE = 1;
    private int TYPE_LINE = 2;

    public MyExamResultsAdapter(Fragment screen, int termId, List<ExamResult> examResults) {
        this.screen = screen;
        this.context = screen.getActivity();
        this.termId = termId;
        this.examResults = examResults;
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
        MyExamResultsAdapterViewHolder viewHolder = new MyExamResultsAdapterViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof MyExamResultsAdapterViewHolder) {
                MyExamResultsAdapterViewHolder semesterHolder = (MyExamResultsAdapterViewHolder) holder;
                View view = semesterHolder.view;
                if (semesterHolder.viewType == TYPE_TITLE) {
                    final String title = examResults.get(position).getSubjectName();
//                //Define and set data
                    TextView txtSubjectScreenResultsStudent = (TextView) view.findViewById(R.id.txtSubjectScreenResultsStudent);
                    txtSubjectScreenResultsStudent.setText(title);

                    RecyclerView mListScoreBySemester = (RecyclerView) view.findViewById(R.id.mListScoreBySemester);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false);
                    mListScoreBySemester.setLayoutManager(gridLayoutManager);

                    ExamsAdapter examsAdapter = new ExamsAdapter(context, termId, examResults.get(position));
                    mListScoreBySemester.setAdapter(examsAdapter);
                    mListScoreBySemester.setNestedScrollingEnabled(false);
                } else {

                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder() - exception messages:" + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return examResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_TITLE;
    }

    public class MyExamResultsAdapterViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public MyExamResultsAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }

    class ExamsAdapter extends RecyclerView.Adapter<ExamsAdapter.ExamResultsAdapterViewHolder> {
        private final String TAG = ExamsAdapter.class.getSimpleName();
        Context context;
        List<String> str_score = new ArrayList<>();
        List<String> str_score_name = new ArrayList<>();
        ExamResult examResult;

        public ExamsAdapter(Context context, int termId, ExamResult examResult) {
            this.context = context;
            this.examResult = examResult;
            if (termId == 0) {
                str_score.add(examResult.getM1());
                str_score.add(examResult.getM2());
                str_score.add(examResult.getM3());
                str_score.add(examResult.getM4());
                str_score.add(examResult.getM5());

                str_score.add(examResult.getM6());
                str_score.add(examResult.getM7());
                str_score_name.addAll(Arrays.asList(context.getResources().getStringArray(R.array.exam_name_term_1)));
            } else {
                str_score.add(examResult.getM8());
                str_score.add(examResult.getM9());
                str_score.add(examResult.getM10());

                str_score.add(examResult.getM11());
                str_score.add(examResult.getM12());
                str_score.add(examResult.getM13());
                str_score.add(examResult.getM14());
                str_score.add(examResult.getM15());

//            str_score.add(examResult.getM16());
//            str_score.add(examResult.getM17());
//            str_score.add(examResult.getM18());
//            str_score.add(examResult.getM19());
//            str_score.add(examResult.getM20());
                str_score_name.addAll(Arrays.asList(context.getResources().getStringArray(R.array.exam_name_term_2)));
            }
        }


        @Override
        public ExamResultsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_score, parent, false); //Inflating the layout
            ExamResultsAdapterViewHolder scoreStudentSemesterAdapterViewHolder = new ExamResultsAdapterViewHolder(view, viewType);
            return scoreStudentSemesterAdapterViewHolder;
        }

        @Override
        public void onBindViewHolder(final ExamResultsAdapterViewHolder holder, final int position) {
            View view = holder.view;
            String score = str_score.get(position);

            final TextView lbScoreMonth = ((TextView) (view.findViewById(R.id.lbScoreMonth)));
            final TextView lbScore = ((TextView) (view.findViewById(R.id.lbScore)));
            lbScoreMonth.setText(str_score_name.get(position));
            if (position == 7) {
                view.setBackgroundResource(R.drawable.bg_score_avg_year);
            } else if (position == 6) {
                view.setBackgroundResource(R.drawable.bg_score_avg_term);
            } else if (position == 5) {
                view.setBackgroundResource(R.drawable.bg_score_avg_month);
            } else if (position == 4) {
                view.setBackgroundResource(R.drawable.bg_score_final);
            }
            String sresult = "";
            String notice = "";
            String exam_dt = "";
            if (score != null && !score.trim().isEmpty()) {
                //{“sresult” = “10”;
//                “notice” = “xxxxxx”;
//                “exam_dt” = “2016-09-09”}
                try {
                    JSONObject scoreObj = new JSONObject(score);
                    sresult = scoreObj.getString("sresult");
                    notice = scoreObj.getString("notice");
                    exam_dt = scoreObj.getString("exam_dt");
                    lbScore.setText(sresult);
                    examResult.setSresult(sresult);
                    examResult.setNotice(notice);
                    examResult.setExam_dt(exam_dt);
                } catch (JSONException e) {
                    lbScore.setText(score);
                    e.printStackTrace();
                }
            } else {
                lbScore.setText("");
            }
            final String finalNotice = notice;
            final String finalExam_dt = exam_dt;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    examResult.setSresult(lbScore.getText().toString());
                    examResult.setExam_name(lbScoreMonth.getText().toString());
                    examResult.setNotice(finalNotice);
                    examResult.setExam_dt(finalExam_dt);
                    _showDetailsExamResults(examResult);
                }
            });
        }

        private void _showDetailsExamResults(ExamResult examResult) {
            try {
                AlertDialog.Builder bDetails = new AlertDialog.Builder(context);
                View examResultDetails = View.inflate(context, R.layout.view_exam_results_details, null);
                ((TextView) examResultDetails.findViewById(R.id.lbExamSubject)).setText(String.valueOf(examResult.getSubjectName()));
                ((TextView) examResultDetails.findViewById(R.id.lbExamDate)).setText(examResult.getExam_name());
                String score = examResult.getSresult();
                ((TextView) examResultDetails.findViewById(R.id.lbExamScore)).setText(String.valueOf(score));
                ((TextView) examResultDetails.findViewById(R.id.lbExamTecherName)).setText(String.valueOf(examResult.getTeacherName()));
                ((TextView) examResultDetails.findViewById(R.id.lbExamDateUpdateScore)).setText(" - " + LaoSchoolShared.formatDate(examResult.getExam_dt(), 2));
                ((TextView) examResultDetails.findViewById(R.id.lbExamNotice)).setText(String.valueOf(examResult.getNotice()));
                bDetails.setView(examResultDetails);
                final Dialog dialog = bDetails.create();
                (examResultDetails.findViewById(R.id.lbExamClose)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return str_score.size();
        }

        public class ExamResultsAdapterViewHolder extends RecyclerView.ViewHolder {
            View view;
            int viewType;

            public ExamResultsAdapterViewHolder(View itemView, int viewType) {
                super(itemView);
                this.view = itemView;
                this.viewType = viewType;
            }
        }
    }


}

