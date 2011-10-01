package 
{

import flash.display.Sprite;
import mx.core.IFlexModuleFactory;
import mx.core.mx_internal;
import mx.styles.CSSStyleDeclaration;
import mx.styles.StyleManager;
import mx.skins.halo.CheckBoxIcon;

[ExcludeClass]

public class _CheckBoxStyle
{

    public static function init(fbs:IFlexModuleFactory):void
    {
        var style:CSSStyleDeclaration = StyleManager.getStyleDeclaration("CheckBox");
    
        if (!style)
        {
            style = new CSSStyleDeclaration();
            StyleManager.setStyleDeclaration("CheckBox", style, false);
        }
    
        if (style.defaultFactory == null)
        {
            style.defaultFactory = function():void
            {
                this.selectedDisabledIcon = mx.skins.halo.CheckBoxIcon;
                this.fontWeight = "normal";
                this.selectedOverIcon = mx.skins.halo.CheckBoxIcon;
                this.upSkin = null;
                this.overIcon = mx.skins.halo.CheckBoxIcon;
                this.overSkin = null;
                this.selectedDisabledSkin = null;
                this.selectedDownIcon = mx.skins.halo.CheckBoxIcon;
                this.disabledIcon = mx.skins.halo.CheckBoxIcon;
                this.textAlign = "left";
                this.selectedDownSkin = null;
                this.selectedUpSkin = null;
                this.selectedOverSkin = null;
                this.upIcon = mx.skins.halo.CheckBoxIcon;
                this.downSkin = null;
                this.selectedUpIcon = mx.skins.halo.CheckBoxIcon;
                this.disabledSkin = null;
                this.downIcon = mx.skins.halo.CheckBoxIcon;
            };
        }
    }
}

}
