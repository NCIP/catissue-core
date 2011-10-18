/**
 * <p>Title: DomainObjectFactory Class>
 * <p>Description:	DomainObjectFactory is a factory for instances of various domain objects.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 21, 2005
 * */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.bizlogic.uidomain.TransformerFactory;
import edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter;
import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;
import edu.wustl.catissuecore.domain.processingprocedure.ActionApplication;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.catissuecore.domain.shippingtracking.Shipment;
import edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.bizlogic.UIDomainTransformer;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.domain.UIRepOfDomain;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.factory.IDomainObjectFactory;
import edu.wustl.simplequery.query.ShoppingCart;

/**
 * DomainObjectFactory is a factory for instances of various domain objects.
 * @author gautam_shetty
 */
public class DomainObjectFactory implements IDomainObjectFactory
{

	/**
	 * Returns the fully qualified name of the class according to the form bean type.
	 * @param FORM_TYPE Form bean Id.
	 * @return the fully qualified name of the class according to the form bean type.
	 */
	public String getDomainObjectName(int FORM_TYPE)
	{
		String className = null;
		switch (FORM_TYPE)
		{
			case Constants.PARTICIPANT_FORM_ID :
				className = Participant.class.getName();
				break;
			case Constants.INSTITUTION_FORM_ID :
				className = Institution.class.getName();
				break;
			case Constants.REPORTED_PROBLEM_FORM_ID :
				className = ReportedProblem.class.getName();
				break;
			case Constants.USER_FORM_ID :
				className = User.class.getName();
				break;
			case Constants.DEPARTMENT_FORM_ID :
				className = Department.class.getName();
				break;
			case Constants.APPROVE_USER_FORM_ID :
				className = User.class.getName();
				break;
			case Constants.COLLECTION_PROTOCOL_FORM_ID :
				className = CollectionProtocol.class.getName();
				break;
			case Constants.DISTRIBUTIONPROTOCOL_FORM_ID :
				className = DistributionProtocol.class.getName();
				break;
			case Constants.STORAGE_CONTAINER_FORM_ID :
				className = StorageContainer.class.getName();
				break;
			case Constants.STORAGE_TYPE_FORM_ID :
				className = StorageType.class.getName();
				break;
			case Constants.SITE_FORM_ID :
				className = Site.class.getName();
				break;
			case Constants.CANCER_RESEARCH_GROUP_FORM_ID :
				className = CancerResearchGroup.class.getName();
				break;
			case Constants.BIOHAZARD_FORM_ID :
				className = Biohazard.class.getName();
				break;
//			case Constants.FROZEN_EVENT_PARAMETERS_FORM_ID :
//				className = FrozenEventParameters.class.getName();
//				break;
//			case Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID :
//				className = CheckInCheckOutEventParameter.class.getName();
//				break;
//			case Constants.RECEIVED_EVENT_PARAMETERS_FORM_ID :
//				className = ReceivedEventParameters.class.getName();
//				break;
			case Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID :
				className = CollectionProtocolRegistration.class.getName();
				break;
			case Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID :
				className = SpecimenCollectionGroup.class.getName();
				break;
//			case Constants.FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID :
//				className = FluidSpecimenReviewEventParameters.class.getName();
//				break;
			case Constants.NEW_SPECIMEN_FORM_ID :
			case Constants.CREATE_SPECIMEN_FORM_ID :
				className = Specimen.class.getName();
				break;
//			case Constants.CELL_SPECIMEN_REVIEW_PARAMETERS_FORM_ID :
//				className = CellSpecimenReviewParameters.class.getName();
//				break;
//			case Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID :
//				className = TissueSpecimenReviewEventParameters.class.getName();
//				break;
			case Constants.DISPOSAL_EVENT_PARAMETERS_FORM_ID :
				className = DisposalEventParameters.class.getName();
				break;
//			case Constants.THAW_EVENT_PARAMETERS_FORM_ID :
//				className = ThawEventParameters.class.getName();
//				break;
//			case Constants.MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_FORM_ID :
//				className = MolecularSpecimenReviewParameters.class.getName();
//				break;
//			case Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID :
//				className = CollectionEventParameters.class.getName();
//				break;
			case Constants.TRANSFER_EVENT_PARAMETERS_FORM_ID :
				className = TransferEventParameters.class.getName();
				break;
//			case Constants.SPUN_EVENT_PARAMETERS_FORM_ID :
//				className = SpunEventParameters.class.getName();
//				break;
//			case Constants.EMBEDDED_EVENT_PARAMETERS_FORM_ID :
//				className = EmbeddedEventParameters.class.getName();
//				break;
//			case Constants.FIXED_EVENT_PARAMETERS_FORM_ID :
//				className = FixedEventParameters.class.getName();
//				break;
//			case Constants.PROCEDURE_EVENT_PARAMETERS_FORM_ID :
//				className = ProcedureEventParameters.class.getName();
//				break;
			case Constants.DISTRIBUTION_FORM_ID :
				className = Distribution.class.getName();
				break;
			case Constants.SPECIMEN_EVENT_PARAMETERS_FORM_ID :
				className = SpecimenEventParameters.class.getName();
				break;
			case Constants.SHOPPING_CART_FORM_ID :
				className = ShoppingCart.class.getName();
				break;
			case Constants.SIMILAR_CONTAINERS_FORM_ID :
				className = StorageContainer.class.getName();
				break;
			case Constants.ALIQUOT_FORM_ID :
				className = Specimen.class.getName();//AliquotSpecimen.class.getName();
				break;
			case Constants.SPECIMEN_ARRAY_TYPE_FORM_ID :
				className = SpecimenArrayType.class.getName();
				break;
			case Constants.SPECIMEN_ARRAY_FORM_ID :
				className = SpecimenArray.class.getName();
				break;

			case Constants.SPECIMEN_ARRAY_ALIQUOT_FORM_ID :
				className = SpecimenArray.class.getName();
				break;
			//Ordering System
			case Constants.REQUEST_LIST_FILTERATION_FORM_ID :
				className = OrderDetails.class.getName();
				break;

			case Constants.REQUEST_DETAILS_FORM_ID :
				className = OrderDetails.class.getName();
				break;

			case Constants.ORDER_FORM_ID :
				className = OrderDetails.class.getName();
				break;

			case Constants.ORDER_ARRAY_FORM_ID :
				className = OrderDetails.class.getName();
				break;
			case Constants.ORDER_PATHOLOGY_FORM_ID :
				className = OrderDetails.class.getName();
				break;
			case Constants.PATHOLOGY_REPORT_REVIEW_FORM_ID :
				className = PathologyReportReviewParameter.class.getName();
				break;
			case Constants.QUARANTINE_EVENT_PARAMETER_FORM_ID :
				className = QuarantineEventParameter.class.getName();
				break;
			case edu.wustl.catissuecore.util.shippingtracking.Constants.SHIPMENT_FORM_ID :
			case edu.wustl.catissuecore.util.shippingtracking.Constants.SHIPMENT_RECEIVING_FORM_ID :
				className = Shipment.class.getName();
				break;
			case edu.wustl.catissuecore.util.shippingtracking.Constants.SHIPMENT_REQUEST_FORM_ID :
				className = ShipmentRequest.class.getName();
				break;
			case Constants.ACTION_APP_FORM_ID :
				className = ActionApplication.class.getName();
				break;
			case Constants.SPP_ID :
				className = SpecimenProcessingProcedure.class.getName();
			break;
			default :

		}
		return className;
	}

