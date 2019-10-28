package com.lelts.fragment.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hello.R;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.myclassfm.adapter.MyclassFm_adapter;
import com.lelts.activity.classes.Class_ing_ContentActivity;
import com.lelts.activity.classes.Class_no_ContentActivity;
import com.lelts.adapter.fragment.adapter.MyViewPagerAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 班级主页
 */

@SuppressLint("InflateParams")
public class ClassFm extends Fragment implements OnClickListener {

	private ArrayList<View> listViews;
	private TextView rb_class_ing;
	private TextView rb_class_no;
	private TextView rb_class_over;
	private View view1, view2, view3;
	private PullToRefreshListView listview1, listview2, listview3;
	private List<HashMap<String, Object>> mList1;
	private ViewPager viewpager_class_main;
	public static final int TYPE_ING = 0, TYPE_NO = 1, TYPE_OVER = 2;
	private int index = 1;
	private SharedPreferences share, shares;
	private String path = Constants.URL_TeacherClassesloadClasses;
	private Editor editor;
	private MyclassFm_adapter ap;
	private TextView txt_warn_nulldata_class_main1,
			txt_warn_nulldata_class_main2, txt_warn_nulldata_class_main3;
	private int size;
	private boolean flag = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_class_main, null);

	}

	/**
	 * 
	 * 网络请求，返回list集合；
	 */
	public void getlist(int i, final String j,
			final PullToRefreshListView listview, final int type,
			final TextView txt_warn) {
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("pageIndex", String.valueOf(i));
		params.addBodyParameter("type", j);

		System.out.println("token==========" + share.getString("Token", "")
				+ "--pageIndex--" + i + "--j--" + j);

		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);
		utils.send(HttpMethod.POST, path, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LodDialogClass.closeCustomCircleProgressDialog();
						Toast.makeText(getActivity(), "您当前网络不佳，请稍后重试！！",
								Toast.LENGTH_LONG).show();
					}

					@SuppressWarnings("deprecation")
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						LodDialogClass.closeCustomCircleProgressDialog();
						String result = arg0.result;
						System.out.println(result);
						try {
							/*
							 * count = 总数;   list = 消息集合({    id =班级ID;    sCode
							 * =班级编号;    sName =班级名称;    dtBeginDate =开课日期;    
							 * dtEndDate =结课日期;    nowLessonId =正在上课班级课次ID;
							 *    stuNum =该班学生数量;
							 */
							JSONObject obj = new JSONObject(result);
							String Result = obj.getString("Result");

							JSONObject obj1 = obj.getJSONObject("Data");

							size = Integer.valueOf(obj1.getString("count"));
							if (index == 1) {
								mList1 = new ArrayList<HashMap<String, Object>>();
							}
							final JSONArray array = obj1.getJSONArray("list");
							if (array != null) {
								for (int k = 0; k < array.length(); k++) {
									JSONObject data = array.getJSONObject(k);
									HashMap<String, Object> map = new HashMap<String, Object>();
									map.put("sCode", data.getString("sCode"));
									map.put("id", data.getString("id"));
									map.put("nowLessonId",
											data.getString("nowLessonId"));
									map.put("dtEndDate",
											data.getString("dtEndDate"));
									map.put("sName", data.getString("sName"));
									map.put("dtBeginDate",
											data.getString("dtBeginDate"));
									map.put("stuNum", data.getString("stuNum"));
									mList1.add(map);
								}
								System.out.println("mList1--" + mList1);
							}
							if (mList1.size()/index<10) {
								flag = true;
							}else{
								flag = false;
							}
							if (mList1.size() != 0) {
								listview.setVisibility(View.VISIBLE);
								txt_warn.setVisibility(View.GONE);
								if (index == 1) {
									ap = new MyclassFm_adapter(mList1,
											getActivity(), type);
									listview.setAdapter(ap);
								} else {
									ap.notifyDataSetChanged();
								}
							} else {
								txt_warn.setVisibility(View.VISIBLE);
								listview.setVisibility(View.GONE);
							}
							listview.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
							initIndicator(mList1, listview);
							listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {
								@Override
								public void onPullDownToRefresh(
										PullToRefreshBase<ListView> refreshView) {
									setMyLable(refreshView);
									index = 1;
									getlist(index, j, listview, type, txt_warn);
									new FinishRefresh(listview).execute();
								}

								@Override
								public void onPullUpToRefresh(
										PullToRefreshBase<ListView> refreshView) {
									setMyLable(refreshView);
									index++;
									getlist(index, j, listview, type, txt_warn);
									new FinishRefresh(listview).execute();
								}
							});

							listview.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									position = position - 1;
									try {
										String scode = mList1.get(position)
												.get("sCode").toString();
										String nowLessonId = mList1
												.get(position)
												.get("nowLessonId").toString();
										String sName = mList1.get(position)
												.get("sName").toString();
										String classid = mList1.get(position)
												.get("id").toString();
										System.out.println(scode + "列表==="
												+ nowLessonId
												+ "----->nowLessonId");
										editor.putString("scode", scode);
										editor.putString("nowLessonId",
												nowLessonId);
										editor.putString("sName", sName);
										editor.putString("id", classid);
										editor.commit();
										Intent intent = new Intent();
										if (j.equals("0")) {
											/*
											 * intent.putExtra("sCode", scode);
											 * intent.putExtra("sName", sName);
											 * intent.putExtra("nowLessonId",
											 * nowLessonId);
											 */
											intent.setClass(
													getActivity(),
													Class_ing_ContentActivity.class);
										} else if (j.equals("1")) {
											intent.setClass(
													getActivity(),
													Class_no_ContentActivity.class);
										} else {
											Toast.makeText(getActivity(),
													"已结课班级无法查看", 0).show();
										}
										startActivity(intent);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	// 设置刷新时间
	private void setMyLable(PullToRefreshBase<ListView> refreshView) {
		String label = DateUtils.formatDateTime(getActivity(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
	}

	/**
	 * 自定义上拉刷新和下拉加载的显示文字
	 */
	private void initIndicator(List<HashMap<String, Object>> list,
			PullToRefreshListView listview) {
		ILoadingLayout endLabel = listview.getLoadingLayoutProxy(true, false);

		endLabel.setPullLabel("下拉刷新");// 刚下拉时，显示的提示
		endLabel.setRefreshingLabel("加载中。。。");// 刷新时
		endLabel.setReleaseLabel("放开以加载更多");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = listview.getLoadingLayoutProxy(false, true);
//		syso
		if (flag == true) {
			endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
			endLabels.setRefreshingLabel("全部加载完毕");// 刷新时
			endLabels.setReleaseLabel("放开以加载更多");// 下来达到一定距离时，显示的提示
		} else {
			endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
			endLabels.setRefreshingLabel("加载中。。。");// 刷新时
			endLabels.setReleaseLabel("放开以加载更多");// 下来达到一定距离时，显示的提示
		}

	}

	private class FinishRefresh extends AsyncTask<Void, Void, Void> {
		private PullToRefreshListView listview;

		public FinishRefresh(PullToRefreshListView listview) {
			super();
			this.listview = listview;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// adapter.notifyDataSetChanged();
			listview.onRefreshComplete();
		}
	}

	/**
	 * 初始化组件
	 */
	private void init() {
		shares = getActivity().getSharedPreferences("clcode",
				getActivity().MODE_PRIVATE);
		editor = shares.edit();
		// 获取id;
		viewpager_class_main = (ViewPager) getActivity().findViewById(
				R.id.viewpager_class_main);
		rb_class_ing = (TextView) getActivity().findViewById(R.id.rb_class_ing);
		rb_class_no = (TextView) getActivity().findViewById(R.id.rb_class_no);
		rb_class_over = (TextView) getActivity().findViewById(
				R.id.rb_class_over);
		// 设置监听;
		rb_class_ing.setOnClickListener(this);
		rb_class_no.setOnClickListener(this);
		rb_class_over.setOnClickListener(this);
		// 创建三个view 如下
		listViews = new ArrayList<View>();
		// 得到view视图；
		view1 = LayoutInflater.from(getActivity()).inflate(
				R.layout.view_class_tabs, null);
		view2 = LayoutInflater.from(getActivity()).inflate(
				R.layout.view_class_tabs, null);
		view3 = LayoutInflater.from(getActivity()).inflate(
				R.layout.view_class_tabs, null);
		listViews.add(view1);
		listViews.add(view2);
		listViews.add(view3);
		share = getActivity().getSharedPreferences("userinfo",
				Context.MODE_PRIVATE);
		listview1 = (PullToRefreshListView) view1
				.findViewById(R.id.listview_class_main);
		listview2 = (PullToRefreshListView) view2
				.findViewById(R.id.listview_class_main);
		listview3 = (PullToRefreshListView) view3
				.findViewById(R.id.listview_class_main);
		// 获得 提示的textview
		txt_warn_nulldata_class_main1 = (TextView) view1
				.findViewById(R.id.txt_warn_nulldata_class_main);
		txt_warn_nulldata_class_main2 = (TextView) view2
				.findViewById(R.id.txt_warn_nulldata_class_main);
		txt_warn_nulldata_class_main3 = (TextView) view3
				.findViewById(R.id.txt_warn_nulldata_class_main);

		LodDialogClass.showCustomCircleProgressDialog(getActivity(), null,
				getString(R.string.common_Loading));

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
		getClass_ing();
		// getClass_no();
		// getClass_over();
		MyViewPagerAdapter adapter = new MyViewPagerAdapter(getActivity(),
				listViews);

		viewpager_class_main.setAdapter(adapter);

		viewpager_class_main.setCurrentItem(0);

		viewpager_class_main.setOnPageChangeListener(new PageChangeListener());

	}

	// 已结课；
	@SuppressWarnings("unused")
	private void getClass_over() {
		LodDialogClass.showCustomCircleProgressDialog(getActivity(), null,
				getString(R.string.common_Loading));
		getlist(index, "2", listview3, TYPE_OVER, txt_warn_nulldata_class_main3);
	}

	// 未开课；
	@SuppressWarnings("unused")
	private void getClass_no() {
		System.out.println("no  no  no");
		LodDialogClass.showCustomCircleProgressDialog(getActivity(), null,
				getString(R.string.common_Loading));

		getlist(index, "1", listview2, TYPE_NO, txt_warn_nulldata_class_main2);
	}

	// 上课中；
	private void getClass_ing() {
		System.out.println("ing  ing  ing");
		LodDialogClass.showCustomCircleProgressDialog(getActivity(), null,
				getString(R.string.common_Loading));

		getlist(index, "0", listview1, TYPE_ING, txt_warn_nulldata_class_main1);
	}

	/**
	 * 
	 * viewpager页面切换监听
	 *
	 */
	private class PageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			switch (position) {
			case 0:
				// mList1.removeAll(mList1);
				index = 1;
				getClass_ing();
				getChoies_ing();

				break;
			case 1:
				// mList1.removeAll(mList1);
				index = 1;
				getClass_no();
				getChoies_on();
				break;
			case 2:
				// mList1.removeAll(mList1);
				index = 1;
				getClass_over();
				getChoies_over();
				break;
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rb_class_ing:
			// mList1.removeAll(mList1);
			index = 1;
			getChoies_ing();
			break;
		case R.id.rb_class_no:
			// mList1.removeAll(mList1);
			index = 1;
			getChoies_on();
			break;
		case R.id.rb_class_over:
			// mList1.removeAll(mList1);
			index = 1;
			getChoies_over();
			break;
		}
	}

	private void getChoies_on() {

		viewpager_class_main.setCurrentItem(1);
		rb_class_ing.setBackgroundResource(R.drawable.left_null);
		rb_class_ing.setTextColor(Color.RED);
		rb_class_no.setBackgroundResource(R.drawable.center_red);
		rb_class_no.setTextColor(Color.WHITE);
		rb_class_over.setBackgroundResource(R.drawable.right_kong);
		rb_class_over.setTextColor(Color.RED);

	}

	private void getChoies_over() {
		viewpager_class_main.setCurrentItem(2);
		rb_class_ing.setBackgroundResource(R.drawable.left_null);
		rb_class_ing.setTextColor(Color.RED);
		rb_class_no.setBackgroundResource(R.drawable.center_null);
		rb_class_no.setTextColor(Color.RED);
		rb_class_over.setBackgroundResource(R.drawable.right_red);
		rb_class_over.setTextColor(Color.WHITE);

	}

	private void getChoies_ing() {
		viewpager_class_main.setCurrentItem(0);
		// 设置背景图片和字体颜色
		rb_class_ing.setBackgroundResource(R.drawable.left_red);
		rb_class_ing.setTextColor(Color.WHITE);
		rb_class_no.setBackgroundResource(R.drawable.center_null);
		rb_class_no.setTextColor(Color.RED);
		rb_class_over.setBackgroundResource(R.drawable.right_kong);
		rb_class_over.setTextColor(Color.RED);

	}

}
