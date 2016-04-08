/**
 * 
 */
package edu.uic.ids517.model;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean ( name = "userInfo")
@SessionScoped
public class DbmsUserBean implements Serializable {

		private static final long serialVersionUID = 1L;
		private String userType;
		private String userName;
		private String password;
		private String dbmsHost;
		private String dbms;
		private String databaseSchema;
		private String sessionId;
		private String clientIp;
		private String tableName;
		private String orgUser;
		
		//Default Constructor
		public DbmsUserBean() {
			
		}
		
		public String getTableName() {
			return tableName;
		}
		
		public void setTableName(String tableName) {
			this.tableName = tableName;
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

		public String getDbmsHost() {
			return dbmsHost;
		}

		public void setDbmsHost(String dbmsHost) {
			this.dbmsHost = dbmsHost;
		}

		public String getDbms() {
			return dbms;
		}

		public void setDbms(String dbms) {
			this.dbms = dbms;
		}

		public String getDatabaseSchema() {
			return databaseSchema;
		}

		public void setDatabaseSchema(String databaseSchema) {
			this.databaseSchema = databaseSchema;
		}

		public String getSessionId() {
			return sessionId;
		}
		
		public void setSessionId(String sessionId) {
			this.sessionId = sessionId;
		}

		public String getClientIp() {
			return clientIp;
		}

		public void setClientIp(String clientIp) {
			this.clientIp = clientIp;
		}
		public String getUserType() {
			return userType;
		}

		public void setUserType(String userType) {
			this.userType = userType;
		}
	

		public String getOrgUser() {
		return orgUser;
		}
		public void setOrgUser(String orgUser) {
		this.orgUser = orgUser;
		}

}
