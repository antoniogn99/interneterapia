package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class TennisFragment extends Fragment {

    private ListView list;
    public static ArrayList<Video> videos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tennis, container, false);

        list = view.findViewById(R.id.tennisVideos);
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

}
