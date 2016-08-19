package com.laoschool.screen;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.model.AsyncCallback;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenSetting extends Fragment implements FragmentLifecycle {

    ScreenSetting thiz = this;

    EditText txbOldPassword;
    EditText txbNewPassword;
    EditText txbConfirmPassword;

    public ScreenSetting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//
        View view = inflater.inflate(R.layout.screen_setting, container, false);
//        RecyclerView mRecylerViewScreenSetting = (RecyclerView) view.findViewById(R.id.mRecylerViewScreenSetting);

        //init adapter
//        final List<String> settings = Arrays.asList(getResources().getStringArray(R.array.settings));
//        RecylerViewScreenSettingAdapter adapter = new RecylerViewScreenSettingAdapter(this, settings);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);

        //set adapter
//        mRecylerViewScreenSetting.setLayoutManager(gridLayoutManager);
//        mRecylerViewScreenSetting.setAdapter(adapter);

        final LinearLayout rootLayout = (LinearLayout) view.findViewById(R.id.rootLayout);
        txbOldPassword = (EditText) view.findViewById(R.id.txbOldPassword);
        txbNewPassword = (EditText) view.findViewById(R.id.txbNewPassword);
        txbConfirmPassword = (EditText) view.findViewById(R.id.txbConfirmPassword);
        TextView btnSubmit = (TextView) view.findViewById(R.id.btnSubmit);
        TextView txvOldPass = (TextView) view.findViewById(R.id.txvOldPass);
        TextView txvNewPass = (TextView) view.findViewById(R.id.txvNewPass);
        TextView txvConfirmPass = (TextView) view.findViewById(R.id.txvConfirmPass);

        txvOldPass.setText(R.string.SCChangePass_OldPass);
        txvNewPass.setText(R.string.SCChangePass_NewPass);
        txvConfirmPass.setText(R.string.SCChangePass_ConfirmPass);
        btnSubmit.setText(R.string.SCCommon_Submit);

        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    rootLayout.requestFocus();
                    InputMethodManager imm = (InputMethodManager) thiz.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootLayout.requestFocus();
                InputMethodManager imm = (InputMethodManager) thiz.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                String username = LaoSchoolShared.myProfile.getSso_id();
                String old_pass = txbOldPassword.getText().toString();
                String new_pass = txbNewPassword.getText().toString();
                String confirm_pass = txbConfirmPassword.getText().toString();

                if(new_pass.length() < 4)
                    Toast.makeText(thiz.getContext(), "Password is at least 4 character!", Toast.LENGTH_SHORT).show();
                else if(!new_pass.equals(confirm_pass)) {
                    Toast.makeText(thiz.getContext(), "New password and confirm password not match!", Toast.LENGTH_SHORT).show();
                }
                else {
                    final ProgressDialog dialog = ProgressDialog.show(thiz.getActivity(), "Please wait ...", "Sending ...", true);
                    LaoSchoolSingleton.getInstance().getDataAccessService().userChangePassword(username, old_pass, new_pass, new AsyncCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            dialog.dismiss();
                            new AlertDialog.Builder(thiz.getContext())
                                    .setTitle("")
                                    .setMessage("Your password has been change.")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })
//                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // do nothing
//                                    }
//                                })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }

                        @Override
                        public void onFailure(String message) {
                            dialog.dismiss();
                            if (message.contains("password is not correct"))
                                Toast.makeText(thiz.getContext(), message, Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(thiz.getContext(), "Some error occur!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAuthFail(String message) {
                            LaoSchoolShared.goBackToLoginPage(thiz.getContext());
                        }
                    });
                }
            }
        });

        return view;
    }

    public static Fragment instantiate(int containerId, String currentRole) {
        return new ScreenSetting();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
        txbOldPassword.setText("");
        txbNewPassword.setText("");
        txbConfirmPassword.setText("");
    }
}
