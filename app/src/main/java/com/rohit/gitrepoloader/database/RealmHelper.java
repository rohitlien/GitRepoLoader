package com.rohit.gitrepoloader.database;

import com.rohit.gitrepoloader.models.RepoInfoDetail;
import com.rohit.gitrepoloader.models.UserData;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmHelper {

    public static void setDefaultConfig() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("notely.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public static void addUserData(final UserData userData) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(userData);
            }
        });
        realm.close();
    }
    public static void addRepoInfoData(final RepoInfoDetail repoInfoDetail) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(repoInfoDetail);
            }
        });
        realm.close();
    }

    public static ArrayList<RepoInfoDetail> getAllRepoList(String username) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RepoInfoDetail> results = realm.where(RepoInfoDetail.class).equalTo("loginName",username).findAll();
        if (results.size() > 0) {
            ArrayList<RepoInfoDetail> repoInfoDetails = new ArrayList<>();
            for (RepoInfoDetail repoInfoDetail : results) {
                repoInfoDetails.add(repoInfoDetail);
            }
            return repoInfoDetails;
        } else
            return null;
    }

    public static boolean isUserExist(String userName) {
        //ta
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<UserData> query = realm.where(UserData.class).equalTo("loginName", userName);
        RealmResults<UserData> results = query.findAll();
        if (results.size() > 0) {
            return true;
        } else
            return false;
    }

    public static UserData getUser(String userName) {
        //ta
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<UserData> query = realm.where(UserData.class).equalTo("loginName", userName);
        RealmResults<UserData> results = query.findAll();
        if (results.size() > 0) {
            return results.first();
        } else
            return null;
    }
    public static RepoInfoDetail getRepoInfo(int id) {
        //ta
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<RepoInfoDetail> query = realm.where(RepoInfoDetail.class).equalTo("id", id);
        RealmResults<RepoInfoDetail> results = query.findAll();
        if (results.size() > 0) {
            return results.first();
        } else
            return null;
    }

}