package com.laoschool.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.laoschool.R;
import com.laoschool.entities.ExamResult;
import com.laoschool.entities.FinalResult;
import com.laoschool.screen.pager.FinalExamPager;


import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;


/**
 * Created by Hue on 6/13/2016.
 */
public class MyFinalResultsPagerAdapter extends FragmentStatePagerAdapter {
    public static final String TAG = MyFinalResultsPagerAdapter.class.getSimpleName();
    private List<String> finaExamlTitle = new ArrayList<>();
    FinalResult finalResult;
    public MyFinalResultsPagerAdapter(FragmentManager fm, Context context, FinalResult result) {
        super(fm);
        this.finalResult = result;
        finaExamlTitle.addAll(Arrays.asList(context.getResources().getStringArray(R.array.final_titles)));

//        1  Normal
//        2  Thi Hoc Ky
//        3  Trung Binh 4 thang
//        4  Trung Binh Hoc ky
//        5  Trung Binh Ca Nam
//        6  Thi Lai Ca Nam
//        7  Thi Tot Nghiep Cap
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        List<ExamResult> examResults = new ArrayList<>();
        examResults.addAll(finalResult.getExam_results());
        FinalResult finalResult = new FinalResult();
        finalResult.setClass_name(this.finalResult.getClassName());
        finalResult.setClass_location(this.finalResult.getCls_location());
        finalResult.setClass_name(this.finalResult.getClassName());
        finalResult.setTeacher_name(this.finalResult.getTeacher_name());
        finalResult.setPassed(this.finalResult.getPassed());
        if (position == 0) {
            finalResult.setExam_results(this.finalResult.getExam_results());
            return FinalExamPager.create(0, finalResult);
        } else if (position == 1) {
            finalResult.setExam_results(this.finalResult.getExam_results());
            return FinalExamPager.create(1, finalResult);
        } else if (position == 2) {
            finalResult.setExam_results(this.finalResult.getExam_results());
            return FinalExamPager.create(2, finalResult);
        } else return null;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return finaExamlTitle.get(position);
    }

    private Fragment mCurrentFragment;

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }
}

