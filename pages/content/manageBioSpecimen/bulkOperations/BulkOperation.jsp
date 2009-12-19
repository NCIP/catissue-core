<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties" %>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>


<SCRIPT>
function onDownLoadTemplate()
{
	var dropdownName =	document.getElementById('operationName').value;
	if (dropdownName == null || dropdownName == 'undefined' || dropdownName == "")
	{
		alert("Incorrect Template Name.");
	}
	else
	{
		var action = "DownloadCSV.do?dropdownName=" + dropdownName;
		mywindow = window.open(action, "Download", "width=10,height=10");
		mywindow.moveTo(0,0);
	}
}

function onUploadClick()
{
	var dropdownName =	document.getElementById('operationName').value;
	if (dropdownName == null || dropdownName == 'undefined' || dropdownName == "")
	{
		alert("Please select a Template Name.");
	}
	else
	{
		var uploadFileName = document.getElementById('file').value;
		var fileNameArray = uploadFileName.split(".");
		var arraySize = fileNameArray.length;
		if(fileNameArray[arraySize -1] != 'csv')
		{
			alert("Please upload a comma seperated file (.csv).");
		}
		else
		{
			/*window.frames[0].document.getElementById('operationName').value=document.getElementById('operationName').value;
			alert(window.frames[0].document.getElementById('file').value);
			window.frames[0].document.getElementById('file').value=document.getElementById('file').value;
			alert(window.frames[0].document.getElementById('operationName').value);
			alert(window.frames[0].document.getElementById('file').value);
			//alert(document.frames("bulkOperationDashoard").document.forms[0].elements("operationName").value);

			//top.parent.frames['bulkOperationDashoard'].location=document.forms[0].action;*/
			document.forms[0].submit();
		}
	}
}

function getCSVOutputReport()
{
	<%
		File file = (File)request.getSession().getAttribute("resultFile");
		if (file != null)
		{
			%>
				var action = "DownloadCSV.do?report=report";
				mywindow = window.open(action, "Download", "width=10,height=10");
				mywindow.moveTo(0,0);
			<%
		}
	%>
}

function showBulkOperationDashboard()
{
	document.forms[0].action="ShowBulkOperationDashboard.do";
	document.forms[0].submit();
}

</SCRIPT>
<head>
	<LINK href="css/styleSheet.css" type="text/css" rel="stylesheet">
	<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
</head>
<body>
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable contentLayout">

  <tr>
    <td class="tablepadding" height="100%" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
      <table width="100%" height="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
      <tr>
        <td align="left" height="1%"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
      </tr>
		<!--<c:if test="${requestScope.report != null }">
		<tr>
			<td >&nbsp;
				<a href="#" onclick="getCSVOutputReport();"><span class="black_ar">
					<bean:message key="bulk.view.report" /></span>
				</a>
			</td>
		</tr>
		</c:if>-->
      <tr height="2%">
        <td align="left" height="2%" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="bulk.bulkoperations" /></span></td>
      </tr>
      <tr height="30%">
        <td align="left" valign="top" class="showhide">

		<logic:empty name="noTemplates">
		<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<html:form action="/FileUpload.do" method="post" enctype="multipart/form-data">
              <tr>
                <td width="1%" align="center" class="black_ar"><span class="blue_ar_b"></span></td>
                <td width="20%" align="left" class="black_ar"><b><bean:message key="bulk.download.template" /></b></td>
                <td width="25%" align="left" valign="middle" class="black_new">
					  <autocomplete:AutoCompleteTag
						  property="operationName"
						  optionsList = "<%=request.getAttribute(Constants.BULK_OPEARTION_LIST)%>"
						  initialValue="<%=request.getAttribute("dropdownName")%>"
						  onChange="onTypeChange(this)"
						  readOnly="false"
						  size="31"
						  styleClass="black_ar"
					/>
				</td>
				<td colspan="2" width="54%" class="black_ar"><span class="blue_ar_b" valign="baseline"></span>
					<html:button styleClass="blue_ar_b" onclick="onDownLoadTemplate()" accesskey="enter" property="">
									<bean:message key="bulk.button.download.template" />
						</html:button>
				</td>
			  </tr>

			  <tr>
                <td colspan="5" width="100%" height="20" align="center" class="black_ar"><span class="blue_ar_b"></span>
				</td>
			  </tr>
              <tr>
                <td align="center" class="black_ar"><span class="blue_ar_b"></span>
				</td>
                <td align="left" class="black_ar"><b><bean:message key="bulk.upload.file" /></b>
				</td>
                <td align="left" valign="middle" width="20%">
					<input id="file" type="file" name="file" value="Browse">
					</input>
				</td>
				<td align="left" valign="left" colspan="2">
						<html:button styleClass="blue_ar_b" onclick="onUploadClick()" accesskey="enter" property="">
								<bean:message key="bulk.button.upload" />
						</html:button>
				</td>
              </tr>
              </html:form>
        </table>
		</logic:empty>
		<logic:notEmpty name = "noTemplates">
			<table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
				<tr height="40">
				    <td align="left" class="black_ar">
						<b><bean:message key="bulk.no.templates.loaded.message" /></b>
					</td>
				</tr>
			</table>
		</logic:notEmpty>

		</td>
      </tr>
      <tr height="*">
      <td height="*" valign="top">
	  <table width="100%" valign="top" border="2" cellpadding="3" cellspacing="0" class="whitetable_bg">
		  <tr>
		   <td width="100%" align="center" class="black_ar">

			<iframe id="bulkOperationDashoard" height="100%"name="bulkOperationDashoard" src="JobDashboard.do" scrolling="auto" frameborder="0" style="width:100%;height:100%;" marginheight='0' marginwidth='0'>
													Your Browser doesn't support IFrames.
				</iframe>
		   </td>
		  </tr>
	  </table>
	  </td>
      </tr>
    </table></td>
  </tr>

</table>
</body>
</html>