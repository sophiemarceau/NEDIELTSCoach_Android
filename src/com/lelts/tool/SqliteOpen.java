package com.lelts.tool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteOpen extends SQLiteOpenHelper {

	public SqliteOpen(Context context) {
		super(context, "newOriental", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	
		/**
		 * 创建 个人闹钟 设置表  
		 				String id;
						String title;
						String starttime;
						String endtime;
						String repeat;
						String sound;
						String remind;
						String note;
		 */
		db.execSQL("create table My_Colock(s_id varchar(20) ,s_title varchar(20) ,s_starttime varchar(20),s_endtime varchar(20),s_repeat varchar(20),s_sound varchar(20),s_remind varchar(20),s_note varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
