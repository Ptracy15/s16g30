<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>File Export</title>
</head>
<body>
		<f:view>

			<center>
				<h3>File Export</h3>
				<f:verbatim>
						<div align="center">
							<h:form>
			<a href = "dbAccess.jsp">Home</a>&nbsp;&nbsp;&nbsp;
					<a href="DataAnalysis.jsp">Data Analysis</a> &nbsp;&nbsp;&nbsp;
					<a href="fileExport.jsp">File Export</a>&nbsp;&nbsp;&nbsp;
					<a href="DatabaseOperation.jsp">DB Operation</a>&nbsp;&nbsp;&nbsp;						
								<br>
							</h:form>
							<br>
					</div>
				</f:verbatim>

			</center>
			<br />
			<br />
			<center>
				<h:form id="myForm3">
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
						<h:message for="here" style="color:red"></h:message>
						<br /> <br />

						<h:panelGrid columns="2">
							<h:commandButton action="#{actionBeanFile.processFileDownload}"
								id="Export" styleClass="button margin2" value="Export as CSV"
								actionListener="#{dbaccess.commandClicked}"></h:commandButton>
							<h:commandButton action="#{actionBeanFile.processXmlDownload}"
								id="Exportxml" styleClass="button margin2"
								value="Export as XML"
								actionListener="#{dbaccess.commandClicked}"></h:commandButton>
						</h:panelGrid>

						<br /> <br /> <br />

						
					</div>

				</h:form>
			</center>
		</f:view>
</body>
</html>