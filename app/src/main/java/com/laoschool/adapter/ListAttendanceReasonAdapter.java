package com.laoschool.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laoschool.R;
import com.laoschool.entities.AttendanceReason;
import com.laoschool.screen.view.AttendanceTableAbsent;
import com.laoschool.screen.view.Languages;
import com.laoschool.shared.LaoSchoolShared;

import java.util.List;
import java.util.Locale;

/**
 * Created by Tran An on 7/4/2016.
 */
public class ListAttendanceReasonAdapter extends RecyclerView.Adapter<ListAttendanceReasonAdapter.ViewHolder> {

    private AttendanceTableAbsent attendanceTableAbsent;
    private List<AttendanceReason> mDataset;
    private int selectedIndex;
    private Context context;

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
    public ListAttendanceReasonAdapter(AttendanceTableAbsent attendanceTableAbsent, List<AttendanceReason> myDataset, int selectedIndex, Context context) {
        this.attendanceTableAbsent = attendanceTableAbsent;
        mDataset = myDataset;
        this.selectedIndex = selectedIndex;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListAttendanceReasonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_attendance_reason, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        View v = holder.mView;
        TextView txvReason = (TextView) v.findViewById(R.id.txvReason);
        ImageView imgReasonCheck = (ImageView) v.findViewById(R.id.imgReasonCheck);
        RelativeLayout btnReason = (RelativeLayout) v.findViewById(R.id.btnReason);

        imgReasonCheck.setColorFilter(Color.parseColor("#0099cc"));

        AttendanceReason attendanceReason = mDataset.get(position);

        SharedPreferences prefs = context.getSharedPreferences(
                LaoSchoolShared.SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
        String language = prefs.getString(Languages.PREFERENCES_NAME, null);
        if (language != null && language.equals(Languages.LANGUAGE_LAOS))
            txvReason.setText(attendanceReason.getLval());
        else
            txvReason.setText(attendanceReason.getSval());

        if(position == 0)
            txvReason.setTextColor(context.getResources().getColor(R.color.colorAttendanceNoReason));

        if(position == selectedIndex)
            imgReasonCheck.setVisibility(View.VISIBLE);
        else
            imgReasonCheck.setVisibility(View.INVISIBLE);

        btnReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendanceTableAbsent.setReasonSelected(position);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void swap(List<AttendanceReason> myDataset, int selectedIndex){
        mDataset = myDataset;
        this.selectedIndex = selectedIndex;
        notifyDataSetChanged();
    }

}
