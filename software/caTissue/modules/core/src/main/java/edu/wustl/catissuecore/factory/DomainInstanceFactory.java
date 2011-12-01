
package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.Aliquot;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CellSpecimenRequirement;
import edu.wustl.catissuecore.domain.ClinicalDiagnosis;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Container;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.ContainerType;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.DistributionSpecimenRequirement;
import edu.wustl.catissuecore.domain.ExistingSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.FluidSpecimenRequirement;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimenRequirement;
import edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.OrderItem;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.passwordutil.Password;
import edu.wustl.catissuecore.domain.PathologicalCaseOrderItem;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.ReportedProblem;
import edu.wustl.catissuecore.domain.ReturnEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenOrderItem;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.StudyFormContext;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.TissueSpecimenRequirement;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry;
import edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry;
import edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry;
import edu.wustl.catissuecore.domain.pathology.BinaryContent;
import edu.wustl.catissuecore.domain.pathology.Concept;
import edu.wustl.catissuecore.domain.pathology.ConceptReferent;
import edu.wustl.catissuecore.domain.pathology.ConceptReferentClassification;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter;
import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.domain.pathology.ReportSection;
import edu.wustl.catissuecore.domain.pathology.SemanticType;
import edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.domain.pathology.XMLContent;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.catissuecore.domain.shippingtracking.Shipment;
import edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest;
import edu.wustl.common.domain.AbstractDomainObject;

public class DomainInstanceFactory
{

	public static void main(String args[]) {
		InstanceFactory<Participant> instFact = DomainInstanceFactory.getInstanceFactory(Participant.class);
		System.out.println(instFact.createObject());
	}

	private DomainInstanceFactory()
	{
		super();
	}

