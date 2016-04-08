package edu.uic.ids517.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.jsp.jstl.sql.*;

@ManagedBean ( name="dbaccess" )
@SessionScoped
public class DBAccessBean implements Serializable {
	//Serializable ID
	private static final long serialVersionUID = 1L;
	
	//variables
	private DbUserInfo dbUserInfo;
	private MessageBean messageBean;
	private String userName;
	private String password;
	private String host;
	private String url;
	private String dbSchema;
	private String dbms;
	private String jdbcDriver;
	private boolean status;
	private Connection connection;
	private DatabaseMetaData databaseMetaData;
	private Statement statement;
	private ResultSet resultSet;
	private ResultSet rs;
	private String queryType;
	private String sqlQuery;
	private String message;
	private Result result;
	private ArrayList columnNames;
	private int numberColumns;
	private ResultSet MetaDatarsmd;
	private int numberRows;
	private boolean renderResult;
	private static final String TABLE_TYPES[] = { "TABLE", "VIEW" };
	private RegUserLoginBean regUserLoginBean;
	private DbaseBean dbase;
	private DataBean dataBean;
	
	private String buttonId;
	private DbmsUserBean userInfo;
	
	public DBAccessBean() {
		status = false;
	}
	


	public boolean connectToDB() throws SQLException {
		DbUserInfo userInfo = new DbUserInfo();
		switch (dbms) {
		case "MySql":
			jdbcDriver = "com.mysql.jdbc.Driver";
			url = "jdbc:mysql://".concat(host).concat(":3306/")
					.concat(dbSchema);
			break;
		case "DB2":
			jdbcDriver = "com.ibm.db2.jcc.DB2Driver";
			url = "jdbc:db2://".concat(host).concat(":50000/").concat(dbSchema);
			break;
		case "Oracle":
			jdbcDriver = "oracle.jdbc.driver.OracleDriver";
			url = "jdbc:oracle:thin:@".concat(host).concat(":1521:")
					.concat(dbSchema);
			break;
		}
		try {
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(url, userName, password);
			statement = connection.createStatement(1004, 1008);
			databaseMetaData = connection.getMetaData();
			status = true;

		} catch (ClassNotFoundException e) {
			System.err.println((new StringBuilder("Error: "))
					.append(e.getMessage())
					.append(" -> Could not load JDBC driver").toString());
			status = false;
			message = (new StringBuilder("Error: ")).append(e.getMessage())
					.append(" -> Could not load JDBC driver").toString();
		} catch (SQLException e) {
			System.err.println("SQLException information: ");
			if (e != null) {
				System.err.println((new StringBuilder("Error message: "))
						.append(e.getMessage()).toString());
				System.err.println((new StringBuilder("SQLSTATE: ")).append(
						e.getSQLState()).toString());
				System.err.println((new StringBuilder("Error Code: ")).append(
						e.getErrorCode()).toString());
				// e.printStackTrace();
				message = "Not Logged In".concat(e.getMessage());
				e = e.getNextException();

				return false;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			status = false;
			message = (new StringBuilder("Error message: ")).append(
					e.getMessage()).toString();
		}
		message = "Logged In";
		return status;
	}

	public boolean closeConnection() {
		boolean status = false;
		try {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
			status = true;
			message = "LoggedOut";
			System.out.println("Connection Closed");
		} catch (SQLException ex) {
			System.err.println("SQLException information");
			for (; ex != null;) {
				System.err.println((new StringBuilder("Error message: "))
						.append(ex.getMessage()).toString());
				System.err.println((new StringBuilder("SQLSTATE: ")).append(
						ex.getSQLState()).toString());
				System.err.println((new StringBuilder("Error Code: ")).append(
						ex.getErrorCode()).toString());
				message = ((new StringBuilder("Error message: ")).append(ex
						.getMessage()).toString()).concat(" ; ").concat(
						(new StringBuilder("SQLSTATE: ")).append(
								ex.getSQLState()).toString());
				// ex.printStackTrace();
				return false;
			}

		}
		return status;
	}

	public boolean execSQLStatement(String sqlQuery) {
		String queryType = sqlQuery.substring(0, 6);
		System.out.println(queryType);
		if (!queryType.equalsIgnoreCase("SELECT")) {
			try {
				statement.executeUpdate(sqlQuery);
				System.out
						.println((new StringBuilder(String.valueOf(sqlQuery)))
								.append("executed").toString());
			} catch (SQLException e) {
				System.err.println((new StringBuilder("SQLState: ")).append(
						e.getSQLState()).toString());
				System.err.println((new StringBuilder("Error Code: ")).append(
						e.getErrorCode()).toString());
				System.err.println((new StringBuilder("Message: ")).append(
						e.getMessage()).toString());
				return false;
			}
			return true;
		}
		if (queryType.equalsIgnoreCase("select")) {
			try {
				rs = statement.executeQuery(sqlQuery);
				ResultSetMetaData rsmd = rs.getMetaData();
				System.out.println((new StringBuilder("SQL Query Executed:"))
						.append(sqlQuery).toString());
				numberColumns = rsmd.getColumnCount();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return true;
		}
	}

	public boolean displayResults(String tableName) {
		try {
			for (; rs.next(); System.out.println("")) {
				for (int i = 1; i <= numberColumns; i++) {
					if (i > 1) {
						System.out.print(",  ");
					}
					String columnValue = rs.getString(i);
					System.out.print(columnValue);
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public List listTables() {
		List tableList = null;
		try {
			resultSet = databaseMetaData.getTables(null, userName, null,
					TABLE_TYPES);
			resultSet.last();
			int numberRows = resultSet.getRow();
			tableList = new ArrayList(numberRows);
			resultSet.beforeFirst();
			String tableName = "";
			if (resultSet != null) {
				while (resultSet.next()) {
					tableName = resultSet.getString("TABLE_NAME");
					if (tableName.length() < 4) {
						tableList.add(tableName);
					} else if (!tableName.substring(0, 4).equalsIgnoreCase(
							"BIN$")) {
						tableList.add(tableName);
					}
				}
			}
		} catch (SQLException e) {
			System.err.println((new StringBuilder("SQLState: ")).append(
					e.getSQLState()).toString());
			System.err.println((new StringBuilder("Error Code: ")).append(
					e.getErrorCode()).toString());
			System.err.println((new StringBuilder("Message: ")).append(
					e.getMessage()).toString());
		}
		System.out.println((new StringBuilder("table list = ")).append(
				tableList).toString());
		return tableList;
	}

	public int countColumnsAndRows() {
		int colCount = 0;
		int rowCount = 0;
		try {
			colCount = rs.getMetaData().getColumnCount();
			System.out.println((new StringBuilder("Column Count = ")).append(
					colCount).toString());
			rs.last();
			rowCount = rs.getRow();
			System.out.println((new StringBuilder("Row Count = ")).append(
					rowCount).toString());
		} catch (SQLException e) {
			System.err.println((new StringBuilder("SQLState: ")).append(
					e.getSQLState()).toString());
			System.err.println((new StringBuilder("Error Code: ")).append(
					e.getErrorCode()).toString());
			System.err.println((new StringBuilder("Message: ")).append(
					e.getMessage()).toString());
		}
		return colCount;
	}

	public boolean executeSQL(String sqlQuery) {
		this.sqlQuery = sqlQuery;
		int result = 0;
		if (!status) {
			System.out.println("Not connected to database");
			return false;
		}
		if (!queryType.equalsIgnoreCase("select")) {
			try {
				if (queryType.equalsIgnoreCase("create")) {
					try {
						result = statement.executeUpdate(sqlQuery);
						message = " Table created successfully";
					} catch (Exception e) {
						message = " Table already exists";
					}
				}
				if (queryType.equalsIgnoreCase("update")) {

					try {
						result = statement.executeUpdate(sqlQuery);
						message = " Table updated successfully";
					} catch (Exception e) {
						message = " Update failed!";
					}

				}
				if (queryType.equalsIgnoreCase("insert")) {
					try {
						statement.executeUpdate(sqlQuery);
						message = "Table row inserted successfully";
					} catch (Exception e) {
						message = " insert failed";
					}
				}
				if (queryType.equalsIgnoreCase("drop")) {
					try {
						statement.executeUpdate(sqlQuery);
						message = " Table dropped successfully";
					} catch (Exception e) {
						message = " No such table exists";
					}
				} else if (queryType.equalsIgnoreCase("truncate")) {
					try {
						statement.executeUpdate(sqlQuery);
						message = " Table truncated successfully";
					} catch (Exception e) {
						message = " No such table exists";
					}
				}
			} catch (NullPointerException e) {
				message = "Error";
				e.printStackTrace();
				return false;
			}
			return true;
		}
		try {
			resultSet = statement.executeQuery(sqlQuery);
			rs = resultSet;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean generateResult() {
		renderResult = true;
		try {
			result = ResultSupport.toResult(rs);
			ResultSetMetaData rsmd = rs.getMetaData();
			numberColumns = rsmd.getColumnCount();
			numberRows = result.getRowCount();
		} catch (SQLException e) {
			renderResult = false;
			return false;
		} catch (Exception e) {
			renderResult = false;
			return false;
		}
		String columnNameList[] = result.getColumnNames();
		columnNames = new ArrayList(numberColumns);
		for (int i = 0; i < numberColumns; i++) {
			columnNames.add(columnNameList[i]);
		}

		return true;
	}
	
	public String dbOperation() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dbase = (DbaseBean) m.get("dbaseBean");
		messageBean = (MessageBean) m.get("messageBean");
		dataBean = (DataBean) m.get("dataBean");
		userInfo = (DbmsUserBean) m.get("dbmsUserBean");
		regUserLoginBean = (RegUserLoginBean) m.get("regUserLoginBean");
		
		switch (buttonId) {
		case "CopyTable":
			DataBaseBean clear = new DataBaseBean();
			clear.clearContens();
			try {
				dbase.connect();
				dbase.executeSQL("CREATE TABLE s16g31." + userInfo.getTableName() + " LIKE " + userInfo.getTableName());
				int err = dbase.executeSQL("INSERT INTO s16g31." + userInfo.getTableName() + " SELECT * FROM "
						+ userInfo.getTableName());
	
				if (err == 1) {
					FacesContext.getCurrentInstance().addMessage(
							"myForm2:errmess",
							new FacesMessage("Tables already exist!"));
					return "success";
				}

				messageBean.setResetMessage("Tables Created Successfully!");
				FacesContext.getCurrentInstance().addMessage("myForm2:errmess",
						new FacesMessage("Tables Created Successfully!"));
				return "success";
			} catch (NullPointerException e) {
				return "success";
			} catch (Exception e) {
				messageBean
				.setResetMessage("Table already exists. Try dropping the tables before creating. If problem persists contact admin!");
				FacesContext.getCurrentInstance().addMessage("myForm2:errmess",
				new FacesMessage("Tables already exists!"));
				return "fail";
			}


		case "userdrop":
		try {
			DataBaseBean clear2 = new DataBaseBean();
			clear2.clearContens();
			dbase.setQueryType("DROP");
			dbase.connect();
			int err = dbase.executeSQL("Drop table "
					+ userInfo.getTableName());

			if (err == 1) {
				FacesContext.getCurrentInstance().addMessage(
						"myForm2:errmess",
						new FacesMessage("Table doesn't exist!"));
				return "success";
			}
			messageBean.setResetMessage("Tables Dropped Successfully!");
			messageBean.setTableMessage("");
			message = "Tables dropped Successfully!";
			FacesContext.getCurrentInstance().addMessage("myForm2:errmess",
					new FacesMessage("Table dropped successfully!"));

			return "success";
		} catch (NullPointerException e) {
			return "success";
		} catch (Exception e) {
			messageBean
					.setResetMessage("Unable to delete the tables before creating. If problem persists contact admin!");
			FacesContext.getCurrentInstance().addMessage("myForm2:errmess",
					new FacesMessage("Unable to delte the table!"));
			return "fail";
		}
		default:
			System.err.println("Bad action");
			// set error messages and codes
			return "fail";
		}
	}
	
	public String getButtonId() {
		return buttonId;
	}

	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}

	public void commandClicked(ActionEvent event) {
		/* retrieve buttonId which you clicked */
		buttonId = event.getComponent().getId();
	}

	public DbUserInfo getDbUserInfo() {
		return dbUserInfo;
	}

	public void setDbUserInfo(DbUserInfo dbUserInfo) {
		this.dbUserInfo = dbUserInfo;
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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDbSchema() {
		return dbSchema;
	}

	public void setDbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}

	public String getDbms() {
		return dbms;
	}

	public void setDbms(String dbms) {
		this.dbms = dbms;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public ResultSet getMetaDatarsmd() {
		return MetaDatarsmd;
	}

	public void setMetaDatarsmd(ResultSet metaDatarsmd) {
		MetaDatarsmd = metaDatarsmd;
	}

	public int getNumberRows() {
		return numberRows;
	}

	public void setNumberRows(int numberRows) {
		this.numberRows = numberRows;
	}

	public boolean isRenderResult() {
		return renderResult;
	}

	public void setRenderResult(boolean renderResult) {
		this.renderResult = renderResult;
	}

	public ArrayList getColumnNames() {
		return columnNames;
	}

	public int getNumberColumns() {
		return numberColumns;
	}

	public void setNumberColumns(int numberColumns) {
		this.numberColumns = numberColumns;
	}

	public void setColumnNames(ArrayList columnNames) {
		this.columnNames = columnNames;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
