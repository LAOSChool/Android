package com.laoschool.screen;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.astuetz.PagerSlidingTabStrip;
import com.laoschool.R;
import com.laoschool.adapter.ListMessageAdapter;
import com.laoschool.entities.Message;
import com.laoschool.listener.OnLoadMoreListener;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.model.sqlite.DataAccessMessage;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;
import com.laoschool.view.ViewpagerDisableSwipeLeft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    static DataAccessMessage dataAccessMessage;
    static ViewpagerDisableSwipeLeft pager;
    static PagerSlidingTabStrip tabs;
    static MessagesPagerAdapter messagesPagerAdapter;
    boolean refeshListMessage = false;

    private static FragmentManager fr;
    private static LinearLayout mMessages;
    private static ProgressBar mProgress;

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
    }

    public void setRefeshListMessage(boolean refeshListMessage) {
        this.refeshListMessage = refeshListMessage;
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
        Log.d(TAG, "onCreateView()");
        return _defineScreenMessage(inflater, container);
    }

    private View _defineScreenMessage(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.screen_message, container, false);
        mMessages = (LinearLayout) view.findViewById(R.id.mMessages);
        mProgress = (ProgressBar) view.findViewById(R.id.mProgress);
        pager = (ViewpagerDisableSwipeLeft) view.findViewById(R.id.messageViewPage);
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);

        pager.setAllowedSwipeDirection(HomeActivity.SwipeDirection.none);

        _defineData();

        _handlerPageChange();

        return view;
    }

    private void _defineData() {
        int countLocal = dataAccessMessage.getMessagesCount();
        Log.d(TAG, "_defineData():count message in Local=" + countLocal);
        if (countLocal > 0) {
            _getDataFormLocal();
        } else {
            _getDataFormServer();
        }
    }

    private void _getDataFormServer() {
        _getDataFormServer(-1);
    }

    private static void _getDataFormServer(final int position) {
        _showProgessLoading(true);
        Log.d(TAG, "_getDataFormServer() position=" + position);
        int form_id = DataAccessMessage.getMaxMessagesID(LaoSchoolShared.myProfile.getId());

        final String classID = "";
        final String fromUserID = ((position == 2) ? String.valueOf(LaoSchoolShared.myProfile.getId()) : "");
        final String fromDate = "";
        final String toUserID = ((position == 0 || position == 1) ? String.valueOf(LaoSchoolShared.myProfile.getId()) : "");
        final String toDate = "";
        final String channel = "";
        final String status = "";
        final String fromID = ((form_id > 0) ? String.valueOf(form_id) : "");
        Log.d(TAG, "_getDataFormServer()):\n" +
                "getMessages(classID=" + classID + "\n" +
                ",fromUserID=" + fromUserID + ",fromDate=" + fromDate + "\n" +
                ",toUserID=" + toUserID + ",toDate=" + toDate + "\n" +
                ",channel=" + channel + ",status=" + status + "\n" +
                ",fromID=" + fromID + ")");
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
                            Log.d(TAG, "_getDataFormServer()):" +
                                    "getMessages()/onSuccess() Results size=" + sizeResults);
                            for (Message message : result) {
                                dataAccessMessage.addOrUpdateMessage(message);
                            }
                            _getDataFormLocal(position);
                            _showProgessLoading(false);
                        } catch (Exception e) {
                            Log.d(TAG, "_getDataFormServer() onSuccess Exception=" + e.getMessage());
                            _showProgessLoading(false);
                        }

                    }

                    @Override
                    public void onFailure(String message) {
                        Log.e(TAG, "NotificationList:setOnRefreshListener():\n" +
                                "getMessages(classID=" + classID + "\n" +
                                ",fromUserID=" + fromUserID + ",fromDate=" + fromDate + "\n" +
                                ",toUserID=" + toUserID + ",toDate=" + toDate + "\n" +
                                ",channel=" + channel + ",status=" + status + "\n" +
                                ",fromID=" + fromID + ")/onFailure():" + message);
                        _showProgessLoading(false);
                    }
                });
    }

    private static void _showProgessLoading(boolean b) {
        if (b) {
            mMessages.setVisibility(View.GONE);
            mProgress.setVisibility(View.VISIBLE);
        } else {
            mMessages.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.GONE);
        }
    }

    private static void _getDataFormLocal(int position) {
        List<Message> messagesForUserInbox = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 1);
        List<Message> messagesToUserUnread = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 0);
        List<Message> messagesFormUser = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_FROM_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 1);
        // Bind the tabs to the ViewPager
        messagesPagerAdapter = new MessagesPagerAdapter(fr, messagesForUserInbox, messagesToUserUnread, messagesFormUser);
        pager.setAdapter(messagesPagerAdapter);
        tabs.setViewPager(pager);
        if (position > -1)
            pager.setCurrentItem(position);
    }

    private static void _getDataFormLocal() {
        _getDataFormLocal(-1);
    }

    private void _handlerPageChange() {
        ViewPager.OnPageChangeListener onPageNotificationChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MessageListFragment notifragment = ((MessagesPagerAdapter) (pager.getAdapter())).getFragment(position);
                switch (position) {
                    case 0:
                        List<Message> messagesForUserInbox = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 1);
                        notifragment._setListMessage(messagesForUserInbox, 0);
                        break;
                    case 1:
                        List<Message> messagesForUserUnread = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 0);
                        notifragment._setListMessage(messagesForUserUnread, 1);
                        break;
                    case 2:
                        List<Message> messagesFormUser = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_FROM_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 1);
                        notifragment._setListMessage(messagesFormUser, 2);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        pager.addOnPageChangeListener(onPageNotificationChangeListener);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            Log.d(getString(R.string.title_screen_message), "-Container Id:" + containerId);
        }
        service = DataAccessImpl.getInstance(getActivity());
        this.thiz = this;
        this.context = getActivity();
        this.fr = getFragmentManager();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_screen_message, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_create_message:
                iScreenMessage._gotoScreenCreateMessage();
