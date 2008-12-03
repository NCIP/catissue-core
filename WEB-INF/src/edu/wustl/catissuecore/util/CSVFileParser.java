
package edu.wustl.catissuecore.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author suhas_khot
 *
 */
public class CSVFileParser extends FileReader
{

	//CSV file reader
	private CSVReader reader;
	//stores the line
	private String[] line;
	//stores line number
	private static long lineNumber = 0;
	//stores entityIdsCollection
	private List<Long> entityIds = new ArrayList<Long>();
	//stores formIdsCollection
	private List<Long> formIds = new ArrayList<Long>();
	//store entityGroup ids
	private Set<Long> entityGroupIds = new HashSet<Long>();

	/**
	 * @return the entityGroupIds
	 */
	public Set<Long> getEntityGroupIds()
	{
		return entityGroupIds;
	}

	/**
	 * @param entityGroupIds the entityGroupIds to set
	 */
	public void setEntityGroupIds(Set<Long> entityGroupIds)
	{
		this.entityGroupIds = entityGroupIds;
	}

	/**
	 * @return the entityIds
	 */
	public List<Long> getEntityIds()
	{
		return entityIds;
	}

	/**
	 * @param entityIds the entityIds to set
	 */
	public void setEntityIds(List<Long> entityIds)
	{
		this.entityIds = entityIds;
	}

	/**
	 * @return the formIds
	 */
	public List<Long> getFormIds()
	{
		return formIds;
	}

	/**
	 * @param formIds the formIds to set
	 */
	public void setFormIds(List<Long> formIds)
	{
		this.formIds = formIds;
	}

	/**
	 * @param filePath
	 * @throws FileNotFoundException file path incorrect
	 */
	public CSVFileParser(String filePath) throws FileNotFoundException
	{
		super(filePath);
		reader = new CSVReader(new FileReader(filePath));
	}

	/**
	 * @return the lineNumber
	 */
	private long getLineNumber()
	{
		return lineNumber;
	}

	/**
	 * This method reads the next line 
	 * @return true if read next line is not null
	 * @throws IOException fails to read file
	 */
	private boolean readNext() throws IOException
	{
		boolean readNextLine = true;
		//To skip the blank lines
		while ((line = reader.readNext()) != null)
		{
			lineNumber++;
			if (line[0].length() != 0 && !line[0].startsWith("##"))
			{
				break;
			}
		}

		if (line == null)
		{
			readNextLine = false;
		}
		return readNextLine;

	}

	/**
	 * @return current line
	 */
	private String[] readLine()
	{
		return line;
	}

	/**
	 * @return entityGroup name
	 * @throws DynamicExtensionsSystemException 
	 */
	private String getEntityGroupName() throws DynamicExtensionsSystemException
	{
		String entityGroupName = null;
		if (isEntityGroup())
		{
			String[] tokens = readLine()[0].split(":");
			if (tokens.length > 1)
			{
				entityGroupName = readLine()[0].split(":")[1].trim();
			}
			else
			{
				throw new DynamicExtensionsSystemException("EntityGroup cannot be kept blank. Please enter EntityGroup name at line "+getLineNumber());
			}
		}
		return entityGroupName;
	}

	/**
	 * @return form names
	 */
	private String[] getFormNames()
	{
		String[] formNames = {};
		if (!isEntityGroup() && isCategory())
		{
			String[] tokens = readLine()[0].split(":");
			if (tokens.length > 1)
			{
				readLine()[0] = readLine()[0].split(":")[1].trim();
				formNames = readLine();
			}
		}
		return formNames;
	}

	/**
	 * @return entities name
	 */
	private String[] getEntitiesName()
	{
		String[] entityNames = null;
		if ((!isEntityGroup()) && (!isCategory()))
		{
			entityNames = readLine();
		}
		return entityNames;
	}

	/**
	 * @return true if it is entityGroup
	 */
	private boolean isEntityGroup()
	{
		boolean isEntityGroup = false;
		if (readLine()[0].contains(Constants.ENTITY_GROUP))
		{
			isEntityGroup = true;
		}
		return isEntityGroup;
	}

	/**
	 * @return true if category
	 */
	private boolean isCategory()
	{
		boolean isCategory = false;
		if (readLine() != null && readLine()[0].contains(Constants.CATEGORY))
		{
			isCategory = true;
		}
		return isCategory;
	}

	/**
	 * @throws IOException fails to read file
	 * @throws DynamicExtensionsSystemException fail to retrieve entity forms
	 */
	public void processCSV() throws IOException, DynamicExtensionsSystemException
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		Long entityGroupId = null;
		while (readNext())
		{
			if (isEntityGroup())
			{
				entityGroupId = entityManager.getEntityGroupId(getEntityGroupName());
				if (entityGroupId == null)
				{
					throw new DynamicExtensionsSystemException(
							"Please enter valid EntityGroup name. Error at line number "
									+ getLineNumber());
				}
				entityGroupIds.add(entityGroupId);
				while (readNext())
				{
					if (readLine() != null && !isCategory() && !isEntityGroup()
							&& readLine()[0].trim().length() != 0)
					{
						for (String entityName : getEntitiesName())
						{

							Long entityId = entityManager.getEntityId(entityName, entityGroupId);
							if (entityId == null)
							{
								throw new DynamicExtensionsSystemException(
										"Please enter valid Entity name. Error at line number "
												+ getLineNumber());
							}
							entityIds.add(entityId);
						}
					}
					if (isEntityGroup())
					{
						entityGroupId = entityManager.getEntityGroupId(getEntityGroupName());
						if (entityGroupId == null)
						{
							throw new DynamicExtensionsSystemException(
									"Please enter valid EntityGroup name. Error at line number "
											+ getLineNumber());
						}
						entityGroupIds.add(entityGroupId);
					}
					if (isCategory())
					{
						String[] formNames=getFormNames();
						if(formNames!= null && formNames.length>0)
						{
							for (String formName : formNames)
							{
								Long rootCategoryEntityId = entityManager
										.getRootCategoryEntityIdByCategoryName(formName);
								if (rootCategoryEntityId == null)
								{
									throw new DynamicExtensionsSystemException(
											"Please enter valid category name. Error at line number "
													+ getLineNumber());
								}
								formIds.add(rootCategoryEntityId);
							}
						}
					}
				}
			}
			break;
		}
		setEntityIds(entityIds);
		setFormIds(formIds);
		setEntityGroupIds(entityGroupIds);
	}
}
