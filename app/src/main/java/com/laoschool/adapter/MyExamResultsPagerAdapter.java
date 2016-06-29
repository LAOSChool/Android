package com.laoschool.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.laoschool.R;
import com.laoschool.entities.ExamResult;
import com.laoschool.screen.ScreenExamResults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Hue on 6/29/2016.
 */
public class MyExamResultsPagerAdapter extends FragmentStatePagerAdapter {

    List<ExamResult> examResults = new ArrayList<>();
    private List<String> termsName = new ArrayList<>();

    public MyExamResultsPagerAdapter(FragmentManager fm, Context context, List<ExamResult> examResults) {
        super(fm);
        termsName.addAll(Arrays.asList(context.getResources().getStringArray(R.array.term_names)));
        this.examResults.addAll(examResults);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return ScreenExamResults.MyExamResultsPager.newInstance(0, termsName.get(0), (ArrayList<ExamResult>) examResults);
        else if (position == 1)
            return ScreenExamResults.MyExamResultsPager.newInstance(1, termsName.get(1), (ArrayList<ExamResult>) examResults);
        else
            return null;
    }

    @Override
    public int getCount() {
        return termsName.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return termsName.get(position);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
