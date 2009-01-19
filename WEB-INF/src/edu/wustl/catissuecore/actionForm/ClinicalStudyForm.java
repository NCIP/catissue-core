/**
 * <p>Title: ClinicalStudyForm Class>
 * <p>Description:  ClinicalStudyForm Class is used to encapsulate all the request parameters passed 
 * from User Add/Edit webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis. *
 * @author Shital lawhale 
 */

package edu.wustl.catissuecore.actionForm;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.ClinicalStudy;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * ClinicalStudyForm Class is used to encapsulate all the request
 * parameters passed from collection protocol Add/Edit webpage.
 * 
 */
public class ClinicalStudyForm extends SpecimenProtocolForm
{       
	private static final long serialVersionUID = 1L;
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(ClinicalStudyRegistrationForm.class);

	private final String ERROR_ITEM_REQD="errors.item.required";
    protected long []protocolCoordinatorIds;

    
    public ClinicalStudyForm()
    {
        super();
    }

    /**
     * Counter that contains number of rows in the 'Add More' functionality. outer block
     */
    private int outerCounter=1;
    

    /**
     * @return Returns the innerLoopValues.
     * 
     */
    protected Map innerLoopValues = new HashMap();
    
    
    
    /**
     * @return the innerLoopValues
     */
    public Map getInnerLoopValues()
    {
        return innerLoopValues;
    }

    
    /**
     * @param innerLoopValues the innerLoopValues to set
     */
    public void setInnerLoopValues(final Map innerLoopValues)
    {
        this.innerLoopValues = innerLoopValues;
    }

    
    /**
     * @return the outerCounter
     */
    public int getOuterCounter()
    {
        return outerCounter;
    }

    
    /**
     * @param outerCounter the outerCounter to set
     */
    public void setOuterCounter(final int outerCounter)
    {
        this.outerCounter = outerCounter;
    }

    
    /**
     * Associates the specified object with the specified key in the map.
     * @param key the key to which the object is mapped.
     * @param value the object which is mapped.
     */
    public void setIvl(final String key, final Object value)///changes here
    {
        if (isMutable())
        {
            innerLoopValues.put(key, value);
        }
    }

    /**
     * Returns the object to which this map maps the specified key.
     * 
     * @param key the required key.
     * @return the object to which this map maps the specified key.
     */
    public Object getIvl(final String key)
    {
        return innerLoopValues.get(key);
    }
    
    /**
     * for reset
     */
    protected void reset()
    {
    }
    
    /**
     * @return Returns the protocolcoordinator ids.
     */
    public long[] getProtocolCoordinatorIds()
    {
        return protocolCoordinatorIds;
    }

    /**
     * @param protocolCoordinatorIds The protocolCoordinatorIds to set.
     */
    public void setProtocolCoordinatorIds(long[] protocolCoordinatorIds)
    {
        this.protocolCoordinatorIds = protocolCoordinatorIds;
    }
    
    /**
     * Copies the data from an AbstractDomain object to a Clinicalstudy object.
     * @param abstractDomain An AbstractDomain object.
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        super.setAllValues(abstractDomain);     
        
        final  ClinicalStudy clinicalStudy = (ClinicalStudy)abstractDomain;        
        final Collection userCollection = clinicalStudy.getCoordinatorCollection();       
        if(userCollection != null)
        {
            protocolCoordinatorIds = new long[userCollection.size()];
            int cnt=0;
            final  Iterator iterator = userCollection.iterator();
            while(iterator.hasNext())
            {
            	final  User user = (User)iterator.next();
                protocolCoordinatorIds[cnt] = user.getId().longValue();
                cnt++;
            }
        }
        setId(abstractDomain.getId());
                
    }
    
    /**
     * 
     * Overrides the validate method of ActionForm.
     * @param mapping ActionMapping
     * @param request HttpServletRequest
     * @return errors
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {       
        ActionErrors errors = super.validate(mapping, request);     
        final Validator validator = new Validator();
        try
        {
            if(this.protocolCoordinatorIds != null && this.principalInvestigatorId!=-1)
            {
                for(int ind=0; ind < protocolCoordinatorIds.length; ind++)
                {
                    if(protocolCoordinatorIds[ind] == this.principalInvestigatorId)
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.pi.coordinator.same"));
                        break;
                    }
                }
            }
            
            if (validator.isEmpty(this.irbID))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ERROR_ITEM_REQD,ApplicationProperties.getValue("clinicalstudy.irbid")));
            }
            
//            System.out.println("Values Map: "+values);
            
            final Iterator iter = this.values.keySet().iterator();
            if(values.isEmpty())
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.one.item.required",ApplicationProperties.getValue("clinicalStudyEvent.eventTitle")));
            }
            // check for atleast 1 specimen requirement per CollectionProtocol Event
            /*for(int i=1;i<=outerCounter;i++)
            {
                String className = "CollectionProtocolEvent:"+i+"__specimenClass";
                Object obj = getValue(className);
                if(obj == null)
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.one.item.required",ApplicationProperties.getValue("collectionprotocol.specimenreq")));
                }
            }*/
            
