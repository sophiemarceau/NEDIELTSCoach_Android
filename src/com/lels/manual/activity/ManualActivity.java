package com.lels.manual.activity;

/**
 * 手动分组
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hello.R;
import com.lels.bean.ExitApplication;
import com.lels.bean.GroupMapClass;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.group.activity.GroupActivity;
import com.lels.group.tool.GroupList;
import com.lels.group.tool.GroupStu;
import com.lels.manual.adapter.MymanualAdapter;
import com.lelts.fragment.classroom.ClassRoomStudent;
import com.lelts.tool.IntentUtlis;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ManualActivity extends Activity implements OnClickListener {

	private Context mContext;
	private ImageButton group_back_image;
	private EditText group_manual_edit, group_manual_editselect;
	private String edit_getEditSelect;
	private ListView mListView;
	private List<Integer> oneList;
	// private ManualAdapter mAdapter;
	private SharedPreferences share;
	private String passcode, activeClassId, groupCnt;
	private Button group_manual_btn;
	private int tag = 1;
	// private String manual;
	private MymanualAdapter adapter;
	private GroupMapClass serializableMap;
	private List<GroupList> bundleList = new ArrayList<GroupList>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_random);

		getIntentValue();
	}

	private void getIntentValue() {
		// TODO Auto-generated method stub
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		passcode = share.getString("passcode", "");
		activeClassId = share.getString("activeClassId", "");
		intView();
	}

	/**
	 * 初始化数据
	 */
	private void intView() {
		// TODO Auto-generated method stub
		mContext = ManualActivity.this;
		group_manual_edit = (EditText) findViewById(R.id.group_random_edit);

		group_manual_editselect = (EditText) findViewById(R.id.group_random_editselect);

		group_manual_btn = (Button) findViewById(R.id.group_random_btn);
		group_manual_btn.setOnClickListener(this);
		
		group_back_image = (ImageButton) findViewById(R.id.group_back_image);
		group_back_image.setOnClickListener(this);

		mListView = (ListView) findViewById(R.id.group_random_listview);
		getSenser();
	}

	private void getSenser() {
		Intent intent = getIntent();
		groupCnt = intent.getStringExtra("edit_str");
		edit_getEditSelect = intent.getStringExtra("editselect_str");
		group_manual_edit.setText(groupCnt);
		group_manual_editselect.setText(edit_getEditSelect);
		oneList = new ArrayList<Integer>();
		for (int i = 0; i < Integer.valueOf(groupCnt); i++) {
			oneList.add(R.drawable.icon_addpic_focused);
		} 
		adapter = new MymanualAdapter(mContext, oneList);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mContext, ManualSelectActivity.class);
				intent.putExtra("index", (position + 1) + "");
				intent.putExtra("num", groupCnt);
				startActivityForResult(intent, 100);
				adapter.setId(position);
			}
		});
	}

	/**
	 * startActivity回调
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1001) {
			System.out.println("1001--");
			Bundle bundle = data.getExtras();
			
			serializableMap = (GroupMapClass) bundle
					.getSerializable("map");
			bundleList = serializableMap.getList();
			System.out.println("1001--" + serializableMap.getMap());
			adapter.setGroups(serializableMap.getMap());
			adapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.group_random_btn:
			
			if (group_manual_btn.getText().toString().equals("重新分组")) {
				getResetHttp();
			} else {
			
				int p = bundleList.size();
				if (p==Integer.valueOf(group_manual_edit.getText().toString())) {
					
					getSureHttp();
				}else{
					dialog();
				}
			}

			break;
		case R.id.group_back_image:

			if (group_manual_btn.getText().toString().equals("重新分组")) {
				IntentUtlis.sysStartActivity(mContext, ClassRoomStudent.class);
				finish();
			} else {
				dialog();
			}
			break;
		}
	}

	public void dialog() {

		AlertDialog.Builder builder = new Builder(ManualActivity.this);

		builder.setMessage("分组尚未完成，确认退出吗?");
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
	// 放弃分组
	private void getDistoryHttp() {
		// TODO Auto-generated method stub
		String path = Constants.URL_ActiveClass_abandonDiviceGroup;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("activeClassId", activeClassId);
		System.out.println("path--" + path + "--Token--"
				+ share.getString("Token", "") + "--activeClassId--"
				+ activeClassId);
		HttpUtils util = new HttpUtils();
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				System.out.println("放弃result--" + arg0.result);
				try {
					JSONObject obj = new JSONObject(arg0.result);
					String Result = obj.getString("Result");
					if (Result.equalsIgnoreCase("true")) {
						IntentUtlis.sysStartActivity(mContext,
								GroupActivity.class);
						finish();
					} else {
						Toast.makeText(mContext, "未解散成功", 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	// 确定分组接口
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
				try {
					JSONObject obj = new JSONObject(result);
					String Result = obj.getString("Result");
					if (Result.equals("true")) {
						group_manual_btn.setText("重新分组");
					} else {
						Toast.makeText(mContext, "未分组成功", 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(result);
			}
		});

	}
}
