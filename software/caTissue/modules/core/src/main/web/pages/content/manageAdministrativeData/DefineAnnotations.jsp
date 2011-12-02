<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">

<%-- TagLibs --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" isELIgnored="false"%>
<%-- Imports --%>
<%@
	page language="java" contentType="text/html"
	import="java.util.List"
	import="edu.wustl.common.beans.NameValueBean"
	
%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="java.util.Iterator"%>
<html>
<c:set var="groupsXML" value="${annotationForm.annotationGroupsXML}"/>
<jsp:useBean id="groupsXML" type="java.lang.String"/>

<c:set var="systemEntitiesList" value="${annotationForm.systemEntitiesList}"/>
<jsp:useBean id="systemEntitiesList" type="java.util.List"/>

<c:set var="conditionalInstancesList" value="${annotationForm.conditionalInstancesList}"/>
<jsp:useBean id="conditionalInstancesList" type="java.util.List"/>

<%
String link = request.getParameter("link");
String containerId = request.getParameter("containerId");
String selectedStaticEntityId=request.getParameter("selectedStaticEntityId");
System.out.println("request parameter:"+request.getParameter("op"));
String strDEOperation = (String) request.getParameter("op");
if(strDEOperation!=null)
{
	System.out.println("setting in session "+strDEOperation);
	session.setAttribute("deoperation",strDEOperation);
}
else
{
	strDEOperation = (String) session.getAttribute("deoperation");
	System.out.println("getting frm session "+strDEOperation);
}

List groupList = (List) request.getAttribute(Constants.SPREADSHEET_DATA_GROUP);

boolean mac = false;
Object os = request.getHeader("user-agent");
if(os!=null && os.toString().toLowerCase().indexOf("mac")!=-1)
{
    mac = true;
}
boolean heightForGrid = true;
if(mac)
{
heightForGrid=false;
}
%>
	<head>
		<%-- Stylesheet --%>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleSheet.css" />
		<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/dhtml_comp/css/dhtmlXGrid.css"/>
		<script src="<%=request.getContextPath()%>/jss/javaScript.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/jss/ajax.js" type="text/javascript"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/js/dhtmlXCommon.js"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/js/dhtmlXGrid.js"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/js/dhtmlXGridCell.js"></script>
	<script  src="<%=request.getContextPath()%>/dhtml_comp/js/dhtmlXGrid_excell_link.js"></script>

	<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/dhtml_comp/css/dhtmlXMenu.css">
		<script language="JavaScript" src="<%=request.getContextPath()%>/dhtml_comp/js/dhtmlXProtobar.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/dhtml_comp/js/dhtmlXMenuBar.js"></script>
		<script>
			<% if (groupList != null && groupList.size() != 0)
{ %>
var myData = [<%int i;%><%for (i=0;i<(groupList.size()-1);i++){%>
<%
	List row = (List)groupList.get(i);
  	int j;
%>
<%="\""%><%for (j=0;j < (row.size()-1);j++){%><%=row.get(j)%>,<%}%><%=row.get(j)%><%="\""%>,<%}%>
<%
	List row = (List)groupList.get(i);
  	int j;
%>
<%="\""%><%for (j=0;j < (row.size()-1);j++){%><%=row.get(j)%>,<%}%><%=row.get(j)%><%="\""%>
];
	<% } %>
		
		</script>
		<script>
			function initializeAnnotationsForm()
			{
				
				if(document.getElementById('optionSelect')!=null)
				document.getElementById('optionSelect').disabled=false;
				
				if(top.location!=location)
				{
					top.location.href = location.href;
				}
				<%  if(link!=null && link.equals("editCondn"))   {%>
					if(document.getElementById('optionSelect')!=null)
					document.getElementById('optionSelect').disabled=true;
					<%}else if(link==null && Constants.EDIT.equals(strDEOperation)){%>
				initializeGridForGroups("<%=groupsXML%>");
				initializeGridForEntities();
				<%}%>
			//	initializeStaticEntities();
			}
			
			function showList()
			{
				if(document.getElementById('chkbox').checked)	     
				{
					document.getElementById('cpList').disabled=false;
				}
				else
				{
					document.getElementById('cpList').disabled=true;
				}		

			}
			function editAnno()
			{
				document.forms[0].action="/catissuecore/DefineAnnotations.do?link=edit";
				document.forms[0].submit();
			}
			
			function editCondition()
			{     
				document.forms[0].action="/catissuecore/SaveAnnotationsConditions.do?containerId="+<%=containerId%>+"&selectedStaticEntityId="+<%=selectedStaticEntityId%>;
				document.forms[0].submit();

			}
			
			function showHideConditions(currentElement)
			{
				var cplist = document.getElementById("cpList");
				var isEnabled = true;
				<c:forEach items="${annotationForm.systemEntitiesList}" var="current">
					var currentName = "${current.name}";
					var currentValue = "${current.value}";
					if(currentElement.value == currentValue && currentName == 'Specimen Processing Procedure')
					{
						cplist.disabled=true;
						isEnabled = false;
					}
					
				</c:forEach>
				if(isEnabled)
				{
					cplist.disabled=false;
				}
			}
			
		function initializeGridForGroups(groupsXML)
        {	

			gridForGroups= new dhtmlXGridObject('divForGroups');
			gridForGroups.setImagePath("dhtml_comp/imgs/");
			//		gridForGroups.enableAutoHeigth(false);  //on safari
			gridForGroups.enableAutoHeigth(true);  //for mozilla, IE
			gridForGroups.setHeader("Group");

			if (document.all)
			{
				gridForGroups.setInitWidthsP("100");
			}
			else
			{
				gridForGroups.setInitWidthsP("98.5");
			}
			
			gridForGroups.setColAlign("left");
			gridForGroups.setColTypes("ro");	
			gridForGroups.setOnRowSelectHandler(groupSelected);
			gridForGroups.init();
		
	        <% if (groupList != null && groupList.size() != 0){ %>
		    for(var row=0;row<myData.length;row=row+1)
			{
				var data = myData[row];		
		        var	dataArr=data.split(",");
				gridForGroups.addRow(dataArr[0],dataArr[1],row+1);
			}
			<%}%>
			//fix for grid display on IE for first time.
			gridForGroups.setSizes();
		
			if(gridForGroups.getRowsNum()>0)
			{
				gridForGroups.selectRow(0,true,false);	
			}
       }
		
			


		</script>
	</head>
	<body onload = "initializeAnnotationsForm()">
	
	<table summary="" cellpadding="5" cellspacing="0" border="0" width="620" height="80">
		<tr style="font-family:arial,helvetica,verdana,sans-serif;">
			<td>
