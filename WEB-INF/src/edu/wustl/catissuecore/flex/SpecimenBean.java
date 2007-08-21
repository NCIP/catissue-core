package edu.wustl.catissuecore.flex;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.wustl.catissuecore.domain.ExternalIdentifier;

public class SpecimenBean implements Externalizable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Long spID = -5L;
	public String scgName = "A";
	public String parentSpecimenName = "B";
	
	public String specimenLabel = "C";
	public String specimenBarcode = "D";
	
	public String specimenClass = "Tissue";
	public String specimenType = "Fresh Tissue";
	
	public String tissueSite = "Heart";
	public String tissueSide = "Right";
	public String pathologicalStatus = "Metastatic";
	
	public Date creationDate = new Date();
	public Double quantity = new Double(10);
	public Double concentration = new Double(20);
	public String comment = "MY COMMENT";
	public List<ExternalIdentifier> exIdColl = new ArrayList<ExternalIdentifier>();
	
	public SpecimenBean()
	{
		ExternalIdentifier ei1 = new ExternalIdentifier();
		ei1.setId(-3L);
		ei1.setName("EIN1");
		ei1.setValue("EIV1");
		
		ExternalIdentifier ei2 = new ExternalIdentifier();
		ei2.setId(-4L);
		ei2.setName("EIN2");
		ei2.setValue("EIV2");
		
		exIdColl.add(ei1);
		exIdColl.add(ei2);
		
	}
	
	public void writeExternal(ObjectOutput out) throws IOException 
	{
		System.out.println("SERVER IN writeExternal START");
        // Write out the client properties from the server representation
		out.writeInt(spID.intValue());
		out.writeUTF(scgName);
        out.writeUTF(parentSpecimenName);
        out.writeUTF(specimenLabel);
        out.writeUTF(specimenBarcode);
        out.writeUTF(specimenClass);
        out.writeUTF(specimenType);
        out.writeUTF(tissueSite);
        out.writeUTF(tissueSide);
        out.writeUTF(pathologicalStatus);
        out.writeObject(creationDate);
        out.writeDouble(quantity);
        out.writeDouble(concentration);
        out.writeUTF(comment);
        out.writeObject(exIdColl);
        
        System.out.println(toString());
        System.out.println("SERVER IN writeExternal DONE");
    }
    
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		System.out.println("SERVER IN readExternal");
		
		spID = new Long(in.readInt());
		scgName = in.readUTF();
		parentSpecimenName = in.readUTF();
		specimenLabel = in.readUTF();
		specimenBarcode = in.readUTF();
		specimenClass = in.readUTF();
		specimenType = in.readUTF();
		tissueSite = in.readUTF();
		tissueSide = in.readUTF();
		pathologicalStatus = in.readUTF();
		creationDate = (Date)in.readObject();
		quantity = in.readDouble();
		concentration = in.readDouble();		
		comment = in.readUTF();
		//Object obj2 = in.readObject();
		//System.out.println("obj2 "+obj2.getClass()+" "+obj2);
		exIdColl = (List)in.readObject();
		
		System.out.println(toString());
	}
	
	public String toString()
	{
		return 	   "spID "+spID+"\n"+
				   "scgName "+scgName+"\n"+
				   "parentSpecimenName "+parentSpecimenName+"\n"+
				   "specimenLabel "+specimenLabel+"\n"+
				   "specimenBarcode "+specimenBarcode+"\n"+
				   "specimenClass "+specimenClass+"\n"+
				   "specimenType "+specimenType+"\n"+
				   "tissueSite "+tissueSite+"\n"+
				   "tissueSide "+tissueSide+"\n"+
				   "pathologicalStatus "+pathologicalStatus+"\n"+
				   "creationDate "+creationDate+"\n"+
				   "quantity "+quantity+"\n"+
				   "concentration "+concentration+"\n"+
				   "comment "+comment+"\n"+
				   "exIdColl "+exIdColl;
	}
}
