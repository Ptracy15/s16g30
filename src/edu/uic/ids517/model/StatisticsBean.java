package edu.uic.ids517.model;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class StatisticsBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String colName;
	private double minValue;
	private double maxValue;
	private double countValue;
	private double mean;
	private double sum;
	
	public double getCountValue() {
		return countValue;
	}
	public void setCountValue(double countValue) {
		this.countValue = countValue;
	}
	
	public double getMinValue() {
		return minValue;
	}
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}
	
	public double getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public double getMean() {
		return mean;
	}
	public void setMean(double mean) {
		this.mean = mean;
	}
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}

}
