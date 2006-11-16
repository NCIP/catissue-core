/*
 * This class handles the events of DeleteLast Column Button.
 * Created on Nov 13, 2006
 * @author mandar_deshmukh
 *
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import edu.wustl.catissuecore.applet.AppletServerCommunicator;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.ui.MultipleSpecimenApplet;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;
import edu.wustl.catissuecore.util.global.Constants;


/*
 * This class handles the events of DeleteLast Column Button.
 * Created on Nov 13, 2006
 * @author mandar_deshmukh
 *
 */
public class DeleteLastHandler extends ButtonHandler
{

	private MultipleSpecimenApplet applet;
	/**
	 * @param table
	 * @param radioButton
	 */
	public DeleteLastHandler(JTable table, JRadioButton radioButton)
	{
		super(table, radioButton);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param table
	 */
	public DeleteLastHandler(JTable table)
	{
		super(table);
		// TODO Auto-generated constructor stub
	}
	public DeleteLastHandler(JTable table, MultipleSpecimenApplet applet) {
		super(table);
		this.applet = applet;
	}
	
	protected void handleAction(ActionEvent event) {
		System.out.println("Inside DeleteLastHandler");
		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel)table.getModel();
		
		if(model.getTotalColumnCount()  == 1)
		{
			System.out.println("\nMinimum one column should remain in the table. Cannot delete the last column.");
		}
		else
		{
			// ------------------------------------
			int pageIndex = model.getCurrentPageIndex();
			int totalPages = model.getTotalPageCount();
			int initialColumnCount = model.getTotalColumnCount();
			System.out.println(" pageIndex: "+pageIndex + "\ttotalPages : "+totalPages);
			
			if(pageIndex == totalPages)
			{
				// delete from model.
				deleteFromModel(model);
				// a] reduce the column count by 1.
				// b] remove the specimen entries from the map for the specimen.
				
				//check for pageindex  and totalPages and delete the last column from UI if it is the current page.
				deleteFromUI(model);
				
				//delete entries of associated objects from the server side for the specimen.
				deleteAssociatedObjects(initialColumnCount);
			}
			else
			{
				// delete from model.
				deleteFromModel(model);
				// a] reduce the column count by 1.
				// b] remove the specimen entries from the map for the specimen.

				//update page links
				applet.updateOnDeleteLastSpecimen();
				
				//delete entries of associated objects from the server side for the specimen.
				deleteAssociatedObjects(initialColumnCount);
			}
			// ------------------------------------
		}
	}
	
	/*
	 * This method deletes the last column from Applet UI.
	 */
	private void deleteFromUI(MultipleSpecimenTableModel model)
	{
		System.out.println("\n>>>>>>>>>>>>>>. deleteFromUI() Modified    >>>>>>>>>>>>>>>>>>>>>\n");
		// check for pageindex  and totalPages
		
		int pageIndex = model.getCurrentPageIndex();
		int totalPages = model.getTotalPageCount();
		System.out.println(" pageIndex: "+pageIndex + "\ttotalPages : "+totalPages);
		//Jitendra: Delete the last column from the page. 
		//If no column left in the page, then display the columns from previous pagelink.
//		if(pageIndex == totalPages)
//		{
			int columnCount = table.getColumnCount();
			TableColumn col = table.getColumnModel().getColumn(columnCount-1); 
			System.out.println("Table Column Count : "+columnCount);
			table.removeColumn(col);
			table.getColumnModel().removeColumn(col );
			//page links handled
			applet.updateOnDeleteLastSpecimen();
			
			System.out.println("Column removed from UI : "+ columnCount);


		//jitendra
//		}else
//		{
//			System.out.println("Column is on different page." );
//			//page links handled
//			//applet.updateOnDeleteLastSpecimen();
//		}
	}

	/*
	 * This method deletes the last column from the model. 
	 */
	private void deleteFromModel(MultipleSpecimenTableModel model)
	{
		System.out.println(" \n >>>>>>>>>>>>>   In deleteFromModel()  <<<<<<<<<<<<<<<<<<<\n");
		model.showMapData();
		int beforeDeletePageCount = model.getTotalPageCount(); 
		int columnCount = model.getTotalColumnCount();
		model.removeLastColumn();
		System.out.println("\n\n  .............   After remove last column  ...............  \n\n");
		int afterDeletePageCount = model.getTotalPageCount();
		int currentPage = model.getCurrentPageIndex(); 
		System.out.println("beforeDeletePageCount : "+ beforeDeletePageCount + " | afterDeletePageCount: " +afterDeletePageCount);
		model.showMapData(); 
	}
	
	private void deleteAssociatedObjects(int column)
	{
		System.out.println(" \n >>>>>>>>......... deleteAssociatedObjects   ............<<<<<<<<<<< \n");

		BaseAppletModel appletModel = new BaseAppletModel();
		HashMap map =new HashMap();
		System.out.println("Specimen to be deleted : "+ column);
		map.put(Constants.MULTIPLE_SPECIMEN_DELETELAST_SPECIMEN_ID, new Long(column));
		appletModel.setData(map);
		
		try
		{
			MultipleSpecimenApplet applet = (MultipleSpecimenApplet) CommonAppletUtil
					.getBaseApplet(table);
			String url = applet.getServerURL()
					+ "/MultipleSpecimenAppletAction.do?method=deleteAssociatedObjects";

			appletModel = (BaseAppletModel) AppletServerCommunicator.doAppletServerCommunication(
					url, appletModel);
			
			Map resultMap = appletModel.getData();
			String target = (String) resultMap.get(Constants.MULTIPLE_SPECIMEN_RESULT);
			
			if(target.equals(Constants.SUCCESS))
			{
				System.out.println("Associated Objects deleted successfully");	
			}
			else
			{
				System.out.println("Cannot delete associated objects");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Exception");
		}
	}

}
