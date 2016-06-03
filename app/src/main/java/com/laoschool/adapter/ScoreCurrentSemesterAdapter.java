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

import com.laoschool.R;
import com.laoschool.entities.ExamResult;
import com.laoschool.shared.LaoSchoolShared;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Hue on 6/1/2016.
 */
public class ScoreCurrentSemesterAdapter extends RecyclerView.Adapter<ScoreCurrentSemesterAdapter.ScoreCurrentSemesterAdapterViewHolder> {
    private static final String TAG = ScoreCurrentSemesterAdapter.class.getSimpleName();
    Context context;
    Map<Long, ArrayList<ExamResult>> scores;
    List<Long> exam_dates = new ArrayList<>();

    public ScoreCurrentSemesterAdapter(Context context, Map<Integer, ArrayList<ExamResult>> scores) {
        this.context = context;

        List<ExamResult> examResults = scores.get(LaoSchoolShared.myProfile.getEclass().getTerm());
        Map<Long, ArrayList<ExamResult>> examByMonthList = new HashMap<>();
        for (ExamResult examResult : examResults) {
            int exam_month = examResult.getExam_month();
            int exam_year = examResult.getExam_year();
            long exam_date = LaoSchoolShared.getLongDate(exam_month, exam_year);
            Log.d(TAG, exam_month + "/" + exam_year + "-exam_date:" + exam_date);
            ArrayList<ExamResult> examTermList = null;
            if (examByMonthList.containsKey(exam_date)) {
                examTermList = examByMonthList.get(exam_date);
                if (examTermList == null)
                    examTermList = new ArrayList();
                examTermList.add(examResult);
            } else {
                examTermList = new ArrayList();
                examTermList.add(examResult);
            }
            if (examTermList.size() > 0)
                examByMonthList.put(exam_date, examTermList);

        }

        Map<Long, ArrayList<ExamResult>> examsTree = new TreeMap<>(examByMonthList);
        this.scores = examsTree;
        for (Long key : examsTree.keySet()) {
            exam_dates.add(key);
        }
    }


    @Override
    public ScoreCurrentSemesterAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_score_by_month, parent, false); //Inflating the layout
        ScoreCurrentSemesterAdapterViewHolder scoreStudentSemesterAdapterViewHolder = new ScoreCurrentSemesterAdapterViewHolder(view, viewType);
        return scoreStudentSemesterAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(ScoreCurrentSemesterAdapterViewHolder holder, int position) {
        View view = holder.view;
        try {
            long exam_date = exam_dates.get(position);
            String monthStr = getMonthString(exam_date);
            List<ExamResult> scoreList = scores.get(exam_date);
            if (scoreList.size() > 0) {
                final ExamResult examResult = scoreList.get(scoreList.size() - 1);
                String score = examResult.getSresult();
                ((TextView) (view.findViewById(R.id.lbScoreMonth))).setText(monthStr);
                if (score != null) {
                    if (!score.trim().isEmpty()) {
                        ((TextView) (view.findViewById(R.id.lbScore))).setText(score);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                             //   _showDetailsExamResults(examResult);

                            }
                        });
                    } else {
                        Log.e(TAG, "onBindViewHolder() - score is empty");
                        view.setVisibility(View.GONE);
                    }
                } else {
                    Log.e(TAG, "onBindViewHolder() - score is null");
                }

                if (examResult.getExam_type() == 2) {
                    ((TextView) (view.findViewById(R.id.lbScoreMonth))).setText("Final");
                    view.setBackgroundResource(R.drawable.border_row_score_by_month_final);
                }
            } else {
                Log.e(TAG, "onBindViewHolder() - exam_date:" + exam_date + " list exam is empty");
                view.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder() - exception=" + e.getMessage());
        }
    }

    private void _showDetailsExamResults(ExamResult examResult) {
        AlertDialog.Builder bDetails = new AlertDialog.Builder(context);
        View examResultDetails = View.inflate(context, R.layout.view_exam_results_details, null);
        ((TextView) examResultDetails.findViewById(R.id.lbExamSubject)).setText(String.valueOf(examResult.getSubjectName()));
        if (examResult.getExam_type() == 2)
            ((TextView) examResultDetails.findViewById(R.id.lbExamDate)).setText(examResult.getTermName());
        else {
            ((TextView) examResultDetails.findViewById(R.id.lbExamDate)).setText(String.valueOf(examResult.getExam_month() + "/" + examResult.getExam_year()));
        }
        String score = examResult.getSresult();
        ((TextView) examResultDetails.findViewById(R.id.lbExamScore)).setText(String.valueOf(score));
        ((TextView) examResultDetails.findViewById(R.id.lbExamTecherName)).setText(String.valueOf(examResult.getTeacherName()));
        ((TextView) examResultDetails.findViewById(R.id.lbExamDateUpdateScore)).setText(" - " + LaoSchoolShared.formatDate(examResult.getExam_dt(), 2));
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

    private String getMonthString(long date) {
        DateFormat inputFormatter1 = new SimpleDateFormat("MMM", Locale.US);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date * 1000);
        String monthParse = inputFormatter1.format(cal.getTime());
        //Log.d(TAG, " getMonthString() - month:" + month + ",month parse:" + monthParse);
        return monthParse;
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public class ScoreCurrentSemesterAdapterViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public ScoreCurrentSemesterAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }


}

