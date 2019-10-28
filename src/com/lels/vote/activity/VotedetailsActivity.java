/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015-10-16 
 * 
 *******************************************************************************/
package com.lels.vote.activity;

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
import com.lels.vote.adapter.VoteDetalisAdapter;
import com.lelts.tool.CalculateListviewGrideview;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015-10-16
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class VotedetailsActivity extends Activity implements OnClickListener {

	private Context contenxt;
	private SharedPreferences share;
	// private ImageView votedetails_back_image;
	private List<HashMap<String, Object>> mlist;

	// 投票的内容
	private TextView votedetails_txt_content;
	// 获取选项
	private ListView mlistview_detalis;
	private String voteId;
	// 结束投票
	private Button vote_btn_endvote;

	// 参数
	private String path;
	private String activeClassId;
	private String optNum;

	// 轮询学生投票的结果
	private Timer collectStuVotes_timer;
	private List<HashMap<String, Object>> collectStuVoteslist;
	private VoteDetalisAdapter votedetalisadapter;
	

	private Boolean flag = true;
	private RelativeLayout my_relative;
	//判断投一次票
	private boolean vote_flag = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vote_details);
		initview();
		collectStuVotes();
		collectStuVotestimer();
	}

	/**
	 * 方法说明：初始化控件
	 * 
	 */
	private void initview() {
		// TODO Auto-generated method stub
		contenxt = this;
		// votedetails_back_image = (ImageView)
		// findViewById(R.id.votedetails_back_image);
		// votedetails_back_image.setOnClickListener(this);

		mlistview_detalis = (ListView) findViewById(R.id.listview_vote_details);
		vote_btn_endvote = (Button) findViewById(R.id.vote_btn_endvote);
		vote_btn_endvote.setOnClickListener(this);
		votedetails_txt_content = (TextView) findViewById(R.id.votedetails_txt_content);

		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);

		activeClassId = share.getString("activeClassId", "");
		Intent getdata = getIntent();
		if (getdata != null) {
			voteId = getdata.getStringExtra("voteId");
			votedetails_txt_content.setText(getdata.getStringExtra("voteDesc"));
		}
		my_relative = (RelativeLayout) findViewById(R.id.my_relative);
	}
	
	
	/**
	 * 方法说明：学生端参与投票iphone、Android 学生App
	 * 
	 */
	private void joinVote() {
		// TODO Auto-generated method stub
		// [Args]：
		//  activeClassId=[互动课堂ID，不可为空]
		//  voteId=[投票定义ID，不可为空]
		//  optNum=[投票选项号，不可为空]
		// [Return]：
		//  Infomation = 提示信息;
		//  Result = 返回结果true/false;
		//  Data = {
		//   voteResult = 投票选项统计[{,
		//    OptionNum = 投票选项号,
		//    OptionDesc = 投票选项描述,
		//    voteNum = 投票率,
		//    ownVote = 是否是自己投的票，1是0不是,
		//    finishVote = 投票是否结束,true是，false否,
		//   }]
		//  }

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("activeClassId", activeClassId);
		params.addBodyParameter("voteId", voteId);
		params.addBodyParameter("optNum", optNum);
		System.out.println("activeClassId===" + activeClassId
				+ "=====voteId===" + voteId + "=====optNum===" + optNum);
		HttpUtils util = new HttpUtils();
		path = Constants.URL_joinVote;
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				System.out.println("老师参与投票的结果----" + result);

//				intent = new Intent(context, VoteDetailActivity.class);
//				intent.putExtra("voteResult", voteResult);
//				intent.putExtra("Subject", Subject);
//				startActivity(intent);
//				finish();
				// try {
				// JSONObject obj = new JSONObject(result);
				// JSONObject data = obj.getJSONObject("Data");
				// JSONArray array = data.getJSONArray("voteResult");
				// for (int i = 0; i < array.length(); i++) {
				// JSONObject voteResult = array.getJSONObject(i);
				// HashMap<String, Object> map = new HashMap<String, Object>();
				// String OptionNum = voteResult.getString("OptionNum");
				// String finishVote = voteResult.getString("finishVote");
				// String voteNum = voteResult.getString("voteNum");
				// String OptionDesc = voteResult.getString("OptionDesc");
				// map.put("OptionNum", OptionNum);
				// map.put("finishVote", finishVote);
				// map.put("voteNum", voteNum);
				// map.put("OptionDesc", OptionDesc);
				// mlist.add(map);
				// }
				// ShowDiglogDevelop();
				// } catch (JSONException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
		});

	}


	/**
	 * 方法说明：教师端心跳汇总当前投票数据 3秒一轮寻
	 * 
	 */
	private void collectStuVotes() {
		
		// TODO Auto-generated method stub
		// [Args]：
		//  activeClassId=[互动课堂ID，不可为空]
		//  voteId=[投票定义ID，不可为空]
		// [Return]：
		//  Infomation = 提示信息;
		//  Result = 返回结果true/false;
		//  Data = [{
		//   OptionNum = 投票选项号,
		//   OptionDesc = 投票选项描述,
		//   voteNum = 投票率,
		//   finishVote = 投票是否结束,true是，false否,
		//  }]
		collectStuVoteslist = new ArrayList<HashMap<String, Object>>();
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("activeClassId", activeClassId);
		params.addBodyParameter("voteId", voteId);
		System.out.println("activeClassId===" + activeClassId
				+ "=====voteId===" + voteId);
		HttpUtils util = new HttpUtils();
		path = Constants.URL_collectStuVotes;
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				System.out.println("轮询投票结果----" + result);
				try {
					JSONObject obj = new JSONObject(result);
					JSONArray array = obj.getJSONArray("Data");
					for (int i = 0; i < array.length(); i++) {
						JSONObject Data = array.getJSONObject(i);
						HashMap<String, Object> map = new HashMap<String, Object>();
						String OptionNum = Data.getString("OptionNum");
						String finishVote = Data.getString("finishVote");
						String voteNum = Data.getString("voteNum");
						String OptionDesc = Data.getString("OptionDesc");
						String ownVote  = Data.getString("ownVote");
						map.put("OptionNum", OptionNum);
						map.put("finishVote", finishVote);
						map.put("voteNum", voteNum);
						map.put("OptionDesc", OptionDesc);
						map.put("ownVote", ownVote);
						collectStuVoteslist.add(map);
					}
					if(flag == true){
						votedetalisadapter = new VoteDetalisAdapter(
								collectStuVoteslist, contenxt);
						mlistview_detalis.setAdapter(votedetalisadapter);
						flag = false;
					}else{
						votedetalisadapter.setdatachanges(collectStuVoteslist);
						votedetalisadapter.notifyDataSetChanged();					
						}
					CalculateListviewGrideview
							.setListViewHeightBasedOnChildren(mlistview_detalis);
					System.out.println("vote_flag======="+vote_flag);
					
					if (vote_flag==true) {
						mlistview_detalis.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
							
								optNum = collectStuVoteslist.get(position).get("OptionNum").toString();
								joinVote();
								vote_flag = false;
								mlistview_detalis.setPressed(false);
								mlistview_detalis.setEnabled(false);
							}
					});
					}
					
				
		
						
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 方法说明：结束投票
	 * 
	 */
	private void finishVote() {
		// TODO Auto-generated method stub
		// [Args]：
		//  activeClassId=[互动课堂ID，不可为空]
		//  voteId=[投票定义ID，不可为空]
		// [Return]：
		//  Infomation = 提示信息;
		//  Result = 返回结果true/false;
		//  Data = {
		//   voteResult = 投票选项统计[{,
		//    OptionNum = 投票选项号,
		//    OptionDesc = 投票选项描述,
		//    voteNum = 投票率,
		//    finishVote = 投票是否结束,true是，false否,
		//   }]
		//  }
		mlist = new ArrayList<HashMap<String, Object>>();

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("activeClassId", activeClassId);
		params.addBodyParameter("voteId", voteId);
		System.out.println("activeClassId===" + activeClassId
				+ "=====voteId===" + voteId);
		HttpUtils util = new HttpUtils();
		path = Constants.URL_finishVote;
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("onFailure");
				LodDialogClass.closeCustomCircleProgressDialog();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				System.out.println("结束投票----" + result);
				LodDialogClass.closeCustomCircleProgressDialog();
				try {
					JSONObject obj = new JSONObject(result);
					JSONObject data = obj.getJSONObject("Data");
					JSONArray array = data.getJSONArray("voteResult");
					for (int i = 0; i < array.length(); i++) {
						JSONObject voteResult = array.getJSONObject(i);
						HashMap<String, Object> map = new HashMap<String, Object>();
						String OptionNum = voteResult.getString("OptionNum");
						String finishVote = voteResult.getString("finishVote");
						String voteNum = voteResult.getString("voteNum");
						String OptionDesc = voteResult.getString("OptionDesc");
						map.put("OptionNum", OptionNum);
						map.put("finishVote", finishVote);
						map.put("voteNum", voteNum);
						map.put("OptionDesc", OptionDesc);
						mlist.add(map);
					}
					
					finish();
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
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
	
		// 结束投票 弹出DiaLog的详情
		case R.id.vote_btn_endvote:
			if (collectStuVotes_timer != null) {
				collectStuVotes_timer.cancel();
			}
			collectStuVotes_timer = null;
			LodDialogClass.showCustomCircleProgressDialog(contenxt, null, "结束投票中...");
			finishVote();
	
			break;

		default:
			break;
		}
	}

	/**
	 * 方法说明：每3秒 请求心跳汇总当前投票数据
	 * 
	 */
	private void collectStuVotestimer() {
		collectStuVotes_timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				collectStuVotes();
			}
		};
		collectStuVotes_timer.schedule(task, 3000, 3000);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		if (collectStuVotes_timer != null) {
			collectStuVotes_timer.cancel();
		}
		collectStuVotes_timer = null;
		super.onDestroy();
	}
}
