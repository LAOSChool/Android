package com.laoschool.screen;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.laoschool.R;
import com.laoschool.screen.view.Languages;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenSchoolInformation extends Fragment implements FragmentLifecycle {


    private static final String TAG = ScreenSchoolInformation.class.getSimpleName();
    private WebView myWebView;

    public ScreenSchoolInformation() {
        // Required empty public constructor
    }

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.screen_school_information, container, false);
        myWebView = (WebView) view.findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);

        // Initialize Firebase Remote Config.
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        //get language
        final String lang;
        SharedPreferences prefs = getActivity().getSharedPreferences(
                LaoSchoolShared.SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
        String language = prefs.getString(Languages.PREFERENCES_NAME, null);
        if (language != null && language.equals(Languages.LANGUAGE_LAOS)) {
            lang = "la";
        } else {
            lang = "en";
        }

        // Fetch remote config.
        mFirebaseRemoteConfig.fetch(getActivity().getResources().getInteger(R.integer.fetch_ex_time))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Make the fetched config available via
                        // FirebaseRemoteConfig get<type> calls.
                        mFirebaseRemoteConfig.activateFetched();
                        String domain_name = mFirebaseRemoteConfig.getString("domain_name");
                        Log.d(TAG, "-domain_name:" + domain_name);
                        myWebView.loadUrl("http://"+domain_name + "/ls2/school_info/?lang=" + lang + "&schoolid=" + LaoSchoolShared.myProfile.getSchool_id());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // There has been an error fetching the config
                        Log.w(TAG, "Error fetching config: " +
                                e.getMessage());

                    }
                });
        return view;
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

    }

    public static Fragment instantiate(int containerId, String currentRole) {
        return new ScreenSchoolInformation();
    }

}
