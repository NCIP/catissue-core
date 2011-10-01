
package edu.wustl.catissuecore.actionForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.GenericSpecimenVO;
import edu.wustl.catissuecore.bean.SpecimenDetailsInfo;

/**
 * action form used to carry specimens, aliquotes and derived
 * for specimen summary page.
 * @author abhijit_naik
 *
 */
public class ViewSpecimenSummaryForm extends ActionForm
		implements
			SpecimenDetailsInfo,
			IPrinterTypeLocation
{

	/**
	 * Unique Serial verson uid.
	 */
	private static final long serialVersionUID = -7978857673984149449L;
	public static final String ADD_USER_ACTION = "ADD";
	public static final String UPDATE_USER_ACTION = "UPDATE";
	public static final String REQUEST_TYPE_MULTI_SPECIMENS = "Multiple Specimen";
	public static final String REQUEST_TYPE_COLLECTION_PROTOCOL = "Collection Protocol";
	public static final String REQUEST_TYPE_ANTICIPAT_SPECIMENS = "anticipatory specimens";

	private List<GenericSpecimen> specimenList = null;
	private List<GenericSpecimen> aliquotList = null;
	private List<GenericSpecimen> derivedList = null;
	private String eventId = null;
	private String selectedSpecimenId = null;
	private String userAction = ADD_USER_ACTION;
	private String requestType;
	private Object summaryObject = null;
	private String lastSelectedSpecimenId = null;
	private String containerMap;
	private String targetSuccess;
	private String submitAction;
	private boolean showParentStorage = true;

	private boolean showCheckBoxes = true;
	private boolean showbarCode = true;
	private boolean showLabel = true;
	private boolean readOnly = false;
	private String printCheckbox;
	private String printerType;
	private String printerLocation;

	private boolean generateLabel;

	public boolean isGenerateLabel()
	{
		return generateLabel;
	}


	public void setGenerateLabel(boolean generateLabel)
	{
		this.generateLabel = generateLabel;
	}

	private boolean multipleSpEditMode = false;

	private Set specimenPrintList = null;//janhavi

	public Set getSpecimenPrintList()
	{
		return this.specimenPrintList;
	}

	public void setSpecimenPrintList(Set<GenericSpecimen> specimenPrintList)
	{
		this.specimenPrintList = specimenPrintList;
	}

	public boolean isMultipleSpEditMode()
	{
		return this.multipleSpEditMode;
	}

	public void setMultipleSpEditMode(boolean multipleSpEditMode)
	{
		this.multipleSpEditMode = multipleSpEditMode;
	}

	public boolean getReadOnly()
	{
		return this.readOnly;
	}

	public void setReadOnly(boolean readOnly)
	{
		this.readOnly = readOnly;
		this.setReadOnlyModeToAll();
	}

	private final HashMap<String, String> titleMap = new HashMap<String, String>();

	private static String collectionProtocolStatus = "";

	private static String specimenExist = "";

	public ViewSpecimenSummaryForm()
	{
		this.titleMap.put(REQUEST_TYPE_MULTI_SPECIMENS, "Specimen details");
		this.titleMap.put(REQUEST_TYPE_COLLECTION_PROTOCOL, "Specimen requirement(s)");
		this.titleMap.put(REQUEST_TYPE_ANTICIPAT_SPECIMENS, "Specimen details");
		this.specimenList = new ArrayList<GenericSpecimen>();
		this.aliquotList = new ArrayList<GenericSpecimen>();
		this.derivedList = new ArrayList<GenericSpecimen>();
		this.userAction = ADD_USER_ACTION;
		this.requestType = REQUEST_TYPE_COLLECTION_PROTOCOL;
		collectionProtocolStatus = "";

	}

	@Override
	public void reset(ActionMapping mapping, ServletRequest request)
	{
		this.specimenList = new ArrayList<GenericSpecimen>();
		this.aliquotList = new ArrayList<GenericSpecimen>();
		this.derivedList = new ArrayList<GenericSpecimen>();
	}

	public String getTitle()
	{
		return this.titleMap.get(this.requestType);
	}

	public String getUserAction()
	{
		return this.userAction;
	}

	public void setUserAction(String userAction)
	{
		this.userAction = userAction;
	}

	public String getRequestType()
	{
		return this.requestType;
	}

	public void switchUserAction()
	{

		if (UPDATE_USER_ACTION.equals(this.userAction))
		{
			this.userAction = ADD_USER_ACTION;
		}
		else
		{
			this.userAction = UPDATE_USER_ACTION;
		}
	}

	public void setRequestType(String requestType)
	{
		this.requestType = requestType;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.SpecimenDetailsInfo#getSelectedSpecimenId()
	 */
	public String getSelectedSpecimenId()
	{
		return this.selectedSpecimenId;
	}

	public void setSelectedSpecimenId(String selectedSpecimenId)
	{
		this.selectedSpecimenId = selectedSpecimenId;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.SpecimenDetailsInfo#getSpecimenList()
	 */
	public List<GenericSpecimen> getSpecimenList()
	{
		return this.specimenList;
	}

	public void setSpecimenList(List<GenericSpecimen> specimenList)
	{
		if (specimenList != null)
		{
			this.specimenList = specimenList;
		}
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.SpecimenDetailsInfo#getAliquotList()
	 */
	public List<GenericSpecimen> getAliquotList()
	{
		return this.aliquotList;
	}

	public void setAliquotList(List<GenericSpecimen> aliquoteList)
	{
		if (aliquoteList != null)
		{
			this.aliquotList = aliquoteList;
		}
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.SpecimenDetailsInfo#getDerivedList()
	 */
	public List<GenericSpecimen> getDerivedList()
	{
		return this.derivedList;
	}

	public void setDerivedList(List<GenericSpecimen> derivedList)
	{
		if (derivedList != null)
		{
			this.derivedList = derivedList;
		}
	}

	public GenericSpecimen getDerived(int index)
	{

		while (index >= this.derivedList.size())
		{
			this.derivedList.add(this.getNewSpecimen());
		}
		return this.derivedList.get(index);
	}

	public void setDerived(int index, GenericSpecimen derivedSpecimen)
	{
		this.derivedList.add(index, derivedSpecimen);
	}

	public GenericSpecimen getAliquot(int index)
	{

		while (index >= this.aliquotList.size())
		{
			this.aliquotList.add(this.getNewSpecimen());
		}

		return this.aliquotList.get(index);
	}

	public void setAliquot(int index, GenericSpecimen aliquotSpecimen)
	{
		this.aliquotList.add(index, aliquotSpecimen);
	}

	public GenericSpecimen getSpecimen(int index)
	{

		while (index >= this.specimenList.size())
		{
			this.specimenList.add(this.getNewSpecimen());
		}
		return this.specimenList.get(index);
	}

	public void setSpecimen(int index, GenericSpecimen specimen)
	{
		this.aliquotList.add(index, specimen);
	}

	public String getEventId()
	{
		return this.eventId;
	}

	public void setEventId(String eventId)
	{

		this.eventId = eventId;
	}

	private GenericSpecimen getNewSpecimen()
	{
		return new GenericSpecimenVO();
	}

	public Object getSummaryObject()
	{
		return this.summaryObject;
	}

	public void setSummaryObject(Object summaryObject)
	{
		this.summaryObject = summaryObject;
	}

	public String getLastSelectedSpecimenId()
	{
		return this.lastSelectedSpecimenId;
	}

	public void setLastSelectedSpecimenId(String lastEventId)
	{
		this.lastSelectedSpecimenId = lastEventId;
	}

	public String getContainerMap()
	{
		return this.containerMap;
	}

	public void setContainerMap(String containerMap)
	{
		this.containerMap = containerMap;
	}

	public String getTargetSuccess()
	{
		return this.targetSuccess;
	}

	public void setTargetSuccess(String targetSuccess)
	{
		this.targetSuccess = targetSuccess;
	}

	public String getSubmitAction()
	{
		return this.submitAction;
	}

	public void setSubmitAction(String submitAction)
	{
		this.submitAction = submitAction;
	}

	public String getCollectionProtocolStatus()
	{
		return collectionProtocolStatus;
	}

	public static void setCollectionProtocolStatus(String collectionProtocolStatus)
	{
		ViewSpecimenSummaryForm.collectionProtocolStatus = collectionProtocolStatus;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.SpecimenDetailsInfo#getShowCheckBoxes()
	 */
	public boolean getShowCheckBoxes()
	{
		return this.showCheckBoxes;
	}

	public void setShowCheckBoxes(boolean showCheckBoxes)
	{
		this.showCheckBoxes = showCheckBoxes;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.SpecimenDetailsInfo#getShowbarCode()
	 */
	public boolean getShowbarCode()
	{
		return this.showbarCode;
	}

	public void setShowbarCode(boolean showbarCode)
	{
		this.showbarCode = showbarCode;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.SpecimenDetailsInfo#getShowLabel()
	 */
	public boolean getShowLabel()
	{
		return this.showLabel;
	}

	public void setShowLabel(boolean showLabel)
	{
		this.showLabel = showLabel;
	}

	public String getSpecimenExist()
	{
		return specimenExist;
	}

	public static void setSpecimenExist(String specimenExist)
	{
		ViewSpecimenSummaryForm.specimenExist = specimenExist;
	}

	public boolean getShowParentStorage()
	{
		return this.showParentStorage;
	}

	public void setShowParentStorage(boolean showParentStorage)
	{
		this.showParentStorage = showParentStorage;
	}

	public String getPrintCheckbox()
	{
		return this.printCheckbox;
	}

	public void setPrintCheckbox(String printCheckbox)
	{
		this.printCheckbox = printCheckbox;
	}

	private void setReadOnlyModeToAll()
	{
		Iterator<GenericSpecimen> specIterator = this.specimenList.iterator();
		this.setReadOnlyMode(specIterator);
		specIterator = this.aliquotList.iterator();
		this.setReadOnlyMode(specIterator);
		specIterator = this.derivedList.iterator();
		this.setReadOnlyMode(specIterator);
	}

	/**
	 * @param readOnlyMode
	 * @param specIterator
	 */
	private void setReadOnlyMode(Iterator<GenericSpecimen> specIterator)
	{
		while (specIterator.hasNext())
		{
			final GenericSpecimen genericSpecimen = specIterator.next();
			if (genericSpecimen.getCheckedSpecimen())
			{
				genericSpecimen.setReadOnly(this.readOnly);
			}
		}
	}

	protected String forwardTo = null;

	/**
	 * @return Returns the forwardTo.
	 */
	public String getForwardTo()
	{
		return this.forwardTo;
	}

	/**
	 * @param forwardTo The forwardTo to set.
	 */
	public void setForwardTo(String forwardTo)
	{
		this.forwardTo = forwardTo;
	}

	public String getPrinterLocation()
	{
		return this.printerLocation;
	}

	public void setPrinterLocation(String printerLocation)
	{
		this.printerLocation = printerLocation;
	}

	public String getPrinterType()
	{
		return this.printerType;
	}

	public void setPrinterType(String printerType)
	{
		this.printerType = printerType;
	}
}
