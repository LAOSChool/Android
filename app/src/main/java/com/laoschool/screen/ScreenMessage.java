package com.laoschool.screen;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.laoschool.R;
import com.laoschool.adapter.ListMessageAdapter;
import com.laoschool.entities.Message;
import com.laoschool.listener.OnLoadMoreListener;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.model.sqlite.CRUDMessage;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenMessage extends Fragment implements FragmentLifecycle {

    private static final String TAG = "ScreenMessage";
    private int containerId;
    private static DataAccessInterface service;
    public static ScreenMessage thiz;

    public static List<Message> messageList;
    static Context context;
    Message message;
    ScreenMessage screenMessage;
    static CRUDMessage crudMessage;
    ViewPager pager;
    MyPagerAdapter myPagerAdapter;
    boolean refeshListMessage;

    FloatingActionButton btnCreateMessage;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public void onPauseFragment() {
        Log.d(TAG, "onPauseFragment");
    }

    @Override
    public void onResumeFragment() {
        Log.d(TAG, "onResumeFragment");
        if (isRefeshListMessage()) {
            myPagerAdapter = new MyPagerAdapter(getFragmentManager());
            pager.setAdapter(myPagerAdapter);
        } else {
            Log.d(TAG, "No reload list message!!!");
        }

    }

    public void setRefeshListMessage(boolean refeshListMessage) {
        this.refeshListMessage = refeshListMessage;
    }

    public boolean isRefeshListMessage() {
        return refeshListMessage;
    }

    public interface IScreenMessage {
        void _gotoScreenCreateMessage();

        void _gotoMessageDetails(Message message);

        void reLogin();
    }

    public IScreenMessage iScreenMessage;


    public ScreenMessage() {

    }

    public static ScreenMessage instantiate(int containerId, String currentRole) {
        ScreenMessage screenMessage = new ScreenMessage();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        screenMessage.setArguments(args);
        return screenMessage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return _defineScreenMessage(inflater, container);
    }

    private View _defineScreenMessage(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.screen_message, container, false);
        pager = (ViewPager) view.findViewById(R.id.messageViewPage);
        btnCreateMessage = (FloatingActionButton) view.findViewById(R.id.btnCreateMessage);

        myPagerAdapter = new MyPagerAdapter(getFragmentManager());
        pager.setAdapter(myPagerAdapter);

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        _handlerCreateMessage();


        return view;
    }

    private void _handlerCreateMessage() {
        btnCreateMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iScreenMessage._gotoScreenCreateMessage();
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            Log.d(getString(R.string.title_screen_message), "-Container Id:" + containerId);
        }
        service = DataAccessImpl.getInstance(getActivity());
        this.thiz = this;
        this.context = getActivity();
        crudMessage = new CRUDMessage(context);
        if (LaoSchoolShared.myProfile==null){

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_screen_message, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.action_create_message:
//                iScreenMessage._gotoScreenCreateMessage();
//                Toast.makeText(getActivity(), "create message", Toast.LENGTH_SHORT).show();
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iScreenMessage = (IScreenMessage) activity;
        if (LaoSchoolShared.myProfile==null){
            iScreenMessage.reLogin();
        }
    }


    public static class MessageListFragment extends Fragment {
        private static String ARG_POSITION = "position";
        private int position;
        private RecyclerView mRecyclerListMessage;
        private Context context;
        SwipeRefreshLayout mSwipeRefreshLayout;

        public static MessageListFragment newInstance(int page) {
            Bundle args = new Bundle();
            args.putInt(ARG_POSITION, page);
            MessageListFragment fragment = new MessageListFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            position = getArguments().getInt(ARG_POSITION);
            this.context = getActivity();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.view_message_list, container, false);
            mRecyclerListMessage = (RecyclerView) view.findViewById(R.id.mRecyclerListMessage);
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            //set adapter
            mRecyclerListMessage.setLayoutManager(linearLayoutManager);

            _defineListMessage();

            //
            _handlerSwipeReload();


            return view;
        }

        private void _handlerSwipeReload() {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    _getListMessageFormServer();
                    // Refresh items
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }

        private void _defineListMessage() {
            //Load message in local
            int countLocal = crudMessage.getMessagesCount();
            Log.d(TAG, "MessageListFragment:count message in Local=" + countLocal);

            if (countLocal > 0) {
                _getListMessageFormLocalData();
            } else {
                _getListMessageFormServer();
            }
        }

        private void _getListMessageFormServer() {
            Log.d(TAG, "MessageListFragment:_getListMessageFormServer() position=" + position);
            int form_id = -1;
            if (position == 0) {
                //get max from id
                if (mRecyclerListMessage.getAdapter() != null) {
                    ListMessageAdapter listMessageAdapter = (ListMessageAdapter) mRecyclerListMessage.getAdapter();
                    form_id = listMessageAdapter.getMessageList().get(0).getId();
                }
            }
            final String classID = "";
            final String fromUserID = ((position == 1) ? String.valueOf(LaoSchoolShared.myProfile.getId()) : "");
            final String fromDate = "";
            final String toUserID = ((position == 0) ? String.valueOf(LaoSchoolShared.myProfile.getId()) : "");
            final String toDate = "";
            final String channel = "";
            final String status = "";
            final String fromID = ((position == 0) && (form_id > -1) ? String.valueOf(form_id) : "");
            service.getMessages(
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
                        public void onSuccess(List<Message> result) {
                            try {
                                int sizeResults = result.size();
                                Log.d(TAG, "MessageListFragment:setOnRefreshListener():\n" +
                                        "getMessages(classID=" + classID + "\n" +
                                        ",fromUserID=" + fromUserID + ",fromDate=" + fromDate + "\n" +
                                        ",toUserID=" + toUserID + ",toDate=" + toDate + "\n" +
                                        ",channel=" + channel + ",status=" + status + "\n" +
                                        ",fromID=" + fromID + ")/onSuccess():Results size=" + sizeResults);
                                for (Message message : result) {
                                    crudMessage.addOrUpdateMessage(message);
                                }
                                _getListMessageFormLocalData();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(String message) {
                            Log.e(TAG, "MessageListFragment:setOnRefreshListener():\n" +
                                    "getMessages(classID=" + classID + "\n" +
                                    ",fromUserID=" + fromUserID + ",fromDate=" + fromDate + "\n" +
                                    ",toUserID=" + toUserID + ",toDate=" + toDate + "\n" +
                                    ",channel=" + channel + ",status=" + status + "\n" +
                                    ",fromID=" + fromID + ")/onFailure():" + message);
                        }
                    });

        }

        private void _getListMessageFormLocalData() {
            List<Message> messagesForUser = new ArrayList<>();
            if (LaoSchoolShared.myProfile != null) {
                messagesForUser = crudMessage.getListMessagesForUser((position == 0) ? Message.MessageColumns.COLUMN_NAME_TO_USR_ID : Message.MessageColumns.COLUMN_NAME_FROM_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0);
                Log.d(TAG, "MessageListFragment:getListMessagesForUser size=" + messagesForUser.size());
            }
            _setListMessage(messagesForUser, position);

        }

        private void _setListMessage(final List<Message> messages, final int position) {
            try {
                final ListMessageAdapter listMessageAdapter = new ListMessageAdapter(mRecyclerListMessage, thiz, messages);
                listMessageAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        int countMessageFormLocal = crudMessage.getMessagesCountFormUser((position == 0) ? Message.MessageColumns.COLUMN_NAME_TO_USR_ID : Message.MessageColumns.COLUMN_NAME_FROM_USR_ID, LaoSchoolShared.myProfile.getId());
                        if (messages.size() < countMessageFormLocal) {
                            Log.d(TAG, "Load More");
                            messages.add(null);
                            listMessageAdapter.notifyItemInserted(messages.size() - 1);
                            _loadMoreData(messages, listMessageAdapter, position);
                        } else {
                            Log.d(TAG, "No message load !!!");
                        }

                    }
                });
                mRecyclerListMessage.setAdapter(listMessageAdapter);

            } catch (Exception e) {
                Log.e(TAG, "MessageListFragment:_setListMessage():" + e.getMessage());
            }

        }

        private void _loadMoreData(final List<Message> messages, final ListMessageAdapter listMessageAdapter, final int position) {
            //Load more data for reyclerview
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("haint", "Load More 2");

                    //Remove loading item
                    messages.remove(messages.size() - 1);
                    listMessageAdapter.notifyItemRemoved(messages.size());

                    //Load data

                    List<Message> messagesForUser = new ArrayList<>();
                    if (LaoSchoolShared.myProfile != null) {
                        messagesForUser = crudMessage.getListMessagesForUser((position == 0) ? Message.MessageColumns.COLUMN_NAME_TO_USR_ID : Message.MessageColumns.COLUMN_NAME_FROM_USR_ID, LaoSchoolShared.myProfile.getId(), 30, messages.size());
                        Log.d(TAG, "MessageListFragment:getListMessagesForUser size=" + messages.size());
                    }
                    messages.addAll(messagesForUser);


                    listMessageAdapter.notifyDataSetChanged();
                    listMessageAdapter.setLoaded();
                }
            }, 2000);
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return (position == 0) ? "Inbox" : "Send";
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return MessageListFragment.newInstance(0);
            } else {
                return MessageListFragment.newInstance(1);
            }

        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

}
