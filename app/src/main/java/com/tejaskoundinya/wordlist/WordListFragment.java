package com.tejaskoundinya.wordlist;



import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.tejaskoundinya.wordlist.com.tejaskoundinya.wordlist.data.WordContract;
import com.tejaskoundinya.wordlist.com.tejaskoundinya.wordlist.data.WordContract.WordEntry;

/**
 * Created by tejas.
 */
public class WordListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int WORD_LOADER = 0;
    ListView listView = null;
    Button button = null;
    private WordListAdapter wordListAdapter;

    private static final int DETAIL_LOADER = 0;

    private static final String[] WORD_COLUMNS = {
            WordEntry.TABLE_NAME + "." + WordEntry._ID,
            WordEntry.COLUMN_WORD_NAME,
            WordEntry.COLUMN_MEANING,
            WordEntry.COLUMN_POS,
    };

    // these constants correspond to the projection defined above, and must change if the
    // projection changes
    private static final int COL_WORD_ID = 0;
    private static final int COL_WORD_NAME = 1;
    private static final int COL_WORD_MEANING = 2;
    private static final int COL_WORD_POS = 3;

    public WordListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        wordListAdapter = new WordListAdapter(getActivity(), null, 0);

        listView =  (ListView) rootView.findViewById(R.id.listView_word);
        listView.setAdapter(wordListAdapter);

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new FetchWordTask(getActivity().getApplicationContext()).execute();
        } else {
            (Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT)).show();
        }

        //new HttpGetTask().execute();
        button = (Button) rootView.findViewById(R.id.button_more_words);
        button.setOnClickListener(buttonClick);

        return rootView;
    }

    View.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                new FetchWordTask(getActivity().getApplicationContext()).execute();
            } else {
                (Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT)).show();
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(WORD_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v("Umbala", "onCreateLoader");
        Uri wordUri = WordEntry.buildWord();
        String sortOrder = "RANDOM()";

        return new CursorLoader(getActivity(), wordUri, WORD_COLUMNS, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        wordListAdapter.swapCursor(data);
        Log.v("Umbala", "Cursor Swapped");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        wordListAdapter.swapCursor(null);
    }
}