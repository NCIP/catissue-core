/**
 * <p>Title: ForwardToFactory Class>
 * <p>Description:	ForwardToFactory is a factory that returns instance of ForwardToProcessor</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Krunal Thakkar
 * @version 1.00
 * Created on May 08, 2006
 */

package edu.wustl.catissuecore.util;

/**
 * ForwardToFactory is a factory that returns instance of ForwardToProcessor
 * @author Krunal Thakkar
 */
public class ForwardToFactory 
{
    public static ForwardToProcessor getForwardToProcessor()
    {
        return new ForwardToProcessor();
    }
}
