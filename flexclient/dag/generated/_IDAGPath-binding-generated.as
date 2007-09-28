
import flash.events.Event;
import flash.events.EventDispatcher;
import flash.events.IEventDispatcher;
import mx.core.IPropertyChangeNotifier;
import mx.events.PropertyChangeEvent;
import mx.utils.ObjectProxy;
import mx.utils.UIDUtil;

import String;
import Boolean;
import int;

class BindableProperty
    implements flash.events.IEventDispatcher
{
	/**
	 * generated bindable wrapper for property name (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'name' moved to '_3373707name'
	 */

    [Bindable(event="propertyChange")]
    public function get name():String
    {
        return this._3373707name;
    }

    public function set name(value:String):void
    {
    	var oldValue:Object = this._3373707name;
        if (oldValue !== value)
        {
			this._3373707name = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "name", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property id (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'id' moved to '_3355id'
	 */

    [Bindable(event="propertyChange")]
    public function get id():String
    {
        return this._3355id;
    }

    public function set id(value:String):void
    {
    	var oldValue:Object = this._3355id;
        if (oldValue !== value)
        {
			this._3355id = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "id", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property isSelected (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'isSelected' moved to '_398301669isSelected'
	 */

    [Bindable(event="propertyChange")]
    public function get isSelected():Boolean
    {
        return this._398301669isSelected;
    }

    public function set isSelected(value:Boolean):void
    {
    	var oldValue:Object = this._398301669isSelected;
        if (oldValue !== value)
        {
			this._398301669isSelected = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "isSelected", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property sourceExpId (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'sourceExpId' moved to '_108527427sourceExpId'
	 */

    [Bindable(event="propertyChange")]
    public function get sourceExpId():int
    {
        return this._108527427sourceExpId;
    }

    public function set sourceExpId(value:int):void
    {
    	var oldValue:Object = this._108527427sourceExpId;
        if (oldValue !== value)
        {
			this._108527427sourceExpId = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "sourceExpId", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property destinationExpId (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'destinationExpId' moved to '_1181525162destinationExpId'
	 */

    [Bindable(event="propertyChange")]
    public function get destinationExpId():int
    {
        return this._1181525162destinationExpId;
    }

    public function set destinationExpId(value:int):void
    {
    	var oldValue:Object = this._1181525162destinationExpId;
        if (oldValue !== value)
        {
			this._1181525162destinationExpId = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "destinationExpId", oldValue, value));
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