//                Toast.makeText(getActivity(), "create message", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach(Activity)");
        super.onAttach(activity);
        iScreenMessage = (IScreenMessage) activity;
        if (LaoSchoolShared.myProfile == null) {
            iScreenMessage.reLogin();
        }
    }


    @SuppressLint("ValidFragment")
    public static class MessageListFragment extends Fragment {
        private static String ARG_POSITION = "position";
        private int position;
        private RecyclerView mRecyclerListMessage;
        private Context context;
        SwipeRefreshLayout mSwipeRefreshLayout;
        private List<Message> messages;

        public MessageListFragment(int position, List<Message> messages) {
            this.position = position;
            this.messages = messages;
        }

        public MessageListFragment() {

        }

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
            // position = getArguments().getInt(ARG_POSITION);
            this.context = getActivity();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.view_message_list, container, false);
            mRecyclerListMessage = (RecyclerView) view.findViewById(R.id.mRecyclerListMessage);
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            //set adapter
            mRecyclerListMessage.setLayoutManager(linearLayoutManager);

            // _defineListMessage();
            _setListMessage(messages, position);
            //
            _handlerSwipeReload();


            return view;
        }

        private void _handlerSwipeReload() {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //_getListMessageFormServer();
                    _getDataFormServer(position);
                    // Refresh items
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }

//        private void _defineListMessage() {
//            //Load message in local
//            int countLocal = dataAccessMessage.getMessagesCount();
//            Log.d(TAG, "NotificationList:count message in Local=" + countLocal);
////
//            if (countLocal > 0) {
//                _getListMessageFormLocalData();
//            } else {
//                _getListMessageFormServer();
//            }
//        }

