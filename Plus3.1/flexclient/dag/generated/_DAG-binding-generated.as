
import flash.events.Event;
import flash.events.EventDispatcher;
import flash.events.IEventDispatcher;
import mx.core.IPropertyChangeNotifier;
import mx.events.PropertyChangeEvent;
import mx.utils.ObjectProxy;
import mx.utils.UIDUtil;

import mx.containers.Panel;

class BindableProperty
{
	/**
	 * generated bindable wrapper for property panel1 (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'panel1' moved to '_995543379panel1'
	 */

    [Bindable(event="propertyChange")]
    public function get panel1():mx.containers.Panel
    {
        return this._995543379panel1;
    }

    public function set panel1(value:mx.containers.Panel):void
    {
    	var oldValue:Object = this._995543379panel1;
        if (oldValue !== value)
        {
			this._995543379panel1 = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "panel1", oldValue, value));
        }
    }


}
