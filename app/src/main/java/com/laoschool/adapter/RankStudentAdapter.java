package com.laoschool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laoschool.R;
import com.laoschool.entities.Ranking;
import com.laoschool.entities.StudentRanking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Hue on 7/6/2016.
 */
public class RankStudentAdapter extends RecyclerView.Adapter<RankStudentAdapter.ViewHolder> {
    private static final String TAG = RankStudentAdapter.class.getSimpleName();
    List<String> examTypeNames = new ArrayList<>();
    List<Ranking> rankings = new ArrayList<>();

    public RankStudentAdapter(Context context, StudentRanking result) {
        examTypeNames.addAll(Arrays.asList(context.getResources().getStringArray(R.array.examTypeNames)));
        if (result.getRankings() != null) rankings.addAll(result.getRankings());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rank_of_exam_type, parent, false); //Inflating the layout
        ViewHolder viewHolder = new ViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        View view = holder.view;
        try {
            TextView txtExamTypeName = (TextView) view.findViewById(R.id.txtExamTypeName);
            TextView lbAve = (TextView) view.findViewById(R.id.lbAve);
            TextView lbGrade = (TextView) view.findViewById(R.id.lbGrade);
            TextView lbAllocation = (TextView) view.findViewById(R.id.lbAllocation1);
//            if (rankings.get(position) != null) {
            txtExamTypeName.setText(examTypeNames.get(position));
            lbAve.setText(rankings.get(position).getAve());
            lbGrade.setText(rankings.get(position).getGrade());
            lbAllocation.setText(rankings.get(position).getAllocation());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.d(TAG, "bind ranking");
        }

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }
}
