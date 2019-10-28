package com.drocode.swithcer;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import com.example.hello.R;
import com.lelts.fragment.data.DataDFm;
import com.lidroid.xutils.BitmapUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	private List<String> imageUrls; // 图片地址list
	private Context context;
	private ImageAdapter self;
	Uri uri;
	Intent intent;
	ImageView imageView;
	DataDFm df;
	
	public static String[] myuri;

//	public static String[] myuri = {
//			"http://www.apkbus.com/template/devc/style/logo.png",
//			"http://www.apkbus.com/template/devc/style/logo.png",
//			"http://www.apkbus.com/template/devc/style/logo.png",
//			"http://www.apkbus.com/template/devc/style/logo.png" };

	private BitmapUtils bitmaputils;

	public ImageAdapter(Context context, DataDFm dft, String[] str_image) {
		// this.imageUrls = imageUrls;
		this.context = context;
		this.self = this;
		bitmaputils = new BitmapUtils(context);
		this.df = dft;
		myuri = str_image;
	}

	public int getCount() {
		return Integer.MAX_VALUE;
	}

	public Object getItem(int position) {
		return imageUrls.get(position % myuri.length);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case 0: {
					self.notifyDataSetChanged();
				}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
			}
		}
	};

	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {

		// Bitmap image;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item,
					null); // 实例化convertView
			Gallery.LayoutParams params = new Gallery.LayoutParams(
					Gallery.LayoutParams.WRAP_CONTENT,
					Gallery.LayoutParams.WRAP_CONTENT);
			convertView.setLayoutParams(params);

			convertView.setTag(myuri);

		} else {
			convertView = (View) convertView.getTag();
		}
		
		imageView = (ImageView) convertView.findViewById(R.id.gallery_image);

		position = position % myuri.length;

		bitmaputils.display(imageView, myuri[position]);
		// 设置缩放比例：保持原样
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);

		df.changePointView(position % myuri.length);
		
		return convertView;
	}

}
