package com.line.novel.database;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NovelDao {
	
	private final String QUERY_ALL = "select * from novel";
	
	private final String UPDATE = "update novel set name = ? , author = ? , "
			+ "lastSection = ? where id = ? ";
	
	private final String DELETE = "delete from novel where id = ?";
	
	private final String INSERT = "insert into Novel values (?,?,?,?)";
	
	private SQLiteDatabase db;
	
	public NovelDao(SQLiteDatabase db){
		this.db = db;
	}
	
	public List<Novel> listAll(){
		
		Cursor cursor = db.rawQuery(QUERY_ALL, null);
		
		return getNovels(cursor);
	}
	
	public void addNovel(Novel novel){
		
		String[] params = new String[]{novel.getId(),novel.getName(),novel.getAuthor(),
				novel.getLastSection()};
		
		db.execSQL(INSERT,params);
	}
	
	public void deleteNovel(Novel novel){
		
		db.execSQL(DELETE, new String[]{novel.getId()});
		
	}
	
	public void updateNovel(Novel novel){
		
		String[] params = new String[]{novel.getName(),novel.getAuthor(),novel.getLastSection(),
				novel.getId()};
		
		db.execSQL(UPDATE,params);
	
	}
	
	public List<Novel> getNovels(Cursor cursor){
		
		List<Novel> novels = new ArrayList<Novel>();
		
		while(cursor.moveToNext()){
			String id = cursor.getString(cursor.getColumnIndex("id"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String author = cursor.getString(cursor.getColumnIndex("author"));
			String lastSection = cursor.getString(cursor.getColumnIndex("lastSection"));
			novels.add(new Novel(id,name,author,lastSection));
		}
		
		return novels;
	}
	
	public void closeDb(){
		
		db.close();
	}

}
