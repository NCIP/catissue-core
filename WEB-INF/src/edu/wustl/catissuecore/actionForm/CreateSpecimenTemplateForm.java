package edu.wustl.catissuecore.actionForm;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


public class CreateSpecimenTemplateForm extends AbstractActionForm
{
	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(CreateSpecimenTemplateForm.class);


	/**
	 * Display Name
	 */
	protected String displayName;

	/**
	 * Type of specimen. e.g. Tissue, Molecular, Cell, Fluid
	 */
	protected String className;

	/**
	 * Sub Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 */
	protected String type;


	/**
     * Anatomic site from which the specimen was derived.
     */
    private String tissueSite;

    /**
     * For bilateral sites, left or right.
     */
    private String tissueSide;

    /**
     * Histopathological character of the specimen 
     * e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
     */
    private String pathologicalStatus;
    
    private String storageLocationForSpecimen;
    
    private String storageLocationForAliquotSpecimen;
    
        
	/**
	 * Concentration of specimen.
	 */
	protected String concentration="0";

	/**
	 * Amount of Specimen.
	 */
	protected String quantity;
	
	/**
     * A historical information about the specimen i.e. whether the specimen is a new specimen
     * or a derived specimen or an aliquot.
     */
    private String lineage;
    
    /**
	 * Unit of specimen.
	 */
	protected String unit;
    
	
	 /**
     * A number that tells how many aliquots to be created.
     */
    private String noOfAliquots;
    
    /**
     * Initial quantity per aliquot.
     */
    private String quantityPerAliquot;
    
    /**
	 * Collection of aliquot specimens derived from this specimen. 
	 */
	protected Collection aliquotSpecimenCollection;
	
	/**
	 * Collection of derive specimens derived from this specimen. 
	 */
	protected Collection deriveSpecimenCollection ;
     
	
	private long collectionEventId;																											// Mandar : CollectionEvent 10-July-06
	private long collectionEventSpecimenId;
	private long collectionEventUserId;
	private String collectionUserName = null;
		
	
	/**
	 * 
	 * @return collectionUserName
	 */
	public String getCollectionUserName()
	{
		return collectionUserName;
	}

	/**
	 * 
	 * @param collectionUserName collectionUserName
	 */
	public void setCollectionUserName(String collectionUserName)
	{
		this.collectionUserName = collectionUserName;
	}

	private long receivedEventId;
	private long receivedEventSpecimenId;
	private long receivedEventUserId;
	private String receivedUserName = null;
	
	/**
	 * 
	 * @return receivedUserName
	 */
	public String getReceivedUserName()
	{
		return receivedUserName;
	}

	/**
	 * 
	 * @param receivedUserName receivedUserName
	 */
	public void setReceivedUserName(String receivedUserName)
	{
		this.receivedUserName = receivedUserName;
	}

	private String collectionEventCollectionProcedure;
	
	private String collectionEventContainer;
	
	private String receivedEventReceivedQuality;
	
	private int noOfDeriveSpecimen;
    
    private Map deriveSpecimenValues = new LinkedHashMap();
        
	private String nodeKey = null;
    
