package com.tejaskoundinya.wordlist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class WordListAdapter extends CursorAdapter {

    public WordListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView word = (TextView) view.findViewById(R.id.tv_word);
        TextView meaning = (TextView) view.findViewById(R.id.tv_meaning);
        word.setText(cursor.getString(1));
        meaning.setText(cursor.getString(2));
    }
}