package com.lelts.fragment.classroom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.group.activity.GroupActivity;
import com.lels.vote.activity.VoteActivity;
//import com.lels.group.activity.GroupActivity;
import com.lelts.activity.classroomconnection.adapter.TeacherClassAdapter;
import com.lelts.activity.main.MainActivity;
import com.lelts.tool.IntentUtlis;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ClassRoomFm extends Fragment implements OnClickListener {
	private RadioButton btn_answer, btn_group, btn_vote, btn_responder,
			btn_more;
	private View mview;
	private Button btn_connection, btn_sure, btn_choose_sure;
	private ImageButton btn_close, btn_choose_close;
	private SharedPreferences share, stushare;
	private AlertDialog alertDialog;
	private TextView txt_code;
	private String passcode, ccId, ccId2;
	private int lessonType;
	private ListView mlistview;
	private TeacherClassAdapter madapter;
	private List<HashMap<String, Object>> list;
	private Context context;
	private int position_selected;
	private Intent intent;
	private Timer timer;
	private String teacherCode, nLessonNo;
	private Editor editor;
	// 判断是否是加载课堂页面
	private int choosetag;
	private TextView txt_tishi2,txt_tishi4;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("onCreate===============");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mview = inflater.inflate(R.layout.fragment_classroom_main, null);
		System.out.println("onCreateView================");
		return mview;
	}

	public void setText(String text) {
		txt_code.setText(text);
	}

	public void setString(String passCode, String teacherCode, String ccId,
			String nLessonNo) {
		this.passcode = passCode;
		this.teacherCode = teacherCode;
		this.ccId = ccId;
		this.nLessonNo = nLessonNo;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initview();
		// getdata();
		// System.out.println("onstart==========");

	}

	// /* (non-Javadoc)
	// * @see android.support.v4.app.Fragment#onResume()
	// */
	// @Override
	// public void onResume() {
	// // TODO Auto-generated method stub
	// // initview();
	// // System.out.println("onresum=============");
	// // stushare = getActivity().getSharedPreferences("stushare",
	// Context.MODE_PRIVATE);
	// // choosetag = stushare.getInt("choosetag", -1);
	// // if (choosetag == 2) {
	// // getdata();
	// //
	// // }else{
	// //
	// // return;
	// // }
	//
	// super.onResume();
	// System.out.println("onresume =====Constants.tag==="+Constants.tag);
	// if(Constants.tag){
	// // Constants.tag = false;
	// getdata();
	// }else{
	//
	// }
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume() //
	 */
	// @Override
	// public void onResume() {
	// // TODO Auto-generated method stub
	// Bundle data = getArguments();//获得从activity中传递过来的值
	// if (data == null) {
	//
	// }else{
	// System.out.println("activity中传递过来的值==="+data.getString("passcode"));
	// txt_code.setText(data.getString("passcode"));
	// }
	//
	// super.onResume();
	// }
	//

	/**
	 * 方法说明：初始化控件
	 *
	 */
	private void initview() {
		// TODO Auto-generated method stub
		context = getActivity();
		btn_answer = (RadioButton) getActivity().findViewById(
				R.id.fragment_classroom_btn_answer);
		btn_group = (RadioButton) mview
				.findViewById(R.id.fragment_classroom_btn_group);
		btn_vote = (RadioButton) mview
				.findViewById(R.id.fragment_classroom_btn_vote);
		btn_responder = (RadioButton) mview
				.findViewById(R.id.fragment_classroom_btn_responder);
		btn_more = (RadioButton) mview
				.findViewById(R.id.fragment_classroom_btn_more);
		btn_connection = (Button) mview
				.findViewById(R.id.fragment_classroom_btn_connection);
		btn_answer.setOnClickListener(this);
		btn_group.setOnClickListener(this);
		btn_vote.setOnClickListener(this);
		btn_responder.setOnClickListener(this);
		btn_more.setOnClickListener(this);
		btn_connection.setOnClickListener(this);

		txt_code = (TextView) getActivity().findViewById(
				R.id.fragment_classroom_txt_code);
		share = getActivity().getSharedPreferences("userinfo",
				Context.MODE_PRIVATE);
		editor = share.edit();
	}
	
	/**
	 * 获取课堂ID iphone、Android 教师/学生App
	 */
	private void getSureGroup() {
		// TODO Auto-generated method stub
		String iDurl = Constants.URL_ActiveClass_getIdByPassCode;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("passCode", passcode);
		HttpUtils util = new HttpUtils();
		util.send(HttpMethod.POST, iDurl, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						System.out.println(result);
						try {
							JSONObject obj = new JSONObject(result);
							JSONObject data = obj.getJSONObject("Data");
							String activeClassId = data
									.getString("activeClassId");
							editor.putString("activeClassId", activeClassId);
							editor.commit();
							if (!activeClassId.equals("null")
									|| !activeClassId.equals("")) {
								System.out.println("lllll");
							
							} else {
								Toast.makeText(context,
										"activeClassId为空", 0).show();
							}

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
	 * @see android.view.View.OnClickListener#onClick(android.view.View) 实现按钮的监听
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//答题
		case R.id.fragment_classroom_btn_answer:
			// ShowDiglog();
			ShowDiglog();
			break;
		case R.id.fragment_classroom_btn_group:
			// IntentUtlis.sysStartActivity(getActivity(), GroupActivity.class);
			ShowDiglog();
			txt_tishi2.setText("选择分组练形式前，请先");
			txt_tishi4.setText("再选择分组练形式。");
			break;
			//投票功能
		case R.id.fragment_classroom_btn_vote:
//			ShowDiglogDevelop();
			ShowDiglog();
			txt_tishi2.setText("选择投票前，请先");
			txt_tishi4.setText("再选择投票。");
			break;

		case R.id.fragment_classroom_btn_responder:
//			ShowDiglogDevelop();
			ShowDiglog();
			txt_tishi2.setText("选择抢答器前，请先");
			txt_tishi4.setText("再选择抢答器。");
			break;

		case R.id.fragment_classroom_btn_more:
//			ShowDiglogDevelop();
			ShowDiglog();
			txt_tishi2.setText("选择更多功能前，请先");
			txt_tishi4.setText("再选择更多功能。");
			break;
		// 跳转到 同步课堂
		case R.id.fragment_classroom_btn_connection:
			passcode = txt_code.getText().toString();
			if (txt_code.getText().equals("")) {
				Toast.makeText(context, "请获取课堂暗号！", Toast.LENGTH_SHORT).show();
			} else {
				TeacherSyncActiveClass();
				
				getSureGroup();
				intent = new Intent(getActivity(),
						ClassRoomConnectionRoomActivity.class);
				intent.putExtra("passcode", txt_code.getText());
				editor.putString("passcode", txt_code.getText().toString());
				editor.commit();
				intent.putExtra("ccId", ccId);
				System.out.println("ccid======================" + ccId);
				startActivity(intent);
			}

			// if (lessonType == 1 || lessonType == 2) {
			//
			// if (lessonType == 1) {
			// ccId = ccId2;
			//
			// } else {
			//
			// ccId = list.get(position_selected).get("id").toString();
			// }
			//
			//
			// if( txt_code.getText().equals("")){
			// Toast.makeText(context, "请获取暗号，请稍候...", Toast.LENGTH_SHORT)
			// .show();
			// }el
			// }
			//
			// } else {
			// Toast.makeText(context, "当前没有课，也没有最近的四节课！", Toast.LENGTH_SHORT)
			// .show();
			// }

			break;
		case R.id.waing_btn_sure:
			alertDialog.dismiss();
			break;
		case R.id.waing_btn_close:
			alertDialog.dismiss();

			break;
		// 选择关闭
		case R.id.waing_choose_classes_btn_close:
			alertDialog.dismiss();
			// timer();
			break;
		// 确定选课
		case R.id.waing_choose_classes_btn_sure:
			alertDialog.dismiss();
			// timer.cancel();
			// getselectclasses();
			break;
		case R.id.waing_develop_btn_close:
			alertDialog.dismiss();
		default:
			break;
		}
	}



	// /**
	// * 方法说明：弹出同步课堂Dialog
	// *
	// */
	 private void ShowDiglog() {
	 // TODO Auto-generated method stub
	 LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
	 View myLoginView = layoutInflater.inflate(R.layout.waring_classroom,
	 null);
	 alertDialog = new AlertDialog.Builder(getActivity()).create();
	 alertDialog.setView(myLoginView, 0, 0, 0, 30);
	 alertDialog.show();
	 alertDialog.getWindow().setGravity(Gravity.CENTER);
	 alertDialog.setCanceledOnTouchOutside(true);
	 btn_sure = (Button) myLoginView.findViewById(R.id.waing_btn_sure);
	 btn_close = (ImageButton) myLoginView
	 .findViewById(R.id.waing_btn_close);
		txt_tishi2 = (TextView) myLoginView.findViewById(R.id.txt_tishi2);
		txt_tishi4 = (TextView) myLoginView.findViewById(R.id.txt_tishi4);
	 btn_sure.setOnClickListener(this);
	 btn_close.setOnClickListener(this);
	 }
	/**
	 * 方法说明：弹出 开发中 Dialog
	 *
	 */
	private void ShowDiglogDevelop() {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
		View myLoginView = layoutInflater.inflate(
				R.layout.waring_classroom_development, null);
		alertDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog.setView(myLoginView, 0, 0, 0, 30);
		alertDialog.show();
		alertDialog.getWindow().setGravity(Gravity.CENTER);
		alertDialog.setCanceledOnTouchOutside(true);
		ImageButton btn_close = (ImageButton) myLoginView
				.findViewById(R.id.waing_develop_btn_close);
	
		btn_close.setOnClickListener(this);
	}

	/**
	 * 方法说明：弹出选课Dialog
	 *
	 */
	private void ShowChooseClassDiglog() {
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
		View myLoginView = layoutInflater.inflate(
				R.layout.waring_choose_classse, null);
		alertDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog.setView(myLoginView, 0, 0, 0, 30);
		alertDialog.show();
		alertDialog.getWindow().setGravity(Gravity.CENTER);
		alertDialog.setCanceledOnTouchOutside(true);
		btn_choose_sure = (Button) myLoginView
				.findViewById(R.id.waing_choose_classes_btn_sure);
		btn_choose_close = (ImageButton) myLoginView
				.findViewById(R.id.waing_choose_classes_btn_close);
		mlistview = (ListView) myLoginView
				.findViewById(R.id.waring_listview_choose_classes);
		btn_choose_sure.setOnClickListener(this);
		btn_choose_close.setOnClickListener(this);
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
		System.out.println("Authentication=========="
				+ share.getString("Token", ""));
		System.out.println("teacherCode============="
				+ share.getString("teacherCode", ""));
		utils.send(HttpMethod.GET, Constants.URL_GetTeacherLessonAndPassCode
				+ "?teacherCode=" + share.getString("teacherCode", ""), params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						System.out.println("result====================="
								+ result);
						try {
							JSONObject obj = new JSONObject(result);
							JSONObject obj1 = obj.getJSONObject("Data");

							passcode = obj1.getString("passCode");
							System.out
									.println("passcode===========" + passcode);
							lessonType = obj1.getInt("lessonType");
							System.out.println("lessonType========"
									+ lessonType);
							if (lessonType == 1) {
								JSONObject obj2 = obj1.getJSONObject("lesson");
								ccId2 = obj2.getString("ID");
								nLessonNo = obj2.getString("nLessonNo");
							}
							getlessonType();

							JSONArray array = obj1.getJSONArray("listLessons");
							list = new ArrayList<HashMap<String, Object>>();
							for (int i = 0; i < array.length(); i++) {
								JSONObject data = array.getJSONObject(i);
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("id", data.getString("ID"));
								map.put("className",
										data.getString("className"));
								map.put("nLessonNo",
										data.getString("nLessonNo"));
								map.put("sClassCode",
										data.getString("sClassCode"));
								map.put("sTeacherCode",
										data.getString("sTeacherCode"));
								// 新加字段
								map.put("sClassID", data.getString("sClassID"));
								map.put("sRoomID", data.getString("sRoomID"));
								map.put("sTeacherID",
										data.getString("sTeacherID"));
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
	 * 方法说明：点击“同步到课堂” 老师上课  passCode=[课堂暗号]&   teacherCode=[教师Code]&  
	 * ccId=[课次主键ID]&   nLessonNo=[课次号]
	 */
	public void TeacherSyncActiveClass() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		System.out.println("TeacherSyncActiveClass的参数===" + "?passCode="
				+ txt_code.getText() + "&sTeacherId="
				+ share.getString("sTeacherID", "") + "&ccId=" + ccId
				+ "&nLessonNo=" + nLessonNo);
		utils.send(
				HttpMethod.GET,
				Constants.URL_TeacherSyncActiveClass + "?passCode="
						+ txt_code.getText() + "&sTeacherId="
						+ share.getString("sTeacherID", "") + "&ccId=" + ccId
						+ "&nLessonNo=" + nLessonNo, params,

				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						System.out
								.println("TeacherSyncActiveClass_result====================="
										+ result);

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
			Toast.makeText(getActivity(), "当前没有课程", Toast.LENGTH_SHORT).show();
			break;
		// 有课次
		case 1:
			txt_code.setText(passcode);
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
	 * 方法说明：提示先进入课堂
	 *
	 */
//	private void toastinfo() {
//		// TODO Auto-generated method stub
//		Toast.makeText(context, "请先同步课堂！",Toast.LENGTH_SHORT).show();
//	}

	// /**
	// * 方法说明： 网络请求选择一节课次 并生成暗号
	// *
	// */
	// private void getselectclasses() {
	// // TODO Auto-generated method stub
	// position_selected = TeacherClassAdapter.getposition();
	// System.out.println("position_selected=============="
	// + position_selected);
	// System.out.println("list================="
	// + list.get(position_selected).get("nLessonNo"));
	// ccId = list.get(position_selected).get("id").toString();
	// RequestParams params = new RequestParams();
	// params.addHeader("Authentication", share.getString("Token", ""));
	// HttpUtils utils = new HttpUtils();
	// System.out.println("?teacherCode="
	// + list.get(position_selected).get("sTeacherCode") + "&ccId="
	// + list.get(position_selected).get("id") + "&nLessonNo="
	// + list.get(position_selected).get("nLessonNo"));
	// utils.send(
	// HttpMethod.GET,
	// Constants.URL_SaveActiveClassPro + "?teacherCode="
	// + share.getString("teacherCode", "")
	// + "&ccId=" + list.get(position_selected).get("id")
	// + "&nLessonNo="
	// + list.get(position_selected).get("nLessonNo"), params,
	// new RequestCallBack<String>() {
	//
	// @Override
	// public void onFailure(HttpException arg0, String arg1) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onSuccess(ResponseInfo<String> arg0) {
	// // TODO Auto-generated method stub
	// nLessonNo = list.get(position_selected).get("nLessonNo").toString();
	// String result = arg0.result;
	// System.out.println("teacherinfo====================="
	// + result);
	// try {
	// JSONObject obj = new JSONObject(result);
	// String code = obj.getString("Data");
	// System.out.println("code======================="
	// + code);
	// txt_code.setText(code);
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	// });
	//
	// }
	//
}
