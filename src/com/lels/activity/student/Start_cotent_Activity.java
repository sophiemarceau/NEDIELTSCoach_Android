package com.lels.activity.student;



import java.util.ArrayList;
import java.util.List;

import com.example.hello.R;
import com.lels.adapter.student.Startcotent_Adapter;
import com.lels.bean.AnswerTestInfo;
import com.lels.bean.TestReportInfo;
import com.lels.constants.Constants;
import com.lelts.activity.classroomconnection.adapter.TestReportAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

public class Start_cotent_Activity extends Activity {
	
	private ListView mListview;
	private Context mContext;
	private ImageButton mImageview_back;
	private Startcotent_Adapter mAdapter;
	private List<AnswerTestInfo> mlist;
	private String path = Constants.URL_findWholeSubmitModeStudentExerciseReport;
	private String paperId;
	private String ccId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_cotent);
		getintentvalues();
		initview();
		gethttp();
		
	}

	private void getintentvalues() {
		Intent intent = getIntent();
		paperId = intent.getStringExtra("pid");
		ccId = intent.getStringExtra("ccid");
	}
	/**
	 * 方法说明：初始化数据
	 *
	 */
	private void initdata() {
		// TODO Auto-generated method stub
//		mlist = new ArrayList<TestReportInfo>();
		for (int i = 0; i <15; i++) {
			TestReportInfo info = new TestReportInfo();
			info.setTestanswer("正确答案：C");
			info.setTestname(i+1+".选择题名称");
			info.setProgress(50);
//			mlist.add(info);
		}
		
//		madapter = new TestReportAdapter(mlist, context);
//		mlistview.setAdapter(madapter);
	}
	private void gethttp() {
		HttpUtils util = new HttpUtils();
		util.send(HttpMethod.GET, path+"?ccId="+ccId+"&paperId="+paperId, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				
			}
		});
	}

	private void initview() {
		mContext = this;
		mImageview_back = (ImageButton) findViewById(R.id.startcotent_back_img);
		mListview = (ListView) findViewById(R.id.start_cotent_listview);
	}
}
