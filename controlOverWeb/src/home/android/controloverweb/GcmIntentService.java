package home.android.controloverweb;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

public class GcmIntentService extends IntentService {

	public static final int NOTIFICATION_INTRUSION_FIRE_ID = 1;
	public static final int NOTIFICATION_OTHER_ID = 2;
	
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    private  String FROM_NOTIFICATION = "from_notification";
    
    SharedPrefOpenHelper prefs = new SharedPrefOpenHelper(this);
    String type , time;
    String contentText;
    
    boolean isAppRunning;
    
    int numMessage ;
    
	public GcmIntentService() {
		super("GcmIntentService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		
		isAppRunning = intent.getBooleanExtra("isAppRunning" , false);
		
		String messageType = gcm.getMessageType(intent);
		
		if(!extras.isEmpty()){
			if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)){
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +extras.toString());
            // If it's a regular GCM message, do some work.
			} else if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)){
				//Get message
				type =  extras.getString("type");
				//time = extras.getString("time");
				sendNotification(type);
			}
		}
		
		// Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String type) {
		// TODO Auto-generated method stub
		mNotificationManager = (NotificationManager)
	                this.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Intent intent = new Intent(this, MainActivity.class );
		intent.putExtra(FROM_NOTIFICATION, true);
		if(isAppRunning){
		  intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		}
		
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intent, 0);
	    if(type.equalsIgnoreCase("Intrusion")){
	    	 
	    	 prefs.SetNotificationIntrusion(prefs.getNotificationIntrusion() + 1);
	    	 
	    }
	    else  if(type.equalsIgnoreCase("Fire")){
	    	
	    	 prefs.SetNotificationFire( prefs.getNotificationFire() + 1);
	    }
	    numMessage = prefs.getNotificationIntrusion() +  prefs.getNotificationFire() ;
	    contentText = "Intrusion:" + prefs.getNotificationIntrusion() + "\nFire:" + prefs.getNotificationFire();
	    
	    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	    
	    long[] pattern = {0, 1000, 1000, 1000};
	    
	    NotificationCompat.Builder mBuilder =
	                new NotificationCompat.Builder(this)
	        .setSmallIcon(R.drawable.fire)
	        .setContentTitle("Control Over Web")
	        .setContentText(contentText)
	        .setNumber(numMessage)
	        .setSound(soundUri)
	        .setAutoCancel(true)
	        .setVibrate(pattern);

	    mBuilder.setContentIntent(contentIntent);
	    if(type.equalsIgnoreCase("Intrusion") || type.equalsIgnoreCase("Fire")){
	    	mNotificationManager.notify(NOTIFICATION_INTRUSION_FIRE_ID, mBuilder.build());
	    }
	    else{
	    	mNotificationManager.notify(NOTIFICATION_OTHER_ID, mBuilder.build());
	    }
	}

}
