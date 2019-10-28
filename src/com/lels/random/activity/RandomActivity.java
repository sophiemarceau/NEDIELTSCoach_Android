package com.lels.random.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.group.activity.GroupActivity;
import com.lels.group.tool.GroupList;
import com.lels.group.tool.GroupStu;
import com.lels.manual.activity.ManualActivity;
import com.lels.random.adapter.RandomAdapter;
import com.lelts.fragment.classroom.ClassRoomStudent;
import com.lelts.tool.CalculateListviewGrideview;
import com.lelts.tool.GridViewForListView;
import com.lelts.tool.IntentUtlis;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class RandomActivity extends Activity implements OnClickListener {
	private Context mContext;
	private EditText group_random_edit, group_random_selet;
	private String edit_getvalue, edit_seletValue;
	private GridViewForListView mListView;
	private RandomAdapter mAdapter;
	private Button group_random_btn;
	private int tag = 1;
	private ImageView group_back_image;
	private SharedPreferences share;
	private String size, activeClassId;
	private String passcode;
	private Editor editor;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_random);
		getIntentValue();
		intView();
		getSenser();
	}

	private void getIntentValue() {
		// TODO Auto-generated method stub
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		editor = share.edit();
		size = share.getString("size", "");
		passcode = share.getString("passcode", "");
		activeClassId = share.getString("activeClassId", "");

	}

	// 判断是否重新分组
	private void getSenser() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		if (size.equals("0")) {// 没有分组
			edit_getvalue = intent.getStringExtra("edit_str");
			edit_seletValue = intent.getStringExtra("editselect_str");
			group_random_selet.setText(edit_seletValue);
			getRandomHttp();
		} else {
			String JSONArray = intent.getStringExtra("JSONArray");
			edit_getvalue = intent.getStringExtra("groupCnt");
			edit_seletValue = intent.getStringExtra("groupMode");
			if (edit_seletValue.equals("0")) {
				group_random_selet.setText("手动分组");
			}else{
				group_random_selet.setText("随机分组");
			}
			group_random_btn.setText("重新分组");
			getFlaglistHttp(JSONArray);
			//死数据--测试用
//			getlistSj();
		}
		group_random_edit.setText(edit_getvalue);
		

	}
