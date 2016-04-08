<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<html>
<head>
<title>Regression Analysis</title>
</head>
<body>
<h3 align="center">IDS517 s16g30 Column Data</h3>
	<div id ="main">

	<f:view>
		<center>
			<h4>Select Variables to perform Regression Analysis:</h4>

			<h:form>
				<h:panelGroup>
					<h:column>
						<h:outputText value="X-Axis Variable " />&nbsp&nbsp
						<h:selectOneMenu value="#{regressionAnalysis.xVariable}"
							onchange="submit()"
							valueChangeListener="#{regressionAnalysis.xValueChange}">
							<f:selectItems value="#{viewTableColumnsBean.checkedItems}"
								var="column" itemValue="#{column.column}"
								itemLabel="#{column.column}" />
						</h:selectOneMenu><br/><br/>
						<h:outputText value="Y-Axis Variable " />&nbsp&nbsp
						<h:selectOneMenu value="#{regressionAnalysis.yVariable}"
							onchange="submit()"
							valueChangeListener="#{regressionAnalysis.yValueChange}">
							<f:selectItems value="#{viewTableColumnsBean.checkedItems}"
								var="column" itemValue="#{column.column}"
								itemLabel="#{column.column}" />
						</h:selectOneMenu>
					</h:column>
				</h:panelGroup>
			</h:form>
			<h:form>
				<h:commandButton id="chart" value="Regression"
					action="#{regressionAnalysis.performRegression}" />
				<h:commandButton id="back" value="Back"
					action="#{regressionAnalysis.goBackToViewTableColumns}" />
			</h:form>
			<table>
			<tr>
			<td>
			<h:form rendered="#{regressionAnalysis.render}">
				
				<table border="1" width="70%" style="background-color: Beige;
						border-bottom-style: solid;
						border-top-style: solid;
 						border-left-style: solid;
 						border-right-style: solid">
				 <tr>
					<td style='font-weight:bold;'>Regression Analysis:&nbsp;&nbsp;</td></tr>
				</table> 
				<table border="1" width="70%" style="background-color: Beige;
						border-bottom-style: solid;
						border-top-style: solid;
 						border-left-style: solid;
 						border-right-style: solid">
				 <tr>
					<td style='font-weight:bold;'>Regression Equation: </td>
					<td><h:outputText value="#{regressionAnalysis.equation}" /></td>
					</tr>
					</table> 
				<table border="1" width="70%" style="background-color: Beige;
						border-bottom-style: solid;
						border-top-style: solid;
 						border-left-style: solid;
 						border-right-style: solid">
				 <tr>
					<td style='font-weight:bold;'>Predictor:</td>
					<td style='font-weight:bold;'>Constant</td>
					<td style='font-weight:bold;'><h:outputText value="#{regressionAnalysis.xVariable}" /></td>
				</tr>
				<tr>
					<td style='font-weight:bold;'>Coef:</td>
					<td><h:outputText value="#{regressionAnalysis.coefConst}" /></td>
					<td><h:outputText value="#{regressionAnalysis.coefSF}" /></td>
				</tr>
				<tr><td style='font-weight:bold;'>SE Coef:</td>
					<td><h:outputText value="#{regressionAnalysis.seCoefConst}" /></td>
					<td><h:outputText value="#{regressionAnalysis.seCoefSF}" /></td>
					</tr><tr><td style='font-weight:bold;'>T:</td>
					<td><h:outputText value="#{regressionAnalysis.tConst}" /></td>
					<td><h:outputText value="#{regressionAnalysis.tSF}" /></td>
					</tr><tr><td style='font-weight:bold;'>P:</td>
					<td><h:outputText value="#{regressionAnalysis.pConst}" /></td>
					<td><h:outputText value="#{regressionAnalysis.pSF}" /></td>
				</tr>
				</table> 
				<table border="1" width="70%" style="background-color: Beige;
						border-bottom-style: solid;
						border-top-style: solid;
 						border-left-style: solid;
 						border-right-style: solid">
				 <tr>
				<td>S: </td>
				<td style='font-weight:bold;'><h:outputText value="#{regressionAnalysis.s}" /></td>
				</tr><tr><td style='font-weight:bold;'>R-Sq: </td><td> <h:outputText value="#{regressionAnalysis.rSquare}" /> %</td>
				</tr><tr><td style='font-weight:bold;'>R-Sq(adj): </td><td><h:outputText value="#{regressionAnalysis.rSquareAdj}" /> %</td>
				</tr>
				</table> 
				<table border="1" width="70%" style="background-color: Beige;
						border-bottom-style: solid;
						border-top-style: solid;
 						border-left-style: solid;
 						border-right-style: solid">
				 <tr>
					<td style='font-weight:bold;'>Source: </td>
					<td style='font-weight:bold;'>Regression</td>
					<td style='font-weight:bold;'>Residual Error</td>
					<td style='font-weight:bold;'>Total</td>
				</tr>
				
				<tr>
					<td style='font-weight:bold;'>DF: </td>
					<td><h:outputText value="#{regressionAnalysis.df}" /></td>
					<td><h:outputText value="#{regressionAnalysis.dfRE}" /></td>
					<td><h:outputText value="#{regressionAnalysis.dfTot}" /></td>
					<tr>
					<td style='font-weight:bold;'>SS: </td>
					<td><h:outputText value="#{regressionAnalysis.ssr}" /></td>
					<td><h:outputText value="#{regressionAnalysis.sse}" /></td>
					<td><h:outputText value="#{regressionAnalysis.ssTot}" /></td>
					</tr>
					<tr>
					<td style='font-weight:bold;'>MS: </td>
					<td><h:outputText value="#{regressionAnalysis.msr}" /></td>
					<td><h:outputText value="#{regressionAnalysis.mse}" /></td>
					</tr>
					<tr>
					<td style='font-weight:bold;'>F: </td>
					<td><h:outputText value="#{regressionAnalysis.f}" /></td>
					</tr>
					<tr>
					<td style='font-weight:bold;'>P: </td> 
					<td><h:outputText value="#{regressionAnalysis.p}"/></td>
					</tr>
				</table>	
			</h:form>
			</td>
		
			</tr>
			</table>
			<h:outputText value="Unable to perform regression on categorical variable!" rendered="#{regressionAnalysis.categorical==null}" />
		</center>
	</f:view>
</div>
</body>
</html>