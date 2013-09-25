/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

import java.io.File;

import titli.controller.Name;
import titli.controller.interfaces.TitliInterface;
import titli.model.Titli;
import titli.model.TitliException;
import titli.model.util.IndexUtility;
import edu.wustl.common.util.logger.Logger;

public class TitliIndexer 
{
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getCommonLogger(TitliIndexer.class);
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		initTitliIndex();
	}
	
	private static void initTitliIndex()
	{
		try
		{
			TitliInterface titli = Titli.getInstance();
			
			Name dbName = titli.getDatabases().keySet().toArray(new Name[0])[0];
			File dbIndexLocation = IndexUtility.getIndexDirectoryForDatabase(dbName);


			if(!dbIndexLocation.exists())
			{
				titli.index();
			}

		}
		catch (TitliException e)
		{
			TitliIndexer.logger.error(e.getMessage(),e) ;
			System.out.println("Exception occured while initialising Keyword Search");
			e.printStackTrace();
		}
	}
}
