package com.laoschool.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.ExamResult;
import com.laoschool.shared.LaoSchoolShared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Hue on 6/6/2016.
 */
public class InputExamResultsAdapter extends RecyclerView.Adapter<InputExamResultsAdapter.ViewHolder> {
    private static final String TAG = InputExamResultsAdapter.class.getSimpleName();
    private int TYPE_DIPSLAY = 0;
    public static final int DISPLAY_LIST_0 = 0;
    public static final int DISPLAY_EIDIT_1 = 1;
    private Context context;
    private List<Integer> studentIds = new ArrayList<>();
    private Map<Integer, ExamResult> groupExamByStudent;
    private int TYPE_SUB_HEADER = 0;
    private int TYPE_EXAM = 1;
    private int TYPE_LINE = 2;
    private List<ExamResult> inputExamResults = new ArrayList<>(studentIds.size());
    private Map<Integer, Float> mapInputExam = new HashMap<>();
    private Map<Integer, ExamResult> mapExam = new HashMap<>();
    private int exam_date_input;
    List<Long> exam_dates;

    public InputExamResultsAdapter(Context context, Map<Integer, ExamResult> groupExamByStudent, int selectedDateInputExamResult) {
        this.context = context;
        Map<Integer, ExamResult> sortgroupExamByStudent = new TreeMap<>(groupExamByStudent);
        this.groupExamByStudent = sortgroupExamByStudent;
        this.exam_date_input = selectedDateInputExamResult;
        for (Integer studentId : sortgroupExamByStudent.keySet()) {
            studentIds.add(studentId);
            mapInputExam.put(studentId, 0f);
        }
        Log.d(TAG, "size:" + studentIds.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_input_exam_by_date, parent, false); //Inflating the layout
        ViewHolder viewHolder = new ViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            final int studentId = studentIds.get(position);
            final ExamResult examResults = groupExamByStudent.get(studentId);
            String userName = examResults.getStudent_name();
            //fill data exam results
            holder.row_title.setText(userName);
            holder.txtInputExamResults.setText(examResults.getSresult());
            holder.lbNickName.setText(examResults.getStd_nickname());
            //final ExamResult[] finalExamResult = {ExamResult.copyValue(examResults)};

            //handler text changer
            holder.txtInputExamResults.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().trim().isEmpty()) {
                        Float exam = Float.valueOf(editable.toString());
                        Log.d(TAG, "addTextChangedListener().afterTextChanged(" + position + ") -exam:" + exam);
                        if (examResults != null) {
                            if (examResults.getSresult() != null) {
                                if (!examResults.getSresult().equals(exam)) {
                                    examResults.setSresult(String.valueOf(exam));
                                    mapExam.put(studentId, examResults);
                                } else {
                                    Log.d(TAG, "-sResults:Duplicate");
                                }
                            } else {
                                Log.d(TAG, "-sResults:Blank");
                                examResults.setSresult(String.valueOf(exam));
                                mapExam.put(studentId, examResults);
                            }
                        } else {
                            Log.d(TAG, "-exam results null");
                        }
                    }
                }
            });


            //Load photo
            String photo = examResults.getStd_photo();
            if (photo != null) {
                LaoSchoolSingleton.getInstance().getImageLoader().get(photo, ImageLoader.getImageListener(holder.imgUserAvata,
                        R.drawable.ic_account_circle_black_36dp, R.drawable.ic_account_circle_black_36dp));
                holder.imgUserAvata.setImageUrl(photo, LaoSchoolSingleton.getInstance().getImageLoader());
            } else {
                holder.imgUserAvata.setDefaultImageResId(R.drawable.ic_account_circle_black_36dp);
            }

            holder.btnAddNoticeExamResutls.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = makeDialogInputNotice(examResults);
                    dialog.show();

                }
            });

            holder.setIsRecyclable(false);
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder() -exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private Dialog makeDialogInputNotice(final ExamResult examResults) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        
        View inputNotice = View.inflate(context, R.layout.view_input_notice_exam_results, null);
        final EditText txtNoticeOfExam = (EditText) inputNotice.findViewById(R.id.txtNoticeOfExam);
        txtNoticeOfExam.setText(examResults.getNotice());
        builder.setView(inputNotice);

        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String notice = txtNoticeOfExam.getText().toString();
                if (!notice.trim().isEmpty()) {
                    examResults.setNotice(notice);
                }
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        Dialog dialog = builder.create();

        return dialog;
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
        return studentIds.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        return TYPE_EXAM;
//    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;
        EditText txtInputExamResults;
        NetworkImageView imgUserAvata;
        TextView row_title;
        TextView lbNickName;
        ImageView btnAddNoticeExamResutls;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
            txtInputExamResults = (EditText) view.findViewById(R.id.txtInputExamResults);
            imgUserAvata = (NetworkImageView) view.findViewById(R.id.row_icon);
            row_title = (TextView) view.findViewById(R.id.row_title);
            lbNickName = (TextView) view.findViewById(R.id.lbNickName);
            btnAddNoticeExamResutls = (ImageView) view.findViewById(R.id.btnAddNoticeExamResutls);
        }
    }

    public void clearData() {
        inputExamResults.clear();
        mapExam.clear();
    }

    public List<ExamResult> getInputExamResults() {
        List<ExamResult> examResults = new ArrayList<>();
        Map<Integer, ExamResult> listMap = new TreeMap<>(mapExam);
        for (Integer studentId : listMap.keySet()) {
            ExamResult examResult = listMap.get(studentId);
            if (examResult != null) {
                examResults.add(examResult);
            }
        }
        return examResults;
    }

    private Map<Long, ArrayList<ExamResult>> groupExamByDate(List<ExamResult> examResults) {
        exam_dates = new ArrayList<>();
        Map<Long, ArrayList<ExamResult>> examByMonthList = new HashMap<>();
        for (ExamResult examResult : examResults) {
            int exam_month = examResult.getExam_month();
            int exam_year = examResult.getExam_year();
            long exam_date = LaoSchoolShared.getLongDate(exam_month, exam_year);
            //Log.d(TAG, exam_month + "/" + exam_year + "-exam_date:" + exam_date);
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
            exam_dates.add(exam_date);
        }

        Map<Long, ArrayList<ExamResult>> examsTree = new TreeMap<>(examByMonthList);
        return examsTree;
    }


}
