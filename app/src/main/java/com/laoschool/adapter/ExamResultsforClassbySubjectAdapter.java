package com.laoschool.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.ExamResult;
import com.laoschool.entities.User;
import com.laoschool.model.AsyncCallback;
import com.laoschool.screen.ScreenExamResults;
import com.laoschool.shared.LaoSchoolShared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Hue on 3/7/2016.
 */
public class ExamResultsforClassbySubjectAdapter extends RecyclerView.Adapter<ExamResultsforClassbySubjectAdapter.RecylerViewScreenListTeacherAdapterViewHolder> {
    private static final String TAG = ExamResultsforClassbySubjectAdapter.class.getSimpleName();
    private int TYPE_DIPSLAY = 0;
    public static final int DISPLAY_LIST_0 = 0;
    public static final int DISPLAY_EIDIT_1 = 1;
    private ScreenExamResults screenExamResults;
    private Context context;
    private ScreenExamResults.IScreenExamResults iScreenExamResults;
    private List<Integer> studentIds = new ArrayList<>();
    private Map<Integer, List<ExamResult>> groupExamByStudent;
    private int TYPE_SUB_HEADER = 0;
    private int TYPE_EXAM = 1;
    private int TYPE_LINE = 2;
    private List<ExamResult> inputExamResults = new ArrayList<>(studentIds.size());
    private Map<Integer, Float> mapInputExam = new HashMap<>();
    private Map<Integer, ExamResult> mapExam = new HashMap<>();
    private Long exam_date_input;

    public ExamResultsforClassbySubjectAdapter(ScreenExamResults screenExamResults, Map<Integer, List<ExamResult>> groupExamByStudent) {
        this.screenExamResults = screenExamResults;
        this.context = screenExamResults.getActivity();
        this.iScreenExamResults = screenExamResults.iScreenExamResults;
        Map<Integer, List<ExamResult>> sortgroupExamByStudent = new TreeMap<>(groupExamByStudent);
        this.groupExamByStudent = sortgroupExamByStudent;
        for (Integer studentId : sortgroupExamByStudent.keySet()) {
            studentIds.add(studentId);
            mapInputExam.put(studentId, 0f);
            //mapExam.put(studentId, null);
        }


    }

