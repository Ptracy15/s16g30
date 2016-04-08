package edu.uic.ids517.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

public class DBQueries {
	FacesContext context2 = FacesContext.getCurrentInstance();
	Map<String, Object> m2 = context2.getExternalContext().getSessionMap();
	DbaseBean dbUtil = (DbaseBean) m2.get("dbaseBean");

	public String exportDataToCSV(String tableName, String fileLocation)
			throws SQLException, ClassNotFoundException {
		String query = "";
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		String status = "FAIL";

		Map<Integer, String> headerMap = getColumnsNames(tableName);

		if (headerMap != null && headerMap.size() > 0) {
			try {
				conn = (Connection) dbUtil.getConnection();

				query = "SELECT ";
				for (int i = 1; i <= headerMap.size(); i++) {
					query += "'" + headerMap.get(i) + "',";
				}
				query = query.substring(0, query.lastIndexOf(","))
						+ " UNION ALL" + " SELECT ";

				for (int j = 1; j <= headerMap.size(); j++) {
					query += headerMap.get(j) + ",";
				}
				query = query.substring(0, query.lastIndexOf(","))
						+ // remove the last comma
						" into OUTFILE '" + fileLocation + "/" + tableName
						+ ".csv'FIELDS TERMINATED BY ',' FROM " + tableName
						+ "";
				// "OBSERVATION,PRICE,SF,BR,BATH,GARAGE,ZIP into OUTFILE  'outfile' FIELDS TERMINATED BY ',' FROM tab1 r";
				System.out.println(query);
				st = (Statement) conn.createStatement();
				rs = st.executeQuery(query);
				status = "SUCCESS";
				// ResultSetMetaData metaData = rs.getMetaData();

			} catch (Exception e) {
				e.printStackTrace();
				st = null;
			} finally {
				if (rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
					}
				if (st != null)
					try {
						st.close();
					} catch (SQLException e) {
					}
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
					}
			}
		}

