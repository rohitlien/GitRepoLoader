package com.rohit.gitrepoloader.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rohit.gitrepoloader.R;
import com.rohit.gitrepoloader.interfaces.InfoLoaderListener;
import com.rohit.gitrepoloader.utils.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by oust on 5/21/18.
 */

public class Dataloader extends AsyncTask{
    public Context context;
    public InfoLoaderListener infoLoaderListener;
    public String userName;

    public Dataloader(Context context, String userName,InfoLoaderListener infoLoaderListener) {
        this.context = context;
        this.infoLoaderListener = infoLoaderListener;
        this.userName=userName;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        hitServerTogetDetails();

        return null;
    }

    private void hitServerTogetDetails() {
        try {
            String user_repo_list_url=context.getResources().getString(R.string.user_repo_list_url);
            user_repo_list_url=user_repo_list_url.replace("{username}",userName);
            JsonArrayRequest jsonObjReq = new JsonArrayRequest(Method.GET, user_repo_list_url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.i("repo list ",response.toString());
                    parseRepoDetails(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("repo list ",error.toString());
                }
            });
            jsonObjReq.setShouldCache(false);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 100000f));
            AppController.getInstance().addToRequestQueue(jsonObjReq, "first");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseRepoDetails(JSONArray response) {
        if(response!=null && response.length()>0){

        }

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(infoLoaderListener!=null){

            //infoLoaderListener.onFinishLoading();
        }
    }
}
