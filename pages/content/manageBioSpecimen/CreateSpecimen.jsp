<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page import="java.util.List,java.util.ListIterator"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.CreateSpecimenForm"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ include file="/pages/content/common/SpecimenCommonScripts.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 

<%@ page language="java" isELIgnored="false" %>
<head>
<script src="jss/script.js"></script>
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<% 
		int exIdRows=1;
		Map map = null;
//		 For Gridpage
		List dataList = (List) request.getAttribute(Constants.SPREADSHEET_DATA_LIST);
		List columnList = (List) request.getAttribute("columnList");
		Integer identifierFieldIndex = new Integer(4);
		String pageOf = (String)request.getAttribute(Constants.PAGEOF);
%>
<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/createSpecimen.js"></script>
<link href="runtime/styles/xp/grid.css" rel="stylesheet" type="text/css" ></link>
<script language="JavaScript" >
		//Set last refresh time
		if(window.parent!=null)
		{
			if(window.parent.lastRefreshTime!=null)
			{
				window.parent.lastRefreshTime = new Date().getTime();
			}
		}	
		function isLabelBarcodeOrClassChange()
		{
			var parentLabelElement = document.getElementById("parentSpecimenLabel");
			var parentBarcodeElement = document.getElementById("parentSpecimenBarcode");
			var classNameElement = document.getElementById("className");
			
			if((parentLabelElement.value != "-1" || parentBarcodeElement.value != "-1") && classNameElement.value != "-1")
			{
				var action = "${requestScope.actionToCall2}";
				document.forms[0].action = action + "&isLabelBarcodeOrClassChange=true";
				document.forms[0].submit();
			}	
		}
	  	function onClassOrLabelOrBarcodeChange(multipleSpecimen,element)
		{
			if(multipleSpecimen == "1")
				{
				   classChangeForMultipleSpecimen();
				}
		    var radioArray = document.getElementsByName("checkedButton");
		 	var flag = "0";
 			if (radioArray[0].checked) 
			{
			  if(document.getElementById("parentSpecimenLabel").value!= "") 
			  {
				   flag = "1";
			  }
			} 
		     else 
		     {
				if (document.getElementById("parentSpecimenBarcode").value != "") 
				{
	     	    	 flag = "1";
				}
			}
 	    	var classNameElement = document.getElementById("className");
			if(flag=="1" && classNameElement.value != "-1")
			{
				document.forms[0].action = "${requestScope.actionToCall1}";
				document.forms[0].submit();
			}	
			else
			{
				alert("Please enter Parent Label/Barcode and Specimen Class");
				element.checked=true;
			}
		}
		
		function deleteExternalIdentifiers()
		{
			${requestScope.deleteChecked}
		}
		function onNormalSubmit()
		{
			var checked = document.forms[0].aliCheckedButton.checked;
			if(checked)
			{
				setSubmitted('ForwardTo','${requestScope.printAction}','pageOfCreateAliquot');
				confirmDisable('${requestScope.actionToCall}',document.forms[0].activityStatus);
			}
			else
			{	
				var temp = "${requestScope.frdTo}";				
				if(temp == "orderDetails")
				{
					setSubmitted('ForwardTo','${requestScope.printAction}','orderDetails');
			     }
			     else
			    {
				   setSubmitted('ForwardTo','${requestScope.printAction}','newSpecimenEdit');
			     }  
				confirmDisable('${requestScope.actionToCall}',document.forms[0].activityStatus);
			}
		}
	</script>
	<logic:equal name="showRefreshTree" value="true">
		<script language="javascript">
			${requestScope.refreshTree}
		</script>
	</logic:equal>
</head>
	<html:errors />
	<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
		${requestScope.messageKey}
	</html:messages>
   <html:form action="${requestScope.action}">
                      <input type="hidden" id="specimenAttributeKey"
				       name="specimenAttributeKey"
				       value="${requestScope.specimenAttributeKey}" />

						<input type="hidden" id="derivedSpecimenCollectionGroup"
				       name="derivedSpecimenCollectionGroup"
				       value="${requestScope.derivedSpecimenCollectionGroup}" />
					   
					   	<input type="hidden" id="rowSelected"
				       name="rowSelected"
				       value="${requestScope.rowSelected}" />
   <table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="580">
<tr>
	<td>&nbsp;</td>
