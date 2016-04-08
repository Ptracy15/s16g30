package edu.uic.ids517.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.faces.context.FacesContext;

public class DataTableBean {

	FacesContext context = FacesContext.getCurrentInstance();
	Map<String, Object> m = context.getExternalContext().getSessionMap();
	DbmsUserBean loginBean = (DbmsUserBean) m.get("dbmsUserBean");
	RegUserLoginBean regUserLoginBean = (RegUserLoginBean) m
			.get("regUserLoginBean");
	static String newline = "\r\n";
	/**
	 * Bad File Name for the External Table
	 */
	private String badFileName = null;
	private final int COLUMN_NAME_ROW = 0;

	/**
	 * List of the External Table columns
	 */
	private List columns = null;

	private final String SPACE = "     ";

	/**
	 * Comma Separated Values File Extention
	 */
	private final String CSV_EXTENTION = ".csv";

	/**
	 * External table bad file extention
	 */
	private final String BAD_EXTENTION = ".bad";

	/**
	 * External table log file extention
	 */
	private final String LOG_EXTENTION = ".log";
	/**
	 * Directory Location referenced by the External Table
	 */
	private String dirLocation = null;

	/**
	 * Log File Name for the External Table
	 */
	private String logFileName = null;

	/**
	 * Name of the External Table
	 */
	private String name = null;

	/**
	 * Replace all blanks in a given name with underscores
	 * 
	 * @param name
	 */

	public DataTableBean(String name) {

		this.name = name.replaceAll(" ", "_");
		columns = new ArrayList();
		badFileName = this.name + BAD_EXTENTION;
		logFileName = this.name + LOG_EXTENTION;
	}

	/**
	 * 
	 * @param column
	 */
	public void addColumn(TableColumnBean column) {
		columns.add(column);
	}

	/**
	 * 
	 * @return
	 */
	public String getBadFileName() {
		return badFileName;
	}

	/**
	 * 
	 * @return
	 */

	public List getColumns() {
		return columns;
	}


	public int insertCsvData(InputStream is, List<TableMetaData> ctTable) {

		String ddl = "INSERT INTO " + getName() + newline + " (" + newline;
		int rows = 0;
		// creating the columnames
		for (int i = 0; i < ctTable.size(); i++) {
			ddl += SPACE + ctTable.get(i).getColumnName() + "," + newline;
		}
		ddl = ddl.substring(0, ddl.lastIndexOf(",")) + // remove the last comma
				// creating the values
				newline + ")" + newline + "VALUES" + getValuesCSV(is);// builds
																		// the
																		// values
																		// portion
		ddl = ddl.substring(0, ddl.lastIndexOf(",")) + ";";// removing the last
															// comma
		System.out.println("The Value of ddl+" + ddl);
		System.out.println("Login : " + loginBean.getUserName());
		// DbaseBean dbaseBean = new DbaseBean();
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		DbaseBean dbaseBean = new DbaseBean();
		// Connecting to the DB
		dbaseBean.connect();
		String userTableQuery = "INSERT INTO s15g103_tables_list(username, table_name)"
				+ " VALUES('" + getName() + "');";
		rows = dbaseBean.executeSQL(ddl);
		System.out.println("Hello>>>>>>>>>>>>>>>>>>>>>>" + rows);
		if (rows != 0)
			// dbaseBean.executeSQL(userTableQuery);

			return rows;
		return rows;
	}

	public String getValuesCSV(InputStream is) {

		InputStreamReader r = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(r);
		int row = 1;
		String ddl = "";
		String value = "";
		String line = "";

		try {
			while ((line = reader.readLine()) != null) {
				if (row == 1) {// not reading the first row as it is header
					row++;
				} else {
					ddl += newline + "( ";
					StringTokenizer tokenizer = new StringTokenizer(line, ",");
					while (tokenizer.hasMoreTokens()) {
						value = tokenizer.nextToken();
						String datatype = ReadData.interpret(value).getClass()
								.getSimpleName();
						if (datatype.equalsIgnoreCase("Double")
								|| datatype.equalsIgnoreCase("Integer")
								|| datatype.equalsIgnoreCase("Long")) {
							ddl += value + ",";
						} else if (datatype.equalsIgnoreCase("String")) {
							ddl += "'" + value + "',";
						}

					}
					ddl = ddl.substring(0, ddl.lastIndexOf(",")) + " ),";
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return ddl;
	}


	public String createTable(List<TableMetaData> ctTable)

	{

		String ddl = "CREATE TABLE " + getName() + newline + " (" + newline;

		System.out.println("DDL " + ddl);

		System.out.println("ctTable Hello : " + ctTable);

		for (int i = 0; i < ctTable.size(); i++) {
			System.out.println("ctTable Hello : "
					+ ctTable.get(i).getColumnType());
			// newdone
			// if
			// (ctTable.get(i).getColumnQuantType().equalsIgnoreCase("Quantitative"))
			// ddl += SPACE + ctTable.get(i).getColumnName() + SPACE+
			// ctTable.get(i).getColumnType() + "," + newline;
			// if
			// (ctTable.get(i).getColumnQuantType().equalsIgnoreCase("Categorical"))
			// ddl += SPACE + ctTable.get(i).getColumnName() + SPACE+
			// "varchar(255)" + "," + newline;
			// if
			// (ctTable.get(i).getColumnQuantType().equalsIgnoreCase("Count"))
			// ddl += SPACE + ctTable.get(i).getColumnName() + SPACE+
			// "varchar(255)" + "," + newline;
			ddl += SPACE + ctTable.get(i).getColumnName() + SPACE + "DOUBLE"
					+ "," + newline;
		}

		ddl = ddl.substring(0, ddl.lastIndexOf(",")) + // remove the last comma
				newline + ");";
		System.out.println(ddl);
		DbaseBean dbaseBean = new DbaseBean();
		System.out.println("Thiis is bad");
		dbaseBean.connect();
		System.out.println("BAD World");
		dbaseBean.executeSQL(ddl);
		System.out.println("Inside createTable in DataTableBean");
		return ddl;
	}

	/**
	 * 
	 * @return
	 */
	public String getDirLocation() {
		return dirLocation;
	}

	/**
	 * Get the location of the file referenced by the External table. This will
	 * be the same as the table name with a .csv extention
	 * 
	 * @return
	 */
	public String getLocation() {
		return name + CSV_EXTENTION;
	}

	/**
	 * 
	 * @return
	 */
	public String getLogFileName() {
		return logFileName;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param badFileName
	 */
	public void setBadFileName(String badFileName) {
		this.badFileName = badFileName;
	}

	/**
	 * 
	 * @param columns
	 */
	public void setColumns(List columns) {
		this.columns = columns;
	}

	/**
	 * 
	 * @param dirLocation
	 */
	public void setDirLocation(String dirLocation) {
		this.dirLocation = dirLocation;
	}

	/**
	 * 
	 * @param logFileName
	 */
	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
}