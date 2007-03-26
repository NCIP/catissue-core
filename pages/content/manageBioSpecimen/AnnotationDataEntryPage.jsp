<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List"%>


<html>
<!-- Initializations START-->


<!-- Initializations END-->

<head>
	<title></title>
	<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXGrid.css"/>
	<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />

	<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXCommon.js"></script>
	<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXGrid.js"></script>
	<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXGridCell.js"></script>
	<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXGrid_excell_link.js"></script>

	<script src="<%=request.getContextPath()%>/jss/javaScript.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/jss/script.js" type="text/javascript"></script>
	<script>
	function deleteSelectedRecords()
	{
	 var selectedRows =	annotationsGrid.getCheckedRows(0);
	 document.getElementById('operation').value = "deleteRecords";
	 document.getElementById('selectedRecords').value = selectedRows;
	 document.forms[0].action = "LoadAnnotationDataEntryPage.do";
	 document.forms[0].submit();
	}
	</script>
	
</head>

<html:form action="LoadDynamicExtentionsDataEntryPage">
	<c:set var="annotationsList" value="${annotationDataEntryForm.annotationsList}"/>
	<jsp:useBean id="annotationsList" type="java.util.List"/>

<html:hidden property = "parentEntityId"></html:hidden>
<html:hidden property = "selectedStaticEntityId"></html:hidden>
<html:hidden property = "selectedStaticEntityRecordId"></html:hidden>

<html:hidden property = "operation" styleId = "operation"></html:hidden>
<html:hidden property = "definedAnnotationsDataXML"></html:hidden>
<input type= "hidden" name = "selectedRecords" id = "selectedRecords"/>
<html:hidden property="id" /><html:hidden property="pageOf"/>
	<!-- Actual HTML Code Start -->
	<br>

		<table  valign="top" id = "test" class= "contentPage" width='100%' border ="0" height="95%"  cellspacing="0" cellpadding="3">
		<tr>
		    <td width="1%"></td>
		<td>
			<table class="tbBordersAllbordersBlack"  height="100%" summary="" cellpadding="3" cellspacing="0" >

				<tr valign="top">
					<td align="left" class="formTitle">
						<bean:message key="app.annotationDataEntryPageTitle"/>
					</td>
				</tr>
				<tr valign="top" >
					<td align="left" >
						<label class="formRequiredLabelWithoutBackgrnd"><bean:message key="app.annotationFormsList"/> :</label>
						<html:select property="selectedAnnotation" styleId= "selectedAnnotation" styleClass="formFieldVerySmallSized">
							<html:options collection="annotationsList" labelProperty="name" property="value" />
						</html:select>
						<html:button property="getDataForAnnotation" styleClass="actionButton" onclick="loadDynamicExtDataEntryPage()" >
								<bean:message key="app.gotoAddAnnotationData"/>
						</html:button>
						<!--<a href="#" id="getDataForAnnotation" onclick="loadDynamicExtDataEntryPage()">GO</a>-->
					</td>
				</tr>

				

				<tr valign="top">
					<td align="left" class="formTitle">
						<bean:message key="app.listOfAnnotationsAddedTitle"/>
					</td>
					
				</tr>
				<tr height="100%" valign="top">
					<td align="left" >
						<div id="definedAnnotationsGrid" valign = "top" width="100%" height="100%" style="background-color:white;overflow:hidden"  />
						<script>
							initAnnotationGrid();
						</script>
					</td>
				</tr>
				<tr valign="bottom">
					<td align="left" class="formLabelAllBorder">
						<html:button property="deleteAnnotationData" styleClass="actionButton" onclick="deleteSelectedRecords();" >
								Delete
						</html:button>
					</td>
				</tr> 
			</table>

		</td></tr>
	
		</table>
</html:form>
</html>