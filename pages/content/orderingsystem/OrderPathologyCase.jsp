
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page  import="edu.wustl.catissuecore.domain.ReportedProblem,edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.Collection,java.util.Iterator,java.util.Map,java.util.List,java.util.ListIterator,java.util.HashMap,java.util.HashSet,java.util.ArrayList,java.util.Set"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.domain.Specimen"%>
<%@ page import="edu.wustl.catissuecore.bean.OrderSpecimenBean"%>
<%@ page import="edu.wustl.catissuecore.actionForm.OrderSpecimenForm"%>
<%@ page import="edu.wustl.catissuecore.actionForm.OrderPathologyCaseForm"%>
<%@ page import="edu.wustl.catissuecore.domain.SpecimenCollectionGroup"%>
<%@ page import="edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport"%>
<%@ include file="/pages/content/common/SpecimenCommonScripts.jsp" %>

<%
  	Collection pathologyCase;
	pathologyCase=(List)request.getAttribute("pathologyCase");
	
%>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>	
<script language="JavaScript">


function onChangeUnit()
{
	var element=document.getElementById("unitSpan");
    span_tags = document.all.tags("span");
	var len=<%=pathologyCase.size()%>;
	for(var counter = 0; counter < span_tags.length; counter++)
	{
        var current_object = span_tags[counter+1];
		current_object.innerHTML=element.innerHTML;
		if(len=="1")
			document.OrderPathologyCase.unitRequestedQuantity.value=element.innerHTML;
		else
			document.OrderPathologyCase.unitRequestedQuantity[counter].value=element.innerHTML;
	}
}



function showDerivative(objectenabled,objectdisabled,element)
{
	var unitRequestedQuantity;
	if(element=="derivative")
	{
		document.OrderPathologyCase.block.checked=false;
		document.OrderPathologyCase.typeOfCase.value="false";
		var showenabled=document.getElementById('showenabled').style;
		showenabled.display="block";
		var showdisabled=document.getElementById('showdisabled').style;
		showdisabled.display="none";
		
	}
	if(element=="block")
	{
		document.OrderPathologyCase.derivative.checked=false;
		document.OrderPathologyCase.typeOfCase.value="true";
		var showenabled=document.getElementById('showenabled').style;
		showenabled.display="none";
		var showdisabled=document.getElementById('showdisabled').style;
		showdisabled.display="block";
		
		var unitreq=document.getElementById("unitSpan");
		unitreq.innerHTML="count";
		
		span_tags = document.all.tags("span");
		for(var counter = 0; counter < span_tags.length; counter++)
		{
			var current_object = span_tags[counter+1];
			current_object.innerHTML=unitreq.innerHTML;
		}
	}
}


function checkAl(element)
{
	var len=<%=pathologyCase.size()%>;
	if(document.getElementById("checked").checked==false)
	{
		if(len=="1")
			document.OrderPathologyCase.selectedItems.checked=false;
		else
		{
			for(var i=0;i<document.OrderPathologyCase.selectedItems.length;i++)
			{
				document.OrderPathologyCase.selectedItems[i].checked=false;
			}
		}
		document.OrderPathologyCase.orderButton.disabled=true;
	}
	else
	{
		if(len=="1")
			document.OrderPathologyCase.selectedItems.checked=true;
		else
		{
			for(var i=0;i<len;i++)
			{
				document.OrderPathologyCase.selectedItems.checked=true;
				document.OrderPathologyCase.selectedItems[i].checked=true;
			}
		}
		document.OrderPathologyCase.orderButton.disabled=false;
	}
}

function putQuantity(element)
{

	var isNumber=IsNumeric(document.getElementById("reqquantity").value);
	if(isNumber==false)
	{
		alert("Enter valid number");
	}
	else
	{
		var len=<%=pathologyCase.size()%>;
		if(len=="1")
		{
			document.OrderPathologyCase.requestedQuantity.value=document.getElementById("reqquantity").value;
		}
		else
		{
			for(var i=0;i<document.OrderPathologyCase.selectedItems.length;i++)
			{
					document.OrderPathologyCase.requestedQuantity[i].value=document.getElementById("reqquantity").value;
			}
		}
	}
}

