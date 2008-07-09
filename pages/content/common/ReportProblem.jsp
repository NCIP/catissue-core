<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Variables,edu.wustl.catissuecore.util.global.Constants"%>
<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
		String formName,prevPage=null,nextPage=null;
		boolean readOnlyValue;
		if (operation.equals(Constants.EDIT))
        {
            formName = Constants.REPORTED_PROBLEM_EDIT_ACTION;
			Long identifier = (Long)request.getAttribute(Constants.PREVIOUS_PAGE);
			prevPage = Constants.PROBLEM_DETAILS_ACTION+"?"+Constants.SYSTEM_IDENTIFIER+"="+identifier;
			identifier = (Long)request.getAttribute(Constants.NEXT_PAGE);
			nextPage = Constants.PROBLEM_DETAILS_ACTION+"?"+Constants.SYSTEM_IDENTIFIER+"="+identifier;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.REPORTED_PROBLEM_ADD_ACTION;
            readOnlyValue = false;
        }
%>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>

	<html:errors/>
	<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
		<%=messageKey%>
	</html:messages>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
	<html:form action="<%=formName%>">	
		<tr>
		    <td class="td_color_bfdcf3">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
					<tr>
					<td width="100%" colspan="2" valign="top">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="100%" height="25" colspan="3" valign="top" class="td_color_bfdcf3">
									<table width="18%" border="0" cellpadding="0" cellspacing="0" background="images/uIEnhancementImages/table_title_bg.gif">
										<tr>
											<td width="80%"><span class="wh_ar_b">&nbsp;&nbsp;&nbsp;
												<bean:message key="ReportedProblems.header" /> </span></td>
											<td width="20%" align="right">
												<img src="images/uIEnhancementImages/table_title_corner2.gif"
												width="31" height="24" /></td>
										</tr>
									</table>
								</td>
				            </tr>
						</table>
					</td>
					</tr>
					
					<tr>
				        <td colspan="2" valign="top" class="td_color_bfdcf3" style="padding-left:10px; 
							padding-right:10px; padding-bottom:10px;">
							<table width="100%" border="0" cellpadding="3" cellspacing="0" bgcolor="#FFFFFF">
					            <tr>
									<td height="15" colspan="3" align="left" class="td_tab_bg">&nbsp;</td>							  	
					            </tr>
								<tr>
									<td align = "left" class=" grey_ar_s">
										<img src="images/uIEnhancementImages/star.gif" alt="Mandatory"
											width="6" height="6" hspace="0" vspace="0" /> 
											<bean:message key="commonRequiredField.message" />
									</td>
									<td height="15" colspan="3" align="left" ></td>
								</tr>
								<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
								<tr>
								<% 
									String backPage = Constants.REPORTED_PROBLEM_SHOW_ACTION+"?"+Constants.PAGE_NUMBER+"="+Constants.START_PAGE; 
								%>								
									<td align = "right" colspan = "2">
										<span class="smalllink">
											<a	class="blue_ar_s_b" href="<%=backPage%>">
												<bean:message key="reportedProblem.home" />
											</a>
										</span>&nbsp;
									</td>
									<td align="left">
										<logic:notEmpty name="prevpage">
											|&nbsp;
												<span class="smalllink"> 
													<a	class="blue_ar_s_b" href="<%=prevPage%>">
														<bean:message key="approveUser.previous" />
													</a>
												</span>&nbsp;								
										</logic:notEmpty>

										<logic:notEmpty name="nextPage">
											|&nbsp; 
												<span class="smalllink"> 
													<a	class="blue_ar_s_b" href="<%=nextPage%>">
														<bean:message key="approveUser.next" />
													</a>
												</span>&nbsp;								
										</logic:notEmpty>
									</td>
								</tr>
								</logic:equal>
			 
						        <tr>
							        <td width="42%" height="25" colspan="2" align="left" class="tr_bg_blue1">
										<span class="blue_ar_b">&nbsp;
											<bean:message key="reportProblem.title"/>&nbsp;&nbsp;
										</span>
									</td>
						            <td width="61%" height="25" align="left" class="tr_bg_blue1">&nbsp;</td>
						        </tr>
								<html:hidden property="operation" value="<%=operation%>" />
								<html:hidden property="id" />
								<tr>
									<td colspan="3" align="left" valign="top" style="padding-top:10px;
									padding-bottom:15px; padding-left:6px;">
										<table width="100%" border="0" cellpadding="3" cellspacing="0">
											<tr>
												<td width="1%" height="25" align="left" class="black_ar">
													<span class="blue_ar_b">
														<img src="images/uIEnhancementImages/star.gif" alt="Mandatory"
															width="6" height="6" hspace="0" vspace="0" />
													</span>
												</td>
												<td width="16%" align="left" class="black_ar">
													<label for="site">
														<bean:message key="fields.from" /> 
													</label>
												</td>
												<td width="82%" align="left" valign="middle">
													<span class="black_ar">
														<html:text  styleClass="black_ar" maxlength="255" size="30"
															styleId="from" property="from"	readonly="<%=readOnlyValue%>" />
								                    </span>
												</td>
											</tr>
							                <tr>
							                    <td width="1%" height="25" align="left" class="black_ar">
													<span class="blue_ar_b">
														<img src="images/uIEnhancementImages/star.gif" alt="Mandatory"
															width="6" height="6" hspace="0" vspace="0" />
													</span>
												</td>
												<td width="16%" align="left" class="black_ar">
													<label for="site">
														<bean:message key="fields.nameofreporter" /> 
													</label>
												</td>
												<td width="82%" align="left" valign="middle">
													<span class="black_ar">
									                    <html:text  styleClass="black_ar" maxlength="255" size="30"
															styleId="nameOfReporter" property="nameOfReporter"	readonly="<%=readOnlyValue%>" />
								                    </span>  
												</td>
											</tr>
							                <tr>
							                    <td width="1%" height="25" align="left" class="black_ar">
													<span class="blue_ar_b">
														<img src="images/uIEnhancementImages/star.gif" alt="Mandatory"
															width="6" height="6" hspace="0" vspace="0" />
													</span>
												</td>		
												<td width="16%" align="left" class="black_ar">
													<label for="site">
														<bean:message key="fields.affiliation" /> 
													</label>
												</td>
									            <td align="left" valign="middle">
													<span class="black_ar">
														<html:text  styleClass="black_ar" maxlength="255" size="30"
															styleId="affiliation" property="affiliation"
															readonly="<%=readOnlyValue%>" />
									                </span> 
												</td>
											</tr>
											<tr>
							                    <td width="1%" height="25" align="left" class="black_ar">
													<span class="blue_ar_b">
														<img src="images/uIEnhancementImages/star.gif" alt="Mandatory"
															width="6" height="6" hspace="0" vspace="0" />
													</span>
												</td>
												<td width="16%" align="left" class="black_ar">
													<label for="site">
														<bean:message key="fields.title" /> 
													</label>
												</td>
								                <td align="left" valign="middle">
													<span class="black_ar">
														<html:text  styleClass="black_ar" maxlength="255" size="30"
															styleId="subject" property="subject"
															readonly="<%=readOnlyValue%>" />
												    </span>  
												</td>
											</tr>
											<tr>
												<td width="1%" height="25" align="left" class="black_ar">
													<span class="blue_ar_b">
														<img src="images/uIEnhancementImages/star.gif" alt="Mandatory"
															width="6" height="6" hspace="0" vspace="0" />
													</span>
												</td>
												<td width="16%" align="left" class="black_ar">
													<label for="site">
														<bean:message key="fields.message" /> 
													</label>
												</td>
							                    <td align="left" valign="middle">
													<span class="black_ar">
														<html:textarea styleClass="formFieldSizedNew" cols="34" rows="5"
														styleId="messageBody" property="messageBody" readonly="<%=readOnlyValue%>" />
													</span>
												</td>
											</tr>
											<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
											<tr>
							                    <td align="left" class="black_ar">
													<span class="blue_ar_b">
														<img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" />
													</span>
												</td>
												<td align="left" class="black_ar">
													<label	for="comments"> 
														<bean:message key="reportProblem.status" />
													</label>
												</td>
												<td colspan="4" align="left" valign="top">
													<label>
														<html:select property="activityStatus" styleClass="formFieldSizedNew"
															styleId="activityStatus" size="1"	onmouseover="showTip(this.id)"
															onmouseout="hideTip(this.id)">
															<html:options name="activityStatusList" labelName="activityStatusList" />
														</html:select>
													</label>
												</td>                 
							                </tr>
									        <tr>
												<td height="25" align="left" class="black_ar">&nbsp;</td>
													<td align="left" class="black_ar">
													<label	for="comments"> 
														<bean:message key="approveUser.comments" />
													</label>
												</td>
												<td colspan="4" align="left" valign="top">
													<label>
														<html:textarea styleClass="formFieldSizedNew" cols="34" rows="5"
														styleId="comments" property="comments" />
													</label>
												</td>                 
							                </tr>
											</logic:equal>
										</table>
									</td>
								</tr>
								<tr  class="td_color_F7F7F7">
									<td colspan="4" class="buttonbg">
										<html:submit styleClass="blue_ar_b">
											<bean:message key="buttons.submit" />
										</html:submit> &nbsp;| 
										<span class="cancellink">
											<html:link	page="/ManageAdministrativeData.do" styleClass="blue_ar_s_b">
												<bean:message key="buttons.cancel" />
											</html:link>
										</span>
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