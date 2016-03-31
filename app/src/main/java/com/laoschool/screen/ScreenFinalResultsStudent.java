package com.laoschool.screen;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.adapter.RecylerViewScreenExamResultsStudentTabAdapter;
import com.laoschool.view.FragmentLifecycle;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenFinalResultsStudent extends Fragment implements FragmentLifecycle {


    private Context context;

    public ScreenFinalResultsStudent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.screen_final_results_student, container, false);
        final Spinner cbxTermScreenRecordStudent = (Spinner) view.findViewById(R.id.cbxTermScreenFinalResultsStudent);
        Button btnShowDetailsScreenRecordStudent = (Button) view.findViewById(R.id.btnShowDetailsScreenFinalResultsStudent);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerViewResultsDetailsScreenFinalResultsStudent);

        //set LayoutManager to recyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        //fill data for cbx
        _fillDataForSpinerFilter(cbxTermScreenRecordStudent, Arrays.asList(getResources().getStringArray(R.array.termTest)));

        //handler onclick show record
        btnShowDetailsScreenRecordStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _term = cbxTermScreenRecordStudent.getSelectedItem().toString();
                if (!_term.equals(getString(R.string.selected))) {
                    _fillDataForListResultFilter(recyclerView, Arrays.asList(getResources().getStringArray(R.array.subjects2)));
                    Toast.makeText(context, "Show final record", Toast.LENGTH_SHORT).show();
                } else {
                    //Show dropdown view
                    cbxTermScreenRecordStudent.performClick();
                }
            }
        });


        return view;
    }

    private void _fillDataForSpinerFilter(Spinner cbx, List<String> classTest) {
        ArrayAdapter<String> dataAdapterclassTest = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, classTest);
        // Drop down layout style - list view with radio button
        dataAdapterclassTest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        cbx.setAdapter(dataAdapterclassTest);
    }

    private void _fillDataForListResultFilter(RecyclerView recyclerView, List<String> datas) {
        //init adapter
        RecylerViewScreenExamResultsStudentTabAdapter adapter = new RecylerViewScreenExamResultsStudentTabAdapter(this, datas);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.context = getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //set display menu item
//        menu.findItem(R.id.action_create_message).setVisible(false);
//        menu.findItem(R.id.action_cancel).setVisible(false);
//        menu.findItem(R.id.action_send_message).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }

    public static Fragment instantiate(int containerId, String currentRole) {
        return new ScreenFinalResultsStudent();
    }
}
