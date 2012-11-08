<!-- orderItem.jsp which shows the list of specimens for ordering-->
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo"%>
<%@ page import="edu.wustl.catissuecore.domain.ReportedProblem,edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.Map,java.util.List,java.util.ListIterator"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.domain.Specimen"%>
<%@ page import="edu.wustl.catissuecore.bean.OrderSpecimenBean"%>
<%@ page import="edu.wustl.catissuecore.actionForm.OrderSpecimenForm"%>
<%@ include file="/pages/content/common/SpecimenCommonScripts.jsp"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp"%>

<%
	OrderSpecimenForm form = (OrderSpecimenForm)request.getAttribute("OrderSpecimenForm");
  	Collection specimen;
	specimen=(List)request.getAttribute("specimen");
	int numOfSpecimens = specimen.size();
%>

<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>
<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
<script language="JavaScript">
var newWindow;
var numOfSpecs= <%=numOfSpecimens%>;
// To change unit based on class and subtype
function onChangeUnit()
{
	var element=document.getElementById("unitSpan");
    span_tags = document.all.tags("span");
    var i;
	for(var counter = 1,i=0; counter < span_tags.length; counter=counter+2,i++)
	{
        var current_object = span_tags[counter+1];
		current_object.innerHTML=element.innerHTML;
		var unitofspecimen=element.innerHTML;
		document.forms[0].unitRequestedQuantity[i].value=unitofspecimen;
	}
}

// for Derivative 
function changeReqQuantUnit(obj,numOfSpec)
{
	var specimenClass = document.forms[0].className.value;
	var specimenSubType = obj.value;
	//var specimenSubType = document.forms[0].type.value;
	var reqQuantUnit = "";
	
	reqQuantUnit = getUnit(specimenClass,specimenSubType);

	document.getElementById("reqQuantId").innerHTML = reqQuantUnit;

	for(var j=0;j<numOfSpec;j++)
	{
		document.getElementById("dervQuantUnitSpanId_"+j).innerHTML = reqQuantUnit;
		document.forms[0].unitRequestedQuantity[j].value = reqQuantUnit;
	}
}


// To check specimen is existing or derived and to display or hide the block
function showDerived(object,element)
{
	if(element=="existingSpecimen")
	{
		document.OrderSpecimen.derivedSpecimen.checked=false;
		document.OrderSpecimen.typeOfSpecimen.value="false";
		var showdropdown=document.getElementById('showdropdown').style;
		showdropdown.display="none";
		
		for(var counter=0;counter<numOfSpecs;counter++)
		{
			var availableQuantity_object = document.getElementById("availQuantUnitSpanId_"+counter);
			var requestQuantity_object = document.getElementById("dervQuantUnitSpanId_"+counter);
			requestQuantity_object.innerHTML=availableQuantity_object.innerHTML;
			if(document.OrderSpecimen.unitRequestedQuantity[counter] != null)
			{
			document.OrderSpecimen.unitRequestedQuantity[counter].value=requestQuantity_object.innerHTML;
			}
		}
	}
	if(element=="derivedSpecimen")
	{
		document.OrderSpecimen.existingSpecimen.checked=false;
		document.OrderSpecimen.typeOfSpecimen.value="true";
		var showdropdown=document.getElementById('showdropdown').style;
		showdropdown.display="block";

		document.getElementById('applyToAllDivId').style.display="none";
	}
}

// To get unit based on class and type for available quantity
function getUnit(classname,type)
{
	if(classname == "Tissue")
	{
		if(type == "<%=Constants.FROZEN_TISSUE_SLIDE%>" || type =="<%=Constants.FIXED_TISSUE_BLOCK%>" || type == "<%=Constants.FROZEN_TISSUE_BLOCK%>" || type == "<%=Constants.NOT_SPECIFIED%>" || type == "<%=Constants.FIXED_TISSUE_SLIDE%>")
		{
			return("<%=Constants.UNIT_CN%>");
		}	
		else
		{
			if(type == "<%=Constants.MICRODISSECTED%>")
				return("<%=Constants.UNIT_CL%>");
			else
				return("<%=Constants.UNIT_GM%>");
		}	
	}
	else if(classname == "Fluid")
		return("<%=Constants.UNIT_ML%>");
	else if(classname == "Cell")
		return("<%=Constants.UNIT_CC%>");
	else if(classname == "Molecular")
		return("<%=Constants.UNIT_MG%>");
}