	/**
	 * Returns an AbstractDomain object copy of the form bean object.
	 *
	 * @param FORM_TYPE Form bean Id.
	 * @param form Form bean object.
	 * @return an AbstractDomain object copy of the form bean object.
	 * @throws AssignDataException AssignDataException.
	  *
     * @deprecated use createDomainObject() instead.
     */
	 @Deprecated
    public AbstractDomainObject getDomainObject(int FORM_TYPE, AbstractActionForm form) throws AssignDataException {
        return createDomainObject(form);
    }

    /**
     * @param arg0 : arg0
     * @param arg1 : arg1
     * @throws ApplicationException : ApplicationException
     * @return AbstractActionForm : AbstractActionForm
     */
    public AbstractActionForm getFormBean(Object arg0, String arg1) throws ApplicationException {
        return null;
    }

    public AbstractDomainObject createDomainObject(UIRepOfDomain uiRepOfDomain) throws AssignDataException {
        return getTransformer(uiRepOfDomain).createDomainObject(uiRepOfDomain);
    }

    public void overwriteDomainObject(AbstractDomainObject domainObject, UIRepOfDomain uiRepOfDomain) {
        getTransformer(uiRepOfDomain).overwriteDomainObject(domainObject, uiRepOfDomain);
    }

    private UIDomainTransformer<UIRepOfDomain, AbstractDomainObject> getTransformer(UIRepOfDomain uiRepOfDomain) {
        return TransformerFactory.getInstance().getTransformer(uiRepOfDomain.getClass());
    }
}