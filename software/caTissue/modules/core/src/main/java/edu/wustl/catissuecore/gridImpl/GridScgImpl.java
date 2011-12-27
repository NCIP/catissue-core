
package edu.wustl.catissuecore.gridImpl;


import edu.wustl.catissuecore.processor.SPPEventProcessor;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;

public class GridScgImpl extends AbstractGridImpl
{

	private static final String EDITID = "EDITID";

	/* (non-Javadoc)
	 * @see edu.wustl.healthstreet.gridImpl.AbstractGridImpl#getGridQuery(java.lang.String)
	 */
	public String getGridQuery(String jsonString, SessionDataBean sessionData) throws BizLogicException
	{
		setSessionData(sessionData);
		String query = null;
		String cpIds = new SPPEventProcessor().getCPIds(sessionData);//getReadDeniedIds(sessionData.getUserName());
		if(cpIds.length() == 0)
		{
			query = "select scg.IDENTIFIER,sp.TITLE,scg.NAME,cpr.PROTOCOL_PARTICIPANT_ID," +
			"cpe.COLLECTION_POINT_LABEL,cp.FIRST_NAME,cp.LAST_NAME " +
			"from CATISSUE_SPECIMEN_COLL_GROUP scg " +
			"inner join catissue_coll_prot_event cpe on cpe.IDENTIFIER = scg.COLLECTION_PROTOCOL_EVENT_ID " +
			"inner join catissue_specimen_protocol sp on sp.IDENTIFIER = cpe.COLLECTION_PROTOCOL_ID " +
			"inner join catissue_coll_prot_reg cpr on cpr.IDENTIFIER = scg.COLLECTION_PROTOCOL_REG_ID " +
			"inner join catissue_participant cp on cp.IDENTIFIER = cpr.PARTICIPANT_ID " +
			"inner join catissue_cpe_spp ccs on ccs.cpe_identifier = scg.COLLECTION_PROTOCOL_EVENT_ID " +
			"where ccs.spp_identifier="+jsonString +" and scg.COLLECTION_STATUS not in ('Pending','overdue','not collected')" +
					" and scg.identifier not in (select csapp.scg_identifier from catissue_spp_application csapp where csapp.SCG_IDENTIFIER =scg.IDENTIFIER  and ccs.spp_identifier = csapp.spp_identifier)";
		}
		else
		{
			query = "select scg.IDENTIFIER,sp.TITLE,scg.NAME,cpr.PROTOCOL_PARTICIPANT_ID," +
					"cpe.COLLECTION_POINT_LABEL,cp.FIRST_NAME,cp.LAST_NAME " +
					"from CATISSUE_SPECIMEN_COLL_GROUP scg " +
					"inner join catissue_coll_prot_event cpe on cpe.IDENTIFIER = scg.COLLECTION_PROTOCOL_EVENT_ID " +
					"inner join catissue_specimen_protocol sp on sp.IDENTIFIER = cpe.COLLECTION_PROTOCOL_ID " +
					"inner join catissue_coll_prot_reg cpr on cpr.IDENTIFIER = scg.COLLECTION_PROTOCOL_REG_ID " +
					"inner join catissue_participant cp on cp.IDENTIFIER = cpr.PARTICIPANT_ID " +
					"inner join catissue_cpe_spp ccs on ccs.cpe_identifier = scg.COLLECTION_PROTOCOL_EVENT_ID " +
					"where ccs.spp_identifier="+jsonString +" and scg.COLLECTION_STATUS not in ('Pending','overdue','not collected')" +
							" and scg.identifier not in (select csapp.scg_identifier from catissue_spp_application csapp where csapp.SCG_IDENTIFIER =scg.IDENTIFIER  and ccs.spp_identifier = csapp.spp_identifier) and sp.IDENTIFIER not in ("+cpIds+")";
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
		String gridColumns = "IDENTIFIER,TITLE,PROTOCOL_PARTICIPANT_ID,FIRST_NAME,NAME,COLLECTION_POINT_LABEL";
		return gridColumns;

	}


	public void beforeRender(com.dhtmlx.connector.DataItem data)
	{
		SessionDataBean sessionData = getSessionData();
		String[] columnString;
		String id = data.get_value("IDENTIFIER");
		data.set_value("IDENTIFIER","<input type='checkbox' name='"+id+"' value='"+id+"'/>");

		String fName =data.get_value("FIRST_NAME");
		String lName =data.get_value("LAST_NAME");
		StringBuffer participantName= new StringBuffer();
		/*if(sessionData != null && !sessionData.isAdmin())
		{
			participantName.append("####");
		}
		else
		{*/
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
		//}
		data.set_value("FIRST_NAME",participantName.toString());
	}
}
