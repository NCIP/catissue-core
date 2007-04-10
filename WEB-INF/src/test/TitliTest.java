/**
 * 
 */
package test;

import java.util.Date;

import titli.controller.interfaces.MatchListInterface;
import titli.controller.interfaces.ResultGroupInterface;
import titli.controller.interfaces.SortedResultMapInterface;
import titli.controller.interfaces.TitliInterface;
import titli.model.Titli;
import titli.model.TitliException;
import edu.wustl.common.util.TitliResultGroup;

/**
 * @author Juber Patel
 *
 */
public class TitliTest
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		int total=0;
		MatchListInterface matches=null;
		
		try
		{
			TitliInterface titli = Titli.getInstance();
			
			
			long start = new Date().getTime();
			titli.index();
			long end = new Date().getTime();
			
			System.out.println("Indexing took "+(end-start)/1000.0+" seconds");
			
					
			matches = titli.search("table:catissue_*");
			
			SortedResultMapInterface sl = matches.getSortedResultMap();
			
			total=0;
			
			for(ResultGroupInterface i : sl.values())
			{
				TitliResultGroup rg = new TitliResultGroup(i);
				
				//System.out.println("Name : "+rg.getlabel()+" Matches : "+rg.getNumberOfMatches()+"  "+rg.getIdList());
				
				//total=total+rg.getIdList().size();
			}
		}
		catch (TitliException e) 
		{
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		System.out.println("Total :  "+total);
		System.out.println("Found "+matches.getNumberOfMatches()+" matches");
		
	}

}
