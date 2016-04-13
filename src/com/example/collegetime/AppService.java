package com.example.collegetime;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;

public class AppService extends Service {

	static final String TAG = "AppService";

	DatabaseHelperAdapter database_helper;
	String setting[]=new String[6];
	String data[][]=new String[20][20];
	static PendingIntent pendingIntent;
	@Override
	public void onCreate() {
		super.onCreate();
		database_helper = new DatabaseHelperAdapter(this);
		//Toast.makeText(this, "OnCreate", Toast.LENGTH_SHORT).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Toast.makeText(this, "OnStartCommand", Toast.LENGTH_SHORT).show();
		Calendar c = Calendar.getInstance(); 
	    int minute = c.get(Calendar.MINUTE);
	    int hour=c.get(Calendar.HOUR);
	    int AM_OR_PM=c.get(Calendar.AM_PM);//if AM it returns 0 and if PM it returns 1
	    
		setting=database_helper.getSettings();
		
		Intent intentt = new Intent();
		startId=5;
	    AlarmManager alarmmanager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		
	    intentt.setClass(this, AppService.class);
	    	 pendingIntent = PendingIntent.getService(this, 0, intentt, PendingIntent.FLAG_UPDATE_CURRENT);
	    
	    AudioManager audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	  // if((minute>=4&&minute<=5)||(minute>=39&&minute<=40)){
	  
	    alarmmanager.set(AlarmManager.RTC_WAKEUP,30*1000, pendingIntent);
	    int mode;
	    if(AM_OR_PM==1)
	    mode=getCurrentMode(hour+12, minute);  
	    else 
	    	mode=getCurrentMode(hour, minute);
	    // Message.message(getApplicationContext(), ""+mode);
	   if(mode==1)
	    audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
	   else if(mode==0)
		   audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	  // }
	

	  //  Message.message(getApplicationContext(),""+hour+":"+minute);
		
		if(Integer.parseInt(setting[5])==0)
		{
			pendingIntent.cancel();
			this.stopSelf();
			
			//startService(new Intent(this, AppService2.class));
			//stopService(new Intent(this, AppService.class));
			
		}
	    
	    
		return super.onStartCommand(intent, flags, startId);
	}

	
	public int getCurrentMode(int hour,int minute){
		Calendar c = Calendar.getInstance(); 
		int period_row=c.get(Calendar.DAY_OF_WEEK)-1;
		if(period_row==0)//here checking for sunday on sunday app will be normal
			return 0;
		data=database_helper.getAllData();
		int lunch_start_hour , lunch_start_minute, lunch_end_hour , lunch_end_minute;
		int period_column=-1;
		int no_of_periods=Integer.parseInt(setting[0]);
		int period_duration=Integer.parseInt(setting[1]);
		int lunch_duration=Integer.parseInt(setting[4]);
		
	    int college_start_hour=Integer.parseInt(setting[2].substring(0,setting[2].indexOf(':')));
	    int college_start_minute=Integer.parseInt(setting[2].substring(setting[2].indexOf(':')+1));
	    int college_end_hour,college_end_minute;
	    if(lunch_duration==0){
	    college_end_minute=(college_start_minute+no_of_periods*period_duration)%60;//considering no lunch and if lunch 
	    college_end_hour=college_start_hour+((college_start_minute+no_of_periods*period_duration)/60);
	    }
	    else{
	    	college_end_minute=(college_start_minute+no_of_periods*period_duration+lunch_duration)%60;//considering no lunch and if lunch 
		    college_end_hour=college_start_hour+((college_start_minute+no_of_periods*period_duration+lunch_duration)/60);
		    
	    }
	    
	    int lunch_after_period=Integer.parseInt(setting[3]);
	    
  if(lunch_duration!=0){
	    	
		    lunch_start_minute=(college_start_minute+lunch_after_period*period_duration)%60;
		    lunch_start_hour=college_start_hour+((college_start_minute+lunch_after_period*period_duration)/60);
		    lunch_end_minute=(lunch_start_minute+lunch_duration)%60;
		    lunch_end_hour=lunch_start_hour+((lunch_start_minute+lunch_duration)/60);
		    //if(hour>lunch_end_hour)
		    	//period_column--;

		   // Message.message(getApplicationContext(),""+lunch_start_hour+":"+lunch_start_minute+" "+college_start_hour+" "+college_start_minute);
		   // Message.message(getApplicationContext(),""+lunch_end_hour+":"+lunch_end_minute+" "+lunch_after_period+" "+period_duration);
		    
		    	if((hour==lunch_start_hour && hour!=lunch_end_hour && minute>lunch_start_minute )||(hour==lunch_end_hour && hour!=lunch_start_hour && minute <= lunch_end_minute)||(hour==lunch_start_hour && hour==lunch_end_hour && minute>lunch_start_minute && minute<=lunch_end_minute))//condition for being in lunch
		    	{
		    		//Message.message(getApplicationContext(),""+lunch_start_hour+":"+lunch_start_minute+" "+college_start_hour+" "+college_start_minute);
		    
		    	return 0;
		    	}
		    }
	    
	    /*int total_minute=(hour-college_start_hour)*60+minute;
	    int period_column=(total_minute/period_duration);*/
	   
	  //  Message.message(getApplicationContext(), ""+hour+":"+minute);
	    //Message.message(getApplicationContext(), ""+college_start_hour+":"+college_start_minute);
	    
	    if(hour<college_start_hour)
	    	return 0;
	    
	    if((hour>college_end_hour)||(hour==college_end_hour && minute>college_end_minute))
	    return 0;
	    
	    
	    
	    if(hour==college_start_hour&&minute<college_start_minute)
	      	return 0;
	    
	    if((hour>college_start_hour )||(hour==college_start_hour && minute>=college_start_minute))
	    {
	    	do{
	    	college_start_minute+=period_duration;
	    	college_start_hour+=college_start_minute/60;
	    	college_start_minute=college_start_minute%60;
	    	period_column++;
	    	}while((college_start_hour<hour)||(college_start_hour==hour && college_start_minute<minute));
	    	
	    }
	    
	  
	    //period_row=c.get(Calendar.DAY_OF_WEEK)-1;//it returns 1 for sunday , 2 for monday and so on 
	    
	    //if(period_row==0)//it is done temporarily to test the app on sunday otherwise phone will be vibration off on sunday
	    	//period_row=6;
	   
	    String s=data[period_row][period_column];
	   if(s.equals("")){
		   return 0;
	   }
	   else
		   return 1;
	
		
	}
	
	public void destroy(){
		pendingIntent.cancel();
		this.stopSelf();
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		//Toast.makeText(this, "OnDestroy", Toast.LENGTH_SHORT).show();
		//AudioManager audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		//audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		//Log.d(TAG, "OnDestroy");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
