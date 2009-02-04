package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This class is used add, update and retrieve text contents on the surgical pathology reports 
 * information using Hibernate.
 */
public class TextReportContentBizLogic extends IntegrationBizLogic
{


		/**
		 * Saves the Text content object in the database.
		 * @param obj The storageType object to be saved.
		 * @param session The session in which the object is saved.
		 * @throws DAOException 
		 */
		protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
		{
			TextContent textContent = (TextContent) obj;
			dao.insert(textContent, sessionDataBean, true, false);
	 	}

		/**
		 * Updates the Text content object in the database.
		 * @param obj The object to be updated.
		 * @param session The session in which the object is saved.
		 * @throws DAOException 
		 */
		protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
		{
			TextContent textContent = (TextContent) obj;
			dao.update(textContent, sessionDataBean, true, false, false);
		}

		/**
		 * This function returns the list of all the objects present in the TextContent table.
		 * @return - List of participants 
		 */
		public Map getAllTextContents() throws Exception
		{
			// Initialising instance of IBizLogic
			IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
			String sourceObjectName = TextContent.class.getName();

			// getting all the text content from the database 
			List listOfTextContents = bizLogic.retrieve(sourceObjectName);
			Map mapOfTextContents = new HashMap();
			for (int i = 0; i < listOfTextContents.size(); i++)
			{
				TextContent textContent = (TextContent) listOfTextContents.get(i);
				mapOfTextContents.put(textContent.getId(), textContent);
			}
			return mapOfTextContents;

		}

		/**
		 * This function takes identifier as parameter and returns corresponding TextContent
		 * @return - TextContent object
		 */
		public TextContent getTextContentById(Long identifier) throws Exception
		{
			// Initialising instance of IBizLogic
			IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
			String sourceObjectName = TextContent.class.getName();

			// getting all the participants from the database 
			List textContentList = bizLogic.retrieve(sourceObjectName, Constants.ID, identifier);
			TextContent textContent = (TextContent) textContentList.get(0);
			return textContent;

		}
	}

