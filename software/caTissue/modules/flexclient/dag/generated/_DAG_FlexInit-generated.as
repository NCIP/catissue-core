package {
import flash.utils.*;
import mx.core.IFlexModuleFactory;
import flash.system.*
import flash.net.registerClassAlias;
import Components.IDAGPath;
import mx.collections.ArrayCollection;
import mx.collections.ArrayList;
import mx.messaging.messages.AcknowledgeMessage;
import mx.messaging.messages.AsyncMessage;
import mx.messaging.messages.CommandMessage;
import mx.messaging.messages.ErrorMessage;
import mx.messaging.messages.HTTPRequestMessage;
import mx.messaging.messages.RemotingMessage;
import mx.utils.ObjectProxy;
import mx.effects.EffectManager;
import mx.core.mx_internal;
import mx.messaging.config.ServerConfig;
import mx.messaging.channels.RTMPChannel;
import mx.messaging.channels.AMFChannel;
import mx.messaging.channels.HTTPChannel;
import mx.messaging.channels.SecureHTTPChannel;
import mx.messaging.channels.SecureAMFChannel;

[Mixin]
public class _DAG_FlexInit
{
   public function _DAG_FlexInit()
   {
       super();
   }
   public static function init(fbs:IFlexModuleFactory):void
   {
      EffectManager.mx_internal::registerEffectTrigger("addedEffect", "added");
      EffectManager.mx_internal::registerEffectTrigger("creationCompleteEffect", "creationComplete");
      EffectManager.mx_internal::registerEffectTrigger("focusInEffect", "focusIn");
      EffectManager.mx_internal::registerEffectTrigger("focusOutEffect", "focusOut");
      EffectManager.mx_internal::registerEffectTrigger("hideEffect", "hide");
      EffectManager.mx_internal::registerEffectTrigger("mouseDownEffect", "mouseDown");
      EffectManager.mx_internal::registerEffectTrigger("mouseUpEffect", "mouseUp");
      EffectManager.mx_internal::registerEffectTrigger("moveEffect", "move");
      EffectManager.mx_internal::registerEffectTrigger("removedEffect", "removed");
      EffectManager.mx_internal::registerEffectTrigger("resizeEffect", "resize");
      EffectManager.mx_internal::registerEffectTrigger("resizeEndEffect", "resizeEnd");
      EffectManager.mx_internal::registerEffectTrigger("resizeStartEffect", "resizeStart");
      EffectManager.mx_internal::registerEffectTrigger("rollOutEffect", "rollOut");
      EffectManager.mx_internal::registerEffectTrigger("rollOverEffect", "rollOver");
      EffectManager.mx_internal::registerEffectTrigger("showEffect", "show");
      flash.net.registerClassAlias("edu.wustl.catissuecore.flex.dag.DAGPath", Components.IDAGPath);
      flash.net.registerClassAlias("flex.messaging.io.ArrayCollection", mx.collections.ArrayCollection);
      flash.net.registerClassAlias("flex.messaging.io.ArrayList", mx.collections.ArrayList);
      flash.net.registerClassAlias("flex.messaging.messages.AcknowledgeMessage", mx.messaging.messages.AcknowledgeMessage);
      flash.net.registerClassAlias("flex.messaging.messages.AsyncMessage", mx.messaging.messages.AsyncMessage);
      flash.net.registerClassAlias("flex.messaging.messages.CommandMessage", mx.messaging.messages.CommandMessage);
      flash.net.registerClassAlias("flex.messaging.messages.ErrorMessage", mx.messaging.messages.ErrorMessage);
      flash.net.registerClassAlias("flex.messaging.messages.HTTPMessage", mx.messaging.messages.HTTPRequestMessage);
      flash.net.registerClassAlias("flex.messaging.messages.RemotingMessage", mx.messaging.messages.RemotingMessage);
      flash.net.registerClassAlias("flex.messaging.io.ObjectProxy", mx.utils.ObjectProxy);
      var styleNames:Array = ["fontWeight", "modalTransparencyBlur", "rollOverColor", "textRollOverColor", "verticalGridLineColor", "backgroundDisabledColor", "textIndent", "barColor", "fontSize", "kerning", "footerColors", "textAlign", "fontStyle", "dropdownBorderColor", "modalTransparencyDuration", "textSelectedColor", "horizontalGridLineColor", "selectionColor", "modalTransparency", "fontGridFitType", "selectionDisabledColor", "disabledColor", "fontAntiAliasType", "modalTransparencyColor", "alternatingItemColors", "dropShadowColor", "themeColor", "letterSpacing", "fontFamily", "color", "fontThickness", "errorColor", "headerColors", "fontSharpness"];

      import mx.styles.StyleManager;

      for (var i:int = 0; i < styleNames.length; i++)
      {
         StyleManager.registerInheritingStyle(styleNames[i]);
      }

ServerConfig.xml =
<services>
	<remoting-service messageTypes="flex.messaging.messages.RemotingMessage">
		<destination id="cdeService">
			<channels>
				<channel ref="my-amf"/>
			</channels>
		</destination>
	</remoting-service>
	<channels>
		<channel id="my-amf" type="mx.messaging.channels.AMFChannel">
			<endpoint uri="http://{server.name}:{server.port}/catissuecore/messagebroker/amf"/>
			<properties>
				<polling-enabled>false</polling-enabled>
			</properties>
		</channel>
		<channel id="my-polling-amf" type="mx.messaging.channels.AMFChannel">
			<endpoint uri="http://{server.name}:{server.port}/catissuecore/messagebroker/amfpolling"/>
			<properties>
				<polling-interval-seconds>8</polling-interval-seconds>
				<polling-enabled>true</polling-enabled>
			</properties>
		</channel>
		<channel id="my-http" type="mx.messaging.channels.HTTPChannel">
			<endpoint uri="http://{server.name}:{server.port}/catissuecore/messagebroker/http"/>
			<properties>
			</properties>
		</channel>
		<channel id="my-secure-amf" type="mx.messaging.channels.SecureAMFChannel">
			<endpoint uri="https://{server.name}:{server.port}/catissuecore/messagebroker/amfsecure"/>
			<properties>
			</properties>
		</channel>
		<channel id="my-rtmp" type="mx.messaging.channels.RTMPChannel">
			<endpoint uri="rtmp://{server.name}:2038"/>
			<properties>
			</properties>
		</channel>
		<channel id="my-secure-http" type="mx.messaging.channels.SecureHTTPChannel">
			<endpoint uri="https://{server.name}:{server.port}/catissuecore/messagebroker/httpsecure"/>
			<properties>
			</properties>
		</channel>
	</channels>
</services>;
   }
   // static references for configured channels
   private static var mx_messaging_channels_RTMPChannel_ref:RTMPChannel;
   private static var mx_messaging_channels_AMFChannel_ref:AMFChannel;
   private static var mx_messaging_channels_HTTPChannel_ref:HTTPChannel;
   private static var mx_messaging_channels_SecureHTTPChannel_ref:SecureHTTPChannel;
   private static var mx_messaging_channels_SecureAMFChannel_ref:SecureAMFChannel;
}  // FlexInit
}  // package
