package com.laoschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.laoschool.R;
import com.laoschool.entities.SchoolYears;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hue on 6/15/2016.
 */
public class SelectedSchoolYearsAdapter extends ArrayAdapter {
    private final List<SchoolYears> schoolYearsList;

    public SelectedSchoolYearsAdapter(Context context, List<SchoolYears> objects) {
        super(context, R.layout.row_selected_school_year, objects);
        this.schoolYearsList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SchoolYears schoolYears = schoolYearsList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_selected_school_year, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.lbSchoolYears);

        // Populate the data into the template view using the data object
        tvName.setText(schoolYears.getYears());
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        SchoolYears schoolYears = schoolYearsList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_selected_school_year_list, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.lbSchoolYears);

        // Populate the data into the template view using the data object
        tvName.setText(schoolYears.getYears());
        // Return the completed view to render on screen
        return convertView;
    }
}

