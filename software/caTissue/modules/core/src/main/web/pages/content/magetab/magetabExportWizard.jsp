<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.bizlogic.magetab.MagetabExportWizardBean"%>
<%@ page import="edu.wustl.catissuecore.bizlogic.magetab.MagetabExportWizardBean.State"%>
<%@ page import="edu.wustl.catissuecore.domain.Specimen"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
<title><bean:message key="magetab.magetabExportWizard"/></title>
<%
	//int pageNum = Integer.parseInt((String)request.getAttribute(Constants.PAGE_NUMBER));
	
	//int totalResults = ((Integer)session.getAttribute(Constants.TOTAL_RESULTS)).intValue();
	//int numResultsPerPage = Integer.parseInt((String)session.getAttribute(Constants.RESULTS_PER_PAGE));
	MagetabExportWizardBean wizardBean = (MagetabExportWizardBean)session.getAttribute(
			MagetabExportWizardBean.MAGETAB_EXPORT_WIZARD_BEAN);
	int currentPage = wizardBean.getCurrentPage();
%>
<LINK href="css/catissue_suite.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="jss/dhtmlwindow.js"></script>
<script type="text/javascript" src="jss/modal.js"></script>
<script type="text/javascript" src="jss/jquery/jquery-1.5.1.min.js"></script>
<SCRIPT language="javascript">
var $j = jQuery.noConflict();
function load() {
	var magetabWizard = this.parent.window.document.getElementById("MagetabWizard");
<%
	if (wizardBean.getState() == State.DEACTIVATED) {
%>
	this.parent.dhtmlmodal.close(magetabWizard);
<%		
	} else {
%>
//	magetabWizard.controls.onclick = function() {
//		submitOperation("Done");
//	};
<%
	}
%>
}

function submitOperation(operation)
{
	var form = document.forms[0];
	var cp = form.pageNo.value;
	if (cp == 2) {
		var ctr = 2;
		var numOfColumnsRemoved = 0;
		
		var numberOfCheckBoxes = 0;
		var numberOfCheckBoxesUnchecked = 0;
		$j('#sdrfData tr th input[type="checkBox"]').each(function(){
			numberOfCheckBoxes++;
			var id= $j(this).attr('id');
			var formElement = "form."+id+".checked";
			var elementVal = eval(formElement);
		//	alert (elementVal);
			if (!elementVal) {
				numberOfCheckBoxesUnchecked++;
			}
		})
		if ((numberOfCheckBoxes>1) && (numberOfCheckBoxes == numberOfCheckBoxesUnchecked)) {
			alert ("Please check atleast one Column");
			return true;
		}
		
		$j('#sdrfData tr th input[type="checkBox"]').each(function(){
			
			var id= $j(this).attr('id');
			var formElement = "form."+id+".checked";
			var elementVal = eval(formElement);
		//	alert (elementVal);
			if (!elementVal) {
				//var column = $j(this).parent().children().index(this); 
				removeColumn (ctr-numOfColumnsRemoved);
				numOfColumnsRemoved++;
			}
			ctr++;
		})
// 		$j('#sdrfData tr th').each(function(){
//             var content=$j(this).html();
            
//             var id = content.indexOf('id=');
//             var numOfColumnsRemoved=0;
//             if (id != -1) {
    //        	alert (id);
//             	var n = content.substring(0,2);
	//		alert (n);
//             	if (n=="So") {
//             		if ($j('#SoCheck').is(":checked")) {

//             		} else {
//             			var column = $j(this).parent().children().index(this); 
//             			removeColumn (column+1);
//             			numOfColumnsRemoved++;
//             		}
            		
            		
//             	}
//             	if (n=="Ex") {
//             		if ($j('#ExCheck').is(":checked")) {

//             		} else {
//             			var column = $j(this).parent().children().index(this); 
//             			removeColumn (column+1);
//             			numOfColumnsRemoved++
//             		}
//             	}
            	
//             	if (n=="Sa") {
					
//             		if ($j('#sampleColumnNumber').is(":checked")) {
//             			alert($j('#sampleColumnNumber').val());
//             		} else {
            		//	alert ("sample unchecked");
//             			var column = $j(this).parent().children().index(this); 
//             			removeColumn (column+1);
//             			numOfColumnsRemoved++
//             		}
//             	}
            	
// 			}
// 		})
		displayColumns();
		//alert ("sss");
	}
	
	//form.action = form.action + "?operation=" + operation;
	form.operation.value = operation;
	form.submit();
	return false;
}

</SCRIPT>

</head>
<body onload="load()">
<html:form action="/MagetabExportWizard.do" style="display:inline">
<input type="hidden" name="operation"/>
<input type="hidden" name="pageNo" value="<%= wizardBean.getCurrentPage() %>"/>
<table height="100%" width="100%" border="0" align="center" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
	<tr>
		<td><div id="errorDiv"></div></td>
	</tr>
	<tr>
		<td align="left">
			<table width="100%" border="0" cellpadding="3" cellspacing="0">
				<tr>
					<td><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr height="100%">
		<td align="left"> <!-- class="showhide" -->
			<jsp:include page='<%= "wizardPage" + currentPage +".jsp"%>' flush="true"/>
		</td>
	</tr>
	<tr >
		<td align="left" class="buttonbg">
		<%
			if(currentPage!=1)
			{
		%>
			<html:button property="btn" styleClass="blue_ar_b" onclick="submitOperation('Back' )">
				<bean:message key="magetab.prev"/>	
			</html:button>
		<%
			}
			if(currentPage!=3)
			{
				boolean disable=false;
				if(currentPage==2)
				{
					MagetabExportWizardBean.SseTable sseTable = wizardBean.getSseTable();					
					Set<Integer> chainSelections = sseTable.getChainSelections();
					if(chainSelections.size()==0)
					{
						disable=true;
					}
				}
				if(!disable)
				{
		%>			
			<html:button property="btn" styleClass="blue_ar_b" onclick="submitOperation('Forward' )">
				<bean:message key="magetab.next"/>	
			</html:button>
			<%
				}
			}
			if(currentPage==3)
			{
			%>
			<html:button property="btn" styleClass="blue_ar_b" onclick="submitOperation('Export' )">
				<bean:message key="magetab.export"/>	
			</html:button>
			<%
			}
			%>
			<html:button property="btn" styleClass="blue_ar_b" onclick="submitOperation('Done' )">
				Cancel
			</html:button>
		</td>
	</tr>
</table>
</html:form>
</body>
</html>
