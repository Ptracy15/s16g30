package edu.uic.ids517.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;




import edu.uic.ids517.dbaccess.DBDetailsInfo;
import edu.uic.ids517.dbaccess.DBase;


@ManagedBean ( name="dbBean")
@SessionScoped
public class DataBaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<String> lisTableList = new ArrayList<String>();
	private ArrayList<String> lisColumnList = new ArrayList<String>();
	private ArrayList<String> lisDisplayTable = new ArrayList<String>();
	private ArrayList<String> lisDiaplaySelectedColumns = new ArrayList<String>();
	private ArrayList<String> lisDaplayResultSet = new ArrayList<String>();
	private String strSelectTableList = new String();
	private String strSelectColumnList[];
	private String strTextArea = new String();
	private String strSqlQuery = new String();
	private int iNosColumns;
	private int iNosRows;

	private String sqlQuery;
	ResultSetMetaData rsmd;
	private Result result;
	private int numberColumns;
	private int numberRows;
	private List<String> columnNames;
	private boolean renderSet;

	private String selectedTable;

	String status = "FAIL";
	FacesContext context = FacesContext.getCurrentInstance();
	ResultSet rs = null;
	Statement st = null;
	private Connection conn;
	DBDetailsInfo dbinfo = new DBDetailsInfo();

	public DataBaseBean() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		DBAccessBean dbaccessBean = (DBAccessBean) m.get("dbaccess");

		dbinfo.setDatabaseSchema(dbaccessBean.getDbSchema());
		dbinfo.setDbms(dbaccessBean.getDbms());
		dbinfo.setDbmsHost(dbaccessBean.getHost());
		dbinfo.setPassword(dbaccessBean.getPassword());
		dbinfo.setUserName(dbaccessBean.getUserName());

		DBase dbAct = new DBase();
		ConnectDB(dbinfo, dbAct);
		lisTableList = dbAct.getLisTableList();
		// dbAct.CloseConnect();
	}

	public String processTableList() {

		DBase dbAct = new DBase();
		ConnectDB(dbinfo, dbAct);
		lisTableList = dbAct.getLisTableList();
		// dbAct.CloseConnect();
		return "SUCCESS";
	}

	public String processColumnList() {
		DBase dbAct = new DBase();
		ConnectDB(dbinfo, dbAct);
		lisColumnList = dbAct.getCoulumns(dbinfo, strSelectTableList);
		// dbAct.CloseConnect();
		renderSet = false;
		return "SUCCESS";
	}

	public String processDisplayTable() {
		DBase dbAct = new DBase();
		ConnectDB(dbinfo, dbAct);
		String sqlQuerry = "Select * from " + strSelectTableList;
		strTextArea = "Select * from " + strSelectTableList;
		try {
			rs = dbAct.executeSQLQuerry(sqlQuerry);
			rsmd = rs.getMetaData();
			result = ResultSupport.toResult(rs);
			numberColumns = rsmd.getColumnCount();
			numberRows = result.getRowCount();
			iNosColumns = numberColumns;
			iNosRows = numberRows;
			String columnNameList[] = result.getColumnNames();
			columnNames = new ArrayList<String>();
			for (int i = 0; i < numberColumns; i++) {
				columnNames.add(columnNameList[i]);
			}
			renderSet = true;
			status = "SUCCESS";
		} catch (SQLException e) {
			// TODO Auto â€?generated catch block
			e.printStackTrace();
			renderSet = false;
			status = "FAIL";
		}

		return "SUCCESS";
	}

	public String clearContens() {

		lisTableList = new ArrayList<String>();
		lisColumnList = new ArrayList<String>();
		lisDisplayTable = new ArrayList<String>();
		lisDiaplaySelectedColumns = new ArrayList<String>();
		lisDaplayResultSet = new ArrayList<String>();
		renderSet = false;
		rs = null;
		strSelectTableList = new String();
		strTextArea = new String();
		strSqlQuery = new String();
		result = null;
		iNosColumns = 0;
		iNosRows = 0;
		sqlQuery = null;

		return "SUCCESS";
	}

	public String processSelectedColumns() {

		// DBDetailsInfo dbinfo = new DBDetailsInfo();
		DBase dbAct = new DBase();
		ConnectDB(dbinfo, dbAct);
		String sqlQuerry = new String();
		String str = new String();
		str = strSelectColumnList[0];
		for (int i = 1; i < strSelectColumnList.length; i++) {
			str = str + "," + strSelectColumnList[i];
		}
		sqlQuerry = "Select " + str + " from " + strSelectTableList;
		System.out.println("STR" + str);
		strTextArea = sqlQuerry;
		try {
			rs = dbAct.executeSQLQuerry(sqlQuerry);
			rsmd = rs.getMetaData();
			result = ResultSupport.toResult(rs);
			numberColumns = rsmd.getColumnCount();
			numberRows = result.getRowCount();
			iNosColumns = numberColumns;
			iNosRows = numberRows;
			String columnNameList[] = result.getColumnNames();
			columnNames = new ArrayList<String>();
			for (int i = 0; i < numberColumns; i++) {
				columnNames.add(columnNameList[i]);
			}
			renderSet = true;
			status = "SUCCESS";
		} catch (SQLException e) {
			// TODO Auto â€?generated catch block
			e.printStackTrace();
			renderSet = false;
			status = "FAIL";
		}

		return "SUCCESS";

	}

	public String procesSQLQuery() {
		// DBDetailsInfo dbinfo = new DBDetailsInfo();
		DBase dbAct = new DBase();
		System.out.println(strTextArea);
		ConnectDB(dbinfo, dbAct);
		if (strTextArea.startsWith("Select")
				|| strTextArea.startsWith("select")) {
			try {
				rs = dbAct.executeSQLQuerry(strTextArea);
				rsmd = rs.getMetaData();
				result = ResultSupport.toResult(rs);
				numberColumns = rsmd.getColumnCount();
				numberRows = result.getRowCount();
				iNosColumns = numberColumns;
				iNosRows = numberRows;
				String columnNameList[] = result.getColumnNames();
				columnNames = new ArrayList<String>();
				for (int i = 0; i < numberColumns; i++) {
					columnNames.add(columnNameList[i]);
				}
				renderSet = true;
				status = "SUCCESS";
			} catch (SQLException e) {
				// TODO Auto â€?generated catch block
				e.printStackTrace();
				renderSet = false;
				status = "FAIL";
			}

			return "SUCCESS";

		} else {
			if (dbAct.executeUpdate(strTextArea)) {
				return "SUCCESS";
			} else {
				return "FAILURE";
			}
		}

	}

	public String processCreateTable() {
		return "";
	}

	public String processDropTable() {
		return "";
	}

	public Boolean ConnectDB(DBDetailsInfo dbinfo, DBase db) {

		db.connect(dbinfo);
		return true;

	}

	public ArrayList<String> getLisTableList() {
		return lisTableList;
	}

	public void setLisTableList(ArrayList<String> lisTableList) {
		this.lisTableList = lisTableList;
	}

	public ArrayList<String> getLisColumnList() {
		return lisColumnList;
	}

	public void setLisColumnList(ArrayList<String> lisColumnList) {
		this.lisColumnList = lisColumnList;
	}

	public ArrayList<String> getLisDisplayTable() {
		return lisDisplayTable;
	}

	public void setLisDisplayTable(ArrayList<String> lisDisplayTable) {
		this.lisDisplayTable = lisDisplayTable;
	}

	public ArrayList<String> getLisDiaplaySelectedColumns() {
		return lisDiaplaySelectedColumns;
	}

	public void setLisDiaplaySelectedColumns(
			ArrayList<String> lisDiaplaySelectedColumns) {
		this.lisDiaplaySelectedColumns = lisDiaplaySelectedColumns;
	}

	public ArrayList<String> getLisDaplayResultSet() {
		return lisDaplayResultSet;
	}

	public void setLisDaplayResultSet(ArrayList<String> lisDaplayResultSet) {
		this.lisDaplayResultSet = lisDaplayResultSet;
	}

	public String getStrSelectTableList() {
		return strSelectTableList;
	}

	public void setStrSelectTableList(String strSelectTableList) {
		this.strSelectTableList = strSelectTableList;
	}

	public String[] getStrSelectColumnList() {
		return strSelectColumnList;
	}

	public void setStrSelectColumnList(String[] strSelectColumnList) {
		this.strSelectColumnList = strSelectColumnList;
	}

	public String getStrTextArea() {
		return strTextArea;
	}

	public void setStrTextArea(String strTextArea) {
		this.strTextArea = strTextArea;
	}

	public String getStrSqlQuery() {
		return strSqlQuery;
	}

	public void setStrSqlQuery(String strSqlQuery) {
		this.strSqlQuery = strSqlQuery;
	}

	public int getiNosColumns() {
		return iNosColumns;
	}

	public void setiNosColumns(int iNosColumns) {
		this.iNosColumns = iNosColumns;
	}

	public int getiNosRows() {
		return iNosRows;
	}

	public void setiNosRows(int iNosRows) {
		this.iNosRows = iNosRows;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public ResultSetMetaData getRsmd() {
		return rsmd;
	}

	public void setRsmd(ResultSetMetaData rsmd) {
		this.rsmd = rsmd;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public int getNumberColumns() {
		return numberColumns;
	}

	public void setNumberColumns(int numberColumns) {
		this.numberColumns = numberColumns;
	}

	public int getNumberRows() {
		return numberRows;
	}

	public void setNumberRows(int numberRows) {
		this.numberRows = numberRows;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public boolean isRenderSet() {
		return renderSet;
	}

	public void setRenderSet(boolean renderSet) {
		this.renderSet = renderSet;
	}

	public String getSelectedTable() {
		return selectedTable;
	}

	public void setSelectedTable(String selectedTable) {
		this.selectedTable = selectedTable;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public FacesContext getContext() {
		return context;
	}

	public void setContext(FacesContext context) {
		this.context = context;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	public Statement getSt() {
		return st;
	}

	public void setSt(Statement st) {
		this.st = st;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

}
