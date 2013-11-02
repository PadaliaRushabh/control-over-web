package home.android.controloverweb;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

public class CheckApplicationLife {

	
	Context context;
	public CheckApplicationLife(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	public boolean isAppRunning(){
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		
		List<RunningTaskInfo> services = manager.getRunningTasks(Integer.MAX_VALUE);
		
		if(services.get(0).topActivity.getPackageName().toString()
						.equalsIgnoreCase(context.getPackageName().toString())){
			 return true;
		}
		
		return false;
	}
}
