package com.example.nasa_images;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**

 A Fragment that displays details of a selected NASA Image and allows the user to save the image.
 This Fragment is called by the DetailsActivity and receives data from the MainActivity through a Bundle.
 The Fragment displays the title, explanation, date, URL, HDURL and image of the selected NASA Image.
 The user can save the image to a local database by clicking the savePhoto button.
 The Fragment contains an AsyncTask that downloads the image in the background and sets it to the imageView.
 The Fragment extends the Fragment class and overrides its onCreateView() and onAttach() methods.
 The Fragment contains a private method insertData() that inserts data into a SQLite database.
 */

public class DetailsFragment extends Fragment {
    private Bundle dataFromActvity;
    private AppCompatActivity parentActivity;
    ImageView imageView;
    SQLiteDatabase db;
    Button savePhoto;
    String title;
    String date;
    String explanation;
    String url;
    String hdUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActvity = getArguments();

        assert dataFromActvity != null;

        String title = dataFromActvity.getString("TITLE");
        String explanation = dataFromActvity.getString("EXPLANATION");
        String url = dataFromActvity.getString("URL");
        String hdUrl = dataFromActvity.getString("HDURL");
        String date = dataFromActvity.getString("DATE");

        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_details, container, false);

        TextView titleView = result.findViewById(R.id.title_text);
        titleView.setText( title );

        TextView sectionNameView = result.findViewById(R.id.explanation_text);
        sectionNameView.setText( explanation );

        TextView dateView = result.findViewById(R.id.date_text);

        dateView.setText( date );

        TextView urlView = result.findViewById(R.id.url_text);
        urlView.setText( url );

        TextView hdUrlView = result.findViewById(R.id.hdurl_text);
        hdUrlView.setText( hdUrl );

        imageView = result.findViewById(R.id.image_view);

        ImageDownloader imageDownloader = new ImageDownloader();

        imageDownloader.execute(url);
        savePhoto = result.findViewById(R.id.savePhoto);
        savePhoto.setOnClickListener(view1 -> {
            long returnC = insertData(title, explanation, date, url, hdUrl);
            if (returnC == -1) {
                Toast.makeText(getContext(), "Error:Your NASA Image was NOT saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Hooray,Your NASA Image was saved!", Toast.LENGTH_SHORT).show();
            }
        });

        return result;
    }

    public class ImageDownloader extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            URL urlObject = null;
            try {
                urlObject = new URL(urls[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bmp = null;
            try {
                assert urlObject != null;
                bmp = BitmapFactory.decodeStream(urlObject.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        parentActivity = (AppCompatActivity) context;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        }

    private long insertData(String title, String explanation, String date, String url, String hdUrl) {
        Opener dbopener = new Opener(getContext());
        db = dbopener.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(dbopener.COL_DATE, date);
        cValues.put(dbopener.COL_EXPLANATION, explanation);
        cValues.put(dbopener.COL_TITLE, title);
        cValues.put(dbopener.COL_URL, url);
        cValues.put(dbopener.COL_URL, hdUrl);

        Log.d("Values inserted into DB", "values are " + date + title + explanation + url + hdUrl);
        return db.insert(dbopener.TABLE_NAME, null, cValues);
    }




}