</tr>
<tr>
 <td>
    <table summary="" cellpadding="3" cellspacing="0" border="0" width="580" >
    <logic:notEmpty name="dataList">
         <tr>
		 <td width="100%" align="right">
		 	    <html:button property="addNewDerived" styleClass="actionButton" onclick="onAddNewButtonClicked();">
				<bean:message key="buttons.addNew"/>
				</html:button>
		 </td>
		 </tr>
   	 	<tr>
			<td class="formTitle" height="20" >
				Derived Specimens For This Parent Specimen
			</td>
		</tr>
   	 	<tr>
			<td>
				<script>
				    var specimenAttributeKey = document.getElementById("specimenAttributeKey");
					if(specimenAttributeKey!=null)
					{
			          parent.window.opener.document.applets[0].setButtonCaption(specimenAttributeKey.value,"");
					}
					function derivedSpecimenGrid(id)
					{
						var cl = mygrid.cells(id,4);
						var rowSelected = cl.getValue();
						var c2 = mygrid.cells(id,0);
						var eventId = c2.getValue();
						var url = "NewMultipleSpecimenAction.do?method=showDerivedSpecimenDialog&rowSelected=" + rowSelected +"&specimenAttributeKey="+document.getElementById("specimenAttributeKey").value;
						document.forms[0].action = url;
				        document.forms[0].submit();
					} 				
					var useDefaultRowClickHandler =2;
					var useFunction = "derivedSpecimenGrid";	
					var gridFor="derivedSpecimen";
				</script>
				<%@ include file="/pages/content/search/GridPage.jsp" %>
			</td>
		</tr>