		return status;
	}

	public String exportTableToXML(String tableName) throws SQLException,
			ClassNotFoundException {
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		try {
			conn = (Connection) dbUtil.getConnection();

			st = (Statement) conn.createStatement();
			rs = st.executeQuery("select * from " + tableName);

			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
			int colCount = rsmd.getColumnCount();

			StringBuilder b = new StringBuilder("<table>\n");

			int num = 1;
			while (rs.next()) {
				b.append("<row>");
				b.append("<num>").append(num++).append("</num>");
				for (int i = 1; i <= colCount; i++) {
					String columnName = rsmd.getColumnName(i);
					b.append('<').append(columnName).append('>');
					b.append(rs.getObject(i));
					b.append("</").append(columnName).append('>');
				}
				b.append("</row>\n");
			}
			b.append("</table>");
			return b.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
	}

	public String exportTableToCSV(String tableName) throws SQLException,
			ClassNotFoundException {
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		// DbaseBean dbUtil;
		try {
			conn = (Connection) dbUtil.getConnection();

			st = (Statement) conn.createStatement();
			rs = st.executeQuery("select * from " + tableName);

			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			StringBuilder b = new StringBuilder("");
			for (int i = 1; i <= colCount; i++) {
				if (i != 1)
					b.append(",");

				String columnName = rsmd.getColumnName(i);
				b.append(columnName);
			}
			b.append("\n");
			int num = 1;
			while (rs.next()) {
				for (int i = 1; i <= colCount; i++) {
					if (i != 1)
						b.append(",");

					b.append(rs.getObject(i));
				}
				b.append("\n");
			}
			return b.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw e;
				}
			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw e;
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw e;
				}
		}
	}

	public Map<Integer, String> getColumnsNames(String tableName)
			throws SQLException, ClassNotFoundException {

		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		// DbaseBean dbUtil;
		Map<Integer, String> headerMap = new HashMap<Integer, String>();
		try {
			conn = (Connection) dbUtil.getConnection();

			st = (Statement) conn.createStatement();
			rs = st.executeQuery("show columns from " + tableName);

			int num = 1;
			while (rs.next()) {
				String col = rs.getObject("Field").toString();
				headerMap.put(num, col);
				num++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
		return headerMap;

	}

	public String deleteTable(String tableName, String netId)
			throws SQLException, ClassNotFoundException {

		String query1;
		String query2;
		int result = 0;
		Statement st = null;
		Connection conn = null;
		// DbaseBean dbUtil;
		String status = "FAIL";
		try {
			conn = (Connection) dbUtil.getConnection();

			st = (Statement) conn.createStatement();
			query1 = "DROP TABLE " + tableName + ";";
			result = st.executeUpdate(query1);
			System.out.println(result);
			query2 = "Delete from prof_num_anal_tableslist where professor_netid='"
					+ netId + "' and table_name='" + tableName + "';";
			dbUtil.executeSQL(query2);
			System.out.println(query2);
			status = "SUCCESS";
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
		return status;

	}

	// student tables delete function

	public String deleteTableStudent(String tableName, String netId)
			throws SQLException, ClassNotFoundException {

		String query4;
		String query3;
		int result = 0;
		Statement st = null;
		Connection conn = null;
		// DbaseBean dbUtil;
		String status = "FAIL";
		try {
			conn = (Connection) dbUtil.getConnection();

			st = (Statement) conn.createStatement();
			query4 = "DROP TABLE " + tableName + ";";
			result = st.executeUpdate(query4);
			System.out.println(result);
			query3 = "Delete from stud_num_anal_tableslist where student_netid='"
					+ netId + "' and table_name='" + tableName + "';";
			dbUtil.executeSQL(query3);
			System.out.println(query3);
			status = "SUCCESS";
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
		return status;

	}

	public List<List<String>> getTableDataForColumns(String[] dynamicHeaders,
			String tableName) throws SQLException, ClassNotFoundException {

		String query;
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		// DbaseBean dbUtil;
		List<List<String>> dynamicList = new ArrayList<List<String>>();
		List<String> rowElements;

		try {
			conn = (Connection) dbUtil.getConnection();

			query = "SELECT ";

			for (int i = 0; i < dynamicHeaders.length; i++) {
				query += dynamicHeaders[i] + ",";
			}
			query = query.substring(0, query.lastIndexOf(",")) + " FROM "
					+ tableName;

			st = (Statement) conn.createStatement();
			rs = st.executeQuery(query);

			while (rs.next()) {
				rowElements = new ArrayList<String>();
				for (int i = 0; i < dynamicHeaders.length; i++) {
					rowElements.add(i, rs.getObject(dynamicHeaders[i])
							.toString());
				}
				dynamicList.add(rowElements);
				rowElements = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
		return dynamicList;

	}

	public List<Double> getTableDataForColumnsAnalysis(String[] dynamicHeaders,
			String tableName) throws SQLException, ClassNotFoundException {

		String query;
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		// DbaseBean dbUtil;
		List<Double> colData = new ArrayList<Double>();

		try {
			conn = (Connection) dbUtil.getConnection();

			query = "SELECT ";

			for (int i = 0; i < dynamicHeaders.length; i++) {
				query += dynamicHeaders[i] + ",";
			}
			query = query.substring(0, query.lastIndexOf(",")) + " FROM "
					+ tableName;

			st = (Statement) conn.createStatement();
			rs = st.executeQuery(query);

			int num = 0;
			while (rs.next()) {
				for (int i = 0; i < dynamicHeaders.length; i++) {
					colData.add(num, Double.parseDouble(rs.getObject(
							dynamicHeaders[i]).toString()));
					num++;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} /*
		 * finally { if (rs != null) try { rs.close(); } catch (SQLException e)
		 * { } if (st != null) try { st.close(); } catch (SQLException e) { } if
		 * (conn != null) try { conn.close(); } catch (SQLException e) { } }
		 */
		return colData;

	}

	public List<Double> getTableDataForGraphAnalysis(String[] dynamicHeaders,
			String tableName) throws SQLException, ClassNotFoundException {

		String query;
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		// DbaseBean dbUtil;
		List<Double> colData = new ArrayList<Double>();
		List<Double> rowElements;

		try {
			conn = (Connection) dbUtil.getConnection();

			query = "SELECT ";

			for (int i = 0; i < dynamicHeaders.length; i++) {
				query += dynamicHeaders[i] + ",";
			}
			query = query.substring(0, query.lastIndexOf(",")) + " FROM "
					+ tableName;

			st = (Statement) conn.createStatement();
			rs = st.executeQuery(query);

			int num = 0;
			while (rs.next()) {
				rowElements = new ArrayList<Double>();

				for (int i = 0; i < dynamicHeaders.length; i++) {

					colData.add(num, Double.parseDouble(rs.getObject(
							dynamicHeaders[i]).toString()));
					num++;
				}
				rowElements.addAll(colData);
				rowElements = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
		return colData;

	}

}
