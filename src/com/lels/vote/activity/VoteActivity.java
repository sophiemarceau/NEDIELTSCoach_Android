package com.lels.vote.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.github.mikephil.charting.data.LineRadarDataSet;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.group.activity.GroupActivity;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VoteActivity extends Activity implements OnClickListener {

	private Context context;
	// 投票描述
	private EditText edittext_contactus;
	// 确认投票
	private Button vote_btnSure;
	private ImageButton image_back;
	// 增加选项
	private TextView vote_addoption;
	private LinearLayout add_linear;
	// 获取选项的内容
	private LinearLayout choose_one, choose_two, choose_three, choose_four,
			choose_five;
	private EditText txt_one, txt_two, txt_three, txt_four, txt_five;
	// 判断点击的增加的选项
	private int sum = 1;
	private Intent intent;
	private SharedPreferences share, teachershare;
	private TextView vote_txt_history_vote;
	// 参数
	private String activeClassId;
	private String voteDesc;
	private String voteOpt;
	private String path;
	// 拼接字符串
	private String txt_choose_one, txt_choose_two, txt_choose_three,
			txt_choose_four, txt_choose_five;
	private String txt_end;
	// 返回的voteId
	private String voteId;
	// 存储选项的内容
	private List<String> moptvote_list;
	private StringBuilder sb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vote);
		ExitApplication.getInstance().addActivity(this);
		// getIntentValue();
		initview();
		setedtextlistener();

	}

	// 初始化控件
	private void initview() {
		// TODO Auto-generated method stub
		context = VoteActivity.this;
		image_back = (ImageButton) findViewById(R.id.vote_back_image);
		image_back.setOnClickListener(this);
		edittext_contactus = (EditText) findViewById(R.id.edittext_contactus);
		vote_addoption = (TextView) findViewById(R.id.vote_addoption);
		vote_addoption.setOnClickListener(this);
		add_linear = (LinearLayout) findViewById(R.id.add_linear);
		choose_one = (LinearLayout) findViewById(R.id.choose_one);
		choose_two = (LinearLayout) findViewById(R.id.choose_two);
		choose_three = (LinearLayout) findViewById(R.id.choose_three);
		choose_four = (LinearLayout) findViewById(R.id.choose_four);
		choose_five = (LinearLayout) findViewById(R.id.choose_five);
		txt_one = (EditText) findViewById(R.id.txt_one);
		txt_two = (EditText) findViewById(R.id.txt_two);
		txt_three = (EditText) findViewById(R.id.txt_three);
		txt_four = (EditText) findViewById(R.id.txt_four);
		txt_five = (EditText) findViewById(R.id.txt_five);
		choose_one.setOnClickListener(this);
		choose_two.setOnClickListener(this);
		choose_three.setOnClickListener(this);
		choose_four.setOnClickListener(this);
		choose_five.setOnClickListener(this);
		vote_btnSure = (Button) findViewById(R.id.vote_btnSure);
		vote_btnSure.setOnClickListener(this);
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		teachershare = getSharedPreferences("teacherinfo", Context.MODE_PRIVATE);
		activeClassId = share.getString("activeClassId", "");
		vote_txt_history_vote = (TextView) findViewById(R.id.vote_txt_history_vote);
		vote_txt_history_vote.setOnClickListener(this);
	}

	/**
	 * 
	 * Edtext的监听
	 * 
	 */

	private void setedtextlistener() {
		// TODO Auto-generated method stub
		// 投票内容 输入框监听
		edittext_contactus.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String str = s.toString();
				// str = str.replace("%", "");
				if (!str.equals("")) {
					int i = str.length();
					if (i > 200) {
						Toast.makeText(VoteActivity.this, "投票内容不可超过200字符", 0)
								.show();
						edittext_contactus.setText(str.subSequence(0, 200));
						edittext_contactus.setSelection(edittext_contactus
								.getText().length());
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		// 投票选项 输入框监听
		txt_one.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String str = s.toString();
				// str = str.replace("%", "");
				if (!str.equals("")) {
					int i = str.length();
					if (i > 50) {
						Toast.makeText(VoteActivity.this, "选项内容不可超过50字符", 0)
								.show();
						txt_one.setText(str.subSequence(0, 50));
						txt_one.setSelection(txt_one.getText().length());
						return;
					}

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		// 投票选项 输入框监听
		txt_two.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String str = s.toString();
				// str = str.replace("%", "");
				if (!str.equals("")) {
					int i = str.length();
					if (i > 50) {
						Toast.makeText(VoteActivity.this, "选项内容不可超过50字符", 0)
								.show();
						txt_two.setText(str.subSequence(0, 50));
						txt_two.setSelection(txt_two.getText().length());
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		// 投票选项 输入框监听
		txt_three.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String str = s.toString();
				// str = str.replace("%", "");
				if (!str.equals("")) {
					int i = str.length();
					if (i > 50) {
						Toast.makeText(VoteActivity.this, "选项内容不可超过50字符", 0)
								.show();
						txt_three.setText(str.subSequence(0, 50));
						txt_three.setSelection(txt_three.getText().length());
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		// 投票内容 输入框监听
		txt_four.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String str = s.toString();
				// str = str.replace("%", "");
				if (!str.equals("")) {
					int i = str.length();
					if (i > 50) {
						Toast.makeText(VoteActivity.this, "选项内容不可超过50字符", 0)
								.show();
						txt_four.setText(str.subSequence(0, 50));
						txt_four.setSelection(txt_four.getText().length());
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		// 投票内容 输入框监听
		txt_five.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String str = s.toString();
				// str = str.replace("%", "");
				if (!str.equals("")) {
					int i = str.length();
					if (i > 50) {
						Toast.makeText(VoteActivity.this, "选项内容不可超过50字符", 0)
								.show();
						txt_five.setText(str.subSequence(0, 50));
						txt_five.setSelection(txt_five.getText().length());
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * 方法说明：获取投票的信息
	 * 
	 */
	private void getdata() {
		// TODO Auto-generated method stub
		System.out.println("sum======" + sum);
		voteDesc = edittext_contactus.getText().toString();
		txt_choose_one = txt_one.getText().toString();
		txt_choose_two = txt_two.getText().toString();
		txt_choose_three = txt_three.getText().toString();
		txt_choose_four = txt_four.getText().toString();
		txt_choose_five = txt_five.getText().toString();
		if (voteDesc != null && voteDesc.trim().length() != 0) {
			if (txt_choose_one.trim().length() == 0
					|| txt_choose_two.trim().length() == 0) {
				Toast.makeText(context, "选项不能为空！", Toast.LENGTH_SHORT).show();
			} else {
				// if (condition) {
				//
				// }
				LodDialogClass.showCustomCircleProgressDialog(context, null,
						"开始投票中...");
				startVote();
			}
		} else {
			Toast.makeText(context, "内容不能为空！", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 获取voteOpt=[投票选项，规则为选项号+":"+选项内容，多个以";"分割，举例:"1:aa;2:bbb"，不可为空]
	 * 
	 */
	private void getvoteOpt() {
		// TODO Auto-generated method stub
		moptvote_list = new ArrayList<String>();
		moptvote_list.add(txt_choose_one);
		moptvote_list.add(txt_choose_two);
		moptvote_list.add(txt_choose_three);
		moptvote_list.add(txt_choose_four);
		moptvote_list.add(txt_choose_five);
		moptvote_list.removeAll(Collections.singleton(""));
		sb = new StringBuilder();

		for (int i = 0; i < moptvote_list.size(); i++) {

			sb.append((i + 1) + ":" + moptvote_list.get(i) + ";");

		}

		voteOpt = sb.deleteCharAt(sb.length() - 1).toString();

		System.out.println("moptvote_list的值======" + moptvote_list
				+ "======voteOpt====" + voteOpt);

	}

	/**
	 * 方法说明：开始投票
	 * 
	 */
	private void startVote() {

		getvoteOpt();
		// [Args]：
		//  activeClassId=[互动课堂ID，不可为空]
		//  voteDesc=[投票描述，不可为空]
		//  voteOpt=[投票选项，规则为选项号+":"+选项内容，多个以";"分割，举例:"1:aa;2:bbb"，不可为空]

		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("activeClassId", activeClassId);
		params.addBodyParameter("voteDesc", voteDesc);
		params.addBodyParameter("voteOpt", voteOpt);
		System.out.println("activeClassId===" + activeClassId
				+ "=====voteDesc===" + voteDesc + "====voteOpt==" + voteOpt);
		HttpUtils util = new HttpUtils();
		path = Constants.URL_startVote;
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("onFailure");
				Toast.makeText(context, "发起投票失败", 0).show();
				LodDialogClass.closeCustomCircleProgressDialog();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				System.out.println("开始投票----" + result);
				try {
					JSONObject obj = new JSONObject(result);
					String Result = obj.getString("Result");
					String Infomation = obj.getString("Infomation");
					if (Result.equals("true")) {

					} else {
						Toast.makeText(context, Infomation, 0).show();
					}
					JSONObject data = obj.getJSONObject("Data");
					voteId = data.getString("voteId");
					intent = new Intent(context, VotedetailsActivity.class);
					intent.putExtra("sum", sum);
					intent.putExtra("voteDesc", voteDesc);
					intent.putExtra("txt_choose_one", txt_choose_one);
					intent.putExtra("txt_choose_two", txt_choose_two);
					intent.putExtra("txt_choose_three", txt_choose_three);
					intent.putExtra("txt_choose_four", txt_choose_four);
					intent.putExtra("txt_choose_five", txt_choose_five);
					intent.putExtra("voteId", voteId);
					startActivity(intent);
					LodDialogClass.closeCustomCircleProgressDialog();
					finish();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	// 监听
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 查看历史 跳转详情
		case R.id.vote_txt_history_vote:
			intent = new Intent(context, VotehistoryActivity.class);
			startActivity(intent);
			break;

		// 增加选项
		case R.id.vote_addoption:

			if (sum == 1) {
				choose_three.setVisibility(View.VISIBLE);
			} else if (sum == 2) {
				choose_four.setVisibility(View.VISIBLE);
			} else if (sum == 3) {
				choose_five.setVisibility(View.VISIBLE);
				vote_addoption.setVisibility(View.INVISIBLE);
			}
			sum++;
			break;
		// 确定投票 ，并传值
		case R.id.vote_btnSure:

			getdata();

			break;
		// 返回键
		case R.id.vote_back_image:
			finish();
			break;

		default:
			break;
		}
	}
}
