<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ListSpecimenEventParametersForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>
<%@ page language="java" isELIgnored="false"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ListSpecimenEventParametersForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>
<%@ page language="java" isELIgnored="false"%>

<%@ include file="/pages/content/common/EventAction.jsp" %>

<head>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<link href="runtime/styles/xp/grid.css" rel="stylesheet" type="text/css"/>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script src="runtime/lib/grid.js"></script>
<script src="runtime/formats/date.js"></script>
<script src="runtime/formats/string.js"></script>
<script src="runtime/formats/number.js"></script>
<script src="<%=request.getContextPath()%>/javascripts/de/jquery-1.3.2.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/javascripts/de/form_plugin.js" type="text/javascript"></script>

<%
String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
String specimenIdentifier = (String)request.getAttribute(Constants.SPECIMEN_ID);
if(specimenIdentifier == null || specimenIdentifier.equals("0"))
	specimenIdentifier = (String)request.getParameter(Constants.SPECIMEN_ID);

if(specimenIdentifier != null && !specimenIdentifier.equals("0"))
	session.setAttribute(Constants.SPECIMEN_ID,specimenIdentifier);

if(specimenIdentifier == null || specimenIdentifier.equals("0"))
{
	specimenIdentifier= (String)session.getAttribute(Constants.SPECIMEN_ID);
}
request.setAttribute("showSPPHeader","true");
request.setAttribute("showSkipEventCheckBoxes","true");
%>

<head>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<link href="runtime/styles/xp/grid.css" rel="stylesheet" type="text/css"/>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script src="runtime/lib/grid.js"></script>
<script src="runtime/formats/date.js"></script>
<script src="runtime/formats/string.js"></script>
<script src="runtime/formats/number.js"></script>
<script src="<%=request.getContextPath()%>/javascripts/de/jquery-1.3.2.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/javascripts/de/form_plugin.js" type="text/javascript"></script>

<script>
	function submitSPPEvents()
	{
		var action="";
		<%
		String IdentifierStr="";
		if(request.getAttribute(Constants.SPECIMEN_ID)!=null){
			IdentifierStr = (String)request.getAttribute(Constants.SPECIMEN_ID);
		%>
			action="SaveSPPEventAction.do?pageOf=pageOfUtilizeSppofSpecimen&operation=insertDEData&specimenId="+"<%=IdentifierStr%>"+"&selectedAll="+${requestScope.selectedAll}+"&sppId="+${requestScope.sppId}+"&typeObject=<%=request.getAttribute("typeObject")%>";
		<%
		}else{
			IdentifierStr = (String)request.getAttribute("scgId");
		%>
			action="SaveSPPEventAction.do?pageOf=pageOfUtilizeSppOfScg&operation=insertDEData&scgId="+"<%=IdentifierStr%>"+"&sppName=<%=request.getAttribute("nameOfSelectedSpp")%>"+"&selectedAll="+${requestScope.selectedAll}+"&sppId="+${requestScope.sppId}+"&typeObject=<%=request.getAttribute("typeObject")%>";
		<%
		}
		%>
		
		var search = 'Control';
		var search1 = 'comboControl';
		var iframeList = document.getElementsByTagName('iframe');
		for(j =0;j<iframeList.length;j++)
		{
			var containerId= iframeList[j].name
			var oDoc = iframeList[j].contentWindow || iframeList[j].contentDocument;
			if (oDoc.document) {
				oDoc = oDoc.document;
			}
			var inputCollection = oDoc.getElementsByTagName('input');
			for(i=0; i<inputCollection.length ;i++)
			{
				if(action.indexOf('?') == -1)
				{
					action=action+'?'+containerId+'!@!'+inputCollection[i].name+'='+inputCollection[i].value;
				}
				else
				{
					action=action+'&'+containerId+'!@!'+inputCollection[i].name+'='+inputCollection[i].value;
				}
			}
			var selectCollection = oDoc.getElementsByTagName('select');
			for(i=0; i<selectCollection.length ;i++)
			{
				if(action.indexOf('?') == -1)
				{
					action=action+'?'+containerId+'!@!'+selectCollection[i].name+'='+selectCollection[i].value;
				}
				else
				{
					action=action+'&'+containerId+'!@!'+selectCollection[i].name+'='+selectCollection[i].value;
				}
			}
			var textAreaCollection = oDoc.getElementsByTagName('textarea');
			for(i=0; i<textAreaCollection.length ;i++)
			{
				if(action.indexOf('?') == -1)
				{
					action=action+'?'+containerId+'!@!'+textAreaCollection[i].name+'='+textAreaCollection[i].value;
				}
				else
				{
					action=action+'&'+containerId+'!@!'+textAreaCollection[i].name+'='+textAreaCollection[i].value;
				}
			}


			oDoc = oDoc.getElementById("name1").contentWindow || iframeList[j].contentDocument;
			if (oDoc.document) {
				oDoc = oDoc.document;
			}
			var inputCollection = oDoc.getElementsByTagName('input');
			for(i=0; i<inputCollection.length ;i++)
			{
				if(inputCollection[i].name.indexOf(search) == 0 || inputCollection[i].name.indexOf(search1) == 0)
				{
					if(action.indexOf('?') == -1)
					{
						action=action+'?'+inputCollection[i].name+'='+inputCollection[i].value;
					}
					else
					{
						action=action+'&'+inputCollection[i].name+'='+inputCollection[i].value;
					}
				}
			}
			var a = document.getElementsByTagName('input');
			var i=0;
			for(i=0; i<a.length ;i++)
			{
				if(a[i].name.indexOf(search) == 0 || a[i].name.indexOf(search1) == 0)
				{
					if(action.indexOf('?') == -1)
					{
						action=action+'?'+a[i].name+'='+a[i].value;
					}
					else
					{
						action=action+'&'+a[i].name+'='+a[i].value;
					}
				}
			}
		}
		document.forms[0].action=action;
	}


</script>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head" width="15%" ><span width="100%" class="wh_ar_b">SPP</span>&nbsp<span width="100%" class="wh_ar_b">Data</span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Site" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
 </table>
<html:form action="SaveSPPEventAction.do?operation=insertDEData">

<%@ include file="/pages/content/manageBioSpecimen/SPPEventsFromDashboard.jsp" %>

</html:form>