package com.laoschool.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.User;
import com.laoschool.model.AsyncCallback;
import com.laoschool.screen.view.TableStudents;
import com.laoschool.shared.LaoSchoolShared;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tran An on 5/25/2016.
 */
public class ListStudentsAdapter extends RecyclerView.Adapter<ListStudentsAdapter.ViewHolder> {

    private TableStudents tableStudents;
    private List<User> userList;
    private List<User> selectedUserList;
    private Context context;

    private List<CheckBox> checkBoxes = new ArrayList<>();

    Bitmap bitmap;
    ImageView imgStudent;

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
    public ListStudentsAdapter(TableStudents tableStudents, List<User> userList, List<User> selectedUserList, Context context) {
        this.tableStudents = tableStudents;
        this.userList = userList;
        this.selectedUserList = selectedUserList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListStudentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_table_student, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        View v = holder.mView;

        imgStudent = (ImageView) v.findViewById(R.id.imgStudent);
        TextView studentName = (TextView) v.findViewById(R.id.txbStudentName);
        RelativeLayout btnSelected = (RelativeLayout) v.findViewById(R.id.btnSelected);
        final CheckBox cbStudentPicker = (CheckBox) v.findViewById(R.id.cbStudentPicker);
        checkBoxes.add(cbStudentPicker);

        final User student = userList.get(position);

        studentName.setText(student.getFullname());
        if(selectedUserList.contains(student))
            cbStudentPicker.setChecked(true);
        else
            cbStudentPicker.setChecked(false);

        btnSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cbStudentPicker.isChecked())
                    cbStudentPicker.setChecked(false);
                else
                    cbStudentPicker.setChecked(true);
                HandlerCheckEvent(cbStudentPicker, student);
            }
        });

        cbStudentPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandlerCheckEvent(cbStudentPicker, student);
            }
        });

        LaoSchoolSingleton.getInstance().getDataAccessService().getImageBitmap(student.getPhoto(), new AsyncCallback<Bitmap>() {
            @Override
            public void onSuccess(Bitmap result) {
                if(result != null)
                    imgStudent.setImageBitmap(result);
            }

            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onAuthFail(String message) {

            }
        });
    }

    void HandlerCheckEvent(CheckBox checkBox, User student) {
        tableStudents.setSelectedStudents(checkBox.isChecked(), student);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void fullCheck(boolean isCheck){
        if(isCheck) {
            selectedUserList.clear();
            selectedUserList.addAll(userList);
            for(CheckBox checkBox: checkBoxes)
                checkBox.setChecked(true);
        }
        else {
            selectedUserList.clear();
            for(CheckBox checkBox: checkBoxes)
                checkBox.setChecked(false);
        }
    }
}
