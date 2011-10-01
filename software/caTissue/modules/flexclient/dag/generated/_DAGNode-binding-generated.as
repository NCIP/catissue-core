
import flash.events.Event;
import flash.events.EventDispatcher;
import flash.events.IEventDispatcher;
import mx.core.IPropertyChangeNotifier;
import mx.events.PropertyChangeEvent;
import mx.utils.ObjectProxy;
import mx.utils.UIDUtil;

import String;
import mx.containers.HBox;
import Array;
import mx.containers.VBox;
import Boolean;
import Number;
import mx.core.Repeater;
import int;
import mx.controls.Label;

class BindableProperty
{
	/**
	 * generated bindable wrapper for property _HBox1 (public)
	 * - generated setter
	 * - generated getter
	 * - original public var '_HBox1' moved to '_1506627793_HBox1'
	 */

    [Bindable(event="propertyChange")]
    public function get _HBox1():mx.containers.HBox
    {
        return this._1506627793_HBox1;
    }

    public function set _HBox1(value:mx.containers.HBox):void
    {
    	var oldValue:Object = this._1506627793_HBox1;
        if (oldValue !== value)
        {
			this._1506627793_HBox1 = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "_HBox1", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property _Label1 (public)
	 * - generated setter
	 * - generated getter
	 * - original public var '_Label1' moved to '_681920764_Label1'
	 */

    [Bindable(event="propertyChange")]
    public function get _Label1():mx.controls.Label
    {
        return this._681920764_Label1;
    }

    public function set _Label1(value:mx.controls.Label):void
    {
    	var oldValue:Object = this._681920764_Label1;
        if (oldValue !== value)
        {
			this._681920764_Label1 = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "_Label1", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property _Label2 (public)
	 * - generated setter
	 * - generated getter
	 * - original public var '_Label2' moved to '_681920765_Label2'
	 */

    [Bindable(event="propertyChange")]
    public function get _Label2():mx.controls.Label
    {
        return this._681920765_Label2;
    }

    public function set _Label2(value:mx.controls.Label):void
    {
    	var oldValue:Object = this._681920765_Label2;
        if (oldValue !== value)
        {
			this._681920765_Label2 = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "_Label2", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property _Label3 (public)
	 * - generated setter
	 * - generated getter
	 * - original public var '_Label3' moved to '_681920766_Label3'
	 */

    [Bindable(event="propertyChange")]
    public function get _Label3():Array
    {
        return this._681920766_Label3;
    }

    public function set _Label3(value:Array):void
    {
    	var oldValue:Object = this._681920766_Label3;
        if (oldValue !== value)
        {
			this._681920766_Label3 = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "_Label3", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property _VBox1 (public)
	 * - generated setter
	 * - generated getter
	 * - original public var '_VBox1' moved to '_1493698499_VBox1'
	 */

    [Bindable(event="propertyChange")]
    public function get _VBox1():mx.containers.VBox
    {
        return this._1493698499_VBox1;
    }

    public function set _VBox1(value:mx.containers.VBox):void
    {
    	var oldValue:Object = this._1493698499_VBox1;
        if (oldValue !== value)
        {
			this._1493698499_VBox1 = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "_VBox1", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property assRep (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'assRep' moved to '_1408226724assRep'
	 */

    [Bindable(event="propertyChange")]
    public function get assRep():mx.core.Repeater
    {
        return this._1408226724assRep;
    }

    public function set assRep(value:mx.core.Repeater):void
    {
    	var oldValue:Object = this._1408226724assRep;
        if (oldValue !== value)
        {
			this._1408226724assRep = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "assRep", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property assVbox (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'assVbox' moved to '_705239114assVbox'
	 */

    [Bindable(event="propertyChange")]
    public function get assVbox():mx.containers.VBox
    {
        return this._705239114assVbox;
    }

    public function set assVbox(value:mx.containers.VBox):void
    {
    	var oldValue:Object = this._705239114assVbox;
        if (oldValue !== value)
        {
			this._705239114assVbox = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "assVbox", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property operatorCombo (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'operatorCombo' moved to '_1169022794operatorCombo'
	 */

    [Bindable(event="propertyChange")]
    public function get operatorCombo():Array
    {
        return this._1169022794operatorCombo;
    }

    public function set operatorCombo(value:Array):void
    {
    	var oldValue:Object = this._1169022794operatorCombo;
        if (oldValue !== value)
        {
			this._1169022794operatorCombo = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "operatorCombo", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property accSelectedIndx (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'accSelectedIndx' moved to '_1120344277accSelectedIndx'
	 */

    [Bindable(event="propertyChange")]
    public function get accSelectedIndx():Number
    {
        return this._1120344277accSelectedIndx;
    }

    public function set accSelectedIndx(value:Number):void
    {
    	var oldValue:Object = this._1120344277accSelectedIndx;
        if (oldValue !== value)
        {
			this._1120344277accSelectedIndx = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "accSelectedIndx", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property nodeName (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'nodeName' moved to '_1122880429nodeName'
	 */

    [Bindable(event="propertyChange")]
    public function get nodeName():String
    {
        return this._1122880429nodeName;
    }

    public function set nodeName(value:String):void
    {
    	var oldValue:Object = this._1122880429nodeName;
        if (oldValue !== value)
        {
			this._1122880429nodeName = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "nodeName", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property expressionId (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'expressionId' moved to '_1147229651expressionId'
	 */

    [Bindable(event="propertyChange")]
    public function get expressionId():int
    {
        return this._1147229651expressionId;
    }

    public function set expressionId(value:int):void
    {
    	var oldValue:Object = this._1147229651expressionId;
        if (oldValue !== value)
        {
			this._1147229651expressionId = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "expressionId", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property nodeNumber (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'nodeNumber' moved to '_1069771755nodeNumber'
	 */

    [Bindable(event="propertyChange")]
    public function get nodeNumber():String
    {
        return this._1069771755nodeNumber;
    }

    public function set nodeNumber(value:String):void
    {
    	var oldValue:Object = this._1069771755nodeNumber;
        if (oldValue !== value)
        {
			this._1069771755nodeNumber = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "nodeNumber", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property operatorBetweenAttrAndAssociation (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'operatorBetweenAttrAndAssociation' moved to '_1819142239operatorBetweenAttrAndAssociation'
	 */

    [Bindable(event="propertyChange")]
    public function get operatorBetweenAttrAndAssociation():String
    {
        return this._1819142239operatorBetweenAttrAndAssociation;
    }

    public function set operatorBetweenAttrAndAssociation(value:String):void
    {
    	var oldValue:Object = this._1819142239operatorBetweenAttrAndAssociation;
        if (oldValue !== value)
        {
			this._1819142239operatorBetweenAttrAndAssociation = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "operatorBetweenAttrAndAssociation", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property nodeType (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'nodeType' moved to '_1123082332nodeType'
	 */

    [Bindable(event="propertyChange")]
    public function get nodeType():String
    {
        return this._1123082332nodeType;
    }

    public function set nodeType(value:String):void
    {
    	var oldValue:Object = this._1123082332nodeType;
        if (oldValue !== value)
        {
			this._1123082332nodeType = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "nodeType", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property operatorArray (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'operatorArray' moved to '_1167269909operatorArray'
	 */

    [Bindable(event="propertyChange")]
    public function get operatorArray():Array
    {
        return this._1167269909operatorArray;
    }

    public function set operatorArray(value:Array):void
    {
    	var oldValue:Object = this._1167269909operatorArray;
        if (oldValue !== value)
        {
			this._1167269909operatorArray = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "operatorArray", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property operatorSelectedIndex (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'operatorSelectedIndex' moved to '_1357111475operatorSelectedIndex'
	 */

    [Bindable(event="propertyChange")]
    public function get operatorSelectedIndex():int
    {
        return this._1357111475operatorSelectedIndex;
    }

    public function set operatorSelectedIndex(value:int):void
    {
    	var oldValue:Object = this._1357111475operatorSelectedIndex;
        if (oldValue !== value)
        {
			this._1357111475operatorSelectedIndex = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "operatorSelectedIndex", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property enable (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'enable' moved to '_1298848381enable'
	 */

    [Bindable(event="propertyChange")]
    public function get enable():Boolean
    {
        return this._1298848381enable;
    }

    public function set enable(value:Boolean):void
    {
    	var oldValue:Object = this._1298848381enable;
        if (oldValue !== value)
        {
			this._1298848381enable = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "enable", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property nodeColor (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'nodeColor' moved to '_439812737nodeColor'
	 */

    [Bindable(event="propertyChange")]
    public function get nodeColor():int
    {
        return this._439812737nodeColor;
    }

    public function set nodeColor(value:int):void
    {
    	var oldValue:Object = this._439812737nodeColor;
        if (oldValue !== value)
        {
			this._439812737nodeColor = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "nodeColor", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property menuData (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'menuData' moved to '_604261975menuData'
	 */

    [Bindable(event="propertyChange")]
    public function get menuData():Array
    {
        return this._604261975menuData;
    }

    public function set menuData(value:Array):void
    {
    	var oldValue:Object = this._604261975menuData;
        if (oldValue !== value)
        {
			this._604261975menuData = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "menuData", oldValue, value));
        }
    }

	/**
	 * generated bindable wrapper for property outputMenuData (public)
	 * - generated setter
	 * - generated getter
	 * - original public var 'outputMenuData' moved to '_1174576586outputMenuData'
	 */

    [Bindable(event="propertyChange")]
    public function get outputMenuData():Array
    {
        return this._1174576586outputMenuData;
    }

    public function set outputMenuData(value:Array):void
    {
    	var oldValue:Object = this._1174576586outputMenuData;
        if (oldValue !== value)
        {
			this._1174576586outputMenuData = value;
            dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "outputMenuData", oldValue, value));
        }
    }


}
