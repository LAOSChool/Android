package com.laoschool.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.laoschool.entities.SchoolYears;

import java.util.List;

/**
 * Created by Hue on 6/15/2016.
 */
public class SelectedSchoolYearsAdapter extends ArrayAdapter {


    private final List<SchoolYears> schoolYearsList;

    public SelectedSchoolYearsAdapter(Context context, int resource, List<SchoolYears> schoolYearsList) {
        super(context, resource, schoolYearsList);
        this.schoolYearsList = schoolYearsList;
    }

    public TextView getView(int position, View convertView, ViewGroup parent) {

        TextView v = (TextView) super
                .getView(position, convertView, parent);
        v.setText(schoolYearsList.get(position).getYears());
        v.setId(schoolYearsList.get(position).getId());
        return v;
    }

    public TextView getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
        TextView v = (TextView) super
                .getView(position, convertView, parent);
        v.setPadding(30, 30, 30, 30);
        v.setText(schoolYearsList.get(position).getYears());
        v.setId(schoolYearsList.get(position).getId());
        return v;
    }

}

