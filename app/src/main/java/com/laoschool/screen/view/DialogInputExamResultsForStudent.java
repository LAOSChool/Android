package com.laoschool.screen.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.ExamResult;
import com.laoschool.model.AsyncCallback;
import com.laoschool.shared.LaoSchoolShared;

/**
 * Created by Hue on 6/9/2016.
 */
@SuppressLint("ValidFragment")
public class DialogInputExamResultsForStudent extends DialogFragment {
    public static final String TAG = DialogInputExamResultsForStudent.class.getSimpleName();
    private final ExamResult examResult;

    public DialogInputExamResultsForStudent(ExamResult examResult) {
        this.examResult = examResult;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_input_exam_resutls_for_student, container, false);
        final Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.TextAppearance_Theme_Dialog);

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
                    R.drawable.ic_account_circle_black_36dp, R.drawable.ic_account_circle_black_36dp));
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
                    examResult.setSresult(exam);
                }
                if (!notice.trim().toString().isEmpty()) {
                    examResult.setNotice(notice);
                }
                examResult.setTeacher_id(LaoSchoolShared.myProfile.getId());
                Log.d(TAG, "-submit examResult:" + examResult.toJson());
                inputExamResults(examResult);

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
        return view;
    }

    private void inputExamResults(ExamResult examResult) {
        LaoSchoolSingleton.getInstance().getDataAccessService().inputExamResults(examResult, new AsyncCallback<ExamResult>() {
            @Override
            public void onSuccess(ExamResult result) {
                Log.d(TAG, "inputExamResults().onSuccess() -result:" + result.toJson());
                getDialog().dismiss();
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
}
