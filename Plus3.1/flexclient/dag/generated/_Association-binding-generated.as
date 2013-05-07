
import flash.events.Event;
import flash.events.EventDispatcher;
import flash.events.IEventDispatcher;
import mx.core.IPropertyChangeNotifier;
import mx.events.PropertyChangeEvent;
import mx.utils.ObjectProxy;
import mx.utils.UIDUtil;

import String;
import int;

class BindableProperty
    implements flash.events.IEventDispatcher
{
	/**
	 * generated bindable wrapper for property associatedNode (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'associatedNode' moved to '_217646120associatedNode'
	 */

    [Bindable(event="propertyChange")]
    public function get associatedNode():String
    {
        return this._217646120associatedNode;
    }

    public function set associatedNode(value:String):void
    {
    	var oldValue:Object = this._217646120associatedNode;
        if (oldValue !== value)
        {
			this._217646120associatedNode = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "associatedNode", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property associatedLink (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'associatedLink' moved to '_217581088associatedLink'
	 */

    [Bindable(event="propertyChange")]
    public function get associatedLink():String
    {
        return this._217581088associatedLink;
    }

    public function set associatedLink(value:String):void
    {
    	var oldValue:Object = this._217581088associatedLink;
        if (oldValue !== value)
        {
			this._217581088associatedLink = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "associatedLink", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property operatorIndex (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'operatorIndex' moved to '_1174525582operatorIndex'
	 */

    [Bindable(event="propertyChange")]
    public function get operatorIndex():int
    {
        return this._1174525582operatorIndex;
    }

    public function set operatorIndex(value:int):void
    {
    	var oldValue:Object = this._1174525582operatorIndex;
        if (oldValue !== value)
        {
			this._1174525582operatorIndex = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "operatorIndex", oldValue, value));
        }
    }


    //    IEventDispatcher implementation
    //
    private var _bindingEventDispatcher:flash.events.EventDispatcher =
        new flash.events.EventDispatcher(flash.events.IEventDispatcher(this));

    public function addEventListener(type:String, listener:Function,
                                     useCapture:Boolean = false,
                                     priority:int = 0,
                                     weakRef:Boolean = false):void
    {
        _bindingEventDispatcher.addEventListener(type, listener, useCapture,
                                                 priority, weakRef);
    }

    public function dispatchEvent(event:flash.events.Event):Boolean
    {
        return _bindingEventDispatcher.dispatchEvent(event);
    }

    public function hasEventListener(type:String):Boolean
    {
        return _bindingEventDispatcher.hasEventListener(type);
    }

    public function removeEventListener(type:String,
                                        listener:Function,
                                        useCapture:Boolean = false):void
    {
        _bindingEventDispatcher.removeEventListener(type, listener, useCapture);
    }

    public function willTrigger(type:String):Boolean
    {
        return _bindingEventDispatcher.willTrigger(type);
    }
}
