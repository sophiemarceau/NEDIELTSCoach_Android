package com.lels.activity.myself;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hello.R;
import com.lels.activity.myself.adapter.MycollectAdapter;
import com.lels.activity.myself.adapter.MymessageAdapter;

/**
 * 我的收藏
 * **/
public class RemindAlterAddressActivity extends Activity implements OnClickListener {

	private ImageButton imageview_back;
	private EditText edittext_remind_address;
	private Button button_alter_complete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remind_alter_address);

		init();
		
	}

	private void init() {
		button_alter_complete = (Button) findViewById(R.id.button_alter_complete);

		edittext_remind_address = (EditText) findViewById(R.id.edittext_remind_address);
		
		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		imageview_back.setOnClickListener(this);
		button_alter_complete.setOnClickListener(this);
		
		edittext_remind_address.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				temp = s;
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (temp.length() > 25) {
					Toast.makeText(RemindAlterAddressActivity.this, "最多只能输入25个字", Toast.LENGTH_LONG).show();;
					CharSequence cs = s.subSequence(0, 25);
					edittext_remind_address.setText(cs);
					edittext_remind_address.setSelection(25);
				}
			}
		});
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;
		case R.id.button_alter_complete:
			Intent intent = new Intent();
			intent.setClass(RemindAlterAddressActivity.this, MyselfAddRemindActivity.class);
			intent.putExtra("address", edittext_remind_address.getText().toString());
			setResult(11, intent);
			finish();
			break;
		default:
			break;
		}
	}

}
