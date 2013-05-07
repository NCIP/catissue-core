package 
{

import flash.display.Sprite;
import mx.core.IFlexModuleFactory;
import mx.core.mx_internal;
import mx.styles.CSSStyleDeclaration;
import mx.styles.StyleManager;
import mx.skins.halo.DefaultDragImage;

[ExcludeClass]

public class _DragManagerStyle
{
    [Embed(_pathsep='true', _resolvedSource='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', source='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', _file='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$defaults.css', symbol='mx.skins.cursor.DragLink', _line='642')]
    private static var _embed_css_Assets_swf_mx_skins_cursor_DragLink_482082494:Class;
    [Embed(_pathsep='true', _resolvedSource='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', source='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', _file='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$defaults.css', symbol='mx.skins.cursor.DragCopy', _line='640')]
    private static var _embed_css_Assets_swf_mx_skins_cursor_DragCopy_478674743:Class;
    [Embed(_pathsep='true', _resolvedSource='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', source='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', _file='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$defaults.css', symbol='mx.skins.cursor.DragReject', _line='644')]
    private static var _embed_css_Assets_swf_mx_skins_cursor_DragReject_1953303401:Class;
    [Embed(_pathsep='true', _resolvedSource='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', source='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', _file='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$defaults.css', symbol='mx.skins.cursor.DragMove', _line='643')]
    private static var _embed_css_Assets_swf_mx_skins_cursor_DragMove_482024091:Class;

    public static function init(fbs:IFlexModuleFactory):void
    {
        var style:CSSStyleDeclaration = StyleManager.getStyleDeclaration("DragManager");
    
        if (!style)
        {
            style = new CSSStyleDeclaration();
            StyleManager.setStyleDeclaration("DragManager", style, false);
        }
    
        if (style.defaultFactory == null)
        {
            style.defaultFactory = function():void
            {
                this.rejectCursor = _embed_css_Assets_swf_mx_skins_cursor_DragReject_1953303401;
                this.defaultDragImageSkin = mx.skins.halo.DefaultDragImage;
                this.moveCursor = _embed_css_Assets_swf_mx_skins_cursor_DragMove_482024091;
                this.copyCursor = _embed_css_Assets_swf_mx_skins_cursor_DragCopy_478674743;
                this.linkCursor = _embed_css_Assets_swf_mx_skins_cursor_DragLink_482082494;
            };
        }
    }
}

}
