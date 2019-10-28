package com.lelts.fragment.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.hello.R;
import com.lels.activity.myself.MyselfActivity;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.costum_widget.CustomProgressDialog;
import com.lelts.fragment.schedule.adapter.StudyPlanAdapter;
import com.lelts.schedule.calendar.CalendarView;
import com.lelts.tool.CalculateListviewGrideview;
//import com.lelts.tool.RefreshDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 课程主页
 * */
public class ScheduleFm extends Fragment implements OnClickListener {
	// private RefreshDialog refreshDialog;
	private static final String TAG = "ScheduleFm";

	private View mview;
	private CustomProgressDialog customDialog;
	// 个人头像
	private ImageView imagview_myself;

	private ListView listview_studyplan;

	/** Called when the activity is first created. */
	/**
	 * gridview 的 adapter
	 * **/
	private CalendarView calV = null;
	private GridView gridView = null;
	private TextView topText = null;

	private static int jumpMonth = 0; // 每次点击按钮，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0; // 点击超过一年，则增加或者减去一年,默认为0(即当前年)

	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = ""; // 当前日期

	// 上一月和下一月的按钮
	ImageButton previous;
	ImageButton next;
	LinearLayout main;

	// SimpleDateFormat format1 = new SimpleDateFormat("yyyy MM");
	// SimpleDateFormat format2 = new SimpleDateFormat("MMM yyyy", Locale.US);

	private String token;

	// 有课程的日期列表 为
	List<String> l_lessondata = new ArrayList<String>();
	// 日期对应课程
	HashMap<String, String> map_dataandlessons = new HashMap<String, String>();
	// 某个日子 里面有多少课程
	private HashMap<String, Object> map_num = new HashMap<String, Object>();
	// 点击日起，下面对应的课程列表
	private List<HashMap<String, Object>> list_plan;

	private boolean isccc = false;
	private StudyPlanAdapter adapter;
	// 提示 没有数据的布局
	private RelativeLayout relative_warn_nulldata_schedule;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	/**
	 * 获取当前日期
	 **/
	@SuppressLint("SimpleDateFormat")
	public void getcurrentdata() {

		Log.d(TAG, "getcurrentdata()");

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		currentDate = sdf.format(date);
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);
		System.out.println("======currentDate====" + currentDate);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.d(TAG, "onCreateView()");
		// showDialog();
		mview = inflater.inflate(R.layout.fragment_schedule_main, null);
		// 加载加载dialog
		LodDialogClass.showCustomCircleProgressDialog(getActivity(), null,
				getString(R.string.common_Loading));

		getdatafromshare();

		getcurrentdata();
		initview();
		getdatafromnet(currentDate);

