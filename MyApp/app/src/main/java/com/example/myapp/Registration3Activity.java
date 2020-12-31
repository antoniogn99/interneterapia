package com.example.myapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Registration3Activity extends CustomAppCompatActivity {

    //To get information about users
    String web1 = "http://www.uamvirtual.es/antonio/app/User/get_by_username.php?username=";
    String web2 = "http://www.uamvirtual.es/antonio/app/User/insert.php";

    //Empty query result
    String NOT_EMPTY = "1";

    //Not empty query result
    String EMPTY = "2";

    //Intertion query success
    String INSERTION_SUCCESS = "1";

    //Insertion query failed
    String INSERTION_FAILED= "2";

    //Instance of the activity used in InsertWebService.postExecute method
    Registration3Activity ACTIVITY_INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DeviceInfo.TYPE == DeviceType.MOBILE) {
            setContentView(R.layout.activity_registration3);
        } else {
            setContentView(R.layout.activity_registration3_tablet);
        }
        ACTIVITY_INSTANCE = this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.slide_right_in,R.transition.slide_right_out);
    }

    //save button behaviour
    public void saveButton(View view) {
        EditText username = (EditText) findViewById(R.id.username);
        User.username = username.getText().toString();

        CheckUsernameWebService checkUsernameWebService = new CheckUsernameWebService();
        checkUsernameWebService.execute();
    }

    //Connect with the database and check if the username is already in use
    //If not, insert the new user in the database using InsertWebService
    public class CheckUsernameWebService extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            URL url;
            String ret = "";

            try {
                url = new URL(web1+User.username);
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
                        ret = getResources().getString(R.string.that_username_is_already_in_use);
                    }
                    else if (state.equals(EMPTY)){
                        ret = getResources().getString(R.string.that_username_is_not_in_use);
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
            if(s.equals(getResources().getString(R.string.that_username_is_not_in_use))){
                InsertWebService insertWebService = new InsertWebService();
                insertWebService.execute(User.username, User.password, User.email, User.birthdate, User.gender);
            } else {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        }

    }

    //Connect with the database and insert a new user
    public class InsertWebService extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            URL url;
            String ret = "";

            try {
                HttpURLConnection urlConn;
                url = new URL(web2);
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.setRequestProperty("Accept", "application/json");
                urlConn.connect();

                //Create the JSONObject
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("username", params[0]);
                jsonParam.put("password", params[1]);
                jsonParam.put("email", params[2]);
                jsonParam.put("birthdate", params[3]);
                jsonParam.put("gender", params[4]);

                //Send the POST parameters
                OutputStream os = urlConn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonParam.toString());
                writer.flush();
                writer.close();

                int responseCode = urlConn.getResponseCode();
                StringBuilder result = new StringBuilder();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    //Read the data
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        //Save the data in "result" variable
                        result.append(line);
                    }

                    //Create a JSONObject using "result"
                    JSONObject json = new JSONObject(result.toString());

                    //Get the state
                    String state = json.getString("state");

                    if (state.equals(INSERTION_SUCCESS)) {
                        ret = getResources().getString(R.string.registration_finished);

                    } else if (state.equals(INSERTION_FAILED)) {
                        ret = ret = getResources().getString(R.string.registration_failed);
                    }

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return ret;

        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals(getResources().getString(R.string.registration_finished))){
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ACTIVITY_INSTANCE, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.transition.slide_left_in,R.transition.slide_left_out);
            } else {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
