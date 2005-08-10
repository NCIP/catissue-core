/*
 * Created on Aug 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.bizlogic;

import java.util.List;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.SignUpUser;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SignUpUserBizLogic extends DefaultBizLogic
{
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.bizlogic.DefaultBizLogic#insert(edu.wustl.catissuecore.dao.DAO, java.lang.Object)
     */
    protected void insert(DAO dao, Object obj) throws DAOException
    {
        SignUpUser user = (SignUpUser) obj;
        
        Department department = null;
        Institution institution = null;
        CancerResearchGroup cancerResearchGroup = null;

        List list = dao.retrieve(Department.class.getName(),
                "systemIdentifier", user.getDepartment()
                        .getSystemIdentifier());
        if (list.size() != 0)
        {
            department = (Department) list.get(0);
        }

        list = dao.retrieve(Institution.class.getName(),
                "systemIdentifier", user.getInstitution()
                        .getSystemIdentifier());
        if (list.size() != 0)
        {
            institution = (Institution) list.get(0);
        }

        list = dao.retrieve(CancerResearchGroup.class.getName(),
                "systemIdentifier", user.getCancerResearchGroup()
                        .getSystemIdentifier());
        if (list.size() != 0)
        {
            cancerResearchGroup = (CancerResearchGroup) list.get(0);
        }
        
        user.setDepartment(department);
        user.setInstitution(institution);
        user.setCancerResearchGroup(cancerResearchGroup);
        
        dao.insert(user,true);
    }

}
