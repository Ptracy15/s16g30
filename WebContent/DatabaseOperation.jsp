<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Database Operation</title>
</head>
<body>
<f:view>

<h3 align="center">IDS517 s16g30 Data Analysis</h3>
	<hr /> <br />
		<div align="center">
			<a href = "dbAccess.jsp">Home</a>&nbsp;&nbsp;&nbsp;
					<a href="DataAnalysis.jsp">Data Analysis</a> &nbsp;&nbsp;&nbsp;
					<a href="fileExport.jsp">File Export</a>&nbsp;&nbsp;&nbsp;
					<a href="DatabaseOperation.jsp">DB Operation</a>&nbsp;&nbsp;&nbsp;
	<br /><br />
</div>
		<h:form id="myForm2">
				<div align="center">
			<h:outputLabel value="Select Table"></h:outputLabel>
						<br>
					
						<br>
			<h:selectOneMenu id="here" rendered="true"
							value="#{dbmsUserBean.tableName}"
							valueChangeListener="#{tableListBean.displayColumns}"
							required="true" requiredMessage="  Select a table">
							<f:selectItem itemValue="#{null}" itemLabel="Choose one" />
							<f:selectItems value="#{tableListBean.tableList}" var="table"
								itemValue="#{table.tableName}" itemLabel="#{table.tableName}" />
			</h:selectOneMenu>
						
			<div>
				
				<h:panelGrid columns="1">
						<h:commandButton action="#{dbaccess.dbOperation}" id="CopyTable" value="Copy Table" actionListener="#{dbaccess.commandClicked}"></h:commandButton>
						&nbsp;
						<h:commandButton action="#{dbaccess.dbOperation}" id="userdrop" value="Drop Table" actionListener="#{dbaccess.commandClicked}"></h:commandButton>
				</h:panelGrid>
						
				</div>
			</div>
		</h:form>
</f:view>
</body>
</html>