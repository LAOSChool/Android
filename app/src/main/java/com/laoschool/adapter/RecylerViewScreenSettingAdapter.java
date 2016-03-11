package com.laoschool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.screen.ScreenSetting;

import java.util.List;

/**
 * Created by Hue on 3/7/2016.
 */
public class RecylerViewScreenSettingAdapter extends RecyclerView.Adapter<RecylerViewScreenSettingAdapter.RecylerViewScreenSettingAdapterViewHolder> {

    private ScreenSetting screenSetting;
    private Context context;
    private List<String> strings;
    private int TYPE_TITLE = 0;
    private int TYPE_LINE = 1;

    public RecylerViewScreenSettingAdapter(ScreenSetting screenSetting, List<String> strings) {
        this.screenSetting = screenSetting;
        this.context = screenSetting.getActivity();
        this.strings = strings;
    }

    @Override
    public RecylerViewScreenSettingAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_LINE)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_line, parent, false); //Inflating the layout
        else if (viewType == TYPE_TITLE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_only_title, parent, false); //Inflating the layout
        }
        RecylerViewScreenSettingAdapterViewHolder viewHolder = new RecylerViewScreenSettingAdapterViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecylerViewScreenSettingAdapterViewHolder holder, int position) {
        View view = holder.view;
        try {
            final String title = strings.get(position);
            if (holder.viewType == TYPE_TITLE) {
                //Define and set data
                TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
                if (txtTitle != null) {
                    txtTitle.setText(title);
                }

                //Handler on click item
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, title, Toast.LENGTH_SHORT).show();

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

    public class RecylerViewScreenSettingAdapterViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public RecylerViewScreenSettingAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }
}
