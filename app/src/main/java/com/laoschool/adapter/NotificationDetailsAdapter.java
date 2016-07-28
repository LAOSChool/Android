package com.laoschool.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.R;
import com.laoschool.entities.Image;
import com.laoschool.entities.Message;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.sqlite.DataAccessImage;
import com.laoschool.screen.PreviewImage;
import com.laoschool.shared.LaoSchoolShared;
import com.laoschool.tools.CustomNetworkImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Hue on 4/20/2016.
 */
public class NotificationDetailsAdapter extends RecyclerView.Adapter<NotificationDetailsAdapter.ListImageNotificationAdapterViewHolder> {

    private static final String TAG = NotificationDetailsAdapter.class.getSimpleName();
    private final Activity activity;
    private Context context;
    private List<Object> objectses;
    ImageLoader imageLoader;

    private int TYPE_DETAILS = 1;
    private int TYPE_IMAGE = 2;

    private TextView txtTilteNotificationDetails;
    private TextView txtContentNotificationDetails;
    private TextView txtDateNotificationDetails;
    private TextView txtFormUserNameNotificationDetails;
    private TextView txtToUserNameNotificationDetails;
    private ImageView imgPiorityNotificationDetails;
    private ImageView imgUserSentNotificationAvata;

    public NotificationDetailsAdapter(Activity activity, Message message) {
        this.activity = activity;
        this.context = activity;
        this.objectses = new ArrayList<>();
        objectses.add(message);
        if (message.getNotifyImages() != null)
            objectses.addAll(message.getNotifyImages());
        else Log.d(TAG, "image of notification null");

        imageLoader = LaoSchoolSingleton.getInstance().getImageLoader();
    }

