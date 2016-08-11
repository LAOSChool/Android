package com.laoschool.screen;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.adapter.RecyclerViewScreenMoreAdapter;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.tools.CircularNetworkImageView;
import com.laoschool.view.FragmentLifecycle;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenMore extends Fragment implements FragmentLifecycle {


    private static final String CURRENT_ROLE = "current_role";
    private static final String TAG = "ScreenMore";
    private Context context;
    private int containerId;
    private String currentRole;
    private boolean checkConn;
    private TextView txtUserName;
    private TextView txtSchoolName;
    private TextView txtClassName;
    private TextView txvProfile;
    private TextView txtStudentName;
    private TextView txtTerm;
    private CircularNetworkImageView userImage;
    private CircularNetworkImageView userImageTeacher;


    public interface IScreenMore {
        void gotoSchoolInformationformMore();

        void gotoListTearcherformMore();

        void gotoSettingformMore();

        void logoutApplication();

        void gotoDetailsUser();

        void gotoExamResultsformMore();

        void gotoSchoolRecordbyYearformMore();

        void gotoScheduleformMore();

        void gotoChangeLanguage();

        void gotoListStudentformMore();
    }

    public IScreenMore iScreenMore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return _defineScreenMorebyRole(inflater, container);
    }

    private View _defineScreenMorebyRole(LayoutInflater inflater, ViewGroup container) {
//        boolean checkConn = LaoSchoolShared.checkConn(context);
//        Log.d(TAG, "-checkConn:" + checkConn);
        if (currentRole != null) {
            if (currentRole.equals(LaoSchoolShared.ROLE_TEARCHER)) {
                return _defineSrceenMoreTeacher(inflater, container);
            } else {
                return _defineSrceenMoreStudent(inflater, container);
            }
        } else {
            return inflater.inflate(R.layout.screen_error_application, container, false);
        }


//        if (checkConn) {
//        } else {
//            return inflater.inflate(R.layout.view_connection_error, container, false);
//        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... args) {
            try {
                Bitmap bitmapp = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
                return bitmapp;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null)
                bmImage.setImageBitmap(result);
            else
                Log.i("ScreenMore", "Can't not load user image");
        }
    }

    private View _defineSrceenMoreStudent(LayoutInflater inflater, ViewGroup container) {
        try {

            View view = inflater.inflate(R.layout.screen_more_student, container, false);
            //
            LinearLayout mDetaislUser = (LinearLayout) view.findViewById(R.id.mDetaislUser);
            txtStudentName = (TextView) view.findViewById(R.id.txtUserNameScreenMoreStudent);
            txtSchoolName = (TextView) view.findViewById(R.id.txtSchoolNameScreenMoreStudent);
            txtTerm = (TextView) view.findViewById(R.id.txtTermScreenMoreStudent);
            RecyclerView mRecylerViewFunctionMore = (RecyclerView) view.findViewById(R.id.mRecylerViewFunctionMore);
            userImage = (CircularNetworkImageView) view.findViewById(R.id.userImage);
            ImageView schoolImage = (ImageView) view.findViewById(R.id.schoolImage);
            ImageView termImage = (ImageView) view.findViewById(R.id.termImage);
            ImageView imgEditProfile = (ImageView) view.findViewById(R.id.imgEditProfile);
            txvProfile = (TextView) view.findViewById(R.id.txvProfile);

//            int color = Color.parseColor("#808080");
//            int color2 = Color.parseColor("#424242");
          //  userImage.setColorFilter(getResources().getColor(R.color.colorIconOnFragment));
            schoolImage.setColorFilter(getResources().getColor(R.color.colorIconOnFragment));
            termImage.setColorFilter(getResources().getColor(R.color.colorIconOnFragment));
            imgEditProfile.setColorFilter(getResources().getColor(R.color.colorIconOnFragment));


            //Handler goto detaisl
//            mDetaislUser.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    iScreenMore.gotoDetailsUser();
//                }
//            });


            fillUserDetailsStudent();

            //init adapte
            List<String> more_student = Arrays.asList(getResources().getStringArray(R.array.more_student));
            RecyclerViewScreenMoreAdapter adapter = new RecyclerViewScreenMoreAdapter(this, more_student);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);

            //set adapter
            mRecylerViewFunctionMore.setLayoutManager(gridLayoutManager);
            mRecylerViewFunctionMore.setAdapter(adapter);
            return view;
        } catch (Exception e) {
            return null;
        }
    }

    private void fillUserDetailsStudent() {
        try {
            String photo = LaoSchoolShared.myProfile.getPhoto();
            if (photo != null) {
                LaoSchoolSingleton.getInstance().getImageLoader().get(photo, ImageLoader.getImageListener(userImage,
                        R.drawable.ic_account_circle_black_36dp, R.drawable.ic_account_circle_black_36dp));
                userImage.setImageUrl(photo, LaoSchoolSingleton.getInstance().getImageLoader());
            } else {
                userImage.setDefaultImageResId(R.drawable.ic_account_circle_black_36dp);
            }
            txvProfile.setText(R.string.SCCommon_Profile);
            txtStudentName.setText(LaoSchoolShared.myProfile.getFullname() + " - " + context.getString(R.string.SCCommon_Class) + " " + LaoSchoolShared.myProfile.getEclass().getTitle());
            txtSchoolName.setText(LaoSchoolShared.myProfile.getSchoolName());
            String split[] = LaoSchoolShared.myProfile.getEclass().getYears().split("-");
            String years = context.getString(R.string.SCCommon_Years) + ": " + split[0] + " - " + split[1];
            txtTerm.setText(years);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "fillUserDetailsStudent() -message:" + e.getMessage());
        }

    }

    private View _defineSrceenMoreTeacher(LayoutInflater inflater, ViewGroup container) {
        try {
            View view = inflater.inflate(R.layout.screen_more_teacher, container, false);
            //
            LinearLayout mDetaislUser = (LinearLayout) view.findViewById(R.id.mDetaislUser);
            userImageTeacher = (CircularNetworkImageView) view.findViewById(R.id.userImage);
            txtUserName = (TextView) view.findViewById(R.id.txtUserNameScreenMoreTeacher);
            txtSchoolName = (TextView) view.findViewById(R.id.txtSchoolNameScreenMoreTeacher);
            txtClassName = (TextView) view.findViewById(R.id.txtContactPhoneScreenMoreTeacher);
            RecyclerView mRecylerViewFunctionMore = (RecyclerView) view.findViewById(R.id.mRecylerViewFunctionMore);
            txvProfile = (TextView) view.findViewById(R.id.txvProfile);

            Typeface roboto = Typeface.createFromAsset(context.getAssets(),
                    "font/Roboto-Regular.ttf"); //use this.getAssets if you are calling from an Activity
            txtUserName.setTypeface(roboto);


            //Handler goto detaisl
            mDetaislUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iScreenMore.gotoDetailsUser();
                }
            });

            fillUserDetailsTeacher();

            //init adapte
            final List<String> more_teacher = Arrays.asList(getResources().getStringArray(R.array.more_teacher));
            RecyclerViewScreenMoreAdapter adapter = new RecyclerViewScreenMoreAdapter(this, more_teacher);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);

            //set adapter
            mRecylerViewFunctionMore.setLayoutManager(gridLayoutManager);
            mRecylerViewFunctionMore.setAdapter(adapter);
            return view;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void fillUserDetailsTeacher() {
        try {
            String photo = LaoSchoolShared.myProfile.getPhoto();
            if (photo != null) {
                LaoSchoolSingleton.getInstance().getImageLoader().get(photo, ImageLoader.getImageListener(userImageTeacher,
                        R.drawable.ic_account_circle_black_36dp, R.drawable.ic_account_circle_black_36dp));
                userImageTeacher.setImageUrl(photo, LaoSchoolSingleton.getInstance().getImageLoader());
            } else {
                userImageTeacher.setDefaultImageResId(R.drawable.ic_account_circle_black_36dp);
            }
            txtUserName.setText(LaoSchoolShared.myProfile.getFullname());
            txtSchoolName.setText(LaoSchoolShared.myProfile.getSchoolName());
            txvProfile.setText(R.string.SCCommon_Profile);
            txtClassName.setText(context.getString(R.string.SCCommon_Class) + " " + LaoSchoolShared.myProfile.getEclass().getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "fillUserDetailsTeacher() -message:" + e.getMessage());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.context = getActivity();
        if (getArguments() != null) {
            checkConn = LaoSchoolShared.checkConn(context);
            Log.d(TAG, "-checkConn:" + checkConn);
//            if (checkConn) {
//
//            }
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            currentRole = getArguments().getString(CURRENT_ROLE);
            Log.d(TAG, "-Container Id:" + containerId);
            Log.d(TAG, "-Role:" + currentRole);
        }

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        Bundle bundle = new Bundle();
        bundle.putString("ScreenMore", TAG);
        mFirebaseAnalytics.logEvent("ScreenMore", bundle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //set display menu item
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPauseFragment() {


    }

    @Override
    public void onResumeFragment() {

    }

    public static Fragment instantiate(int containerId, String currentRole) {
        ScreenMore fragment = new ScreenMore();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        args.putString(CURRENT_ROLE, currentRole);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LaoSchoolShared.myProfile == null) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.logoutApplication();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iScreenMore = (IScreenMore) activity;
    }
}
