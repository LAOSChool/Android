package com.laoschool.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.laoschool.entities.Master;

import java.util.List;

/**
 * Created by Hue on 5/30/2016.
 */
public class SubjectDropDownAdapter extends ArrayAdapter {


    private final List<Master> subjectList;

    public SubjectDropDownAdapter(Context context, int resource, List<Master> subjectList) {
        super(context, resource, subjectList);
        this.subjectList = subjectList;
    }

    public TextView getView(int position, View convertView, ViewGroup parent) {

        TextView v = (TextView) super
                .getView(position, convertView, parent);
        v.setText(subjectList.get(position).getSval());
        v.setId(subjectList.get(position).getId());
        return v;
    }

    public TextView getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
        TextView v = (TextView) super
                .getView(position, convertView, parent);
        v.setPadding(30, 30, 30, 30);
        v.setText(subjectList.get(position).getSval());
        v.setId(subjectList.get(position).getId());
        return v;
    }

}