// to check all elements
function checkAl(element)
{
	var isValidTodistribute = true;
		for(var i=0;i<document.forms[0].selectedItems.length;i++)
		{
				 var distributionSiteId = "value(OrderSpecimenBean:"+i+"_distributionSite)";
				 var distributionSiteValue =  document.getElementById(distributionSiteId).value;
				
				for(var j=0;j<document.forms[0].selectedItems.length;j++)
				{
					 var distributionSiteIdInner = "value(OrderSpecimenBean:"+j+"_distributionSite)";
					 var distributionSiteValueInner =  document.getElementById(distributionSiteIdInner).value;
					 if(distributionSiteValue != distributionSiteValueInner)
					 {
						isValidTodistribute = false;
						showErrorMessage("Specimen from multiple site exist : Can order specimens only from only one site at a time")
						break;
					 }
				}
				if(isValidTodistribute == false)
				{
					break;
				}
		}

		if(isValidTodistribute == true)
		{
			enableCheckBox();

			var len=<%=specimen.size()%>;
			if(document.getElementById("checked").checked==false)
			{
				if(len=="1")
					document.OrderSpecimen.selectedItems.checked=false;
				else
				{
					for(var i=0;i<document.OrderSpecimen.selectedItems.length;i++)
						document.OrderSpecimen.selectedItems[i].checked=false;
				}
				document.OrderSpecimen.orderButton.disabled=true;
				
				document.OrderSpecimen.submitButton1.disabled=true;
			}
			else
			{
				if(len=="1")
					document.OrderSpecimen.selectedItems.checked=true;
				else
				{
					for(var i=0;i<len;i++)
					{
						document.OrderSpecimen.selectedItems.checked=true;
						document.OrderSpecimen.selectedItems[i].checked=true;
					}
				}
				
				document.OrderSpecimen.orderButton.disabled=false;
				document.OrderSpecimen.submitButton1.disabled=false;
				
			}
		}
}

//put quantity 
function putQuantity(reqQuantId)
{
	var cnt=0;
	var requestedQuantity=document.getElementById(reqQuantId).value;
	var isNumber=IsNumeric(requestedQuantity);
	if(isNumber==false)
	{
		alert("Enter valid number");
	}
	else
	{
		var len=<%=specimen.size()%>;

		if(len=="1")
		{
			document.OrderSpecimen.requestedQuantity.value=requestedQuantity;
			cnt++;
		}
		else
		{
			for(var i=0;i<document.OrderSpecimen.selectedItems.length;i++)
			{
				document.OrderSpecimen.requestedQuantity[i].value=requestedQuantity;
				cnt++;
			}
		}
	}
}
//check if any value is numeric or not
function IsNumeric(sText)
{
   var ValidChars = "0123456789.";
   var IsNumber=true;
   var Char;
   for (i = 0; i < sText.length && IsNumber == true; i++) 
   { 
	   Char = sText.charAt(i); 
	   if (ValidChars.indexOf(Char) == -1) 
			IsNumber = false;
   }
   return IsNumber;
}

// to display define array page
function DefineArray()
{
	var action="DefineArray.do?typeOf="+"<%=Constants.SPECIMEN_ORDER_FORM_TYPE%>";
	document.OrderSpecimen.action = action ;		
    document.OrderSpecimen.submit();    
}

// to display biospecimenarray page
function biospecimenArray()
{
	var action="OrderBiospecimenArray.do";
	document.OrderSpecimen.action = action ;		
    document.OrderSpecimen.submit();    
}

// to display pathology case page
function pathology()
{
	var action="OrderPathologyCase.do";
	document.OrderSpecimen.action = action ;		
    document.OrderSpecimen.submit();    
}


