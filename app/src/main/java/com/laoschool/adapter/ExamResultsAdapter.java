package com.laoschool.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.ExamResult;
import com.laoschool.screen.ScreenExamResults;
import com.laoschool.screen.view.DialogInputExamResultsForStudent;
import com.laoschool.shared.LaoSchoolShared;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Hue on 6/30/2016.
 */
public class ExamResultsAdapter extends RecyclerView.Adapter<ExamResultsAdapter.StudentInfomationViewHolder> {
    private static final String TAG = ExamResultsAdapter.class.getSimpleName();
    private ScreenExamResults screenExamResults;
    private Context context;
    private ScreenExamResults.IScreenExamResults iScreenExamResults;
    private static final int TYPE_SEARCH = 0;
    private int TYPE_EXAM = 1;
    private int subjectId;
    private List<ExamResult> examResults = new ArrayList<>();
    List<ExamResult> origin_examResults = new ArrayList<>();

    public ExamResultsAdapter(ScreenExamResults screenExamResults, int subjectId, List<ExamResult> examResults) {
        this.screenExamResults = screenExamResults;
        this.context = screenExamResults.getActivity();
        this.iScreenExamResults = screenExamResults.iScreenExamResults;
        this.subjectId = subjectId;
        this.examResults = examResults;
        this.origin_examResults.addAll(examResults);

    }

