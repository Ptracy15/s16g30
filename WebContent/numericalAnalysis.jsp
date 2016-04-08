<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<html>
<head>
<title>Analysis</title>
</head>
<body>
<h3 align="center">IDS517 s16g30 Column Data</h3>
	<div id ="main">
	<f:view>
		
		<center>
			<h:messages showDetail="#{true}" showSummary="#{false}" />
			<h:form>

				<t:dataTable id="dataList" var="item" 
					value="#{numericalAnalysis.statisticsBeanList}" border="1"
					cellspacing="0" cellpadding="1" columnClasses="columnClass1 border"
					headerClass="headerClass" footerClass="footerClass"
					rowClasses="rowClass2" styleClass="dataTableEx" width="200"
					style="background-color: Beige;
						border-bottom-style: solid;
						border-top-style: solid;
 						border-left-style: solid;
 						border-right-style: solid" renderedIfEmpty="false">
					<h:column>
						<f:facet name="header">
							<h:outputText styleClass="outputHeader" value="Selected Columns" />
						</f:facet>
						<t:outputText styleClass="outputText" value="#{item.colName}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText styleClass="outputHeader" value="Minimum" />
						</f:facet>
						<t:outputText styleClass="outputText" value="#{item.minValue}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText styleClass="outputHeader" value="Maximum" />
						</f:facet>
						<h:outputText styleClass="outputText" value="#{item.maxValue}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText styleClass="outputHeader" value="Average" />
						</f:facet>
						<h:outputText styleClass="outputText" value="#{item.mean}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText styleClass="outputHeader" value="Total" />
						</f:facet>
						<h:outputText styleClass="outputText" value="#{item.sum}" />
					</h:column>
										<h:column>
						<f:facet name="header">
							<h:outputText styleClass="outputHeader" value="Count" />
						</f:facet>
						<h:outputText styleClass="outputText" value="#{item.countValue}" />
					</h:column>
				</t:dataTable>
				<h:commandButton id="back" value="Back"
					action="#{numericalAnalysis.goBackToViewTableColumns}" />

			</h:form>
					<h:outputText value="Column chosen is categorical or you might have imported it wrongly!" rendered="#{numericalAnalysis.categorical==null}" />
				</center>				

			</f:view>
</div>
</body>
</html>
