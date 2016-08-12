package com.laoschool.screen;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.laoschool.R;
import com.laoschool.adapter.ListMessageAdapter;
import com.laoschool.screen.pager.MessagesPager;
import com.laoschool.adapter.MessagesPagerAdapter;
import com.laoschool.entities.Message;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.model.sqlite.DataAccessMessage;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;
import com.laoschool.view.ViewpagerDisableSwipeLeft;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenMessage extends Fragment implements FragmentLifecycle {

    private static final String TAG = ScreenMessage.class.getSimpleName();
    public static final int TAB_INBOX_0 = 0;
    public static final int TAB_UNREAD_1 = 1;
    public static final int TAB_SENT_2 = 2;

    private static Context context;
    private int containerId;
    private static DataAccessInterface service;
    public static ScreenMessage thiz;

    Message message;
    static DataAccessMessage dataAccessMessage;
    static ViewpagerDisableSwipeLeft pager;
    static PagerSlidingTabStrip tabs;
    static MessagesPagerAdapter messagesPagerAdapter;
    boolean refeshListMessage = false;

    private static FragmentManager fr;
    private static LinearLayout mMessages;
    private static ProgressBar mProgress;
    private boolean alreadyExecuted = false;
    private String currentRole;
    private RecyclerView mSearchMessageList;
    View mBacgroundSearch;
    private SearchView mSearchMessage;
    private MenuItem itemSearch;
    private View mExspanSearch;

    private static boolean reloadDataAfterCreateMessages = false;
    private static boolean reloadDataAfterRequestAttendane = false;

    private boolean onSearch = false;
    private FirebaseAnalytics mFirebaseAnalytics;

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
        _showProgessLoading(false);
        if (onSearch) {
            if (itemSearch != null) {
                MenuItemCompat.collapseActionView(itemSearch);
                //itemSearch.setVisible(true);
            } else {
                Log.d(TAG, "onResumeFragment() -item Search null");
            }
        }

        if (getUserVisibleHint() && reloadDataAfterCreateMessages) {
            Log.d(TAG, "onResumeFragment() -on Reload Data After Create Messages");
            loadLocalMessageSent();
        }
        if (getUserVisibleHint() && reloadDataAfterRequestAttendane) {
            Log.d(TAG, "onResumeFragment() -on Reload data After request attendance");
            resetDataMessageSentPager();
        }
    }

    private void resetDataMessageSentPager() {
        _showProgessLoading(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //get message  sent pager
                int pos = TAB_SENT_2;
                String tagSendPager = "android:switcher:" + pager.getId() + ":" + pos;
                Log.d(TAG, "resetDataMessageSentPager() - tagSendPager:" + tagSendPager);
                MessagesPager messagesPager = (MessagesPager) getFragmentManager().findFragmentByTag(tagSendPager);
                MessagesPagerAdapter messagesPagerAdapter = ((MessagesPagerAdapter) (pager.getAdapter()));

                int countPager = messagesPagerAdapter.getCount();
                Log.d(TAG, "resetDataMessageSentPager() - count Pager:" + countPager);
                if (messagesPagerAdapter.getRegisteredFragment(pos) != null) {
                    Log.d(TAG, "resetDataMessageSentPager() -page get fragement != null");
                    if (messagesPager == null) {
                        messagesPager = messagesPagerAdapter.getRegisteredFragment(pos);
                    }
                }

                if (messagesPager != null) {
                    List<Message> messagesFormUser = queryDataFormPosition(pos);
                    int size = messagesFormUser.size();
                    if (size > 0) {
                        messagesPager.reloadData(pos, messagesFormUser);
                    }
                } else {
                    Log.d(TAG, "resetDataMessageSentPager() - Sent pager null");
                }
                _showProgessLoading(false);
                reloadDataAfterRequestAttendane = false;
            }
        }, 1000);
    }

    private List<Message> queryDataFormPosition(int currentItem) {
        switch (currentItem) {
            case TAB_INBOX_0:
                return dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 1);
            case TAB_UNREAD_1:
                return dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 0);
            case TAB_SENT_2:
                return dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_FROM_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 1);
            default:
                return new ArrayList<>();
        }

    }

    public void setRefeshListMessage(boolean refeshListMessage) {
        this.refeshListMessage = refeshListMessage;
    }


    public interface IScreenMessage {
        void gotoScreenCreateMessage();

        void gotoMessageDetails(Message message);
    }

    public IScreenMessage iScreenMessage;


    public ScreenMessage() {

    }

    public static ScreenMessage instantiate(int containerId, String currentRole) {
        Log.d(TAG, "instantiate()");
        ScreenMessage screenMessage = new ScreenMessage();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        args.putString(LaoSchoolShared.CURRENT_ROLE, currentRole);
        screenMessage.setArguments(args);
        return screenMessage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        if (currentRole == null)
            return inflater.inflate(R.layout.screen_error_application, container, false);
        else {
            return _defineScreenMessage(inflater, container);
        }
    }

    private View _defineScreenMessage(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.screen_message, container, false);
        mMessages = (LinearLayout) view.findViewById(R.id.mMessages);
        mProgress = (ProgressBar) view.findViewById(R.id.mProgress);
        pager = (ViewpagerDisableSwipeLeft) view.findViewById(R.id.messageViewPage);
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        pager.setAllowedSwipeDirection(HomeActivity.SwipeDirection.all);
        mExspanSearch = view.findViewById(R.id.mExspanSearch);
        mBacgroundSearch = view.findViewById(R.id.mBacgroundSearch);

        mSearchMessageList = (RecyclerView) view.findViewById(R.id.mSearchMessageList);
        mSearchMessageList.setLayoutManager(new LinearLayoutManager(context));
        mSearchMessageList.setVisibility(View.GONE);

        if (!alreadyExecuted && getUserVisibleHint()) {
            _defineData();
            Bundle bundle = new Bundle();
            mFirebaseAnalytics.logEvent(TAG, bundle);
        }

        onExspanSearch();
        return view;
    }

    private void onExspanSearch() {
        mExspanSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuItemCompat.expandActionView(itemSearch);
                itemSearch.setVisible(true);
            }
        });
    }

    private void _defineData() {
        _showProgessLoading(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int userId = LaoSchoolShared.myProfile.getId();
                int countLocal = dataAccessMessage.getMessagesCountForUserId(userId);
                Log.d(TAG, "_defineData()/getMessagesCountForUserId(" + userId + ")=" + countLocal);
                if (countLocal > 0) {
                    getNewMessages();
//                    _getDataFormLocal();
                } else {
                    _getDataFormServer();
                }
                alreadyExecuted = true;
            }
        }, LaoSchoolShared.LOADING_TIME);
    }

    private void getNewMessages() {
        final String userID = String.valueOf(LaoSchoolShared.myProfile.getId());
        final int form_id = DataAccessMessage.getMaxMessagesID(LaoSchoolShared.myProfile.getId());
        service.getMessages("", "", "", "", userID, "", "", String.valueOf(form_id)
                , new AsyncCallback<List<Message>>() {
                    @Override
                    public void onSuccess(final List<Message> result) {
                        try {
                            for (Message message : result) {
                                dataAccessMessage.addOrUpdateMessage(message);
                            }
                            service.getMessages("", userID, "", "", "", "", "", String.valueOf(form_id), new AsyncCallback<List<Message>>() {
                                @Override
                                public void onSuccess(final List<Message> result) {
                                    try {
                                        for (Message message : result) {
                                            dataAccessMessage.addOrUpdateMessage(message);
                                        }
                                        _getDataFormLocal(-1);
                                    } catch (Exception e) {
                                        Log.d(TAG, "_getDataFormServer() onSuccess Exception=" + e.getMessage());
                                    }

                                }

                                @Override
                                public void onFailure(String message) {
                                    Log.e(TAG, "onFailure():" + message);
                                }

                                @Override
                                public void onAuthFail(String message) {
                                    LaoSchoolShared.goBackToLoginPage(context);
                                }
                            });
                        } catch (Exception e) {
                            Log.d(TAG, "_getDataFormServer() onSuccess Exception=" + e.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(String message) {
                        Log.e(TAG, "onFailure():" + message);
                    }

                    @Override
                    public void onAuthFail(String message) {
                        LaoSchoolShared.goBackToLoginPage(context);
                    }
                });
    }

    private void _getDataFormServer() {
        _getDataFormServer(-1);
    }

    private static void _getDataFormServer(final int position) {
        Log.d(TAG, "_getDataFormServer() position=" + position);
        if (position == -1) {
            initDataMessage();
            return;
        }
        int form_id = DataAccessMessage.getMaxMessagesID(LaoSchoolShared.myProfile.getId());
        final String classID = "";
        final String fromUserID = ((position == 2) ? String.valueOf(LaoSchoolShared.myProfile.getId()) : "");
        final String fromDate = "";
        final String toUserID = ((position < 2) ? String.valueOf(LaoSchoolShared.myProfile.getId()) : "");
        final String toDate = "";
        final String channel = "";
        final String status = "";
        final String fromID = ((form_id > 0) ? String.valueOf(form_id) : "");
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
                    public void onSuccess(final List<Message> result) {
                        try {
                            int sizeResults = result.size();
                            Log.d(TAG, "_getDataFormServer()):" +
                                    "getMessagesFormServer()/onSuccess() Results size=" + sizeResults);
                            for (Message message : result) {
                                dataAccessMessage.addOrUpdateMessage(message);
                            }
                            _getDataFormLocal(position);
                        } catch (Exception e) {
                            Log.d(TAG, "_getDataFormServer() onSuccess Exception=" + e.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(String message) {
                        Log.e(TAG, "onFailure():" + message);
                    }

                    @Override
                    public void onAuthFail(String message) {
                        LaoSchoolShared.goBackToLoginPage(context);
                    }
                });
    }

    public void updateDataMessage(final int position) {
        Log.d(TAG, "updateDataMessage() position=" + position);
        if (position == -1) {
            initDataMessage();
            return;
        }
        int form_id = DataAccessMessage.getMaxMessagesID(LaoSchoolShared.myProfile.getId());
        final String classID = "";
        final String fromUserID = ((position == 2) ? String.valueOf(LaoSchoolShared.myProfile.getId()) : "");
        final String fromDate = "";
        final String toUserID = ((position < 2) ? String.valueOf(LaoSchoolShared.myProfile.getId()) : "");
        final String toDate = "";
        final String channel = "";
        final String status = "";
        final String fromID = ((form_id > 0) ? String.valueOf(form_id) : "");
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
                    public void onSuccess(final List<Message> result) {
                        try {
                            int sizeResults = result.size();
                            Log.d(TAG, "updateDataMessage()):" +
                                    "onSuccess() Results size=" + sizeResults);
                            int end = 0;
                            for (int i = 0; i < sizeResults; i++) {
                                Message message = result.get(i);
                                dataAccessMessage.addOrUpdateMessage(message);
                                if (i == (sizeResults - 1)) {
                                    end = 1;
                                    break;
                                }
                            }
                            if (end == 1) {
                                Log.d(TAG, "updateDataMessage())/onSuccess() - addOrUpdateMessage finish");
                                if (position == 2) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            String tagSendPager = "android:switcher:" + pager.getId() + ":" + 2;
                                            Log.d(TAG, "tagSendPager:" + tagSendPager);
                                            MessagesPager messagesPager = (MessagesPager) getFragmentManager().findFragmentByTag(tagSendPager);
                                            if (((MessagesPagerAdapter) (pager.getAdapter())).getFragment(2) != null) {
                                                Log.d(TAG, "page get fragement != null");
                                                if (messagesPager == null) {
                                                    messagesPager = ((MessagesPagerAdapter) (pager.getAdapter())).getFragment(2);
                                                }
                                            }

                                            if (messagesPager != null) {
                                                List<Message> messagesFormUser = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_FROM_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 1);
                                                // messagesPager.li
                                                messagesPager.reloadData(2, messagesFormUser);
                                            } else {
                                                Log.d(TAG, "Sent pager null");
                                            }

                                            _showProgessLoading(false);
                                            reloadDataAfterCreateMessages = false;
                                        }
                                    }, 2000);
                                } else {
                                    _showProgessLoading(false);
                                    reloadDataAfterCreateMessages = false;
                                }
                            }
                        } catch (Exception e) {
                            Log.d(TAG, "updateDataMessage() onSuccess Exception=" + e.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(String message) {
                        _showProgessLoading(false);
                        Log.e(TAG, "onFailure():" + message);
                    }

                    @Override
                    public void onAuthFail(String message) {
                        _showProgessLoading(false);
                        LaoSchoolShared.goBackToLoginPage(context);
                    }
                }

        );
    }

    public void reloadSentPager() {
        _showProgessLoading(true);
        int form_id = DataAccessMessage.getMaxMessagesID(LaoSchoolShared.myProfile.getId());
        final String classID = "";
        final String fromUserID = String.valueOf(LaoSchoolShared.myProfile.getId());
        final String fromDate = "";
        final String toUserID = "";
        final String toDate = "";
        final String channel = "";
        final String status = "";
        final String fromID = ((form_id > 0) ? String.valueOf(form_id) : "");
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
                    public void onSuccess(final List<Message> result) {
                        try {
                            int sizeResults = result.size();
                            Log.d(TAG, "reloadSentPager():" +
                                    "onSuccess() Results size=" + sizeResults);
                            int end = 0;
                            for (int i = 0; i < sizeResults; i++) {
                                Message message = result.get(i);
                                dataAccessMessage.addOrUpdateMessage(message);
                                if (i == (sizeResults - 1)) {
                                    end = 1;
                                    break;
                                }
                            }
                            if (end == 1) {
                                Log.d(TAG, "reloadSentPager()/onSuccess() - addOrUpdateMessage finish");
                                loadLocalMessageSent();
                            } else {
                                _showProgessLoading(false);
                                reloadDataAfterCreateMessages = false;
                            }
                        } catch (Exception e) {
                            Log.d(TAG, "updateDataMessag() onSuccess Exception=" + e.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(String message) {
                        _showProgessLoading(false);
                        Log.e(TAG, "onFailure():" + message);
                    }

                    @Override
                    public void onAuthFail(String message) {
                        _showProgessLoading(false);
                        LaoSchoolShared.goBackToLoginPage(context);
                    }
                }

        );
    }


    private static void initDataMessage() {
        final String userID = String.valueOf(LaoSchoolShared.myProfile.getId());
        service.getMessages("", "", "", "", userID, "", "", ""
                , new AsyncCallback<List<Message>>() {
                    @Override
                    public void onSuccess(final List<Message> result) {
                        try {
                            for (Message message : result) {
                                dataAccessMessage.addOrUpdateMessage(message);
                            }
                            service.getMessages("", userID, "", "", "", "", "", "", new AsyncCallback<List<Message>>() {
                                @Override
                                public void onSuccess(final List<Message> result) {
                                    try {
                                        for (Message message : result) {
                                            dataAccessMessage.addOrUpdateMessage(message);
                                        }
                                        _getDataFormLocal(-1);
                                    } catch (Exception e) {
                                        Log.d(TAG, "_getDataFormServer() onSuccess Exception=" + e.getMessage());
                                    }

                                }

                                @Override
                                public void onFailure(String message) {
                                    Log.e(TAG, "onFailure():" + message);
                                }

                                @Override
                                public void onAuthFail(String message) {
                                    LaoSchoolShared.goBackToLoginPage(context);
                                }
                            });
                        } catch (Exception e) {
                            Log.d(TAG, "_getDataFormServer() onSuccess Exception=" + e.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(String message) {
                        Log.e(TAG, "onFailure():" + message);
                    }

                    @Override
                    public void onAuthFail(String message) {
                        LaoSchoolShared.goBackToLoginPage(context);
                    }
                });
    }

    private static void _showProgessLoading(boolean show) {
        if (mMessages != null) {
            mMessages.setVisibility(show ? View.GONE : View.VISIBLE);
        } else {
            Log.d(TAG, "mMessages Null");
        }
        if (mProgress != null) {
            mProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        } else {
            Log.d(TAG, "mProgress Null");
        }
    }

    private static void _getDataFormLocal(int position) {
        _initPageData(true);
        if (position > -1) {
            pager.setCurrentItem(position);
        }
        _handlerPageChange();
        _showProgessLoading(false);

    }

    private static void _initPageData(boolean inita) {
        List<Message> messagesForUserInbox = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 1);
        List<Message> messagesToUserUnread = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 0);
        List<Message> messagesFormUser = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_FROM_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 1);
        // Bind the tabs to the ViewPager
        if (inita)
            messagesPagerAdapter = new MessagesPagerAdapter(context, fr, thiz, messagesForUserInbox, messagesToUserUnread, messagesFormUser);
        else {
            messagesPagerAdapter = new MessagesPagerAdapter(context, fr, thiz, new ArrayList<Message>(), new ArrayList<Message>(), new ArrayList<Message>());
        }
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(messagesPagerAdapter);
        tabs.setViewPager(pager);

    }

    private static void _getDataFormLocal() {
        _getDataFormLocal(-1);
    }

    private static void _handlerPageChange() {
        ViewPager.OnPageChangeListener onPageNotificationChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (!reloadDataAfterCreateMessages & !reloadDataAfterRequestAttendane) {
                    MessagesPager notifragment = ((MessagesPagerAdapter) (pager.getAdapter())).getRegisteredFragment(position);
                    switch (position) {
                        case TAB_INBOX_0:
                            List<Message> messagesForUserInbox = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 1);
                            notifragment.reloadData(TAB_INBOX_0, messagesForUserInbox);
                            break;
                        case TAB_UNREAD_1:
                            List<Message> messagesForUserUnread = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 0);
                            //notifragment.setListMessage(messagesForUserUnread, 1, true);
                            notifragment.reloadData(TAB_UNREAD_1, messagesForUserUnread);
                            break;
//                        case TAB_SENT_2:
//                            List<Message> messagesFormUser = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_FROM_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 1);
//                            //notifragment.setListMessage(messagesForUserUnread, 1, true);
//                            notifragment.reloadData(TAB_SENT_2, messagesFormUser);
//                            break;
                    }
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
            currentRole = getArguments().getString(LaoSchoolShared.CURRENT_ROLE);
            if (currentRole != null) {
                Log.d(TAG, "-Container Id:" + containerId + ",currentRole:" + currentRole);
            } else {
                Log.d(TAG, "-Container Id:" + containerId + ",currentRole null");
            }
        }
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        service = DataAccessImpl.getInstance(getActivity());
        this.thiz = this;
        this.context = getActivity();
        this.fr = getFragmentManager();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (currentRole != null) {
//
            inflater.inflate(R.menu.menu_screen_message, menu);
            itemSearch = menu.findItem(R.id.search);
            itemSearch.setVisible(false);
            mSearchMessage = (SearchView) menu.findItem(R.id.search).getActionView();
            mSearchMessage.setQueryHint(context.getString(R.string.SCCommon_Search));
            onExpanCollapseSearch();
        }
    }

    private void onExpanCollapseSearch() {
        MenuItemCompat.setOnActionExpandListener(itemSearch, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                itemSearch.setVisible(true);
                mBacgroundSearch.setVisibility(View.VISIBLE);
                mSearchMessageList.setVisibility(View.VISIBLE);
                tabs.setVisibility(View.GONE);
                mMessages.setVisibility(View.GONE);
                mExspanSearch.setVisibility(View.GONE);

                ((HomeActivity) getActivity()).hideBottomBar();
                ((HomeActivity) getActivity()).displaySearch();

                expandSearchMessages();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mMessages.setVisibility(View.VISIBLE);
                tabs.setVisibility(View.VISIBLE);
                mExspanSearch.setVisibility(View.VISIBLE);
                mSearchMessageList.setVisibility(View.GONE);
                mBacgroundSearch.setVisibility(View.GONE);
                ((HomeActivity) getActivity()).showBottomBar();
                itemSearch.setVisible(false);
                ((HomeActivity) getActivity()).cancelSearch();
                clearListSearch();
                return true;
            }
        });
    }

    private void expandSearchMessages() {
        final int currentPositon = pager.getCurrentItem();
        Log.d(TAG, "expandSearchMessages() -position:" + currentPositon);
        final int userId = LaoSchoolShared.myProfile.getId();
        try {
            final List<Message> messageList = messagesPagerAdapter.getRegisteredFragment(pager.getCurrentItem()).getMessages();
            ListMessageAdapter listMessageAdapter = new ListMessageAdapter(mSearchMessageList, thiz, messageList, pager.getCurrentItem());
            mSearchMessageList.setAdapter(listMessageAdapter);
            mSearchMessage.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    if (!query.trim().isEmpty()) {
                        onSearch = true;
                        mBacgroundSearch.setVisibility(View.GONE);

                        mSearchMessageList.setEnabled(true);
                        messageList.clear();
                        if (currentPositon == 0) {
                            //search in box
                            messageList.addAll(DataAccessMessage.searchMessageInbox(userId, 1, query));
                        } else if (currentPositon == 1) {
                            //search in box un read
                            messageList.addAll(DataAccessMessage.searchMessageInbox(userId, 0, query));
                        } else if (currentPositon == 2) {
                            //search send box
                            messageList.addAll(DataAccessMessage.searchMessageSend(userId, query));
                        }
                        ListMessageAdapter listMessageAdapter = new ListMessageAdapter(mSearchMessageList, thiz, messageList, pager.getCurrentItem());
                        mSearchMessageList.setAdapter(listMessageAdapter);

                        if (query.length() > 5) {
                            Bundle query_search = new Bundle();
                            query_search.putString("query_text", query);
                            mFirebaseAnalytics.logEvent(TAG, query_search);
                        }
                    }
                    return true;
                }
            });
            mBacgroundSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MenuItemCompat.collapseActionView(itemSearch);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearListSearch() {
        mSearchMessageList.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_create_message:
                iScreenMessage.gotoScreenCreateMessage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach(context)");
        super.onAttach(context);
        iScreenMessage = (IScreenMessage) context;
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


    public void reloadDataAfterCreateMessages() {
        try {
            int form_id = DataAccessMessage.getMaxMessagesID(LaoSchoolShared.myProfile.getId());
            final String classID = "";
            final String fromUserID = String.valueOf(LaoSchoolShared.myProfile.getId());
            final String fromDate = "";
            final String toUserID = "";
            final String toDate = "";
            final String channel = "";
            final String status = "";
            final String fromID = ((form_id > 0) ? String.valueOf(form_id) : "");
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
                        public void onSuccess(final List<Message> result) {
                            try {
                                int sizeResults = result.size();
                                if (sizeResults > 0) {
                                    Log.d(TAG, "reloadDataAfterCreateMessages()/onSuccess() Results size=" + sizeResults);
                                    for (int i = 0; i < sizeResults; i++) {
                                        Message message = result.get(i);
                                        dataAccessMessage.addOrUpdateMessage(message);
                                    }
                                } else {
                                    Log.d(TAG, "reloadDataAfterCreateMessages()/onSuccess() - Nothing to change");
                                }
                            } catch (Exception e) {
                                Log.d(TAG, "reloadDataAfterCreateMessages()/onSuccess() - Exception=" + e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(String message) {
                            _showProgessLoading(false);
                            Log.e(TAG, "onFailure():" + message);
                        }

                        @Override
                        public void onAuthFail(String message) {
                            _showProgessLoading(false);
                            LaoSchoolShared.goBackToLoginPage(context);
                        }
                    }

            );
            reloadDataAfterCreateMessages = true;
        } catch (Exception e) {
            Log.e(TAG, "reloadDataAfterCreateMessages() -exception:" + e.getMessage());
        }
    }

    public void reloadDataAfterRequestAttendane() {
        try {
            int form_id = DataAccessMessage.getMaxMessagesID(LaoSchoolShared.myProfile.getId());
            final String classID = "";
            final String fromUserID = String.valueOf(LaoSchoolShared.myProfile.getId());
            final String fromDate = "";
            final String toUserID = "";
            final String toDate = "";
            final String channel = "";
            final String status = "";
            final String fromID = ((form_id > 0) ? String.valueOf(form_id) : "");
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
                        public void onSuccess(final List<Message> result) {
                            try {
                                int sizeResults = result.size();
                                if (sizeResults > 0) {
                                    Log.d(TAG, "reloadDataAfterRequestAttendane()/onSuccess() Results size=" + sizeResults);
                                    for (int i = 0; i < sizeResults; i++) {
                                        Message message = result.get(i);
                                        dataAccessMessage.addOrUpdateMessage(message);
                                    }
                                } else {
                                    Log.d(TAG, "reloadDataAfterRequestAttendane()/onSuccess() - Nothing to change");
                                }
                            } catch (Exception e) {
                                Log.d(TAG, "reloadDataAfterRequestAttendane()/onSuccess() - Exception=" + e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(String message) {
                            _showProgessLoading(false);
                            Log.e(TAG, "onFailure():" + message);
                        }

                        @Override
                        public void onAuthFail(String message) {
                            _showProgessLoading(false);
                            LaoSchoolShared.goBackToLoginPage(context);
                        }
                    }

            );
            reloadDataAfterRequestAttendane = true;
        } catch (Exception e) {
            Log.e(TAG, "reloadDataAfterRequestAttendane() -exception:" + e.getMessage());
        }
    }

    private void loadLocalMessageSent() {
        _showProgessLoading(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String tagSendPager = "android:switcher:" + pager.getId() + ":" + 2;
                Log.d(TAG, "loadLocalMessageSent() - tagSendPager:" + tagSendPager);
                MessagesPager messagesPager = (MessagesPager) getFragmentManager().findFragmentByTag(tagSendPager);
                if (((MessagesPagerAdapter) (pager.getAdapter())).getRegisteredFragment(2) != null) {
                    Log.d(TAG, "loadLocalMessageSent() -page get fragement != null");
                    if (messagesPager == null) {
                        messagesPager = ((MessagesPagerAdapter) (pager.getAdapter())).getRegisteredFragment(2);
                    }
                }

                if (messagesPager != null) {
                    List<Message> messagesFormUser = dataAccessMessage.getListMessagesForUser(Message.MessageColumns.COLUMN_NAME_FROM_USR_ID, LaoSchoolShared.myProfile.getId(), 30, 0, 1);
                    int size = messagesFormUser.size();
                    if (size > 0) {
                        messagesPager.reloadData(2, messagesFormUser);
                    }
                } else {
                    Log.d(TAG, "loadLocalMessageSent() - Sent pager null");
                }
                _showProgessLoading(false);
                reloadDataAfterCreateMessages = false;
            }
        }, 2000);

    }

}
