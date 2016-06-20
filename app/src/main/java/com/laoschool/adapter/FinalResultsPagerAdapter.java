package com.laoschool.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.laoschool.entities.ExamResult;
import com.laoschool.entities.FinalResult;
import com.laoschool.screen.pager.FinalExamPager;


import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;


/**
 * Created by Hue on 6/13/2016.
 */
public class FinalResultsPagerAdapter extends FragmentStatePagerAdapter {
    public static final String TAG = FinalResultsPagerAdapter.class.getSimpleName();
    private List<String> finaExamlTitle = Arrays.asList("Total", "Term 1", "Term 2", "Reviews");
    FinalResult finalResult;
    List<ExamResult> resultsforTerm1 = new ArrayList<>();
    List<ExamResult> resultsforTerm2 = new ArrayList<>();
    List<ExamResult> resultsforTotal = new ArrayList<>();

    public FinalResultsPagerAdapter(FragmentManager fm, FinalResult result) {
        super(fm);
        this.finalResult = result;

//        1  Normal
//        2  Thi Hoc Ky
//        3  Trung Binh 4 thang
//        4  Trung Binh Hoc ky
//        5  Trung Binh Ca Nam
//        6  Thi Lai Ca Nam
//        7  Thi Tot Nghiep Cap
        List<ExamResult> resultList = result.getExam_results();
        for (ExamResult examResult : resultList) {
            if (examResult.getExam_type() < 4) {
                if (examResult.getTerm_id() == 1) {
                    resultsforTerm1.add(examResult);
                } else if (examResult.getTerm_id() == 2) {
                    resultsforTerm2.add(examResult);
                }
            } else {
                resultsforTotal.add(examResult);
            }
        }
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
            finalResult.setExam_results(resultsforTotal);
            return FinalExamPager.create(0, finalResult);
        } else if (position == 1) {
            finalResult.setExam_results(resultsforTerm1);
            return FinalExamPager.create(1, finalResult);
        } else if (position == 2) {
            finalResult.setExam_results(resultsforTerm2);
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

