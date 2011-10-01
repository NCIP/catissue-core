package 
{

import flash.display.Sprite;
import mx.core.IFlexModuleFactory;
import mx.core.mx_internal;
import mx.styles.CSSStyleDeclaration;
import mx.styles.StyleManager;
import mx.skins.halo.BusyCursor;

[ExcludeClass]

public class _CursorManagerStyle
{
    [Embed(_pathsep='true', _resolvedSource='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', source='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', _file='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$defaults.css', symbol='mx.skins.cursor.BusyCursor', _line='503')]
    private static var _embed_css_Assets_swf_mx_skins_cursor_BusyCursor_264038237:Class;

    public static function init(fbs:IFlexModuleFactory):void
    {
        var style:CSSStyleDeclaration = StyleManager.getStyleDeclaration("CursorManager");
    
        if (!style)
        {
            style = new CSSStyleDeclaration();
            StyleManager.setStyleDeclaration("CursorManager", style, false);
        }
    
        if (style.defaultFactory == null)
        {
            style.defaultFactory = function():void
            {
                this.busyCursor = mx.skins.halo.BusyCursor;
                this.busyCursorBackground = _embed_css_Assets_swf_mx_skins_cursor_BusyCursor_264038237;
            };
        }
    }
}

}
