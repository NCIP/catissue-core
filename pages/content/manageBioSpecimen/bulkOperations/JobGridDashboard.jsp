<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties" %>
<head>
<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXGrid.css"/>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script type="text/javascript" src="jss/ajax.js"></script>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script  src="dhtml_comp/js/dhtmlXCommon.js"></script>
<script  src="dhtml_comp/js/dhtmlXGrid.js"></script>
<script  src="dhtml_comp/js/dhtmlXGridCell.js"></script>
<script  src="dhtml_comp/js/dhtmlXGrid_mcol.js"></script>

<script>
	var xmlString='${requestScope.msgBoardXml}';

	function rowClick(id)
	{
		var colid ='${requestScope.identifierFieldIndex}';
		var cl = mygrid.cells(id,colid);
		var reqType=mygrid.cells(id,1).getValue();
		var searchId = cl.getValue();
		var url = "DocumentDownload.do?id="+searchId;
		window.location.href = url;
	}

	function showAttachment(id) {
		if (id == null || id == 'undefined' || id == "")
		{
			return;
		}
		else
		{
			var action = "DocumentDownload.do?id=" + id;
			mywindow=window.open(action,"Download","width=10,height=10");
			mywindow.moveTo(0,0);
		}
	}

	function initializeAjaxCall()
	{
		var noOfRows=mygrid.getRowsNum();
		for(var i=1;i<=noOfRows;i++)
		{
			var jobId=mygrid.cells(i,0).getValue();
			var status=mygrid.cells(i,3).getValue();
			if(status!="Completed"&&status!="Failed")
			{
				statusUpdateAjaxCall(jobId,i);
			}
		}
	}

	function statusUpdateAjaxCall(jobId, index)
	{
		var url="JobGridAjax.do?index="+index+"&jobId="+jobId;
		var request=newXMLHTTPReq();
		if(request == null)
		{
			alert ("Your browser does not support AJAX!");
			return;
		}
		var handlerFunction = getReadyStateHandler(request,responseHandler,true);
		request.onreadystatechange = handlerFunction;
		request.open("POST",url,true);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		request.send("");
	}

	function responseHandler(response)
	{
		var jsonResponse = eval('('+ response+')');
		if(jsonResponse.resultObject!=null)
	    {
			var jobStatus = jsonResponse.resultObject.jobStatus;
			var jobId=jsonResponse.resultObject.identifier;
			var jobName=jsonResponse.resultObject.jobName;
			var totalRecords=jsonResponse.resultObject.totalRecords;
			var processedRecords=jsonResponse.resultObject.processedRecords;
			var failedRecords=jsonResponse.resultObject.failedRecords;
			var timeTaken=jsonResponse.resultObject.timeTaken;
			var startedBy=jsonResponse.resultObject.startedBy;
			var index = jsonResponse.resultObject.index;
			var StatusObject=mygrid.cells(index,3);
			var statusValue=StatusObject.getValue();
			if(jobStatus=="Completed"||jobStatus=="Failed")
			{
				mygrid.cells(index,3).setCValue(jobStatus);
			}
			mygrid.cells(index,1).setCValue(jobName);
			mygrid.cells(index,4).setCValue(totalRecords);
			mygrid.cells(index,5).setCValue(processedRecords);
			mygrid.cells(index,6).setCValue(failedRecords);
			mygrid.cells(index,7).setCValue(timeTaken);
			if(jobStatus!="Completed"&&jobStatus!="Failed")
			{
				statusUpdateAjaxCall(jobId,index);
			}
		}
	}
</script>
<style>
.even
{
	background-color:#F8F8F8;
}
.uneven
{
	background-color:#FFFFFF;
}
.grid_on_hover
{
	background-color:#bfdcf3;
  font-size:20px;
  cursor: pointer;
}
</style>
</head>
	<table width="100%" border="0" cellpadding="3" cellspacing="0" >



		<tr height="100%">
		<td width="100%" height="100%">
		<table width="100%" height="100%" valign="top"border="0" cellpadding="3" cellspacing="0" align="center">
				<tr height="100%" width="100%">
					<td valign="top" height="100%" width="100%">
						<div id="gridbox" width="100%" height="340px"></div>
						<script>
			mygrid = new dhtmlXGridObject('gridbox');
				mygrid.setImagePath("dhtml_comp/imgs/");
				mygrid.init();
				mygrid.setStyle("font-family: Arial, Helvetica, sans-serif;font-size: 12px;font-weight: bold;color: #000000;background-color: #E2E2E2; border-left-width: 1px;border-left-color: #CCCCCC; border-top-width: 1px;border-top-color: #CCCCCC;border-bottom-color: #CCCCCC; border-bottom-width: 1px; border-right-width: 1px;border-right-color: #E2E2E2; text-align:left;padding-left:10px;padding-top:1px;padding-bottom:1px;align:left;height:100%;");

				mygrid.setEditable("FALSE");
				mygrid.enableAlterCss("uneven","even");
				mygrid.enableRowsHover(true,'grid_on_hover');
				mygrid.enableAutoHeigth(true);
				mygrid.objBox.style.height="100%";
				mygrid.loadXMLString(xmlString);
				mygrid.enableResizing(xmlString);
				//mygrid.setOnRowSelectHandler(funcName);

				for(var row=0;row<mygrid.getRowsNum();row++)
				{
					mygrid.setRowTextStyle(row+1,"font-family: Arial, Helvetica, sans-serif;font-size: 12px;padding-left:10px;color: #000000;border-left-width: 1px;border-left-color: #CCCCCC;  border-bottom-color: #CCCCCC; border-bottom-color: #CCCCCC; border-right-width: 1px;border-right-color: #FFFFFF; Cursor: pointer;word-wrap:break-word;");
				}

				//mygrid.setSizes();
				function getIDColumns()
				{
					var hiddenColumnNumbers = new Array();
					hiddenColumnNumbers[0]=${requestScope.identifierFieldIndex};
					return hiddenColumnNumbers;
				}


				//:To hide ID columns
				/*var hideCols = getIDColumns();
				for(i=0;i<hideCols.length;i++)
				{
					mygrid.setHeaderCol(hideCols[i],"");
					mygrid.setColumnHidden(hideCols[i],true);
				}*/
				initializeAjaxCall();
			// Bug Fixed : - 12548  and 12552  ( added by amit_doshi @ 24 July 2009)
			mygrid.objBox.style.overflowX = "hidden";
		    mygrid.objBox.style.overflowY = "hidden";



		</script>
					</td>
			</tr>

		</table>
		</td>
		</tr>

</table>
