
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="java.util.List"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
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
	
List annoList=(List) request.getAttribute(Constants.SPREADSHEET_DATA_RECORD);

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

<script>

<% List indexIds=(List) request.getAttribute("RecordIds");  %>
var recordIds = [ <%int i;%><%for (i=0;i<(indexIds.size());i++){%>
	<%="\""%><%=indexIds.get(i)%><%="\""%>,
<% } %> ];

</script>


<script>	
	
	function displayAnnotationGrid()
	{
		var entityName = '<%=request.getAttribute("entityName")%>';
		var rowId= '<%=request.getAttribute("entityId")%>' +":"+ '<%=request.getAttribute("staticEntityId")%>' +":"+ '<%=request.getAttribute("staticEntityRecordId")%>';
		var div=document.getElementById('editAnnotations');
		div.innerHTML="<br><table class=\"whitetable_bg\" style=\"font-family:verdana;font-size:0.71em;font-weight:normal;\"><th allign=\"center\"> Update Records for "+entityName+" </th></table>";
		dannotationsGrid = new dhtmlXGridObject('displayAnnotationsGrid');
		dannotationsGrid.setImagePath("dhtml_comp/imgs/");
		dannotationsGrid.enableAutoHeigth(true,500);
		dannotationsGrid.setHeader("Record Id,Created Date,Updated By,Action");
		dannotationsGrid.setInitWidthsP("25,25,25,24");
		dannotationsGrid.enableAlterCss("even","uneven");
		dannotationsGrid.setSkin("light");
		dannotationsGrid.enableRowsHover(true,'grid_hover');
		dannotationsGrid.setColAlign("left,left,left,left");
		dannotationsGrid.setColTypes("ro,ro,ro,link");
		dannotationsGrid.setColSorting("int,str,str");
		dannotationsGrid.setOnRowDblClickedHandler(editAnnotation);
		dannotationsGrid.init();

		<% if (annoList != null && annoList.size() != 0)
		{ %>
		for(var row=0;row<xmlData.length;row=row+1)
		{	
			var recordId=recordIds[row];
			var index=rowId+":"+recordId;
			var data = xmlData[row];		        
			dannotationsGrid.addRow(index,data,row+1);
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

	<table width="100%" border="2" cellpadding="0"
		cellspacing="0" class="whitetable_bg" height="100%">
		<tr height="80%">
			<td valign="top">
				<div id="displayAnnotationsGrid" valign="top" width="100%"
						height="100%" border="2" style="background-color:#d7d7d7;overflow:hidden;" />
				<script>
					displayAnnotationGrid();
				</script>
			</td>
		</tr>
	</table>
</html:form>