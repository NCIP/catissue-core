<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<!-- 
	 @author Virender Mehta 
	 @version 1.1	
	 Jsp name: DefineConsent.jsp
	 Description: This jsp is associted with CollectionProtocol.jsp and it is for entering participant Consents	
	 Company: Washington University, School of Medicine, St. Louis.
-->						

<%-- Main table Start --%>
<table style="display:none;" cellpadding="4" cellspacing="0" border="0" class="contentPage" width="99%" id="table4">
	<tr>
		<td>
			<%-- First Table for Unsigned URL   Start--%>
			<table summary="" cellpadding="3" cellspacing="0" border="0"  width="99%" id="table10">
				
				<%-- Title Add Consents --%>
				<tr>
					<td class="formTitle" height="20" colspan="4" width="99%">
						<bean:message key="consent.addconsents" />
						</div>
					</td>
				</tr>
				
				<%-- Title Unsigned URL form --%>					
				<tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
						<label for="unsignedConsentURLName">
							<bean:message key="consent.unsignedformurl" />
						</label>
					</td>
					<td class="formField" colspan="2">
						<html:text styleClass="formFieldSized" maxlength="200" size="30" styleId="unsignedConsentURLName" property="unsignedConsentURLName"/>
					</td>
				</tr>
			</table>	
			<%-- First Table for Unsigned URL   End--%>					
		</td>
	</tr>		

	<tr>
		<td style="width:99%;">
			<%-- Outer Table for Add Consents  Start --%>			
			<table summary="" cellpadding="0" cellspacing="0" border="0"  width="99%">
				
				<%-- Title Consent Tier--%>
				<tr>
					<td class="formTitle" height="20%">
						<bean:message key="consent.consenttiers" />
					</td>
					<%-- Add and Remove action button--%>							
					<td align="right" class="formTitle">
						<html:button property="addButton" styleClass="actionButton" onclick="addConsentTier()" value="Add More"/>
						<html:button property="removeButton" styleClass="actionButton" onclick="deleteSelected()" value="Delete"/>
					</td>
				</tr>
	
				<tr>
					<td colspan="2">
						<div style="overflow:auto;height:250px;">
							<%-- Inner Table for Add Consents  Start --%>											
							<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%" id="innertable">
								<tr>
									<%-- Title Serial No --%>											
									<td class="formLeftSubTableTitle" width="5%">
										<div align="center">
											<bean:message key="requestlist.dataTabel.serialNo.label" />
										</div>
									</td>	
									
									<%-- Title CheckBox --%>	
									<%
									if(operation.equals(Constants.EDIT))
									{
									%>
									<td class="formLeftSubTableTitle" width="5%">
										<div align="center">
											<input type=checkbox name="selectAll" onclick="checkAll(this)" disabled="disabled"/>
										</div>	
									</td>	
									<%
									}
									else
									{
									%>
									<td class="formLeftSubTableTitle" width="5%">
										<div align="center">
											<input type=checkbox name="selectAll" onclick="checkAll(this)"/>
										</div>	
									</td>	
									<%
									}
									%>
									<%-- Title Statements --%>
									<td class="formLeftSubTableTitle" width="90%">
										<html:hidden property="consentTierCounter"/>
										<bean:message key="consent.statements" />
									</td>
								</tr>
								<%-- If Operation=Edit then get Consents from DB and Operation=Add then show one ConsentTier --%>								
								<%
								CollectionProtocolForm collectionProtocolForm = null;
								collectionProtocolForm =(CollectionProtocolForm)request.getAttribute("collectionProtocolForm");
								int noOfConsents=1;
								if(collectionProtocolForm != null && collectionProtocolForm instanceof CollectionProtocolForm)
								{
									noOfConsents = collectionProtocolForm.getConsentTierCounter();								
								}
								for(int counter=0;counter<noOfConsents;counter++)
								{		
									String consentName="consentValue(ConsentBean:"+counter+"_statement)";
									String consentKey="consentValue(ConsentBean:"+counter+"_consentTierID)";
									boolean readonly=false;
								%>
								<tr>
									<td class="tabrightmostcell" width="3%" align="right" width="5%">
										<%=(noOfConsents-counter)%>.
									</td>
									<td class="formField" width="10%" align="center" width="5%">
									<%
									if(operation.equals(Constants.EDIT))
									{
										readonly=true;
									%>
									<input type="checkbox" name="consentcheckBoxs" Id="check1" disabled="disabled"/>	
									<%
									}
									else
									{
									%>
									<input type="checkbox" name="consentcheckBoxs" Id="check1"/>
									<%
									}
									%>
									</td>
									<html:hidden property="<%=consentKey%>"/>
									<td class="formField" width="90%">
										<html:textarea styleClass="formFieldSized"  style="width:90%;" rows="2" property="<%=consentName%>" readonly="<%=readonly%>"/>
									</td>
								</tr>
								<%
								}
								%>						
							</table>
						<%-- Inner Table for Add Consents  End --%>										
						</div>
					</td>
				</tr>
				<%-- Outer Table for Add Consents End --%>
			</table>	
		</td>
	</tr>	
</table>	
<%-- Main table End --%>