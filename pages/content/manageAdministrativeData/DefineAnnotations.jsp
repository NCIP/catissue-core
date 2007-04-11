<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">

<%-- TagLibs --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%-- Imports --%>
<%@
	page language="java" contentType="text/html"
	import="java.util.List"
	import="edu.wustl.common.beans.NameValueBean"
%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
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
System.out.println("containerId:"+containerId);
%>
	<head>
		<%-- Stylesheet --%>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleSheet.css" />
		<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/dhtml_comp/css/dhtmlXGrid.css"/>
		<script src="<%=request.getContextPath()%>/jss/javaScript.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/jss/ajax.js" type="text/javascript"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXCommon.js"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXGrid.js"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXGridCell.js"></script>
	<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXGrid_excell_link.js"></script>

	<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/dhtml_comp/css/dhtmlXMenu.css">
		<script language="JavaScript" src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXProtobar.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXMenuBar.js"></script>

		<script>
			function initializeAnnotationsForm()
			{
				document.getElementById('optionSelect').disabled=false;
				if(top.location!=location)
				{
					top.location.href = location.href;
				}
				<%  if(link!=null && link.equals("editCondn"))   {%>
					document.getElementById('optionSelect').disabled=true;
					<%}else{%>
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
				//alert('EditAnno');
				document.forms[0].action="/catissuecore/DefineAnnotations.do?link=edit";
				document.forms[0].submit();
			}
			
			function editCondition()
			{     
				document.forms[0].action="/catissuecore/SaveAnnotationsConditions.do?containerId="+<%=containerId%>+"&selectedStaticEntityId="+<%=selectedStaticEntityId%>;
				document.forms[0].submit();

			}
			

		</script>
	</head>

	<html:form styleId='annotationForm' action='/DefineAnnotations'>
	
	<body onload = "initializeAnnotationsForm()">



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
						<html:select property="selectedStaticEntityId" styleId="optionSelect" styleClass="formFieldSized15" >
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

   <table valign="top" border="0" height="80%" width = "600" cellspacing="0" cellpadding="3">

   	<%if (link==null){ %>

	<tr valign="top" height = "80%">
	 <td  width="2%"></td>	
       <td>
				<table valign="top" width="100%" border='0' height="80%" cellspacing="0" cellpadding="0" >
			<!-- Main Page heading -->				    
				 <tr height="2%">
					   <td class="formTitle" height="25" colspan="3">
						  <bean:message key="app.availableGrp"/> 
						</td>
				 </tr>
					<tr valign="top" width="100%" height="100%">
						<td  align="left">
							<table  class = "tbBordersAllbordersBlack" border="1" cellspacing="0" cellpadding="0" valign="top" width="100%" height="80%">						
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
	</body>
	</html:form>
</html>
