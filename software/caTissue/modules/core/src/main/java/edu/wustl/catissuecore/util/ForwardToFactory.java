/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

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

import edu.wustl.common.factory.IForwordToFactory;

/**
 * ForwardToFactory is a factory that returns instance of ForwardToProcessor
 * @author Krunal Thakkar
 */
public final class ForwardToFactory implements IForwordToFactory
{
	
	public ForwardToProcessor getForwardToProcessor()
    {
        return new ForwardToProcessor();
    }
    public ForwardToPrintProcessor getForwardToPrintProcessor()
    {
        return new ForwardToPrintProcessor();
    }
}
