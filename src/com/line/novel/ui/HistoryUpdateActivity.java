package com.line.novel.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.line.novel.database.DatabaseOpenHelper;
import com.line.novel.database.Section;
import com.line.novel.database.SectionDao;
import com.line.novelapp.R;

public class HistoryUpdateActivity extends Activity implements OnItemClickListener{
	
private ListView listView;
	
	private SimpleAdapter adapter;
	
	private List<Map<String,String>> data;
	
	private DatabaseOpenHelper helper;
	
	private SectionDao sDao;
	
	@Override
	protected void onCreate(Bundle savedState){
		
		super.onCreate(savedState);
		setContentView(R.layout.update_list);
		setTitle("小说更新记录");
		
		data = new ArrayList<Map<String,String>>();
		
		helper = new DatabaseOpenHelper(this,2);
		sDao = new SectionDao(helper.getWritableDatabase());
		
		listView = (ListView) findViewById(R.id.listView);
		initData();
		adapter = new SimpleAdapter(this,data,R.layout.update_list_item,
				new String[]{"title","desc"},new int[]{R.id.novelTitle,R.id.novelDesc});
		
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(this);
	}
	
	private void initData(){
		
		List<Section> sections = sDao.listAll();
		
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
	public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
		
		Intent intent = new Intent(this,NovelPostActivity.class);
		Map<String,String> map = data.get(position);
		intent.putExtra("path",map.get("path"));
		intent.putExtra("title",map.get("title"));
		startActivity(intent);
		
	}
}
