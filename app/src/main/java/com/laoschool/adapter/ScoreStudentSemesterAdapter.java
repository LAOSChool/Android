package com.laoschool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
    Map<String, String> scores;
    List<String> months = new ArrayList<>();

    public ScoreStudentSemesterAdapter(Context context, Map<String, String> scores) {
        this.context = context;
        this.scores = scores;

        for (String key : scores.keySet()) {
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
        String month = months.get(position);
        String score = scores.get(month);
        ((TextView) (view.findViewById(R.id.lbScoreMonth))).setText(month);
        ((TextView) (view.findViewById(R.id.lbScore))).setText(score);
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
