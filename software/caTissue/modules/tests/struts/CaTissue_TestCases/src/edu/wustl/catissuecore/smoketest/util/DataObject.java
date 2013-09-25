/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.smoketest.util;

import java.util.StringTokenizer;

public class DataObject
{
	String id;
	String name;
	String values[];
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String[] getValues()
	{
		return values;
	}
	public void setValues(String[] values)
	{
		this.values = values;
	}
	public void addValues(String [] values)
	{

			id = values[0];
			name = values[1];
			StringTokenizer st = new StringTokenizer(values[2], "|");
			String arr[] = new String[st.countTokens()];
			int i=0;
			while (st.hasMoreElements())
			{
				arr[i] = new String();
				arr[i] = st.nextElement().toString();
				i++;
			}
			this.values = arr;
	}
}
