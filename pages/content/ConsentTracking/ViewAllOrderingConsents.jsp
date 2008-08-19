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
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
			
<html>
	<head>
		<title>caTissue Core v 1.1</title>
			<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />
			<script src="jss/script.js" type="text/javascript"></script>
			<script src="jss/overlib_mini.js" type="text/javascript"></script>
			<script language="JavaScript">
			function cancelWindow()
			{
			  parent.allConsentWindow.hide();
			}
			//This function will check if the verifyCheck box is checked or not
			function submitAllResponses()
				{
			
					
					var barcodelabelStatus;
					var parentId;
					var rowCount;
					var verificationStatusKey;
					<%
						String index="";
						String noOfRows = (String)request.getParameter("noOfRows");
						String labelIndexValues = (String)request.getAttribute("labelIndexCount");
						StringTokenizer stringTokenForIndex = new StringTokenizer(labelIndexValues,"|");
						int rowCount = Integer.parseInt(noOfRows);
						List mapSize = (List)request.getAttribute("listOfMap");
						if(mapSize.size()!=0)
					    {
														
						int i=0;
						while(stringTokenForIndex.hasMoreTokens()) 
						{
							index=stringTokenForIndex.nextToken();	
					%>
						var checkboxKey="verifyAllCheckBox"+<%=i%>;
						var checkboxInstance=document.getElementById(checkboxKey);
						var indexval="<%=index%>";		
								
						var labelStatus="labelStatus"+indexval;
						consentVerificationkey = "value(RequestDetailsBean:"+indexval+"_consentVerificationkey)";
						var parentId=window.parent.document.getElementById(labelStatus);
						var rowstatus= "value(RequestDetailsBean:"+indexval+"_rowStatuskey)";
						var statusValue = window.parent.document.getElementById(rowstatus).value;
															
						if(statusValue!="disable")
						{
								
								if(checkboxInstance.checked) 
								{
									parentId.innerHTML="Verified"+"<input type='hidden' name='" + consentVerificationkey + "' value='Verified' id='" + consentVerificationkey + "'/>";
								}
								else
								{
									parentId.innerHTML="View"+"<input type='hidden' name='" + consentVerificationkey + "' value='View' id='" + consentVerificationkey + "'/>";
								}
						 }
							
						
						<%
							i=i+1;
						}
					
					}
				%>
					
					parent.allConsentWindow.hide();
				}
			</script>
	</head>
	<body>		
			<%-- Set Variables according to the pages --%>
			<%-- Main table Start --%>
			<table width="100%" border="0" cellpadding="3" cellspacing="0" class="maintable" >
			<tr>
			<td>
			<table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
			<tr>
			<td>
			<div style="height:250px; background-color: #ffffff;overflow: auto;">
		<!--	<div width="600px" height="100" style="height:50px;oveflow:auto;display:block" id="consent">-->
			
			<table width="100%" border="0" cellpadding="3" cellspacing="0" id="table4" >
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
					<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">
						<bean:message key="collectionprotocolregistration.consentform"/>
						</span>
					</td>
				</tr>
				<tr>
					<td class="showhide">
						<table cellpadding="3" cellspacing="0" border="0" width="100%" id="table5">
							<tr>
								<td class="noneditable" width="35%">
									&nbsp;&nbsp;&nbsp;
									<bean:message key="consent.barcodelable"/>
								</td>
								<td class="noneditable" >
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
								<td class="noneditable" width="35%">
									&nbsp;&nbsp;&nbsp;
									<bean:message key="collectionprotocolregistration.signedurlconsent"/>
								</td>
								<td class="noneditable" >
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
								<td class="noneditable">
									&nbsp;&nbsp;&nbsp;
									<bean:message key="collectionprotocolregistration.witnessname"/>
								</td>	
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
								<td class="noneditable">
									&nbsp;&nbsp;&nbsp;
									<bean:message key="collectionprotocolregistration.consentdate"/>
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
				<%-- Inner table that will show Consents Start--%>								
				<tr>
					<td>
						<table cellpadding="3" cellspacing="0" border="0" width="100%" id="consentTable">
							<%-- Serial No # --%>	
							<tr>
								<td class="tableheading">
									<div align="left">
										<bean:message key="requestlist.dataTabel.serialNo.label" />
									</div>
								</td>
								<%-- Title ( Consent Tiers) --%>									
								<td class="tableheading">
									<div>	
										<bean:message key="collectionprotocolregistration.consentTiers" />
									</div>	
								</td>
								<%--Title (Participant response) --%>										
								<td  class="tableheading">
									<div align="left">
										<bean:message key="collectionprotocolregistration.participantResponses" />
									<div>	
								</td>
								<td class="tableheading">
									<div align="left">
										<bean:message key="consent.responsestatus" />
									</div>
								</td>
							</tr>
							<%-- Get Consents and Responses from DB --%>	
							<!-- Setting the local variable count for the row counting for setting row color-->
							<c:set var="count" value='1' scope="page"/>
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
							<c:set var="style" value="black_ar" scope="page" />	
							<c:if test='${pageScope.count % 2 == 0}'>
						<c:set var="style" value="tabletd1" scope="page" />
					</c:if>
							<%-- Serial No # --%>										
							<tr>

								<td class='${pageScope.style}'>
									<%=iCounter+1%>.
								</td>
								<%-- Get Consents # --%>										
								<td class='${pageScope.style}' width="31%">
									<%=consentDisplay%>
								</td>
								<td align="left" class='${pageScope.style}'>
									<%=responseDisplay%>
								</td>
								<td align="left" class='${pageScope.style}'>
									 <%=specimenResponseDisplay%>
								</td>
							</tr>
							<c:set var="count" value='${pageScope.count+1}' scope="page" />
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
								<td class="black_ar" colspan="3">
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
				</table>
				
				</div>
				</td>
				</tr>
				<tr>
					<td class="buttonbg" align="left" >
						<input type="button" name="doneButton" class="blue_ar_b" value="Ok" onclick="submitAllResponses()"/>
						&nbsp;|&nbsp;
						<html:link href="#" styleClass="cancellink" onclick= "cancelWindow();">
													<bean:message key="buttons.cancel" />
												</html:link>
					</td>
				</tr>
			
			</table>
			</td>
			</tr>
			</table>
			<%-- Main table End --%>
	</body>
</html>