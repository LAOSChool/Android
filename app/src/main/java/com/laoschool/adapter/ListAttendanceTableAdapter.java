package com.laoschool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.laoschool.R;
import com.laoschool.entities.User;

import java.util.List;
import java.util.Random;

/**
 * Created by Tran An on 6/2/2016.
 */
public class ListAttendanceTableAdapter extends RecyclerView.Adapter<ListAttendanceTableAdapter.ViewHolder> {

    private List<User> mDataset;
    private Context context;

    boolean showdata = false;

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
    public ListAttendanceTableAdapter(List<User> listStudent, Context context) {
        mDataset = listStudent;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListAttendanceTableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_attendance_table, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        View v = holder.mView;

        TextView txtStudentName = (TextView) v.findViewById(R.id.txtStudentName);
        ImageView icDH = (ImageView) v.findViewById(R.id.icDH);
        ImageView icCP = (ImageView) v.findViewById(R.id.icCP);
        ImageView icKP = (ImageView) v.findViewById(R.id.icKP);

        User student = mDataset.get(position);

        txtStudentName.setText(student.getFullname());

        Random rand = new Random();;
        int randomNum = rand.nextInt((3 - 1) + 1) + 1;
        if(showdata) {
            if(randomNum == 1)
                icDH.setVisibility(View.VISIBLE);
            if(randomNum == 2)
                icCP.setVisibility(View.VISIBLE);
            if(randomNum == 3)
                icKP.setVisibility(View.VISIBLE);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void swap() {
        showdata = true;
        notifyDataSetChanged();
    }

}
