package 
{

import flash.display.Sprite;
import mx.core.IFlexModuleFactory;
import mx.core.mx_internal;
import mx.styles.CSSStyleDeclaration;
import mx.styles.StyleManager;
import mx.skins.halo.ComboBoxArrowSkin;

[ExcludeClass]

public class _ComboBoxStyle
{

    public static function init(fbs:IFlexModuleFactory):void
    {
        var style:CSSStyleDeclaration = StyleManager.getStyleDeclaration("ComboBox");
    
        if (!style)
        {
            style = new CSSStyleDeclaration();
            StyleManager.setStyleDeclaration("ComboBox", style, false);
        }
    
        if (style.defaultFactory == null)
        {
            style.defaultFactory = function():void
            {
                this.editableOverSkin = mx.skins.halo.ComboBoxArrowSkin;
                this.paddingLeft = 5;
                this.arrowButtonWidth = 22;
                this.fontWeight = "bold";
                this.upSkin = mx.skins.halo.ComboBoxArrowSkin;
                this.leading = 0;
                this.overSkin = mx.skins.halo.ComboBoxArrowSkin;
                this.editableDisabledSkin = mx.skins.halo.ComboBoxArrowSkin;
                this.editableUpSkin = mx.skins.halo.ComboBoxArrowSkin;
                this.cornerRadius = 5;
                this.paddingRight = 5;
                this.editableDownSkin = mx.skins.halo.ComboBoxArrowSkin;
                this.downSkin = mx.skins.halo.ComboBoxArrowSkin;
                this.disabledSkin = mx.skins.halo.ComboBoxArrowSkin;
                this.dropDownStyleName = "comboDropDown";
            };
        }
    }
}

}
