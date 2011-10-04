package edu.wustl.catissuecore.bizlogic.magetab;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AbstractSDRFNode;
import edu.wustl.catissuecore.domain.Specimen;

public interface Transformer {
	String getName();
	String getUserFriendlyName();
	String getLocalName();
	boolean isMageTabSpec();
	void transform(Specimen specimen, AbstractSDRFNode sdrfNode);
}
