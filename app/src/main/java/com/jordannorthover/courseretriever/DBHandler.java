package com.jordannorthover.courseretriever;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class DBHandler implements DBConstants {

	DBHelper helper;
	SQLiteDatabase db; 
	public static final String TAG = DBHandler.class.getSimpleName();
	private Context context;
    ContentResolver resolver;
    Uri tableUri;
	
	public DBHandler(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
		helper = new DBHelper(context);
        resolver = context.getContentResolver();
	}
	
	public long insertCourse(long id,String shortname, String fullname,
			int enrolledusercount, String idnumber, int visible){
        tableUri = Uri.parse(CourseProvider.AUTHORITY + "/" + COURSE_TABLE);

		ContentValues values = new ContentValues();
        values.put(COURSE_ID,id);
		values.put(COURSE_SHORT_NAME, shortname);
		values.put(COURSE_FULL_NAME, fullname);
		values.put(COURSE_ENROLLED_USERS, enrolledusercount);
		values.put(COURSE_ID_NUMBER, idnumber);
		values.put(COURSE_VISIBILITY, visible);
		Uri uri = resolver.insert(tableUri, values);
        long cid = ContentUris.parseId(uri);

        return cid;
	}

    public Cursor getAllCourses(){
        tableUri = Uri.parse(CourseProvider.AUTHORITY + "/" + COURSE_TABLE);
        //db = mapHelper.getWritableDatabase();
        String[] columns ={COURSE_ID,COURSE_SHORT_NAME,COURSE_FULL_NAME,COURSE_ENROLLED_USERS,COURSE_ID_NUMBER,COURSE_VISIBILITY};
        Cursor cursor = resolver.query(tableUri, columns, null, null, null);//db.query(MapHelper.LOCATION_TABLE, columns, null, null, null, null, null);

        return cursor;
    }

    public long insertSection(long id,String name,long courseId){
        tableUri = Uri.parse(CourseProvider.AUTHORITY + "/" + SECTION_TABLE);
        ContentValues values = new ContentValues();
        values.put(SECTION_ID,id);
        values.put(SECTION_NAME, name);
        values.put(SECTION_COURSE, courseId);
        Uri uri = resolver.insert(tableUri, values);
        long sid = ContentUris.parseId(uri);

        return sid;
    }

    public Cursor getSectionByCourse(long courseId){
        tableUri = Uri.parse(CourseProvider.AUTHORITY + "/" + SECTION_TABLE);
        String[] columns = {SECTION_ID,SECTION_NAME,SECTION_COURSE};

        String[] selectionArgs = {Long.toString(courseId)};
        Log.d(TAG, "id queried: " + courseId);
        Cursor cursor = resolver.query(tableUri, columns, SECTION_COURSE+" =?", selectionArgs, null);
        return cursor;


    }

    public long insertModule(long id,String url, String name,
                             String modIcon, long sectionId){
        tableUri = Uri.parse(CourseProvider.AUTHORITY + "/" + MODULE_TABLE);
        ContentValues values = new ContentValues();
        values.put(MODULE_ID,id);
        values.put(MODULE_URL, url);
        values.put(MODULE_NAME, name);
        values.put(MODULE_ICON, modIcon);
        values.put(MODULE_SECTION, sectionId);
        Uri uri = resolver.insert(tableUri, values);
        long mid = ContentUris.parseId(uri);

        return mid;
    }

    public Cursor getModuleBySection(long courseId){
        tableUri = Uri.parse(CourseProvider.AUTHORITY + "/" + MODULE_TABLE);
        String[] columns = {MODULE_ID,MODULE_URL,MODULE_NAME,MODULE_ICON,MODULE_SECTION};

        String[] selectionArgs = {Long.toString(courseId)};
        Log.d(TAG, "id queried: " + courseId);
        Cursor cursor = resolver.query(tableUri, columns, MODULE_SECTION+" =?", selectionArgs, null);
        return cursor;


    }

    public long insertContent(long id,String type, String filename, int filesize, String fileurl,
                              long timecreated, long timemodified, String author, long moduleId) {
        tableUri = Uri.parse(CourseProvider.AUTHORITY + "/" + CONTENT_TABLE);
        ContentValues values = new ContentValues();
        values.put(CONTENT_ID,id);
        values.put(CONTENT_TYPE, type);
        values.put(CONTENT_FILENAME, filename);
        values.put(CONTENT_FILESIZE, filesize);
        values.put(CONTENT_FILEURL, fileurl);
        values.put(CONTENT_TIMECREATED, timecreated);
        values.put(CONTENT_TIMEMODIFIED, timemodified);
        values.put(CONTENT_AUTHOR, author);
        values.put(CONTENT_MODULE, moduleId);
        Uri uri = resolver.insert(tableUri, values);
        long cid = ContentUris.parseId(uri);

        return cid;
    }

    public Cursor getContentByModule(long moduleId){
        tableUri = Uri.parse(CourseProvider.AUTHORITY + "/" + CONTENT_TABLE);
        String[] columns = {CONTENT_ID,CONTENT_TYPE,CONTENT_FILENAME,CONTENT_FILESIZE,CONTENT_FILEURL,CONTENT_TIMECREATED,CONTENT_TIMEMODIFIED,CONTENT_AUTHOR,CONTENT_MODULE};

        String[] selectionArgs = {Long.toString(moduleId)};
        Log.d(TAG, "id queried: " + moduleId);
        Cursor cursor = resolver.query(tableUri, columns, CONTENT_MODULE+" =?", selectionArgs, null);
        return cursor;


    }


}
