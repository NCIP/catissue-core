package edu.wustl.catissuecore.bizlogic.magetab;



import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AbstractSDRFNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.ExtractNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.SampleNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.SourceNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.attribute.MaterialTypeAttribute;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.util.logger.Logger;


public class MaterialTypeTransformer extends AbstractTransformer {
	private MaterialTypeTransformerConfig config;
	private static transient final Logger logger =  Logger.getCommonLogger(MaterialTypeTransformer.class);
	
	public MaterialTypeTransformer(MaterialTypeTransformerConfig config) {
		super("MaterialType", "Material Type", "Specimen Type");
		this.config = config;
	}

	@Override
	public void transform(Specimen specimen, AbstractSDRFNode sdrfNode) {
		String key = specimen.getSpecimenClass().toLowerCase().trim();
		String key1=specimen.getSpecimenType().toLowerCase().trim();
		if("molecular".equals(key)&&config.getKnownMolecularTypes().contains(key1))			
		{
			key=key1+"/"+key;
		}
		logger.debug("the key is "+key+" and the node is "+sdrfNode.getClass().getSimpleName()+" for the specimen "+specimen.getId());
		String mgedType = config.getMaterialTypesMap().get(key);
		logger.debug("the mged type is "+mgedType);
		if (mgedType != null) {
			MaterialTypeAttribute mtAttr = new MaterialTypeAttribute();
			mtAttr.setNodeName(mgedType);
			mtAttr.setNodeType("MaterialType");			
			mtAttr.termSourceREF = config.getTermSource().getName();
			logger.debug("the termsource name is "+config.getTermSource().getName());
			if (sdrfNode instanceof SourceNode) {				
				((SourceNode)sdrfNode).materialType = mtAttr;
			} else if (sdrfNode instanceof SampleNode) { 
				((SampleNode)sdrfNode).materialType = mtAttr;
			} else if (sdrfNode instanceof ExtractNode) {
				((ExtractNode)sdrfNode).materialType = mtAttr;
			} else {
				logger.error("Got an unexpected node");
			}
		}

	}
	
}
