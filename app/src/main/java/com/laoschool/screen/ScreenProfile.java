package com.laoschool.screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenProfile extends Fragment implements FragmentLifecycle {

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
            if (fromPosition == LaoSchoolShared.POSITION_SCREEN_MORE_4) {
                inflater.inflate(R.menu.menu_screen_profile, menu);
            } else if (fromPosition == LaoSchoolShared.POSITION_SCREEN_LIST_TEACHER_9) {

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
            if (fromPosition == LaoSchoolShared.POSITION_SCREEN_MORE_4) {

            } else if (fromPosition == LaoSchoolShared.POSITION_SCREEN_LIST_TEACHER_9) {
                Toast.makeText(activity, activity.selectedTeacher, Toast.LENGTH_SHORT).show();
            } else if (fromPosition == LaoSchoolShared.POSITION_SCREEN_LIST_STUDENT_OF_CLASS_18) {
                User user = activity.selectedStudent;
                if (user != null) {
                    Toast.makeText(activity, user.getFullname(), Toast.LENGTH_SHORT).show();
                    lbUserName.setText(user.getFullname());
                    lbPhoneNumber.setText(user.getPhone());
                    lbNickName.setText(user.getNickname());
                    String photo = user.getPhoto();
                    if (photo != null) {
                        LaoSchoolSingleton.getInstance().getImageLoader().get(photo, ImageLoader.getImageListener(imgAvata,
                                R.drawable.ic_account_circle_black_36dp, R.drawable.ic_account_circle_black_36dp));
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Fragment instantiate(int containerId, String currentRole) {
        return new ScreenProfile();
    }
}
