package com.laoschool.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.ExamResult;
import com.laoschool.model.AsyncCallback;
import com.laoschool.screen.ScreenExamResults;
import com.laoschool.shared.LaoSchoolShared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Hue on 5/20/2016.
 */
public class ExamResultsByTermPagerAdapter extends FragmentStatePagerAdapter {
    public static final String TAG = ExamResultsByTermPagerAdapter.class.getSimpleName();
    private List<Integer> termIds = new ArrayList<>();
    private List<String> terms = new ArrayList<>();
    private List<ArrayList<ExamResult>> lists = new ArrayList<>();

    public ExamResultsByTermPagerAdapter(FragmentManager fm, HashMap<Integer, String> hashterms, Map<Integer, ArrayList<ExamResult>> examResultList) {
        super(fm);
        Map<Integer, String> treeterms = new TreeMap<>(hashterms);
        for (Integer key : treeterms.keySet()) {
            ArrayList<ExamResult> exams = examResultList.get(key);
            termIds.add(key);
            terms.add(hashterms.get(key));
            lists.add(exams);
        }

    }

    @Override
    public int getCount() {
        return terms.size();
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return ScreenExamResults.ExamResultsByTermPager.newInstance(0, termIds.get(0), lists.get(0));
        else if (position == 1)
            return ScreenExamResults.ExamResultsByTermPager.newInstance(1, termIds.get(1), lists.get(1));
        else
            return null;


    }

    @Override
    public CharSequence getPageTitle(int position) {
        return terms.get(position);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}

