package com.lels.customcontrols;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class Myscrollview extends ListView {

    public Myscrollview(Context context) {
        super(context);
    }

    public Myscrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Myscrollview(Context context, AttributeSet attrs,
        int defStyle) {
        super(context, attrs, defStyle);
    }
        
    @Override
   
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
        MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
