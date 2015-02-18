package com.jordannorthover.courseretriever;

public interface DBConstants {
	public static final String DATABASE_NAME="courseretriever.db";
	//Database Tables
    public static final String USER_TABLE = "user";
	public static final String COURSE_TABLE="course";
    public static final String SECTION_TABLE="section";
    public static final String MODULE_TABLE="module";
    public static final String CONTENT_TABLE="content";
    public static final String ENROL_TABLE="enrol";

    //Columns for user table
    public static final String USER_ID = "_id";
    public static final String USER_USERNAME = "username";
    public static final String USER_FIRST_NAME = "firstname";
    public static final String USER_LAST_NAME = "lastname";

    //Columns for enroln table
    public static final String ENROL_ID = "_id";
    public static final String ENROL_USER = "user";
    public static final String ENROL_COURSE = "course";


	//Columns for course table
	public static final String COURSE_ID = "_id";
	public static final String COURSE_SHORT_NAME = "shortname";
	public static final String COURSE_FULL_NAME = "fullname";
	public static final String COURSE_ENROLLED_USERS = "enrolledusercount";
	public static final String COURSE_ID_NUMBER = "idnumber";
	public static final String COURSE_VISIBILITY = "visible";

    //Columns for section table
    public static final String SECTION_ID = "_id";
    public static final String SECTION_NAME = "name";
    public static final String SECTION_COURSE = "course";


    //Columns for module table
    public static final String MODULE_ID = "_id";
    public static final String MODULE_URL =  "url";
    public static final String MODULE_NAME = "name";
    public static final String MODULE_ICON = "modicon";
    public static final String MODULE_SECTION = "section";

    //Columns for content table
    public static final String CONTENT_ID = "_id";
    public static final String CONTENT_TYPE = "type";
    public static final String CONTENT_FILENAME = "filename";
    public static final String CONTENT_FILESIZE = "filesize";
    public static final String CONTENT_FILEURL = "fileurl";
    public static final String CONTENT_TIMECREATED = "timecreated";
    public static final String CONTENT_TIMEMODIFIED = "timemodified";
    public static final String CONTENT_AUTHOR = "author";
    public static final String CONTENT_MODULE = "module";

    //Columns for search suggestions
    public static final String SUGGESTION_ID = "_id";



}