            while (iter.hasNext())
            {
            	final String key = (String)iter.next();
            	final String value = (String)values.get(key);
                                
                if(key.indexOf("collectionPointLabel")!=-1 && validator.isEmpty(value))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ERROR_ITEM_REQD,ApplicationProperties.getValue("clinicalStudyEvent.label")));
                }
                
                if(key.indexOf("studyCalendarEventPoint")!=-1 && validator.isEmpty(value))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ERROR_ITEM_REQD,ApplicationProperties.getValue("clinicalStudyEvent.studycalendartitle")));
                }
                
                if(key.indexOf("noOfEntries")!=-1 )
                {
                    if (validator.isEmpty(value))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ERROR_ITEM_REQD,ApplicationProperties.getValue("clinicalStudyEvent.noOfEntries")));
                    }
                    else
                    {
                        if ("#".equals(value))
                        { values.put(key, "-1");}
                    }
                }
                
                if(key.indexOf("studyFormId")!=-1 && !validator.isValidOption(value))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("clinicalStudyEvent.studyForm")));
                }
                
                if(key.indexOf("studyFormLabel")!=-1 && validator.isEmpty(value))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ERROR_ITEM_REQD,ApplicationProperties.getValue("clinicalStudyEvent.studyFormLabel")));
                }
                
                
            }
            
            
            
            /*for (int i=0; i < getOuterCounter(); i++)
            {
            
                int innerCount = 1;
                if (getIvl(""+i) != null)
                    innerCount = Integer.parseInt(getIvl(""+i).toString());
                
                String eventLabelKey = Constants.CLINICAL_STUDY_EVENT + i + "_" + "_collectionPointLabel";
                if (values.get(eventLabelKey) == null)
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ERROR_ITEM_REQD,ApplicationProperties.getValue("clinicalStudyEvent.label")));
                                    }
                
                String noOfEntriesKey = Constants.CLINICAL_STUDY_EVENT + i + "_noOfEntries";
                
                String noOfEntriesValue = "";
                if (values.get(noOfEntriesKey) != null)
                    noOfEntriesValue = values.get(noOfEntriesKey).toString();
                
                if (noOfEntriesValue != null && noOfEntriesValue.equals(""))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ERROR_ITEM_REQD,ApplicationProperties.getValue("clinicalStudyEvent.noOfEntries")));
                }
                if (noOfEntriesValue != null && noOfEntriesValue.equals("#"))
                {
                    values.put(noOfEntriesKey, "-1");
                }
                for (int j=0; j< innerCount; j++)
                {
                                    
                    String studyFormIdKey = Constants.FORM_CONTEXT + i + "_" + j + "_studyFormId";
                    String studyFormLabelKey = Constants.FORM_CONTEXT + i + "_" + j + "_studyFormLabel";
                    
                    if (values.get(studyFormIdKey).equals(Constants.SELECT_OPTION_VALUE))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ERROR_ITEM_REQD,ApplicationProperties.getValue("clinicalStudyEvent.studyForm")));
                        
                    }
                    String studyFormLabelValue = values.get(studyFormLabelKey).toString();
                    
                    if (studyFormLabelValue != null && studyFormLabelValue.equals(""))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ERROR_ITEM_REQD,ApplicationProperties.getValue("clinicalStudyEvent.studyFormLabel")));
                        
                    }
                }
                
            }*/
                

        }
        catch (Exception excp)
        {
            // use of logger as per bug 79
        	logger.error(excp.getMessage(),excp); 
        	logger.debug(excp);
            errors = new ActionErrors();
        }
        return errors;
    }
        
    /**
     *@return Returns the id assigned to form bean
     */
    public int getFormId()
    {
        return Constants.CLINICALSTUDY_FORM_ID;
    }
    
    
    
    /**
     * This method sets Identifier of Objects inserted by AddNew activity in Form-Bean which initialized AddNew action
     * @param addNewFor - FormBean ID of the object inserted
     * @param addObjectIdentifier - Identifier of the Object inserted 
     */
    public void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier)
    {
        if("principalInvestigator".equals(addNewFor))
        {
            setPrincipalInvestigatorId(addObjectIdentifier.longValue());
        }
        else if("protocolCoordinator".equals(addNewFor))
        {
        	final  long []pcoordIDs = {Long.parseLong(addObjectIdentifier.toString())};           
            setProtocolCoordinatorIds(pcoordIDs);
        } 
    }   
    
 
}