//        private void _getListMessageFormServer() {
//            Log.d(TAG, "NotificationList:_getListMessageFormServer() position=" + position);
//            int form_id = DataAccessMessage.getMaxMessagesID(LaoSchoolShared.myProfile.getId());
//
//            final String classID = "";
//            final String fromUserID = ((position == 2) ? String.valueOf(LaoSchoolShared.myProfile.getId()) : "");
//            final String fromDate = "";
//            final String toUserID = ((position == 0 || position == 1) ? String.valueOf(LaoSchoolShared.myProfile.getId()) : "");
//            final String toDate = "";
//            final String channel = "";
//            final String status = "";
//            final String fromID = ((form_id > 0) ? String.valueOf(form_id) : "");
//            service.getMessages(
//                    classID//classID
//                    , fromUserID//from user ID
//                    , fromDate//from date
//                    , toDate//to date
//                    , toUserID//to user ID
//                    , channel//channel
//                    , status//status
//                    , fromID//from id
//                    , new AsyncCallback<List<Message>>() {
//                        @Override
//                        public void onSuccess(List<Message> result) {
//                            try {
//                                int sizeResults = result.size();
//                                Log.d(TAG, "NotificationList:setOnRefreshListener():\n" +
//                                        "getMessages(classID=" + classID + "\n" +
//                                        ",fromUserID=" + fromUserID + ",fromDate=" + fromDate + "\n" +
//                                        ",toUserID=" + toUserID + ",toDate=" + toDate + "\n" +
//                                        ",channel=" + channel + ",status=" + status + "\n" +
//                                        ",fromID=" + fromID + ")/onSuccess() Results size=" + sizeResults);
//                                for (Message message : result) {
//                                    dataAccessMessage.addOrUpdateMessage(message);
//                                }
//                                _getListMessageFormLocalData();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(String message) {
//                            Log.e(TAG, "NotificationList:setOnRefreshListener():\n" +
//                                    "getMessages(classID=" + classID + "\n" +
//                                    ",fromUserID=" + fromUserID + ",fromDate=" + fromDate + "\n" +
//                                    ",toUserID=" + toUserID + ",toDate=" + toDate + "\n" +
//                                    ",channel=" + channel + ",status=" + status + "\n" +
//                                    ",fromID=" + fromID + ")/onFailure():" + message);
//                        }
//                    });
//
//        }
//
//        public void _getListMessageFormLocalData() {
//            Log.d(TAG, "NotificationList:_getListMessageFormLocalData() position=" + position);
//            List<Message> messagesForUser = new ArrayList<>();
//            if (LaoSchoolShared.myProfile != null) {
//                if (position == 0) {
//                    messagesForUser = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 1);
//                } else if (position == 1) {
//                    messagesForUser = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 0);
//                } else if (position == 2) {
//                    messagesForUser = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_FROM_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 1);
//                }
//                Log.d(TAG, "MessageListFragment:getListMessagesForUser size=" + messagesForUser.size());
//            }
//            _setListMessage(messagesForUser, position);
//        }

        private void _setListMessage(final List<Message> messages, final int position) {
            try {
                final ListMessageAdapter listMessageAdapter = new ListMessageAdapter(mRecyclerListMessage, thiz, messages, position);
                listMessageAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                                                             @Override
                                                             public void onLoadMore() {
                                                                 int countMessageFormLocal = 0;
                                                                 if (position == 0) {
                                                                     countMessageFormLocal = dataAccessMessage.getMessagesCountFormUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId());
                                                                 } else if (position == 1) {
                                                                     countMessageFormLocal = dataAccessMessage.getMessagesCountFormUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), 0);
                                                                 } else if (position == 2) {
                                                                     countMessageFormLocal = dataAccessMessage.getMessagesCountFormUser(Message.MessageColumns.COLUMN_NAME_FROM_USR_ID, LaoSchoolShared.myProfile.getId());
                                                                 }

                                                                 if (messages.size() < countMessageFormLocal) {
                                                                     Log.d(TAG, "Load More");
                                                                     messages.add(null);
                                                                     listMessageAdapter.notifyItemInserted(messages.size() - 1);
                                                                     _loadMoreData(messages, listMessageAdapter, position);
                                                                 } else {
                                                                     Log.d(TAG, "No message load !!!");
                                                                 }

                                                             }
                                                         }

                );
                mRecyclerListMessage.setAdapter(listMessageAdapter);

            } catch (Exception e) {
                Log.e(TAG, "NotificationList:_setListMessage():" + e.getMessage());
            }
        }

        private void _loadMoreData(final List<Message> messages, final ListMessageAdapter listMessageAdapter, final int position) {
            //Load more data for reyclerview
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "Load More 2");

                    //Remove loading item
                    messages.remove(messages.size() - 1);
                    listMessageAdapter.notifyItemRemoved(messages.size());

                    //Load data

                    List<Message> messagesForUser = new ArrayList<>();
                    if (LaoSchoolShared.myProfile != null) {
                        switch (position) {
                            case 0:
                                messagesForUser = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), messages.size() + 30, messages.size(), 1);
                                break;
                            case 1:
                                messagesForUser = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), messages.size() + 30, messages.size(), 0);
                                break;
                            case 2:
                                messagesForUser = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_FROM_USR_ID, LaoSchoolShared.myProfile.getId(), messages.size() + 30, messages.size(), 1);
                                break;
                        }
                        Log.d(TAG, "MessageListFragment:getListMessagesForUser size=" + messages.size());
                    }
                    messages.addAll(messagesForUser);

                    listMessageAdapter.notifyDataSetChanged();
                    listMessageAdapter.setLoaded();
                }
            }, 2000);
        }

    }

    public static class MessagesPagerAdapter extends FragmentPagerAdapter {
        private FragmentManager mFragmentManager;
        private Map<Integer, String> mFragmentTags;
        private List<Message> messagesForUserInbox;
        private List<Message> messagesToUserUnread;
        private List<Message> messagesFormUser;


        public MessagesPagerAdapter(FragmentManager fr, List<Message> messagesForUserInbox, List<Message> messagesToUserUnread, List<Message> messagesFormUser) {
            super(fr);
            mFragmentManager = fr;
            mFragmentTags = new HashMap<Integer, String>();
            this.messagesForUserInbox = messagesForUserInbox;
            this.messagesToUserUnread = messagesToUserUnread;
            this.messagesFormUser = messagesFormUser;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0)
                return "Inbox";
            else if (position == 1)
                return "Unread";
            else if (position == 2)
                return "Send";
            else
                return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new MessageListFragment(0, messagesForUserInbox);
            } else if (position == 1) {
                return new MessageListFragment(1, messagesToUserUnread);
            } else if (position == 2) {
                return new MessageListFragment(2, messagesFormUser);
            } else {
                return null;
            }
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                // record the fragment tag here.
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        public MessageListFragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null)
                return null;
            return (MessageListFragment) mFragmentManager.findFragmentByTag(tag);
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        if (LaoSchoolShared.myProfile == null) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.logoutApplication();
        }
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach(Context)");
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach()");
        super.onDetach();
    }
}
