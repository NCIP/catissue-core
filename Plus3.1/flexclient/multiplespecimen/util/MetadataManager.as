package util
{
	import mx.rpc.remoting.mxml.RemoteObject;
	import mx.rpc.AbstractOperation;
	import mx.rpc.events.ResultEvent;
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import valueobjects.SpecimenData;
	import util.Utility;
	
	
	[Event(name="initCompleteEvent", type="flash.events.Event")]
	public class MetadataManager extends EventDispatcher
	{
		private var remoteObj:RemoteObject = new RemoteObject("cdeService");

		private var counter:int = 0;
		private var counterEndVal:int = 8;
		
		public var spDataList:ArrayCollection;
		public function init(mode:String):void
		{
			remoteObj.channelSet = Utility.getChannelSet();
			if(mode==MSPParameter.MODE_PARAM_VAL_EDIT)	
			{
				counterEndVal = 9;
				loadData("readSpecimenList",handleReadSpecimenResult);
			}
			else
			{
				
			}
			
			loadData("getTissueSidePVList",handleTissueSideResult);
			loadData("getTissueSitePVList",handleTissueSiteResult);
			loadData("getPathologicalStatusPVList",handlePathologicalStatusResult);
			loadData("getSpecimenClassStatusPVList",handleSpecimenClassResult);
			loadData("getFluidSpecimenTypePVList",handleFluidSpecimenTypeResult);
			loadData("getTissueSpecimenTypePVList",handleTissueSpecimenTypeResult);
			loadData("getMolecularSpecimenTypePVList",handleMolecularSpecimenTypeResult);
			loadData("getCellSpecimenTypePVList",handleCellSpecimenTypeResult);
			
			
			loadData("getUserList",handleUserListResult);
			loadData("getProcedureList",handleProcedureListResult);
			loadData("getContainerList",handleContainerListResult);
			loadData("getReceivedQualityList",handleReceivedQualityListResult);

			//Biohazard
			loadData("getBiohazardTypeList",handleBioazardTypeListResult);
			loadData("getBiohazardNameList",handleBioazardNameListResult);
			
		}
		
		public function initDerivedSp():void
		{
			loadData("getSpecimenClassStatusPVList",handleSpecimenClassResult);
			loadData("getFluidSpecimenTypePVList",handleFluidSpecimenTypeResult);
			loadData("getTissueSpecimenTypePVList",handleTissueSpecimenTypeResult);
			loadData("getMolecularSpecimenTypePVList",handleMolecularSpecimenTypeResult);
			loadData("getCellSpecimenTypePVList",handleCellSpecimenTypeResult);

		}
		
		private function updateCounter():void
		{
			counter++;
			if(counter == counterEndVal)
			{
				var event:Event = new Event("initCompleteEvent");
				dispatchEvent(event);
			}
		}
		
		private function handleReadSpecimenResult(event:ResultEvent):void 
		{
			spDataList = event.result as ArrayCollection;
			updateCounter();
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
		
		private function handleUserListResult(event:ResultEvent) : void
		{
			MetadataModel.getInstance().userList = event.result as ArrayCollection;

		}

		private function handleProcedureListResult(event:ResultEvent) : void
		{
			MetadataModel.getInstance().procedureList = event.result as ArrayCollection;

		}

		private function handleContainerListResult(event:ResultEvent) : void
		{
			MetadataModel.getInstance().containerList = event.result as ArrayCollection;

		}
		
		private function handleReceivedQualityListResult(event:ResultEvent) : void
		{
			MetadataModel.getInstance().receivedQualityList = event.result as ArrayCollection;

		}
		

		private function handleBioazardTypeListResult(event:ResultEvent) : void
		{
			MetadataModel.getInstance().biohazardTypeList = event.result as ArrayCollection;

		}
		

		private function handleBioazardNameListResult(event:ResultEvent) : void
		{
			MetadataModel.getInstance().biohazardNameList = event.result as ArrayCollection;

		}
		
		
	 
	}
}