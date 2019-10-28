package com.lels.adapter.classesfm_details;


import java.util.HashMap;
import java.util.List;

import com.example.hello.R;
import com.lels.constants.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class Class_ing_Adapter extends BaseAdapter {
	private Context mContext;
	private List<HashMap<String,Object>> list ;
    private String iocpath = Constants.URL_TeacherIMG;
  
	public Class_ing_Adapter(Context mContext,
			List<HashMap<String, Object>> list) {
		super();
		this.mContext = mContext;
		this.list = list;
	}


	public List<HashMap<String, Object>> getList() {
		return list;
	}


	public void setList(List<HashMap<String, Object>> list) {
		this.list = list;
	}
  
    public int getCount() {  
        return list.size();  
    }  
  
    public Object getItem(int position) {  
        return list.get(position);  
    }  
  
    public long getItemId(int position) {  
        return 0;  
    }  
  
    // create a new ImageView for each item referenced by the Adapter  
    public View getView(int position, View convertView, ViewGroup parent) {  
        ViewHolder holder=null; 
        if (convertView == null) {  // if it's not recycled, initialize some attributes  
            holder=new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_classes_teachers, null);
        	holder.teachername=(TextView) convertView.findViewById(R.id.classes_teacher_name);
        	holder.icoImageView=(ImageView) convertView.findViewById(R.id.classes_teacher_photo);
        	holder.icoImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);  
//        	holder.icoImageView.setPadding(20, 10, 0, 0);
        	convertView.setTag(holder);
        } else { 
        	holder=(ViewHolder) convertView.getTag();
        }  
        	holder.teachername.setText(list.get(position).get("sName").toString());
        	String path= list.get(position).get("IconUrl").toString();
        	if(path.equals("null")||path.equals("")){
        		
        	}else{
        		
        		String p = path;
        		ImageLoader.getInstance().displayImage(p, holder.icoImageView);
        	}
        	return convertView;  
    }  
    class ViewHolder{
    	private TextView teachername,teacherfunction;
    	private ImageView icoImageView;
    }
}
