<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
<head>
<title>Table Details</title>
</head>
<body>
	<f:view>
	
<h3 align="center">IDS517 s16g30 - Choose One Column</h3>
<hr /> <br />
	<div align="center">
		<table>
			<tr>
				<td>
				<h:form> 
					<a href="DataAnalysis.jsp">Back</a> &nbsp;&nbsp;&nbsp;
						<a href="dbAccess.jsp">Home</a>&nbsp;&nbsp;&nbsp;
						<a href="fileExport.jsp">File Export</a> &nbsp;&nbsp;&nbsp;								
				</h:form>
				<br>
				</td>
			</tr>
		</table>
	</div>
	
		<center>
			<h2><h:outputText value="Table Name: #{tableListBean.selectedTable} " /></h2>
			<br /> <br />
			
			</center>
			<h:form>
			<center>
				<h:dataTable value="#{viewTableColumnsBean.tableInfo}" var="o" border="1" style="background-color: Beige;
						border-bottom-style: solid;
						border-top-style: solid;
 						border-left-style: solid;
 						border-right-style: solid">
					<h:column>
						<f:facet name="header">
							<h:outputText value="Select" />
						</f:facet>
						<h:selectBooleanCheckbox
							value="#{viewTableColumnsBean.checked[o.column]}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Column Name" />
						</f:facet>
						<h:outputText value="#{o.column}" />
					</h:column>
				</h:dataTable>
				<br/>
<%-- 				<h:panelGrid columns="3"> --%>
<div align="center">
				<h:commandButton id="regressionAnalysis" value="Regression Analysis"
						action="#{viewTableColumnsBean.gotoRegressionAnalysis}" /><br /><br />
					<h:commandButton id="mathAnalysis" value="Statistics"
						action="#{viewTableColumnsBean.gotoNumericalAnalysis}" /><br /><br />
					<h:commandButton id="graphAnalysis" value="Chart Analysis"
						action="#{viewTableColumnsBean.gotoGraphAnalysis}" /><br /><br />

<%-- 					<h:commandButton id="displayTable" value="Display Table" --%>
<%-- 						action="#{viewTableColumnsBean.displayTable}" /> --%>
<%-- 				</h:panelGrid> --%>
</div>
				</center>
			</h:form>
			<center>
			<h:outputText style="color:red" value="Column chosen is of count/categorical type.Please choose Quantitative variable(s) to perform Statistical/Regression analysis !" rendered="#{viewTableColumnsBean.categorical==null}" />
			</center>
	</f:view>
</body>
</html>
