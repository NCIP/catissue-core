/**
 * 
 */

package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.List;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.querysuite.bizlogic.QueryBizLogic;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * @author chetan_patil
 * @created Sep 13, 2007, 7:39:46 PM
 */
public class CatissuecoreQueryBizLogic extends DefaultBizLogic {

    private QueryBizLogic<IParameterizedQuery> queryBizLogic = new QueryBizLogic<IParameterizedQuery>();

    @Override
    protected void preInsert(Object obj1, DAO dao1, SessionDataBean sessiondatabean) throws DAOException,
            UserNotAuthorizedException {
        queryBizLogic.preProcessQuery((IParameterizedQuery) obj1);
    }

    @Override
    protected void preUpdate(DAO dao1, Object obj, Object obj1, SessionDataBean sessiondatabean)
            throws BizLogicException, UserNotAuthorizedException {
        queryBizLogic.preProcessQuery((IParameterizedQuery) obj1);
    }

    @Override
    public List retrieve(String sourceObjectName, String[] selectColumnName, String[] whereColumnName,
            String[] whereColumnCondition, Object[] whereColumnValue, String joinCondition) throws DAOException {
        List list = super.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition,
                whereColumnValue, joinCondition);
        for (Object o : list) {
            queryBizLogic.postProcessQuery((IParameterizedQuery) o);
        }
        return list;
    }

}
