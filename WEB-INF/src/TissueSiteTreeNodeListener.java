import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.event.MouseInputListener;
import javax.swing.tree.DefaultMutableTreeNode;

import netscape.javascript.JSObject;
import edu.wustl.catissuecore.tissuesite.TissueSiteTreeNode;
import edu.wustl.catissuecore.util.global.Constants;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TissueSiteTreeNodeListener implements MouseInputListener
{
    /**
     * Corresponds to an applet environment.
     */
    private AppletContext appletContext = null;
    
    /**
     * @return Returns the appletContext.
     */
    public AppletContext getAppletContext()
    {
        return appletContext;
    }
    
    /**
     * @param appletContext The appletContext to set.
     */
    public void setAppletContext(AppletContext appletContext)
    {
        this.appletContext = appletContext;
    }
    
    public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
    	if(e.getClickCount()==2)
        {
    		Object object = e.getSource();
    		JTree tree = null;

    		if (object instanceof JTree)
    		{
    			tree = (JTree) object;
    			
    			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                     .getLastSelectedPathComponent();

    			TissueSiteTreeNode  treeNode = (TissueSiteTreeNode) node
                     .getUserObject();
             
    			//Set the values in the parent window.
    			Applet applet = this.appletContext.getApplet(Constants.TREE_APPLET_NAME);
    			JSObject window = JSObject.getWindow(applet);
    			//commented as this will set in MouseClick
    			String propertyName = applet.getParameter(Constants.PROPERTY_NAME);

    			// if 'Tissue Site' Root node selected then do nothing
    			if(treeNode.toString().equals(Constants.TISSUE_SITE))
    			{
    				return;
    			}
    			String setValue = "setParentWindowValue('"+propertyName+"','"+treeNode.toString()+"')";
             
    			//commented as this will set in MouseClick
    			window.eval(setValue);
    			window.eval("closeWindow()");
    		}
        }
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
