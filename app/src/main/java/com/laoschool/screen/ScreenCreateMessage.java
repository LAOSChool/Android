package com.laoschool.screen;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.entities.Message;
import com.laoschool.entities.User;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.model.sqlite.CRUDMessage;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenCreateMessage extends Fragment implements FragmentLifecycle {


    private static final String CURRENT_ROLE = "current_role";
    private static final String TAG = "ScreenCreateMessage";
    private int containerId;
    private String testMessage;
    private String currentRole;
    private Context context;
    private DataAccessInterface service;

    //Student
    private EditText txtMessageTitleStudent;
    private EditText txtMessageContentStudent;
    private CheckBox cbSendSmsStudent;
    TextView txtMessageTo;

    //Teacher
    private EditText txtMessageTitleTeacher;

    private EditText txtMessageContentTeacher;
    //dbsql
    CRUDMessage crudMessage;


    public void setTestMessage(String testMessage) {
        this.testMessage = testMessage;
    }

    @Override
    public void onPauseFragment() {
        try {
            Log.d(getString(R.string.title_screen_create_message), "onPauseFragment()");
            //Toast.makeText(getActivity(), "onPauseFragment():" + getString(R.string.title_screen_create_message), Toast.LENGTH_SHORT).show();
            _resetForm();
        } catch (Exception e) {
        }
    }

    @Override
    public void onResumeFragment() {
        if (testMessage != null) {
            if (!testMessage.equals("back")) {
                Log.d(getString(R.string.title_screen_create_message), "onPauseFragment()");
                Toast.makeText(getActivity(), "message:" + testMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

    interface IScreenCreateMessage {
        void gotoListStudent();

        void goBackToMessage();
    }

    private IScreenCreateMessage iScreenCreateMessage;

    public ScreenCreateMessage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (currentRole.equals(LaoSchoolShared.ROLE_STUDENT)) {
            return _defineCreateMessageStudent(inflater, container);
        } else if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
            return _defineCreateMessageTeacher(inflater, container);
        } else {
            return _defineCreateMessageStudent(inflater, container);
        }
    }

    private View _defineCreateMessageTeacher(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.screen_create_message_tearcher, container, false);
        txtMessageTitleTeacher = (EditText) view.findViewById(R.id.txtMessageTitleTeacher);
        txtMessageContentTeacher = (EditText) view.findViewById(R.id.txtMessageContentTeacher);
        return view;
    }

    private View _defineCreateMessageStudent(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.screen_create_message_student, container, false);
        txtMessageTitleStudent = (EditText) view.findViewById(R.id.txtMessageTitleStudent);
        txtMessageContentStudent = (EditText) view.findViewById(R.id.txtMessageContentStudent);
        txtMessageTo = (TextView) view.findViewById(R.id.txtConversionMessageTo);
        cbSendSmsStudent = (CheckBox) view.findViewById(R.id.cbSendSmsStudent);
        try {
            service.getUserById(LaoSchoolShared.myProfile.getEclass().getHead_teacher_id(), new AsyncCallback<User>() {
                @Override
                public void onSuccess(User result) {
                    txtMessageTo.setText(result.getFullname());
                }

                @Override
                public void onFailure(String message) {

                }
            });
        } catch (Exception e) {
            //e.printStackTrace();
            Toast.makeText(context, "Server null", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private boolean validateMessageTitle(EditText edit) {
        if (edit.getText().toString().trim().isEmpty()) {
            requestFocus(edit);
            Toast.makeText(context, R.string.err_msg_input_message_title, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateMessageConten(EditText edit) {
        if (edit.getText().toString().trim().isEmpty()) {
            requestFocus(edit);
            Toast.makeText(context, R.string.err_msg_input_message_conten, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            //show soft keyboard
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.context = getActivity();
        service = DataAccessImpl.getInstance(context);
        crudMessage = new CRUDMessage(context);

        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            currentRole = getArguments().getString(CURRENT_ROLE);
            Log.d(getString(R.string.title_screen_create_message), "-Container Id:" + containerId);
        }
        if (testMessage != null) {
            Log.d(getString(R.string.title_screen_create_message), "-Message:" + testMessage);
        } else {
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //set display menu item
        inflater.inflate(R.menu.menu_screen_create_message, menu);

    }

    public static Fragment instantiate(int containerId, String currentRole) {
        ScreenCreateMessage fragment = new ScreenCreateMessage();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        args.putString(CURRENT_ROLE, currentRole);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iScreenCreateMessage = (IScreenCreateMessage) activity;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_send_message:
                _submitForm(currentRole);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void _submitForm(String currentRole) {
        if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
            if (!validateMessageTitle(txtMessageTitleTeacher)) {
                return;
            }
            if (!validateMessageConten(txtMessageContentTeacher)) {
                return;
            }
        } else {
            if (!validateMessageTitle(txtMessageTitleStudent)) {
                return;
            }
            if (!validateMessageConten(txtMessageContentStudent)) {
                return;
            }
            if (LaoSchoolShared.checkConn(context)) {
                if (LaoSchoolShared.myProfile != null) {
                    if (LaoSchoolShared.myProfile.getEclass() != null) {
                        final Message message = new Message();

                        message.setTitle(txtMessageTitleStudent.getText().toString());
                        message.setContent(txtMessageContentStudent.getText().toString());
                        message.setChannel(cbSendSmsStudent.isChecked() ? 0 : 1);

                        message.setFrom_usr_id(LaoSchoolShared.myProfile.getId());
                        message.setTo_usr_id(LaoSchoolShared.myProfile.getEclass().getHead_teacher_id());
                        message.setFrom_user_name(LaoSchoolShared.myProfile.getFullname());
                        message.setTo_user_name(txtMessageTo.getText().toString());
                        message.setClass_id(LaoSchoolShared.myProfile.getEclass().getId());
                        message.setSchool_id(LaoSchoolShared.myProfile.getSchool_id());

                        service.createMessage(message, new AsyncCallback<Message>() {
                            @Override
                            public void onSuccess(Message result) {
                                Log.d(TAG, "Message results:" + result.toJson());
                                Toast.makeText(context, R.string.msg_create_message_sucessfully, Toast.LENGTH_SHORT).show();
                                // save local
                                crudMessage.addMessage(result);
                                _resetForm();
                                iScreenCreateMessage.goBackToMessage();
                            }

                            @Override
                            public void onFailure(String message1) {
                                Toast.makeText(context, R.string.err_msg_create_message, Toast.LENGTH_SHORT).show();
                                Log.d(TAG, R.string.err_msg_create_message + ":" + message1);
                                _resetForm();
                                iScreenCreateMessage.goBackToMessage();
                            }
                        });

                    } else {
                        Toast.makeText(context, R.string.err_msg_create_message, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, R.string.err_msg_create_message + "_1");

                    }
                } else {
                    Toast.makeText(context, R.string.err_msg_create_message, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, R.string.err_msg_create_message + "_2");
                }
            } else {
                Toast.makeText(context, R.string.err_msg_network_disconnect, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void _resetForm() {
        if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
            txtMessageTitleTeacher.getText().clear();
            txtMessageContentTeacher.getText().clear();
            txtMessageTitleTeacher.clearFocus();
            txtMessageContentTeacher.clearFocus();
        } else {
            txtMessageTitleStudent.getText().clear();
            txtMessageContentStudent.getText().clear();
            txtMessageTitleStudent.clearFocus();
            txtMessageContentStudent.clearFocus();
            cbSendSmsStudent.setChecked(false);
        }
    }
}
