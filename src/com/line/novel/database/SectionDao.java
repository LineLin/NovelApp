package com.line.novel.database;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SectionDao {

	private SQLiteDatabase db;
	
	private final String listAll = "select * from section";
	
	private final String deleteSql = "delete from section where id = ?";
	
	private final String insert = "insert into section values (?,?,?,?)";
	
	public SectionDao(SQLiteDatabase db){
		this.db = db;
	}
	
	public List<Section> listAll(){
		Cursor cursor = db.rawQuery(listAll, null);
		return cursorToList(cursor);
	}
	
	public void delete(String id){
		db.execSQL(deleteSql, new String[]{id});
	}
	
	public void insert(Section section){
		String[] params = new String[]{section.getId(),section.getTitle(),section.getDesc(),
				section.getPath()};
		db.execSQL(insert,params);
	}
	
	public List<Section> cursorToList(Cursor cursor){
		
		List<Section> sections = new ArrayList<Section>();
		
		while(cursor.moveToNext()){
			String id = cursor.getString(cursor.getColumnIndex("id"));
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String path = cursor.getString(cursor.getColumnIndex("path"));
			String desc = cursor.getString(cursor.getColumnIndex("desc"));
			sections.add(new Section(id,title,path,desc));
		}
		
		return sections;
	}
	
	public void closeDb(){
		db.close();
	}
	
	
}
