
package edu.wustl.catissuecore.gridImpl;


import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.Validator;

public class GridSpecimenImpl extends AbstractGridImpl
{

	/* (non-Javadoc)
	 * @see edu.wustl.healthstreet.gridImpl.AbstractGridImpl#getGridQuery(java.lang.String)
	 */
	public String getGridQuery(String jsonString, SessionDataBean sessionData) throws BizLogicException
	{
		String query = "Select 0 as CHKBOX, "+
"case when scg.NAME is null then '' else scg.NAME end as SCG_NAME , "+
"case when Specimen1.LABEL  is null then '' else Specimen1.LABEL end as Label ,"+ 
"case when Specimen1.BARCODE  is null then '' else Specimen1.BARCODE end as Barcode ,"+ 
"case when AbstractSpecimen1.PARENT_SPECIMEN_ID is null then '' else AbstractSpecimen1.PARENT_SPECIMEN_ID end as Parent_Specimen_Id ,"+ 
"case when AbstractSpecimen1.SPECIMEN_CLASS  is null then '' else AbstractSpecimen1.SPECIMEN_CLASS end as Class , "+
"case when AbstractSpecimen1.SPECIMEN_TYPE is null then '' else  AbstractSpecimen1.SPECIMEN_TYPE end as Type , "+
"case when Specimen1.AVAILABLE_QUANTITY  is null then '' else Specimen1.AVAILABLE_QUANTITY end as Avl_Quantity ,"+ 
"case when AbstractSpecimen1.LINEAGE is null then '' else AbstractSpecimen1.LINEAGE end as Lineage, "+
"Specimen1.IDENTIFIER as Identifier "+
"FROM  CATISSUE_SPECIMEN_CHAR SpecimenCharacteristics1 , CATISSUE_ABSTRACT_SPECIMEN AbstractSpecimen1 ,"+ 
"CATISSUE_EXTERNAL_IDENTIFIER ExternalIdentifier1 , CATISSUE_SPECIMEN Specimen1 , catissue_spec_tag_items atg, catissue_specimen_coll_group scg "+ 
" WHERE scg.identifier = Specimen1.SPECIMEN_COLLECTION_GROUP_ID AND SpecimenCharacteristics1.IDENTIFIER   =  AbstractSpecimen1.SPECIMEN_CHARACTERISTICS_ID  AND "+ 
"Specimen1.IDENTIFIER   =  ExternalIdentifier1.SPECIMEN_ID  AND AbstractSpecimen1.IDENTIFIER   =  "+
"Specimen1.IDENTIFIER  AND Specimen1.IDENTIFIER   =  ExternalIdentifier1.SPECIMEN_ID   AND atg.tag_id= "+ jsonString +" and "+ 
"( ( Specimen1.IDENTIFIER  =atg.obj_id  )   AND UPPER(Specimen1.ACTIVITY_STATUS ) != UPPER('Disabled')  ) "+
" ORDER BY Specimen1.IDENTIFIER ,SpecimenCharacteristics1.IDENTIFIER ,ExternalIdentifier1.IDENTIFIER , "+
" AbstractSpecimen1.IDENTIFIER "; 
		setSessionData(sessionData);
//		String query = "select 0 as CHKBOX, " +
//		"case when catissue_specimen.BARCODE is null then '' else catissue_specimen.BARCODE end as BARCODE, " +
//				"case when catissue_specimen.LABEL is null then '' else catissue_specimen.LABEL end as LABEL, " +
//				
//				" case when catissue_specimen_coll_group.NAME is null then '' else catissue_specimen_coll_group.NAME end as SPECIMEN_GROUP_NAME," +
//				"case when catissue_abstract_specimen.PARENT_SPECIMEN_ID is null then '' else catissue_abstract_specimen.PARENT_SPECIMEN_ID end as PARENT_SPECIMEN_ID, " +
//				"case when catissue_abstract_specimen.SPECIMEN_CLASS is null then '' else catissue_abstract_specimen.SPECIMEN_CLASS end as SPECIMEN_CLASS, " +
//				
//				"case when catissue_abstract_specimen.SPECIMEN_TYPE is null then '' else catissue_abstract_specimen.SPECIMEN_TYPE end as SPECIMEN_TYPE, " +
//				"case when catissue_specimen_char.TISSUE_SITE is null then '' else catissue_specimen_char.TISSUE_SITE end as TISSUE_SITE, " +
//				"case when catissue_abstract_specimen.PATHOLOGICAL_STATUS is null then '' else catissue_abstract_specimen.PATHOLOGICAL_STATUS end as PATHOLOGICAL_STATUS, " +
//				"case when catissue_container.NAME is null then '' else catissue_container.NAME end as NAME, " +
//				"case when catissue_abstract_specimen.LINEAGE is null then '' else catissue_abstract_specimen.LINEAGE end as LINEAGE, " +
//				"catissue_specimen.AVAILABLE_QUANTITY, " +
//				"case when catissue_specimen.COLLECTION_STATUS is null then '' else catissue_specimen.COLLECTION_STATUS end as COLLECTION_STATUS " +
//				"from catissue_specimen " +
//				"LEFT JOIN catissue_specimen_position ON catissue_specimen.identifier = catissue_specimen_position.SPECIMEN_ID " +
//				"LEFT JOIN catissue_container ON catissue_specimen_position.container_id = catissue_container.identifier " +
//				"LEFT JOIN catissue_abstract_specimen ON catissue_specimen.identifier = catissue_abstract_specimen.identifier " +
//				"LEFT JOIN catissue_specimen_char ON catissue_abstract_specimen.SPECIMEN_CHARACTERISTICS_ID = catissue_specimen_char.identifier " +
//				"left join catissue_specimen_coll_group on catissue_specimen.SPECIMEN_COLLECTION_GROUP_ID = catissue_specimen_coll_group.IDENTIFIER " +
//				"left join catissue_coll_prot_reg on catissue_specimen_coll_group.COLLECTION_PROTOCOL_REG_ID = catissue_coll_prot_reg.IDENTIFIER " +
//				" where catissue_coll_prot_reg.PROTOCOL_PARTICIPANT_ID like '" + jsonString + "'";
		return query;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.healthstreet.gridImpl.AbstractGridImpl#getDisplayColumnString()
	 */
	public String getDisplayColumnString() throws BizLogicException
	{
		StringBuffer gridColumns = new StringBuffer();
		return gridColumns.toString();
	}

	/* (non-Javadoc)
	 * @see edu.wustl.healthstreet.gridImpl.AbstractGridImpl#getTableColumnString()
	 */
	public String getTableColumnString() throws BizLogicException
	{
		String gridColumns = "CHKBOX,SCG_NAME,Label,Barcode,Parent_Specimen_Id,Class,Type,Avl_Quantity,Lineage,Identifier";
		return gridColumns;
	}
	
	public void beforeRender(com.dhtmlx.connector.DataItem data)
	{
	
		String specimenLabel =data.get_value("Label");
		String barcode =data.get_value("Barcode");
	
		StringBuffer specimenNameString= new StringBuffer();
		specimenNameString.append(specimenLabel);
		if(!Validator.isEmpty(barcode))
		{
			if(!Validator.isEmpty(specimenLabel))
			specimenNameString.append("(");
			specimenNameString.append(barcode);
			specimenNameString.append(")");
		}
		data.set_value("Label",specimenNameString.toString());
		
		String specimenClass =data.get_value("Class");
		String specimenType =data.get_value("Type");
	
		StringBuffer classNameStr= new StringBuffer();
		classNameStr.append(specimenClass);
		if(!Validator.isEmpty(specimenType))
		{
			classNameStr.append("(");
			classNameStr.append(specimenType);
			classNameStr.append(")");
		}
		data.set_value("Class",classNameStr.toString());
	}
}
