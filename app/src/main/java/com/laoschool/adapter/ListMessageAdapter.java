package com.laoschool.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.Message;
import com.laoschool.listener.OnLoadMoreListener;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.sqlite.DataAccessMessage;
import com.laoschool.screen.ScreenMessage;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.tools.CustomNetworkImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Tran An on 08/04/2016.
 */
public class ListMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "Adp_ListMessage";
    private final int positionPage;
    List<Message> messageList;
    ScreenMessage screenMessage;
    private Context context;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private RecyclerView mRecyclerView;

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }


    public ListMessageAdapter(RecyclerView mRecyclerView, ScreenMessage screenMessage, List<Message> messageList, int positionPage) {
        this.messageList = messageList;
        this.screenMessage = screenMessage;
        this.context = screenMessage.getActivity();
        this.mRecyclerView = mRecyclerView;
        this.positionPage = positionPage;
        _handelerScrollList();

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


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_message, parent, false);
            return new ListMessageAdapterViewHolder(view, viewType);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            if (holder instanceof ListMessageAdapterViewHolder) {
                ListMessageAdapterViewHolder listHolder = (ListMessageAdapterViewHolder) holder;
                View view = listHolder.view;
                final Message message = messageList.get(position);
                final TextView txbTitle = ((TextView) view.findViewById(R.id.txbTitle));
                TextView txbContent = ((TextView) view.findViewById(R.id.txbContent));
                final TextView txbSender = ((TextView) view.findViewById(R.id.txbSender));
                final TextView txtDateSend = ((TextView) view.findViewById(R.id.txtDateSend));
                final NetworkImageView imgUserAvata = (NetworkImageView) view.findViewById(R.id.imgUserAvata);
                final ImageView imgGotoDetails = (ImageView) view.findViewById(R.id.imgFlagMessage);
                final ImageView imgStar = (ImageView) view.findViewById(R.id.imgStar);


                if (LaoSchoolShared.myProfile != null) {

                    if (LaoSchoolShared.myProfile.getId() == message.getFrom_usr_id()) {
                        txbSender.setText("from: me");
                        txbSender.setTextColor(context.getResources().getColor(R.color.colorRead));
                        txbTitle.setTextColor(context.getResources().getColor(R.color.colorRead));
                        txtDateSend.setTextColor(context.getResources().getColor(R.color.colorRead));
                        txbSender.setTypeface(null, Typeface.NORMAL);
                        txbTitle.setTypeface(null, Typeface.NORMAL);
                        txtDateSend.setTypeface(null, Typeface.NORMAL);
                        imgGotoDetails.setColorFilter(context.getResources().getColor(R.color.colorRead));
                        view.setBackgroundColor(context.getResources().getColor(R.color.color_bg_read));
                    } else {
                        txbSender.setText(message.getFrom_user_name());
                        if (message.getIs_read() == 0) {
                            txbSender.setTextColor(context.getResources().getColor(R.color.colorSenderUnread));
                            txbTitle.setTextColor(context.getResources().getColor(R.color.colorUnread));
                            txtDateSend.setTextColor(context.getResources().getColor(R.color.colorDateUnread));
                            txbSender.setTypeface(null, Typeface.BOLD);
                            txbTitle.setTypeface(null, Typeface.BOLD);
                            txtDateSend.setTypeface(null, Typeface.BOLD);
                            imgGotoDetails.setColorFilter(context.getResources().getColor(R.color.colorUnread));
                            view.setBackgroundColor(context.getResources().getColor(R.color.color_bg_un_read));
                        } else {
                            txbSender.setTextColor(context.getResources().getColor(R.color.colorRead));
                            txbTitle.setTextColor(context.getResources().getColor(R.color.colorRead));
                            txtDateSend.setTextColor(context.getResources().getColor(R.color.colorRead));
                            txbSender.setTypeface(null, Typeface.NORMAL);
                            txbTitle.setTypeface(null, Typeface.NORMAL);
                            txtDateSend.setTypeface(null, Typeface.NORMAL);
                            imgGotoDetails.setColorFilter(context.getResources().getColor(R.color.colorPriorityLow));
                            view.setBackgroundColor(context.getResources().getColor(R.color.color_bg_read));
                        }
                    }
                    if (message.getFrm_user_photo() != null) {
                        LaoSchoolSingleton.getInstance().getImageLoader().get(message.getFrm_user_photo(), ImageLoader.getImageListener(imgUserAvata,
                                R.drawable.ic_account_circle_black_36dp, android.R.drawable
                                        .ic_dialog_alert));
                        imgUserAvata.setImageUrl(message.getFrm_user_photo(), LaoSchoolSingleton.getInstance().getImageLoader());
                    } else {
                        imgUserAvata.setDefaultImageResId(R.drawable.ic_account_circle_black_36dp);
                    }
//
                    txbContent.setText(message.getContent());
                    txbTitle.setText(message.getTitle());
                    txtDateSend.setText(LaoSchoolShared.formatDate(message.getSent_dt(), 0));

                    if (message.getImp_flg() == 0) {
                        imgStar.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_star_border_white_24dp));
                        imgStar.setColorFilter(screenMessage.getActivity().getResources().getColor(R.color.colorPriorityLow));
                    } else {
                        imgStar.setImageDrawable(LaoSchoolShared.getDraweble(context, R.drawable.ic_star_white_24dp));
                        imgStar.setColorFilter(screenMessage.getActivity().getResources().getColor(R.color.colorPriorityHigh));
                    }
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        screenMessage.setMessage(message);
                        screenMessage.iScreenMessage._gotoMessageDetails(message);
                        txbTitle.setTextColor(context.getResources().getColor(R.color.colorRead));
                        txbSender.setTextColor(context.getResources().getColor(R.color.colorRead));
                        txtDateSend.setTextColor(context.getResources().getColor(R.color.colorRead));
                        imgGotoDetails.setColorFilter(screenMessage.getActivity().getResources().getColor(R.color.colorRead));

                        if (message.getIs_read() == 0) {
                            //Update is read
                            message.setIs_read(1);
                            DataAccessMessage.updateMessage(message);
                            _updateStatusMessageToServer(message);
                        }
                        Log.d(TAG, "Page:" + positionPage);
                        if (positionPage == 1) {
                            messageList.remove(position);
                        }
                        notifyDataSetChanged();


                    }
                });
            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void _updateStatusMessageToServer(Message message) {
        LaoSchoolSingleton.getInstance().getDataAccessService().updateMessageIsRead(message, new AsyncCallback<Message>() {
            @Override
            public void onSuccess(Message result) {
                Log.d(TAG, "_updateStatusMessageToServer() onSuccess():" + result.getId());
            }

            @Override
            public void onFailure(String message) {
                Log.e(TAG, "_updateStatusMessageToServer() onFailure():" + message);
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

    public class ListMessageAdapterViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public ListMessageAdapterViewHolder(View itemView, int viewType) {
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
}
