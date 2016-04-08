<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Select DB Option</title>
</head>
<body>
<h3 align="center">IDS517 s16g30</h3>
<hr /> <br />
		<f:view>
			<div align="center">
				<h:form> 
					<a href="DataAnalysis.jsp">Data Analysis</a> &nbsp;
					<a href="fileExport.jsp">File Export</a>&nbsp;
					<a href="DatabaseOperation.jsp">DB Operation</a>&nbsp;
										<br ><br ><br >
					<h:commandLink action="#{logOutBean.goLoginPage}" value="Logout" /> 
				</h:form>
			</div>
</f:view>

	
</body>
</html>
