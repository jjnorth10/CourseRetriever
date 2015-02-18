package com.jordannorthover.courseretriever;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jordannorthover.communication.APIEndPoints;
import com.jordannorthover.communication.Communication;
import com.jordannorthover.communication.CommunicationResponse;
import com.jordannorthover.entity.Content;
import com.jordannorthover.entity.Module;


import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CourseModuleActivity extends ListActivity implements CommunicationResponse,FunctionNames,ActionMode.Callback {
    public static final String TAG = CourseModuleActivity.class.getSimpleName();
    ListView listView;
    ModuleListAdapter adapter;
    Communication comm;
    int userid=0;
    int courseid;
    String section;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    List<Module> modules;
    List<String> moduleFiles;
    List<String> moduleFileNames;
    ActionMode mode;
    CourseModuleActivity act;
    String token;
    String filename;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_modules);
        mode=null;

        listView = getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //registerForContextMenu(listView);
        comm = new Communication(this);
        userid=getIntent().getExtras().getInt("userid");
        courseid=getIntent().getExtras().getInt("courseid");
        section=getIntent().getExtras().getString("section");
        context = this;
        act = this;

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listView.clearChoices();
                listView.setItemChecked(position,true);
                Log.d(TAG,"selected position "+position);
                //if (mode != null) {
                //    return false;
                //}

                // Start the CAB using the ActionMode.Callback defined above
                mode = act.startActionMode(act);
                //view.setSelected(true);

                return true;
            }
        });
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
                Intent homeIntent = new Intent(this, CourseSectionActivity.class);
                homeIntent.putExtra("courseid",courseid);
                homeIntent.putExtra("userid",userid);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return super.onOptionsItemSelected(item);

    }

    public CourseModuleActivity() {
        // TODO Auto-generated constructor stub
    }

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v,
        ContextMenuInfo menuInfo) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.course_contents, menu);
      super.onCreateContextMenu(menu, v, menuInfo);
     }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        Module module = modules.get(position);
        List<Content> contents = module.getContent();
        final String fileurl=contents.get(0).getFileurl();
        final String filename=contents.get(0).getFilename();
        switch (item.getItemId()) {
        case(R.id.download):
            StringBuilder sbuilder = new StringBuilder();
            String queryParams = "&token="+token;
            sbuilder.append(fileurl);
            sbuilder.append(queryParams);
            String url = sbuilder.toString();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle(filename);
            request.setDescription("File Downloading...");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            String fileName = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url));
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            DownloadManager manager = (DownloadManager) act.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
            download(position);
        }
        return super.onContextItemSelected(item);
    }*/
    public void download(int position){
        Module module = modules.get(position);
        List<Content> contents = module.getContent();
        final String fileurl=contents.get(0).getFileurl();
        final String filename=contents.get(0).getFilename();
        StringBuilder sbuilder = new StringBuilder();
        String queryParams = "&token="+token;
        sbuilder.append(fileurl);
        sbuilder.append(queryParams);
        String url = sbuilder.toString();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(filename);
        request.setDescription("File Downloading...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        String fileName = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager manager = (DownloadManager) act.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }

    public void share(int position){
        Module module = modules.get(position);
        List<Content> contents = module.getContent();
        final String fileurl=contents.get(0).getFileurl();
        final String filename=contents.get(0).getFilename();
        if(isFileInCache(filename)){
            comm.shareFile(fileurl);
        }else {
            StringBuilder sbuilder = new StringBuilder();
            String queryParams = "&token=" + token;

            sbuilder.append(fileurl);
            sbuilder.append(queryParams);
            String url = sbuilder.toString();
            Log.d(TAG, "File url: " + url);
            comm.retrieveFile(act, url, "",2);

        }
    }

    public boolean isFileInCache(String fileName){
        File file = new File(context.getExternalCacheDir(),fileName);
        if (file.exists()){
            Log.d(TAG,"file in cache");
            return true;

        }else{
            Log.d(TAG,"file not in cache");
            return false;
        }
    }




    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        //String filename=moduleFiles.get(position);
        //Toast.makeText(context, filename, Toast.LENGTH_LONG).show();
        Module module = modules.get(position);
        List<Content> contents = module.getContent();
        final String fileurl=contents.get(0).getFileurl();
        final String filename=contents.get(0).getFilename();
        builder=new AlertDialog.Builder(this);
        builder.setTitle("Open "+filename);
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isFileInCache(filename)){
                    comm.showFile(fileurl);
                }else {
                    StringBuilder sbuilder = new StringBuilder();

                    //HashMap<String,Object> map = new HashMap<String,Object>();
                    //map.put("token", token);
                    String queryParams = "&token=" + token;

                    sbuilder.append(fileurl);
                    sbuilder.append(queryParams);
                    String url = sbuilder.toString();
                    Log.d(TAG, "File url: " + url);
                    comm.retrieveFile(act, url, "",1);

                }



            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        dialog = builder.create();
        dialog.show();



    }








    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.course_contents, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        int position = listView.getCheckedItemPosition();
        switch (item.getItemId()) {

            case R.id.download:

                Log.d(TAG,"list position "+position);
                download(position);
                mode.finish(); // Action picked, so close the CAB
                return true;
            case R.id.share:

                share(position);
                mode.finish(); // Action picked, so close the CAB
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mode = null;
    }

    @Override
    public void onSuccess(int communicationId, JSONArray array) {
        // TODO Auto-generated method stub

        modules=new ArrayList<Module>();
        moduleFiles=new ArrayList<String>();
        moduleFileNames=new ArrayList<String>();
        for(int i=0;i<array.length();i++){
            try {
                JSONObject object = array.getJSONObject(i);
                if(object.getString("name").equals(section)){
                    JSONArray moduleArray = object.getJSONArray("modules");
                    for(int j=0;j<moduleArray.length();j++){
                        JSONObject moduleObject = moduleArray.getJSONObject(j);



                        if(moduleObject.has("contents")){
                            //modules.add(moduleName);
                            String moduleName = moduleObject.getString("name");
                            String moduleIcon = moduleObject.getString("modicon");
                            String moduleUrl = moduleObject.getString("url");
                            List<Content> contents = new ArrayList<Content>();
                            JSONArray contentArray = moduleObject.getJSONArray("contents");
                            JSONObject contentObject = contentArray.getJSONObject(0);
                            String type = contentObject.getString("type");
                            String filename = contentObject.getString("filename");
                            int filesize = contentObject.getInt("filesize");
                            String fileurl = contentObject.getString("fileurl");
                            long timecreated = contentObject.getLong("timecreated");
                            long timemodified = contentObject.getLong("timemodified");
                            String author = contentObject.getString("author");
                            Content content = new Content(type, filename, filesize, fileurl, timecreated, timemodified, author);
                            contents.add(content);
                            Module module = new Module(moduleUrl, moduleName, moduleIcon, contents);
                            modules.add(module);
                            //moduleFiles.add(file.getString("fileurl"));
                            //moduleFileNames.add(file.getString("filename"));
                        }

                    }
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        adapter = new ModuleListAdapter(context,modules);//new ArrayAdapter<String>(context, R.layout.simple_list_item, R.id.list_item, modules);
        listView.setAdapter(adapter);

    }

    @Override
    public void onSuccess(int communicationId, JSONObject object) {

    }

    @Override
    public void onError(int communicationId, String message) {

    }


    class ModuleListAdapter extends BaseAdapter{
        private Context context;
        private List<Module> modules;

        public ModuleListAdapter(Context context,List<Module> modules) {
            super();
            this.context = context;
            this.modules = modules;
            // TODO Auto-generated constructor stub
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return modules.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return modules.get(position);
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
            View row = inflater.inflate(R.layout.list_item_2, parent, false);
            ImageView image = (ImageView)row.findViewById(R.id.image);
            TextView title = (TextView)row.findViewById(R.id.list_item_title);
            title.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Bold.ttf"));
            TextView description = (TextView)row.findViewById(R.id.list_item_desc);
            title.setText(modules.get(position).getName());
            Date d = new Date(modules.get(position).getContent().get(0).getTimemodified()*1000);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            description.setText("Date Modified: "+dateFormat.format(d));
            String url = modules.get(position).getContent().get(0).getFileurl();
            setFileImage(image,url);

            return row;
        }

        public void setFileImage(ImageView image,String url){
            String type = MimeTypeMap.getFileExtensionFromUrl(url);
            if(type.equals("pdf")){
                image.setImageDrawable(getResources().getDrawable(R.drawable.pdf));
            }
            if(type.equals("doc")){
                image.setImageDrawable(getResources().getDrawable(R.drawable.word));
            }
            if(type.equals("ppt")){
                image.setImageDrawable(getResources().getDrawable(R.drawable.powerpoint));
            }
            if(type.equals("pptx")){
                image.setImageDrawable(getResources().getDrawable(R.drawable.powerpoint));
            }
            if(type.equals("xls")){
                image.setImageDrawable(getResources().getDrawable(R.drawable.excel));
            }

        }

    }



}