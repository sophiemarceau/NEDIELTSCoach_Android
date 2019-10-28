package com.lels.manual.activity;

/**
 * 手动分组未分组人员界面
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.bean.ButtonControl;
import com.lels.bean.ExitApplication;
import com.lels.bean.GroupMapClass;
import com.lels.constants.Constants;
import com.lels.group.tool.GroupList;
import com.lels.group.tool.GroupStu;
import com.lels.manual.adapter.ManualSelectGridAdapter;
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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ManualSelectActivity extends Activity implements OnClickListener {

	private Context mContext;
	private ManualSelectGridAdapter mAdapter;
	private GridView item_group_gridView;
	private Button commit_ok;
	private SharedPreferences share;
	private String index, passCode, activeClassId, groupCnt;
	private ImageButton group_back_image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manual_select);
		mContext = this;
		getValues();
		initView();
	}

	private void getValues() {
		// TODO Auto-generated method stub
		Intent mIntent = getIntent();
		index = mIntent.getStringExtra("index");
		groupCnt = mIntent.getStringExtra("num");
	}

	HashMap<String, Object> dataMap = new HashMap<String, Object>();

	/**
	 * 点击事件选中人员或取消人员，保存到map中；
	 * 
	 * @param mList
	 */
	private void getData(final List<HashMap<String, Object>> mList) {
		// TODO Auto-generated method stub

		item_group_gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				HashMap<String, Object> objMap = mList.get(position);
				String key = objMap.get("uid").toString().trim();
				if (null != dataMap.get(key)) {
					dataMap.remove(key);
				} else {
					dataMap.put(key, objMap);
				}
				mAdapter.chiceState(position);
				mAdapter.notifyDataSetChanged();
			}
		});
		mAdapter = new ManualSelectGridAdapter(mList, mContext);
		item_group_gridView.setAdapter(mAdapter);
	}

	/**
	 * 初始化数据；
	 */
	private void initView() {
		// TODO Auto-generated method stub
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		passCode = share.getString("passcode", "");
		activeClassId = share.getString("activeClassId", "");
		item_group_gridView = (GridView) findViewById(R.id.item_group_gridView);
		group_back_image = (ImageButton) findViewById(R.id.group_back_image);
		group_back_image.setOnClickListener(this);
		commit_ok = (Button) findViewById(R.id.commit_ok);
		commit_ok.setOnClickListener(this);
		getNoGroupDataFromNet();
	}

	// 未分组学员的网络解析
	public void getNoGroupDataFromNet() {
		String url = Constants.URL_ActiveClass_loadNoGroupStudents;
		RequestParams params = new RequestParams();

		/**
		 * groupNum=[当前需要分组号，不可为空] passCode=[互动课堂暗号，不可为空]
		 * activeClassId=[互动课堂ID，不可为空] flag=[标识，是否重新分组，0不是1是，不可为空]
		 */
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("groupNum", index);
		params.addBodyParameter("passCode", passCode);
		params.addBodyParameter("activeClassId", activeClassId);
		System.out.println("passCode--" + passCode + "--activeClassId--"
				+ activeClassId + "----" + index + "--URL--" + url);
		HttpUtils utils = new HttpUtils();

		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("ManualSelectActivity-----onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				System.out.println("M---" + result);

				// 本地json---测试
				/*
				 * try { InputStreamReader inputReader = new InputStreamReader(
				 * getResources().getAssets().open("selectOnline"));
				 * BufferedReader bufReader = new BufferedReader(inputReader);
				 * String line = ""; result = ""; while ((line =
				 * bufReader.readLine()) != null) result += line;
				 * System.out.println("json===>" + result); } catch (IOException
				 * e) { System.out.println("json error===>" + e.getMessage());
				 * e.printStackTrace(); }
				 */

				try {
					JSONObject obj = new JSONObject(result);
					String Infomation = obj.getString("Infomation");
					String Result = obj.getString("Result");
					JSONObject Data = obj.getJSONObject("Data");
					/**
					 * Data = { studentOnLineNum = 本课堂当前在线人数,
					 * classStudentTotalNum = 本课堂总人数, studentList = 在线的学生列表信息[{
					 * uid = 学生UID, iconUrl = 学生图像, studentloginno = 学生登录序号,
					 * studentname = 学生姓名, studentloginstatus = 学生在线状态(0离线1在线),
					 * studentlogintime = 学生第一次登录时间, studentlogofftime = 学生登出时间,
					 * studentloginlasttime = 学生最近一次在线的时间, acId =
					 * 学生所在的课堂主表ActiveClass的主键ID, id = 课堂与学生的关系表的主键ID }] }
					 */
					String studentOnLineNum = Data
							.getString("studentOnLineNum");
					String classStudentTotalNum = Data
							.getString("classStudentTotalNum");
					JSONArray studentList = Data.getJSONArray("studentList");
					List<HashMap<String, Object>> mListOut = new ArrayList<HashMap<String, Object>>();
					for (int i = 0; i < studentList.length(); i++) {
						JSONObject studentListIn = studentList.getJSONObject(i);
						HashMap<String, Object> inMap = new HashMap<String, Object>();
						inMap.put("uid", studentListIn.getString("uid"));
						inMap.put("iconUrl", studentListIn.getString("iconUrl"));
						inMap.put("studentloginno",
								studentListIn.getString("studentloginno"));
						inMap.put("studentname",
								studentListIn.getString("studentname"));
						inMap.put("studentloginstatus",
								studentListIn.getString("studentloginstatus"));
						inMap.put("studentlogintime",
								studentListIn.getString("studentlogintime"));
						inMap.put("studentlogofftime",
								studentListIn.getString("studentlogofftime"));
						inMap.put("studentloginlasttime",
								studentListIn.getString("studentloginlasttime"));
						inMap.put("acId", studentListIn.getString("acId"));
						inMap.put("id", studentListIn.getString("id"));
						if (studentListIn.getString("studentloginstatus")
								.equals("1")) {
							mListOut.add(inMap);
						}
					}
					// 接下来是适配....
					getData(mListOut);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.commit_ok:
			if (ButtonControl.isFastClick()) {
				return;
			} else {
				int i = 0;
				// 拼接的最终newMap
				HashMap<String, Object> newMap = new HashMap<String, Object>();
				// HashMap<String, Object> datasMap = new HashMap<String,
				// Object>();

				// List<HashMap<String, Object>> mapList = new
				// ArrayList<HashMap<String, Object>>();
				List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
				// 迭代dataMap(dataMap是点击选中的对象)
				Iterator iter = dataMap.keySet().iterator();
				while (iter.hasNext()) {
					i++;
					HashMap<String, Object> addObjMap = new HashMap<String, Object>();
					String key = (String) iter.next(); // key
					HashMap<String, Object> value = (HashMap<String, Object>) dataMap
							.get(key);
					// 本地展示的集合
					/*
					 * datasMap.put("iconUrl", value.get("iconUrl").toString());
					 * datasMap.put("studentname",
					 * value.get("studentname").toString());
					 * dataList.add(datasMap);
					 */
					// 获取参数信息
					addObjMap.put("UID", value.get("uid").toString());
					addObjMap.put("groupOrder", i);
					dataList.add(addObjMap);
				}

				newMap.put("groupNum", index);
				newMap.put("groups", dataList);

				if (dataList.size() > 1) {
					// 转换成json串；
					ObjectMapper mapper = new ObjectMapper();
					String jsonfromMap;
					try {
						jsonfromMap = mapper.writeValueAsString(newMap);
						System.out.println("jsonfromMap--" + jsonfromMap);

						getManualHttp(jsonfromMap);

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(mContext, "每组人数必须大于1人，请重新选择",
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.group_back_image:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 手动分组 iphone、Android 教师App
	 */
	// hsl modify
	private void getManualHttp(String groupInfos) {
		// TODO Auto-generated method stub
		String path = Constants.URL_ActiveClass_handDivideIntoGroups;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));

		params.addBodyParameter("groupInfos", groupInfos);
		params.addBodyParameter("passCode", passCode);
		params.addBodyParameter("activeClassId", activeClassId);
		params.addBodyParameter("groupCnt", groupCnt);

		System.out.println("groupInfos-" + groupInfos + "passCode-" + passCode
				+ "-activeClassId-" + activeClassId + "-groupCnt-" + groupCnt);
		HttpUtils util = new HttpUtils();
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub

				/*
				 * "Data": {   "size": 用于判断是否有分组，0没有,大于0有；只有大于0时，才会有下面信息
				 *   "groupMode": 分组规则,   "groupCnt": 分组数,   "groupInfos": [
				 * 多个分组LIST    [{      "GroupMethod": 分组规则,      "GroupCnt":
				 * 分组数, "GroupNum": 组号     "sName": 学生姓名,      "IconUrl": 学生头像,
				 * "GroupOrder": 组内顺序号    }]   ]  }
				 */
				String result = arg0.result;
				System.out.println("手动分组 iphone、Android 教师App" + result);

				/*
				 * try { InputStreamReader inputReader = new InputStreamReader(
				 * getResources().getAssets().open("json")); BufferedReader
				 * bufReader = new BufferedReader(inputReader); String line =
				 * ""; result = ""; while ((line = bufReader.readLine()) !=
				 * null) result += line; System.out.println("json===>" +
				 * result); } catch (IOException e) {
				 * System.out.println("json error===>" + e.getMessage());
				 * e.printStackTrace(); }
				 */

				try {
					JSONObject obj = new JSONObject(result);
					String Infomation = obj.getString("Infomation");
					String Result = obj.getString("Result");

					if (Result.equals("true")) {

						JSONObject Data = obj.getJSONObject("Data");
						String groupMode = Data.getString("groupMode");
						String groupCnt = Data.getString("groupCnt");
						JSONArray groupInfos = Data.getJSONArray("groupInfos");
						List<GroupList> listviewList = new ArrayList<GroupList>();
						// hsl add
						HashMap<Integer, GroupList> map = new HashMap<Integer, GroupList>();
						// hsl add
						for (int i = 0; i < groupInfos.length(); i++) {

							JSONArray groupArray = groupInfos.getJSONArray(i);
							// 取出某个组

							if (groupArray.length() > 0) {
								List<GroupStu> gridList = new ArrayList<GroupStu>();
								for (int j = 0; j < groupArray.length(); j++) {
									JSONObject NND = groupArray
											.getJSONObject(j);
									String GroupMethod = NND
											.getString("GroupMethod");
									String IconUrl = NND.getString("IconUrl");
									String GroupCnt = NND.getString("GroupCnt");
									String GroupNum = NND.getString("GroupNum");
									String sName = NND.getString("sName");
									gridList.add(new GroupStu(GroupCnt,
											GroupNum, sName, IconUrl));
								}
								// hsl modify
								listviewList.add(new GroupList(gridList));
								map.put(Integer.parseInt(index.trim()),
										new GroupList(gridList));
								// hsl modify
							}
						}
						Intent intent = new Intent();
						// 传递数据
						final GroupMapClass myMap = new GroupMapClass();
						myMap.setMap(map);// 将map数据添加到封装的myMap<span></span>中
						myMap.setList(listviewList);
						System.out.println("Mymap--" + myMap.getMap());
						Bundle bundle = new Bundle();
						bundle.putSerializable("map", myMap);
						intent.putExtras(bundle);
						intent.setClass(mContext, ManualActivity.class);
						setResult(1001, intent);
						finish();
					} else {
						Toast.makeText(mContext, Infomation, 0).show();
					}

					// --
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

}
