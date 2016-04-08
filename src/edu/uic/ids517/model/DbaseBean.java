package edu.uic.ids517.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean ( name="dbaseBean")
@SessionScoped
public class DbaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private DbmsUserBean dbmsUserBean;
	private MessageBean messageBean;
	private String userName;


	public DbmsUserBean getDbmsUserBean() {
		return dbmsUserBean;
	}

	public void setDbmsUserBean(DbmsUserBean dbmsUserBean) {
		this.dbmsUserBean = dbmsUserBean;
	}

	public MessageBean getMessageBean() {
		return messageBean;
	}

	public void setMessageBean(MessageBean messageBean) {
		this.messageBean = messageBean;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public ResultSet getRs() {
		return rs;
	}


	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	public DatabaseMetaData getDatabaseMetaData() {
		return databaseMetaData;
	}

	public void setDatabaseMetaData(DatabaseMetaData databaseMetaData) {
		this.databaseMetaData = databaseMetaData;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getHtmlResultSet() {
		return htmlResultSet;
	}

	public void setHtmlResultSet(String htmlResultSet) {
		this.htmlResultSet = htmlResultSet;
	}

	public static String[] getTableTypes() {
		return TABLE_TYPES;
	}

	private String password;
	private String url;
	private boolean status = false;
	private static final String[] TABLE_TYPES = { "TABLE", "VIEW" };
	private String jdbcDriver;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet, rs;
	private DatabaseMetaData databaseMetaData;
	private String queryType;
	private String htmlResultSet;

	// prepare driver and url first
	public boolean connect() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		messageBean = (MessageBean) m.get("messageBean");
		dbmsUserBean = (DbmsUserBean) m.get("dbmsUserBean");
		userName = dbmsUserBean.getUserName();
		password = dbmsUserBean.getPassword();

		switch (dbmsUserBean.getDbms().toLowerCase()) {

		case "mysql":
			jdbcDriver = "com.mysql.jdbc.Driver";
			url = "jdbc:mysql://" + dbmsUserBean.getDbmsHost() + ":3306/"
					+ dbmsUserBean.getDatabaseSchema();
			break;

		case "db2":
			jdbcDriver = "com.ibm.db2.jcc.DB2Driver";
			url = "jdbc:db2://" + dbmsUserBean.getDbmsHost() + ":50000/"
					+ dbmsUserBean.getDatabaseSchema();
			userName = userName.toUpperCase();
			break;

		case "oracle":
			jdbcDriver = "oracle.jdbc.driver.OracleDriver";
			url = "jdbc:oracle:thin:@" + dbmsUserBean.getDbmsHost() + ":1521:"
					+ dbmsUserBean.getDatabaseSchema();
			userName = userName.toUpperCase();
			break;

		case "odbc":
		default:
			//
			break;
		} // end switch

		try {
			// register driver
			Class.forName(jdbcDriver);
			// get connection
			connection = DriverManager.getConnection(url, userName, password);
			// get SQL statement object instance
			statement = connection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);

			// retrieve DB meta data
			databaseMetaData = connection.getMetaData();

			DbaseBean dbaseBean = new DbaseBean();
			dbaseBean.setConnection(connection);
			dbaseBean.setDatabaseMetaData(databaseMetaData);
			dbaseBean.setJdbcDriver(jdbcDriver);
			dbaseBean.setUserName(userName);
			dbaseBean.setPassword(password);
			dbaseBean.setUrl(url);
			dbaseBean.setResultSet(resultSet);
			m.put("dbaseBean", dbaseBean);

			status = true;
		}

		catch (ClassNotFoundException e) {
			// assumes there is a corresponding printException method
			// PrintException("Connect: Class not found Exception - could not loadJDBC driver");
			status = false;
		}

		catch (SQLException e) {
			// printException(e,"Connect: SQLException information");
			status = false;
		} catch (Exception e) {
			// printException(e,"Connect: Exception information");
			status = false;
		}

		return status;

	} // end connect()

	public boolean closeConnection() throws SQLException {
		// release resources in reverse order (result set, statement,connection
		connection.close();
		status = true;
		return status;
	} // end closeConnection()

	public int executeSQL(String sqlQuery) {

		// JDBC executeUpdate or executeQuery based on type
		// need try-catch blocks
		// resultSet is generated

		int errcode = 0;
		try {

			if (sqlQuery.substring(0, 2).equalsIgnoreCase("SE")
					|| sqlQuery.substring(0, 2).equalsIgnoreCase("SH")) {
				resultSet = statement.executeQuery(sqlQuery); // query string
			} else {
				errcode = statement.executeUpdate(sqlQuery); // query string
				statement.executeUpdate("commit;");
			}
		} catch (Exception e) {
			errcode = 1;
			e.printStackTrace();
		}
		return errcode;
	} // end executeSQL()

	public String retrieveElement() throws SQLException {
		String pass = "none";
		if (resultSet != null) {
			while (resultSet.next()) {
				pass = resultSet.getString(1);
			}
		}
		return pass;
	}

	// generate list of tables and views associated with user
	public List<String> tableList() throws SQLException {

		List<String> l = new ArrayList<String>();

		// nned try-catch blocks
		rs = databaseMetaData.getTables(null, userName, null, TABLE_TYPES);

		String tableName = "";
		if (rs != null) {
			while (rs.next()) {
				tableName = rs.getString("TABLE_NAME");
				if (!dbmsUserBean.getDbms().equalsIgnoreCase("oracle")
						|| tableName.length() < 4)
					l.add(tableName);
				else if (!tableName.substring(0, 4).equalsIgnoreCase("BIN$"))
					l.add(tableName);
			}
		}

		return l;
	} // end tableList()

	// generate list of columns given a table name
	public List<String> columnList(String tableName) throws SQLException {

		List<String> l = new ArrayList<String>();

		// need try-catch blocks

		rs = databaseMetaData.getColumns(null, dbmsUserBean.getUserName(),
				tableName, null);
		String columnName = "";
		if (rs != null) {
			while (rs.next()) {
				columnName = rs.getString("COLUMN_NAME");
				System.out.println("column name:" + columnName);
				l.add(columnName);
			}
		}
		return l;
	} // end columnList()

	// convert result set to html (here or in SQLUtil class?) to String
	public String getHtmlTable() throws Exception {

		StringBuffer htmlRows = new StringBuffer();
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		int columnCount = resultSetMetaData.getColumnCount();

		htmlRows.append("<table align=center border=2 style=\"background-color: Beige;border-bottom-style: solid;border-top-style: solid;border-left-style: solid;border-right-style: solid\"><TBODY>");
		htmlRows.append("<tr>");
		for (int i = 1; i <= columnCount; i++)
			htmlRows.append("<td><b>" + resultSetMetaData.getColumnName(i)
					+ "</td>");

		htmlRows.append("</tr>");

		while (resultSet.next()) {
			htmlRows.append("<tr>");
			for (int i = 1; i <= columnCount; i++)
				htmlRows.append("<td>" + resultSet.getString(i) + "</td>");
		}

		htmlRows.append("</tr>");
		htmlRows.append("</TBODY></TABLE>");
		return htmlRows.toString();
	} // end getHtmlTable

	public DbaseBean() {
		// TODO Auto-generated constructor stub
	}

}
