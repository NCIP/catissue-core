<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties" %>
<head>
<link rel="STYLESHEET" type="text/css"
	href="dhtmlx_suite/css/dhtmlxgrid.css" />
<link rel="STYLESHEET" type="text/css"
	href="dhtmlx_suite/skins/dhtmlxgrid_dhx_skyblue.css" />
<script language="JavaScript" type="text/javascript"
	src="jss/bulkOperatorScripts.js"></script>
<script type="text/javascript" src="jss/bulkOperatorAjax.js"></script>
<link href="css/bulkOperator.css" rel="stylesheet" type="text/css" />
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxgrid.js"></script>
<script src="dhtmlx_suite/js/dhtmlxgridcell.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxgrid_mcol.js"></script>
<script>
	var xmlString='${requestScope.msgBoardXml}';
	var latestRowId='';
	
	
	

	function showAttachment(id) {
		if (id == null || id == 'undefined' || id == "")
		{
			return;
		}
		else
		{
			var action = "MatchingReportDownload.do?id=" + id;
			mywindow=window.open(action,"Download","width=10,height=10");
			mywindow.moveTo(0,0);
		}
	}

	
	function getStatus(index)
	{
		var status;
		
		status=mygrid.cells(index,5).getValue();
				
		return status;
	}
	
	function updateStatus(index,jobStatus)
	{
	
		
					mygrid.cells(index,5).setCValue(jobStatus);
				
		return status;
	}
	function initiateAjaxForNewRows(id)
	{
		var rowIndex=mygrid.getRowIndex(id);
		//alert("rowIndex   "+rowIndex);
		var jobId=mygrid.cells(id,0).getValue();
		//alert("jobId   "+jobId);
				//alert("Id   "+id);
		var status=getStatus(id);
			
		if(status!="Completed"&&status!="Failed")
			{
				statusUpdateAjaxCall(jobId,id);
			}
	}

	function initializeAjaxCall()
	{
		var noOfRows=mygrid.getRowsNum();
		if(noOfRows != 0 && noOfRows != null && noOfRows != '')
		{
			for(var i=1;i<=noOfRows;i++)
			{
				var jobId=mygrid.cells(i,0).getValue();
				var status=getStatus(i);
				if(status!="Completed"&&status!="Failed")
				{
					statusUpdateAjaxCall(jobId,i);
				}
			}
		}
	}

	function statusUpdateAjaxCall(jobId, index)
	{
		
		var url="BulkMatchingGridAction.do?index="+index+"&jobId="+jobId;
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
			var StatusObject=getStatus(index);
		
			if(jobStatus=="Completed"||jobStatus=="Failed")
			{
				updateStatus(index,jobStatus)
			}
			mygrid.cells(index,1).setCValue(jobName);
		
				mygrid.cells(index,6).setCValue(totalRecords);
				mygrid.cells(index,7).setCValue(processedRecords);
				mygrid.cells(index,8).setCValue(failedRecords);
				mygrid.cells(index,9).setCValue(timeTaken);
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
				mygrid.setImagePath("dhtmlx_suite/imgs/");
				mygrid.setSkin("dhx_skyblue");
				mygrid.init();
				mygrid.setEditable("FALSE");
				mygrid.enableAlterCss("uneven","even");
				mygrid.enableRowsHover(true,'grid_on_hover');
				mygrid.enableAutoHeigth(true);
				mygrid.objBox.style.height="100%";
				mygrid.loadXMLString(xmlString);
				mygrid.enableResizing(xmlString);
				//mygrid.setOnRowSelectHandler(funcName);
				//mygrid.selectRow(mygrid.getRowIndex(actualRowCount),false,false,true);

				
				mygrid.setSizes();
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
				//Initializing ajax call to update individual cells of grid.
				//initializeAjaxCall();

			mygrid.objBox.style.overflowX = "hidden";
		    mygrid.objBox.style.overflowY = "hidden";



		</script>
					</td>
			</tr>

		</table>
		</td>
		</tr>

	</tr>

</table>
