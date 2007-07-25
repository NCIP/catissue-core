package util
{
	import mx.rpc.remoting.mxml.RemoteObject;
	import mx.rpc.AbstractOperation;
	import mx.rpc.events.ResultEvent;
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import flash.events.Event;
	import flash.events.EventDispatcher;
	
	[Event(name="initCompleteEvent", type="flash.events.Event")]
	public class MetadataManager extends EventDispatcher
	{
		private var remoteObj:RemoteObject = new RemoteObject("cdeService");
		private var counter:int = 0;
		
		public function init():void
		{
			loadData("getTissueSidePVList",handleTissueSideResult);
			loadData("getTissueSitePVList",handleTissueSiteResult);
			loadData("getPathologicalStatusPVList",handlePathologicalStatusResult);
			loadData("getSpecimenClassStatusPVList",handleSpecimenClassResult);
			loadData("getFluidSpecimenTypePVList",handleFluidSpecimenTypeResult);
			loadData("getTissueSpecimenTypePVList",handleTissueSpecimenTypeResult);
			loadData("getMolecularSpecimenTypePVList",handleMolecularSpecimenTypeResult);
			loadData("getCellSpecimenTypePVList",handleCellSpecimenTypeResult);
		}
		
		private function updateCounter():void
		{
			counter++;
			if(counter == 8)
			{
				var event:Event = new Event("initCompleteEvent");
				dispatchEvent(event);
			}
		}
			
		private function loadData(remoteFuncName:String ,handlerFunction:Function):void
		{
			var operation:AbstractOperation = remoteObj.getOperation(remoteFuncName);
			operation.addEventListener(ResultEvent.RESULT,handlerFunction);
			operation.send();
		}
		
		private function handleTissueSideResult(event:ResultEvent):void 
		{
			MetadataModel.getInstance().tissueSidePVList = event.result as ArrayCollection;
			updateCounter();
		}
		
		private function handleTissueSiteResult(event:ResultEvent):void 
		{
			MetadataModel.getInstance().tissueSitePVList = event.result as ArrayCollection;			
			updateCounter();
		}
		
		private function handlePathologicalStatusResult(event:ResultEvent):void 
		{
			MetadataModel.getInstance().pathologicalStatusPVList = event.result as ArrayCollection;
			updateCounter();
		}
		
		private function handleSpecimenClassResult(event:ResultEvent):void 
		{
			MetadataModel.getInstance().specimenClassPVList = event.result as ArrayCollection;
			updateCounter();
		}
		
		private function handleFluidSpecimenTypeResult(event:ResultEvent):void 
		{
			handleSpecimenTypeResult(event,0);
		}
		
		private function handleTissueSpecimenTypeResult(event:ResultEvent):void 
		{
			handleSpecimenTypeResult(event,1);
		}
		
		private function handleMolecularSpecimenTypeResult(event:ResultEvent):void 
		{
			handleSpecimenTypeResult(event,2);
		}
		
		private function handleCellSpecimenTypeResult(event:ResultEvent):void 
		{
			handleSpecimenTypeResult(event,3);
		}
		
		private function handleSpecimenTypeResult(event:ResultEvent, index:int):void 
		{
			var spTypeList:Object = event.result as ArrayCollection;
			MetadataModel.getInstance().specimenTypePVArrayColl.addItemAt(spTypeList,index);
			updateCounter();				
		}
	}
}