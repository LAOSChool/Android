package com.laoschool;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.laoschool.model.DataAccessImpl;
import com.laoschool.model.DataAccessInterface;
import com.laoschool.model.sqlite.DatabaseHandler;

/**
 * Created by Hue on 4/20/2016.
 */
public class LaoSchoolSingleton {
    private static LaoSchoolSingleton instance;
    private static DatabaseHandler dataBaseHelper;
    private static DataAccessInterface service;
    public static Context context;
    private static ImageLoader mImageLoader;
    private static RequestQueue mRequestQueue;


    public LaoSchoolSingleton(Context context) {
        this.context = context;
    }

    public static void initInstance(Context context) {
        if (instance == null) {
            // Create the instance
            instance = new LaoSchoolSingleton(context);
        }


    }
    public static LaoSchoolSingleton getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
//            mRequestQueue = Volley.newRequestQueue(context);
            Cache cache = new DiskBasedCache(context.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            // Don't forget to start the volley request queue
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(LaoSchoolSingleton.getInstance().getRequestQueue(),
                    new ImageLoader.ImageCache() {
                        private final android.support.v4.util.LruCache<String, Bitmap>
                                cache = new android.support.v4.util.LruCache<String, Bitmap>(20);

                        @Override
                        public Bitmap getBitmap(String url) {
                            return cache.get(url);
                        }

                        @Override
                        public void putBitmap(String url, Bitmap bitmap) {
                            cache.put(url, bitmap);
                        }
                    });
        }
        return mImageLoader;
    }

    public DatabaseHandler getDataBaseHelper() {
        if (dataBaseHelper == null) {
            dataBaseHelper = new DatabaseHandler(context);
        }
        return dataBaseHelper;
    }

    public DataAccessInterface getDataAccessService() {
        if (service == null)
            service = DataAccessImpl.getInstance(context);
        return service;
    }


}
