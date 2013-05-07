/**
 * <p>
 * Title: BiohazardHDAO Class>
 * <p>
 * Description: BiohazardHDAO is used to add biohazard information into the
 * database using Hibernate.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 * 
 * @author Aniruddha Phadnis
 * @version 1.00 Created on Dec 28, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.List;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.DAO;

/**
 * BiohazardHDAO is used to add biohazard information into the database using
 * Hibernate.
 * 
 * @author aniruddha_phadnis
 */
public class BiohazardBizLogic extends CatissueDefaultBizLogic
{

	/**
	 * Overriding the parent class's 
	 * method to validate the enumerated attribute
	 * values
	 */
	/**
	 * @param 
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		final Biohazard hazard = (Biohazard) obj;
		String message = "";
		// Added by Ashish
		if (hazard == null)
		{
			message = ApplicationProperties.getValue("app.biohazard");
			throw this.getBizLogicException(null, "domain.object.null.err.msg", message);
		}

		final Validator validator = new Validator();
		if (Validator.isEmpty(hazard.getName()))
		{
			message = ApplicationProperties.getValue("biohazard.name");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}

		if (!validator.isValidOption(hazard.getType()) || Validator.isEmpty(hazard.getType()))
		{
			message = ApplicationProperties.getValue("biohazard.type");
			throw this.getBizLogicException(null, "errors.item.required", message);

		}
		// END
		final List biohazardList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_BIOHAZARD, null);
		if (!Validator.isEnumeratedValue(biohazardList, hazard.getType()))
		{
			throw this.getBizLogicException(null, "type.errMsg", "");
		}

		return true;
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO,
	 *      java.lang.Object)
	 */
	@Override
	public String getObjectId(DAO dao, Object domainObject)
	{
		return edu.wustl.catissuecore.util.global.Constants.ADMIN_PROTECTION_ELEMENT;
	}

	/**
	 * To get PrivilegeName for authorization check from
	 * 'PermissionMapDetails.xml' (non-Javadoc)
	 * 
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	@Override
	protected String getPrivilegeKey(Object domainObject)
	{
		return edu.wustl.catissuecore.util.global.Constants.ADD_EDIT_BIOHAZARD;
	}
}