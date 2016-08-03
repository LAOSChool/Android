package com.laoschool.screen.pager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.adapter.ListMessageAdapter;
import com.laoschool.adapter.MessagesPagerAdapter;
import com.laoschool.entities.Message;
import com.laoschool.listener.OnLoadMoreListener;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.sqlite.DataAccessMessage;
import com.laoschool.screen.ScreenMessage;
import com.laoschool.shared.LaoSchoolShared;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class MessagesPager extends Fragment {
    private static final String ARG_LIST = "list";
    private static final String TAG = MessagesPager.class.getSimpleName();
    private static String ARG_POSITION = "position";
    private int position;
    private RecyclerView mRecyclerListMessage;
    private ScreenMessage screenMessage;
    private Context context;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView lbNoMessage;
    private List<Message> messages;
    private ListMessageAdapter listMessageAdapter;

    public MessagesPager() {
    }

    public List<Message> getMessages() {
        return messages;
    }

    public MessagesPager(ScreenMessage screenMessage, int position, List<Message> messages) {
        this.screenMessage = screenMessage;
        this.position = position;
        this.messages = messages;
        this.context = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
            messages = getArguments().getParcelableArrayList(ARG_LIST);
        }
        this.context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_message_list, container, false);
        lbNoMessage = (TextView) view.findViewById(R.id.lbNoMessage);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mRecyclerListMessage = (RecyclerView) view.findViewById(R.id.mRecyclerListMessage);
        mRecyclerListMessage.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        //set adapter
        mRecyclerListMessage.setLayoutManager(linearLayoutManager);
        if (messages != null) {
            if (messages.size() > 0) {
                lbNoMessage.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                if (screenMessage != null) {
                    setListMessage(messages, position, false);
                    _handlerSwipeReload();
                }

            } else {
                lbNoMessage.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
        } else {
            lbNoMessage.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
        }


        return view;
    }

    private void _handlerSwipeReload() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMessagesFormServer(position);
                // Refresh items
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getMessagesFormServer(final int position) {
        Log.d(TAG, "getMessagesFormServer() -position=" + position);
        int form_id = DataAccessMessage.getMaxMessagesID(LaoSchoolShared.myProfile.getId());

        final String classID = "";
        final String fromUserID = ((position == 2) ? String.valueOf(LaoSchoolShared.myProfile.getId()) : "");
        final String fromDate = "";
        final String toUserID = ((position == 0 || position == 1) ? String.valueOf(LaoSchoolShared.myProfile.getId()) : "");
        final String toDate = "";
        final String channel = "";
        final String status = "";
        final String fromID = ((form_id > 0) ? String.valueOf(form_id) : "");
        Log.d(TAG, "getMessagesFormServer(classID=" + classID + "\n" +
                ",fromUserID=" + fromUserID + ",fromDate=" + fromDate + "\n" +
                ",toUserID=" + toUserID + ",toDate=" + toDate + "\n" +
                ",channel=" + channel + ",status=" + status + "\n" +
                ",fromID=" + fromID + ")");
        LaoSchoolSingleton.getInstance().getDataAccessService().getMessages(
                classID//classID
                , fromUserID//from user ID
                , fromDate//from date
                , toDate//to date
                , toUserID//to user ID
                , channel//channel
                , status//status
                , fromID//from id
                , new AsyncCallback<List<Message>>() {
                    @Override
                    public void onSuccess(final List<Message> result) {
                        try {
                            int sizeResults = result.size();
                            if (sizeResults > 0) {
                                Log.d(TAG, "getMessagesFormServer() onSuccess() -results size=" + sizeResults);
                                for (Message message : result) {
                                    DataAccessMessage.addOrUpdateMessage(message);
                                }
                                swapData();
                            } else {
                                Log.d(TAG, "getMessagesFormServer() onSuccess() -Nothing to change");
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "getMessagesFormServer() onSuccess() -exception=" + e.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(String message) {
                        Log.e(TAG, "getMessagesFormServer(classID=" + classID + "\n" +
                                ",fromUserID=" + fromUserID + ",fromDate=" + fromDate + "\n" +
                                ",toUserID=" + toUserID + ",toDate=" + toDate + "\n" +
                                ",channel=" + channel + ",status=" + status + "\n" +
                                ",fromID=" + fromID + ")/onFailure():" + message);
                    }

                    @Override
                    public void onAuthFail(String message) {
                        LaoSchoolShared.goBackToLoginPage(context);
                    }
                });
    }

    private void swapData() {
        List<Message> messagesForUser = new ArrayList<>();
        if (LaoSchoolShared.myProfile != null) {
            if (position == 0) {
                messagesForUser = DataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 1);
            } else if (position == 1) {
                messagesForUser = DataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 0);
            } else if (position == 2) {
                messagesForUser = DataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_FROM_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 1);
            }
            Log.d(TAG, "swapData() -size:" + messagesForUser.size());
        }
        setListMessage(messagesForUser, position, true);
    }

    public void setListMessage(final List<Message> messages, final int position, boolean swapData) {
        try {
            if (messages.size() > 0) {
                lbNoMessage.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            } else {
                lbNoMessage.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
            listMessageAdapter = new ListMessageAdapter(mRecyclerListMessage, screenMessage, messages, position);
            handlerOnLoadMore(position, messages);

            if (!swapData)
                mRecyclerListMessage.setAdapter(listMessageAdapter);
            else
                mRecyclerListMessage.swapAdapter(listMessageAdapter, true);

        } catch (Exception e) {
            Log.e(TAG, "setListMessage() -exception:" + e.getMessage());
        }
    }

    private void loadMoreData(final List<Message> messages, final ListMessageAdapter listMessageAdapter, final int position) {
        //Load more data for reyclerview
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "loadMoreData()");

                //Remove loading item
                messages.remove(messages.size() - 1);
                listMessageAdapter.notifyItemRemoved(messages.size());

                //Load data

                List<Message> messagesForUser = new ArrayList<>();
                if (LaoSchoolShared.myProfile != null) {
                    switch (position) {
                        case 0:
                            messagesForUser = DataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), messages.size() + 30, messages.size(), 1);
                            break;
                        case 1:
                            messagesForUser = DataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), messages.size() + 30, messages.size(), 0);
                            break;
                        case 2:
                            messagesForUser = DataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_FROM_USR_ID, LaoSchoolShared.myProfile.getId(), messages.size() + 30, messages.size(), 1);
                            break;
                    }
                    Log.d(TAG, "loadMoreData() -size=" + messages.size());
                }
                messages.addAll(messagesForUser);

                listMessageAdapter.notifyDataSetChanged();
                listMessageAdapter.setLoaded();
            }
        }, 2000);
    }

    public void reloadData(int position, final List<Message> messages) {
        if (listMessageAdapter != null) {
            Log.d(TAG, "adpter not null");
            listMessageAdapter.swapData(messages);
        } else {
            Log.d(TAG, "adpter  null");
            listMessageAdapter = new ListMessageAdapter(mRecyclerListMessage, screenMessage, messages, position);
            mRecyclerListMessage.setAdapter(listMessageAdapter);

            handlerOnLoadMore(position, messages);

        }
    }

    private void handlerOnLoadMore(final int position, final List<Message> messages) {
        listMessageAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                                                     @Override
                                                     public void onLoadMore() {
                                                         int countMessageFormLocal = 0;
                                                         if (position == 0) {
                                                             countMessageFormLocal = DataAccessMessage.getMessagesCountFormUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId());
                                                         } else if (position == 1) {
                                                             countMessageFormLocal = DataAccessMessage.getMessagesCountFormUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), 0);
                                                         } else if (position == 2) {
                                                             countMessageFormLocal = DataAccessMessage.getMessagesCountFormUser(Message.MessageColumns.COLUMN_NAME_FROM_USR_ID, LaoSchoolShared.myProfile.getId());
                                                         }
                                                         if (messages != null) {
                                                             if (messages.size() < countMessageFormLocal) {
                                                                 Log.d(TAG, "onLoadMore()");
                                                                 messages.add(null);
                                                                 listMessageAdapter.notifyItemInserted(messages.size() - 1);
                                                                 loadMoreData(messages, listMessageAdapter, position);
                                                             } else {
                                                                 Log.d(TAG, "onLoadMore() -message: no message load !!!");
                                                             }
                                                         } else {
                                                             Log.d(TAG, "onLoadMore() -message: null");
                                                         }


                                                     }
                                                 }

        );
    }

}
