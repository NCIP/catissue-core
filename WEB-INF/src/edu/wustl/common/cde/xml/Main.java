package edu.wustl.common.cde.xml;

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/*
 * $Id: Main.java,v 1.1 2005/08/24 10:46:01 Kapil Exp $
 *
 * Copyright 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */
 
public class Main
{
    
    // This sample application demonstrates how to unmarshal an instance
    // document into a Java content tree and access data contained within it.
    
    public static void main( String[] args )
	{
        try
		{
            // create a JAXBContext capable of handling classes generated into
            // the pspl.cde package
            JAXBContext jc = JAXBContext.newInstance( "edu.wustl.common.cde.xml" );
            
            // create an Unmarshaller
            Unmarshaller u = jc.createUnmarshaller();
            
            // unmarshal a root instance document into a tree of Java content
            // objects composed of classes from the pspl.cde package.
            XMLCDECACHE root = 
                (XMLCDECACHE)u.unmarshal( new FileInputStream( "CDEConfig.xml" ) );
                
            // display the cde details
			java.util.List l1 = root.getXMLCDE();
			if (!l1.isEmpty())
			{
				for (int z =0; z<l1.size(); z++ )
				{
					XMLCDE cdeobj = (XMLCDE)l1.get(z);
					displayCde( cdeobj );

				}
			}

        }
		catch( JAXBException je )
		{
            je.printStackTrace();
        }
		catch( IOException ioe )
		{
            ioe.printStackTrace();
        }
  
  } // main
	
	
	public static void displayCde( XMLCDE cdeojb )
	{
        // display the cdeojb

        System.out.println( "NAME : \t" + cdeojb.getName() );
        System.out.println( "PUBLICID : \t" + cdeojb.getPublicId() ); 
	    System.out.println( "CACHE : \t" + cdeojb.isCache() );
		System.out.println( "LAZYLOADING : \t" + cdeojb.isLazyLoading() );

		java.util.List l2 = cdeojb.getXMLPermissibleValues();
			if (!l2.isEmpty())
			{
	            XMLPermissibleValueType pvt = (XMLPermissibleValueType)l2.get(0);
		        displayPV(pvt);
			}
		System.out.println( "\n\t----------------------------------------");
    } //displayCde
	
	public static void displayPV(XMLPermissibleValueType pvt)
	{
		System.out.println( "\tEVS Term: " + pvt.getEvsTerminology());
		System.out.println( "\tConcept Code: " + pvt.getConceptCode());
		System.out.println( "\tParentConcept Code: " + pvt.getParentConceptCode());
		System.out.println( "\tTree Depth: " + pvt.getDepthOfHierarchyTree());

	} // displayPV


    

} // class


// --------------------------------------------------------
/*    
    public static void displayAddress( CDE cdeojb ) {
        // display the cdeojb
        System.out.println( "\t" + cdeojb.getName() );
        System.out.println( "\t" + cdeojb.getPublicId() ); 
    }
    
    public static void displayItems( PermissibleValueType pvt ) {
        // the items object contains a List of pspl.cde.ItemType objects
        List itemTypeList = items.getItem();

                
        // iterate over List
        for( Iterator iter = itemTypeList.iterator(); iter.hasNext(); ) {
            Items.ItemType item = (Items.ItemType)iter.next(); 
            System.out.println( "\t" + item.getQuantity() +
                                " copies of \"" + item.getProductName() +
                                "\"" ); 
        }
    }
*/







