package com.line.novel.ui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.line.novelapp.R;

public class NovelDetailActivity extends Activity{
	
	private WebView webView; 
	
	@Override
	public void onCreate(Bundle savedState){
		super.onCreate(savedState);
		setContentView(R.layout.novel_detail);
		
		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		String path = getIntent().getStringExtra("path");
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput(path)));
			line = reader.readLine();
			while(line != null){
				sb.append(line);
				line = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		webView.loadData(sb.toString(),"text/html","UTF-8");
	}
	
	
}
