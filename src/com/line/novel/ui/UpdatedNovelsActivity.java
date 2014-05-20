package com.line.novel.ui;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.line.novel.database.DatabaseOpenHelper;
import com.line.novel.database.Section;
import com.line.novel.database.SectionDao;
import com.line.novelapp.R;

public class UpdatedNovelsActivity extends Activity {
	
	private ListView listView;
	
	private SimpleAdapter adapter;
	
	private List<Map<String,String>> data;
	
	private DatabaseOpenHelper helper;
	
	private SectionDao sDao;
	
	private UpdateMsgReceiver receiver;
	
	@Override
	protected void onCreate(Bundle saveState){
		
		super.onCreate(saveState);
		setContentView(R.layout.update_list);
		
		
		helper = new DatabaseOpenHelper(this,1);
		sDao = new SectionDao(helper.getWritableDatabase());
		
		//注册广播
		receiver = new UpdateMsgReceiver();
		registerReceiver(receiver, new IntentFilter("com.line.novel.NOVEL_UPDATE"));
		System.out.println("注册了");
		
		
		
		listView = (ListView) findViewById(R.id.listView);
		initData();
		adapter = new SimpleAdapter(this,data,R.layout.update_list_item,
				new String[]{"title","desc"},new int[]{R.id.novelTitle,R.id.novelDesc});
		listView.setAdapter(adapter);
	}
	
	private void initData(){
//		Intent intent = getIntent();
		data = new ArrayList<Map<String,String>>();
		List<Section> sections = sDao.listAll();
		
		for(Section s : sections){
			Map<String,String> map = new HashMap<String,String>();
			map.put("title",s.getTitle());
			map.put("desc",s.getDesc());
			map.put("path",s.getPath());
			data.add(map);
		}
		
		
		Map<String,String> map1 = new HashMap<String,String>();
		map1.put("title","第一百五十一章，积极的人");
		map1.put("desc","小林子是kdj是考虑到及阿空间的时刻设计咯电视卡 1");
		data.add(map1);
		
	}
	
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		sDao.closeDb();
		helper.close();
		unregisterReceiver(receiver);
		System.out.println("取消注册");
	}
	
	class UpdateMsgReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent intent) {
			System.out.println("UpdateNovel接受到广播");
			Section section = intent.getParcelableExtra("section");
			Map<String,String> map = new HashMap<String,String>();
			map.put("title", section.getTitle());
			map.put("desc", section.getDesc());
			map.put("path", section.getPath());
			data.add(map);
			adapter.notifyDataSetChanged();
		}
		
	}
}
