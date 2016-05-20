package com.laoschool.adapter;

import android.content.Context;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification, parent, false);
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
                final ImageView imgStar = (ImageView) view.findViewById(R.id.imgStar);

                view.setBackgroundColor((message.getIs_read() == 0) ?
                        context.getResources().getColor(R.color.color_bg_un_read) :
                        context.getResources().getColor(R.color.color_bg_read));

                if (LaoSchoolShared.myProfile != null) {
                    if (LaoSchoolShared.myProfile.getId() == message.getFrom_usr_id()) {
                        txbSender.setText("me");
                    } else {
                        txbSender.setText(message.getFrom_user_name());
                    }
                    if (message.getIs_read() == 0) {
                        txbSender.setTextColor(context.getResources().getColor(R.color.colorUnread));
                        txbTitle.setTextColor(context.getResources().getColor(R.color.colorTitleUnread));
                        txtDateSend.setTextColor(context.getResources().getColor(R.color.colorDateUnread));
                        txbSender.setTypeface(null, Typeface.BOLD);
                        txbTitle.setTypeface(null, Typeface.BOLD);
                        txtDateSend.setTypeface(null, Typeface.BOLD);
                        imgFlagMessage.setColorFilter(context.getResources().getColor(R.color.colorUnread));
                        view.setBackgroundColor(context.getResources().getColor(R.color.color_bg_un_read));
                    } else {
                        txbSender.setTextColor(context.getResources().getColor(R.color.colorRead));
                        txbTitle.setTextColor(context.getResources().getColor(R.color.colorRead));
                        txtDateSend.setTextColor(context.getResources().getColor(R.color.colorRead));
                        txbSender.setTypeface(null, Typeface.NORMAL);
                        txbTitle.setTypeface(null, Typeface.NORMAL);
                        txtDateSend.setTypeface(null, Typeface.NORMAL);
                        imgFlagMessage.setColorFilter(context.getResources().getColor(R.color.colorRead));
                        view.setBackgroundColor(context.getResources().getColor(R.color.color_bg_read));
                    }
                    if (message.getImp_flg() == 0) {
                        imgStar.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_star_border_white_24dp));
                        imgStar.setColorFilter(context.getResources().getColor(R.color.colorPriorityLow));
                    } else {
                        imgStar.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_star_white_24dp));
                        imgStar.setColorFilter(context.getResources().getColor(R.color.colorPriorityHigh));
                    }
                    imgUserAvata.setDefaultImageResId(R.drawable.ic_account_circle_black_36dp);
                    txbContent.setText(message.getContent());
                    txbTitle.setText(message.getTitle());
                    txtDateSend.setText(LaoSchoolShared.formatDate(message.getSent_dt(), 0));

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
