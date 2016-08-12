package com.laoschool.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.ExamResult;
import com.laoschool.shared.LaoSchoolShared;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Hue on 6/30/2016.
 */
public class InputExamResultsbyTypeAdapter extends RecyclerView.Adapter<InputExamResultsbyTypeAdapter.ViewHolder> {
    private static final String TAG = InputExamResultsbyTypeAdapter.class.getSimpleName();
    private final Activity activity;
    private final List<ExamResult> examResults;
    private Context context;
    private Map<Integer, ExamResult> mapExam = new HashMap<>();
    List<ExamResult> origin_examResults = new ArrayList<>();
    private int exam_date_input;
    List<Long> exam_dates;


    public InputExamResultsbyTypeAdapter(Activity activity, Context context, List<ExamResult> examResults, int selectedDateInputExamResult) {
        this.context = context;
        this.activity = activity;
        this.examResults = examResults;
        this.exam_date_input = selectedDateInputExamResult;
        origin_examResults.addAll(examResults);
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
            final ExamResult result = this.examResults.get(position);
            // final_result = defineResults(final_result);
            String sresult = "";
            String notice = "";
            String exam_dt = "";
            String scoreJson = getScorebyType(result, exam_date_input);

            if (scoreJson != null && !scoreJson.trim().isEmpty()) {
                //{“sresult” = “10”;
//                “notice” = “xxxxxx”;
//                “exam_dt” = “2016-09-09”}
                try {
                    JSONObject scoreObj = new JSONObject(scoreJson);
                    sresult = scoreObj.getString("sresult");
                    notice = scoreObj.getString("notice");
                    exam_dt = scoreObj.getString("exam_dt");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            result.setSresult(sresult);
            result.setNotice(notice);
            result.setExam_dt(exam_dt);
            String userName = result.getStd_fullname();
            final int studentId = result.getStudent_id();

            //fill data exam results
            holder.row_title.setText(userName);
            holder.lbNickName.setText(result.getStd_nickname());
            holder.txtInputExamResults.setText(result.getSresult());
            if (position == (examResults.size() - 1)) {
                holder.txtInputExamResults.setImeOptions(EditorInfo.IME_ACTION_DONE);
            } else {
                holder.txtInputExamResults.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            }

            //Load photo
            String photo = result.getStd_photo();
            if (photo != null) {
                LaoSchoolSingleton.getInstance().getImageLoader().get(photo, ImageLoader.getImageListener(holder.imgUserAvata,
                        R.drawable.ic_account_circle_black_36dp, R.drawable.ic_account_circle_black_36dp),R.dimen.avata_width,R.dimen.avata_height, ImageView.ScaleType.FIT_XY);
                holder.imgUserAvata.setImageUrl(photo, LaoSchoolSingleton.getInstance().getImageLoader());
            } else {
                holder.imgUserAvata.setDefaultImageResId(R.drawable.ic_account_circle_black_36dp);
            }


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
                    String score = editable.toString();
                    if (!score.trim().isEmpty()) {
                        Log.d(TAG, "addTextChangedListener().afterTextChanged(" + position + ") -exam:" + editable.toString());
                        Float exam = Float.valueOf(score);
                        if (exam < 0 || exam > 10) {
                            holder.txtInputExamResults.setError("");
                            return;
                        }
                        result.setSresult(String.valueOf(exam));
                        mapExam.put(studentId, result);
                    }
                }
            });
            holder.txtInputExamResults.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasfocus) {
                    if (!hasfocus) {
                        Log.d(TAG, "-focus change:" + hasfocus);
                        if (!holder.txtInputExamResults.getText().toString().trim().isEmpty()) {
                            holder.txtInputExamResults.setText(result.getSresult());
                            //LaoSchoolShared.hideSoftKeyboard(activity);
                        }
                    }
                }
            });

            holder.txtInputExamResults.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        LaoSchoolShared.hideSoftKeyboard(activity);
                    }
                    return handled;
                }
            });


            holder.btnAddNoticeExamResutls.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = makeDialogInputNotice(result);
                    dialog.show();

                }
            });
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder() -exception:" + e.getMessage());
            e.printStackTrace();
        }
        holder.setIsRecyclable(false);
    }

    private String getScorebyType(ExamResult result, int exam_date_input) {
        String type = "";
        switch (exam_date_input) {
            case 1:
                type = result.getM1();
                break;
            case 2:
                type = result.getM2();
                break;
            case 3:
                type = result.getM3();
                break;
            case 4:
                type = result.getM4();
                break;
            case 6:
                type = result.getM6();
                break;
            case 7:
                type = result.getM7();
                break;
            case 8:
                type = result.getM8();
                break;
            case 9:
                type = result.getM9();
                break;
            case 10:
                type = result.getM10();
                break;
            case 12:
                type = result.getM12();
                break;
        }
        return type;
    }

    private ExamResult defineResults(ExamResult examResult) {
        switch (exam_date_input) {
            case 1:
                examResult = parseScore(examResult, examResult.getM1());
                break;
            case 2:
                examResult = parseScore(examResult, examResult.getM2());
                break;
            case 3:
                examResult = parseScore(examResult, examResult.getM3());
                break;
            case 4:
                examResult = parseScore(examResult, examResult.getM4());
                break;
            case 6:
                examResult = parseScore(examResult, examResult.getM6());
                break;
            case 7:
                examResult = parseScore(examResult, examResult.getM7());
                break;
            case 8:
                examResult = parseScore(examResult, examResult.getM8());
                break;
            case 9:
                examResult = parseScore(examResult, examResult.getM9());
                break;
            case 10:
                examResult = parseScore(examResult, examResult.getM10());
                break;
            case 12:
                examResult = parseScore(examResult, examResult.getM12());
                break;
        }
        return examResult;
    }

    private ExamResult parseScore(ExamResult examResult, String score) {
        String sresult = "";
        String notice = "";
        String exam_dt = "";
        if (score != null && !score.trim().isEmpty()) {
            //{“sresult” = “10”;
//                “notice” = “xxxxxx”;
//                “exam_dt” = “2016-09-09”}
            try {
                JSONObject scoreObj = new JSONObject(score);
                sresult = scoreObj.getString("sresult");
                notice = scoreObj.getString("notice");
                exam_dt = scoreObj.getString("exam_dt");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        examResult.setSresult(sresult);
        examResult.setNotice(notice);
        examResult.setExam_dt(exam_dt);
        return examResult;
    }

    private Dialog makeDialogInputNotice(final ExamResult examResults) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View inputNotice = View.inflate(context, R.layout.view_input_notice_exam_results, null);
        final EditText txtNoticeOfExam = (EditText) inputNotice.findViewById(R.id.txtNoticeOfExam);
        txtNoticeOfExam.setText(examResults.getNotice());
        builder.setView(inputNotice);

        builder.setPositiveButton(R.string.SCCommon_Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String notice = txtNoticeOfExam.getText().toString();
                if (!notice.trim().isEmpty()) {
                    examResults.setNotice(notice);
                }
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton(R.string.SCCommon_Cancel, new DialogInterface.OnClickListener() {
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
        return examResults.size();
    }

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


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        examResults.clear();
        if (charText.length() == 0) {
            examResults.addAll(origin_examResults);
        } else {
            for (ExamResult examResult : origin_examResults) {
                String studentName = examResult.getStudent_name();
                if (charText.length() != 0 && studentName.toLowerCase(Locale.getDefault()).contains(charText)) {
                    examResults.add(examResult);
                }
            }
        }
        notifyDataSetChanged();
    }


}

