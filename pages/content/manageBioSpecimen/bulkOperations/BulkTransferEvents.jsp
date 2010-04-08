<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.BulkEventOperationsForm"%>
<%@ page import="java.util.*"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<LINK href="css/catissue_suite.css" type=text/css rel=stylesheet>
<head>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
    <script type="text/javascript" src="jss/wz_tooltip.js"></script>
</head>

<%
    List speList = (List)request.getAttribute(Constants.SPECIMEN_ID_LIST);
    BulkEventOperationsForm bulkEventOperationsForm =(BulkEventOperationsForm) request.getAttribute("bulkEventOperationsForm");
	int count= speList.size();

%>

<script language="JavaScript" type="text/javascript">

function beforeApplyAll()
 {
 	var specimencount = "<%= count %>";
	  if(specimencount >1)
	 {
		ApplyToAll();
	 }
	 else
	 {
		 alert("Only one specimen is present!");
	 }

 }

function ApplyToAll() {

var fields = document.getElementsByTagName("input");

var i =0; var text=""; var valueToSet = ""; var isFirstField = true; var isFirstFieldDisabled = false; for (i=0; i<fields.length;i++) { text = fields[i].name; if(text.indexOf("_TOSCLABEL")>=0) { if(isFirstField) { if(!fields[i].disabled) { valueToSet = fields[i].value; } else { valueToSet = ""; isFirstFieldDisabled = true;								} isFirstField = false; } if(isFirstFieldDisabled) { fields[i].disabled = true; } else { fields[i].disabled = false; }

fields[i].value = valueToSet;



} else if(text.indexOf("_TOVirLoc")>=0) { if(isFirstFieldDisabled) { fields[i].checked = true; } else { fields[i].checked = false; } } } }

function virtualLocationSelChanged(specimenId) { if(document.getElementById("VirLocChk"+specimenId).checked) { document.getElementById("SelCont"+specimenId).value = ""; document.getElementById("SelCont"+specimenId).disabled = true; } else { document.getElementById("SelCont"+specimenId).value = ""; document.getElementById("SelCont"+specimenId).disabled = false; } }

</script> <html:form action="BulkTransferEventsSubmit.do" > <jsp:include page="/pages/content/manageBioSpecimen/bulkOperations/BulkEventsCommonAttributes.jsp" />
<table width="100%">
<tr> <td>
<bean:define id="firstSpecimen" value="true" />
<table width="100%" border="0" cellspacing="0" cellpadding="2">
<tr>

  <td width="20%" class="tableheading"><strong><bean:message key="specimenPPI"/></strong></td>
  <td width="20%" class="tableheading"><strong><bean:message key="specimenLabel"/></strong></td>
  <td width="15%" class="tableheading"><strong><bean:message key="specimenType"/></strong></td>
  <td width="15%" class="tableheading"><strong><bean:message key="specimenQuantity"/></strong></td>
  <td width="15%" class="tableheading"><strong><bean:message key="specimenfromLocation"/></strong></td>

  <td width="15%" class="tableheading" nowrap><strong><bean:message key="specimenDestContainer"/></strong></td>

  <!--  if first Specimen in the list add "Apply to All" Link -->
  <td class="tableheading" width="1%">
  <logic:equal name="firstSpecimen"  value="true" >
   <html:link href="#" styleClass="black_ar" onmouseover="Tip('Apply first location to all',WIDTH,280)" onclick="javascript:beforeApplyAll();" > <bean:message key="aliquots.applyFirstToAll"/> </html:link>  <bean:define id="firstSpecimen" value="false"/>
  </logic:equal>
  <logic:notEqual name="firstSpecimen" value="true" > &nbsp;
  <bean:define id="onlyoneSpecimen" value="false" />
  </logic:notEqual>
 </td>

 </tr>

 <tr>




<logic:iterate id="specimenId" name="<%=Constants.SPECIMEN_ID_LIST%>" scope="request" indexId="id">
<% String specimenLabelField = "fieldValue(ID_"+specimenId+"_LABEL)";
String specimenPPIField = "fieldValue(ID_"+specimenId+"_PPI)";
String specimenQuantityField = "fieldValue(ID_"+specimenId+"_QUANTITY)";
String specimenTypeField = "fieldValue(ID_"+specimenId+"_SPECTYPE)";

