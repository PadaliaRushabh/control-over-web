package home.android.controloverweb;


import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements TaskFragment.TaskCallbacks {

	public static final String EXTRA_MESSAGE = "message";
	public static final String INTERNET_CONNECTION = "Unable to connect to Internet";
	public static final String ERROR_UNKNOW = "Something Bad happned\nCheck your Internet connection";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000; // google play service error dialog
    public static final int NOTIFICATION_INTRUSION_FIRE_ID = 1;

	String SENDER_ID = "815606489158";
	static final String TAG = "GCMDemo";
	boolean completed = false;
	
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	//SharedPreferences prefs;
	Context context;
	private TaskFragment mTaskFragment; 
	String regid;
	
	SharedPrefOpenHelper pref = new SharedPrefOpenHelper(this);
	MongoDBOpenHelper mongoHelper;
	InternetConnection connection = new InternetConnection(this);
	
	GoogleFunctions google = new GoogleFunctions(this);
	
	TextView home , outside , temp_time;
	TextView textView_intrusion_value , textView_fire_value ,textView_intrusion_time , textView_fire_time;
	ProgressBar bar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		bar = (ProgressBar) findViewById(R.id.Progress);
		
		if (savedInstanceState != null) {
			
			completed = savedInstanceState.getBoolean("completed", false);
			if(completed){
				bar.setVisibility(View.GONE);
				bar.setIndeterminate(false);
			}
			
		}
		Bundle extras = getIntent().getExtras(); 
		
		if(extras != null){
			NotificationManager mNotificationManager = (NotificationManager)
	                this.getSystemService(Context.NOTIFICATION_SERVICE);
				
			mNotificationManager.cancel(NOTIFICATION_INTRUSION_FIRE_ID);

		}
		
		pref.SetNotificationIntrusion(0);
		pref.SetNotificationFire(0);
			
		home = (TextView)findViewById(R.id.textView_home);
		outside = (TextView)findViewById(R.id.textView_outside);
		temp_time = (TextView)findViewById(R.id.textView_temp_time);
		
		textView_intrusion_value = (TextView)findViewById(R.id.textView_intrusion_value);
		textView_fire_value = (TextView)findViewById(R.id.textView_fire_value);
		textView_intrusion_time = (TextView)findViewById(R.id.textView_intrusion_time);
		textView_fire_time = (TextView)findViewById(R.id.textView_fire_time);
		
		context = getApplicationContext();
		
		FragmentManager frm = getFragmentManager();
		mTaskFragment = (TaskFragment) frm.findFragmentByTag("task");
		
		if(mTaskFragment == null){
			System.out.println("taskfra");
			mTaskFragment = new TaskFragment();
			frm.beginTransaction().add(mTaskFragment, "task").commit();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putBoolean("completed", completed);
		outState.putString("homeTemp", home.getText().toString());
		outState.putString("OutsideTemp", outside.getText().toString());
		outState.putString("TimeTemp", temp_time.getText().toString());
		outState.putString("Intrusion", textView_intrusion_value.getText().toString());
		outState.putString("Fire", textView_fire_value.getText().toString());
		outState.putString("LastChecked",textView_intrusion_time.getText().toString() );
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		completed = savedInstanceState.getBoolean("completed");
		
		home.setText(savedInstanceState.getString("homeTemp")) ;
		outside.setText(savedInstanceState.getString("OutsideTemp")); 
		temp_time.setText(savedInstanceState.getString("TimeTemp"));
		
		textView_intrusion_value.setText(savedInstanceState.getString("Intrusion")); 
		textView_fire_value.setText(savedInstanceState.getString("Fire")); 
		
		textView_intrusion_time.setText(savedInstanceState.getString("LastChecked")); 
		textView_fire_time.setText(savedInstanceState.getString("LastChecked")); 
		
	}
	public void afterAttach(){
		if(connection.isConnectedToInternet() == true){
			mTaskFragment.start();
		}
		else{
			showToast(INTERNET_CONNECTION);
			bar.setVisibility(View.GONE);
			bar.setIndeterminate(false);
		}
	}
	public void showToast(String Message){
		Toast toast = Toast.makeText(this, Message , Toast.LENGTH_LONG);
		toast.show();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//checkPlayService();
		if(!completed){
			afterAttach();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId()){
		case R.id.action_refresh:
			if(connection.isConnectedToInternet() == true){
				//new getDataFromServer().execute();
				mTaskFragment.start();
			}
			else{
				showToast(INTERNET_CONNECTION);
			}
			return true;		
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPreExecute() {
		// TODO Auto-generated method stub
		completed = false;
		bar.setVisibility(View.VISIBLE);
		bar.setIndeterminate(true);
	}
	@Override
	public void onProgressUpdate(int percent) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCancelled() {
		// TODO Auto-generated method stub
		bar.setVisibility(View.GONE);
		bar.setIndeterminate(false);
	}
	@Override
	public void onPostExecute() {
		// TODO Auto-generated method stub
		bar.setVisibility(View.GONE);
		bar.setIndeterminate(false);
	}
	@Override
	public void passDataToActivity(String homeTemp, String OutsideTemp,
			String Unit, String TimeTemp, String Intrusion, String Fire) {
		// TODO Auto-generated method stub
		home.setText("Home Temperature\n\n" + homeTemp + " " + Unit);
		outside.setText("Outside Temperature\n\n" + OutsideTemp + " " + Unit);
		temp_time.setText("Last updated at: "+ TimeTemp );
		
		textView_intrusion_value.setText("Last Intrusion Detection at\n\n" + Intrusion);
		textView_fire_value.setText("Last Fire Detection at\n\n" + Fire);
		textView_intrusion_time.setText("Last Checked At:" + new Date()); 
		textView_fire_time.setText("Last Checked At:" + new Date());
		
		completed = true;
	}

}
