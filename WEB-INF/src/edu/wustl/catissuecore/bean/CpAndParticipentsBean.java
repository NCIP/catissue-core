
package edu.wustl.catissuecore.bean;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author janhavi_hasabnis
 */
public class CpAndParticipentsBean implements Externalizable,Comparable<CpAndParticipentsBean>
{
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * name.
	 */
	private String name;
	/**
	 * value.
	 */
	private String value;
	
	private Boolean isPHIView;

	

	/**
	 * Default Constructor.
	 */
	public CpAndParticipentsBean()
	{

	}

	/**
	 * @param nameParam - nameParam
	 * @param valueParam - valueParam
	 */
	public CpAndParticipentsBean(String nameParam, String valueParam,boolean isPhiView)
	{
		this.name = nameParam;
		this.value = valueParam;
		this.isPHIView = isPhiView;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @param nameParam The name to set.
	 */
	public void setName(String nameParam)
	{
		//System.out.println("The name written is:"+name);
		this.name = nameParam;
	}

	/**
	 * @return Returns the value.
	 */
	public String getValue()
	{
		return this.value;
	}

	/**
	 * @param valueParam The value to set.
	 */
	public void setValue(String valueParam)
	{
		this.value = valueParam;
	}

	/**
	 * @param in - ObjectInput
	 * @throws IOException - IOException
	 * @throws ClassNotFoundException - ClassNotFoundException
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		this.name = in.readUTF();
		this.isPHIView = in.readBoolean();
	}

	/**
	 * @param out - ObjectOutput
	 * @throws IOException - IOException
	 */
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeUTF(this.name);
		out.writeUTF(this.value);
		out.writeBoolean(this.isPHIView);

	}

	/**
	 * @return String
	 */
	@Override
	public String toString()
	{
		return new String("name:" + this.name + " value:" + this.value);
	}
	
	public boolean isPHIView() {
		return isPHIView;
	}

	public void setPHIView(boolean isPHIView) {
		this.isPHIView = isPHIView;
	}

	@Override
	public int compareTo(CpAndParticipentsBean cpBean) {
		
			int result = 0;
			if (this.getClass().getName().equals(cpBean.getClass().getName()))
			{
				final CpAndParticipentsBean cpbean = (CpAndParticipentsBean) cpBean;
				result = String.CASE_INSENSITIVE_ORDER.compare(this.getName(),cpbean.getName());
		        if (result == 0) {
		        	result = this.getName().compareTo(cpbean.getName());
		        }
				
			}
			return result;
		
	}
	@Override
	public boolean equals(Object obj)
	{
		Boolean result = Boolean.FALSE;
		if (this == obj)
		{
			result = true;
		}
		if (obj == null)
		{
			result = false;
		}
		if(obj instanceof CpAndParticipentsBean)
		{
			CpAndParticipentsBean bean = (CpAndParticipentsBean)obj;
			result = this.value.equals(bean.value);
		}
		return result;
	}
	
	@Override
	public int hashCode()
	{
		return Integer.valueOf(this.value);
	}
}