//to add items to order list
function orderToList()
{
	var cnt=0;
	var len=<%=specimen.size()%>;
	if(len=="1")
	{    
		if(document.OrderSpecimen.selectedItems.checked==true);
		cnt++;
	}
	else
	{
		for(var i=0;i<document.OrderSpecimen.selectedItems.length;i++)
		{
			if(document.OrderSpecimen.selectedItems[i].checked==true)
				cnt++;
		}
	}
	if(cnt==0)
	{
		alert("Please check atleast one item");
	}
	else
	{
		var action="AddToOrderListSpecimen.do?typeOf=specimen&requestFromPage=${requestScope.requestFromPage}";
		document.OrderSpecimen.action = action ;		
		document.OrderSpecimen.submit();
	}
}
//for enabling and disabling the AddToOrderList button for single checkbox select
function onCheck(element)
{
	
	var siteId = "value(OrderSpecimenBean:"+element.value+"_distributionSite)";
	var siteName =  document.getElementById(siteId).value;
	
	var cnt=0;
	var len=<%=specimen.size()%>;
	if(len=="1")
	{    
		if(document.OrderSpecimen.selectedItems.checked==true);
		cnt++;
	}
	else
	{
		for(var i=0;i<document.OrderSpecimen.selectedItems.length;i++)
		{
			var checkBoxId = "checkBox_"+i;
			
			var distributionSiteId = "value(OrderSpecimenBean:"+i+"_distributionSite)";
			var distributionSiteValue =  document.getElementById(distributionSiteId).value;
			
			if(distributionSiteValue != siteName)
				document.getElementById(checkBoxId).disabled=true;	

			if(document.OrderSpecimen.selectedItems[i].checked==true
				&& document.getElementById(checkBoxId).disabled==false)
			{
				cnt++;
			}
		}
	}
	if(cnt>0)
	{
		document.OrderSpecimen.orderButton.disabled=false;
		document.OrderSpecimen.submitButton1.disabled=false;
	}
	else
	{
		document.OrderSpecimen.orderButton.disabled=true;
		document.OrderSpecimen.submitButton1.disabled=true;
		enableCheckBox();
	}
}

function enableCheckBox()
{
	for(var i=0;i<document.OrderSpecimen.selectedItems.length;i++)
	{
			var checkBoxId = "checkBox_"+i;
			document.getElementById(checkBoxId).disabled=false;	
			if(document.OrderSpecimen.selectedItems[i].checked==true)
			{
				document.OrderSpecimen.orderButton.disabled=false;
			}
	}
}

function showSpecimenDetails(id)
{
	showNewPage('SearchObject.do?pageOf=pageOfNewSpecimen&operation=search&id=' + id );
}
function showNewPage(action)
{
   	if(newWindow!=null)
	{
	   newWindow.close();
	}
     newWindow = window.open(action,'','scrollbars=yes,status=yes,resizable=yes,width=860, height=600');
     newWindow.focus(); 
	
}
	

</script>
<script language="JavaScript" type="text/javascript"
	src="jss/Hashtable.js"></script>
<%
boolean readOnlyValue=false,readOnlyForAll=false;
String onClassChangeFunctionName = "typeChangeGeneralized(this)";
%>

<%@ include file="/pages/content/common/ActionErrors.jsp"%>


<!-- Include external css and js files-->
<LINK REL=StyleSheet HREF="css/styleSheet.css" TYPE="text/css">
<script language="JavaScript" type="text/javascript" src="jss/script.js"></script>