	public String getClassName()
	{
		return className;
	}

	
	public void setClassName(String className)
	{
		this.className = className;
	}

	
	public String getType()
	{
		return type;
	}

	
	public void setType(String type)
	{
		this.type = type;
	}

	
	public String getTissueSite()
	{
		return tissueSite;
	}

	
	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}

	
	public String getTissueSide()
	{
		return tissueSide;
	}

	
	public void setTissueSide(String tissueSide)
	{
		this.tissueSide = tissueSide;
	}

	
	public String getPathologicalStatus()
	{
		return pathologicalStatus;
	}

	
	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	
	public String getConcentration()
	{
		return concentration;
	}

	
	public void setConcentration(String concentration)
	{
		this.concentration = concentration;
	}

	
	public String getQuantity()
	{
		return quantity;
	}

	
	public void setQuantity(String quantity)
	{
		this.quantity = quantity;
	}

	
	public String getNoOfAliquots()
	{
		return noOfAliquots;
	}

	
	public void setNoOfAliquots(String noOfAliquots)
	{
		this.noOfAliquots = noOfAliquots;
		if(noOfAliquots!=null&&noOfAliquots.equals("0"))
		{
			this.noOfAliquots = "";
		}
		
	}

	
	public String getQuantityPerAliquot()
	{
		return quantityPerAliquot;
	}

	
	public void setQuantityPerAliquot(String quantityPerAliquot)
	{
		this.quantityPerAliquot = quantityPerAliquot;
	}

	
	public String getCollectionEventCollectionProcedure()
	{
		return collectionEventCollectionProcedure;
	}

	
	public void setCollectionEventCollectionProcedure(String collectionEventCollectionProcedure)
	{
		this.collectionEventCollectionProcedure = collectionEventCollectionProcedure;
	}

	
	public String getCollectionEventContainer()
	{
		return collectionEventContainer;
	}

	
	public void setCollectionEventContainer(String collectionEventContainer)
	{
		this.collectionEventContainer = collectionEventContainer;
	}

	
	public String getReceivedEventReceivedQuality()
	{
		return receivedEventReceivedQuality;
	}

	
	public void setReceivedEventReceivedQuality(String receivedEventReceivedQuality)
	{
		this.receivedEventReceivedQuality = receivedEventReceivedQuality;
	}

	
	public int getNoOfDeriveSpecimen()
	{
		return noOfDeriveSpecimen;
	}

	
	public void setNoOfDeriveSpecimen(int noOfDeriveSpecimen)
	{
		this.noOfDeriveSpecimen = noOfDeriveSpecimen;
	}

	
	public Object getDeriveSpecimenValue(String key)
	{
		return deriveSpecimenValues.get(key);
	}
	
	public void setDeriveSpecimenValue(String key, Object value)
	{
		if (isMutable())
	   	 {
	   		if(deriveSpecimenValues==null)
	   		{
	   			deriveSpecimenValues = new LinkedHashMap();
	   		}
			deriveSpecimenValues.put(key, value);
	   	 }
	}

	public Map getDeriveSpecimenValues()
	{
		return deriveSpecimenValues;
	}
	
 	public void setDeriveSpecimenValues(Map deriveSpecimenValues)
 	{
		this.deriveSpecimenValues = deriveSpecimenValues;
	}
 
 	/**
	 * @return Returns the values.
	 */
	public Collection getAllDeriveSpecimenValuess() 
	{
		return deriveSpecimenValues.values();
	}
 	
	@Override
	public int getFormId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void reset()
	{
		
		
	}

	public void setAllValues(AbstractDomainObject arg0)
	{
		// TODO Auto-generated method stub
		
	}


	
	public String getLineage()
	{
		return lineage;
	}


	
	public void setLineage(String lineage)
	{
		this.lineage = lineage;
	}


	
	public Collection getAliquotSpecimenCollection()
	{
		return aliquotSpecimenCollection;
	}


	
	public void setAliquotSpecimenCollection(Collection aliquotSpecimenCollection)
	{
		this.aliquotSpecimenCollection = aliquotSpecimenCollection;
	}


	
	public Collection getDeriveSpecimenCollection()
	{
		return deriveSpecimenCollection;
	}


	
	public void setDeriveSpecimenCollection(Collection deriveSpecimenCollection)
	{
		this.deriveSpecimenCollection = deriveSpecimenCollection;
	}


	
	public String getDisplayName()
	{
		return displayName;
	}


	
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}


	
	public String getUnit()
	{
		return unit;
	}


	
	public void setUnit(String unit)
	{
		this.unit = unit;
	}


	
	public long getCollectionEventId()
	{
		return collectionEventId;
	}


	
	public void setCollectionEventId(long collectionEventId)
	{
		this.collectionEventId = collectionEventId;
	}


	
	public long getCollectionEventSpecimenId()
	{
		return collectionEventSpecimenId;
	}


	
	public void setCollectionEventSpecimenId(long collectionEventSpecimenId)
	{
		this.collectionEventSpecimenId = collectionEventSpecimenId;
	}


	
	public long getCollectionEventUserId()
	{
		return collectionEventUserId;
	}


	
	public void setCollectionEventUserId(long collectionEventUserId)
	{
		this.collectionEventUserId = collectionEventUserId;
	}


	
	public long getReceivedEventId()
	{
		return receivedEventId;
	}


	
	public void setReceivedEventId(long receivedEventId)
	{
		this.receivedEventId = receivedEventId;
	}


	
	public long getReceivedEventSpecimenId()
	{
		return receivedEventSpecimenId;
	}


	
	public void setReceivedEventSpecimenId(long receivedEventSpecimenId)
	{
		this.receivedEventSpecimenId = receivedEventSpecimenId;
	}


	
	public long getReceivedEventUserId()
	{
		return receivedEventUserId;
	}


	
	public void setReceivedEventUserId(long receivedEventUserId)
	{
		this.receivedEventUserId = receivedEventUserId;
	}


	
	public String getStorageLocationForSpecimen()
	{
		return storageLocationForSpecimen;
	}


	
	public void setStorageLocationForSpecimen(String storageLocationForSpecimen)
	{
		this.storageLocationForSpecimen = storageLocationForSpecimen;
	}


	
	public String getStorageLocationForAliquotSpecimen()
	{
		return storageLocationForAliquotSpecimen;
	}


	
	public void setStorageLocationForAliquotSpecimen(String storageLocationForAliquotSpecimen)
	{
		this.storageLocationForAliquotSpecimen = storageLocationForAliquotSpecimen;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
       ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();
        double aliquotQuantity = 0;
        double initialQuantity = 0;
        try
        {
    		if (validator.isEmpty(this.className))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("specimen.type")));
			}
			if (validator.isEmpty(this.type))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("specimen.subType")));
			}
			List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS, null);
			if (!Validator.isEnumeratedValue(specimenClassList, this.className))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.invalid",
						ApplicationProperties.getValue("specimen.type")));
			}

			if (!Validator.isEnumeratedValue(AppUtility.getSpecimenTypes(this.className), this.type))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.invalid",
						ApplicationProperties.getValue("specimen.subType")));
			}
			
			if (!validator.isEmpty(quantity))
			{					
				try
				{
					quantity = new BigDecimal(quantity).toPlainString();
					if(AppUtility.isQuantityDouble(className,type))
        			{						
        		        if(!validator.isDouble(quantity,true))
        		        {
        		        	errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.required",ApplicationProperties.getValue("specimen.quantity")));        		        	
        		        }
        			}
        			else
        			{        				
        				if(!validator.isNumeric(quantity,0))
        		        {
        		        	errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("specimen.quantity")));        		        	
        		        }
        			}
				}
				catch (NumberFormatException exp)
		        {    		  
					errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("specimen.quantity")));
				}
				
			}
			else
			{
				errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("specimen.quantity")));
			}
			
		
			if(!this.quantityPerAliquot.equals(""))
			{
				if (quantityPerAliquot != null && quantityPerAliquot.trim().length() != 0)
				{
					try
					{
						quantityPerAliquot = new BigDecimal(quantityPerAliquot).toPlainString();
						if (AppUtility.isQuantityDouble(this.className, this.type))
						{
							if (!validator.isDouble(quantityPerAliquot.trim()))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
										.getValue("aliquots.qtyPerAliquot")));
								
							}
						}
						else
						{
							if (!validator.isPositiveNumeric(quantityPerAliquot.trim(),1))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
										.getValue("aliquots.qtyPerAliquot")));
							}
						}
					}
					catch (NumberFormatException exp)
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties.getValue("aliquots.qtyPerAliquot")));
					}
				}
			}
			
			if(!this.noOfAliquots.equals(""))
			{
				if (!validator.isNumeric(this.noOfAliquots))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
							ApplicationProperties.getValue("specimenArrayAliquots.noOfAliquots")));
				}
				else
				{
					try
					{
						if(this.quantityPerAliquot.equals(""))
						{
							if(this.quantity.equals("0") || this.quantity.equals("0") )
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid",
										ApplicationProperties.getValue("specimen.quantity")));
							}
							else
							{
								aliquotQuantity = Double.parseDouble(this.quantity)/Double.parseDouble(this.noOfAliquots);
								initialQuantity = Double.parseDouble(this.quantity) - (aliquotQuantity * Double.parseDouble(this.noOfAliquots));
							}
						}
						else
						{
							aliquotQuantity = Double.parseDouble(this.quantityPerAliquot);
							initialQuantity = Double.parseDouble(this.quantity);
							initialQuantity = initialQuantity - (aliquotQuantity * Double.parseDouble(this.noOfAliquots));
						}
						if(initialQuantity < 0)
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid",
									ApplicationProperties.getValue("cpbasedentry.quantityperaliquot")));
						}
					}
					catch (NumberFormatException exp)
			        {    		  
						errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("specimen.quantity")));
					}
				}
			}
			
			if(this.tissueSite.equals(""))
            {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("specimen.tissueSite")));
            }
            	
			if (this.tissueSide.equals(""))
			{
               errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("specimen.tissueSide")));
			}
        	
			if(this.noOfAliquots!=null && !this.noOfAliquots.equals(""))
			{
				if (!this.storageLocationForAliquotSpecimen.equals("Auto")&&!this.storageLocationForAliquotSpecimen.equals("Manual")&&!this.storageLocationForAliquotSpecimen.equals("Virtual"))
				{
	               errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("cpbasedentry.aliquotstoragelocation")));
				}
			}
			if (!this.storageLocationForSpecimen.equals("Auto")&&!this.storageLocationForSpecimen.equals("Manual")&&!this.storageLocationForSpecimen.equals("Virtual"))
			{
               errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("cpbasedentry.specimenstoragelocation")));
			}
			if (this.pathologicalStatus.equals(""))
            {
               errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("specimen.pathologicalStatus")));
            }
			List tissueSiteList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_TISSUE_SITE, null);
			if (!Validator.isEnumeratedValue(tissueSiteList, this.tissueSite))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.invalid",ApplicationProperties.getValue("specimen.tissueSite")));
			}

			List tissueSideList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_TISSUE_SIDE, null);
			if (!Validator.isEnumeratedValue(tissueSideList, this.tissueSide))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.invalid",ApplicationProperties.getValue("specimen.tissueSide")));
			}

			List pathologicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_PATHOLOGICAL_STATUS, null);

			if (!Validator.isEnumeratedValue(pathologicalStatusList, this.pathologicalStatus))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.invalid",ApplicationProperties.getValue("specimen.pathologicalStatus")));
			}

 			
			if ((collectionEventUserId) == 0L)
	        {
	       		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Collection Event's user"));
	        }
			
			// checks the collectionProcedure
			if (!validator.isValidOption(this.getCollectionEventCollectionProcedure()))
			{
				String message = ApplicationProperties.getValue("collectioneventparameters.collectionprocedure");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",message));
			}			
			
			List procedureList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
			if (!Validator.isEnumeratedValue(procedureList, this.getCollectionEventCollectionProcedure()))
			{
				String message = ApplicationProperties.getValue("cpbasedentry.collectionprocedure");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid",message));
			}
			//Container validation
			if (!validator.isValidOption(this.getCollectionEventContainer()))
			{
				String message = ApplicationProperties.getValue("collectioneventparameters.container");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",message));
			}
			List containerList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CONTAINER, null);
			if (!Validator.isEnumeratedValue(containerList, this.getCollectionEventContainer()))
			{
				String message = ApplicationProperties.getValue("collectioneventparameters.container");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid",message));
			}
			if ((receivedEventUserId) == 0L)
	        {
	       		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Received Event's user"));
	        }			
			List qualityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RECEIVED_QUALITY, null);
			if (!Validator.isEnumeratedValue(qualityList, this.receivedEventReceivedQuality))
			{
				String message = ApplicationProperties.getValue("cpbasedentry.receivedquality");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid",message));
				
			}

			if(this.className.equals(Constants.MOLECULAR))
			{
				if(!validator.isDouble(this.concentration,true))
				{
					errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("specimen.concentration")));	
				}
			}
			
			if(this.noOfDeriveSpecimen>=1)
			{
				boolean bSpecimenClass = false;
				boolean bSpecimenType = false;
				Map deriveSpecimenMap = deriveSpecimenMap();
				Iterator it = deriveSpecimenMap.keySet().iterator();
				while(it.hasNext())
				{
					String key = (String)it.next();
					String mapValue = (String)deriveSpecimenMap.get(key);
					if(!bSpecimenClass)
					{
						if(key.indexOf("specimenClass")!=-1 && !validator.isValidOption(mapValue))
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.specimenclass")));
							bSpecimenClass = true;
						}
					}
					if((key.indexOf("_concentration"))!=-1&&mapValue!=null)
					{
						mapValue = new BigDecimal(mapValue).toPlainString();
						if(!validator.isDouble(mapValue, true))
						{
							errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("specimen.concentration")));	
						}
					}
					
					if(!bSpecimenType)
					{
						if(key.indexOf("specimenType")!=-1 && !validator.isValidOption(mapValue))
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.specimetype")));
							bSpecimenType = true;
						}
					}
					
					
					if((key.indexOf("_quantity"))!=-1)
					{
						if (!validator.isEmpty(mapValue))
						{					
							try
							{
								mapValue = new BigDecimal(mapValue).toPlainString();
								if(AppUtility.isQuantityDouble(className,type))
			        			{						
			        		        if(!validator.isDouble(mapValue,true))
			        		        {
			        		        	errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.required",ApplicationProperties.getValue("specimen.quantity")));        		        	
			        		        }
			        			}
			        			else
			        			{        				
			        				if(!validator.isNumeric(mapValue,0))
			        		        {
			        		        	errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("cpbasedentry.derivedspecimen.quantity")));        		        	
			        		        }
			        			}
							}
							catch (NumberFormatException exp)
					        {    		  
								errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("cpbasedentry.derivedspecimen.quantity")));
							}
						}
						else
						{
							errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("cpbasedentry.derivedspecimen.quantity")));
						}
					}
				}
			}
        }
        catch(Exception excp)
        {
        	logger.error(excp.getMessage(),excp);
        }
        return errors;
     }


	public Map deriveSpecimenMap()
	{
		int iCount;
		Map deriveSpecimenMap = new LinkedHashMap<String, String>();
		for(iCount = 1 ; iCount <= noOfDeriveSpecimen ; iCount++)
		{
			String key = null;
			key  = "DeriveSpecimenBean:"+iCount+"_specimenClass";
			String specimenClass = (String)deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, specimenClass);
			
			key = "DeriveSpecimenBean:"+iCount+"_id";
			String id = (String)deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, id);
			
			key = "DeriveSpecimenBean:"+iCount+"_specimenType";
			String specimenType = (String)deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, specimenType);
			
			key = "DeriveSpecimenBean:"+iCount+"_quantity";
			String quantity = (String)deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, quantity);
			
			key = "DeriveSpecimenBean:"+iCount+"_concentration";
			String conc = (String)deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, conc);
			
			key = "DeriveSpecimenBean:"+iCount+"_storageLocation";
			String storageLocation = (String)deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, storageLocation);
		}
		return deriveSpecimenMap;
	}
	
	public String getNodeKey()
	{
		return nodeKey;
	}
	
	public void setNodeKey(String nodeKey)
	{
		this.nodeKey = nodeKey;
	}
    

}
