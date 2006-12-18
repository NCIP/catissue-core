<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.StorageTypeForm"%>

<head>
	<script language="JavaScript" src="jss/script.js" type="text/javascript"></script>
	<script language="javascript">
//Mandar : 18-Apr-06 : bugid: 644 : - Dimension 2 capacity label 
	function capacityChanged(element)
	{
		var elementValue = element.value;
		if(elementValue.length>0)
		{
			try
			{
				var num = parseInt(elementValue);
				col1= document.getElementById("col1");
				col2= document.getElementById("col2");
				if(num>1)
				{
					col1.innerHTML="*";
					col2.className="formRequiredLabel";
				}
				else
				{
					col1.innerHTML="&nbsp;";
					col2.className="formLabel";
				}
			}
			catch(err)
			{
				//alert("Please enter a valid number.");
			}
		}
	}

//	function validate()
//	{

	//	if(validateAny(document.forms[0].holdsStorageTypeIds,"Please select Proper Storage Types")=="true")
		//{
			//if(validateAny(document.forms[0].holdsSpecimenClassTypes,"Please select Proper Specimen Classes")=="true")	
			//{
			//	document.forms[0].submit();
			//	return true;
			//}
		//}
	//}
	
	function validate(submittedFor,forwardTo)
	{		
		document.forms[0].submittedFor.value = submittedFor;
		document.forms[0].forwardTo.value    = forwardTo;
		
		if(validateAny(document.forms[0].holdsStorageTypeIds)==false)
		{
			alert("Selecting other options with 'Any' option is not allowed");
		}
		else
		{
			if(validateAny(document.forms[0].holdsSpecimenClassTypes)==false)
			{
				alert("Selecting other options with 'Any' option is not allowed");
			}
			else
			{
				document.forms[0].submit();
			}
		}
		
	}
	
	
	function onRadioButtonClick(element)
	{
		var specimenClass = document.getElementById("holdSpecimenClassTypeIds");
		var specimenArray = document.getElementById("holdsSpecimenArrTypeIds");
		
		if(element == "Specimen")
		{
			specimenClass.disabled = false;
			specimenArray.disabled = true;
			var len = specimenArray.length;
			for (var i = 0; i < len; i++) 
			{
				specimenArray.options[i].selected = false;
			}
		}
		if(element == "SpecimenArray")
		{
			specimenClass.disabled = true;
			specimenArray.disabled = false;
			var len = specimenClass.length;
			for (var i = 0; i < len; i++) 
			{
				specimenClass.options[i].selected = false;
			}
			
		}
		
	}
	</script>

</head>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>	
<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String reqPath = (String)request.getAttribute(Constants.REQ_PATH);  
		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
		String forwardTo=(String)request.getAttribute(Constants.FORWARD_TO);
        String formName;

        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.STORAGE_TYPE_EDIT_ACTION;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.STORAGE_TYPE_ADD_ACTION;
            readOnlyValue = false;
        }
		
	//Mandar : 18-Apr-06 : bugid: 644 : - Dimension 2 capacity label
	StorageTypeForm form = null;
	int dimTwoCapacity = 0;
	Object obj = request.getAttribute("storageTypeForm");
	if(obj != null && obj instanceof StorageTypeForm)
	{
		 form = (StorageTypeForm)obj;
	}
	if(form!=null)
	{
		dimTwoCapacity = form.getTwoDimensionCapacity();  
	}
	//Mandar : 18-Apr-06 : bugid: 644 : - Dimension 2 capacity label end	

%>	
			
<html:errors/>
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
    
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="720">

