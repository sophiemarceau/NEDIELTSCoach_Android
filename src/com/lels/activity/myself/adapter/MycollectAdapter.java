package com.lels.activity.myself.adapter;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.constants.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MycollectAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	Context mContext;
	List<HashMap<String, Object>> mlist;

	public MycollectAdapter(Context context, List<HashMap<String, Object>> list) {
		this.mContext = context;
		this.mlist = list;

	}

	@Override
	public int getCount() {

		if (mlist.size() > 0) {
			return mlist.size();
		} else {
			return 0;
		}

	}

	@Override
	public Object getItem(int arg0) {
		if (mlist.size() > 0) {
			return mlist.size();
		} else {
			return 0;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewholer;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_myself_mycollect, null);

			viewholer = new ViewHolder();
			viewholer.textview_message_title = (TextView) convertView
					.findViewById(R.id.textview_message_title);
			viewholer.imageview_mycollect_docx_type = (ImageView) convertView.findViewById(R.id.imageview_mycollect_docx_type);

			convertView.setTag(viewholer);
		} else {

			viewholer = (ViewHolder) convertView.getTag();

		}

		if (mlist.size() > 0) {
			viewholer.textview_message_title.setText(mlist.get(position)
					.get("Name").toString());
			
//			if (mlist.get(position).get("FileType")
//					.toString().equalsIgnoreCase("docx")) {
//				viewholer.imageview_mycollect_docx_type.setImageResource(R.drawable.icon_myself_collect);
//			}else{
//				viewholer.imageview_mycollect_docx_type.setImageResource(R.drawable.icon_myself_collect);
//			}
			if(mlist.get(position).get("StorePoint").toString().equals("1")){
				viewholer.imageview_mycollect_docx_type.setBackgroundResource(R.drawable.icon_myself_collect_doc);
			}else{
				viewholer.imageview_mycollect_docx_type.setBackgroundResource(R.drawable.icon_myself_collect);
			}
			
		}
		return convertView;
	}

	class ViewHolder {
		TextView textview_message_title;
		ImageView imageview_mycollect_docx_type;
	}
	
	private void delectcollect(String token,int postion){
		postion--;

		String url = new Constants().URL_MYSELF_MESSAGE_DELECT;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);
		
		params.addBodyParameter("mrId", mlist.get(postion).get("mrId").toString());

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.GET, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject str = new JSONObject(responseInfo.result);
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("onFailure");

					}

				});

	
	}

}
