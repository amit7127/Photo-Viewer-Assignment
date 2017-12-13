package com.example.amit.photoview;

import android.content.Context;
import android.content.SharedPreferences;

import io.realm.Realm;

/**
 * Created by Amit on 12-12-2017.
 */

public class PhotoView {

    private static PhotoView photoView;

    private PhotoView() {
    }

    public static PhotoView getPhotoView() {
        if (photoView == null) {
            photoView = new PhotoView();
        }
        return photoView;
    }

    public Realm getRealmDB(Context context) {
        Realm.init(context);
        return Realm.getDefaultInstance();
    }

    public SharedPreferences getSharedPreference(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("Login Data", Context.MODE_PRIVATE);
        return sharedpreferences;
    }

}
