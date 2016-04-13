package com.example.collegetime;

import java.util.Calendar;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class Settings extends Activity implements android.widget.RadioGroup.OnCheckedChangeListener {
			Button strt_time,save_button,cancel_button;
				CheckBox chkbox;
				TextView txt_view1,txt_view2;
				Spinner spnr1,spnr2,spnr3,spnr4;
				int VISIBILITY=0;
				int top;
				private int hour;
				private int minute;
				RadioGroup screen_orientation;
				static int orientation;
				static final int TIME_DIALOG_ID = 999;
				DatabaseHelperAdapter database_helper;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
	strt_time=(Button)findViewById(R.id.start_time);
	save_button=(Button)findViewById(R.id.save_button);
	cancel_button=(Button)findViewById(R.id.cancel_button);
	chkbox=(CheckBox)findViewById(R.id.checkBox1);
	txt_view1=(TextView)findViewById(R.id.lunch_dura_text_view);
	txt_view2=(TextView)findViewById(R.id.lunch_after_period_text_view);
	spnr1=(Spinner)findViewById(R.id.lunch_duration);
	spnr2=(Spinner)findViewById(R.id.lunch_aftr_prd);
	spnr3=(Spinner)findViewById(R.id.period_no);
	spnr4=(Spinner)findViewById(R.id.period_duration);
	screen_orientation=(RadioGroup)findViewById(R.id.screen_orientation);
	//portrait=(RadioGroup)findViewById(R.id.portrait);
	database_helper = new DatabaseHelperAdapter(this);
	String[] setting=new String[6];
	setting=database_helper.getSettings();
	top=Integer.parseInt(setting[0]);
	spnr3.setSelection(top-4);
	spnr4.setSelection((Integer.parseInt(setting[1]))/5-6);
	spnr1.setSelection((Integer.parseInt(setting[4]))/5-2);
	spnr2.setSelection((Integer.parseInt(setting[3]))-1);
	strt_time.setText(setting[2]);
	
	if(Integer.parseInt(setting[6])==1)
	screen_orientation.check(R.id.landscape);
	else 
	screen_orientation.check(R.id.portrait);
	
	if(Integer.parseInt(setting[3])!=0&&Integer.parseInt(setting[3])!=0){
	txt_view1.setVisibility(View.VISIBLE);
		spnr1.setVisibility(View.VISIBLE);
	txt_view2.setVisibility(View.VISIBLE);
	spnr2.setVisibility(View.VISIBLE);
	chkbox.setChecked(true);
	VISIBILITY=1;
	}
	final Calendar c = Calendar.getInstance();
	hour = Integer.parseInt(setting[2].substring(0, setting[2].indexOf(':')));//presetting the value on the button from database
	minute = Integer.parseInt(setting[2].substring(setting[2].indexOf(':')+1));
	
	screen_orientation.setOnCheckedChangeListener(this);
	
	strt_time.setOnClickListener(new View.OnClickListener() {

		public void onClick(View v) {

			showDialog(TIME_DIALOG_ID);

		}

	});
	
			save_button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					database_helper.insert_setting( 0,spnr3.getSelectedItem().toString());
					database_helper.insert_setting( 1,spnr4.getSelectedItem().toString());
					database_helper.insert_setting( 3,spnr2.getSelectedItem().toString());
					database_helper.insert_setting( 4,spnr1.getSelectedItem().toString());
					database_helper.insert_setting( 2,pad(hour)+":"+pad(minute));
					if(VISIBILITY==0){//here i have to do just opposite bcoz value of visibility is changed in on checkedchange method
					database_helper.insert_setting( 3,"0");//putting lunch after period zero
					database_helper.insert_setting( 4,"0");//putting lunch duration to be zero coz lunch is not present
					}
					Intent intent =new Intent(getApplicationContext(),TimeTableActivity.class);
					intent.putExtra("MANAGE_BUTTON", true);
					finish();
					startActivity(intent);
				}
			});
			
			cancel_button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent =new Intent(getApplicationContext(),TimeTableActivity.class);
					intent.putExtra("MANAGE_BUTTON", true);
					finish();
					startActivity(intent);
				}
			});
		
		chkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				//Message.message(getApplicationContext(), "wow!!");
				if(VISIBILITY==1){
					txt_view1.setVisibility(View.INVISIBLE);
					spnr1.setVisibility(View.INVISIBLE);
					txt_view2.setVisibility(View.INVISIBLE);
					spnr2.setVisibility(View.INVISIBLE);
					VISIBILITY=0;
					//database_helper.insert_setting( 3,"0");
					//database_helper.insert_setting( 4,"0");
				}
					else{
					VISIBILITY=1;
				txt_view1.setVisibility(View.VISIBLE);
					spnr1.setVisibility(View.VISIBLE);
				txt_view2.setVisibility(View.VISIBLE);
				spnr2.setVisibility(View.VISIBLE);
					}
			}
		});
		
		//spnr3.setTop(7);
		spnr3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//database_helper.insert_setting( 0,spnr3.getSelectedItem().toString());
		
				spnr3.setSelection(arg2);
				
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		
		
		});
		
spnr4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
		//database_helper.insert_setting( 1,spnr4.getSelectedItem().toString());
		
		spnr4.setSelection(arg2);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
});


//for lunch after period
	spnr2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub

			//database_helper.insert_setting( 3,spnr2.getSelectedItem().toString());

			spnr2.setSelection(arg2);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	});


//for lunch duration
	spnr1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub

			//database_helper.insert_setting( 4,spnr1.getSelectedItem().toString());

			spnr1.setSelection(arg2);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	});

				/*private void hello() {
				// TODO Auto-generated method stub
				TimePickerDialog tpd = new TimePickerDialog(getApplicationContext(),
		        new TimePickerDialog.OnTimeSetListener() {
		 
		            @Override
		            public void onTimeSet(TimePicker view, int hourOfDay,
		                    int minute) {
	//                strt_time.setText(hourOfDay + ":" + minute);
		            }
		        }, 1,6, false);
		tpd.show();
			
			}*/
		
		
	}
	


	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			// set date picker as current date
			String time=strt_time.getText().toString();
			hour=Integer.parseInt(time.substring(0, time.indexOf(':') ));
			minute=Integer.parseInt(time.substring(time.indexOf(':')+1 ));
			return new TimePickerDialog(this, timePickerListener, hour,minute,false
					);
		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

		// when dialog box is closed, below method will be called.
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;
 
			
			// set current time into textview
			strt_time.setText(new StringBuilder().append(pad(hour))
					.append(":").append(pad(minute)));
			Message.message(getApplicationContext(), ""+hour);
			
			//database_helper.insert_setting( 2,pad(hour)+":"+pad(minute));

		}
	};

	private static String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}




	@Override
	public void onCheckedChanged(RadioGroup arg0, int checked_id) {
		// TODO Auto-generated method stub
		switch(checked_id){
		case R.id.landscape:
			database_helper.insert_setting( 6,"1");
			break;
		case R.id.portrait:
			database_helper.insert_setting( 6,"0");
			break;
		}
	}
	
	

}
