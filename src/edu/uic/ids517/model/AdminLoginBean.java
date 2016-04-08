
package edu.uic.ids517.model;

import java.io.Serializable;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;

@ManagedBean (name="adminLoginBean")
@SessionScoped
public class AdminLoginBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private DbaseBean dbase;
	private MessageBean messageBean;
	private DataBean dataBean;
	private DbmsUserBean userInfo;
	public AdminLoginBean() {

	}
	
public String AdminLogin()
{
	try {
		
		FacesContext context = FacesContext.getCurrentInstance();
		 Map <String, Object> m = context.getExternalContext().getSessionMap();
		 dbase = (DbaseBean) m.get("dbaseBean");
		 messageBean = (MessageBean) m.get("messageBean");
		dataBean = (DataBean) m.get("dataBean");
		userInfo = (DbmsUserBean) m.get("dbmsUserBean");
		HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
		userInfo.setSessionId(session.getId());
		dbase.setDbmsUserBean(userInfo);
		if(dbase.getConnection()!=null)
		{
			dbase.closeConnection();	
		}
		boolean status=dbase.connect();
		if(status==true){
			return "adminSuccess";
		}
		else {
			FacesContext.getCurrentInstance().addMessage(
					"myForm2:errmess",
					new FacesMessage("Login Failed! Try login again or go to home if you are a regular user!"));
			return "adminFailed";
			
		}
		}
		catch(Exception e){
			FacesContext.getCurrentInstance().addMessage(
					"myForm2:errmess",
					new FacesMessage("Some error occurred while logging in. Please try again later!"));
			return "adminFailed";
			//TODO
		}
}

}
