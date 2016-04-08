package edu.uic.ids517.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletContext;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

@ManagedBean(name = "regressionAnalysis")
@SessionScoped
public class RegressionAnalysis {
	public String xVariable;
	public String yVariable;
	public String getEquation() {
		return equation;
	}
	public void setEquation(String equation) {
		this.equation = equation;
	}
	public double getCoefConst() {
		return coefConst;
	}
	public void setCoefConst(double coefConst) {
		this.coefConst = coefConst;
	}
	public double getCoefSF() {
		return coefSF;
	}
	public void setCoefSF(double coefSF) {
		this.coefSF = coefSF;
	}
	public double getSeCoefConst() {
		return seCoefConst;
	}
	public void setSeCoefConst(double seCoefConst) {
		this.seCoefConst = seCoefConst;
	}
	public double getSeCoefSF() {
		return seCoefSF;
	}
	public void setSeCoefSF(double seCoefSF) {
		this.seCoefSF = seCoefSF;
	}
	public double gettConst() {
		return tConst;
	}
	public void settConst(double tConst) {
		this.tConst = tConst;
	}
	public double gettSF() {
		return tSF;
	}
	public void settSF(double tSF) {
		this.tSF = tSF;
	}
	public double getpConst() {
		return pConst;
	}
	public void setpConst(double pConst) {
		this.pConst = pConst;
	}
	public double getpSF() {
		return pSF;
	}
	public void setpSF(double pSF) {
		this.pSF = pSF;
	}
	public double getS() {
		return s;
	}
	public void setS(double s) {
		this.s = s;
	}
	public double getrSquare() {
		return rSquare;
	}
	public void setrSquare(double rSquare) {
		this.rSquare = rSquare;
	}
	public double getrSquareAdj() {
		return rSquareAdj;
	}
	public void setrSquareAdj(double rSquareAdj) {
		this.rSquareAdj = rSquareAdj;
	}
	public double getDf() {
		return df;
	}
	public void setDf(int df) {
		this.df = df;
	}
	public double getDfRE() {
		return dfRE;
	}
	public void setDfRE(int dfRE) {
		this.dfRE = dfRE;
	}
	public double getDfTot() {
		return dfTot;
	}
	public void setDfTot(int dfTot) {
		this.dfTot = dfTot;
	}
	public double getSsr() {
		return ssr;
	}
	public void setSsr(double ssr) {
		this.ssr = ssr;
	}
	public double getSse() {
		return sse;
	}
	public void setSse(double sse) {
		this.sse = sse;
	}
	public double getSsTot() {
		return ssTot;
	}
	public void setSsTot(double ssTot) {
		this.ssTot = ssTot;
	}
	public double getMsr() {
		return msr;
	}
	public void setMsr(double msr) {
		this.msr = msr;
	}
	public double getMse() {
		return mse;
	}
	public void setMse(double mse) {
		this.mse = mse;
	}
	public double getF() {
		return f;
	}
	public void setF(double f) {
		this.f = f;
	}
	public double getP() {
		return p;
	}
	public void setP(double p) {
		this.p = p;
	}
	private String equation;
	private double coefConst;
	private double coefSF;
	private double seCoefConst;
	private double seCoefSF;
	private double tConst;
	private double tSF;
	private double pConst;
	private double pSF;
	private double s;
	private double rSquare;
	private double rSquareAdj;
	private String categorical="yes";
	public String getCategorical() {
		return categorical;
	}
	public void setCategorical(String categorical) {
		this.categorical = categorical;
	}
	private int df;
	private int dfRE;
	private int dfTot;
	private double ssr;
	private double sse;
	private double ssTot;
	private double msr;
	private double mse;
	private double f;
	private double p;

	FacesContext context = FacesContext.getCurrentInstance();
	Map<String, Object> m = context.getExternalContext().getSessionMap();
	TableListBean viewTableBean = (TableListBean) m.get("tableListBean");
	public String tableName = viewTableBean.getSelectedTable();
	DbaseBean dbUtil=(DbaseBean)m.get("dbaseBean");
	//DbaseBean dbUtil = new DbaseBean();
	ResultSet rs = null;
	Statement st = null;
	Connection conn = null;
	boolean render = false;

