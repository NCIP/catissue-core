<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
	<script language="JavaScript" src="jss/script.js" type="text/javascript"></script>
	<script type="text/javascript">
	//Dimension 2 capacity label
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
					col1.innerHTML='<span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span>';
					col2.className="black_ar";
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

	function validate(submittedFor,forwardTo)
	{		
		document.forms[0].submittedFor.value = submittedFor;
		document.forms[0].forwardTo.value    = forwardTo;
		if(validateAny(document.forms[0].holdsStorageTypeIds)==false)
		{
			alert("<bean:message key="errmsg.storagetype"/>");
		}
		else
		{
			if(validateAny(document.forms[0].holdsSpecimenClassTypes)==false)
			{
				alert("<bean:message key="errmsg.storagetype"/>");
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
			
<html:errors/>
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
<!-- Contents Starts here -->
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
	<html:form action='${requestScope.formName}'method="post">	
		<html:hidden property="operation"/>
		<html:hidden property="submittedFor"/>
		<html:hidden property="forwardTo"/>
		<html:hidden property="id" />
		<html:hidden property="redirectTo"/>
  <tr>
    <td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="td_color_bfdcf3">
			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
			          <tr>
						<td width="100%" colspan="2" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
				              <tr>
								<td colspan="3" valign="top" class="td_color_bfdcf3">
									<table width="15%" border="0" cellpadding="0" cellspacing="0"						background="images/uIEnhancementImages/table_title_bg.gif">
					                  <tr>
										<td width="74%">
											<span class="wh_ar_b">&nbsp;&nbsp;&nbsp;
												<bean:message key="storageType.name" />
											</span>
										 </td>
					                     <td width="26%" align="right">
											<img src="images/uIEnhancementImages/table_title_corner2.gif" width="31" height="24" /></td>
									   </tr>
					                </table>
								</td>
							</tr>
							<tr>
				                <td width="1%" valign="top" class="td_color_bfdcf3">&nbsp;
								</td>
				                <td width="9%" valign="top" class="td_tab_bg">&nbsp;
								</td>
				                <td width="90%" valign="bottom" class="td_color_bfdcf3" >
									<table width="100%" border="0" cellpadding="0" cellspacing="0">
					                  <tr>
										<td width="4%" class="td_tab_bg" >&nbsp;
										</td>
			<!-- for tabs selection -->
					<logic:equal name="operation" value='${requestScope.operationAdd}'>
					                    <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif" >
											<img src="images/uIEnhancementImages/tab_add_user.jpg" alt="Add" width="57" height="22" /></td>
					                    <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif">
											<html:link page="/SimpleQueryInterface.do?pageOf=pageOfStorageType&aliasName=StorageType">
												<img src="images/uIEnhancementImages/tab_edit_user.jpg" alt="Edit" width="59" height="22" border="0" /></html:link></td>
					                    <td width="15%" valign="bottom"		background="images/uIEnhancementImages/tab_bg.gif">&nbsp;
										</td>
					</logic:equal>

					<logic:equal name="operation" value='${requestScope.operationEdit}'>
										<td width="6%" valign="bottom"													background="images/uIEnhancementImages/tab_bg.gif" >
											<html:link page="/StorageType.do?operation=add&pageOf=pageOfStorageType">
												<img src="images/uIEnhancementImages/tab_add_user1.jpg" alt="Add" width="57" height="22" border="0" />
											</html:link>
										</td>
					                    <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif">
											<img src="images/uIEnhancementImages/tab_edit_user1.jpg" alt="Edit" width="59" height="22" />
										</td>
					                    <td width="15%" valign="bottom"													background="images/uIEnhancementImages/tab_bg.gif">&nbsp;
										</td>
					</logic:equal>
					                    <td width="65%" valign="bottom" class="td_tab_bg" >
											&nbsp;
										</td>
										<td width="1%" align="left" valign="bottom" class="td_color_bfdcf3" >
											&nbsp;
										</td>
									</tr>
				                </table>
							</td>              
						</tr>
		            </table>
				</td>
            </tr>
            <tr>
				<td colspan="2" class="td_color_bfdcf3" style="padding-left:10px; padding-right:10px;					padding-bottom:10px;">
					<table width="100%" border="0" cellpadding="3" cellspacing="0" bgcolor="#FFFFFF">
						<tr>
			                <td colspan="3" align="left">
								<table width="99%" border="0" cellpadding="1" cellspacing="0">
									<tr>
				                      <td>
										<table width="100%" border="0" cellpadding="0" cellspacing="0" class="td_color_ffffff">
											<tr>
									            <td class=" grey_ar_s">
													<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /> 
													<bean:message key="commonRequiredField.message" />
												</td>
					                          </tr>
										  </table>
										 </td>
					                    </tr>
									</table>
								</td>
							 </tr>
				             <tr>
								<td align="left" class="tr_bg_blue1">
									<span class="blue_ar_b">
			<logic:equal name="operation" value='${requestScope.operationAdd}'>
										<bean:message key="storageType.title"/>
			</logic:equal>
			<logic:equal name="operation" value='${requestScope.operationEdit}'>
										<bean:message key="storageType.editTitle"/>
			</logic:equal>
									</span>
								</td>
					            <td align="right" class="tr_bg_blue1">&nbsp;
								</td>
				              </tr>
							  <tr>
				                <td colspan="3" align="left" style="padding-top:10px; padding-bottom:15px;">
									<div id="part_det" >
					                    <table width="100%" border="0" cellpadding="3" cellspacing="0">
                      <!-- Name of the storageType -->
						                   <tr>
											  <td width="1%" align="left" class="black_ar">
												<span class="blue_ar_b">
													<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
												</span>
											   </td>
					                           <td width="16%" align="left" class="black_ar">
													<label for="type">
														<bean:message key="storageType.type"/> 
									    			</label>
												</td>
						                        <td colspan="3" align="left">
													<label>
														<html:text styleClass="black_ar"  maxlength="255"  size="30" styleId="type" property="type"/>
													</label>
												</td>
					                          </tr>
											  <tr>
						                         <td align="left" class="black_ar">&nbsp;
												 </td>
						                         <td align="left" class="black_ar">
													<label for="defaultTemperature">
														<bean:message key="storageType.defaultTemperature"/>
													</label>
												</td>
					                            <td colspan="3" align="left">
													<html:text styleClass="black_ar"  maxlength="10"  size="30" styleId="defaultTemperature" property="defaultTemperature"/>
													<span class="black_ar">
														&nbsp;<sup>0</sup>C
													</span>
												</td>
					                          </tr>
											  <tr>
						                          <td align="left" class="black_ar">&nbsp;
												  </td>
						                          <td align="left" class="black_ar">&nbsp;
												  </td>
						                          <td align="left" class="tabletd1">
													<bean:message key="storageType.name" />
												  </td>
						                          <td align="left" class="tabletd1">
													<label>
														<html:radio property="specimenOrArrayType" value="Specimen" onclick="onRadioButtonClick('Specimen')"/> 
													</label>
														<bean:message key="storageContainer.specimenClass" />
												  </td>
								                  <td align="left" class="tabletd1">
													<html:radio property="specimenOrArrayType" value="SpecimenArray" onclick="onRadioButtonClick('SpecimenArray')"/>
													<bean:message key="storageContainer.specimenArrayType" />
												</td>
											</tr>
											<tr>
												<td align="left" class="black_ar">&nbsp;
												</td>
								                <td align="left" class="black_ar">
													<bean:message key="storageType.holds" />
												</td>
								                <td width="25%" align="left" class="tabletd1">
													<html:select property="holdsStorageTypeIds" styleClass="formFieldSized12" styleId="holdStorageTypeIds" size="4" multiple="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
														<html:options collection='${requestScope.holds_List_1}' labelProperty="name" property="value"/>
													</html:select>
												</td>
								                <td width="25%" align="left" class="tabletd1">
				<logic:equal name="storageTypeForm" property="specimenOrArrayType" value="Specimen">
													<html:select property="holdsSpecimenClassTypes" styleClass="formFieldSized12" styleId="holdSpecimenClassTypeIds" size="4" multiple="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
														<html:options collection='${requestScope.holds_List_2}' labelProperty="name" property="value"/>
													</html:select>
				</logic:equal>
				
				<logic:equal name="storageTypeForm" property="specimenOrArrayType" value="SpecimenArray">
													<html:select property="holdsSpecimenClassTypes" styleClass="formFieldSized12" styleId="holdSpecimenClassTypeIds" size="4" multiple="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="true">
														<html:options collection='${requestScope.holds_List_2}' labelProperty="name" property="value"/>
													</html:select>
				</logic:equal>
												</td>
												<td width="30%" align="left" class="tabletd1">
				<logic:equal name="storageTypeForm" property="specimenOrArrayType" value="SpecimenArray">
													<html:select property="holdsSpecimenArrTypeIds" styleClass="formFieldSized12" styleId="holdsSpecimenArrTypeIds" size="4" multiple="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
														<html:options collection='${requestScope.holds_List_3}' labelProperty="name" property="value"/>
													</html:select>
				</logic:equal>
				<logic:equal name="storageTypeForm" property="specimenOrArrayType" value="Specimen">
													<html:select property="holdsSpecimenArrTypeIds" styleClass="formFieldSized12" styleId="holdsSpecimenArrTypeIds" size="4" multiple="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="true">
														<html:options collection='${requestScope.holds_List_3}' labelProperty="name" property="value"/>
													</html:select>
				</logic:equal>
												</td>
											</tr>
					                    </table>
					                </div>
								</td>
				             </tr>
				             <tr onclick="showHide('add_id')">
								 <td height="20" align="left" class="tr_bg_blue1">
									<span class="blue_ar_b">
										<label for="defaultCapacity">
											<bean:message key="storageType.defaultCapacity"/>
										</label>
									</span>
								</td>
				                <td height="20" align="right" class="tr_bg_blue1" >
									<img src="images/uIEnhancementImages/dwn_arrow1.gif" width="7" height="8" hspace="10" border="0" class="tr_bg_blue1" />
								</td>
				              </tr>
							  <tr>
				                <td colspan="3" style="padding-top:10px;">
									<div id="add_id" style="display:none" >
					                  <table width="100%" border="0" cellpadding="2" cellspacing="0">
										<tr>
					                      <td width="1%" align="left" valign="top" class="tdPartition">
											<span class="blue_ar_b">
												<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="3"  />
											</span>
										   </td>
						                   <td width="17%" align="left" valign="top" class="tdPartition">
												<label for="oneDimensionCapacity">
													<bean:message key="storageType.oneDimensionCapacity"/>
												</label>
											</td>
						                    <td width="27%" align="left" class="tdPartition">
												<html:text styleClass="black_ar"  maxlength="10"  size="20" styleId="oneDimensionCapacity" property="oneDimensionCapacity"/>
											</td>
						                    <td width="1%" align="left" class="tdPartition">
												<span class="blue_ar_b">
													<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
												</span>
											</td>
						                    <td width="19%" align="left" class="tdPartition">
												<label for="oneDimensionLabel">
													<bean:message key="storageType.oneDimensionLabel"/>
												</label>
											</td>
											<td width="35%" align="left" class="tdPartition">
												<html:text styleClass="black_ar" maxlength="255" size="20" styleId="oneDimensionLabel" property="oneDimensionLabel"/>
											</td>
					                    </tr>
										<tr>
					                      <td align="left" valign="top" class="tdPartition">
											<span class="blue_ar_b">
												<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="3" />
											</span>
										  </td>
					                      <td align="left" valign="top" class="tdPartition">
											<label for="twoDimensionCapacity">
												<bean:message key="storageType.twoDimensionCapacity"/>
											</label>
										  </td>
					                      <td align="left" class="tdPartition">
											<html:text styleClass="black_ar"  maxlength="10"  size="20" styleId="twoDimensionCapacity" property="twoDimensionCapacity" onkeyup="capacityChanged(this)" />
										  </td>
									      <td align="left" valign="top" class="tdPartition" id ="col1">
												${requestScope.strStar}
										  </td>
					                      <td align="left" class="tdPartition">
											<label for="twoDimensionLabel">
												<bean:message key="storageType.twoDimensionLabel"/>
											</label>
										  </td>
					                      <td align="left" class="tdPartition">
												<html:text styleClass="black_ar"  maxlength="255" size="20" styleId="twoDimensionLabel" property="twoDimensionLabel"/>
										   </td>
					                    </tr>
				                  </table>
								</td>
				              </tr>
							  <tr >
								<td colspan="3">&nbsp;
								</td>
				              </tr>
			  <!-- Action buttons begins -->
							  <tr  class="td_color_F7F7F7">
					             <td colspan="3" class="buttonbg">
									<html:button styleClass="blue_ar_b" property="submitPage" title="Submit Only"
										value='${requestScope.submit}' onclick='${requestScope.normalSubmit}'>  
									</html:button>
									&nbsp;|&nbsp;
									<html:button styleClass="blue_ar_b" property="storageContainerPage"		title="Submit and Add Container" value='${requestScope.addContainer}'			
					  					onclick='${requestScope.forwardToSubmit}'>
									</html:button>
									&nbsp;|&nbsp;
									<span class="cancellink">
										<html:link page="/ManageAdministrativeData.do" >
											<bean:message key="buttons.cancel" />
										</html:link>
									</span>
								  </td>
								</tr>
			  <!-- Action Buttons Ends -->
						</table>
					 </td>
				  </tr>
			  </table>
		   </td>
		</tr>
    </table>
  </td>
 </tr>
</html:form>
</table>
<p><!--end content --></p>
