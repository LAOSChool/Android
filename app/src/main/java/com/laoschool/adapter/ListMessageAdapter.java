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
    private DataAccessMessage dataAccessMessage;

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public List<Message> getMessageList() {
        return messageList;
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
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_message, parent, false); //Inflating the layout
//        ListMessageAdapterViewHolder recyclerViewListMessageAdapterViewHolder = new ListMessageAdapterViewHolder(view, viewType);
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
                final ImageView imgFlagMessage = (ImageView) view.findViewById(R.id.imgFlagMessage);


                if (LaoSchoolShared.myProfile != null) {

                    if (LaoSchoolShared.myProfile.getId() == message.getFrom_usr_id()) {
                        txbSender.setText("from: me");
                        txbSender.setTextColor(
                                context.getResources().getColor(R.color.colorDefault));
                        txbTitle.setTextColor(
                                context.getResources().getColor(R.color.color_messsage_read));
                        txtDateSend.setTextColor(
                                context.getResources().getColor(R.color.colorDefault));
                        txbSender.setTypeface(null, Typeface.NORMAL);
                        txbTitle.setTypeface(null, Typeface.NORMAL);
//                        imgUserAvata.setColorFilter(context.getResources().getColor(R.color.color_messsage_read));
                        view.setBackgroundColor(
                                context.getResources().getColor(R.color.color_bg_read));
                    } else {
                        txbSender.setText("from: " + message.getFrom_user_name());

                        txbSender.setTextColor((message.getIs_read() == 0) ?
                                context.getResources().getColor(R.color.colorDefault) :
                                context.getResources().getColor(R.color.colorDefault));
                        txbTitle.setTextColor((message.getIs_read() == 0) ?
                                context.getResources().getColor(R.color.color_messsage_tilte_not_read) :
                                context.getResources().getColor(R.color.color_messsage_read));
                        txtDateSend.setTextColor((message.getIs_read() == 0) ?
                                context.getResources().getColor(R.color.colorDefault) :
                                context.getResources().getColor(R.color.colorDefault));

                        txbSender.setTypeface(null, (message.getIs_read() == 0) ? Typeface.NORMAL : Typeface.NORMAL);
                        txbTitle.setTypeface(null, (message.getIs_read() == 0) ? Typeface.NORMAL : Typeface.NORMAL);
//                        imgUserAvata.setColorFilter((message.getIs_read() == 0) ?
//                                context.getResources().getColor(R.color.color_messsage_send_date_not_read) :
//                                context.getResources().getColor(R.color.color_messsage_read));
                        view.setBackgroundColor((message.getIs_read() == 0) ?
                                context.getResources().getColor(R.color.color_bg_un_read) :
                                context.getResources().getColor(R.color.color_bg_read));
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
                    imgFlagMessage.setColorFilter(screenMessage.getActivity().getResources().getColor(R.color.colorDefault));
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        screenMessage.setMessage(message);
                        screenMessage.iScreenMessage._gotoMessageDetails(message);
                        txbTitle.setTextColor(context.getResources().getColor(R.color.color_messsage_read));
                        txbSender.setTextColor(context.getResources().getColor(R.color.colorDefault));
                        txtDateSend.setTextColor(context.getResources().getColor(R.color.colorDefault));
                        imgFlagMessage.setColorFilter(screenMessage.getActivity().getResources().getColor(R.color.colorDefault));

                        if (message.getIs_read() == 0) {
                            //Update is read
                            message.setIs_read(1);
                            dataAccessMessage.updateMessage(message);
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
        LaoSchoolSingleton.getInstance().getDataAccessService().updateMessage(message, new AsyncCallback<Message>() {
            @Override
            public void onSuccess(Message result) {
                Log.d(TAG, "_updateStatusMessageToServer() onSuccess():" + result.getId());
            }

            @Override
            public void onFailure(String message) {
                Log.e(TAG, "_updateStatusMessageToServer() onFailure():" + message);
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
