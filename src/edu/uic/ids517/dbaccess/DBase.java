package edu.uic.ids517.dbaccess;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.transform.Result;

public class DBase {

	private String userName;
	private String password;
	private String host;
	private String url;
	private String dbSchema;
	private String dbms;
	private String jdbcDriver;
	private boolean status = false;

	private Connection connection;
	private DatabaseMetaData databaseMetaData;
	private Statement statement;
	private ResultSet resultSet, rs;
	private ResultSetMetaData resultSetMetaData;
	private Result result;
	private int numberColumns;
	private int numberRows;
	private static final String[] TABLE_TYPES = { "TABLE", "VIEW" };
	private ArrayList<String> lisTableList = new ArrayList<String>();

	public ArrayList<String> getLisTableList() {
		return lisTableList;
	}

	public void setLisTableList(ArrayList<String> lisTableList) {
		this.lisTableList = lisTableList;
	}

	public boolean connect(DBDetailsInfo dbUserInfo) {

		userName = dbUserInfo.getUserName();
		password = dbUserInfo.getPassword();
		host = dbUserInfo.getDbmsHost();
		dbSchema = dbUserInfo.getDatabaseSchema();
		// dbms = dbUserInfo.getDbms();

		jdbcDriver = "com.mysql.jdbc.Driver";
		url = "jdbc:mysql://" + host + ":3306/" + dbSchema;
		try {
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(url, userName, password);
			statement = connection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			databaseMetaData = connection.getMetaData();
			rs = databaseMetaData.getTables(null, userName, null, TABLE_TYPES);
			while (rs.next()) {
				lisTableList.add(rs.getString("TABLE_NAME"));

			}

		} catch (ClassNotFoundException e) {
			System.err.println("Error: "
					+ ((ClassNotFoundException) e).getMessage()
					+ " -> Could not load JDBC driver");
			status = false;
		} catch (SQLException e) {
			System.err.println("SQLException information: ");
			while (e != null) {
				System.err.println("Error message: " + e.getMessage());
				System.err.println("SQLSTATE: " + e.getSQLState());
				System.err.println("Error Code: " + e.getErrorCode());
				e.printStackTrace();
				e = e.getNextException();
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		}

		return status;
	}

	public void CloseConnect() {
		try {
			// rs.close();
			statement.close();
			connection.close();
		} catch (SQLException ex) {
			System.err.println("SQLException information");
			while (ex != null) {
				System.err.println("Error message: " + ex.getMessage());
				System.err.println("SQLSTATE: " + ex.getSQLState());
				System.err.println("Error Code: " + ex.getErrorCode());
				ex.printStackTrace();
				ex = ex.getNextException();
			}
		}
	}

	public ArrayList getCoulumns(DBDetailsInfo dbUserInfo, String selectedTable) {
		ArrayList<String> lisTColumnList = new ArrayList<String>();
		try {
			rs = databaseMetaData.getColumns(null, dbUserInfo.getUserName(),
					selectedTable, null);
			while (rs.next()) {
				lisTColumnList.add(rs.getString("COLUMN_NAME"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lisTColumnList;

	}

	public boolean executeUpdate(String sqlInsertQuerry) {
		try {
			System.out.println("The Value in sql" + sqlInsertQuerry);
			System.out.println("Ts" + statement);
			statement.executeUpdate(sqlInsertQuerry);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ResultSet executeSQLQuerry(String sqlQuery) {
		try {
			resultSet = statement.executeQuery(sqlQuery);
			return resultSet;
		} catch (SQLException e) {
			System.err.println("SQLState: " + ((SQLException) e).getSQLState());
			System.err.println("Error Code: "
					+ ((SQLException) e).getErrorCode());
			System.err.println("Message: " + e.getMessage());
			return resultSet;
		}

	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

}
