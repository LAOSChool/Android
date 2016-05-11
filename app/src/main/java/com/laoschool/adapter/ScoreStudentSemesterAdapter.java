package com.laoschool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laoschool.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Hue on 5/9/2016.
 */
public class ScoreStudentSemesterAdapter extends RecyclerView.Adapter<ScoreStudentSemesterAdapter.ScoreStudentSemesterAdapterViewHolder> {
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
            ((TextView) (view.findViewById(R.id.lbScoreMonth))).setText(String.valueOf(month));
            List<String> scoreList = scores.get(month);
            if (scoreList.size() > 0) {
                String score = scoreList.get(scoreList.size() - 1);
                ((TextView) (view.findViewById(R.id.lbScore))).setText(score);
            } else {
                ((TextView) (view.findViewById(R.id.lbScore))).setText("");
            }
        } catch (Exception e) {
            Log.e("ScoreAdapter", "onBindViewHolder() Exception=" + e.getMessage());
        }
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
