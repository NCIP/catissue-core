package 
{

import flash.display.Sprite;
import mx.core.IFlexModuleFactory;
import mx.core.mx_internal;
import mx.styles.CSSStyleDeclaration;
import mx.styles.StyleManager;
import mx.skins.halo.ListDropIndicator;

[ExcludeClass]

public class _MenuStyle
{
    [Embed(_pathsep='true', _resolvedSource='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', source='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', _file='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$defaults.css', symbol='MenuCheckDisabled', _line='929')]
    private static var _embed_css_Assets_swf_MenuCheckDisabled_261251336:Class;
    [Embed(_pathsep='true', _resolvedSource='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', source='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', _file='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$defaults.css', symbol='MenuSeparator', _line='939')]
    private static var _embed_css_Assets_swf_MenuSeparator_1758368201:Class;
    [Embed(_pathsep='true', _resolvedSource='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', source='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', _file='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$defaults.css', symbol='MenuCheckEnabled', _line='930')]
    private static var _embed_css_Assets_swf_MenuCheckEnabled_1379955217:Class;
    [Embed(_pathsep='true', _resolvedSource='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', source='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', _file='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$defaults.css', symbol='MenuRadioEnabled', _line='937')]
    private static var _embed_css_Assets_swf_MenuRadioEnabled_800944776:Class;
    [Embed(_pathsep='true', _resolvedSource='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', source='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', _file='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$defaults.css', symbol='MenuBranchEnabled', _line='928')]
    private static var _embed_css_Assets_swf_MenuBranchEnabled_863963623:Class;
    [Embed(_pathsep='true', _resolvedSource='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', source='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', _file='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$defaults.css', symbol='MenuBranchDisabled', _line='927')]
    private static var _embed_css_Assets_swf_MenuBranchDisabled_1506968816:Class;
    [Embed(_pathsep='true', _resolvedSource='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', source='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$Assets.swf', _file='C:/Eclipse/workspace/catissuecore_Merge/WEB-INF/flex/frameworks/libs/framework.swc$defaults.css', symbol='MenuRadioDisabled', _line='938')]
    private static var _embed_css_Assets_swf_MenuRadioDisabled_844773937:Class;

    public static function init(fbs:IFlexModuleFactory):void
    {
        var style:CSSStyleDeclaration = StyleManager.getStyleDeclaration("Menu");
    
        if (!style)
        {
            style = new CSSStyleDeclaration();
            StyleManager.setStyleDeclaration("Menu", style, false);
        }
    
        if (style.defaultFactory == null)
        {
            style.defaultFactory = function():void
            {
                this.branchDisabledIcon = _embed_css_Assets_swf_MenuBranchDisabled_1506968816;
                this.paddingLeft = 1;
                this.checkIcon = _embed_css_Assets_swf_MenuCheckEnabled_1379955217;
                this.dropShadowEnabled = true;
                this.checkDisabledIcon = _embed_css_Assets_swf_MenuCheckDisabled_261251336;
                this.radioIcon = _embed_css_Assets_swf_MenuRadioEnabled_800944776;
                this.borderStyle = "menuBorder";
                this.paddingBottom = 1;
                this.radioDisabledIcon = _embed_css_Assets_swf_MenuRadioDisabled_844773937;
                this.dropIndicatorSkin = mx.skins.halo.ListDropIndicator;
                this.paddingTop = 1;
                this.paddingRight = 0;
                this.verticalAlign = "middle";
                this.separatorSkin = _embed_css_Assets_swf_MenuSeparator_1758368201;
                this.branchIcon = _embed_css_Assets_swf_MenuBranchEnabled_863963623;
            };
        }
    }
}

}
