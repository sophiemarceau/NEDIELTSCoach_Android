/*******************************************************************************
z * 2015年6月19日 
 * 
 *******************************************************************************/
package com.lelts.fragment.classroom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.lels.bean.AnswerSheetInfo;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lelts.activity.classroomconnection.adapter.AnswerSheetAdapter;
import com.lelts.activity.classroomconnection.adapter.TeacherClassAdapter;
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
import android.widget.Toast;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年6月19日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class AnswerSheetActivity extends Activity implements OnClickListener {
	private SwipeListView mSwipeListView;
	public static int deviceWidth;
	private AnswerSheetAdapter madapter;
	private List<HashMap<String, Object>> mlist;
	private Context context;
	private ImageButton img_add, img_back;
	private String ccId, code, ActiveClassPaperInfoId;
	private int flush;
	private SharedPreferences share, teacherinfo;
	// 判断试卷是否能点击或者删除
	private int PushStatus;
	//判断试卷是否打包成功
	private String PaperState;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer_sheet);
		ExitApplication.getInstance().addActivity(this);
		initview();
		gettest();
		deviceWidth = getDeviceWidth();
		mSwipeListView
				.setSwipeListViewListener(new TestBaseSwipeListViewListener());
		reload();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		System.out.println("执行  ===========onResume()");

		initview();
		gettest();
		deviceWidth = getDeviceWidth();
		mSwipeListView
				.setSwipeListViewListener(new TestBaseSwipeListViewListener());
		reload();
		super.onResume();
	}

	/**
	 * 方法说明：实例化控件
	 *
	 */
	private void initview() {
		// TODO Auto-generated method stub
		context = this;
		mSwipeListView = (SwipeListView) findViewById(R.id.listview_activity_answer_sheet);
		img_add = (ImageButton) findViewById(R.id.img_add_activity_answer_sheet);
		img_add.setOnClickListener(this);
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		teacherinfo = getSharedPreferences("teacherinfo", Context.MODE_PRIVATE);
		code = teacherinfo.getString("code", null);
		ccId = teacherinfo.getString("ccId", null);
		img_back = (ImageButton) findViewById(R.id.img_back_activity_answer_sheet);
		img_back.setOnClickListener(this);
		LodDialogClass.showCustomCircleProgressDialog(context, null,
				getString(R.string.common_Loading));
	}

	/**
	 * 方法说明：网络获取试卷
	 *
	 */
	private void gettest() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		System.out.println("code==========" + code);
		System.out.println("ccId=============" + ccId);

		// + "?passCode=" + code+"&ccId="+ccId,
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);
		utils.send(HttpMethod.GET, Constants.URL_ActiveClassExerciseList
				+ "?passCode=" + code + "&ccId=" + ccId, params,
				new RequestCallBack<String>() {

					//  P_ID = 试卷ID,
					//    Name = 试卷名称,
					//    PaperNumber = 试卷编号,
					//    QuestionCount = 试题数量,
					//    ActiveClassPaperInfoId = 课堂主键ID,
					//    PushStatus = 随堂练习 推送状态：
					// 0未推送1已推送2已完成(如果PushStatus>0,说明已被使用,则不可删除)
//			  PaperState = 试卷状态，0 试卷初始创建、1 试卷未发布、2 试卷发布成功等待打包、3 试卷打包完成,
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						LodDialogClass.closeCustomCircleProgressDialog();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						System.out.println("listresult====================="
								+ result);
						try {
							JSONObject obj = new JSONObject(result);
							JSONArray array = obj.getJSONArray("Data");
							mlist = new ArrayList<HashMap<String, Object>>();
							for (int i = 0; i < array.length(); i++) {
								JSONObject data = array.getJSONObject(i);
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("Name", data.getString("Name"));
								map.put("QuestionCount",
										data.getString("QuestionCount"));
								map.put("P_ID", data.getString("P_ID"));
								map.put("ActiveClassPaperInfoId", data
										.getString("ActiveClassPaperInfoId"));
								map.put("PushStatus", data.getInt("PushStatus"));
								map.put("PaperState", data.getString("PaperState"));
								mlist.add(map);
							}

							System.out.println("mylist=============" + mlist);
							madapter = new AnswerSheetAdapter(context, mlist,
									mSwipeListView);
							mSwipeListView.setAdapter(madapter);
							madapter.notifyDataSetChanged();
							LodDialogClass.closeCustomCircleProgressDialog();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
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
			PushStatus = Integer.parseInt(mlist.get(position).get("PushStatus")
					.toString());
			PaperState = mlist.get(position).get("PaperState").toString();
			System.out.println("PushStatus===试卷的状态=="
					+ Integer.parseInt(mlist.get(position).get("PushStatus")
							.toString())+"是否打包完成====="+PaperState);
			if (PushStatus > 0) {
				// 不可删除和点击
				Toast.makeText(context, "该试卷已被使用！", Toast.LENGTH_SHORT).show();
			} else {
				// 可以删除和点击
				Intent intent = new Intent(context, AnswerTestActivity.class);
				intent.putExtra("pId", mlist.get(position).get("P_ID")
						.toString());
				intent.putExtra("code", code);
				intent.putExtra("ActiveClassPaperInfoId", mlist.get(position)
						.get("ActiveClassPaperInfoId").toString());
				intent.putExtra("Name", mlist.get(position).get("Name")
						.toString());
				intent.putExtra("PaperState",PaperState);
				startActivity(intent);
				// 销毁页面
				// finish();
			}

		}

		@Override
		public void onDismiss(int[] reverseSortedPositions) {
			for (int position : reverseSortedPositions) {
				mlist.remove(position);
			}
			madapter.notifyDataSetChanged();
		}
	}

	/*
	 * (non-Javadoc) 点击按钮 事件
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 添加练习按钮
		case R.id.img_add_activity_answer_sheet:
			IntentUtlis.sysStartActivity(context, AddTestActivity.class);
			// finish();
			break;
		// 返回按钮
		case R.id.img_back_activity_answer_sheet:
			// IntentUtlis.sysStartActivity(context, ClassRoomStudent.class);
			finish();
			break;

		default:
			break;
		}
	}

}
