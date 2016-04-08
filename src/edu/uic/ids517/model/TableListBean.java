package edu.uic.ids517.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

@ManagedBean(name = "tableListBean")
@SessionScoped
public class TableListBean {

	private String sqlQuery;
	ResultSetMetaData rsmd;
	private Result result;
	private int numberColumns;
	private int numberRows;
	private List<String> columnNames;
	private boolean renderSet;

	FacesContext context2 = FacesContext.getCurrentInstance();
	Map<String, Object> m2 = context2.getExternalContext().getSessionMap();
	DbaseBean dbaseBean = (DbaseBean) m2.get("dbaseBean");
	Connection conn = dbaseBean.getConnection();
	ResultSet rs = null;
	Statement st = null;
	// Connection conn = dbaseBean.getConnection();
	@ManagedProperty("#{tableList}")
	private TableList[] tableList;
	@ManagedProperty("#{selectedTable}")
	private String selectedTable;

	String status = "FAIL";
	FacesContext context = FacesContext.getCurrentInstance();
	Map<String, Object> m = context.getExternalContext().getSessionMap();
	DbmsUserBean loginBean = (DbmsUserBean) m.get("dbmsUserBean");
	RegUserLoginBean regUserLoginBean = (RegUserLoginBean) m
			.get("regUserLoginBean");
	private boolean isRendered = false;

	public boolean isRendered() {
		return isRendered;
	}

	public void setRendered(boolean rendered) {
		this.isRendered = rendered;
	}

	public TableList[] getTableList() {
		try {
			String[] TABLE_TYPES = { "TABLE", "VIEW" };
			DatabaseMetaData databaseMetaData;
			String query = "";

			System.out.println(query);
			// st = conn.createStatement();
			st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			databaseMetaData = conn.getMetaData();
			rs = databaseMetaData.getTables(null, dbaseBean.getUserName(),
					null, TABLE_TYPES);
			rs.last();
			int count = rs.getRow();
			tableList = new TableList[count];
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				tableList[i] = new TableList(rs.getString("TABLE_NAME"));
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage msg = new FacesMessage("" + " Error Occured");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		setTableList(tableList);
		return tableList;
	}

	public void setTableList(TableList[] tableList2) {
		this.tableList = tableList2;
	}

	public String getSelectedTable() {
		return selectedTable;
	}

	public void setSelectedTable(String selectedItem) {
		this.selectedTable = selectedItem;
	}

	public void displayColumns(ValueChangeEvent value) {
		String selectedTable = (String) value.getNewValue();
		setSelectedTable((String) value.getNewValue());
		if (selectedTable != null) {
			isRendered = true;
		} else {
			isRendered = false;
		}
	}

	public ArrayList<String> getColumnList() {
		int i = 0;
		ArrayList<String> columnNames = new ArrayList<String>();
		try {
			String query = "Show columns from " + selectedTable + " ;";

			System.out.println(query);
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				columnNames.add(rs.getString(i));
				i++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return columnNames;
	}

	public String gotoNextPage() {
		return "viewTableColumns";
	}

	public String viewTableData() {

		sqlQuery = "select * from " + selectedTable + ";";
		try {
			dbaseBean.executeSQL(sqlQuery);
			rs = dbaseBean.getResultSet();
			rsmd = rs.getMetaData();
			result = ResultSupport.toResult(rs);
			numberColumns = rsmd.getColumnCount();
			numberRows = result.getRowCount();
			String columnNameList[] = result.getColumnNames();
			columnNames = new ArrayList<String>();
			for (int i = 0; i < numberColumns; i++) {
				columnNames.add(columnNameList[i]);
			}
			renderSet = true;
			status = "SUCCESS";
		} catch (SQLException e) {
			// TODO Auto generated catch block
			e.printStackTrace();
			renderSet = false;
			status = "FAIL";
		}
		return status;

	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public void setRsmd(ResultSetMetaData rsmd) {
		this.rsmd = rsmd;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public void setNumberColumns(int numberColumns) {
		this.numberColumns = numberColumns;
	}

	public void setNumberRows(int numberRows) {
		this.numberRows = numberRows;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public void setRenderSet(boolean renderSet) {
		this.renderSet = renderSet;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public Result getResult() {
		return result;
	}

	public int getNumberColumns() {
		return numberColumns;
	}

	public int getNumberRows() {
		return numberRows;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public boolean isRenderSet() {
		return renderSet;
	}

	public void setDbaseBean(DbaseBean dbaseBean) {
		this.dbaseBean = dbaseBean;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	public void setSt(Statement st) {
		this.st = st;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setContext(FacesContext context) {
		this.context = context;
	}

	public void setM(Map<String, Object> m) {
		this.m = m;
	}

}
