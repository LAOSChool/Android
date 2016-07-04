package com.laoschool.screen;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.adapter.RecyclerViewConversionMessageAdapter;
import com.laoschool.entities.Message;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.model.sqlite.DataAccessMessage;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenMessageDetails extends Fragment implements FragmentLifecycle {


    private static final String TAG = "ScreenMessageDetails";
    private int containerId;
    private TextView txtTilteMessageDetails;
    private TextView txtContentMessageDetails;
    private TextView txtDateMessageDetails;
    private TextView txtFormUserNameMessageDetails;
    private TextView txtToUserNameMessageDetails;
    private ImageView imgPiorityMessageDetails;
    private NetworkImageView imgUserSentMessageAvata;


    private RecyclerView mRecylerViewConversionMessage;
    private Context context;
    private DataAccessInterface service;
    private DataAccessMessage dataAccessMessage;


    EditText txtConversionMessage;
    TextView btnSendMesasage;
    CheckBox cbSwichUser;

    CheckBox cbSMS;

    public ScreenMessageDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            Log.d(getString(R.string.title_screen_message_details), "-Container Id:" + containerId);
        }
        this.context = getActivity();
        this.service = DataAccessImpl.getInstance(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.screen_message_details, container, false);
        txtTilteMessageDetails = (TextView) view.findViewById(R.id.txtTilteMessageDetails);
        txtContentMessageDetails = (TextView) view.findViewById(R.id.txtContentMessageDetails);
        txtDateMessageDetails = (TextView) view.findViewById(R.id.txtDateMessageDetails);
        txtFormUserNameMessageDetails = (TextView) view.findViewById(R.id.txtFormUserNameMessageDetails);
        txtToUserNameMessageDetails = (TextView) view.findViewById(R.id.txtToUserNameMessageDetails);
        imgPiorityMessageDetails = (ImageView) view.findViewById(R.id.imgPiorityMessageDetails);
        imgUserSentMessageAvata = (NetworkImageView) view.findViewById(R.id.imgUserSentMessageAvata);

        //init Conversion list
        mRecylerViewConversionMessage = (RecyclerView) view.findViewById(R.id.mRecylerViewConversionMessage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecylerViewConversionMessage.setLayoutManager(linearLayoutManager);


        txtConversionMessage = (EditText) view.findViewById(R.id.txtConversionMessage);
        btnSendMesasage = (TextView) view.findViewById(R.id.btnSendMesasage);

        cbSwichUser = (CheckBox) view.findViewById(R.id.cbSwichUser);

        cbSMS = (CheckBox) view.findViewById(R.id.cbSMS);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_screen_message_details, menu);
    }


    @Override
    public void onPauseFragment() {
    }

    @Override
    public void onResumeFragment() {
        try {
            String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_MESSAGE_0);
            ScreenMessage screenMessage = (ScreenMessage) ((HomeActivity) getActivity()).getSupportFragmentManager().findFragmentByTag(tag);
            if (screenMessage != null) {
                Message message = screenMessage.getMessage();

                //set Title Message
                ((HomeActivity) getActivity()).getSupportActionBar().setTitle(message.getFrom_user_name());

                txtTilteMessageDetails.setText(message.getTitle());
                txtContentMessageDetails.setText(message.getContent());
                txtDateMessageDetails.setText(LaoSchoolShared.formatDate(message.getSent_dt(), 2));
                if (message.getImp_flg() == 0) {
                    imgPiorityMessageDetails.setColorFilter(screenMessage.getActivity().getResources().getColor(R.color.colorPriorityLow));
                    imgPiorityMessageDetails.setTag(R.color.colorPriorityLow);
                } else {
                    imgPiorityMessageDetails.setColorFilter(
                            screenMessage.getActivity().getResources().getColor(R.color.colorPriorityHigh));
                    imgPiorityMessageDetails.setTag(R.color.colorPriorityHigh);
                }
                // _handlerImagePriotyClick(message);
                if (message.getFrm_user_photo() != null) {
                    LaoSchoolSingleton.getInstance().getImageLoader().get(message.getFrm_user_photo(), ImageLoader.getImageListener(imgUserSentMessageAvata,
                            R.drawable.ic_account_circle_black_36dp, android.R.drawable
                                    .ic_dialog_alert));
                    imgUserSentMessageAvata.setImageUrl(message.getFrm_user_photo(), LaoSchoolSingleton.getInstance().getImageLoader());
                } else {
                    imgUserSentMessageAvata.setDefaultImageResId(R.drawable.ic_account_circle_black_36dp);
                }

                txtFormUserNameMessageDetails.setText(message.getFrom_user_name());
                txtToUserNameMessageDetails.setText("to " + message.getTo_user_name());

                //set Adaper
//            List<Message> messages=dataAccessMessage.getAllMessages();
//            _setConversionMessages(messages);
//                service.getMessages("", "", "", "", "", "", "", "", new AsyncCallback<List<Message>>() {
//                    @Override
//                    public void onSuccess(List<Message> result) {
//                        _setConversionMessages(result);
//                    }
//
//                    @Override
//                    public void onFailure(String message) {
//                        Log.d(TAG, "set Adaper error:" + message);
//                    }
//                });
//
//
//                //Handler onclic send
//                btnSendMesasage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        _sendMessage();
//                    }
//                });
            }
        } catch (Exception e) {
            Log.e(TAG, "onResumeFragment() Exception=" + e.getMessage());
        }
    }

    private void _handlerImagePriotyClick(final Message message) {
        imgPiorityMessageDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgPiorityMessageDetails.getTag().equals(R.color.colorDefault)) {
                    imgPiorityMessageDetails.setColorFilter(context.getResources().getColor(R.color.colorStar));
                    imgPiorityMessageDetails.setTag(R.color.colorStar);
                    message.setImp_flg(1);
                } else {
                    imgPiorityMessageDetails.setColorFilter(context.getResources().getColor(R.color.colorDefault));
                    imgPiorityMessageDetails.setTag(R.color.colorDefault);
                    message.setImp_flg(0);
                }
                DataAccessMessage.updateMessage(message);
                _updatePriotyFormServer(message);
            }
        });
    }

    private void _updatePriotyFormServer(Message message) {
        final ProgressDialog ringProgressDialog = new ProgressDialog(this.getActivity());
        ringProgressDialog.setTitle("Please wait ...");
        ringProgressDialog.setMessage("Loading ...");
        ringProgressDialog.setIndeterminate(false);
        ringProgressDialog.show();
        service.updateMessageIsFlag(message, new AsyncCallback<Message>() {
            @Override
            public void onSuccess(Message result) {
                ringProgressDialog.dismiss();
            }

            @Override
            public void onFailure(String message) {
                ringProgressDialog.dismiss();
            }

            @Override
            public void onAuthFail(String message) {
                LaoSchoolShared.goBackToLoginPage(context);
            }
        });
    }

    private void _setConversionMessages(List<Message> messages) {
        RecyclerViewConversionMessageAdapter viewConversionMessage = new RecyclerViewConversionMessageAdapter(getActivity(), messages);
        mRecylerViewConversionMessage.setAdapter(viewConversionMessage);

        //Scroll to bottom
        mRecylerViewConversionMessage.scrollToPosition(messages.size() - 1);
    }


    public static Fragment instantiate(int containerId, String currentRole) {
        ScreenMessageDetails fragment = new ScreenMessageDetails();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        fragment.setArguments(args);
        return fragment;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            //show soft keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private boolean validateMessageTitle(EditText edit) {
        if (edit.getText().toString().trim().isEmpty()) {
            requestFocus(edit);
            Toast.makeText(context, R.string.SCCreateMessage_err_msg_input_message_title, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void _sendMessage() {
        if (!validateMessageTitle(txtConversionMessage)) {
            return;
        }
        if (LaoSchoolShared.checkConn(context)) {
            if (LaoSchoolShared.myProfile != null) {
                String messageValue = txtConversionMessage.getText().toString();
                if (LaoSchoolShared.myProfile.getEclass() != null) {
                    final Message message = new Message();

                    message.setTitle("Title:" + messageValue);
                    message.setContent(messageValue);
                    message.setChannel(0);

                    //
                    if (cbSwichUser.isChecked()) {
                        message.setFrom_usr_id(LaoSchoolShared.myProfile.getEclass().getHead_teacher_id());
                        message.setTo_usr_id(LaoSchoolShared.myProfile.getId());
                    } else {
                        message.setFrom_usr_id(LaoSchoolShared.myProfile.getId());
                        message.setTo_usr_id(LaoSchoolShared.myProfile.getEclass().getHead_teacher_id());
                    }

                    message.setFrom_user_name(LaoSchoolShared.myProfile.getFullname());
                    message.setTo_user_name("");
                    message.setClass_id(LaoSchoolShared.myProfile.getEclass().getId());

                    message.setChannel((cbSMS.isChecked()) ? 0 : 1);

                    service.createMessage(message, new AsyncCallback<Message>() {
                        @Override
                        public void onSuccess(Message result) {
                            Log.d(TAG, "Message results:" + result.toJson());
                            // save local
                            dataAccessMessage.addMessage(result);
                            _resetForm();
                            //Reload conversion
                            _refeshData();
                        }

                        @Override
                        public void onFailure(String message1) {
                            Log.d(TAG, R.string.SCCreateMessage_err_msg_create_message + ":" + message1);
                            _resetForm();
                        }

                        @Override
                        public void onAuthFail(String message) {
                            LaoSchoolShared.goBackToLoginPage(context);
                        }
                    });

                } else {
                    Toast.makeText(context, R.string.SCCreateMessage_err_msg_create_message, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, R.string.SCCreateMessage_err_msg_create_message + "_1");

                }
            } else {
                Toast.makeText(context, R.string.SCCreateMessage_err_msg_create_message, Toast.LENGTH_SHORT).show();
                Log.d(TAG, R.string.SCCreateMessage_err_msg_create_message + "_2");
            }
        } else {
            Toast.makeText(context, R.string.SCCommon_err_msg_network_disconnect, Toast.LENGTH_SHORT).show();
        }

    }

    private void _refeshData() {
        //set Adaper
//        List<Message> messages = dataAccessMessage.getAllMessages();
//        _setConversionMessages(messages);
        service.getMessages("", "", "", "", "", "", "", "", new AsyncCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> result) {
                _setConversionMessages(result);
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "set Adaper error:" + message);
            }

            @Override
            public void onAuthFail(String message) {
                LaoSchoolShared.goBackToLoginPage(context);
            }
        });
    }

    private void _resetForm() {
        txtConversionMessage.getText().clear();
        txtConversionMessage.clearFocus();
        //Hide key board
        LaoSchoolShared.hideSoftKeyboard(getActivity());
    }

}
