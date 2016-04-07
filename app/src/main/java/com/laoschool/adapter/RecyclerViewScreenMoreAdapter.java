package com.laoschool.adapter;

import android.content.Context;
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
                if (txtMoreScreenName != null) {
                    txtMoreScreenName.setText(title);
                }
                if (title.equals(context.getString(R.string.title_screen_school_information))) {
                } else if (title.equals(context.getString(R.string.title_screen_list_teacher))) {
                    if (row_icon != null) {
                        row_icon.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_list_black_24dp));
                    }
                } else if (title.equals(context.getString(R.string.title_screen_setting))) {
                    if (row_icon != null) {
                        row_icon.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_settings_black_24dp));
                    }
                } else if (title.equals(context.getString(R.string.action_log_out))) {
                    if (row_icon != null) {
                        row_icon.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_exit_to_app_black_24dp));
                    }

                } else if (title.equals(context.getString(R.string.title_screen_final_results_student))) {
                } else if (title.equals(context.getString(R.string.title_screen_exam_results))) {
                    if (row_icon != null) {
                        row_icon.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_date_range_black_24dp));
                    }
                }
                //Handler on click item
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context.getActivity(), "position:" + position, Toast.LENGTH_SHORT).show();
                        if (title.equals(context.getString(R.string.title_screen_school_information))) {
                            iScreenMore.gotoSchoolInformationformMore();
                        } else if (title.equals(context.getString(R.string.title_screen_list_teacher))) {
                            iScreenMore.gotoListTearcherformMore();
                        } else if (title.equals(context.getString(R.string.title_screen_setting))) {
                            iScreenMore.gotoSettingformMore();
                        } else if (title.equals(context.getString(R.string.action_log_out))) {
                            iScreenMore.logoutApplication();
                        } else if (title.equals(context.getString(R.string.title_screen_final_results_student))) {
                            iScreenMore.gotoSchoolRecordbyYearformMore();
                        } else if (title.equals(context.getString(R.string.title_screen_exam_results))) {
                            iScreenMore.gotoExamResultsformMore();
                        }else if (title.equals(context.getString(R.string.title_screen_schedule))) {
                            iScreenMore.gotoScheduleformMore();
                        }
                    }
                });
            } else if (holder.viewType == TYPE_SUB_HEADER) {
                TextView row_title = (TextView) view.findViewById(R.id.txtTitle);
                row_title.setText("More");
                row_title.setTextColor(context.getResources().getColor(R.color.textColorSubHeader));
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
            return TYPE_SUB_HEADER;
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
