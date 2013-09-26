/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.bizlogic.magetab;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AbstractSDRFNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.ExtractNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.SampleNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.SourceNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.attribute.CharacteristicsAttribute;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.util.logger.Logger;

public abstract class AbstractCharacteristicTransformer extends AbstractTransformer {
	
    private transient final Logger logger = Logger.getCommonLogger(AbstractCharacteristicTransformer.class);
    
    private CharacteristicTransformerConfig config;
	
	public AbstractCharacteristicTransformer(String name, String userFriendlyName, String localName, CharacteristicTransformerConfig config) {
		super(name, userFriendlyName, localName);
		
		this.config = config;
	}
	
	public AbstractCharacteristicTransformer(String name, String userFriendlyName, String localName) {
		super(name, userFriendlyName, localName);
	}
	
	@Override
	public void transform(Specimen specimen, AbstractSDRFNode sdrfNode) {
		String value = getCharacteristicValue(specimen);
		if (value == null) {
			return;
		}
		
		CharacteristicsAttribute charAttr = new CharacteristicsAttribute();
		charAttr.type = getCharacteristicName();
//		charAttr.setAttributeType(getCharacteristicName());		
//		charAttr.setAttributeValue(value);
		charAttr.setNodeName(value);		
		if (config != null) {
			TermSource termSource = config.getTermSource();
			if (termSource != null) {
				charAttr.termSourceREF = termSource.getName();
			}
		}

		if (sdrfNode instanceof SourceNode) {
			((SourceNode)sdrfNode).characteristics.add(charAttr);
		} else if (sdrfNode instanceof SampleNode) {
			((SampleNode)sdrfNode).characteristics.add(charAttr);
		} else if (sdrfNode instanceof ExtractNode) {
			((ExtractNode)sdrfNode).characteristics.add(charAttr);
		} else {
		    logger.error("Unknown node type: "+sdrfNode);
		}
	}

	public String getCharacteristicName() {
		return getName();
	}
	
	public abstract String getCharacteristicValue(Specimen specimen);

}
