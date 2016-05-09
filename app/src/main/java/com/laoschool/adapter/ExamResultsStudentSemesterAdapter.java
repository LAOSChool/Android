package com.laoschool.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.R;

import java.util.List;

/**
 * Created by Hue on 3/11/2016.
 */
public class ExamResultsStudentSemesterAdapter extends RecyclerView.Adapter<ExamResultsStudentSemesterAdapter.ExamResultsStudentSemesterAdapterViewHolder> {
    private Fragment screen;
    private Context context;
    private List<String> strings;

    private int TYPE_SUB_HEADER = 0;
    private int TYPE_TITLE = 1;
    private int TYPE_LINE = 2;

    public ExamResultsStudentSemesterAdapter(Fragment screen, List<String> strings) {
        this.screen = screen;
        this.context = screen.getActivity();
        this.strings = strings;
    }

    @Override
    public ExamResultsStudentSemesterAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_LINE)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_line, parent, false); //Inflating the layout
        else if (viewType == TYPE_TITLE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exam_results_student, parent, false); //Inflating the layout
        } else if (viewType == TYPE_SUB_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_only_title, parent, false); //Inflating the layout
        }
        ExamResultsStudentSemesterAdapterViewHolder viewHolder = new ExamResultsStudentSemesterAdapterViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExamResultsStudentSemesterAdapterViewHolder holder, int position) {
        View view = holder.view;
        try {
            final String title = strings.get(position);
            if (holder.viewType == TYPE_TITLE) {
//                //Define and set data
                TextView txtSubjectScreenResultsStudent = (TextView) view.findViewById(R.id.txtSubjectScreenResultsStudent);
                txtSubjectScreenResultsStudent.setText(title);
                //Handler on click item
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, title, Toast.LENGTH_SHORT).show();

                    }
                });
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    @Override
    public int getItemViewType(int position) {
        String item = strings.get(position);
        if (item.equals(context.getString(R.string.row_sub_header)))
            return TYPE_SUB_HEADER;
        else if (!item.equals(context.getString(R.string.row_line)))
            return TYPE_TITLE;
        else
            return TYPE_LINE;

    }

    public class ExamResultsStudentSemesterAdapterViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public ExamResultsStudentSemesterAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }
}

