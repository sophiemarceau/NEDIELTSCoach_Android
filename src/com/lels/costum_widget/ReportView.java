package com.lels.costum_widget;

import com.example.hello.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReportView extends LinearLayout {

	private final String[] chars = {"A","B","C","D","E","F","G","H","I","J","K","L","M","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	private Context context;
	public ReportView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		//LayoutInflater.from(context).inflate(R.layout.num_line_char, this);
	}
	
	public void fit(int array) {
		this.removeAllViews();
		this.listener = null;
		System.out.println("to fit size : " + array);
		for (int i = 0; i < array; i++) {
			LayoutParams lp = new LayoutParams(75, LayoutParams.MATCH_PARENT);
			View child = LayoutInflater.from(context).inflate(R.layout.num_line_char, null);
			TextView view = (TextView)child.findViewById(R.id.nl_index);
			view.setText(chars[i]);
			if (i == array - 1) {
				TextView content = (TextView)child.findViewById(R.id.nl_content);
				RelativeLayout.LayoutParams rl = (android.widget.RelativeLayout.LayoutParams) content.getLayoutParams();
				rl.rightMargin = (int) this.context.getResources().getDimension(R.dimen.ten_dp);
				content.setLayoutParams(rl);
				lp.width = 90;
			}
			this.addView(child, lp);
			final int index = i;
			child.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (null != listener) {
						listener.onClick(v, index);
					}
				}
			});
		}
	}
	
	private MyOnclick listener;
	public void setOnClick(MyOnclick listener) {
		this.listener = listener;
	}
	
	public interface MyOnclick {
		void onClick(View v, int position);
	}
	
	public void setHeight(int index, int height) {
		TextView view = (TextView) this.getChildAt(index).findViewById(R.id.nl_content);
		view.setHeight(height);
	}
	
	public void setText(int index, String text) {
		TextView view = (TextView) this.getChildAt(index).findViewById(R.id.nl_content);
		view.setText(text);
	}
	
	public void setIndex(int index, String text) {
		TextView view = (TextView) this.getChildAt(index).findViewById(R.id.nl_index);
		view.setText(text);
	}
	
	public void setBackground(int index, int resid) {
		TextView view = (TextView) this.getChildAt(index).findViewById(R.id.nl_content);
		view.setBackgroundResource(resid);
	}

}
