
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
<%@ page import="edu.wustl.catissuecore.domain.SpecimenArrayType"%>
<%@ page import="edu.wustl.catissuecore.domain.SpecimenArray"%>
<%@ page import="edu.wustl.catissuecore.domain.StorageContainer"%>
<%@ page import="edu.wustl.catissuecore.bean.OrderSpecimenBean"%>

<%@ page import="edu.wustl.catissuecore.actionForm.OrderBiospecimenArrayForm"%>

<%
  Collection specimenArray=null;
  if(request.getAttribute("SpecimenNameList")!=null)
  	specimenArray=(List)request.getAttribute("SpecimenNameList");
%>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>	
<script language="JavaScript">


function showTissueSlideFromBlock(object,element)
{
	if(element=="existingArray")
	{
		document.OrderBiospecimenArray.tissueSlidefromBlock.checked=false;
		document.OrderBiospecimenArray.existingArray.checked=true;
		var showdropdown=document.getElementById('showdropdown').style;
		showdropdown.display="none";
		document.OrderBiospecimenArray.typeOfArray.value="false";
		var rowCount = document.getElementById('rowCount').value;			
		for(var i=0;i<rowCount;i++)
		{
			var reqVar = 'requestedQuantity_' +i;	
			//TextBox of the Requested Qty
			txtId = document.getElementById(reqVar);				
			txtId.disabled=true;
			txtId.value="NA";
			var unitReqQtyId = "unitRequestedQuantity_" + i;
			document.getElementById(unitReqQtyId).innerHTML="";
		}	

	}
	if(element=="tissueSlidefromBlock")
	{
		document.OrderBiospecimenArray.existingArray.checked=false;
		document.OrderBiospecimenArray.tissueSlidefromBlock.checked=true;
		var showdropdown=document.getElementById('showdropdown').style;
		showdropdown.display="block";
		document.OrderBiospecimenArray.typeOfArray.value="true";
		
		var rowCount = document.getElementById('rowCount').value;			
		for(var i=0;i<rowCount;i++)
		{
			var reqVar = 'requestedQuantity_' +i;	
			//TextBox of the Requested Qty			
			txtId = document.getElementById(reqVar);				
			txtId.disabled=false;
			txtId.value="";
			var unitReqQtyId = "unitRequestedQuantity_" + i;
			document.getElementById(unitReqQtyId).innerHTML="<%=Constants.UNIT_CN%>";
		}	
	}
}


