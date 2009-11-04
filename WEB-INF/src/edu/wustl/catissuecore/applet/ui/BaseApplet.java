/*
 * <p>Title: BaseApplet.java</p>
 * <p>Description:	This class initializes the fields of BaseApplet.java</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Sep 18, 2006
 */

package edu.wustl.catissuecore.applet.ui;

import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * <p> Base Applet class for all applets.It will contain basic functionality.
 * common across to other child applets which will be derived from it.
 * </p>
 * @author Ashwin Gupta
 * @version 1.1
 * @see javax.swing.JApplet
 */

public class BaseApplet extends JApplet
{

	/**
	 * logger.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(BaseApplet.class);
	/**
	 * Default Serial Version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Specify the serverURL field.
	 */
	protected String serverURL;

	/**
	 * Default Constructor.
	 */
	public BaseApplet()
	{
		super();
	}

	/**
	 * @see java.applet.Applet#init()
	 */
	@Override
	public void init()
	{
		super.init();
		this.preInit();
		this.doInit();
		this.postInit();
	}

	/**
	 * Used to perform pre initialization operation.
	 */
	protected void preInit()
	{
		// set server URL
		this.serverURL = this.getParameter(Constants.APPLET_SERVER_URL_PARAM_NAME);
		this.setLookAndFeel();
	}

	/**
	 * Used to perform actual logical initialization operation & must be.
	 * overridden in child classes.
	 */
	protected void doInit()
	{
		//Empty doInit method.
	}

	/**
	 * Used to perform post initialization operation.
	 */
	protected void postInit()
	{
		//Empty postInit method.
	}

	/**
	 * Sets the default L&F of Applet to System L&F.
	 */
	private void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (final ClassNotFoundException e)
		{
			BaseApplet.LOGGER.error(e.getMessage(), e);
		}
		catch (final InstantiationException e)
		{
			BaseApplet.LOGGER.error(e.getMessage(), e);
		}
		catch (final IllegalAccessException e)
		{
			BaseApplet.LOGGER.error(e.getMessage(), e);
		}
		catch (final UnsupportedLookAndFeelException e)
		{
			BaseApplet.LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * @return Returns the serverURL.
	 */
	public String getServerURL()
	{
		return this.serverURL;
	}
}
