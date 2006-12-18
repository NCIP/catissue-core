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
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
<html:errors/>
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
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%">
<tr>
	<td>
		<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
			<tr>
				<td class="formMessage" colspan="3">* indicates a required field</td>
			</tr>
			
			<tr>
				<td class="formTitle" height="20" colspan="5">
				<logic:equal name="operation" value="<%=Constants.ADD%>">
					<bean:message key="arrayType.title"/>
				</logic:equal>
				<logic:equal name="operation" value="<%=Constants.EDIT%>">
					<bean:message key="arrayType.editTitle"/>
				</logic:equal>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">*</td>
				<td class="formRequiredLabel">
					<label for="name">
						<bean:message key="arrayType.name" />
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:text styleClass="formFieldSized10"  maxlength="255"  size="30" styleId="name" property="name"/>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">*</td>
				<td class="formRequiredLabel">
					<label for="specimenClass">
						<bean:message key="arrayType.specimenClass" />
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:select property="specimenClass" styleClass="formFieldSized" styleId="className" size="1" onchange="onClassChange(this)">
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
					</html:select>
				</td>
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
				<td class="formRequiredNotice" width="5">*</td>
				<td class="formRequiredLabel">
					<label for="specimenType">
						<bean:message key="arrayType.specimenType" />
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:select property="specimenTypes" styleClass="formFieldSized" styleId="type" size="4" multiple="true">
						<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
					</html:select>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">&nbsp;</td>
				<td class="formLabel" width="140">
					<label for="comments">
						<bean:message key="arrayType.comments"/>
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:textarea styleClass="formFieldSized" rows="3" styleId="comment" property="comment"/>
				</td>
			</tr>
			
			<tr>
				<td class="formTitle" colspan="5">
					<label for="capacity">
						<bean:message key="arrayType.capacity" />
					</label>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">*</td>
				<td class="formRequiredLabel">
					<label for="oneDimensionCapacity">
						<bean:message key="arrayType.oneDimensionCapacity" />
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:text styleClass="formFieldSized10" maxlength="10"  size="30" styleId="oneDimensionCapacity" property="oneDimensionCapacity"/>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">&nbsp;</td>
				<td class="formLabel">
					<label for="twoDimensionCapacity">
						<bean:message key="arrayType.twoDimensionCapacity" />
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:text styleClass="formFieldSized10" maxlength="10"  size="30" styleId="twoDimensionCapacity" property="twoDimensionCapacity"/>
				</td>
			</tr>
			
			<tr>
				<td align="right" colspan="3">
				<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
							<td>
								<html:submit styleClass="actionButton">
									<bean:message  key="buttons.submit" />
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
</table>
</html:form>