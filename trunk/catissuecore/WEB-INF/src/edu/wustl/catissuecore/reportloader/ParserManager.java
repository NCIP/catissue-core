package edu.wustl.catissuecore.reportloader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.ehcache.CacheException;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author sandeep_ranade
 * Represents a factory pattern for parsers. It creates particular parser instance
 * depending upon the parameter.   
 */
public final class ParserManager

 {

	/**
	 * ParserManager 
	 */
	private static ParserManager parserManager=null;
	
	/**
	 * Constructor
	 */
	private ParserManager()
	{
	}
	
	
	/**
	 * @return instance of ParserManager.
	 */
	public static synchronized ParserManager getInstance()
	{
		if(parserManager==null)
		{
			parserManager=new ParserManager();
			parserManager.initialize();
		}
		return parserManager;
	}
	
	/**
	 * initializes the parsser manager
	 */
	private void initialize()
	{
		// participant map for cache manager
		Map participantMap = null;
		ParticipantBizLogic bizlogic = (ParticipantBizLogic) BizLogicFactory.getInstance()
				.getBizLogic(Participant.class.getName());
		Map cpMap = null;
		List cprList=null;
		try
		{
			BizLogicFactory bizFactory=BizLogicFactory.getInstance();
			CollectionProtocolRegistrationBizLogic cpbizlogic= (CollectionProtocolRegistrationBizLogic) bizFactory.getBizLogic(CollectionProtocolRegistration.class.getName());
			cprList=cpbizlogic.getAllParticipantRegistrationInfo();
			// get all participant list to set to chache manager
			participantMap = bizlogic.getAllParticipants();
		}
		catch (Exception ex)
		{
			Logger.out.error("Exception occured getting List of Participants " ,ex);
		}
		// getting instance of catissueCoreCacheManager and adding participantMap to cache
		try
		{
			// get instance of catissueCoreCacheManager
			CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager
			.getInstance();
			// add objects to cacheManager
			catissueCoreCacheManager.addObjectToCache(Constants.MAP_OF_PARTICIPANTS, (HashMap) participantMap);
			catissueCoreCacheManager.addObjectToCache(edu.wustl.catissuecore.util.global.Constants.LIST_OF_REGISTRATION_INFO, (Vector)cprList);
		}
		catch (CacheException e)
		{
			Logger.out
					.error("Exception occured while creating instance of CatissueCoreCacheManager" , e);
		}	
	}
	
	/**
	 * @param type represents the parser type.
	 * @return the parser depending upon the type specified as input.   
	 */
	public Parser getParser(String type)
	{
		Parser parser=null; 
		if(type.equals(Parser.HL7_PARSER))
		{	
			parser=new HL7Parser();
		}	
		return parser;
	}


	
	
}
