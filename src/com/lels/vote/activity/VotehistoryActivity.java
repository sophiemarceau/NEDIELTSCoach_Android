/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015-10-16 
 * 
 *******************************************************************************/ 
package com.lels.vote.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.bean.VotehisVotes;
import com.lels.bean.Voteopts;
import com.lels.constants.Constants;
import com.lels.vote.adapter.HistoryVoteAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

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
public class VotehistoryActivity  extends Activity implements OnClickListener{
	
		private Context context;
		private SharedPreferences share,teachershare;
		private ImageButton vote_history_back_image;
		private ListView listview_vote_history;
		private List<VotehisVotes> mlist;
		private List<Voteopts> mlistopts;
		List<HashMap<String, Object>> chooselist;
		private HistoryVoteAdapter madapter;
		private Voteopts opteinfo;
		
		//参数
		private String path;
		private String activeClassId;
		/* (non-Javadoc)
		 * @see android.app.Activity#onCreate(android.os.Bundle)
		 */
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_vote_history);
			ExitApplication.getInstance().addActivity(this);
			initview();
			LodDialogClass.showCustomCircleProgressDialog(context, "", getString(R.string.common_Loading));
			loadVoteHisInfo();
		}
	
		/**
		 * 方法说明：初始化控件
		 *
		 */
		private void initview() {
			// TODO Auto-generated method stub
			context = this;
			vote_history_back_image = (ImageButton) findViewById(R.id.vote_history_back_image);
			vote_history_back_image.setOnClickListener(this);
			listview_vote_history = (ListView) findViewById(R.id.listview_vote_history);
			share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
			teachershare = getSharedPreferences("teacherinfo", Context.MODE_PRIVATE);
		}
		
		/**
		 * 方法说明：历史投票的记录
		 *
		 */
		private void loadVoteHisInfo() {
			// TODO Auto-generated method stub
			//			[Args]：
			//			 activeClassId=[互动课堂ID，不可为空]
			//			[Return]：
			//			 Infomation = 提示信息;
			//			 Result = 返回结果true/false;
			//			 Data = {
			//			  hisVoteSize = 数据大小，如果为0则没有，大于0表示有数据,
			//			  hisVotes = 历史投票信息[{,
			//			   ID = 投票ID,voteId,
			//			   ActiveClassID = 互动课堂ID,
			//			   Subject = 投票描述,
			//			   opts = 投票选项统计[{,
			//			    OptionNum = 投票选项号,
			//			    OptionDesc = 投票选项描述,
			//			    voteNum = 投票率,
			//						 ownVote = 是否是自己投的票，1是0不是,
			//			    finishVote = 投票是否结束,true是，false否,
			//			   }]
			//			  }]
			//			 }
			mlist = new ArrayList<VotehisVotes>();
			activeClassId = share.getString("activeClassId", "");
			RequestParams params = new RequestParams();
			params.addHeader("Authentication", share.getString("Token", ""));
			params.addBodyParameter("activeClassId", activeClassId);
			System.out.println("activeClassId==="+activeClassId);
			HttpUtils util = new HttpUtils();
			path = Constants.URL_loadVoteHisInfo;
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
					System.out.println("历史投票----"+result);
					try {
						JSONObject obj = new JSONObject(result);
						JSONObject data = obj.getJSONObject("Data");
						JSONArray array = data.getJSONArray("hisVotes");
					System.out.println("ARRAY========"+array);
						for (int i = 0; i < array.length(); i++) {
							JSONObject voteResult = array.getJSONObject(i);
							String ID = voteResult.getString("ID");
							String Subject = voteResult.getString("Subject");
							String ActiveClassID = voteResult.getString("ActiveClassID");
							JSONArray array2 = voteResult.getJSONArray("opts");
							System.out.println("ARRAY22========"+array2);
							if(array2 == null||array2.equals("null")||array2.equals(null)){
								System.out.println("str3==studentChooseList==为空！");
							}else{
								
								mlistopts = new ArrayList<Voteopts>();
								for (int j = 0; j < array2.length(); j++) {
									JSONObject opts = array2.getJSONObject(j);
									String OptionNum = opts.getString("OptionNum");
									String OptionDesc = opts.getString("OptionDesc");
									String voteNum = opts.getString("voteNum");
									String ownVote = opts.getString("ownVote");
									opteinfo  = new Voteopts(OptionNum, OptionDesc,voteNum,ownVote);
									mlistopts.add(opteinfo);
								}
								System.out.println("mlistopts==="+mlistopts);
								VotehisVotes hisvoteinfo = new VotehisVotes(ID, ActiveClassID, Subject, mlistopts);
								mlist.add(hisvoteinfo);
							}
						System.out.println("mlist==="+mlist);
								
							}
						madapter = new HistoryVoteAdapter(context, mlist);
						listview_vote_history.setAdapter(madapter);
						//关闭dialog
						LodDialogClass.closeCustomCircleProgressDialog();
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
		}
	

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		
		
		
		
		
		//返回
		case R.id.vote_history_back_image:
			finish();
			break;

		default:
			break;
		}
	}
	

}
