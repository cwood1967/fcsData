package org.stowers.microscopy.fcs;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.LogRecord;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.chart.axis.LogarithmicAxis;

public class TestFcsPlots extends JFrame {

	private XYSeries trajectory;
	// private XYSeries series2;
	XYSeriesCollection dataset;

	public TestFcsPlots(final String title, double[] xdata, double ydata[]) {
		dataset = new XYSeriesCollection();
		addData(title, xdata, ydata);
		// TODO Auto-generated constructor stub
	}

	public void addData(String key, double[] xdata, double ydata[]) {
		XYSeries series = fillseries(key, xdata, ydata);
		dataset.addSeries(series);
	}

	private XYSeries fillseries(String key, double[] xdata, double[] ydata) {
		int num = ydata.length;
		// int num = 5000;

		XYSeries xyseries = new XYSeries(key);
		for (int i = 0; i < num; i++) {
			xyseries.add(xdata[i], ydata[i]);
		}
		return xyseries;

	}

	private JFreeChart createChart(XYSeriesCollection dataset, String title) {

		JFreeChart chart = ChartFactory.createScatterPlot(title, "Time",
				"Intensity", dataset, PlotOrientation.VERTICAL, false, false, false);
		XYPlot plot = (XYPlot) chart.getPlot();
		// plot.setBackgroundPaint(Color.decode("#F1F1F4"));
		plot.setBackgroundPaint(Color.decode("#FFFFFF"));
//		ValueAxis xaxis = plot.getDomainAxis();
		LogarithmicAxis xaxis = new LogarithmicAxis("tau d");
		plot.setDomainAxis(xaxis);
		XYLineAndShapeRenderer rend = new XYLineAndShapeRenderer();
		rend.setSeriesLinesVisible(0, true);
		rend.setSeriesShapesVisible(0, false);
		rend.setSeriesStroke(0, new BasicStroke(1.f));
		rend.setSeriesPaint(0, Color.green);
		plot.setRenderer(rend);
		plot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);

		ValueAxis axis = plot.getDomainAxis();
		axis.setTickMarkInsideLength(7.f);
		axis.setTickMarkOutsideLength(0.f);
		//
		ValueAxis dtop = plot.getDomainAxis();
		plot.setDomainAxis(1, dtop);

		ValueAxis topaxis = plot.getDomainAxis(1);
		plot.setDomainGridlinesVisible(false);
		plot.setRangeGridlinesVisible(false);
		topaxis.setTickMarkInsideLength(7.f);
		topaxis.setTickMarkOutsideLength(0.f);
		topaxis.setTickLabelsVisible(true);

		ValueAxis raxis = plot.getRangeAxis();
		raxis.setTickMarkInsideLength(7.f);
		raxis.setTickMarkOutsideLength(0.f);

		RectangleInsets offset = new RectangleInsets(0, 0, 0, 0);
		plot.setAxisOffset(offset);
		return chart;

	}

	public JFreeChart getChart() {
		String title = "My Chart";
		JFreeChart chart = createChart(dataset, title);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1000, 600));
		setContentPane(chartPanel);
		chart.setAntiAlias(true);

		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		return null; // createChart(dataset, title);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
