package com.laoschool.screen;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenMarkScoreStudent extends Fragment implements FragmentLifecycle {


    private static final String TAG = "ScreenMarkScoreStudent";
    private Context context;

    public String getDataMessage() {
        return dataMessage;
    }

    public void setDataMessage(String dataMessage) {
        this.dataMessage = dataMessage;
    }

    private String dataMessage;
    private int containerId;

    private TextView txtUserName;
    private LinearLayout mFactorScoreI;

    private List<String> markScore = new ArrayList<String>();

    interface IScreenMarkScoreStudent {
        void doneMarkScoreStudent();
    }

    public IScreenMarkScoreStudent iScreenMarkScoreStudent;

    public ScreenMarkScoreStudent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.screen_mark_score_student, container, false);
        Log.d(getString(R.string.title_screen_mark_score_student), "-Tag:" + getTag());
        txtUserName = (TextView) view.findViewById(R.id.txtUserName);
        mFactorScoreI = (LinearLayout) view.findViewById(R.id.mFactorScoreI);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.context = getActivity();
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            Log.d(getString(R.string.title_screen_mark_score_student), "-Container Id:" + containerId);

//            String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_EXAM_RESULTS_2);
//            Log.d(getString(R.string.title_screen_select_list_student), "-TAG Screen Exam Results:" + tag);


