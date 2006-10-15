/**
 * <p>Title: BiohazardHDAO Class>
 * <p>Description:	BiohazardHDAO is used to add biohazard information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Dec 28, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.List;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;

/**
 * BiohazardHDAO is used to add biohazard information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class BiohazardBizLogic extends DefaultBizLogic
{

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		Biohazard hazard = (Biohazard) obj;
		String message = "";
		// Added by Ashish
		if (hazard == null)
		{
			message = ApplicationProperties.getValue("app.biohazard");
			throw new DAOException(ApplicationProperties.getValue("domain.object.null.err.msg",message));	
		}

		Validator validator = new Validator();
		if (validator.isEmpty(hazard.getName()))
		{
			message = ApplicationProperties.getValue("biohazard.name");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}

		if (!validator.isValidOption(hazard.getType()) || validator.isEmpty(hazard.getType()))
		{
			message = ApplicationProperties.getValue("biohazard.type");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	

		} 
		//END
		List biohazardList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_BIOHAZARD, null);
		if (!Validator.isEnumeratedValue(biohazardList, hazard.getType()))
		{
			throw new DAOException(ApplicationProperties.getValue("type.errMsg"));
		}

		return true;
	}
}