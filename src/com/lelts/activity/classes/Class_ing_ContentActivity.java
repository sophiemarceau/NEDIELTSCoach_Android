package com.lelts.activity.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lels.activity.student.Stu_CorrectActivity;
import com.lels.activity.student.Stu_ListActivity;
import com.lels.activity.student.Stu_StartActivity;
import com.lels.activity.student.Stu_TaskActivity;
import com.lels.adapter.classesfm_details.Class_ing_Adapter;
import com.lels.adapter.classesfm_details.PopAdapter;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.myclassing.frag.Class_ing_Frag_Cs;
import com.myclassing.frag.Class_ing_Frag_Mk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
public class Class_ing_ContentActivity extends Activity implements
		OnClickListener {

	private GridView mGridView;
	private ImageButton mImg_back;
	private Context mContext;
	private Class_ing_Adapter mAdapter;
	@SuppressWarnings("unused")
	// private String PATH =
	// "http://img04.sogoucdn.com/app/a/100520024/96e086f51df001cbbe99ed2c7994d12f";
	private RelativeLayout mRelative_total_num, mRelative_on_total,
			mRelative_start_total, mRelative_stop_total;
	private ListView pop_listview;
	private List<HashMap<String, Object>> pop_list;
	private SharedPreferences share;
	private String classCode, lessonId, sName, classid;
	private String url = Constants.URL_TeacherClassesgetLessonInfo;
	private List<HashMap<String, Object>> mdataList;
	private RelativeLayout lin_pop;
	private TextView classes_num, ing_class_num_value, classes_time_value,
			classes_adress_value, student_totle_num, stu_on_value,
			stu_start_value, stu_stop_value, ing_tv;
	private View contentView;
	private PopupWindow popupWindow;
	private LinearLayout lin;
	private SharedPreferences shareclass_ing;
	private Editor editor;
	private RelativeLayout relative_warn_nulldata_clastudy;
	private int icount,pophight;
	private HorizontalScrollView scrollview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_class_ing);

		mContext = this;

		getintent();
		initview();
		getList();
		// addFrag();
	}

	/**
	 * 获得参数数据;
	 */
	private void getintent() {

		shareclass_ing = getSharedPreferences("clcode", MODE_PRIVATE);
		sName = shareclass_ing.getString("sName", "");
		classCode = shareclass_ing.getString("scode", "");
		lessonId = shareclass_ing.getString("nowLessonId", "");
		classid = shareclass_ing.getString("id", "");
		System.out.println("classid----------" + classid + "lessonId---"
				+ lessonId + "===============");
	}

	/*
	 * 得到gridview的list集合
	 */
	private void getList() {

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("sClassId", classid);
		params.addBodyParameter("lessonId", lessonId);

		HttpUtils util = new HttpUtils();
		util.configCurrentHttpCacheExpiry(0);
		util.configDefaultHttpCacheExpiry(0);
		util.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(mContext, "数据为空", Toast.LENGTH_LONG).show();
				LodDialogClass.closeCustomCircleProgressDialog();
			}

			@SuppressWarnings("unused")
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				LodDialogClass.closeCustomCircleProgressDialog();
				String result = arg0.result;
				System.out.println("-----" + arg0.result);
				JSONObject obj;
				try {
					obj = new JSONObject(result);
					JSONObject obj1 = obj.getJSONObject("Data");
					// 时间
					String times = obj1.getString("times");
					classes_time_value.setText(times);
					// 地点
					String sName = obj1.getString("sName");
					String sAddress = obj1.getString("sAddress");
					if (sName.equals("null") || sName.equals("")) {
						classes_adress_value.setText("");
					} else {
						if (sAddress.equals("null")) {

							classes_adress_value.setText("" + sName);
						} else {
							classes_adress_value.setText(sAddress + sName);
						}
					}
					// 班级人数
					String stuNum = obj1.getString("stuNum");
					student_totle_num.setText(stuNum);

					// 待批改数
					String correctNum = obj1.getString("correctNum");
					stu_stop_value.setText(correctNum);

					// 任务数
					String taskNum = obj1.getString("taskNum");
					stu_start_value.setText(taskNum);

					// 课上练习数
					String paperSum = obj1.getString("paperSum");
					stu_on_value.setText(paperSum);
					// 获取模考次数
					String lessonScoresCnt = obj1.getString("lessonScoresCnt");
					icount = Integer.parseInt(lessonScoresCnt);
					FragmentManager fm = getFragmentManager();
					FragmentTransaction ft = fm.beginTransaction();
					if (icount == 0) {
					} else if (icount == 1) {
						List<Integer> manylist = new ArrayList<Integer>();
						JSONObject objone = obj1.getJSONObject("one");
						manylist.add(Integer.valueOf(objone
								.getString("TotalScore")));
						manylist.add(Integer.valueOf(objone
								.getString("ListenScore")));
						manylist.add(Integer.valueOf(objone
								.getString("SpeakScore")));
						manylist.add(Integer.valueOf(objone
								.getString("ReadScore")));
						manylist.add(Integer.valueOf(objone
								.getString("WriteScore")));
						ft.add(R.id.frag, new Class_ing_Frag_Cs(manylist));
						ft.commit();
					} else if (icount > 1) {
						JSONArray arrymany = obj1.getJSONArray("many");
						List<HashMap<String, String>> manylist = new ArrayList<HashMap<String, String>>();
						for (int i = 0; i < arrymany.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							/*
							 * "TotalScore": "0", "nLessonNo": 3, "ST_ID": 1403
							 */
							JSONObject manyobj = arrymany.getJSONObject(i);
							int TotalScore = Integer.valueOf(manyobj
									.getString("TotalScore"));

							map.put("TotalScore",
									manyobj.getString("TotalScore"));
							map.put("nLessonNo", manyobj.getString("nLessonNo"));
							manylist.add(map);

							System.out.println("TotalScore--"
									+ manyobj.getString("TotalScore")
									+ "--nLessonNo--"
									+ manyobj.getString("nLessonNo"));

						}

						ft.add(R.id.frag, new Class_ing_Frag_Mk(manylist));
						ft.commit();
					}
					getPoplist(obj1);
					popItemlistener();
					getTeacherList(obj1);

					// addFrag("1");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			private void popItemlistener() {
				pop_listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						ing_tv.setText(pop_list.get(position).get("nLessonNo")
								.toString());
						lessonId = pop_list.get(position).get("id").toString();
						System.out.println("lessonId-------" + lessonId);

						editor.putString("nowLessonId", lessonId);
						editor.commit();
						getList();
						popupWindow.dismiss();
					}
				});
			}

			private void getPoplist(JSONObject obj1) throws JSONException {
				pop_list = new ArrayList<HashMap<String, Object>>();
				JSONArray arraylessons = obj1.getJSONArray("lessons");
				for (int i = 0; i < arraylessons.length(); i++) {
					JSONObject lessondata = arraylessons.getJSONObject(i);
					HashMap<String, Object> mappop = new HashMap<String, Object>();
					mappop.put("nLessonNo", lessondata.getString("nLessonNo"));
					mappop.put("id", lessondata.getString("id"));
					pop_list.add(mappop);
					if (lessondata.getString("id").equals(lessonId)) {
						ing_tv.setText(lessondata.getString("nLessonNo"));
					}
				}
				System.out.println("pop_list>>>>>>>>>>>" + pop_list);
				PopAdapter popadapter = new PopAdapter(pop_list, mContext);
				pop_listview.setAdapter(popadapter);
				pophight = getTotalHeightofListView(pop_listview);
			}

			private void getTeacherList(JSONObject obj1) throws JSONException {
				mdataList = new ArrayList<HashMap<String, Object>>();
				JSONArray arrayteachers = obj1.getJSONArray("teachers");
				for (int k = 0; k < arrayteachers.length(); k++) {
					JSONObject data = arrayteachers.getJSONObject(k);
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("sName", data.getString("sName"));
					map.put("IconUrl", data.getString("IconUrl"));
					mdataList.add(map);
				}
				if (mdataList.size() > 0) {

					mAdapter = new Class_ing_Adapter(mContext, mdataList);
					mGridView.setAdapter(mAdapter);
					mGridView.setNumColumns(mdataList.size());
				} else {
					scrollview.setVisibility(View.GONE);
					relative_warn_nulldata_clastudy.setVisibility(View.VISIBLE);
				}

				/*
				 * DisplayMetrics dm = new DisplayMetrics();
				 * getWindowManager().getDefaultDisplay().getMetrics(dm); float
				 * density = dm.density; int gridviewWidth = (int) (size *
				 * (length + 4) * density); int itemWidth = (int) (length *
				 * density);
				 */

				DisplayMetrics dm = new DisplayMetrics();
				// 取得窗口属性
				getWindowManager().getDefaultDisplay().getMetrics(dm);
				// 窗口的宽度
				int screenWidth = dm.widthPixels;

				getWindowManager().getDefaultDisplay().getMetrics(dm);
				float density = dm.density;
				int allWidth = (int) (mdataList.size() * (100 + 4) * density);
				int itemWidth = (int) (100 * density);
				LayoutParams params = new LayoutParams(allWidth,
						LayoutParams.FILL_PARENT);
				mGridView.setLayoutParams(params);
				mGridView.setColumnWidth(itemWidth);
				// mGridView.setHorizontalSpacing(5);
				mGridView.setStretchMode(GridView.NO_STRETCH);
			}
		});
	}

	public int getTotalHeightofListView(ListView listView) {
		ListAdapter mAdapter = (ListAdapter) listView.getAdapter();
		int totalHeight = 0;
		for (int i = 0; i < mAdapter.getCount(); i++) {
			View mView = mAdapter.getView(i, null, listView);
			mView.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			totalHeight += mView.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (mAdapter.getCount() - 1));
		int listviewHeight = params.height;
		return listviewHeight;

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getList();
	}

	/*
	 * 初始化组件
	 */
	private void initview() {
		scrollview = (HorizontalScrollView) findViewById(R.id.scrollview);
		scrollview.setHorizontalFadingEdgeEnabled(false);
		mGridView = (GridView) findViewById(R.id.grid);
		mImg_back = (ImageButton) findViewById(R.id.ing_class_img);
		ing_tv = (TextView) findViewById(R.id.ing_tv);
		lin_pop = (RelativeLayout) findViewById(R.id.ing_linear_tv);
		lin = (LinearLayout) findViewById(R.id.frag);

		relative_warn_nulldata_clastudy = (RelativeLayout) findViewById(R.id.relative_warn_nulldata_clastudy);

		// 获取布局id
		mRelative_total_num = (RelativeLayout) findViewById(R.id.stu_relative_total_num);
		mRelative_on_total = (RelativeLayout) findViewById(R.id.stu_relative_no_total);
		mRelative_start_total = (RelativeLayout) findViewById(R.id.stu_relative_start_total);
		mRelative_stop_total = (RelativeLayout) findViewById(R.id.stu_relative_stop_total);
		classes_time_value = (TextView) findViewById(R.id.classes_time_value);
		classes_adress_value = (TextView) findViewById(R.id.ing_adress_value);
		student_totle_num = (TextView) findViewById(R.id.student_totle_num);
		stu_on_value = (TextView) findViewById(R.id.stu_on_value);
		stu_start_value = (TextView) findViewById(R.id.stu_start_value);
		stu_stop_value = (TextView) findViewById(R.id.stu_stop_value);

		contentView = LayoutInflater.from(mContext).inflate(
				R.layout.pop_window, null);
		pop_listview = (ListView) contentView.findViewById(R.id.pop_listview);
		// 赋值
		classes_num = (TextView) findViewById(R.id.classes_num);
		ing_class_num_value = (TextView) findViewById(R.id.ing_class_num_value);
		classes_num.setText(classCode);
		ing_class_num_value.setText(sName);
		// 设置监听
		mImg_back.setOnClickListener(this);
		mRelative_total_num.setOnClickListener(this);
		mRelative_on_total.setOnClickListener(this);
		mRelative_start_total.setOnClickListener(this);
		mRelative_stop_total.setOnClickListener(this);
		lin_pop.setOnClickListener(this);
		// 实例化share
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		editor = shareclass_ing.edit();

		LodDialogClass.showCustomCircleProgressDialog(mContext, null,
				getString(R.string.common_Loading));
	}

	/*
	 * 返回按鈕监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ing_class_img:
			finish();
			break;
		case R.id.ing_linear_tv:
			showPopupWindow(v);
			break;
		case R.id.stu_relative_total_num:// 班级人数
			Intent intentlist = new Intent();
			intentlist.putExtra("icount", icount);
			intentlist.setClass(mContext, Stu_ListActivity.class);
			startActivity(intentlist);
			break;
		case R.id.stu_relative_no_total:// 课上练习
			Intent intentstart = new Intent();
			intentstart.setClass(mContext, Stu_StartActivity.class);
			startActivity(intentstart);
			break;
		case R.id.stu_relative_start_total:// 任务数量
			Intent intenttask = new Intent();
			System.out.println("跳转的lessonid>>>>>>" + lessonId);
			intenttask.setClass(mContext, Stu_TaskActivity.class);
			startActivity(intenttask);
			break;
		case R.id.stu_relative_stop_total:// 待批改
			Intent intentstop = new Intent();
			System.out.println("跳转的lessonid>>>>>>" + lessonId);
			intentstop.setClass(mContext, Stu_CorrectActivity.class);
			startActivity(intentstop);
			break;
		}

	}

	private void showPopupWindow(View v) {
		// 一个自定义的布局，作为显示的内容
		int width = lin_pop.getWidth();
		if (pop_list.size()>=10) {
			
			popupWindow = new PopupWindow(contentView, width, pophight/pop_list.size()*10,
					true);
		}else{
			popupWindow = new PopupWindow(contentView, width, pophight,
					true);
		}

		popupWindow.setTouchable(true);

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("mengdd", "onTouch : ");

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.xiala_di));

		// 设置好参数之后再show
		popupWindow.showAsDropDown(v);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
