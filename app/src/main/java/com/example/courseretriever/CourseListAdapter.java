package com.example.courseretriever;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CourseListAdapter extends BaseAdapter {
	List<Course> courses;
	Context context;
	public CourseListAdapter(Context context,List<Course> courses) {
		// TODO Auto-generated constructor stub
		this.courses = courses;
		this.context=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return courses.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return courses.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.list_item, parent, false);
		TextView title = (TextView)row.findViewById(R.id.list_item_title);
		title.setTypeface(Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Bold.ttf"));
		TextView description = (TextView)row.findViewById(R.id.list_item_desc);
		title.setText(courses.get(position).getShortname());
		description.setText(courses.get(position).getFullname());
		return row;
	}

}
