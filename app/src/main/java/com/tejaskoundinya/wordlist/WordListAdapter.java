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

/**
 * Created by tejas
 */
/*public class WordListAdapter extends ArrayAdapter<JSONObject> {
    private Context context;
    private JSONObject[] jsonObject;
    public WordListAdapter(Context context, JSONObject[] jsonObject) {
        super(context, R.layout.row_layout, jsonObject);
        this.context = context;
        this.jsonObject = jsonObject;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        TextView word = (TextView) rowView.findViewById(R.id.tv_word);
        TextView meaning = (TextView) rowView.findViewById(R.id.tv_meaning);
        String s_word = "";
        String s_meaning = "";
        try {
            s_word = (String)jsonObject[position].get("word");
            s_meaning = (String)jsonObject[position].get("meaning");
        } catch (Exception e) {
            e.printStackTrace();
        }
        word.setText(s_word);
        meaning.setText(s_meaning);
        return rowView;
    }
}*/

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