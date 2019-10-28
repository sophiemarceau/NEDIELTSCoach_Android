package com.lels.activity.myself;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hello.R;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.lels.activity.myself.adapter.MyremindTestAdapter;
import com.lels.teacher.db.DbHelper;
import com.lelts.tool.ClockInfo;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;

/**
 * 我的收藏
 * */
public class MyselfRemindActivity extends Activity implements OnClickListener {

	private ImageButton imageview_back;
	private SwipeListView listview_myself_remind;
	private TextView imageview_add_mysetting;

	// private List<HashMap<String, Object>> list;

	private ClockInfo clockInfo = new ClockInfo();
	private DbUtils db;

	private static List<ClockInfo> list_c = new ArrayList<ClockInfo>();
	private static MyremindTestAdapter adapter;

	public static DbHelper dbHelper;

	private static int c_id = 0;
	
	public static int deviceWidth;
	
	private static RelativeLayout relative_warn_nulldata_remind;
	private static RelativeLayout relative_warn_nulldata_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_remind);
		deviceWidth = getDeviceWidth();
		init();

		dbHelper = DbHelper.getInstance(this);
		
		list_c = new ArrayList<ClockInfo>();

		adapter = new MyremindTestAdapter(MyselfRemindActivity.this, list_c,listview_myself_remind);
		listview_myself_remind.setAdapter(adapter);
		
