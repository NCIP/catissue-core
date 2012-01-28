
package edu.wustl.catissuecore.gridImpl;


import edu.wustl.catissuecore.processor.SPPEventProcessor;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;

public class GridSpecimenImpl extends AbstractGridImpl
{

	private static final String EDITID = "EDITID";

	/* (non-Javadoc)
	 * @see edu.wustl.healthstreet.gridImpl.AbstractGridImpl#getGridQuery(java.lang.String)
	 */
	public String getGridQuery(String jsonString, SessionDataBean sessionData) throws BizLogicException
	{
		setSessionData(sessionData);
		SPPEventProcessor processor = new SPPEventProcessor();
		String cpIds = processor.getCPIds(sessionData);
		String query = null;
		if(cpIds.length() == 0)
		{
			query = "select cs.IDENTIFIER,sp.TITLE,cpr.PROTOCOL_PARTICIPANT_ID ,participant.FIRST_NAME,participant.LAST_NAME,scg.NAME, " +
					"cpe.COLLECTION_POINT_LABEL,cs.LABEL, cas.SPECIMEN_TYPE " +
					"from catissue_specimen cs " +
					"inner join catissue_specimen_coll_group scg on scg.identifier = cs.SPECIMEN_COLLECTION_GROUP_ID " +
					"inner join catissue_coll_prot_event cpe on cpe.IDENTIFIER = scg.COLLECTION_PROTOCOL_EVENT_ID " +
					"inner join catissue_specimen_protocol sp on sp.IDENTIFIER = cpe.COLLECTION_PROTOCOL_ID " +
					"inner join catissue_coll_prot_reg cpr on cpr.IDENTIFIER = scg.COLLECTION_PROTOCOL_REG_ID  " +
					"inner join catissue_participant participant on participant.IDENTIFIER = cpr.PARTICIPANT_ID " +
					"inner join catissue_cp_req_specimen cprs on cprs.identifier = cs.REQ_SPECIMEN_ID  " +
					"inner join catissue_abstract_specimen cas on cas.IDENTIFIER = cs.IDENTIFIER " +
					"where cprs.SPP_IDENTIFIER ="+jsonString+"  and cs.COLLECTION_STATUS ='Collected' and cs.activity_status='Active' and cs.identifier not in (select caa.SPECIMEN_ID from catissue_action_application  caa where caa.spp_app_identifier = cs.spp_application_id)";
		}
		else
		{
			query = "select cs.IDENTIFIER,sp.TITLE,cpr.PROTOCOL_PARTICIPANT_ID ,participant.FIRST_NAME,participant.LAST_NAME,scg.NAME, " +
			"cpe.COLLECTION_POINT_LABEL,cs.LABEL, cas.SPECIMEN_TYPE " +
			"from catissue_specimen cs " +
			"inner join catissue_specimen_coll_group scg on scg.identifier = cs.SPECIMEN_COLLECTION_GROUP_ID " +
			"inner join catissue_coll_prot_event cpe on cpe.IDENTIFIER = scg.COLLECTION_PROTOCOL_EVENT_ID " +
			"inner join catissue_specimen_protocol sp on sp.IDENTIFIER = cpe.COLLECTION_PROTOCOL_ID " +
			"inner join catissue_coll_prot_reg cpr on cpr.IDENTIFIER = scg.COLLECTION_PROTOCOL_REG_ID  " +
			"inner join catissue_participant participant on participant.IDENTIFIER = cpr.PARTICIPANT_ID " +
			"inner join catissue_cp_req_specimen cprs on cprs.identifier = cs.REQ_SPECIMEN_ID  " +
			"inner join catissue_abstract_specimen cas on cas.IDENTIFIER = cs.IDENTIFIER " +
			"where cprs.SPP_IDENTIFIER ="+jsonString+"  and cs.COLLECTION_STATUS ='Collected' and cs.activity_status='Active' and cs.identifier not in (select caa.SPECIMEN_ID from catissue_action_application  caa where caa.spp_app_identifier = cs.spp_application_id)" +
					"and sp.identifier not in ("+cpIds+")";
		}
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
		String gridColumns = "IDENTIFIER,TITLE,PROTOCOL_PARTICIPANT_ID,FIRST_NAME,NAME,COLLECTION_POINT_LABEL,LABEL,SPECIMEN_TYPE";
		return gridColumns;

	}


	public void beforeRender(com.dhtmlx.connector.DataItem data)
	{
		String[] columnString;
		String id = data.get_value("IDENTIFIER");
		data.set_value("IDENTIFIER","<input type='checkbox' name='"+id+"' value='"+id+"'/>");

		String fName =data.get_value("FIRST_NAME");
		String lName =data.get_value("LAST_NAME");

		StringBuffer participantName= new StringBuffer();

		if (fName == null && lName == null)
		{
			participantName.append("N/A");
		}
		else
		{
			if (lName != null && lName.trim().length() > 0
					&& fName != null
					&& fName.length() > 0)
			{
				participantName.append(lName);
				participantName.append("&nbsp;");
				participantName.append(",");
				participantName.append("&nbsp;");
				participantName.append(fName);
			}
			else
			{
				if (fName != null && fName.trim().length() > 0)
				{
					participantName.append(fName);
				}
				else if (lName != null && lName.trim().length() > 0)
				{
					participantName.append(lName);
				}
			}
		}
		data.set_value("FIRST_NAME",participantName.toString());
	}
}
