<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">

<%-- TagLibs --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%-- Imports --%>
<%@
	page language="java" contentType="text/html"
	import="java.util.List"
	import="edu.wustl.common.beans.NameValueBean"
%>
<%@page import="java.util.Iterator"%>
<html>
<c:set var="groupsXML" value="${annotationForm.annotationGroupsXML}"/>
<jsp:useBean id="groupsXML" type="java.lang.String"/>

<c:set var="systemEntitiesList" value="${annotationForm.systemEntitiesList}"/>
<jsp:useBean id="systemEntitiesList" type="java.util.List"/>


	<head>
		<%-- Stylesheet --%>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleSheet.css" />
		<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/dhtml_comp/css/dhtmlXGrid.css"/>
		<script src="<%=request.getContextPath()%>/jss/javaScript.js" type="text/javascript"></script>
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
				if(top.location!=location)
				{
					top.location.href = location.href;
				}
				initializeGridForGroups("<%=groupsXML%>");
				initializeGridForEntities();
				initializeStaticEntities();
			}
			function initializeStaticEntities()
			{
				entityList=new dhtmlXMenuBarObject('staticEntityList','100%',0," ");
				entityList.setOnClickHandler(onButtonClick);
				//create menu item
				var item = new dhtmlXMenuItemObject("buildform","Build New Annotation Form","");

				entityList.addItem(entityList,item);
				//create submenu
				var subMenu = new dhtmlXMenuBarPanelObject(entityList,item,false,120,true);

				<%
					Iterator<NameValueBean>  iter = systemEntitiesList.iterator();
					NameValueBean bean = null;
					String entityName = null,entityId = null;
					while(iter.hasNext())
					{
						bean = iter.next();
						if(bean!=null)
						{
							entityName = bean.getName();
							entityId = bean.getValue();
				%>
					var item = new dhtmlXMenuItemObject("<%=entityId%>","<%=entityName%>","");
					entityList.addItem(subMenu,item);
				<%
						}
					}
				%>

			}
		</script>
	</head>

	<html:form styleId='annotationForm' action='/DefineAnnotations'>
	<body onload = "initializeAnnotationsForm()">
	<br>
	<table valign="top"  align='center' width='90%'  border='0' height="90%" cellspacing="0" cellpadding="0" >
		<!-- Main Page heading -->

		<tr valign="top" width="100%">
			<td width="100%" align="left">
				<div id="staticEntityList" style="width:10%;">
				</div>
			</td>
		</tr>
		<tr valign="top" width="100%">
			<td width="100%" align="left">
				&nbsp;
			</td>
		</tr>
		<tr valign="top" width="100%" height="100%">
			<td  align="left">
				<table  class = "tbBordersAllbordersBlack" border="1" valign="top" width="100%" height="100%">
					<tr valign="top">
						<td colspan="2" align="left">
							<html:button disabled = "true" styleClass="actionButton" property = "delete" onclick="featureNotSupported()" >Delete</html:button>
						</td>
					</tr>

					<tr height="100%" valign="top">
						<td align="left" width="20%" height="100%" valign="top" rowspan="2">
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
	<html:hidden styleId="selectedStaticEntityId" property="selectedStaticEntityId" value=""/>
	</body>
	</html:form>
</html>
