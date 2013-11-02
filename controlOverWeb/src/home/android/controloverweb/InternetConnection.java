package home.android.controloverweb;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnection {

	private Context context ;
	
	public InternetConnection(Context context){
		this.context = context;
	}
	
	public boolean isConnectedToInternet(){
		
		ConnectivityManager Cmanager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if(Cmanager != null){
			NetworkInfo[] info = Cmanager.getAllNetworkInfo();
			
			if(info != null){
				for(int i=0 ; i < info.length ; i++){
					if(info[i].getState() == NetworkInfo.State.CONNECTED){
						return true;
					}
				}
			}
		}
		return false;
	}
}
