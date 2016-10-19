package com.laoschool.screen.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.ExamResult;
import com.laoschool.model.AsyncCallback;
import com.laoschool.screen.ScreenExamResults;
import com.laoschool.shared.LaoSchoolShared;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Hue on 6/9/2016.
 */
@SuppressLint("ValidFragment")
public class DialogInputExamResultsForStudent extends DialogFragment implements View.OnClickListener {
    public static final String TAG = DialogInputExamResultsForStudent.class.getSimpleName();
    private final ExamResult examResult;
    private final int subjectId;
    private final int position;
    private final int termId;
    private InputMethodManager imm;
    private ScreenExamResults screenExamResults;

    public DialogInputExamResultsForStudent(ScreenExamResults screenExamResults, int subjectId, int termId, int position, ExamResult examResult) {
        this.examResult = examResult;
        this.subjectId = subjectId;
        this.termId = termId;
        this.position = position;
        this.screenExamResults = screenExamResults;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_input_exam_resutls_for_student, container, false);
        final Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.TextAppearance_Theme_Dialog);
        View mainDialog = view.findViewById(R.id.mainDialog);
        TextView lbStudentName = (TextView) view.findViewById(R.id.txbTitleDialog);
        TextView lbNickName = (TextView) view.findViewById(R.id.lbNickName);
        ImageView imgCloseDialog = (ImageView) view.findViewById(R.id.imgCloseDialog);


        TextView lbSubjectOfExam = (TextView) view.findViewById(R.id.lbSubjectOfExam);
        TextView lbMonthOfExam = (TextView) view.findViewById(R.id.lbMonthOfExam);
        NetworkImageView imgIcon = (NetworkImageView) view.findViewById(R.id.imgIcon);
        final EditText txtScoreOfExam = (EditText) view.findViewById(R.id.txtScoreOfExam);
        final EditText txtNoticeOfExam = (EditText) view.findViewById(R.id.txtNoticeOfExam);

        TextView btnSubmitInputExamResults = (TextView) view.findViewById(R.id.btnSubmitInputExamResults);
        TextView btnCancelSubmitInputExamResults = (TextView) view.findViewById(R.id.btnCancelSubmitInputExamResults);

        //set student name
        lbStudentName.setText(examResult.getStudent_name());
        lbNickName.setText(examResult.getStd_nickname());
        //set subject name
        lbSubjectOfExam.setText(examResult.getSubjectName());

        //load photo
        String photo = examResult.getStd_photo();
        if (photo != null) {
            LaoSchoolSingleton.getInstance().getImageLoader().get(photo, ImageLoader.getImageListener(imgIcon,
                    R.drawable.ic_account_circle_black_36dp, R.drawable.ic_account_circle_black_36dp), R.dimen.avata_width, R.dimen.avata_height, ImageView.ScaleType.FIT_XY);
            imgIcon.setImageUrl(photo, LaoSchoolSingleton.getInstance().getImageLoader());
        } else {
            imgIcon.setDefaultImageResId(R.drawable.ic_account_circle_black_36dp);
        }
        lbMonthOfExam.setText(examResult.getExam_name());

        //set score & notice
        txtScoreOfExam.setText(examResult.getSresult());
        txtNoticeOfExam.setText(examResult.getNotice());


        //

        btnSubmitInputExamResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String exam = txtScoreOfExam.getText().toString();
                String notice = txtNoticeOfExam.getText().toString();
                if (!exam.trim().toString().isEmpty()) {
                    Float score = Float.valueOf(exam);
                    if (score > 0 && score <= 10) {
                        examResult.setSresult(exam);
                        if (!notice.trim().toString().isEmpty()) {
                            examResult.setNotice(notice);
                        } else {
                            examResult.setNotice("");
                        }
                        examResult.setTeacher_id(LaoSchoolShared.myProfile.getId());
                        inputExamResults(examResult);
                    } else {
                        txtScoreOfExam.setError("Score > 0  and score < 10");
                    }
                } else {
                    //set Empty score
                    examResult.setSresult("");
                    examResult.setNotice("");
                    examResult.setTeacher_id(LaoSchoolShared.myProfile.getId());
                    inputExamResults(examResult);
                }


            }
        });

        //
        btnCancelSubmitInputExamResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        imgCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        txtScoreOfExam.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        txtNoticeOfExam.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtScoreOfExam.clearFocus();
                txtNoticeOfExam.clearFocus();
                //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        return view;
    }

    private void hideKeyboard(View v) {
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void hideSoftKeyBoard(EditText editText) {
        //editText.setInputType(0);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void inputExamResults(ExamResult examResult) {
        String score = LaoSchoolShared.makeJsonScore(examResult.getSresult(), examResult.getNotice());
        if (termId == 1) {
            switch (position) {
                case 0:
                    Log.d(TAG, "inputExamResults() - input term " + termId + " for M1 score:" + score);
                    examResult.setM1(score);
                    break;
                case 1:
                    Log.d(TAG, "inputExamResults() - input term " + termId + " for M2 score:" + score);
                    examResult.setM2(score);
                    break;
                case 2:
                    Log.d(TAG, "inputExamResults() - input term " + termId + " for M3 score:" + score);
                    examResult.setM3(score);
                    break;
                case 3:
                    Log.d(TAG, "inputExamResults() - input term " + termId + " for M4 score:" + score);
                    examResult.setM4(score);
                    break;
                case 5:
                    Log.d(TAG, "inputExamResults() - input term " + termId + " for M6 score:" + score);
                    examResult.setM6(score);
                    break;
            }
        } else if (termId == 2) {
            switch (position) {
                case 0:
                    Log.d(TAG, "inputExamResults() - input term " + termId + " for M8 score:" + score);
                    examResult.setM8(score);
                    break;
                case 1:
                    Log.d(TAG, "inputExamResults() - input term " + termId + " for M9 score:" + score);
                    examResult.setM9(score);
                    break;
                case 2:
                    Log.d(TAG, "inputExamResults() - input term " + termId + " for M10 score:" + score);
                    examResult.setM10(score);
                    break;
                case 3:
                    Log.d(TAG, "inputExamResults() - input term " + termId + " for M11 score:" + score);
                    examResult.setM11(score);
                    break;
                case 5:
                    Log.d(TAG, "inputExamResults() - input term " + termId + " for M12 score:" + score);
                    examResult.setM12(score);
                    break;
            }

        }

        LaoSchoolSingleton.getInstance().getDataAccessService().inputExamResults(examResult, new AsyncCallback<ExamResult>() {
            @Override
            public void onSuccess(ExamResult result) {
                Log.d(TAG, "inputExamResults().onSuccess() -result:" + result.toJson());
                getDialog().dismiss();
                screenExamResults.reloadDataAfterInputSingleScore(subjectId);
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "inputExamResults().onFailure() -message:" + message);

            }

            @Override
            public void onAuthFail(String message) {
                Log.d(TAG, "inputExamResults().onAuthFail() -message:" + message);
            }
        });
    }


    @Override
    public void onClick(View view) {

    }
}