    @Override
    public RecylerViewScreenListTeacherAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_LINE)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_line, parent, false); //Inflating the layout
        else if (viewType == TYPE_EXAM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exam_results, parent, false); //Inflating the layout
        } else if (viewType == TYPE_SUB_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_only_title, parent, false); //Inflating the layout
        }
        RecylerViewScreenListTeacherAdapterViewHolder viewHolder = new RecylerViewScreenListTeacherAdapterViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecylerViewScreenListTeacherAdapterViewHolder holder, int position) {
        View view = holder.view;
        try {
            if (holder.viewType == TYPE_EXAM) {
                int studentId = studentIds.get(position);
                defineRowbyStudentProfile(view, studentId);
            } else if (holder.viewType == TYPE_SUB_HEADER) {
                TextView row_title = (TextView) view.findViewById(R.id.txtTitle);
                row_title.setText("Results:" + (studentIds.size()));
                row_title.setTextColor(context.getResources().getColor(R.color.textColorSubHeader));
            }
        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder() -exception:" + e.getMessage());
        }
    }

    private void defineRowbyStudentProfile(final View view, int studentId) {
        List<ExamResult> examResultByStudentId = groupExamByStudent.get(studentId);
        Map<Integer, ArrayList<ExamResult>> groupExamByTerm = groupDataByTerm(examResultByStudentId);
        String userName = examResultByStudentId.get(0).getStudent_name();

        //Define view
        TextView row_title = (TextView) view.findViewById(R.id.row_title);
        RelativeLayout mInputExamResults = (RelativeLayout) view.findViewById(R.id.mInoutExamResults);
        defineInputExamResults(mInputExamResults, studentId, groupExamByDate(examResultByStudentId));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.mListScoreBySemester);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);


        //fill data exam results
        row_title.setText(userName);
        ScoreCurrentSemesterAdapter scoreStudentSemesterAdapter = new ScoreCurrentSemesterAdapter(context, groupExamByTerm);
        recyclerView.setAdapter(scoreStudentSemesterAdapter);
        View row_action = view.findViewById(R.id.row_action);
        //handler display
        if (TYPE_DIPSLAY == DISPLAY_LIST_0) {
            recyclerView.setVisibility(View.VISIBLE);
            row_action.setVisibility(View.VISIBLE);
            mInputExamResults.setVisibility(View.GONE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "All", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (TYPE_DIPSLAY == DISPLAY_EIDIT_1) {
            recyclerView.setVisibility(View.GONE);
            row_action.setVisibility(View.GONE);
            mInputExamResults.setVisibility(View.VISIBLE);
            view.setOnClickListener(null);
        }


        LaoSchoolSingleton.getInstance().getDataAccessService().getUserById(studentId, new AsyncCallback<User>() {
            @Override
            public void onSuccess(User result) {
                getStudentProfileById(result, view);

            }

            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onAuthFail(String message) {

            }
        });
    }

    private void defineInputExamResults(RelativeLayout mInputExamResults, final int studentId, Map<Long, ArrayList<ExamResult>> groupExamByTerm) {
        final TextView txtInputExamResults = (TextView) mInputExamResults.findViewById(R.id.txtInputExamResults);
        ExamResult examResult = null;
        if (TYPE_DIPSLAY == DISPLAY_EIDIT_1) {
            if (exam_date_input != null) {
                List<ExamResult> examResults = groupExamByTerm.get(exam_date_input);
                examResult = examResults.get(examResults.size() - 1);
                txtInputExamResults.setText(examResult.getSresult());
            } else {
                txtInputExamResults.setText("");
            }

        }
        final ExamResult finalExamResult = examResult;
        txtInputExamResults.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!txtInputExamResults.getText().toString().trim().isEmpty()) {
                    float exam = Float.parseFloat(txtInputExamResults.getText().toString());
                    if (finalExamResult != null) {
                        finalExamResult.setSresult(String.valueOf(exam));
                        mapInputExam.put(studentId, exam);
                        mapExam.put(studentId, finalExamResult);
                    }

                }
            }
        });
    }

    private void getStudentProfileById(final User result, final View view) {
        final NetworkImageView imgUserAvata = (NetworkImageView) view.findViewById(R.id.row_icon);
        //Load photo student
        if (result.getUserDetail().getPhoto() != null) {
            LaoSchoolSingleton.getInstance().getImageLoader().get(result.getUserDetail().getPhoto(), ImageLoader.getImageListener(imgUserAvata,
                    R.drawable.ic_account_circle_black_36dp, android.R.drawable
                            .ic_dialog_alert));
            imgUserAvata.setImageUrl(result.getUserDetail().getPhoto(), LaoSchoolSingleton.getInstance().getImageLoader());
        } else {
            imgUserAvata.setDefaultImageResId(R.drawable.ic_account_circle_black_36dp);
        }
    }

    private Map<Integer, ArrayList<ExamResult>> groupDataByTerm(List<ExamResult> examResultByStudentId) {
        Map<Integer, ArrayList<ExamResult>> groupExamByTerm = new HashMap<>();
        for (ExamResult examResult : examResultByStudentId) {
            int examTermId = examResult.getTerm_id();
            ArrayList<ExamResult> temp;
            if (groupExamByTerm.containsKey(examTermId)) {
                temp = groupExamByTerm.get(examTermId);
                temp.add(examResult);
            } else {
                temp = new ArrayList<>();
                temp.add(examResult);
            }
            groupExamByTerm.put(examTermId, temp);
        }
        return groupExamByTerm;
    }

    @Override
    public int getItemCount() {
        if (studentIds.size() > 0)
            return studentIds.size();
        else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_EXAM;
    }

    public class RecylerViewScreenListTeacherAdapterViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public RecylerViewScreenListTeacherAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }

    public void setTYPE_DIPSLAY(int TYPE_DIPSLAY) {
        this.TYPE_DIPSLAY = TYPE_DIPSLAY;
        inputExamResults.clear();
        notifyDataSetChanged();
    }

    public void setEditDisplay(long date_exam) {
        this.TYPE_DIPSLAY = TYPE_EXAM;
        inputExamResults.clear();
        mapExam.clear();
        exam_date_input = date_exam;
        notifyDataSetChanged();
    }


    public List<ExamResult> getInputExamResults() {
        List<ExamResult> examResults = new ArrayList<>();
        for (Integer studentId : mapExam.keySet()) {
            ExamResult examResult = mapExam.get(studentId);
//            examResult.setStudent_id(studentId);
//            examResult.setSresult(String.valueOf(mapInputExam.get(studentId)));
            if (examResult != null) {
                examResults.add(examResult);
            }
        }
        return examResults;
    }

    private Map<Long, ArrayList<ExamResult>> groupExamByDate(List<ExamResult> examResults) {
        Map<Long, ArrayList<ExamResult>> examByMonthList = new HashMap<>();
        for (ExamResult examResult : examResults) {
            int exam_month = examResult.getExam_month();
            int exam_year = examResult.getExam_year();
            long exam_date = LaoSchoolShared.getLongDate(exam_month, exam_year);
            Log.d(TAG, exam_month + "/" + exam_year + "-exam_date:" + exam_date);
            ArrayList<ExamResult> examTermList = null;
            if (examByMonthList.containsKey(exam_date)) {
                examTermList = examByMonthList.get(exam_date);
                if (examTermList == null)
                    examTermList = new ArrayList();
                examTermList.add(examResult);
            } else {
                examTermList = new ArrayList();
                examTermList.add(examResult);
            }
            if (examTermList.size() > 0)
                examByMonthList.put(exam_date, examTermList);

        }

        Map<Long, ArrayList<ExamResult>> examsTree = new TreeMap<>(examByMonthList);
        return examsTree;
    }
}
