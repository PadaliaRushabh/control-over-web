package home.android.controloverweb;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;


public class TaskFragment extends Fragment {
	
	private static final String TAG = TaskFragment.class.getSimpleName();
	GoogleCloudMessaging gcm;
	GoogleFunctions google ;
	
	String regid;
	String SENDER_ID = "815606489158";
	//private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000; // google play service error dialog
	
	SharedPrefOpenHelper pref;
	
	static interface TaskCallbacks{
		
		 public void onPreExecute();
		 public void onProgressUpdate(int percent);
		 public void onCancelled();
		 public void onPostExecute();
		 public void passDataToActivity(String homeTemp , String OutsideTemp , String Unit
				 , String TimeTemp , String Intrusion , String Fire);
	}
	
	private TaskCallbacks mCallbacks;
	private getDataFromServer getDataFromServerTask;
	//private registerInBackground registerInBackgroundTask;
	
	private boolean isRunning_serverTask;
	private boolean isRunning_RegisterBackgroundTask;
	
	public void passDataToActivity(String homeTemp , String OutsideTemp , String Unit
			 , String TimeTemp , String Intrusion , String Fire){
		mCallbacks.passDataToActivity(homeTemp, OutsideTemp, Unit, TimeTemp, Intrusion, Fire);
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		if(!(activity instanceof TaskCallbacks)){
			throw new IllegalStateException("Activity must implement the TaskCallback Interface");
		}
		mCallbacks = (TaskCallbacks) activity;
		
		pref = new SharedPrefOpenHelper(getActivity());
		google = new GoogleFunctions(getActivity());
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		cancel();
	}
	
	
	/** TASK FRAGMENT API**/
	
	public void start() {
		// TODO Auto-generated method stub
		if(!isRunning_serverTask){
			getDataFromServerTask = new getDataFromServer();
			getDataFromServerTask.execute();
				
			isRunning_serverTask = true;	
		}
	}
	
	public void cancel() {
		// TODO Auto-generated method stub
		if(isRunning_serverTask){
			getDataFromServerTask.cancel(false);
			getDataFromServerTask = null;
				
			isRunning_serverTask = false;	
		}	
	}
	
	public boolean isRunningServerTask(){
		return isRunning_serverTask;
	}
	
	public boolean isRunningRegisterInBackgroundTask(){
		return isRunning_RegisterBackgroundTask;
	}
	
	private class getDataFromServer extends AsyncTask<Void, Void, Void>{
		MongoDBOpenHelper mongoHelper;
		
		 String homeTemp ;
		 String outsideTemp ;
		 String unit;
		 String temptime ;
		 String intrusion ;
		 String fire;
		
		@Override
		protected Void doInBackground(Void... ignore) {
			// TODO Auto-generated method stub
			
			serverFetch();
			
			if(gcm == null){
				gcm = GoogleCloudMessaging.getInstance(getActivity());
			}
			//serverFetch();
			if(google.checkPlayService()){
				gcm = GoogleCloudMessaging.getInstance(getActivity());
				regid = pref.getRegistrationId();
				if(regid.isEmpty() || regid == null || regid == ""){
					try {
						regid = gcm.register(SENDER_ID);
						if(!regid.isEmpty() || regid != null || regid != ""){
							sendRegistrationIdToBackend(regid);
							pref.storeRegistrationId(regid);
						}
						
					} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			}
				
			return null;
		}
		
		private void sendRegistrationIdToBackend(String regid) {
			// TODO Auto-generated method stub
			try{
				//System.out.println("redid:" + regid);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost("http://controloverweb.herokuapp.com/register");
				//HttpPost httpPost = new HttpPost("http://192.168.0.109:3000/register");
				
				List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
				nameValuePair.add(new BasicNameValuePair("registerID", regid));
			
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
				
				HttpResponse response = httpClient.execute(httpPost);
				
				String content = EntityUtils.toString(response.getEntity());
				//System.out.println("content:" + content);				
				
			} catch (ClientProtocolException e) {
				System.out.println(e);
				//showToast(ERROR_UNKNOW);
				return;
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		    	System.out.println(e);
		    	//showToast(ERROR_UNKNOW);
		    	return;
		        // TODO Auto-generated catch block
		    }
			catch(MongoException e){
				System.out.println(e);
				//showToast(ERROR_UNKNOW);
		    	return;
			}
			catch (Exception e){
				System.out.println(e);
				//showToast(ERROR_UNKNOW);
		    	return;
			}
			
	}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			mCallbacks.onPreExecute();
			isRunning_serverTask = true;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		
			passDataToActivity(homeTemp ,outsideTemp,unit,temptime ,intrusion,fire );
			mCallbacks.onPostExecute();
			isRunning_serverTask = false;
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			
			mCallbacks.onCancelled();
			isRunning_serverTask = false;
		}
		
	
	private void serverFetch(){
		try {
			mongoHelper = new MongoDBOpenHelper();
			DBObject DBObj = mongoHelper.getTemperature();
			DBObject  DBObjIntrusion = mongoHelper.getIntrusion();
			DBObject  DBObjFire = mongoHelper.getFire();
			//mongoHelper.getIntrusionAndFire();
			
			homeTemp = DBObj.get("home").toString();
			outsideTemp = DBObj.get("outside").toString();
			unit = DBObj.get("unit").toString();
			temptime = DBObj.get("time").toString();
			intrusion = DBObjIntrusion.get("time").toString();
			fire = DBObjFire.get("time").toString();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//showToast(ERROR_UNKNOW);
			return;
		}
		catch(MongoException e){
			System.out.println(e);
			//showToast(ERROR_UNKNOW);
	    	return;
		}
		catch (Exception e){
			System.out.println(e);
			//showToast(ERROR_UNKNOW);
	    	return;
		}
	}

	
}
	

}
