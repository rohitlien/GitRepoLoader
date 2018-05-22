package com.rohit.gitrepoloader.utils;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.rohit.gitrepoloader.database.RealmHelper;

import io.realm.Realm;

import static android.content.ContentValues.TAG;

/**
 * Created by oust on 5/20/18.
 */

public class AppController extends Application {
    private RequestQueue mRequestQueue;
    private static Context mContext;
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mInstance=this;
        Realm.init(this);
        RealmHelper.setDefaultConfig();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
        Log.e("VOLLEY", "Added request object");
    }

    public static Context getmContext() {
        return mContext;
    }
}
