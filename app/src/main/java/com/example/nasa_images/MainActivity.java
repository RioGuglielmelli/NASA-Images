package com.example.nasa_images;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
/**

 This class represents the main activity of the app, which allows the user to select a date and search for NASA's Astronomy Picture of the Day for that date.
 It contains an inner class NasaAPI, which extends AsyncTask and uses NASA's API to retrieve data about the selected date's picture.
 It also contains an inner class DatePickerFragment, which displays a date picker dialog when the user clicks the "Select Date" button.
 The user can then click the "Search" button to retrieve data about the selected date's picture, and the app transitions to the DetailsActivity to display the retrieved data.
 Implements the onDateSet method from the DatePickerDialog.OnDateSetListener interface to retrieve the selected date from the date picker dialog.
 Extends the BaseActivity class to provide navigation drawer functionality.
 */

public class MainActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {
    private final String API_URL = "https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d";
    private TextView selectedDateText;
    private Calendar selectedDate;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectedDateText = findViewById(R.id.selectedDate);
        Button searchButton = findViewById(R.id.searchButton);
        NasaAPI api = new NasaAPI();


        searchButton.setOnClickListener((v) -> {
            NasaAPI req = new NasaAPI();
            System.out.println(String.format("%s&date=%d-%d-%d", API_URL, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE)));
            req.execute(String.format("%s&date=%d-%d-%d", API_URL, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE)));
        });
    }

    public class NasaAPI extends AsyncTask<String, Integer, NASAObject> {
        private ProgressBar progressBar = findViewById(R.id.progressBar);

        @Override
        protected NASAObject doInBackground(String... urls) {
            NASAObject object = new NASAObject();

            publishProgress(0);
            progressBar.setAlpha(1);

            try {
                //create a URL object of what server to contact:
                URL url = new URL(urls[0]);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //JSON reading:
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String result = sb.toString(); //result is the whole string

                // Convert string to JSON:
                JSONObject json = new JSONObject(result);

                String nasaTitle = json.getString("title");
                String nasaExplanation = json.getString("explanation");
                String nasaURL = json.getString("url");
                String nasaHDURL = json.getString("hdurl");
                String nasaDate = json.getString("date");

                object = new NASAObject(nasaTitle, nasaExplanation, nasaURL, nasaHDURL, nasaDate);

                for (int i = 0; i < 20; i++) {
                    try {
                        publishProgress(i * 5);
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            progressBar.setAlpha(0);

            return object;
        }

        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(NASAObject nasaObject) {

            Bundle dataToPass = new Bundle();
            dataToPass.putString("TITLE", nasaObject.getTitle());
            dataToPass.putString("EXPLANATION", nasaObject.getExplanation());
            dataToPass.putString("URL", nasaObject.getUrl());
            dataToPass.putString("HDURL", nasaObject.getHdUrl());
            dataToPass.putString("DATE", nasaObject.getDate());

            Intent nextActivity = new Intent(MainActivity.this, DetailsActivity.class);
            nextActivity.putExtras(dataToPass); //send data to next activity
            startActivity(nextActivity); //make the transition

        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();

        c.set(year, month, day);

        selectedDate = c;

        selectedDateText.setText(c.getTime().toGMTString());
    }

    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getActivity();
            return new DatePickerDialog(getActivity(), listener, year, month, day);
        }
    }
}
