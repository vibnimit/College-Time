package com.example.collegetime;


import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;

public class SplashScreen extends Activity {
	
	DatabaseHelperAdapter database_helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int currentAPIVersion = android.os.Build.VERSION.SDK_INT;

		if (currentAPIVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {

		    // RUN THE CODE SPECIFIC TO THE API LEVELS ABOVE HONEYCOMB (API 11+)   
		    ActionBar actionBar = getActionBar();
		    actionBar.hide();
		}
		setContentView(R.layout.splash);
		database_helper = new DatabaseHelperAdapter(this);
		final Intent intent;
		try {

			boolean firstboot = getSharedPreferences("BOOT_PREF", MODE_PRIVATE)
					.getBoolean("firstboot", true);

			if (firstboot) {
				database_helper.building_database();
				intent = new Intent(this, Settings.class);
				// initialize_database.setVisibility(View.GONE);
				//Message.message(getApplicationContext(),
				//		"Database created now you can modify and view it");
				// place your code that will run single time
				getSharedPreferences("BOOT_PREF", MODE_PRIVATE).edit()
						.putBoolean("firstboot", false).commit();

			}
			
			else
			intent = new Intent(this, MainMenu.class);
		}
		finally {

		}
		
	
		Thread logo_timer = new Thread() {

			public void run() {
				try {
					int logotimer = 0;
					while (logotimer < 2000) {
						sleep(100);
						logotimer += 100;
					}
					startActivity(intent);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					finish();
				}

			}

		};
		logo_timer.start();

		
		
	}

}
