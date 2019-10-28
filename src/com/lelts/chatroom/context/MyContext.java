package com.lelts.chatroom.context;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyContext {
	
	private static MyContext cxt;
	private SharedPreferences mPreferences;
//	public static final String USER_NAME = "sl.hlzy@163.com";
//	public static final String DEFAULT_STRING = "";
//	public static final int DEFAULT_INT = -1;
//	public static final String PWD = "123456";
	public static final String TOKEN = "a48DBnK4COPXGq0r4CudcSlGchA97H82yK7Wz7hn34GVf4MFTa/Dlc7Bg9Wq7mlPP503O3e+OCqwRhrtQxq4oA==";
	public static final String TOKEN1 = "RCd1Q43QBe6EALzL6z8wtjUGuK45/WkC5T2L/prinN6GqbZLhqfMvrhjI23MTUAdvxavf7LdXIE=";
	public static MyContext getInstance() {
		if (null == cxt) {
			cxt = new MyContext();
		}
		return cxt;
	}
	
	public void init(Context context) {
		mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public SharedPreferences getSharedPreferences() {
        return mPreferences;
    }

}
 
