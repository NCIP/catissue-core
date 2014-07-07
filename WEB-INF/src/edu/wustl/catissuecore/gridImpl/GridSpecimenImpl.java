
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
				"case when Specimen1.LABEL  is null then '' else Specimen1.LABEL end as LABEL ,"+ 
				"case when Specimen1.LABEL  is null then '' else Specimen1.LABEL end as LABELONLY ,"+
				"case when Specimen1.BARCODE  is null then '' else Specimen1.BARCODE end as BARCODE ,"+ 
				"CASE WHEN (select label from catissue_Specimen where identifier=Specimen1.parent_specimen_id) IS NULL THEN '' "+
				"ELSE  (select label from catissue_Specimen where identifier=Specimen1.parent_specimen_id) END as PARENTLABEL,"+ 
				"case when Specimen1.SPECIMEN_CLASS  is null then '' else Specimen1.SPECIMEN_CLASS end as CLASS , "+
				"case when Specimen1.SPECIMEN_TYPE is null then '' else  Specimen1.SPECIMEN_TYPE end as TYPE , "+
				"case when Specimen1.AVAILABLE_QUANTITY  is null then 0 else Specimen1.AVAILABLE_QUANTITY end as AVL_QUANTITY,"+ 
				"case when Specimen1.LINEAGE is null then '' else Specimen1.LINEAGE end as LINEAGE, "+
				"Specimen1.IDENTIFIER as IDENTIFIER "+
				"FROM   "+ 
				"CATISSUE_EXTERNAL_IDENTIFIER ExternalIdentifier1 , CATISSUE_SPECIMEN Specimen1 , catissue_spec_tag_items atg, catissue_specimen_coll_group scg "+ 
				" WHERE scg.identifier = Specimen1.SPECIMEN_COLLECTION_GROUP_ID AND "+ 
				"Specimen1.IDENTIFIER   =  ExternalIdentifier1.SPECIMEN_ID  AND "+
				" Specimen1.IDENTIFIER   =  ExternalIdentifier1.SPECIMEN_ID   AND atg.tag_id= "+ jsonString +" and "+ 
				"( ( Specimen1.IDENTIFIER  =atg.obj_id  )   AND UPPER(Specimen1.ACTIVITY_STATUS ) != UPPER('Disabled')  ) "+
				" ORDER BY Specimen1.IDENTIFIER ,ExternalIdentifier1.IDENTIFIER "; 
		
		

		setSessionData(sessionData);
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
		String gridColumns = "CHKBOX,SCG_NAME,LABEL,BARCODE,PARENTLABEL,CLASS,TYPE,AVL_QUANTITY,LINEAGE,IDENTIFIER,LABELONLY";
		return gridColumns;
	}
	
	public void beforeRender(com.dhtmlx.connector.DataItem data)
	{
	
		String specimenLabel =data.get_value("LABEL");
		String barcode =data.get_value("BARCODE");
	    data.set_value("LABELONLY",specimenLabel);
		StringBuffer specimenNameString= new StringBuffer();
		specimenNameString.append(specimenLabel);
		if(!Validator.isEmpty(barcode))
		{
			//if(!Validator.isEmpty(specimenLabel))
			specimenNameString.append(" (");
			specimenNameString.append(barcode);
			specimenNameString.append(")");
		}
		data.set_value("LABEL",specimenNameString.toString());
		
		String specimenClass =data.get_value("CLASS");
		String specimenType =data.get_value("TYPE");
	
		StringBuffer classNameStr= new StringBuffer();
		classNameStr.append(specimenClass);
		if(!Validator.isEmpty(specimenType))
		{
			classNameStr.append(" (");
			classNameStr.append(specimenType);
			classNameStr.append(")");
		}
		data.set_value("CLASS",classNameStr.toString());
		
		String pLabel = data.get_value("PARENTLABEL");
		if(Validator.isEmpty(pLabel))
			data.set_value("PARENTLABEL","");
		
	}
}
