package com.laoschool.screen;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.adapter.ListImageSelectedAdapter;
import com.laoschool.entities.Image;
import com.laoschool.entities.Message;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ScreenCreateAnnouncement extends Fragment implements FragmentLifecycle {
    private int containerId;

    private IScreenCreateAnnouncement mListener;
    private Context context;
    private static DataAccessInterface service;
    private String TAG = "SCreateAnnouncement";

    private RecyclerView mReclerViewImageSeleted;

    private List<Image> files = new ArrayList<>();

    private TextView lbClass;
    private EditText txtTitle;
    private EditText txtContent;
    private String currentRole;
    private ImageView imgPrioty;

    private ListImageSelectedAdapter listImageSelectedAdapter;

    public interface IScreenCreateAnnouncement {

        void _goBackAnnocements();
    }

    public ScreenCreateAnnouncement() {
        // Required empty public constructor
    }

    public static Fragment instantiate(int containerId, String currentRole) {
        ScreenCreateAnnouncement fragment = new ScreenCreateAnnouncement();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        args.putString(LaoSchoolShared.CURRENT_ROLE, currentRole);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            currentRole = getArguments().getString(LaoSchoolShared.CURRENT_ROLE);
            Log.d(getString(R.string.SCCommon_Attendance), "-Container Id:" + containerId);
        }
        this.context = getActivity();
        this.service = DataAccessImpl.getInstance(context);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (currentRole == null)
            return inflater.inflate(R.layout.screen_error_application, container, false);
        else {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.screen_create_announcement, container, false);
            lbClass = (TextView) view.findViewById(R.id.lbClassResNotification);
            txtTitle = (EditText) view.findViewById(R.id.txtNotificationTitleTeacher);
            txtContent = (EditText) view.findViewById(R.id.txtNotificationContentTeacher);
            imgPrioty = (ImageView) view.findViewById(R.id.imgPrioty);
            TextView txvTo = (TextView) view.findViewById(R.id.txvTo);

            txvTo.setText(R.string.SCCreateMessage_To);
            txtTitle.setHint(R.string.SCCreateMessage_Subject);
            txtContent.setHint(R.string.SCCreateMessage_Content);

            imgPrioty.setColorFilter(getResources().getColor(R.color.colorDefault));
            imgPrioty.setTag(R.color.colorDefault);

            imgPrioty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imgPrioty.getTag().equals(R.color.colorDefault)) {
                        imgPrioty.setColorFilter(getResources().getColor(R.color.colorStar));
                        imgPrioty.setTag(R.color.colorStar);
                    } else {
                        imgPrioty.setColorFilter(getResources().getColor(R.color.colorDefault));
                        imgPrioty.setTag(R.color.colorDefault);
                    }
                }
            });

            mReclerViewImageSeleted = (RecyclerView) view.findViewById(R.id.mReclerViewImageSeleted);

            //set layout manager
            CustomGridLayoutManager customGridLayoutManager = new CustomGridLayoutManager(context);
            mReclerViewImageSeleted.setLayoutManager(customGridLayoutManager);

            listImageSelectedAdapter = new ListImageSelectedAdapter(context, files);
            mReclerViewImageSeleted.setAdapter(listImageSelectedAdapter);
            return view;
        }
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof IScreenCreateAnnouncement) {
            mListener = (IScreenCreateAnnouncement) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IScreenCreateAnnouncement");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
        Log.d(TAG, "onDetach()");
    }

    @Override
    public void onPauseFragment() {
        Log.d(TAG, "onPauseFragment()");
    }

    @Override
    public void onResumeFragment() {
        Log.d(TAG, "onResumeFragment()");
        _setMyClass();
    }

    private void _setMyClass() {
        try {
            lbClass.setText(LaoSchoolShared.myProfile.getEclass().getTitle());
        } catch (Exception e) {
            Log.d(TAG, "_setMyClass() Error:" + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }

    private void _setListImageSelected(List<Image> files) {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_screen_create_announcement, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_send_announcement:

                _sentNotification();

                return true;
            case R.id.action_attach_file:

                _showDialogChooseFile();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void _showDialogChooseFile() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setMessage(items[0]);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (files.size() < 5) {
                    if (items[item].equals("Take Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, LaoSchoolShared.SELECT_CAMERA);
                    } else if (items[item].equals("Choose from Library")) {

                        _chosseImageFormSdCard();

                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(context, R.string.SCCreateAnnocement_err_msg_limit_img_seleted, Toast.LENGTH_SHORT).show();
                }
            }
        });

        Dialog dialog = builder.create();
        if (files.size() < 5)
            dialog.show();
    }

    private void _sentNotification() {
        if (!validateMessageTitle(txtTitle)) {
            return;
        }
        if (!validateMessageConten(txtContent)) {
            return;
        }
        //get list image
        Log.d(TAG, "File size:" + files.size());
        if (files.size() > 0) {
            List<Image> imageList = ((ListImageSelectedAdapter) (mReclerViewImageSeleted.getAdapter())).getListImage();
            for (Image image : imageList) {
                Log.d(TAG, image.toString());
            }
        }
        Message message = new Message();
        message.setSchool_id(LaoSchoolShared.myProfile.getSchool_id());
        message.setClass_id(LaoSchoolShared.myProfile.getEclass().getId());
        message.setTitle(txtTitle.getText().toString());
        message.setContent(txtContent.getText().toString());
        if (imgPrioty.getTag() != null) {
            if (imgPrioty.getTag().equals(R.color.colorDefault)) {
                message.setImp_flg(0);
            } else {
                message.setImp_flg(1);
            }
        }
        message.setNotifyImages(files);
//        Log.d(TAG, "Message create:" + message.toString());
        _confirmSentNotification(message);


    }

    private void _confirmSentNotification(final Message message) {
        LaoSchoolShared.hideSoftKeyboard(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getString(R.string.SCCreateAnnocement_msg_confirm_sent_notication) + " " + LaoSchoolShared.myProfile.getEclass().getTitle());

        builder.setPositiveButton(R.string.SCCommon_Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                _createNotification(message);
            }
        });
        builder.setNegativeButton(R.string.SCCommon_Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        Dialog dialog = builder.create();
        dialog.show();

    }

    private void _createNotification(Message message) {
        try {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(getString(R.string.SCCommon_Create));
            progressDialog.show();
            LaoSchoolSingleton.getInstance().getDataAccessService().createNotification(message, new AsyncCallback<Message>() {
                @Override
                public void onSuccess(Message result) {
                    progressDialog.dismiss();
                    Log.d(TAG, result.toString());
                    String alert = getString(R.string.SCCommon_Successfully);
                    _showAlertMessage(alert);
                }

                @Override
                public void onFailure(String message) {
                    progressDialog.dismiss();
                    Log.e(TAG, message);
                    String alert = getString(R.string.SCCreateMessage_err_msg_create_message);
                    _showAlertMessage(alert);
                }

                @Override
                public void onAuthFail(String message) {
                    LaoSchoolShared.goBackToLoginPage(context);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "/_createNotification() Exception message:" + e.getMessage());
        }
    }

    private void _showAlertMessage(String alert) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(alert);
        builder.setNegativeButton(R.string.SCCommon_Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                _resetForm();
                dialogInterface.dismiss();
                if (mListener != null) {
                    mListener._goBackAnnocements();
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void _resetForm() {
        txtContent.getText().clear();
        txtTitle.getText().clear();
        txtTitle.clearFocus();
        txtContent.clearFocus();
        imgPrioty.setColorFilter(getResources().getColor(R.color.colorDefault));

        try {
            ListImageSelectedAdapter adapter = ((ListImageSelectedAdapter) mReclerViewImageSeleted.getAdapter());
            adapter.getListImage().clear();
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
        }

        files.clear();

        LaoSchoolShared.hideSoftKeyboard(getActivity());
    }

    private void _chosseImageFormSdCard() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, LaoSchoolShared.SELECT_PHOTO);
    }

    public class CustomGridLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomGridLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
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

    private boolean validateMessageConten(EditText edit) {
        if (edit.getText().toString().trim().isEmpty()) {
            requestFocus(edit);
            Toast.makeText(context, R.string.SCCreateMessage_err_msg_input_message_conten, Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "/onActivityResult():requestCode=" + requestCode + ",resultCode=" + resultCode);
        if (requestCode == LaoSchoolShared.SELECT_CAMERA) {
            if (resultCode == getActivity().RESULT_OK) {
                Log.d(TAG, "Take Photo");
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    if (destination.isFile())
                        _addImage(destination.getAbsolutePath());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == LaoSchoolShared.SELECT_PHOTO) {
            if (resultCode == getActivity().RESULT_OK) {
                Log.d(TAG, "Pick image url");
                if (data.getData() != null)
                    _addImage(LaoSchoolShared.getPath(context, data.getData()));
            }
        }
    }

    private void _addImage(String absolutePath) {
        if (files.size() < 5) {
            Log.d(TAG, "url:" + absolutePath);
            Image newImage = new Image();
            newImage.setLocal_file_url(absolutePath);
            files.add(newImage);
            listImageSelectedAdapter.notifyItemInserted(files.size());
        } else {
            Log.d(TAG, getString(R.string.SCCreateAnnocement_err_msg_limit_img_seleted));
        }
    }
}
