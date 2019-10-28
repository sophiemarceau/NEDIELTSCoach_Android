package com.lelts.fragment.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.hello.R;
import com.lelts.activity.main.MainActivity;
import com.lelts.fragment.data.adapter.ScreenDateAdapter;
import com.lelts.tool.PrintTool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DataScreenActivity extends Activity implements OnClickListener {

	private static final String TAG = "DataScreenActivity";

	private ImageButton imageview_back;
	private TextView textview_screen_ok;

	private GridView gridview_screen_date;
	private GridView gridview_screen_date_attr;
	private GridView gridview_screen_date_source;
	private GridView gridview_screen_date_time;

	private ScreenDateAdapter adapter, adapter1, adapter2, adapter3;
	private List<HashMap<String, Object>> l_map, l_map1, l_map2, l_map3;

	private int position_format_selected = 0;

	private int position_attr_selected = 0;

	private int position_source_selected = 0;

	private int position_time_selected = 0;

	private String[] str = { "不限", "WORD", "Excel", "PPT", "PDF", "视频" };

	private String[] str1 = { "不限", "口语", "阅读", "写作", "听力" };

	private String[] str2 = { "不限", "集团", "个人" };

	private String[] str3 = { "不限", "2015", "2014", "2013", "2012" };

	private String[] str_code = { "0", "1", "2", "3", "4", "5" };
	private String[] str1_code = { "0", "KY", "YD", "XZ", "TL" };
	private String[] str2_code = { "0", "1", "2" };
	private String[] str3_code = { "0", "2015", "2014", "2013", "2012" };

	private PrintTool print;

	String fileType;
	String nameCode;
	String roleId;
	String uploadYear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_screening);

		print = new PrintTool(DataScreenActivity.this);

		getdatafromintent();

		initview();

		initdata();
		initgridview0();
		initgridview1();
		initgridview2();
		initgridview3();

		initposition();
	}

	private void getdatafromintent() {
		Bundle b = getIntent().getExtras();

		fileType = b.getString("fileType");
		nameCode = b.getString("nameCode");
		roleId = b.getString("roleId");
		uploadYear = b.getString("uploadYear");

		for (int i = 0; i < str_code.length; i++) {
			if (fileType.equalsIgnoreCase(str_code[i])) {
				position_format_selected = i;
			}
		}

		for (int i = 0; i < str1_code.length; i++) {
			if (nameCode.equalsIgnoreCase(str1_code[i])) {
				position_attr_selected = i;
			}
		}

		for (int i = 0; i < str2_code.length; i++) {
			if (roleId.equalsIgnoreCase(str2_code[i])) {
				position_source_selected = i;
			}
		}

		for (int i = 0; i < str3_code.length; i++) {
			if (uploadYear.equalsIgnoreCase(str3_code[i])) {
				position_time_selected = i;
			}
		}

		System.out.println("nameCode==" + nameCode);
		System.out.println("nameCode==" + roleId);
		System.out.println("nameCode==" + uploadYear);

	}

	private void initposition() {
		// adapter 1 2
		// edittext_starttime.

		adapter.updata(position_format_selected);
		adapter1.updata(position_attr_selected);
		adapter2.updata(position_source_selected);
		adapter3.updata(position_time_selected);

	}

	private void initview() {

		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		textview_screen_ok = (TextView) findViewById(R.id.textview_screen_ok);

		gridview_screen_date = (GridView) findViewById(R.id.gridview_screen_date);
		gridview_screen_date_attr = (GridView) findViewById(R.id.gridview_screen_date_attr);
		gridview_screen_date_source = (GridView) findViewById(R.id.gridview_screen_date_source);
		gridview_screen_date_time = (GridView) findViewById(R.id.gridview_screen_date_time);

		imageview_back.setOnClickListener(this);
		textview_screen_ok.setOnClickListener(this);
	}

	private void initdata() {

		l_map = new ArrayList<HashMap<String, Object>>();

		// String[] str = { "不限", "WORD", "excel", "PPT", "PDF", "视频资料" };

		for (int i = 0; i < str.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("screenkey", str[i]);
			l_map.add(map);
		}

		l_map1 = new ArrayList<HashMap<String, Object>>();

		// String[] str1 = { "不限", "口语", "阅读", "听力", "写作" };

		for (int i = 0; i < str1.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("screenkey", str1[i]);
			l_map1.add(map);
		}

		l_map2 = new ArrayList<HashMap<String, Object>>();

		// String[] str2 = { "不限","集团", "个人" };

		for (int i = 0; i < str2.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("screenkey", str2[i]);
			l_map2.add(map);
		}

		l_map3 = new ArrayList<HashMap<String, Object>>();

		// String[] str3 = { "不限","2016", "2015","2014","2013" };

		for (int i = 0; i < str3.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("screenkey", str3[i]);
			l_map3.add(map);
		}

	}

	private void initgridview0() {
		adapter = new ScreenDateAdapter(DataScreenActivity.this, l_map,
				position_format_selected);
		gridview_screen_date.setAdapter(adapter);

		gridview_screen_date.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				position_format_selected = position;
				RelativeLayout relative = (RelativeLayout) view
						.findViewById(R.id.relative_screen_background);
				// relative.setBackground(getResources().getDrawable(
				// R.drawable.icon_screen_selected));
				relative.setBackgroundResource(R.drawable.icon_screen_selected);

				print.printforLog(TAG, "onitemclick");

				adapter = new ScreenDateAdapter(DataScreenActivity.this, l_map,
						position_format_selected);
				gridview_screen_date.setAdapter(adapter);

			}
		});

	}

	private void initgridview1() {
		adapter1 = new ScreenDateAdapter(DataScreenActivity.this, l_map1,
				position_attr_selected);
		gridview_screen_date_attr.setAdapter(adapter1);

		gridview_screen_date_attr
				.setOnItemClickListener(new OnItemClickListener() {

					@SuppressLint("NewApi")
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						position_attr_selected = position;
						RelativeLayout relative = (RelativeLayout) view
								.findViewById(R.id.relative_screen_background);
						// relative.setBackground(getResources().getDrawable(
						// R.drawable.icon_screen_selected));
						relative.setBackgroundResource(R.drawable.icon_screen_selected);

						print.printforLog(TAG, "onitemclick");

						adapter1 = new ScreenDateAdapter(
								DataScreenActivity.this, l_map1,
								position_attr_selected);
						gridview_screen_date_attr.setAdapter(adapter1);
					}
				});

	}

	private void initgridview2() {
		adapter2 = new ScreenDateAdapter(DataScreenActivity.this, l_map2,
				position_source_selected);
		gridview_screen_date_source.setAdapter(adapter2);

		gridview_screen_date_source
				.setOnItemClickListener(new OnItemClickListener() {

					@SuppressLint("NewApi")
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						position_source_selected = position;
						RelativeLayout relative = (RelativeLayout) view
								.findViewById(R.id.relative_screen_background);
						// relative.setBackground(getResources().getDrawable(
						// R.drawable.icon_screen_selected));
						relative.setBackgroundResource(R.drawable.icon_screen_selected);

						print.printforLog(TAG, "onitemclick");

						adapter2 = new ScreenDateAdapter(
								DataScreenActivity.this, l_map2,
								position_source_selected);
						gridview_screen_date_source.setAdapter(adapter2);
					}
				});

	}

	private void initgridview3() {
		adapter3 = new ScreenDateAdapter(DataScreenActivity.this, l_map3,
				position_time_selected);
		gridview_screen_date_time.setAdapter(adapter3);

		gridview_screen_date_time
				.setOnItemClickListener(new OnItemClickListener() {

					@SuppressLint("NewApi")
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						position_time_selected = position;
						RelativeLayout relative = (RelativeLayout) view
								.findViewById(R.id.relative_screen_background);
						// relative.setBackground(getResources().getDrawable(
						// R.drawable.icon_screen_selected));
						relative.setBackgroundResource(R.drawable.icon_screen_selected);

						print.printforLog(TAG, "onitemclick");

						adapter3 = new ScreenDateAdapter(
								DataScreenActivity.this, l_map3,
								position_time_selected);
						gridview_screen_date_time.setAdapter(adapter3);
					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;
		case R.id.textview_screen_ok:
			Intent intent = new Intent();
			intent.setClass(DataScreenActivity.this, MainActivity.class);
			Bundle b = new Bundle();
			// b.putStringArray("fileType", str_code);
			// b.putStringArray("nameCode", str_code);
			// b.putStringArray("roleId", str_code);
			// b.putStringArray("uploadYear", str_code);
			b.putString("fileType", str_code[position_format_selected]);
			b.putString("nameCode", str1_code[position_attr_selected]);
			b.putString("roleId", str2_code[position_source_selected]);
			b.putString("uploadYear", str3_code[position_time_selected]);
			intent.putExtras(b);
			setResult(11, intent);
			finish();
			break;
		default:
			break;
		}
	}

}
