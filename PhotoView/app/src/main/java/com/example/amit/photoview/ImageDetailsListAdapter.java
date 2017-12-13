package com.example.amit.photoview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.amit.photoview.models.ImageDetails;
import com.like.LikeButton;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Amit on 12-12-2017.
 */

public class ImageDetailsListAdapter extends RecyclerView.Adapter<ImageDetailsListAdapter.MyViewHolder> {
    private List<ImageDetails> imageDetails;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ImageDetails details = imageDetails.get(position);
        holder.imageDetailsTextView.setText(details.getImageDetails());
        //holder.image.setImageURI(Uri.parse(details.getImageSource()));
        new ImageLoadTask(details.getImageSource(), holder.image, holder.progressBar).execute();
        holder.likeButton.setLiked(details.isFevourite());
    }

    @Override
    public int getItemCount() {
        return imageDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView imageDetailsTextView;
        public ImageView image;
        public ProgressBar progressBar;
        public LikeButton likeButton;

        public MyViewHolder(View view) {
            super(view);
            imageDetailsTextView = (TextView) view.findViewById(R.id.img_details);
            image = (ImageView) view.findViewById(R.id.image_view);
            progressBar = view.findViewById(R.id.image_progress);
            likeButton = view.findViewById(R.id.like_button_item);
        }
    }

    public ImageDetailsListAdapter(List<ImageDetails> imageDetails) {
        this.imageDetails = imageDetails;
    }

}
