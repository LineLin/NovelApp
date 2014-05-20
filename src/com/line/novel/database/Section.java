package com.line.novel.database;

import java.util.UUID;

import android.os.Parcel;
import android.os.Parcelable;

public class Section implements Parcelable{
	
	public static final Parcelable.Creator<Section> CREATOR = new Parcelable.Creator<Section>() {

		@Override
		public Section createFromParcel(Parcel source) {
			return new Section(source);
		}

		@Override
		public Section[] newArray(int size) {
			return new Section[size];
		}
		
		
	};
	
	private String id;
	
	private String title;
	
	private String path;
	
	private String desc;
	
	
	public Section(String title,String desc,String path){
		this.title = title;
		this.path = path;
		this.desc = desc;
		this.id = UUID.randomUUID().toString().replaceAll("-","");
	}
	
	public Section(String id,String title,String path,String desc){
		this.id = id;
		this.path = path;
		this.title = title;
		this.desc = desc;
	}
	
	public Section(Parcel source){
		this.id = source.readString();
		this.title = source.readString();
		this.desc = source.readString();
		this.path = source.readString();
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getId() {
		return id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(desc);
		dest.writeString(path);
	}


	
}
