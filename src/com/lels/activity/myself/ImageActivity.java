package com.lels.activity.myself;

import com.example.hello.R;
import com.lels.constants.Constants;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ImageActivity extends Activity{
	private Context mContext;
	private ImageView image_context;
	private ImageButton image_cj_back;
	private String image;
	private String imagepath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		
		initview();
		putImage();
	}
	private void initview() {
		mContext = this;
		image_cj_back = (ImageButton) findViewById(R.id.imageview_cj_back);
		image_cj_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		image_context = (ImageView) findViewById(R.id.imageView_context);
		
		Intent intent = getIntent();
		image = intent.getStringExtra("image");
		imagepath = Constants.URL_TeacherIMG+image;
	}
	private void putImage() {
		BitmapUtils bit = new BitmapUtils(mContext);
		bit.display(image_context, imagepath);
	}

}
  