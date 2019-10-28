package com.lels.activity.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.adapter.student.Stu_TaskAdapter;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class Stu_TaskActivity extends Activity implements OnClickListener {
	private ImageButton taskBackIcon;
	private ListView taskListView;
	private List<HashMap<String, Object>> taskList, typelist1, typelist2,
			typelist3,typelist4;
	private Stu_TaskAdapter taskAdapter;
	private Context mContext;
	private String classid;
	private String lessonid;
	private SharedPreferences share, shares;
	private String url = Constants.URL_HomeloadTaskList;
	private RelativeLayout relative_warn_nulldata_clastudy;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stu_task);
		getintent();
		initView();
		initData();
		setListener();
	}

	/**
	 * 获取传递的classid班级课次,lessonid课次id.
	 */
	private void getintent() {
		shares = getSharedPreferences("clcode", MODE_PRIVATE);
		classid = shares.getString("id", "");
		lessonid = shares.getString("nowLessonId", "");
		System.out.println(classid + ">>>>>>>>>" + lessonid);
		// 实例化share
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
	}

	/**
	 * 按钮监听
	 */
	private void setListener() {
		taskBackIcon.setOnClickListener(this);
	}

	/**
	 * 获得list集合数据
	 */
	private void initData() {

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("classId", classid);
		params.addBodyParameter("lessonId", lessonid);

		HttpUtils utils = new HttpUtils();
		utils.configSoTimeout(1000*10);
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			private String result;

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LodDialogClass.closeCustomCircleProgressDialog();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				LodDialogClass.closeCustomCircleProgressDialog();
				String result = arg0.result;
				System.out.println("???" + result);
				JSONObject obj;
				try {
					obj = new JSONObject(result);
					result = obj.getString("Result");
					JSONObject obj1 = obj.getJSONObject("Data");
					JSONArray array = obj1.getJSONArray("list");

					taskList = new ArrayList<HashMap<String, Object>>();
					typelist1 = new ArrayList<HashMap<String, Object>>();
					typelist2 = new ArrayList<HashMap<String, Object>>();
					typelist3 = new ArrayList<HashMap<String, Object>>();
					typelist4 = new ArrayList<HashMap<String, Object>>();

					for (int i = 0; i < array.length(); i++) {
						JSONObject data = array.getJSONObject(i);
						if (data.getString("TaskType").equals("1")) {
							HashMap<String, Object> map1 = new HashMap<String, Object>();
							map1.put("checkFinish",
									data.getString("checkFinish"));// 是否完成
																	// 0=未完成；1=已完成;
							map1.put("checkRemind",
									data.getString("checkRemind"));// 是否已提醒
																	// 0=未完成；1=已完成;
							map1.put("TaskType", data.getString("TaskType"));// 任务类型
																				// 1:模考;
																				// 2:练习;
																				// 3:资料;
							map1.put("CreateTime", data.getString("CreateTime"));// 创建时间;
							map1.put("ST_ID", data.getString("ST_ID"));// 任务主键
							map1.put("Name", data.getString("Name"));// name
							map1.put("RefID", data.getString("RefID"));// 引用关联的ID[资料ID或模考、练习ID];
							map1.put("StorePoint", data.getString("StorePoint"));// 存储点[只针对任务类型为3，如果任务类型为1或2，默认为0，
																					// 1:够快网盘;
																					// 2:CC视频]
							map1.put("classid", classid);
							typelist1.add(map1);
						} else

						if (data.getString("TaskType").equals("2")) {

							HashMap<String, Object> map2 = new HashMap<String, Object>();
							map2.put("checkFinish",
									data.getString("checkFinish"));// 是否完成
																	// 0=未完成；1=已完成;
							map2.put("checkRemind",
									data.getString("checkRemind"));// 是否已提醒
																	// 0=未完成；1=已完成;
							map2.put("TaskType", data.getString("TaskType"));// 任务类型
																				// 1:模考;
																				// 2:练习;
																				// 3:资料;
							map2.put("CreateTime", data.getString("CreateTime"));// 创建时间;
							map2.put("ST_ID", data.getString("ST_ID"));// 任务主键
							map2.put("RefID", data.getString("RefID"));// 引用关联的ID[资料ID或模考、练习ID];
							map2.put("StorePoint", data.getString("StorePoint"));// 存储点[只针对任务类型为3，如果任务类型为1或2，默认为0，
																					// 1:够快网盘;
																					// 2:CC视频]
							map2.put("Name", data.getString("Name"));// name
							map2.put("classid", classid);
							typelist2.add(map2);
						} else if (data.getString("TaskType").equals("3")) {

							HashMap<String, Object> map3 = new HashMap<String, Object>();
							map3.put("checkFinish",
									data.getString("checkFinish"));// 是否完成
																	// 0=未完成；1=已完成;
							map3.put("checkRemind",
									data.getString("checkRemind"));// 是否已提醒
																	// 0=未完成；1=已完成;
							map3.put("TaskType", data.getString("TaskType"));// 任务类型
																				// 1:模考;
																				// 2:练习;
																				// 3:资料;
							map3.put("CreateTime", data.getString("CreateTime"));// 创建时间;
							map3.put("ST_ID", data.getString("ST_ID"));// 任务主键
							map3.put("RefID", data.getString("RefID"));// 引用关联的ID[资料ID或模考、练习ID];
							map3.put("StorePoint", data.getString("StorePoint"));// 存储点[只针对任务类型为3，如果任务类型为1或2，默认为0，
																					// 1:够快网盘;
																					// 2:CC视频]
							map3.put("Name", data.getString("Name"));// name
							map3.put("classid", classid);
							typelist3.add(map3);
						} else if (data.getString("TaskType").equals("4")) {
							HashMap<String, Object> map4 = new HashMap<String, Object>();
							map4.put("checkFinish",
									data.getString("checkFinish"));// 是否完成
																	// 0=未完成；1=已完成;
							map4.put("checkRemind",
									data.getString("checkRemind"));// 是否已提醒
																	// 0=未完成；1=已完成;
							map4.put("TaskType", data.getString("TaskType"));// 任务类型
																				// 1:模考;
																				// 2:练习;
																				// 3:资料;
							map4.put("CreateTime", data.getString("CreateTime"));// 创建时间;
							map4.put("ST_ID", data.getString("ST_ID"));// 任务主键
							map4.put("RefID", data.getString("RefID"));// 引用关联的ID[资料ID或模考、练习ID];
							map4.put("StorePoint", data.getString("StorePoint"));// 存储点[只针对任务类型为3，如果任务类型为1或2，默认为0，
																					// 1:够快网盘;
																					// 2:CC视频]
							map4.put("Name", data.getString("Name"));// name
							map4.put("classid", classid);
							typelist4.add(map4);
						}

					}

					taskList.addAll(typelist1);
					taskList.addAll(typelist2);
					taskList.addAll(typelist3);
					taskList.addAll(typelist4);
					
					// 数据为空则提示，listview 消失
					if (taskList.size() != 0) {
						taskListView.setVisibility(View.VISIBLE);
						relative_warn_nulldata_clastudy
								.setVisibility(View.GONE);
						setAdapter(taskList);
					} else {
						taskListView.setVisibility(View.GONE);
						relative_warn_nulldata_clastudy
								.setVisibility(View.VISIBLE);
					}
					
					
					System.out.println("taskList" + taskList);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 适配adapter
	 */
	private void setAdapter(final List<HashMap<String, Object>> list) {
		taskAdapter = new Stu_TaskAdapter(taskList, mContext);
		taskListView.setAdapter(taskAdapter);
		taskListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//增加浏览次数
				addHttps(list.get(position).get("RefID").toString());
				
				String tp = list.get(position).get("TaskType").toString();
				if (tp.equals("1") || tp.equals("2")||tp.equals("4")) {
					Intent intent = new Intent();
					intent.putExtra("ST_ID", list.get(position).get("ST_ID")
							.toString());
					intent.putExtra("RefID", list.get(position).get("RefID")
							.toString());
					intent.setClass(mContext, Renwu_contentActivity.class);
					startActivity(intent);
				} else {

					Intent intent = new Intent();
					intent.putExtra("RefID", list.get(position).get("RefID")
							.toString());
					if (list.get(position).get("StorePoint").toString()
							.equals("2")) {
						intent.setClass(mContext, Renwu_zlcontentActivity.class);
					} else if (list.get(position).get("StorePoint").toString()
							.toString().equals("1")) {
						intent.setClass(mContext,
								Renwu_doccontentActivity.class);
					}
					startActivity(intent);
				}
			}

		});
	}
	private void addHttps(String mateId) {
		// TODO Auto-generated method stub
		String url = Constants.URL_Material_lookUpMaterials;
		RequestParams params = new RequestParams();

		params.addHeader("Authentication", share.getString("Token", ""));// 添加保密的东西

		// params.addBodyParameter("pageIndex", pageIndex);
		params.addBodyParameter("mId", mateId);
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				try {
					JSONObject obj = new JSONObject(result);
					String Result = obj.getString("Result");
					System.out.println(result);
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}
	/**
	 * 初始化组件
	 */
	private void initView() {
		mContext = this;
		taskBackIcon = (ImageButton) findViewById(R.id.stutask_back_img);
		taskListView = (ListView) findViewById(R.id.stu_task_listview);
		relative_warn_nulldata_clastudy = (RelativeLayout) findViewById(R.id.relative_warn_nulldata_clastudy);
		LodDialogClass.showCustomCircleProgressDialog(mContext, null, getString(R.string.common_Loading));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.stutask_back_img:
			finish();
			break;
		}

	}
}
