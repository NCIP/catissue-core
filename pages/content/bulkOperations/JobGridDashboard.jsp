<%--L
   Copyright Washington University in St. Louis
   Copyright SemanticBits
   Copyright Persistent Systems
   Copyright Krishagni

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
L--%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties"%>
<head>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.css">
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/skins/dhtmlxgrid_dhx_skyblue.css">
<script language="JavaScript" type="text/javascript"
	src="jss/bulkOperatorScripts.js"></script>
<script type="text/javascript" src="jss/bulkOperatorAjax.js"></script>
<link href="css/bulkOperator.css" rel="stylesheet" type="text/css" />
<script src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxcommon.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgridcell.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_mcol.js"></script>

<script>
	var xmlString='${requestScope.msgBoardXml}';
	var latestRowId='';
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
		if(noOfRows != 0 && noOfRows != null && noOfRows != '')
		{
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
	}

	function initiateAjaxForNewRows(id)
	{
		var rowIndex=mygrid.getRowIndex(id);
		//alert("rowIndex   "+rowIndex);
		var jobId=mygrid.cells(id,0).getValue();
		//alert("jobId   "+jobId);
				//alert("Id   "+id);
		var status=mygrid.cells(id,3).getValue()
			//alert("status   "+status);
		if(status!="Completed"&&status!="Failed")
			{
				statusUpdateAjaxCall(jobId,id);
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
				//statusUpdateAjaxCall(jobId,index);
			}
		}
	}


	function getGridXml()
	{
		if(latestRowId != null && latestRowId != '' && latestRowId != 'undefined')
		{
			var jobID=latestRowId;
		}
		else
		{
			var jobID=mygrid.cells(1,0).getValue();
		}
		if(jobID != null && jobID != '' && jobID != 'undefined')
		{
			var url="JobDashboard.do?requestType=ajax&jobId="+jobID;
			var request=newXMLHTTPReq();
			if(request == null)
			{
				alert ("Your browser does not support AJAX!");
				return;
			}
			var handlerFunction = getReadyStateHandler(request,setGridXML,true);
			request.onreadystatechange = handlerFunction;
			request.open("POST",url,true);
			request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			request.send("");
		}
	}
	function setGridXML(response)
	{
		var jsonResponse = eval('('+ response+')');
		//alert("jsonResponse   "+jsonResponse);
		if(jsonResponse.resultObject!=null)
	    {
			var xmlGrid=jsonResponse.resultObject.xmlGrid;
			var contentList=new String(jsonResponse.resultObject.contentList);
			if(contentList != null && contentList != 'undefined')
			{
				//alert("contentList   "+contentList);
				latestRowId=jsonResponse.resultObject.latestId;
				var rowsList=contentList.split("#");
				//alert("latestRowId   "+latestRowId);
				for(var i=0;i<rowsList.length;i++)
				{
					var rowList=rowsList[i].split(",");
					//alert("rowList   "+rowList);
					var content=rowList[0]+","+rowList[1]+","+rowList[2]+","+rowList[3]+","+rowList[4]+","+rowList[5]+","+rowList[6]+","+rowList[7]+","+rowList[8];
					//alert("content   "+content);
					//alert(mygrid.getRowsNum());
					mygrid.addRow(mygrid.getRowsNum()+1,content,0);
					//mygrid.setRowTextStyle(mygrid.getRowsNum(),"font-family: Arial, Helvetica, sans-serif;font-size: 12px;padding-left:10px;color: #000000;border-left-width: 1px;border-left-color: #CCCCCC;  border-bottom-color: #CCCCCC; border-bottom-color: #CCCCCC; border-right-width: 1px;border-right-color: #FFFFFF; Cursor: pointer;word-wrap:break-word;");
					var newRowId=mygrid.getRowId(0);
					//alert(newRowId);
					initiateAjaxForNewRows(newRowId);
					//alert(mygrid.getRowsNum());

				}
				/*for(var row=0;row<=mygrid.getRowsNum();row++)
				{
					mygrid.setRowTextStyle(row+1,"font-family: Arial, Helvetica, sans-serif;font-size: 12px;padding-left:10px;color: #000000;border-left-width: 1px;border-left-color: #CCCCCC;  border-bottom-color: #CCCCCC; border-bottom-color: #CCCCCC; border-right-width: 1px;border-right-color: #FFFFFF; Cursor: pointer;word-wrap:break-word;");
				}*/
			}
			//alert(mygrid.getRowsNum());

			//alert("xmlGrid   "+xmlGrid);
			//mygrid.loadXMLString(xmlGrid);
			//alert(jsonResponse.resultObject.gridRefreshTime);
			var refreshTimeInterval=jsonResponse.resultObject.gridRefreshTime;
			if(refreshTimeInterval != null && refreshTimeInterval != 0 )
			{
				parent.refreshinterval=refreshTimeInterval;
			}
		}
		//alert("refreshinterval"+parent.refreshinterval);
		//parent.refreshinterval=10;
		//alert("refreshinterval"+parent.refreshinterval);
		for(var row=0;row<=mygrid.getRowsNum();row++)
				{
					mygrid.setRowTextStyle(row+1,"font-family: Arial, Helvetica, sans-serif;font-size: 12px;padding-left:10px;color: #000000;border-left-width: 1px;border-left-color: #CCCCCC;  border-bottom-color: #CCCCCC; border-bottom-color: #CCCCCC; border-right-width: 1px;border-right-color: #FFFFFF; Cursor: pointer;word-wrap:break-word;");
				}
	}
</script>
<style>
.even {
	background-color: #F8F8F8;
}

.uneven {
	background-color: #FFFFFF;
}

.grid_on_hover {
	background-color: #bfdcf3;
	font-size: 20px;
	cursor: pointer;
}
</style>
</head>
<table width="100%" border="0" cellpadding="3" cellspacing="0">



	<tr height="100%">
		<td width="100%" height="100%">
		<table width="100%" height="100%" valign="top" border="0"
			cellpadding="3" cellspacing="0" align="center">
			<tr height="100%" width="100%">
				<td valign="top" height="100%" width="100%">
				<div id="gridbox" width="100%" height="340px"></div>
				<script>
				mygrid = new dhtmlXGridObject('gridbox');
				mygrid.setImagePath("dhtmlx_suite/imgs/");
				mygrid.setSkin("dhx_skyblue");
				mygrid.init();
				mygrid.setEditable("FALSE");
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
				initializeAjaxCall();

			mygrid.objBox.style.overflowX = "hidden";
		    mygrid.objBox.style.overflowY = "hidden";



		</script></td>
			</tr>

		</table>
		</td>
	</tr>

	</tr>

</table>
