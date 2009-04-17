/**
 * <p>Title: BaseShipmentForm Class</p>
 * <p>Description:BaseShipmentForm class is the subclass of he AbstractActionForm bean classes. </p>
 * Copyright: Copyright (c) 2008
 * Company:
 * @author vijay_chittem
 * @version 1.00
 * Created on July 16, 2008
 */
package edu.wustl.catissuecore.actionForm.shippingtracking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.shippingtracking.BaseShipment;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.catissuecore.util.global.Variables;
/**
 * BaseShipmentForm class  contains all the common fields
 * of ShipmentForm and ShipmentRequestForm.
 **/
public class BaseShipmentForm extends AbstractActionForm
{
	/**
	 * Serializable class requires Serial version id.
	 */
	private static long serialVersionUID = 1L;
	/**
	 * ShipmentLabel.
	 */
	protected String label;
	/**
	 * Shipment creation date.
	 */
	protected String createdDate;
	/**
	 * Shipment/request send date.
 	 */
	protected String sendDate;
	/**
	 * shipment send time hours.
	 */
	protected String sendTimeHour;
	/**
	 * shipment send time in minutes.
	 */
	protected String sendTimeMinutes;
	/**
	 * Shipment/Request sender's siteId.
	 */
	protected long senderSiteId;
	/**
	 * Shipment/Request Receiver's siteId.
	 */
	protected long receiverSiteId;
	/**
	 * Contact person at the sender site.
	 */
	protected long senderContactId;
	/**
	 * Contact person at the receiver site.
	 */
	protected long receiverContactId;
	/**
	 * Shipment sender's Comments.
	 */
	protected String senderComments;
	/**
	 * Shipment receiver's Comments.
	 */
	protected String receiverComments;
	/**
	 * Specimen/container status.
	 */
	protected String status;
	/**
	 * Variable to keep track of whether label or barcode have been supplied for specimens.
	 */
	protected String specimenLabelChoice;
	/**
	 * Variable to keep track of whether label or barcode have been supplied for specimens.
	 */
	protected String containerLabelChoice;
	/**
	 *  Map for holding values related to specimens included in the shipment.
	 */
	protected Map specimenDetailsMap=new HashMap();
	/**
	 * Counter to keep track of number of specimens included in the shipment.
	 */
	protected int specimenCounter=0;
	//bug 11026
	/**
	 * Contact person at the sender site.
	 */
	private String senderName;
	/**
	 * Contact person at the receiver site.
	 */
	private String senderEmail;
	/**
	 * String to hold phone no. of sender.
	 */
	private String senderPhone;
	/**
	 * String to hold name of sending site.
	 */
	private String senderSiteName;
	/**
	 * String to hold name of recieving site.
	 */
	private String receiverSiteName;
	/**
	 * String to hold name of the coordinator of receiving site.
	 */
	private String receiverSiteCoordinator;
	/**
	 * String to hold phone no. of receiving's site coordinator.
	 */
	private String receiverSiteCoordinatorPhone;
	/**
	 * List to hold list of all specimen.
	 */
    private List<Specimen> specimenList;
	/**
	 * list to hold label or barcode of specimen.
	 */
	private List<String> lblOrBarcodeSpecimenL = new ArrayList<String>();
	/**
	 * list to hold label or barcode of containers.
	 */
	private List<String> lblOrBarcodeContainerL = new ArrayList<String>();
	/**
	 * gets the list of label or barcode of containers.
	 * @return lblOrBarcodeContainerL.
	 */
	public List<String> getLblOrBarcodeContainerL()
	{
		return lblOrBarcodeContainerL;
	}
	/**
	 * sets the list of label or barcode of container for the shipment.
	 * @param lblOrBarcodeContainerL label or barcode of container to set.
	 */
	public void setLblOrBarcodeContainerL(List<String> lblOrBarcodeContainerL)
	{
		this.lblOrBarcodeContainerL = lblOrBarcodeContainerL;
	}
	/**
	 * gets the list of label or barcode of specimen for the shipment.
	 * @return lblOrBarcodeSpecimenL
	 */
	public List<String> getLblOrBarcodeSpecimenL()
	{
		return lblOrBarcodeSpecimenL;
	}
	/**
	 * sets the list of label or barcode of specimen for the shipment.
	 * @param lblOrBarcodeSpecimenL label or barcode list of specimen to set.
	 */
	public void setLblOrBarcodeSpecimenL(List<String> lblOrBarcodeSpecimenL)
	{
		this.lblOrBarcodeSpecimenL = lblOrBarcodeSpecimenL;
	}
	/**
	 * gets the specimen list.
	 * @return specimenList
	 */
	public List<Specimen> getSpecimenList()
	{
		return specimenList;
	}
	/**
	 * sets the specimen list for the shipment.
	 * @param specimenList list of specimen.
	 */
	public void setSpecimenList(List<Specimen> specimenList)
	{
		this.specimenList = specimenList;
	}
	/**
	 * gets the site name of the sender.
	 * @return senderSiteName
	 */
	public String getSenderSiteName()
	{
		return senderSiteName;
	}
	/**
	 * sets the site name of the sender.
	 * @param senderSiteName name of site of sender.
	 */
	public void setSenderSiteName(String senderSiteName)
	{
		this.senderSiteName = senderSiteName;
	}
	/**
	 * gets the site name of the receiver.
	 * @return receiverSiteName
	 */
	public String getReceiverSiteName()
	{
		return receiverSiteName;
	}
	/**
	 * sets the receiver site name.
	 * @param receiverSiteName site name of the receiver.
	 */
	public void setReceiverSiteName(String receiverSiteName)
	{
		this.receiverSiteName = receiverSiteName;
	}
	/**
	 * gets the coordinator of receiver site.
	 * @return receiverSiteCoordinator
	 */
	public String getReceiverSiteCoordinator()
	{
		return receiverSiteCoordinator;
	}
	/**
	 * sets the coordinator of receiver site.
	 * @param receiverSiteCoordinator coordinator of receiver site to set.
	 */
	public void setReceiverSiteCoordinator(String receiverSiteCoordinator)
	{
		this.receiverSiteCoordinator = receiverSiteCoordinator;
	}
	/**
	 * gets the phone no. of receiver site coordinator.
	 * @return receiverSiteCoordinatorPhone
	 */
	public String getReceiverSiteCoordinatorPhone()
	{
		return receiverSiteCoordinatorPhone;
	}
	/**
	 * sets the phone no. of receiver site coordinator.
	 * @param receiverSiteCoordinatorPhone receiver site coordinator phone number to set.
	 */
	public void setReceiverSiteCoordinatorPhone(String receiverSiteCoordinatorPhone)
	{
		this.receiverSiteCoordinatorPhone = receiverSiteCoordinatorPhone;
	}
	/**
	 * gets the sender name.
	 * @return senderName
	 */
	public String getSenderName()
	{
		return senderName;
	}
	/**
	 * sets the sender name.
	 * @param senderName name of sender to set.
	 */
	public void setSenderName(String senderName)
	{
		this.senderName = senderName;
	}
	/**
	 * sets the sender email.
	 * @return senderEmail
	 */
	public String getSenderEmail()
	{
		return senderEmail;
	}
	/**
	 * sets the sender email.
	 * @param senderEmail sender email to set.
	 */
	public void setSenderEmail(String senderEmail)
	{
		this.senderEmail = senderEmail;
	}
	/**
	 * gets the sender phone no.
	 * @return senderPhone
	 */
	public String getSenderPhone()
	{
		return senderPhone;
	}
	/**
	 * sets the sender phone no.
	 * @param senderPhone phone of sender to set.
	 */
	public void setSenderPhone(String senderPhone)
	{
		this.senderPhone = senderPhone;
	}
	/**
	 * This method gets the value stored in the  specimenDetailsMap with key as the key.
	 * @return Object
	 * @param key to fetch object.
	 */
	public Object getSpecimenDetails(String key)
	{
		return specimenDetailsMap.get(key);
	}
	/**
	 * This method stores the value 'value' in the  specimenDetailsMap with 'key' as the key.
	 * @param key the key of the obect.
	 * @param value value pointed by the key.
	 */
	public void setSpecimenDetails(String key, Object value)
	{
		specimenDetailsMap.put(key, value);
	}
	/**
	 * Map for holding values related to containers included in the shipment.
	 */
	protected Map containerDetailsMap=new HashMap();
	/**
	 * Counter to keep track of number of containers included in the shipment.
	 */
	protected int containerCounter=0;
	/**
	 * This method gets the value stored in the  containerDetailsMap with key as the key.
	 * @return Object
	 * @param key to fetch object.
	 */
	public Object getContainerDetails(String key)
	{
		return containerDetailsMap.get(key);
	}
	/**
	 * This method stores the value 'value' in the  containerDetailsMap with 'key' as the key.
	 * @param key the key of the object.
	 * @param value value pointed by key.
	 */
	public void setContainerDetails(String key, Object value)
	{
		if(value!=null)
		{
			containerDetailsMap.put(key, value);
		}
	}
	/**
	 * Gets the SpecimenLabel.
	 * @return the label
	 * @see #setLabel(String)
	 */
	public String getLabel()
	{
		return label;
	}
    /**
	 * Sets the SpecimenLabel.
	 * @param label the label to set.
	 * @see #getLabel()
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}
	/**
	 * @return the senderComments.
	 * @see #setSenderComments(String)
	 */
	public String getSenderComments()
	{
		return senderComments;
	}
	/**
	 * Sets Shipment sender's Comments.
	 * @param senderComments the senderComments to set
	 * @see #getSenderComments()
	 */
	public void setSenderComments(String senderComments)
	{
		this.senderComments = senderComments;
	}
	/**
	 * Gets Shipment  receiver's Comments.
	 * @return the receiverComments
	 * @see #setReceiverComments(String)
	 */
	public String getReceiverComments()
	{
		return receiverComments;
	}
	/**
	 * Sets Shipment receiver's Comments.
	 * @param receiverComments the receiverComments to set
	 * @see #getReceiverComments()
	 */
	public void setReceiverComments(String receiverComments)
	{
		this.receiverComments = receiverComments;
	}
	/**
	 * Gets Specimen/container status.
	 * @return the status
	 * @see #setStatus(String)
	 */
	public String getStatus()
	{
		return status;
	}
	/**
	 * Sets Specimen/container status.
	 * @param status the status to set
	 * @see #getStatus()
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}
	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors=super.validate(mapping, request);
		if(errors==null)
		{
			errors=new ActionErrors();
		}
		else if(errors!=null && errors.size()>0)
		{
			return errors;
		}
		validateBasicShipmentInformation(errors);
		validateShipmentContentDetails(errors);
		return errors;
	}
	/**
	 * This method deals with the validation of shipment content details.
	 * @param errors action errors.
	 */
	protected void validateShipmentContentDetails(ActionErrors errors)
	{
		String keyLabel="";
		String keyBarcode="";
		String valueLabel="";
		String valueBarcode="";
		int counter=0;
		boolean isSpecimenPresent=false,isContainerPresent=false;
		Validator validator=new Validator();
		if(this.specimenCounter>0)
		{
			for(counter=1;counter<=this.specimenCounter;counter++)
			{
				keyLabel="specimenLabel_"+counter;
				keyBarcode="specimenBarcode_"+counter;
				valueLabel=(String)this.getSpecimenDetails(keyLabel);
				valueBarcode=(String)this.getSpecimenDetails(keyBarcode);
				if(!validator.isEmpty(valueBarcode) || !validator.isEmpty(valueLabel))
				{
					isSpecimenPresent=true;
				}
			}
			if(isSpecimenPresent && validator.isEmpty(this.specimenLabelChoice))
			{
				errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.required","Choice of Label Or Barcode For Sepcimen"));
			}
			else if(isSpecimenPresent
					&& !(this.specimenLabelChoice.trim().equals("SpecimenLabel"))
					&& !(this.specimenLabelChoice.trim().equals("SpecimenBarcode")))
			{
				errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.invalid","Choice of Label Or Barcode For Sepcimen"));
			}
		}
		if(this.containerCounter>0)
		{
			for(counter=1;counter<=this.containerCounter;counter++)
			{
				keyLabel="containerLabel_"+counter;
				keyBarcode="containerBarcode_"+counter;
				valueLabel=(String)this.getContainerDetails(keyLabel);
				valueBarcode=(String)this.getContainerDetails(keyBarcode);
				if(!validator.isEmpty(valueBarcode) || !validator.isEmpty(valueLabel))
				{
					isContainerPresent=true;
				}
			}
			if(isContainerPresent && validator.isEmpty(this.containerLabelChoice))
			{
				errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.required","Choice of Label Or Barcode For Container"));
			}
			else if(isContainerPresent
					&& !(this.containerLabelChoice.trim().equals("ContainerLabel"))
					&& !(this.containerLabelChoice.trim().equals("ContainerBarcode")))
			{
				errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.invalid","Choice of Label Or Barcode For Container"));
			}
		}
		if(!isSpecimenPresent && !isContainerPresent)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.required",ApplicationProperties.getValue("shipment.specimenContainerRequired")));
		}
	}
	/**
	 * This method validates basic shipment information.
	 * @param errors returning action errors.
	 */
	protected void validateBasicShipmentInformation(ActionErrors errors)
	{
		Validator validator=new Validator();
		String dateErrorString=validator.validateDate(this.sendDate,false);
		//checking for empty label
		if(validator.isEmpty(this.label))
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.required",ApplicationProperties.getValue("shipment.label")));
		}
		//checking for empty senderSiteId (default = 0 is empty)
		if(this.senderSiteId == 0)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.required",ApplicationProperties.getValue("shipment.senderSite")));
		}
		//checking for empty receiverSiteId (default = 0 is empty)
		if(this.receiverSiteId == 0)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.required",ApplicationProperties.getValue("shipment.receiverSite")));
		}
		if (!validator.isValidOption(""+(this.senderSiteId)))
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.required",ApplicationProperties.getValue("shipment.senderSite")));
		}
		if (!validator.isValidOption(""+(this.receiverSiteId)))
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.required",ApplicationProperties.getValue("shipment.receiverSite")));
		}
		//checking for empty date
		if(dateErrorString!=null && !dateErrorString.trim().equals(""))
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.required",ApplicationProperties.getValue("shipment.sendDate")));
		}
		//checking same sender and receiver site
		if(this.senderSiteId == this.receiverSiteId)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item",ApplicationProperties.getValue("shipment.specimenSiteValidation")));
		}
	}
	/**
     * Copies the data from an AbstractDomain object to a BaseShipmentForm object.
     * @param abstractDomainObject the domain object to set.
     */
	public void setAllValues(AbstractDomainObject abstractDomainObject)
	{
		if(abstractDomainObject instanceof BaseShipment)
		{
			BaseShipment shipment=(BaseShipment)abstractDomainObject;
			populateBasicShipmentProperties(shipment);
			populateShipmentContentsDetails(shipment);
		}
	}
	/**
	 * Iterates through specimens list and sets the contents of shipment.
	 * @param specimens list of specimens.
	 */
	public void setShipmentContentsUsingSpecimen(List<Specimen> specimens)
	{
		if(specimens!=null && specimens.size()>0)
		{
			this.specimenCounter=0;
			for(Specimen s : specimens)
			{
				this.specimenCounter++;
				String label = s.getLabel();
				if(label==null)
				{
					label=" ";
				}
				this.specimenDetailsMap.put("specimenLabel_"+specimenCounter,label);
				String barcode = s.getBarcode();
				if(barcode==null)
				{
					barcode = " ";
				}
				this.specimenDetailsMap.put("specimenBarcode_"+specimenCounter,barcode);
			}
		}
	}
	/**
	 * This method sets the shipment details using container details.
	 * @param storageContainers list of storage containers.
	 */
	public void setShipmentContentsUsingContainer(List<StorageContainer> storageContainers)
	{
		if(storageContainers!=null && storageContainers.size()>0)
		{
			this.containerCounter=0;
			for(StorageContainer s : storageContainers)
			{
				this.containerCounter++;
				String label = s.getName();
				if(label==null)
				{
					label=" ";
				}
				this.containerDetailsMap.put("containerLabel_"+containerCounter,label);
				String barcode = s.getBarcode();
				if(barcode==null)
				{
					barcode = " ";
				}
				this.containerDetailsMap.put("containerBarcode_"+containerCounter,barcode);
			}
		}
	}
	/**
	 * This method iterates through container collection and populates shipment content details.
	 * @param shipment shipment object.
	 */
	protected void populateShipmentContentsDetails(BaseShipment shipment)
	{
		this.specimenLabelChoice="SpecimenLabel";
		this.containerLabelChoice="ContainerLabel";
		this.containerCounter=0;
		this.specimenCounter=0;
		if(shipment.getContainerCollection()!=null)
		{
			Iterator<StorageContainer> containerIterator=shipment.getContainerCollection().iterator();
			while(containerIterator.hasNext())
			{
				StorageContainer container=containerIterator.next();
				if(container.getStorageType()!=null
						&& !container.getStorageType().getName().equals(
								Constants.SHIPMENT_CONTAINER_TYPE_NAME))
				{
					this.containerCounter++;
				    String name = container.getName();
				    if(name==null)
				    {
				    	name = " ";
				    }
					this.containerDetailsMap.put("containerLabel_"+containerCounter,name);
					String barcode = container.getBarcode();
					if(barcode==null)
					{
						barcode = " ";
					}
					this.containerDetailsMap.put("containerBarcode_"+containerCounter,barcode);
				}
				else
				{
					populateSpecimenDetails(container.getSpecimenPositionCollection());
				}
			}
		}
	}
	/**
	 * This method sets the specimen details map.
	 * @param specimenPositionCollection collection of specimen position.
	 */
	private void populateSpecimenDetails(Collection<SpecimenPosition> specimenPositionCollection)
	{
		Iterator<SpecimenPosition> spPosIterator = specimenPositionCollection.iterator();
		while(spPosIterator.hasNext())
		{
			SpecimenPosition position=spPosIterator.next();
			this.specimenCounter++;
			String label = position.getSpecimen().getLabel();
			if(label==null)
			{
				label=" ";
			}
			this.specimenDetailsMap.put("specimenLabel_"+specimenCounter,label);
			String barcode = position.getSpecimen().getBarcode();
			if(barcode==null)
			{
				barcode = " ";
			}
			this.specimenDetailsMap.put("specimenBarcode_"+specimenCounter,barcode);
		}
	}
	private Long getShipmentId(BaseShipment shipment)
	{
		Long shipmentId = Long.valueOf(0);
		if(shipment.getId()!=null)
		{
			shipmentId = shipment.getId();
		}
		return shipmentId;
	}
	private Long getSenderContactId(BaseShipment shipment)
	{
		Long senderId = Long.valueOf(0);
		if(shipment.getSenderContactPerson()==null || shipment.getSenderContactPerson().getId()==null)
		{
			senderId=Long.valueOf(0);
		}
		else
		{
			senderId=shipment.getSenderContactPerson().getId();
		}
		return senderId;
	}
	private Long getSendetSiteId(BaseShipment shipment)
	{
		Long senderSiteId = Long.valueOf(0);
		if(shipment.getSenderSite()==null || shipment.getSenderSite().getId()==null)
		{
			senderSiteId=Long.valueOf(0);
		}
		else
		{
			senderSiteId=shipment.getSenderSite().getId();
		}
		return senderSiteId;
	}
	/**
	 * This method sets the basic properties of shipment.
	 * @param shipment the shipment object.
	 */
	protected void populateBasicShipmentProperties(BaseShipment shipment)
	{
		this.setId(getShipmentId(shipment));
//		this.id = getShipmentId(shipment);
//		if(shipment.getSenderContactPerson()==null || shipment.getSenderContactPerson().getId()==null)
//		{
//			this.senderContactId=0;
//		}
//		else
//		{
//			this.senderContactId=shipment.getSenderContactPerson().getId();
//		}
		this.senderContactId = getSenderContactId(shipment);
//		if(shipment.getSenderSite()==null || shipment.getSenderSite().getId()==null)
//		{
//			this.senderSiteId=0;
//		}
//		else
//		{
//			this.senderSiteId=shipment.getSenderSite().getId();
//		}
		this.senderSiteId = getSendetSiteId(shipment);
		if(shipment.getReceiverSite()==null || shipment.getReceiverSite().getId()==null)
		{
			this.receiverSiteId=0;
		}
		else
		{
			this.receiverSiteId=shipment.getReceiverSite().getId();
		}
		if(shipment.getReceiverContactPerson()==null || shipment.getReceiverContactPerson().getId()==null)
		{
			this.receiverContactId=0;
		}
		else
		{
			this.receiverContactId=shipment.getReceiverContactPerson().getId();
		}
		if(shipment.getCreatedDate()==null)
		{
			this.createdDate=Utility.parseDateToString(new Date(),Variables.dateFormat);
			//this.createdDate=Utility.parseDateToString(shipment.getCreatedDate(),Variables.dateFormat);
		}
		else
		{
			this.createdDate=Utility.parseDateToString(shipment.getCreatedDate(),Variables.dateFormat);
		}
		this.label=shipment.getLabel();
		Calendar calender = Calendar.getInstance();
		if(shipment.getSendDate()!=null)
		{
			calender.setTime(shipment.getSendDate());
			this.sendTimeHour = Utility.toString(Integer.toString(calender.get(Calendar.HOUR_OF_DAY)));
	 	   	this.sendTimeMinutes = Utility.toString(Integer.toString(calender.get(Calendar.MINUTE)));
	 		// date foramt change by geeta
	 	   	this.sendDate = Utility.parseDateToString(shipment.getSendDate(),Variables.dateFormat);
		}
		this.senderComments=shipment.getSenderComments();
		this.setActivityStatus(shipment.getActivityStatus());
//		this.activityStatus=shipment.getActivityStatus();
		this.receiverComments = shipment.getReceiverComments();
	}
	/**
	 * Returns the id assigned to form bean.
	 * @return base shipment form id
	 */
	@Override
	public int getFormId()
	{
		return Constants.BASESHIPMENT_FORM_ID;
	}
	/**
     * Resets the values of all the fields.
     * Is called by the overridden reset method defined in ActionForm.
     */
	protected void clearAllValues()
	{
		this.label = null;
		this.createdDate = null;
		this.sendDate = null;
		this.senderSiteId = -1;
		this.receiverSiteId = -1;
		this.senderContactId = -1;
		this.receiverContactId = -1;
		this.senderComments = null;
		this.receiverComments = null;
		this.status = null;
		this.specimenDetailsMap = new HashMap<String, String>();
		this.specimenCounter=0;
		this.containerDetailsMap=new HashMap<String, String>();
		this.containerCounter=0;
	}
	/**
	 * Gets the number of containers included in the shipment.
	 * @return containerCounter
	 */
	public int getContainerCounter()
	{
		return containerCounter;
	}
	/**
	 * Sets the number of containers included in the shipment.
	 * @param containerCounter the counter of container to set.
	 */
	public void setContainerCounter(int containerCounter)
	{
		this.containerCounter = containerCounter;
	}
	/**
	 * Gets the container details map of the shipment.
	 * @return containerDetailsMap
	 */
	public Map<String, String> getContainerDetailsMap()
	{
		return containerDetailsMap;
	}
	/**
	 * Sets the container details map of the shipment.
	 * @param containerDetailsMap map containing the details of the container.
	 */
	public void setContainerDetailsMap(Map<String, String> containerDetailsMap)
	{
		this.containerDetailsMap = containerDetailsMap;
	}
	/**
	 * Gets the date of shipment creation.
	 * @return createdDate
	 */
	public String getCreatedDate()
	{
		return createdDate;
	}
	/**
	 * Sets the date of shipment creation.
	 * @param createdDate the date to set.
	 */
	public void setCreatedDate(String createdDate)
	{
		this.createdDate = createdDate;
	}
	/**
	 * Gets the userId of the contact at the recieving site.
	 * @return receiverContactId
	 */
	public long getReceiverContactId()
	{
		return receiverContactId;
	}
	/**
	 * Sets the userId of the contact at the recieving site.
	 * @param receiverContactId the contact id of the receiver.
	 */
	public void setReceiverContactId(long receiverContactId)
	{
		this.receiverContactId = receiverContactId;
	}
	/**
	 * Gets the id the recieving site.
	 * @return receiverSiteId
	 */
	public long getReceiverSiteId()
	{
		return receiverSiteId;
	}
	/**
	 * Sets the id the recieving site.
	 * @param receiverSiteId the site id of the receiver
	 */
	public void setReceiverSiteId(long receiverSiteId)
	{
		this.receiverSiteId = receiverSiteId;
	}
	/**
	 * Gets the date when the shipment is to be sent.
	 * @return sendDate
	 */
	public String getSendDate()
	{
		return sendDate;
	}
	/**
	 * Sets the date when the shipment is to be sent.
	 * @param sendDate the sending date to set.
	 */
	public void setSendDate(String sendDate)
	{
		this.sendDate = sendDate;
	}
	/**
	 * Gets the userId of the contact at the sending site.
	 * @return senderContactId
	 */
	public long getSenderContactId()
	{
		return senderContactId;
	}
	/**
	 * Sets the userId of the contact at the sending site.
	 * @param senderContactId the contact id of the sender.
	 */
	public void setSenderContactId(long senderContactId)
	{
		this.senderContactId = senderContactId;
	}
	/**
	 * Gets the id the sending site.
	 * @return senderSiteId
	 */
	public long getSenderSiteId()
	{
		return senderSiteId;
	}
	/**
	 * Sets the id the sending site.
	 * @param senderSiteId the site id of the sender.
	 */
	public void setSenderSiteId(long senderSiteId)
	{
		this.senderSiteId = senderSiteId;
	}
	/**
	 * Gets the specimen counter.
	 * @return specimenCounter
	 */
	public int getSpecimenCounter()
	{
		return specimenCounter;
	}
	/**
	 * Sets the specimen counter.
	 * @param specimenCounter count of specimen.
	 */
	public void setSpecimenCounter(int specimenCounter)
	{
		this.specimenCounter = specimenCounter;
	}
	/**
	 * Gets the specimen details map.
	 * @return specimenDetailsMap
	 */
	public Map<String, String> getSpecimenDetailsMap()
	{
		return specimenDetailsMap;
	}
	/**
	 * Sets the specimen details map.
	 * @param specimenDetailsMap map containing specimen details.
	 */
	public void setSpecimenDetailsMap(Map<String, String> specimenDetailsMap)
	{
		this.specimenDetailsMap = specimenDetailsMap;
	}
	/**
	 * Gets the container label choice.
	 * @return containerLabelChoice
	 */
	public String getContainerLabelChoice()
	{
		return containerLabelChoice;
	}
	/**
	 * Sets the container label choice.
	 * @param containerLabelChoice choice of container label to set.
	 */
	public void setContainerLabelChoice(String containerLabelChoice)
	{
		this.containerLabelChoice = containerLabelChoice;
	}
	/**
	 * Gets the specimen label choice.
	 * @return specimenLabelChoice
	 */
	public String getSpecimenLabelChoice()
	{
		return specimenLabelChoice;
	}
	/**
	 * Sets the specimen label choice.
	 * @param specimenLabelChoice string containing specimen label.
	 */
	public void setSpecimenLabelChoice(String specimenLabelChoice)
	{
		this.specimenLabelChoice = specimenLabelChoice;
	}
	/**
	 * Gets the send time hour.
	 * @return sendTimeHour
	 */
	public String getSendTimeHour()
	{
		return sendTimeHour;
	}
	/**
	 * Sets the send time in hours.
	 * @param sendTimeHour time in hours.
	 */
	public void setSendTimeHour(String sendTimeHour)
	{
		this.sendTimeHour = sendTimeHour;
	}
	/**
	 * Gets the send time in minutes.
	 * @return sendTimeMinutes
	 */
	public String getSendTimeMinutes()
	{
		return sendTimeMinutes;
	}
	/**
	 * Sets the send time in minutes.
	 * @param sendTimeMinutes time in minutes to set.
	 */
	public void setSendTimeMinutes(String sendTimeMinutes)
	{
		this.sendTimeMinutes = sendTimeMinutes;
	}
	/**
	 * this method overrides the reset method of parent class.
	 */
	@Override
	protected void reset()
	{
	}
	@Override
	public void setAddNewObjectIdentifier(String paramString, Long paramLong)
	{
	}
}