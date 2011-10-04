package edu.wustl.catissuecore.bizlogic.magetab;

import edu.wustl.catissuecore.domain.Specimen;

public class ExternalIdTransformer extends AbstractCharacteristicTransformer {
	//private IdType externalIdType = IdType.GSID; 
	
	public ExternalIdTransformer() {
		//super("ExternalId", "External Id", "Global Specimen Identifier");
		super("ExternalId", "External Id", "External Id");
	}

//	public IdType getExternalIdType() {
//		return externalIdType;
//	}
//
//	public void setExternalIdType(IdType externalIdType) {
//		this.externalIdType = externalIdType;
//	}
	
	@Override
	public String getCharacteristicValue(Specimen specimen) {
		return specimen.getLabel();
//		switch (externalIdType) {
//		case GSID: return specimen.getGlobalSpecimenIdentifier();
//		case CATISSUE_ID: return specimen.getId().toString();
//		case CATISSUE_LABEL: return specimen.getLabel();
//		case CATISSUE_BARCODE: return specimen.getBarcode();
//		default:
//			return "N/A";
//		}
	}
	
//
//	public static enum IdType {
//		GSID {public String toString() {return "GSID";}},
//		CATISSUE_ID {public String toString() {return "caTissue ID";}},
//		CATISSUE_LABEL {public String toString() {return "caTissue Label";}},
//		CATISSUE_BARCODE {public String toString() {return "caTissue Barcode";}}
//	}
	
	@Override
	public boolean isMageTabSpec() {
		// TODO Auto-generated method stub
		return false;
	}

}
