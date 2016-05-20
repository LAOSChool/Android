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
        TextView btnSubmit = (TextView) view.findViewById(R.id.btnSubmit);

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

                final ProgressDialog dialog = ProgressDialog.show(thiz.getActivity(), "Please wait ...", "Sending ...", true);

                String user_id = LaoSchoolShared.myProfile.getSso_id();
                String old_pass = txbOldPassword.getText().toString();
                String new_pass = txbNewPassword.getText().toString();
                LaoSchoolSingleton.getInstance().getDataAccessService().userChangePassword(user_id, old_pass, new_pass, new AsyncCallback<String>() {
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
                        if(message.contains("password is not correct"))
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
    }
}