//		listview_myself_remind.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View view, final int position,
//					long arg3) {
//				Button btn_delete = (Button) view.findViewById(R.id.btn_delete_stu_start_item);
//				btn_delete.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						listview_myself_remind.closeAnimate(position);
//						System.out.println("mlist.get(position).getId()====华东删除的id=="
//								+ list_c.get(position).getId());
//						MyselfRemindActivity.delete(String.valueOf(list_c.get(position)
//								.getId()));
//						// deletetest(position);
//						listview_myself_remind.dismiss(position);
//		                select();
//					}
//				});
//			}
//		});
		select();
	}
	
	// 插入数据
	private void add(ClockInfo info) {
		dbHelper.save(info);
		select();
	}

	// 查询
	private static void select() {
		list_c = dbHelper.search(ClockInfo.class);

		// Log.d("TAG", "list_c的长度为======" + String.valueOf(list_c.size()));
		// 首次进入时 判断是否有数据
		if (list_c != null) {
			Collections.sort(list_c, new Comparator<ClockInfo>() {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				@Override
				public int compare(ClockInfo lhs, ClockInfo rhs) {
					long ci2 = 0;
					long ci1 = 0;
					try {
						ci2 = sdf.parse(rhs.getStarttime()).getTime() / 1000;
						ci1 = sdf.parse(lhs.getStarttime()).getTime() / 1000;
						System.out.println("list_sort : " + ci2 + ", " + ci1);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return (int) (ci2 - ci1);
				}
			});
			adapter.setUserEntities(list_c);
			adapter.notifyDataSetChanged();

			if (list_c.size() == 0) {
				relative_warn_nulldata_remind.setVisibility(View.VISIBLE);
				relative_warn_nulldata_list.setVisibility(View.GONE);
			} else {
				relative_warn_nulldata_remind.setVisibility(View.GONE);
				relative_warn_nulldata_list.setVisibility(View.VISIBLE);
			}

			if (list_c.size() > 0) {
				Log.d("TAG", "clock的 id====" + list_c.get(list_c.size() - 1).getId());
				Log.d("TAG", list_c.get(list_c.size() - 1).getNote());
				c_id = list_c.get(list_c.size() - 1).getId();
			}
		} else {
			relative_warn_nulldata_remind.setVisibility(View.VISIBLE);
			relative_warn_nulldata_list.setVisibility(View.GONE);
		}
	}

	// 修改
//	private void update() {
//		ClockInfo entity = (ClockInfo) dbHelper.searchOne(ClockInfo.class, 2);
//		boolean isTrue1 = dbHelper.update(entity, "id");
//		if (isTrue1) {
//			showToast("修改成功");
//		} else {
//			showToast("修改失败");
//		}
//	}

	// 删除 根据条件 id等于一的我就把它删除了
	public static void delete(String clock_id) {
		System.out.println("String.valueOf(info.getId())===="+clock_id);
		boolean isTrue = dbHelper.deleteCriteria(ClockInfo.class, "id", clock_id);
		if (isTrue) {
			Log.d("tag", "删除成功");
		} else {
			Log.d("tag", "删除失败");
		}
		select();
		
	}
	
	public static void delete(ClockInfo cinfo) {
		System.out.println("String.valueOf(info.getId())===="+cinfo.getId());
		boolean isTrue = dbHelper.deleteCriteria(ClockInfo.class, "id", String.valueOf(cinfo.getId()));
		if (isTrue) {
			Log.d("tag", "删除成功");
		} else {
			Log.d("tag", "删除失败");
		}
	}

	private void showToast(String name) {
		Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
	}

	private void init() {
		
		relative_warn_nulldata_remind = (RelativeLayout) findViewById(R.id.relative_warn_nulldata_remind);
		relative_warn_nulldata_list = (RelativeLayout) findViewById(R.id.relative_warn_nulldata_list);

		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		imageview_back.setOnClickListener(this);

		imageview_add_mysetting = (TextView) findViewById(R.id.imageview_add_mysetting);

		listview_myself_remind = (SwipeListView) findViewById(R.id.listview_myself_remind);
		
		listview_myself_remind
				.setSwipeListViewListener(new TestBaseSwipeListViewListener());

		imageview_add_mysetting.setOnClickListener(this);
		
		
		reload();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;
		case R.id.imageview_add_mysetting:
			Intent intent = new Intent();
			Bundle b = new Bundle();
			b.putInt("c_id", c_id);
			
			intent.setClass(MyselfRemindActivity.this,
					MyselfAddRemindActivity.class);
			intent.putExtras(b);
			startActivityForResult(intent, 11);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		if (resultCode == 11) {
			Bundle b = data.getExtras();
			ClockInfo info = new ClockInfo();
			info = (ClockInfo) b.getSerializable("info");
			System.out.println("info.getNote()===" + info.getNote());
			System.out.println("info.getNote()===" + info.getTitle());
			add(info);

		}else if(resultCode == 10){
			select();
		}
	}
	
	private int getDeviceWidth() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	private void reload() {
		listview_myself_remind.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
		listview_myself_remind.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
		// mSwipeListView.setSwipeActionRight(settings.getSwipeActionRight());
		listview_myself_remind.setOffsetLeft((float) (deviceWidth * 1 / 1.3));
		// mSwipeListView.setOffsetRight(convertDpToPixel(settings.getSwipeOffsetRight()));
		listview_myself_remind.setAnimationTime(0);
		listview_myself_remind.setSwipeOpenOnLongPress(false);
	}
	
	class TestBaseSwipeListViewListener extends BaseSwipeListViewListener {

		@Override
		public void onClickFrontView(int position) {

			Intent intent = new Intent();
			intent.setClass(MyselfRemindActivity.this,
					MyselfRemindDeatilActivity.class);
			Bundle b = new Bundle();
			b.putSerializable("delete", list_c.get(position));
			b.putString("c_id", String.valueOf(list_c.get(position).getId()));
//			b.putInt("id_img", 1);
			intent.putExtra("id_img", 1);
			System.out.println("传递的    list_c.get(position).getid===="+list_c.get(position).getId()+ list_c.get(position).getNote());
			intent.putExtras(b);
//			startActivity(intent);
			startActivityForResult(intent, 10);
			
		}

		@Override
		public void onDismiss(int[] reverseSortedPositions) {
//			for (int position : reverseSortedPositions) {
//				list_c.remove(position);
//			}
			adapter.notifyDataSetChanged();
		}
	}

}


