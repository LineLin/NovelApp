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

public class NovelDetailActivity extends Activity implements OnClickListener{
	
	private Button vertifyBtn;
	
	private Button cancelBtn;
	
	private TextView nameView;
	
	private TextView authorView;
	
	private TextView sectionView;
	
	@Override
	protected void onCreate(Bundle savedState){
		super.onCreate(savedState);
		setContentView(R.layout.novel_detail);
		vertifyBtn = (Button) findViewById(R.id.vertifyBtn);
		cancelBtn = (Button) findViewById(R.id.cancerBtn);
		vertifyBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		nameView = (TextView) findViewById(R.id.novelName);
		authorView = (TextView) findViewById(R.id.novelAuthor);
		sectionView = (TextView) findViewById(R.id.novelLastSection);
		
		String flag = getIntent().getStringExtra("flag");
		
		if(flag.equals("update")){
			Novel novel = getIntent().getParcelableExtra("novel");
			nameView.setText(novel.getName());
			authorView.setText(novel.getAuthor());
			sectionView.setText(novel.getLastSection());
			vertifyBtn.setText("修改");
		}
		
		if(flag.equals("add")){
			vertifyBtn.setText("添加");
		}
		
	}
	
	@Override
	public void onClick(View view){
		
		if(view == vertifyBtn){
			
			String name = nameView.getText().toString();
			String author = authorView.getText().toString();
			String section = sectionView.getText().toString();
			String flag = getIntent().getStringExtra("flag");
			Novel novel;
			
			if("add".equals(flag)){
				novel = new Novel(name,author,section);
				Intent intent =  new Intent(IndexActivity.ACTION_FLAG_NOVEL_ADD);
				intent.putExtra("novel",novel);
				startService(intent);
			}
			 
			if("update".equals(flag)){
				novel = getIntent().getParcelableExtra("novel");
				novel.setAuthor(author);
				novel.setLastSection(section);
				novel.setName(name);
				Intent intent = new Intent(IndexActivity.ACTION_FLAG_NOVEL_UPDATE);
				intent.putExtra("novel",novel);
				startService(intent);
			}
			
		}
		this.finish();
	}
}
