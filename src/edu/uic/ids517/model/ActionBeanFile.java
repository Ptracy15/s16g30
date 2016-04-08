package edu.uic.ids517.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;


@ManagedBean
@SessionScoped
public class ActionBeanFile implements Serializable {

	private static final long serialVersionUID = 1L;
	private DbaseBean dbase;
	private MessageBean message;
	private DataBean dataBean;
	private DbmsUserBean dbmsUser;
	private int downlaodedrows;

	public int getDownlaodedrows() {
		return downlaodedrows;
	}

	public void setDownlaodedrows(int downlaodedrows) {
		this.downlaodedrows = downlaodedrows;
	}

	public ActionBeanFile() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dbase = (DbaseBean) m.get("dbaseBean");
		message = (MessageBean) m.get("messageBean");
		dataBean = (DataBean) m.get("dataBean");
		dbmsUser = (DbmsUserBean) m.get("dbmsUser");
	}

	public String processFileDownload() {

		DataBaseBean ckear = new DataBaseBean();
		ckear.clearContens();
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		Map<String, Object> m = fc.getExternalContext().getSessionMap();
		dbase = (DbaseBean) m.get("dbaseBean");
		message = (MessageBean) m.get("messageBean");
		dataBean = (DataBean) m.get("dataBean");
		dbmsUser = (DbmsUserBean) m.get("dbmsUserBean");
		FileOutputStream fos = null;

		String path = fc.getExternalContext().getRealPath("/temp");
		String tableName = dbmsUser.getTableName();
		String fileNameBase = tableName + ".csv";
		java.net.URL check = getClass().getClassLoader().getResource(
				"config.properties");
		File check2 = new File(check.getPath());
		path = check2.getParent();
		String fileName = path + "/" + dbmsUser.getUserName() + "_"
				+ fileNameBase;

		File f = new File(fileName);
		try {
			f.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		dbase.connect();
		dbase.setQueryType("SELECT");
		dbase.executeSQL("select * from " + tableName);
		if (dbase.getResultSet() == null) {
			FacesContext.getCurrentInstance().addMessage("myForm3:errmess",
					new FacesMessage("Table doesn't exist!"));
			return "failed";
		}
		Result result = ResultSupport.toResult(dbase.getResultSet());
		downlaodedrows = result.getRowCount();
		Object[][] sData = result.getRowsByIndex();
		String columnNames[] = result.getColumnNames();
		StringBuffer sb = new StringBuffer();
		try {
			fos = new FileOutputStream(fileName);

			for (int i = 0; i < columnNames.length; i++) {
				sb.append(columnNames[i].toString() + ",");
			}

			sb.append("\n");

			fos.write(sb.toString().getBytes());

			for (int i = 0; i < sData.length; i++) {
				sb = new StringBuffer();
				for (int j = 0; j < sData[0].length; j++) {
					sb.append(sData[i][j].toString() + ",");
				}

				sb.append("\n");
				fos.write(sb.toString().getBytes());
			}

			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		String mimeType = ec.getMimeType(fileName);
		FileInputStream in = null;
		byte b;

		ec.responseReset();
		ec.setResponseContentType(mimeType);
		ec.setResponseContentLength((int) f.length());
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\""
				+ fileNameBase + "\"");

		try {
			in = new FileInputStream(f);
			OutputStream output = ec.getResponseOutputStream();
			while (true) {
				b = (byte) in.read();
				if (b < 0)
					break;
				output.write(b);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		fc.responseComplete();
		return "SUCCESS";

	}

	public String processXmlDownload() throws IOException, SQLException {
		DataBaseBean ckear = new DataBaseBean();
		ckear.clearContens();
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		Map<String, Object> m = fc.getExternalContext().getSessionMap();
		dbase = (DbaseBean) m.get("dbaseBean");
		message = (MessageBean) m.get("messageBean");
		dataBean = (DataBean) m.get("dataBean");
		dbmsUser = (DbmsUserBean) m.get("dbmsUserBean");
		FileOutputStream fos = null;

		String path = fc.getExternalContext().getRealPath("/temp");
		String tableName = dbmsUser.getTableName();
		String fileNameBase = tableName + ".xml";
		java.net.URL check = getClass().getClassLoader().getResource(
				"config.properties");
		File check2 = new File(check.getPath());
		path = check2.getParent();
		String fileName = path + "/" + dbmsUser.getUserName() + "_"
				+ fileNameBase;
		File f = new File(fileName);
		try {
			f.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		dbase.connect();
		dbase.setQueryType("SELECT");
		dbase.executeSQL("select * from " + tableName);
		ResultSet resultSet = dbase.getResultSet();
		Result result = ResultSupport.toResult(dbase.getResultSet());
		downlaodedrows = result.getRowCount();
		if (resultSet == null) {
			FacesContext.getCurrentInstance().addMessage("myForm3:errmess",
					new FacesMessage("Table doesn't exist!"));
			return "failed";
		}
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int totalNumColums = rsmd.getColumnCount();
		/*
		 * result = ResultSupport.toResult(resultSet); int rowCount
		 * =result.getRowCount();
		 */

		StringBuffer b = new StringBuffer(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<table>\n");
		int num = 0;
		if (resultSet != null) {
			num = 1;
		}
		// System.out.println(resultSet);
		while (dbase.getResultSet().next()) {
			b.append("<row").append(num).append(">\n");
			// b.append("<num>").append(num++).append("</num>");
			for (int i = 0; i <= totalNumColums - 1; i++) {
				String columnName = rsmd.getColumnName(i + 1);
				b.append('<').append(columnName).append(">");
				b.append(resultSet.getObject(i + 1));
				b.append("</").append(columnName).append(">\n");
			}
			b.append("</row").append(num).append(">\n");
			num++;
		}
		b.append("</table>");

		fos = new FileOutputStream(fileName);
		fos.write(b.toString().getBytes());
		fos.flush();
		fos.close();
		String mimeType = ec.getMimeType(fileName);
		FileInputStream in = null;
		byte sb;

		ec.responseReset();
		ec.setResponseContentType(mimeType);
		ec.setResponseContentLength((int) f.length());
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\""
				+ fileNameBase + "\"");

		try {
			in = new FileInputStream(f);
			OutputStream output = ec.getResponseOutputStream();
			while (true) {
				sb = (byte) in.read();
				if (sb < 0)
					break;
				output.write(sb);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		fc.responseComplete();
		return "SUCCESS";
	}
}
