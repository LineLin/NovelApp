package com.line.novel.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.line.novel.database.Novel;
import com.line.novelapp.R;

public class AddNovelActivity extends Activity implements OnClickListener{
	
	private Button addBtn;
	
	private Button cancelBtn;
	
	private TextView nameView;
	
	private TextView authorView;
	
	private TextView sectionView;
	
	@Override
	protected void onCreate(Bundle savedState){
		super.onCreate(savedState);
		setContentView(R.layout.novel_add);
		addBtn = (Button) findViewById(R.id.addBtn);
		cancelBtn = (Button) findViewById(R.id.cancerBtn);
		addBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		
		nameView = (TextView) findViewById(R.id.novelName);
		authorView = (TextView) findViewById(R.id.novelAuthor);
		sectionView = (TextView) findViewById(R.id.novelLastSection);
	}
	
	@Override
	public void onClick(View view){
		
		if(view == addBtn){
			
			String name = nameView.getText().toString();
			String author = authorView.getText().toString();
			String section = sectionView.getText().toString();
			Novel novel = new Novel(name,author,section);
			Intent intent =  new Intent("com.line.novel.SERVICE_MANAGER");
			intent.putExtra("novel",novel);
			startService(intent);
			
		}
		this.finish();
	}
}
