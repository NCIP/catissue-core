
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

public class SpecimenBean implements Externalizable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Long spID = -1L;
	/*public String scgName = "A";
	public String parentSpecimenName = "B";*/
	public String parentType = "";
	public String parentName = "";

	public String specimenLabel = "";
	public String specimenBarcode = "";
	public String lineage = "";

	public String specimenClass = "";
	public String specimenType = "";

	public String tissueSite = "";
	public String tissueSide = "";
	public String pathologicalStatus = "";

	public Date creationDate = new Date();
	public Double quantity = new Double(0);
	public Double concentration = new Double(0);
	public String storage= "";
	public String comment = "";
	public List<ExternalIdentifier> exIdColl = new ArrayList<ExternalIdentifier>();
	public List<Biohazard> biohazardColl = new ArrayList<Biohazard>();
	public EventParamtersBean collectionEvent = new EventParamtersBean();
	public EventParamtersBean receivedEvent = new EventParamtersBean();
	public List<SpecimenBean> derivedColl = new ArrayList<SpecimenBean>();
	
	public String mode = ""; 

	public SpecimenBean()
	{
		

	}

	
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getClassName()
	 */
	public String getClassName()
	{
		// TODO Auto-generated method stub
		return this.specimenClass;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getConcentration()
	 */
	public String getConcentration()
	{
		// TODO Auto-generated method stub
		if (concentration != null)
		{
			return this.concentration.toString();
		}
		return null;
	}

	
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getDisplayName()
	 */
	public String getDisplayName()
	{
		// TODO Auto-generated method stub
		return this.specimenLabel;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getParentName()
	 */
	public String getParentName()
	{
		// TODO Auto-generated method stub
		return this.parentName;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getPathologicalStatus()
	 */
	public String getPathologicalStatus()
	{
		// TODO Auto-generated method stub
		return this.pathologicalStatus;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getQuantity()
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
	public String getStorageContainerForSpecimen()
	{
		// TODO Auto-generated method stub
		return "Virtual";
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getTissueSide()
	 */
	public String getTissueSide()
	{
		// TODO Auto-generated method stub
		return this.tissueSide;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getTissueSite()
	 */
	public String getTissueSite()
	{
		// TODO Auto-generated method stub
		return this.tissueSite;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getType()
	 */
	public String getType()
	{
		// TODO Auto-generated method stub
		return this.specimenType;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getUniqueIdentifier()
	 */
	public String getUniqueIdentifier()
	{
		// TODO Auto-generated method stub
		if (spID != null)
		{
			return this.spID.toString();
		}
		return null;

	}

	public void writeExternal(ObjectOutput out) throws IOException
	{
		System.out.println("SERVER IN writeExternal START");
		// Write out the client properties from the server representation
		out.writeInt(spID.intValue());
		/*out.writeUTF(scgName);
		out.writeUTF(parentSpecimenName);*/
		out.writeUTF(parentType);
		out.writeUTF(parentName);
		out.writeUTF(specimenLabel);
		out.writeUTF(specimenBarcode);
		out.writeUTF(lineage);
		out.writeUTF(specimenClass);
		out.writeUTF(specimenType);
		out.writeUTF(tissueSite);
		out.writeUTF(tissueSide);
		out.writeUTF(pathologicalStatus);
		out.writeObject(creationDate);
		out.writeDouble(quantity);
		out.writeDouble(concentration);
		out.writeUTF(storage);
		out.writeUTF(comment);
		out.writeObject(exIdColl);
		out.writeObject(biohazardColl);
		out.writeObject(collectionEvent);
		out.writeObject(receivedEvent);
		out.writeObject(derivedColl);
		
		System.out.println(toString());
		System.out.println("SERVER IN writeExternal DONE");
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		System.out.println("SERVER IN readExternal");

		spID = new Long(in.readInt());
		/*scgName = in.readUTF();
		parentSpecimenName = in.readUTF();*/
		parentType = in.readUTF();
		parentName = in.readUTF();
		specimenLabel = in.readUTF();
		specimenBarcode = in.readUTF();
		lineage = in.readUTF();
		specimenClass = in.readUTF();
		specimenType = in.readUTF();
		tissueSite = in.readUTF();
		tissueSide = in.readUTF();
		pathologicalStatus = in.readUTF();
		creationDate = (Date) in.readObject();
		quantity = in.readDouble();
		concentration = in.readDouble();
		storage = in.readUTF();
		comment = in.readUTF();
		//Object obj2 = in.readObject();
		//System.out.println("obj2 "+obj2.getClass()+" "+obj2);
		exIdColl = (List) in.readObject();
		biohazardColl = (List) in.readObject();
		collectionEvent = (EventParamtersBean) in.readObject();
		receivedEvent = (EventParamtersBean) in.readObject();
		derivedColl = (List) in.readObject();
		
		System.out.println(toString());
	}

	public String toString()
	{
		return "spID " + spID + "\n" + "parentName " + parentName + "\n" + "specimenLabel "
				+ specimenLabel + "\n" + "specimenBarcode " + specimenBarcode + "\n" + "lineage " + lineage +"\n" + "specimenClass " + specimenClass + "\n" + "specimenType "
				+ specimenType + "\n" + "tissueSite " + tissueSite + "\n" + "tissueSide " + tissueSide + "\n" + "pathologicalStatus "
				+ pathologicalStatus + "\n" + "creationDate " + creationDate + "\n" + "quantity " + quantity + "\n" + "concentration "
				+ concentration + "\n" + "comment " + comment + "\n" + "exIdColl " + exIdColl + "\n" + "collEvent " + collectionEvent;
	}


	public String getLineage() {
		return lineage;
	}


	public void setLineage(String lineage) {
		this.lineage = lineage;
	}
}
