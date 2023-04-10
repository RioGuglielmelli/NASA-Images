package com.example.nasa_images;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.DialogFragment;

        import android.annotation.SuppressLint;
        import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;

public class MainActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {
    private final String API_URL = "https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d";
    private TextView selectedDateText;
    private Calendar selectedDate;
    private NasaObject currentObject;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectedDateText = findViewById(R.id.selectedDate);
        Button searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener((v) -> {
            NasaAPI req = new NasaAPI();
            System.out.println(String.format("%s&date=%d-%d-%d", API_URL, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE)));
            req.execute(String.format("%s&date=%d-%d-%d", API_URL, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE)));
        });
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

    public class NasaAPI extends AsyncTask<String, Integer, NasaObject> {
        @Override
        protected NasaObject doInBackground(String... urls) {
            NasaObject object = new NasaObject();

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

                object.setTitle(json.getString("title"));
                object.setExplanation(json.getString("explanation"));
                object.setUrl(json.getString("url"));
                object.setHdUrl(json.getString("hdurl"));
                object.setDate(new Date(json.getString("date")));

                currentObject = object;
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            return object;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(NasaObject object) {

        }

    }

}