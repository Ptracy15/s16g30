package edu.uic.ids517.model;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


@ManagedBean (name="logOutBean")
@SessionScoped
public class LogOutBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private DbaseBean dbase;

	private RegUserLoginBean regUserLoginBean;
	private DbmsUserBean userInfo;

	public LogOutBean() {
		// TODO Auto-generated constructor stub
	}

	public String goLoginPage() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		DBAccessBean dbaccessBean = (DBAccessBean) m.get("dbaccess");
		dbase = (DbaseBean) m.get("dbaseBean");
		userInfo = (DbmsUserBean) m.get("dbmsUserBean");
		regUserLoginBean = (RegUserLoginBean) m.get("regUserLoginBean");
		try {
			dbase.closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FacesContext.getCurrentInstance().getExternalContext()
				.invalidateSession();
		return "logout";

	}

}
