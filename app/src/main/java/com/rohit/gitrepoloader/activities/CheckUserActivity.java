package com.rohit.gitrepoloader.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rohit.gitrepoloader.R;
import com.rohit.gitrepoloader.database.RealmHelper;
import com.rohit.gitrepoloader.models.UserData;
import com.rohit.gitrepoloader.utils.AppController;
import com.rohit.gitrepoloader.utils.GitLoaderTools;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CheckUserActivity extends AppCompatActivity {

    private EditText username_et;
    private TextView check_user_tv;
    private LinearLayout error_ll;
    private boolean isNetworkQueued=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_user);

        initViews();
    }

    private void initViews() {
        username_et =(EditText) findViewById(R.id.username_et);
        check_user_tv=(TextView) findViewById(R.id.check_user_tv);
        error_ll =(LinearLayout) findViewById(R.id.error_ll);

        check_user_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username_et!=null){
                    username_et.setError(null);
                    if(username_et.getText().toString()!=null && !username_et.getText().toString().isEmpty()){
                        if(!isNetworkQueued) {
                            checkUser(username_et.getText().toString());
                        } else{
                            printWaitToast();
                        }
                    }else{
                        username_et.setError("Please enter your username!");
                    }
                }
            }
        });
    }

    private void checkUser(String username) {
        username=username.toLowerCase();
        if(RealmHelper.isUserExist(username)){
            openReposInfo(username);
        }else {
            if(GitLoaderTools.checkInternetStatus()) {
                hitServerForValidUser(username);
            }else{
                GitLoaderTools.showToast(getResources().getString(R.string.no_network_msg));
            }
        }
    }

    private void printWaitToast() {
        Toast.makeText(this, "Wait ! Your query is on queue !", Toast.LENGTH_SHORT).show();
    }

    private void hitServerForValidUser(final String userName) {
        try {
            String verify_user_url=getResources().getString(R.string.verify_user);
            verify_user_url=verify_user_url.replace("{username}",userName);
            isNetworkQueued=true;
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, verify_user_url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    isNetworkQueued=false;
                    Log.i("Check user",response.toString());
                    saveUserDetails(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    isNetworkQueued=false;
                    Log.i("Check user",error.toString());
                    fadeErrorAnimation();
                }
            });
            jsonObjReq.setShouldCache(false);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 100000f));
            AppController.getInstance().addToRequestQueue(jsonObjReq, "first");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveUserDetails(JSONObject response) {
        if(response!=null) {
            UserData userData = new UserData();
            userData.setAvatar(response.optString("avatar_url"));
            userData.setCompany(response.optString("company"));
            userData.setEmail(response.optString("email"));
            userData.setFollowers(response.optInt("followers"));
            userData.setFollowing(response.optInt("following"));
            userData.setHtml_url(response.optString("html_url"));
            userData.setId(response.optInt("id"));
            userData.setName(response.optString("name"));
            String loginName=response.optString("login");
            loginName=loginName.toLowerCase();
            userData.setLoginName(loginName);
            RealmHelper.addUserData(userData);

            openReposInfo(loginName);
        }
    }

    private void openReposInfo(String userName) {

        username_et.setText("");

        Intent intent =new Intent(CheckUserActivity.this,UserProfileActivity.class);
        intent.putExtra("username",userName);
        startActivity(intent);

    }

    public void fadeErrorAnimation(){
        error_ll.setVisibility(View.VISIBLE);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(error_ll, "alpha", 1f, 0.1f);
        fadeOut.setDuration(1500);
        fadeOut.setStartDelay(500);
        fadeOut.start();
        fadeOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                error_ll.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }


}
