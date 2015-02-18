package com.jordannorthover.courseretriever;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper implements DBConstants {
	
	private static final int DATABASE_VERSION = 8;
	private Context context;
	public static final String TAG = DBHelper.class.getSimpleName();

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context=context;
		// TODO Auto-generated constructor stub
	}
    private static final String CREATE_TABLE_USER="create table "+USER_TABLE+" ("
            +USER_ID					+" integer primary key, "
            +USER_FIRST_NAME			+" text, "
            +USER_LAST_NAME			    +" text, "
            +USER_USERNAME		        +" text );";

    private static final String CREATE_TABLE_ENROL="create table "+ENROL_TABLE+" ("
            +ENROL_ID					+" integer primary key autoincrement, "
            +ENROL_USER			        +" integer, "
            +ENROL_COURSE		        +" integer, "
            +"FOREIGN KEY ("+ENROL_USER+") REFERENCES "+USER_TABLE+" ("+USER_ID+"), "
            +"FOREIGN KEY ("+ENROL_COURSE+") REFERENCES "+COURSE_TABLE+" ("+COURSE_ID+"));";

    private static final String CREATE_TABLE_COURSE="create table "+COURSE_TABLE+" ("
			+COURSE_ID					+" integer primary key, "
			+COURSE_SHORT_NAME			+" text not null, "
			+COURSE_FULL_NAME			+" text not null, "
			+COURSE_ENROLLED_USERS		+" integer, "
			+COURSE_ID_NUMBER			+" integer, "
			+COURSE_VISIBILITY			+" text );";

    private static final String CREATE_TABLE_SECTION="create table "+SECTION_TABLE+" ("
            +SECTION_ID				+" integer primary key, "
            +SECTION_NAME			+" text not null, "
            +SECTION_COURSE         +" integer, "
            +"FOREIGN KEY ("+SECTION_COURSE+") REFERENCES "+COURSE_TABLE+" ("+COURSE_ID+"));";

    private static final String CREATE_TABLE_MODULE="create table "+MODULE_TABLE+" ("
            +MODULE_ID				+" integer primary key, "
            +MODULE_URL 			+" text not null, "
            +MODULE_NAME			+" text not null, "
            +MODULE_ICON    		+" text, "
            +MODULE_SECTION         +" integer, "
            +"FOREIGN KEY ("+MODULE_SECTION+") REFERENCES "+SECTION_TABLE+" ("+SECTION_ID+"));";

    private static final String CREATE_TABLE_CONTENT="create table "+CONTENT_TABLE+" ("
            +CONTENT_ID				+" integer primary key, "
            +CONTENT_TYPE 			+" text, "
            +CONTENT_FILENAME		+" text not null, "
            +CONTENT_FILESIZE   	+" integer, "
            +CONTENT_FILEURL        +" text, "
            +CONTENT_TIMECREATED    +" integer, "
            +CONTENT_TIMEMODIFIED   +" integer, "
            +CONTENT_AUTHOR         +" text, "
            +CONTENT_MODULE         +" integer, "
            +"FOREIGN KEY ("+CONTENT_MODULE+") REFERENCES "+MODULE_TABLE+" ("+MODULE_ID+"));";

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			db.execSQL(CREATE_TABLE_COURSE);
			Log.d(TAG, "Created table: "+COURSE_TABLE);

            db.execSQL(CREATE_TABLE_SECTION);
            Log.d(TAG, "Created table: "+SECTION_TABLE);

            db.execSQL(CREATE_TABLE_MODULE);
            Log.d(TAG, "Created table: "+MODULE_TABLE);

            db.execSQL(CREATE_TABLE_CONTENT);
            Log.d(TAG, "Created table: "+CONTENT_TABLE);
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this.context, ""+e, Toast.LENGTH_LONG).show();
			Log.e(TAG, e.getMessage());
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		try {
			db.execSQL("DROP TABLE IF EXISTS " + COURSE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SECTION_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + MODULE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + CONTENT_TABLE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this.context, ""+e, Toast.LENGTH_LONG).show();
			Log.e(TAG, e.getMessage());
		}
		
		onCreate(db);
		Log.d(TAG, "Upgraded table: "+COURSE_TABLE);
        Log.d(TAG, "Upgraded table: "+SECTION_TABLE);
        Log.d(TAG, "Upgraded table: "+MODULE_TABLE);
        Log.d(TAG, "Upgraded table: "+CONTENT_TABLE);


	}

}
