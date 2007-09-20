package edu.wustl.catissuecore.flex.dag;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class DAGPath implements Externalizable {
	private String toolTip =null;
	private String id= null;
	private boolean isSelected = false;
	private int sourceExpId =0;
	private int destinationExpId =0;
		
	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}
	
	
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		toolTip = in.readUTF();
		id =   in.readUTF();
		isSelected = in.readBoolean();
		sourceExpId = in.readInt();
		destinationExpId = in.readInt();
		
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeUTF(toolTip);
		out.writeUTF(id);
		out.writeBoolean(isSelected);
		out.writeInt(sourceExpId);
		out.writeInt(destinationExpId);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSourceExpId() {
		return sourceExpId;
	}

	public void setSourceExpId(int sourceExpId) {
		this.sourceExpId = sourceExpId;
	}

	public int getDestinationExpId() {
		return destinationExpId;
	}

	public void setDestinationExpId(int destinationExpId) {
		this.destinationExpId = destinationExpId;
	}



	

	


}
