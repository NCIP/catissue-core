<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>

<%@ include file="/pages/content/common/AdminCommonCode.jsp"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp"%>
<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.css"/>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn_bricks.css">
<script language="JavaScript"  type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxcommon.js"></script>
<script  language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.js"></script>
<script  language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxDataView/codebase/connector/dhtmlxdataprocessor.js"></script>
<script  src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_export.js"></script>
<script  language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxDataView/codebase/connector/connector.js"></script>
<script   language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgridcell.js"></script>
<script  language="JavaScript" type="text/javascript"  src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_srnd.js"></script>
<script  src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>
<script language="JavaScript" src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_filter.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn.js"></script>
<%
String header;
	String alias = (String) request.getParameter("aliasName");

		header = alias + ".header";
		String title = alias + ".Search";
String pageOf = (String) request.getAttribute(Constants.PAGE_OF);
%>



<script>
var columns =${requestScope.columnList};
var colWidth =${requestScope.ColWidth};
var pageOf ="${requestScope.pageOf}";
var aliasName ="${requestScope.aliasName}";
var ColCount = ${requestScope.ColCount};
</script>
	<html:form action="/SimpleQueryInterface.do">
<table width="100%" border="3" cellpadding="0" cellspacing="0" class="maintable">
 <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key="<%=header%>" /></span></td>
      <%
	       String pageTitle= "Page Title - "+ApplicationProperties.getValue(header);
	  %>

		<td align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="<%=pageTitle%>"  width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
	
	<tr>
	<td class="td_tab_bg" ><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
	<!----Add tab hidden for the Specimen Search----->
	<logic:notEqual name="pageOf" value="<%=Constants.PAGE_OF_DISTRIBUTION%>">
		<logic:notEqual name="pageOf" value="<%=Constants.PAGE_OF_DISTRIBUTION_ARRAY%>">
			<logic:notEqual name="pageOf" value="<%=Constants.PAGE_OF_NEW_SPECIMEN%>">
        		<td valign="bottom"><html:link href="#" onclick="callSerachAction('CommonTab.do?pageOf='+pageOf)">
				<img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22" /></html:link></td>
       		</logic:notEqual>

			<td valign="bottom"><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit" width="59" height="22" border="0" /></td>
		</logic:notEqual>
	</logic:notEqual>

		<logic:equal name="pageOf" value="<%=Constants.PAGE_OF_USER_ADMIN%>">
        <td valign="bottom"><html:link page="/ApproveUserShow.do?pageNum=1"><img src="images/uIEnhancementImages/tab_approve_user.jpg" alt="Approve New Users" width="139" height="22" border="0" /></html:link></td>
		</logic:equal>
		<logic:equal name="pageOf" value="pageOfStorageContainer">
		<td  valign="bottom"><a href="#"><img src="images/uIEnhancementImages/view_map2.gif" alt="View Map"width="76" height="22" border="0" onclick="vieMapTabSelected()"/></a></td>
		</logic:equal>
<!-- These tabs are visible in case of specimen page--->
		 <logic:equal name="pageOf" value="<%=Constants.PAGE_OF_NEW_SPECIMEN%>">
			<td valign="bottom"><html:link page="/CreateSpecimen.do?operation=add&amp;pageOf=&virtualLocated=true">	<img src="images/uIEnhancementImages/tab_derive2.gif" alt="Derive" width="56" height="22" border="0"/>	</html:link></td>
			<td valign="bottom"><html:link page="/Aliquots.do?pageOf=pageOfAliquot"><img src="images/uIEnhancementImages/tab_aliquot2.gif" alt="Aliquot" width="66" height="22" border="0" >		</html:link></td>
			<td valign="bottom"><html:link page="/QuickEvents.do?operation=add"><img src="images/uIEnhancementImages/tab_events2.gif" alt="Events" width="56" height="22" border="0" />		</html:link></td>
			<td valign="bottom"><html:link page="/MultipleSpecimenFlexInitAction.do?pageOf=pageOfMultipleSpWithMenu"><img src="images/uIEnhancementImages/tab_multiple2.gif" alt="Multiple" width="66" height="22" border="0" />	</html:link></td>
		</logic:equal>
		<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>

	</tr>
	
	
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">

      <tr>
        <td colspan="2" align="left" class="bottomtd"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
      </tr>
      <tr>
        <td colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b"> &nbsp;<bean:message key="<%=title%>" /></span></td>

      </tr>

</table>
<table width="100%">
	<tr>
		<td align="left" class="bottomtd">
			<%@ include file="/pages/content/common/ActionErrors.jsp" %>
		</td>
	</tr>
</table>

	
			<table width="100%" >
	
	

	<tr>
		<td align="left">
				<div id='gridbox1' style='width:99%; height:500px; background-color:#d7d7d7;overflow:hidden'></div>
				<div><span id="pagingArea"></span>&nbsp;<span id="infoArea"></span></div>
		</td>
	<tr>
	<tr>
		<td align="left" width="33%" class="black_ar">
			
		</td>
	</tr>


</table>
</tr>
</td>
</table>
</html:form>
	<script>

	 mygrid = new dhtmlXGridObject("gridbox1");
	 mygrid.setImagePath("dhtmlxSuite_v35/dhtmlxGrid/codebase/imgs/");
	 mygrid.setHeader(columns);
	 //mygrid.setInitWidths("200,200");
	 mygrid.setInitWidthsP(colWidth);
	 mygrid.setEditable(false);
	 mygrid.setSkin("light");
	 mygrid.enableAutoHeigth(true);
	// mygrid.attachHeader("#connector_text_filter,#connector_text_filter");
	 mygrid.enableDistributedParsing(true,10,250);
	  mygrid.enablePaging(true, 10, 5, "pagingArea", true, "infoArea");
    mygrid.setPagingSkin("bricks");
	var headers = "";
	
	for(var row=0;row<ColCount;row++)
	{
		if(row !=0 && row < ColCount)
		headers = headers+",";
		
		headers = headers+"#connector_text_filter";
		
	}
	
	mygrid.attachHeader(headers);
	mygrid.init();
	 var gridQString = "LoadGridServlet?paramJson=1&aliasName="+aliasName+"&pageOf="+pageOf;//save query string to global variable (see step 5 for details)
	 
	 mygrid.loadXML(gridQString+"&connector=true");

</script>

