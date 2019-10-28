package com.myclassing.frag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.hello.R;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class Class_ing_Frag_Mk extends Fragment {
	private LineChartView chart;
	private LineChartData data;
	private List<HashMap<String, String>> list;
	private boolean hasAxes = true;
	private boolean hasLines = true;
	private boolean hasPoints = true;
	private ValueShape shape = ValueShape.CIRCLE;
	private boolean isFilled = false;// 阴影填充
	private boolean hasLabels = false;
	private boolean isCubic = false;
	private boolean hasLabelForSelected = true;// 点击显示小提示

	// float[] randomNumbersTab = new float[list.size()];

	public Class_ing_Frag_Mk(List<HashMap<String, String>> list) {
		super();
		this.list = list;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_classing_mk, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		chart = (LineChartView) getActivity().findViewById(
				R.id.spread_line_chart);
		chart.setOnValueTouchListener(new ValueTouchListener());

		// generateValues(list);

		generateData();

		chart.setViewportCalculationEnabled(false);

		resetViewport();

	}

	private void resetViewport() {
		final Viewport v = new Viewport(chart.getMaximumViewport());
		System.out.println("chart.getMaximumViewport()--" +chart.getMaximumViewport());
		v.bottom = 0;
		v.top = 10;
		chart.setMaximumViewport(v);
		chart.setCurrentViewport(v, true);
	}
		private void generateData() {

			List<Line> lines = new ArrayList<Line>();

			List<PointValue> values = new ArrayList<PointValue>();
			List<AxisValue> mAxisValues = new ArrayList<AxisValue>();
			for (int j = 0; j < list.size(); ++j) {
				values.add(new PointValue(j, Integer.valueOf(list.get(j).get(
						"TotalScore"))));
				System.out.println("j--" + j + "--TotalScore--"
						+ Integer.valueOf(list.get(j).get("TotalScore")));
				String lessonNo = list.get(j).get("nLessonNo");
				if ("null".equals(lessonNo) || TextUtils.isEmpty(lessonNo)) { 
					lessonNo = "0";
				}
				System.out.println("lesson No. : " + lessonNo);
				AxisValue axisValue = new AxisValue(j + 1);
				//axisValue.setValue(i + 1);
				char[] labels = lessonNo.toCharArray();
				axisValue.setLabel(labels);
				mAxisValues.add(axisValue);
			}
			// 坐标轴
			Axis axisX = new Axis(); // X轴
			axisX.setTextColor(Color.BLACK);
//			axisX.setMaxLabelChars(10);
			axisX.setValues(mAxisValues);
			
			Line line = new Line(values);
			line.setColor(Color.RED);
			line.setShape(shape);
			line.setCubic(isCubic);
			line.setFilled(isFilled);
			line.setHasLabels(hasLabels);
			line.setHasLabelsOnlyForSelected(hasLabelForSelected);
			line.setHasLines(hasLines);
			line.setHasPoints(hasPoints);
			lines.add(line);

			data = new LineChartData(lines);

			if (hasAxes) {
				Axis axisY = new Axis().setHasLines(true);
				data.setAxisXBottom(axisX);
				data.setAxisYLeft(axisY);
			} else {
				data.setAxisXBottom(null);
				data.setAxisYLeft(null);
			}
			data.setBaseValue(Float.NEGATIVE_INFINITY);
			chart.setLineChartData(data);

	}

	private class ValueTouchListener implements
			LineChartView.LineChartOnValueTouchListener {

		@Override
		public void onValueTouched(int selectedLine, int selectedValue,
				PointValue value) {
			/*
			 * Toast.makeText(getActivity(), "课次：" + value.getX() + "总成绩：" +
			 * value.getY(), Toast.LENGTH_SHORT).show();
			 */
		}

		@Override
		public void onNothingTouched() {

		}

	}
}
