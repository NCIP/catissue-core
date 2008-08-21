<!-- orderBiospecimenArray.jsp which shows the specimen array List for ordering-->
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
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
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
					showErrorMessage("Specimen Array from multiple Site Exits : Can distribute specimen Array from only one site")
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
			document.OrderBiospecimenArray.submitButton.disabled=true;
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
			document.OrderBiospecimenArray.submitButton.disabled=false;
		}
	}
}

function putQuantity(element)
{
	var firstRequiredQuantity = document.getElementById("requestedQuantity_0").value;
	var isNumber=IsNumeric(firstRequiredQuantity);
	if(isNumber==false)
	{
		alert("Enter valid number");
	}
	else
	{
		var len=<%=specimenArray.size()%>;
		if(len=="1")
		{
			document.OrderBiospecimenArray.requestedQuantity_0.value=firstRequiredQuantity;
		}
		else
		{
			for(var i=0;i<document.OrderBiospecimenArray.selectedItems.length;i++)
			{
				var reqVar = 'requestedQuantity_' +i;				
				txtId = document.getElementById(reqVar);				
				txtId.value=firstRequiredQuantity;
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
function onCheck(element)
{
	
	var siteId = "value(OrderSpecimenBean:"+element.value+"_distributionSite)";
	var siteName =  document.getElementById(siteId).value;
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

			var checkBoxId = "checkBox_"+i;
			
			var distributionSiteId = "value(OrderSpecimenBean:"+i+"_distributionSite)";
			var distributionSiteValue =  document.getElementById(distributionSiteId).value;
			
			if(distributionSiteValue != siteName)
				document.getElementById(checkBoxId).disabled=true;	

			if(document.OrderBiospecimenArray.selectedItems[i].checked==true && document.getElementById(checkBoxId).disabled==false)
			{
				cnt++;
			}
		}
	}
	
	if(cnt>0)
	{
		document.OrderBiospecimenArray.orderButton.disabled=false;
		document.OrderBiospecimenArray.submitButton.disabled=false;
	}
	else
	{
		document.OrderBiospecimenArray.orderButton.disabled=true;
		document.OrderBiospecimenArray.submitButton.disabled=true;
		enableCheckBox();
	}
}

function enableCheckBox()
{
	for(var i=0;i<document.OrderBiospecimenArray.selectedItems.length;i++)
	{
			var checkBoxId = "checkBox_"+i;
			document.getElementById(checkBoxId).disabled=false;	
			if(document.OrderBiospecimenArray.selectedItems[i].checked==true)
			{
				document.OrderBiospecimenArray.orderButton.disabled=false;
			}
	}
}

</script>
<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
<%boolean readOnlyValue=false,readOnlyForAll=false;
String onClassChangeFunctionName = "onTypeChange(this)";%>


<%@ include file="/pages/content/common/ActionErrors.jsp" %>

<!-- Include external css and js files-->
<LINK REL=StyleSheet HREF="css/styleSheet.css" TYPE="text/css">
<script language="JavaScript" type="text/javascript" src="jss/script.js"></script>


<html:form action="AddToOrderListArray.do" name="OrderBiospecimenArray" type="edu.wustl.catissuecore.actionForm.OrderBiospecimenArrayForm">

<jsp:useBean id="form" class="edu.wustl.catissuecore.actionForm.OrderBiospecimenArrayForm" scope="request"/>	


<% String arrayType=form.getTypeOfArray();%>
	<html:hidden property="typeOfArray"/>
<div>
	<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" valign="top">
		<tr>
			<td class="bottomtd">&nbsp;
			</td>
		</tr>
		<tr>
			<td valign="bottom" >
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td valign="bottom" onclick="biospecimen()"><img src="images/uIEnhancementImages/cp_biospecimen1.gif" alt="Biospecimen" width="111" height="20" /></td>
                        <td valign="bottom" onclick="pathology()"><img src="images/uIEnhancementImages/cp_pathological1.gif" alt="Pathological Case" width="127" height="20" border="0" /></td>
                        <td valign="bottom"><img src="images/uIEnhancementImages/cp_biospecimenA.gif" alt="Biospecimen Array" width="132" height="20" border="0"></td>
                        <td width="85%" valign="bottom" class="cp_tabbg">&nbsp;</td>
                      </tr>
                    </table></td>
                  </tr>
		 <tr>
			<td class="cp_tabtable">
				<table summary="" cellpadding="3" cellspacing="0" border="0"  width="100%">
					 <tr>
                        <td colspan="3" class=" bottomtd"></td>
                     </tr>
					 <tr>
						<td width="100%" colspan="3" align="left" class="tr_bg_blue1">
							<span class="blue_ar_b">
								<bean:message key="requestdetails.tabname.label.arrayRequests" />
							</span>
						</td>
					</tr>	
					<tr>
						<td width="1%" align="center" class="black_ar">
							<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
						</td>
                        <td width="22%" align="left" class="black_ar">
							<bean:message key="orderingsystem.label.requestFor" />
						</td>
						<td width="77%" align="left" class="black_ar">
							<INPUT TYPE="radio" NAME="existingArray" value="existingArray" onclick="showTissueSlideFromBlock('showdropdown','existingArray')">
								<bean:message key="orderingsystem.label.existingArray" />
									&nbsp;
							<INPUT TYPE="radio" NAME="tissueSlidefromBlock" value="tissueSlidefromBlock" onclick="showTissueSlideFromBlock('showdropdown','tissueSlidefromBlock')">
								<bean:message key="orderingsystem.label.tissueSlidefromBlock" />
						</td>
					</tr>
					<tr>
						<td class="bottomtd">&nbsp;</td>
					</tr>
					<tr>
		               <td colspan="3" class="toptd dividerline">
						<div id="showdropdown"  style="display: none;">
							&nbsp;&nbsp;<html:button styleClass="black_ar" property="submitButton" value="Apply To All" onclick="putQuantity(this)" onmouseover="Tip(' Assign first required quantity to all')" disabled="true">
							</html:button>
						</div>
		<!------div for the derived specimen------------>
		
						</td>
					</tr>
					<tr>
                        <td colspan="3" class="black_ar ">
                          <table width="100%" border="0" cellspacing="0" cellpadding="4">
                            <tr>
                              <td class="tableheading">
								<INPUT TYPE="checkbox" NAME="checked" id="checked" onclick="checkAl(this)"/>
							  </td>
							  <td class="tableheading">
								<strong>
									<bean:message key="orderingsystem.label.arrayName" />
								</strong>
							   </td>
							   <td class="tableheading"><strong>
									<bean:message key="orderingsystem.label.distributionSite" />
									</strong>
								</td>
								<td class="tableheading"><strong>
									<bean:message key="orderingsystem.label.requestedQuantity" />
									</strong>
								</td>
								<td class="tableheading"><strong>
									<bean:message key="orderingsystem.label.description" />
										</strong>
								</td>
							</tr>
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
							String checkBoxId = "checkBox_"+i;
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
								<tr>
									<td valign="top">
										<html:multibox property="selectedItems"  styleId="<%=checkBoxId%>" value="<%=cnt%>" onclick="onCheck(this)"/>
									</td> 
									<td class="black_ar_t" > 
											<%=obj.getName()%>
											<html:hidden property="<%=specimenName%>" value="<%=obj.getName()%>"/>
											<html:hidden property="<%=specimenId%>" value="<%=obj.getId().toString()%>"/>
											<html:hidden property="<%=typeOfItem%>" value="specimenArray"/>
											<html:hidden property="<%=specimenClass%>" value="<%=obj.getSpecimenArrayType().getSpecimenClass()%>"/>
											<html:hidden property="<%=specimenType%>" value="<%=test%>"/>
									</td>
									<td class="black_ar_t"> 
											<%=distributionSiteName%>
											<html:hidden property="<%=distributionSite%>"  styleId="<%=distributionSite%>"   value="<%=distributionSiteName%>"/>
									</td>
									<td class="black_ar_t">
										<html:text property="<%=requestedQuantity%>" styleClass="black_ar" maxlength="8"  size="5" styleId="<%=txtReqQty%>" value="NA" disabled="true" style="text-align:right"/>
											<span id="<%=unitReqQtyId%>"></span>
											<html:hidden property="<%=unitRequestedQuantity%>" value="" styleId="unitRequestedQuantity"/>
									</td>
									<td class="black_ar_t">  
										<html:textarea styleClass="black_ar" rows="2" cols="25"  styleId="description" property="<%=description%>"/>		
									</td>
								</tr>
								<%i++;
							}
			%>

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
					</td>
				</tr>
				<tr>
                    <td colspan="3" class=" bottomtd"></td>
                </tr>
				<tr>
                    <td colspan="3" align="right" class="buttonbg">
						<html:button styleClass="blue_ar_b" property="orderButton" onclick="orderToList()" disabled="true">
								<bean:message key="orderingsystem.button.addToOrderList"/>
				 	    </html:button>
					</td>
                 </tr>
              </table>
			</td>
          </tr>
	</table>
</div>
</html:form>