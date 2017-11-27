package com.bmob.lostfound;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toast;

import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.bmob.lostfound.bean.Found;
import com.bmob.lostfound.bean.Lost;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 添加失物/招领信息界面
 * 
 * @ClassName: AddActivity
 * @Description: TODO
 * @author smile
 * @date 2014-5-21 上午11:41:06
 */
public class AddActivity extends BaseActivity implements OnClickListener {

	EditText edit_title, edit_photo, edit_describe,edit_time,edit_place;
	Button btn_back, btn_true;

	TextView tv_add;
	String from = "";
	
	String old_title = "";
	String old_describe = "";
	String old_phone = "";
	String old_time = "";
	String old_place = "";
	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_add);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_add = (TextView) findViewById(R.id.tv_add);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_true = (Button) findViewById(R.id.btn_true);

		edit_photo = (EditText) findViewById(R.id.edit_photo);
		edit_describe = (EditText) findViewById(R.id.edit_describe);
		edit_title = (EditText) findViewById(R.id.edit_title);
		edit_time = (EditText)findViewById(R.id.edit_time);
		edit_place = (EditText)findViewById(R.id.edit_place);
	}

	@Override
	public void initListeners() {
		// TODO Auto-generated method stub
		btn_back.setOnClickListener(this);
		btn_true.setOnClickListener(this);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		from = getIntent().getStringExtra("from");
		old_title = getIntent().getStringExtra("title");
		old_phone = getIntent().getStringExtra("phone");
		old_describe = getIntent().getStringExtra("describe");
		
		edit_title.setText(old_title);
		edit_describe.setText(old_describe);
		edit_photo.setText(old_phone);
		edit_time.setText(old_time);
		edit_place.setText(old_place);
		
		if (from.equals("Lost")) {
			tv_add.setText("添加失物信息");
		} else {
			tv_add.setText("添加招领信息");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btn_true) {
			addByType();
		} else if (v == btn_back) {
			finish();
		}
	}
	String title = "";
	String describe = "";
	String photo="";
	String time ="";
	String place ="";
	
	/**根据类型添加失物/招领
	  * addByType
	  * @Title: addByType
	  * @throws
	  */
	private void addByType(){
		title = edit_title.getText().toString();
		describe = edit_describe.getText().toString();
		photo = edit_photo.getText().toString();
		time = edit_time.getText().toString();
		place = edit_place.getText().toString();
		
		if(TextUtils.isEmpty(title)){
			ShowToast("请填写标题");
			return;
		}
		if(TextUtils.isEmpty(describe)){
			ShowToast("请填写描述");
			return;
		}
		if(TextUtils.isEmpty(photo)){
			ShowToast("请填写手机");
			return;
		}
		if(TextUtils.isEmpty(time)){
			ShowToast("请填写时间");
			return;
		}
		if(TextUtils.isEmpty(place)){
			ShowToast("请填写地点");
			return;
		}
		new Thread(new Runnable() {
            @Override
            public void run() {
                if(from.equals("Lost")){
                    addLost();
                }else{
                    addFound();
                }
            }
        }).start();
	}
	public Handler handler = new Handler(){

		public void handleMessage(android.os.Message msg) {
			Toast toast1;
			Toast toast2;
			switch(msg.what){
				case 1:
					Toast.makeText(getApplicationContext(), "添加成功，请返回",
							Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(getApplicationContext(), "添加失败",
							Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};
	public String res = null;
	private void addLost(){
		OkHttpClient okHttpClient = new OkHttpClient();
		String url ="http://182.254.133.173/androidapi/create_lost_items.php?what="+title+"&when="+time+"&where="+place+"&detail="+describe+"&contact="+photo+"";
		Request request = new Request.Builder()
				.url(url)
				.build();
		

		try{
			Response response = okHttpClient.newCall(request).execute();
			res = response.body().string();
			handler.sendEmptyMessage(1);
		}catch (Exception e)
		{
			e.printStackTrace();

			handler.sendEmptyMessage(2);
		}
	}
	
	private void addFound(){
		OkHttpClient okHttpClient = new OkHttpClient();
		String url ="http://182.254.133.173/androidapi/create_found_items.php?what="+title+"&when="+time+"&where="+place+"&detail="+describe+"&contact="+photo+"";
		Request request = new Request.Builder()
				.url(url)
				.build();

		try{
			Response response = okHttpClient.newCall(request).execute();
			res = response.body().string();
			handler.sendEmptyMessage(1);
		}catch (Exception e)
		{
			e.printStackTrace();
			handler.sendEmptyMessage(2);
		}
	}



}