    @Override
    public ListImageNotificationAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_DETAILS)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notitication_details, parent, false);
        else if (viewType == TYPE_IMAGE)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image_details, parent, false);
        ListImageNotificationAdapterViewHolder listImageNotificationAdapterViewHolder = new ListImageNotificationAdapterViewHolder(view, viewType);
        return listImageNotificationAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(ListImageNotificationAdapterViewHolder holder, final int position) {
        View view = holder.view;
        try {
            if (holder.viewType == TYPE_DETAILS) {
                Message notification = (Message) objectses.get(0);
                txtTilteNotificationDetails = (TextView) view.findViewById(R.id.txtTilteNotificationDetails);
                txtContentNotificationDetails = (TextView) view.findViewById(R.id.txtContentNotificationDetails);
                txtDateNotificationDetails = (TextView) view.findViewById(R.id.txtDateNotificationDetails);
                txtFormUserNameNotificationDetails = (TextView) view.findViewById(R.id.txtFormUserNameNotificationDetails);
                txtToUserNameNotificationDetails = (TextView) view.findViewById(R.id.txtToUserNameNotificationDetails);
                imgPiorityNotificationDetails = (ImageView) view.findViewById(R.id.imgPiorityNotificationDetails);
                imgUserSentNotificationAvata = (ImageView) view.findViewById(R.id.imgUserSentNotificationAvata);

                txtTilteNotificationDetails.setText(notification.getTitle());
                txtContentNotificationDetails.setText(notification.getContent());
                txtDateNotificationDetails.setText(LaoSchoolShared.formatDate(notification.getSent_dt(), 2));

                if (notification.getImp_flg() == 1) {
                    imgPiorityNotificationDetails.setColorFilter(
                            context.getResources().getColor(R.color.colorPriorityHigh));
                    imgPiorityNotificationDetails.setTag(R.color.colorPriorityHigh);
                } else {
                    imgPiorityNotificationDetails.setColorFilter(
                            context.getResources().getColor(R.color.colorPriorityLow));
                    imgPiorityNotificationDetails.setTag(R.color.colorPriorityLow);
                }
//                imgPiorityNotificationDetails.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (imgPiorityNotificationDetails.getTag().equals(R.color.colorPriorityLow)) {
//                            imgPiorityNotificationDetails.setColorFilter(context.getResources().getColor(R.color.colorPriorityHigh));
//                            imgPiorityNotificationDetails.setTag(R.color.colorPriorityHigh);
//                        } else {
//                            imgPiorityNotificationDetails.setColorFilter(context.getResources().getColor(R.color.colorPriorityLow));
//                            imgPiorityNotificationDetails.setTag(R.color.colorPriorityLow);
//                        }
//                    }
//                });
                imgUserSentNotificationAvata.setColorFilter(context.getResources().getColor(R.color.colorUnread));

                txtFormUserNameNotificationDetails.setText(notification.getFrom_user_name());
                txtToUserNameNotificationDetails.setText("to " + notification.getTo_user_name());

                //view.setVisibility(View.GONE);

            } else if (holder.viewType == TYPE_IMAGE) {
                final Image image = (Image) objectses.get(position);

                final CustomNetworkImageView imgImageSelected = (CustomNetworkImageView) view.findViewById(R.id.imgImageSelected);
                final ImageView imgDownloadImage = (ImageView) view.findViewById(R.id.imgDownloadImage);
                final ImageView imgPhoto = (ImageView) view.findViewById(R.id.imgPhoto);
                TextView txtImageCaption = (TextView) view.findViewById(R.id.txtImageCaption);

                imgDownloadImage.setColorFilter(context.getResources().getColor(R.color.colorDefault));
                imgPhoto.setColorFilter(context.getResources().getColor(R.color.colorPrimary));

                txtImageCaption.setText(image.getCaption());
                if (image.getLocal_file_url() == null) {
                    imgDownloadImage.setVisibility(View.VISIBLE);
                    //load image form url
                    _loadImageFormUrl(imgImageSelected, imgDownloadImage, image);
                } else {
                    File imageFile = new File(image.getLocal_file_url());
                    if (imageFile.exists()) {
                        imgDownloadImage.setVisibility(View.GONE);

                        Log.d("LoadImage", "form local url:" + imageFile.getAbsolutePath());
                        final Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        _showImage(imgImageSelected, imgDownloadImage, image, bitmap);

                    } else {
                        imgDownloadImage.setVisibility(View.VISIBLE);
                        //load image form url
                        _loadImageFormUrl(imgImageSelected, imgDownloadImage, image);
                    }
                }
//                if (image.getLocal_file_url() != null || !image.getLocal_file_url().trim().isEmpty() || !image.getLocal_file_url().equals("null")) {
//
//                } else {
//
//                }

                View.OnClickListener previewOnclick = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, PreviewImage.class);
                        intent.putExtra(Image.ImageColumns.COLUMN_NAME_NOTIFY_ID, image.getNotify_id());
                        intent.putExtra(PreviewImage.POSITION_DISPLAY, position);
                        activity.startActivity(intent);
                    }
                };
                imgImageSelected.setOnClickListener(previewOnclick);
                txtImageCaption.setOnClickListener(previewOnclick);
                imgPhoto.setOnClickListener(previewOnclick);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void _loadImageFormUrl(final CustomNetworkImageView imgImageSelected, final ImageView imgDownloadImage, final Image image) {
        if (image.getFile_url() != null) {
            Log.d("LoadImage", "_loadImageFormUrl():Url=" + image.getFile_url());
            String url = image.getFile_url();
            imageLoader.get(url, ImageLoader.getImageListener(imgImageSelected,
                    R.drawable.loading, android.R.drawable
                            .ic_dialog_alert));
            imgImageSelected.setImageUrl(url, imageLoader);
            LaoSchoolSingleton.getInstance().getDataAccessService().getImageBitmap(image.getFile_url(), new AsyncCallback<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap result) {
                    imgDownloadImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            _saveImagebyBitmap(image, result);
                            imgDownloadImage.setVisibility(View.GONE);
                            notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void onFailure(String message) {
                    Log.d("LoadImage", "_loadImageFormUrl()Error messag:" + image.getLocal_file_url());
                    imgImageSelected.setImageResource(R.drawable.no_photo);
                    imgDownloadImage.setVisibility(View.GONE);
                }

                @Override
                public void onAuthFail(String message) {
                    LaoSchoolShared.goBackToLoginPage(context);
                }
            });
        } else {
            Log.d("LoadImage", "_loadImageFormUrl() :Url= null");
        }
    }

    private void _showImage(CustomNetworkImageView imgImageSelected, final ImageView imgDownloadImage, final Image image, final Bitmap bitmap) {
        imgImageSelected.setLocalImageBitmap(bitmap);
        imgDownloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _saveImagebyBitmap(image, bitmap);
                imgDownloadImage.setVisibility(View.GONE);
                notifyDataSetChanged();
            }
        });
    }

    private void _saveImagebyBitmap(Image image, Bitmap result) {
        try {
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/" + context.getString(R.string.SCCommon_AppName);
            File dir = new File(file_path);
            if (!dir.exists())
                dir.mkdirs();
            Log.d("SaveImage", "ext dir:" + file_path);

            File file = new File(dir, image.getFile_name()); // the File to save to
            Log.d("SaveImage", "file dir:" + file.getAbsolutePath());
            OutputStream fOut = new FileOutputStream(file);

            result.compress(Bitmap.CompressFormat.PNG, 90, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush();
            fOut.close(); // do not forget to close the stream
            if (file != null) {
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                Log.d("SaveImage", "Save image to:" + file.getAbsolutePath());
            } else {
                Log.d("SaveImage", "file null");
            }

            //update local url image
            image.setLocal_file_url(file.getAbsolutePath());
            DataAccessImage.updateImage(image);
            Toast.makeText(context, R.string.SCCreateAnnocement_SaveImage, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.SCCreateAnnocement_err_msg_download_image_fails, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return objectses.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_DETAILS;
        } else {
            return TYPE_IMAGE;
        }

    }

    public class ListImageNotificationAdapterViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public ListImageNotificationAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
