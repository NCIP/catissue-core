////////////////////////////////////////////////////////////////////////////////
//
// @file: MessageBoxHelper
// @author: Jignesh Wala
// @date: 11-08-2008
// @description: Extending the AlertManager to create more customized alerts.
//
////////////////////////////////////////////////////////////////////////////////
package Components
{
	import flash.display.Sprite;
	import flash.events.Event;
	
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	
		
	public class AlertManager
	{
		static private var alertStack:Array;
		static private var callbackStack:Object;
		static private var defaultOptionStack:Object;
		
		static private var alertPanel:Alert;
		
		static public function showInfo(text:String="", closeHandler:Function=null, flag:uint=0x4, defaultButtonFlag:uint=0x4):Alert
		{
			return showAlert(text, MessageBoxHelper.INFO_TITLE, flag, null, closeHandler, MessageBoxHelper.INFO_ICON, defaultButtonFlag);
		}
		
		static public function showWarningWithYesNo(text:String="", closeHandler:Function=null):Alert
		{
			return showAlert(text, MessageBoxHelper.WARNING_TITLE, Alert.YES|Alert.NO, null, closeHandler, MessageBoxHelper.WARNING_ICON, Alert.NO);
		}
		
		static public function showWarning(text:String="", closeHandler:Function=null, flag:uint=0x4, defaultButtonFlag:uint=0x4):Alert
		{
			return showAlert(text, MessageBoxHelper.WARNING_TITLE, flag, null, closeHandler, MessageBoxHelper.WARNING_ICON, defaultButtonFlag);
		}
		
		static public function showError(text:String="", closeHandler:Function=null, flag:uint=0x4, defaultButtonFlag:uint=0x4):Alert
		{
			return showAlert(text, MessageBoxHelper.ERROR_TITLE, flag, null, closeHandler, MessageBoxHelper.ERROR_ICON, defaultButtonFlag);
		}

		static public function showAlert(text:String="", title:String="", flag:uint=0x4, 
										 parent:Sprite=null, closeHandler:Function=null, 
										 iconClass:Class=null, defaultButtonFlag:uint=0x4):Alert
		{
			
			alertPanel = Alert.show(text, title, flag, parent, closeHandler, iconClass, defaultButtonFlag);
						
			if (alertPanel.stage)
			{
				alertPanel.stage.addEventListener(Event.RESIZE, onStageResize);
			}
			
			// in case of multiple alert showing at the same time, we can close all at once
			if (!alertStack)
			{
				alertStack = new Array();
			}
			if (!callbackStack)
			{
				callbackStack = new Object();
			}
			if (!defaultOptionStack)
			{
				defaultOptionStack = new Object();
			}
			alertStack.push(alertPanel);
			callbackStack[alertPanel] = closeHandler;
			defaultOptionStack[alertPanel] = defaultButtonFlag;
			
			return alertPanel;
		}
		
		static private function closeAlert(e:Event):void
		{
			// close the alert one by one
			for each (var openAlert:Alert in alertStack)
			{
				var closingCallback:Function = callbackStack[openAlert];
				var defaultOption:uint = defaultOptionStack[openAlert];
				if (closingCallback != null)
				{
					var ce:CloseEvent = new CloseEvent(CloseEvent.CLOSE);
					if (defaultOption)
					{
						ce.detail = defaultOption;
					}
					else
					{
						ce.detail = Alert.CANCEL;
					}
					closingCallback(ce);
				}
				if (openAlert && openAlert.isPopUp)
				{
					//alertPanel.systemManager.removeEventListener(BrowserActionEvent.BROWSER_ACTION_EVENT, closeAlert);
					alertPanel.stage.removeEventListener(Event.RESIZE, onStageResize);
					PopUpManager.removePopUp(openAlert);
				}
			}
			alertStack = null;
			callbackStack = null;
			defaultOptionStack = null;
		}
		
		static private function onStageResize(e:Event):void
		{
			if (alertPanel)
			{
				PopUpManager.centerPopUp(alertPanel);
				if (alertPanel.parentApplication.width < alertPanel.width)
					alertPanel.x = 0;
				if (alertPanel.parentApplication.height < alertPanel.height)
					alertPanel.y = 0;
			}
		}
		
		public function AlertManager()
		{
			throw new Error("AlertManager is a static class and should not be initialized.");
		}
	}
}