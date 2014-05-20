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
	
	private ArrayAdapter<Novel> adapter;
	
	private DatabaseOpenHelper helper;
	
	private NovelDao nDao;
	
	private MyReceiver receiver;
	
	@Override
	protected void onCreate(Bundle saveState){
		super.onCreate(saveState);
		setContentView(R.layout.novel_list);
		
		//打开数据库链接
		helper = new DatabaseOpenHelper(this,1);
		nDao = new NovelDao(helper.getWritableDatabase());
		

		
		//初始化和注册接收器
		receiver = new MyReceiver();
		registerReceiver(receiver, new IntentFilter("com.line.novel.NOVEL_LIST"));		

		initData();
		listView = (ListView) findViewById(R.id.listView);
		adapter = new ArrayAdapter<Novel>(this,R.layout.novel_list_item,
				novels);
		listView.setAdapter(adapter);
		addBtn = (Button) findViewById(R.id.addNovel);
		addBtn.setOnClickListener(this);
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
	public void onDestroy(){
		super.onDestroy();
		unregisterReceiver(receiver);
		nDao.closeDb();
		helper.close();
	}
	
	class MyReceiver extends BroadcastReceiver{
		
		@Override
		public void onReceive(Context context,Intent intent){
			System.out.println("Novel接受的广播");
			Novel novel = intent.getParcelableExtra("novel");
			novels.add(novel);
			adapter.notifyDataSetChanged();
		}

	}
}
