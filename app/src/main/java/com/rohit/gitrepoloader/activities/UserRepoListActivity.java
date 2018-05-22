package com.rohit.gitrepoloader.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.rohit.gitrepoloader.R;
import com.rohit.gitrepoloader.adapters.RepoListAdapter;
import com.rohit.gitrepoloader.database.Dataloader;
import com.rohit.gitrepoloader.database.GitPreferences;
import com.rohit.gitrepoloader.database.RealmHelper;
import com.rohit.gitrepoloader.interfaces.InfoLoaderListener;
import com.rohit.gitrepoloader.interfaces.PageLoaderListener;
import com.rohit.gitrepoloader.models.RepoInfoDetail;
import com.rohit.gitrepoloader.utils.AppController;
import com.rohit.gitrepoloader.utils.GitLoaderTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserRepoListActivity extends AppCompatActivity implements PageLoaderListener,InfoLoaderListener {

    private ImageView backbtn;
    private LinearLayout ll_loading,no_repo_ll;
    private RecyclerView repo_recycler_view;
    private TextView no_repo_text;
    private ProgressBar progress;
    private String userName;
    private ArrayList<RepoInfoDetail> repoInfoDetails=new ArrayList<>();
    private RepoListAdapter repoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_repo_list);

        initViews();

        getIntentData();
    }

    private void getIntentData() {
        userName = getIntent().getStringExtra("username");
        if (userName != null && !userName.isEmpty()) {
            getData();
        } else {
            GitLoaderTools.showToast(getResources().getString(R.string.error_msg));
            finish();
        }
    }

    private void getData() {
        if (GitPreferences.getSavedInt(userName + "index") == 0) {
            hitServerForRepoList(GitPreferences.getSavedInt(userName + "index") + 1);
        } else {
            repoInfoDetails = RealmHelper.getAllRepoList(userName);
            if(repoInfoDetails!=null && repoInfoDetails.size()>0) {
                setAdapter();
            }else{
                progress.setVisibility(View.GONE);
                no_repo_text.setText("No repositories found !");
            }

        }
    }

    private void hitServerForRepoList(final int pageNo) {

        try {
            String user_repo_list_url = getResources().getString(R.string.user_repo_list__page_url);
            user_repo_list_url = user_repo_list_url.replace("{username}", userName);
            user_repo_list_url=user_repo_list_url.replace("{pageNo}",""+pageNo);
            user_repo_list_url=user_repo_list_url.replace("{pageLimit}","20");
            JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET, user_repo_list_url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.i("repo list ", response.toString());
                    GitPreferences.saveintVar(userName+"index",pageNo);
                    parseRepoDetails(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("repo list ", error.toString());
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
        ll_loading.setVisibility(View.GONE);
        if (response != null && response.length() > 0) {
            for(int i=0;i<response.length();i++){
                try {
                    JSONObject j = response.optJSONObject(i);
                    RepoInfoDetail repoInfoDetail = new RepoInfoDetail();
                    repoInfoDetail.setId(j.optInt("id"));
                    repoInfoDetail.setFull_name(j.optString("full_name"));
                    repoInfoDetail.setRepo_html_url(j.optString("html_url"));
                    repoInfoDetail.setOpen_issues_count(j.optInt("open_issues_count"));
                    repoInfoDetail.setCreated_at(j.optString("created_at"));
                    repoInfoDetail.setDefault_branch(j.optString("default_branch"));
                    repoInfoDetail.setDescription(j.optString("description"));
                    repoInfoDetail.setWatchers_count(j.optInt("watchers_count"));
                    repoInfoDetail.setStargazers_count(j.optInt("stargazers_count"));
                    repoInfoDetail.setSize(j.optInt("size"));
                    repoInfoDetail.setName(j.optString("name"));

                    JSONObject ownerJson = j.optJSONObject("owner");
                    if (ownerJson != null) {
                        repoInfoDetail.setLogin_id(ownerJson.optInt("id"));
                        repoInfoDetail.setLoginName(ownerJson.optString("login"));
                        repoInfoDetail.setAvatar_url(ownerJson.optString("avatar_url"));
                    }
                    RealmHelper.addRepoInfoData(repoInfoDetail);
                    repoInfoDetails.add(repoInfoDetail);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(repoInfoDetails!=null && repoInfoDetails.size()>0)
            setAdapter();
            else{
                progress.setVisibility(View.GONE);
                no_repo_text.setText("No repositories found !");
            }
        }else{
            GitPreferences.saveBooleanVar(userName+"dataLoaded",true);
            if(no_repo_ll.getVisibility()==View.VISIBLE){
                progress.setVisibility(View.GONE);
                no_repo_text.setText("No repositories found !");
            }
        }
    }

    private void setAdapter() {
        no_repo_ll.setVisibility(View.GONE);
        if(repoListAdapter==null) {
            repoListAdapter = new RepoListAdapter(this, repoInfoDetails,this,this);
            repoListAdapter.setUserName(userName);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            repo_recycler_view.setLayoutManager(mLayoutManager);
            repo_recycler_view.setItemAnimator(new DefaultItemAnimator());
            repo_recycler_view.setAdapter(repoListAdapter);
        }else{
            repoListAdapter.notifyChanges(repoInfoDetails);
        }

    }

    private void initViews() {

        backbtn = (ImageView) findViewById(R.id.back_button);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        repo_recycler_view = (RecyclerView) findViewById(R.id.repo_recycler_view);
        no_repo_ll=(LinearLayout)findViewById(R.id.no_repo_ll);
        progress=(ProgressBar)findViewById(R.id.progress);
        no_repo_text=(TextView)findViewById(R.id.no_repo_text);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void getNextData() {
        if(!GitPreferences.getSavedBoolean(userName+"dataLoaded")) {
            hitServerForRepoList(GitPreferences.getSavedInt(userName + "index") + 1);
            ll_loading.setVisibility(View.VISIBLE);
        }else{
            ll_loading.setVisibility(View.GONE);
        }
    }

    @Override
    public void openWebUrl(String url) {
        Intent intent=new Intent(UserRepoListActivity.this,RepoWebActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }

    @Override
    public void openRepoDetails(RepoInfoDetail repoInfoDetail) {
        Intent intent=new Intent(UserRepoListActivity.this,RepoDetailActivity.class);
        intent.putExtra("id",repoInfoDetail.getId());
        startActivity(intent);
    }
}
