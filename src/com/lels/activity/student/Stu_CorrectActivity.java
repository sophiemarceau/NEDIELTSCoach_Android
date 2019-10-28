package com.lels.activity.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lels.adapter.student.Stu_Correct_Adapter;
import com.lels.adapter.student.StudentStartAdapter;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class Stu_CorrectActivity extends Activity implements OnClickListener {
	private Context mContext;
	private PullToRefreshListView mListview;// 列表
	private List<HashMap<String, Object>> mList;// 列表集合
	private Stu_Correct_Adapter mAdapter;// 列表adapter
	private ImageButton back_img;// 返回按钮
	private String classid;// 班级编码
	private SharedPreferences share, share_class;// share
	private String url = Constants.URL_TeacherClassesgetCorrectList;// 路径

	private String index = "1";
	private RelativeLayout relative_warn_nulldata_clastudy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stu_correct);
		getintent();// 获取传递值
		initview();// 初始化组件
		getlist();// 网络请求获取集合
	}

	private boolean flag = false;

	/**
	 * 获取传递值
	 */
	private void getintent() {
		share_class = getSharedPreferences("clcode", MODE_PRIVATE);
		classid = share_class.getString("nowLessonId", "");
		System.out.println(">>>>>" + classid);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		getlist();
	}

	/**
	 * 网络请求数据
	 */
	private void getlist() {
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("ccId", classid);
		params.addBodyParameter("pageIndex", index);
		HttpUtils util = new HttpUtils();
		util.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LodDialogClass.closeCustomCircleProgressDialog();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				LodDialogClass.closeCustomCircleProgressDialog();
				String result = arg0.result;
				System.out.println(result);
				try {
					JSONObject obj = new JSONObject(result);
					JSONObject data = obj.getJSONObject("Data");
					String count = data.getString("count");
					JSONArray array = data.getJSONArray("list");
					if (index.equals("1")) {
						mList = new ArrayList<HashMap<String, Object>>();
					}
					for (int i = 0; i < array.length(); i++) {
						JSONObject objcorrect = array.getJSONObject(i);
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("paperName", objcorrect.getString("paperName"));// 试卷名
						map.put("uName", objcorrect.getString("uName"));// 学生姓名
						map.put("ExamID", objcorrect.getString("ExamID"));// 答案信息
						map.put("TaskSubmitTime",
								objcorrect.getString("TaskSubmitTime"));// 任务提交时间
						map.put("LcName", objcorrect.getString("LcName"));// 试题类型名称："口语"、"写作"等;
						map.put("taskType", objcorrect.getString("taskType"));
						map.put("paperId", objcorrect.getString("paperId"));
						map.put("nGender", objcorrect.getString("nGender"));// 性别[1男2女];暂为空
						mList.add(map);
					}
					if (mList.size() / Integer.valueOf(index) < 10) {
						flag = true;
					} else {
						flag = false;
					}
					// 数据为空则提示，listview 消失
					if (mList.size() != 0) {
						mListview.setVisibility(View.VISIBLE);
						relative_warn_nulldata_clastudy
								.setVisibility(View.GONE);
						mAdapter = new Stu_Correct_Adapter(mList, mContext);
						if (index.equals("1")) {
							mListview.setAdapter(mAdapter);
						} else {
							mAdapter.notifyDataSetChanged();
						}
						refresh(mListview, mAdapter);
					} else {
						mListview.setVisibility(View.GONE);
						relative_warn_nulldata_clastudy
								.setVisibility(View.VISIBLE);
					}

					mListview.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							String taskType = mList.get(position - 1)
									.get("taskType").toString();
							String paperId = mList.get(position - 1)
									.get("paperId").toString();
							String ExamID = mList.get(position - 1)
									.get("ExamID").toString();
							if (mList.get(position - 1).get("LcName")
									.toString().equals("口语")) {
								if (taskType.equals("1")) {// 1模考
									Intent mkintent = new Intent();
									mkintent.putExtra("ExamID", ExamID);
									mkintent.putExtra("paperId", paperId);
									System.out
											.println("1模考ExamID----" + ExamID);
									mkintent.setClass(mContext,
											Correct_listener_mkActivity.class);
									startActivity(mkintent);

								} else if (taskType.equals("2")) {// 2练习

									Intent mkintent = new Intent();
									mkintent.putExtra("ExamID", ExamID);
									mkintent.putExtra("paperId", paperId);
									System.out
											.println("2练习ExamID----" + ExamID);
									mkintent.setClass(mContext,
											Correct_listener_lxActivity.class);
									startActivity(mkintent);
								}

							} else if (mList.get(position - 1).get("LcName")
									.toString().equals("写作")) {
								Intent xzintent = new Intent();
								xzintent.putExtra("ExamID", ExamID);
								xzintent.putExtra("paperId", paperId);
								xzintent.setClass(mContext,
										Correct_writeActivity.class);
								startActivity(xzintent);
							}
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	private class FinishRefresh extends AsyncTask<Void, Void, Void> {
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
			mListview.onRefreshComplete();
		}
	}

	public void refresh(final PullToRefreshListView listview,
			final Stu_Correct_Adapter adapter) {
		listview.setMode(Mode.BOTH);
		initIndicator();
		listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// pageIndex.equals("1");
				index = "1";
				getlist();
				new FinishRefresh().execute();
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				int x = Integer.parseInt(index);
				x++;
				index = String.valueOf(x);
				getlist();
				new FinishRefresh().execute();
				adapter.notifyDataSetChanged();
			}
		});
	}

	private void initIndicator() {
		ILoadingLayout startLabels = mListview.getLoadingLayoutProxy(true,
				false);
		startLabels.setPullLabel("下拉刷新");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在刷新...");// 刷新时
		startLabels.setReleaseLabel("放开以加载");// 下来达到一定距离时，显示的提示
		ILoadingLayout endLabels = mListview.getLoadingLayoutProxy(false, true);
		if (flag == true) {
			endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
			endLabels.setRefreshingLabel("全部加载完成");// 刷新时
			endLabels.setReleaseLabel("放开以加载更多");// 下来达到一定距离时，显示的提示aaa
		} else {
			endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
			endLabels.setRefreshingLabel("加载中。。。");// 刷新时
			endLabels.setReleaseLabel("放开以加载更多");// 下来达到一定距离时，显示的提示
		}

	}

	/**
	 * 初始化组件
	 */
	private void initview() {
		mContext = this;

		LodDialogClass.showCustomCircleProgressDialog(mContext, null,
				getString(R.string.common_Loading));
		// 实例化share
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);

		mListview = (PullToRefreshListView) findViewById(R.id.stu_correct_listview);
		back_img = (ImageButton) findViewById(R.id.stucorrect_back_img);
		back_img.setOnClickListener(this);
		relative_warn_nulldata_clastudy = (RelativeLayout) findViewById(R.id.relative_warn_nulldata_clastudy);
	}

	/**
	 * 返回按钮监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.stucorrect_back_img:
			finish();
			break;

		default:
			break;
		}
	}
}