function IsNumeric(sText)
{
   var ValidChars = "0123456789.";
   var IsNumber=true;
   var Char;
   for (i = 0; i < sText.length && IsNumber == true; i++) 
   { 
      Char = sText.charAt(i); 
      if (ValidChars.indexOf(Char) == -1) 
      {
         IsNumber = false;
      }
   }
   return IsNumber;
}

function DefineArray()
{
	var action="DefineArray.do?typeOf="+"<%=Constants.PATHOLOGYCASE_ORDER_FORM_TYPE%>";
	document.OrderPathologyCase.action = action ;		
    document.OrderPathologyCase.submit();    
}

function biospecimenArray()
{
	var action="OrderBiospecimenArray.do";
	document.OrderPathologyCase.action = action ;		
    document.OrderPathologyCase.submit();    
}

function specimen()
{
	var action="OrderExistingSpecimen.do";
	document.OrderPathologyCase.action = action ;		
    document.OrderPathologyCase.submit();    
}


function orderToList()
{
	var cnt=0;
	var len=<%=pathologyCase.size()%>;
	if(len=="1")
	{
		if(document.OrderPathologyCase.selectedItems.checked==true);
		cnt++;
	}

	for(var i=0;i<document.OrderPathologyCase.selectedItems.length;i++)
	{
		if(document.OrderPathologyCase.selectedItems[i].checked==true)
		{
			cnt++;
		}
	}
	if(cnt==0)
	{
		alert("Please check atleast one item");
	}
	else
	{
		var action="AddToOrderListPathologyCase.do?typeOf=pathologyCase";
		document.OrderPathologyCase.action = action ;		
		document.OrderPathologyCase.submit();
	}
}

//for enabling and disabling the AddToOrderList button
function onCheck()
{
	var cnt=0;
	var len=<%=pathologyCase.size()%>;
	if(len=="1")
	{    
		if(document.OrderPathologyCase.selectedItems.checked==true);
		cnt++;
	}
	else
	{
		for(var i=0;i<document.OrderPathologyCase.selectedItems.length;i++)
		{
			if(document.OrderPathologyCase.selectedItems[i].checked==true)
			{
				cnt++;
			}
		}
	}
	if(cnt>0)
		document.OrderPathologyCase.orderButton.disabled=false;
	else
		document.OrderPathologyCase.orderButton.disabled=true;
}
</script>

<%
	boolean readOnlyValue=false,readOnlyForAll=false;
	String onClassChangeFunctionName = "onTypeChange(this)";
%>

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
<html:errors/>    


<!-- Include external css and js files-->
<LINK REL=StyleSheet HREF="css/styleSheet.css" TYPE="text/css">
<script language="JavaScript" type="text/javascript" src="jss/script.js"></script>


