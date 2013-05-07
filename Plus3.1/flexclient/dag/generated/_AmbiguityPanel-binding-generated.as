
import flash.events.Event;
import flash.events.EventDispatcher;
import flash.events.IEventDispatcher;
import mx.core.IPropertyChangeNotifier;
import mx.events.PropertyChangeEvent;
import mx.utils.ObjectProxy;
import mx.utils.UIDUtil;

import mx.collections.ArrayCollection;
import mx.controls.DataGrid;

class BindableProperty
{
	/**
	 * generated bindable wrapper for property _DataGrid1 (public)
	 * - generated setter
	 * - generated getter
	 * - original public var '_DataGrid1' moved to '_129454722_DataGrid1'
	 */

    [Bindable(event="propertyChange")]
    public function get _DataGrid1():mx.controls.DataGrid
    {
        return this._129454722_DataGrid1;
    }

    public function set _DataGrid1(value:mx.controls.DataGrid):void
    {
    	var oldValue:Object = this._129454722_DataGrid1;
        if (oldValue !== value)
        {
			this._129454722_DataGrid1 = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "_DataGrid1", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property pathList (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'pathList' moved to '_1234169443pathList'
	 */

    [Bindable(event="propertyChange")]
    public function get pathList():ArrayCollection
    {
        return this._1234169443pathList;
    }

    public function set pathList(value:ArrayCollection):void
    {
    	var oldValue:Object = this._1234169443pathList;
        if (oldValue !== value)
        {
			this._1234169443pathList = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "pathList", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property selectedList (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'selectedList' moved to '_1754705447selectedList'
	 */

    [Bindable(event="propertyChange")]
    public function get selectedList():ArrayCollection
    {
        return this._1754705447selectedList;
    }

    public function set selectedList(value:ArrayCollection):void
    {
    	var oldValue:Object = this._1754705447selectedList;
        if (oldValue !== value)
        {
			this._1754705447selectedList = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "selectedList", oldValue, value));
        }
    }


}