function checkAl(element)
{
	var len=<%=specimenArray.size()%>;
	if(document.getElementById("checked").checked==false)
	{
		if(len=="1")
			document.OrderBiospecimenArray.selectedItems.checked=false;
		else
		{
			for(var i=0;i<document.forms[0].selectedItems.length;i++)
			{
				document.OrderBiospecimenArray.selectedItems[i].checked=false;
			}
		}
		document.OrderBiospecimenArray.orderButton.disabled=true;
	}
	else
	{
		if(len=="1")
			document.OrderBiospecimenArray.selectedItems.checked=true;
		else
		{
			for(var i=0;i<len;i++)
			{
				document.OrderBiospecimenArray.selectedItems.checked=true;
				document.OrderBiospecimenArray.selectedItems[i].checked=true;
			}
		}
		document.OrderBiospecimenArray.orderButton.disabled=false;
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
		var len=<%=specimenArray.size()%>;
		if(len=="1")
		{
			document.OrderBiospecimenArray.requestedQuantity.value=document.getElementById("reqquantity").value;
		}
		else
		{
			for(var i=0;i<document.OrderBiospecimenArray.selectedItems.length;i++)
			{
				var reqVar = 'requestedQuantity_' +i;				
				txtId = document.getElementById(reqVar);				
				txtId.value=document.getElementById("reqquantity").value;
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
	var action="DefineArray.do?typeOf="+"<%=Constants.ARRAY_ORDER_FORM_TYPE%>";
	document.OrderBiospecimenArray.action = action ;		
    document.OrderBiospecimenArray.submit();    
}

function biospecimen()
{
	var action="OrderExistingSpecimen.do";
	document.OrderBiospecimenArray.action = action ;		
    document.OrderBiospecimenArray.submit();    
}

function pathology()
{
	var action="OrderPathologyCase.do";
	document.OrderBiospecimenArray.action = action ;		
    document.OrderBiospecimenArray.submit();    
}


function orderToList()
{
	var cnt=0;
	var len=<%=specimenArray.size()%>;
	if(len=="1")
		if(document.OrderBiospecimenArray.selectedItems.checked==true)
			cnt++;

	for(var i=0;i<document.OrderBiospecimenArray.selectedItems.length;i++)
	{
		if(document.OrderBiospecimenArray.selectedItems[i].checked==true)
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
		var action="AddToOrderListArray.do?typeOf=specimenArray";
		document.OrderBiospecimenArray.action = action ;		
		document.OrderBiospecimenArray.submit();
	}
}
//for enabling and disabling the AddToOrderList button
function onCheck()
{
	var cnt=0;
	var len=<%=specimenArray.size()%>;
	if(len=="1")
	{    
		if(document.OrderBiospecimenArray.selectedItems.checked==true);
		cnt++;
	}
	else
	{
		for(var i=0;i<document.OrderBiospecimenArray.selectedItems.length;i++)
		{
			if(document.OrderBiospecimenArray.selectedItems[i].checked==true)
			{
				cnt++;
			}
		}
	}
	if(cnt>0)
		document.OrderBiospecimenArray.orderButton.disabled=false;
	else
		document.OrderBiospecimenArray.orderButton.disabled=true;
}

</script>
<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
<%boolean readOnlyValue=false,readOnlyForAll=false;
String onClassChangeFunctionName = "onTypeChange(this)";%>


<html:errors/>

<!-- Include external css and js files-->
<LINK REL=StyleSheet HREF="css/styleSheet.css" TYPE="text/css">
<script language="JavaScript" type="text/javascript" src="jss/script.js"></script>


<html:form action="AddToOrderListArray.do" name="OrderBiospecimenArray" type="edu.wustl.catissuecore.actionForm.OrderBiospecimenArrayForm">

<jsp:useBean id="form" class="edu.wustl.catissuecore.actionForm.OrderBiospecimenArrayForm" scope="request"/>	


<% String arrayType=form.getTypeOfArray();%>
	<html:hidden property="typeOfArray"/>
<div height="90%">			
	<table summary="Table to display BioSPecimen Array Page" cellpadding="0" cellspacing="0" border="1" class="tabPage" width="100%" style="border-right:1px solid #5C5C5C;padding-right:0em;">
		<tr style="background-color:#AAAAAB">
			
			<td height="30" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="biospecimen()" nowrap>		<bean:message key="orderingSystem.tab.biospecimen"/>				
			</td>
				
			<td height="30" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="pathology()" nowrap>			<bean:message key="orderingSystem.tab.pathologicalCase"/>			
			</td>

			<td height="30" class="tabMenuItemSelected" nowrap>				 
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
					<table summary="" cellpadding="0" cellspacing="0" border="0"  width="100%" style="height:100%">
						<tr>
							<td colspan='4'  class="formMessage">	
								<bean:message key="requiredfield.message"/>
							</td>
						</tr>	

						<tr>
							<td class="formTitle" height="30" colspan="3">
								<bean:message key="orderingsystem.label.requestdetailsarray" />
							</td>
						</tr>

						<tr>
							<td>
								<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" >
									<tr>
										<td class="formRequiredNotice" width="1%" height="30">&nbsp;*</td>
	
										<td width="29%" class="formRequiredLabel" align="left" height="30">
											&nbsp;&nbsp;<bean:message key="orderingsystem.label.requestFor" />
										</td>
										
										
										<td width="30%" class="formRequiredLabelWithoutBorder" align="left" height="30">
											<INPUT TYPE="radio" NAME="existingArray" value="existingArray" onclick="showTissueSlideFromBlock('showdropdown','existingArray')">
											<bean:message key="orderingsystem.label.existingArray" />
										</td>
											
										<td width="30%" class="formRequiredLabelWithoutBorder" align="left" height="30">
											<INPUT TYPE="radio" NAME="tissueSlidefromBlock" value="tissueSlidefromBlock" onclick="showTissueSlideFromBlock('showdropdown','tissueSlidefromBlock')">
											<bean:message key="orderingsystem.label.tissueSlidefromBlock" />
										</td>
	
										<td width="10%" class="formField" align="left" height="30">&nbsp;</td>
									</tr>
								</table>
							</td>
						</tr>
		<tr>
           <td width="100%" colspan="3" >
				<div id="showdropdown"  style=" position:relative ;display: none; width=100%;">
				<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
					<tr>
                        <td class="formRequiredNotice"  width="1%" height="30">&nbsp;</td>
						<td width="29%" class="formLabel" height="30">
							<bean:message key="orderingsystem.label.requestedQuantity"/>
						</td>

                        <td width="70%" class="formField" colspan="3" height="30">&nbsp;&nbsp;
							<INPUT TYPE="text" NAME="reqquantity" id="reqquantity" size="5" MAXLENGTH="8"/>
							
							<html:button styleClass="actionButton" property="submitButton" styleClass="actionButton" value="Apply To All" onclick="putQuantity(this)">
							</html:button>
						</td>
                    </tr>
				</table>
				</div>
				</td>
			</tr>
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
										<bean:message key="orderingsystem.label.arrayName" />
									</th>
									
									<th class="dataTableHeader" width="25%" align="left">
										<bean:message key="orderingsystem.label.distributionSite" />
									</th>

									<th class="dataTableHeader" width="30%" align="left">
										<bean:message key="orderingsystem.label.requestedQuantity" />
									</th>
									
									<th class ="dataTableHeader"  align="left" width="70%">
										<bean:message key="orderingsystem.label.description" />
									</th>
		                    </tr>
	 						<!-- getting values form the specimen object-->
							<%
						int i=0;
						Iterator it=specimenArray.iterator();
						String typeOfArray;
						Set specimenArrayType;

						while(it.hasNext())
						{
							SpecimenArray obj=(SpecimenArray)it.next();
							String cnt=new Integer(i).toString();
							String unitRequestedQuantity = "value(OrderSpecimenBean:"+i+"_unitRequestedQuantity)";
							String specimenName = "value(OrderSpecimenBean:"+i+"_specimenName)";
							String description = "value(OrderSpecimenBean:"+i+"_description)";
							String specimenId="value(OrderSpecimenBean:"+i+"_specimenId)";
							String typeOfItem="value(OrderSpecimenBean:"+i+"_typeOfItem)";
							String requestedQuantity = "value(OrderSpecimenBean:"+i+"_requestedQuantity)";
							String specimenClass="value(OrderSpecimenBean:"+i+"_specimenClass)";
							String specimenType="value(OrderSpecimenBean:"+i+"_specimenType)";
							String distributionSite="value(OrderSpecimenBean:"+i+"_distributionSite)";
							String distributionSiteName="N/A";
							if( obj.getLocatedAtPosition()!=null && obj.getLocatedAtPosition().getParentContainer()!=null)
							{	
								StorageContainer storageContainer = (StorageContainer)obj.getLocatedAtPosition().getParentContainer();
								distributionSiteName=(String)storageContainer.getSite().getName();
							}	
								

							String test=null;
							specimenArrayType=(Set)obj.getSpecimenArrayType().getSpecimenTypeCollection();	
							Iterator it1=specimenArrayType.iterator();
							while(it1.hasNext())
							{
								typeOfArray=(String)it1.next();
								if((typeOfArray.equals("Fixed Tissue Block"))||(typeOfArray.equals("Frozen Tissue Block")))
									test="block";
								else
									test="unblock";
							}
								String txtReqQty = "requestedQuantity_" + i;
								String unitReqQtyId = "unitRequestedQuantity_" + i;
							%>
									<tr class="dataRowLight" width="100%">
			
										<td class="dataCellText" width="5%">
											<html:multibox property="selectedItems" value="<%=cnt%>" onclick="onCheck()"/>
										</td> <!--for chk box -->
										<td class="dataCellText" width="30%"> 
											<%=obj.getName()%>
											<html:hidden property="<%=specimenName%>" value="<%=obj.getName()%>"/>
											<html:hidden property="<%=specimenId%>" value="<%=obj.getId().toString()%>"/>
											<html:hidden property="<%=typeOfItem%>" value="specimenArray"/>
											<html:hidden property="<%=specimenClass%>" value="<%=obj.getSpecimenArrayType().getSpecimenClass()%>"/>
											<html:hidden property="<%=specimenType%>" value="<%=test%>"/>
										</td>

										<td class="dataCellText" width="30%"> 
											<%=distributionSiteName%>
											<html:hidden property="<%=distributionSite%>" value="<%=distributionSiteName%>"/>
										</td>
	
										<td class="dataCellText" width="25%" >
											<html:text property="<%=requestedQuantity%>" styleClass="formFieldSized3" maxlength="8"  size="5" styleId="<%=txtReqQty%>" value="NA" disabled="true"/>
											<span id="<%=unitReqQtyId%>"></span>
											<html:hidden property="<%=unitRequestedQuantity%>" value="" styleId="unitRequestedQuantity"/>
										</td>
											
										<td class="dataCellText" width="20%">  
											<html:textarea styleClass="formFieldSized10" rows="2" cols="8"  styleId="description" property="<%=description%>"/>		
										</td>
									</tr>
								<%i++;
						}%>
							<input type="hidden" name="rowCount" id="rowCount" value="<%=i%>" />
						<script>
							if("<%=arrayType%>"=="existingArray")
							{
								document.OrderBiospecimenArray.existingArray.checked=true;
								document.OrderBiospecimenArray.tissueSlidefromBlock.checked=false;
								document.OrderBiospecimenArray.typeOfArray.value="false";
								showTissueSlideFromBlock('showdropdown',"existingArray");
							}
							else
							{
								document.OrderBiospecimenArray.tissueSlidefromBlock.checked=true;
								document.OrderBiospecimenArray.existingArray.checked=false;
								document.OrderBiospecimenArray.typeOfArray.value="true";
								showTissueSlideFromBlock('showdropdown',"tissueSlidefromBlock");
							}
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
							<td>
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

