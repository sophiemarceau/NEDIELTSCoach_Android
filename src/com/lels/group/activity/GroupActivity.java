package com.lels.group.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.group.adapter.GroupAdapter;
import com.lels.manual.activity.ManualActivity;
import com.lels.random.activity.RandomActivity;
import com.lelts.activity.classroomconnection.adapter.ClassroomConnectionAdapter;
import com.lelts.tool.CalculateListviewGrideview;
import com.lelts.tool.GridViewForGridview;
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
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 分组
 * 
 * @author Administrator
 *
 */
public class GroupActivity extends Activity implements OnClickListener {
	private Context context;
	private EditText group_editselect;
	private EditText group_edit;
	private GridViewForGridview group_grid;
	private Button group_btn;
	private ImageButton groupstu_back_image;
	private ClassroomConnectionAdapter adapter;
	private String url = Constants.URL_TeacherOrStudentGetStudentOnLine;
	private TextView tv_sj;
	private TextView tv_sd;
	private TextView tv_stuNum;
	private PopupWindow popupWindow;
	private String passcode;
	private SharedPreferences share;
	private List<HashMap<String, Object>> list, onlinelist;
	private Timer timer;
	private Editor editor;
	private boolean flag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);
		getIntentValue();
		initview();
		getList();
		timer();
		ExitApplication.getInstance().addActivity(this);
	}

	private void getIntentValue() {
		// TODO Auto-generated method stub
		context = this;
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		passcode = share.getString("passcode", "");

	}

	/**
	 * 网络请求获取学员列表
	 */
	private void getList() {
		// 测量gridview高度
		// CalculateListviewGrideview.setGridViewHeightBased(group_grid);
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("passCode", passcode);
		params.addBodyParameter("roleId", "2");
		HttpUtils util = new HttpUtils();
		util.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LodDialogClass.closeCustomCircleProgressDialog();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				System.out.println("result-----" + result);
				// 本地解析json
				/*
				 * try { InputStreamReader inputReader = new InputStreamReader(
				 * getResources().getAssets().open("onlinestu")); BufferedReader
				 * bufReader = new BufferedReader(inputReader); String line =
				 * ""; result = ""; while ((line = bufReader.readLine()) !=
				 * null) result += line; System.out.println("json===>" +
				 * result); } catch (IOException e) {
				 * System.out.println("json error===>" + e.getMessage());
				 * e.printStackTrace(); }
				 */

				try {
					JSONObject obj = new JSONObject(result);
					JSONObject Data = obj.getJSONObject("Data");

					String tudentOnLineNum = Data.getString("studentOnLineNum");// 在线人数
					String classStudentTotalNum = Data
							.getString("classStudentTotalNum");// 总人数
					tv_stuNum.setText("(" + tudentOnLineNum + "/"
							+ classStudentTotalNum + ")");
					list = new ArrayList<HashMap<String, Object>>();
					onlinelist = new ArrayList<HashMap<String, Object>>();
					JSONArray studentList = Data.getJSONArray("studentList");

					/*
					 * studentList = 在线的学生列表信息[{    uid = 学生UID,    iconUrl =
					 * 学生图像,    studentloginno = 学生登录序号,    studentname = 学生姓名,
					 *    studentloginstatus = 学生在线状态(0离线1在线),
					 *    studentlogintime = 学生第一次登录时间,    studentlogofftime =
					 * 学生登出时间,    studentloginlasttime = 学生最近一次在线的时间,    acId =
					 * 学生所在的课堂主表ActiveClass的主键ID,    id = 课堂与学生的关系表的主键ID   }]
					 */
					for (int i = 0; i < studentList.length(); i++) {
						JSONObject data = studentList.getJSONObject(i);
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("uid", data.getString("uid"));
						map.put("iconUrl", data.getString("iconUrl"));
						map.put("studentname", data.getString("studentname"));
						map.put("studentloginstatus",
								data.getString("studentloginstatus"));
						if (data.getString("studentloginstatus").equals("1")) {
							onlinelist.add(map);
						}
						list.add(map);

					}
					if (flag == true) {
						adapter = new ClassroomConnectionAdapter(list, context);
						group_grid.setAdapter(adapter);
						flag = false;
					} else {
						adapter.setdatachanges(list);
						adapter.notifyDataSetChanged();
					}
					LodDialogClass.closeCustomCircleProgressDialog();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	private void initview() {

		group_editselect = (EditText) findViewById(R.id.group_editselect);
		group_editselect.setOnClickListener(this);

		group_btn = (Button) findViewById(R.id.group_btn);
		group_btn.setOnClickListener(this);

		tv_stuNum = (TextView) findViewById(R.id.tv_stuNum);
		group_edit = (EditText) findViewById(R.id.group_edit);
		group_grid = (GridViewForGridview) findViewById(R.id.group_grid);

		groupstu_back_image = (ImageButton) findViewById(R.id.groupstu_back_image);
		groupstu_back_image.setOnClickListener(this);
		LodDialogClass.showCustomCircleProgressDialog(context, null,getString(R.string.common_Loading));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.groupstu_back_image:
			finish();
			break;
		case R.id.group_editselect:
			showPop(group_editselect);
			break;
		case R.id.group_btn:
			String edit_str = group_edit.getText().toString();
			String editselect_str = group_editselect.getText().toString();

			if (edit_str.length() > 0 && !edit_str.equals("")) {
				int Num = Integer.parseInt(edit_str);
				if (Num < 2 || Num > 12) {
					Toast.makeText(context, "请输入2~12之间的数字", Toast.LENGTH_SHORT)
							.show();
				} else {
					int stuNum = onlinelist.size() / Num;

					if (editselect_str.equalsIgnoreCase("随机分组")) {

						if (onlinelist.size() >= 4 && stuNum >= 2) {
							Intent intent = new Intent();
							intent.putExtra("edit_str", edit_str);
							intent.putExtra("passcode", passcode);
							intent.putExtra("editselect_str", editselect_str);
							intent.setClass(context, RandomActivity.class);
							startActivity(intent);
						} else {
							Toast.makeText(context, "分组条件异常，请从新设定分组数及分组规则",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						if (onlinelist.size() >= 4 && stuNum >= 2) {
							Intent intent = new Intent();
							intent.putExtra("edit_str", edit_str);
							intent.putExtra("passcode", passcode);
							intent.putExtra("editselect_str", editselect_str);
							intent.putExtra("tag", "1");
							intent.setClass(context, ManualActivity.class);
							startActivity(intent);
						} else {
							Toast.makeText(context, "分组条件异常，请从新设定分组数及分组规则",
									Toast.LENGTH_SHORT).show();
						}
					}
				}
			} else {

				Toast.makeText(context, "请输入2~12之间的数字", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		case R.id.tv_sj:
			group_editselect.setText(tv_sj.getText().toString());
			popupWindow.dismiss();
			break;
		case R.id.tv_sd:
			group_editselect.setText(tv_sd.getText().toString());
			popupWindow.dismiss();
			break;
		}
	}

	private void showPop(View v) {
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.pop_group, null);
		tv_sj = (TextView) contentView.findViewById(R.id.tv_sj);
		tv_sj.setOnClickListener(this);
		tv_sd = (TextView) contentView.findViewById(R.id.tv_sd);
		tv_sd.setOnClickListener(this);
		// 一个自定义的布局，作为显示的内容
		int width = group_edit.getWidth();
		popupWindow = new PopupWindow(contentView, width,
				LayoutParams.WRAP_CONTENT, true);

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
		/*
		 * popupWindow.setBackgroundDrawable(getResources().getDrawable(
		 * R.drawable.background_login_edittext));
		 */
		// 设置好参数之后再show
		popupWindow.showAsDropDown(v);
	}

	/**
	 * 方法说明：每隔三秒 刷新数据
	 *
	 */
	private void timer() {
		timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getList();
			}
		};
		timer.schedule(task, 3000, 3000);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		timer.cancel();
		super.onPause();
	}

	/**
	 * 销毁定时器的方式
	 */
	@Override
	protected void onStop() {
		timer.cancel();// 程序退出时cancel timer
		super.onStop();
	}

}