<html:form action="<%=formName%>" method="post">	
<!-- NEW Institute REGISTRATION BEGINS-->
	<tr>
	<td>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0">
		<tr>
			<td>
				<html:hidden property="operation" value="<%=operation%>"/>
				<html:hidden property="submittedFor" value="<%=submittedFor%>"/>
				<html:hidden property="forwardTo" value="<%=forwardTo%>"/>
			</td>
		</tr>
		
		<tr>
			<td><html:hidden property="id" />
				<html:hidden property="<%=Constants.REQ_PATH%>" value="<%=reqPath%>" />
			</td>
		</tr>

		<tr>
			 <td class="formMessage" colspan="3">* indicates a required field</td>
		</tr>

		<tr>
			 <td class="formTitle" height="20" colspan="3">
				<logic:equal name="operation" value="<%=Constants.ADD%>">
					<bean:message key="storageType.title"/>
				</logic:equal>
				<logic:equal name="operation" value="<%=Constants.EDIT%>">
					<bean:message key="storageType.editTitle"/>
				</logic:equal>
			 </td>
		</tr>

		<!-- Name of the storageType -->
		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="storageType.type"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="255"  size="30" styleId="type" property="type"/>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="defaultTemperature">
					<bean:message key="storageType.defaultTemperature"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="10"  size="30" styleId="defaultTemperature" property="defaultTemperature"/>
				°C
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="oneDimensionCapacity">
					<bean:message key="storageType.holds"/>
				</label>
			</td>
			<td class="formField" nowrap>
				<table>
				<tr>
					<td class="standardText" align="center">Storage Type</td>
					<td class="standardText" align="center"><html:radio property="specimenOrArrayType" value="Specimen" onclick="onRadioButtonClick('Specimen')"/> Specimen Class</td>
					<td class="standardText" align="center"><html:radio property="specimenOrArrayType" value="SpecimenArray" onclick="onRadioButtonClick('SpecimenArray')"/>Specimen Array Type</td>
					
				</tr>
				<tr>
				<td class="formField" nowrap align="center">
				<html:select property="holdsStorageTypeIds" styleClass="formFieldVerySmallSized" styleId="holdStorageTypeIds" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
				<html:options collection="<%=Constants.HOLDS_LIST1%>" labelProperty="name" property="value"/>
				</html:select>
				</td>
				<td class="formField" nowrap align="center">
				<logic:equal name="storageTypeForm" property="specimenOrArrayType" value="Specimen">
				<html:select property="holdsSpecimenClassTypes" styleClass="formFieldVerySmallSized" styleId="holdSpecimenClassTypeIds" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
				<html:options collection="<%=Constants.HOLDS_LIST2%>" labelProperty="name" property="value"/>
				</html:select>
				</logic:equal>
				
				<logic:equal name="storageTypeForm" property="specimenOrArrayType" value="SpecimenArray">
				<html:select property="holdsSpecimenClassTypes" styleClass="formFieldVerySmallSized" styleId="holdSpecimenClassTypeIds" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="true">
				<html:options collection="<%=Constants.HOLDS_LIST2%>" labelProperty="name" property="value"/>
				</html:select>
				</logic:equal>
				</td>
				<td class="formField" nowrap align="center">
				<logic:equal name="storageTypeForm" property="specimenOrArrayType" value="SpecimenArray">
				<html:select property="holdsSpecimenArrTypeIds" styleClass="formFieldVerySmallSized" styleId="holdsSpecimenArrTypeIds" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
				<html:options collection="<%=Constants.HOLDS_LIST3%>" labelProperty="name" property="value"/>
				</html:select>
				</logic:equal>
				<logic:equal name="storageTypeForm" property="specimenOrArrayType" value="Specimen">
				<html:select property="holdsSpecimenArrTypeIds" styleClass="formFieldVerySmallSized" styleId="holdsSpecimenArrTypeIds" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="true">
				<html:options collection="<%=Constants.HOLDS_LIST3%>" labelProperty="name" property="value"/>
				</html:select>
				</logic:equal>
				</td>
				
				</tr>
				<%--<tr>
				<td class="standardText" align="center" colspan="2">Specimen Array Type</td>
				</tr>
				<tr>
				<td class="formField" nowrap align="center" colspan="2">
				<html:select property="holdsSpecimenArrTypeIds" styleClass="formFieldVerySmallSized" styleId="holdsSpecimenArrTypeIds" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
				<html:options collection="<%=Constants.HOLDS_LIST3%>" labelProperty="name" property="value"/>
				</html:select>
				</td>
				</tr>
				--%>
				
				</table>
							
			</td>
		</tr>

		<tr>			
			<td class="formTitle" colspan="3">
				<label for="defaultCapacity">
					<bean:message key="storageType.defaultCapacity"/>
				</label>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="oneDimensionCapacity">
					<bean:message key="storageType.oneDimensionCapacity"/>
				</label>
			</td>
			<td class="formField">
					<html:text styleClass="formFieldSized10"  maxlength="10"  size="10" styleId="oneDimensionCapacity" property="oneDimensionCapacity"/>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="oneDimensionLabel">
					<bean:message key="storageType.oneDimensionLabel"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="255"  size="30" styleId="oneDimensionLabel" property="oneDimensionLabel"/>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="twoDimensionCapacity">
					<bean:message key="storageType.twoDimensionCapacity"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="10"  size="10" styleId="twoDimensionCapacity" property="twoDimensionCapacity" onkeyup="capacityChanged(this)" />
			</td>
		</tr>
<!--  Mandar : 18-Apr-06 : bugid: 644 : - Dimension 2 capacity label -->
<% 
	String tdClassName ="formLabel";
	String strStar = "&nbsp;";
	if(dimTwoCapacity > 1)
	{
		tdClassName="formRequiredLabel";
		strStar = "*";
	}
 %>
		<tr>
			<td class="formRequiredNotice" width="5" id="col1"><%=strStar%></td>
			<td class="<%=tdClassName%>" id="col2">
<!--  Mandar : 18-Apr-06 : bugid: 644 : - Dimension 2 capacity label end -->
				<label for="twoDimensionLabel">
					<bean:message key="storageType.twoDimensionLabel"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="255" size="30" styleId="twoDimensionLabel" property="twoDimensionLabel"/>
			</td>
		</tr>
		<tr>
		  <td align="right" colspan="3">
			<!-- action buttons begins -->
			<table cellpadding="4" cellspacing="0" border="0">
				<tr>
					<%
						String normalSubmit = "validate('" + submittedFor+ "','" + Constants.STORAGE_TYPE_FORWARD_TO_LIST[0][1]+"')";
						String forwardToSubmit = "validate('ForwardTo','" + Constants.STORAGE_TYPE_FORWARD_TO_LIST[1][1]+"')";																				
						System.out.println("-------normal submit" + normalSubmit);
						System.out.println("-------forwardTo Submit" + forwardToSubmit);
					%>				
					<td nowrap class="formFieldNoBorders">
								<html:button styleClass="actionButton"
										property="submitPage" 
										title="Submit Only"
										value="<%=Constants.STORAGE_TYPE_FORWARD_TO_LIST[0][0]%>"										
										onclick="<%=normalSubmit%>"> 
								</html:button>
					</td>
					<td nowrap class="formFieldNoBorders">									
									<html:button styleClass="actionButton"  
											property="storageContainerPage" 
											title="Submit and Add Container"
											value="<%=Constants.STORAGE_TYPE_FORWARD_TO_LIST[1][0]%>" 											
					  						onclick="<%=forwardToSubmit%>">
									</html:button>
					</td>
				</tr>
			</table>
			<!-- action buttons end -->
			</td>
		</tr>

		</table>
		
	  </td>
	 </tr>

	 <!-- NEW Institute REGISTRATION ends-->
	 
	 </html:form>
 </table>