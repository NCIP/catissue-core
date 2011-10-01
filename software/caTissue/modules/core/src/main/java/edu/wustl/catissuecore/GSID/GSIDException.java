package edu.wustl.catissuecore.GSID;

import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;

public class GSIDException extends ApplicationException
{

	 /**
     * Serial Version Unique Identifier.
     */
    private static final long serialVersionUID = -8514900107659307676L;

    /**
     * The Only public constructor to restrict creating object without
     * initializing mandatory members.
     * @param errorKey The object which will represent the root cause of the error.
     * @param exception root exception, if any, which caused this error.
     * @param msgValues custom message, additional information.
     */
    public GSIDException(ErrorKey errorKey, Exception exception, String msgValues)
    {
            super(errorKey,exception,msgValues);
    }


    
    public GSIDException(ErrorKey errorKey, Exception exception, String msgValues,String customizedMsg)
    {
            super(errorKey, exception, msgValues,customizedMsg);
    }

}
