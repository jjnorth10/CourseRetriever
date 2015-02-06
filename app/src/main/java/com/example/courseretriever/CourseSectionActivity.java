package com.example.courseretriever;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.example.communication.APIEndPoints;
import com.example.communication.Communication;
import com.example.communication.CommunicationResponse;
import com.example.entity.Module;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CourseSectionActivity extends ListActivity implements FunctionNames, CommunicationResponse {
	

	ListView listView;
	SectionListAdapter adapter;
	Communication comm;
	Context context;
	
	

	//String[] sections= new String[];
	//String[][] modules= new String[][];
	String token;
	List<String> sections;
	int courseid=0;
	public static final String TAG = CourseSectionActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_sections);
		
		
		
		listView=getListView();
		comm=new Communication(this);
		context=this;
		courseid=getIntent().getExtras().getInt("courseid");
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        token=prefs.getString("token", "");
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("wstoken", token);
		map.put("moodlewsrestformat", "json");
		map.put("wsfunction", coreCourseGetContents);
		map.put("courseid", courseid);
		String queryParams = Communication.getQueryString(map);
		comm.send(1,this, APIEndPoints.apiUrl, APIEndPoints.service, queryParams);
	}



	public CourseSectionActivity() {
		// TODO Auto-generated constructor stub
	}
	
	
	






	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String section = sections.get(position);
		Intent i = new Intent(this,CourseModuleActivity.class);
		i.putExtra("section", section);
		i.putExtra("courseid", courseid);
		startActivity(i);
	}



    @Override
    public void onSuccess(int communicationId, JSONArray array) {
        // TODO Auto-generated method stub
        sections=new ArrayList<String>();
        for(int i=0;i<array.length();i++){
            try {
                JSONObject object = array.getJSONObject(i);
                String name = object.getString("name");
                sections.add(name);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        adapter = new SectionListAdapter(context, sections);//ArrayAdapter<String>(context, R.layout.simple_list_item, R.id.list_item, sections);
        listView.setAdapter(adapter);

    }

    @Override
    public void onSuccess(int communicationId, JSONObject object) {

    }

    @Override
    public void onError(int communicationId, String message) {

    }

    class SectionListAdapter extends BaseAdapter{
		private Context context;
		private List<String> sections;
		
		public SectionListAdapter(Context context,List<String> sections) {
			super();
			this.context = context;
			this.sections = sections;
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return sections.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return sections.get(position);
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
			View row = inflater.inflate(R.layout.simple_list_item, parent, false);
			TextView text = (TextView)row.findViewById(R.id.list_item);
			text.setTypeface(Typeface.createFromAsset(getAssets(), "RobotoCondensed-Bold.ttf"));
			text.setTextColor(getResources().getColor(R.color.blue));
			text.setText(sections.get(position));
			
			
			return row;
		}
	}

	

	
}
