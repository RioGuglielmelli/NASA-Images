package com.example.nasa_images;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {
    private final String API_URL = "https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d";
    private TextView selectedDateText;
    private Calendar selectedDate;
    String nasaDate;
    String nasaURL;
    String nasaHDURL;
    String nasaTitle;
    String nasaExplanation;

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
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String selectedDateString = dateFormat.format(selectedDate.getTime());
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            Log.d("User Date on Retrieve", "value is: " + selectedDate);
            editor.putString("date", selectedDateString);
            editor.putString("title", nasaTitle);
            editor.putString("url", nasaURL);
            editor.putString("explanation", nasaExplanation);
            editor.putString("hdurl", nasaHDURL);
            editor.commit();
            System.out.println(selectedDateString + nasaTitle);
            startActivity(intent);
        });
    }
    public class NasaAPI extends AsyncTask<String, Integer, Bitmap> {

        Bitmap nasaPic;

        @Override
        protected Bitmap doInBackground(String... urls) {
            NASAObject object = new NASAObject();

            publishProgress(0);

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

                nasaTitle = json.getString("title");
                nasaExplanation = json.getString("explanation");
                nasaURL = json.getString("url");
                nasaHDURL = json.getString("hdurl");
                nasaDate = json.getString("date");
                URL nasaPicURL = new URL(nasaURL);
                response.close();

                BufferedInputStream bis = new BufferedInputStream(nasaPicURL.openStream());
                nasaPic = BitmapFactory.decodeStream(bis);
                File path = Environment.getExternalStorageDirectory();
                File dir = new File(path.getAbsolutePath() + "/Pictures");
                dir.mkdir();
                Log.d("File Path", "File Path is: " + dir);
                File nasaFile = new File(dir, nasaDate + ".png");
                if (nasaFile.exists()) {
                    FileOutputStream outputStream = new FileOutputStream(nasaFile);
                    nasaPic.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    nasaPic = BitmapFactory.decodeFile(path.getAbsolutePath());
                    outputStream.flush();
                    outputStream.close();
                }
                bis.close();

                for (int i = 0; i < 100; i++) {
                    try {
                        publishProgress(i);
                        Thread.sleep(30);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return nasaPic;
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



        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(NASAObject object) {


        }

    }

