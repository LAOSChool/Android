package com.laoschool.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.entities.ExamResult;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Hue on 5/9/2016.
 */
public class ScoreStudentSemesterAdapter extends RecyclerView.Adapter<ScoreStudentSemesterAdapter.ScoreStudentSemesterAdapterViewHolder> {
    private static final String TAG = ScoreStudentSemesterAdapter.class.getSimpleName();
    Context context;
    Map<Integer, ArrayList<ExamResult>> scores;
    List<Integer> months = new ArrayList<>();

    public ScoreStudentSemesterAdapter(Context context, Map<Integer, ArrayList<ExamResult>> scores) {
        this.context = context;
        this.scores = scores;

        for (Integer key : scores.keySet()) {
            months.add(key);
        }
    }


    @Override
    public ScoreStudentSemesterAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_score_by_month, parent, false); //Inflating the layout
        ScoreStudentSemesterAdapterViewHolder scoreStudentSemesterAdapterViewHolder = new ScoreStudentSemesterAdapterViewHolder(view, viewType);
        return scoreStudentSemesterAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(ScoreStudentSemesterAdapterViewHolder holder, int position) {
        View view = holder.view;
        try {
            int month = months.get(position);
            String monthStr;
            if (month < 100) {
                monthStr = _getMonthString(month);
            } else {
                monthStr = "Final exam";
            }
            List<ExamResult> scoreList = scores.get(month);
            if (scoreList.size() > 0) {
                final ExamResult examResult = scoreList.get(scoreList.size() - 1);
                String score = "";
                if (examResult.getResult_type_id() == 1) {
                    score = String.valueOf(examResult.getIresult());
                } else if (examResult.getResult_type_id() == 2) {
                    score = String.valueOf(examResult.getFresult());
                }
                if (!score.trim().isEmpty()) {
                    ((TextView) (view.findViewById(R.id.lbScoreMonth))).setText(monthStr);
                    ((TextView) (view.findViewById(R.id.lbScore))).setText(score);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            _showDetailsExamResults(examResult);

                        }
                    });
                } else {
                    Log.e(TAG, "onBindViewHolder() - score is empty");
                }
            } else {
                ((TextView) (view.findViewById(R.id.lbScoreMonth))).setText("");
                ((TextView) (view.findViewById(R.id.lbScore))).setText("");
            }
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder() - exception=" + e.getMessage());
        }
    }

    private void _showDetailsExamResults(ExamResult examResult) {
        AlertDialog.Builder bDetails = new AlertDialog.Builder(context);
        View examResultDetails = View.inflate(context, R.layout.view_exam_results_details, null);
        ((TextView) examResultDetails.findViewById(R.id.lbExamSubject)).setText(String.valueOf(examResult.getSubjectName()));
        ((TextView) examResultDetails.findViewById(R.id.lbExamDate)).setText(String.valueOf(examResult.getExam_month() + "/" + examResult.getExam_year()));
        ((TextView) examResultDetails.findViewById(R.id.lbExamScore)).setText(String.valueOf(examResult.getIresult()));
        ((TextView) examResultDetails.findViewById(R.id.lbExamTecherName)).setText(String.valueOf(examResult.getTeacherName()));
        ((TextView) examResultDetails.findViewById(R.id.lbExamDateUpdateScore)).setText(String.valueOf(examResult.getExam_dt()));
        ((TextView) examResultDetails.findViewById(R.id.lbExamNotice)).setText(String.valueOf(examResult.getNotice()));
        bDetails.setCustomTitle(examResultDetails);
        final Dialog dialog = bDetails.create();
        ((TextView) examResultDetails.findViewById(R.id.lbExamClose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private String _getMonthString(int month) {
        DateFormat inputFormatter1 = new SimpleDateFormat("MMM", Locale.US);
        Calendar cal = Calendar.getInstance();
        cal.set(2016, month, 10);
        return inputFormatter1.format(cal.getTime());
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public class ScoreStudentSemesterAdapterViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public ScoreStudentSemesterAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }
}
