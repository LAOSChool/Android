package com.laoschool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laoschool.R;
import com.laoschool.entities.Message;
import com.laoschool.shared.LaoSchoolShared;

import java.util.List;

/**
 * Created by Hue on 4/12/2016.
 */
public class RecyclerViewConversionMessageAdapter extends RecyclerView.Adapter<RecyclerViewConversionMessageAdapter.RecyclerViewConversionMessageViewHolder> {

    private static final String TAG = "ConverMessageAdapter";
    private int TYPE_CONVERSION_TO = 1;
    private int TYPE_CONVERSION_FROM = 2;
    private Context context;

    private List<Message> messages;

    public RecyclerViewConversionMessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public RecyclerViewConversionMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_CONVERSION_TO) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_conversion_to, parent, false); //Inflating the layout
        } else if (viewType == TYPE_CONVERSION_FROM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_conversion_form, parent, false); //Inflating the layout
        }
        RecyclerViewConversionMessageViewHolder recyclerViewConversionMessageViewHolder = new RecyclerViewConversionMessageViewHolder(view, viewType);
        return recyclerViewConversionMessageViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewConversionMessageViewHolder holder, int position) {
        try {

            int viewType = holder.viewType;
            View view = holder.view;
            Message message = messages.get(position);
            if (viewType == TYPE_CONVERSION_TO) {
                ((TextView) (view.findViewById(R.id.txtConversionMessageTo))).setText(message.getContent());
                if (message.getSent_dt() != null)
                    ((TextView) (view.findViewById(R.id.txtToSendDate))).setText(message.getSent_dt());
                if (position > 1) {
                    Message message1 = messages.get(position - 1);
                    if (message.getFrom_usr_id()==message1.getFrom_usr_id()){
                        ((TextView) (view.findViewById(R.id.txtConversionMessageTo))).setBackgroundResource(R.drawable.ic_radius_box);
                    }else {
                        ((TextView) (view.findViewById(R.id.txtConversionMessageTo))).setBackgroundResource(R.drawable.ic_chat_bubble_blue_lao);
                    }
                }



            } else if (viewType == TYPE_CONVERSION_FROM) {
                ((TextView) (view.findViewById(R.id.txtConversionMessageForm))).setText(message.getContent());
                if (message.getSent_dt() != null)
                    ((TextView) (view.findViewById(R.id.txtFomSentDate))).setText(message.getSent_dt());

                if (position > 1) {
                    Message message1 = messages.get(position - 1);
                    if (message.getFrom_usr_id()==message1.getFrom_usr_id()){
                        ((TextView) (view.findViewById(R.id.txtConversionMessageForm))).setBackgroundResource(R.drawable.ic_radius_box_red_lao);
                    }else {
                        ((TextView) (view.findViewById(R.id.txtConversionMessageForm))).setBackgroundResource(R.drawable.ic_chat_bubble_red_lao_48dp_2x);
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "/onBindViewHolder() error:" + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (LaoSchoolShared.myProfile != null) {
            if (message.getFrom_usr_id() == LaoSchoolShared.myProfile.getId()) {
                return TYPE_CONVERSION_FROM;
            } else
                return TYPE_CONVERSION_TO;
        } else {
            return TYPE_CONVERSION_FROM;
        }

    }

    public class RecyclerViewConversionMessageViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public RecyclerViewConversionMessageViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }
}
