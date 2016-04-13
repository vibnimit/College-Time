package com.example.collegetime;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

public class MainMenu extends Activity {

	Button viewButton;
	Button manageButton;
	Button ringButton, silentButton;
	// Button vibrate;
	DatabaseHelperAdapter database_helper;
	ToggleButton tb;
	static int service;

	// DatabaseHelperAdapter database_helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		// database_helper = new DatabaseHelperAdapter(this);
		database_helper = new DatabaseHelperAdapter(this);
		viewButton = (Button) findViewById(R.id.button_view);
		manageButton = (Button) findViewById(R.id.button_manage);
		ringButton = (Button) findViewById(R.id.ring_mode);
		silentButton = (Button) findViewById(R.id.silent_mode);
		// vibrate=(Button) findViewById(R.id.vibrator);
		tb = (ToggleButton) findViewById(R.id.toggle);
		Intent serviceIntent = new Intent();
		serviceIntent.setClass(this, AppService.class);
		String[] setting = new String[6];
		setting = database_helper.getSettings();
		service = Integer.parseInt(setting[5]);
		// bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
		if (service == 1)
			tb.setChecked(true);
		else
			tb.setChecked(false);
		viewButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						TimeTableActivity.class);
				/*
				 * Don't know why, but defining this intent outside the class,
				 * at the starting of the class MainMenu results in an RUN TIME
				 * ERROR....to be investigated later
				 */

				intent.putExtra("MANAGE_BUTTON", false);
				startActivity(intent);

				// data=database_helper.getAllData();
				// Message.message(MainMenu.this, data);

			}
		});
		manageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Message.message(MainMenu.this, "hello");
				Intent intent = new Intent(MainMenu.this,
						TimeTableActivity.class);
				// Message.message(MainMenu.this, "hiii");
				intent.putExtra("MANAGE_BUTTON", true);
				// Message.message(MainMenu.this, "byeee");
				startActivity(intent);
				// Message.message(MainMenu.this, "gud bye");
			}
		});

		silentButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String str= new String();
				if(tb.isChecked())
				{
				tb.setChecked(false);
				database_helper.insert_setting(5, "0");
				str=" Auto Silent Mode Off.";
				}
				str= "Phone will go to Silent Mode."+str;
				AudioManager audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				 audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
				 Message.message(getApplicationContext(), str);
				 NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			    	notificationManager.cancelAll();
			}
		});
		ringButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String str= new String();
				if(tb.isChecked())
				{
				tb.setChecked(false);
				str=" Auto Silent Mode Off.";
				database_helper.insert_setting(5, "0");
				}
				str= "Phone will go to Ringing Mode."+str;
				 AudioManager audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				 Message.message(getApplicationContext(), str);
				 NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			    	notificationManager.cancelAll();
			}
		});

		/*
		 * vibrate.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { //Intent intent=new Intent(
		 * getApplicationContext(), TimeTableActivity.class);
		 * //intent.putExtra("MANAGE_BUTTON",true); //startActivity(intent);
		 * //Intent intent=new Intent( getApplicationContext(),
		 * AppService.class); //startService(intent);
		 * 
		 * 
		 * } });
		 */

	}

	public void createNotification(MainMenu mainMenu) {
	    // Prepare intent which is triggered if the
	    // notification is selected
	    Intent intent = new Intent(this, MainMenu.class);
	    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

	    // Build notification
	    // Actions are just fake
	    Notification noti = new Notification.Builder(this)
	        .setContentTitle("Auto Silent Mode ON")
	        .setContentText("Press to open College Time").setSmallIcon(R.drawable.ic_launcher)
	        .setContentIntent(pIntent)
	        .build();
	    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	    // hide the notification after its selected
	    noti.flags |= Notification.FLAG_NO_CLEAR;
	   notificationManager.notify(0, noti);

	  }
	
	public void onToggleClicked(View view) {
		// Is the toggle on?
		boolean on = ((ToggleButton) view).isChecked();

		if (on) {
			database_helper.insert_setting(5, "1");
			Message.message(getApplicationContext(), "service on");
			Intent intent = new Intent(getApplicationContext(),
					AppService.class);
			startService(intent);
			createNotification(this);
			// Intent intent=new Intent( getApplicationContext(),
			// AppService.class);
			// startService(intent);
			// intent.addCategory(AppService.TAG);

			// this.startService(new Intent(this, AppService.class));

			// Enable vibrate
		} else {
			// stopService(new Intent(MainMenu.this, AppService.class));
			// service =0;
			database_helper.insert_setting(5, "0");
			Message.message(getApplicationContext(), "Wait! for 5 seconds....");
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	    	notificationManager.cancelAll();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

}
