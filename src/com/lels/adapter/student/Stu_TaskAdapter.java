package com.lels.adapter.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hello.R;
import com.lels.bean.Myservice;
import com.lels.constants.Constants;
import com.lelts.tool.ViewHolder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Stu_TaskAdapter extends BaseAdapter {
	private List<HashMap<String, Object>> list;
	private Context ctx;
	private String checkFinish;
	private String checkRemind;
	private ViewHolder holder;
	private List<ViewHolder> holders = new ArrayList<ViewHolder>();
	private Map<Integer, Integer> state;
	private Map<Integer, Boolean> state_click;
	private SharedPreferences share, sharecode;
	private String url = Constants.URL_TeacherClassesremindTask;
	private SharedPreferences sharetag;

	public Stu_TaskAdapter(List<HashMap<String, Object>> list, Context ctx) {
		super();
		this.list = list;
		this.ctx = ctx;
		state = new HashMap<Integer, Integer>();
		state_click = new HashMap<Integer, Boolean>();
		sharetag = ctx.getSharedPreferences("tag", ctx.MODE_PRIVATE);
	}

	@Override
	public int getCount() {
		return list.isEmpty() ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.isEmpty() ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = null;

		if (convertView == null) {
			
			holder = new ViewHolder();
			convertView = LayoutInflater.from(ctx).inflate(
					R.layout.item_stu_task, null);
			holder.taskTitle = (TextView) convertView
					.findViewById(R.id.task_title);
			holder.taskIcon = (ImageView) convertView
					.findViewById(R.id.task_num_icon);
			holder.taskRen = (ImageView) convertView
					.findViewById(R.id.task_renwuliebiao_icon);
			holder.taskUp = convertView.findViewById(R.id.task_line_up);
			holder.taskDown = convertView.findViewById(R.id.task_line_down);
			holder.tasktx = (ImageView) convertView
					.findViewById(R.id.task_tixing);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.taskTitle.setText(list.get(position).get("Name").toString());// 标题名字

		/**
		 * 判断是否完成
		 */

		holder.tasktx.setTag(position);
		holder.tasktx.setOnClickListener(new lvButtonListener());
		checkFinish = list.get(position).get("checkFinish").toString();// 判断是否完成
		checkRemind = list.get(position).get("checkRemind").toString();//判断是否提醒
		int finish = Integer.parseInt(checkFinish);
			if (finish == 0) {
				holder.taskRen.setImageResource(R.drawable.renwuliebiao_hui);
				holder.tasktx.setVisibility(View.VISIBLE);// 右端提醒按钮显示
			} else {
				holder.taskRen.setImageResource(R.drawable.renwuliebiao_hong);
				holder.tasktx.setVisibility(View.INVISIBLE);// 隐藏
			}
			if (state_click.containsKey(position) ||
					"1".equalsIgnoreCase(checkRemind)) {
				// holder.taskRen.setImageResource(R.drawable.renwuliebiao_hui);//
				// 中间按钮灰色
				holder.tasktx.setImageResource(R.drawable.yixing);// 右端提醒按钮隐藏
			} else {
				// holder.taskRen.setImageResource(R.drawable.renwuliebiao_hong);//
				// 中间按钮红色
				holder.tasktx.setImageResource(R.drawable.tixing);
			}
			
			if (!state_click.containsKey(position) &&
					"1".equalsIgnoreCase(checkRemind)) {
				state_click.put(position, true);
			}
			
		/**
		 * 上下线显示
		 */
		if (list.size() == 1) {
			holder.taskUp.setVisibility(View.INVISIBLE);
			holder.taskDown.setVisibility(View.INVISIBLE);
		} else {
			if (position == 0) {
				holder.taskUp.setVisibility(View.INVISIBLE);
				holder.taskDown.setVisibility(View.VISIBLE);
			} else if (position == list.size() - 1) {
				holder.taskDown.setVisibility(View.INVISIBLE);
				holder.taskUp.setVisibility(View.VISIBLE);
			}else{
				holder.taskDown.setVisibility(View.VISIBLE);
				holder.taskUp.setVisibility(View.VISIBLE);
			}
		}
		/**
		 * 判断同类型第一个显示图标
		 */
		String type = list.get(position).get("TaskType").toString();// 类型：1模考、2资料、3练习

		System.out.println("type==========" + type);

		int ty = Integer.parseInt(type);
		switch (ty) {
		case 1:
			holder.taskIcon.setImageResource(R.drawable.mk_gray);
			break;
		case 2:
			holder.taskIcon.setImageResource(R.drawable.lx_gray);
			break;
		case 3:
			holder.taskIcon.setImageResource(R.drawable.zl_gray);
			break;
		case 4:
			holder.taskIcon.setImageResource(R.drawable.cs);
			break;
		default:
			break;
		}
		if (state.containsValue(ty)) {

		} else {
			state.put(position, ty);
		}

		if (state.containsKey(position)) {
			holder.taskIcon.setVisibility(View.VISIBLE);
			if (finish!=0) {
				switch (state.get(position)) {
				case 1:
					holder.taskIcon.setImageResource(R.drawable.mokao);
					break;
				case 2:
					holder.taskIcon.setImageResource(R.drawable.lianxi);
					break;
				case 3:
					holder.taskIcon.setImageResource(R.drawable.ziliao);
					break;
				case 4:
					holder.taskIcon.setImageResource(R.drawable.csred);
					break;
				default:
					break;
				}
				
			}else{
			}
		} else {
			holder.taskIcon.setVisibility(View.INVISIBLE);
		}
		
		
		
		return convertView;
	}

	class lvButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int index = (Integer) v.getTag();
			/*System.out.println(state_click.get(index) + ">>>" + index);
			Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
			t.setToNow(); // 取得系统时间。
			int year = t.year;
			int month = t.month;
			int date = t.monthDay;
			int hour = t.hour; // 0-23
			int minute = t.minute;
			int second = t.second;
			
			System.out.println(year+"-"+month+"-"+date+"-"+hour+"-"+minute+"-"+second);
			*/
			if (state_click.get(index) != null) {
				Toast.makeText(ctx, "今日已提醒", Toast.LENGTH_SHORT).show();
			} else {
				Intent intent = new Intent(ctx, Myservice.class);
				ctx.startService(intent);
				sharetag.edit().putInt("clicktag", index);// 记录点击状态
				sharetag.edit().commit();
				state_click.put(index, true);
				sharecode = ctx
						.getSharedPreferences("clcode", ctx.MODE_PRIVATE);
				String st_id = list.get(index).get("ST_ID").toString();// 任务id
				String classcode = sharecode.getString("id", "");// 班级编号
				String name = list.get(index).get("Name").toString();// 任务名称
				gethttp(classcode, st_id, name);
				
			}

			notifyDataSetChanged();

		}

		private void gethttp(String classcode, String stid, String name) {
			// 实例化share
			share = ctx.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
			RequestParams params = new RequestParams();
			params.addHeader("Authentication", share.getString("Token", ""));
			params.addBodyParameter("sClassID", classcode);
			params.addBodyParameter("message", name);
			params.addBodyParameter("taskID", stid);
			
			System.out.println("參數---"+classcode+"---"+name+"---"+stid);
			
			HttpUtils util = new HttpUtils();
			util.send(HttpMethod.POST, url, params,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							Toast.makeText(ctx, "提醒失败", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							String result = arg0.result;
							System.out.println(result);
							Toast.makeText(ctx, "提醒成功", Toast.LENGTH_SHORT).show();
						}
					});
		}

	}

	class ViewHolder {
		TextView taskTitle;
		ImageView taskIcon, taskRen, tasktx;
		View taskUp, taskDown;
	}

}
