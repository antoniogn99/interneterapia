package com.example.myapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class VideoListViewAdapter extends ArrayAdapter<Video> {

    private final Activity context;
    private final ArrayList<Video> videos;

    public VideoListViewAdapter(Activity context, ArrayList<Video> videos) {
        super(context, R.layout.video_list_view_item,videos);

        this.context=context;
        this.videos=videos;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView;

        if (DeviceInfo.TYPE == DeviceType.MOBILE) {
            rowView=inflater.inflate(R.layout.video_list_view_item, null,true);
        } else {
            rowView=inflater.inflate(R.layout.video_list_view_item_tablet, null,true);
        }

        TextView titleText = rowView.findViewById(R.id.title);
        TextView subtitleText = rowView.findViewById(R.id.subtitle);
        ImageView thumbnail = rowView.findViewById(R.id.thumbnail);

        titleText.setText(videos.get(position).getTitle());
        subtitleText.setText(videos.get(position).getDescriptionn());
        thumbnail.setImageResource(R.drawable.default_image);
        new DownloadImageTask(thumbnail).execute(videos.get(position).getThumbnail());

        return rowView;

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}