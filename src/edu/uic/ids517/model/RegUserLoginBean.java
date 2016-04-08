package edu.uic.ids517.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@ManagedBean (name="regUserLoginBean")
@SessionScoped
public class RegUserLoginBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private DbaseBean dbase;
	private MessageBean messageBean;
	private DataBean dataBean;
	private DbmsUserBean userInfo;
	private String userSelect;
	private RegUserLoginBean regUserLoginBean;

	private String userName;
	private String confirmPassword;

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
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

	private String password;
	private String question;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private String answer;
	private String email;

	public RegUserLoginBean() {

	}

	public String Login() {
		try {
			String pass;
			FacesContext context = FacesContext.getCurrentInstance();
			Map<String, Object> m = context.getExternalContext()
					.getSessionMap();
			dbase = (DbaseBean) m.get("dbaseBean");
			messageBean = (MessageBean) m.get("messageBean");
			dataBean = (DataBean) m.get("dataBean");
			userInfo = (DbmsUserBean) m.get("dbmsUserBean");
			regUserLoginBean = (RegUserLoginBean) m.get("regUserLoginBean");
			HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
			userInfo.setSessionId(session.getId());
			userInfo.setUserName(getPropValues("userName"));
			userInfo.setPassword(getPropValues("password"));
			dbase.setQueryType("SELECT");
			dbase.setDbmsUserBean(userInfo);
			if (dbase.getConnection() != null) {
				dbase.closeConnection();
			}
			dbase.connect();
			
			pass = dbase.retrieveElement();
			if (pass.equals(regUserLoginBean.getPassword())) {
				Date date = new Date();
				Timestamp ts = new Timestamp(date.getTime());
				String datetime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
						.format(ts);
				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
				String ipAddress = request.getHeader("X-FORWARDED-FOR");
				if (ipAddress == null) {
				    ipAddress = request.getRemoteAddr();
				}
				String details = "\'" + regUserLoginBean.getUserName() + "\'"
						+ ",\'" + userInfo.getDbmsHost() + "\'" + ",\'"
						+ userInfo.getDatabaseSchema() + "\'" + ",\'"
						+ userInfo.getDbmsHost() + "\'" + ",\'"
						+ ipAddress + "\'" + ",\'"
						+ userInfo.getSessionId() + "\'" + ",\'" + datetime
						+ "\'" + ",\'" +"Not Yet" +"\')";
				return "LoginSuccess";
			} else {
				FacesContext.getCurrentInstance().addMessage(
						"myForm:newPassword1",
						new FacesMessage("Login Failed!"));
				return "LoginFailed";
			}
		} catch (SQLException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "Exception";
		}
	}
	public String getPropValues(String key) throws IOException {
		 
		String result = "";
		Properties prop = new Properties();
		String propFileName = "config.properties";
 
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		prop.load(inputStream);
		if (inputStream == null) {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}

		result = prop.getProperty(key);
		return result;
	}

}
