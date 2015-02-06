package com.example.courseretriever;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.example.communication.APIEndPoints;
import com.example.communication.Communication;
import com.example.communication.CommunicationResponse;

import android.support.v7.app.ActionBarActivity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ListActivity implements CommunicationResponse {
	


	
	TextView textView;
	ListView listView;
	Communication comm;
	DBHandler handler;
	Context context;
	MainActivity act;
	List<Course> courses;
	CourseListAdapter adapter;
	int userid;//=5161;
	String token;//="9d3ba6fa96d6f20390bc2d59eead7966";
	String functionName = "core_enrol_get_users_courses";
	
	public static final String TAG  = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        listView = getListView();
        comm=new Communication(this);
        handler = new DBHandler(this);
        context = this;
        act = this;
        userid=getIntent().getExtras().getInt("userid");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        token=prefs.getString("token", "");
        
        HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("wstoken", token);
		map.put("moodlewsrestformat", "json");
		map.put("wsfunction", functionName);
		map.put("userid", userid);
		String queryParams = Communication.getQueryString(map);
		comm.send(1,act, APIEndPoints.apiUrl, APIEndPoints.service, queryParams);
				
			
		
		
		
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Course course = courses.get(position);
		Intent i = new Intent(this,CourseSectionActivity.class);
		i.putExtra("courseid", course.getId());
		startActivity(i);
	}








    @Override
    public void onSuccess(int communicationId, JSONArray array) {
        // TODO Auto-generated method stub
        courses = new ArrayList<Course>();
        for(int i=0;i<array.length();i++){
            try {
                JSONObject object = array.getJSONObject(i);
                int id = object.getInt("id");
                String shortname = object.getString("shortname");
                String fullname = object.getString("fullname");
                int enrolledusercount = object.getInt("enrolledusercount");
                String idnumber = object.getString("idnumber");
                int visible = object.getInt("visible");
                Course course = new Course(id, shortname, fullname, enrolledusercount, idnumber, visible);
                courses.add(course);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        adapter = new CourseListAdapter(context, courses);
        listView.setAdapter(adapter);

    }

    @Override
    public void onSuccess(int communicationId, JSONObject object) {

    }

    @Override
    public void onError(int communicationId, String message) {

    }
}
