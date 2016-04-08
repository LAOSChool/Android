package com.laoschool.screen;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.laoschool.R;
import com.laoschool.adapter.RecyclerViewListMessageAdapter;
import com.laoschool.adapter.RecylerViewScreenExamResultsStudentTabAdapter;
import com.laoschool.entities.ListMessages;
import com.laoschool.entities.Message;
import com.laoschool.entities.User;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;
import com.laoschool.view.ViewpagerDisableSwipeLeft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenMessage extends Fragment implements FragmentLifecycle {

    private int containerId;
    private String messageId;

    private static DataAccessInterface service;
    public static ScreenMessage thiz;

    public static List<Message> messageList;
    static Context context;
    Message message;
    ScreenMessage screenMessage;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }

   public interface IScreenMessage {
        void _gotoScreenCreateMessage();

        void _gotoMessageDetails(String messageId);
    }

    public IScreenMessage iScreenMessage;

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
            return (position == 0) ? new MessageListFragment() : new MessageListFragment();
        }
    }

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

        ViewPager pager = (ViewPager) view.findViewById(R.id.messageViewPage);
        pager.setAdapter(new MyPagerAdapter(getFragmentManager()));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        return view;
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
    }

    private static void getListMessage() {
        service.getMessages("", "", "", "", "", "", "", new AsyncCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> result) {
                messageList = new ArrayList<Message>();
                messageList.addAll(result);
            }

            @Override
            public void onFailure(String message) {

            }
        });
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
                Toast.makeText(getActivity(), "create message", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iScreenMessage = (IScreenMessage) activity;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public static class MessageListFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.view_message_list, container, false);
            final RecyclerView mRecyclerListMessage = (RecyclerView) view.findViewById(R.id.mRecyclerListMessage);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);

            //set adapter
            mRecyclerListMessage.setLayoutManager(gridLayoutManager);
            service.getMessages("", "", "", "", "", "", "", new AsyncCallback<List<Message>>() {
                @Override
                public void onSuccess(List<Message> result) {
                    RecyclerViewListMessageAdapter recyclerViewListMessageAdapter = new RecyclerViewListMessageAdapter(thiz,result);
                    mRecyclerListMessage.setAdapter(recyclerViewListMessageAdapter);

                }

                @Override
                public void onFailure(String message) {

                }
            });


            return view;
        }
    }
}