<html:form action="AddToOrderListSpecimen.do"
	type="edu.wustl.catissuecore.actionForm.OrderSpecimenForm"
	name="OrderSpecimen">
	<div>
	<table summary="" cellpadding="0" cellspacing="0" border="0"
		width="100%" valign="top">
		<tr>
			<td class="bottomtd"><html:hidden property="concentration" /> <html:hidden
				property="typeOfSpecimen" /></td>
		</tr>
		<tr>
			<td valign="bottom">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td valign="bottom"><img
						src="images/uIEnhancementImages/cp_biospecimen.gif"
						alt="Biospecimen" width="111" height="20" /></td>
					<td valign="bottom" onclick="pathology()"><img
						src="images/uIEnhancementImages/cp_pathological1.gif"
						alt="Pathological Case" width="127" height="20" border="0" /></td>
					<td valign="bottom" onClick="biospecimenArray()"><img
						src="images/uIEnhancementImages/cp_biospecimenA1.gif"
						alt="Biospecimen Array" width="132" height="20" border="0"></td>
					<td width="85%" valign="bottom" class="cp_tabbg">&nbsp;</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="cp_tabtable">
			<table summary="" cellpadding="3" cellspacing="0" border="0"
				width="100%">
				<tr>
					<td colspan="3" class=" bottomtd"></td>
				</tr>
				<tr>
					<td width="100%" colspan="3" align="left"
						class="tr_bg_blue1 blue_ar_b"><bean:message
						key="orderingsystem.label.specimenList" /></td>
				</tr>
				<tr>
					<td width="1%" align="center" class="black_ar"><img
						src="images/uIEnhancementImages/star.gif" alt="Mandatory"
						width="6" height="6" hspace="0" vspace="0" /></td>
					<td width="22%" align="left" class="black_ar"><bean:message
						key="requesteddetails.request.specimen" /></td>
					<td width="77%" align="left" class="black_ar"><INPUT
						TYPE="radio" NAME="existingSpecimen" value="existingSpecimen"
						onclick="showDerived('showdropdown',this.value)"> <bean:message
						key="orderingsystem.label.existingSpecimen" /> &nbsp; <INPUT
						TYPE="radio" NAME="derivedSpecimen" value="derivedSpecimen"
						onclick="showDerived('showdropdown',this.value)"> <bean:message
						key="orderingsystem.label.derivedSpecimen" /></td>
				</tr>
				<tr>
					<td colspan="3"><!------div for the derived specimen------------>
					<div id="showdropdown" style="display: none;">
					<table summary="" cellpadding="2" cellspacing="0" border="0"
						width="100%">
						<!-- get  specimen class list-->
						<tr>
							<td width="1%" align="center" class="black_ar"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" /></td>
							<td width="22%"  align="left" class="black_ar"><bean:message
								key="orderingsystem.label.classList" /></td>
							<td width="32%" colspan="2" class="black_ar"><autocomplete:AutoCompleteTag
								property="className"
								optionsList="<%=request.getAttribute(Constants.SPECIMEN_CLASS_LIST)%>"
								initialValue="<%=form.getClassName()%>"
								onChange="onTypeChange(this);resetVirtualLocated()"
								readOnly="false" styleClass="black_ar" size="20" /></td>
							<!-- get specimen type list-->

							<td align="center" width="1%" class="black_ar"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" /></td>
							<td class="black_ar" align="left" wdth="18%"><bean:message
								key="orderingsystem.label.typeList" /></td>

							<%
										String classValue = (String)form.getClassName();
										specimenTypeList = (List)specimenTypeMap.get(classValue);
										
										boolean subListEnabled = false;
								
										if(specimenTypeList == null)
										{
											specimenTypeList = new ArrayList();
											specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
										}
										
										
										pageContext.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
										
										String subTypeFunctionName ="changeReqQuantUnit(this,"+numOfSpecimens+")"; 
										
										String readOnlyForAliquot = "false";
						
						%>

							<td class="black_ar" width="26%"><div id="specimenTypeId"><autocomplete:AutoCompleteTag
								property="type"
								optionsList="<%=request.getAttribute(Constants.SPECIMEN_TYPE_MAP)%>"
								initialValue="<%=form.getType()%>"
								onChange="<%=subTypeFunctionName%>"
								readOnly="<%=readOnlyForAliquot%>"
								dependsOn="<%=form.getClassName()%>" styleClass="black_ar"
								size="20" /></div></td>
						</tr>
						<tr>
							<td width="1%" align="center" class="black_ar"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" /></td>
							<td width="22%" align="left" class="black_ar"><bean:message
								key="orderingsystem.label.requestedQuantity" /></td>
							<td width="17%" colspan="1" class="black_ar"><input
								id="requiredQuantId" class="black_ar" style="text-align: right"
								maxlength="8" size="5" type="text" value="" /> <span
								id="reqQuantId"></span></td>
							<td width="15%" colspan="1" align="right" class="black_ar">
							&nbsp;&nbsp;<html:button styleClass="black_ar"
								property="submitButton2" value="Apply To All"
								onclick="putQuantity('requiredQuantId')"
								onmouseover="Tip(' Assign first required quantity to all')">
							</html:button></td>
							<td width="45%" colspan="3" align="left" class="black_ar">
							&nbsp;
							</td>

						</tr>
					</table>
					</div>
					<!---Derived Specimen div closed--></td>
				</tr>
				<tr>
					<td class="bottomtd"></td>
				</tr>
				<html:hidden property="unit" />
				<%	
			if(request.getAttribute("value")!=null)
			{
				String strarray[]=(String[])request.getAttribute("array");
			}
			String strarray[]=(String[])request.getAttribute("array");
			%>
				<tr>
					<td colspan="3" class="bottomtd dividerline">
					<div id="applyToAllDivId" style="display: block;">
					&nbsp;&nbsp;<html:button styleClass="black_ar"
						property="submitButton1" value="Apply To All"
						onclick="putQuantity('requestedQuantity')"
						onmouseover="Tip(' Assign first required quantity to all')"
						disabled="true">
					</html:button></div>
					<table width="100%" border="0" cellspacing="0" cellpadding="4">
						<tr>
							<td class="bottomtd" colspan="6"></td>
						</tr>
						<tr>
							<td class="tableheading"><INPUT TYPE="checkbox"
								NAME="checked" id="checked" onclick="checkAl(this)" /></td>
							<td class="tableheading"><strong> <bean:message
								key="orderingsystem.SpecimenLabel" /> </strong></td>
							<td class="tableheading"><strong> <bean:message
								key="orderingsystem.label.distributionSite" /> </strong></td>
							<td class="tableheading"><strong> <bean:message
								key="orderingsystem.label.availableQuantity" /> </strong></td>
							<td class="tableheading"><strong> <bean:message
								key="orderingsystem.label.requestedQuantity" /> </strong></td>
							<td class="tableheading"><strong> <bean:message
								key="orderingsystem.label.description" /> </strong></td>
						</tr>

						<!-- getting values form the specimen object-->

						<%
							int i=0;
							Iterator it=specimen.iterator();
							while(it.hasNext())
							{	
								
					        	Specimen obj=(Specimen)it.next();
								String availQuantUnitSpanId = "availQuantUnitSpanId_"+i;
								String dervQuantUnitSpanId = "dervQuantUnitSpanId_"+i;
								String cnt=new Integer(i).toString();
								String unitRequestedQuantity = "value(OrderSpecimenBean:"+i+"_unitRequestedQuantity)";
								String specimenName = "value(OrderSpecimenBean:"+i+"_specimenName)";
								String availableQuantity = "value(OrderSpecimenBean:"+i+"_availableQuantity)";
								String requestedQuantity = "value(OrderSpecimenBean:"+i+"_requestedQuantity)";
								String description = "value(OrderSpecimenBean:"+i+"_description)";
								String isDerived="value(OrderSpecimenBean:"+i+"_isDerived)";
								String specimenId="value(OrderSpecimenBean:"+i+"_specimenId)";
								String typeOfItem="value(OrderSpecimenBean:"+i+"_typeOfItem)";
								String specimenClass="value(OrderSpecimenBean:"+i+"_specimenClass)";
								String specimenType="value(OrderSpecimenBean:"+i+"_specimenType)";
								String distributionSite="value(OrderSpecimenBean:"+i+"_distributionSite)";
								String collectionStatus="value(OrderSpecimenBean:"+i+"_collectionStatus)";
								String isAvailablekey ="value(OrderSpecimenBean:"+i+"_isAvailable)";
								String checkBoxId = "checkBox_"+i;
								String distributionSiteName="N/A";
								if(obj.getSpecimenPosition()!=null)
								{	
									distributionSiteName=(String)obj.getSpecimenPosition().getStorageContainer().getSite().getName();
								}	
								
								String specimenClickFunction = "showSpecimenDetails("+obj.getId().toString()+")";
							%>
						<tr>
							<td valign="top"><html:multibox property="selectedItems"
								styleId="<%=checkBoxId%>" value="<%=cnt%>"
								onclick="onCheck(this)" /></td>
							<!--for chk box -->
							<td class="black_ar_t"><html:link href="#" styleId="label"
								styleClass="view" onclick="<%=specimenClickFunction%>">

								<%=obj.getLabel()%>

							</html:link> <html:hidden property="<%=specimenName%>"
								value="<%=obj.getLabel()%>" /> <html:hidden
								property="<%=specimenId%>" value="<%=obj.getId().toString()%>" />
							<html:hidden property="<%=typeOfItem%>" value="specimen" /> <html:hidden
								property="<%=collectionStatus%>"
								value="<%=obj.getCollectionStatus()%>" /> <html:hidden
								property="<%=isAvailablekey%>"
								value="<%=obj.getIsAvailable().toString()%>" /></td>

							<td class="black_ar_t"><%=distributionSiteName%> <html:hidden
								property="<%=distributionSite%>" styleId="<%=distributionSite%>"
								value="<%=distributionSiteName%>" /></td>
							<td class="black_ar_t"><%=obj.getAvailableQuantity()%>&nbsp;
							<span id="<%=availQuantUnitSpanId%>"> </span> <script>
								var v= getUnit('<%=obj.getClassName() %>','<%=obj.getSpecimenType() %>');
										document.getElementById("<%=availQuantUnitSpanId%>").innerHTML=v;
										
							</script> <html:hidden property="<%=availableQuantity%>"
								value="<%=obj.getAvailableQuantity().toString()%>" /></td>
							<td class="black_ar_t"><html:text styleClass="black_ar"
								maxlength="8" size="5" styleId="requestedQuantity"
								value="<%=obj.getAvailableQuantity().toString()%>"
								property="<%=requestedQuantity%>" style="text-align:right" />&nbsp;
							<span id="<%=dervQuantUnitSpanId%>"> </span> <script>
										var v= getUnit('<%=obj.getClassName() %>','<%=obj.getSpecimenType() %>');
										document.getElementById("<%=dervQuantUnitSpanId%>").innerHTML=v;
									</script> <html:hidden property="<%=unitRequestedQuantity%>" value=""
								styleId="unitRequestedQuantity" /> <html:hidden
								property="<%=isDerived%>" styleId="isDerived" value="" /> <html:hidden
								property="<%=specimenClass%>" styleId="specimenClass"
								value="<%=obj.getClassName()%>" /> <html:hidden
								property="<%=specimenType%>" styleId="specimenType"
								value="<%=obj.getSpecimenType()%>" /></td>

							<td class="black_ar_t"><html:textarea rows="2" cols="25"
								styleId="description" styleClass="black_ar"
								property="<%=description%>" /></td>
						</tr>
						<%i++;
							}%>
						<script>
						if("<%=form.getTypeOfSpecimen()%>"=="existingSpecimen")
						{
							document.OrderSpecimen.existingSpecimen.checked=true;
							document.OrderSpecimen.derivedSpecimen.checked=false;
							document.OrderSpecimen.typeOfSpecimen.value="false";
							showDerived('showdropdown',"existingSpecimen");
						}
						else
						{
							document.OrderSpecimen.derivedSpecimen.checked=true;
							document.OrderSpecimen.existingSpecimen.checked=false;
							document.OrderSpecimen.typeOfSpecimen.value="true";
							showDerived('showdropdown',"derivedSpecimen");
						}
					</script>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="3" class="bottomtd"></td>
				</tr>
				<tr>
					<td align="center" class="black_ar dividerline">&nbsp;</td>
					<td align="left" class="black_ar dividerline"><label
						for="AddToArrayName"> <bean:message
						key="orderingsystem.label.defineArrayName" /> </label></td>
					<td align="left" class="black_new dividerline"><html:select
						property="addToArray" name="OrderSpecimenForm"
						styleClass="formFieldSized10" size="1"
						onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
						<html:options collection="<%=Constants.ORDERTO_LIST_ARRAY%>"
							labelProperty="name" property="value" />
					</html:select> &nbsp; <a href="#" class="view" onclick="DefineArray()"><bean:message
						key="orderingsystem.button.defineArray" /></a></td>
				</tr>
				<tr>
					<td colspan="3" class=" bottomtd"></td>
				</tr>
				<tr>
					<td colspan="3" align="right" class="buttonbg"><html:button
						styleClass="blue_ar_b" property="orderButton"
						onclick="orderToList()" disabled="true">
						<bean:message key="orderingsystem.button.addToOrderList" />
					</html:button></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>

	</div>
</html:form>

