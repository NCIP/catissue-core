<!-- orderPathologyCase.jsp Which shows the pathologycases List for ordering-->
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page  import="edu.wustl.catissuecore.domain.ReportedProblem,edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.Collection,java.util.Iterator,java.util.Map,java.util.List,java.util.ListIterator,java.util.HashMap,java.util.HashSet,java.util.ArrayList,java.util.Set"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.domain.Specimen"%>
<%@ page import="edu.wustl.catissuecore.bean.OrderSpecimenBean"%>
<%@ page import="edu.wustl.catissuecore.actionForm.OrderSpecimenForm"%>
<%@ page import="edu.wustl.catissuecore.actionForm.OrderPathologyCaseForm"%>
<%@ page import="edu.wustl.catissuecore.domain.SpecimenCollectionGroup"%>
<%@ page import="edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport"%>
<%@ page import="edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport"%>
<%@ page import="edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport"%>
<%@ include file="/pages/content/common/SpecimenCommonScripts.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 

<%
OrderPathologyCaseForm form = (OrderPathologyCaseForm)request.getAttribute("orderPathologyCaseForm");
  	Collection pathologyCase;
	pathologyCase=(List)request.getAttribute("pathologyCase");
	
%>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>	
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>	
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
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
	<% int size=pathologyCase.size();%>
	var unitRequestedQuantity;
	if(element=="derivative")
	{
		document.OrderPathologyCase.block.checked=false;
		document.OrderPathologyCase.typeOfCase.value="false";
		var showenabled=document.getElementById('showenabled').style;
		showenabled.display="block";
		var showdisabled=document.getElementById('showdisabled').style;
		showdisabled.display="none";
		
		for(var i = 0; i < <%=size%>; i++)
		{
			var requestQtyId = "requestedQuantity"+i;
			document.getElementById(requestQtyId).value="";
			document.getElementById(requestQtyId).readOnly=false;
		}
		
		span_tags = document.all.tags("span");
		for(var counter = 0; counter < span_tags.length; counter++)
		{
			var current_object = span_tags[counter+1];
			current_object.innerHTML="";
		}
		
	}
	if(element=="block")
	{
		document.OrderPathologyCase.derivative.checked=false;
		document.OrderPathologyCase.typeOfCase.value="true";
		var showenabled=document.getElementById('showenabled').style;
		showenabled.display="none";
		var showdisabled=document.getElementById('showdisabled').style;
		showdisabled.display="block";
		
		
		for(var i = 0; i < <%=size%>; i++)
		{
			var requestQtyId = "requestedQuantity"+i;
			document.getElementById(requestQtyId).value="1";
			document.getElementById(requestQtyId).readOnly=true;
		}
			
		var unitreq=document.getElementById("unitSpan");
		unitreq.innerHTML="Count";
		
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
	var isValidTodistribute = true;
	for(var i=0;i<document.forms[0].selectedItems.length;i++)
	{
			    var collectionProtocolId = "value(OrderSpecimenBean:"+i+"_collectionProtocol)";
				var collectionProtocol =  document.getElementById(collectionProtocolId).value;
				
				for(var j=0;j<document.forms[0].selectedItems.length;j++)
				{
					 var collectionProtocolIdInner = "value(OrderSpecimenBean:"+j+"_collectionProtocol)";
					 var collectionProtocolValueInner =  document.getElementById(collectionProtocolIdInner).value;
					 if(collectionProtocol != collectionProtocolValueInner)
					 {
						isValidTodistribute = false;
						showErrorMessage("SPR from multiple collection protocol exist,you can place an order from only one CP at a time.")
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
			document.OrderPathologyCase.submitButton.disabled=true;
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
			document.OrderPathologyCase.submitButton.disabled=false;
		}
	}
}

function putQuantity(element)
{
	var firstRequestedQuantity=document.getElementById("requestedQuantity0").value;
	var isNumber=IsNumeric(firstRequestedQuantity);
	if(isNumber==false)
	{
		alert("Enter valid number");
	}
	else
	{
		var len=<%=pathologyCase.size()%>;
		if(len=="1")
		{
			document.OrderPathologyCase.requestedQuantity.value=firstRequestedQuantity;
		}
		else
		{
			for(var i=0;i<document.OrderPathologyCase.selectedItems.length;i++)
			{
					//document.OrderPathologyCase.requestedQuantity[i].value=document.getElementById("reqquantity").value;
					//Bug fixed -5837
					document.getElementById("requestedQuantity"+i).value=firstRequestedQuantity;
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
function onCheck(element)
{

	var collectionProtocolId = "value(OrderSpecimenBean:"+element.value+"_collectionProtocol)";
	var collectionProtocol =  document.getElementById(collectionProtocolId).value;
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
			var checkBoxId = "checkBox_"+i;
			
			var colprotId = "value(OrderSpecimenBean:"+i+"_collectionProtocol)";
			var colprotValue =  document.getElementById(colprotId).value;

			if(colprotValue != collectionProtocol)
				document.getElementById(checkBoxId).disabled=true;	

			if(document.OrderPathologyCase.selectedItems[i].checked==true)
			{
				cnt++;
			}
		}
	}
	if(cnt>0)
	{
		document.OrderPathologyCase.orderButton.disabled=false;
		document.OrderPathologyCase.submitButton.disabled=false;
	}
	else
	{
		document.OrderPathologyCase.orderButton.disabled=true;
		document.OrderPathologyCase.submitButton.disabled=true;
		enableCheckBox();
	}
}

function enableCheckBox()
{
	for(var i=0;i<document.OrderPathologyCase.selectedItems.length;i++)
	{
			var checkBoxId = "checkBox_"+i;
			document.getElementById(checkBoxId).disabled=false;	
			if(document.OrderPathologyCase.selectedItems[i].checked==true)
			{
				document.OrderPathologyCase.orderButton.disabled=false;
			}
	}
}
</script>

<%
	boolean readOnlyValue=false,readOnlyForAll=false;
	String onClassChangeFunctionName = "onTypeChange(this)";
%>

<%@ include file="/pages/content/common/ActionErrors.jsp" %>


<!-- Include external css and js files-->
<LINK REL=StyleSheet HREF="css/styleSheet.css" TYPE="text/css">
<script language="JavaScript" type="text/javascript" src="jss/script.js"></script>


<html:form action="AddToOrderListSpecimen.do" type="edu.wustl.catissuecore.actionForm.OrderPathologyCaseForm"
			name="OrderPathologyCase">
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
                   <td valign="bottom" onclick="specimen()"><img src="images/uIEnhancementImages/cp_biospecimen1.gif" alt="Biospecimen" width="111" height="20" /></td>
                    <td valign="bottom"><img src="images/uIEnhancementImages/cp_pathological.gif" alt="Pathological Case" width="127" height="20" border="0" /></td>
                     <td valign="bottom" onClick="biospecimenArray()"><img src="images/uIEnhancementImages/cp_biospecimenA1.gif" alt="Biospecimen Array" width="132" height="20" border="0"></td>
                     <td width="85%" valign="bottom" class="cp_tabbg">&nbsp;</td>
                   </tr>
                </table>
			</td>
           </tr>
		   <tr>
			<td class="cp_tabtable">
				<table summary="" cellpadding="3" cellspacing="0" border="0"  width="100%">
				 <tr>
                    <td colspan="3" class=" bottomtd">
						<html:hidden property="typeOfCase"/>
						<html:hidden property="unit" value=""/>
						<html:hidden property="concentration"/>
					</td>
                 </tr>
				<tr>
					<td width="100%" colspan="3" align="left" class="tr_bg_blue1 blue_ar_b">
						<bean:message key="orderingsystem.label.caseList" />
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
						<INPUT TYPE="radio" NAME="derivative" value="derivative" onclick="showDerivative('showenabled','showdisabled',this.value)">
							<bean:message key="orderingsystem.label.derivative" />
						&nbsp;
						<INPUT TYPE="radio" NAME="block" value="block" onclick="showDerivative('showenabled','showdisabled',this.value)">
							<bean:message key="orderingsystem.label.block" />
					</td>
				</tr>
				<tr>
	               <td colspan="3" >
						<div id="showenabled">
							<table summary="" cellpadding="2" cellspacing="0" border="0" width="100%">
											<!-- get  specimen class list-->
								<tr>
								 	<td width="1%" align="center" class="black_ar">
										<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
									</td>
									<td width="22%" align="left" class="black_ar">
										<bean:message key="orderingsystem.label.classList" />
									</td>
								    <td width="27%" class="black_ar" >
										<autocomplete:AutoCompleteTag property="className"
										  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_CLASS_LIST)%>"
										  initialValue="<%=form.getClassName()%>"
										  onChange="onTypeChange(this);resetVirtualLocated()"
										  readOnly="false"
										  styleClass="black_ar"
										  size="25"
									    />
									</td>
						 <!-- get specimen type list-->
								 	<td align="center" width="1%" class="black_ar">
										<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
									</td>
									<td class="black_ar" align="left" wdth="22%">
										<bean:message key="orderingsystem.label.typeList" />
									</td>
									 <td  class="black_ar" width="27%">
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
										
										String subTypeFunctionName ="onSubTypeChangeUnit('className',this,'unitSpan')"; 
										
										String readOnlyForAliquot = "false";
						
							%>
								
											<autocomplete:AutoCompleteTag property="type"
											  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_TYPE_MAP)%>"
											  initialValue="<%=form.getType()%>"
											  onChange="<%=subTypeFunctionName%>"
											  readOnly="<%=readOnlyForAliquot%>"
											  dependsOn="<%=form.getClassName()%>"
											  styleClass="black_ar"
											  size="25"
									        />
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
		               <td colspan="3" >
						<div id="showdisabled"  style="display: none;">
							<table summary="" cellpadding="2" cellspacing="0" border="0" width="100%">
											<!-- get  specimen class list-->
								<tr>
								 	<td width="1%" align="center" class="black_ar">
										<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
									</td>
									 <td width="22%" align="left" class="black_ar">
										<bean:message key="orderingsystem.label.classList" />
									</td>
									<td width="27%" class="black_ar" >
										<autocomplete:AutoCompleteTag property="className"
										  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_CLASS_LIST)%>"
										  initialValue="Tissue"
										  onChange="onTypeChange(this);resetVirtualLocated()"
										  readOnly="true"
										  styleClass="black_ar"
										  size="25"
									    />
									</td>
									<td align="center" width="1%" class="black_ar">
										<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
									</td>
									<td class="black_ar" align="left" wdth="22%">
										<bean:message key="orderingsystem.label.typeList" />
									</td>
									<td  class="black_ar" width="27%">
										<autocomplete:AutoCompleteTag property="name"
										  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_TYPE_MAP)%>"
										  initialValue="Block"
										  readOnly="true"
										  styleClass="black_ar"
										  size="25"
								        />
									</td>
						        </tr>
							</table>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<table summary="" cellpadding="2" cellspacing="0" border="0" width="100%">
							<tr>
							 	<td width="1%" align="center" class="black_ar">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
								</td>
								<td width="22%" align="left" class="black_ar">
									<bean:message key="orderingsystem.label.tissueSite" />
								</td>
							    <td width="27%" class="black_ar" >
									<autocomplete:AutoCompleteTag property="tissueSite"
										  optionsList = "<%=request.getAttribute(Constants.TISSUE_SITE_LIST)%>"
										  initialValue=""
										  
										  styleClass="black_ar"
										  size="25"
							        />
									
								</td>
								<td align="center" width="1%" class="black_ar">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
								</td>
								<td class="black_ar" align="left" wdth="22%">
									<bean:message key="orderingsystem.label.pathologicalStatus" />
								</td>
								<td  class="black_ar" width="27%">
									<autocomplete:AutoCompleteTag property="pathologicalStatus"
										  optionsList = "<%=request.getAttribute(Constants.PATHOLOGICAL_STATUS_LIST)%>"
										  initialValue=""
										  styleClass="black_ar"
										  size="25"
							        />
								</td>
				            </tr>
						</table>
					</td>
				</tr>
				<tr><td class="bottomtd" colspan="3"></td></tr>
				<tr>
					<td class="black_ar dividerline" colspan="3">
							&nbsp;&nbsp;
						<html:button styleClass="black_ar" property="submitButton" value="Apply To All" onclick="putQuantity(this)" onmouseover="Tip(' Assign first required quantity to all')" disabled="true">
						</html:button>
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
                    <td colspan="3" class="black_ar ">
	                   <table width="100%" border="0" cellspacing="0" cellpadding="4">
                           <tr>
                             <td class="tableheading">
								<INPUT TYPE="checkbox" NAME="checked" id="checked" onclick="checkAl(this)"/>
							 </td>
						     <td class="tableheading"><strong>
								<bean:message key="orderingsystem.label.caseName" />
								</strong>
							 </td>

							 <td class="tableheading"><strong>
								<bean:message key="orderingsystem.label.colprot" />
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
						<tr><td class="bottomtd"></td></tr>
						<!-- getting values form the specimen object-->
						
						<%
							int i=0;
							Iterator it=pathologyCase.iterator();
							while(it.hasNext())
							{				
					        	SurgicalPathologyReport obj=(SurgicalPathologyReport)it.next();
								String cnt=new Integer(i).toString();
								String unitRequestedQuantity = "value(OrderSpecimenBean:"+i+"_unitRequestedQuantity)";
								String specimenName = "value(OrderSpecimenBean:"+i+"_specimenName)";
								String availableQuantity = "value(OrderSpecimenBean:"+i+"_availableQuantity)";
								String requestedQuantity = "value(OrderSpecimenBean:"+i+"_requestedQuantity)";
								String requestedQuantityId = "requestedQuantity"+i;
								String description = "value(OrderSpecimenBean:"+i+"_description)";
								String isDerived="value(OrderSpecimenBean:"+i+"_isDerived)";
								String specimenId="value(OrderSpecimenBean:"+i+"_specimenId)";
								String typeOfItem="value(OrderSpecimenBean:"+i+"_typeOfItem)";
								String specimenCollectionGroup="value(OrderSpecimenBean:"+i+"_specimenCollectionGroup)";
								String collectionProtocolId="value(OrderSpecimenBean:"+i+"_collectionProtocol)";
								String collectionProtocol = "";
								String checkBoxId = "checkBox_"+i;
								SpecimenCollectionGroup speccollgrp;
								if (obj instanceof IdentifiedSurgicalPathologyReport) {
									speccollgrp =(SpecimenCollectionGroup)obj.getSpecimenCollectionGroup();
									collectionProtocol = speccollgrp.getCollectionProtocolRegistration().getCollectionProtocol().getTitle();
								}
								else if(obj instanceof DeidentifiedSurgicalPathologyReport)
								{
									speccollgrp =(SpecimenCollectionGroup)obj.getSpecimenCollectionGroup();
									collectionProtocol = speccollgrp.getCollectionProtocolRegistration().getCollectionProtocol().getTitle();
								}
								else
								{
									collectionProtocol = obj.getSpecimenCollectionGroup().getCollectionProtocolRegistration().
										getCollectionProtocol().getTitle();
									continue;
								}
								
								
							%>
							<tr>
								<td valign="top">
									<html:multibox property="selectedItems" styleId="<%=checkBoxId%>"  value="<%=cnt%>" onclick="onCheck(this)"/>
								</td> <!--for chk box -->
								<td class="black_ar_t">
											<%=obj.getSpecimenCollectionGroup().getSurgicalPathologyNumber()%>

										<html:hidden property="<%=specimenName%>" value="<%=obj.getSpecimenCollectionGroup().getSurgicalPathologyNumber()%>"/>
										<html:hidden property="<%=specimenId%>" value="<%=obj.getId().toString()%>"/>
										<html:hidden property="<%=typeOfItem%>" value="pathologyCase"/>
										<html:hidden property="<%=specimenCollectionGroup%>" value="<%=speccollgrp.getId().toString()%>"/>
								</td>
								<td class="black_ar_t">
									<%=collectionProtocol%>
									<html:hidden property="<%=collectionProtocolId%>" styleId="<%=collectionProtocolId%>" 
									value="<%=collectionProtocol%>"/>
								</td>
								<td class="black_ar_t">
										<html:text styleClass="black_ar" maxlength="8"  size="5"  styleId="<%=requestedQuantityId%>" property="<%=requestedQuantity%>" style="text-align:right"/>
										&nbsp;
										<span id="unitSpan"></span>		
										<html:hidden property="<%=unitRequestedQuantity%>" value="" styleId="unitRequestedQuantity"/>
										<html:hidden property="<%=isDerived%>" styleId="isDerived" value=""/>
								</td>

								<td class="black_ar_t">
										<html:textarea styleClass="black_ar" rows="2" cols="25"  styleId="description" property="<%=description%>"/>		
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
				</td>
			</tr>
			 <tr>
                 <td align="center" class="black_ar dividerline">&nbsp;</td>
                  <td align="left" class="black_ar dividerline">
						<label for="AddToArrayName">
						 <bean:message key="orderingsystem.label.defineArrayName" />
						</label>
				  </td>
				  <td align="left" class="black_new dividerline">
						<html:select property="addToArray" styleClass="formFieldSized10" size="1" 
						 			onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
									<html:options collection="<%= Constants.ORDERTO_LIST_ARRAY  %>" labelProperty="name" property="value"/>		
								</html:select> 
								&nbsp; <a href="#" class="view" onclick="DefineArray()"><bean:message key="orderingsystem.button.defineArray"/></a>
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