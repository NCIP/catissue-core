/**
 */

package edu.wustl.catissuecore.dto;



public class ParticipantConsentFileDTO {
	
	private String fileName=null;
	byte[] byteArr= {};
	
	public String getFileName()
	{
		return fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public byte[] getByteArr()
	{
		return byteArr;
	}
	
	public void setByteArr(byte[] byteArr)
	{
		this.byteArr = byteArr;
	}
		
}
