package com.xiachen.fatter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.activeandroid.query.Select;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xiachen on 15/12/18.
 */
public class StatisticsActivity extends AppCompatActivity {

    private LineChart chart;
    private LineData mData;
    private float average;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (displayMetrics.heightPixels * 0.6);
        p.width = (int) (displayMetrics.widthPixels * 1.0);
        getWindow().setAttributes(p);

        chart = (LineChart) findViewById(R.id.linechart);
        setupChart();

    }

    private void setupChart() {
        mData = getData();

        LimitLine ll1 = new LimitLine(average, "平均值：" + average);
        ll1.setLineWidth(2f);
        ll1.enableDashedLine(20f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll1.setTextSize(10f);
        ll1.setTextColor(Color.RED);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll1);
        leftAxis.setAxisMaxValue(70f);
        leftAxis.setAxisMinValue(40f);
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawLimitLinesBehindData(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelRotationAngle(20f);
        xAxis.setSpaceBetweenLabels(3);

        chart.getAxisRight().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);

        chart.setDescription("");

        chart.setNoDataTextDescription("没有数据，请坚持每天记录哦");
        chart.setDrawGridBackground(false);
        chart.setData(mData);

        Legend l = chart.getLegend();

        l.setForm(Legend.LegendForm.CIRCLE);
        l.setFormSize(6f);
        l.setTextColor(Color.RED);

        chart.animateX(2500, Easing.EasingOption.EaseInOutQuart);


    }

    private LineData getData() {

        List<Bean> datas = new Select().from(Bean.class).orderBy("Date DESC").limit(20).execute();
        Collections.reverse(datas);
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        float totalWeight = 0;
        for (int i = 0; i < datas.size(); i++) {
            xVals.add(datas.get(i).date.substring(8, 11));
            yVals.add(new Entry(datas.get(i).weight, i));
            totalWeight += datas.get(i).weight;
        }
        average = totalWeight / datas.size();
        Utils.showLog(StatisticsActivity.class, "totalWeight:" + totalWeight + "\ndatas.size():" + datas.size());

        LineDataSet set1 = new LineDataSet(yVals, "近" + datas.size() + "天体重趋势");
        set1.setLineWidth(1.75f);
        set1.setCircleSize(3f);
        set1.setColor(Color.rgb(255, 208, 160));
        set1.setCircleColor(Color.rgb(255, 208, 150));
        set1.setHighLightColor(Color.rgb(255, 208, 150));
        set1.setValueTextSize(12f);

        mData = new LineData(xVals, set1);
        return mData;
    }
}
