package com.line.novel.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.line.novel.utils.ChineaseFiguresToNum;

import android.os.Parcel;
import android.os.Parcelable;

public class Novel implements Parcelable {

	private String id;

	private String name;

	private String author;

	private String lastSection;

	private List<SectionBody> updateWindow;

	public static final Parcelable.Creator<Novel> CREATOR = new Parcelable.Creator<Novel>() {

		@Override
		public Novel createFromParcel(Parcel source) {
			return new Novel(source);
		}

		@Override
		public Novel[] newArray(int size) {
			return new Novel[size];
		}

	};
	
	private Novel(Parcel source) {
		this.id = source.readString();
		this.name = source.readString();
		this.author = source.readString();
		this.lastSection = source.readString();
	}

	public Novel(String name, String author, String lastSection) {
		this.id = UUID.randomUUID().toString().replaceAll("-", "");
		this.name = name;
		this.author = author;
		this.lastSection = lastSection;
	}

	public Novel(String id, String name, String author, String lastSection) {
		this(name, author, lastSection);
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLastSection() {
		return lastSection;
	}

	public void setLastSection(String lastSection) {
		this.lastSection = lastSection;
	}

	public void updateLastSection(String title) {

		if(updateWindow == null){
			updateWindow = new ArrayList<SectionBody>();
			updateWindow.add(new SectionBody(this.lastSection));
		}
		String capter = getCapter(title);
		if(capter != null){
			title = "第" + capter + "卷 第"  + getSection(title) + sectionKeyWord();
		}else{
			title = "第"  + getSection(title) + sectionKeyWord();
		}
		updateWindow.add(new SectionBody(title));

		// String value =
		// ChineaseFiguresToNum.coverToNum(getSection(lastSection)) + "";
		// if(getCapter(lastSection) != null){
		// value = ChineaseFiguresToNum.coverToNum(getCapter(this.lastSection))
		// + ""
		// + value;
		// }
		// System.out.println("第"+getCapter(lastSection)+"卷"+"第"+getSection(lastSection)+"章");
		// updateWindow.add(Long.valueOf(value));

	}

	public void vertifyUpdate() {
		
		//如果缓存窗口为空的话，那么不用进行确认更新
		if(updateWindow == null){
			updateWindow = new ArrayList<SectionBody>();
			updateWindow.add(new SectionBody(this.lastSection));
			return ;
		}
		
////		long[] tmp = new long[updateWindow.size()];
////		String[] ary = updateWindow.toString().replace("[", "").replace("]", "")
////				.split(",");
////		
////		System.out.println("----text----" + ary.toString());
//		
//		if (getCapter(this.lastSection) != null) {
//			for (int i = 0; i < ary.length; i++) {
//				tmp[i] = Long.valueOf(ChineaseFiguresToNum
//						.coverToNum(getCapter(ary[i]))
//						+ ""
//						+ ChineaseFiguresToNum.coverToNum(getSection(ary[i])));
//			}
//		} else {
//			for (int i = 0; i < ary.length; i++) {
//				tmp[i] = Long.valueOf(ChineaseFiguresToNum.coverToNum(getSection(ary[i])));
//			}
//		}
//		
//		Arrays.sort(tmp);
//		int i = 0;
//		for (i = 1; i < tmp.length; i++) {
//			if (tmp[i] - tmp[i - 1] != 1) {
//				break;
//			}
//		}
		
		
		Collections.sort(updateWindow);
		int  i;
		for(i = 1; i < updateWindow.size(); i++){
			if(updateWindow.get(i).key - updateWindow.get(i-1).key != 1){
				break;
			}
		}
		
		//0～i-1 都已验证，删除0～i-2的数据，更新i-1到最新章节
		i = i-2;
		while(i-- >= 0){
			updateWindow.remove(0);
		}
		
		this.lastSection = updateWindow.get(0).title;
		
	}

	public boolean isUpdate(String title) {
		
		if(title.contains("第") && title.contains(sectionKeyWord()) &&
				compareSection(this.lastSection, title) < 0){
			return true;
		}
		return false;
	}

	public String getCapter(String name) {
		if (name.contains("卷")) {
			int start = name.indexOf("第");
			int end = name.indexOf("卷");
			String capter = name.substring(start+1, end);
			return capter;
		} else {
			return null;
		}
	}

	public String getSection(String title) {
		int start, end;
		start = title.lastIndexOf("第");
		end = title.lastIndexOf(sectionKeyWord());
		return title.substring(start+1, end);
	}

	private String sectionKeyWord() {
		return lastSection.substring(lastSection.length()-1,
				lastSection.length());
	}

	public int compareSection(String str1, String str2) {
		String cap1 = getCapter(str1);
		String cap2 = getCapter(str2);
		System.out.println(cap1 + " vs " + cap2);
		if (cap1 != null && cap2 != null) {
			long num1 = ChineaseFiguresToNum.coverToNum(cap1);
			long num2 = ChineaseFiguresToNum.coverToNum(cap2);
			if(num1 != num2){
				return (int)(num1 - num2);
			}
		}

		String sec1 = getSection(str1);
		String sec2 = getSection(str2);
		long num1 = ChineaseFiguresToNum.coverToNum(sec1);
		long num2 = ChineaseFiguresToNum.coverToNum(sec2);
		System.out.println(num1 + " vs " + num2);
		return (int)(num1-num2);
	}
	
//	private long getCompareKey(String str){
//		
//	}
	
	@Override
	public String toString(){
		return name;
	}
	
	@Override
	public int hashCode(){
		return name.hashCode();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeString(this.author);
		dest.writeString(this.lastSection);
	}
	
	private class SectionBody implements Comparable<SectionBody>{
		
		private String title = "";
		
		private long key;
		
		public SectionBody(String title){
			String section = getSection(title);
			String capter = getCapter(title);
			long numCapter = 0, numSection = 0;
			
			if(capter != null){ 
				numCapter = ChineaseFiguresToNum.coverToNum(capter);
				this.title = "第" + capter + "卷 ";
			}
			
			this.title += "第" + section + sectionKeyWord();
			
			numSection = ChineaseFiguresToNum.coverToNum(section);
			key = Long.valueOf(numCapter + "" + numSection);
		}
		
		
		@Override
		public int compareTo(SectionBody obj) {
			
			return (int)(this.key - obj.key);
		}
	}
}
