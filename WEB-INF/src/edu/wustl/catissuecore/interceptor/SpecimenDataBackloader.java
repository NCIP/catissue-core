package edu.wustl.catissuecore.interceptor;

import java.util.TimerTask;
/*
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.interceptor.wmq.SpecimenWmqProcessor;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.interceptor.SaveUpdateInterceptThread.eventType;
import edu.wustl.dao.query.generator.ColumnValueBean;
*/

public class SpecimenDataBackloader extends TimerTask
{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	/*static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}

	private static final Logger LOGGER = Logger.getCommonLogger(SpecimenDataBackloader.class);

	private static final String SPECIMEN_ID_QUERY = " select specimen.identifier from catissue_specimen specimen "
			+"join catissue_specimen_coll_group scg on  scg.identifier = specimen.specimen_collection_group_id and specimen.collection_status = 'Collected' and specimen.activity_status not like 'Disabled' "
			+"join  catissue_coll_prot_reg cpr on cpr.identifier = scg.collection_protocol_reg_id "
			+"join catissue_collection_protocol cp on cp.identifier = cpr.collection_protocol_id and cp.is_empi_enable = 1 "
			+"join catissue_participant participant on participant.identifier = cpr.participant_id and participant.empi_id is not null"
			+ " where specimen.identifier not in (select specimen_id from catissue_specimen_message_log )";
	*//**
	 * This method called to get JDBCDAO instance.
	 * @return JDBCDAO instance.
	 * @throws BizLogicException BizLogicException
	 *//*
	protected JDBCDAO openJDBCSession() throws DAOException
	{
		JDBCDAO jdbcDAO = null;

			final String applicationName = CommonServiceLocator.getInstance().getAppName();
			jdbcDAO = DAOConfigFactory.getInstance().getDAOFactory(applicationName).getJDBCDAO();
			jdbcDAO.openSession(null);

			return jdbcDAO;
	}

	*//**
	 * This method called to close JDBC session.
	 * @param jdbcDAO jdbcDAO
	 * @throws BizLogicException BizLogicException
	 *//*
	protected void closeJDBCSession(JDBCDAO jdbcDAO) throws DAOException
	{

			if (jdbcDAO != null)
			{
				jdbcDAO.closeSession();
			}


	}


	public void sendOldSpeciemnDataToCider()
	{
		final String path = System.getProperty("app.propertiesFile");
		try
		{
			XMLPropertyHandler.init(path);
			LOGGER.info("sysout WMQ initialazation in progress");
			SpecimenWmqProcessor.getInstance();
			//fetch all the specimens which are in collected state
			List<Long> specimenIdList = getSpecimenIdentifiersList();
			final DefaultBizLogic bizLogic = new DefaultBizLogic();

			LOGGER.info("Number of Specimen's in backLoad " + specimenIdList.size());
			SpecimenInterceptor interceptor = new SpecimenInterceptor();
			//iterate on each specimen & then send the information to the CIDER using specimen Interceptor.
			for(Long specimenId : specimenIdList)
			{
				LOGGER.info("Processing Specimen with ID "+specimenId);
				 Specimen specimen =(Specimen) bizLogic.retrieve(Specimen.class.getName(), specimenId);
				 interceptor.process(specimen, eventType.onInsert);
			}

		}
		catch (Exception e)
		{
			LOGGER.error("Error occured while processing the specimens",e);
		}

	}

	private List<Long> getSpecimenIdentifiersList() throws DAOException
	{
		final List<Long> specimenIdList = new ArrayList<Long>();
		JDBCDAO  dao = null;
		try
		{
			dao = openJDBCSession();
		List<ColumnValueBean> colValueBeanList = new ArrayList<ColumnValueBean>();
		//colValueBeanList.add(new ColumnValueBean("SHORT_TITLE", shortTitle));
		final List<List<String>> resultList = dao.executeQuery(SPECIMEN_ID_QUERY, colValueBeanList);
			for(List<String> recordList : resultList)
			{
				specimenIdList.add(Long.parseLong(recordList.get(0)));
			}
		}
		finally
		{
			closeJDBCSession(dao);
		}
			LOGGER.info("Specimen data backload completed successfully.");
		return specimenIdList;
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		sendOldSpeciemnDataToCider();
	}


*/}
