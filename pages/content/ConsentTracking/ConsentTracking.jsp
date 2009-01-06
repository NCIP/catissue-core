 <jsp:directive.page import="edu.wustl.catissuecore.domain.ConsentTier"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.actionForm.*"%>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<!-- 
	 @author Virender Mehta 
	 @version 1.1	
	 Jsp name: ConsentTracking.jsp
	 Company: Washington University, School of Medicine, St. Louis.
-->
<LINK href="css/catissue_suite.css" type=text/css rel=stylesheet>
<script src="jss/javaScript.js" type="text/javascript"></script>
<script language="JavaScript">

var consentIDArray=new Array(<%=form.getConsentTierCounter()%>);
var changeInStatus=false;

function cancelWindow()
{
  parent.consentWindow.hide();
}

function changeInResponse(responseIdkey)
{
	var index=-1;
	var flag=false;
	for(i=0;i<consentIDArray.length;i++)
	{
		if(consentIDArray[i]==responseIdkey)
		{
			flag=true;
			break;
		}
		if(consentIDArray[i]!=null&&consentIDArray[i]!="")
		{
			index=i;
		}
	}
	if(flag==false)
	{
		consentIDArray[index+1]=responseIdkey;
		changeInStatus=true;
	}	
}

function submitString()
{
	var str="";
	str=consentIDArray[0];
	for(i=1;i<consentIDArray.length;i++)
	{
		if(consentIDArray[i]!=null)
		{
			str=str+","+consentIDArray[i];
		}
	}
	document.forms[0].stringOfResponseKeys.value=str;
	//document.forms[0].submit();
}


<%-- On calling this function all the response dropdown value set to "Withdraw" --%>
function withdrawAll(element)
{	
	var withdraw = "<%=form.getConsentTierMap()%>";
	for(var i=0;i<element;i++)
	{
		var withdrawId = withdraw.replace(/`/,i);
		var withdrawObject = document.getElementById(withdrawId);
		if(withdrawObject.options.length==1)
		{
			withdrawObject.selectedIndex=0; 
		}
		else
		{
			withdrawObject.selectedIndex=3; 
		}
	
	}
}

<%--Popup Window will open up on calling this function--%>	
function popupWindow(nofConsentTiers)
{
	<%
		List specimenList = (List)session.getAttribute(Constants.SPECIMEN_LIST);
	%>
	var withdraw = "<%=form.getConsentTierMap()%>";
	var iCount=nofConsentTiers;
	for(var i=0;i<nofConsentTiers;i++)
	{
		var withdrawId = withdraw.replace(/`/,i);
		var withdrawObject = document.getElementById(withdrawId);
		if(withdrawObject.value=="Withdrawn")
		{
			iCount--;
		}
	}	
	<%--When Withdraw All button is clicked--%>	
	if(iCount==0)
	{
		var url="pages/content/ConsentTracking/consentDialog.jsp?withrawall=true&response=withdraw&pageOf="+"<%=pageOf%>";
			window.open(url,'WithdrawAll','height=350,width=530,center=1,scrollbars=1,resizable=0');
		}
	else if(iCount==nofConsentTiers)
	{	
		
		<%
			if(pageOf.equals("pageOfConsent"))
			{
		%>
			document.forms[0].submit();
			self.close();
		<%
			}
		%>
		
		if(changeInStatus==false)
		{
			<%
				Object formInstance = form;
				if(formInstance instanceof NewSpecimenForm)
				{
			%>
					return onNormalSubmit();
			<%
				}
				else
			   {
			%>
					return <%=normalSubmit%>;
			<%
			   }
			%>
		}
		else
		{
			submitString();
			<%	
			if(specimenList!=null && specimenList.size()>1 || !(pageOf.equals("pageOfNewSpecimenCPQuery")))
			{
			%>	
		
				var url="pages/content/ConsentTracking/consentDialog.jsp?withrawall=true&response=nowithdraw&pageOf="+"<%=pageOf%>";
				window.open(url,'WithdrawAll','height=350,width=530,center=1,scrollbars=1,resizable=0');
			<%}
			else
			{%>
				document.forms[0].action="<%=Constants.CP_QUERY_SPECIMEN_EDIT_ACTION%>";
				document.forms[0].submit();
			<%}
			%>
		}
		
	}	
	else
	{
	if(changeInStatus==false)
		{
			<%
				Object formVar = form;
				if(formVar instanceof NewSpecimenForm)
				{
			%>
					return onNormalSubmit();
			<%
				}
				else if(formVar instanceof SpecimenCollectionGroupForm)
			   {
			%>
					return <%=normalSubmit%>;
			<%
			   }
			%>
		}
	   var url="pages/content/ConsentTracking/consentDialog.jsp?withrawall=false&response=withdraw&pageOf="+"<%=pageOf%>";
		window.open(url,'Withdraw','height=365,width=530,center=1,scrollbars=1,resizable=0');
		
	}
}	

