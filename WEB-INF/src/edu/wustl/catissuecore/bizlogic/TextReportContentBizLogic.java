
package edu.wustl.catissuecore.bizlogic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

/**
 * This class is used add, update and retrieve text contents on the surgical pathology reports.
 * information using Hibernate.
 */
public class TextReportContentBizLogic extends CatissueDefaultBizLogic
{

	private transient final Logger logger = Logger.getCommonLogger(TextReportContentBizLogic.class);

	/**
	 * Saves the Text content object in the database.
	 * @param dao DAO object
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws BizLogicException throws BizLogicException
	 */
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final TextContent textContent = (TextContent) obj;
			dao.insert(textContent);
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace() ;
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	/**
	 * Updates the Text content object in the database.
	 * @param dao DAO object
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws BizLogicException throws BizLogicException
	 */
	@Override
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final TextContent textContent = (TextContent) obj;
			dao.update(textContent);
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * This function returns the list of all the objects present in the TextContent table.
	 * @return Map - Key : Long, value:TextContent
	 * @throws Exception throws Exception
	 */
	public Map<Long, TextContent> getAllTextContents() throws Exception
	{
		// Initialising instance of IBizLogic
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		final String sourceObjectName = TextContent.class.getName();

		// getting all the text content from the database 
		final List listOfTextContents = bizLogic.retrieve(sourceObjectName);
		final Map<Long, TextContent> mapOfTextContents = new HashMap();
		for (int i = 0; i < listOfTextContents.size(); i++)
		{
			final TextContent textContent = (TextContent) listOfTextContents.get(i);
			mapOfTextContents.put(textContent.getId(), textContent);
		}
		return mapOfTextContents;

	}

	/**
	 * This function takes identifier as parameter and returns corresponding TextContent
	 * @param identifier - identifier
	 * @return TextContent object
	 * @throws Exception - throws Exception
	 */
	public TextContent getTextContentById(Long identifier) throws Exception
	{
		// Initialising instance of IBizLogic
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		final String sourceObjectName = TextContent.class.getName();

		// getting all the participants from the database 
		final Object object = bizLogic.retrieve(sourceObjectName, identifier);
		final TextContent textContent = (TextContent) object;
		return textContent;

	}
}
