package com.example.collegetime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import com.example.sql_database.Message;
//import com.example.collegetime.DatabaseHelperAdapter.DatabaseHelper;

public class DatabaseHelperAdapter {
	
	DatabaseHelper helper;

	public DatabaseHelperAdapter(Context context) {
		helper = new DatabaseHelper(context);
	}

	public void building_database(){
		SQLiteDatabase db=helper.getWritableDatabase();
		//Cursor cursor=db.query(DatabaseHelper.TABLE_NAME, DatabaseHelper.PERIOD, null, null,null, null,null);
		ContentValues contentvalues = new ContentValues();
		ContentValues contentvalues1 = new ContentValues();
			long id=0;
			//for(int j=0;j<7;j++)
			//{
		//Message.message(null, "Updating");
			//Message.message(DatabaseHelper.context, "reach there");
			Cursor cursor=db.rawQuery("SELECT COUNT(*) FROM"+DatabaseHelper.TABLE_NAME, null);
			Cursor cursor1=db.rawQuery("SELECT COUNT(*) FROM"+DatabaseHelper.TABLE_NAME1, null);
			try {
				if (cursor.moveToFirst())
				{
					//Message.message(DatabaseHelper.context, "reach here");
					for(int j=0;j<6;j++)
					{
						for(int i=0;i<DatabaseHelper.PERIOD.length;i++)
							contentvalues.put(DatabaseHelper.PERIOD[i], "");
						id=db.insert(DatabaseHelper.TABLE_NAME,null, contentvalues);
					}

				}
				if(cursor1.moveToFirst()){
					contentvalues1.put(DatabaseHelper.No_of_periods, "6");
					contentvalues1.put(DatabaseHelper.Period_duration, "50");
					contentvalues1.put(DatabaseHelper.Period_start_time, "8:00");
					contentvalues1.put(DatabaseHelper.Lunch_after_period, "0");
					contentvalues1.put(DatabaseHelper.Lunch_duration, "0");
					contentvalues1.put(DatabaseHelper.is_Service_on, "0");
					contentvalues1.put(DatabaseHelper.orientation, "1");
					id=db.insert(DatabaseHelper.TABLE_NAME1,null, contentvalues1);
				}
			} finally {
				// TODO Auto-generated catch block
			cursor.close();
			cursor1.close();
			}
			//}
			//return id;
	}
	public long insert_data(String name,int bid) {
		
		SQLiteDatabase db=helper.getWritableDatabase();
		
		ContentValues contentvalues = new ContentValues();

		//else{
		int row,column;
		column=bid%10;
		bid=bid/10;
		row=bid;
		//for(int i=0;i<8;i++)
		contentvalues.put(DatabaseHelper.PERIOD[column], name);
		//contentvalues.put(DatabaseHelper.PASSWORD,password);
		//long id=db.insert(DatabaseHelper.TABLE_NAME,null, contentvalues);
		long id=db.update(DatabaseHelper.TABLE_NAME, contentvalues,"_id="+(row),null);
		//Message.message(DatabaseHelper.context, column);
		
		return id;
		}
	//}
	
	public void insert_setting(int column_index,String value){
		long id=0;
		SQLiteDatabase db=helper.getWritableDatabase();
		String[] column_name={DatabaseHelper.No_of_periods,DatabaseHelper.Period_duration ,DatabaseHelper.Period_start_time,DatabaseHelper.Lunch_after_period,DatabaseHelper.Lunch_duration,DatabaseHelper.is_Service_on,DatabaseHelper.orientation};
		ContentValues contentvalues2 = new ContentValues();
		contentvalues2.put(column_name[column_index], value);
		id=db.update(DatabaseHelper.TABLE_NAME1, contentvalues2,"_id="+1,null);
	
	}
	
