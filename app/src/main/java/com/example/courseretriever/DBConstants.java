package com.example.courseretriever;

public interface DBConstants {
	public static final String DATABASE_NAME="courseretriever.db";
	//Database Tables
	public static final String COURSE_TABLE="course";
	
	//Columns for course table
	public static final String COURSE_ID = "_id";
	public static final String COURSE_SHORT_NAME = "shortname";
	public static final String COURSE_FULL_NAME = "fullname";
	public static final String COURSE_ENROLLED_USERS = "enrolledusercount";
	public static final String COURSE_ID_NUMBER = "idnumber";
	public static final String COURSE_VISIBILITY = "visible";

}
