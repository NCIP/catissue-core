<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.SiteForm"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>

<%@ include file="/pages/content/common/AdminCommonCode.jsp"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp"%>
<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />
<link rel="STYLESHEET" type="text/css" href="newDhtmlx/dhtmlxgrid.css"/>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script language="JavaScript"  type="text/javascript" src="newDhtmlx/dhtmlxcommon.js"></script>
<script  language="JavaScript" type="text/javascript" src="newDhtmlx/dhtmlxgrid.js"></script>
<script   language="JavaScript" type="text/javascript" src="newDhtmlx/dhtmlxgridcell.js"></script>
<script  language="JavaScript" type="text/javascript"  src="newDhtmlx/ext/dhtmlxgrid_srnd.js"></script>

<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>
<script language="JavaScript" src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script>
var mygrid;
var selectedComboValue;
function onComboChange(asd){
	var option = asd.options;
	var selectValue = "";
	for(var cnt=0;cnt<option.length;cnt++){
		if(option[cnt].selected){
				selectValue = option[cnt].value;
				selectedComboValue = option[cnt].value;
				break;
		}
	}
	if(selectValue!=""){
		<logic:equal name="operation" value="scg">
				gridQString = "LoadGridServlet?paramJson="+selectValue+"&gridType=scg";//save query string to global variable (see step 5 for details)
				mygrid.enableSmartRendering(true,10);
				mygrid.clearAndLoad(gridQString+"&connector=true&dhx_sort[1]=des");
		</logic:equal>
		<logic:equal name="operation" value="specimen">
			gridQString = "LoadGridServlet?paramJson="+selectValue+"&gridType=specimen";//save query string to global variable (see step 5 for details)
			mygrid.enableSmartRendering(true,10);
			mygrid.clearAndLoad(gridQString+"&connector=true&dhx_sort[1]=des");
		</logic:equal>
	}

}


</script>


	<html:form action="UtilizeSpp.do">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b">Apply SPP<!--bean:message key="site.header" /--></span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Site" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
   <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="4%" class="td_tab_bg" ><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
	<logic:equal name="operation" value="scg">
        <td valign="bottom" ><img src="images/uIEnhancementImages/scg_sel.gif" alt="Speciamn Collection Group" width="131" height="22" /></td>
        <td valign="bottom"><html:link
													page="/UtilizeSpp.do?operation=specimen&amp;pageOf=pageOfUtilizeSpp&amp;menuSelected=5"><img src="images/uIEnhancementImages/speciman_notsel.gif" alt="Specimen" width="62" height="22" border="0" /></html:link></td>
	</logic:equal>
	<logic:equal name="operation" value="specimen">
		 <td valign="bottom" ><html:link page="/UtilizeSpp.do?operation=scg&amp;pageOf=pageOfUtilizeSpp&amp;menuSelected=5"><img src="images/uIEnhancementImages/scg_notsel.gif" alt="Speciamn Collection Group" width="131" height="22" /></html:link></td>
        <td valign="bottom"><img src="images/uIEnhancementImages/speciman_sel.gif" alt="Specimen" width="62" height="22" border="0" /></td>
	</logic:equal>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
	</td>
	</tr>
</table>
<table width="100%">
	<tr>
		<td align="left" class="bottomtd">
			<%@ include file="/pages/content/common/ActionErrors.jsp" %>
		</td>
	</tr>
