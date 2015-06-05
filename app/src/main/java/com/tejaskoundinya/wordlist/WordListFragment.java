package com.tejaskoundinya.wordlist;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.tejaskoundinya.wordlist.com.tejaskoundinya.wordlist.data.WordContract.WordEntry;

/**
 * Created by tejas.
 */
public class WordListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private String LOG_TAG = WordListFragment.class.getSimpleName();

    private static final int WORD_LOADER = 0;
    GridView listView = null;
    Button button = null;
    private WordListAdapter wordListAdapter;
    WordClicked mCallback;
    private int mClickPosition;

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

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getIntent().putExtra("clickPosition", mClickPosition);
    }

    @Override
    public void onResume() {
        super.onResume();
        mClickPosition = getActivity().getIntent().getIntExtra("clickPosition", 0);
    }

    public WordListFragment() {
    }

    public interface WordClicked {
        public void sendWord(String word, String meaning);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        wordListAdapter = new WordListAdapter(getActivity(), null, 0);

        listView =  (GridView) rootView.findViewById(R.id.listView_word);
        listView.setAdapter(wordListAdapter);

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new FetchWordTask(getActivity().getApplicationContext()).execute();
        } else {
            (Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT)).show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                mClickPosition = position;
                if (cursor != null) {
                    //Toast.makeText(getActivity(), (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) + ", " + Configuration.SCREENLAYOUT_SIZE_LARGE, Toast.LENGTH_LONG).show();
                    // Check form factor of device. Large form factors behave differently with a different layout.
                    if( (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE ) {
                        mCallback.sendWord(cursor.getString(COL_WORD_NAME), cursor.getString(COL_WORD_MEANING));
                    }
                    else {
                        Intent intent = new Intent(getActivity(), WordDetailActivity.class);
                        intent.putExtra("word", cursor.getString(COL_WORD_NAME));
                        intent.putExtra("meaning", cursor.getString(COL_WORD_MEANING));
                        startActivity(intent);
                    }
                }
            }
        });

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
                // Forcing the loader to load existing data if there is no internet connection
                getLoaderManager().getLoader(WORD_LOADER).forceLoad();
                (Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT)).show();
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Callback for inter fragment communication
        try {
            mCallback = (WordClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement WordClicked");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(WORD_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Creating a CursorLoader
        Log.v(LOG_TAG, "onCreateLoader");
        Uri wordUri = WordEntry.buildWord();
        String sortOrder = "RANDOM()";

        return new CursorLoader(getActivity(), wordUri, WORD_COLUMNS, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data == null) {
            return;
        }
        wordListAdapter.swapCursor(data);
        // Check form factor of device. Large form factors behave differently with a different layout.
        if( (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE ) {
            try {
                data.move(mClickPosition);
                mCallback.sendWord(data.getString(COL_WORD_NAME), data.getString(COL_WORD_MEANING));
            } catch (CursorIndexOutOfBoundsException e) {
                Log.e(LOG_TAG, e.getMessage());
                return;
            }
        }
        Log.v(LOG_TAG, "Cursor Swapped");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        wordListAdapter.swapCursor(null);
    }
}