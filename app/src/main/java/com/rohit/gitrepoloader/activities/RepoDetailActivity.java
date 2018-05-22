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
import com.rohit.gitrepoloader.models.RepoInfoDetail;
import com.squareup.picasso.Picasso;

public class RepoDetailActivity extends Activity {

    private ImageView user_avatar,img_close;
    private TextView repo_name,repo_description;
    private LinearLayout see_button;
    private RepoInfoDetail repoInfoDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);

        initViews();
    }

    private void initViews() {
        user_avatar = (ImageView)findViewById(R.id.user_avatar);
        repo_name = (TextView)findViewById(R.id.repo_name);
        repo_description=(TextView)findViewById(R.id.repo_description);
        see_button =(LinearLayout)findViewById(R.id.see_button);
        img_close=(ImageView)findViewById(R.id.img_close);
        if(getIntent()!=null){
            int id=getIntent().getIntExtra("id",0);
            repoInfoDetail= RealmHelper.getRepoInfo(id);
            if(repoInfoDetail!=null){
                setData();
            }
        }

    }

    private void setData() {
        Picasso.with(this).load(repoInfoDetail.getAvatar_url()).into(user_avatar);
        repo_name.setText(""+repoInfoDetail.getFull_name());
        repo_description.setText(""+repoInfoDetail.getDescription());

        see_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RepoDetailActivity.this,RepoWebActivity.class);
                intent.putExtra("url",repoInfoDetail.getRepo_html_url());
                startActivity(intent);
                finish();
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
