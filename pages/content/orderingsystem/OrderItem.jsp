
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page  import="edu.wustl.catissuecore.domain.ReportedProblem,edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.Map,java.util.List,java.util.ListIterator"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.domain.Specimen"%>
<%@ page import="edu.wustl.catissuecore.bean.OrderSpecimenBean"%>
<%@ page import="edu.wustl.catissuecore.actionForm.OrderSpecimenForm"%>
<%@ include file="/pages/content/common/SpecimenCommonScripts.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 

<%
	OrderSpecimenForm form = (OrderSpecimenForm)request.getAttribute("OrderSpecimenForm");
  	Collection specimen;
	specimen=(List)request.getAttribute("specimen");
	
%>

<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>	
<script language="JavaScript">
var newWindow;
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

// To check specimen is existing or derived and to display or hide the block
function showDerived(object,element)
{
	if(element=="existingSpecimen")
	{
		document.OrderSpecimen.derivedSpecimen.checked=false;
		document.OrderSpecimen.typeOfSpecimen.value="false";
		var showdropdown=document.getElementById('showdropdown').style;
		showdropdown.display="none";
		span_tags = document.all.tags("span");
		var i;
		for(var counter = 1,i=0; counter <span_tags.length; counter=counter+2,i++)
		{
			var availableQuantity_object = span_tags[counter];
			var requestQuantity_object = span_tags[counter+1];
			requestQuantity_object.innerHTML=availableQuantity_object.innerHTML;
			var temp=requestQuantity_object.innerHTML;
			document.OrderSpecimen.unitRequestedQuantity[i].value=temp;
		}
	}
	if(element=="derivedSpecimen")
	{
		document.OrderSpecimen.existingSpecimen.checked=false;
		document.OrderSpecimen.typeOfSpecimen.value="true";
		var showdropdown=document.getElementById('showdropdown').style;
		showdropdown.display="block";
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
						showErrorMessage("Specimen from multiple site exist : Can distribute specimens from only one site")
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
			}
		}
}