</table>
	</td>
	</tr>
	<tr>
		 <td width="100%" align="right">
		 	    <html:button property="closebutton" styleClass="actionButton" onclick="closeWindow();">
				<bean:message key="buttons.close"/>
				</html:button>
		 </td>
        </tr>
	</table>
	</logic:notEmpty>		<%-- datalist not empty. to check if this block is required any more --%>
	<logic:empty name="showDerivedPage">
		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="580">
		   <logic:equal name="pageOf" value="query">
		   	<tr>
    		    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
		   			<tr>
				  	<td align="right" colspan="3">
					<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
						   	<td>
						   		<html:submit styleClass="actionButton" onclick="${requestScope.changeAction1}">
						   			<bean:message key="${requestScope.editViewButton}"/>
						   		</html:submit>
						   	</td>
						</tr>
					</table>
					<!-- action buttons end -->
				  </td>
				  </tr>
				</table>
			   </td>
			</tr>
			</logic:equal>
			  <!-- NEW SPECIMEN REGISTRATION BEGINS-->
	    	  <tr>
			    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0" width="580">
				 <tr>
					<td>
						<html:hidden property="${requestScope.oper}" value="${requestScope.operation}"/>
						<html:hidden property="submittedFor" value="ForwardTo"/>
						<html:hidden property="forwardTo" value="${requestScope.frdTo}"/>
						<html:hidden property="multipleSpecimen" value="${requestScope.multipleSpecimen}"/>
						<html:hidden property="containerId" styleId="containerId"/>
						<html:hidden property="nextForwardTo" />
						<td></td>
					</td>
				 </tr>
				 <tr>
					<td>
						<html:hidden property="positionInStorageContainer" />
					</td>
				  </tr>
				<logic:equal name="pageOf" value="${requestScope.query}">
				 <tr>
					<td>
						<html:hidden property="sysmtemIdentifier"/>
					</td>
				 </tr>
				</logic:equal>
				<logic:notEqual name="oper" value="${requestScope.search}">
				 <tr>
				     <td class="formTitle" height="20" colspan="6">
				     	<bean:message key="createSpecimen.derived.title"/>
				     </td>
				 </tr>
				 <logic:notEqual name="multipleSpecimen" value="1">	<!-- to verify for valid case 1 -->
				 <tr>
			     	<td class="formFieldNoBordersSimple" width="5"><b>*</b></td>
				    <td class="formFieldNoBordersSimple" width="175">
					<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="1" onclick="onRadioButtonClick(this)"  >
				    &nbsp;
			        </html:radio>
						<label for="specimenCollectionGroupId">
							<b><bean:message key="createSpecimen.parentLabel"/></b>
						</label>
					</td>
					<td class="formFieldNoBordersSimple" colspan="2">
					<logic:equal name="createSpecimenForm" property="checkedButton" value="1">
				     <html:text styleClass="formFieldSized15"  maxlength="50"  size="30" styleId="parentSpecimenLabel" property="parentSpecimenLabel" disabled="false" />
			        </logic:equal>
			        <logic:equal name="createSpecimenForm" property="checkedButton" value="2">
			 	     <html:text styleClass="formFieldSized15"  maxlength="50"  size="30" styleId="parentSpecimenLabel" property="parentSpecimenLabel" disabled="true" />
			        </logic:equal>
		        	</td>
				 </tr>				 
				 <tr>
			     	<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
				    <td class="formFieldNoBordersSimple" width="175">
					<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="2" onclick="onRadioButtonClick(this)">
				    &nbsp;
			        </html:radio>
						<label for="specimenCollectionGroupId">
							<b><bean:message key="createSpecimen.parentBarcode"/></b>
						</label>
					</td>
					<td class="formFieldNoBordersSimple" colspan="2">
					<logic:equal name="createSpecimenForm" property="checkedButton" value="1">
				    <html:text styleClass="formFieldSized15"  maxlength="50"  size="30" styleId="parentSpecimenBarcode" property="parentSpecimenBarcode" disabled="true" />
			        </logic:equal>
			
			        <logic:equal name="createSpecimenForm" property="checkedButton" value="2">
				    <html:text styleClass="formFieldSized15"  maxlength="50"  size="30" styleId="parentSpecimenBarcode" property="parentSpecimenBarcode" disabled="false" />
			        </logic:equal>
		        	</td>
				 </tr>
				</logic:notEqual>	<%-- if(!multipleSpecimen.equals("1")) case 1 --%>
				<logic:equal name="isSpecimenLabelGeneratorAvl" value="false">
				<tr>
			     	<td class="formFieldNoBordersSimple" width="5">
				     	<logic:notEqual name="oper" value="${requestScope.view}"><b>*<b></logic:notEqual>
				     	<logic:equal name="oper" value="${requestScope.view}">&nbsp;</logic:equal>
				    </td>
				    <td class="formFieldNoBordersSimple">
						<label for="label">
							<b><bean:message key="specimen.label"/></b>
						</label>
					</td>
				    <td class="formFieldNoBordersSimple" colspan="4">
				     	<html:text styleClass="formFieldSized15" size="30" maxlength="50"  styleId="label" property="label" readonly="${requestScope.readOnlyForAll}"/>
				    </td>
				 </tr>
 				</logic:equal>	<%-- if(!Variables.isSpecimenLabelGeneratorAvl) --%>
				 <tr>
				 	<td class="formFieldNoBordersSimple" width="5"><b>*</b></td>
				    <td class="formFieldNoBordersSimple">
				     	<label for="className">
				     		<b><bean:message key="specimen.type"/></b>
				     	</label>
				    </td>
				    <td class="formFieldNoBordersSimple" colspan="2">
					 <autocomplete:AutoCompleteTag property="className"
										  optionsList = "${requestScope.specClassList}"
										  initialValue="${createSpecimenForm.className}"
										  onChange="onTypeChange(this);resetVirtualLocated()"
										  readOnly="${requestScope.readOnlyForAll}"
									    />
		        	</td>
				 </tr>
				 <tr>
				    <td class="formFieldNoBordersSimple" width="5"><b>*</b></td>
				    <td class="formFieldNoBordersSimple">
				     	<label for="type">
				     		<b><bean:message key="specimen.subType"/></b>
				     	</label>
				    </td>
				    <td class="formFieldNoBordersSimple" colspan="2">
					  <autocomplete:AutoCompleteTag property="type"
										  optionsList = "${requestScope.specimenTypeMap}"
										  initialValue="${createSpecimenForm.type}"
										  onChange="onSubTypeChangeUnit('className',this,'unitSpan')"
										  readOnly="false"
										  dependsOn="${createSpecimenForm.className}"
					        />
		        	</td>
				 </tr>
				 <tr>
					<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
					<td class="formFieldNoBordersSimple">							
						<label for="createdDate">
							<bean:message key="specimen.createdDate"/>
						</label>								
					</td>
					<td class="formFieldNoBordersSimple" colspan="2" >
						<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>				
								<td class="message" >
									<ncombo:DateTimeComponent name="createdDate"
							  			id="createdDate"
							  			formName="createSpecimenForm"
							  			value='${requestScope.createdDate}'
							  			styleClass="formDateSized10"/>
								<bean:message key="page.dateFormat" />&nbsp;
								</td>
							</tr>
						</table>
					</td>
				</tr>
				 <tr>
			     	<td class="formFieldNoBordersSimple" width="5">
				     	&nbsp;
				    </td>
				    <td class="formFieldNoBordersSimple">
						<label for="concentration">
							<bean:message key="specimen.concentration"/>
						</label>
					</td>
					<logic:equal name="unitSpecimen" value="${requestScope.UNIT_MG}">
				    		<td class="formFieldNoBordersSimple" colspan="2">
				     			<html:text styleClass="formFieldSized15" size="30" styleId="concentration" property="concentration" readonly="${requestScope.readOnlyForAll}" disabled="false"/>
								&nbsp;<bean:message key="specimen.concentrationUnit"/>
				   			</td>
					</logic:equal>	<!-- if(unitSpecimen.equals(Constants.UNIT_MG)) -->
					<logic:notEqual name="unitSpecimen" value="${requestScope.UNIT_MG}">
							<td class="formFieldNoBordersSimple" colspan="2">
				     			<html:text styleClass="formFieldSized15" size="30" maxlength="10"  styleId="concentration" property="concentration" readonly="${requestScope.readOnlyForAll}" disabled="false"/>
				     			&nbsp;<bean:message key="specimen.concentrationUnit"/>
				    		</td>
					</logic:notEqual>
				 </tr>
				 <tr>
			     	<td class="formFieldNoBordersSimple" width="5">
				     	<logic:notEqual name="${requestScope.oper}" value="${requestScope.view}"><b>*</b></logic:notEqual>
				     	<logic:equal name="${requestScope.oper}" value="${requestScope.view}">&nbsp;</logic:equal>
				    </td>
				    <td class="formFieldNoBordersSimple">
						<label for="quantity">
							<b><bean:message key="specimen.quantity"/></b>
						</label>
					</td>
				    <td class="formFieldNoBordersSimple" colspan="2">
				     	<html:text styleClass="formFieldSized15" size="30" maxlength="10"  styleId="quantity" property="quantity" readonly="${requestScope.readOnlyForAll}"/>
				     	<span id="unitSpan">${requestScope.unitSpecimen}</span>
				     	<html:hidden property="unit"/>
				    </td>
				 </tr>
			 	<logic:notEqual name="multipleSpecimen" value="1">	<!-- to verify for valid case 2 -->
				<tr>
				 	<td class="formFieldNoBordersSimple" width="5"><b>*</b></td>
					<td class="formFieldNoBordersSimple">
					   <label for="className">
					   		<b><bean:message key="specimen.positionInStorageContainer"/></b>
					   </label>
					</td>
					${requestScope.jsForOutermostDataTable}
					${requestScope.jsEquivalentFor}
					<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
				<td class="formFieldNoBordersSimple" colSpan="4">
						<table border="0">
						<logic:equal name="${requestScope.oper}" value="${requestScope.ADD}">
						<tr>
							<td ><html:radio value="1" onclick="onRadioButtonGroupClickForDerived(this)" styleId="stContSelection" property="stContSelection"/></td>
							<td class="formFieldNoBorders">																			
									<bean:message key="specimen.virtuallyLocated" />											
							</td>
						</tr>
						<tr>
							<td ><html:radio value="2" onclick="onRadioButtonGroupClickForDerived(this)" styleId="stContSelection" property="stContSelection"/></td>
							<td>
								<ncombo:nlevelcombo dataMap="${requestScope.dataMap}" 
									attributeNames="${requestScope.attrNames}" 
									initialValues="${requestScope.initValues}"  
									styleClass = "formFieldSized5" 
									tdStyleClass = "customFormField" 
									labelNames="${requestScope.labelNames}" 
									rowNumber="1" 
									onChange = "onCustomListBoxChange(this)"
									formLabelStyle="formLabelBorderless"
									tdStyleClassArray="${requestScope.tdStyleClassArray}"
									disabled = "${requestScope.dropDownDisable}"
									noOfEmptyCombos = "3"/>
									</tr>
									</table>
							</td>
						</tr>
						<tr>
							<td ><html:radio value="3" onclick="onRadioButtonGroupClickForDerived(this)" styleId="stContSelection" property="stContSelection"/></td>
							<td class="formFieldNoBordersSimple">
								<html:text styleClass="formFieldSized10"  size="30" styleId="selectedContainerName" property="selectedContainerName" disabled= "${requestScope.textBoxDisable}"/>
								<html:text styleClass="formFieldSized3"  size="5" styleId="pos1" property="pos1" disabled= "${requestScope.textBoxDisable}"/>
								<html:text styleClass="formFieldSized3"  size="5" styleId="pos2" property="pos2" disabled= "${requestScope.textBoxDisable}"/>
								<html:button styleClass="actionButton" property="containerMap" onclick="${requestScope.buttonOnClicked}" disabled= "${requestScope.textBoxDisable}">
									<bean:message key="buttons.map"/>
								</html:button>
							</td>
						</tr>
						</logic:equal>								
						</table>					
				</td>	
				 </tr>
				 </logic:notEqual>	<%-- if(!multipleSpecimen.equals("1")) case 2 --%>
					<logic:equal name="exceedsMaxLimit" value="true">
					<tr>
						<td>
								<bean:message key="container.maxView"/>
						</td>
					</tr>
					</logic:equal>
				<logic:notEqual name="isSpecimenBarcodeGeneratorAvl" value="true">
				 <tr>
			     	<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
				    <td class="formFieldNoBordersSimple">
						<label for="barcode">
							<bean:message key="specimen.barcode"/>
						</label>
					</td>
				    <td class="formFieldNoBordersSimple" colspan="2">
						<html:text styleClass="formFieldSized"  maxlength="50" size="30" styleId="barcode" property="barcode" readonly="${requestScope.readOnlyForAll}" />
		        	</td>
				 </tr>
				 </logic:notEqual> <!-- if(!Variables.isSpecimenBarcodeGeneratorAvl ) -->
				 <tr>
			     	<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
				    <td class="formFieldNoBordersSimple">
						<label for="comments">
							<bean:message key="specimen.comments"/>
						</label>
					</td>
				    <td class="formFieldNoBordersSimple" colspan="2">
				    	<html:textarea styleClass="formFieldSized" rows="3" styleId="comments" property="comments" readonly="${requestScope.readOnlyForAll}"/>
				    </td>
				 </tr>
 				<!-- Mandar : 16June08 External Identifiers start -->