	String status = "FAIL";
	DBQueries dbq;
	public List<TableInfo> selectedColumnObj;
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
		if(value.getNewValue() != null)
		setxVariable(value.getNewValue().toString());
	}

	public void yValueChange(ValueChangeEvent value) {
		if(value.getNewValue() != null)
		setyVariable((String) value.getNewValue().toString());
	}
		
	public String performRegression(){
		double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
		String query = "select " + xVariable + ", " + yVariable + " from " + viewTableBean.getSelectedTable() + ";";
		String query1 = "select count(" + xVariable + "), count(" + yVariable + ") from " + viewTableBean.getSelectedTable() + ";";

		System.out.println(query);
		System.out.println(query1);

		
		Connection conn = dbUtil.getConnection();
		Statement st;
		Statement st1;
		
		try {
			st = (Statement) conn.createStatement();
			st1 = (Statement) conn.createStatement();
			ResultSet rs1 = st1.executeQuery(query1);
			Integer xrowCount = 0;
			Integer yrowCount = 0;
			while (rs1.next()) {
				 xrowCount =rs1.getInt(1);
				 yrowCount =rs1.getInt(2);
			}
			ResultSet rs = st.executeQuery(query);
			 double[] x = new double[xrowCount];
		     double[] y = new double[yrowCount];
			int n = 0;
			while (rs.next()) {
				x[n] = rs.getDouble(xVariable);
				y[n] = rs.getDouble(yVariable);
	            sumx  += x[n];
	            sumx2 += x[n] * x[n];
	            sumy  += y[n];
				n++;

			}
			double xbar = sumx / n;
	        double ybar = sumy / n;

	        // second pass: compute summary statistics
	        double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
	        for (int i = 0; i < n; i++) {
	            xxbar += (x[i] - xbar) * (x[i] - xbar);
	            yybar += (y[i] - ybar) * (y[i] - ybar);
	            xybar += (x[i] - xbar) * (y[i] - ybar);
	        }
	        double beta1 = xybar / xxbar;
	        double beta0 = ybar - beta1 * xbar;
	        
	        // print results
			setEquation("Y = " + beta0 + " + " + beta1 + " " + "X");
	     

	        // analyze results
	        int df = n - 2;
	        double rss = 0.0;      // residual sum of squares
	        double ssr = 0.0;      // regression sum of squares
	        for (int i = 0; i < n; i++) {
	            double fit = beta1*x[i] + beta0;
	            rss += (fit - y[i]) * (fit - y[i]);
	            ssr += (fit - ybar) * (fit - ybar);
	        }
	        double R2    = ssr / yybar;
	        double svar  = rss / df;
	        double svar1 = svar / xxbar;
	        double svar0 = svar/n + xbar*xbar*svar1;
			setCoefConst((beta0));

	        setCoefSF(beta1);

	        setSeCoefConst(Math.sqrt(svar0));
	   
	        setSeCoefSF(Math.sqrt(svar1));



	        double tOfConstant=beta0/(Math.sqrt(svar0));
	        double tOfSF=beta1/(Math.sqrt(svar1));

	        settConst(tOfConstant);

	        settSF(tOfSF);


	        setpConst(0.0);

	        setpSF(0.001);
	  
	        
	        double MSR = ssr;
	        double MSE = rss/(n-2);
	        double F = MSR/MSE;
	        setS(Math.sqrt(MSE));

	        setrSquare((R2*100));
	        setrSquareAdj((R2*100));


	        svar0 = svar * sumx2 / (n * xxbar);

	        setDf(1);
	        setDfRE(n-2);
	        setDfTot(n-1);
	 
	        setSsr(ssr);
	        setSse(rss);
	        setSsTot(yybar);


	        setMsr(MSR);
	        setMse(MSE);
	      
	        setF(F);
	        setP(0.001);


	        categorical="no";
	        generateScatterPlot2();
	        setRender(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			categorical=null;
		}
		return "SUCCESS";
	}

	private void generateScatterPlot2() {

		XYSeries data = new XYSeries("data");
		try {
			String query = "select " + xVariable + "," + yVariable + " from " + viewTableBean.getSelectedTable() + ";";
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

		JFreeChart chart = ChartFactory.createScatterPlot("Regression Graph", // chart
				xVariable, // x axis label
				yVariable, // y axis label
				dataset, // data 
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);
		Shape shape  = new Ellipse2D.Double(0,0,3,3);
		XYPlot xyPlot = (XYPlot) chart.getPlot();
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);
		XYItemRenderer renderer = xyPlot.getRenderer();
		renderer.setSeriesStroke(0,new BasicStroke(1.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
		renderer.setBaseShape(shape);
		renderer.setSeriesPaint(0, Color.blue);
		renderer.setSeriesShape(0, shape);
		xyPlot.setBackgroundPaint(Color.white);
		double[] coeffs = Regression.getOLSRegression(dataset, 0);
		LineFunction2D linefunction2d = new LineFunction2D(coeffs[0], coeffs[1]);
		XYDataset series2 = DatasetUtilities.sampleFunction2D(linefunction2d, 10, 50, 5, "Linear Regression Line");
		xyPlot.setDataset(2, series2); 

		XYLineAndShapeRenderer lineDrawer = new XYLineAndShapeRenderer(true, false);
		lineDrawer.setSeriesPaint(0, Color.red);
		lineDrawer.setSeriesStroke(0, new BasicStroke(3.5f));
		xyPlot.setRenderer(2, lineDrawer);
		saveChart(chart);
	}
	private void saveChart(JFreeChart chart) {
		try {
			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

			String deploymentDirectoryPath = ctx.getRealPath("/");
			String path = deploymentDirectoryPath + "/JFreechart.png";
			System.out.println(deploymentDirectoryPath);
			File file = new File(path);
			ChartUtilities.saveChartAsPNG(file, chart, 550, 500);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setRender(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*ChartFrame frame1=new ChartFrame("Chart",chart);
		  frame1.setVisible(true);
		  frame1.setSize(800,600);*/
	}
	/*public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}*/
	public String goBackToViewTableColumns(){
		setRender(false);
		return "viewTableColumns";
	}
}
