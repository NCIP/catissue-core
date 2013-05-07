






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
public class _Components_AmbiguityPanel_inlineComponent1WatcherSetupUtil extends Sprite
    implements mx.binding.IWatcherSetupUtil
{
    public function _Components_AmbiguityPanel_inlineComponent1WatcherSetupUtil()
    {
        super();
    }

    public static function init(fbs:IFlexModuleFactory):void
    {
        import Components.AmbiguityPanel_inlineComponent1;
        (Components.AmbiguityPanel_inlineComponent1).watcherSetupUtil = new _Components_AmbiguityPanel_inlineComponent1WatcherSetupUtil();
    }

    public function setup(target:Object,
                          propertyGetter:Function,
                          bindings:Array,
                          watchers:Array):void
    {
        import mx.core.DeferredInstanceFromFunction;
        import flash.events.EventDispatcher;
        import mx.utils.UIDUtil;
        import flash.events.MouseEvent;
        import mx.controls.CheckBox;
        import Components.AmbiguityPanel;
        import mx.core.IDeferredInstance;
        import mx.core.ClassFactory;
        import mx.core.mx_internal;
        import mx.events.PropertyChangeEvent;
        import flash.events.Event;
        import mx.core.IFactory;
        import mx.core.IPropertyChangeNotifier;
        import mx.core.DeferredInstanceFromClass;
        import mx.utils.ObjectProxy;
        import mx.binding.BindingManager;
        import flash.events.IEventDispatcher;

        var tempWatcher:mx.binding.Watcher;

        // writeWatcher id=1 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher shouldWriteChildren=true
        watchers[1] = new mx.binding.PropertyWatcher("data",
            {
                dataChange: true
            }
                                                                 );

        // writeWatcher id=2 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher shouldWriteChildren=true
        watchers[2] = new mx.binding.PropertyWatcher("isSelected",
            null
                                                                 );


        // writeWatcherBottom id=1 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher
        // writePropertyWatcherBottom id=1 size=1
        watchers[1].addListener(bindings[0]);
        watchers[1].propertyGetter = propertyGetter;
        watchers[1].updateParent(target);

 





        // writeWatcherBottom id=2 shouldWriteSelf=true class=flex2.compiler.as3.binding.PropertyWatcher
        // writePropertyWatcherBottom id=2 size=1
        watchers[2].addListener(bindings[0]);
        watchers[1].addChild(watchers[2]);

 






        bindings[0].uiComponentWatcher = 1;
        bindings[0].execute();
    }
}

}
