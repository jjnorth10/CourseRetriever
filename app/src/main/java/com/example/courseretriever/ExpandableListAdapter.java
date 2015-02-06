package com.example.courseretriever;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	private String[] modules= new String[99];
	private String[][] contents = new String[99][99];
	private Context context;
	public ExpandableListAdapter(Context context,String[] modules,String[][] contents) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.modules=modules;
		this.contents=contents;
	}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return modules.length;
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return contents[groupPosition].length;
	}
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return modules[groupPosition];
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return contents[groupPosition][childPosition];
	}
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.simple_list_item, parent, false);
		TextView title = (TextView)row.findViewById(R.id.list_item);
		title.setText(modules[groupPosition]);
		return row;
	}
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.simple_list_item, parent, false);
		TextView title = (TextView)row.findViewById(R.id.list_item);
		title.setText(contents[groupPosition][childPosition]);
		return row;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
