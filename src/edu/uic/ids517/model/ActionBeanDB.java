package edu.uic.ids517.model;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean (name="actionBeanDB")
@SessionScoped
public class ActionBeanDB implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty (value="#{dbaccess}")
	private DBAccessBean dbaccess;

	public String processLogin() {
		String result = "FAIL";
		boolean res = false;
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		DBAccessBean dbaccessBean = (DBAccessBean) m.get("dbaccess");
		DbmsUserBean dbmsUserBean = new DbmsUserBean();
		dbmsUserBean.setDatabaseSchema(dbaccessBean.getDbSchema());
		dbmsUserBean.setDbmsHost(dbaccessBean.getHost());
		dbmsUserBean.setDbms(dbaccessBean.getDbms());
		dbmsUserBean.setPassword(dbaccessBean.getPassword());
		dbmsUserBean.setUserName(dbaccessBean.getUserName());
		dbmsUserBean.setSessionId(request.getSession().getId());
		m.put("dbmsUserBean", dbmsUserBean);
		DbaseBean dbaseBean = new DbaseBean();
		dbaseBean.connect();
		try {
			res = dbaccess.connectToDB();
			if (res) {
				result = "SUCCESS";
			}
		} catch (SQLException e) {
			result = "FAIL";
			e.printStackTrace();
		}
		return result;
	}

	public String processLogout() {
		String result = "FAIL";
		boolean res = false;

		res = dbaccess.closeConnection();
		if (res) {
			result = "YES";
		} else {
			result = "NO";
		}

		return result;
	}

	public ActionBeanDB() {
	}

	public DBAccessBean getDbaccess() {
		return dbaccess;
	}

	public void setDbaccess(DBAccessBean dbaccess) {
		this.dbaccess = dbaccess;
	}
}
