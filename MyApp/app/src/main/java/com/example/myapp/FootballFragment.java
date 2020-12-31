package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


public class FootballFragment extends Fragment {

    private ListView list;
    public static ArrayList<Video> videos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_football, container, false);

        list = view.findViewById(R.id.footballVideos);
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
