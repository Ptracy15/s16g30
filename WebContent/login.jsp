<?xml version="1.0" encoding="ISO-8859-1" ?>
<jsp:root
xmlns:jsp="http://java.sun.com/JSP/Page"
xmlns:f="http://java.sun.com/jsf/core"
xmlns:h="http://java.sun.com/jsf/html"
xmlns:t="http://myfaces.apache.org/tomahawk"
version="2.2">
 <jsp:directive.page language="java"
 contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" />
 <jsp:text>
 <![CDATA[ <?xml version="1.0" encoding="ISO-8859-1" ?> ]]>
 </jsp:text>
 <jsp:text>
 <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
 </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Log In</title>
<style>
table.gridtable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
}
table.gridtable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
}
table.gridtable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
}
</style>
</head>
<body>
<f:view>
<h3 align="center">IDS 517 s16g30 Login</h3>
	<div align="center">
		<h:form>
			<h:panelGrid columns="2" styleClass="gridtable">

				<h:outputText value="Username *" />
				<h:inputText id="userName" value="#{dbaccess.userName}" />

				<h:outputText value="Password *" />
				<h:inputSecret id="password" value="#{dbaccess.password}" />

				<h:outputText value="Database *" />
				<h:selectOneListbox value="#{dbaccess.dbms}" size="3">
					<f:selectItem itemValue="MySql" />
					<f:selectItem itemValue="DB2" />
					<f:selectItem itemValue="Oracle" />
				</h:selectOneListbox>

				<h:outputText value="Host *" />
				<h:selectOneListbox value="#{dbaccess.host}" size="3">
					<f:selectItem itemValue="localhost" />
					<f:selectItem itemValue="131.193.209.54" itemLabel="server-54" />
					<f:selectItem itemValue="131.193.209.57" itemLabel="server-57" />
				</h:selectOneListbox>

				<h:outputText value="Schema *" />
				<h:inputText id="dbSchema" value="#{dbaccess.dbSchema}" />
				<h:outputText value="Click here to Login" />
				<h:commandButton type="submit" value="Login"
					action="#{actionBeanDB.processLogin}" />

				<h:outputText value="Status" />
				<h:outputText style="color:red" value="#{dbaccess.message}" />


			</h:panelGrid>
		</h:form>
	</div>

</f:view>
</body>
</html>
</jsp:root>