package com.tejaskoundinya.wordlist.com.tejaskoundinya.wordlist.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tejas
 */
public class WordContract {

    public static final String CONTENT_AUTHORITY = "com.tejaskoundinya.wordlist";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_WORD = "word";

    public static final class WordEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORD).build();
        // Table name
        public static final String TABLE_NAME = "word";
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORD;

        public static final String COLUMN_WORD_NAME = "word_name";
        public static final String COLUMN_MEANING = "meaning";
        public static final String COLUMN_POS = "pos";

        public static Uri buildWordUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildWord() {
            return CONTENT_URI.buildUpon().build();
        }

        public static int getWordCountFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

    }

}
