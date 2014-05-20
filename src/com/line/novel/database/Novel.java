package com.line.novel.database;

import java.util.ArrayList;
import java.util.Arrays;
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

	private List<String> updateCache;

	// private final static String[] NUMBER =
	// {"1","2","3","4","5","6","7","8","9",
	// "0","零","一","二","三","四","五","六","七","八","九"};
	//
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
		this.id = UUID.randomUUID().toString().replace("-", "");
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

	public void updateLastSection(String lastSection) {

		if (updateCache == null) {
			updateCache = new ArrayList<String>();
			// String value =
			// ChineaseFiguresToNum.coverToNum(getSection(this.lastSection))
			// +"";
			// if(getCapter(lastSection) != null){
			// value =
			// ChineaseFiguresToNum.coverToNum(getCapter(this.lastSection)) + ""
			// + value;
			// }
			updateCache.add(this.lastSection);
		}

		updateCache.add(this.lastSection);

		// String value =
		// ChineaseFiguresToNum.coverToNum(getSection(lastSection)) + "";
		// if(getCapter(lastSection) != null){
		// value = ChineaseFiguresToNum.coverToNum(getCapter(this.lastSection))
		// + ""
		// + value;
		// }
		// System.out.println("第"+getCapter(lastSection)+"卷"+"第"+getSection(lastSection)+"章");
		// updateCache.add(Long.valueOf(value));

	}

	public void UpdateWithoutCache() {
		long[] tmp = new long[updateCache.size()];
		String[] ary = updateCache.toString().replace("[", "").replace("]", "")
				.split(",");
		if (getCapter(lastSection) != null) {
			for (int i = 0; i < ary.length; i++) {
				tmp[i] = Long.valueOf(ChineaseFiguresToNum
						.coverToNum(getCapter(ary[i]))
						+ ""
						+ ChineaseFiguresToNum.coverToNum(getSection(ary[i])));
			}
		} else {
			for (int i = 0; i < ary.length; i++) {
				tmp[i] = Long.valueOf(ChineaseFiguresToNum.coverToNum(getSection(ary[i])));
			}
		}
		
		Arrays.sort(tmp);
		int i = 0;
		for (i = 1; i < tmp.length; i++) {
			if (tmp[i] - tmp[i - 1] != 1) {
				break;
			}
		}
		
		this.lastSection = updateCache.get(i-1);
	}

	public boolean isUpdate(String title) {
		if (compareSection(lastSection, title) < 0) {
			return true;
		} else {
			return false;
		}
	}

	public String getCapter(String name) {
		if (name.contains("卷")) {
			int start = name.indexOf("第");
			int end = name.indexOf("卷");
			String capter = name.substring(start, end + 1);
			return capter;
		} else {
			return null;
		}
	}

	public String getSection(String title) {
		int start, end;
		start = title.lastIndexOf("第");
		end = lastSection.lastIndexOf(sectionKeyWord());
		return title.substring(start, end + 1);
	}

	private String sectionKeyWord() {
		return lastSection.substring(lastSection.lastIndexOf("第"),
				lastSection.length());
	}

	public int compareSection(String str1, String str2) {
		String cap1 = getCapter(str1);
		String cap2 = getCapter(str2);
		if (cap1 != null && cap2 != null && cap1.compareTo(cap2) != 0) {
			return cap1.compareTo(cap2);
		}

		String sec1 = getSection(str1);
		String sec2 = getSection(str2);
		return sec1.compareTo(sec2);
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

}
