package com.laoschool.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.laoschool.entities.Message;
import com.laoschool.screen.ScreenMessage;
import com.laoschool.screen.pager.MessagesPager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hue on 5/19/2016.
 */
public class MessagesPagerAdapter extends FragmentPagerAdapter {
    protected static final String TAG = MessagesPagerAdapter.class.getSimpleName();
    private static final int POSITION_TO_USER_ID = 0;
    private static final int POSITION_TO_USER_ID_UNREAD = 1;
    private static final int POSITION_FORM_USER_ID = 2;
    private FragmentManager mFragmentManager;
    private Map<Integer, String> mFragmentTags;
    private List<Message> messagesForUserInbox;
    private List<Message> messagesToUserUnread;
    private List<Message> messagesFormUser;
    private ScreenMessage screenMessage;


    public MessagesPagerAdapter(FragmentManager fr, ScreenMessage screenMessage, List<Message> messagesForUserInbox, List<Message> messagesToUserUnread, List<Message> messagesFormUser) {
        super(fr);
        mFragmentManager = fr;
        mFragmentTags = new HashMap<Integer, String>();
        this.screenMessage = screenMessage;
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
        if (position == POSITION_TO_USER_ID) {
            return new MessagesPager(screenMessage, POSITION_TO_USER_ID, messagesForUserInbox);
        } else if (position == POSITION_TO_USER_ID_UNREAD) {
            return new MessagesPager(screenMessage, POSITION_TO_USER_ID_UNREAD, messagesToUserUnread);
        } else if (position == POSITION_FORM_USER_ID) {
            return new MessagesPager(screenMessage, POSITION_FORM_USER_ID, messagesFormUser);
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

    public MessagesPager getFragment(int position) {
        String tag = mFragmentTags.get(position);
        if (tag == null)
            return null;
        return (MessagesPager) mFragmentManager.findFragmentByTag(tag);
    }

}

