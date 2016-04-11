package com.laoschool.screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.laoschool.R;
import com.laoschool.entities.Message;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.view.FragmentLifecycle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenMessageDetails extends Fragment implements FragmentLifecycle {


    private static final String TAG = "ScreenMessageDetails";
    private int containerId;
    private TextView txtTilteMessageDetails;
    private TextView txtContentMessageDetails;
    private TextView txtDateMessageDetails;
    private TextView txtFormUserNameMessageDetails;
    private TextView txtToUserNameMessageDetails;
    private ImageView imgPiorityMessageDetails;

    public ScreenMessageDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.screen_message_details, container, false);
        txtTilteMessageDetails = (TextView) view.findViewById(R.id.txtTilteMessageDetails);
        txtContentMessageDetails = (TextView) view.findViewById(R.id.txtContentMessageDetails);
        txtDateMessageDetails = (TextView) view.findViewById(R.id.txtDateMessageDetails);
        txtFormUserNameMessageDetails = (TextView) view.findViewById(R.id.txtFormUserNameMessageDetails);
        txtToUserNameMessageDetails = (TextView) view.findViewById(R.id.txtToUserNameMessageDetails);
        imgPiorityMessageDetails = (ImageView) view.findViewById(R.id.imgPiorityMessageDetails);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            containerId = getArguments().getInt(LaoSchoolShared.CONTAINER_ID);
            Log.d(getString(R.string.title_screen_message_details), "-Container Id:" + containerId);
        }
    }

    @Override
    public void onPauseFragment() {
    }

    @Override
    public void onResumeFragment() {
        String tag = LaoSchoolShared.makeFragmentTag(containerId, LaoSchoolShared.POSITION_SCREEN_MESSAGE_0);
        ScreenMessage screenMessage = (ScreenMessage) ((HomeActivity) getActivity()).getSupportFragmentManager().findFragmentByTag(tag);
        if (screenMessage != null) {
//            Message
            Message message = screenMessage.getMessage();
            if (message == null) {
                message = Message.fromJson("{\n" +
                        "  \"id\": 1,\n" +
                        "  \"school_id\": 1,\n" +
                        "  \"class_id\": 1,\n" +
                        "  \"from_usr_id\": 1,\n" +
                        "  \"from_user_name\": \"NamNT1\",\n" +
                        "  \"to_usr_id\": 2,\n" +
                        "  \"to_user_name\": \"Hue1\",\n" +
                        "  \"content\": \"test message\",\n" +
                        "  \"msg_type_id\": 1,\n" +
                        "  \"channel\": 1,\n" +
                        "  \"is_sent\": 1,\n" +
                        "  \"sent_dt\": \"2016-03-24 00:00:00.0\",\n" +
                        "  \"is_read\": 1,\n" +
                        "  \"read_dt\": \"2016-03-24 00:00:00.0\",\n" +
                        "  \"imp_flg\": 1,\n" +
                        "  \"other\": \"ko co gi quan trong\",\n" +
                        "  \"title\": \"test title\",\n" +
                        "  \"cc_list\": \"\",\n" +
                        "  \"schoolName\": \"Truong Tieu Hoc Thanh Xuan Trung\",\n" +
                        "  \"messageType\": \"NX\"\n" +
                        "}");
                Toast.makeText(getActivity(), "Message null", Toast.LENGTH_SHORT).show();
            }
            txtTilteMessageDetails.setText(message.getTitle());
            txtContentMessageDetails.setText(message.getContent());
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                txtDateMessageDetails.setText("Date " + sdf.format((message.getSent_dt())));
            } catch (Exception e) {
                Log.d(TAG, "Eror:" + e.getMessage());
                txtDateMessageDetails.setText("Date " + message.getSent_dt());
            }
            if ((message.getImp_flg() == 1)) {
                imgPiorityMessageDetails.setImageDrawable(LaoSchoolShared.getDraweble(getActivity(), R.drawable.ic_flag_black_24dp));
            } else {
                imgPiorityMessageDetails.setImageDrawable(null);
            }

            txtFormUserNameMessageDetails.setText(message.getFrom_user_name());
            txtToUserNameMessageDetails.setText("to " + message.getTo_user_name());
        }

    }


    public static Fragment instantiate(int containerId, String currentRole) {
        ScreenMessageDetails fragment = new ScreenMessageDetails();
        Bundle args = new Bundle();
        args.putInt(LaoSchoolShared.CONTAINER_ID, containerId);
        fragment.setArguments(args);
        return fragment;
    }

}
