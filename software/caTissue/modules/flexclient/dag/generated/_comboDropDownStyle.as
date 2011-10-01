package 
{

import flash.display.Sprite;
import mx.core.IFlexModuleFactory;
import mx.core.mx_internal;
import mx.styles.CSSStyleDeclaration;
import mx.styles.StyleManager;

[ExcludeClass]

public class _comboDropDownStyle
{

    public static function init(fbs:IFlexModuleFactory):void
    {
        var style:CSSStyleDeclaration = StyleManager.getStyleDeclaration(".comboDropDown");
    
        if (!style)
        {
            style = new CSSStyleDeclaration();
            StyleManager.setStyleDeclaration(".comboDropDown", style, false);
        }
    
        if (style.defaultFactory == null)
        {
            style.defaultFactory = function():void
            {
                this.fontWeight = "normal";
                this.cornerRadius = 0;
                this.dropShadowEnabled = true;
                this.shadowDirection = "center";
                this.borderThickness = 0;
                this.shadowDistance = 1;
                this.backgroundColor = 0xffffff;
            };
        }
    }
}

}