//put quantity 
function putQuantity(element)
{
	var cnt=0;
	var isNumber=IsNumeric(document.getElementById("reqquantity").value);
	if(isNumber==false)
	{
		alert("Enter valid number");
	}
	else
	{
		var len=<%=specimen.size()%>;
		if(len=="1")
		{
			document.OrderSpecimen.requestedQuantity.value=document.getElementById("reqquantity").value;
			cnt++;
		}
		else
		{
			for(var i=0;i<document.OrderSpecimen.selectedItems.length;i++)
			{
				document.OrderSpecimen.requestedQuantity[i].value=document.getElementById("reqquantity").value;
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
		var action="AddToOrderListSpecimen.do?typeOf=specimen";
		document.OrderSpecimen.action = action ;		
		document.OrderSpecimen.submit();
	}
}
//for enabling and disabling the AddToOrderList button
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
	}
	else
	{
		document.OrderSpecimen.orderButton.disabled=true;
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
<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
<%
boolean readOnlyValue=false,readOnlyForAll=false;
String onClassChangeFunctionName = "typeChangeGeneralized(this)";
%>

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
<html:errors/>    


<!-- Include external css and js files-->
<LINK REL=StyleSheet HREF="css/styleSheet.css" TYPE="text/css">
<script language="JavaScript" type="text/javascript" src="jss/script.js"></script>


<html:form action="AddToOrderListSpecimen.do"  type="edu.wustl.catissuecore.actionForm.OrderSpecimenForm" name="OrderSpecimen">
<div height="90%">
  <table summary="" cellpadding="0" cellspacing="0" border="0" class="tabPage" width="100%" style="border-right:1px solid #5C5C5C;padding-right:0em;">
		<tr style="background-color:#AAAAAB">
			<td height="30" class="tabMenuItemSelected" nowrap>				
				<bean:message key="orderingSystem.tab.biospecimen"/>				
			</td>
				
			<td height="30" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="pathology()" nowrap>				 
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
									
									
	<html:hidden property="concentration"/>
	<html:hidden property="typeOfSpecimen"/>
	<table summary="" cellpadding="0" cellspacing="0" border="0"  width="100%">
		<tr>
			<td colspan='4'  class="formMessage">	
				<bean:message key="requiredfield.message"/>
			</td>
		</tr>	

		<tr>
			<td class="formTitle" height="30" colspan="3">
				<bean:message key="orderingsystem.label.specimenList" />
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
						
						
						<td width="30%" class="formLabelWithoutBorder" align="left" height="30">
							<INPUT TYPE="radio" NAME="existingSpecimen" value="existingSpecimen" onclick="showDerived('showdropdown',this.value)">
							<bean:message key="orderingsystem.label.existingSpecimen" />
						</td>
							
						<td width="30%" class="formLabelWithoutBorder" align="left" height="30">
							<INPUT TYPE="radio" NAME="derivedSpecimen" value="derivedSpecimen" onclick="showDerived('showdropdown',this.value)">
							<bean:message key="orderingsystem.label.derivedSpecimen" />
						</td>

						<td width="10%" class="formField" align="left" height="30">&nbsp;</td>
					</tr>
			<tr>

               <td width="100%" colspan="5" >
					<div id="showdropdown"  style=" position:relative ;display: none; width=100%;">
					
						<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
							
							<!-- get  specimen class list-->
							<tr>
							 	<td class="formRequiredNotice" width="1%">*</td>
								<td width="29%" class="formRequiredLabel" >
									<bean:message key="orderingsystem.label.classList" />
								</td>
								
								 <td width="70%" class="formField" >
									<%--<html:select property="specimenClassName" styleClass="formFieldSized15" styleId="classList" size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="<%=onClassChangeFunctionName%>">
										<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value" />
									</html:select>--%>
									  <autocomplete:AutoCompleteTag property="className"
										  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_CLASS_LIST)%>"
										  initialValue="<%=form.getClassName()%>"
										  onChange="onTypeChange(this);resetVirtualLocated()"
										  readOnly="false"
									    />
								</td>
		                    </tr>
						
		                
							<!-- get specimen type list-->
							<tr>
							 	<td class="formRequiredNotice" width="1%">*</td>
								<td width="29%" class="formRequiredLabel">
										<bean:message key="orderingsystem.label.typeList" />
								</td>
						    <!-- --------------------------------------- -->
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
						  <td  class="formField">
								   <autocomplete:AutoCompleteTag property="type"
										  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_TYPE_MAP)%>"
										  initialValue="<%=form.getType()%>"
										  onChange="<%=subTypeFunctionName%>"
										  readOnly="<%=readOnlyForAliquot%>"
										  dependsOn="<%=form.getClassName()%>"
					        />
						</td>
		                    </tr>
						</table>
						
					</div>
				</td>
				</tr>
				
					<tr>
                        <td class="formRequiredNotice"  width="1%" height="30">&nbsp;</td>
						<td width="29%" class="formLabel" height="30">
							<bean:message key="orderingsystem.label.requestedQuantity"/>
						</td>

                        <td width="70%" class="formField" colspan="3" height="30">&nbsp;&nbsp;
							<INPUT TYPE="text" NAME="reqquantity" id="reqquantity" size="5" MAXLENGTH="8"/>
							<span id="unitSpan"></span>
							<html:hidden property="unit"/>
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

							<th class="dataTableHeader" width="25%" align="left">
								<bean:message key="orderingsystem.label.specimenName" />
							</th>

							<th class="dataTableHeader" width="25%" align="left">
								<bean:message key="orderingsystem.label.distributionSite" />
							</th>
							
							<th class="dataTableHeader"  align="left" width="18%">
								<bean:message key="orderingsystem.label.availableQuantity" />
							</th>
							
							<th class="dataTableHeader"  align="left" width="18%">
								<bean:message key="orderingsystem.label.requestedQuantity" />
							</th>
							
							<th class ="dataTableHeader"  align="left" width="34%">
								<bean:message key="orderingsystem.label.description" />
							</th>
		                </tr>
  					
						<!-- getting values form the specimen object-->
					
						<%
							int i=0;
							Iterator it=specimen.iterator();
							while(it.hasNext())
							{				
					        	Specimen obj=(Specimen)it.next();
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
							<tr class="dataRowLight" width="100%">
	
        						<td class="dataCellText" width="5%">
									<html:multibox property="selectedItems" styleId="<%=checkBoxId%>" value="<%=cnt%>" onclick="onCheck(this)"/>
								</td> <!--for chk box -->

								<td class="dataCellText" width="25%">
								
								<html:link href="#" styleId="label" onclick="<%=specimenClickFunction%>">
									<%=obj.getLabel()%>
								</html:link>	
									<html:hidden property="<%=specimenName%>" value="<%=obj.getLabel()%>"/>
									<html:hidden property="<%=specimenId%>" value="<%=obj.getId().toString()%>"/>
									<html:hidden property="<%=typeOfItem%>" value="specimen"/>
									<html:hidden property="<%=collectionStatus%>" value="<%=obj.getCollectionStatus()%>"/>
									<html:hidden property="<%=isAvailablekey%>" value="<%=obj.getIsAvailable().toString()%>"/>
								</td>

								<td class="dataCellText" width="25%" >

								<%=distributionSiteName%>
								<html:hidden property="<%=distributionSite%>" styleId="<%=distributionSite%>" 
								value="<%=distributionSiteName%>"/>
									
								
								</td>

								<td class="dataCellText" width="18%">
									<%=obj.getAvailableQuantity()%>&nbsp;
									<script>
										var v= getUnit('<%=obj.getClassName() %>','<%=obj.getSpecimenType() %>');
										document.write(v);
									</script>

									<html:hidden property="<%=availableQuantity%>" value="<%=obj.getAvailableQuantity().toString()%>"/>
										
								</td>

									<td class="dataCellText" width="20%">
										<html:text styleClass="formFieldSized3" maxlength="8"  size="5"  styleId="requestedQuantity" value="<%=obj.getAvailableQuantity().toString()%>" property="<%=requestedQuantity%>"/>&nbsp;
									<script>
										var v= getUnit('<%=obj.getClassName() %>','<%=obj.getSpecimenType() %>');
										document.write(v);
									</script>
										<span id="requnitSpan"></span>		
										<html:hidden property="<%=unitRequestedQuantity%>" value="" styleId="unitRequestedQuantity"/>
										<html:hidden property="<%=isDerived%>" styleId="isDerived" value=""/>
										
										<html:hidden property="<%=specimenClass%>" styleId="specimenClass" value="<%=obj.getClassName()%>"/>
										<html:hidden property="<%=specimenType%>" styleId="specimenType" value="<%=obj.getSpecimenType()%>"/>

									</td>

									<td class="dataCellText" width="34%">
										<html:textarea rows="2" cols="25"  styleId="description" property="<%=description%>"/>		
									</td>
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
                <!--/div-->
               </td>
            </tr>
	</table>
									
	</td>
	 </tr>
	 <tr>
		<td align="right" colspan='4' class="FormField" style="background-color:white;border-left:1px solid #5C5C5C">
			<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
							<td  width="50%" class="formLabelBorderless" style="background-color:white">
								<span style="font-size:1em">
									<label for="AddToArrayName">
										 <bean:message key="orderingsystem.label.defineArrayName" />
									 </label>:
								</span>
								<html:select property="addToArray" name="OrderSpecimenForm" styleClass="formFieldSized10" size="1" 
						 			onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.ORDERTO_LIST_ARRAY%>" labelProperty="name" property="value"/>		
								</html:select>  							
							</td>
						
							<td  width="50%">
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