</script>							

<%-- Set Variables according to the pages --%>
<%
	String collection="";
	String width="";
	String innerWidth="";
	
	if(pageOf!=null)
	{
		if(pageOf.equals("pageOfCollectionProtocolRegistration")||pageOf.equals("pageOfCollectionProtocolRegistrationCPQuery"))
		{
			width="81%";
			innerWidth="100%";
			collection="responseList";
		}
		else if(pageOf.equals("pageOfSpecimenCollectionGroupCPQuery")||pageOf.equals("pageOfSpecimenCollectionGroup"))
		{
			width="636px";
			innerWidth = "636px";
			collection="specimenCollectionGroupResponseList";
		}
        else if(pageOf.equals("pageOfNewSpecimen")||pageOf.equals("pageOfNewSpecimenCPQuery"))
		{
			width="100%";
			innerWidth="100%";
			collection="specimenResponseList";
     	}  
        else if(pageOf.equals("pageOfDistribution"))
		{
			width="100%";
			innerWidth="100%";
		}
		else if(pageOf.equals("pageOfOrdering"))
		{
			width="100%";
			innerWidth="100%";
		}
		if(pageOf.equals("pageOfConsent"))
		{
			width="100%";
			innerWidth="100%";
			collection="responseList";
		}
	}
	// Patch_Id: Improve_Space_Usability_On_Specimen_Page_2
	// variable to get the current display style of collect recieve event table
	String showHideStatus=request.getParameter("showHideStatus");
