package com.lels.activity.myself;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.example.hello.R;
import com.lels.activity.myself.adapter.PopRemindAdapter;
import com.lels.teacher.db.DbHelper;
import com.lelts.tool.ClockInfo;
import com.widget.time.JudgeDate;
import com.widget.time.ScreenInfo;
import com.widget.time.WheelMain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyselfRemindDeatilActivity extends Activity implements
		OnClickListener {

	private ImageView image1,image2,image3,image4,image5;
	
	private ImageButton imageview_back;
	private Button button_delmark_details;
	private TextView edittext_plan_title;

	private TextView textview_plan_starttime;
	private TextView textview_plan_repeat;
	private TextView textview_plan_sound;
	private TextView textview_plan_remind;

	private TextView edittext_plan_note;

	private View view;

	private TextView edittext_plan_endtime;
	private int id_img;
	private ClockInfo info = new ClockInfo();
	private String c_id;
	public DbHelper dbHelper;
	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_remind_details);

		getdatafromintent();
		init();

		initdata();
	}

	private void getdatafromintent() {
		info = (ClockInfo) getIntent().getExtras().getSerializable("delete");
		c_id =  (String) getIntent().getExtras().getString("c_id");
//		int id_img = 1;
		id_img = getIntent().getIntExtra("id_img", 0);
		System.out.println("传递过来的 info id ====" + info.getId());
		System.out.println("传递过来的 info id ====" + info.getNote());
		System.out.println("传递过来的 c_id ====" + c_id);
		System.out.println("传递过来的 id_img ===="+ id_img);
	}

	private void init() {
		
		image1 = (ImageView) findViewById(R.id.img_right1);
		image2 = (ImageView) findViewById(R.id.img_right2);
		image3 = (ImageView) findViewById(R.id.img_right3);
		image4 = (ImageView) findViewById(R.id.img_right4);
		image5 = (ImageView) findViewById(R.id.img_right5);
		
		
		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		button_delmark_details = (Button) findViewById(R.id.button_delmark_details);
		edittext_plan_title = (TextView) findViewById(R.id.edittext_plan_title);

		textview_plan_starttime = (TextView) findViewById(R.id.textview_plan_starttime);
		textview_plan_repeat = (TextView) findViewById(R.id.textview_plan_repeat);
		textview_plan_sound = (TextView) findViewById(R.id.textview_plan_sound);
		textview_plan_remind = (TextView) findViewById(R.id.textview_plan_remind);
		edittext_plan_note = (TextView) findViewById(R.id.edittext_plan_note);

		edittext_plan_endtime = (TextView) findViewById(R.id.edittext_plan_endtime);

		imageview_back.setOnClickListener(this);
		button_delmark_details.setOnClickListener(this);
	}

	private void initdata() {

		if(id_img==1){
			edittext_plan_title.setText(info.getTitle());
			textview_plan_starttime.setText(info.getStarttime());
			String ss = info.getEndtime();
			if (null != ss) {
				if (ss.length() > 10) {
					StringBuilder sb = new StringBuilder(ss.substring(0, 10));
					/*for (int i = 0; i < ss.length() - 10; i++) {
						sb.append(".");
					}*/
					sb.append("...");
					edittext_plan_endtime.setText(sb.toString());
				} else {
					edittext_plan_endtime.setText(ss);
				}
			}
			textview_plan_repeat.setText(info.getRepeat());
			textview_plan_sound.setText(info.getSound());
			textview_plan_remind.setText(info.getRemind());
			edittext_plan_note.setText(info.getNote());
			image1.setVisibility(View.GONE);
			image2.setVisibility(View.GONE);
			image3.setVisibility(View.GONE);
			image4.setVisibility(View.GONE);
			image5.setVisibility(View.GONE);
		}else{
			edittext_plan_title.setText(info.getTitle());
			textview_plan_starttime.setText(info.getStarttime());
			String ss = info.getEndtime();
			if (null != ss) {
				edittext_plan_endtime.setText(info.getEndtime());
				if (ss.length() > 10) {
					StringBuilder sb = new StringBuilder(ss.substring(0, 10));
					/*for (int i = 0; i < ss.length() - 10; i++) {
						sb.append(".");
					}*/
					sb.append("...");
					edittext_plan_endtime.setText(sb.toString());
				} else {
					edittext_plan_endtime.setText(ss);
				}
			}
			//edittext_plan_endtime.setText(info.getEndtime());
			textview_plan_repeat.setText(info.getRepeat());
			textview_plan_sound.setText(info.getSound());
			textview_plan_remind.setText(info.getRemind());
			edittext_plan_note.setText(info.getNote());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;
		case R.id.button_delmark_details:
			// dbHelper = .dbHelper;
			Log.d("TAG", String.valueOf(info.getId()));

			MyselfRemindActivity.delete(c_id);

			Intent intent = new Intent();
			intent.setClass(MyselfRemindDeatilActivity.this,
					MyselfRemindActivity.class);
			intent.putExtra("c_id", c_id);
			setResult(10, intent);
			finish();
			break;
		default:
			break;
		}
	}

}
