package com.tejaskoundinya.wordlist;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.tejaskoundinya.wordlist.com.tejaskoundinya.wordlist.data.WordContract;

import org.apache.commons.io.IOUtils;
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
import java.util.Vector;

/**
 * Created by tejas
 */
public class FetchWordTask extends AsyncTask<Void, Void, String> {

    private final Context mContext;

    public FetchWordTask(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(Void... p) {
        String op = "";

        try {
            op = downloadPage();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return op;
    }

    @Override
    protected void onPostExecute(String result) {
        // Parse JSON file
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(jsonArray == null) {
            return;
        }

        JSONObject[] jsonObjects = new JSONObject[jsonArray.length()];

        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonObjects[i] = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Vector<ContentValues> cVVector = new Vector<ContentValues>(jsonObjects.length);

        for(JSONObject jsonObject : jsonObjects) {
            ContentValues wordValues = new ContentValues();
            try {
                wordValues.put(WordContract.WordEntry.COLUMN_WORD_NAME, (String)jsonObject.get("word"));
                wordValues.put(WordContract.WordEntry.COLUMN_MEANING, (String)jsonObject.get("meaning"));
            } catch (JSONException e) {
                Log.e("FetchWordTask", e.getMessage(), e);
                e.printStackTrace();
            }

            cVVector.add(wordValues);

        }

        int inserted = 0;
        if(cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = mContext.getContentResolver().bulkInsert(WordContract.WordEntry.CONTENT_URI, cvArray);
        }

/*
        WordListAdapter adapter = new WordListAdapter(mContext, jsonObjects);
        
        listView.setAdapter(adapter);
*/
    }

    private String downloadPage() {
        // Call custom API created. Returns a list of 10 words and meanings
        String strurl = "http://tejaskoundinya-words-app.appspot.com/english_words";
        URL url = null;
        String response = "";
        HttpURLConnection connection = null;

        try {
            url = new URL(strurl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

         try {
             connection = (HttpURLConnection)url.openConnection();
         } catch (IOException e) {
             e.printStackTrace();
         }

        try {
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            response = IOUtils.toString(inputStream);
            Log.v("Getter", "Your data: " + response);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}