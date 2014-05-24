package com.line.novel.ui;

import java.util.List;
import java.util.Timer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.line.novel.app.NovelManager;
import com.line.novel.database.DatabaseOpenHelper;
import com.line.novel.database.Novel;
import com.line.novel.database.NovelDao;
import com.line.novelapp.R;

public class NovelListActivity extends Activity implements OnClickListener,OnItemClickListener{
	
	private List<Novel> novels;
	
	private ListView listView;
	
	private Button addBtn;
	
	private ArrayAdapter<Novel> adapter;
	
	private DatabaseOpenHelper helper;
	
	private NovelDao nDao;
	
	private MyReceiver receiver;
	
	private PopupWindow pw;
	
	@Override
	protected void onCreate(Bundle saveState){
		super.onCreate(saveState);
		setContentView(R.layout.novel_list);
		
		//打开数据库链接
		helper = new DatabaseOpenHelper(this,2);
		nDao = new NovelDao(helper.getWritableDatabase());
		

		
		//初始化和注册接收器
		receiver = new MyReceiver();
		registerReceiver(receiver, new IntentFilter("com.line.novel.NOVEL_LIST"));		
		
		//初始化
		initData();
		
		listView = (ListView) findViewById(R.id.listView);
		adapter = new ArrayAdapter<Novel>(this,R.layout.novel_list_item,
				novels);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		
		
		addBtn = (Button) findViewById(R.id.addNovel);
		addBtn.setOnClickListener(this);
	}
	
	private void initData(){
		
		novels = nDao.listAll();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0,1,1,"退出");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		System.exit(1);
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onClick(View view){
		
		Intent intent = new Intent(this,NovelDetailActivity.class);
		intent.putExtra("flag","add");
		startActivity(intent);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view,final int position, long arg3) {
		
		View menuView = LayoutInflater.from(this).inflate(R.layout.popmenu,null);
		TextView title = (TextView) menuView.findViewById(R.id.menu_novel);
		TextView update = (TextView) menuView.findViewById(R.id.menu_xiugai);
		TextView delete = (TextView) menuView.findViewById(R.id.menu_delete);
		title.setText(((TextView)view).getText());
		
		//设置监听器
		update.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){
				Intent intent = new Intent(NovelListActivity.this,NovelDetailActivity.class);
				intent.putExtra("flag","update");
				intent.putExtra("novel",novels.get(position));
				startActivity(intent);
				hidenPopupWindow();
			}
		});
		
		delete.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){
				Novel novel = novels.get(position);
				Timer timer = NovelManager.timers.get(novel.getId());
				if(timer != null){
					timer.cancel();
				}
				nDao.deleteNovel(novel);
				novels.remove(position);
				NovelListActivity.this.adapter.notifyDataSetChanged();
				hidenPopupWindow();
			}
		});
		
		initPopupWindow(view,menuView);
	}
	
	private void initPopupWindow(View parent,View content){
		
		pw = new PopupWindow(content);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		pw.setWidth(dm.widthPixels - 40);
		pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		pw.setTouchable(true);
		pw.setBackgroundDrawable(new BitmapDrawable());
		pw.setOutsideTouchable(true);
		pw.showAtLocation(parent, Gravity.CENTER,0,0);
	}
	
	public void hidenPopupWindow(){
		if(pw != null && pw.isShowing()){
			pw.dismiss();
		}
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
			Novel novel = intent.getParcelableExtra("novel");
			novels.add(novel);
			adapter.notifyDataSetChanged();
		}

	}
}
