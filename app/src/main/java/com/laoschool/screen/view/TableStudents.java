package com.laoschool.screen.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.adapter.ListStudentsAdapter;
import com.laoschool.entities.User;
import com.laoschool.shared.LaoSchoolShared;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tran An on 5/25/2016.
 */
public class TableStudents extends View {

    Context context;
    CheckBox checkBox;

    List<User> userList = new ArrayList<>();
    List<User> selectedStudents = new ArrayList<>();

    LinearLayout formDialog;

    public interface TableStudentsListener {
        void onBtnDoneClick(List<User> selectedStudents);
        void onBtnCancelClick();
    }

    private TableStudentsListener listener;

    public TableStudents(Context context, TableStudentsListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    public View getView(List<User> list_students, List<User> selected_students) {
        final View v = View.inflate(context, R.layout.table_students, null);
        userList.addAll(list_students);
        selectedStudents.addAll(selected_students);

        ImageView imgClass = (ImageView) v.findViewById(R.id.imgClass);
        TextView txbClassName = (TextView) v.findViewById(R.id.txbClassName);
        RecyclerView tableStudentView = (RecyclerView) v.findViewById(R.id.tableStudentView);
        checkBox = (CheckBox) v.findViewById(R.id.checkBox);
        LinearLayout btnDone = (LinearLayout) v.findViewById(R.id.btnDone);
        LinearLayout btnCancel = (LinearLayout) v.findViewById(R.id.btnCancel);

        formDialog = (LinearLayout) v.findViewById(R.id.formDialog);

        int color = Color.parseColor("#ffffff");
        imgClass.setColorFilter(color);
        tableStudentView.setHasFixedSize(true);
        txbClassName.setText("Class "+ LaoSchoolShared.selectedClass.getTitle());

        if(userList.size() == selectedStudents.size())
            checkBox.setChecked(true);
        else
            checkBox.setChecked(false);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        tableStudentView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        final ListStudentsAdapter mAdapter = new ListStudentsAdapter(this, userList, selectedStudents, context);
        tableStudentView.setAdapter(mAdapter);

        btnDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onBtnDoneClick(selectedStudents);
            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onBtnCancelClick();
            }
        });

        formDialog.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    formDialog.requestFocus();
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });

        checkBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()) {
                    selectedStudents.clear();
                    selectedStudents.addAll(userList);
                    mAdapter.fullCheck(true);
                }
                else {
                    selectedStudents.clear();
                    mAdapter.fullCheck(false);
                }
            }
        });

        return v;
    }

    public void setSelectedStudents(boolean isSelect, User student) {
        if(isSelect) {
            if(!selectedStudents.contains(student))
                selectedStudents.add(student);
        }
        else {
            if(selectedStudents.contains(student))
                selectedStudents.remove(student);
        }

        if(selectedStudents.size() == userList.size())
            checkBox.setChecked(true);
        else
            checkBox.setChecked(false);

        View v = formDialog;
        formDialog.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
