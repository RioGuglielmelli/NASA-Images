package com.example.nasa_images;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class NASAList extends BaseActivity {
    private ArrayList<NASAImages> images = new ArrayList<>();
    SQLiteDatabase db;
    myListAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_log);
        EditText l4ET = (EditText) findViewById(R.id.editTextL4);
        Button deB = (Button) findViewById(R.id.deleteButton);
        ListView l4List = findViewById(R.id.listViewL4);
        loadDataDB();
        l4List.setAdapter(myAdapter = new myListAdapter());
        myAdapter.setData(images);

        l4ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String filterText = toString().toLowerCase();
                ArrayList<NASAImages> filter = new ArrayList<>();
                if(!filterText.isEmpty()){
                    for (NASAImages filled : images){
                        if (filled.getNasaTitle().toLowerCase().contains(filterText)
                                || filled.getNasaDate().toLowerCase().contains(filterText)){
                            filter.add(filled);
                        }
                    }
                    myAdapter.setData(filter);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    class myListAdapter extends BaseAdapter {
        ArrayList<NASAImages> list = new ArrayList<>();

        public void setData(ArrayList<NASAImages> data) {
            list.clear();
            list.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public NASAImages getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {

            View newView = view;
            LayoutInflater inflater = getLayoutInflater();

            NASAImages thisRow = getItem(i);
            if(newView == null) {
                newView = inflater.inflate(R.layout.image_list, parent, false);
            }
            TextView newTxt1 = newView.findViewById(R.id.textView2);
            TextView newTxt2 = newView.findViewById(R.id.textView3);

            newTxt1.setText(thisRow.getNasaTitle());
            newTxt2.setText(thisRow.getNasaDate());

            return newView;
        }
    }
    private void loadDataDB() {
        //connect to database:
        Opener dbOpener = new Opener(this);
        db = dbOpener.getWritableDatabase();
        //calls to call columns listed
        String[] columns = {Opener.COL_ID, Opener.COL_TITLE, Opener.COL_EXPLANATION, Opener.COL_URL, Opener.COL_HDURL, Opener.COL_DATE};
        //query all results from database:
        Cursor results = db.query(false, Opener.TABLE_NAME, columns, null, null, null, null, null, null);
        int titleColIndex = results.getColumnIndex(Opener.COL_TITLE);
        int explanationColIndex = results.getColumnIndex(Opener.COL_EXPLANATION);
        int urlColIndex = results.getColumnIndex(Opener.COL_URL);
        int hdurlColIndex = results.getColumnIndex(Opener.COL_HDURL);
        int DATEColIndex = results.getColumnIndex(Opener.COL_DATE);
        int idColIndex = results.getColumnIndex(Opener.COL_ID);

        while (results.moveToNext()) {
            String Title = results.getString(titleColIndex);
            String Explanation = results.getString(explanationColIndex);
            String URL = results.getString(urlColIndex);
            String HDURL = results.getString(hdurlColIndex);
            String Date = results.getString(DATEColIndex);
            images.add(new NASAImages(Title,Explanation,URL,HDURL,Date));


        }




    }
    public class NASAImages {
        String nasaTitle;
        String nasaExplanation;
        String nasaURL;
        String nasaHDURL;
        String nasaDate;

        public NASAImages( String nasaTitle,String nasaExplanation, String nasaDate, String nasaHDURL, String nasaURL) {
            this.nasaTitle = nasaTitle;
            this.nasaExplanation = nasaExplanation;
            this.nasaDate = nasaDate;
            this.nasaHDURL = nasaHDURL;
            this.nasaURL = nasaURL;
        }

        public String getNasaTitle() {
            return nasaTitle;
        }

        public String getNasaDate() {
            return nasaDate;
        }

        public String getNasaExplain() {
            return nasaExplanation;
        }

        public String getNasaUrl() {
            return nasaURL;
        }

        public String getNasaHDURL() {
            return nasaHDURL;
        }
    }
}


