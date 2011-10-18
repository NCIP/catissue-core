package edu.wustl.catissuecore.uiobject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.AliquotForm;
import edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm;
import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.actionForm.DisposalEventParametersForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.actionForm.RequestDetailsForm;
import edu.wustl.catissuecore.actionForm.SOPForm;
import edu.wustl.catissuecore.actionForm.SpecimenArrayAliquotForm;
import edu.wustl.catissuecore.actionForm.SpecimenArrayForm;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.actionForm.SpecimenForm;
import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentRequestForm;
import edu.wustl.catissuecore.bean.ConsentResponseBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.UIObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.factory.IUIObjectFactory;

public class UIObjectFactory implements IUIObjectFactory
{
	public UIObject getUIObject(AbstractActionForm form)
	throws AssignDataException
	{
		UIObject uiObject = null;
		if(form instanceof SpecimenForm)
		{
			if(form instanceof NewSpecimenForm)
			{
				uiObject =  createSpecimenUIObject((NewSpecimenForm)form);
			}
			else if(form instanceof CreateSpecimenForm)
			{
				uiObject =  createSpecimenUIObject((CreateSpecimenForm)form);
			}
		}
		else if(form instanceof AliquotForm)
		{
			uiObject =  createAliquotUIObject((AliquotForm)form);
		}
		else if(form instanceof SpecimenArrayForm || form instanceof SpecimenArrayAliquotForm)
		{
			if(form instanceof SpecimenArrayForm)
			{
				uiObject =  createSpecimenArrayUIObject((SpecimenArrayForm)form);
			}
			else if(form instanceof SpecimenArrayAliquotForm)
			{
				uiObject =  createSpecimenArrayUIObject((SpecimenArrayAliquotForm)form);;
			}
		}
		else if(form instanceof StorageContainerForm)
		{
			uiObject = createStorageContainerUIObject((StorageContainerForm)form);
		}
		else if(form instanceof CollectionProtocolRegistrationForm || form instanceof ParticipantForm)
		{
			if(form instanceof ParticipantForm)
			{
				ParticipantForm pform = (ParticipantForm)form;
				ParticipantUIObject partUIObject = new ParticipantUIObject();
				Map<Long, CollectionProtocolRegistrationUIObject> cprMap = new HashMap<Long, CollectionProtocolRegistrationUIObject>();
				if(pform.getConsentResponseBeanCollection() != null && !pform.getConsentResponseBeanCollection().isEmpty() )
				{
					Iterator<ConsentResponseBean> itr = pform.getConsentResponseBeanCollection().iterator();
					while(itr.hasNext())
					{
						CollectionProtocolRegistrationUIObject cprUIObject = new CollectionProtocolRegistrationUIObject();
						ConsentResponseBean crb = (ConsentResponseBean)itr.next();
						cprUIObject.setConsentWithdrawalOption(crb.getConsentWithdrawalOption());
						Long cpId = Long.valueOf(crb.getCollectionProtocolID());
						cprMap.put(cpId, cprUIObject);
					}
					partUIObject.setCprUIObject(cprMap);
				}
				uiObject = partUIObject;
			}
		}
		else if(form instanceof UserForm)
		{
			uiObject = createUserUIObject((UserForm)form);
		}
		else if(form instanceof SpecimenCollectionGroupForm)
		{
			uiObject = createSCGUIObject((SpecimenCollectionGroupForm)form);
		}
		else if(form instanceof RequestDetailsForm)
		{
			uiObject = createOrderDetailsUIObject((RequestDetailsForm)form);
		}
		else if(form instanceof DisposalEventParametersForm)
		{
			uiObject = createDisposalEventParametersUIObject((DisposalEventParametersForm)form);
		}
		else if(form instanceof ShipmentRequestForm)
		{
			uiObject = createShipmentRequestUIObject((ShipmentRequestForm)form);
		}
		else if(form instanceof SOPForm)
		{
			uiObject = createSPPUIObject((SOPForm)form);
		}
		return uiObject;
	}

