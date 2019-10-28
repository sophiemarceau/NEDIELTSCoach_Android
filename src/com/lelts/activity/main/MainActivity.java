package com.lelts.activity.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.Conversation;

import com.example.hello.R;
import com.lels.bean.ExitApplication;
import com.lels.constants.Constants;
import com.lelts.activity.classroomconnection.adapter.TeacherClassAdapter;
import com.lelts.adapter.fragment.adapter.FragmentTabAdapter;
import com.lelts.chatroom.context.MyContext;
import com.lelts.fragment.chat.ChatEFm;
import com.lelts.fragment.classes.ClassFm;
import com.lelts.fragment.classroom.ClassRoomFm;
import com.lelts.fragment.data.DataDFm;
import com.lelts.fragment.schedule.ScheduleFm;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 主要页面
 **/
// implements OnClickListener
public class MainActivity extends FragmentActivity implements OnClickListener {

	Context mContext;
	private AlertDialog alertDialog;
	
	private ScheduleFm a = new ScheduleFm();
	private ClassFm b = new ClassFm();
	private ClassRoomFm c = new ClassRoomFm();
	private DataDFm d = new DataDFm();
	private ChatEFm e = new ChatEFm();
	// fragments.add(new ScheduleFm());
	// fragments.add(new ClassFm());
	// fragments.add(new ClassRoomFm());
	// fragments.add(new DataDFm());
	// fragments.add(new ChatEFm());
	private FragmentTabAdapter tabAdapter;
	// 计划，班级，课堂，资料，聊天
	private RadioButton rad_schedule, rad_class, rad_classroom, rad_data, rad_chat;
	/**
	 * Called when the activity is first created.
	 */
	private RadioGroup rgs;
	public List<Fragment> fragments = new ArrayList<Fragment>();
	public String hello = "hello ";
	private boolean isOut;
	// tagshare 用来保存加载到第几个页面
	private SharedPreferences tagshare;

	private View mview;
	private SharedPreferences share,shareToken, stushare;
	private TeacherClassAdapter madapter;
	private ListView mlistview;
	private String passcode, ccId, ccId2;
	private int lessonType;
	private List<HashMap<String, Object>> list;
	private String teacherCode, nLessonNo;
	private Context context;
	private Button btn_connection, btn_sure, btn_choose_sure;
	private TextView txt_code;
	private ImageButton btn_close, btn_choose_close;
	private int position_selected;
	public boolean tag;

	private int id;

	private int tag2;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initview();
		gettag();
		ExitApplication.getInstance().addActivity(this);
		addFragment();

