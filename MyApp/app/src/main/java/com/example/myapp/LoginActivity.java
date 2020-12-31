package com.example.myapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import java.util.HashMap;

public class LoginActivity extends CustomAppCompatActivity {

    //To get information about the user
    String get_by_username_web = "http://www.uamvirtual.es/antonio/app/User/get_by_username.php?username=";

    //To get information about the videos
    String videos_web = "http://www.uamvirtual.es/antonio/app/Video/get_all.php";

    //To get the tags of a video
    String get_by_id_web = "http://www.uamvirtual.es/antonio/app/Appears/get_by_id.php?idvideo=";

    //Empty query result
    String NOT_EMPTY = "1";

    //Not empty query result
    String EMPTY = "2";

    //Instance of the activity used in LoginWebService.postExecute method
    LoginActivity ACTIVITY_INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DeviceInfo.TYPE == DeviceType.MOBILE) {
            setContentView(R.layout.activity_login);
        } else {
            setContentView(R.layout.activity_login_tablet);
        }
        ACTIVITY_INSTANCE = this;
        EditText username = findViewById(R.id.login_username);
        EditText password = findViewById(R.id.login_password);
        username.setText(User.username);
        password.setText(User.password);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.fadein,R.transition.fadeout);
    }

    //logIn button behaviour
    public void logInButton(View view) {
        EditText username = findViewById(R.id.login_username);
        EditText password = findViewById(R.id.login_password);
        User.username = username.getText().toString();
        User.password = password.getText().toString();

        LoginWebService loginWebService = new LoginWebService();
        loginWebService.execute();
    }

    //Connect with the database and check if username and password are right
    public class LoginWebService extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            URL url;
            String ret = "";

            try {
                url = new URL(get_by_username_web+User.username);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                        " (Linux; Android 1.5; es-ES) Ejemplo HTTP");

                int responseCode = connection.getResponseCode();
                StringBuilder result = new StringBuilder();

                if (responseCode == HttpURLConnection.HTTP_OK){


                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    //Read the data
                    String line;
                    while ((line = reader.readLine()) != null) {
                        //Save the data in "result" variable
                        result.append(line);
                    }

                    //Create a JSONObject using "result"
                    JSONObject json = new JSONObject(result.toString());

                    //Get the state
                    String state = json.getString("state");

                    if (state.equals(NOT_EMPTY)){
                        String password = json.getJSONObject("user").getString("password");
                        if(password.equals(User.password)) {
                            ret = getResources().getString(R.string.login_finished);
                        } else {
                            ret = getResources().getString(R.string.wrong_password_or_username);
                        }

                    }
                    else if (state.equals(EMPTY)){
                        ret = getResources().getString(R.string.wrong_password_or_username);
                    }

                }
            } catch (MalformedURLException e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }

            return ret;

        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals(getResources().getString(R.string.login_finished))){
                LoadVideosWebService loadVideosWebService = new LoadVideosWebService();
                loadVideosWebService.execute();
            } else {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Connect with the database and get videos info
    public class LoadVideosWebService extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            URL url;
            String ret = "";

            //Modify the map if more sports are added
            HashMap<String,ArrayList<Video>> map = new HashMap<>();
            map.put("TENNIS",TennisFragment.videos);
            map.put("FOOTBALL",FootballFragment.videos);

            //If the videos have already been loaded, return
            if (TennisFragment.videos.isEmpty() == false) return ret;


            //Get the videos
            try {
                url = new URL(videos_web);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                        " (Linux; Android 1.5; es-ES) Ejemplo HTTP");

                int responseCode = connection.getResponseCode();
                StringBuilder result = new StringBuilder();

                if (responseCode == HttpURLConnection.HTTP_OK){


                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    //Read the data
                    String line;
                    while ((line = reader.readLine()) != null) {
                        //Save the data in "result" variable
                        result.append(line);
                    }

                    //Create a JSONObject using "result"
                    JSONObject json = new JSONObject(result.toString());

                    //Get the state
                    String state = json.getString("state");

                    if (state.equals(NOT_EMPTY)){
                        JSONArray videos = json.getJSONArray("videos");
                        String idvideo;
                        String title;
                        String description;
                        String videoUrl;
                        String thumbnail;
                        for(int i=0;i<videos.length();i++){

                            //Create the video
                            idvideo = videos.getJSONObject(i).getString("idvideo");
                            title = videos.getJSONObject(i).getString("title");
                            description = videos.getJSONObject(i).getString("description");
                            videoUrl = videos.getJSONObject(i).getString("url");
                            thumbnail = videos.getJSONObject(i).getString("thumbnail");
                            Video video = new Video(title,description,videoUrl,thumbnail);












                            //Get the tags of the video that indicates where it has to appear
                            try {
                                url = new URL(get_by_id_web + idvideo);
                                connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                                        " (Linux; Android 1.5; es-ES) Ejemplo HTTP");

                                responseCode = connection.getResponseCode();
                                result = new StringBuilder();

                                if (responseCode == HttpURLConnection.HTTP_OK){


                                    in = new BufferedInputStream(connection.getInputStream());
                                    reader = new BufferedReader(new InputStreamReader(in));

                                    //Read the data
                                    while ((line = reader.readLine()) != null) {
                                        //Save the data in "result" variable
                                        result.append(line);
                                    }

                                    //Create a JSONObject using "result"
                                    json = new JSONObject(result.toString());

                                    //Get the state
                                    String state2 = json.getString("state");

                                    if (state2.equals(NOT_EMPTY)){
                                        JSONArray appears = json.getJSONArray("appears");

                                        for(int j=0;j<appears.length();j++){
                                            String tag = appears.getJSONObject(j).getString("tag");

                                            //Add the video to the list associated with that tag
                                            map.get(tag).add(video);
                                        }

                                    }
                                    else if (state2.equals(EMPTY)){
                                        return getResources().getString(R.string.error_loading_data);
                                    }

                                }
                            } catch (MalformedURLException e) {
                                System.out.println(e);
                            } catch (IOException e) {
                                System.out.println(e);
                            } catch (JSONException e) {
                                System.out.println(e);
                            }











                        }

                    }
                    else if (state.equals(EMPTY)){
                        return getResources().getString(R.string.error_loading_data);
                    }

                }
            } catch (MalformedURLException e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }

            return ret;

        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals(getResources().getString(R.string.error_loading_data))) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ACTIVITY_INSTANCE, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.transition.fadein, R.transition.fadeout);
            }
        }
    }

}