</table>
<logic:equal name="operation" value="scg">
	<table width="100%" >
	<tr>
			<td align="left" width="33%" class="black_ar">
				&nbsp;&nbsp;SPP
				&nbsp;&nbsp;&nbsp;&nbsp;

				<html:select
										property="name" styleId="sppIds"
										styleClass="formFieldSizedNew"
										onchange="onComboChange(this,'cpCheckId')">
										<html:options collection="sppList" labelProperty="name"
											property="value" />
									</html:select></td>
			</td>
	</tr>
	<script>
		selectedComboValue = ${requestScope.selectedSpp};
		var optionlist = document.getElementById("sppIds").options
		for(var cnt=0;cnt<optionlist.length;cnt++){
			if(optionlist[cnt].value == selectedComboValue){
				optionlist[cnt].selected = true;
				break;
			}
		}

	</script>
	<tr>
		<td align="left">
				<div id='gridbox1' style='width:99%; height:200px; background-color:#d7d7d7;overflow:hidden'></div>
		</td>
	<tr>
	<tr>
		<td align="left" width="33%" class="black_ar">
			<input type="checkbox" id="selectallcheck" onclick="onSelectAll(this)" property="" id="checkAllOnThisPage" name="checkAllOnThisPage"/>
				Check All On This Page &nbsp
			<!--input type="checkbox" onclick="onSelectAll(this)" property="" id="checkAll" name="checkAll"/!-->
			<!--	Check All -->
		</td>
	</tr>

	<tr>
       <td class="buttonbg" colspan="2">
		<input type="button" class="blue_ar_b" onclick="onSubmit(this.form.orderedString)" value="Enter SPP Data" name="proceed">
	   </td>
     </tr>

	<script>

		var columns =${requestScope.columns};
		var colWidth =${requestScope.colWidth};
		var isWidthInPercent=${requestScope.isWidthInPercent};
		var colTypes =${requestScope.colTypes};



	 mygrid = new dhtmlXGridObject({parent:"gridbox1"});
	 mygrid.setImagePath("newDhtmlx/imgs/");
	 mygrid.setHeader(columns);
	 mygrid.setInitWidths("45,150,150,150,150,*");
	 // gridObj.setColAlign(gridInfo['columnAlign']);
	 mygrid.setEditable(false);
	 mygrid.setSkin("light");
	 mygrid.enableAutoHeigth(true,"290");
	 mygrid.init();
	// gridObj.enablePaging(true, 5, 3, gridInfo['pagingArea'], true, gridInfo['infoArea']);
	// gridObj.setPagingSkin("bricks");
	 mygrid.setColSorting(",connector,connector,connector,connector,connector");
	 gridQString = "LoadGridServlet?paramJson=1&gridType=scg";//save query string to global variable (see step 5 for details)
	 mygrid.enableSmartRendering(true,10);
	 mygrid.loadXML(gridQString+"&connector=true&dhx_sort[1]=des");
	 mygrid.attachEvent("onXLE", function(grid_obj,count){
		onSelectAll(document.getElementById("selectallcheck"));
	 });



	</script>



	</table>
	</logic:equal>

	<logic:equal name="operation" value="specimen">
			<table width="100%" >
	<tr>
			<td align="left" width="33%" class="black_ar">
				&nbsp;&nbsp;SPP
				&nbsp;&nbsp;&nbsp;&nbsp;

				<html:select
										property="name" styleId="sppIds"
										styleClass="formFieldSizedNew"
										onchange="onComboChange(this,'cpCheckId')">
										<html:options collection="sppList" labelProperty="name"
											property="value" />
									</html:select></td>
			</td>
	</tr>
	<script>
		selectedComboValue = ${requestScope.selectedSpp};
		var optionlist = document.getElementById("sppIds").options
		for(var cnt=0;cnt<optionlist.length;cnt++){
			if(optionlist[cnt].value == selectedComboValue){
				optionlist[cnt].selected = true;
				break;
			}
		}

	</script>

	<tr>
		<td align="left">
				<div id='gridbox1' style='width:99%; height:200px; background-color:#d7d7d7;overflow:hidden'></div>
		</td>
	<tr>
	<tr>
		<td align="left" width="33%" class="black_ar">
			<input type="checkbox"  id="selectallcheck"  onclick="onSelectAll(this)" property="" id="checkAllOnThisPage" name="checkAllOnThisPage"/>
				Check All On This Page &nbsp
			<!--input type="checkbox" onclick="onSelectAll(this)" property="" id="checkAll" name="checkAll"/!-->
			<!--	Check All -->
		</td>
	</tr>

	<tr>
       <td class="buttonbg" colspan="2">
		<input type="button" class="blue_ar_b" onclick="onSubmit(this.form.orderedString)" value="Enter SPP Data" name="proceed">
	   </td>
     </tr>

	<script>

		//var myData =${requestScope.myData};
		var columns =${requestScope.columns};
		var colWidth =${requestScope.colWidth};
		var isWidthInPercent=${requestScope.isWidthInPercent};
		var colTypes =${requestScope.colTypes};


	 mygrid = new dhtmlXGridObject({parent:"gridbox1"});
	 mygrid.setImagePath("newDhtmlx/imgs/");
	 mygrid.setHeader(columns);
	 mygrid.setInitWidths("45,150,100,150,100,140,150,*");
	// gridObj.setColAlign(gridInfo['columnAlign']);
	 mygrid.setEditable(false);
	 mygrid.setSkin("light");
	 mygrid.enableAutoHeigth(true,"290");
	 mygrid.init();
	// gridObj.enablePaging(true, 5, 3, gridInfo['pagingArea'], true, gridInfo['infoArea']);
	// gridObj.setPagingSkin("bricks");
	 mygrid.setColSorting(",connector,connector,connector,connector,connector");
	 gridQString = "LoadGridServlet?paramJson=1&gridType=specimen";//save query string to global variable (see step 5 for details)
	 mygrid.enableSmartRendering(true,10);
	 mygrid.loadXML(gridQString+"&connector=true&dhx_sort[1]=des");
	  mygrid.attachEvent("onXLE", function(grid_obj,count){
		onSelectAll(document.getElementById("selectallcheck"));
	 });

	</script>



	</table>


	</logic:equal>
</html:form>
<script>

function onSubmit(){
	var selectedAll = "false";
	var rowCount = mygrid.getRowsNum();
	var selecedValue="";

	if(document.getElementById("gridselectall").checked){
			selectedAll = "true";
	}else{
		for(i=0;i<rowCount;i++)
		{
			try{
				var cl = mygrid.cellByIndex(i,0);
				if(cl.cell.firstChild.checked){
					selecedValue += cl.cell.firstChild.value+',';
				}
			}catch(ex){
				break;
			}

		}
		selecedValue = selecedValue!=""?selecedValue.substr(0,selecedValue.length-1):selecedValue;
	}
	var url = ""
	<logic:equal name="operation" value="scg">
		url = "DisplaySPPEventsFromDashboardAction.do?pageOf=pageOfDynamicEvent&scgId="+selecedValue+"&selectedAll="+selectedAll+"&sppId="+selectedComboValue;
	</logic:equal>
	<logic:equal name="operation" value="specimen">
		url = "DisplaySPPEventsFromDashboardAction.do?pageOf=pageOfDynamicEvent&specimenId="+selecedValue+"&selectedAll="+selectedAll+"&sppId="+selectedComboValue;
	</logic:equal>
	location.href  = url;
}
function onSelectAll(element){
	document.getElementById("gridselectall").checked = element.checked;
	document.getElementById("selectallcheck").checked = element.checked;
	var rowCount = mygrid.getRowsNum();
	var selecedValue="";
	mygrid.setEditable(true);
	var state=element.checked;

	rowCount = mygrid.getRowsNum();
	for(i=0;i<rowCount;i++)
	{
		try{
			var cl = mygrid.cellByIndex(i,0);
			cl.cell.firstChild.checked = state;

		}catch(ex){
			break;
		}
	}

}
onComboChange(document.getElementById("sppIds"),'cpCheckId');
</script>
