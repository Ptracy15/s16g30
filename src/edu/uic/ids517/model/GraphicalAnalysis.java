package edu.uic.ids517.model;

import java.awt.Color;
import java.awt.Shape;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

@ManagedBean(name = "graphicalAnalysis")
@SessionScoped
public class GraphicalAnalysis {
	FacesContext context2 = FacesContext.getCurrentInstance();
	Map<String, Object> m2 = context2.getExternalContext().getSessionMap();
	DbaseBean dbUtil = (DbaseBean) m2.get("dbaseBean");
	boolean render = false;
	boolean xyRender = false;

	public String xVariable;
	public String yVariable;
	public String variable;

	DBQueries dbq;

	FacesContext context = FacesContext.getCurrentInstance();
	Map<String, Object> m = context.getExternalContext().getSessionMap();
	TableListBean viewTableBean = (TableListBean) m.get("tableListBean");
	public String tableName = viewTableBean.getSelectedTable();

	public String selectedGraph;

	private SelectItem[] graphsList = {
			
			new SelectItem("Time Series", "Time Series"),
			new SelectItem("Scatterplot", "Scatterplot")};

	public void setGraphsList(SelectItem[] graphsList) {
		this.graphsList = graphsList;
	}

	public SelectItem[] getGraphsList() {
		return graphsList;
	}

	public String getSelectedGraph() {
		return selectedGraph;
	}

	public void setSelectedGraph(String selectedGraph) {
		this.selectedGraph = selectedGraph;
	}

	public void graphChange(ValueChangeEvent event) {
		String graph = event.getNewValue().toString();
		setSelectedGraph(graph);
		if (graph.equalsIgnoreCase("Scatter Plot")
				|| graph.equalsIgnoreCase("Time Series")) {
			setXyRender(true);
		} else
			setXyRender(false);
	}

	public boolean isRender() {
		return render;
	}

	public void setRender(boolean render) {
		this.render = render;
	}

	public String getxVariable() {
		return xVariable;
	}

	public void setxVariable(String xVariable) {
		this.xVariable = xVariable;
	}

	public String getyVariable() {
		return yVariable;
	}

	public void setyVariable(String yVariable) {
		this.yVariable = yVariable;
	}

	public void xValueChange(ValueChangeEvent value) {
		if (value.getNewValue() != null)
			setxVariable(value.getNewValue().toString());
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public void valueChange(ValueChangeEvent value) {
		if (value.getNewValue() != null)
			setVariable(value.getNewValue().toString());

	}

	public void yValueChange(ValueChangeEvent value) {
		if (value.getNewValue() != null)
			setyVariable((String) value.getNewValue().toString());
	}

	public String generateChart() {

		switch (selectedGraph) {
		case "Scatter Plot":
			generateScatterPlot();
			break;
		case "Time Series":
			generateTimeSeriesPlot();
			break;
		}
		return "SUCCESS";

	}

	

	private void generateTimeSeriesPlot() {
		XYSeries data = new XYSeries("data");
		try {
			String query = "select " + xVariable + "," + yVariable + " from "
					+ viewTableBean.getSelectedTable() + ";";
		
			Connection conn = dbUtil.getConnection();
			Statement st = (Statement) conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				data.add(rs.getDouble(xVariable), rs.getDouble(yVariable));
			}
		} catch (SQLException e) {
		
			e.printStackTrace();
		}

		XYDataset dataset = new XYSeriesCollection(data);
		JFreeChart chart = ChartFactory.createTimeSeriesChart("TimeSeries",
				"Time", "Value", dataset, true, true, true);
		saveChart(chart);
	}

	private void generateScatterPlot() {

		XYSeries data = new XYSeries("data");
		try {
			String query = "select " + xVariable + "," + yVariable + " from "
					+ viewTableBean.getSelectedTable() + ";";
			
			Connection conn = dbUtil.getConnection();
			Statement st = (Statement) conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				data.add(rs.getDouble(xVariable), rs.getDouble(yVariable));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		XYDataset dataset = new XYSeriesCollection(data);

		JFreeChart chart = ChartFactory.createScatterPlot("Scatter Plot", // chart
				xVariable, // x axis label
				yVariable, // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);

		XYPlot xyPlot = (XYPlot) chart.getPlot();
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);
		Shape cross = ShapeUtilities.createDiagonalCross(3, 1);
		XYItemRenderer renderer = xyPlot.getRenderer();
		renderer.setSeriesShape(0, cross);
		renderer.setSeriesPaint(0, Color.green);
		xyPlot.setBackgroundPaint(Color.white);

		saveChart(chart);
	}

	

	public String goBackToViewTableColumns() {
		setRender(false);
		return "viewTableColumns";
	}

	private void saveChart(JFreeChart chart) {
		try {
			ServletContext ctx = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();

			String deploymentDirectoryPath = ctx.getRealPath("/");
			String path = deploymentDirectoryPath + "/JFreechart.png";
			File file = new File(path);
			if (!(file.isDirectory()))
			{
				file.delete();
			}
		
				File file1 = new File(path);
			ChartUtilities.saveChartAsPNG(file1, chart, 800, 600);
			
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setRender(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * ChartFrame frame1=new ChartFrame("Chart",chart);
		 * frame1.setVisible(true); frame1.setSize(800,600);
		 */
	}

	public boolean isXyRender() {
		return xyRender;
	}

	public void setXyRender(boolean xyRender) {
		this.xyRender = xyRender;
	}
}
