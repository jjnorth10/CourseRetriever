package com.example.communication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.example.courseretriever.R;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Toast;

public class Communication {
	public static final String TAG = Communication.class.getSimpleName();
	public String URL;// = "http://192.168.0.6:9000/location/list";
	JSONArray array;
	JSONObject object;
	int statusCode;
	String errorString = "Error cannot connect";
	
	ProgressDialog progressDialog;
	ProgressDialog progressDialog2;
	Context context;
	public Communication(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	
	public void send (final int communicationId, final CommunicationResponse communicationResponse,String apiUrl,String path,String queryParams){
		StringBuilder builder = new StringBuilder();
		builder.append(apiUrl);
		builder.append(path);
		builder.append(queryParams);
		URL = builder.toString();
		Log.d(TAG, "Url: "+URL);
		
		AsyncTask<String,Object,String> async = new AsyncTask<String,Object,String>(){
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage("Loading...");
				progressDialog.show();
			}


			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				String json;
				try{
					StrictMode.ThreadPolicy policy = new StrictMode.
					ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy); 
					
					//java.net.URL url = new java.net.URL(URL);
					//HttpURLConnection con =(HttpURLConnection) url.openConnection();
					
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(arg0[0]);
					HttpResponse httpResponse = httpClient.execute(httpGet);
					statusCode=httpResponse.getStatusLine().getStatusCode();
					HttpEntity entity = httpResponse.getEntity();
					InputStream is = entity.getContent();
					//return is;
					try {
						 
						InputStreamReader isReader = new InputStreamReader(is);
						BufferedReader reader = new BufferedReader(isReader);
						StringBuilder sb = new StringBuilder();
						String line = null;
						while((line = reader.readLine())!=null){
							sb.append(line+"\n");
							
						}
						is.close();
						json = sb.toString();
						Log.d(TAG, "json string: "+json);
						return json;
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						Log.d(TAG, "ERROR",e);
						e.printStackTrace();
						return errorString;
					}catch(IOException e){
						Log.d(TAG, "ERROR",e);
						e.printStackTrace();
						return errorString;
					}catch(Exception e){
						Log.d(TAG, "ERROR",e);
						e.printStackTrace();
						return errorString;
						
					}
				
				}catch(Exception e){
					Log.d(TAG, "ERROR",e);
					e.printStackTrace();
					return  errorString;
					
				}
			}
			;
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				progressDialog.dismiss();
				if(result== errorString){
					Toast.makeText(context, result, Toast.LENGTH_LONG);
					Log.d(TAG, result);
				}else{
				try {
					//array=new JSONArray(result);
					Object json = new JSONTokener(result).nextValue();
					if (json instanceof JSONObject){
						object = new JSONObject(result);
						communicationResponse.onSuccess(communicationId,object);
					}else if (json instanceof JSONArray){
						array = new JSONArray(result);
						communicationResponse.onSuccess(communicationId,array);
					}
					
					//Log.d(TAG, "result object: "+object);
					//Response response = new Response(object);
					/*if(response.isSuccess()){
						communicationResponse.onSuccess(object);
					}else{
						communicationResponse.onError(response.getMessage());
					}*/
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d(TAG, "ERROR",e);
					e.printStackTrace();
				}
				}
				
			}
		};
		async.execute(URL);
	}
	
	
	public void downloadFile (final CommunicationResponse communicationResponse,String fileUrl,String fileName){
		//StringBuilder builder = new StringBuilder();
		//builder.append(apiUrl);
		//builder.append(path);
		//builder.append(queryParams);
		URL = fileUrl;
		//final String filename = fileName; 
		Log.d(TAG, "Url: "+URL);
		
		AsyncTask<String,String,String> async = new AsyncTask<String,String,String>(){
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage("Loading...");
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setCancelable(true);
				progressDialog.show();
			}


			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				int byteCount;
				try{
					StrictMode.ThreadPolicy policy = new StrictMode.
					ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy); 
					
					
					
					 URL url = new URL(arg0[0]);
		             URLConnection connection = url.openConnection();
		             connection.connect();
					
					
					long lengthOfFile = connection.getContentLength();
					
					
					String fileName = URLUtil.guessFileName(URL, null, MimeTypeMap.getFileExtensionFromUrl(URL));
					File folder = new File(Environment.getExternalStorageDirectory().toString(),".CourseRetriever");
					File file = new File(folder,fileName);
					if (!folder.isDirectory()){
		                folder.mkdir();
					}
					try {
			            file.createNewFile();
			        } catch (IOException e1) {
			            e1.printStackTrace();
			        }
					InputStream is = new BufferedInputStream(url.openStream());
					
					OutputStream os = new FileOutputStream(file);
					
					byte[] buffer = new byte[1024];
					
					long total = 0;
					
					while((byteCount = is.read(buffer))!= -1){
						total += byteCount;
						progressDialog.setProgress((int) ((total * 100) / lengthOfFile));
						os.write(buffer, 0, byteCount);
					}
					os.flush();
					os.close();
					is.close();
					
					return fileName;
					
				
				}catch(Exception e){
					Log.d(TAG, "ERROR",e);
					e.printStackTrace();
					return  errorString;
					
				}
			}
			;
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				progressDialog.dismiss();
				if(result== errorString){
					Toast.makeText(context, result, Toast.LENGTH_LONG).show();
					Log.d(TAG, result);
				}else{
					//Toast.makeText(context, "File Downloaded", Toast.LENGTH_LONG).show();
					showPdf(URL);
				}
				
			}
			
			
		};
		async.execute(URL);
		
	}
	public void showPdf(String url)
    {
		String fileName = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url));
		File file = new File(Environment.getExternalStorageDirectory().toString()+"/.CourseRetriever/"+fileName);
        PackageManager packageManager = context.getPackageManager();
        String type = MimeTypeMap.getFileExtensionFromUrl(url);
        Log.d(TAG, "type: "+type);
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/"+type);
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        if(type.equals("pdf")){
        	 intent.setDataAndType(uri, "application/pdf");
		}
        else if(type.equals("doc")){
			 intent.setDataAndType(uri,"application/msword");
		}
        else if(type.equals("ppt")){
			intent.setDataAndType(uri,"application/vnd.ms-powerpoint");
		}
        else if(type.equals("xls")){
			intent.setDataAndType(uri,"application/vnd.ms-excel");
		}else{
			intent.setDataAndType(uri,"text/plain");
		}
       
        context.startActivity(intent);
    }

    public void downloadFile2 (final CommunicationResponse communicationResponse,String fileUrl,String fileName){
        //StringBuilder builder = new StringBuilder();
        //builder.append(apiUrl);
        //builder.append(path);
        //builder.append(queryParams);
        URL = fileUrl;
        //final String filename = fileName;
        Log.d(TAG, "Url: "+URL);

        AsyncTask<String,String,String> async = new AsyncTask<String,String,String>(){

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(true);
                progressDialog.show();
            }


            @Override
            protected String doInBackground(String... arg0) {
                // TODO Auto-generated method stub
                int byteCount;
                try{
                    StrictMode.ThreadPolicy policy = new StrictMode.
                            ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);



                    URL url = new URL(arg0[0]);
                    URLConnection connection = url.openConnection();
                    connection.connect();


                    long lengthOfFile = connection.getContentLength();


                    String fileName = URLUtil.guessFileName(URL, null, MimeTypeMap.getFileExtensionFromUrl(URL));
                    File folder = new File(Environment.getExternalStorageDirectory().toString(),".CourseRetriever");
                    File file = new File(folder,fileName);
                    if (!folder.isDirectory()){
                        folder.mkdir();
                    }
                    try {
                        file.createNewFile();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    InputStream is = new BufferedInputStream(url.openStream());

                    OutputStream os = new FileOutputStream(file);

                    byte[] buffer = new byte[1024];

                    long total = 0;

                    while((byteCount = is.read(buffer))!= -1){
                        total += byteCount;
                        progressDialog.setProgress((int) ((total * 100) / lengthOfFile));
                        os.write(buffer, 0, byteCount);
                    }
                    os.flush();
                    os.close();
                    is.close();

                    return fileName;


                }catch(Exception e){
                    Log.d(TAG, "ERROR",e);
                    e.printStackTrace();
                    return  errorString;

                }
            }
            ;
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                progressDialog.dismiss();
                if(result== errorString){
                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                    Log.d(TAG, result);
                }else{
                    //Toast.makeText(context, "File Downloaded", Toast.LENGTH_LONG).show();
                    shareFile(URL);
                }

            }


        };
        async.execute(URL);

    }

    public void shareFile(String url)
    {
        String fileName = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url));
        File file = new File(Environment.getExternalStorageDirectory().toString()+"/.CourseRetriever/"+fileName);
        PackageManager packageManager = context.getPackageManager();
        String type = MimeTypeMap.getFileExtensionFromUrl(url);
        Log.d(TAG, "type: "+type);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        Uri uri = Uri.fromFile(file);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        if(type.equals("pdf")){
            shareIntent.setType("application/pdf");
        }
        else if(type.equals("doc")){
            shareIntent.setType("application/msword");
        }
        else if(type.equals("ppt")){
            shareIntent.setType("application/vnd.ms-powerpoint");
        }
        else if(type.equals("xls")){
            shareIntent.setType("application/vnd.ms-excel");
        }else{
            shareIntent.setType("text/plain");
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share To: "));
    }
	
	public void sendForImage (final CommunicationImage activity,String url){
		StringBuilder builder = new StringBuilder();
		URL = url;
		Log.d(TAG, "Url: "+URL);
		
		AsyncTask<String,Object,Bitmap> async = new AsyncTask<String,Object,Bitmap>(){
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				progressDialog2 = new ProgressDialog(context);
				progressDialog2.setMessage("Loading...");
				progressDialog2.show();
			}


			@Override
			protected Bitmap doInBackground(String... arg0) {
				// TODO Auto-generated method stub
			
				try{
					StrictMode.ThreadPolicy policy = new StrictMode.
					ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy); 
					
					//java.net.URL url = new java.net.URL(URL);
					//HttpURLConnection con =(HttpURLConnection) url.openConnection();
					
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(arg0[0]);
					HttpResponse httpResponse = httpClient.execute(httpGet);
					statusCode=httpResponse.getStatusLine().getStatusCode();
					HttpEntity entity = httpResponse.getEntity();
					InputStream is = entity.getContent();
					Bitmap bitmap = BitmapFactory.decodeStream(is);
					return bitmap;
					
				
				}catch(Exception e){
					Log.d(TAG, "ERROR",e);
					e.printStackTrace();
					return null;
					
					
				}
			}
			
			@Override
			protected void onPostExecute(Bitmap bitmap) {
				// TODO Auto-generated method stub
				try {
			        if ((progressDialog2 != null) && progressDialog2.isShowing()) {
			            progressDialog2.dismiss();
			        }
			    } catch (final IllegalArgumentException e) {
			        // Handle or log or ignore
			    } catch (final Exception e) {
			        // Handle or log or ignore
			    } finally {
			        progressDialog2 = null;
			    }  
				if(bitmap == null){
					
				}else{
					try {
					
						activity.setImage(bitmap);
					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.d(TAG, "ERROR",e);
						e.printStackTrace();
					}
				}
			}
				
			
		};
		async.execute(URL);
	}
	public JSONArray getResult(){
		return this.array;
	}
	
	
	
	public static String getQueryString(HashMap<String,Object> map){
		String query="";
		
		
		for(Map.Entry<String, Object> entry: map.entrySet()){
			if(query==""){
				query=query+"?"+entry.getKey()+"="+entry.getValue();
			}else{
				query=query+"&"+entry.getKey()+"="+entry.getValue();
			}
			
		}
		return query;
	}
		
	

}
