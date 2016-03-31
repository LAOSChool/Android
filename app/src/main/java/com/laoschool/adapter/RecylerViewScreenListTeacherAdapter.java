package com.laoschool.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.screen.ScreenListTeacher;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hue on 3/7/2016.
 */
public class RecylerViewScreenListTeacherAdapter extends RecyclerView.Adapter<RecylerViewScreenListTeacherAdapter.RecylerViewScreenListTeacherAdapterViewHolder> {
    private ScreenListTeacher screenListTeacher;
    private Context context;
    private List<String> strings;
    private ScreenListTeacher.IScreenListTeacher iScreenListTeacher;
    private int TYPE_SUB_HEADER = 0;
    private int TYPE_TITLE = 1;
    private int TYPE_LINE = 2;

    public RecylerViewScreenListTeacherAdapter(ScreenListTeacher screenListTeacher, List<String> strings) {
        this.screenListTeacher = screenListTeacher;
        this.context = screenListTeacher.getActivity();
        this.strings = strings;
        this.iScreenListTeacher = screenListTeacher.iScreenListTeacher;
    }

    @Override
    public RecylerViewScreenListTeacherAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_LINE)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_line, parent, false); //Inflating the layout
        else if (viewType == TYPE_TITLE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_avata, parent, false); //Inflating the layout
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
            final String title = strings.get(position);
            if (holder.viewType == TYPE_TITLE) {
                //Define and set data
                TextView row_title = (TextView) view.findViewById(R.id.row_title);
//                Typeface roboto = Typeface.createFromAsset(context.getAssets(),
//                        "font/Roboto-Regular.ttf"); //use this.getAssets if you are calling from an Activity
//                row_title.setTypeface(roboto);
                CircleImageView row_icon = (CircleImageView) view.findViewById(R.id.row_icon);
                if (row_title != null) {
                    row_title.setText(title);
                }
                if (row_icon != null) {
                   // row_icon.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_account_box_black_24dp));
                }

                //Handler on click item
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
                        screenListTeacher.setSeletedTearcher(title);
                        iScreenListTeacher.gotoScreenTeacherDetailsformScreenListTeacher(title);

                    }
                });
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    @Override
    public int getItemViewType(int position) {
        String item = strings.get(position);
        if (item.equals(context.getString(R.string.row_sub_header)))
            return TYPE_SUB_HEADER;
        else if (!item.equals(context.getString(R.string.row_line)))
            return TYPE_TITLE;
        else
            return TYPE_LINE;

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
}
