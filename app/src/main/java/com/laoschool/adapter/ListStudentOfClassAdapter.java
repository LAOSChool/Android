package com.laoschool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.User;
import com.laoschool.screen.ScreenListStudent;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hue on 6/13/2016.
 */
public class ListStudentOfClassAdapter extends RecyclerView.Adapter<ListStudentOfClassAdapter.ViewHolder> {
    private ScreenListStudent screenListStudent;
    private Context context;
    private List<User> userList;
    private int TYPE_SUB_HEADER = 0;
    private int TYPE_TITLE = 1;
    private int TYPE_LINE = 2;

    public ListStudentOfClassAdapter(ScreenListStudent screenListStudent, List<User> users) {
        this.screenListStudent = screenListStudent;
        this.context = screenListStudent.getActivity();
        this.userList = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_LINE)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_line, parent, false); //Inflating the layout
        else if (viewType == TYPE_TITLE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_avata_with_nick_name, parent, false); //Inflating the layout
        } else if (viewType == TYPE_SUB_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_only_title, parent, false); //Inflating the layout
        }
        ViewHolder viewHolder = new ViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        View view = holder.view;
        try {
            User user = userList.get(position);
            final String title = user.getFullname();
            if (holder.viewType == TYPE_TITLE) {
                //Define and set data
                TextView row_title = (TextView) view.findViewById(R.id.row_name);
                TextView row_nick_name = (TextView) view.findViewById(R.id.row_nick_name);
                NetworkImageView row_icon = (NetworkImageView) view.findViewById(R.id.row_icon);

                row_title.setText(title);
                row_nick_name.setText(user.getNickname());
                //Load photo
                String photo = user.getPhoto();
                if (photo != null) {
                    LaoSchoolSingleton.getInstance().getImageLoader().get(photo, ImageLoader.getImageListener(row_icon,
                            R.drawable.ic_account_circle_black_36dp, R.drawable.ic_account_circle_black_36dp));
                    row_icon.setImageUrl(photo, LaoSchoolSingleton.getInstance().getImageLoader());
                } else {
                    row_icon.setDefaultImageResId(R.drawable.ic_account_circle_black_36dp);
                }
                //Handler on click item
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, title, Toast.LENGTH_SHORT).show();

                    }
                });
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public int getItemViewType(int position) {
//        String item = userList.get(position);
//        if (item.equals(context.getString(R.string.row_sub_header)))
//            return TYPE_SUB_HEADER;
//        else if (!item.equals(context.getString(R.string.row_line)))
        return TYPE_TITLE;
//        else
//            return TYPE_LINE;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }
}

