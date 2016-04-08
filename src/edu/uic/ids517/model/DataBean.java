package edu.uic.ids517.model;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped

public class DataBean {

private String variableName;
public String getVariableName() {
	return variableName;
}

public void setVariableName(String variableName) {
	this.variableName = variableName;
}

public String getDataType() {
	return dataType;
}

public void setDataType(String dataType) {
	this.dataType = dataType;
}

public String getVariableType() {
	return variableType;
}

public void setVariableType(String variableType) {
	this.variableType = variableType;
}

public String getDataValue() {
	return dataValue;
}

public void setDataValue(String dataValue) {
	this.dataValue = dataValue;
}

public ArrayList<String> getDataTypeList() {
	return dataTypeList;
}

public void setDataTypeList(ArrayList<String> dataTypeList) {
	this.dataTypeList = dataTypeList;
}

private String dataType;
private String variableType;
private String dataValue;

private ArrayList<String> dataTypeList = new ArrayList<String>(4);

public DataBean() {

}

@PostConstruct
public void init() {
dataTypeList.add("string");
dataTypeList.add("integer");
dataTypeList.add("double");
dataTypeList.add("date");
}
}