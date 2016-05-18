package com.laoschool.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laoschool.R;
import com.laoschool.entities.TimeTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hue on 5/18/2016.
 */
public class TimeTablePageAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = TimeTablePageAdapter.class.getSimpleName();
    private List<String> dayOfWeeks = new ArrayList<>(Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
    Map<Integer, ArrayList<TimeTable>> timeTablebyDayMap;
    private FragmentManager fragmentManager;
    private Map<Integer, String> mFragmentTags;

    public TimeTablePageAdapter(FragmentManager fragmentManager, Map<Integer, ArrayList<TimeTable>> timeTablebyDayMap) {
        super(fragmentManager);
        this.fragmentManager=fragmentManager;
        this.timeTablebyDayMap = timeTablebyDayMap;
        this.mFragmentTags = new HashMap<Integer, String>();
    }

    @Override
    public Fragment getItem(int position) {
        return DayOfWeekPage.newInstance(position, timeTablebyDayMap.get(position));
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = dayOfWeeks.get(position);
        return title;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object object = super.instantiateItem(container, position);
        if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            String tag = fragment.getTag();
            mFragmentTags.put(position, tag);
        }
        return object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    public static class DayOfWeekPage extends Fragment {

        private static final String ARG_POSITION = "pos";
        private static final String ARG_LIST = "list";
        private Context context;
        private int page;
        private ArrayList<TimeTable> timeTables;

        public DayOfWeekPage() {
        }

        public static DayOfWeekPage newInstance(int page, ArrayList<TimeTable> timeTables) {
            Bundle args = new Bundle();
            args.putInt(ARG_POSITION, page);
            args.putParcelableArrayList(ARG_LIST, timeTables);
            DayOfWeekPage fragment = new DayOfWeekPage();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                page = getArguments().getInt(ARG_POSITION);
                timeTables = getArguments().getParcelableArrayList(ARG_LIST);
            }
            this.context = getActivity();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.view_day_of_week_time_table, container, false);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.mTimeTableStudentbyDayofWeek);
            TextView lbNoSession = (TextView) view.findViewById(R.id.lbNoSession);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            if (timeTables.size() > 0) {
                lbNoSession.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(new SessionAdapter(context, 0, timeTables));
                //recyclerView.setNestedScrollingEnabled(false);
            } else {
                lbNoSession.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }


            return view;
        }
    }
}
