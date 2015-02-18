package com.jordannorthover.courseretriever;

public class Course {
	private int id; //id of course
	private String shortname;    //short name of course
	private String fullname;    //long name of course
	private int enrolledusercount;  //Number of enrolled users in this course
	private String idnumber;   //id number of course
	private int visible;  //1 means visible, 0 means hidden course
	
	public Course(int id, String shortname, String fullname,
			int enrolledusercount, String idnumber, int visible) {
		super();
		this.id = id;
		this.shortname = shortname;
		this.fullname = fullname;
		this.enrolledusercount = enrolledusercount;
		this.idnumber = idnumber;
		this.visible = visible;
	}
	public Course() {
		// TODO Auto-generated constructor stub
	}
	
	public int getId() {
		return id;
	}
	public String getShortname() {
		return shortname;
	}
	public String getFullname() {
		return fullname;
	}
	public int getEnrolledusercount() {
		return enrolledusercount;
	}
	public String getIdnumber() {
		return idnumber;
	}
	public int getVisible() {
		return visible;
	}

}
