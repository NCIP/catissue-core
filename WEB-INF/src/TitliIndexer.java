import titli.controller.interfaces.TitliInterface;
import titli.model.Titli;

/**
 * 
 */

/**
 * @author chetan_patil
 *
 */
public class TitliIndexer 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		TitliInterface titli = Titli.getInstance();
		titli.index();
	}

}
