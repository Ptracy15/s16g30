<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Data Analysis</title>
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
		
	<center>
		<h2>Select table for data analysis</h2>
		<h:form>
			<h:column>
				<h:outputLabel>Table List:</h:outputLabel>
					<h:selectOneMenu styleClass="SelectOnelistbox_mono" rendered="true"
					value="#{tableListBean.selectedTable}"
					valueChangeListener="#{tableListBean.displayColumns}"
					required="true" requiredMessage="Select a table">
					<f:selectItem itemValue="" itemLabel="Choose one..." />
					<f:selectItems value="#{tableListBean.tableList}" var="table"
						itemValue="#{table.tableName}" itemLabel="#{table.tableName}" />
				</h:selectOneMenu>
			</h:column>
			<br />
			<br />
			<h:commandButton styleClass="Analyze-button margin"
				value="Analyze data" action="#{tableListBean.gotoNextPage}" />
			<br />
			<br />
			<br />
		</h:form>
	</center>
</f:view>
</body>
</html>