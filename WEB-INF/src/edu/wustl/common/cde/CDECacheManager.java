/*
 * Created on Aug 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.cde;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.bizlogic.CDEBizLogic;
import edu.wustl.catissuecore.exception.BizLogicException;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.xml.XMLCDE;
import edu.wustl.common.cde.xml.XMLPermissibleValueType;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CDECacheManager
{
    public void refresh(Map cdeXMLMAP)
    {
    	Logger.out.info("Initializing CDE Cache Manager");
        CDEDownloader cdeDownloader = null;
        List downloadedCDEList = new ArrayList();
        List errorLogs = new ArrayList();
        
        try
        {
            cdeDownloader = new CDEDownloader();
            cdeDownloader.connect();
        }
        catch (Exception exp)
        {
            //Logging the error message.
            errorLogs.add(exp.getMessage());
            return;
        }
        
        Set ketSet = cdeXMLMAP.keySet();
        Iterator it = ketSet.iterator();
        while (it.hasNext())
        {
            Object key = it.next();
            XMLCDE xmlCDE = (XMLCDE) cdeXMLMAP.get(key);
            if (xmlCDE.isCache())
            {
                try
                {
                    CDE cde = cdeDownloader.downloadCDE(xmlCDE);
                    
                    configurePermissibleValues(cde, xmlCDE);
                    downloadedCDEList.add(cde);
                }
                catch(Exception exp)
                {
                    errorLogs.add(exp.getMessage());
                }
            }
        }
        
        //Inserting the downloaded CDEs into database.
        CDEBizLogic cdeBizLogic = new CDEBizLogic();
        Iterator iterator = downloadedCDEList.iterator();
        while (iterator.hasNext())
        {
            CDE cde = (CDE) iterator.next();
            try
            {
                cdeBizLogic.insert(cde, null, Constants.HIBERNATE_DAO);
            }
            catch (UserNotAuthorizedException userNotAuthExp)
            {
                errorLogs.add(userNotAuthExp.getMessage());
            }
            catch (BizLogicException bizLogicExp)
            {
                errorLogs.add(bizLogicExp.getMessage());
            }
        }
    }
    
    /**
     * Sets the parent permissible values for each of the permissible value of the CDE 
     * depending on the parent-child relationships present in the XMlCDE provided. 
     * @param cde The CDE whose permissible values are to be configured.
     * @param xmlCDE The XMLCDE object for the cde which contains the parent-child relationships between the 
     * 				 permissible values.  
     */
    private void configurePermissibleValues(CDE cde, XMLCDE xmlCDE)
    {
        Set configuredPermissibleValues = new HashSet();
        Set permissibleValues = cde.getPermissibleValues();
        
        Iterator iterator = xmlCDE.getXMLPermissibleValues().iterator();
        while (iterator.hasNext())
        {
            XMLPermissibleValueType xmlPermissibleValueType = (XMLPermissibleValueType) iterator.next();
            
            //The permissible value.
            PermissibleValueImpl permissibleValue = (PermissibleValueImpl)getPermissibleValueObject(permissibleValues,
                    								xmlPermissibleValueType.getConceptCode());
            
            if (permissibleValue != null)
            {
                // If the parent permissible value concept code is null set the cde value for the permissible value.
                if (xmlPermissibleValueType.getParentConceptCode() == null || 
                        xmlPermissibleValueType.getParentConceptCode().equals(""))
                {
                    permissibleValue.setCde(cde);
                    permissibleValue.setParentPermissibleValue(null);
                }
                else// If the parent permissible value concept code is not null, set the parent permissible value 
                {// and set the cde as null.
                    //Parent permissible value.
                    PermissibleValue parentPermissibleValue = getPermissibleValueObject(permissibleValues,
                            									xmlPermissibleValueType.getParentConceptCode());
                    
                    //Set the parent permissible value of this permissible value.
                    permissibleValue.setParentPermissibleValue(parentPermissibleValue);
                    permissibleValue.setCde(null);
                }
                
                configuredPermissibleValues.add(permissibleValue);
            }
        }
        
        //Get the permissible values from the set whose relationship is not present in the xml file.
        permissibleValues.removeAll(configuredPermissibleValues);
        
        //Set the CDE for all the above permissible values.
        //i.e. put these permissible values under the cde in the tree structure.
        Iterator it = permissibleValues.iterator();
        while (it.hasNext())
        {
            PermissibleValueImpl permissibleValueImpl = (PermissibleValueImpl) it.next();
            permissibleValueImpl.setCde(cde);
            permissibleValueImpl.setParentPermissibleValue(null);
        }
        
        if (!permissibleValues.isEmpty())
        {
            configuredPermissibleValues.addAll(permissibleValues);
        }
        
        //Set the configured permissible value set to the cde.  
        CDEImpl cdeImpl = (CDEImpl)cde;
        cdeImpl.setPermissibleValues(configuredPermissibleValues);
    }
    
    /**
     * Returns the permissible value object for the concept code from the Set of permissible values.
     * @param permissibleValues The Set of permissible values.
     * @param conceptCode The conceptCode whose permissible value object is required.
     * @return the permissible value object for the concept code from the Set of permissible values.
     */
    private PermissibleValue getPermissibleValueObject(Set permissibleValues, String conceptCode)
    {
        Iterator iterator = permissibleValues.iterator();
        while (iterator.hasNext())
        {
            PermissibleValue permissibleValue = (PermissibleValue)iterator.next();
            if (permissibleValue.getValue().equals(conceptCode))
            {
                return permissibleValue;
            }
        }
        
        return null;
    }
}