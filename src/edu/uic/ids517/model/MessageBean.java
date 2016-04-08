package edu.uic.ids517.model;

public class MessageBean {
	private String message=" ";
	private String tableMessage=" ";
	/**
	 * @return the tableMessage
	 */
	public String getTableMessage() {
		return tableMessage;
	}
	/**
	 * @param tableMessage the tableMessage to set
	 */
	public void setTableMessage(String tableMessage) {
		this.tableMessage = tableMessage;
	}
	private String adminMessage=" ";
	/**
	 * @return the adminMessage
	 */
	public String getAdminMessage() {
		return adminMessage;
	}
	/**
	 * @param adminMessage the adminMessage to set
	 */
	public void setAdminMessage(String adminMessage) {
		this.adminMessage = adminMessage;
	}
	/**
	 * @return the registerMessage
	 */
	public String getRegisterMessage() {
		return registerMessage;
	}
	/**
	 * @param registerMessage the registerMessage to set
	 */
	public void setRegisterMessage(String registerMessage) {
		this.registerMessage = registerMessage;
	}
	/**
	 * @return the resetMessage
	 */
	public String getResetMessage() {
		return resetMessage;
	}
	/**
	 * @param resetMessage the resetMessage to set
	 */
	public void setResetMessage(String resetMessage) {
		this.resetMessage = resetMessage;
	}
	private String registerMessage=" ";
	private String resetMessage=" ";
	 /**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	private String errorMessage;
	/**
	 * 
	 */
	public MessageBean() {
		// TODO Auto-generated constructor stub
	}

}
