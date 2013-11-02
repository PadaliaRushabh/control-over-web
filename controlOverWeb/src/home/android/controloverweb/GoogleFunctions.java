package home.android.controloverweb;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class GoogleFunctions {

	Context context;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000; 
	public GoogleFunctions(Context context){
		this.context = context;
	}
	protected boolean checkPlayService(){
	
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.context);
		if(resultCode != ConnectionResult.SUCCESS){
			if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
				GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context , PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				System.out.println("This device is not supported");
				//finish();
			}
			return false;
		} else{
			System.out.println("This device is supported");
		}
			return true;
	}
}
