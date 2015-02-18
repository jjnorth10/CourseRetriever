package com.jordannorthover.entity;

public class Content {
	private String type;   //a file or a folder or external link
	private String fileName;   //filename
	private int fileSize;    //filesize
	private String fileUrl;   //downloadable file url
	private long timeCreated;  //Time created
	private long timeModified;   //Time modified
	private String author;    //Content owner
	

	public Content() {
		// TODO Auto-generated constructor stub
	}


	public Content(String type, String filename, int filesize, String fileurl,
			long timecreated, long timemodified, String author) {
		super();
		this.type = type;
		this.fileName = filename;
		this.fileSize = filesize;
		this.fileUrl = fileurl;
		this.timeCreated = timecreated;
		this.timeModified = timemodified;
		this.author = author;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getFilename() {
		return fileName;
	}


	public void setFilename(String filename) {
		this.fileName = filename;
	}


	public int getFilesize() {
		return fileSize;
	}


	public void setFilesize(int filesize) {
		this.fileSize = filesize;
	}


	public String getFileurl() {
		return fileUrl;
	}


	public void setFileurl(String fileurl) {
		this.fileUrl = fileurl;
	}


	public long getTimecreated() {
		return timeCreated;
	}


	public void setTimecreated(long timecreated) {
		this.timeCreated = timecreated;
	}


	public long getTimemodified() {
		return timeModified;
	}


	public void setTimemodified(long timemodified) {
		this.timeModified = timemodified;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}
	
	
	
	

}