<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" id="externalIdentifiersTable">
		<tr>
			<td class="formFieldAllBorders" align="right" width="1%">
				<a id="imageEI" style="text-decoration:none" href="javascript:switchStyle('imageEI','eiDispType','externalIdentifiers','addExId');">  
				<img src="images/nolines_minus.gif" border="0" width="18" height="18"/>
				</a>
				<input type="hidden" name="eiDispType" value="${requestScope.eiDispType1}" id="eiDispType" />
			</td> 
			<!-- Patch ends here -->
		     <td class="formTitle" width="200" height="20" colspan="2">
		     	<bean:message key="specimen.externalIdentifier"/>
		     </td>
		     <td class="formButtonField" width="300" colspan="2">
		     	<html:button property="addExId" styleClass="actionButton" styleId="addExId" onclick="insExIdRow('addExternalIdentifier')">
		     		<bean:message key="buttons.addMore"/>
		     	</html:button>
		    </td>
		    <td class="formTitle" align="Right">
					<html:button property="deleteExId" styleClass="actionButton" onclick='${requestScope.delExtIds}' disabled="true">
						<bean:message key="buttons.delete"/>
					</html:button>
			</td>
			<!-- Patch ends here -->
		 </tr>
		 <tr>
			 <td colspan="6" width="100%">
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%" id="externalIdentifiers">
			<!-- Patch ends here -->
		 	<tr>
			 	<td class="formSerialNumberLabel" width="5">
			     	#
			    </td>
			    <td class="formLeftSubTableTitle" width="350">
					<bean:message key="externalIdentifier.name"/>
				</td>
			    <td class="formRightSubTableTitle" colspan="2" width="350">
					<bean:message key="externalIdentifier.value"/>
				</td>
				<td class="formRightSubTableTitle" width="50">
					<label for="delete" align="center">
						<bean:message key="addMore.delete" />
					</label>
				</td>
			 </tr>
		 	 <tbody id="addExternalIdentifier">
		 	 <html:hidden property="exIdCounter"/>
		 	 <logic:iterate id="xi" name="exIdList">
				<tr>
				 	<td class="formSerialNumberField" width="5">${xi.xtrnId}.
					 	<html:hidden property="${xi.exIdentifier}" />
			 		</td>
				    <td class="formField" width="365" >
			     		<html:text styleClass="formFieldSized15" maxlength="255"  styleId="${xi.exName}" property="${xi.exName}" />
			    	</td>
			    	<td class="formField" colspan="2">
			     		<html:text styleClass="formFieldSized15" maxlength="255"  styleId="${xi.exValue}" property="${xi.exValue}" />
			    	</td>
					<td class="formField" width="5">
						<input type=checkbox name="${xi.check}" id="${xi.check}" ${xi.exCondition} onClick="enableButton(document.forms[0].deleteExId,document.forms[0].exIdCounter,'chk_ex_')">		
					</td>
				 </tr>
			  </logic:iterate>
		 </tbody>
		 </table>
	 	</td>
	 </tr>
