package com.laoschool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laoschool.R;

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
    Map<Integer, ArrayList<String>> scores;
    List<Integer> months = new ArrayList<>();

    public ScoreStudentSemesterAdapter(Context context, Map<Integer, ArrayList<String>> scores) {
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
            String monthStr = "";
            if (month < 100) {
                monthStr = _getMonthString(month);
            } else {
                monthStr = "Final exam";
            }
            List<String> scoreList = scores.get(month);
            if (scoreList.size() > 0) {
                String score = scoreList.get(scoreList.size() - 1);
                Log.d(TAG, " -score:" + score);
                ((TextView) (view.findViewById(R.id.lbScoreMonth))).setText(monthStr);
                ((TextView) (view.findViewById(R.id.lbScore))).setText(score);
            } else {
                ((TextView) (view.findViewById(R.id.lbScoreMonth))).setText("");
                ((TextView) (view.findViewById(R.id.lbScore))).setText("");
            }
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder() - exception=" + e.getMessage());
        }
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
