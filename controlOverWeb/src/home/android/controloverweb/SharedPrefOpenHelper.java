package home.android.controloverweb;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class SharedPrefOpenHelper {

	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final String UNREAD_NOTIFICATION_INTRUSION = "notification_intrusion";
	private static final String UNREAD_NOTIFICATION_FIRE = "notification_fire";
	
	Context context;
	
	public SharedPrefOpenHelper(Context context){
		this.context = context;
	}
	public SharedPrefOpenHelper(){
		
	}
	public String getRegistrationId(){
		
		final SharedPreferences prefs =  getpreferences();
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		
		if(registrationId.isEmpty()){
			//"Registration not found."
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
		
		float registeredVersion = prefs.getFloat(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentversion = getAppVersion();
		
		if(registeredVersion != currentversion){
			//APP Version Changed
			return "";
		}
		return registrationId;
	}
	
	public int getNotificationIntrusion(){
		
		final SharedPreferences prefs =  getpreferences();
		int numNotification = prefs.getInt(UNREAD_NOTIFICATION_INTRUSION, 0);
		
		return numNotification;
	}
	
	public void SetNotificationIntrusion(int number){
		
		final SharedPreferences prefs =  getpreferences();
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putInt(UNREAD_NOTIFICATION_INTRUSION, number);
		
		editor.commit();
	}

	private int getAppVersion() {
		// TODO Auto-generated method stub
		
		try {
			PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	        
		} catch(NameNotFoundException Exc){
			 throw new RuntimeException("Could not get package name: " + Exc);
		}
	}

	private SharedPreferences getpreferences() {
		// TODO Auto-generated method stub
		
		return context.getSharedPreferences(MainActivity.class.getSimpleName(),
	            Context.MODE_PRIVATE);
	}

	public void storeRegistrationId(String regid) {
		// TODO Auto-generated method stub
		final SharedPreferences prefs =  getpreferences();
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putString(PROPERTY_REG_ID, regid);
		editor.putFloat(PROPERTY_APP_VERSION, getAppVersion());
		
		editor.commit();
		
	}
	public int getNotificationFire() {
		// TODO Auto-generated method stub
		final SharedPreferences prefs =  getpreferences();
		int numNotification = prefs.getInt(UNREAD_NOTIFICATION_FIRE, 0);
		
		return numNotification;
	}
	public void SetNotificationFire(int number) {
		// TODO Auto-generated method stub
		final SharedPreferences prefs =  getpreferences();
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putInt(UNREAD_NOTIFICATION_FIRE, number);
		
		editor.commit();
	}
}