//            HomeActivity homeActivity = (HomeActivity) getActivity();
//            ScreenExamResults screenExamResults = (ScreenExamResults) homeActivity.getSupportFragmentManager().findFragmentByTag(tag);
//            if (screenExamResults != null) {
//                Log.d(getString(R.string.title_screen_mark_score_student), "Data:" + screenExamResults.getData());
//                this.setDataMessage(dataMessage);
//                Toast.makeText(homeActivity, "Data=" + dataMessage, Toast.LENGTH_SHORT).show();
//            } else {
//                Log.d(getString(R.string.title_screen_mark_score_student), "Data is null");
//                Toast.makeText(homeActivity, "Data is null", Toast.LENGTH_SHORT).show();
//            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_screen_mark_score_student, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_done_mark_score:
                iScreenMarkScoreStudent.doneMarkScoreStudent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPauseFragment() {
    }

    @Override
    public void onResumeFragment() {
        try {
            HomeActivity activity = (HomeActivity) getActivity();
            Log.d(getString(R.string.title_screen_mark_score_student), "data:" +
                    activity.student);
//            Toast.makeText(getActivity(), "Data =" +
//                    activity.student, Toast.LENGTH_SHORT).show();
            if (txtUserName != null) {
                txtUserName.setText(activity.student);
            }
            if (mFactorScoreI != null) {
                _defineViewScore();
            } else {
                Log.d(TAG, "mFactorScoreI null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void _defineViewScore() {
        try {
            List<String> scores = Arrays.asList("9", "3", "8", "2", "1", "0", "9", "3", "9", "3", "8", "9", "3", "8", "2", "1", "0", "9", "3", "9", "3", "8", "+");
            markScore.addAll(scores);
            int countScore = scores.size();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout.LayoutParams mainScoreParam = (RelativeLayout.LayoutParams) mFactorScoreI.getLayoutParams();
            int height = ((getResources().getDimensionPixelSize(R.dimen.width_height_score_box)) + (getResources().getDimensionPixelSize(R.dimen.magrin_left_bottom_score)) + (getResources().getDimensionPixelSize(R.dimen.magrin_left_bottom_score)));
            Log.d(TAG, "Height score:" + height);
            if (countScore > 10) {
                final int countLine = Math.round(countScore / 10) + 1;
                Log.d(TAG, "-Count:" + countScore + "-countLine :" + countLine);
                int start = 0;
                int end = 10;
                for (int line = 0; line < countLine; line++) {
                    final LinearLayout mScore = new LinearLayout(context);
                    mScore.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams mScoreparam = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    mScore.setLayoutParams(mScoreparam);
                    Log.d(TAG, "-start:" + start + "-end :" + end);
                    List<String> cloneScores = scores.subList(start, end);
                    start += 10;
                    end += 10;
                    if (line > 0) {
                        if (end > (countScore - 10)) {
                            end = countScore;
                        }
                    }
                    for (int indexScore = 0; indexScore < cloneScores.size(); indexScore++) {
                        final String score = cloneScores.get(indexScore);
                        final RelativeLayout panelScore = _defineViewByScore(score, line, indexScore);
                        mScore.addView(panelScore);
                        View.OnClickListener changeScore = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "Change score in " + v.getTag());
                                Toast.makeText(context, "Change score", Toast.LENGTH_SHORT).show();
                            }
                        };

                        final int _indexScore = indexScore;
                        final int _line = line;
                        View.OnClickListener addScore = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                _addNewScore(mFactorScoreI, mScore, v, countLine, _line, _indexScore + 1);
                            }
                        };
                        if (score.equals("+")) {
                            panelScore.setOnClickListener(addScore);
                        } else {
                            panelScore.setOnClickListener(changeScore);
                        }


                    }
                    mFactorScoreI.addView(mScore);
                }

                mainScoreParam.height = (countLine * height);
                mFactorScoreI.setLayoutParams(mainScoreParam);
            } else {
                LinearLayout mScore = new LinearLayout(context);
                mScore.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams mScoreparam = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mScore.setLayoutParams(mScoreparam);

                for (int i = 0; i < countScore; i++) {
                    String score = scores.get(i);
                    Log.d(TAG, "-ScorePageAdapter :" + score);
                    //
                    RelativeLayout panelScore = _defineViewByScore(score, 0, i);
                    mScore.addView(panelScore);
                }

                //Add + in end
                View view_mark_score = inflater.inflate(R.layout.view_mark_score, null);
                TextView txtScore = (TextView) view_mark_score.findViewById(R.id.txtScore);

                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.width_height_score_box), context.getResources().getDimensionPixelSize(R.dimen.width_height_score_box));
                //param.leftMargin = context.getResources().getDimensionPixelOffset(R.dimen.magrin_left_score);
                view_mark_score.setLayoutParams(param);

                txtScore.setText("+");
                mScore.addView(view_mark_score);
                mFactorScoreI.addView(mScore);
                mainScoreParam.height = height;
                mFactorScoreI.setLayoutParams(mainScoreParam);
            }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void _addNewScore(final LinearLayout mFactorScoreI, LinearLayout mScore, final View panelScore, int countLine, final int line, final int scoreIndex) {
        //Toast.makeText(context, "Add score", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "countLine:" + countLine + "\t-line:" + line + "\t-scoreIndex:" + scoreIndex);

        //View viewNewScore = _defineViewByScore("0");
        View viewAddScore;
        boolean flagNewLine = false;
        if (mScore.getChildCount() >= 10) {
            RelativeLayout.LayoutParams mainScoreParam = (RelativeLayout.LayoutParams) mFactorScoreI.getLayoutParams();
            int height = ((getResources().getDimensionPixelSize(R.dimen.width_height_score_box)) + (getResources().getDimensionPixelSize(R.dimen.magrin_left_bottom_score)) + (getResources().getDimensionPixelSize(R.dimen.magrin_left_bottom_score)));
            Log.d(TAG, "Height score:" + height);
            countLine++;
            mScore = new LinearLayout(context);
            mScore.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams mScoreparam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mScore.setLayoutParams(mScoreparam);
            //mScore.addView(viewNewScore);
            viewAddScore = _defineViewByScore("+", line + 1, 0);
            mScore.addView(viewAddScore);
            markScore.add("0");
            mFactorScoreI.addView(mScore);
            mainScoreParam.height = countLine * height;
            mFactorScoreI.setLayoutParams(mainScoreParam);
            flagNewLine = true;
        } else {
            //mScore.addView(viewNewScore);
            viewAddScore = _defineViewByScore("+", line, scoreIndex);
            mScore.addView(viewAddScore);
            markScore.add("0");
            flagNewLine = false;

        }
        final LinearLayout finalMScore = mScore;

        final boolean finalFlagNewLine = flagNewLine;
        final int finalCountLine = countLine;
        viewAddScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int _scoreIndex = scoreIndex + 1;
                int _countLine = finalCountLine;
                int _line = line;
                if (finalFlagNewLine) {
                    //_countLine = finalCountLine + 1;
                    _line = line + 1;
                    _scoreIndex = 1;
                }
                _addNewScore(mFactorScoreI, finalMScore, v, _countLine, _line, _scoreIndex);
            }
        });
        TextView txtScore = (TextView) panelScore.findViewById(R.id.txtScore);
        if (txtScore != null) {
            txtScore.setText("0");
        }
        panelScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Change score in " + v.getTag());

                Toast.makeText(context, "Change score in " + v.getTag(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private RelativeLayout _defineViewByScore(String score, int row, int j) {
        RelativeLayout panelScore = new RelativeLayout(context);
        panelScore.setTag(row + "-" + j);
        panelScore.setBackgroundColor(getResources().getColor(R.color.colorLaosBlue));

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.width_height_score_box), context.getResources().getDimensionPixelSize(R.dimen.width_height_score_box));
        params1.setMargins(0, 0, getResources().getDimensionPixelSize(R.dimen.magrin_left_bottom_score), getResources().getDimensionPixelSize(R.dimen.magrin_left_bottom_score));
        panelScore.setLayoutParams(params1);

        TextView txtScore = new TextView(context);
        txtScore.setId(R.id.txtScore);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        txtScore.setTextColor(getResources().getColor(android.R.color.white));
        txtScore.setLayoutParams(layoutParams);

        txtScore.setText(score);
        panelScore.addView(txtScore);
        return panelScore;
    }


    public static Fragment instantiate(int containerId, String currentRole) {
        ScreenMarkScoreStudent fragment = new ScreenMarkScoreStudent();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iScreenMarkScoreStudent = (IScreenMarkScoreStudent) activity;
    }

}
