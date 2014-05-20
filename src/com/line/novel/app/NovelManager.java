package com.line.novel.app;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.line.novel.database.DatabaseOpenHelper;
import com.line.novel.database.Novel;
import com.line.novel.database.NovelDao;
import com.line.novel.database.Section;
import com.line.novel.database.SectionDao;
import com.line.novel.ui.NovelDetailActivity;
import com.line.novelapp.R;

public class NovelManager extends Service{
	
	private NovelDao nDao;
	
	private SectionDao sDao;
	
	private DatabaseOpenHelper helper;
	
	private NotificationManager nManager;
	
	private Map<String,Timer> timers;
	
	@Override
	public void onCreate(){
		super.onCreate();
		helper = new DatabaseOpenHelper(this,2);
		nDao = new NovelDao(helper.getWritableDatabase());
		sDao = new SectionDao(helper.getWritableDatabase());
		nManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE
				);
		timers = new HashMap<String,Timer>();
	}
	
	@Override
	public IBinder onBind(Intent intent){
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		
		Novel novel = intent.getParcelableExtra("novel");
		nDao.addNovel(novel);
		
		//通知小说列表更新小说
		Intent updateIntent = new Intent("com.line.novel.NOVEL_LIST");
		updateIntent.putExtra("novel",novel);
		sendBroadcast(updateIntent);
		
		Timer timer = new Timer();
		timers.put(novel.getId(),timer);
		timer.schedule(new NovelTimerTask(this, timer, novel),0);
		return Service.START_REDELIVER_INTENT;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		sDao.closeDb();
		nDao.closeDb();
		helper.close();
	}
	
	class NovelTimerTask extends TimerTask{
		
		private Timer timer;
		
		private Novel novel;
		
		private Context context;
		
		public NovelTimerTask(Context context,Timer timer,Novel novel){
			this.timer = timer;
			this.novel = novel;
			this.context = context;
			
		}
		
		@Override
		public void run(){
			
			novel.vertifyUpdate();
			nDao.updateNovel(novel);
			System.out.println("最新章节" + novel.getLastSection());
			HttpGet hGet = new HttpGet("http://tieba.baidu.com/f?kw="+novel.getName());
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse resp = httpClient.execute(hGet);
				HttpEntity entity = resp.getEntity();
				String result = EntityUtils.toString(entity, "UTF-8");
				Document doc = Jsoup.parse(result);
				Elements nodes = doc.getElementsByClass("threadlist_title");
				
				for(int i = 0; i<nodes.size();i++){
					final Element link = nodes.get(i).getElementsByTag("a").get(0);
					if(novel.isUpdate(link.attr("title"))){
						System.out.println("标题：" + link.attr("title"));
						new Thread(new Runnable(){
							@Override
							public void run(){
								HttpClient httpClient = new DefaultHttpClient();
								HttpGet hGet = new HttpGet("http://tieba.baidu.com"
										+ link.attr("href"));
								try {
									//获取小说正文
									System.out.println("url-->"+hGet.getURI());
									HttpEntity entity = httpClient.execute(hGet).getEntity();
									String result = EntityUtils.toString(entity,"UTF-8");
//									System.out.println("result");
									Document doc = Jsoup.parse(result);
									Element body = doc.getElementsByClass("d_post_content_firstfloor").get(0);
									
									//保存到本地
									String fileName = link.attr("href").replaceAll("/","")+".html";
									FileOutputStream output = context.openFileOutput(fileName,MODE_PRIVATE);
									OutputStreamWriter writer = new OutputStreamWriter(output);
									writer.write("<html><head></head><body>" + body.toString() + "</body></html>");
									writer.close();
									
									//更新最新章节
									novel.updateLastSection(link.attr("title").replaceAll(" ",""));
									
									//保存记录到数据库
									int index = body.toString().lastIndexOf("----");
									String content = body.toString().trim().replaceAll(" ","").replaceAll("<.*/>","").substring(index,index+100);
									Section section = new Section(link.attr("title"),content,fileName); 
									sDao.insert(section);
									
									//通知更新小说列表有消息需要刷新
									Intent updateIntent = new Intent("com.line.novel.NOVEL_UPDATE");
									updateIntent.putExtra("section",section);
									context.sendBroadcast(updateIntent);
									
									//通知栏
									NotificationCompat.Builder mBuilder =
									        new NotificationCompat.Builder(context)
									        .setSmallIcon(R.drawable.ic_launcher)
									        .setContentTitle(link.attr("title"));
									
									Intent detailIntent = new Intent(context,NovelDetailActivity.class);
									detailIntent.putExtra("path",section.getPath());
									PendingIntent pi = PendingIntent.getActivity(context,
											0, detailIntent,PendingIntent.FLAG_ONE_SHOT);
									mBuilder.setContentIntent(pi);
									nManager.notify(Integer.valueOf(section.getPath().substring(1,3)), mBuilder.build());
									
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}).start();
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		
			Calendar calendar = Calendar.getInstance();
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			
			if(0 <= hour && hour < 7){
				timer.schedule(new NovelTimerTask(context,timer,novel),(7-hour)*1000*60*60);
			}else{
				timer.schedule(new NovelTimerTask(context,timer,novel),20*1000*60);
			}
		}
		
	}
	
}
