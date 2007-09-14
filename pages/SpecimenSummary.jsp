<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<html>
		<html:form action="SubmitSpecimenCollectionProtocol.do">		
		<table summary="" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="dataTablePrimaryLabel" height="20">
						Specimen Requirement
					</td>
				</tr>
		<logic:empty name="viewSpecimenSummaryForm" property="specimenList" >
			<tr>
				<td class="dataTableWhiteCenterHeader" colspan="9">  
									No specimens to display for current action!!
								</td>
			</tr>		
		</logic:empty>
		<logic:notEmpty name="viewSpecimenSummaryForm" property="specimenList" >	
			<tr>	
			   <td>
				<table summary="" cellpadding="3"
								cellspacing="0" border="0" class="dataTable" width="100%">
					<tr>
						<th class="formSerialNumberLabelForTable" scope="col" > &nbsp </th>
						<th class="formSerialNumberLabelForTable" scope="col">Label</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Class</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Type</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Tissue Site</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Tissue Side</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Pathological Status</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Storage</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Qty</th>
						
					</tr>
				
				  <logic:iterate name="viewSpecimenSummaryForm" property="specimenList" id="specimen" indexId="counter">
					<tr>
						<td> <html:radio property="selectedSpecimenId" value="uniqueIdentifier" idName="specimen" 
						onclick=" form.action='GenericSpecimenSummary.do'; submit()"/> </td>
						<td class="dataCellText" > <bean:write name="specimen" property="displayName" />
						<html:hidden name="specimen" indexed="true" property="displayName" /></td>
						<td class="dataCellText"> <bean:write name="specimen" property="className" />
						<html:hidden name="specimen" indexed="true" property="className" /></td>
						<td class="dataCellText"> <bean:write name="specimen" property="type" />
						<html:hidden name="specimen" indexed="true" property="type" /></td>
						<td class="dataCellText"> <bean:write name="specimen" property="tissueSite" />
						<html:hidden name="specimen" indexed="true" property="tissueSite" /></td>
						<td class="dataCellText"> <bean:write name="specimen" property="tissueSide" />
						 <html:hidden name="specimen" indexed="true" property="tissueSide" /></td>
						<td class="dataCellText"> <bean:write name="specimen" property="pathologicalStatus" />
						<html:hidden name="specimen" indexed="true" property="pathologicalStatus" /></td>

						<logic:empty name="viewSpecimenSummaryForm" property="eventId">
							<logic:equal name="specimen" property="storageContainerForSpecimen" value="Virtual">
							
								<td class="dataCellText"> <bean:write name="specimen" property="storageContainerForSpecimen" />
								<html:hidden name="specimen" indexed="true" property="storageContainerForSpecimen" /></td>
							</logic:equal>
							
							<logic:notEqual name="specimen" property="storageContainerForSpecimen" value="Virtual">
								<td class="dataCellText"> <html:text size="10" name="specimen" indexed="true" property="storageContainerForSpecimen" /></td>
							</logic:notEqual>
						</logic:empty>
						<logic:notEmpty name="viewSpecimenSummaryForm" property="eventId">
								<td class="dataCellText"> <bean:write name="specimen" property="storageContainerForSpecimen" />
								<html:hidden name="specimen" indexed="true" property="storageContainerForSpecimen" /></td>
						</logic:notEmpty>
												
						<td class="dataCellText"> <bean:write name="specimen" property="quantity" />
						<html:hidden name="specimen" indexed="true" property="quantity" />
						</td>
						
					</tr>
				  </logic:iterate>	
				</table>
				</td>
			</tr>
		</logic:notEmpty>
		<tr>
		
			<logic:notEmpty name="viewSpecimenSummaryForm" property="eventId">
				<html:hidden property="eventId"  />
			</logic:notEmpty>
			
			<td class="dataTablePrimaryLabel" height="20">

			<logic:notEmpty name="viewSpecimenSummaryForm" property="aliquotList" >
				<p>Aliquot details
		</td>
		</tr>
		<tr>
		<td>
			<table summary="" cellpadding="3"
							cellspacing="0" border="0" class="dataTable" width="100%">
				<tr>	
					<th class="formSerialNumberLabelForTable" scope="col">Parent</th>
					<th class="formSerialNumberLabelForTable" scope="col">Label</th>
					<th class="formSerialNumberLabelForTable" scope="col"> Storage</th>
					<th class="formSerialNumberLabelForTable" scope="col"> Qty</th>
					</tr>
				  <logic:iterate name="viewSpecimenSummaryForm" property="aliquotList" id="aliquot">
					<tr>
						<td > <bean:write name="aliquot" property="parentName" /></td>		      		
						<td > <bean:write name="aliquot" property="displayName" /></td>
						<td > <bean:write name="aliquot" property="storageContainerForSpecimen" /></td>
						<td > <bean:write name="aliquot" property="quantity" /></td>
					</tr>
				  </logic:iterate>	
				</table>
			</logic:notEmpty>		
		 </td>
		</tr>
		<tr>
		 
		<logic:notEmpty name="viewSpecimenSummaryForm" property="derivedList" >		
		<td class="dataTablePrimaryLabel" height="20">
			<p>Derived details
		 </td>
		 </tr>
		 <td>
				    <table summary="" cellpadding="3"
						cellspacing="0" border="0" class="dataTable" width="100%">
			<tr>
				<th class="formSerialNumberLabelForTable" scope="col">Parent</th>
	      		<th class="formSerialNumberLabelForTable" scope="col">Label</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> Class</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> Type</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> Qty</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> Storage</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> concentration</th>

				</tr>
		      <logic:iterate name="viewSpecimenSummaryForm" property="derivedList" id="derived">
		      	<tr>
		      		<td > <bean:write name="derived" property="parentName" /></td>
		      		<td > <bean:write name="derived" property="displayName" /></td>
		      		<td > <bean:write name="derived" property="className" /></td>
		      		<td > <bean:write name="derived" property="type" /></td>
		      		<td > <bean:write name="derived" property="quantity" /></td>
		      		<td > <bean:write name="derived" property="storageContainerForSpecimen" /></td>
		      		<td > <bean:write name="derived" property="concentration" /></td>
		      	</tr>
		      </logic:iterate>	
		    </table>
          </td>
		</logic:notEmpty>
		</tr>
		<tr>
		<logic:notEmpty name="viewSpecimenSummaryForm" property="eventId">		
		   <td>
			<html:submit  value="Save Collection Protocol"/>
			</td>
		</logic:notEmpty>
		
		<logic:empty name="viewSpecimenSummaryForm" property="eventId">		
		    <td>
			<html:submit value="Save Specimens"/>
			</td>
		</logic:empty>
		</tr>
		</table>
		</html:form>		
</html>