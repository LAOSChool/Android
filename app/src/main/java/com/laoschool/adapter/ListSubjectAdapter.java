package com.laoschool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laoschool.R;
import com.laoschool.entities.TimeTable;
import com.laoschool.screen.view.TableSubject;

import java.util.List;

/**
 * Created by Tran An on 6/2/2016.
 */
public class ListSubjectAdapter extends RecyclerView.Adapter<ListSubjectAdapter.ViewHolder> {

    private List<TimeTable> mDataset;
    private Context context;
    private TableSubject tableSubject;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListSubjectAdapter(List<TimeTable> timeTableList, Context context, TableSubject tableSubject) {
        mDataset = timeTableList;
        this.context = context;
        this.tableSubject = tableSubject;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListSubjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subject, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        View v = holder.mView;

        TextView txtSubjectName = (TextView) v.findViewById(R.id.txtSubjectName);
        RelativeLayout btnRowSubject = (RelativeLayout) v.findViewById(R.id.btnRowSubject);

        final TimeTable timeTable = mDataset.get(position);

        txtSubjectName.setText("Tiet "+ (position+1) + " - "+ timeTable.getSubject_Name());

        btnRowSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableSubject.setSelectedSubject(timeTable);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
