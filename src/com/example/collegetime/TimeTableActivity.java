package com.example.collegetime;

import com.example.collegetime.Message;
import com.example.collegetime.DatabaseHelperAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class TimeTableActivity extends Activity {
	String EMPTY = "somestring132342532";
	int no_of_periods = 8;
	static int period_duration;
	int period_start_hour;
	int period_start_minute;
	int period_end_hour;
	int period_end_minute;
	int lunch_after_period;
	int lunch_duration;
	String period_time=new String();
	String lunch_time=new String();
	long id;
	Button strt_time;
	Boolean MANAGE = false;
	String day[] = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	String pdRoman[]={"I","II","III","IV","V","VI","VII","VIII","IX","X"};
	DatabaseHelperAdapter database_helper;
	static String[][] data = new String[20][20];
	static String[] setting=new String[6];
	MainMenu main_menu;
	int flag = 1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_table);
		
		database_helper = new DatabaseHelperAdapter(this);
		Intent intent = getIntent();
		//final Settings setng=new Settings();//Settings is a class made by me
		//Spinner spnr3=(Spinner)findViewById(R.id.period_no);
		MANAGE = intent.getBooleanExtra("MANAGE_BUTTON", true);
		setting=database_helper.getSettings();
		
		if(Integer.parseInt(setting[6])==1)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			else
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		TableLayout layout = (TableLayout) findViewById(R.id.layout);

		/*try {

			boolean firstboot = getSharedPreferences("BOOT_PREF", MODE_PRIVATE)
					.getBoolean("firstboot", true);

			if (firstboot) {
				database_helper.building_database();
				// initialize_database.setVisibility(View.GONE);
				//Message.message(getApplicationContext(),
				//		"Database created now you can modify and view it");
				// place your code that will run single time
				getSharedPreferences("BOOT_PREF", MODE_PRIVATE).edit()
						.putBoolean("firstboot", false).commit();

			}
		} finally {

		}*/

		//setng.spnr3.setTop(4);
		
		data = database_helper.getAllData();		
		
		//System.out.println("hello");
		no_of_periods=Integer.parseInt(setting[0]);
		period_duration=Integer.parseInt(setting[1]);
		period_start_hour=Integer.parseInt(setting[2].substring(0, setting[2].indexOf(':')));//extracting hour from string like 8:00
		period_start_minute=Integer.parseInt(setting[2].substring(setting[2].indexOf(':')+1));
		lunch_after_period=Integer.parseInt(setting[3]);
		lunch_duration=Integer.parseInt(setting[4]);
		
		TableRow periodNumRow = new TableRow(this);
		//Message.message(this, spnr3.toString());
		int k=1;
		if(lunch_after_period==0)
			no_of_periods-=1;//if lunch is not present then one column is reduced
		for(int i=1;i<=no_of_periods+1;i++)
		{
			if(i==1)
			{
				TextView corner = new TextView(this);
				corner.setText("Period/\nDay");
				corner.setPadding(10, 10, 10, 10);
				corner.setTextSize(10);
				corner.setGravity(Gravity.CENTER);
				periodNumRow.addView(corner);
			}
			TextView pdNum = new TextView(this);
			if(i==lunch_after_period+1&&lunch_after_period!=0)//this condition is for setting the design of lunch column
			{
				
				period_end_hour=period_start_hour+((period_start_minute+lunch_duration)/60);
				period_end_minute=(period_start_minute+lunch_duration)%60;
				//lunch_time=period_start_hour+":"+period_start_minute+"-"+period_end_hour+":"+period_end_minute;
				
				if(period_end_minute<10&&period_start_minute<10)
					lunch_time=period_start_hour+":"+"0"+period_start_minute+"-"+period_end_hour+":"+"0"+period_end_minute;
					else if(period_end_minute<10&&period_start_minute>=10)
						lunch_time=period_start_hour+":"+period_start_minute+"-"+period_end_hour+":"+"0"+period_end_minute;
					else if(period_start_minute<10&&period_end_minute>=10)
						lunch_time=period_start_hour+":"+"0"+period_start_minute+"-"+period_end_hour+":"+period_end_minute;
					else
						lunch_time=period_start_hour+":"+period_start_minute+"-"+period_end_hour+":"+period_end_minute;

				pdNum.setText(lunch_time);
				
				period_start_hour=period_end_hour;
				period_start_minute=period_end_minute;
			}
			
			else{
				period_end_hour=period_start_hour+((period_start_minute+period_duration)/60);
				period_end_minute=(period_start_minute+period_duration)%60;
				if(period_start_hour>12)
					period_start_hour-=12;
				if(period_end_hour>12)
					period_end_hour-=12;
				
				if(period_end_minute<10&&period_start_minute<10)
					period_time=period_start_hour+":"+"0"+period_start_minute+"-"+period_end_hour+":"+"0"+period_end_minute;
					else if(period_end_minute<10&&period_start_minute>=10)
						period_time=period_start_hour+":"+period_start_minute+"-"+period_end_hour+":"+"0"+period_end_minute;
					else if(period_start_minute<10&&period_end_minute>=10)
						period_time=period_start_hour+":"+"0"+period_start_minute+"-"+period_end_hour+":"+period_end_minute;
					else
						period_time=period_start_hour+":"+period_start_minute+"-"+period_end_hour+":"+period_end_minute;

				//period_time=period_start_hour+":"+period_start_minute+"-"+period_end_hour+":"+period_end_minute;
				
				period_start_hour=period_end_hour;
				period_start_minute=period_end_minute;
				
				pdNum.setText(pdRoman[k-1]+"\n"+period_time);
				k++;
			}
			pdNum.setPadding(10, 10, 10, 10);
			pdNum.setGravity(Gravity.CENTER);
			periodNumRow.addView(pdNum);
			
		}
		layout.addView(periodNumRow);
		
		String recess[]={"R","E","C","E","S","S"};
		for (int j = 1; j <= 6; j++) {
			TableRow row = new TableRow(this);
			row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			TextView dayOfWeek = new TextView(this);
			dayOfWeek.setText(day[j - 1]);
			dayOfWeek.setPadding(10, 10, 10, 10);
			row.addView(dayOfWeek);
			int p=0;
			for (int i = 0; i <= no_of_periods; i++)//here also because of one lunch we are executing it one more time
			{
				//but we have used another variable p for creating exactly those no. of periods which are req.

				final TextView period = new TextView(this);
				period.setMinimumWidth(120);
				if(i==lunch_after_period&&lunch_after_period!=0)
					period.setText(recess[j-1]);
					else{
						period.setId(10 * j + p);
				period.setText(data[j][p]);
					}
				period.setGravity(Gravity.CENTER);
				// period.setLayoutParams(LayoutParams,);
				period.setPadding(10, 10, 10, 10);
				if (MANAGE == true) {
					period.setLongClickable(true);
					// period.setClickable(true);
					period.setOnLongClickListener(new View.OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {

							// String text = new String();
							setNewValueOfPeriod(period);
							return false;
						}
					});
					//spnr3.findFocus();
					
				}
				period.setTextSize(20);
				if(i==lunch_after_period&&lunch_after_period!=0){
					period.setTextSize(20);
					period.setTextScaleX(2.5f);
					period.setTextColor(0xffff0000);
					period.setBackgroundColor(0xcceeffff);
					period.setLongClickable(false);
					period.setPadding(10, 10, 10, 10);
				}
				else{
				if (data[j][p].equals(""))
					{period.setBackgroundResource(R.drawable.color_empty);
					period.setTextColor(0xffffffff);
					}
					
				else
					{period.setBackgroundResource(R.drawable.color_non_empty);
					period.setTextColor(0xffffffff);
					}
				p++;
				}
				row.addView(period);

			}
			layout.addView(row, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}

		if (MANAGE == true)
			Toast.makeText(getApplicationContext(),
					"Press and hold on a period to edit", Toast.LENGTH_LONG)
					.show();
		/*Message.message(this, ""+data[6][0]);
		Message.message(this, ""+data[6][1]);
		Message.message(this, ""+data[6][2]);
		Message.message(this, ""+data[7][0]);
		Message.message(this, ""+data[7][1]);
		Message.message(this, ""+data[7][2]);
		*/
	}
	

	private void setNewValueOfPeriod(final TextView period) {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Edit Period");
		alertDialog.setMessage("Enter Period Title: ");
		final EditText input = new EditText(this);
		input.setText(period.getText().toString());
		input.requestFocus();
		alertDialog.setView(input);

		alertDialog.setNegativeButton("Delete",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// myInput=CANCELLED;
						period.setText("");
						period.setBackgroundResource(R.drawable.color_empty);
						database_helper.insert_data("", period.getId());

					}

				});
		alertDialog.setPositiveButton("Change",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						String myInput = input.getText().toString();
						period.setText(myInput);
						if(myInput.equalsIgnoreCase(""))
							period.setBackgroundResource(R.drawable.color_empty);
						else
						period.setBackgroundResource(R.drawable.color_non_empty);
						try {
							id = database_helper.insert_data(myInput,
									period.getId());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							Message.message(TimeTableActivity.this,
									"Unsuccessful" + e);
						}
						if (id < 0) {
							Message.message(TimeTableActivity.this,
									"Unsuccessful" + id);
						} else {
							Message.message(TimeTableActivity.this,
									"Successfully inserted a period");
						}

					}

				});
		alertDialog.setNeutralButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
					}
				});
		alertDialog.show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (MANAGE == true)
			getMenuInflater().inflate(R.menu.time_table_manage, menu);
		else
			getMenuInflater().inflate(R.menu.time_table_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.ManageTTMenu) {

			Intent intent = new Intent(getApplicationContext(),
					TimeTableActivity.class);
			intent.putExtra("MANAGE_BUTTON", true);
			finish();
			startActivity(intent);
		}
		if (item.getItemId() == R.id.editSettingMenu) {
			Intent intent = new Intent(getApplicationContext(),
					Settings.class);
			finish();
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

}
