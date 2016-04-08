package com.laoschool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.entities.Message;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.screen.ScreenMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tran An on 08/04/2016.
 */
public class RecyclerViewListMessageAdapter extends RecyclerView.Adapter<RecyclerViewListMessageAdapter.RecyclerViewListMessageAdapterViewHolder>{
    List<Message> messageList;
    private static DataAccessInterface service;
    ScreenMessage screenMessage;


    public RecyclerViewListMessageAdapter(ScreenMessage screenMessage,List<Message> messageList){
        this.messageList=messageList;
        this.screenMessage=screenMessage;
    }



    @Override
    public RecyclerViewListMessageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_line_message, parent, false); //Inflating the layout
        RecyclerViewListMessageAdapterViewHolder recyclerViewListMessageAdapterViewHolder = new RecyclerViewListMessageAdapterViewHolder(view, viewType);
        return recyclerViewListMessageAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewListMessageAdapterViewHolder holder, int position) {
        View view = holder.view;
        try {
           final Message message = messageList.get(position);
            ((TextView) view.findViewById(R.id.txbTitle)).setText(message.getTitle());
            ((TextView) view.findViewById(R.id.txbContent)).setText(message.getContent());
            ((TextView) view.findViewById(R.id.txbSender)).setText(message.getFrom_user_name());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    screenMessage.setMessage(message);
                    screenMessage.iScreenMessage._gotoMessageDetails("");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class RecyclerViewListMessageAdapterViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public RecyclerViewListMessageAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }
}
