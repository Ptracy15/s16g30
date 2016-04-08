<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<html>
<head>
<title>Chart Analysis</title>
</head>
<body>
<h3 align="center">IDS517 s16g30 Column Data</h3>
	<div id ="main">
	
	<f:view>
		
		
		<center>
			<h:form>
				<br />
				<h3>Select a Graph to perform analysis</h3>
				<h:selectOneRadio value="#{graphicalAnalysis.selectedGraph}"
					onchange="submit()"
					valueChangeListener="#{graphicalAnalysis.graphChange}">

					<f:selectItem itemValue="Scatter Plot" />
			
					<f:selectItem itemValue="Time Series" />
				</h:selectOneRadio>
			</h:form>
			<br />
			<h:form>
				<h:panelGroup rendered="#{!graphicalAnalysis.xyRender}">
					<h:column>
						<h:outputText value="Select Column" />
						<h:selectOneMenu value="#{graphicalAnalysis.variable}"
							onchange="submit()"
							valueChangeListener="#{graphicalAnalysis.valueChange}">
							<f:selectItem itemValue="" itemLabel="Select" />
							<f:selectItems value="#{viewTableColumnsBean.checkedItems}"
								var="column" itemValue="#{column.column}"
								itemLabel="#{column.column}" />
						</h:selectOneMenu>
					</h:column>
				</h:panelGroup>
				<h:panelGroup rendered="#{graphicalAnalysis.xyRender}">
					<h:column>
						<h:outputText value="X-Axis Variable" />
						<h:selectOneMenu value="#{graphicalAnalysis.xVariable}"
							onchange="submit()"
							valueChangeListener="#{graphicalAnalysis.xValueChange}">
							<f:selectItem itemValue="" itemLabel="Select" />

							<f:selectItems value="#{viewTableColumnsBean.checkedItems}"
								var="column" itemValue="#{column.column}"
								itemLabel="#{column.column}" />
						</h:selectOneMenu>

						<h:outputText value="Y-Axis Variable " />
						<h:selectOneMenu value="#{graphicalAnalysis.yVariable}"
							onchange="submit()"
							valueChangeListener="#{graphicalAnalysis.yValueChange}">
							<f:selectItem itemValue="" itemLabel="Select" />

							<f:selectItems value="#{viewTableColumnsBean.checkedItems}"
								var="column" itemValue="#{column.column}"
								itemLabel="#{column.column}" />
						</h:selectOneMenu>
					</h:column>
				</h:panelGroup>

			</h:form>
			<h:form>
				<h:commandButton id="chart" value="Generate Chart"
					action="#{graphicalAnalysis.generateChart}" />
				<h:commandButton id="back" value="Back"
					action="#{graphicalAnalysis.goBackToViewTableColumns}" />
			</h:form>
			<h:form rendered="#{graphicalAnalysis.render}">
				<h:graphicImage value="/JFreechart.png"></h:graphicImage>
			</h:form>
		</center>


	</f:view>
</div>
</body>