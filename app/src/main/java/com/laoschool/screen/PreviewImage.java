package com.laoschool.screen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.laoschool.R;
import com.laoschool.LaoSchoolSingleton;
import com.laoschool.entities.Image;
import com.laoschool.model.AsyncCallback;
import com.laoschool.model.sqlite.DataAccessImage;
import com.laoschool.tools.CustomNetworkImageView;

import java.io.File;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PreviewImage extends AppCompatActivity {

    public static final String POSITION_DISPLAY = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preview_image);
        int noti_id = getIntent().getIntExtra(Image.ImageColumns.COLUMN_NAME_NOTIFY_ID, 0);
        int position = getIntent().getIntExtra(POSITION_DISPLAY, 0);
        if (position > 0) {
            position--;
        }
        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this, DataAccessImage.getListImageFormNotificationId(noti_id));

        ViewPager mViewPager = (ViewPager) findViewById(R.id.previewImagePager);
        mViewPager.setAdapter(mCustomPagerAdapter);

        mViewPager.setCurrentItem(position);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        List<Image> images;

        public CustomPagerAdapter(Context context, List<Image> images) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            final CustomNetworkImageView imageView = (CustomNetworkImageView) itemView.findViewById(R.id.imageView);
            TextView txtCaption = (TextView) itemView.findViewById(R.id.txtCaption);

            Image image = images.get(position);

            txtCaption.setText(image.getCaption());

            if (image.getLocal_file_url() == null) {
                //load image form url
                _loadImageFormUrl(imageView, image);
            } else {
                File imageFile = new File(image.getLocal_file_url());
                if (imageFile.exists()) {

                    Log.d("LoadImage", "form local url:"+imageFile.getAbsolutePath());
                    final Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    _showImage(imageView, bitmap);

                } else {
                    //load image form url
                    _loadImageFormUrl(imageView, image);
                }
            }


            container.addView(itemView);

            return itemView;
        }

        private void _loadImageFormUrl(final CustomNetworkImageView imageView, Image image) {
            LaoSchoolSingleton.getInstance().getImageLoader().get(image.getFile_url(), ImageLoader.getImageListener(imageView,
                    R.mipmap.ic_launcher, android.R.drawable
                            .ic_dialog_alert));
            imageView.setImageUrl(image.getFile_url(), LaoSchoolSingleton.getInstance().getImageLoader());
//            LaoSchoolSingleton.getInstance().getDataAccessService().getImageBitmap(image.getFile_url(), new AsyncCallback<Bitmap>() {
//                @Override
//                public void onSuccess(Bitmap result) {
//                    imageView.setImageBitmap(result);
//                }
//
//                @Override
//                public void onFailure(String message) {
//                    imageView.setImageResource(R.drawable.no_photo);
//                }
//            });
        }

        private void _showImage(CustomNetworkImageView imageView, Bitmap bitmap) {
            imageView.setLocalImageBitmap(bitmap);

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }


    }

}
