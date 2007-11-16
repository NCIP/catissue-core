<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>


<html>
<!-- Initializations START-->


<!-- Initializations END-->

<head>
	<title></title>
	<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXGrid.css"/>
	<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />

	<script  src="<%=request.getContextPath()%>/dhtml_comp/js/dhtmlXCommon.js"></script>
	<script  src="<%=request.getContextPath()%>/dhtml_comp/js/dhtmlXGrid.js"></script>
	<script  src="<%=request.getContextPath()%>/dhtml_comp/js/dhtmlXGridCell.js"></script>
	<script  src="<%=request.getContextPath()%>/dhtml_comp/js/dhtmlXGrid_excell_link.js"></script>

	<script src="<%=request.getContextPath()%>/jss/javaScript.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/jss/script.js" type="text/javascript"></script>
	
	<%
	List dataList = (List) request.getAttribute(Constants.SPREADSHEET_DATA_RECORD);
	%>
	
	<script>
			<% if (dataList != null && dataList.size() != 0)
{ %>
var myData = [<%int i;%><%for (i=0;i<(dataList.size()-1);i++){%>
<%
	List row = (List)dataList.get(i);
  	int j;
%>
<%="\""%><%for (j=0;j < (row.size()-1);j++){%><%=row.get(j)%>,<%}%><%=row.get(j)%><%="\""%>,<%}%>
<%
	List row = (List)dataList.get(i);
  	int j;
%>
<%="\""%><%for (j=0;j < (row.size()-1);j++){%><%=row.get(j)%>,<%}%><%=row.get(j)%><%="\""%>
];
	<% } %>
		
		</script>
	
	
	<script>
	
	
	function initAnnotationGrid()
{
	annotationsGrid = new dhtmlXGridObject('definedAnnotationsGrid');
	annotationsGrid.setImagePath("dhtml_comp/imgs/");
	annotationsGrid.setHeader("Annotation,Last Updated,Updated By");
	annotationsGrid.setInitWidthsP("31,31,32")
	annotationsGrid.setColAlign("left,left,left,left")
	annotationsGrid.setColTypes("link,ro,ro");
	annotationsGrid.init();
	//var annotationXMLFld = document.getElementById('definedAnnotationsDataXML');
	//annotationsGrid.loadXMLString(annotationXMLFld.value);
	<% if (dataList != null && dataList.size() != 0)
{ %>
	 for(var row=0;row<myData.length;row=row+1)
	{
		var data = myData[row];		        
		annotationsGrid.addRow(row+1,data,row+1);
	}
	
	<% } %>
}
	
	
	function deleteSelectedRecords()
	{
	 var selectedRows =	annotationsGrid.getCheckedRows(0);	 
	
     if(selectedRows.length > 0){
  	  var recordArray = selectedRows.split(",");
	  var rows="";
	  for(var i=0;i<recordArray.length;i++)
	  {
		 if(myData[recordArray[i]-1] != null)
		  {
			 var str=  myData[recordArray[i]-1];
			 var str1 = str.split(",");	
			 if(str1[str1.length-1]!=null)// && (i+1) < recordArray.length)
				rows=rows+str1[str1.length-1]+"," ;
		  }
	  }
	 
	 document.getElementById('operation').value = "deleteRecords";
	 document.getElementById('selectedRecords').value = rows;
	 document.forms[0].action = "LoadAnnotationDataEntryPage.do";
	 document.forms[0].submit();
	 }
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
<html:hidden styleId = "definedAnnotationsDataXML" property = "definedAnnotationsDataXML"></html:hidden>
<input type= "hidden" name = "selectedRecords" id = "selectedRecords"/>
<html:hidden property="id" /><html:hidden property="pageOf"/>
	<!-- Actual HTML Code Start -->
	<br>

		<table  valign="top" id = "test" class= "contentPage" width='100%' border ="0" height="90%"  cellspacing="0" cellpadding="3">
		<tr>
		    <td width="1%"></td>
		<td>
			<table valign="top" width="100%" class="tbBordersAllbordersBlack" height="85%" summary="" cellpadding="3" cellspacing="0" >

				<tr valign="top">
					<td align="left" class="formTitle" colspan="10">
						<bean:message key="app.annotationDataEntryPageTitle"/>
					</td>	

				</tr>
				<tr valign="top" >
					<td align="left" colspan="4">
						<label class="formRequiredLabelWithoutBackgrnd"><bean:message key="app.annotationFormsList"/> :</label>
					</td>
					<td align="left" class="formFieldForAnnotaionList" colspan="6" >
						<html:select property="selectedAnnotation" styleId= "selectedAnnotation" styleClass="formFieldSized35">
							<html:options collection="annotationsList" labelProperty="name" property="value" />
						</html:select>

						<html:button property="getDataForAnnotation" styleClass="actionButton" onclick="loadDynamicExtDataEntryPage()" >
								<bean:message key="app.gotoAddAnnotationData"/>
						</html:button>
					</td>	
					
				</tr>

				

				<tr valign="top">
					<td align="left" class="formTitle" colspan="10" >
						<bean:message key="app.listOfAnnotationsAddedTitle"/>
					</td>
					
				</tr>
				<tr height="100%" valign="top">
					<td align="left" colspan="10">
						<div id="definedAnnotationsGrid" valign = "top" width="100%" height="90%" style="background-color:white;overflow:hidden;"  />
						<script>
							initAnnotationGrid();
						</script>
					</td>
				</tr>
			</table>

		</td></tr>
	
		</table>
</html:form>
</html>