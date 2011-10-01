package edu.wustl.catissuecore.bizlogic.magetab;



import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AbstractSDRFNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.ExtractNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.SampleNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.SourceNode;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.util.logger.Logger;

public class DescriptionTransformer extends AbstractTransformer {
	private static transient final Logger logger =  Logger.getCommonLogger(DescriptionTransformer.class);
	public DescriptionTransformer() {
		super("Description", "Description", "Description");
	}

	@Override
	public void transform(Specimen specimen, AbstractSDRFNode sdrfNode) {
		if (specimen.getComment() != null && !specimen.getComment().isEmpty()) {
			if (sdrfNode instanceof SourceNode) {
				((SourceNode)sdrfNode).description = specimen.getComment();
			} else if (sdrfNode instanceof SampleNode) {
				((SampleNode)sdrfNode).description = specimen.getComment();
			} else if (sdrfNode instanceof ExtractNode) {
				((ExtractNode)sdrfNode).description = specimen.getComment();
			} else {
				logger.error("Got an unexpected node");
			}
		}
	}

}