	private UIObject createSPPUIObject(SOPForm form)
	{
		SPPUIObject sppUIObject = new SPPUIObject();
		sppUIObject.setXmlFileName(form.getXmlFileName());
		return sppUIObject;
	}

	private UIObject createDisposalEventParametersUIObject(
			DisposalEventParametersForm form)
	{
		DisposalEventParametersUIObject eventUIObject = new DisposalEventParametersUIObject();
		eventUIObject.setActivityStatus(form.getActivityStatus());
		return eventUIObject;
	}

	public UIObject createDisposalEventParametersUIObject()
	{
		return new DisposalEventParametersUIObject();
	}

	private UIObject createOrderDetailsUIObject(RequestDetailsForm form)
	{
		OrderUIObject orderDetailsUIObject = new OrderUIObject();
		orderDetailsUIObject.setMailNotification(form.getMailNotification());
		orderDetailsUIObject.setOperationAdd(Boolean.FALSE);
		return orderDetailsUIObject;
	}

	public UIObject createOrderDetailsUIObject()
	{
		OrderUIObject orderDetailsUIObject = new OrderUIObject();
		orderDetailsUIObject.setOperationAdd(Boolean.FALSE);
		return orderDetailsUIObject;
	}

	private UIObject createSCGUIObject(SpecimenCollectionGroupForm form)
	{
		SpecimenCollectionGroupUIObject scgUIObject =  new SpecimenCollectionGroupUIObject();
		scgUIObject.setApplyEventsToSpecimens(form.isApplyEventsToSpecimens());
		scgUIObject.setApplyChangesTo(form.getApplyChangesTo());
		scgUIObject.setConsentWithdrawalOption(form.getWithdrawlButtonStatus());
		if (Constants.TRUE.equals(form.getRestrictSCGCheckbox()))
		{
			scgUIObject.setIsCPBasedSpecimenEntryChecked(Boolean.TRUE);
		}
		else
		{
			scgUIObject.setIsCPBasedSpecimenEntryChecked(Boolean.FALSE);
		}
		//scgUIObject.setIsToInsertAnticipatorySpecimens(Boolean.TRUE);
		//scgUIObject.setStringOfResponseKeys(form.getStringOfResponseKeys());
		return scgUIObject;
	}

	public UIObject createSCGUIObject()
	{
		SpecimenCollectionGroupUIObject scgUIObject =  new SpecimenCollectionGroupUIObject();
		//scgUIObject.setIsToInsertAnticipatorySpecimens(Boolean.TRUE);
		return scgUIObject;
	}

	private UIObject createUserUIObject(UserForm form)
	{
		UserUIObject userUIObject = new UserUIObject();
		userUIObject.setNewPassword(form.getNewPassword());
		userUIObject.setOldPassword(form.getOldPassword());
		userUIObject.setPageOf(form.getPageOf());
		userUIObject.setTargetIdp(form.getTargetIdp());
		userUIObject.setTargetIdpLoginName(form.getTargetLoginName());
		userUIObject.setTargetPassword(form.getTargetPassword());
		userUIObject.setRoleId(form.getRole());
		userUIObject.setGrouperUser(form.getGrouperUser());
		return userUIObject;
	}

	private UIObject createStorageContainerUIObject(
			StorageContainerForm form)
	{
		StorageContainerUIObject storageContainerUIObject = new StorageContainerUIObject();
		storageContainerUIObject.setNoOfContainers(form.getNoOfContainers());
		storageContainerUIObject.setSimilarContainerMap(form.getSimilarContainersMap());
		return storageContainerUIObject;
	}

	public UIObject createStorageContainerUIObject()
	{
		return new StorageContainerUIObject();
	}

