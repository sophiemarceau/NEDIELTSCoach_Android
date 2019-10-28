/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年9月16日 
 * 
 *******************************************************************************/
package com.lelts.tool;

import java.lang.reflect.Field;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年9月16日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class CalculateListviewGrideview {

	/**
	 * 
	 * 方法说明：计算listview高度
	 *
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	/**
	 * 
	 * 方法说明：计算gridview高度
	 *
	 * @param mgrid
	 */
	public static void setGridViewHeightBasedOnChildren(ListView mgrid) {
		// 获取GridView对应的Adapter
		ListAdapter listAdapter = mgrid.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int rows;
		int columns = 0;
		int horizontalBorderHeight = 0;
		Class<?> clazz = mgrid.getClass();
		try {
			// 利用反射，取得每行显示的个数
			Field column = clazz.getDeclaredField("mRequestedNumColumns");
			column.setAccessible(true);
			columns = (Integer) column.get(mgrid);
			// 利用反射，取得横向分割线高度
			Field horizontalSpacing = clazz.getDeclaredField("mRequestedHorizontalSpacing");
			horizontalSpacing.setAccessible(true);
			horizontalBorderHeight = (Integer) horizontalSpacing.get(mgrid);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// 判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
		// if(listAdapter.getCount()%columns>0){
		// rows=listAdapter.getCount()/columns+1;
		// }else {
		// rows=listAdapter.getCount()/columns;
		// }
		rows = listAdapter.getCount() / 1;
		int totalHeight = 0;
		for (int i = 0; i < rows; i++) { // 只计算每项高度*行数
			View listItem = listAdapter.getView(i, null, mgrid);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = mgrid.getLayoutParams();
		params.height = totalHeight + horizontalBorderHeight * (rows - 1);// 最后加上分割线总高度
		mgrid.setLayoutParams(params);
	}
	/**
	 * 
	 * @param 最新测量gridview的高度问题，5列
	 */
	
    public static void setListViewHeightBasedOnChildren1(GridView listView) {  
        // 获取listview的adapter  
           ListAdapter listAdapter = listView.getAdapter();  
           if (listAdapter == null) {  
               return;  
           }  
           // 固定列宽，有多少列  
           int col = 5;// listView.getNumColumns();  
           int totalHeight = 0;  
           // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，  
           // listAdapter.getCount()小于等于8时计算两次高度相加  
           for (int i = 0; i < listAdapter.getCount(); i += col) {  
            // 获取listview的每一个item  
               View listItem = listAdapter.getView(i, null, listView);  
               listItem.measure(0, 0);  
               // 获取item的高度和  
               totalHeight += listItem.getMeasuredHeight();  
           }  
      
           // 获取listview的布局参数  
           ViewGroup.LayoutParams params = listView.getLayoutParams();  
           // 设置高度  
           params.height = totalHeight;  
           // 设置margin  
           ((MarginLayoutParams) params).setMargins(10, 10, 10, 10);  
           // 设置参数  
           listView.setLayoutParams(params);  
       }  
	/**
	 * 
	 * 方法说明：计算gridview高度
	 *
	 * @param mgrid
	 */
	public static void setGridViewHeightBasedOnChildrenx(GridView gridView) {
        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
                return;
        }
        int rows;
        int columns=0;
        int horizontalBorderHeight=0;
        Class<?> clazz=gridView.getClass();
        try {
                //利用反射，取得每行显示的个数
                Field column=clazz.getDeclaredField("mRequestedNumColumns");
                column.setAccessible(true);
                columns=(Integer)column.get(gridView);
                //利用反射，取得横向分割线高度
                Field horizontalSpacing=clazz.getDeclaredField("mRequestedHorizontalSpacing");
                horizontalSpacing.setAccessible(true);
                horizontalBorderHeight=(Integer)horizontalSpacing.get(gridView);
        } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
        }
        //判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
        if(listAdapter.getCount()%columns>0){
                rows=listAdapter.getCount()/columns+1;
        }else {
                rows=listAdapter.getCount()/columns;
        }
        int totalHeight = 0;
        for (int i = 0; i < rows; i++) { //只计算每项高度*行数
                View listItem = listAdapter.getView(i, null, gridView);
                listItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight+horizontalBorderHeight*(rows-1);//最后加上分割线总高度
        gridView.setLayoutParams(params);
}
}
