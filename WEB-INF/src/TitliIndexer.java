import java.io.File;

import titli.controller.Name;
import titli.controller.interfaces.TitliInterface;
import titli.model.Titli;
import titli.model.TitliException;
import titli.model.util.IndexUtility;
import titli.model.util.TitliResultGroup;

public class TitliIndexer 
{

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
			System.out.println("Exception occured while initialising TiTLi Search");
			e.printStackTrace();
		}
	}
}
