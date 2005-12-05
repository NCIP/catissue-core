package edu.wustl.catissuecore.exceptionformatter;
/**
 * 
 * @author sachin_lale
 * Description: Interface defines method for formatting the database specific Exception message  
 */
public interface ExceptionFormatter {
	public String formatMessage(Exception objExcp,Object args[]);
}
