package com.line.novel.ui;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;

import com.line.novelapp.R;

@SuppressWarnings("deprecation")
public class IndexActivity extends ActivityGroup{
	
	private TabHost tabHost;
	
	
	@Override
	protected void onCreate(Bundle savedState){
		super.onCreate(savedState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.main_activity);
		
		tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup(getLocalActivityManager());
		View tabView = (View) findViewById(R.layout.tab_inflate);
		initTabs(tabView);
	}
	
	private void initTabs(View view){
		
		View tab1 = LayoutInflater.from(this).inflate(R.layout.tab_inflate,null);
		TextView text1 = (TextView) tab1.findViewById(R.id.tabName);
		text1.setText(getResources().getString(R.string.index));
		
		//设置首页tab
		tabHost.addTab(tabHost.newTabSpec("tab1")
				.setIndicator(tab1)
				.setContent(new Intent(this,UpdatedNovelsActivity.class))
				);
		
		View tab2 = LayoutInflater.from(this).inflate(R.layout.tab_inflate,null);
		TextView text2 = (TextView) tab2.findViewById(R.id.tabName);
		text2.setText(getResources().getString(R.string.novels));
		
		//设置小说tab
		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator(tab2)
				.setContent(new Intent(this,NovelListActivity.class))
				);
		
	}
	
}
