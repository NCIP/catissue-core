
package edu.wustl.catissuecore.applet.ui;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.wustl.catissuecore.applet.AppletServerCommunicator;
import edu.wustl.catissuecore.applet.component.BaseTable;
import edu.wustl.catissuecore.applet.listener.BaseActionHandler;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.model.RowHeaderColumnModel;
import edu.wustl.catissuecore.applet.model.SpecimenColumnModel;

/**
 * This applet displays main UI for multiple specimen page.
 * @author mandar_deshmukh
 */
public class MultipleSpecimenApplet extends BaseApplet
{

	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;

	public void doInit()
	{
		int columnNumber = Integer.parseInt(this.getParameter("noOfSpecimen"));
		MultipleSpecimenTableModel model = new MultipleSpecimenTableModel(columnNumber,getInitDataMap());

		BaseTable table = new BaseTable(model)
		{

			public Class getColumnClass(int column)
			{
				return getValueAt(0, column).getClass();
			}
		};

		//table.getColumnModel().setColumnSelectionAllowed(true);
		table.setColumnSelectionAllowed(true);
		//table.setRowHeight(3,50);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane);
		JButton showAll = new JButton("ShowData");

		showAll.addActionListener(new BaseActionHandler(table));

		getContentPane().add(showAll, BorderLayout.SOUTH);

		//  Create custom columns
		new RowHeaderColumnModel(table); //	for zeroth column
		for (int cnt = 1; cnt < columnNumber; cnt++)
		{
			new SpecimenColumnModel(table, cnt);
		}
		//		SpecimenColumnModel SpecimenFieldColumn1= new SpecimenColumnModel(table, 1);
		//		SpecimenColumnModel SpecimenFieldColumn2 = new SpecimenColumnModel(table, 2);
		//		SpecimenColumnModel SpecimenFieldColumn3 = new SpecimenColumnModel(table, 3);

	}

	public static void main(String[] args)
	{
		MultipleSpecimenApplet frame = new MultipleSpecimenApplet();
	}

	/**
	 * This method initialize data lists 
	 */
	private Map getInitDataMap()
	{
		BaseAppletModel appletModel = new BaseAppletModel();
		appletModel.setData(new HashMap());
		try
		{
			String url = getServerURL() + "/MultipleSpecimenAppletAction.do?method=initData";

			appletModel = (BaseAppletModel) AppletServerCommunicator.doAppletServerCommunication(
					url, appletModel);

			return appletModel.getData();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