</td>
		</tr>
		<tr style="font-family:arial,helvetica,verdana,sans-serif;">
	
	<td>
<pre><b> Note:</b></pre>
<pre>
 1. Please take backup of the database before add or edit of a Dynamic Extension.
 2. You need to restart the caTissue JBoss every time after add or edit of a new Dynamic Extension.
 3. Please contact your caTissue system administrator before starting this activity.
</pre>
			</td> 
		</tr>
	</table>
	<%@ include file="/pages/content/common/ActionErrors.jsp" %>
	<html:form styleId='annotationForm' action='/DefineAnnotations'>
	

	 <html:hidden property="deoperation" value="<%=strDEOperation%>"/>
 	<%if (strDEOperation.equals("add") || ( link!=null && link.equals("editCondn") ))
		{ 		
	%>
<table id="tableID" width="600" cellspacing="0" cellpadding="0">
      <tr height = 10>
      	   <td></td>
      	   <td></td>
      </tr>
  	
	  <tr>	 
  	   	  <td width = "2%"></td>
		  <td>        
          	  <table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">			

				<tr>
					   <td class="formTitle" height="20" colspan="3">
						  <bean:message key="app.buldAnnotationForms"/> 
						</td>
				 </tr>
				 
				 <tr>
		
					<td class ="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel" >
					   <bean:message key="anno.Entity"/> 
					</td>
					<td class="formField" >
						<html:select property="selectedStaticEntityId" styleId="optionSelect" styleClass="formFieldSized15" onchange="showHideConditions(this)">
							<html:options collection="systemEntitiesList"  labelProperty="name" property="value"/> 
					  </html:select>
					</td>
				</tr>

				 <tr>
				         <td class="formRequiredNotice" height="20"  width="5">&nbsp;</td>
					   <td class="formField" height="20" colspan="3">
						   <b> <bean:message key="anno.condn"/>  </b>
						</td>
				 </tr>
				

				<tr>
		
					<td class ="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
					   <bean:message key="collectionprotocol.protocoltitle" />
					</td>
					<td class="formField" >
						<html:select property="conditionVal" styleClass="formFieldSized20" multiple ="true" 
						 size="4" styleId="cpList">
							 <html:options collection="conditionalInstancesList" labelProperty="name" property="value"/>
						</html:select>

						
					</td>
				</tr>

								
				<tr>
						<td align="center" colspan="3">
						<!-- action buttons begins -->
						<table cellpadding="4" cellspacing="0" border="0"  >
							<tr>
								<td>
								<%if (link==null){ %>
								<html:button styleId="go1btn"  styleClass="actionButton" property = "delete" onclick="submitForm()" >
								  <bean:message key="buttons.submit"/>
								</html:button>
								<%} %>
								<%if (link!=null && link.equals("editCondn")){ %>
								<html:button   styleClass="actionButton" property = "delete" onclick="editCondition()" >
								  <bean:message key="buttons.submit"/>
								</html:button>	
								
								<html:button   styleClass="actionButton" property = "delete" onclick="javascript:history.go(-1);" >
								  <bean:message key="buttons.cancel"/>
								</html:button>								
								<%} %>

								</td>
								
							</tr>
						</table>
						</td>
					</tr>

				



			  </table>
			</td>
      </tr>
  </table>

	<%}%>
   <table valign="top" border="0" height="450" width = "100%" cellspacing="0" cellpadding="3">
	
   	<%if (link==null && strDEOperation.equals("edit")){ 

	%>

	<tr valign="top" height = "100%">
       <td>
				<table valign="top" width="100%" border='0' height="100%" cellspacing="0" cellpadding="0" >
			<!-- Main Page heading -->				    
				 <tr height="*">
					   <td class="formTitle" colspan="3">
						  <bean:message key="app.availableGrp"/> 
						</td>
				 </tr>
					<tr valign="top" width="100%" height="100%">
						<td  align="left">
							<table  class = "tbBordersAllbordersBlack" border="1" cellspacing="0" cellpadding="0" valign="top" width="100%" height="100%">						
								<tr height="100%" valign="top">								
									<td align="left" width="18%" height="100%" valign="top" rowspan="2">
										<!--Groups list-->
										<div id="divForGroups" width="100%" height="100%" style="background-color:white;overflow:hidden"/>
									</td>								
									<td align="left" valign="top">
										<!--List of entities-->
										<div id="gridForEnities" width="100%" height="100%" style="background-color:white;overflow:hidden"/>
									</td>									
								</tr>
							</table>
						</td>
					</tr>	
					
			  </table>
	   </td>	 
	</tr>
	  

	<%}%>
		
</table>

	<html:hidden styleId="selectedStaticEntityId" property="selectedStaticEntityId" value=""/>
	</html:form>
	</body>
</html>