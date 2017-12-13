package com.example.amit.photoview;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amit.photoview.models.ImageDetails;
import com.example.amit.photoview.models.ImageList;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import io.realm.Realm;

public class Gallery extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    ImageDetailsListAdapter adapter;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.username_textview);
        TextView emailId = headerView.findViewById(R.id.email_textview);
        userName.setText(getIntent().getStringExtra("name"));
        emailId.setText(getIntent().getStringExtra("email"));

        realm = PhotoView.getPhotoView().getRealmDB(Gallery.this);

        recyclerView = findViewById(R.id.recycler_view);

        final List<ImageDetails> detailsList = realm.where(ImageDetails.class).findAll();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new ImageDetailsListAdapter(detailsList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(Gallery.this, detailsList.get(position).getImageId(), Toast.LENGTH_SHORT).show();
                        Log.i("Item",detailsList.get(position).getImageId() + "");

                        // Create custom dialog object
                        final Dialog dialog = new Dialog(Gallery.this);
                        // Include dialog.xml file
                        dialog.setContentView(R.layout.gallery_details_dialog);
                        // Set dialog title
                        dialog.setTitle("Custom Dialog");

                        ImageView imageView = dialog.findViewById(R.id.image_dialog);
                        ProgressBar progressBar = dialog.findViewById(R.id.progress_dialog);
                        TextView  imageDetails = dialog.findViewById(R.id.image_details_dialog);
                        Button directionButton = dialog.findViewById(R.id.direction_dialog);
                        LikeButton likeButton = dialog.findViewById(R.id.like_dialog);

                        final ImageDetails details = detailsList.get(position);
                        new ImageLoadTask(details.getImageSource(), imageView, progressBar).execute();
                        imageDetails.setText(details.getImageDetails());
                        likeButton.setLiked(details.isFevourite());

                        likeButton.setOnLikeListener(new OnLikeListener() {
                            @Override
                            public void liked(LikeButton likeButton) {
                                realm.beginTransaction();
                                ImageDetails details1 = realm.where(ImageDetails.class).equalTo("imageId", details.getImageId()).findFirst();
                                details1.setFevourite(likeButton.isLiked());
                                realm.commitTransaction();
                            }

                            @Override
                            public void unLiked(LikeButton likeButton) {
                                realm.beginTransaction();
                                ImageDetails details1 = realm.where(ImageDetails.class).equalTo("imageId", details.getImageId()).findFirst();
                                details1.setFevourite(likeButton.isLiked());
                                realm.commitTransaction();
                            }
                        });

                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                adapter.notifyDataSetChanged();
                            }
                        });

                        directionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                // Create custom dialog object
                                final Dialog mapDialog = new Dialog(Gallery.this);
                                // Include dialog.xml file
                                mapDialog.setContentView(R.layout.map_dialog);
                                TextView mapText = mapDialog.findViewById(R.id.map_text);
                                TextView goToMap = mapDialog.findViewById(R.id.go_to_map_text);
                                mapText.setText(details.getAddress());
                                goToMap.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String map = "http://maps.google.co.in/maps?q=" + details.getAddress();
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                                        startActivity(intent);
                                    }
                                });
                                mapDialog.show();

                            }
                        });

                        dialog.show();


                    }
                })
        );


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_list) {
            // Handle the camera action
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        } else if (id == R.id.nav_grid) {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_menu:
                SharedPreferences sharedPreferences = PhotoView.getPhotoView().getSharedPreference(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(getString(R.string.is_user_loggedin), false); // Storing boolean - true/false
                editor.putString(getString(R.string.userName), ""); // Storing string
                editor.putString(getString(R.string.userMail), ""); // Storing integer
                editor.commit();

                Intent intent = new Intent(Gallery.this, MainActivity.class);
                startActivity(intent);
                finish();
                //Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    boolean doubleBackToExitPressedOnce = false;


}
