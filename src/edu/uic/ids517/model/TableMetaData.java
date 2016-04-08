package edu.uic.ids517.model;

public class TableMetaData {

	private String columnName;
	private String columnType;
	private String databaseColumnType;
	private String columnQuantType;

	public String getColumnQuantType() {
		return columnQuantType;
	}

	public void setColumnQuantType(String columnQuantType) {
		this.columnQuantType = columnQuantType;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getDatabaseColumnType() {
		return databaseColumnType;
	}

	public void setDatabaseColumnType(String databaseColumnType) {
		this.databaseColumnType = databaseColumnType;
	}

}
