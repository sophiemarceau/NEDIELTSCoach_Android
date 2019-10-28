package com.lelts.tool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 闹钟 数据库的创建 
 * **/
@SuppressLint("SimpleDateFormat")
public class SqliteDao {
	private SQLiteDatabase mSqliteDatabase;
	private SqliteOpen mSqliteOpen;
	private Cursor mCursor;
	private List<?> mList;

	//Sqlitedatabase;
	public SQLiteDatabase getDB(Context context){
		mSqliteOpen=new SqliteOpen(context);
		mSqliteDatabase=mSqliteOpen.getWritableDatabase();
		return mSqliteDatabase;		
	}
	// 添加个人设置信息
//	String s_id;
//	String s_title;
//	String s_starttime;
//	String s_endtime;
//	String s_repeat;
//	String s_sound;
//	String s_remind;
//	String s_note;
	public void insertinfo(Context context,String s_id,String s_title ,String s_starttime,String s_endtime,String s_repeat,String s_sound,String s_remind,String s_note){
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm");
		mSqliteDatabase=getDB(context);
		ContentValues values = new ContentValues();
		values.put("s_id", s_id);
		values.put("s_title", s_title);
		values.put("s_starttime",format.format(new Date(s_starttime)) );
		values.put("s_endtime",format.format(new Date( s_endtime)));
		values.put("s_repeat",s_repeat);
		values.put("s_sound", s_sound);
		values.put("s_remind", s_remind);
		values.put("s_note", s_note);
		mSqliteDatabase.insert("My_Colock", null, values);
		mSqliteDatabase.close();
		
	}
	
	
	
//	public Cursor getCursor(Context context){
//		mSqliteDatabase=getDB(context);
//		String sql="select * from students";
//		mCursor=mSqliteDatabase.rawQuery(sql, null);
////		mList=new ArrayList<E>();
//		return mCursor;
//		}
//	
//	public void getAdd(Context context,String name,int age,String sex){
//		mSqliteDatabase=getDB(context);
//		String sql="insert into students (name,age,sex) values (?,?,?)";
//		mSqliteDatabase.execSQL(sql, new Object[]{name,age,sex});
//	}
//	
//	public Cursor getUpdate(Context context){
//		mSqliteDatabase=getDB(context);
//		
//		return mCursor;
//		}
//	
//	// 添加答案的方法
//	public void InsertInfoAnswer(Context context,String s_id,String s_code ,String s_answer) {
//		// TODO Auto-generated method stub
//		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//		mSqliteDatabase=getDB(context);
//		ContentValues values = new ContentValues();
//		values.put("s_id", s_id);
//		values.put("s_code", s_code);
//		values.put("s_answer", s_answer);
//		values.put("s_time", format.format(new Date()));
//		mSqliteDatabase.insert("Stu_Answer", null, values);
//		mSqliteDatabase.close();
//	}
//	//更改答案的方法
//	public void UpdateInfoAnswer(Context context,String s_id, String s_answer,String s_code) {
//		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//		mSqliteDatabase=getDB(context);
//		ContentValues values = new ContentValues();
//		values.put("s_answer", s_answer);
//		values.put("s_code", s_code);
//		values.put("s_time", format.format(new Date()));
//		mSqliteDatabase.update("Stu_Answer", values, "s_id=?", new String[] { s_id });
//		mSqliteDatabase.close();
//	}
//	//查询所有的答案
//	public List<StuAnswerInfo> GetallAnswer(Context context) {
//		mSqliteDatabase=getDB(context);
//		List<StuAnswerInfo> list = new ArrayList<StuAnswerInfo>();
//		Cursor cursor = mSqliteDatabase.query(false, "Stu_Answer", null, null, null, null, null,
//				null, null);
//		while (cursor.moveToNext()) {
//			String s_id = cursor.getString(cursor.getColumnIndex("s_id"));
//			String s_answer = cursor.getString(cursor.getColumnIndex("s_answer"));
//			String s_time = cursor.getString(cursor.getColumnIndex("s_time"));
//			String s_code = cursor.getString(cursor.getColumnIndex("s_code"));
//			list.add(new StuAnswerInfo(s_id, s_code, s_answer, s_time));
//
//		}
//		cursor.close();
//		mSqliteDatabase.close();
//		return list;
//
//	}
//	//按s_id查找
//
//	public Boolean SelectBys_id(Context context,String s_id) {
//		mSqliteDatabase=getDB(context);
//		Cursor query = mSqliteDatabase.query("Stu_Answer", null, "s_id=?",
//				new String[] { s_id }, null, null, null);
//		if (query.moveToNext()) {
//			return true;
//		} else {
//			return false;
//		}
//
//	}
//	//根据s_id 来查找
//	public String setAnswer(Context context,String s_id) {
//		String s_answer = null;
//		mSqliteDatabase=getDB(context);
//		Cursor cursor = mSqliteDatabase.query("Stu_Answer", null, "s_id = ?",
//				new String[] { s_id }, null, null, null);
//		while (cursor.moveToNext()) {
//			s_answer = cursor.getString(cursor.getColumnIndex("s_answer"));
//		}
//		cursor.close();
//		mSqliteDatabase.close();
//		return s_answer;
//
//	}
//	
//	//删除数据库
//	public void deleteDatabase(Context context) {
//		
//		mSqliteDatabase=getDB(context);
//		mSqliteDatabase.delete("Stu_Answer", null, null);
//		mSqliteDatabase.close();
//		}
	
}
