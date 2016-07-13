package com.laoschool.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.laoschool.R;
import com.laoschool.screen.ScreenMore;
import com.laoschool.shared.LaoSchoolShared;

import java.util.List;

/**
 * Created by Hue on 3/5/2016.
 */
public class RecyclerViewScreenMoreAdapter extends RecyclerView.Adapter<RecyclerViewScreenMoreAdapter.RecyclerViewScreenMoreAdapterViewHolder> {

    private ScreenMore screenMore;
    private Context context;
    private ScreenMore.IScreenMore iScreenMore;
    private List<String> object;
    private int TYPE_SUB_HEADER = 0;
    private int TYPE_TITLE = 1;
    private int TYPE_LINE = 2;


    public RecyclerViewScreenMoreAdapter(ScreenMore screenMore, List<String> object) {
        this.screenMore = screenMore;
        this.context = screenMore.getActivity();
        this.iScreenMore = screenMore.iScreenMore;
        this.object = object;
    }

    @Override
    public RecyclerViewScreenMoreAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_LINE)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_line, parent, false); //Inflating the layout
        else if (viewType == TYPE_TITLE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_with_icon, parent, false); //Inflating the layout
        } else if (viewType == TYPE_SUB_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_only_title, parent, false); //Inflating the layout
        }
        RecyclerViewScreenMoreAdapterViewHolder recyclerViewScreenMoreAdapterViewHolder = new RecyclerViewScreenMoreAdapterViewHolder(view, viewType);
        return recyclerViewScreenMoreAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewScreenMoreAdapterViewHolder holder, final int position) {
        View view = holder.view;
        try {
            final String title = object.get(position);
            if (holder.viewType == TYPE_TITLE) {
                //Define and set data
                TextView txtMoreScreenName = (TextView) view.findViewById(R.id.row_title);
                ImageView row_icon = (ImageView) view.findViewById(R.id.row_icon);

                int color = Color.parseColor("#808080");
                row_icon.setColorFilter(context.getResources().getColor(R.color.colorIconOnFragment));

                if (txtMoreScreenName != null) {
                    txtMoreScreenName.setText(title);
                }
                if (title.equals(context.getString(R.string.SCCommon_SchoolInfomation))) {
                    if (row_icon != null) {
                        row_icon.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_account_balance_black_24dp));
                    }
                } else if (title.equals(context.getString(R.string.SCCommon_ListTeacher))) {

                } else if (title.equals(context.getString(R.string.SCCommon_ClassInfo))) {
                    if (row_icon != null) {
                        row_icon.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_people_black_24dp));
                    }
                }else if (title.equals(context.getString(R.string.SCCommon_TimeTable))) {
                    if (row_icon != null) {
                        row_icon.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_date_range_black_24dp));
                    }
                } else if (title.equals(context.getString(R.string.SCCommon_FinalResults))) {
                    if (row_icon != null) {
                        row_icon.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_chrome_reader_mode_black_24dp));
                    }
                } else if (title.equals(context.getString(R.string.SCCommon_ChangePassword))) {
                    if (row_icon != null) {
                        row_icon.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_info_black_24dp));
                    }
                } else if (title.equals(context.getString(R.string.SCCommon_Language))) {
                    if (row_icon != null) {
                        row_icon.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_language_black_24dp));
                    }
                } else if (title.equals(context.getString(R.string.SCCommon_Logout))) {
                    if (row_icon != null) {
                        row_icon.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_exit_to_app_black_24dp));
                    }

                } else if (title.equals(context.getString(R.string.SCCommon_FinalResults))) {
                } else if (title.equals(context.getString(R.string.SCCommon_ExamResults))) {
                    if (row_icon != null) {
                        row_icon.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_date_range_black_24dp));
                    }
                }
                //Handler on click item
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context.getActivity(), "position:" + position, Toast.LENGTH_SHORT).show();
                        if (title.equals(context.getString(R.string.SCCommon_SchoolInfomation))) {
                            iScreenMore.gotoSchoolInformationformMore();
                        } else if (title.equals(context.getString(R.string.SCCommon_ListTeacher))) {
                            iScreenMore.gotoListTearcherformMore();
                        } else if (title.equals(context.getString(R.string.SCCommon_ChangePassword))) {
                            iScreenMore.gotoSettingformMore();
                        } else if (title.equals(context.getString(R.string.SCCommon_Logout))) {
                            iScreenMore.logoutApplication();
                        } else if(title.equals(context.getString(R.string.SCCommon_Language))) {
                            iScreenMore.gotoChangeLanguage();
                        } else if (title.equals(context.getString(R.string.SCCommon_FinalResults))) {
                            iScreenMore.gotoSchoolRecordbyYearformMore();
                        } else if (title.equals(context.getString(R.string.SCCommon_ExamResults))) {
                            iScreenMore.gotoExamResultsformMore();
                        }else if (title.equals(context.getString(R.string.SCCommon_TimeTable))) {
                            iScreenMore.gotoScheduleformMore();
                        }if (title.equals(context.getString(R.string.SCCommon_ClassInfo))) {
                            iScreenMore.gotoListStudentformMore();
                        }
                    }
                });
            } else if (holder.viewType == TYPE_SUB_HEADER) {
                TextView row_title = (TextView) view.findViewById(R.id.txtTitle);
                row_title.setText("More");
                row_title.setTextColor(context.getResources().getColor(R.color.textColorSubHeader));
                row_title.setVisibility(View.GONE);
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return object.size();
    }

    @Override
    public int getItemViewType(int position) {
        String item = object.get(position);
        if (item.equals(context.getString(R.string.row_sub_header)))
            return TYPE_TITLE;
        else if (!item.equals(context.getString(R.string.row_line)))
            return TYPE_TITLE;
        else
            return TYPE_LINE;
    }

    public class RecyclerViewScreenMoreAdapterViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private int viewType;

        public RecyclerViewScreenMoreAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }
}
