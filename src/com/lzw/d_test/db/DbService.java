package com.lzw.d_test.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//Create table if not exists infos (_id integer primary key autoincrement,title,digest,imgsrc)
public class DbService {
	private DbOpenHelper dbOpenHelper;
	
	public DbService(Context context){
		
		dbOpenHelper = new DbOpenHelper(context);
	}
	
	//查询数据库
	public Cursor select(){
		SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
		return database.rawQuery("select * from infos", null);
	}
	
	//插入数据
	public boolean insert(String title,String digest,String imgsrc){
		Cursor cursor = select();
		while (cursor.moveToNext()) {
			String title1 = cursor.getString(cursor.getColumnIndex("title"));
			if (title.equals(title1)) {
				return false;
			}
		}
		SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
		//Create table if not exists infos (_id integer primary key autoincrement,title,digest,imgsrc)
		//insert into infos (subject,summary,cover)values('subject','summary','cover');
		database.execSQL("insert into infos (title,digest,imgsrc)values('"+title+"','"+digest+"','"+imgsrc+"')");
		return true;
	}
	
	//删除数据
	public void delete(int id){
		SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
		database.execSQL("delete from infos where _id="+id);
	}
	
}
