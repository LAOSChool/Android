package com.laoschool.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.laoschool.R;
import com.laoschool.entities.Image;

import java.util.List;

/**
 * Created by Hue on 4/20/2016.
 */
public class ListImageSelectedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "LImageSelectedAdapter";
    private Context context;
    private List<Image> fileurls;

    public ListImageSelectedAdapter(final Context context, List<Image> fileurls) {
        this.context = context;
        this.fileurls = fileurls;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image_seleted, parent, false);
        ListImageSelectedAdapterViewHolder listImageSelectedAdapterViewHolder = new ListImageSelectedAdapterViewHolder(view, viewType);
        return listImageSelectedAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            if (holder instanceof ListImageSelectedAdapterViewHolder) {
                ListImageSelectedAdapterViewHolder listHolder = (ListImageSelectedAdapterViewHolder) holder;
                View view = listHolder.view;
                final Image image = fileurls.get(position);
                String url = image.getLocal_file_url();

                ImageView imgImageSelected = (ImageView) view.findViewById(R.id.imgImageSelected);
                TextView txtImageSelectedUrl = (TextView) view.findViewById(R.id.txtImageSelectedUrl);
                ImageView imgImageSelectedDelete = (ImageView) view.findViewById(R.id.imgDownloadImage);
                TextView txtImageSelectedFileSize = (TextView) view.findViewById(R.id.txtImageSelectedFileSize);
                final EditText txtCaptionImage = (EditText) view.findViewById(R.id.txtCaptionImage);
                imgImageSelectedDelete.setColorFilter(context.getResources().getColor(R.color.colorDefault));
                final int THUMBSIZE = 128;
                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(
                        BitmapFactory.decodeFile(url),
                        512,
                        384);

                //
                //imgImageSelected.setImageURI(Uri.parse(url));
                imgImageSelected.setImageBitmap(ThumbImage);
                txtImageSelectedUrl.setText(url);
                //txtImageSelectedFileSize.setText(Long.toString(returnCursor.getLong(sizeIndex)));

                //
                imgImageSelectedDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            fileurls.remove(position);
                            notifyItemRemoved(position);
                        } catch (Exception e) {
                            fileurls.remove(0);
                            notifyItemRemoved(0);
                        }

                    }
                });

                txtCaptionImage.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        image.setCaption(txtCaptionImage.getText().toString());
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return fileurls.size();
    }

    public List<Image> getListImage() {
        return fileurls;
    }

    public class ListImageSelectedAdapterViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;

        public ListImageSelectedAdapterViewHolder(View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            this.viewType = viewType;
        }
    }

    public void clearData() {
        int size = this.fileurls.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.fileurls.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }
}
