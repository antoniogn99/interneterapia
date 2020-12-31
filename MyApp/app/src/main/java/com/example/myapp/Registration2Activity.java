package com.example.myapp;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.icu.util.Calendar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


public class Registration2Activity extends CustomAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DeviceInfo.TYPE == DeviceType.MOBILE) {
            setContentView(R.layout.activity_registration2);
        } else {
            setContentView(R.layout.activity_registration2_tablet);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.slide_right_in,R.transition.slide_right_out);
    }

    //done button behaviour
    public void doneButton(View view) {
        TextView birthdate = (TextView) findViewById(R.id.birthdate);
        TextView gender = (TextView) findViewById(R.id.gender);

        if(birthdate.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.enter_your_date_of_birth),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(gender.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.enter_your_gender),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, Registration3Activity.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_left_in,R.transition.slide_left_out);
    }

    //gender TextView behaviour
    public void genderTextView(View view) {
        showGenderPickerDialog();
    }

    public void showGenderPickerDialog() {
        GenderPickerFragment newFragment = new GenderPickerFragment();
        newFragment.show(getSupportFragmentManager(),"genderPicker");
    }

    public static class GenderPickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.gender))
                    .setItems(R.array.gender_array,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            TextView gender = (TextView) getActivity().findViewById(R.id.gender);
                            switch(which) {
                                case 0:
                                    gender.setText(R.string.male);
                                    User.gender = getResources().getString(R.string.gender_db_format_male);
                                    break;
                                case 1:
                                    gender.setText(R.string.female);
                                    User.gender = getResources().getString(R.string.gender_db_format_female);
                                    break;
                                case 2:
                                    gender.setText(R.string.non_binary);
                                    User.gender = getResources().getString(R.string.gender_db_format_nonbinary);
                                    break;
                                default:

                            }
                        }
                    });
            return builder.create();
        }
    }

    //birthdate TextView behaviour
    public void birthdateTextView(final View view) {
        showDatePickerDialog();
    }

    public void showDatePickerDialog(){
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            /*final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);*/

            int year = 2000;
            int month = 0;
            int day = 1;

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), 3, this, year, month, day);
            //return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView birthdate = (TextView) getActivity().findViewById(R.id.birthdate);
            birthdate.setText(localDateFormat(year,month+1,day));
            User.birthdate = dbDateFormat(year,month+1,day);
        }

        public String localDateFormat(int year, int month, int day) {
            String ret = "";
            ret += day <= 9 ? "0"+day : day;
            ret += "/";
            ret += month <= 9 ? "0"+month : month;
            ret += "/";
            ret += year;
            return ret;
        }

        public String dbDateFormat(int year, int month, int day) {
            String ret = "";
            ret += year;
            ret += "/";
            ret += month <= 9 ? "0"+month : month;
            ret += "/";
            ret += day <= 9 ? "0"+day : day;
            return ret;
        }
    }
}
