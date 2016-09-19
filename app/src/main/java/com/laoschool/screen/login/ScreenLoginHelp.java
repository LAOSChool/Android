package com.laoschool.screen.login;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.laoschool.R;
import com.laoschool.screen.view.Languages;
import com.laoschool.shared.LaoSchoolShared;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenLoginHelp extends Fragment {

    private static final String TAG = ScreenLoginHelp.class.getSimpleName();
    ScreenLogin containerz;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    public void setContainer(ScreenLogin container) {
        this.containerz = container;
    }

    public ScreenLoginHelp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_login_help, container, false);

        RelativeLayout header = (RelativeLayout) view.findViewById(R.id.header);
        ImageButton btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        TextView txvTitle = (TextView) view.findViewById(R.id.txvTitle);

        txvTitle.setText(R.string.SCCommon_Help);

        int color = Color.parseColor("#ffffff");
        btnBack.setColorFilter(color);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerz.switchToScreenLoginMain();
            }
        });

        final WebView myWebView = (WebView) view.findViewById(R.id.webview);
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
                        myWebView.loadUrl("http://" + domain_name + "/ls2/help/?lang=" + lang);

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

}