	public static  synchronized InstanceFactory getInstanceFactory(Class<? extends AbstractDomainObject> klass)
	{
		InstanceFactory<? extends AbstractDomainObject> instanceFactory = null;
		if(Participant.class.equals(klass)){
			instanceFactory = ParticipantFactory.getInstance();
		}
		else if(Biohazard.class.equals(klass)){
			instanceFactory = BiohazardFactory.getInstance();
		}
		else if(Address.class.equals(klass)){
			instanceFactory = AddressFactory.getInstance();
		}
		else if(Aliquot.class.equals(klass)){
			instanceFactory = AliquotFactory.getInstance();
		}
		else if(CancerResearchGroup.class.equals(klass)){
			instanceFactory = CancerResearchGroupFactory.getInstance();
		}
		else if(Capacity.class.equals(klass)){
			instanceFactory = CapacityFactory.getInstance();
		}
		else if(ClinicalDiagnosis.class.equals(klass)){
			instanceFactory = ClinicalDiagnosisFactory.getInstance();
		}
		else if(Department.class.equals(klass)){
			instanceFactory = DepartmentFactory.getInstance();
		}
		else if(Institution.class.equals(klass)){
			instanceFactory = InstitutionFactory.getInstance();
		}
		else if(Container.class.equals(klass)){
			instanceFactory = ContainerFactory.getInstance();
		}
		else if(ContainerPosition.class.equals(klass)){
			instanceFactory = ContainerPositionFactory.getInstance();
		}
		else if(ContainerType.class.equals(klass)){
			instanceFactory = ContainerTypeFactory.getInstance();
		}
		else if(User.class.equals(klass)){
			instanceFactory = UserFactory.getInstance();
		}
		else if(Password.class.equals(klass)){
			instanceFactory = PasswordFactory.getInstance();
		}
		else if(ParticipantMedicalIdentifier.class.equals(klass)){
			instanceFactory = ParticipantMedicalIdentifierFactory.getInstance();
		}
		else if(Race.class.equals(klass)){
			instanceFactory = RaceFactory.getInstance();
		}
		else if(ReportedProblem.class.equals(klass)){
			instanceFactory = ReportedProblemFactory.getInstance();
		}
		else if(Site.class.equals(klass)){
			instanceFactory = SiteFactory.getInstance();
		}
		else if(Specimen.class.equals(klass)){
			instanceFactory = SpecimenFactory.getInstance();
		}
		else if(TissueSpecimen.class.equals(klass)){
			instanceFactory = TissueSpecimenFactory.getInstance();
		}
		else if(FluidSpecimen.class.equals(klass)){
			instanceFactory = FluidSpecimenFactory.getInstance();
		}
		else if(CellSpecimen.class.equals(klass)){
			instanceFactory = CellSpecimenFactory.getInstance();
		}
		else if(MolecularSpecimen.class.equals(klass)){
			instanceFactory = MolecularSpecimenFactory.getInstance();
		}
		else if(CollectionProtocolRegistration.class.equals(klass)){
			instanceFactory = CollectionProtocolRegistrationFactory.getInstance();
		}
		else if(SpecimenCollectionGroup.class.equals(klass)){
			instanceFactory = SpecimenCollectionGroupFactory.getInstance();
		}
		else if(SpecimenArray.class.equals(klass)){
			instanceFactory = SpecimenArrayFactory.getInstance();
		}
		else if(SpecimenArrayContent.class.equals(klass)){
			instanceFactory = SpecimenArrayContentFactory.getInstance();
		}
		else if(OrderDetails.class.equals(klass)){
			instanceFactory = OrderDetailsFactory.getInstance();
		}
		else if(OrderItem.class.equals(klass)){
			instanceFactory = OrderItemFactory.getInstance();
		}
		else if(SpecimenOrderItem.class.equals(klass)){
			instanceFactory = SpecimenOrderItemFactory.getInstance();
		}
		else if(DerivedSpecimenOrderItem.class.equals(klass)){
			instanceFactory = DerivedSpecimenOrderItemFactory.getInstance();
		}
		else if(ExistingSpecimenOrderItem.class.equals(klass)){
			instanceFactory = ExistingSpecimenOrderItemFactory.getInstance();
		}
		else if(NewSpecimenArrayOrderItem.class.equals(klass)){
			instanceFactory = NewSpecimenArrayOrderItemFactory.getInstance();
		}
		else if(ExistingSpecimenArrayOrderItem.class.equals(klass)){
			instanceFactory = ExistingSpecimenArrayOrderItemFactory.getInstance();
		}
		else if(PathologicalCaseOrderItem.class.equals(klass)){
			instanceFactory = PathologicalCaseOrderItemFactory.getInstance();
		}
		else if(SpecimenArrayType.class.equals(klass)){
			instanceFactory = SpecimenArrayTypeFactory.getInstance();
		}
		else if(SpecimenCharacteristics.class.equals(klass)){
			instanceFactory = SpecimenCharacteristicsFactory.getInstance();
		}
		else if(SpecimenPosition.class.equals(klass)){
			instanceFactory = SpecimenPositionFactory.getInstance();
		}
		else if(SpecimenRequirement.class.equals(klass)){
			instanceFactory = SpecimenRequirementFactory.getInstance();
		}
		else if(CellSpecimenRequirement.class.equals(klass)){
			instanceFactory = CellSpecimenRequirementFactory.getInstance();
		}
		else if(FluidSpecimenRequirement.class.equals(klass)){
			instanceFactory = FluidSpecimenRequirementFactory.getInstance();
		}
		else if(TissueSpecimenRequirement.class.equals(klass)){
			instanceFactory = TissueSpecimenRequirementFactory.getInstance();
		}
		else if(MolecularSpecimenRequirement.class.equals(klass)){
			instanceFactory = MolecularSpecimenRequirementFactory.getInstance();
		}
		else if(StorageContainer.class.equals(klass)){
			instanceFactory = StorageContainerFactory.getInstance();
		}
		else if(StorageType.class.equals(klass)){
			instanceFactory = StorageTypeFactory.getInstance();
		}
		else if(StudyFormContext.class.equals(klass)){
			instanceFactory = StudyFormContextFactory.getInstance();
		}
		else if(ConsentTier.class.equals(klass)){
			instanceFactory = ConsentTierFactory.getInstance();
		}
		else if(ConsentTierResponse.class.equals(klass)){
			instanceFactory = ConsentTierResponseFactory.getInstance();
		}
		else if(ConsentTierStatus.class.equals(klass)){
			instanceFactory = ConsentTierStatusFactory.getInstance();
		}
		else if(DistributedItem.class.equals(klass)){
			instanceFactory = DistributedItemFactory.getInstance();
		}
		else if(Distribution.class.equals(klass)){
			instanceFactory = DistributionFactory.getInstance();
		}
		else if(DistributionSpecimenRequirement.class.equals(klass)){
			instanceFactory = DistributionSpecimenRequirementFactory.getInstance();
		}
		else if(ExternalIdentifier.class.equals(klass)){
			instanceFactory = ExternalIdentifierFactory.getInstance();
		}
		else if(CollectionProtocol.class.equals(klass)){
			instanceFactory = CollectionProtocolFactory.getInstance();
		}
		else if(DistributionProtocol.class.equals(klass)){
			instanceFactory = DistributionProtocolFactory.getInstance();
		}
		else if(DisposalEventParameters.class.equals(klass)){
			instanceFactory = DisposalEventParametersFactory.getInstance();
		}
		else if(ReturnEventParameters.class.equals(klass)){
			instanceFactory = ReturnEventParametersFactory.getInstance();
		}
	    if(TransferEventParameters.class.equals(klass)){
			instanceFactory = TransferEventParametersFactory.getInstance();
		}
		/**
		 * caTIES Factories.
		 */
		else if(BinaryContent.class.equals(klass)){
			instanceFactory = BinaryContentFactory.getInstance();
		}
		else if(Concept.class.equals(klass)){
			instanceFactory = ConceptFactory.getInstance();
		}
		else if(ConceptReferent.class.equals(klass)){
			instanceFactory = ConceptReferentFactory.getInstance();
		}
		else if(ConceptReferentClassification.class.equals(klass)){
			instanceFactory = ConceptReferentClassificationFactory.getInstance();
		}
		else if(SurgicalPathologyReport.class.equals(klass)){
			instanceFactory = SurgicalPathologyReportFactory.getInstance();
		}
		else if(DeidentifiedSurgicalPathologyReport.class.equals(klass)){
			instanceFactory =DeidentifiedSurgicalPathologyReportFactory.getInstance();
		}
		else if(IdentifiedSurgicalPathologyReport.class.equals(klass)){
			instanceFactory =IdentifiedSurgicalPathologyReportFactory.getInstance();
		}
		else if(PathologyReportReviewParameter.class.equals(klass)){
			instanceFactory =PathologyReportReviewParameterFactory.getInstance();
		}
		else if(QuarantineEventParameter.class.equals(klass)){
			instanceFactory =QuarantineEventParameterFactory.getInstance();
		}
		else if(ReportLoaderQueue.class.equals(klass)){
			instanceFactory =ReportLoaderQueueFactory.getInstance();
		}
		else if(ReportSection.class.equals(klass)){
			instanceFactory =ReportSectionFactory.getInstance();
		}
		else if(SemanticType.class.equals(klass)){
			instanceFactory =SemanticTypeFactory.getInstance();
		}
		else if(TextContent.class.equals(klass)){
			instanceFactory =TextContentFactory.getInstance();
		}
		else if(XMLContent.class.equals(klass)){
			instanceFactory =XMLContentFactory.getInstance();
		}
		else if(CollectionProtocolEvent.class.equals(klass)){
			instanceFactory =CollectionProtocolEventFactory.getInstance();
		}
		/**
		 * De-Integration Factories.
		 */
		else if(SCGRecordEntry.class.equals(klass)){
			instanceFactory =SCGRecordEntryFactory.getInstance();
		}
		else if(ParticipantRecordEntry.class.equals(klass)){
			instanceFactory =ParticipantRecordEntryFactory.getInstance();
		}
		else if(SpecimenRecordEntry.class.equals(klass)){
			instanceFactory =SpecimenRecordEntryFactory.getInstance();
		}
		/**
		 * Shipment Factories.
		 */
		else if(Shipment.class.equals(klass)){
			instanceFactory = ShipmentFactory.getInstance();
		}
		else if(ShipmentRequest.class.equals(klass)){
			instanceFactory = ShipmentRequestFactory.getInstance();
		}
		else if(SpecimenProcessingProcedure.class.equals(klass)){
			instanceFactory = SPPFactory.getInstance();
		}
		return instanceFactory;
	}
}
