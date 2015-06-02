package com.tejaskoundinya.wordlist;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.tejaskoundinya.wordlist.com.tejaskoundinya.wordlist.data.WordContract;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(result);
        } catch (Exception e) {
            e.printStackTrace();
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
        String strurl = "http://tejaskoundinya-words-app.appspot.com/english_words";
        StringBuilder builder = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(strurl);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                Log.v("Getter", "Your data: " + builder.toString());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return builder.toString();
    }
}