<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<% 
		String operation = (String)request.getAttribute(Constants.OPERATION);
		String formName,pageView=operation,editViewButton="buttons."+Constants.EDIT;
		boolean readOnlyValue=false,readOnlyForAll=false;

		if(operation.equals(Constants.EDIT))
		{
			editViewButton="buttons."+Constants.VIEW;
			formName = Constants.SPECIMEN_EDIT_ACTION;
			readOnlyValue=true;
		}
		else
		{
			formName = Constants.SPECIMEN_ADD_ACTION;
			readOnlyValue=false;
		}

		if (operation.equals(Constants.VIEW))
		{
			readOnlyForAll=true;
		}

		String pageOf = (String)request.getAttribute(Constants.PAGEOF);
%>

	<html:errors />

		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
			
		   <html:form action="<%=Constants.SPECIMEN_ADD_ACTION%>">
		   
		   <logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
		   	<tr>
    		    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
		   			<tr>
				  	<td align="right" colspan="3">
					<%
						String changeAction = "setFormAction('MakeParticipantEditable.do?"+Constants.EDITABLE+"="+!readOnlyForAll+"')";
				 	%>
					<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
						   	<td>
						   		<html:submit styleClass="actionButton" onclick="<%=changeAction%>">
						   			<bean:message key="<%=editViewButton%>"/>
						   		</html:submit>
						   	</td>
							<td>
								<html:reset styleClass="actionButton">
									<bean:message key="buttons.export"/>
								</html:reset>
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
			
			<!-- If operation is equal to edit or search but,the page is for query the identifier field is not shown -->
			<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
				<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
			<!-- ENTER IDENTIFIER BEGINS-->	
			  <br/>	
  	    	  <tr>
    		    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
			 	 
				  <tr>
				     <td class="formTitle" height="20" colspan="3">
				     	<bean:message key="user.searchTitle"/>
				     </td>
				  </tr>
				  
				  <tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="identifier">
								<bean:message key="user.identifier"/>
							</label>
						</td>
					    <td class="formField">
					    	<html:text styleClass="formFieldSized" size="30" styleId="identifier" property="identifier" readonly="<%=readOnlyForAll%>"/>
					    </td>
				  </tr>	

				 <%
					String changeAction = "setFormAction('"+Constants.PARTICIPANT_SEARCH_ACTION+"');setOperation('"+Constants.SEARCH+"');";
				 %>
 
				  <tr>
				   <td align="right" colspan="3">
					 <table cellpadding="4" cellspacing="0" border="0">
						 <tr>
						    <td>
						    	<html:submit styleClass="actionButton" value="Search" onclick="<%=changeAction%>"/></td>
						 </tr>
					 </table>
				   </td>
				  </tr>

				 </table>
			    </td>
			  </tr>
			  <!-- ENTER IDENTIFIER ENDS-->
			  	</logic:notEqual>
			  </logic:notEqual>
			  
			   	
			  <!-- NEW SPECIMEN REGISTRATION BEGINS-->
	    	  <tr>
			    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
				 <tr>
					<td>
						<html:hidden property="<%=Constants.OPERATION%>" value="<%=operation%>"/>
					</td>
				 </tr>
				 
				<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
				 <tr>
					<td>
						<html:hidden property="sysmtemIdentifier"/>
					</td>
				 </tr>
				</logic:equal>

				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.SEARCH%>">
					<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">
				 		<tr>
				     		<td class="formMessage" colspan="3">* indicates a required field</td>
				 		</tr>
				 	</logic:notEqual>
				 <tr>
				     <td class="formTitle" height="20" colspan="3">
				     <%String title = "specimen."+pageView+".title";%>
				     	<bean:message key="<%=title%>"/>
				     </td>
				     <td class="formTitle">
		        		&nbsp;
   					</td>
				 </tr>
				 <tr>
			     	<td class="formRequiredNotice" width="5">*</td>
				    <td class="formLabel">
						<label for="specimenCollectionGroupId">
							<bean:message key="specimen.specimenCollectionGroupId"/>
						</label>
					</td>
					<td class="formSmallField">
			     		<html:select property="specimenCollectionGroupId" styleClass="formFieldSmallSized" styleId="specimenCollectionGroupId" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="specimenCollectionGroupIdList" labelName="specimenCollectionGroupIdList"/>		
						</html:select>
		        	</td>
		        	<td class="formField">
		        		<a href="SpecimenCollectionGroup.do?operation=add">
      						<bean:message key="app.specimenCollectionGroup" />
   						</a>
   					</td>
				 </tr>
				 <tr>
				 	<td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formLabel">
				     	<label for="type">
				     		<bean:message key="specimen.type"/>
				     	</label>
				    </td>
				    <td class="formField">
				     	<html:select property="type" styleClass="formFieldSized" styleId="type" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="specimenTypeList" labelName="specimenTypeList"/>		
						</html:select>
		        	</td>
		        	<td class="formField">
		        		&nbsp;
   					</td>
				 </tr>
				 <tr>
				    <td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formLabel">
				     	<label for="subType">
				     		<bean:message key="specimen.subType"/>
				     	</label>
				    </td>
				    <td class="formField">
				     	<html:select property="subType" styleClass="formFieldSized" styleId="subType" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="specimenSubTypeList" labelName="specimenSubTypeList"/>		
						</html:select>
		        	</td>
		        	<td class="formField">
		        		&nbsp;
   					</td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel">
						<label for="tissueSite">
							<bean:message key="specimen.tissueSite"/>
						</label>
					</td>
				     <td class="formField">
				     	<html:select property="tissueSite" styleClass="formFieldSized" styleId="tissueSite" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="tissueSiteList" labelName="tissueSiteList"/>		
						</html:select>
		        	  </td>
		        	  <td class="formField">
		        	  	&nbsp;
   					  </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel">
						<label for="tissueSide">
							<bean:message key="specimen.tissueSide"/>
						</label>
					</td>
				     <td class="formField">
				     	<html:select property="tissueSide" styleClass="formFieldSized" styleId="tissueSide" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="tissueSideList" labelName="tissueSideList"/>		
						</html:select>
		        	  </td>
		        	  <td class="formField">
		        	  &nbsp;
   					  </td>
				 </tr>
				 <tr>
				    <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="pathologicalStatus">
							<bean:message key="specimen.pathologicalStatus"/>
						</label>
					</td>
				    <td class="formField">
				     	<html:select property="pathologicalStatus" styleClass="formFieldSized" styleId="pathologicalStatus" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="pathologicalStatusList" labelName="pathologicalStatusList"/>		
						</html:select>
		        	</td>
		        	<td class="formField">
		        	&nbsp;
   					</td>
				 </tr>
				 <tr>
			     	<td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="concentration">
							<bean:message key="specimen.concentration"/>
						</label>
					</td>
				    <td class="formField">
				     	<html:text styleClass="formFieldSized" size="30" styleId="concentration" property="concentration" readonly="<%=readOnlyForAll%>"/>
				    </td>
				    <td class="formField">
		        	&nbsp;
   					</td>
				 </tr>
				 <tr>
			     	<td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="quantity">
							<bean:message key="specimen.quantity"/>
						</label>
					</td>
				    <td class="formField">
				     	<html:text styleClass="formFieldSized" size="30" styleId="quantity" property="quantity" readonly="<%=readOnlyForAll%>"/>
				    </td>
				    <td class="formField">
		        	&nbsp;
   					</td>
				 </tr>

				 <!-- Storage Begins here -->
				 <tr>
				     <td class="formSubTableTitle" height="20" colspan="2">
				     	<bean:message key="specimen.storage"/>
				     </td>
				     <td class="formButtonField">
				     	<html:submit styleClass="actionButton">
							<bean:message key="buttons.showMap"/>
						</html:submit>
				    </td>
				  </tr>
 				  <tr>
			     	<td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="storageContainer">
							<bean:message key="specimen.storageContainer"/>
						</label>
					</td>
				    <td class="formField">
				     	<html:text styleClass="formFieldSized" size="30" styleId="storageContainer" property="storageContainer" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>
				 <tr>
			     	<td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="positionDimensionOne">
							<bean:message key="specimen.positionDimensionOne"/>
						</label>
					</td>
				    <td class="formField">
				     	<html:text styleClass="formFieldSized" size="30" styleId="positionDimensionOne" property="positionDimensionOne" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>
				 <tr>
			     	<td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="positionDimensionTwo">
							<bean:message key="specimen.positionDimensionTwo"/>
						</label>
					</td>
				    <td class="formField">
				     	<html:text styleClass="formFieldSized" size="30" styleId="positionDimensionTwo" property="positionDimensionTwo" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>
				 <!-- Storage Ends here -->
				 
				 <!-- External Identifiers Begin here -->
				 <tr>
				     <td class="formSubTableTitle" height="20" colspan="2">
				     	<bean:message key="specimen.externalIdentifier"/>
				     </td>
				     <td class="formButtonField">
				     	<html:submit styleClass="actionButton">
							<bean:message key="buttons.addMore"/>
						</html:submit>
				    </td>
				  </tr>
				 <tr>
				 	<td class="formSerialNumberLabel" width="5">
				     	#
				    </td>
				    <td class="formLeftSubTableTitle">
						<bean:message key="externalIdentifier.name"/>
					</td>
				    <td class="formRightSubTableTitle">
						<bean:message key="externalIdentifier.value"/>
					</td>
				 </tr>
				 <tr>
				 	<td class="formSerialNumberField" width="5">
				     	1
				     </td>
				    <td class="formField">
						<html:text styleClass="formFieldSized" size="30" styleId="externalIdentifierName" property="externalIdentifierType(1)" readonly="<%=readOnlyForAll%>"/>
					</td>
				    <td class="formField">
				     	<html:text styleClass="formFieldSized" size="30" styleId="externalIdentifierValue" property="externalIdentifierName(1)" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>	
				 			 
				 <tr>
			     	<td class="formSerialNumberField" width="5">
				     	2
				     </td>
				    <td class="formField">
						<html:text styleClass="formFieldSized" size="30" styleId="externalIdentifierName" property="externalIdentifierType(2)" readonly="<%=readOnlyForAll%>"/>
					</td>
				    <td class="formField">
				     	<html:text styleClass="formFieldSized" size="30" styleId="externalIdentifierValue" property="externalIdentifierName(2)" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>
				 <!-- External Identifiers End here -->				 
				 
				 <!-- Bio-hazards Begin here -->
				 <tr>
				     <td class="formSubTableTitle" height="20" colspan="3">
				     	<bean:message key="specimen.biohazards"/>
				     </td>
				 </tr>
				 <tr>
				 	<td class="formSerialNumberLabel" width="5">
				     	#
				    </td>
				    <td class="formLeftSubTableTitle">
						<bean:message key="biohazards.type"/>
					</td>
				    <td class="formRightSubTableTitle">
						<bean:message key="biohazards.name"/>
					</td>
				 </tr>
				 <tr>
				     <td class="formSerialNumberField" width="5">
				     	1
				     </td>
				     <td class="formField">
				     	<html:select property="biohazard(1)" styleClass="formFieldSized" styleId="race" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="biohazardTypeList" labelName="biohazardTypeList"/>		
						</html:select>
					 </td>
				     <td class="formField">
				     	<html:select property="biohazard(1)" styleClass="formFieldSized" styleId="race" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="biohazardNameList" labelName="biohazardNameList"/>		
						</html:select>
		        	 </td>
				 </tr>
				 <tr>
				    <td class="formSerialNumberField" width="5">
				     	2
				     </td>
				    <td class="formField">
				    	<html:select property="biohazard(2)" styleClass="formFieldSized" styleId="gender" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="biohazardTypeList" labelName="biohazardTypeList"/>		
						</html:select>
				    </td>
				    <td class="formField">
				     	<html:select property="biohazard(2)" styleClass="formFieldSized" styleId="gender" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="biohazardNameList" labelName="biohazardNameList"/>		
						</html:select>
		        	</td>
				 </tr>
				 <!-- Bio-hazards End here -->	
				 
				 <tr>
			     	<td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="comments">
							<bean:message key="specimen.comments"/>
						</label>
					</td>
				    <td class="formField">
				    	<html:textarea styleClass="formFieldSized" rows="3" styleId="comments" property="comments" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>
				 
 			   	 <logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">		
				 	<tr>
				  		<td align="right" colspan="3">
							<%
								String changeAction = "setFormAction('"+formName+"')";
				 			%>
							<!-- action buttons begins -->
							<table cellpadding="4" cellspacing="0" border="0">
								<tr>
						   			<td>
						   				<html:submit styleClass="actionButton" onclick="<%=changeAction%>">
						   					<bean:message key="buttons.submit"/>
						   				</html:submit>
						   			</td>
									<td>
										<html:reset styleClass="actionButton">
											<bean:message key="buttons.reset"/>
										</html:reset>
									</td> 
									<td>
										<html:reset styleClass="actionButton">
											<bean:message key="buttons.moreSpecimen"/>
										</html:reset>
									</td>
								</tr>
							</table>
							<!-- action buttons end -->
				  		</td>
				 	</tr>
				 </logic:notEqual>
				 
				</logic:notEqual>
				</table>
			  </td>
			 </tr>
			 
			 <!-- NEW SPECIMEN REGISTRATION ends-->
			 </html:form>
		</table>