%>
	<%-- Main table Start --%>
	<table width="100%" border="0" cellpadding="3" cellspacing="0"   id="consentTabForSCG">
		<%--Title of the form i.e Consent Form --%>				
	
		<tr>
			<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">
				<%
				ConsentTierData consentTierForm =(ConsentTierData)form;
				List consentTierList=(List)consentTierForm.getConsentTiers();
				boolean withdrawAllDisabled=false;
				if(consentTierList==null||consentTierList.isEmpty())
				{
					consentTierList =new ArrayList();
					withdrawAllDisabled=true;
				}%>
				
				<div style="margin-top:2px;">
					<bean:message key="collectionprotocolregistration.consentform"/>
				</div>
			</span>
		  </td>
		</tr>
		
			<%-- If page of Collection Protocol Registration --%>						
			<%
			if(pageOf.equals("pageOfCollectionProtocolRegistration")||pageOf.equals("pageOfCollectionProtocolRegistrationCPQuery") || pageOf.equals("pageOfConsent"))
			{
			%>
			<tr>
				<td align="left" class="showhide"><table width="100%" border="0" cellspacing="0" cellpadding="3">
						<%--Signed URL --%>				
						<tr>
							<td class="noneditable" width="39%">
								&nbsp;&nbsp;&nbsp;<bean:message key="collectionprotocolregistration.signedurlconsent"/>
							</td>
							<td class="noneditable">
								<html:text styleClass="formFieldSized18" property="signedConsentUrl" />
							</td>
						</tr>
						<%--Witness Name --%>									
						<tr>
							<td class="noneditable">
								&nbsp;&nbsp;&nbsp;<bean:message key="collectionprotocolregistration.witnessname"/>
							</td>	
							<td class="noneditable">
								<html:select property="witnessId" styleClass="formFieldSized18" styleId="witnessId" size="1"
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" >
								<html:options collection="witnessList" labelProperty="name" property="value" />
								</html:select>
							</td>
						</tr>
						<%--Consent Date --%>									
						<tr>
							<td class="noneditable">
								&nbsp;&nbsp;&nbsp;<bean:message key="collectionprotocolregistration.consentdate"/>
							</td>	
							<td class="noneditable">
							<%
							if(pageOf.equals("pageOfConsent"))
							{
								if(signedConsentDate.trim().length() > 0)
								{
									Integer consentYear = new Integer(Utility.getYear(signedConsentDate ));
									Integer consentMonth = new Integer(Utility.getMonth(signedConsentDate ));
									Integer consentDay = new Integer(Utility.getDay(signedConsentDate ));
								%>
								<ncombo:DateTimeComponent name="consentDate"
									id="consentDate"
									formName="consentForm"	
									month= "<%=consentMonth %>"
									year= "<%=consentYear %>"
									day= "<%= consentDay %>" 
									value="<%=signedConsentDate %>"
									pattern="<%=Variables.dateFormat %>"
									styleClass="black_ar"
								/>		
								<% 
								}
								else
								{  
								%>
								<ncombo:DateTimeComponent name="consentDate"
									id="consentDate"
									formName="consentForm"	
									styleClass="black_ar" 
									pattern="<%=Variables.dateFormat %>"
								/>		
								<%
								}
							}
							else
							{
								if(signedConsentDate.trim().length() > 0)
								{
									Integer consentYear = new Integer(Utility.getYear(signedConsentDate ));
									Integer consentMonth = new Integer(Utility.getMonth(signedConsentDate ));
									Integer consentDay = new Integer(Utility.getDay(signedConsentDate ));
								%>
								<ncombo:DateTimeComponent name="consentDate"
									id="consentDate"
									formName="collectionProtocolRegistrationForm"	
									month= "<%=consentMonth %>"
									year= "<%=consentYear %>"
									day= "<%= consentDay %>" 
									pattern="<%=Variables.dateFormat %>"
									value="<%=signedConsentDate %>"
									
									styleClass="black_ar"
								/>		
								<% 
								}
								else
								{  
								%>
								<ncombo:DateTimeComponent name="consentDate"
									id="consentDate"
									formName="collectionProtocolRegistrationForm"	
									styleClass="black_ar" 
									pattern="<%=Variables.dateFormat %>"
								/>		
								<%
								}
							}
							%>
							<span class="grey_ar_s">
							<bean:message key="page.dateFormat" />&nbsp;
							</span>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<%-- If page of Specimen Collection Group or New Specimen --%>						
			<%
			}
			if(pageOf.equals("pageOfSpecimenCollectionGroupCPQuery")||pageOf.equals("pageOfNewSpecimen")
					||pageOf.equals("pageOfNewSpecimenCPQuery")||pageOf.equals("pageOfSpecimenCollectionGroup")||pageOf.equals("pageOfDistribution")
					||pageOf.equals("pageOfOrdering")
					)
			{
				Object formObject = form;
				String witnessName="";
				if (formObject instanceof SpecimenCollectionGroupForm)
				{
					witnessName=((SpecimenCollectionGroupForm)formObject).getWitnessName();
				}
				else if(formObject instanceof NewSpecimenForm)
				{
					witnessName=((NewSpecimenForm)formObject).getWitnessName();
				}
				else if(formObject instanceof DistributionForm)
				{
					witnessName=((DistributionForm)formObject).getWitnessName();
				}
				String consentDate=form.getConsentDate();		
				String signedUrl=form.getSignedConsentUrl();
				
			%>
			<%--Get Signed URL --%>	
			<tr>
				<td align="left" class="showhide" colspan="4"><table width="100%" border="0" cellspacing="0" id="consentFields" cellpadding="3">
					<%-- Inner table that will show Consents--%>
					
						<tr>
							<td width="30%" class="noneditable"><strong><bean:message key="collectionprotocolregistration.signedurlconsent"/></strong></td>

							<td class="noneditable">
								<label>
								<%
									if(signedUrl==null||signedUrl.equals(""))
									{
								%>
										&nbsp;
								<%
									}
									else
									{
								%>		
									<%=signedUrl%>
								<%
									}
								%>	
								</label>
							</td>
						</tr>
						<%--Get Witness Name --%>						
						<tr>
							<td class="noneditable"><strong><bean:message key="collectionprotocolregistration.witnessname"/>
							</strong></td>
							<td class="noneditable">
								<label >
									<%
									if(witnessName==null||witnessName.equals(""))
									{
									%>
										&nbsp;
									<%
									}
									else
									{
									%>		
										<%=witnessName%>
									<%
									}
									%>

								</label>
							</td>
						</tr>
						<%--Get Consent Date --%>														
						<tr>
							<td class="noneditable"><label for="User"><strong><bean:message key="collectionprotocolregistration.consentdate"/>
							</td>		
							<td class="noneditable">
								<label>
									<%
									if(consentDate==null||consentDate.equals(""))
									{
									%>
										&nbsp;
									<%
									}
									else
									{
									%>		
										<%=consentDate%>
									<%
									}
									%>
								</label>
							</td>
						</tr>
					</table>
				</td>
			</tr>			
			<%
			}
			%>				
			<tr>
				<td>
				<%-- Inner table that will show Consents Start--%>
				<div id="" style="overflow:auto;"> <table width="100%" border="0" cellspacing="0"         cellpadding="4">
						<%-- Serial No # --%>	
						<tr class="tableheading">
							<td width="8%" align="left" class="black_ar_b"><bean:message key="requestlist.dataTabel.serialNo.label" /></td>
							<%-- Title ( Consent Tiers) --%>									
							<td width="17%" align="left" nowrap="nowrap" class="black_ar_b">
									<bean:message key="collectionprotocolregistration.consentTiers" />
							</td>
							<%--Title (Participant response) --%>										
							<td width="24%" align="left" nowrap="nowrap" class="black_ar_b">
									<bean:message key="collectionprotocolregistration.participantResponses" />
							</td>
							
							<%
							if(pageOf.equals("pageOfSpecimenCollectionGroupCPQuery")||pageOf.equals("pageOfNewSpecimen")
									||pageOf.equals("pageOfNewSpecimenCPQuery")||pageOf.equals("pageOfSpecimenCollectionGroup")||pageOf.equals("pageOfDistribution")||pageOf.equals("pageOfOrdering"))
							{
							%>
							<%-- Title ( Response Status if page of SCG or New Specimen --%>									
							<td class="black_ar_b">
									<bean:message key="consent.responsestatus" />
							</td>
							<%
							}
							%>										
						</tr>
						<%-- Get Consents and Responses from DB --%>	
						<!-- Setting the local variable count for the row counting for setting row color-->
							<c:set var="count" value='1' scope="page"/>
						<%-- For loop Start --%>							
						<%	
						
						for(int counter=0;counter<consentTierList.size();counter++)
						{
							 String[] stringArray =	(String[])consentTierList.get(counter);
							 String responseKey=null;
							 String responseIdKey=null;
							 String consentIDKey = stringArray[0];
							 String consents = stringArray[1];
							 String participantResponseKey =stringArray[2];
							 String participantResponseIDKey=stringArray[3];
							 if(pageOf.equals("pageOfSpecimenCollectionGroupCPQuery")||pageOf.equals("pageOfNewSpecimen")
										||pageOf.equals("pageOfNewSpecimenCPQuery")||pageOf.equals("pageOfSpecimenCollectionGroup")||pageOf.equals("pageOfDistribution")||pageOf.equals("pageOfOrdering"))
							 {
								responseKey=stringArray[4];
								responseIdKey=stringArray[5];
							 }
							 String consentStatementKey ="ConsentBean:"+counter+"_statement";
							 String participantKey ="ConsentBean:"+counter+"_participantResponse";
							 String specimenKey ="ConsentBean:"+counter+"_specimenLevelResponse";
							 String scgIDKey ="ConsentBean:"+counter+"_specimenCollectionGroupLevelResponseID";
							 String specimenIDKey="ConsentBean:"+counter+"_specimenLevelResponseID";
							 String scgKey ="ConsentBean:"+counter+"_specimenCollectionGroupLevelResponse";
							 Object formObject = form;
							 String consentResponseDisplay="";
							 String responseDisplay="";
							 String specimenResponseDisplay="";
							 String idKey="";
							 String statusKey="";
							 String statusDisplay="";
							 if (formObject instanceof SpecimenCollectionGroupForm)
								{
									consentResponseDisplay=(String)((SpecimenCollectionGroupForm)formObject).getConsentResponseForScgValue(consentStatementKey);
									responseDisplay=(String)((SpecimenCollectionGroupForm)formObject).getConsentResponseForScgValue(participantKey);
									statusDisplay=(String)((SpecimenCollectionGroupForm)formObject).getConsentResponseForScgValue(scgKey);
									Object tmpID = ((SpecimenCollectionGroupForm)formObject).getConsentResponseForScgValue(scgIDKey);
									if(tmpID!=null)
									{
										idKey=tmpID.toString();
									}
								}
								else if(formObject instanceof CollectionProtocolRegistrationForm)
								{
									
									consentResponseDisplay=(String)((CollectionProtocolRegistrationForm)formObject).getConsentResponseValue(consentStatementKey);
									responseDisplay=(String)((CollectionProtocolRegistrationForm)formObject).getConsentResponseValue(participantKey);
								}
								else if(formObject instanceof NewSpecimenForm)
								{
									consentResponseDisplay=(String)((NewSpecimenForm)formObject).getConsentResponseForSpecimenValue(consentStatementKey);
									responseDisplay=(String)((NewSpecimenForm)formObject).getConsentResponseForSpecimenValue(participantKey);
									statusDisplay=(String)((NewSpecimenForm)formObject).getConsentResponseForSpecimenValue(specimenKey);
									Object tmporaryID=((NewSpecimenForm)formObject).getConsentResponseForSpecimenValue(specimenIDKey);
									if(tmporaryID!=null)
									{
										statusKey=tmporaryID.toString();
									}
								}
								else if(formObject instanceof DistributionForm)
								{
									consentResponseDisplay=(String)((DistributionForm)formObject).getConsentResponseForDistributionValue(consentStatementKey);
									responseDisplay=(String)((DistributionForm)formObject).getConsentResponseForDistributionValue(participantKey);
									specimenResponseDisplay=(String)((DistributionForm)formObject).getConsentResponseForDistributionValue(specimenKey);
								}
								else if(formObject instanceof ConsentResponseForm)
								{
									
									consentResponseDisplay=(String)((ConsentResponseForm)formObject).getConsentResponseValue(consentStatementKey);
									responseDisplay=(String)((ConsentResponseForm)formObject).getConsentResponseValue(participantKey);
								}
															
						%>		
						<%-- Serial No # --%>	
						<c:set var="style" value="black_ar" scope="page" />	
							<c:if test='${pageScope.count % 2 == 0}'>
								<c:set var="style" value="tabletd1" scope="page" />
							</c:if>
						<tr>
							<td align="left" class='${pageScope.style}'	 ><%=counter+1%>.</td>
							<%-- Get Consents # --%>										
							<td class='${pageScope.style}'>
							<html:hidden property="<%=consentIDKey%>"/>
							<html:hidden property="<%=consents%>"/>
							<%=consentResponseDisplay%>
							</td>
							<%-- If Page of Collection Protocol Reg then show drop down --%>										
							<%
							if(pageOf.equals("pageOfCollectionProtocolRegistration")||pageOf.equals("pageOfCollectionProtocolRegistrationCPQuery")  || pageOf.equals("pageOfConsent"))
							{
							%>
								<td class='${pageScope.style}'>
									<html:hidden property="<%=participantResponseIDKey%>"/>
									<html:select property="<%=participantResponseKey%>" styleClass="formFieldSized10" styleId="<%=participantResponseKey%>" size="1"
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
									<html:options collection="<%=collection%>" labelProperty="name" property="value"/>
									</html:select>
								</td>
							<%-- If Page of SCG or New Specimen or Distribution then show participant Response. --%>																			
							<%
							}
							else if(pageOf.equals("pageOfSpecimenCollectionGroupCPQuery")||pageOf.equals("pageOfNewSpecimen")
									||pageOf.equals("pageOfNewSpecimenCPQuery")||pageOf.equals("pageOfSpecimenCollectionGroup")||pageOf.equals("pageOfDistribution")||pageOf.equals("pageOfOrdering"))
							{
							%>
							<td class='${pageScope.style}'>
								<html:hidden property="<%=participantResponseIDKey%>"/>
								<html:hidden property="<%=participantResponseKey%>"/>
								<%=responseDisplay%>
							</td>
							<%-- If Page of SCG then show SCG level Response dropdown --%>																												
							<%
							}
							if(pageOf.equals("pageOfSpecimenCollectionGroupCPQuery")||pageOf.equals("pageOfSpecimenCollectionGroup"))
							{
								
								if(operation.equals(Constants.EDIT))
								{
									idKey ="changeInResponse('"+idKey+"')";
								}
								else
								{
									idKey=";";
								}

							%>
							<%
								if(operation.equals(Constants.EDIT)&&statusDisplay.equals(Constants.WITHDRAWN))
								{
							%>
								<td class='${pageScope.style}'>
								<html:select property="<%=responseKey%>" styleClass="formFieldSized10" styleId="<%=responseKey%>" size="1"
											onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
										<html:option value="Withdrawn"><bean:message key="consent.withdrawn" /></html:option>
								</html:select>
								</td>
							<%
								}
								else
								{
							%>
								<td class='${pageScope.style}'>
									<html:hidden property="<%=responseIdKey%>"/>
									<html:select property="<%=responseKey%>" styleClass="formFieldSized10" styleId="<%=responseKey%>" size="1"
										onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="<%=idKey%>">
										<html:options collection="<%=collection%>" labelProperty="name" property="value" />
									</html:select>
								</td>
							<%
								}
							%>
							<%-- If Page of New Specimen then show Specimen level Response dropdown --%>									
							<%
							}
							else if(pageOf.equals("pageOfNewSpecimen")||pageOf.equals("pageOfNewSpecimenCPQuery"))
							{
								String keyValue=";";
								if(operation.equals(Constants.EDIT))
								{
									keyValue="changeInResponse('"+statusKey+"')";
								}
					
							%>
							<%
								if(statusDisplay!=null&&operation.equals(Constants.EDIT)&&statusDisplay.equals(Constants.WITHDRAWN))
								{
							%>
								<td class='${pageScope.style}'>
								<html:select property="<%=responseKey%>" styleClass="formFieldSized10" styleId="<%=responseKey%>" size="1"
											onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
										<html:option value="Withdrawn"><bean:message key="consent.withdrawn" /></html:option>
								</html:select>
								</td>
							<%
								}
								else
								{
							%>
							<td class='${pageScope.style}'>
								<html:hidden property="<%=responseIdKey%>"/>
								<html:select property="<%=responseKey%>" styleClass="formFieldSized10" styleId="<%=responseKey%>" size="1"
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="<%=keyValue%>">
									<html:options collection="<%=collection%>" labelProperty="name" property="value" />
								</html:select>
							</td>
							<%
								}  
							%>
							<%-- If Page of Distribution then show Specimen Level response --%>																											
							<%
							}
							else if(pageOf.equals("pageOfDistribution") || pageOf.equals("pageOfOrdering"))
							{
							%>
							<td class='${pageScope.style}'>
								<html:hidden property="<%=responseIdKey%>"/>
								<html:hidden property="<%=responseKey%>"/>
								 <%=specimenResponseDisplay%>
							</td>
						</tr>	
						<c:set var="count" value='${pageScope.count+1}' scope="page" />
						<%
						}
							%>
								<%-- For loop Ends --%>						
						<% 
						}
						if(pageOf.equals("pageOfDistribution") ||pageOf.equals("pageOfOrdering"))
						{
						%>
						<%--Verification Message --%>													
						<tr>
							<%
								String verificationKeyValue = request.getParameter(Constants.STATUS);
								String status="";
								if(verificationKeyValue.equals(Constants.VERIFIED))
								{
									status="checked=checked";
								}
								else
								{
									status="";
								}
							%>
							<td class="black_ar">
								<input type="checkbox" name="verifyAllCheckBox" id="verifyAllCheckBox"
								<%=status%> />
							</td>
							<td class="black_ar" colspan="3">
								<label><b><bean:message key="consent.verificationmessage" /><b></label>
							</td>
						</tr>
						<%-- action button --%>																
						<!--<tr>
							<td class="buttonbg" align="left" colspan="4">
								<input type="button" name="doneButton" style="blue_ar_b" value="Done" onclick="submitAllResponses()"/>
							</td>
						</tr>-->
						<% 
						}%>
						
									
					   </table>	</div>
					<%-- Inner table that will show Consents--%>
				</td>	
			</tr>
			<tr>
			<td class="buttonbg" align="left">

			<%
			if(pageOf.equals("pageOfDistribution") ||pageOf.equals("pageOfOrdering"))
						{%>

						<input type="button" name="doneButton" class="blue_ar_b" value="Ok" onclick="submitAllResponses()"/>
						
					<%	}
			if(operation.equals(Constants.EDIT))
				{
				String str = "withdrawAll('"+ consentTierList.size()+"')";
				%>
					
						<html:button property="addButton" disabled="<%=withdrawAllDisabled%>" styleClass="blue_ar_c" onclick="<%=str%>" value="Withdraw All"/>&nbsp;
					
				<%
				}
				%>
				
			<% if(operation.equals(Constants.EDIT) && pageOf.equals("pageOfConsent") )	
		     	{
		     %>
		     	    &nbsp;|&nbsp;
		    <% 
		      }
		    %>
		     	 
				
			<%	if(pageOf.equals("pageOfConsent"))
						{
						%>
							<%-- action button --%>																
							
									<input type="button" name="doneConsentButton" class="blue_ar_c" value="Done" onclick="submitConsentResponses()"/>
							
						<% 
						}
						%>
				
				</td></tr>
	</table>
	<%-- Main table End --%>