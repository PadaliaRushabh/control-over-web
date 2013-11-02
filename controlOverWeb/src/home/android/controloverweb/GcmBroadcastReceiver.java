package home.android.controloverweb;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;


public class GcmBroadcastReceiver  extends WakefulBroadcastReceiver{

	 //private static final String NOTIFICATION_DELETED_ACTION = "NOTIFICATION_DELETED";
	 SharedPrefOpenHelper prefs = new SharedPrefOpenHelper();
	 CheckApplicationLife app;
	@Override
	public void onReceive(Context context, Intent intent) {
		app = new CheckApplicationLife(context);
		// TODO Auto-generated method stub
		// Explicitly specify that GcmIntentService will handle the intent.
		
		ComponentName comp = new ComponentName(context.getPackageName() , GcmIntentService.class.getName());
		intent.putExtra("isAppRunning",app.isAppRunning());
		startWakefulService(context, (intent.setComponent(comp)));
		setResultCode(Activity.RESULT_OK);
		
	}
	

}
