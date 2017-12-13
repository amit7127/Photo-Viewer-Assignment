package com.example.amit.photoview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.amit.photoview.models.ImageDetails;
import com.example.amit.photoview.models.ImageList;

import java.util.List;

import io.realm.Realm;

public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Realm realm = PhotoView.getPhotoView().getRealmDB(SplashScreen.this);
        List<ImageDetails> detailsList = realm.where(ImageDetails.class).findAll();
        if (detailsList.size() < 1){
            new ImageList().setDemoDataToDB(SplashScreen.this);
        }

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                SharedPreferences sharedPreferences = PhotoView.getPhotoView().getSharedPreference(getApplicationContext());
                boolean isLoggedIn = sharedPreferences.getBoolean(getString(R.string.is_user_loggedin), false);
                if (!isLoggedIn) {

                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                } else {
                    String name = sharedPreferences.getString(getString(R.string.userName), null);
                    String mail = sharedPreferences.getString(getString(R.string.userMail), null);
                    Intent intent = new Intent(getApplicationContext(), Gallery.class);
                    intent.putExtra("name", name);
                    intent.putExtra("email", mail);
                    startActivity(intent);
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
