package com.laoschool;

import android.app.Application;

/**
 * Created by Hue on 4/20/2016.
 */
public class LaoSchoolApplication extends Application {
    public LaoSchoolApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLaoSchoolSingleton();

    }
    protected void initLaoSchoolSingleton() {
        // Initialize the instance of TextToSpeechSingleton
        LaoSchoolSingleton.initInstance(getApplicationContext());
    }
}
