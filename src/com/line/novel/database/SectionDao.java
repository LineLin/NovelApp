package com.line.novel.database;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SectionDao {

	private SQLiteDatabase db;
	
	private final String listAll = "select * from section";
	
	private final String listUnRead = "select * from section where readed = 0 ";
	
	private final String insert = "insert into section values (?,?,?,?,?)";
	
	private final String findByPId = "select * from section where path like ?";
	
	private final String updateState = "update section set readed = ? where id = ?";
	
	public SectionDao(SQLiteDatabase db){
		this.db = db;
	}
	
	public List<Section> listAll(){
		Cursor cursor = db.rawQuery(listAll, null);
		return cursorToList(cursor);
	}
	
	public List<Section> listUnRead(){
		Cursor cursor = db.rawQuery(listUnRead, null);
		return cursorToList(cursor);
	}
	
	public void insert(Section section){
		Object[] params = new Object[]{section.getId(),section.getTitle(),section.getDesc(),
				section.getPath(),section.getReaded()};
		db.execSQL(insert,params);
	}
	
	public void updateSectionState(String id,int readed){
		Object[] params = new Object[]{readed,id};
		db.execSQL(updateState, params);
	}
	
	public List<Section> cursorToList(Cursor cursor){
		
		List<Section> sections = new ArrayList<Section>();
		
		while(cursor.moveToNext()){
			String id = cursor.getString(cursor.getColumnIndex("id"));
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String path = cursor.getString(cursor.getColumnIndex("path"));
			String desc = cursor.getString(cursor.getColumnIndex("desc"));
			int readed = cursor.getInt(cursor.getColumnIndex("readed"));
			sections.add(new Section(id,title,desc,path,readed));
		}
		
		return sections;
	}
	
	public boolean isExit(String pId){
		Cursor cursor = db.rawQuery(findByPId, new String[]{"%"+pId+"%"});
		if(cursor.getCount() != 0){
			return true;
		}
		
		return false;
	}
	
	public void closeDb(){
		db.close();
	}
	
	
}
