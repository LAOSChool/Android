package com.laoschool.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laoschool.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Hue on 5/24/2016.
 */
public class FinalResultsAdapter extends RecyclerView.Adapter<FinalResultsAdapter.FinalResultsAdapterViewHolder> {

    private static final int VIEW_HEARDER = 0;
    private static final int VIEW_EXAM = 1;
    private List<Object> objects;
    private Context context;

    public FinalResultsAdapter(Context context) {
        this.context = context;
        String[] header = new String[]{"1", "2", "3", "4", "Test 1", "6", "7", "8", "9", "Test 2", "Term1", "Term2", "Total"};
        objects = new ArrayList<>();
        // objects.add(1);
        //objects.add(header);
        objects.addAll(Arrays.asList(context.getResources().getStringArray(R.array.subjects)));
    }

    @Override
    public FinalResultsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subject_final_results, parent, false);
//            return new FinalResultsAdapterViewHolder(view, viewType);
//        if (viewType == VIEW_HEARDER) {
//        } else if (viewType == VIEW_EXAM) {
//            View viewHeader = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_header_final_results, parent, false);
//            return new FinalResultsAdapterViewHolder(viewHeader, viewType);
//        } else {
//            return null;
//        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subject_final_results, parent, false);
        return new FinalResultsAdapterViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(FinalResultsAdapterViewHolder holder, int position) {
        View view = holder.view;
//        if (holder.viewType == VIEW_HEARDER) {
//
//        } else if (holder.viewType == VIEW_EXAM) {
            ((TextView) view.findViewById(R.id.lbSubject)).setText(objects.get(position).toString());
//        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (objects.get(position) instanceof String) {
//            return VIEW_EXAM;
//        } else if (objects.get(position) instanceof String[]) {
//            return VIEW_HEARDER;
//        } else {
//            return -1;
//        }
//    }

    public class FinalResultsAdapterViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public FinalResultsAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }
}
