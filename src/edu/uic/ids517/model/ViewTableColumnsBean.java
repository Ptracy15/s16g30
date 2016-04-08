package edu.uic.ids517.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "viewTableColumnsBean")
@SessionScoped
public class ViewTableColumnsBean {
	String status = "FAIL";
	DbaseBean dtb;
	private Map<String, Boolean> checked = new HashMap<String, Boolean>();
	String tableName;
	public List<TableInfo> checkedItems;
	private String categorical = "yes";

	public String getCategorical() {
		return categorical;
	}

	public void setCategorical(String categorical) {
		this.categorical = categorical;
	}

	DbaseBean dbUtil = new DbaseBean();
	ResultSet rs = null;
	Statement st = null;
	Connection conn = null;
	boolean render;
	int i = 0;
	private TableInfo[] tableInfo;

	public TableInfo[] getTableInfo() {
		// TODO Auto-generated constructor stub
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		TableListBean viewTableBean = (TableListBean) m.get("tableListBean");
		DbaseBean dbase = (DbaseBean) m.get("dbaseBean");
		tableName = viewTableBean.getSelectedTable();
		String query = "Show Columns from " + tableName + " ;";
		try {
			dbase.connect();
			dbase.executeSQL(query);
			rs = dbase.getResultSet();
			rs.last();
			int count = rs.getRow();
			tableInfo = new TableInfo[count];
			rs.beforeFirst();

			if (tableInfo != null && tableInfo.length > 1) {
				if (tableInfo[0] != null) {

				} else {
					while (rs.next()) {
						tableInfo[i] = new TableInfo(rs.getString(1),
								rs.getString(2));
						i++;
					}
					i = 0;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// dbase.closeConnection();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		setTableInfo(tableInfo);
		return tableInfo;
	}

	public String displayTable() {

		if (getSelectedColumnNames().equalsIgnoreCase("SUCCESS")) {
			return "displayTable";
		} else {
			return "FAIL";
		}
	}

	public String getSelectedColumnNames() {

		checkedItems = new ArrayList<TableInfo>();

		for (TableInfo tabInfo : tableInfo) {
			if (checked.get(tabInfo.getColumn())) {
				checkedItems.add(tabInfo);
			}
		}
		checked.clear(); // If necessary.

		if (checkedItems != null) {
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			session.setAttribute("selectedColumns", checkedItems);
			session.setAttribute("tableName", tableName);
			return "SUCCESS";
		} else {
			return "FAIL";
		}
	}

	public String goBackToViewTableColumns() {

		return "viewTableColumns";
	}

	public void setTableInfo(TableInfo[] tableInfo) {
		this.tableInfo = tableInfo;
	}

	public String gotoNumericalAnalysis() {
		for (TableInfo tabInfo : tableInfo) {
			if (checked.get(tabInfo.getColumn())) {
				if (tabInfo.getDataType().substring(0, 2)
						.equalsIgnoreCase("VA")) {
					categorical = "yes";
//					return "FAIL";
					return "numericalAnalysis";
				}
			}
		}
		if (getSelectedColumnNames().equalsIgnoreCase("SUCCESS")) {
			categorical = "no";
			return "numericalAnalysis";
		} else {
			return "FAIL";
		}

	}

	public String gotoGraphAnalysis() {
		if (getSelectedColumnNames().equalsIgnoreCase("SUCCESS")) {
			return "graphicalAnalysis";
		} else {
			return "FAIL";
		}
	}

	public String gotoRegressionAnalysis() {
		for (TableInfo tabInfo : tableInfo) {
			if (checked.get(tabInfo.getColumn())) {
				if (tabInfo.getDataType().substring(0, 2)
						.equalsIgnoreCase("VA")) {
					categorical = null;
					return "FAIL";
				}
			}
		}
		if (getSelectedColumnNames().equalsIgnoreCase("SUCCESS")) {
			categorical = "no";
			return "regressionAnalysis";
		} else {
			return "FAIL";
		}
	}

	public Map<String, Boolean> getChecked() {
		return checked;
	}

	public void setChecked(Map<String, Boolean> checked) {
		this.checked = checked;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<TableInfo> getCheckedItems() {
		return checkedItems;
	}

	public void setCheckedItems(List<TableInfo> checkedItems) {
		this.checkedItems = checkedItems;
	}
}
