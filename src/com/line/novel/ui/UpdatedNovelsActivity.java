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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.line.novel.database.DatabaseOpenHelper;
import com.line.novel.database.Section;
import com.line.novel.database.SectionDao;
import com.line.novelapp.R;

public class UpdatedNovelsActivity extends Activity implements OnItemClickListener{
	
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
		
		
		helper = new DatabaseOpenHelper(this,2);
		sDao = new SectionDao(helper.getWritableDatabase());
		
		//注册广播
		receiver = new UpdateMsgReceiver();
		registerReceiver(receiver, new IntentFilter(IndexActivity.ACTION_FLAG_POST_UPDATE));
		
		listView = (ListView) findViewById(R.id.listView);
		initData();
		adapter = new SimpleAdapter(this,data,R.layout.update_list_item,
				new String[]{"title","desc"},new int[]{R.id.novelTitle,R.id.novelDesc});
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(this);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0,0,1,"小说更新记录");
		menu.add(0,1,1,"退出");
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case 0: 
			Intent intent = new Intent(this,HistoryUpdateActivity.class);
			startActivity(intent);
			break;
		case 1:
			System.exit(1);
		}
		return super.onMenuItemSelected(featureId, item);
	}


	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
		
		Intent intent = new Intent(this,NovelPostActivity.class);
		Map<String,String> map = data.get(position);
		intent.putExtra("path",map.get("path"));
		intent.putExtra("title",map.get("title"));
		startActivity(intent);
		
		//将文章设置为已读
		sDao.updateSectionState(map.get("id"),1);
		data.remove(position);
		this.adapter.notifyDataSetChanged();
		
	}

	private void initData(){

		data = new ArrayList<Map<String,String>>();
		List<Section> sections = sDao.listUnRead();
		
		for(Section s : sections){
			Map<String,String> map = new HashMap<String,String>();
			map.put("id",s.getId());
			map.put("title",s.getTitle());
			map.put("desc",s.getDesc());
			map.put("path",s.getPath());
			data.add(map);
		}
		
	}
		
	@Override
	protected void onDestroy(){
		super.onDestroy();
		sDao.closeDb();
		helper.close();
		unregisterReceiver(receiver);
	}
	
	class UpdateMsgReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent intent) {
			Section section = intent.getParcelableExtra("section");
			Map<String,String> map = new HashMap<String,String>();
			map.put("id",section.getId());
			map.put("title", section.getTitle());
			map.put("desc", section.getDesc());
			map.put("path", section.getPath());
			data.add(map);
			adapter.notifyDataSetChanged();
		}
		
	}
}