	public String[][] getAllData(){
		SQLiteDatabase db=helper.getWritableDatabase();
		
		
		Cursor cursor=db.query(DatabaseHelper.TABLE_NAME, DatabaseHelper.PERIOD, null, null,null, null,null);
		
		String row[][]=new String[20][20];
		
		//StringBuffer buffer=new StringBuffer();
		int j=1,i=0;
		
		try {
			while(cursor.moveToNext())
			{
				
				//int index1=cursor.getColumnIndex(DatabaseHelper.UID);
				//int cid=cursor.getInt(0);
				if(j<=6){
					for(i=0;i<DatabaseHelper.PERIOD.length;i++)
					{	
						row[j][i]=cursor.getString(i);
						//buffer.append(" "+row[i]);
					}
				}
				
				/*else{
				//row[j][0]=
					//cursor.moveToPrevious();
					Message.message(DatabaseHelper.context,cursor.getString(1));		
				}*/
				j++;
				
				
				}
		} finally {
			// TODO Auto-generated catch block
				cursor.close();
		}
		
		return row;
	}
	
	public String[] getSettings(){
		String settings[]=new String[7];
		SQLiteDatabase db=helper.getWritableDatabase();
		int k=0;
		String[] column={DatabaseHelper.No_of_periods,DatabaseHelper.Period_duration ,DatabaseHelper.Period_start_time,DatabaseHelper.Lunch_after_period,DatabaseHelper.Lunch_duration,DatabaseHelper.is_Service_on,DatabaseHelper.orientation};
		Cursor cursor1=db.query(DatabaseHelper.TABLE_NAME1,column, null, null,null, null,null);
		try {
			while(cursor1.moveToNext()){
				//Message.message(DatabaseHelper.context, "wow!!");
				for(k=0;k<7;k++)
				settings[k]=cursor1.getString(k);
			
			}
		} finally {
			// TODO Auto-generated catch block
			cursor1.close();
		}
		return settings;
	}
	
	

	static class DatabaseHelper extends SQLiteOpenHelper {
		private static final String DATABASE_NAME = "vib_database";
		private static final String TABLE_NAME = "TIMETABLE";
		private static final String TABLE_NAME1 = "TIMETABLE1";
		private static final String UID = "_id";
		//private static final String NAME = "hello";
		private static final String[] PERIOD = {"I","II","III","IV","V","VI","VII","VIII","IX","X"};
		private static final String No_of_periods = "NUMBER_OF_PERIODS";
		private static final String Period_duration = "PERIOD_DURATION";
		private static final String Lunch_duration = "LUNCH_DURATION";
		private static final String Lunch_after_period = "LUNCH_AFTER_PERIOD";
		private static final String Period_start_time = "PERIOD_START_TIME";
		private static final String is_Service_on = "SERVICE";
		private static final String orientation = "ORIENTATION";
		//private static final String PERIOD7 = "VII";
		//private static final String PERIOD8 = "VIII";
		//private static final String PASSWORD = "password";
		private static final int DATABASE_VERSION = 1;
		//private static final int DATABASE_VERSION1 = 1;
		private static Context context;
		private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
				+ "("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +PERIOD[0]
				+ " VARCHAR(255),"+PERIOD[1]+" VARCHAR(255),"+PERIOD[2]+" VARCHAR(255),"+PERIOD[3]+" VARCHAR(255),"+PERIOD[4]+" VARCHAR(255),"+PERIOD[5]+" VARCHAR(255),"+PERIOD[6]+" VARCHAR(255),"+PERIOD[7]+" VARCHAR(255),"+PERIOD[8]+" VARCHAR(255),"+PERIOD[9]+" VARCHAR(255));";
		private static final String CREATE_TABLE1 = "CREATE TABLE " + TABLE_NAME1
				+ "("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +No_of_periods+" VARCHAR(255),"+Period_duration+" VARCHAR(255),"+Period_start_time+" VARCHAR(255),"+Lunch_duration+" VARCHAR(255),"+Lunch_after_period+" VARCHAR(255),"+is_Service_on+" VARCHAR(255),"+orientation+" VARCHAR(255));";
		
		private static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
		private static final String DROP_TABLE1 = "DROP TABLE IF EXISTS "+TABLE_NAME1;
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
			//Message.message(context, "constructor called");
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(CREATE_TABLE);
				db.execSQL(CREATE_TABLE1);
				//Message.message(context, "onCreate called");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				Message.message(context, "" + e);
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			try {
				//Message.message(context, "onUpgrade called");
				db.execSQL(DROP_TABLE);
				db.execSQL(DROP_TABLE1);
				onCreate(db);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				Message.message(context, "" + e);
			}

		}

	}

}
