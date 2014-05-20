package com.line.novel.ui;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.line.novel.database.DatabaseOpenHelper;
import com.line.novel.database.Novel;
import com.line.novel.database.NovelDao;
import com.line.novelapp.R;

public class NovelListActivity extends Activity implements OnClickListener{
	
	private List<Novel> novels;
	
	private ListView listView;
	
	private Button addBtn;
	
	private ArrayAdapter<String> adapter;
	
	private DatabaseOpenHelper helper;
	
	private NovelDao nDao;
	
	private MyReceiver receiver;
	
	@Override
	protected void onCreate(Bundle saveState){
		super.onCreate(saveState);
		setContentView(R.layout.novel_list);
		
		helper = new DatabaseOpenHelper(this,1);
		nDao = new NovelDao(helper.getWritableDatabase());
		
		initData();
		listView = (ListView) findViewById(R.id.listView);
		adapter = new ArrayAdapter<String>(this,R.layout.novel_list_item,
				novels.toString().replace("[","").replace("]","").split(", "));
		listView.setAdapter(adapter);
		addBtn = (Button) findViewById(R.id.addNovel);
		addBtn.setOnClickListener(this);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		registerReceiver(receiver, new IntentFilter("com.line.novel.NOVEL_LIST"));
	}
	
	private void initData(){
		/*
		novels = new ArrayList<String>();
		novels.add("武极天下");
		novels.add("绝世唐门");
		novels.add("不败战神");
		*/
		novels = nDao.listAll();
	}
	
	@Override
	public void onClick(View view){
		
		Intent intent = new Intent(this,AddNovelActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onStop(){
		super.onStop();
		unregisterReceiver(receiver);
	}
	
	class MyReceiver extends BroadcastReceiver{
		
		@Override
		public void onReceive(Context context,Intent intent){
			Novel novel = intent.getParcelableExtra("novel");
			novels.add(novel);
			adapter.notifyDataSetChanged();
		}

	}
}
