package com.line.novel.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper{
	
	private final String sql = "create table novel ("
			+ "id varchar(32) primary key unique,"
			+ "name varchar(20),"
			+ "author varchar(20),"
			+ "lastSection varchar(20)"
			+ ");";
	
	private final String sql2 = "create table section("
			+ "id varchar(32) primary key unique,"
			+ "title varchar(32),"
			+ "desc varchar(255),"
			+ "path varchar(255),"
			+ "readed boolean"
			+ ");";
	
	public DatabaseOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	public DatabaseOpenHelper(Context context,int version){
		this(context,"novel.db3",null,version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql);
		db.execSQL(sql2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL("drop table novel");
//		db.execSQL("drop table section");
//		onCreate(db);
	}

}
