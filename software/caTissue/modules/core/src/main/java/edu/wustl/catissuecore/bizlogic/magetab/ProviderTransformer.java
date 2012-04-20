package edu.wustl.catissuecore.bizlogic.magetab;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AbstractSDRFNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.SourceNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.attribute.ProviderAttribute;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.logging.api.util.StringUtils;

public class ProviderTransformer extends AbstractTransformer {
    private static transient final Logger logger = Logger
            .getCommonLogger(ProviderTransformer.class);

    public ProviderTransformer() {
        super("Provider", "Provider", "PI");
    }

    @Override
    public void transform(Specimen specimen, AbstractSDRFNode sdrfNode) {
        SourceNode sourceNode;
        if (sdrfNode instanceof SourceNode) {
            sourceNode = (SourceNode) sdrfNode;
        } else {
            logger.debug("Got a node which needs has no provider attribute for specimen"
                    + (StringUtils.isBlank(specimen.getLabel()) ? StringUtils
                            .isBlank(specimen.getBarcode()) ? specimen
                            .getLabel() + "_ID" : specimen.getBarcode()
                            : specimen.getLabel()));
            return;
        }
        CollectionProtocol cp = specimen.getSpecimenCollectionGroup()
                .getCollectionProtocolRegistration().getCollectionProtocol();
        if (cp != null && cp.getPrincipalInvestigator() != null) {
            ProviderAttribute providerAttribute = new ProviderAttribute();
            providerAttribute.setNodeName(cp.getPrincipalInvestigator()
                    .getLastName()
                    + ", "
                    + cp.getPrincipalInvestigator().getFirstName());
            providerAttribute.setNodeType("Provider");
            sourceNode.provider = providerAttribute;
        }
    }

    @Override
    public boolean isMageTabSpec() {
        return true;
    }

}
