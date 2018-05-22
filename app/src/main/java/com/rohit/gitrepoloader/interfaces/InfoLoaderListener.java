package com.rohit.gitrepoloader.interfaces;

import com.rohit.gitrepoloader.models.RepoInfoDetail;

/**
 * Created by oust on 5/21/18.
 */

public interface InfoLoaderListener {
    public void openWebUrl(String url);
    public void openRepoDetails(RepoInfoDetail repoInfoDetail);
}