private void getlistSj() {
	// TODO Auto-generated method stub
	List<GroupList> listviewList = new ArrayList<GroupList>();
	for (int i = 0; i < 2; i++) {
		List<GroupStu> gridList = new ArrayList<GroupStu>();
		for (int j = 0; j < 20; j++) {
			gridList.add(new GroupStu("1","1","1","12","","1"));
		}
		listviewList.add(new GroupList(gridList));
	}
	getList(listviewList);
}
	// 未分组赋值
	private void getRandomHttp() {
		// TODO Auto-generated method stub
		String path = Constants.URL_ActiveClass_autoDivideIntoGroups;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("passCode", passcode);
		params.addBodyParameter("activeClassId", activeClassId);
		params.addBodyParameter("groupCnt", edit_getvalue);

		System.out.println("passCode-000" + passcode + "-activeClassId-"
				+ activeClassId + "-groupCnt-" + edit_getvalue);
		HttpUtils util = new HttpUtils();
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				LodDialogClass.closeCustomCircleProgressDialog();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				System.out.println("未分组接口--" + result);
				/*
				 * "Data": {   "groupInfos": [[{
				 *     "ActiveClassGroupID": 分组ID,     "StudentID": 学生UID,
				 *     "GroupNum": 组号     "sName": 学生姓名,     "IconUrl": 学生头像,
				 *     "GroupOrder": 组内顺序号    }]]  }
				 */

				JSONObject obj;
				try {
					obj = new JSONObject(result);
					JSONObject data = obj.getJSONObject("Data");
					JSONArray groupInfos = data.getJSONArray("groupInfos");
					List<GroupList> listviewList = new ArrayList<GroupList>();
					for (int i = 0; i < groupInfos.length(); i++) {
						List<GroupStu> gridList = new ArrayList<GroupStu>();
						JSONArray TMD = groupInfos.getJSONArray(i);
						for (int j = 0; j < TMD.length(); j++) {
							JSONObject NND = TMD.getJSONObject(j);
							String ActiveClassGroupID = NND.getString("ActiveClassGroupID");
							String IconUrl = NND.getString("IconUrl");
							String StudentID = NND.getString("StudentID");
							String GroupNum = NND.getString("GroupNum");
							String sName = NND.getString("sName");
							String GroupOrder = NND.getString("GroupOrder");
							gridList.add(new GroupStu(ActiveClassGroupID,StudentID,GroupNum,sName,IconUrl,GroupOrder));
						
						}
						listviewList.add(new GroupList(gridList));
					}
					getList(listviewList);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	// 有分组的赋值
	private void getFlaglistHttp(String jSONArray) {
		// TODO Auto-generated method stub
		try {
			JSONArray groupInfos = new JSONArray(jSONArray);
			List<GroupList> listviewList = new ArrayList<GroupList>();
			for (int i = 0; i < groupInfos.length(); i++) {
				
				List<GroupStu> gridList = new ArrayList<GroupStu>();
				JSONArray TMD = groupInfos.getJSONArray(i);
				for (int j = 0; j < TMD.length(); j++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					JSONObject NND = TMD.getJSONObject(j);
					String GroupMethod = NND.getString("GroupMethod");
					String IconUrl = NND.getString("IconUrl");
					String GroupCnt = NND.getString("GroupCnt");
					String GroupNum = NND.getString("GroupNum");
					String sName = NND.getString("sName");
//					String GroupOrder = NND.getString("GroupOrder");
					gridList.add(new GroupStu(GroupCnt, GroupMethod, GroupNum, sName, IconUrl));
				}
				listviewList.add(new GroupList(gridList));
			}
			System.out.println("到了这一步--"+listviewList);
			getList(listviewList);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void intView() {
		// TODO Auto-generated method stub
		mContext = this;

		group_random_edit = (EditText) findViewById(R.id.group_random_edit);
		group_random_selet = (EditText) findViewById(R.id.group_random_editselect);

		group_random_btn = (Button) findViewById(R.id.group_random_btn);
		group_random_btn.setOnClickListener(this);

		group_back_image = (ImageView) findViewById(R.id.group_back_image);
		group_back_image.setOnClickListener(this);
		mListView = (GridViewForListView) findViewById(R.id.group_random_listview);
		LodDialogClass.showCustomCircleProgressDialog(mContext, null, getString(R.string.common_Loading));
	}

	// 生成listview条目,并把gridlist传递进去
	private void getList(List<GroupList> listviewlist) {
		mAdapter = new RandomAdapter(mContext, listviewlist);
		mListView.setAdapter(mAdapter);
		LodDialogClass.closeCustomCircleProgressDialog();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.group_random_btn:
			if (group_random_btn.getText().toString().equals("重新分组")) {
				getResetHttp();
			}else{
				getSureHttp();
			}
			
			break;
		case R.id.group_back_image:
			if (group_random_btn.getText().toString().equals("重新分组")) {
				IntentUtlis.sysStartActivity(mContext, ClassRoomStudent.class);
				finish();
			} else {
				dialog();
			}
			break;
		default:
			break;
		}
	}
	//重新分组
	private void getResetHttp() {
		// TODO Auto-generated method stub
		String path = Constants.URL_ActiveClass_resetGroup;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("activeClassId", activeClassId);
		HttpUtils util = new HttpUtils();
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				System.out.println("重新result--"+arg0.result);
				try {
					JSONObject obj = new JSONObject(arg0.result);
					String Infomation  = obj.getString("Infomation");
					String Result = obj.getString("Result");
					if (Result.equalsIgnoreCase("true")) {
						IntentUtlis.sysStartActivity(mContext, GroupActivity.class);
						editor.putString("size", "0");
						editor.commit();
						finish();
					}else{
						Toast.makeText(mContext, Infomation, 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	

	public void dialog() {

		AlertDialog.Builder builder = new Builder(RandomActivity.this);

		builder.setMessage("分组尚未完成，确认退出吗?");
//
//		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				getDistoryHttp();
			}

		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();

			}

		});

		builder.create().show();

	}

	// 放弃分组
	private void getDistoryHttp() {
		// TODO Auto-generated method stub
		String path = Constants.URL_ActiveClass_abandonDiviceGroup;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("activeClassId", activeClassId);
		System.out.println("path--"+path+"--Token--"+share.getString("Token", "")+"--activeClassId--"+activeClassId);
		HttpUtils util = new HttpUtils();
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				System.out.println("放弃result--"+arg0.result);
				try {
					JSONObject obj = new JSONObject(arg0.result);
					String Result = obj.getString("Result");
					if (Result.equalsIgnoreCase("true")) {
						IntentUtlis.sysStartActivity(mContext, GroupActivity.class);
						editor.putString("size", "0");
						editor.commit();
						finish();
					}else{
						Toast.makeText(mContext, "未解散成功", 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	// 完成分组，学生端需要轮询查询
	private void getSureHttp() {
		// TODO Auto-generated method stub
		String url = Constants.URL_ActiveClass_confirmDiviceGroup;
		RequestParams params = new RequestParams();
		/**
		 * [Args]：  passCode=[互动课堂暗号，不可为空]  activeClassId=[互动课堂ID，不可为空]
		 */
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("passCode", passcode);
		params.addBodyParameter("activeClassId", activeClassId);
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
				System.out.println(result);
				JSONObject obj;
				try {
					obj = new JSONObject(arg0.result);
					String Result = obj.getString("Result");
					if (Result.equals("true")) {
						group_random_btn.setText("重新分组");
					}else{
						Toast.makeText(mContext, "未分组成功", 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});

	}
}
