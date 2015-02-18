package com.jordannorthover.courseretriever;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jordannorthover.communication.APIEndPoints;
import com.jordannorthover.communication.Communication;
import com.jordannorthover.communication.CommunicationResponse;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CourseSectionActivity extends ListActivity implements FunctionNames,DBConstants, CommunicationResponse {
	

	ListView listView;
	SectionListAdapter adapter;

	Communication comm;
	Context context;
    DBHandler handler;
	

	//String[] sections= new String[];
	//String[][] modules= new String[][];
	String token;
	List<String> sections;
    int userid=0;
	int courseid=0;
	public static final String TAG = CourseSectionActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_sections);
        handler = new DBHandler(this);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
		
		listView=getListView();
		comm=new Communication(this);
		context=this;
        userid=getIntent().getExtras().getInt("userid");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.putExtra("userid",userid);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return super.onOptionsItemSelected(item);

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
        i.putExtra("userid", userid);
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
                //handler.insertSection(name,courseid);
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
