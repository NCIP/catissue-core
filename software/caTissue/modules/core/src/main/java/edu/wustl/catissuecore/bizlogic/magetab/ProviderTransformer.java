package edu.wustl.catissuecore.bizlogic.magetab;


import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AbstractSDRFNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.SourceNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.attribute.ProviderAttribute;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.logging.api.util.StringUtils;

public class ProviderTransformer extends AbstractTransformer {
	private static transient final Logger logger = Logger
			.getCommonLogger(ProviderTransformer.class);

	public ProviderTransformer() {
		super("Provider", "Provider", "Participant");
	}

	@Override
	public void transform(Specimen specimen, AbstractSDRFNode sdrfNode) {
		SourceNode sourceNode;
		if (sdrfNode instanceof SourceNode) {
			sourceNode = (SourceNode) sdrfNode;
		} else {
			logger.debug("Got a node which needs has no provider attribute for specimen"
					+ (StringUtils.isBlank(specimen.getLabel()) ? StringUtils
							.isBlank(specimen.getBarcode()) ? specimen.getLabel()+"_ID" : specimen
							.getBarcode() : specimen.getLabel()));
			return;
		}
		SpecimenCollectionGroup scg = specimen.getSpecimenCollectionGroup();
		Participant participant = specimen.getSpecimenCollectionGroup()
				.getCollectionProtocolRegistration().getParticipant();
		if (participant != null) {
			ProviderAttribute providerAttribute = new ProviderAttribute();
			providerAttribute.setNodeName(participant.getLastName() + ", "
					+ participant.getFirstName());
			providerAttribute.setNodeType("Provider");
			// providerAttribute.setAttributeValue(site.getName());
			sourceNode.provider = providerAttribute;
		}
	}

}
