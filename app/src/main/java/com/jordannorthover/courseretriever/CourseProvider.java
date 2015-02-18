package com.jordannorthover.courseretriever;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;


public class CourseProvider extends ContentProvider {

    public static final String AUTHORITY = "content://com.example.courseretriever.CourseProvider";
    public static final String BASE = "com.example.courseretriever.CourseProvider";
    public static final Uri CONTENT_URI = Uri.parse(AUTHORITY);
    public static final Uri COURSE_URI = Uri.parse(AUTHORITY+"/"+DBConstants.COURSE_TABLE);
    public static final Uri SECTION_URI = Uri.parse(AUTHORITY+"/"+DBConstants.SECTION_TABLE);
    public static final Uri MODULE_URI = Uri.parse(AUTHORITY+"/"+DBConstants.MODULE_TABLE);
    public static final Uri COURSE_CONTENT_URI = Uri.parse(AUTHORITY+"/"+DBConstants.CONTENT_TABLE);
    public static final Uri USER_URI = Uri.parse(AUTHORITY+"/"+DBConstants.USER_TABLE);
    public static final Uri ENROL_URI = Uri.parse(AUTHORITY+"/"+DBConstants.ENROL_TABLE);
    public static final Uri SUGGESTION_URI = Uri.parse(AUTHORITY+"/"+ SearchManager.SUGGEST_URI_PATH_QUERY);

    public static final String TAG = CourseProvider.class.getSimpleName();

    final static int COURSE = 1;
    final static int COURSE_ID = 2;
    final static int MODULE = 3;
    final static int MODULE_ID = 4;
    final static int CONTENT = 5;
    final static int CONTENT_ID = 6;
    final static int SECTION = 7;
    final static int SECTION_ID = 8;
    final static int USER = 9;
    final static int USER_ID = 10;
    final static int ENROL = 11;
    final static int ENROL_ID = 12;
    final static int SEARCH_SUGGEST =13;

    private static final String[] SEARCH_SUGGEST_COLUMNS = {
            DBConstants.SUGGESTION_ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
    };

    private SQLiteDatabase db;
    DBHelper dbHelper;


    private final static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);



    static {
        uriMatcher.addURI(BASE, DBConstants.COURSE_TABLE, COURSE);
        uriMatcher.addURI(BASE, DBConstants.COURSE_TABLE+"/#", COURSE);

        uriMatcher.addURI(BASE, DBConstants.MODULE_TABLE, MODULE);
        uriMatcher.addURI(BASE, DBConstants.MODULE_TABLE+"/#", MODULE);



        uriMatcher.addURI(BASE, DBConstants.CONTENT_TABLE, CONTENT);
        uriMatcher.addURI(BASE, DBConstants.CONTENT_TABLE+"/#", CONTENT);

        uriMatcher.addURI(BASE, DBConstants.SECTION_TABLE, SECTION);
        uriMatcher.addURI(BASE, DBConstants.SECTION_TABLE+"/#", SECTION);

        uriMatcher.addURI(BASE, DBConstants.USER_TABLE, USER);
        uriMatcher.addURI(BASE, DBConstants.USER_TABLE+"/#", USER);

        uriMatcher.addURI(BASE, DBConstants.ENROL_TABLE, ENROL);
        uriMatcher.addURI(BASE, DBConstants.ENROL_TABLE+"/#", ENROL);

        uriMatcher.addURI(BASE, SearchManager.SUGGEST_URI_PATH_QUERY , SEARCH_SUGGEST);
        uriMatcher.addURI(BASE, "limit/*", SEARCH_SUGGEST);
        uriMatcher.addURI(BASE, SearchManager.SUGGEST_URI_PATH_QUERY + "/#", SEARCH_SUGGEST);



    }

    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, uri.toString());
        Log.d(TAG, "Uri matcher: "+uriMatcher.match(uri));
        switch (uriMatcher.match(uri)) {
            case SEARCH_SUGGEST:
                Log.d(TAG, "Search suggestions requested.");
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(DBConstants.COURSE_TABLE);


                HashMap<String, String> columnMap = new HashMap<String, String>();
                columnMap.put(DBConstants.SUGGESTION_ID, COURSE_ID + " AS " + DBConstants.SUGGESTION_ID);
                columnMap.put(SearchManager.SUGGEST_COLUMN_TEXT_1, DBConstants.COURSE_FULL_NAME + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
                columnMap.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, COURSE_ID + " AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
                builder.setProjectionMap(columnMap);

                String[] selArgs = {"%"+selectionArgs[0]+"%"};

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = builder.query(db,SEARCH_SUGGEST_COLUMNS, selection, selArgs, null, null, sortOrder, null);

                return cursor;
            default:
                db = dbHelper.getWritableDatabase();
                Cursor defcursor=null;
                defcursor = db.query(uri.getLastPathSegment(), projection, selection, selectionArgs, null, null, sortOrder);

                //db.close();
                defcursor.setNotificationUri(getContext().getContentResolver(), uri);
                return defcursor;
        }

    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = dbHelper.getWritableDatabase();
        long id = db.insert(uri.getLastPathSegment(), null, values);

        //db.close();
        getContext().getContentResolver().notifyChange(uri, null);
        switch(uriMatcher.match(uri)) {
            case(COURSE):
                return Uri.parse(DBConstants.COURSE_TABLE + "/" + id);
            case(SECTION):
                return Uri.parse(DBConstants.SECTION_TABLE + "/" + id);
            case(MODULE):
                return Uri.parse(DBConstants.MODULE_TABLE + "/" + id);
            case(CONTENT):
                return Uri.parse(DBConstants.CONTENT_TABLE + "/" + id);
            case(USER):
                return Uri.parse(DBConstants.USER_TABLE + "/" + id);
            case(ENROL):
                return Uri.parse(DBConstants.ENROL_TABLE + "/" + id);


        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = dbHelper.getWritableDatabase();
        int count=0;
        count = db.delete(uri.getLastPathSegment(), selection, selectionArgs);

        //db.close();
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db = dbHelper.getWritableDatabase();
        int count=0;
        count=db.update(uri.getLastPathSegment(), values, selection, selectionArgs);


        //db.close();
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