		fragAdapter();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Intent intent = getIntent();
		if (intent == null) {
			return;
		} else {
			tag2 = intent.getIntExtra("tag", 0);
			System.out.println("tag====" + tag2);
			if (tag2 == 2) {
				ft.show(c);
				ft.hide(a);
				ft.hide(b);
				ft.hide(d);
				ft.hide(e);
				rad_classroom.setChecked(true);
				ft.commit();
			}
		}
		super.onResume();
	}

	/**
	 * 
	 * 方法说明：初始化控件
	 *
	 */
	private void initview() {
		mContext = this;
		context = this;
		rgs = (RadioGroup) findViewById(R.id.main_tab_group);
		// 获取id 计划，班级，课堂，资料，聊天
		rad_chat = (RadioButton) findViewById(R.id.main_tab_chat);
		rad_class = (RadioButton) findViewById(R.id.main_tab_class);
		rad_classroom = (RadioButton) findViewById(R.id.main_tab_classroom);
		rad_data = (RadioButton) findViewById(R.id.main_tab_data);
		rad_schedule = (RadioButton) findViewById(R.id.main_tab_schedule);
		tagshare = getSharedPreferences("stushare", MODE_PRIVATE);
		// 获取监听
		// rad_chat.setOnClickListener(this);
		// rad_class.setOnClickListener(this);
		// rad_classroom.setOnClickListener(this);
		// rad_data.setOnClickListener(this);
		// rad_schedule.setOnClickListener(this);
		// 设置 默认第一个fragment 计划
		// FragmentManager fm=getSupportFragmentManager();
		// FragmentTransaction ft=fm.beginTransaction();
		// ft.add(R.id.tab_content, new ScheduleFm());
		//
		// }
		// ft.commit();

		// btn_connection =
		// (Button)findViewById(R.id.fragment_classroom_btn_connection);
		// btn_connection.setOnClickListener(this);
		mview = LayoutInflater.from(MainActivity.this).inflate(R.layout.fragment_classroom_main, null);
		// 得到sharedPreference中保存的值
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		shareToken = getSharedPreferences("userChatToken", Context.MODE_PRIVATE);
		// txt_code = (TextView)
		// getSupportFragmentManager().findFragmentById(R.id.tab_content).getView().findViewById(R.id.fragment_classroom_txt_code);
		// txt_code = (TextView)
		// c.getActivity().findViewById(R.id.fragment_classroom_txt_code);
		txt_code = (TextView) mview.findViewById(R.id.fragment_classroom_txt_code);
		// tag = Constants.tag;
	}

	private void fragAdapter() {
		tabAdapter = new FragmentTabAdapter(this, fragments, R.id.tab_content, rgs);
		tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
			@Override
			public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
				// System.out.println("Extra---- " + index
				// + " checked!!! ");
				switch (checkedId) {
				case R.id.main_tab_chat:
//					 ShowDiglogDevelop();

//					System.out.println("123456===");
					//MyContext.TOKEN1    是测试的token
					/**
					 * 还需要判断一下是用登录时返回的Token还是自己去调接口来获取Token
					 */
					//shareToken.getString("chatToken", "")
//					RongIM.connect(shareToken.getString("chatToken", ""), new ConnectCallback() {
//						@Override
//						public void onSuccess(String arg0) {
//							System.out.println("login的聊天数据 ？？？654321===" + shareToken.getString("chatToken", ""));
//						}
//
//						@Override
//						public void onError(ErrorCode arg0) {
//						}
//
//						@Override
//						public void onTokenIncorrect() {
//						}
//					});
					

					Constants.tag = false;
					break;
				case R.id.main_tab_classroom:
					// FragmentManager fm=getSupportFragmentManager();
					// FragmentTransaction ft=fm.beginTransaction();
					// ft.add(R.id.tab_content, new ClassRoomFm());
					// ft.commit();
					// Editor ed = tagshare.edit();
					// ed.putInt("choosetag", 2);
					// ed.commit();
					id = checkedId;
					Constants.tag = true;
					System.out.println("点击按钮===Constants.tag====" + Constants.tag);
					getdata();
					break;
				case R.id.main_tab_class:

					Constants.tag = false;
					break;
				case R.id.main_tab_data:

					Constants.tag = false;
					break;
				case R.id.main_tab_schedule:

					Constants.tag = false;
					break;

				default:
					break;
				}
			}
		});
	}

	/**
	 * * 添加framgnet到list<>里面
	 */
	private void addFragment() {

		fragments.add(a);
		fragments.add(b);
		fragments.add(c);
		fragments.add(d);
		fragments.add(e);
	}

	/**
	 * 方法说明：弹出 开发中 Dialog
	 *
	 */
	private void ShowDiglogDevelop() {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
		View myLoginView = layoutInflater.inflate(R.layout.waring_classroom_development, null);
		alertDialog = new AlertDialog.Builder(MainActivity.this).create();
		alertDialog.setView(myLoginView, 0, 0, 0, 30);
		alertDialog.show();
		alertDialog.getWindow().setGravity(Gravity.CENTER);
		alertDialog.setCanceledOnTouchOutside(true);
		ImageButton btn_close = (ImageButton) myLoginView.findViewById(R.id.waing_develop_btn_close);
		btn_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isOut) {
				return super.onKeyDown(keyCode, event);
			} else {
				isOut = true;
				Toast.makeText(mContext, "再按一次返回键退出", Toast.LENGTH_LONG).show();
				//
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						isOut = false;
					}
				};
				timer.schedule(task, 3000); // 调试设置为5s
				return true;

			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 方法说明：获取tag 返回的具体值
	 *
	 */
	private void gettag() {
		// TODO Auto-generated method stub
	}

	/**
	 * 方法说明：网络请求数据教师的课次信息 0无课次 1当前有课次 2当前无课次 有最近四节课
	 *
	 */
	private void getdata() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		System.out.println("Authentication==========" + share.getString("Token", ""));
		System.out.println("sTeacherId=============" + share.getString("sTeacherID", ""));
		utils.send(HttpMethod.GET,
				Constants.URL_GetTeacherLessonAndPassCode + "?sTeacherId=" + share.getString("sTeacherID", ""), params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						System.out.println("result=====================" + result);
						try {
							JSONObject obj = new JSONObject(result);
							JSONObject obj1 = obj.getJSONObject("Data");

							passcode = obj1.getString("passCode");
							System.out.println("passcode===========" + passcode);
							lessonType = obj1.getInt("lessonType");
							System.out.println("lessonType========" + lessonType);
							if (lessonType == 1) {
								JSONObject obj2 = obj1.getJSONObject("lesson");
								ccId2 = obj2.getString("ID");
								nLessonNo = obj2.getString("nLessonNo");
							} else {

							}
							getlessonType();

							JSONArray array = obj1.getJSONArray("listLessons");
							list = new ArrayList<HashMap<String, Object>>();
							for (int i = 0; i < array.length(); i++) {
								JSONObject data = array.getJSONObject(i);
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("id", data.getString("ID"));
								map.put("className", data.getString("className"));
								map.put("nLessonNo", data.getString("nLessonNo"));
								map.put("sClassCode", data.getString("sClassCode"));
								map.put("sTeacherCode", data.getString("sTeacherCode"));
								// 新加字段
								map.put("sClassID", data.getString("sClassID"));
								map.put("sRoomID", data.getString("sRoomID"));
								map.put("sTeacherID", data.getString("sTeacherID"));
								list.add(map);
							}

							madapter = new TeacherClassAdapter(context, list);
							mlistview.setAdapter(madapter);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

	}

	/**
	 * 方法说明：判断当前是否有课次
	 *
	 */
	private void getlessonType() {
		// TODO Auto-generated method stub
		System.out.println("lessonType========" + lessonType);
		switch (lessonType) {
		// 无课次
		case 0:
			Toast.makeText(MainActivity.this, "当前没有课程", Toast.LENGTH_SHORT).show();
			break;
		// 有课次
		case 1:
			ClassRoomFm classRoomFm = (ClassRoomFm) MainActivity.this.getSupportFragmentManager()
					.findFragmentByTag(id + "");
			classRoomFm.setString(passcode, teacherCode, ccId2, nLessonNo);
			classRoomFm.setText(passcode);

			break;
		// 当前无课次，显示最近的课次
		case 2:
			ShowChooseClassDiglog();
			// timer.cancel();
			break;

		default:
			break;
		}
	}

	/**
	 * 方法说明：弹出选课Dialog
	 *
	 */
	private void ShowChooseClassDiglog() {
		LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
		View myLoginView = layoutInflater.inflate(R.layout.waring_choose_classse, null);
		alertDialog = new AlertDialog.Builder(MainActivity.this).create();
		alertDialog.setView(myLoginView, 0, 0, 0, 30);
		alertDialog.show();
		alertDialog.getWindow().setGravity(Gravity.CENTER);
		alertDialog.setCanceledOnTouchOutside(true);
		btn_choose_sure = (Button) myLoginView.findViewById(R.id.waing_choose_classes_btn_sure);
		btn_choose_close = (ImageButton) myLoginView.findViewById(R.id.waing_choose_classes_btn_close);
		mlistview = (ListView) myLoginView.findViewById(R.id.waring_listview_choose_classes);
		btn_choose_sure.setOnClickListener(this);
		btn_choose_close.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 选择关闭
		switch (v.getId()) {
		case R.id.waing_choose_classes_btn_close:
			alertDialog.dismiss();
			//// timer();
			break;
		// 确定选课
		case R.id.waing_choose_classes_btn_sure:
			alertDialog.dismiss();
			// timer.cancel();
			getselectclasses();
			break;
		case R.id.waing_develop_btn_close:
			alertDialog.dismiss();

		default:
			break;
		}

	}

	/**
	 * 方法说明： 网络请求选择一节课次 并生成暗号
	 *
	 */
	private void getselectclasses() {
		// TODO Auto-generated method stub
		position_selected = TeacherClassAdapter.getposition();
		System.out.println("position_selected==============" + position_selected);
		System.out.println("list=================" + list.get(position_selected).get("nLessonNo"));
		ccId = list.get(position_selected).get("id").toString();
		nLessonNo = list.get(position_selected).get("nLessonNo").toString();
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		System.out.println("?teacherCode=" + list.get(position_selected).get("sTeacherCode") + "&ccId="
				+ list.get(position_selected).get("id") + "&nLessonNo=" + list.get(position_selected).get("nLessonNo"));
		teacherCode = list.get(position_selected).get("sTeacherCode").toString();

		utils.send(HttpMethod.GET,
				Constants.URL_SaveActiveClassPro + "?sTeacherId=" + share.getString("sTeacherID", "") + "&ccId="
						+ list.get(position_selected).get("id") + "&nLessonNo="
						+ list.get(position_selected).get("nLessonNo"),
				params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						nLessonNo = list.get(position_selected).get("nLessonNo").toString();
						String result = arg0.result;
						System.out.println("teacherinfo=====================" + result);
						try {
							JSONObject obj = new JSONObject(result);
							String code = obj.getString("Data");
							System.out.println("code=======================" + code);
							// Bundle bundle = new Bundle();
							// bundle.putString("passcode", code);
							// c.setArguments(bundle);
							ClassRoomFm classRoomFm = (ClassRoomFm) MainActivity.this.getSupportFragmentManager()
									.findFragmentByTag(id + "");
							classRoomFm.setText(code);
							classRoomFm.setString(code, teacherCode, ccId, nLessonNo);
							System.out.println("课堂暗号====" + txt_code.getText());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		tag2 = 0;
		System.out.println("destory === tag==" + tag2);
		super.onPause();
	}

}
