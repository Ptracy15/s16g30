package edu.uic.ids517.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.math3.stat.StatUtils;

@ManagedBean(name = "numericalAnalysis")
@SessionScoped
public class NumericalAnalysis {

	DbaseBean dbUtil = new DbaseBean();
	ResultSet rs = null;
	Statement st = null;
	Connection conn = null;
	boolean render;
	int i = 0;

	String status = "FAIL";
	DBQueries dbq;
	private String categorical = "yes";

	public String getCategorical() {
		return categorical;
	}

	public void setCategorical(String categorical) {
		this.categorical = categorical;
	}
	
	private static List<Double> singleColData;
	private static String[] selectedCols; // Optional.
	public List<TableInfo> selectedColumnObj;
	public String tableName;
	private List<StatisticsBean> statisticsBeanList = new ArrayList<StatisticsBean>();

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatisticsBeanList(List<StatisticsBean> statisticsBeanList) {
		this.statisticsBeanList = statisticsBeanList;
	}

	public String goBackToViewTableColumns() {
		return "viewTableColumns";
	}

	public List<StatisticsBean> getStatisticsBeanList() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			Map<String, Object> m = context.getExternalContext()
					.getSessionMap();
			TableListBean viewTableBean = (TableListBean) m
					.get("tableListBean");
			ViewTableColumnsBean viewTableColumnsBean = (ViewTableColumnsBean) m
					.get("viewTableColumnsBean");
			tableName = viewTableBean.getSelectedTable();
			selectedColumnObj = viewTableColumnsBean.checkedItems;
			statisticsBeanList.clear();
			categorical = "no";
			loadDynamicList(selectedColumnObj, tableName); // Preload dynamic
															// list.
		} catch (NumberFormatException e) {
			categorical = null;
		}
		return statisticsBeanList;
	}

	private void loadDynamicList(List<TableInfo> selectedColumnObj,
			String tableName) {

		selectedCols = new String[selectedColumnObj.size()];
		for (int i = 0; i < selectedColumnObj.size(); i++) {
			selectedCols[i] = selectedColumnObj.get(i).getColumn();
		}

		try {
			dbq = new DBQueries();
			int num = 0;
			for (int i = 0; i < selectedCols.length; i++) {
				singleColData = new ArrayList<Double>();
				String[] oneColumnArray = new String[] { selectedCols[i] };
				singleColData = dbq.getTableDataForColumnsAnalysis(
						oneColumnArray, tableName);
				StatisticsBean statBeanObj = processRequest(singleColData);
				statBeanObj.setColName(oneColumnArray[0]);
				statisticsBeanList.add(num, statBeanObj);
				
				num++;
			}

		} catch (SQLException e) {
			 e.printStackTrace();
		} catch (ClassNotFoundException e) {
			 e.printStackTrace();
		}

	}

	public StatisticsBean processRequest(List<Double> listValues) {

		double[] values = new double[listValues.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = listValues.get(i);
		}

		double minValue = StatUtils.min(values);
		double maxValue = StatUtils.max(values);
		double mean = StatUtils.mean(values);
		double sum = getSum(values);
		double countValue = getCount(values);


		StatisticsBean sb = new StatisticsBean();
		sb.setMinValue(round(minValue, 2));
		sb.setMaxValue(round(maxValue, 2));
		sb.setMean(round(mean, 2));
		sb.setSum(round(sum, 2));
		sb.setCountValue(countValue);
		
		render = true;
		return sb;
	}

	public boolean isRender() {
		return render;
	}

	public void setRender(boolean render) {
		this.render = render;
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
	
	   public static double getSum(double[] array)
	    {
		   double sum = 0.0;
	        for (double i : array) 
	        {
	         sum += i;
	        }
	        return sum;
	    }
	   
	   public static double getCount(double[] values)
	    {
		   double count = 0.0;
		   for (int i = 0; i < values.length; i++) {
			   count ++;
		   }
	        return count;
	    }

}
