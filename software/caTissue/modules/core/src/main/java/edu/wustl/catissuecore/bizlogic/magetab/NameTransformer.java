package edu.wustl.catissuecore.bizlogic.magetab;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AbstractSDRFNode;
import edu.wustl.catissuecore.domain.Specimen;
import gov.nih.nci.logging.api.util.StringUtils;

public class NameTransformer extends AbstractTransformer {
	public NameTransformer() {
		super("Name", "Name","Name");
	}
	
	@Override
	public void transform(Specimen specimen, AbstractSDRFNode sdrfNode) {
		sdrfNode.setNodeName(StringUtils.isBlank(specimen
				.getLabel()) ? StringUtils.isBlank(specimen
						.getBarcode()) ? specimen.getId()+"" : specimen.getBarcode()
						: specimen.getLabel());
	}
	
	@Override
	public boolean isMageTabSpec() {
		// TODO Auto-generated method stub
		return true;
	}
}
