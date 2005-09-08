/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.dao.JDBCDAO;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

public class QueryBizLogic extends DefaultBizLogic
{

    private static final String ALIAS_NAME_TABLE_NAME_MAP_QUERY = 
        "select ALIAS_NAME ,TABLE_NAME from " +
        "`catissuecore`.`catissue_query_interface_table_data`";

    public static HashMap getQueryObjectNameTableNameMap()
    {
        List list = null;
        HashMap queryObjectNameTableNameMap = new HashMap();
        JDBCDAO dao =null;
        try
        {
            dao = new JDBCDAO();
            dao.openSession();
            list = dao.executeQuery(ALIAS_NAME_TABLE_NAME_MAP_QUERY);

            Iterator iterator = list.iterator();
            while (iterator.hasNext())
            {
                List row = (List) iterator.next();
                queryObjectNameTableNameMap.put(row.get(0), row.get(1));
                Logger.out.info("Key: "+row.get(0)+" value: "+row.get(1));
            }

            
        }
        catch (DAOException daoExp)
        {
            Logger.out.debug("Could not obtain table object relation. Exception:"
                    + daoExp.getMessage(), daoExp);
        }
        catch (ClassNotFoundException classExp)
        {
            Logger.out.debug("Could not obtain table object relation. Exception:"
                    + classExp.getMessage(), classExp);
        }
        finally
        {
            try
            {
                dao.closeSession();
            }
            catch (DAOException e)
            {
                Logger.out.debug(e.getMessage(),e);
            }
        }
        return queryObjectNameTableNameMap;
    }
    
   

}