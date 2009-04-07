
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="java.util.List"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>


<head>
<title></title>
<link rel="STYLESHEET" type="text/css"
	href="dhtml_comp/css/dhtmlXGrid.css" />
<link href="css/catissue_suite.css" type=text/css rel=stylesheet>

<script
	src="<%=request.getContextPath()%>/dhtml_comp/js/dhtmlXCommon.js"></script>
<script src="<%=request.getContextPath()%>/dhtml_comp/js/dhtmlXGrid.js"></script>
<script
	src="<%=request.getContextPath()%>/dhtml_comp/js/dhtmlXGridCell.js"></script>
<script
	src="<%=request.getContextPath()%>/dhtml_comp/js/dhtmlXGrid_excell_link.js"></script>
<script src="<%=request.getContextPath()%>/jss/ajax.js" type="text/javascript"></script>

<script src="<%=request.getContextPath()%>/jss/javaScript.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/jss/script.js"
	type="text/javascript"></script>

<%
	
List annoList=(List) request.getAttribute("annotationListFromXML");

	%>


<script>
			<% if (annoList != null && annoList.size() != 0)
{ %>
var xmlData = [<%int i;%><%for (i=0;i<(annoList.size()-1);i++){%>
<%
	List row = (List)annoList.get(i);
  	int j;
%>
<%="\""%><%for (j=0;j < (row.size()-1);j++){%><%=row.get(j)%>,<%}%><%=row.get(j)%><%="\""%>,<%}%>
<%
	List row = (List)annoList.get(i);
  	int j;
%>
<%="\""%><%for (j=0;j < (row.size()-1);j++){%><%=row.get(j)%>,<%}%><%=row.get(j)%><%="\""%>
];
	<% } %>
		
</script>


<c:set var="annotationsList"
		value="${annotationDataEntryForm.annotationsList}" />
	<jsp:useBean id="annotationsList" type="java.util.List"/>

<script>

var indexIds = [<%int i;%><%for (i=0;i<(annotationsList.size());i++){%>
<%
	NameValueBean annotationBean = (NameValueBean)annotationsList.get(i);
%>
	<%="\""%><%=annotationBean.getValue()%><%="\""%>,
	
<% } %> ];

</script>


<script>	
	
	function displayAnnotationGrid()
	{
		dannotationsGrid = new dhtmlXGridObject('displayAnnotationsGrid');
		dannotationsGrid.setImagePath("dhtml_comp/imgs/");
		dannotationsGrid.setHeader("Group,Form,Completed Forms,Action");
		dannotationsGrid.setInitWidthsP("24,38,15,20");
		dannotationsGrid.enableAlterCss("even","uneven");
		dannotationsGrid.setSkin("light");
		dannotationsGrid.enableRowsHover(true,'grid_hover');
		dannotationsGrid.setColAlign("left,left,left,left");
		dannotationsGrid.setColTypes("ro,ro,ro,link");
		dannotationsGrid.setColSorting("str,str,int");
		dannotationsGrid.setOnBeforeSelect(doOnRowSelected);
		dannotationsGrid.setOnRowDblClickedHandler(loadDynamicExtDataEntryPage);
		dannotationsGrid.init();

		<% if (annoList != null && annoList.size() != 0)
		{ %>
		 for(var row=0;row<xmlData.length;row=row+1)
			{
				var annotationId=indexIds[row];
				var data = xmlData[row];		        
				dannotationsGrid.addRow(annotationId,data,row+1);
			}
		
		<% } %>
	}

	</script>

</head>

<html:form action="LoadDynamicExtentionsDataEntryPage">

	<html:hidden property="parentEntityId"></html:hidden>
	<html:hidden property="selectedStaticEntityId"></html:hidden>
	<html:hidden property="selectedStaticEntityRecordId"></html:hidden>
	<html:hidden property="operation" styleId="operation"></html:hidden>
	<html:hidden styleId="definedAnnotationsDataXML"
		property="definedAnnotationsDataXML"></html:hidden>
	<input type="hidden" name="selectedRecords" id="selectedRecords" />
	<html:hidden property="id" />
	<html:hidden property="pageOf" />
	<html:hidden property="selectedAnnotation" styleId="selectedAnnotation"/>
	<div id="editAnnotations" valign="top" width="100%"
						height="100%" style="background-color:#FFFFFF;overflow:hidden;" >
	</div>
	<table height="100%" width="100%" border="0" cellpadding="0"
		cellspacing="0" class="whitetable_bg">
		<tr height="100%">
			<td colspan="3" valign="top" class="showhide" height="100%">
				<div id="displayAnnotationsGrid" valign="top" width="100%"
						height="100%" style="background-color:#d7d7d7;overflow:hidden;" />
				<script>
					displayAnnotationGrid();
				</script>
			</td>
		</tr>
	</table>
</html:form>