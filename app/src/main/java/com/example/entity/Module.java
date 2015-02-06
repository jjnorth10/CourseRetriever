package com.example.entity;

import java.util.List;

public class Module {
	private String url;
	private String name;
	private String modIcon;
	private List<Content> content;
	
	
	public Module() {
		// TODO Auto-generated constructor stub
	}


	public Module(String url, String name, String modIcon, List<Content> content) {
		super();
		this.url = url;
		this.name = name;
		this.modIcon = modIcon;
		this.content = content;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getModIcon() {
		return modIcon;
	}


	public void setModIcon(String modIcon) {
		this.modIcon = modIcon;
	}


	public List<Content> getContent() {
		return content;
	}


	public void setContent(List<Content> content) {
		this.content = content;
	}


}