	private UIObject createSpecimenUIObject(NewSpecimenForm form)
	{
		SpecimenUIObject specimenUIObject = new SpecimenUIObject();
		specimenUIObject.setApplyChangesTo(form.getApplyChangesTo());
		specimenUIObject.setConsentWithdrawalOption(form.getWithdrawlButtonStatus());
		specimenUIObject.setParentSpecimenId(form.getParentSpecimenId());
		specimenUIObject.setParentSpecimenLabel(form.getParentSpecimenName());
		specimenUIObject.setCreatedOn(form.getCreatedDate());
		specimenUIObject.setTransferStatus(form.getTransferStatus());
		specimenUIObject.setReasonForTransfer(form.getReasonForTransfer());
		specimenUIObject.setFromPositionDimensionOne(form.getFromPositionDimensionOne());
		specimenUIObject.setFromPositionDimensionTwo(form.getFromPositionDimensionTwo());
		if(form.getFromStorageContainerId()!=null&&!form.getFromStorageContainerId().equals(""))
		{
			specimenUIObject.setFromStorageContainerId(form.getFromStorageContainerId());
		}
		if(form.getPositionDimensionOne()!=null&&!form.getPositionDimensionOne().equals(""))
		{

			specimenUIObject.setPositionDimensionOne(form.getPositionDimensionOne());
			specimenUIObject.setPositionDimensionTwo(form.getPositionDimensionTwo());
		}
		if(form.getPos1()!=null&&!form.getPos1().equals(""))
		{
			specimenUIObject.setPositionDimensionOne(form.getPos1());
			specimenUIObject.setPositionDimensionTwo(form.getPos2());
		}

		if(form.getStorageContainer()!=null)
		{
			specimenUIObject.setStorageContainer(form.getStorageContainer());
		}
		if(form.getSelectedContainerName()!=null)
		{
			specimenUIObject.setStorageContainer(form.getSelectedContainerName());
		}
		specimenUIObject.setReasonForTransfer(form.getReasonForTransfer());
		specimenUIObject.setDispose(form.isDispose());
		specimenUIObject.setDisposeStatus(form.getDisposeStatus());
		specimenUIObject.setReason(form.getReason());
		return specimenUIObject;
	}

	private UIObject createSpecimenUIObject(CreateSpecimenForm form)
	{
		SpecimenUIObject specimenUIObject = new SpecimenUIObject();
		specimenUIObject.setDisposeParentSpecimen(form.getDisposeParentSpecimen());
		return specimenUIObject;
	}

	private UIObject createAliquotUIObject(AliquotForm form)
	{
		SpecimenUIObject specimenUIObject = new SpecimenUIObject();
		specimenUIObject.setDisposeParentSpecimen(form.getDisposeParentSpecimen());
		return specimenUIObject;
	}

	public UIObject createSpecimenUIObject()
	{
		return new SpecimenUIObject();
	}

	private UIObject createSpecimenArrayUIObject(SpecimenArrayForm form)
	{
		SpecimenArrayUIObject specimenArrayUIObject = new SpecimenArrayUIObject();
		return specimenArrayUIObject;
	}

	/**
	 * Get the instance of SpecimenArrayUIObject.
	 * @param form SpecimenArrayAliquotForm
	 * @return specimenArrayUIObject
	 */
	private UIObject createSpecimenArrayUIObject(SpecimenArrayAliquotForm form)
	{
		SpecimenArrayUIObject specimenArrayUIObject = new SpecimenArrayUIObject();
		specimenArrayUIObject.setAliquotCount(Integer.parseInt(form.getAliquotCount()));
		specimenArrayUIObject.setAliqoutMap(form.getSpecimenArrayAliquotMap());
		return specimenArrayUIObject;
	}

	private UIObject createSpecimenArrayUIObject()
	{
		return new SpecimenArrayUIObject();
	}

	private UIObject createShipmentRequestUIObject(ShipmentRequestForm form)
	{
		ShipmentRequestUIObject requestUIObject=new ShipmentRequestUIObject();
		requestUIObject.setRequestProcessed(Boolean.FALSE);
		return requestUIObject;
	}
}
