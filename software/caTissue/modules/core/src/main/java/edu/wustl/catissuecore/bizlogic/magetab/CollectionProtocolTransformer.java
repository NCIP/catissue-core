package edu.wustl.catissuecore.bizlogic.magetab;

import edu.wustl.catissuecore.domain.Specimen;

public class CollectionProtocolTransformer extends AbstractCharacteristicTransformer{
	
	public CollectionProtocolTransformer() {
		super("Collection Protocol Name", "Collection Protocol Name", "Collection Protocol Name");
	}


	@Override
	public String getCharacteristicValue(Specimen specimen) {
		// TODO Auto-generated method stub
		return specimen.getSpecimenCollectionGroup().getCollectionProtocolEvent().getCollectionProtocol().getTitle();
	}
	
	@Override
	public boolean isMageTabSpec() {
		// TODO Auto-generated method stub
		return false;
	}
}
