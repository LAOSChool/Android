package com.laoschool.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.Message;
import com.laoschool.listener.OnLoadMoreListener;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.sqlite.DataAccessMessage;
import com.laoschool.screen.ScreenAnnouncements;
import com.laoschool.shared.LaoSchoolShared;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Hue on 4/19/2016.
 */
public class ListNotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ListNotificationAdapter";
    private ScreenAnnouncements screenAnnouncements;
    private final RecyclerView mRecyclerView;
    private final Context context;
    private final int positionPage;
    private List<Message> messageList;
    private ScreenAnnouncements.IScreenAnnouncements iScreenAnnouncements;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }


    public ListNotificationAdapter(ScreenAnnouncements screenAnnouncements, RecyclerView mRecyclerView, int positionPage, List<Message> messageList) {
        this.screenAnnouncements = screenAnnouncements;
        this.mRecyclerView = mRecyclerView;
        this.context = screenAnnouncements.getActivity();
        this.messageList = messageList;
        this.iScreenAnnouncements = screenAnnouncements.iScreenAnnouncements;
        this.positionPage = positionPage;
        _handelerScrollList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_message, parent, false);
            return new ListNotificationAdapterViewHolder(view, viewType);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_loading, parent, false);
            return new LoadingViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            if (holder instanceof ListNotificationAdapterViewHolder) {
                ListNotificationAdapterViewHolder listHolder = (ListNotificationAdapterViewHolder) holder;
                View view = listHolder.view;
                final Message message = messageList.get(position);
                final TextView txbTitle = ((TextView) view.findViewById(R.id.txbTitle));
                TextView txbContent = ((TextView) view.findViewById(R.id.txbContent));
                final TextView txbSender = ((TextView) view.findViewById(R.id.txbSender));
                final TextView txtDateSend = ((TextView) view.findViewById(R.id.txtDateSend));
                final NetworkImageView imgUserAvata = (NetworkImageView) view.findViewById(R.id.imgUserAvata);
                final ImageView imgFlagMessage = (ImageView) view.findViewById(R.id.imgFlagMessage);

                view.setBackgroundColor((message.getIs_read() == 0) ?
                        context.getResources().getColor(R.color.color_bg_un_read) :
                        context.getResources().getColor(R.color.color_bg_read));

                if (LaoSchoolShared.myProfile != null) {
                    if (LaoSchoolShared.myProfile.getId() == message.getFrom_usr_id()) {
                        txbSender.setText("me");
                        txbSender.setTextColor(
                                context.getResources().getColor(R.color.color_messsage_sender_read));
                        txbTitle.setTextColor(
                                context.getResources().getColor(R.color.color_messsage_read));
                        txtDateSend.setTextColor(
                                context.getResources().getColor(R.color.color_messsage_read));
                        txbSender.setTypeface(null, Typeface.NORMAL);
                        txbTitle.setTypeface(null, Typeface.NORMAL);
                        imgUserAvata.setColorFilter(context.getResources().getColor(R.color.color_messsage_read));
                    } else {
                        txbSender.setText(message.getFrom_user_name());

                        txbSender.setTextColor((message.getIs_read() == 0) ?
                                context.getResources().getColor(android.R.color.black) :
                                context.getResources().getColor(R.color.color_messsage_sender_read));
                        txbTitle.setTextColor((message.getIs_read() == 0) ?
                                context.getResources().getColor(R.color.color_messsage_tilte_not_read) :
                                context.getResources().getColor(R.color.color_messsage_read));
                        txtDateSend.setTextColor((message.getIs_read() == 0) ?
                                context.getResources().getColor(R.color.color_messsage_send_date_not_read) :
                                context.getResources().getColor(R.color.color_messsage_read));

                        txbSender.setTypeface(null, (message.getIs_read() == 0) ? Typeface.BOLD : Typeface.NORMAL);
                        txbTitle.setTypeface(null, (message.getIs_read() == 0) ? Typeface.BOLD : Typeface.NORMAL);
                        imgUserAvata.setColorFilter((message.getIs_read() == 0) ?
                                context.getResources().getColor(R.color.color_messsage_send_date_not_read) :
                                context.getResources().getColor(R.color.color_messsage_read));
                    }
                    imgUserAvata.setDefaultImageResId(R.drawable.ic_account_circle_black_36dp);
                    //imgUserAvata.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_account_circle_black_36dp));
                    txbContent.setText(message.getContent());
                    txbTitle.setText(message.getTitle());
                    DateFormat outputFormatter1 = new SimpleDateFormat("dd-MMM");
                    DateFormat inputFormatter1 = new SimpleDateFormat("yyyy-MM-dd");

                    Date date1;
                    if (message.getSent_dt() != null) {
                        date1 = inputFormatter1.parse(message.getSent_dt());
                    } else {
                        date1 = new Date();
                    }
                    String output1 = outputFormatter1.format(date1);
                    txtDateSend.setText(output1);
                    imgFlagMessage.setColorFilter(context.getResources().getColor(R.color.color_messsage_read));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (message.getIs_read() == 0) {
                                //Update is read
                                message.setIs_read(1);
                                DataAccessMessage.updateMessage(message);
                                _updateIsReadNotification(message);
                            }
                            screenAnnouncements.setNotification(message);
                            screenAnnouncements.iScreenAnnouncements.gotoScreenAnnouncementDetails(message);
                            if (positionPage == 1) {
                                messageList.remove(position);
                            }
                            notifyDataSetChanged();
                        }
                    });
                }
            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void _updateIsReadNotification(Message message) {
        LaoSchoolSingleton.getInstance().getDataAccessService().updateNotification(message, new AsyncCallback<Message>() {
            @Override
            public void onSuccess(Message result) {
                Log.d(TAG, "_updateIsReadNotification() onSuccess():" + result.getId());
            }

            @Override
            public void onFailure(String message) {
                Log.e(TAG, "_updateIsReadNotification() onFailure():" + message);
            }

            @Override
            public void onAuthFail(String message) {
                LaoSchoolShared.goBackToLoginPage(context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoaded() {
        isLoading = false;
    }


    public class ListNotificationAdapterViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public ListNotificationAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

    private void _handelerScrollList() {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public List<Message> getNotificationList() {
        return messageList;
    }

    public void setNotificationList(List<Message> messageList) {
        this.messageList = messageList;
    }
}
