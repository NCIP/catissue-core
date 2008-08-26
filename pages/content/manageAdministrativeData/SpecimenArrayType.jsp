<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.actionForm.SpecimenArrayTypeForm"%>
<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="java.util.HashMap"%>

<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
        String formName;
//		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);

//        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.SPECIMENARRAYTYPE_EDIT_ACTION;
//            readOnlyValue = false;
        }
        else
        {
            formName = Constants.SPECIMENARRAYTYPE_ADD_ACTION;
//            readOnlyValue = false;
        }
        %>
<script type="text/javascript">

<%
	List specimenClassList = (List) request.getAttribute(Constants.SPECIMEN_CLASS_LIST);
	HashMap specimenTypeMap = (HashMap) request.getAttribute(Constants.SPECIMEN_TYPE_MAP);
	SpecimenArrayTypeForm form = (SpecimenArrayTypeForm)request.getAttribute("arrayTypeForm");
//	String operation = (String)request.getAttribute(Constants.OPERATION);
	System.out.println(" Form ::  " + form);
%>

<%
	int classCount=0;
	for(classCount=1;classCount<specimenClassList.size();classCount++)
	{
		String keyObj = (String)((NameValueBean)specimenClassList.get(classCount)).getName() ;
		List subList = (List)specimenTypeMap.get(keyObj);
		String arrayData = "";
		for(int listSize=0;listSize<subList.size();listSize++ )
		{
			if(listSize == subList.size()-1 )
				arrayData = arrayData + "\"" + ((NameValueBean)subList.get(listSize)).getName() + "\"";
			else
    			arrayData = arrayData + "\"" + ((NameValueBean)subList.get(listSize)).getName() + "\",";   
		}
%>
		var <%=keyObj%>Array = new Array(<%=arrayData%>);
<%	    		
	}
%>

		function typeChange(specArrayTypeArr)
		{ 
			var specimenTypeCombo = "type";
			ele = document.getElementById(specimenTypeCombo);
			//To Clear the Combo Box
			ele.options.length = 0;

		    if (specArrayTypeArr != null) {
				specArrayTypeArr.sort();
				//ele.options[0] = new Option('-- Select --','-1');
				var j=0;
				//Populating the corresponding Combo Box
				for(i=0;i<specArrayTypeArr.length;i++)
				{
						ele.options[j++] = new Option(specArrayTypeArr[i],specArrayTypeArr[i]);
				}
		    } else {
				ele.options[0] = new Option('-- Select --','-1');
			}
		}
			
		function onClassChange(element)
		{
			
			if(element.value == "Tissue")
			{
				typeChange(TissueArray);
			}
			else if(element.value == "Fluid")
			{
				typeChange(FluidArray);
			}
			else if(element.value == "Cell")
			{
				typeChange(CellArray);
			}
			else if(element.value == "Molecular")
			{
				typeChange(MolecularArray);
			} else {
				typeChange(null);	
			}
		}
		
		
</script>

