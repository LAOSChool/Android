package com.laoschool.screen.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.laoschool.R;
import com.laoschool.adapter.ListSubjectAdapter;
import com.laoschool.entities.TimeTable;

import java.util.List;

/**
 * Created by Tran An on 6/2/2016.
 */
public class TableSubject extends View {

    Context context;
    RecyclerView tableSubjectView;

    public interface TableSubjectListener {
        void onSelectSubject(TimeTable timeTable);
    }

    private TableSubjectListener listener;

    public TableSubject(Context context, TableSubjectListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    public View getView() {
        final View v = View.inflate(context, R.layout.view_subject_list, null);

        tableSubjectView = (RecyclerView) v.findViewById(R.id.tableSubjectView);
        ImageView imageView15 = (ImageView) v.findViewById(R.id.imageView15);

        imageView15.setColorFilter(Color.parseColor("#ffffff"));

        // use a linear layout manager
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        tableSubjectView.setLayoutManager(mLayoutManager);
        tableSubjectView.setHasFixedSize(true);

        return v;
    }

    public void setListSubject(List<TimeTable> timeTables) {
        ListSubjectAdapter mAdapter = new ListSubjectAdapter(timeTables, context, this);
        tableSubjectView.setAdapter(mAdapter);
    }

    public void setSelectedSubject(TimeTable timeTable) {
        if(listener != null)
            listener.onSelectSubject(timeTable);
    }
}
