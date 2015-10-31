package com.weather.app.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.weather.app.R;
import com.weather.app.util.LogUtil;
import com.weather.app.util.MyApplication;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.List;

/**
 * Created by zengpu on 15/11/21.
 */
public class HourlyForecastChartView {

//    final Typeface tf = Typeface.createFromAsset(MyApplication.getContext().getAssets(),
//            "fonts/NotoSansCJKsc-Thin.otf");

    public GraphicalView getChartGraphicalView(Context context,
                                               Typeface tf,
                                               String[] titles,
                                               String[] mStrDates,
                                               List<double[]> xValues,
                                               List<double[]> yValues) {

        return ChartFactory.getCubeLineChartView(context, getDataSet(titles, xValues, yValues),
                getRenderer(titles, mStrDates, yValues,tf), 0.3f);
    }

    public XYMultipleSeriesDataset getDataSet(String[] titles,
                                              List<double[]> xValues,
                                              List<double[]> yValues) {

        XYMultipleSeriesDataset dataset= new XYMultipleSeriesDataset();

        for (int i = 0; i < titles.length; i++){
            TimeSeries timeSeries = new TimeSeries(titles[i]);
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);

            for (int k = 0; k < xV.length; k++) {
                timeSeries.add(xV[k],yV[k]);
            }

            dataset.addSeries(timeSeries);
        }
        return dataset;
    }

    public XYMultipleSeriesRenderer getRenderer(String[] titles,
                                                String[] mStrDates,
                                                List<double[]> yValues,
                                                Typeface tf){

        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
//        mRenderer.setXTitle("day");//设置为X轴的标题
//        mRenderer.setYTitle("temp");//设置y轴的标题
//        mRenderer.setAxisTitleTextSize(20);//设置轴标题文本大小
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.TRANSPARENT);
        mRenderer.setPanEnabled(false);//图表移动
        mRenderer.setShowLegend(false);//图例
        mRenderer.setAxesColor(Color.TRANSPARENT);
//        mRenderer.setYLabelsPadding(10);
//        mRenderer.setChartTitle("价格走势图");//设置图表标题
//        mRenderer.setChartTitleTextSize(30);//设置图表标题文字的大小
        mRenderer.setLabelsTextSize(35);//设置标签的文字大小
//        mRenderer.setLegendTextSize(20);//设置图例文本大小
//        mRenderer.setPointSize(10f);//设置点的大小
        mRenderer.setYAxisMin(getMin(yValues.get(0)));//设置y轴最小值是0

        mRenderer.setYAxisMax(getMax(yValues.get(0)));
//        mRenderer.setYAxisMax(getMax(yValues.get(0)) + 1);
        mRenderer.setYLabels(0);//设置Y轴刻度个数（貌似不太准确）
        mRenderer.setXAxisMax(7.5);
        mRenderer.setXAxisMin(0.5);
        mRenderer.setShowAxes(true);
//        mRenderer.setShowGrid(true);//显示网格

        LogUtil.i("TAG", "temp is " + yValues.get(0).toString());

        //将x标签栏目显示如：1,2,3,4替换为显示1月，2月，3月，4月
        mRenderer.addXTextLabel(1, mStrDates[0]);
        mRenderer.addXTextLabel(2, mStrDates[1]);
        mRenderer.addXTextLabel(3, mStrDates[2]);
        mRenderer.addXTextLabel(4, mStrDates[3]);
        mRenderer.addXTextLabel(5, mStrDates[4]);
        mRenderer.addXTextLabel(6, mStrDates[5]);
        mRenderer.addXTextLabel(7, mStrDates[6]);
//        mRenderer.addXTextLabel(6, transformDate(mStrDates[5]));
//        mRenderer.addXTextLabel(7, transformDate(mStrDates[6]));
        mRenderer.setXLabels(0);//设置只显示如1月，2月等替换后的东西，不显示1,2,3等
        mRenderer.setXLabelsColor(MyApplication.getContext().getResources()
                .getColor(R.color.chart_line_color));
        mRenderer.setShowCustomTextGrid(false);
        mRenderer.setShowLabels(false);


//        mRenderer.setMargins(new int[]{5, 5, 5, 5});//设置视图位置
        mRenderer.setMargins(new int[]{0,0,0,-10});//设置视图位置
        mRenderer.setMarginsColor(Color.TRANSPARENT);
//        mRenderer.removeYTextLabel(10);
        mRenderer.clearYTextLabels();
//        mRenderer.clearXTextLabels();
//        mRenderer.setShowGridY(true);
//        mRenderer.setYLabelsPadding(80);
        mRenderer.setTextTypeface(tf);
//        mRenderer.setRange(new double[]{0d, 0d, 100d, 100d});
        mRenderer.setFitLegend(true);
//        mRenderer.setLabelFormat();


        int i;
        for (i = 0; i < titles.length; i++) {


            XYSeriesRenderer xySeriesRenderer = new XYSeriesRenderer();

            xySeriesRenderer.setColor(MyApplication.getContext().getResources()
                    .getColor(R.color.chart_line_color));//设置颜色
            xySeriesRenderer.setPointStyle(PointStyle.POINT);//设置点的样式
            xySeriesRenderer.setPointStrokeWidth(20);
            xySeriesRenderer.setFillPoints(true);//填充点（显示的点是空心还是实心）
            xySeriesRenderer.setDisplayChartValues(true);//将点的值显示出来
            xySeriesRenderer.setChartValuesSpacing(1);//显示的点的值与图的距离
            xySeriesRenderer.setChartValuesTextSize(35);//点的值的文字大小
            xySeriesRenderer.setLineWidth(0.05f);//设置线宽
//            xySeriesRenderer.setChartValuesFormat();
//            NumberFormat  numberFormat = "q";
//            xySeriesRenderer.setGradientEnabled(true);
//            xySeriesRenderer.setGradientStart(0.1, 765432);
//            xySeriesRenderer.setGradientStop(0.8,123456);
//            xySeriesRenderer.setDisplayBoundingPoints(false);
//            XYSeriesRenderer.FillOutsideLine fillOutsideLine = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.b);
//            xySeriesRenderer.addFillOutsideLine(fillOutsideLine);
            xySeriesRenderer.setFillBelowLine(true);
            xySeriesRenderer.setFillBelowLineColor(MyApplication.getContext().getResources()
                    .getColor(R.color.hight_temp_area_color));
//
            mRenderer.addSeriesRenderer(xySeriesRenderer);
        }
        return mRenderer;
    }

    public static double getMax(double[] array) {
        double Max = array[0];
        for (int i = 1; i < array.length-1; i++) {
            if (Max < array[i]) {
                Max = array[i];
            }
        }
//        if (Math.abs(Max) < 5) {
//            return Max + Math.abs(Max * 0.4);
//        } else {
//            return Max + Math.abs(Max * 0.5);
//        }
        return Max + 2;
    }

    public static double getMin(double[] array) {
        double Min = array[0];
        for (int i = 1; i < array.length-1; i++) {
            if (Min > array[i]) {
                Min = array[i];
            }
        }
        return Min - Math.abs(Min*0.02);
    }

}
