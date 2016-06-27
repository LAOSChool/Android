package com.laoschool.screen.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laoschool.R;
import com.laoschool.shared.LaoSchoolShared;

import java.util.Locale;

/**
 * Created by Tran An on 6/19/2016.
 */
public class Languages extends View {

    public static final String PREFERENCES_NAME = "language";
    public static final String LANGUAGE_ENGLISH = "English";
    public static final String LANGUAGE_LAOS = "Laos";

    Context context;

    public interface LanguagesListener {
        void onChangeLanguage();
    }

    private LanguagesListener listener;

    public Languages(Context context, LanguagesListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    public View getView() {
        View view = View.inflate(context, R.layout.view_language, null);

        TextView txvHeader = (TextView) view.findViewById(R.id.txvHeader);
        RelativeLayout btnEnglish = (RelativeLayout) view.findViewById(R.id.btnEnglish);
        RelativeLayout btnLaos = (RelativeLayout) view.findViewById(R.id.btnLaos);
        TextView txvEnglish = (TextView) view.findViewById(R.id.txvEnglish);
        ImageView imgEnglish = (ImageView) view.findViewById(R.id.imgEnglish);
        TextView txvLaos = (TextView) view.findViewById(R.id.txvLaos);
        ImageView imgLaos = (ImageView) view.findViewById(R.id.imgLaos);

        txvHeader.setText(R.string.SCCommon_Language);

        SharedPreferences prefs = context.getSharedPreferences(
                LaoSchoolShared.SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
        String language = prefs.getString(Languages.PREFERENCES_NAME, null);
        if(language != null && language.equals(Languages.LANGUAGE_LAOS)) {
            imgLaos.setVisibility(VISIBLE);
            txvLaos.setTextColor(Color.parseColor("#2962FF"));
            imgLaos.setColorFilter(Color.parseColor("#2962FF"));
        } else {
            imgEnglish.setVisibility(VISIBLE);
            txvEnglish.setTextColor(Color.parseColor("#2962FF"));
            imgEnglish.setColorFilter(Color.parseColor("#2962FF"));
        }

        btnEnglish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale locale = new Locale("");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                context.getApplicationContext().getResources().updateConfiguration(config, null);

                SharedPreferences prefs = context.getSharedPreferences(
                        LaoSchoolShared.SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
                prefs.edit().putString(PREFERENCES_NAME, LANGUAGE_ENGLISH).apply();

                listener.onChangeLanguage();
            }
        });

        btnLaos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale locale = new Locale("lo");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                context.getApplicationContext().getResources().updateConfiguration(config, null);

                SharedPreferences prefs = context.getSharedPreferences(
                        LaoSchoolShared.SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
                prefs.edit().putString(PREFERENCES_NAME, LANGUAGE_LAOS).apply();

                listener.onChangeLanguage();
            }
        });

        return view;
    }
}
