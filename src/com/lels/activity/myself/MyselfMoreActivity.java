package com.lels.activity.myself;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.hello.R;
import com.lels.activity.myself.adapter.MycollectAdapter;
import com.lels.activity.myself.adapter.MymessageAdapter;

/**
 * 我的收藏
 * **/
public class MyselfMoreActivity extends Activity implements OnClickListener {

	private ImageButton imageview_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_more);

		init();
		
	}

	private void init() {

		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		imageview_back.setOnClickListener(this);
		
		
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;

		default:
			break;
		}
	}

}
