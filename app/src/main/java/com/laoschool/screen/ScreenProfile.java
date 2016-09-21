package com.laoschool.screen;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.User;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.tools.CircularNetworkImageView;
import com.laoschool.view.FragmentLifecycle;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenProfile extends Fragment implements FragmentLifecycle {
    String TAG = ScreenProfile.class.getSimpleName();
    HomeActivity activity;
    CircularNetworkImageView imgAvata;
    TextView lbUserName;
    TextView lbPhoneNumber;
    TextView lbAddress1;
    TextView lbAddress2;
    TextView lbParentName;
    TextView lbContactPhone;
    TextView lbContactEmail;
    TextView lbNickName;
    int fromPosition = -1;
    User user;
    MenuItem itemSendMessage;
    private List<User> userList;

    interface IProfile {
        void sendMessage(List<User> userList, User user);
    }

    private IProfile iProfile;

    public ScreenProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.screen_profile, container, false);
        imgAvata = (CircularNetworkImageView) view.findViewById(R.id.imgAvata);
        lbUserName = (TextView) view.findViewById(R.id.lbUserName);

        lbNickName = (TextView) view.findViewById(R.id.lbNickName);
        lbPhoneNumber = (TextView) view.findViewById(R.id.lbPhoneNumber);
        lbAddress1 = (TextView) view.findViewById(R.id.lbAdress1);
        lbAddress2 = (TextView) view.findViewById(R.id.lbAdress2);
        lbParentName = (TextView) view.findViewById(R.id.lbParentName);
        lbContactPhone = (TextView) view.findViewById(R.id.lbContactPhone);
        lbContactEmail = (TextView) view.findViewById(R.id.lbContactEmail);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (fromPosition > -1) {
            inflater.inflate(R.menu.menu_screen_profile, menu);
            itemSendMessage = menu.findItem(R.id.action_send_message);
            if (fromPosition == LaoSchoolShared.POSITION_SCREEN_MORE_4) {
                itemSendMessage.setVisible(false);
            } else if (fromPosition == LaoSchoolShared.POSITION_SCREEN_LIST_TEACHER_9) {
                Toast.makeText(activity, activity.selectedTeacher, Toast.LENGTH_SHORT).show();
                itemSendMessage.setVisible(false);
            } else if (fromPosition == LaoSchoolShared.POSITION_SCREEN_LIST_STUDENT_OF_CLASS_18) {
                itemSendMessage.setVisible(true);
            } else if (fromPosition == LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5) {
                itemSendMessage.setVisible(true);
            } else {
                itemSendMessage.setVisible(false);
                //fillProfile();
            }
        } else {

        }
    }

    @Override
    public void onPauseFragment() {


    }

    @Override
    public void onResumeFragment() {
        try {
            activity = (HomeActivity) getActivity();
            fromPosition = activity.beforePosition;
            Log.d(TAG, "-fromPosition:" + fromPosition);
            if (fromPosition == LaoSchoolShared.POSITION_SCREEN_MORE_4) {
            } else if (fromPosition == LaoSchoolShared.POSITION_SCREEN_LIST_TEACHER_9) {
                Toast.makeText(activity, activity.selectedTeacher, Toast.LENGTH_SHORT).show();
            } else if (fromPosition == LaoSchoolShared.POSITION_SCREEN_LIST_STUDENT_OF_CLASS_18) {
                fillProfile();
            } else if (fromPosition == LaoSchoolShared.POSITION_SCREEN_CREATE_MESSAGE_5) {
                fillProfile();
            } else {
                //fillProfile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fillProfile() {
        user = activity.selectedStudent;
        userList=activity.selectedUserList;
        if (user != null) {
            Toast.makeText(activity, user.getFullname(), Toast.LENGTH_SHORT).show();
            lbUserName.setText(user.getFullname());
            lbPhoneNumber.setText(user.getPhone());
            lbNickName.setText(user.getNickname());
            String photo = user.getPhoto();
            if (photo != null) {
                LaoSchoolSingleton.getInstance().getImageLoader().get(photo, ImageLoader.getImageListener(imgAvata,
                        R.drawable.ic_account_circle_black_36dp, R.drawable.ic_account_circle_black_36dp),70,70, ImageView.ScaleType.FIT_XY);
                imgAvata.setImageUrl(photo, LaoSchoolSingleton.getInstance().getImageLoader());
            } else {
                imgAvata.setDefaultImageResId(R.drawable.ic_account_circle_black_36dp);
            }

            if (user.getUserDetail() != null) {
                lbAddress1.setText(user.getUserDetail().getAddr1());
                lbAddress2.setText(user.getUserDetail().getAddr2());
                lbParentName.setText(user.getUserDetail().getStd_contact_name());
                lbContactPhone.setText(user.getUserDetail().getStd_contact_phone());
                lbContactEmail.setText(user.getUserDetail().getStd_contact_email());
            }
        }
    }

    public static Fragment instantiate(int containerId, String currentRole) {
        return new ScreenProfile();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_send_message:
                iProfile.sendMessage(userList,user);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iProfile = (IProfile) context;
    }
}
