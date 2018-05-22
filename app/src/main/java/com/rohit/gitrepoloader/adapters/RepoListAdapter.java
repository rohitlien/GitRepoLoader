package com.rohit.gitrepoloader.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rohit.gitrepoloader.R;
import com.rohit.gitrepoloader.database.GitPreferences;
import com.rohit.gitrepoloader.interfaces.InfoLoaderListener;
import com.rohit.gitrepoloader.interfaces.PageLoaderListener;
import com.rohit.gitrepoloader.models.RepoInfoDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by oust on 5/21/18.
 */

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<RepoInfoDetail> repoInfoDetails;
    private PageLoaderListener pageLoaderListener;
    private String userName;
    private InfoLoaderListener infoLoaderListener;

    public RepoListAdapter(Context context, ArrayList<RepoInfoDetail> repoInfoDetails, PageLoaderListener pageLoaderListener
                            , InfoLoaderListener infoLoaderListener) {
        this.context = context;
        this.repoInfoDetails = repoInfoDetails;
        this.pageLoaderListener=pageLoaderListener;
        this.infoLoaderListener=infoLoaderListener;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.repo_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try{
            holder.repo_name.setText(""+repoInfoDetails.get(position).getName());
            //String htmlString="<u>"+repoInfoDetails.get(position).getRepo_html_url()+"</u>";
            holder.repo_html_url.setText(repoInfoDetails.get(position).getRepo_html_url());
            holder.repo_html_url.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

            holder.repo_html_url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(infoLoaderListener!=null){
                        infoLoaderListener.openWebUrl(repoInfoDetails.get(position).getRepo_html_url());
                    }
                }
            });
            holder.details_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    infoLoaderListener.openRepoDetails(repoInfoDetails.get(position));
                }
            });
            Picasso.with(context).load(repoInfoDetails.get(position).getAvatar_url()).into(holder.user_avatar);
            holder.size.setText(""+repoInfoDetails.get(position).getSize());
            holder.watchers.setText(""+repoInfoDetails.get(position).getWatchers_count());
            holder.open_issue_count.setText(""+repoInfoDetails.get(position).getOpen_issues_count());

            if(position==(repoInfoDetails.size()-1)){
                if(pageLoaderListener!=null){
                    //if(!GitPreferences.getSavedBoolean(userName+"dataLoaded")){
                        pageLoaderListener.getNextData();
                    //}
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return repoInfoDetails.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        ImageView user_avatar;
        TextView repo_name,repo_html_url,open_issue_count,size,watchers;
        LinearLayout details_ll;
        public MyViewHolder(View itemView) {
            super(itemView);
            user_avatar=(ImageView)itemView.findViewById(R.id.user_avatar);
            repo_name=(TextView)itemView.findViewById(R.id.repo_name);
            repo_html_url=(TextView)itemView.findViewById(R.id.repo_html_url);
            open_issue_count=(TextView)itemView.findViewById(R.id.open_issue_count);
            size=(TextView)itemView.findViewById(R.id.size);
            watchers=(TextView)itemView.findViewById(R.id.watchers);
            details_ll=(LinearLayout)itemView.findViewById(R.id.detail_ll);
        }
    }

    public void notifyChanges(ArrayList<RepoInfoDetail> repoInfoDetails){
        this.repoInfoDetails=repoInfoDetails;
        notifyDataSetChanged();
    }
}
