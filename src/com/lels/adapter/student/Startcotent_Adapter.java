package com.lels.adapter.student;

import java.util.List;
import com.example.hello.R;
import com.lels.bean.TestReportInfo;
import com.lelts.tool.RoundProgressBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class Startcotent_Adapter extends BaseAdapter {
	private List<TestReportInfo> list;
	private Context context;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	final int TYPE_3 = 2;
	private int progress;
	ViewHolder1 holder1 = null;
	ViewHolder2 holder2 = null;
	ViewHolder3 holder3 = null;
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.isEmpty()?0:list.size();
	}

	@Override
	public int getItemViewType(int position) {
		int p = position % 6;
		if (p == 0) {
			return TYPE_1;
		} else if (p < 3) {
			return TYPE_2;
		} else {
			return TYPE_3;
		}

	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// 判断加载第几个布局的type变量
				int type = getItemViewType(position);

				if (convertView == null) {
					switch (type) {
					case TYPE_1:
						convertView = LayoutInflater.from(context).inflate(
								R.layout.item_test_repor_choose, null);
						holder1 = new ViewHolder1();
						holder1.choose_txt_answer = (TextView) convertView
								.findViewById(R.id.item_txtanswer_test_report_choose);
						holder1.choose_txt_name = (TextView) convertView
								.findViewById(R.id.item_txtname_test_report_choose);
						holder1.choose_pro2 = (RoundProgressBar) convertView
								.findViewById(R.id.roundProgressBar2_choose);
						holder1.choose_pro3 = (RoundProgressBar) convertView
								.findViewById(R.id.roundProgressBar3_choose);
						holder1.btn_A = (Button) convertView
								.findViewById(R.id.btn1_choose);
						holder1.btn_B = (Button) convertView
								.findViewById(R.id.btn2_choose);
						holder1.btn_C = (Button) convertView
								.findViewById(R.id.btn3_choose);
						holder1.btn_D = (Button) convertView
								.findViewById(R.id.btn4_choose);
						
						holder1.txt_true = (TextView) convertView.findViewById(R.id.item_txttrue_test_report_choose);
					
						convertView.setTag(holder1);
						break;
					case TYPE_2:
						convertView = LayoutInflater.from(context).inflate(
								R.layout.item_test_repor_complet, null);
						holder2 = new ViewHolder2();
						holder2.complet_txt_name = (TextView) convertView
								.findViewById(R.id.item_txtname_test_report_complet);
						holder2.complet_txt_answer = (TextView) convertView
								.findViewById(R.id.item_txtanswer_test_report_complet);
						holder2.complet_pro2 = (RoundProgressBar) convertView
								.findViewById(R.id.roundProgressBar2_complet);
						holder2.complet_pro3 = (RoundProgressBar) convertView
								.findViewById(R.id.roundProgressBar3_complet);
						holder2.btn_yes = (Button) convertView.findViewById(R.id.btn1_complet);
						holder2.btn_no = (Button) convertView.findViewById(R.id.btn2_complet);
						
						holder2.txt_true = (TextView) convertView.findViewById(R.id.item_txttrue_test_report_complet);
					
						convertView.setTag(holder2);
						break;

					case TYPE_3:
						convertView = LayoutInflater.from(context).inflate(
								R.layout.item_test_repor_just, null);
						holder3 = new ViewHolder3();
						holder3.just_txt_name = (TextView) convertView
								.findViewById(R.id.item_txtname_test_report_just);
						holder3.just_txt_answer = (TextView) convertView
								.findViewById(R.id.item_txtanswer_test_report_just);
						holder3.just_pro2 = (RoundProgressBar) convertView
								.findViewById(R.id.roundProgressBar2_just);
						holder3.just_pro3 = (RoundProgressBar) convertView
								.findViewById(R.id.roundProgressBar3_just);
						holder3.btn_true = (Button) convertView.findViewById(R.id.btn1_just);
						holder3.btn_false = (Button) convertView.findViewById(R.id.btn3_just);
						holder3.btn_notgiven = (Button) convertView.findViewById(R.id.btn4_just);
						holder3.txt_true = (TextView) convertView.findViewById(R.id.item_txttrue_test_report_just);
						convertView.setTag(holder3);
						break;

					}

				} else {
					switch (type) {
					case TYPE_1:
						holder1 = (ViewHolder1) convertView.getTag();
						break;
					case TYPE_2:
						holder2 = (ViewHolder2) convertView.getTag();
						break;
					case TYPE_3:
						holder3 = (ViewHolder3) convertView.getTag();
						break;
					}
				}

				// 设置属性
				switch (type) {
				case TYPE_1:
					holder1.choose_txt_answer.setText(list.get(position)
							.getTestanswer());
					holder1.choose_txt_name.setText(list.get(position).getTestname());

					new Thread(new Runnable() {

						@Override
						public void run() {
							while (progress < list.get(position).getProgress()) {
								progress += 3;

								System.out.println(progress);

								holder1.choose_pro2.setProgress(progress);
								holder1.choose_pro3.setProgress(progress);

								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}

						}
					}).start();
					break;
				case TYPE_2:

					break;

				case TYPE_3:

					break;

				}

				return convertView;
			}

			class ViewHolder1 {
				TextView choose_txt_name, choose_txt_answer, choose_txt_progress,txt_true;
				Button btn_A, btn_B, btn_C, btn_D;
				RoundProgressBar choose_pro2, choose_pro3;
			}

			class ViewHolder2 {
				TextView complet_txt_name, complet_txt_answer, complet_txt_progress,txt_true;
				Button btn_yes, btn_no;
				RoundProgressBar complet_pro2, complet_pro3;
			}

			class ViewHolder3 {
				TextView just_txt_name, just_txt_answer, just_txt_progress,txt_true;
				Button btn_true,btn_false,btn_notgiven;
				RoundProgressBar just_pro2, just_pro3;
			}
}
