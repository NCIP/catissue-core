






package
{
import flash.display.Sprite;
import mx.core.IFlexModuleFactory;
import mx.binding.ArrayElementWatcher;
import mx.binding.FunctionReturnWatcher;
import mx.binding.IWatcherSetupUtil;
import mx.binding.PropertyWatcher;
import mx.binding.RepeaterComponentWatcher;
import mx.binding.RepeaterItemWatcher;
import mx.binding.XMLWatcher;
import mx.binding.Watcher;

[ExcludeClass]
[Mixin]
public class _Components_DAGNodeWatcherSetupUtil extends Sprite
    implements mx.binding.IWatcherSetupUtil
{
    public function _Components_DAGNodeWatcherSetupUtil()
    {
        super();
    }

    public static function init(fbs:IFlexModuleFactory):void
    {
        import Components.DAGNode;
        (Components.DAGNode).watcherSetupUtil = new _Components_DAGNodeWatcherSetupUtil();
    }

    public function setup(target:Object,
                          propertyGetter:Function,
                          bindings:Array,
                          watchers:Array):void
    {
        import mx.core.DeferredInstanceFromFunction;
        import flash.events.EventDispatcher;
        import mx.containers.HBox;
        import mx.core.IDeferredInstance;
        import DAG;
        import mx.core.ClassFactory;
        import mx.core.mx_internal;
        import flash.utils.IDataOutput;
        import mx.controls.Menu;
        import mx.core.IPropertyChangeNotifier;
        import mx.core.Repeater;
        import mx.controls.HScrollBar;
        import mx.utils.ObjectProxy;
        import flash.utils.IDataInput;
        import mx.controls.Label;
        import flash.utils.IExternalizable;
        import mx.containers.Canvas;
        import mx.controls.Button;
        import mx.utils.UIDUtil;
        import mx.controls.Alert;
        import flash.events.MouseEvent;
        import mx.events.MenuEvent;
        import mx.containers.Box;
        import mx.events.ListEvent;
        import mx.rpc.events.ResultEvent;
        import mx.skins.halo.BrokenImageBorderSkin;
        import mx.events.FlexEvent;
        import mx.controls.ComboBox;
        import mx.core.UIComponentDescriptor;
        import mx.containers.VBox;
        import mx.collections.ArrayCollection;
        import mx.events.PropertyChangeEvent;
        import flash.events.Event;
        import mx.core.IFactory;
        import mx.core.DeferredInstanceFromClass;
        import mx.binding.BindingManager;
        import mx.rpc.events.FaultEvent;
        import flash.events.IEventDispatcher;

        var tempWatcher:mx.binding.Watcher;

        // writeWatcher id=10 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher shouldWriteChildren=true
        watchers[10] = new mx.binding.PropertyWatcher("operatorArray",
            {
                propertyChange: true
            }
                                                                 );

        // writeWatcher id=5 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher shouldWriteChildren=true
        watchers[5] = new mx.binding.PropertyWatcher("assRep",
            {
                propertyChange: true
            }
                                                                 );

        // writeWatcher id=6 shouldWriteSelf=true class=flex2.compiler.as3.binding.RepeaterDataProviderWatcher shouldWriteChildren=true
        watchers[6] = new mx.binding.PropertyWatcher("dataProvider",
            {
                collectionChange: true
            }
                                                                 );

        // writeWatcher id=7 shouldWriteSelf=true class=flex2.compiler.as3.binding.RepeaterItemWatcher shouldWriteChildren=true
        watchers[7] = new mx.binding.RepeaterItemWatcher(watchers[6]);

        // writeWatcher id=9 shouldWriteSelf=false class=flex2.compiler.as3.binding.FunctionReturnWatcher shouldWriteChildren=true

        // writeWatcher id=11 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher shouldWriteChildren=true
        watchers[11] = new mx.binding.PropertyWatcher("operatorIndex",
            null
                                                                 );

        // writeWatcher id=8 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher shouldWriteChildren=true
        watchers[8] = new mx.binding.PropertyWatcher("associatedNode",
            null
                                                                 );

        // writeWatcher id=3 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher shouldWriteChildren=true
        watchers[3] = new mx.binding.PropertyWatcher("nodeName",
            {
                propertyChange: true
            }
                                                                 );

        // writeWatcher id=4 shouldWriteSelf=false class=flex2.compiler.as3.binding.PropertyWatcher shouldWriteChildren=true

        // writeWatcher id=1 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher shouldWriteChildren=true
        watchers[1] = new mx.binding.PropertyWatcher("nodeColor",
            {
                propertyChange: true
            }
                                                                 );

        // writeWatcher id=2 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher shouldWriteChildren=true
        watchers[2] = new mx.binding.PropertyWatcher("nodeNumber",
            {
                propertyChange: true
            }
                                                                 );

        // writeWatcher id=12 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher shouldWriteChildren=true
        watchers[12] = new mx.binding.PropertyWatcher("enable",
            {
                propertyChange: true
            }
                                                                 );


        // writeWatcherBottom id=10 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher
        // writePropertyWatcherBottom id=10 size=1
        watchers[10].addListener(bindings[6]);
        watchers[10].propertyGetter = propertyGetter;
        watchers[10].updateParent(target);

 





        // writeWatcherBottom id=5 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher
        // writePropertyWatcherBottom id=5 size=3
        tempWatcher = watchers[5];
        tempWatcher.addListener(bindings[4]);
        tempWatcher.addListener(bindings[7]);
        tempWatcher.addListener(bindings[5]);
        watchers[5].propertyGetter = propertyGetter;
        watchers[5].updateParent(target);

 





        // writeWatcherBottom id=6 shouldWriteSelf=true class=flex2.compiler.as3.binding.RepeaterDataProviderWatcher
        // writePropertyWatcherBottom id=6 size=3
        tempWatcher = watchers[6];
        tempWatcher.addListener(bindings[4]);
        tempWatcher.addListener(bindings[7]);
        tempWatcher.addListener(bindings[5]);
        watchers[5].addChild(watchers[6]);

 





        // writeWatcherBottom id=7 shouldWriteSelf=true class=flex2.compiler.as3.binding.RepeaterItemWatcher
        watchers[6].addChild(watchers[7]);

 





        // writeWatcherBottom id=9 shouldWriteSelf=false class=flex2.compiler.as3.binding.FunctionReturnWatcher

 





        // writeWatcherBottom id=11 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher
        // writePropertyWatcherBottom id=11 size=1
        watchers[11].addListener(bindings[7]);
        watchers[7].addChild(watchers[11]);

 





        // writeWatcherBottom id=8 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher
        // writePropertyWatcherBottom id=8 size=1
        watchers[8].addListener(bindings[4]);
        watchers[7].addChild(watchers[8]);

 





        // writeWatcherBottom id=3 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher
        // writePropertyWatcherBottom id=3 size=1
        watchers[3].addListener(bindings[2]);
        watchers[3].propertyGetter = propertyGetter;
        watchers[3].updateParent(target);

 





        // writeWatcherBottom id=4 shouldWriteSelf=false class=flex2.compiler.as3.binding.PropertyWatcher

 





        // writeWatcherBottom id=1 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher
        // writePropertyWatcherBottom id=1 size=1
        watchers[1].addListener(bindings[0]);
        watchers[1].propertyGetter = propertyGetter;
        watchers[1].updateParent(target);

 





        // writeWatcherBottom id=2 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher
        // writePropertyWatcherBottom id=2 size=1
        watchers[2].addListener(bindings[1]);
        watchers[2].propertyGetter = propertyGetter;
        watchers[2].updateParent(target);

 





        // writeWatcherBottom id=12 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher
        // writePropertyWatcherBottom id=12 size=1
        watchers[12].addListener(bindings[8]);
        watchers[12].propertyGetter = propertyGetter;
        watchers[12].updateParent(target);

 






        bindings[0].uiComponentWatcher = 1;
        bindings[0].execute();
        bindings[1].uiComponentWatcher = 2;
        bindings[1].execute();
        bindings[2].uiComponentWatcher = 3;
        bindings[2].execute();
        bindings[3].uiComponentWatcher = 4;
        bindings[3].execute();
        bindings[4].uiComponentWatcher = 5;
        bindings[4].execute();
        bindings[5].uiComponentWatcher = 5;
        bindings[5].execute();
        bindings[6].uiComponentWatcher = 10;
        bindings[6].execute();
        bindings[7].uiComponentWatcher = 5;
        bindings[7].execute();
        bindings[8].uiComponentWatcher = 12;
        bindings[8].execute();
    }
}

}
