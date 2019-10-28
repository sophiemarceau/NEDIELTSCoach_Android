package com.lels.activity.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.hello.R;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lels.adapter.student.StuAdapter;
import com.lels.adapter.student.StudentStartAdapter;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lelts.activity.classes.Class_ing_ContentActivity;
import com.lelts.tool.IntentUtlis;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class Stu_ListActivity extends Activity implements OnClickListener {
	private List<HashMap<String, Object>> mStu_list;
	private Context mContext;
	private StuAdapter mAdapter;
	private ListView mListview;
	private ImageButton back_btn;
	private String sClassId;
	private SharedPreferences share, share_class;
	private String PATH = Constants.URL_TeacherClassesgetClassStus;
	
	private int icount;
	private RelativeLayout relative_warn_nulldata_clastudy;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_list);
		mContext = this;
		LodDialogClass.showCustomCircleProgressDialog(mContext, null, getString(R.string.common_Loading));
		getintents();
		initview();
		getlist();
	}

	private void getintents() {
		Intent intent = getIntent();
		icount = intent.getIntExtra("icount", 0);
		System.out.println(icount);
		share_class = getSharedPreferences("clcode", MODE_PRIVATE);
		sClassId = share_class.getString("id", "");

	}

	private void getlist() {
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("sClassId", sClassId);
		System.out.println("sClassId----"+sClassId);
		HttpUtils util = new HttpUtils();
		util.send(HttpMethod.POST, PATH, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				LodDialogClass.closeCustomCircleProgressDialog();
				String result = arg0.result;
				System.out.println(result);
					try {
						JSONObject obj = new JSONObject(result);
						JSONObject obj1 = obj.getJSONObject("Data");
						mStu_list = new ArrayList<HashMap<String, Object>>();
						JSONArray list = obj1.getJSONArray("list");

						for (int k = 0; k < list.length(); k++) {
							JSONObject data = list.getJSONObject(k);
							HashMap<String, Object> map = new HashMap<String, Object>();
							//  StudentName = 学生姓名;
							map.put("sName", data.getString("sName"));
							//   sCode = 学生编号;
							map.put("sCode", data.getString("sCode"));
							map.put("sStudentID", data.getString("sStudentID"));
							//   finishTask = 完成任务率;
							map.put("finishTask", data.getString("finishTask"));
							//  DateDiff = 距离考试时间;
							String DateDiff = data
									.getString("DateDiff");
							
								map.put("DateDiff",DateDiff);
							
							//  avgScore = 模考平均分;
							map.put("avgScore", data.getString("avgScore"));
							// UID = 学生用户ID;
							map.put("UID", data.getString("UID"));
							//  nGender = 性别[1男2女];
							map.put("nGender", data.getString("nGender"));
							// IconUrl = 头像地址;
							map.put("IconUrl", data.getString("IconUrl"));
							map.put("icount", icount);
							System.out.println("IconUrl"+data.getString("IconUrl"));
							mStu_list.add(map);
						}
						// 数据为空则提示，listview 消失
						if (mStu_list.size()> 0) {
							mListview.setVisibility(View.VISIBLE);
							relative_warn_nulldata_clastudy
									.setVisibility(View.GONE);
							setadapter(mStu_list);
						} else {
							mListview.setVisibility(View.GONE);
							relative_warn_nulldata_clastudy
									.setVisibility(View.VISIBLE);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
			}

		});
	}

	private void setadapter(List<HashMap<String, Object>> list) {
		System.out.println("list>>>>>>" + list);
		mAdapter = new StuAdapter(list, mContext);
		mListview.setAdapter(mAdapter);
	}
	private void initview() {
		
		mListview = (ListView) findViewById(R.id.stu_list_listview);
		back_btn = (ImageButton) findViewById(R.id.stulist_back_img);
		back_btn.setOnClickListener(this);

		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		relative_warn_nulldata_clastudy = (RelativeLayout) findViewById(R.id.relative_warn_nulldata_clastudy);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.stulist_back_img:
			finish();
			break;

		default:
			break;
		}
	}

}