</table>				  				
  				<!-- Mandar : 16June08 External Identifiers end -->
				 </table>
				 <logic:notEqual name="multipleSpecimen" value="1">	<!-- to verify for valid case 3 -->
				 <logic:notEqual name="pageOf" value="${requestScope.query}">				 			
				<table cellpadding="4" cellspacing="0">
					<tr>					
						<td class="formFieldNoBordersBold" height="20" colspan="5">
						<input type="checkbox" name="aliCheckedButton" onclick="onCheckboxButtonClick(this)" /> &nbsp; <bean:message key="specimen.aliquot.message"/>
							&nbsp;&nbsp;&nbsp;
		                <bean:message key="aliquots.noOfAliquots"/>
	                    &nbsp;
                        <input type="text" id="noOfAliquots" name="noOfAliquots" class = "formFieldSized5" disabled="true" />
						&nbsp;&nbsp;&nbsp;
		                <bean:message key="aliquots.qtyPerAliquot"/>
	                    &nbsp;
                        <input type="text" id="quantityPerAliquot" name="quantityPerAliquot" class = "formFieldSized5" disabled="true" />
	    				</td>
					</tr>	
					<tr>
						<td colspan="3" class="formLabelNoBackGround" width="40%">
							<html:checkbox property="disposeParentSpecimen">
							<bean:message key="aliquots.disposeParentSpecimen" />
							</html:checkbox>
						</td>
					</tr>
					<tr>					
						<td class="formFieldNoBorders" colspan="5"  height="20" >
							<html:checkbox styleId="printCheckbox" property="printCheckbox" value="true" onclick="">
								<bean:message key="print.checkboxLabel"/>
							</html:checkbox>
						</td>
					</tr>
				</table>
				</logic:notEqual>
				</logic:notEqual><!-- to verify for valid case 3 -->
								
 			   	 <logic:notEqual name="${requestScope.oper}" value="${requestScope.view}">		
				 	<tr>
				  		<td align="right" colspan="4">
				  		<logic:notEqual name="multipleSpecimen" value="1">	<!-- to verify for valid case 4 -->
							<table cellpadding="4" cellspacing="0">
								<tr>
						   			<td>
						   				<html:button styleClass="actionButton" property="submitButton" onclick="onNormalSubmit()">
						   					<bean:message key="buttons.submit"/>
						   				</html:button>
						   			</td>
									<td class="formFieldNoBorders" nowrap>
										<html:button
											styleClass="actionButton" property="moreButton"
											title="${requestScope.SPECIMEN_BUTTON_TIPS}"
											value="${requestScope.SPECIMEN_FORWARD_TO_LIST}"
											onclick="${requestScope.addMoreSubmit}" >
										</html:button>
									</td>
				  		</logic:notEqual> <!-- to verify for valid case 4 -->
				  		<logic:equal name="multipleSpecimen" value="1">
							<table cellpadding="4" cellspacing="0" width="100%">
							<tr>
					   			<td align="right">
					   				<html:submit styleClass="actionButton" onclick="javaScript:${requestScope.changeAction3}">
					   					<bean:message key="buttons.submit"/>
					   				</html:submit>
					   			</td>
								<td width="2%">
					   				&nbsp;
					   			</td>
							</tr>
							</table>
						</logic:equal>	
				  		</td>
				 	</tr>
				 </logic:notEqual>
				</logic:notEqual>				
			 <!-- NEW SPECIMEN REGISTRATION ends-->
	</table>
	</logic:empty> <%-- if(request.getAttribute("showDerivedPage")==null) --%>
 </html:form>