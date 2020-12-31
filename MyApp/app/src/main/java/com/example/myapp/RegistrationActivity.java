package com.example.myapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.regex.Pattern;

public class RegistrationActivity extends CustomAppCompatActivity {

    //To get information about the user
    String web = "http://www.uamvirtual.es/antonio/app/User/get_by_email.php?email=";

    //Empty query result
    String NOT_EMPTY = "1";

    //Not empty query result
    String EMPTY = "2";

    //Instance of the activity used in CheckEmailWebService.postExecute method
    RegistrationActivity ACTIVITY_INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DeviceInfo.TYPE == DeviceType.MOBILE) {
            setContentView(R.layout.activity_registration);
        } else {
            setContentView(R.layout.activity_registration_tablet);
        }
        ACTIVITY_INSTANCE = this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.fadein,R.transition.fadeout);
    }

    //next button behaviour
    public void nextButton(View view) {
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);

        if (!isValidAddress(email.getText().toString())) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.invalid_email_address),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPassword(password.getText().toString())) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.enter_a_valid_password),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        User.email = email.getText().toString();
        User.password = password.getText().toString();

        //Check if the email is already in used
        CheckEmailWebService checkEmailWebService = new CheckEmailWebService();
        checkEmailWebService.execute();
    }

    public boolean isValidAddress(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public boolean isValidPassword(String password) {
        //Could be more restirictive in the future
        if(password.isEmpty()) return false;
        return true;
    }

    //Connect with the database and check if the email is already in use
    public class CheckEmailWebService extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            URL url;
            String ret = "";

            try {
                url = new URL(web+User.email);
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
                        ret = getResources().getString(R.string.that_email_address_is_already_in_use);
                    }
                    else if (state.equals(EMPTY)){
                        ret = getResources().getString(R.string.that_email_address_is_not_in_use);
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
            if(s.equals(getResources().getString(R.string.that_email_address_is_not_in_use))){
                Intent intent = new Intent(ACTIVITY_INSTANCE, Registration2Activity.class);
                startActivity(intent);
                overridePendingTransition(R.transition.slide_left_in,R.transition.slide_left_out);
            } else {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
