package com.laoschool.screen;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.laoschool.R;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenMessage extends Fragment implements FragmentLifecycle{

    private int containerId;

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }

    interface IScreenMessage {
        void _gotoScreenCreateMessage();
    }

    private IScreenMessage iScreenMessage;

    public ScreenMessage() {

    }

    public static ScreenMessage instantiate(int containerId) {
        ScreenMessage screenMessage = new ScreenMessage();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        screenMessage.setArguments(args);
        return screenMessage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.screen_message, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            Log.d(getString(R.string.title_screen_message), "-Container Id:" + containerId);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //set display menu item
//        menu.findItem(R.id.action_create_message).setVisible(true);
//        menu.findItem(R.id.action_cancel).setVisible(false);
//        menu.findItem(R.id.action_send_message).setVisible(false);
//        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_screen_message, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_create_message:
                iScreenMessage._gotoScreenCreateMessage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iScreenMessage = (IScreenMessage) activity;
    }
}
