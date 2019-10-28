package com.lelts.activity.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.adapter.classesfm_details.Class_ing_Adapter;
import com.lels.adapter.classesfm_details.Class_no_Adapter;
import com.lels.bean.Teacher;
import com.lels.constants.Constants;
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
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Class_no_ContentActivity extends Activity implements OnClickListener {
	private GridView mGridView;
	private List<HashMap<String,Object>> mList;
	private Context mContext;
	private Class_no_Adapter mAdapter;
	private ImageButton class_back_img;
	private SharedPreferences share,shareclass_ing;
	private String classCode, lessonId, sName,classid;
	private String url = Constants.URL_TeacherClassesgetLessonInfo;
	private TextView classes_num;
	private TextView clesses_num_value;
	private TextView student_totle_num;
	private TextView classes_time_value;
	private TextView classes_adress_value;
	private TextView ing_tv;
	private RelativeLayout relative_warn_nulldata_clastudy;
	private HorizontalScrollView scrollview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classes);
		
		mContext=this;
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		getintent();
		initview();
		getList();
		
	}
	/**
	 * 获得传过来的数据;
	 */
	private void getintent() {
		shareclass_ing = getSharedPreferences("clcode", MODE_PRIVATE);
		sName = shareclass_ing.getString("sName", "");
		classCode = shareclass_ing.getString("scode", "");
		lessonId = shareclass_ing.getString("nowLessonId", "");
		classid = shareclass_ing.getString("id", "");
		System.out.println("sname="+sName+"classcode="+classCode+"lessonid="+lessonId+"===============");
	}
	private void getList() {
		/*mList=new ArrayList<Teacher>();
		mList.add(new Teacher(PATH,"李晓东", "写作老师"));
		mList.add(new Teacher(PATH,"待定", "听力老师"));
		mList.add(new Teacher(PATH,"待定", "阅读老师"));
		mAdapter=new Class_no_Adapter(mList, mContext);
		mGridView.setAdapter(mAdapter);*/
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		System.out.println("classid=" + classid + "------lessonid="
				+ lessonId + "--------");
		params.addBodyParameter("sClassId", classid);
		params.addBodyParameter("lessonId", lessonId);

		HttpUtils util = new HttpUtils();
		util.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				System.out.println("------" + result + "--------");
				JSONObject obj;
				try {
					obj = new JSONObject(result);
					JSONObject obj1 = obj.getJSONObject("Data");
					String times = obj1.getString("times");
					classes_time_value.setText(times);
					String roomName = obj1.getString("roomName");
					classes_adress_value.setText(roomName);
					String stuNum = obj1.getString("stuNum");
					student_totle_num.setText(stuNum);
					String lessonsSize = obj1.getString("lessonsSize");
					ing_tv.setText(lessonsSize);
					mList = new ArrayList<HashMap<String, Object>>();
					JSONArray arraylessons = obj1.getJSONArray("lessons");
					JSONArray arrayteachers = obj1.getJSONArray("teachers");
					for (int k = 0; k < arrayteachers.length(); k++) {
						JSONObject data = arrayteachers.getJSONObject(k);
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("sName", data.getString("sName"));
						map.put("IconUrl", data.getString("IconUrl"));
						mList.add(map);
					}
					if (mList.size() > 0) {

						mAdapter = new Class_no_Adapter(mList,mContext);
						mGridView.setAdapter(mAdapter);
						mGridView.setNumColumns(mList.size());
					} else {
						scrollview.setVisibility(View.GONE);
						relative_warn_nulldata_clastudy.setVisibility(View.VISIBLE);
					}
					DisplayMetrics dm = new DisplayMetrics();
					// 取得窗口属性
					getWindowManager().getDefaultDisplay().getMetrics(dm);
					// 窗口的宽度
					int screenWidth = dm.widthPixels;

					getWindowManager().getDefaultDisplay().getMetrics(dm);
					float density = dm.density;
					int allWidth = (int) (mList.size() * (100 + 4) * density);
					int itemWidth = (int) (100 * density);
					LayoutParams params = new LayoutParams(allWidth,
							LayoutParams.FILL_PARENT);
					mGridView.setLayoutParams(params);
					mGridView.setColumnWidth(itemWidth);
					// mGridView.setHorizontalSpacing(5);
					mGridView.setStretchMode(GridView.NO_STRETCH);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	private void initview() {
		scrollview = (HorizontalScrollView) findViewById(R.id.scrollview);
		scrollview.setHorizontalFadingEdgeEnabled(false);
		class_back_img = (ImageButton) findViewById(R.id.class_back_img);
		class_back_img.setOnClickListener(this);
		mGridView=(GridView) findViewById(R.id.grid);
		classes_num = (TextView) findViewById(R.id.classes_num);
		ing_tv = (TextView) findViewById(R.id.ing_tv);
		classes_num.setText(classCode);
		clesses_num_value = (TextView) findViewById(R.id.ing_class_num_value);
		clesses_num_value.setText(sName);
		student_totle_num = (TextView) findViewById(R.id.student_totle_num);
		classes_time_value = (TextView) findViewById(R.id.classes_time_value);
		classes_adress_value = (TextView) findViewById(R.id.ing_adress_value);
		relative_warn_nulldata_clastudy = (RelativeLayout) findViewById(R.id.relative_warn_nulldata_clastudy);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.class_back_img:
			finish();
			break;

		default:
			break;
		}
	}
	
}
