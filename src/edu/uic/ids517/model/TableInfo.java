package edu.uic.ids517.model;

import java.io.Serializable;

public class TableInfo  implements Serializable {

	private static final long serialVersionUID = 1L;
	private  String dataType; 
	private String column;

   public TableInfo() {}

    public TableInfo(String column, String dataType ) {
    	this.column = column;
    	this.dataType= dataType;
    }
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

}