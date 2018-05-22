package com.rohit.gitrepoloader.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rohit.gitrepoloader.R;
import com.rohit.gitrepoloader.database.RealmHelper;
import com.rohit.gitrepoloader.models.UserData;
import com.rohit.gitrepoloader.utils.GitLoaderTools;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends Activity {

    private ImageView closeBtn,user_avatar;
    private TextView userName,user_html_url,following,followers;
    private LinearLayout get_repo_ll;
    private String loginName;
    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initViews();
    }

    private void initViews() {
        closeBtn=(ImageView)findViewById(R.id.closeBtn);
        user_avatar=(ImageView)findViewById(R.id.user_avatar);
        userName=(TextView)findViewById(R.id.userName);
        user_html_url=(TextView)findViewById(R.id.user_html_url);
        following=(TextView)findViewById(R.id.following);
        followers=(TextView)findViewById(R.id.followers);
        get_repo_ll=(LinearLayout)findViewById(R.id.get_repo_ll);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(getIntent()!=null){
            loginName=getIntent().getStringExtra("username");
            userData= RealmHelper.getUser(loginName);
            if(userData==null){
                GitLoaderTools.showToast("Something went wrong !");
                finish();
                return;
            }
        }
        if(loginName==null || loginName.isEmpty()){
            GitLoaderTools.showToast("Something went wrong !");
            finish();
            return;
        }

        setData();

        get_repo_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(UserProfileActivity.this,UserRepoListActivity.class);
                intent.putExtra("username",loginName);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setData() {
        Picasso.with(this).load(userData.getAvatar()).into(user_avatar);
        userName.setText(""+userData.getName());
        user_html_url.setText(""+userData.getHtml_url());
        followers.setText("Followers : "+userData.getFollowers());
        following.setText("Following : "+userData.getFollowing());
    }
}
