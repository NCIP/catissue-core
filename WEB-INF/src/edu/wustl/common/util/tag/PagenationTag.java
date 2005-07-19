
package edu.wustl.common.util.tag;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Divyabhanu Singh
 *@version 1.0
 */

public class PagenationTag extends TagSupport
{

    protected String name = "Bhanu";
    protected int pageNum = 1;
    protected String prevPage = null;
    protected int totalResults = 1000;
    protected int numResultsPerPage = 5;
    protected int m_pageLinkStart = 1;
    protected int m_pageLinkEnd = 10;
    protected boolean m_showNext = true;
    protected String searchTerm = null;
    protected String searchTermValues = null;
    protected String [] selectedOrgs = null;
    private int numLinks = 10;
    private int resultLowRange = 1;
    private int resultHighRange = 1;
    private String pageName = null;
    

    public int doStartTag()
    {
        try
        {
            JspWriter out = pageContext.getOut();
            out.println("<table class=\"dataTableWhiteLabel\" border=0 bordercolor=#666699 width=100%>");

            if (pageNum > numLinks)
            {
                if (pageNum % numLinks != 0)
                    m_pageLinkStart = ((pageNum / numLinks) * numLinks + 1);
                else
                    m_pageLinkStart = (pageNum - numLinks) + 1;
            }
            else
                //For first time or for PageNum < 10.
                m_pageLinkStart = 1;

            //Set the values of the ending Links on the Page.
            //This checks if number of Results left in the arrayList is less than numResults i.e. showNext==zero
//            System.out.println("totalResults = " + totalResults
//                    + "  numResultsPerPage = " + numResultsPerPage
//                    + " m_pageLinkStart = " + m_pageLinkStart);
//           System.out.println(" totalResults "+totalResults+" numResultsPerPage =  "+numResultsPerPage+"  ");
            if ((totalResults - ((m_pageLinkStart - 1) * numResultsPerPage)) >= numResultsPerPage
                    * numLinks)
            {
                m_pageLinkEnd = m_pageLinkStart + (numLinks - 1);

            }
            else
            {
                if ((totalResults - (m_pageLinkStart * numResultsPerPage)) > 0)
                {
                    if (totalResults % numResultsPerPage == 0)
                    {
                        m_pageLinkEnd = (m_pageLinkStart + (totalResults - (m_pageLinkStart * numResultsPerPage))
                                / numResultsPerPage);
                    }
                    else
                    {
                        m_pageLinkEnd = (m_pageLinkStart + (totalResults - (m_pageLinkStart * numResultsPerPage))
                                / numResultsPerPage) + 1;
                    }
                }
                else
                {
                    m_pageLinkEnd = (m_pageLinkStart + (totalResults - (m_pageLinkStart * numResultsPerPage))
                            / numResultsPerPage);

                }
            }
//            System.out.println("totalResults = " + totalResults
//                    + "  m_pageLinkStart" + m_pageLinkStart
//                    + " numResultsPerPage = " + numResultsPerPage
//                    + " numLinks = " + numLinks+" m_pageLinkEnd = "+m_pageLinkEnd);
            // If we have exhausted our resultset, then set m_showNext as false. which means NEXT link must not be shown
            if ((totalResults - ((m_pageLinkStart - 1) * numResultsPerPage)) <= (numResultsPerPage * numLinks))
            {
                m_showNext = false;
            }

            resultLowRange = (pageNum - 1) * numResultsPerPage + 1;
            if (totalResults - ((pageNum - 1) * numResultsPerPage) < numResultsPerPage)
            {
                resultHighRange = resultLowRange + totalResults
                        - ((pageNum - 1) * numResultsPerPage) - 1;
            }
            else
            {
                resultHighRange = resultLowRange + numResultsPerPage - 1;
            }
//            System.out.println("resultLowRange = "+resultLowRange+" resultHighRange "+resultHighRange+" pageNum = "+pageNum);
            out.println("<tr> <td class = \"formtextbg\" align=\"CENTER\">"+name+"</td>");
            out.println("<td  align = \"CENTER\" class = \"formtextbg\">Showing Results "
                            + resultLowRange
                            + " - "
                            + resultHighRange
                            + " of "
                            + totalResults + "</td>");
            
            if ((m_pageLinkEnd) > numLinks)
            {
                out.print("<td align=\"CENTER\"><a href=\"javascript:send("+(m_pageLinkStart -1)+","+totalResults+",'"+prevPage+"','"+pageName+"')"
                                    + "\"> &gt;&gt;PREV  </a></td>");
            }
            else
            {

                out.print("<td align=\"CENTER\">&nbsp;</td>");
            }

            int i = m_pageLinkStart;
            for (i = m_pageLinkStart; i <= m_pageLinkEnd; i++)
            {
                if (i != pageNum)
                {
                    out.print("<td align=\"CENTER\"> <a href=\"javascript:send("+i+","+totalResults+",'"+prevPage+"','"+pageName+"')"
                                    + "\">"
                                    + i + " </a></td>");
                }
                else
                {
                    out.print("<td align=\"CENTER\">" + i + " </td>");
                }
            }
            if (m_showNext == true)
            {
                out.print("<td align=\"CENTER\"><a href=\"javascript:send("+i+","+totalResults+",'"+prevPage+"','"+pageName+"')"
                                +"\"> NEXT>>  </a> </td>");
            }
            else
            {
                out.print("<td align=\"CENTER\">&nbsp;</td>");
            }
            out.print("</tr></table>");

        }
        catch (IOException ioe)
        {
            System.out.println("Error generating prime: " + ioe);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (SKIP_BODY);
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @param pageNum The pageNum to set.
     */
    public void setPageNum(int pageNum)
    {
        try
        {
            this.pageNum = pageNum;
        }
        catch (NumberFormatException nfe)
        {
            this.pageNum = 1;
            nfe.printStackTrace();
        }

    }

    /**
     * @param totalResults The totalResults to set.
     */
    public void setTotalResults(int totalResults)
    {
        try
        {
            this.totalResults = totalResults;
        }
        catch (NumberFormatException nfe)
        {
            this.totalResults = 1000;
        }
    }

    /**
     * @param numResultsPerPage The numResultsPerPage to set.
     */
    public void setNumResultsPerPage(int numResultsPerPage)
    {
        try
        {
            this.numResultsPerPage = numResultsPerPage;
        }
        catch (NumberFormatException nfe)
        {
            this.numResultsPerPage = 10;
        }
    }

    /**
     * @param searchTerm The searchTerm to set.
     */
    public void setSearchTerm(String searchTerm)
    {
        this.searchTerm = searchTerm;
    }
    /**
     * @param searchTermvalues The searchTermvalues to set.
     */
    public void setSearchTermValues(String searchTermvalues)
    {
        this.searchTermValues = searchTermvalues;
    }
    /**
     * @param selectedOrgs The selectedOrgs to set.
     */
    public void setSelectedOrgs(String[] selectedOrgs)
    {
        this.selectedOrgs = selectedOrgs;
    }
    /**
     * @return Returns the prevPage.
     */
    public String getPrevPage()
    {
        return prevPage;
    }
    /**
     * @param prevPage The prevPage to set.
     */
    public void setPrevPage(String prevPage)
    {
        this.prevPage = prevPage;
    }
    /**
     * @return Returns the pageName.
     */
    public String getPageName()
    {
        return pageName;
    }
    /**
     * @param pageName The pageName to set.
     */
    public void setPageName(String pageName)
    {
        this.pageName = pageName;
    }
}