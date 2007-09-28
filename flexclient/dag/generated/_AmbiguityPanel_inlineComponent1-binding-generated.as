
import flash.events.Event;
import flash.events.EventDispatcher;
import flash.events.IEventDispatcher;
import mx.core.IPropertyChangeNotifier;
import mx.events.PropertyChangeEvent;
import mx.utils.ObjectProxy;
import mx.utils.UIDUtil;

import Components.AmbiguityPanel;

class BindableProperty
{
	/**
	 * generated bindable wrapper for property outerDocument (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'outerDocument' moved to '_88844982outerDocument'
	 */

    [Bindable(event="propertyChange")]
    public function get outerDocument():Components.AmbiguityPanel
    {
        return this._88844982outerDocument;
    }

    public function set outerDocument(value:Components.AmbiguityPanel):void
    {
    	var oldValue:Object = this._88844982outerDocument;
        if (oldValue !== value)
        {
			this._88844982outerDocument = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "outerDocument", oldValue, value));
        }
    }


}
