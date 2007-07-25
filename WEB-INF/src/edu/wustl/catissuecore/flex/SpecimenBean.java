package edu.wustl.catissuecore.flex;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class SpecimenBean implements Externalizable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String scgName = "A";
	public String parentSpecimenName = "B";
	
	public String specimenLabel = "C";
	public String specimenBarcode = "D";
	
	public String specimenClass = "E";
	public String specimenType = "F";
	
	public String tissueSite = "G";
	public String tissueSide = "H";
	public String pathologicalStatus = "I";
	
	//public Date creationDate;
	public Double quantity = new Double(1);
	public Double concentration = new Double(2);
	
	public void writeExternal(ObjectOutput out) throws IOException 
	{
		System.out.println("SERVER IN writeExternal START");
        // Write out the client properties from the server representation
        out.writeUTF(scgName);
        out.writeUTF(parentSpecimenName);
        out.writeUTF(specimenLabel);
        out.writeUTF(specimenBarcode);
        out.writeUTF(specimenClass);
        out.writeUTF(specimenType);
        out.writeUTF(tissueSite);
        out.writeUTF(tissueSide);
        out.writeUTF(pathologicalStatus);
        
        out.writeDouble(quantity);
        out.writeDouble(concentration);
        
        System.out.println(toString());
        System.out.println("SERVER IN writeExternal DONE");
    }
    
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		System.out.println("SERVER IN readExternal");
		scgName = in.readUTF();
		parentSpecimenName = in.readUTF();
		specimenLabel = in.readUTF();
		specimenBarcode = in.readUTF();
		specimenClass = in.readUTF();
		specimenType = in.readUTF();
		tissueSite = in.readUTF();
		tissueSide = in.readUTF();
		pathologicalStatus = in.readUTF();
        
		quantity = in.readDouble();
		concentration = in.readDouble();
		
		System.out.println(toString());
	}
	
	public String toString()
	{
		return "scgName "+scgName+"\n"+
				   "parentSpecimenName "+parentSpecimenName+"\n"+
				   "specimenLabel "+specimenLabel+"\n"+
				   "specimenBarcode "+specimenBarcode+"\n"+
				   "specimenClass "+specimenClass+"\n"+
				   "specimenType "+specimenType+"\n"+
				   "tissueSite "+tissueSite+"\n"+
				   "tissueSide "+tissueSide+"\n"+
				   "pathologicalStatus "+pathologicalStatus+"\n"+
				   "quantity "+quantity+"\n"+
				   "concentration "+concentration;
	}
}