String specimenFromLocField = "fieldValue(ID_"+specimenId+"_FROMLOC)";
String specimenFromLocIDField = "fieldValue(ID_"+specimenId+"_FROMLOCID)";
String specimenFromLocPos1Field = "fieldValue(ID_"+specimenId+"_FROMLOCPOS1)";
String specimenFromLocPos2Field = "fieldValue(ID_"+specimenId+"_FROMLOCPOS2)";
String specimenToSCLabelField = "fieldValue(ID_"+specimenId+"_TOSCLABEL)";
String specimenToSCIDField = "fieldValue(ID_"+specimenId+"_TOSCID)";
String specimenToSCPos1Field = "fieldValue(ID_"+specimenId+"_TOSCPOS1)";
String specimenToSCPos2Field = "fieldValue(ID_"+specimenId+"_TOSCPOS2)";

String containerId = "Cont"+specimenId; String selContainerId = "SelCont"+specimenId; String pos1Id = "Pos1Id"+specimenId; String pos2Id = "Pos2Id"+specimenId;

String specimenList = "specimenId("+specimenId+")"; String specimenToVirLocField = "fieldValue(ID_"+specimenId+"_TOVirLoc)"; String methodCall = "virtualLocationSelChanged("+specimenId+")"; String virLoc = "VirLocChk"+specimenId;

%>

 <tr>
<html:hidden property="orderedString" />
<html:hidden property="<%=specimenFromLocIDField%>" />
<html:hidden property="<%=specimenFromLocPos1Field%>" />
<html:hidden property="<%=specimenFromLocPos2Field%>" />
<html:hidden property="<%=specimenList%>" />
<html:hidden property="<%=specimenFromLocField%>" />

<td >
<div id="specimenPPIField"
	style="word-wrap: break-word;width:100;
	font-family: verdana;
	font-size: 11px;
	color: #171717;
	vertical-align:middle;
	margin:0px;">
	<label for="type"> <bean:write name="bulkEventOperationsForm" property="<%=specimenPPIField%>" /> </label>
</div>	
</td>

<td> 
<div id="specimenLabelField"
	style="word-wrap: break-word;width:200;
	font-family: verdana;
	font-size: 11px;
	color: #171717;
	vertical-align:middle;
	margin:0px;">
	<label for="type"> <bean:write name="bulkEventOperationsForm" property="<%=specimenLabelField%>" /> </label>
</div>	
</td>

<td class="black_ar" >
	<label for="type"> <bean:write name="bulkEventOperationsForm" property="<%=specimenTypeField%>" /> </label>
</td>

<td class="black_ar" >
	<label for="type"> <bean:write name="bulkEventOperationsForm" property="<%=specimenQuantityField%>" /> </label>
</td>

<td class="black_ar" >
	<label for="type"> <bean:write name="bulkEventOperationsForm" property="<%=specimenFromLocField%>" /> </label> <!--html:text styleId="<%=containerId%>" property="<%=specimenFromLocField%>" readonly="true" /-->
</td>


<!-- To Container Field starts -->

<%
String url = "ShowFramedPage.do?pageOf=pageOfSpecimen&amp;selectedContainerName="+selContainerId+"&amp;pos1="+pos1Id+"&amp;pos2="+pos2Id+"&amp;containerId="+selContainerId+"&amp;"+
Constants.CAN_HOLD_SPECIMEN_CLASS+"="+bulkEventOperationsForm.getFieldValue("ID_"+specimenId+"_CLASS")+ "&amp;" + Constants.CAN_HOLD_COLLECTION_PROTOCOL +"=" +bulkEventOperationsForm.getFieldValue("ID_"+specimenId+"_CPID");
String buttonOnClicked = "mapButtonClickedOnSpecimen('"+url+"','transferEvents','"+selContainerId+"')";	%>

<td class="black_ar"  colspan="2"> <logic:equal name="bulkEventOperationsForm" property="<%=specimenToVirLocField%>" value="true" >
<html:text styleId="<%=selContainerId%>" styleClass="black_ar" size="25" property="<%=specimenToSCLabelField%>" disabled="true" /></logic:equal>


<logic:notEqual name="bulkEventOperationsForm" property="<%=specimenToVirLocField%>" value="true" >
<html:text styleId="<%=selContainerId%>" styleClass="black_ar" size="25" property="<%=specimenToSCLabelField%>" disabled="false" />			</logic:notEqual>
<html:text styleId="<%=pos1Id%>" styleClass="black_ar" size="4" property="<%=specimenToSCPos1Field%>" disabled="false" />
<html:text styleId="<%=pos2Id%>" styleClass="black_ar" size="4" property="<%=specimenToSCPos2Field%>" disabled="false" />

<a href="#" onclick="<%=buttonOnClicked%>"><img src="images/Tree.gif" border="0" width="13" height="15" title='View storage locations'></a>
</td>

<!-- To Container Field ends -->
</tr> </logic:iterate> </table></td> </tr>
<tr>
<td class="buttonbg">
<html:submit styleClass="blue_ar_b"/>
</td> </tr>
</table>
</html:form>
