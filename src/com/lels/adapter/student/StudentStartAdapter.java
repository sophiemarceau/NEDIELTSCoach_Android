package com.lels.adapter.student;

import java.util.HashMap;
import java.util.List;

import com.example.hello.R;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.lels.constants.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class StudentStartAdapter extends BaseAdapter {
	private Context context;
	private List<HashMap<String, Object>> mlist;
	private SwipeListView mSwipeListView;

	private SharedPreferences share,shareclass_ing;
	
	private String ccId, paperId;

	public StudentStartAdapter(Context context,
			List<HashMap<String, Object>> mlist, SwipeListView mSwipeListView) {
		super();
		this.context = context;
		this.mlist = mlist;
		this.mSwipeListView = mSwipeListView;
		shareclass_ing  =context.getSharedPreferences("clcode", context.MODE_PRIVATE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return mlist.isEmpty() ? 0 : mlist.size();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return mlist.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Asdapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc) []
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		share = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_stu_start, null);
			holder = new ViewHolder();
			holder.btn_delete = (Button) convertView
					.findViewById(R.id.btn_delete_stu_start_item);
			holder.txt_testname = (TextView) convertView
					.findViewById(R.id.txt_name_stu_start_item);
			holder.txt_testnum = (TextView) convertView
					.findViewById(R.id.txt_num_stu_start_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		/*
		 * [Return]：  Infomation = 提示信息;  Result = 返回结果true/false;  Data =
		 * 练习试卷列表[   单个试卷信息{    P_ID = 试卷ID,    Name = 试卷名称,    PaperNumber =
		 * 试卷编号,    QuestionCount = 试题数量,    ActiveClassPaperInfoId = 课堂主键ID   }
		 *  ]
		 */

		holder.txt_testname.setText(mlist.get(position).get("Name").toString());
		holder.txt_testnum.setText((position+1)+"");
		String QuestionCount = mlist.get(position).get("QuestionCount")
				.toString();
		/*int question = Integer.parseInt(QuestionCount);
		if (question > 0) {
			holder.num_total.setText("( " + question + " )");
		}*/

		holder.btn_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mlist.get(position).get("PushStatus").toString().equals("0")) {
					mSwipeListView.closeAnimate(position);
					mSwipeListView.dismiss(position);
					deletetest(position);
					notifyDataSetChanged();
				}else{
					Toast.makeText(context, "试卷已使用，不可删除", 0).show();
				}
			}
		});
		return convertView;
	}

	/**
	 * 方法说明：删除试题
	 *
	 */
	private void deletetest(int position) {
		ccId = shareclass_ing.getString("nowLessonId", "");
		paperId = mlist.get(position).get("P_ID").toString();
		RequestParams params = new RequestParams();
		params.addHeader("Authentication",share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);
		utils.send(HttpMethod.GET, Constants.URL_ActiveClassExerciseDelete
				+ "?ccId=" + ccId + "&paperId=" + paperId, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						System.out.println("========失败=======");

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String result = arg0.result;
						System.out.println("deleteresult====================="
								+ result);

					}
				});
	}

	class ViewHolder {
		Button btn_delete;
		TextView txt_testname, txt_testnum, num_total;
	}

}