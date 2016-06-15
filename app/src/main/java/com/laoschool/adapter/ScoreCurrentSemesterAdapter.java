package com.laoschool.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laoschool.R;
import com.laoschool.entities.ExamResult;
import com.laoschool.screen.ScreenExamResults;
import com.laoschool.screen.view.DialogInputExamResultsForStudent;
import com.laoschool.shared.LaoSchoolShared;

import java.text.DateFormatSymbols;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hue on 6/1/2016.
 */
public class ScoreCurrentSemesterAdapter extends RecyclerView.Adapter<ScoreCurrentSemesterAdapter.ScoreCurrentSemesterAdapterViewHolder> {
    private static final String TAG = ScoreCurrentSemesterAdapter.class.getSimpleName();
    private final Activity activity;
    private final ScreenExamResults screenExamResults;
    private final int subjectId;
    Context context;
    List<ExamResult> scores;
    ScreenExamResults.IScreenExamResults iScreenExamResults;

    public ScoreCurrentSemesterAdapter(ScreenExamResults screenExamResults, int subjectId, Context context, ScreenExamResults.IScreenExamResults iScreenExamResults, List<ExamResult> scores) {
        this.activity = screenExamResults.getActivity();
        this.screenExamResults = screenExamResults;
        this.context = context;
        this.iScreenExamResults = iScreenExamResults;
        this.subjectId = subjectId;

//        List<ExamResult> examResults = scores.get(LaoSchoolShared.myProfile.getEclass().getTerm());
//        Map<Integer, ExamResult> examByMonthList = new HashMap<>();
//        for (ExamResult examResult : examResults) {
//            int exam_id = examResult.getExam_id();
//            examByMonthList.put(exam_id, examResult);
//        }
//
//        Map<Integer, ExamResult> examsTree = new TreeMap<>(examByMonthList);
        this.scores = scores;
//        for (Integer examId : examsTree.keySet()) {
//            exam_Ids.add(examId);
//        }
    }


    @Override
    public ScoreCurrentSemesterAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_score, parent, false); //Inflating the layout
        ScoreCurrentSemesterAdapterViewHolder scoreStudentSemesterAdapterViewHolder = new ScoreCurrentSemesterAdapterViewHolder(view, viewType);
        return scoreStudentSemesterAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(final ScoreCurrentSemesterAdapterViewHolder holder, final int position) {
        View view = holder.view;
        final ExamResult examResult = scores.get(position);
        int exam_month = examResult.getExam_month();
        int exam_type = examResult.getExam_type();

        String monthStr = "DEF";
        if (exam_month > 0) {
            monthStr = getMonthString(examResult.getExam_month());
        }
        String score = examResult.getSresult();

        TextView lbScoreMonth = ((TextView) (view.findViewById(R.id.lbScoreMonth)));
        TextView lbScore = ((TextView) (view.findViewById(R.id.lbScore)));

        if (exam_type == 2) {
            lbScoreMonth.setText(examResult.getExam_name());
            view.setBackgroundResource(R.drawable.bg_score_final);
        } else if (exam_type == 3) {
            lbScoreMonth.setText(examResult.getExam_name());
            view.setBackgroundResource(R.drawable.bg_score_avg_month);
        } else if (exam_type == 4) {
            lbScoreMonth.setText(examResult.getExam_name());
            view.setBackgroundResource(R.drawable.bg_score_avg_term);
        } else {
            lbScoreMonth.setText(monthStr);
        }

        if (score != null && !score.trim().isEmpty()) {
            lbScore.setText(score);
        } else {
            lbScore.setText("");
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick " + position);
//                _showDetailsExamResults(examResult);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (examResult.getExam_type() <= 2) {
                    DialogInputExamResultsForStudent dialogInputExamResultsForStudent = new DialogInputExamResultsForStudent(screenExamResults, subjectId, examResult);
                    dialogInputExamResultsForStudent.show(activity.getFragmentManager(), DialogInputExamResultsForStudent.TAG);

                } else {
                    _showDetailsExamResults(examResult);
                }
                return false;
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

    private String getMonthString(int month) {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(Locale.UK);
        String monthParse = dateFormatSymbols.getShortMonths()[month - 1];
        return monthParse;
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public class ScoreCurrentSemesterAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;
        int viewType;

        public ScoreCurrentSemesterAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
            //this.view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }


}

