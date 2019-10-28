package com.lelts.tool;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class GridViewForGridview extends GridView {
	public GridViewForGridview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public GridViewForGridview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public GridViewForGridview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