<html:form action="AddToOrderListSpecimen.do" type="edu.wustl.catissuecore.actionForm.OrderPathologyCaseForm"
			name="OrderPathologyCase">
  <div height="90%">			
	<table summary="Table to display Pathology Case Reports" cellpadding="0" cellspacing="0" border="0" class="tabPage" width="100%" style="border-right:1px solid #5C5C5C;padding-right:0em;">
		<tr style="background-color:#AAAAAB">
			<td height="30" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="specimen()" nowrap>		
				<bean:message key="orderingSystem.tab.biospecimen"/>				
			</td>
				
			<td height="30" class="tabMenuItemSelected" nowrap>				 
				<bean:message key="orderingSystem.tab.pathologicalCase"/>			
			</td>

			<td height="30" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="biospecimenArray()" nowrap>				 
				<bean:message key="orderingSystem.tab.biospecimenArray"/>				
			</td>
				
			<td width="10%" class="tabMenuSeparator">
				<html:button styleClass="actionButton" property="defineArrayButton" onclick="DefineArray()">
				<bean:message key="orderingsystem.button.defineArray"/>
				</html:button> 
			</td>
		 </tr>
			 
			<tr>
			  	<td style="border-left:1px solid #5C5C5C" colspan="6">

	<html:hidden property="typeOfCase"/>
	<html:hidden property="unit" value=""/>
	<html:hidden property="concentration"/>
	<table summary="" cellpadding="0" cellspacing="0" border="0"  width="100%">
	<tr>
		<td colspan='4'  class="formMessage">	
			<bean:message key="requiredfield.message"/>
		</td>
	</tr>	

	<tr>
        <td class="formTitle" height="30" colspan="3">
			<bean:message key="orderingsystem.label.caseList" />
		</td>
    </tr>

	<tr>
         <td>
				<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" style="height:100%">
                    <tr>
					 	<td class="formRequiredNotice" width="1%" height="30">&nbsp;*</td>

						<td width="29%" class="formRequiredLabel" align="left" height="30">
							&nbsp;&nbsp;<bean:message key="orderingsystem.label.requestFor" />
						</td>

						<td width="30%" class="formRequiredLabelWithoutBorder" align="left" height="30">
							<INPUT TYPE="radio" NAME="derivative" value="derivative" onclick="showDerivative('showenabled','showdisabled',this.value)">
							<bean:message key="orderingsystem.label.derivative" />
						</td>
							
						<td width="30%" class="formRequiredLabelWithoutBorder" align="left" height="30">
							<INPUT TYPE="radio" NAME="block" value="block" onclick="showDerivative('showenabled','showdisabled',this.value)">
							<bean:message key="orderingsystem.label.block" />
						</td>

						<td width="10%" class="formField" align="left" height="30">&nbsp;</td>
					</tr>
			<tr>
               <td width="100%" colspan="5" >
				<div id="showenabled"  style=" position:relative ;display: none; width=100%;">
				<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
					<!-- get  specimen class list-->
					<tr>
					 	<td class="formRequiredNotice" width="1%">*</td>
						<td width="29%" class="formRequiredLabel" >
							<bean:message key="orderingsystem.label.classList" />
						</td>
						
						 <td width="70%" class="formField" >
							<html:select property="specimenClass" styleClass="formFieldSized15" styleId="classList" size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="<%=onClassChangeFunctionName%>">
								<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value" />
							</html:select>
						</td>
                    </tr>
				
                
					<!-- get specimen type list-->
					<tr>
					 	<td class="formRequiredNotice" width="1%">*</td>
						<td width="29%" class="formRequiredLabel">
								<bean:message key="orderingsystem.label.typeList" />
						</td>
							<%
								boolean subListEnabled = false;
								specimenTypeList = new ArrayList();
								specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
								pageContext.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
								String subTypeFunctionName ="onSubTypeChangeUnit('specimenClass',this,'unitSpan'),onChangeUnit()";
							%>

                        <td  width="70%" class="formField">
							<html:select property="type" styleClass="formFieldSized15" styleId="type"
							 size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="<%=subListEnabled%>" onchange="<%=subTypeFunctionName%>">
								<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
							</html:select>
						</td>
                    </tr>
				</table>
				</div>
				</td>
			</tr>
				
			<tr>

               <td width="100%" colspan="5" >
				<div id="showdisabled"  style=" position:relative ;display: none; width=100%;">
				
				<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
					
					<!-- get  specimen class list-->
					<tr>
					 	<td class="formRequiredNotice" width="1%">*</td>
						<td width="29%" class="formRequiredLabel" >
							<bean:message key="orderingsystem.label.classList" />
						</td>
						
						 <td width="70%" class="formField" >
							<SELECT NAME="Tissue" disabled="true" value="Tissue" size="1" selected="selected">
							<OPTION selected value="Tissue">Tissue</OPTION>
							</SELECT>
						</td>
                    </tr>
				
                
					<!-- get specimen type list-->
					<tr>
					 	<td class="formRequiredNotice" width="1%">*</td>
						<td width="29%" class="formRequiredLabel">
								<bean:message key="orderingsystem.label.typeList" />
						</td>
                        <td  width="70%" class="formField">
							<SELECT NAME="Block" disabled="true" value="Block" selected="selected" size="1">
							<OPTION selected value="Block">Block</OPTION>
							</SELECT>
						</td>
                    </tr>

   					
				</table>
				</div>
				</td>
				
				</tr>
                    <tr>
					 	<td class="formRequiredNotice" width="1%" height="30" >&nbsp;*</td>
						<td width="29%" class="formRequiredLabel" height="30" >
							&nbsp;&nbsp;<bean:message key="orderingsystem.label.tissueSite" />
						</td>
						
						 <td width="70%" class="formField" height="30" colspan="3">
							&nbsp;<html:select property="tissueSite" styleClass="formFieldSized15" styleId="classList" size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" >
								<html:options collection="<%=Constants.TISSUE_SITE_LIST%>" labelProperty="name" property="value" />
							</html:select>
						</td>
                    </tr>
                    
                    
                    <tr>
					 	<td class="formRequiredNotice" width="1%" height="30" >&nbsp;*</td>
						<td width="29%" class="formRequiredLabel" height="30">
							&nbsp;&nbsp;<bean:message key="orderingsystem.label.pathologicalStatus" />
						</td>
						
						 <td width="70%" class="formField" height="30" colspan="3">&nbsp;
							<html:select property="pathologicalStatus" styleClass="formFieldSized15" styleId="classList" size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.PATHOLOGICAL_STATUS_LIST%>" labelProperty="name" property="value" />
							</html:select>
						</td>
                    </tr>
                    
					<tr>
                        <td class="formRequiredNotice"  width="1%" height="30">&nbsp;</td>
						<td width="29%" class="formLabel" height="30">
							&nbsp;&nbsp;<bean:message key="orderingsystem.label.requestedQuantity"/>
						</td>

                        <td width="70%" class="formField" colspan="3" height="30" colspan="2">&nbsp;&nbsp;
							<INPUT TYPE="text" NAME="reqquantity" id="reqquantity" size="5" MAXLENGTH="8"/>
							&nbsp;<span id="unitSpan"></span>
							
							&nbsp;&nbsp;&nbsp;&nbsp;
							
							<html:button styleClass="actionButton" property="submitButton" styleClass="actionButton" value="Apply To All" onclick="putQuantity(this)">
							</html:button>
						</td>

                    </tr>
                </table>
                </td>
            </tr>
            
			<%	
			if(request.getAttribute("value")!=null)
			{
				String strarray[]=(String[])request.getAttribute("array");
			}
			String strarray[]=(String[])request.getAttribute("array");
			%>
			
			<tr>
                <td>
                	<!--div class="tableScroll"-->
					<table summary="Enter summary of data here" cellpadding="3"
						cellspacing="0" border="0" class="dataTable" width="100%">
    				<tr>
							<th class="dataTableHeader" width="5%">
							<INPUT TYPE="checkbox" NAME="checked" id="checked" onclick="checkAl(this)"/>
							</th>

							<th class="dataTableHeader" width="30%" align="left">
								<bean:message key="orderingsystem.label.caseName" />
							</th>
							
							
							<th class="dataTableHeader"  align="left" width="25%">
								<bean:message key="orderingsystem.label.requestedQuantity" />
							</th>
							
							<th class ="dataTableHeader"  align="left" width="15%">
								<bean:message key="orderingsystem.label.description" />
							</th>
                    </tr>
  					
						<!-- getting values form the specimen object-->
						
						<%
							int i=0;
							Iterator it=pathologyCase.iterator();
							while(it.hasNext())
							{				
					        	IdentifiedSurgicalPathologyReport obj=(IdentifiedSurgicalPathologyReport)it.next();
								String cnt=new Integer(i).toString();
								String unitRequestedQuantity = "value(OrderSpecimenBean:"+i+"_unitRequestedQuantity)";
								String specimenName = "value(OrderSpecimenBean:"+i+"_specimenName)";
								String availableQuantity = "value(OrderSpecimenBean:"+i+"_availableQuantity)";
								String requestedQuantity = "value(OrderSpecimenBean:"+i+"_requestedQuantity)";
								String description = "value(OrderSpecimenBean:"+i+"_description)";
								String isDerived="value(OrderSpecimenBean:"+i+"_isDerived)";
								String specimenId="value(OrderSpecimenBean:"+i+"_specimenId)";
								String typeOfItem="value(OrderSpecimenBean:"+i+"_typeOfItem)";
								String specimenCollectionGroup="value(OrderSpecimenBean:"+i+"_specimenCollectionGroup)";
								SpecimenCollectionGroup speccollgrp=(SpecimenCollectionGroup)obj.getSpecimenCollectionGroup();
								
							%>
							<tr class="dataRowLight" width="100%">
	
        						<td class="dataCellText" width="5%">
									<html:multibox property="selectedItems" value="<%=cnt%>" onclick="onCheck()"/>
								</td> <!--for chk box -->

									<td class="dataCellText" width="30%">
											<%=obj.getSpecimenCollectionGroup().getSurgicalPathologyNumber()%>

										<html:hidden property="<%=specimenName%>" value="<%=obj.getSpecimenCollectionGroup().getSurgicalPathologyNumber()%>"/>
										<html:hidden property="<%=specimenId%>" value="<%=obj.getId().toString()%>"/>
										<html:hidden property="<%=typeOfItem%>" value="pathologyCase"/>
										<html:hidden property="<%=specimenCollectionGroup%>" value="<%=speccollgrp.getId().toString()%>"/>
									</td>

									<td class="dataCellText" width="25%">
										<html:text styleClass="formFieldSized3" maxlength="8"  size="5"  styleId="requestedQuantity" property="<%=requestedQuantity%>"/>
										&nbsp;
										<span id="requnitSpan"></span>		
										<html:hidden property="<%=unitRequestedQuantity%>" value="" styleId="unitRequestedQuantity"/>
										<html:hidden property="<%=isDerived%>" styleId="isDerived" value=""/>
									</td>

									<td class="dataCellText" width="20%">
										<html:textarea styleClass="formFieldSized10" rows="2" cols="8"  styleId="description" property="<%=description%>"/>		
									</td>
							</tr>
							<%i++;
							}%>
					<script>
						document.OrderPathologyCase.derivative.checked=true;
						document.OrderPathologyCase.block.checked=false;
						document.OrderPathologyCase.typeOfCase.value="false";
						showDerivative('showenabled','showdisabled',"derivative");
					</script>


                </table>
                <!--/div-->
                </td>
            </tr>
</table>
									
				</td>
			 </tr>
			 <tr>
				<td align="right" colspan='4' class="tabField">

				<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
							<td width="50%" class="formLabelBorderless" style="background-color:white">
									<label for="AddToArrayName">
										 <bean:message key="orderingsystem.label.defineArrayName" />
									 </label>:
								<html:select property="addToArray" styleClass="formFieldSized10" size="1" 
						 			onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
									<html:options collection="<%= Constants.ORDERTO_LIST_ARRAY  %>" labelProperty="name" property="value"/>		
								</html:select>  							
							</td>
						
							<td width="50%">
							<html:button styleClass="actionButton" property="orderButton" onclick="orderToList()" disabled="true">
									<bean:message key="orderingsystem.button.addToOrderList"/>
							  </html:button>

							</td>
		
						</tr>
					</table>
						
						<!-- action buttons end -->
				</td>		 
			</tr>		  			  
</table>
</div>
</html:form>