		return mview;
	}

	private void getdatafromshare() {
		SharedPreferences share = getActivity().getSharedPreferences(
				"userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");
	}

	/**
	 * 加载视图dialog
	 */
	// private void showDialog() {
	// refreshDialog = new RefreshDialog(getActivity());
	// refreshDialog.showdialog();
	// }
	@SuppressWarnings("static-access")
	private void getdatafromnet(String ssss) {

		System.out.println("getdatafromnet(String )" + ssss);

		String url = new Constants().URL_Teacher_MONTH_LESSONS + "?dateParam="
				+ ssss;// 登录的网址
		System.out.println("url=====" + url);
		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		// params.addBodyParameter("dateParam", ssss);// 指定日期
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(0);
		http.configDefaultHttpCacheExpiry(0);
		http.configTimeout(1000 * 10);
		http.configSoTimeout(1000 * 5);
		http.send(HttpRequest.HttpMethod.GET, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();

					}

					@SuppressWarnings({ "unchecked", "unused" })
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// refreshDialog.missdialog();
						System.out.println("ScheduleFm---"
								+ responseInfo.result);

						try {
							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息

							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							map_num = new HashMap<String, Object>();
							JSONArray array = new JSONArray(Data);
							map_dataandlessons = new HashMap<String, String>();
							l_lessondata.clear();
							if (array != null) {

								for (int i = 0; i < array.length(); i++) {
									JSONObject obj = array.optJSONObject(i);

									Iterator<String> it = obj.keys();
									while (it.hasNext()) {

										String s_d = (String) it.next();
										// 获取 有课程的日期列表
										l_lessondata.add(s_d);
										Log.d(TAG, s_d);
									}
									map_dataandlessons.put(l_lessondata.get(i),
											obj.getString(l_lessondata.get(i)));
									System.out.println(map_dataandlessons
											.get(l_lessondata.get(i)));
									Log.d(TAG,
											""
													+ map_dataandlessons
															.get(l_lessondata
																	.get(i)));
								}

								map_num = getdatadotnum(map_dataandlessons);

								gridView.setAdapter(null);

								calV = new CalendarView(getActivity(),
										getResources(), jumpMonth, jumpYear,
										year_c, month_c, day_c, map_num);

								gridView.setAdapter(calV);

								// 设置当天的 课程信息
								listview_studyplan.setAdapter(null);
								list_plan = new ArrayList<HashMap<String, Object>>();

								list_plan = getlistdata(map_dataandlessons,
										currentDate);

								// 点击更新 listview 的 adapter
								if (list_plan.size() > 0) {
									listview_studyplan
											.setVisibility(View.VISIBLE);
									relative_warn_nulldata_schedule
											.setVisibility(View.GONE);
									findNearestClassPlan(System
											.currentTimeMillis());
									adapter = new StudyPlanAdapter(
											getActivity(), list_plan, true);
									listview_studyplan.setAdapter(adapter);

									System.out.println("=====有数据=====");
								} else {
									listview_studyplan.setVisibility(View.GONE);
									relative_warn_nulldata_schedule
											.setVisibility(View.VISIBLE);
									System.out.println("=====无数据=====");
								}

							}
							LodDialogClass.closeCustomCircleProgressDialog();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("onFailure");
						LodDialogClass.closeCustomCircleProgressDialog();
						if (error.getExceptionCode() == 504) {

						}
					}

				});

	}

	private void initview() {

		// 课程的列表 数据
		listview_studyplan = (ListView) mview
				.findViewById(R.id.listview_studyplan);
		// setlistdata();

		imagview_myself = (ImageView) mview.findViewById(R.id.imagview_myself);
		imagview_myself.setOnClickListener(this);

		calV = new CalendarView(getActivity(), getResources(), jumpMonth,
				jumpYear, year_c, month_c, day_c, map_num);
		gridView = (GridView) mview.findViewById(R.id.gridView1);
		relative_warn_nulldata_schedule = (RelativeLayout) mview
				.findViewById(R.id.relative_warn_nulldata_schedule);

		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
		gridView.setBackgroundResource(R.color.white);
		gridView.setPadding(1, 1, 1, 1);
		CalculateListviewGrideview.setGridViewHeightBasedOnChildrenx(gridView);
		// 选中日期之后 listview 要刷新数据

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				String scheduleDay = calV.getDateByClickItem(position).split(
						"\\.")[0]; // 这一天的阳历
				String titleYear = calV.getShowYear();
				String titleMonth = calV.getShowMonth();
				int startPosition = calV.getStartPositon();
				int endPosition = calV.getEndPosition();

				if (position >= startPosition && position <= endPosition) {
					// Toast.makeText(
					// getActivity(),
					// "被点击的日期 : " + titleYear + "年" + titleMonth + "月"
					// + scheduleDay + "日", Toast.LENGTH_LONG)
					// .show();

					// calV.setClickFlag(position);

					String selected_data = titleYear
							+ "-"
							+ String.format("%02d", Integer.valueOf(titleMonth))
							+ "-"
							+ String.format("%02d",
									Integer.valueOf(scheduleDay));

					System.out.println("这一天的日历为====" + selected_data);

					listview_studyplan.setAdapter(null);

					list_plan = new ArrayList<HashMap<String, Object>>();

					list_plan = getlistdata(map_dataandlessons, selected_data);

					// 点击更新 listview 的 adapter

					Log.d(TAG, "当前日期为===" + currentDate + "选择的日期为==="
							+ selected_data);

					if (list_plan.size() > 0) {

						if (selected_data.equalsIgnoreCase(currentDate)) {
							Log.d(TAG, "listview_studyplan跳转" + isccc);
							isccc = true;
						} else {
							isccc = false;
							Log.d(TAG, "listview_studyplan不跳转" + isccc);
						}
						listview_studyplan.setVisibility(View.VISIBLE);
						relative_warn_nulldata_schedule
								.setVisibility(View.GONE);
						findNearestClassPlan(System.currentTimeMillis());
						adapter = new StudyPlanAdapter(getActivity(),
								list_plan, isccc);
						listview_studyplan.setAdapter(adapter);
					} else {
						listview_studyplan.setVisibility(View.GONE);
						relative_warn_nulldata_schedule
								.setVisibility(View.VISIBLE);
					}

					calV.setCurrentFlag(position);
					calV.notifyDataSetChanged();

				} else if (position < startPosition) {
					getPreviousMonth();
				} else if (position > endPosition) {
					getNextMonth();
				} else {
					Toast.makeText(getActivity(), "No", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		gridView.setAdapter(calV);
		previous = (ImageButton) mview.findViewById(R.id.previous);
		next = (ImageButton) mview.findViewById(R.id.next);

		main = (LinearLayout) mview.findViewById(R.id.main);

		topText = (TextView) mview.findViewById(R.id.toptext);
		addTextToTopTextView(topText);

		previous.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LodDialogClass.showCustomCircleProgressDialog(getActivity(),
						null, getString(R.string.common_Loading));

				getPreviousMonth();
			}
		});

		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LodDialogClass.showCustomCircleProgressDialog(getActivity(),
						null, getString(R.string.common_Loading));
				getNextMonth();
			}
		});

	}

	// 上一个月 的 日历列表
	private void getPreviousMonth() {
		Log.d(TAG, "getPreviousMonth()");

		System.out.println("上一个月的数据jumpMonth之前==" + jumpMonth);
		// showDialog();
		jumpMonth--;
		System.out.println("上一个月的数据jumpMonth之后==" + jumpMonth);
		// 获取当前的日期 （即此月的上一个月的日期）
		int day_s = day_c;

		int year_s = year_c + jumpYear;
		int month_s = month_c + jumpMonth;

		if (month_s > 0) {
			// //往下一个月跳转
			if (month_s % 12 == 0) {
				year_s = year_c + month_s / 12 - 1;
				month_s = 12;
			} else {
				year_s = year_c + month_s / 12;
				month_s = month_s % 12;
			}
		} else {
			// 往上一个月跳转
			year_s = year_c - 1 + month_s / 12;
			month_s = month_s % 12 + 12;
			if (month_s % 12 == 0) {

			}
		}
		String data_s = String.valueOf(year_s) + "-"
				+ String.format("%02d", Integer.valueOf(month_s)) + "-"
				+ String.format("%02d", Integer.valueOf(day_s));
		if (!currentDate.equalsIgnoreCase(data_s)) {
			data_s = String.valueOf(year_s) + "-"
					+ String.format("%02d", Integer.valueOf(month_s)) + "-"
					+ String.format("%02d", 1);
		}
		System.out.println("上个月的日期为===" + data_s);

		getdatafromnet(data_s);

		// calV = new CalendarView(getActivity(), getResources(), jumpMonth,
		// jumpYear, year_c, month_c, day_c,null);
		// gridView.setAdapter(calV);
		// addTextToTopTextView(topText);
		topText.setText(String.valueOf(year_s) + "年"
				+ String.format("%02d", Integer.valueOf(month_s)) + "月");
	}

	// 下一个月的 日历列表
	private void getNextMonth() {

		Log.d(TAG, "getNextMonth()");
		System.out.println("下一个月的数据");
		// showDialog();

		jumpMonth++;

		System.out.println("下一个月的数据jumpMonth之后==" + jumpMonth);
		// 获取当前的日期 （即此月的上一个月的日期）
		int day_s = day_c;

		int year_s = year_c + jumpYear;
		int month_s = month_c + jumpMonth;

		if (month_s > 0) {
			// //往下一个月跳转
			if (month_s % 12 == 0) {
				year_s = year_c + month_s / 12 - 1;
				month_s = 12;
			} else {
				year_s = year_c + month_s / 12;
				month_s = month_s % 12;
			}
		} else {
			// 往上一个月跳转
			year_s = year_c - 1 + month_s / 12;
			month_s = month_s % 12 + 12;
			if (month_s % 12 == 0) {

			}
		}
		String data_s = String.valueOf(year_s) + "-"
				+ String.format("%02d", Integer.valueOf(month_s)) + "-"
				+ String.format("%02d", Integer.valueOf(day_s));

		if (!currentDate.equalsIgnoreCase(data_s)) {
			data_s = String.valueOf(year_s) + "-"
					+ String.format("%02d", Integer.valueOf(month_s)) + "-"
					+ String.format("%02d", 1);
		}
		System.out.println("下个月的日期为===" + data_s);

		getdatafromnet(data_s);

		// addTextToTopTextView(topText);
		topText.setText(String.valueOf(year_s) + "年"
				+ String.format("%02d", Integer.valueOf(month_s)) + "月");
	}

	@SuppressWarnings("unused")
	private void addTextToTopTextView(TextView view) {
		String datestr = null;
		StringBuffer textDate = new StringBuffer();
		// 判断月份
		if (Integer.valueOf(calV.getShowMonth()) > 9) {
			datestr = calV.getShowYear() + "年" + calV.getShowMonth() + "月";
		} else {
			datestr = calV.getShowYear() + "年0" + calV.getShowMonth() + "月";
		}
		view.setText(datestr);
		view.setTextColor(Color.WHITE);
		view.setTypeface(Typeface.DEFAULT_BOLD);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imagview_myself:
			Intent intent = new Intent();
			intent.setClass(getActivity(), MyselfActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	// 获取 某个日起 对应的 小圆点 　　list<日期，个数>
	private HashMap<String, Object> getdatadotnum(
			HashMap<String, String> dataandlessons) {

		HashMap<String, Object> map_num = new HashMap<String, Object>();

		@SuppressWarnings("unused")
		List<HashMap<String, Object>> l_lesson = new ArrayList<HashMap<String, Object>>();
		try {
			for (int i = 0; i < l_lessondata.size(); i++) {

				String s = dataandlessons.get(l_lessondata.get(i));
				System.out.println("日起的校园点位============" + s);
				if (s.equalsIgnoreCase("null")) {
					break;
				}
				JSONObject obj = new JSONObject(s);
				String lessons = obj.getString("lessons");
				JSONArray array = new JSONArray(lessons);

				// String lesson = l_lessondata.get(i);
				// String lesson_t = String.format("%02d",
				// Integer.valueOf(lesson, 11));

				// map <日期，对应课程数量>
				map_num.put(l_lessondata.get(i), String.valueOf(array.length()));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return map_num;
	}

	/***
	 * param dataandlessons: calendar - select - data -listviewdata
	 * 
	 * */
	private List<HashMap<String, Object>> getlistdata(
			HashMap<String, String> dataandlessons, String ri) {

		System.out.println("日期为====================================" + ri);

		List<HashMap<String, Object>> l_data = new ArrayList<HashMap<String, Object>>();

		if (!dataandlessons.containsKey(ri)) {
			return l_data;
		}
		try {
			// for (int i = 0; i < l_lessondata.size(); i++) {
			String s = dataandlessons.get(ri);

			JSONObject obj = new JSONObject(s);
			String lessons = obj.getString("lessons");
			JSONArray array = new JSONArray(lessons);
			for (int j = 0; j < array.length(); j++) {

				// "id": 29905,
				// "sCode": "YA10113",
				// "SectEnd": 1435717800000,
				// "sNameBr": "朝阳国贸校区瑞赛商务楼6层VIP3",
				// "sNameBc": "IELTS1对1班（基础）",
				// "sAddress": "北京市朝阳区建国路128号中航工业大厦",
				// "SectBegin": 1435708800000,
				// "nowLessonId": 29905,
				// "nLessonNo": 21

				JSONObject obj_l = array.optJSONObject(j);
				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("id", obj_l.get("id"));
				map.put("classId", obj_l.get("classId"));
				map.put("sCode", obj_l.get("sCode"));
				map.put("SectEnd", obj_l.get("SectEnd"));
				map.put("sNameBr", obj_l.get("sNameBr"));
				map.put("sNameBc", obj_l.get("sNameBc"));
				map.put("sAddress", obj_l.get("sAddress"));
				map.put("SectBegin", obj_l.get("SectBegin"));
				map.put("nowLessonId", obj_l.get("nowLessonId"));
				map.put("nLessonNo", obj_l.get("nLessonNo"));
				map.put("inTime", false);
				l_data.add(map);
			}

			// }

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return l_data;
	}

	/*
	 * // 显示自定义加载对话框 public CustomProgressDialog
	 * showCustomCircleProgressDialog(String title, String msg) { if
	 * (customDialog != null) { try { customDialog.cancel(); customDialog =
	 * null; } catch (Exception e) { } }
	 * 
	 * 
	 * customDialog = CustomProgressDialog.createDialog(this.getActivity()); //
	 * dialog.setIndeterminate(false); //
	 * dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	 * customDialog.setTitle(title); customDialog.setMessage(msg);
	 * 
	 * try { customDialog.show(); } catch (Exception e) { }
	 * customDialog.setCanceledOnTouchOutside(false);//设置dilog点击屏幕空白处不消失 return
	 * customDialog; }
	 */

	private void findNearestClassPlan(long currentInterval) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		Iterator<HashMap<String, Object>> iters = list_plan.iterator();
		try {
			currentInterval = sdf.parse(sdf.format(new Date(currentInterval)))
					.getTime();
			while (iters.hasNext()) {
				HashMap<String, Object> iter = iters.next();
				long startTime = Long.parseLong(iter.get("SectBegin")
						.toString());
				long endTime = Long.parseLong(iter.get("SectEnd").toString());
				iter.put("inTime", currentInterval >= startTime
						&& currentInterval <= endTime);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}