<html:form action="<%=formName%>">
	<html:hidden property="operation" value="<%=operation%>" />
	<html:hidden property="id" />
	<html:hidden property="submittedFor" value="<%=submittedFor%>"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key="SpecimenArrayType.header" /></span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Specimen Array Type"  width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_tab_bg" ><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
		<%
						if(operation.equals(Constants.ADD))
						{ 
		%>
        <td valign="bottom"><img src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add" width="57" height="22" /></td>
        <td valign="bottom"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfSpecimenArrayType&amp;aliasName=SpecimenArrayType"><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit" width="59" height="22" border="0" /></html:link></td>
		<% 
						}
						   if(operation.equals(Constants.EDIT))
						{
					%>
		<td valign="bottom"><html:link page="/SpecimenArrayType.do?operation=add&amp;pageOf=pageOfSpecimenArrayType"><img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22" /></html:link></td>
        <td valign="bottom"><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit" width="59" height="22" border="0" /></td>
		<%
							}
						%>

        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
        <tr>
          <td colspan="2" align="left"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
        </tr>
        <tr>
          <td colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<logic:equal name="operation" value="<%=Constants.ADD%>"><bean:message key="arrayType.title"/></logic:equal><logic:equal name="operation" value="<%=Constants.EDIT%>"><bean:message key="arrayType.editTitle"/></logic:equal></span></td>        </tr>
        <tr>
          <td colspan="2" align="left" class="showhide"><table width="100%" border="0" cellpadding="3" cellspacing="0">
              <form id="form" name="form2" method="post" action="">
                <tr>
                  <td width="1%" align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td width="16%" align="left" class="black_ar"><label for="name">
									<bean:message key="arrayType.name" />
								</label> </td>
                  <td width="83%" align="left">
                    <html:text styleClass="black_ar"  maxlength="255"  size="30" styleId="name" property="name"/>
                  </td>
                </tr>
                <tr>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><bean:message key="arrayType.specimenClass" /></td>
                  <td align="left" class="black_new">
								<html:select property="specimenClass" styleClass="formFieldSizedNew" styleId="className" size="1" onchange="onClassChange(this)">
									<%
										String classValue = form.getSpecimenClass();
										if((operation != null) && (operation.equals(Constants.EDIT)))
										{
									%>
										<html:option value="<%=classValue%>"><%=classValue%></html:option>
									<%
										}else{
									%>
										<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
									<%
										}
									%>
								</html:select></td>
                </tr>
				<%
								String classValue = (String)form.getSpecimenClass();
								List specimenTypeList = (List)specimenTypeMap.get(classValue);
								
								if(specimenTypeList == null)
								{
									specimenTypeList = new ArrayList();
									specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
								}
								pageContext.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
							%>
                <tr>
                  <td align="center" class="black_ar_t" ><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="3" /></td>
                  <td align="left" class="black_ar_t"><label for="specimenType">
									<bean:message key="arrayType.specimenType" />
								</label></td>
                  <td align="left" class="black_new">
								<html:select property="specimenTypes" styleClass="formFieldSizedNew" styleId="type" size="4" multiple="true">
									<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
								</html:select></td>
                </tr>
                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" valign="top" class="black_ar_t"><label for="comments">
									<bean:message key="arrayType.comments"/>
								</label> </td>
                  <td align="left"><html:textarea styleClass="black_ar_s" cols="80" rows="3" styleId="comment" property="comment"/></td>
                </tr>
              
          </table></td>
        </tr>
        <tr onclick="javascript:showHide('add_dimension')">
          <td width="96%" align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message
										key="arrayType.capacity" /></span></td>
          <td width="4%" align="right" class="tr_bg_blue1"><a id="imgArrow_add_dimension"><img
											src="images/uIEnhancementImages/up_arrow.gif" width="80" height="9" hspace="10" border="0"/></a></td>
        </tr>
        <tr>
          <td colspan="2" valign="top" class="showhide1"><div id="add_dimension" style="display:block" >
              <table width="100%" border="0" cellpadding="3" cellspacing="0">
                <tr>
                  <td width="1%" align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td width="16%" align="left" class="black_ar"><label for="oneDimensionCapacity">
								<bean:message key="arrayType.oneDimensionCapacity" />
							</label></td>
                  <td width="83%" colspan="4" align="left" valign="top"><html:text styleClass="black_ar" maxlength="10"  size="20" styleId="oneDimensionCapacity" property="oneDimensionCapacity" style="text-align:right"/></td>
                </tr>
                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" class="black_ar"><label for="twoDimensionCapacity">
								<bean:message key="arrayType.twoDimensionCapacity" />
							</label></td>
                  <td colspan="4" align="left" valign="top"><html:text styleClass="black_ar" maxlength="10"  size="20" styleId="twoDimensionCapacity" property="twoDimensionCapacity" style="text-align:right"/></td>
                </tr>
              </table>
          </div></td>
        </tr>
        <tr >
          <td colspan="2" class="bottomtd"></td>
        </tr>
        <tr  >
          <td colspan="2" class="buttonbg"><html:submit styleClass="blue_ar_b">
						<bean:message  key="buttons.submit" />
					</html:submit>
            &nbsp;| <html:link
										page="/ManageAdministrativeData.do" styleClass="cancellink">
										<bean:message key="buttons.cancel" />
									</html:link></td>
        </tr>
      </table></td>
  </tr>
</table>
</html:form>