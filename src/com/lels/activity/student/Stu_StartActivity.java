package com.lels.activity.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.hello.R;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.lels.adapter.student.StudentStartAdapter;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lelts.fragment.classroom.TestReportActivity;
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
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Stu_StartActivity extends Activity implements OnClickListener {
	private SwipeListView mSwipeListView;
	public static int deviceWidth;
	private StudentStartAdapter madapter;
	private List<HashMap<String, Object>> mlist;
	private Context context;
	private ImageButton mBack_img, mAdd_img;
	private SharedPreferences share;
	private String ccId;
	private String Path = Constants.URL_ActiveClassExerciseList;
	private SharedPreferences shareclass_ing, teacherinfo;
	private Editor editor;
	// 提示没有数据的布局
	private RelativeLayout relative_warn_nulldata_clastudy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stu_start);
		System.out.println("stu_start>>>>>>>oncreate");
		getintent();
		initview();
		initdata();
		deviceWidth = getDeviceWidth();
		mSwipeListView
				.setSwipeListViewListener(new TestBaseSwipeListViewListener());
		reload();
	}

	@Override
	protected void onStart() {
		super.onStart();
		initdata();
	}

	private void getintent() {
		teacherinfo = getSharedPreferences("teacherinfo", MODE_PRIVATE);
		editor = teacherinfo.edit();

		shareclass_ing = getSharedPreferences("clcode", MODE_PRIVATE);
		ccId = shareclass_ing.getString("nowLessonId", "");
		System.out.println("---->" + ccId);
	}

	/**
	 * 方法说明：实例化控件
	 *
	 */
	private void initview() {
		context = this;

		LodDialogClass.showCustomCircleProgressDialog(context, null, getString(R.string.common_Loading));
		mSwipeListView = (SwipeListView) findViewById(R.id.listview_activity_stu_start);

		mBack_img = (ImageButton) findViewById(R.id.stustart_back_img);
		mBack_img.setOnClickListener(this);

		mAdd_img = (ImageButton) findViewById(R.id.stustart_add_img);
		mAdd_img.setOnClickListener(this);

		relative_warn_nulldata_clastudy = (RelativeLayout) findViewById(R.id.relative_warn_nulldata_clastudy);

		// 实例化share
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
	}

	/**
	 * 方法说明：初始化数据
	 *
	 */
	private void initdata() {
		/*
		 * mlist = new ArrayList<StudentStartInfo>(); for (int i = 0; i < 15;
		 * i++) { mlist.add(new StudentStartInfo(i+"", "图书馆口语场景练习")); } madapter
		 * = new StudentStartAdapter(context, mlist,mSwipeListView);
		 * mSwipeListView.setAdapter(madapter);
		 */
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);
		utils.send(HttpMethod.GET,
				Path + "?passCode=''" + "&" + "ccId=" + ccId, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LodDialogClass.closeCustomCircleProgressDialog();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						LodDialogClass.closeCustomCircleProgressDialog();
						/*
						 * [Return]：  Infomation = 提示信息;  Result =
						 * 返回结果true/false;  Data = 练习试卷列表[   单个试卷信息{    P_ID =
						 * 试卷ID,    Name = 试卷名称,    PaperNumber = 试卷编号,
						 *    QuestionCount = 试题数量,    ActiveClassPaperInfoId =
						 * 课堂主键ID   }  ]
						 */
						String result = arg0.result;
						System.out.println("---------->" + result);
						try {
							JSONObject obj = new JSONObject(result);
							JSONArray array = obj.getJSONArray("Data");
							mlist = new ArrayList<HashMap<String, Object>>();
							for (int i = 0; i < array.length(); i++) {
								JSONObject obj1 = array.getJSONObject(i);
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("P_ID", obj1.getString("P_ID"));// 试卷ID,
								map.put("Name", obj1.getString("Name"));
								map.put("PaperNumber",
										obj1.getString("PaperNumber"));
								map.put("QuestionCount",
										obj1.getString("QuestionCount"));
								map.put("ActiveClassPaperInfoId", obj1
										.getString("ActiveClassPaperInfoId"));
								map.put("PushStatus",
										obj1.getString("PushStatus"));
								mlist.add(map);
							}
							// 数据为空则提示，listview 消失
							if (mlist.size() != 0) {
								mSwipeListView.setVisibility(View.VISIBLE);
								relative_warn_nulldata_clastudy
										.setVisibility(View.GONE);
								madapter = new StudentStartAdapter(context,
										mlist, mSwipeListView);
								mSwipeListView.setAdapter(madapter);
								madapter.notifyDataSetChanged();
							} else {
								mSwipeListView.setVisibility(View.GONE);
								relative_warn_nulldata_clastudy
										.setVisibility(View.VISIBLE);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	private int getDeviceWidth() {
		return getResources().getDisplayMetrics().widthPixels;
	}

	private void reload() {
		mSwipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
		mSwipeListView.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
		// mSwipeListView.setSwipeActionRight(settings.getSwipeActionRight());
		mSwipeListView.setOffsetLeft((float) (deviceWidth * 1 / 1.3));
		// mSwipeListView.setOffsetRight(convertDpToPixel(settings.getSwipeOffsetRight()));
		mSwipeListView.setAnimationTime(0);
		mSwipeListView.setSwipeOpenOnLongPress(false);

	}

	class TestBaseSwipeListViewListener extends BaseSwipeListViewListener {

		@Override
		public void onClickFrontView(int position) {
			if (Integer.valueOf(mlist.get(position).get("PushStatus")
					.toString()) == 0) {
				Toast.makeText(context, "该试卷没有练习报告", Toast.LENGTH_SHORT).show();
			} else {
				Intent mintent = new Intent();
				mintent.putExtra("pId", mlist.get(position).get("P_ID")
						.toString());
				mintent.putExtra("ccId", ccId);
				mintent.putExtra("titlename", mlist.get(position).get("Name")
						.toString());
				editor.putInt("choose", 3);
				editor.commit();
				mintent.setClass(context, TestReportActivity.class);
				startActivity(mintent);
			}
		}

		@Override
		public void onDismiss(int[] reverseSortedPositions) {
			for (int position : reverseSortedPositions) {
				mlist.remove(position);
			}
			if (mlist.size() == 0) {
				mSwipeListView.setVisibility(View.GONE);
				relative_warn_nulldata_clastudy.setVisibility(View.VISIBLE);
			} else {
				mSwipeListView.setVisibility(View.VISIBLE);
				relative_warn_nulldata_clastudy.setVisibility(View.GONE);
				madapter.notifyDataSetChanged();
			}

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("stu_start>>>>>>>>>>>>ondestroy");
	}

	/*
	 * 返回按钮监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.stustart_back_img:
			finish();
			break;
		case R.id.stustart_add_img:
			Intent intent = new Intent();
			System.out.println("stu_start>>>>>>>" + ccId);
			intent.setClass(context, Add_StuStart_Activity.class);
			startActivity(intent);
		default:
			break;
		}
	}

}
