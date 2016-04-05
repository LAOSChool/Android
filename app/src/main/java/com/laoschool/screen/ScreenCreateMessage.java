package com.laoschool.screen;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.entities.Message;
import com.laoschool.entities.User;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenCreateMessage extends Fragment implements FragmentLifecycle {


    private static final String CURRENT_ROLE = "current_role";
    private int containerId;
    private String testMessage;
    private String currentRole;
    private Context context;

    EditText txtTitle;
    TextInputLayout inputLayoutTitle;


    public void setTestMessage(String testMessage) {
        this.testMessage = testMessage;
    }

    @Override
    public void onPauseFragment() {
        Log.d(getString(R.string.title_screen_create_message), "onPauseFragment()");
        //Toast.makeText(getActivity(), "onPauseFragment():" + getString(R.string.title_screen_create_message), Toast.LENGTH_SHORT).show();
        _resetForm();
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
        return view;
    }

    private View _defineCreateMessageStudent(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.screen_create_message_student, container, false);
        txtTitle = (EditText) view.findViewById(R.id.txtMessageTitleStudent);
        inputLayoutTitle = (TextInputLayout) view.findViewById(R.id.input_layout_message_title_student);

//        Log.d("Create Message:", "-Tag:" + getTag());
        view.findViewById(R.id.btnGetStudent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iScreenCreateMessage.gotoListStudent();
            }
        });
        return view;
    }

    private boolean validateMessageTitle() {
        if (txtTitle.getText().toString().trim().isEmpty()) {
            inputLayoutTitle.setError(getString(R.string.err_msg_input_message_title));
            requestFocus(txtTitle);
            return false;
        } else {
            inputLayoutTitle.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.context = getActivity();
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
                _submitForm();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void _submitForm() {
        if (!validateMessageTitle()) {
            return;
        }

        //call API Send Message
        final DataAccessInterface service = DataAccessImpl.getInstance(context);
        Message message=new Message();
        //message.setContent();

        Toast.makeText(context, "Send Message", Toast.LENGTH_SHORT).show();
        iScreenCreateMessage.goBackToMessage();
        _resetForm();
    }

    private void _resetForm() {
        txtTitle.getText().clear();
        inputLayoutTitle.setErrorEnabled(false);
        txtTitle.clearFocus();
    }
}
