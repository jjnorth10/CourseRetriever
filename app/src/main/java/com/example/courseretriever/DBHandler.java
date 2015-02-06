package com.example.courseretriever;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBHandler implements DBConstants {

	DBHelper helper;
	SQLiteDatabase db; 
	public static final String TAG = DBHandler.class.getSimpleName();
	private Context context;
	
	public DBHandler(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
		helper = new DBHelper(context);
	}
	
	public long insertCourse(long id, String shortname, String fullname,
			int enrolledusercount, String idnumber, int visible){
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COURSE_ID, id);
		values.put(COURSE_SHORT_NAME, shortname);
		values.put(COURSE_FULL_NAME, fullname);
		values.put(COURSE_ENROLLED_USERS, enrolledusercount);
		values.put(COURSE_ID_NUMBER, idnumber);
		values.put(COURSE_VISIBILITY, visible);
		long i = db.insert(COURSE_TABLE, null, values);
		db.close();
		return i;
	}

}
