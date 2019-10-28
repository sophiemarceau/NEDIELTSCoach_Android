package com.myclassing.frag;

import java.util.List;

import com.example.hello.R;
import com.lels.constants.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Class_ing_Frag_Cs extends Fragment implements OnClickListener {

	private List<Integer> list;
	private TextView total, listener, write, kybtn, read;
	private RelativeLayout relative;

	public Class_ing_Frag_Cs(List<Integer> list) {
		super();
		this.list = list;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.class_ing_scode, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		intview();
	}

	private void intview() {
		
		WindowManager wm = getActivity().getWindowManager();
		int screen_heigth = wm.getDefaultDisplay().getHeight();
		
		total = (TextView) getActivity().findViewById(R.id.btn_total);
		listener = (TextView) getActivity().findViewById(R.id.btn_listener);
		write = (TextView) getActivity().findViewById(R.id.btn_write);
		kybtn = (TextView) getActivity().findViewById(R.id.btn_ky);
		read = (TextView) getActivity().findViewById(R.id.btn_read);
        
		total.setHeight((list.get(0))*screen_heigth/50);
		listener.setHeight((list.get(1))*screen_heigth/50);
		write.setHeight((list.get(2))*screen_heigth/50);
		kybtn.setHeight((list.get(3))*screen_heigth/50);
		read.setHeight((list.get(4))*screen_heigth/50); 

		total.setOnClickListener(this);
		listener.setOnClickListener(this);
		write.setOnClickListener(this);
		kybtn.setOnClickListener(this);
		read.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_total:

			break;
		case R.id.btn_listener:

			break;
		case R.id.btn_write:

			break;
		case R.id.btn_ky:

			break;
		case R.id.btn_read:

			break;
		}
	}

}
