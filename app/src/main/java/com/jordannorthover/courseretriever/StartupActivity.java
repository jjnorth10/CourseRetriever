package com.jordannorthover.courseretriever;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jordannorthover.communication.APIEndPoints;
import com.jordannorthover.communication.Communication;
import com.jordannorthover.communication.CommunicationResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class StartupActivity extends Activity implements FunctionNames,CommunicationResponse {
	EditText username;
    EditText password;
	Button enter;

	StartupActivity act;
	AlertDialog dialog;
	AlertDialog.Builder builder;
	Communication comm;
	Context context;
    public static final String TAG  = StartupActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_activity_2);
        username = (EditText)findViewById(R.id.usernameET);
        password = (EditText)findViewById(R.id.passwordET);
		enter = (Button)findViewById(R.id.enter);

		context =this;
		act = this;
		comm = new Communication(context);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final SharedPreferences.Editor editor = prefs.edit();
		String usernamePrefs = prefs.getString("username", "");
        String passwordPrefs = prefs.getString("password", "");
		username.setText(usernamePrefs);
        password.setText(passwordPrefs);
		
		OnClickListener enterListener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//String token = tokenET.getText().toString();
                String user = username.getText().toString();
                String pass = password.getText().toString();
				editor.putString("username", user);
                editor.putString("password", pass);
				editor.commit();
				
				HashMap<String,Object> map = new HashMap<String,Object>();

                map.put("username", user);
                map.put("password", pass);
                map.put("service", "moodle_mobile_app");
                String queryParams = Communication.getQueryString(map);
				comm.send(1,act, APIEndPoints.apiUrl, APIEndPoints.token, queryParams);

				
			}};
			

			
			enter.setOnClickListener(enterListener);

	}

	public StartupActivity() {
		// TODO Auto-generated constructor stub
	}






    @Override
    public void onSuccess(int communicationId, JSONArray array) {

    }

    @Override
    public void onSuccess(int communicationId, JSONObject object) {
        if(communicationId==1){
            String token = null;
            try {
                token = object.getString("token");
                Log.d(TAG, "Token: " + token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(token!=null){
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                final SharedPreferences.Editor editor = prefs.edit();
                editor.putString("token",token);
                editor.commit();
            }
            HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("wstoken", token);
		    map.put("moodlewsrestformat", "json");
			map.put("wsfunction", core_webservice_get_site_info);
			String queryParams = Communication.getQueryString(map);
            comm.send(2,this,APIEndPoints.apiUrl,APIEndPoints.service,queryParams);
        }else if(communicationId==2) {
            try {
                int userid = object.getInt("userid");
                Intent i = new Intent(act, MainActivity.class);
                i.putExtra("userid", userid);
                startActivity(i);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onError(int communicationId, String message) {



    }
}