    @Override
    public StudentInfomationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_SEARCH) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_exam_results, parent, false); //Inflating the layout
        } else if (viewType == TYPE_EXAM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exam_results, parent, false); //Inflating the layout
        }
        StudentInfomationViewHolder viewHolder = new StudentInfomationViewHolder(view, viewType);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(StudentInfomationViewHolder holder, int position) {
        try {
            if (holder instanceof StudentInfomationViewHolder) {
                StudentInfomationViewHolder infomationViewHolder = holder;
                if (holder.viewType == TYPE_SEARCH) {
                } else if (holder.viewType == TYPE_EXAM) {
                    View view = infomationViewHolder.view;
                    ExamResult examResult = examResults.get(position);
                    String userName = examResult.getStudent_name();

                    TextView row_title = (TextView) view.findViewById(R.id.row_title);
                    TextView lbNickName = (TextView) view.findViewById(R.id.lbNickName);
                    NetworkImageView imgUserAvata = (NetworkImageView) view.findViewById(R.id.row_icon);

                    //list score of student
                    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.mListScoreBySemester);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(gridLayoutManager);

                    int currentTerm = LaoSchoolShared.myProfile.getEclass().getTerm();
                    EditExamAdapter scoresAdapter = new EditExamAdapter(context, currentTerm, examResult);
                    recyclerView.setAdapter(scoresAdapter);
                    recyclerView.setNestedScrollingEnabled(false);

                    //fill data exam results
                    lbNickName.setText(examResult.getStd_nickname());
                    row_title.setText(userName);


                    //Load photo student
                    String photo = examResult.getStd_photo();
                    if (photo != null) {
                        LaoSchoolSingleton.getInstance().getImageLoader().get(photo, ImageLoader.getImageListener(imgUserAvata,
                                R.drawable.ic_account_circle_black_36dp, R.drawable.ic_account_circle_black_36dp));
                        imgUserAvata.setImageUrl(photo, LaoSchoolSingleton.getInstance().getImageLoader());
                    } else {
                        imgUserAvata.setDefaultImageResId(R.drawable.ic_account_circle_black_36dp);
                    }

                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder() -exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return examResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_EXAM;
    }

    public class StudentInfomationViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public StudentInfomationViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
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

    class EditExamAdapter extends RecyclerView.Adapter<EditExamAdapter.ViewHolder> {
        private final String TAG = EditExamAdapter.class.getSimpleName();
        Context context;
        List<String> str_score = new ArrayList<>();
        List<String> str_score_name = new ArrayList<>();
        ExamResult examResult;

        public EditExamAdapter(Context context, int termId, ExamResult examResult) {
            this.context = context;
            this.examResult = examResult;
            if (termId == 1) {
                str_score.add(examResult.getM1());
                str_score.add(examResult.getM2());
                str_score.add(examResult.getM3());
                str_score.add(examResult.getM4());
                str_score.add(examResult.getM5());

                str_score.add(examResult.getM6());
                str_score.add(examResult.getM7());
                str_score_name.addAll(Arrays.asList(context.getResources().getStringArray(R.array.exam_name_term_1)));
            } else {
                str_score.add(examResult.getM8());
                str_score.add(examResult.getM9());
                str_score.add(examResult.getM10());

                str_score.add(examResult.getM11());
                str_score.add(examResult.getM12());
                str_score.add(examResult.getM13());
                str_score.add(examResult.getM14());
                str_score.add(examResult.getM15());

                str_score_name.addAll(Arrays.asList(context.getResources().getStringArray(R.array.exam_name_term_2)));
            }
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_score, parent, false); //Inflating the layout
            ViewHolder scoreStudentSemesterAdapterViewHolder = new ViewHolder(view, viewType);
            return scoreStudentSemesterAdapterViewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            View view = holder.view;
            String score = str_score.get(position);

            final TextView lbScoreMonth = ((TextView) (view.findViewById(R.id.lbScoreMonth)));
            final TextView lbScore = ((TextView) (view.findViewById(R.id.lbScore)));
            lbScoreMonth.setText(str_score_name.get(position));
            if (position == 7) {
                view.setBackgroundResource(R.drawable.bg_score_avg_year);
            } else if (position == 6) {
                view.setBackgroundResource(R.drawable.bg_score_avg_term);
            } else if (position == 5) {
                view.setBackgroundResource(R.drawable.bg_score_avg_month);
            } else if (position == 4) {
                view.setBackgroundResource(R.drawable.bg_score_final);
            }
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
                    lbScore.setText(sresult);
                    examResult.setSresult(sresult);
                    examResult.setNotice(notice);
                    examResult.setExam_dt(exam_dt);
                } catch (JSONException e) {
                    lbScore.setText(score);
                    e.printStackTrace();
                }
            } else {
                lbScore.setText("");
            }
            final String finalNotice = notice;
            final String finalExam_dt = exam_dt;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position < 4 || position == 5) {
                        DialogInputExamResultsForStudent dialogInputExamResultsForStudent = new DialogInputExamResultsForStudent(screenExamResults, subjectId, examResult);
                        dialogInputExamResultsForStudent.show(screenExamResults.getActivity().getFragmentManager(), DialogInputExamResultsForStudent.TAG);
                    } else {
                        examResult.setSresult(lbScore.getText().toString());
                        examResult.setExam_name(lbScoreMonth.getText().toString());
                        examResult.setNotice(finalNotice);
                        examResult.setExam_dt(finalExam_dt);
                        _showDetailsExamResults(examResult);
                    }
                }
            });
        }

        private void _showDetailsExamResults(ExamResult examResult) {
            try {
                AlertDialog.Builder bDetails = new AlertDialog.Builder(context);
                View examResultDetails = View.inflate(context, R.layout.view_exam_results_details, null);
                ((TextView) examResultDetails.findViewById(R.id.lbExamSubject)).setText(String.valueOf(examResult.getSubjectName()));
                ((TextView) examResultDetails.findViewById(R.id.lbExamDate)).setText(examResult.getExam_name());
                String score = examResult.getSresult();
                ((TextView) examResultDetails.findViewById(R.id.lbExamScore)).setText(String.valueOf(score));
                ((TextView) examResultDetails.findViewById(R.id.lbExamTecherName)).setText(String.valueOf(examResult.getTeacherName()));
                ((TextView) examResultDetails.findViewById(R.id.lbExamDateUpdateScore)).setText(" - " + LaoSchoolShared.formatDate(examResult.getExam_dt(), 2));
                ((TextView) examResultDetails.findViewById(R.id.lbExamNotice)).setText(String.valueOf(examResult.getNotice()));
                bDetails.setView(examResultDetails);
                final Dialog dialog = bDetails.create();
                (examResultDetails.findViewById(R.id.lbExamClose)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return str_score.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            View view;
            int viewType;

            public ViewHolder(View itemView, int viewType) {
                super(itemView);
                this.view = itemView;
                this.viewType = viewType;
            }
        }
    }

}

