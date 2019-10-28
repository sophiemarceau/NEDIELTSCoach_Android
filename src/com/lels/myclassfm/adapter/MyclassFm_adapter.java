package com.lels.myclassfm.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.hello.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyclassFm_adapter extends BaseAdapter {
	private int TYPE;
	private List<HashMap<String, Object>> mList;
	private Context mContext;
	public MyclassFm_adapter(List<HashMap<String, Object>> mList, Context mContext, int TYPE) {
		super();
		this.mList = mList;
		this.mContext = mContext;
		this.TYPE = TYPE;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.isEmpty() ? 0 : mList.size();
	}




	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_viewpager_classfm, null);
			holder.mAverageNum=(TextView) convertView.findViewById(R.id.classfm_averageNum);
			holder.mClassnum=(TextView) convertView.findViewById(R.id.classfm_classnum);
			holder.mstartTimeQuantum=(TextView) convertView.findViewById(R.id.classfm_starttimeQuantum);
			holder.mTotleNum=(TextView) convertView.findViewById(R.id.classfm_totleNum);
			holder.mendTimeQuantum=(TextView) convertView.findViewById(R.id.classfm_endtimeQuantum);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
			holder.mClassnum.setText(mList.get(position).get("sCode").toString());			

			//根据传过来的TYPE值来判定颜色的选取；
			switch (TYPE) {
			case 0:
				holder.mClassnum.setTextColor(Color.RED);
				break;
			case 1:
				holder.mClassnum.setTextColor(Color.GRAY);
				holder.mClassnum.setAlpha(0.6f);
				break;
			case 2:
				holder.mClassnum.setAlpha(0.3f);
				break;
			}
			holder.mAverageNum.setText(mList.get(position).get("sName").toString());
			holder.mstartTimeQuantum.setText(mList.get(position).get("dtBeginDate").toString());
			holder.mTotleNum.setText(mList.get(position).get("stuNum").toString()+"人班");
			holder.mendTimeQuantum.setText(mList.get(position).get("dtEndDate").toString());
		return convertView;
	}
	class ViewHolder{
		TextView mClassnum,mAverageNum,mstartTimeQuantum,mendTimeQuantum,mTotleNum;
	} 
}
