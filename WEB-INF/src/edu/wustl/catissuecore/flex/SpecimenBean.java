
package edu.wustl.catissuecore.flex;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.ExternalIdentifier;

// TODO: Auto-generated Javadoc
/**
 * The Class SpecimenBean.
 */
public class SpecimenBean implements Externalizable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The sp id. */
	public Long spID = -1L;
	/*public String scgName = "A";
	public String parentSpecimenName = "B";*/
	/** The parent type. */
	public String parentType = "";

	/** The parent name. */
	public String parentName = "";

	/** The specimen label. */
	public String specimenLabel = "";

	/** The specimen barcode. */
	public String specimenBarcode = "";

	/** The lineage. */
	public String lineage = "";

	/** The specimen class. */
	public String specimenClass = "";

	/** The specimen type. */
	public String specimenType = "";

	/** The tissue site. */
	public String tissueSite = "";

	/** The tissue side. */
	public String tissueSide = "";

	/** The pathological status. */
	public String pathologicalStatus = "";

	/** The creation date. */
	public Date creationDate = new Date();

	/** The quantity. */
	public Double quantity = new Double(0);

	/** The concentration. */
	public Double concentration = new Double(0);

	/** The storage. */
	public String storage = "";

	/** The comment. */
	public String comment = "";

	/** The ex id coll. */
	public List<ExternalIdentifier> exIdColl = new ArrayList<ExternalIdentifier>();

	/** The biohazard coll. */
	public List<Biohazard> biohazardColl = new ArrayList<Biohazard>();

	/** The collection event. */
	public EventParamtersBean collectionEvent = new EventParamtersBean();

	/** The received event. */
	public EventParamtersBean receivedEvent = new EventParamtersBean();

	/** The derived coll. */
	public List<SpecimenBean> derivedColl = new ArrayList<SpecimenBean>();

	/** The mode. */
	public String mode = "";

	/**
	 * Instantiates a new specimen bean.
	 */
	public SpecimenBean()
	{

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getClassName()
	 */
	/**
	 * Gets the class name.
	 *
	 * @return the class name
	 */
	public String getClassName()
	{
		// TODO Auto-generated method stub
		return this.specimenClass;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getConcentration()
	 */
	/**
	 * Gets the concentration.
	 *
	 * @return the concentration
	 */
	public String getConcentration()
	{
		// TODO Auto-generated method stub
		if (this.concentration != null)
		{
			return this.concentration.toString();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getDisplayName()
	 */
	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName()
	{
		// TODO Auto-generated method stub
		return this.specimenLabel;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getParentName()
	 */
	/**
	 * Gets the parent name.
	 *
	 * @return the parent name
	 */
	public String getParentName()
	{
		// TODO Auto-generated method stub
		return this.parentName;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getPathologicalStatus()
	 */
	/**
	 * Gets the pathological status.
	 *
	 * @return the pathological status
	 */
	public String getPathologicalStatus()
	{
		// TODO Auto-generated method stub
		return this.pathologicalStatus;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getQuantity()
	 */
	/**
	 * Gets the quantity.
	 *
	 * @return the quantity
	 */
	public String getQuantity()
	{
		// TODO Auto-generated method stub
		if (this.quantity != null)
		{
			return this.quantity.toString();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getStorageContainerForSpecimen()
	 */
	/**
	 * Gets the storage container for specimen.
	 *
	 * @return the storage container for specimen
	 */
	public String getStorageContainerForSpecimen()
	{
		// TODO Auto-generated method stub
		return "Virtual";
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getTissueSide()
	 */
	/**
	 * Gets the tissue side.
	 *
	 * @return the tissue side
	 */
	public String getTissueSide()
	{
		// TODO Auto-generated method stub
		return this.tissueSide;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getTissueSite()
	 */
	/**
	 * Gets the tissue site.
	 *
	 * @return the tissue site
	 */
	public String getTissueSite()
	{
		// TODO Auto-generated method stub
		return this.tissueSite;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getType()
	 */
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType()
	{
		// TODO Auto-generated method stub
		return this.specimenType;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getUniqueIdentifier()
	 */
	/**
	 * Gets the unique identifier.
	 *
	 * @return the unique identifier
	 */
	public String getUniqueIdentifier()
	{
		// TODO Auto-generated method stub
		if (this.spID != null)
		{
			return this.spID.toString();
		}
		return null;

	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	public void writeExternal(ObjectOutput out) throws IOException
	{
		System.out.println("SERVER IN writeExternal START");
		// Write out the client properties from the server representation
		out.writeInt(this.spID.intValue());
		/*out.writeUTF(scgName);
		out.writeUTF(parentSpecimenName);*/
		out.writeUTF(this.parentType);
		out.writeUTF(this.parentName);
		out.writeUTF(this.specimenLabel);
		out.writeUTF(this.specimenBarcode);
		out.writeUTF(this.lineage);
		out.writeUTF(this.specimenClass);
		out.writeUTF(this.specimenType);
		out.writeUTF(this.tissueSite);
		out.writeUTF(this.tissueSide);
		out.writeUTF(this.pathologicalStatus);
		out.writeObject(this.creationDate);
		out.writeDouble(this.quantity);
		out.writeDouble(this.concentration);
		out.writeUTF(this.storage);
		out.writeUTF(this.comment);
		out.writeObject(this.exIdColl);
		out.writeObject(this.biohazardColl);
		out.writeObject(this.collectionEvent);
		out.writeObject(this.receivedEvent);
		out.writeObject(this.derivedColl);

		System.out.println(this.toString());
		System.out.println("SERVER IN writeExternal DONE");
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		System.out.println("SERVER IN readExternal");

		this.spID = new Long(in.readInt());
		/*scgName = in.readUTF();
		parentSpecimenName = in.readUTF();*/
		this.parentType = in.readUTF();
		this.parentName = in.readUTF();
		this.specimenLabel = in.readUTF();
		this.specimenBarcode = in.readUTF();
		this.lineage = in.readUTF();
		this.specimenClass = in.readUTF();
		this.specimenType = in.readUTF();
		this.tissueSite = in.readUTF();
		this.tissueSide = in.readUTF();
		this.pathologicalStatus = in.readUTF();
		this.creationDate = (Date) in.readObject();
		this.quantity = in.readDouble();
		this.concentration = in.readDouble();
		this.storage = in.readUTF();
		this.comment = in.readUTF();
		//Object obj2 = in.readObject();
		//System.out.println("obj2 "+obj2.getClass()+" "+obj2);
		this.exIdColl = (List) in.readObject();
		this.biohazardColl = (List) in.readObject();
		this.collectionEvent = (EventParamtersBean) in.readObject();
		this.receivedEvent = (EventParamtersBean) in.readObject();
		this.derivedColl = (List) in.readObject();

		System.out.println(this.toString());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "spID " + this.spID + "\n" + "parentName " + this.parentName + "\n"
				+ "specimenLabel " + this.specimenLabel + "\n" + "specimenBarcode "
				+ this.specimenBarcode + "\n" + "lineage " + this.lineage + "\n" + "specimenClass "
				+ this.specimenClass + "\n" + "specimenType " + this.specimenType + "\n"
				+ "tissueSite " + this.tissueSite + "\n" + "tissueSide " + this.tissueSide + "\n"
				+ "pathologicalStatus " + this.pathologicalStatus + "\n" + "creationDate "
				+ this.creationDate + "\n" + "quantity " + this.quantity + "\n" + "concentration "
				+ this.concentration + "\n" + "comment " + this.comment + "\n" + "exIdColl "
				+ this.exIdColl + "\n" + "collEvent " + this.collectionEvent;
	}

	/**
	 * Gets the lineage.
	 *
	 * @return the lineage
	 */
	public String getLineage()
	{
		return this.lineage;
	}

	/**
	 * Sets the lineage.
	 *
	 * @param lineage the new lineage
	 */
	public void setLineage(String lineage)
	{
		this.lineage = lineage;
	}
}
