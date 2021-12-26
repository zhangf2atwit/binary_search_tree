package edu.wit.cs.comp2350.tests;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import edu.wit.cs.comp2350.BST;
import edu.wit.cs.comp2350.DiskLocation;
import edu.wit.cs.comp2350.LinkedList;
import edu.wit.cs.comp2350.LocationContainer;


public class ChartMaker extends ApplicationFrame {
	private static final long serialVersionUID = 1L;
	private static final int DATASETS = 2;

	/**
	 *
	 * @param title  the frame title.
	 */
	public ChartMaker(String title) {
		super(title);
		JPanel chartPanel = createDemoPanel();
		chartPanel.setPreferredSize(new java.awt.Dimension(850, 500));
		setContentPane(chartPanel);
	}

	/**
	 * Creates a chart with two datasets of algorithm runtimes and
	 * sets appearance of chart objects.
	 *
	 * @return A chart instance.
	 */
	private static JFreeChart createChart() {
		XYDataset[] data = new XYDataset[DATASETS];

		data[0] = createDataset('l', "list");
		data[1] = createDataset('b', "binary tree");


		JFreeChart chart = ChartFactory.createXYLineChart(
				"Randomized insertion runtimes ",// chart title
				"input size",              		 // x axis label
				"time(ms)",               		 // y axis label
				data[0],                  		 // data
				PlotOrientation.VERTICAL, 
				true,                     		 // include legend
				true,                     		 // tooltips
				false                     		 // urls
				);

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.getDomainAxis().setLowerMargin(0.0);
		plot.getDomainAxis().setUpperMargin(0.0);
		XYLineAndShapeRenderer renderer[] = new XYLineAndShapeRenderer[DATASETS];

		for (int i = 0; i < DATASETS; i++) {
			plot.setDataset(i, data[i]);
			renderer[i] = new XYLineAndShapeRenderer(); 
			plot.setRenderer(i, renderer[i]);
		}

		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.red); 
		plot.getRendererForDataset(plot.getDataset(1)).setSeriesPaint(0, Color.blue);

		return chart;
	}

	/**
	 * Creates a dataset based on algorithm runtimes.
	 *
	 * @param algo  The character used to represent algorithm
	 * @param name  The name of algorithm to appear in the legend
	 * 
	 * @return A sample dataset.
	 */
	public static XYDataset createDataset(char algo, String name) {
		XYDataset result = DatasetUtils.sampleFunction2D(new AddRuns(algo),
				0.0, 1000.0, 41, name);
		return result;
	}

	/**
	 *
	 * @return A panel.
	 */
	public static JPanel createDemoPanel() {
		JFreeChart chart = createChart();
		return new ChartPanel(chart);
	}

	/**
	 * Generates all the runtimes for the graph.
	 */
	static class AddRuns implements Function2D {
		private char algo;

		public AddRuns(char a) { algo = a; }

		public double getValue(double t) {
			ArrayList<DiskLocation> arrD = new ArrayList<DiskLocation>(Double.valueOf(t).intValue());

			final int samples = 100;	// number of samples at each array size

			if (t == 0)
				return 0;

			for (int i = 0; i < t; i++) {
				arrD.add(new DiskLocation(i, 1));
			}
			DiskLocation[] d = new DiskLocation[Double.valueOf(t).intValue()];
			LocationContainer l = new BST();
			double sec = 0;
			for (int i = 0; i < samples; i++) {

				Collections.shuffle(arrD);		// randomize array

				switch (algo) {
				case 'l':
					l = new LinkedList();
					break;
				case 'b':
					l = new BST();
					break;
				default:
					System.out.println("Invalid data structure");
					System.exit(0);
					break;
				}

				arrD.toArray(d);

				long start = System.nanoTime();
				RunInsert(l, d);
				long end = System.nanoTime();

				sec += (end-start)/1E6;
			}

			return sec/samples;
		}
	}

	private static void RunInsert(LocationContainer T, DiskLocation[] vals) {

		for (int i = 0; i < vals.length; i++)
			T.insert(vals[i]);
	}

	/**
	 * Starting point for the chartmaker.
	 *
	 * @param args  ignored.
	 */
	public static void main(String[] args) {
		ChartMaker cm = new ChartMaker(
				"Chart Window");
		cm.pack();
		RefineryUtilities.centerFrameOnScreen(cm);
		cm.setVisible(true);
	}

}
