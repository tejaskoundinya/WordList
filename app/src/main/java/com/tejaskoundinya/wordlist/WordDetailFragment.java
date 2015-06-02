package com.tejaskoundinya.wordlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by tejas on 2/6/15.
 */
public class WordDetailFragment extends Fragment {

    private String word;
    private String meaning;
    private TextView textView_word;
    private TextView textView_meaning;

    private static final String WORD_SHARE_HASHTAG = " #WordListApp";
    private ShareActionProvider mShareActionProvider;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_word_detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (word != null) {
            mShareActionProvider.setShareIntent(createShareWordIntent());
        }
    }

    public WordDetailFragment() {
        setHasOptionsMenu(true);
    }

    private Intent createShareWordIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Word: " + word + "\nMeaning: " + meaning + "\n" + WORD_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_word_detail, container, false);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        word = getActivity().getIntent().getStringExtra("word");
        meaning = getActivity().getIntent().getStringExtra("meaning");
        textView_word = (TextView) rootView.findViewById(R.id.textView_detail_word);
        textView_meaning = (TextView) rootView.findViewById(R.id.textView_detail_meaning);
        textView_word.setText(word);
        textView_meaning.setText(meaning);
        return rootView;
    }
}