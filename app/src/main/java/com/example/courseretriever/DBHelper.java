package com.example.courseretriever;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper implements DBConstants {
	
	private static final int DATABASE_VERSION = 1;
	private Context context;
	public static final String TAG = DBHelper.class.getSimpleName();

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context=context;
		// TODO Auto-generated constructor stub
	}

	private static final String CREATE_TABLE_COURSE="create table "+COURSE_TABLE+" ("
			+COURSE_ID					+" integer primary key , "
			+COURSE_SHORT_NAME			+" text not null, "
			+COURSE_FULL_NAME			+" text not null, "
			+COURSE_ENROLLED_USERS		+" integer, "
			+COURSE_ID_NUMBER			+" integer, "
			+COURSE_VISIBILITY			+" text );";

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			db.execSQL(CREATE_TABLE_COURSE);
			Log.d(TAG, "Created table: "+COURSE_TABLE);
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this.context, ""+e, Toast.LENGTH_LONG).show();
			Log.e(TAG, e.getMessage());
		}
		
		onCreate(db);;
		Log.d(TAG, "Upgraded table: "+COURSE_TABLE);


	}

}
