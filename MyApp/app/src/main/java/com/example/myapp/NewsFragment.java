package com.example.myapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;


public class NewsFragment extends Fragment {

    private ListView list;
    private static ArrayList<Video> videos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        loadVideos();
        list = view.findViewById(R.id.newVideos);
        VideoListViewAdapter adapter=new VideoListViewAdapter(getActivity(), videos);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                PlayerActivity.videoPath = videos.get(position).getUrl();
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.transition.fadein,R.transition.fadeout);
            }

        });

        return view;
    }

    public void loadVideos() {

        if(!videos.isEmpty()) return;

        videos.addAll(TennisFragment.videos);
        videos.addAll(FootballFragment.videos);

        HashSet<Video> set = new HashSet<>();
        set.addAll(videos);
        videos = new ArrayList<>(set);
    }
}
