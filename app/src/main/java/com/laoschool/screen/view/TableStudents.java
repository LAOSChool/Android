package com.laoschool.screen.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
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
    RecyclerView tableStudentView;
    ListStudentsAdapter mAdapter;
    CheckBox checkBox;
    EditText edtSearch;

    List<User> userList;
    List<User> selectedStudents = new ArrayList<>();

    User recentSelectedUser;

    LinearLayout formDialog;

    public interface TableStudentsListener {
        void onBtnDoneClick(List<User> selectedStudents);
        void onBtnCancelClick();
        void onSearch(List<User> searchList);
    }

    private TableStudentsListener listener;

    public TableStudents(Context context, TableStudentsListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    public View getView() {
        final View v = View.inflate(context, R.layout.table_students, null);

        tableStudentView = (RecyclerView) v.findViewById(R.id.tableStudentView);
        checkBox = (CheckBox) v.findViewById(R.id.checkBox);
        formDialog = (LinearLayout) v.findViewById(R.id.formDialog);
        edtSearch = (EditText) v.findViewById(R.id.edtSearch);

        ImageView imgClass = (ImageView) v.findViewById(R.id.imgClass);
        TextView txbClassName = (TextView) v.findViewById(R.id.txbClassName);
        LinearLayout btnDone = (LinearLayout) v.findViewById(R.id.btnDone);
        LinearLayout btnCancel = (LinearLayout) v.findViewById(R.id.btnCancel);

        int color = Color.parseColor("#ffffff");
        imgClass.setColorFilter(color);
        tableStudentView.setHasFixedSize(true);
        txbClassName.setText("Class "+ LaoSchoolShared.selectedClass.getTitle());

        // use a linear layout manager
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        tableStudentView.setLayoutManager(mLayoutManager);

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

        edtSearch.addTextChangedListener(new TextWatcher() {
             public void afterTextChanged(Editable s) {
             }

             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                 /*This method is called to notify you that, within s, the count characters beginning at start are about to be replaced by new text with length after. It is an error to attempt to make changes to s from this callback.*/
             }

             public void onTextChanged(CharSequence s, int start, int before, int count) {
//                 Log.i("edtSearch", "key press!");
                 if(edtSearch.getText().length() == 0) {
                     mAdapter.swap(userList, selectedStudents);
                     if(listener != null)
                         listener.onSearch(userList);

                     mLayoutManager.scrollToPositionWithOffset(userList.indexOf(recentSelectedUser), 20);
                 } else {
                     List<User> searchList = new ArrayList<User>();
                     for(User student: userList)
                         if(student.getFullname().contains(edtSearch.getText()))
                             searchList.add(student);
                     mAdapter.swap(searchList, selectedStudents);
                     if(listener != null)
                         listener.onSearch(searchList);
                 }
             }
        });

        return v;
    }

    public void setData(List<User> list_students, List<User> selected_students) {
        userList = list_students;
        selectedStudents.clear();
        selectedStudents.addAll(selected_students);
        edtSearch.getText().clear();

        if(userList.size() == selectedStudents.size())
            checkBox.setChecked(true);
        else
            checkBox.setChecked(false);

        if(mAdapter == null) {
            mAdapter = new ListStudentsAdapter(this, userList, selectedStudents, context);
            tableStudentView.setAdapter(mAdapter);
        } else {
            mAdapter.swap(userList, selectedStudents);
        }
    }

    public void reset() {
        tableStudentView.smoothScrollToPosition(0);
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

        recentSelectedUser = student;
        edtSearch.getText().clear();
        if(listener != null)
            listener.onSearch(userList);

        View v = formDialog;
        formDialog.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
