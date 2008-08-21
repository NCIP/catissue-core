<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

			
<%@ include file="/pages/content/common/ActionErrors.jsp" %>    
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

	<tr>
		<td>
	
			<table summary="" cellpadding="3" cellspacing="0" border="0">
<%
	String operation = "operation";
	String operationValue ="add";
	request.setAttribute("add",operationValue);
%>
		<!-- Received Event Parameters -->
				<tr>
					<td class="formRequiredLabel">
						<html:link page="/ReceivedEventParameters.do?pageOf=pageOfReceivedEventParameters" paramId="<%=operation%>" paramName="<%=operationValue%>">
							Received Event Parameters	
						</html:link>
					</td>
				</tr>
		<!-- Disposal Event Parameters -->
				<tr>
					<td class="formRequiredLabel">
						<html:link page="/DisposalEventParameters.do?pageOf=pageOfDisposalEventParameters" paramId="<%=operation%>" paramName="<%=operationValue%>">
							Disposal Event Parameters	
						</html:link>
					</td>
				</tr>
		<!-- Fixed Event Parameters -->
				<tr>
					<td class="formRequiredLabel">
						<html:link page="/FixedEventParameters.do?pageOf=pageOfFixedEventParameters" paramId="<%=operation%>" paramName="<%=operationValue%>">
							Fixed Event Parameters	
						</html:link>
					</td>
				</tr>
		<!-- Collection Event Parameters -->
				<tr>
					<td class="formRequiredLabel">
						<html:link page="/CollectionEventParameters.do?pageOf=pageOfCollectionEventParameters" paramId="<%=operation%>" paramName="<%=operationValue%>">
							Collection Event Parameters	
						</html:link>
					</td>
				</tr>
		<!-- Transfer Event Parameters -->
				<tr>
					<td class="formRequiredLabel">
						<html:link page="/TransferEventParameters.do?pageOf=pageOfTransferEventParameters" paramId="<%=operation%>" paramName="<%=operationValue%>">
							Transfer Event Parameters	
						</html:link>
					</td>
				</tr>
		<!-- Procedure Event Parameters -->
				<tr>
					<td class="formRequiredLabel">
						<html:link page="/ProcedureEventParameters.do?pageOf=pageOfProcedureEventParameters" paramId="<%=operation%>" paramName="<%=operationValue%>">
							Procedure Event Parameters	
						</html:link>
					</td>
				</tr>		
		<!-- CheckInCheckOut Event Parameters -->
				<tr>
					<td class="formRequiredLabel">
						<html:link page="/CheckInCheckOutEventParameters.do?pageOf=pageOfCheckInCheckOutEventParameters" paramId="<%=operation%>" paramName="<%=operationValue%>">
							CheckInCheckOut Event Parameters	
						</html:link>
					</td>
				</tr>
		<!-- Frozen Event Parameters -->
				<tr>
					<td class="formRequiredLabel">
						<html:link page="/FrozenEventParameters.do?pageOf=pageOfFrozenEventParameters" paramId="<%=operation%>" paramName="<%=operationValue%>">
							Frozen Event Parameters	
						</html:link>
					</td>
				</tr>
		<!-- Thaw Event Parameters -->
				<tr>
					<td class="formRequiredLabel">
						<html:link page="/ThawEventParameters.do?pageOf=pageOfThawEventParameters" paramId="<%=operation%>" paramName="<%=operationValue%>">
							Thaw Event Parameters	
						</html:link>
					</td>
				</tr>

		<!-- Spun Event Parameters -->
				<tr>
					<td class="formRequiredLabel">
						<html:link page="/SpunEventParameters.do?pageOf=pageOfSpunEventParameters" paramId="<%=operation%>" paramName="<%=operationValue%>">
							Spun Event Parameters	
						</html:link>
					</td>
				</tr>

		<!-- Embedded Event Parameters -->
				<tr>
					<td class="formRequiredLabel">
						<html:link page="/EmbeddedEventParameters.do?pageOf=pageOfEmbeddedEventParameters" paramId="<%=operation%>" paramName="<%=operationValue%>">
							Embedded Event Parameters	
						</html:link>
					</td>
				</tr>

		<!-- MolecularSpecimenReview Event Parameters -->
				<tr>
					<td class="formRequiredLabel">
						<html:link page="/MolecularSpecimenReviewParameters.do?pageOf=pageOfMolecularSpecimenReviewParameters" paramId="<%=operation%>" paramName="<%=operationValue%>">
							Molecular Specimen Review Parameters	
						</html:link>
					</td>
				</tr>
		<!-- Tissue Specimen Review Event Parameters -->
				<tr>
					<td class="formRequiredLabel">
						<html:link page="/TissueSpecimenReviewEventParameters.do?pageOf=pageOfTissueSpecimenReviewParameters" paramId="<%=operation%>" paramName="<%=operationValue%>">
							Tissue Specimen Review Event Parameters
						</html:link>
					</td>
				</tr>

		<!-- CellSpecimenReview Event Parameters -->
				<tr>
					<td class="formRequiredLabel">
						<html:link page="/CellSpecimenReviewParameters.do?pageOf=pageOfCellSpecimenReviewParameters" paramId="<%=operation%>" paramName="<%=operationValue%>">
							Cell Specimen Review Parameters	
						</html:link>
					</td>
				</tr>

		<!-- Fluid Specimen Review Event Parameters -->
				<tr>
					<td class="formRequiredLabel">
						<html:link page="/FluidSpecimenReviewEventParameters.do?pageOf=pageOfFluidSpecimenReviewParameters" paramId="<%=operation%>" paramName="<%=operationValue%>">
							Fluid Specimen Review Event Parameters
						</html:link>
					</td>
				</tr>

			</table>
		
		</td>
	</tr>

 </table>