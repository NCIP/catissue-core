<jsp:directive.page import="edu.wustl.catissuecore.domain.ConsentTier"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.*"%>



<!-- 
	 @author Virender Mehta 
	 @version 1.1	
	 Jsp name: ViewAllConsents.jsp
	 Company: Washington University, School of Medicine, St. Louis.
	 Description:This jsp will be shown when user clicks on View All link on Distribution page
-->						
<html>
	<head>
		<title>caTissue Core v 1.1</title>
			<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
			<script src="jss/script.js" type="text/javascript"></script>
			<script src="jss/overlib_mini.js" type="text/javascript"></script>
			<script language="JavaScript">

			//This function will check if the verifyCheck box is checked or not
			function submitAllResponses()
				{
			
					var addMore=parent.opener.document.getElementById("addMore");
					var tableRows=addMore.rows;
					var barcodelabelStatus;
					var parentId;
					var verificationStatusKey;
					<%
						List mapSize = (List)request.getAttribute("listOfMap");
						List listOfString = (List)request.getAttribute("listOfStringArray");
						for(int i=0;i<mapSize.size();i++)
						{
							String[] specimenValues=(String[])listOfString.get(i);				
							String labelBarcode=specimenValues[4];
					%>
						var checkboxKey="verifyAllCheckBox"+<%=i%>;
						var checkboxInstance=document.getElementById(checkboxKey);
						for(j=1;j<=tableRows.length;j++)
						{	
							var lable = "value(DistributedItem:" + j + "_Specimen_label)";
							var barcode = "value(DistributedItem:" + j + "_Specimen_barcode)";
							var lableBarcodeObj=parent.opener.document.getElementById(lable);
							if(lableBarcodeObj.value=="")
							{
								lableBarcodeObj=parent.opener.document.getElementById(barcode);
							}
							if(lableBarcodeObj.value=="<%=labelBarcode%>")
							{
								var barcodelabelStatus="barcodeStatus"+j;
								verificationStatusKey = "value(DistributedItem:"+j+"_verificationKey)";
								var parentId=parent.opener.document.getElementById(barcodelabelStatus);
								if(checkboxInstance.checked)
								{
									parentId.innerHTML="Verified"+"<input type='hidden' name='" + verificationStatusKey + "' value='Verified' id='" + verificationStatusKey + "'/>";
									break;
								}
								else
								{
									parentId.innerHTML="View"+"<input type='hidden' name='" + verificationStatusKey + "' value='View' id='" + verificationStatusKey + "'/>";
									break;
								}
							}
						}
					<%
					}
					%>
					window.close ();
				}
			</script>
	</head>
	<body>		
		<html:form action="Distribution.do">
			<%-- Set Variables according to the pages --%>
			<%-- Main table Start --%>
			<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" id="table4" >
			  	<%
				String verifiedRows=request.getParameter("verifiedRows");
				StringTokenizer stringToken = new StringTokenizer(verifiedRows,",");
				List verifiedList=new ArrayList();
				while (stringToken.hasMoreTokens()) 
				{
					String rowNo=stringToken.nextToken();
					verifiedList.add(rowNo);
				}
				System.out.println(verifiedList.size());
				List listOfStringArray = (List)request.getAttribute("listOfStringArray");
				List listOfMap = (List)request.getAttribute("listOfMap");
				for(int counter=0;counter<listOfMap.size();counter++)
				{
					String[] specimenAttributes=(String[])listOfStringArray.get(counter);				
					String witnessName=specimenAttributes[0];
					String consentDate=specimenAttributes[1];		
					String signedUrl=specimenAttributes[2];
					int consentTierCounter=Integer.parseInt(specimenAttributes[3]);
					String barcodeLabel=specimenAttributes[4];
					Map map =(Map)listOfMap.get(counter);
				%>
				<tr>
					<td class="formTitle">
						<bean:message key="collectionprotocolregistration.consentform"/>
					</td>
				</tr>
				<tr>
					<td colspan="4">
						<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%" id="table5" colspan="2" >
							<tr>
								<td class="tabrightmostcell" width="35%">
									&nbsp;&nbsp;&nbsp;
									<bean:message key="consent.barcodelable"/>
								</td>
								<td class="formField" >
									<label>
										<%
											if(barcodeLabel==null||barcodeLabel.equals(""))
											{
										%>
											&nbsp;
										<%
											}
											else
											{
										%>		
											<%=barcodeLabel%>
										<%
											}
										%>	
									</label>
							   </td>
							</tr>
							
							<tr>
								<td class="tabrightmostcell" width="35%">
									&nbsp;&nbsp;&nbsp;
									<bean:message key="collectionprotocolregistration.signedurlconsent"/>
								</td>
								<td class="formField" >
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
								<td class="tabrightmostcell">
									&nbsp;&nbsp;&nbsp;
									<bean:message key="collectionprotocolregistration.witnessname"/>
								</td>	
								<td class="formField">
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
								<td class="tabrightmostcell">
									&nbsp;&nbsp;&nbsp;
									<bean:message key="collectionprotocolregistration.consentdate"/>
								</td>		
								<td class="formField">
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
				<%-- Inner table that will show Consents Start--%>								
				<tr>
					<td>
						<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%" id="consentTable">
							<%-- Serial No # --%>	
							<tr>
								<td class="formLeftSubTableTitle">
									<div align="left">
										<bean:message key="requestlist.dataTabel.serialNo.label" />
									</div>
								</td>
								<%-- Title ( Consent Tiers) --%>									
								<td class="formLeftSubTableTitle">
									<div>	
										<bean:message key="collectionprotocolregistration.consentTiers" />
									</div>	
								</td>
								<%--Title (Participant response) --%>										
								<td  class="formLeftSubTableTitle">
									<div align="left">
										<bean:message key="collectionprotocolregistration.participantResponses" />
									<div>	
								</td>
								<td class="formLeftSubTableTitle">
									<div align="left">
										<bean:message key="consent.responsestatus" />
									</div>
								</td>
							</tr>
							<%-- Get Consents and Responses from DB --%>	
							<%-- For loop Start --%>							
							<%	
							for(int iCounter=0;iCounter<consentTierCounter;iCounter++)
							{
								 String idKey="ConsentBean:"+iCounter+"_consentTierID";
								 String statementKey="ConsentBean:"+iCounter+"_statement";
								 String responseKey="ConsentBean:"+iCounter+"_participantResponse";
								 String participantResponceIdKey="ConsentBean:"+iCounter+"_participantResponseID";
								 String specimenResponsekey  = "ConsentBean:"+iCounter+"_specimenLevelResponse";
								 String specimenResponseIDkey ="ConsentBean:"+iCounter+"_specimenLevelResponseID";
								 String consentDisplay=(String)map.get(statementKey);
								 String responseDisplay=(String)map.get(responseKey);
								 String specimenResponseDisplay=(String)map.get(specimenResponsekey);
								
							%>		
							<%-- Serial No # --%>										
							<tr>
								<td class="tabrightmostcell">
									<%=iCounter+1%>.
								</td>
								<%-- Get Consents # --%>										
								<td class="formField" width="31%">
									<%=consentDisplay%>
								</td>
								<td align="left" class="formField">
									<%=responseDisplay%>
								</td>
								<td align="left" class="formField">
									 <%=specimenResponseDisplay%>
								</td>
							</tr>	
							<%
							}
							%>
							<tr>
							<%
								String checkboxID="verifyAllCheckBox"+counter;
								if(verifiedList.size()!=0)
								{
									String iCount=""+counter;
									String status="";
									if(verifiedList.contains(iCount))
									{								
										status="checked=checked";
							%>
									
									<%	
									}
									else
									{
										status="";
									}
									%>
									<td class="tabrightmostcell">
											<input type="checkbox" name="verifyAllCheckBox" id="<%=checkboxID%>" <%=status%>
											 />
									</td>

							<%
								}
								else
								{
							%>
								
										<td class="tabrightmostcell">
											<input type="checkbox" name="verifyAllCheckBox" id="<%=checkboxID%>"/>
										</td>
							<%
								}
							%>
								<td class="formField" colspan="3">
									<label><b><bean:message key="consent.verificationmessage" /><b></label>
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
						</table>	
						<%-- Inner table that will show Consents--%>
					</td>	
				</tr>
				<%
				}
				%>
				<%-- action button --%>																
				<tr>
					<td class="tabrightmostcell" align="right" colspan="4">
						<input type="button" name="doneButton" style="actionButton" value="Done" onclick="submitAllResponses()"/>
					</td>
				</tr>
			
			</table>
			<%-- Main table End --%>
		</html:form>
	</body>
</html>