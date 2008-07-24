package valueobjects
{
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	import flash.utils.IExternalizable;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	
	import util.Constants;
	import util.SpecimenValidator;
	import util.MSPParameter;

	[Bindable]
	[RemoteClass(alias="edu.wustl.catissuecore.flex.SpecimenBean")]
	public class SpecimenData implements IExternalizable
	{
		public var specimenTypePVList:ArrayCollection;	
		public var isSelected:Boolean;		
		public var parentType:String;
		
		public var spID:Number = -3;
		public var mode:String = '';
//		public var scgName:String = '';
	//	public var parentSpecimenName:String = '';
	
		public var parentName:String =null;
		public var parentNameErrStr:String = null;
		
		public var specimenLabel:String = '';
		public var specimenLabelErrStr:String = null;
		
		public var specimenBarcode:String = '';
		public var lineage:String = '';
		public var specimenClass:String = '';
		public var specimenClassErrStr:String = null;
		public var specimenType:String = '';
		public var specimenTypeErrStr:String = null;
		
		public var tissueSite:String = '';
		public var tissueSiteErrStr:String = null;
		public var tissueSide:String = '';
		public var tissueSideErrStr:String = null;
		public var pathologicalStatus:String = '';
		public var pathologicalStatusErrStr:String = null;
		
		public var creationDate:Date;
		public var quantity:Number;
		public var quantityErrStr:String= null;
		public var concentration:Number;
		public var storage:String;
		public var comment:String;
		public var unit:String;
		
		public var exIdColl:ArrayCollection = new ArrayCollection();
		public var collectionEvent:EventBean ;
		public var receivedEvent:EventBean ;
		
		public var biohazardColl:ArrayCollection = new ArrayCollection();
		public var derivedSpColl:ArrayCollection = new ArrayCollection();
		public var isParentEnabled:Boolean= true;
		public function SpecimenData(specimenLabel:String="",specimenBarcode:String="",tissueSide:String="")
		{
			this.parentType = Constants.NEW_SPECIMEN;
			
//			this.scgName = '';
//			this.parentSpecimenName = ''
			this.parentName = '';
			
			this.specimenLabel = specimenLabel;
			this.specimenBarcode = specimenBarcode;
			this.lineage = '';
			this.specimenClass = 'Fluid';
			this.specimenType = 'Not Specified';
			
			this.tissueSite = 'Not Specified'
			this.tissueSide = 'Not Specified';
			this.pathologicalStatus = 'Not Specified';
			
			this.creationDate =  new Date();
			this.quantity = new Number();
			this.concentration = new Number();
			this.storage = "Virtual";
			
			this.comment = '';
			this.unit = '';	
			
//			var exBean:ExternalIdentifierBean = new ExternalIdentifierBean(false,0,'A','B');
			var exBean:ExternalIdentifierBean = new ExternalIdentifierBean();
			exIdColl.addItem(exBean);
			
			collectionEvent = new EventBean();
			receivedEvent = new EventBean();
			
			var biohazardBean:BiohazardBean = new BiohazardBean();
			biohazardColl.addItem(biohazardBean);
			
			isParentEnabled = true;
		}
		
		public function copy(spData:SpecimenData):void
		{
			this.specimenTypePVList = spData.specimenTypePVList;
			this.parentType = spData.parentType;
			this.parentName = spData.parentName;
			this.specimenLabel = spData.specimenLabel;
			this.specimenBarcode = spData.specimenBarcode;
			this.lineage = spData.lineage;
			this.specimenClass = spData.specimenClass;
			
			this.specimenType = spData.specimenType;
			
			this.tissueSite = spData.tissueSite;
			this.tissueSide = spData.tissueSide;
			this.pathologicalStatus = spData.pathologicalStatus;
			
			this.creationDate = spData.creationDate;
			this.quantity = spData.quantity;
			this.concentration = spData.concentration;
			this.storage = spData.storage;
			
			this.comment = spData.comment;
			this.unit = spData.unit;
			copyExId(spData.exIdColl);
			copyCollectedEvent(spData.collectionEvent);
			copyReceivedEvent(spData.receivedEvent);
			
			copyBiohazard(spData.biohazardColl);
			copyDerived(spData.derivedSpColl);
//			this.collectionEvent = spData.collectionEvent;
			//this.exIdColl = spData.exIdColl;
		}
		
		public function copyAttributes(spData:SpecimenData,attributeList:ArrayCollection):void
		{
			if(attributeList.contains(Constants.PARENT))
			{
				this.parentType = spData.parentType;
			}
			if(attributeList.contains(Constants.PARENT_NAME))
			{
				//this.scgName = spData.scgName;
				//this.parentSpecimenName = spData.parentSpecimenName;
				this.parentName = spData.parentName;
				
			}
			if(attributeList.contains(Constants.LABEL))
			{
				this.specimenLabel = spData.specimenLabel;
			}
			if(attributeList.contains(Constants.BARCODE))
			{	
				this.specimenBarcode = spData.specimenBarcode;
			}
			if(attributeList.contains(Constants.CLASS))
			{
				this.specimenClass = spData.specimenClass;
				this.specimenTypePVList = spData.specimenTypePVList;
			}
			if(attributeList.contains(Constants.TYPE))
			{
				this.specimenType = spData.specimenType;
			}
			if(attributeList.contains(Constants.TISSUE_SITE))
			{
				this.tissueSite = spData.tissueSite;
			}
			if(attributeList.contains(Constants.TISSUE_SIDE))
			{
				this.tissueSide = spData.tissueSide;
			}
			if(attributeList.contains(Constants.PATHOLOGICAL_STATUS))
			{
				this.pathologicalStatus = spData.pathologicalStatus;
			}
			if(attributeList.contains(Constants.CREATED_ON))
			{
				this.creationDate = spData.creationDate;
			}
			if(attributeList.contains(Constants.QUANTITY))
			{
				this.quantity = spData.quantity;
			}
			if(attributeList.contains(Constants.CONCENTRATION))
			{
				this.concentration = spData.concentration;
			}
			if(attributeList.contains(Constants.STORAGE))
			{
				this.storage = spData.storage;
			}
			if(attributeList.contains(Constants.COMMENT))
			{
				this.comment = spData.comment;
			}
			if(attributeList.contains(Constants.COLLECTED_EVENT))
			{
				copyCollectedEvent(spData.collectionEvent);
			}
			if(attributeList.contains(Constants.RECEIVED_EVENT))
			{
				copyReceivedEvent(spData.receivedEvent);
			}
			if(attributeList.contains(Constants.EXTERNAL_IDENTIFIER))
			{
				copyExId(spData.exIdColl);
			}
			if(attributeList.contains(Constants.BIOHAZARDS))
			{
				copyBiohazard(spData.biohazardColl);
			}
			if(attributeList.contains(Constants.DERIVATIVE))
			{
				copyDerived(spData.derivedSpColl);
			}
						
		}
		
		private function copyExId(exIdCollCopy:ArrayCollection):void
		{
			this.exIdColl = new ArrayCollection();
			for(var i:int=0;i<exIdCollCopy.length;i++)
			{
				var exIdBeanCopy:ExternalIdentifierBean = ExternalIdentifierBean(exIdCollCopy.getItemAt(i));
				var exIdBean:ExternalIdentifierBean  = new ExternalIdentifierBean();
				exIdBean.copy(exIdBeanCopy);
				
				this.exIdColl.addItem(exIdBean);
			}
		}
		
		private function copyBiohazard(biohazardCollCopy:ArrayCollection):void
		{
			this.biohazardColl = new ArrayCollection();
			for(var i:int=0;i<biohazardCollCopy.length;i++)
			{
				var biohazardCopy:BiohazardBean = BiohazardBean(biohazardCollCopy.getItemAt(i));
				var biohzardBean:BiohazardBean  = new BiohazardBean();
				biohzardBean.copy(biohazardCopy);
				
				this.biohazardColl.addItem(biohzardBean);
			}
		}

		public function copyCollectedEvent(collectedEvent:EventBean):void
		{
			var collectedEventBean:EventBean = new EventBean();
			collectedEventBean.userName = collectedEvent.userName;
			collectedEventBean.eventDate = collectedEvent.eventDate;
			collectedEventBean.eventHour = collectedEvent.eventHour;
			collectedEventBean.eventMinute = collectedEvent.eventMinute;
			collectedEventBean.collectionProcedure = collectedEvent.collectionProcedure;
			collectedEventBean.container = collectedEvent.container;
			collectedEventBean.comment = collectedEvent.comment;	
			this.collectionEvent = collectedEventBean;
		}	
		public function copyReceivedEvent(recievedEvent:EventBean):void
		{
			var receivedEventBean:EventBean = new EventBean();
			receivedEventBean.userName = recievedEvent.userName;
			receivedEventBean.eventDate = recievedEvent.eventDate;
			receivedEventBean.eventHour = recievedEvent.eventHour;
			receivedEventBean.eventMinute = recievedEvent.eventMinute;
			receivedEventBean.receivedQuality = recievedEvent.receivedQuality;
			receivedEventBean.comment = recievedEvent.comment;	
			this.receivedEvent = receivedEventBean;
		}	
		private function copyDerived(derivedCollCopy:ArrayCollection):void
		{
			this.derivedSpColl = new ArrayCollection();
			for(var i:int=0;i<derivedCollCopy.length;i++)
			{
				var derivedCopy:SpecimenData = SpecimenData(derivedCollCopy.getItemAt(i));
				var derivedBean:SpecimenData  = new SpecimenData();
				derivedBean.copy(derivedCopy);
			
				this.derivedSpColl.addItem(derivedBean);
			}
		}
		public function writeExternal(output:IDataOutput) :void 
		{
			output.writeInt(spID);
/*			output.writeUTF(scgName);
			output.writeUTF(parentSpecimenName);*/
			output.writeUTF(parentType);			
			output.writeUTF(parentName);
			output.writeUTF(specimenLabel);
			output.writeUTF(specimenBarcode);
			output.writeUTF(lineage);
			output.writeUTF(specimenClass);
			output.writeUTF(specimenType);
			output.writeUTF(tissueSite);
			output.writeUTF(tissueSide);
			output.writeUTF(pathologicalStatus);
			output.writeObject(creationDate);
			output.writeDouble(quantity);
			output.writeDouble(concentration);
			output.writeUTF(storage);
			output.writeUTF(comment);
			output.writeObject(exIdColl);	
			output.writeObject(biohazardColl);	
			output.writeObject(collectionEvent);
			output.writeObject(receivedEvent);
			output.writeObject(derivedSpColl);
    	}
        
    	public function readExternal(input:IDataInput):void
    	{
			spID = input.readInt();
/*			scgName = input.readUTF();
			parentSpecimenName = input.readUTF();*/
			parentType = input.readUTF();
			parentName = input.readUTF();
			specimenLabel = input.readUTF();
			specimenBarcode = input.readUTF();
			lineage = input.readUTF();
			specimenClass = input.readUTF();
			specimenType = input.readUTF();
			tissueSite = input.readUTF();
			tissueSide = input.readUTF();
			pathologicalStatus = input.readUTF();
			creationDate = input.readObject() as Date;
			quantity = input.readDouble();
			concentration = input.readDouble();
			storage = input.readUTF();
			comment = input.readUTF();
			exIdColl = input.readObject() as ArrayCollection;
			biohazardColl = input.readObject() as ArrayCollection;
			collectionEvent = input.readObject() as EventBean;
			receivedEvent = input.readObject() as EventBean;
			derivedSpColl = input.readObject() as ArrayCollection;
       }
       
		public function calcUnit() : void 
       	{
       		if (specimenClass == null || specimenType == null)
			{
				return;
			}		
			
			if (specimenClass==Constants.CELL)
			{
				unit = Constants.UNIT_CC;
			}
			else if (specimenClass==Constants.FLUID)
			{
				unit = Constants.UNIT_ML;
			}
			else if (specimenClass == Constants.MOLECULAR)
			{
				unit = Constants.UNIT_MG;
			}
			else if (specimenClass == Constants.TISSUE)
			{
				
				if (specimenType == Constants.FIXED_TISSUE_BLOCK  || 
					specimenType == Constants.FROZEN_TISSUE_BLOCK || 
					specimenType == Constants.FIXED_TISSUE_SLIDE  ||
					specimenType == Constants.FROZEN_TISSUE_SLIDE || 
					specimenType == Constants.NOT_SPECIFIED)
				{
					unit = Constants.UNIT_CN;
				}
				else if (specimenType == Constants.MICRODISSECTED)
				{
					unit = Constants.UNIT_CL;
				}
				else
				{
					unit = Constants.UNIT_GM;
				}
			}
		}
		public function deepCopy() :SpecimenData
		{
			var spData:SpecimenData = new SpecimenData();
			spData.specimenLabel = this.specimenLabel;
			spData.specimenBarcode = this.specimenBarcode;
			return spData;
		}
		
		public function validate(isNewSpecimen:Boolean = true):Boolean
		{
			var specimenValidator:SpecimenValidator  = new SpecimenValidator();
			var isValid:Boolean = true;
			if(isNewSpecimen && mode == MSPParameter.MODE_PARAM_VAL_ADD && this.parentName == "")
			{
				if(this.parentType == "New_Specimen")
					parentNameErrStr = "Please enter Specimen Collection Group Name";
				else
					parentNameErrStr = "Please enter parent specimen label";	
						
				isValid = false && isValid;
			}

			else
			{
				this.parentNameErrStr = null;
				isValid = true && isValid;	
			}
			if(this.specimenLabel == "")
			{
				this.specimenLabelErrStr = "Please enter Label";
				isValid = false && isValid;
			} 
			else
			{
				this.specimenLabelErrStr = null;
				isValid = true && isValid;
			}
					
			if(this.specimenClass == "" || this.specimenClass == Constants.SELECT)
			{
				this.specimenClassErrStr = "Please select specimen class";
				isValid = false && isValid;
			}	
			else
			{
				this.specimenClassErrStr = null;
				isValid = true && isValid;
				 	
			}	
			if(this.specimenType == "" || this.specimenType == Constants.SELECT)
			{

				this.specimenTypeErrStr = "Please select specimen type";
				isValid = false && isValid;
			}	
			else
			{
				this.specimenTypeErrStr = null;
				isValid = true && isValid;
				 	
			}
			if(isNewSpecimen && (this.tissueSite == "" || this.tissueSite == Constants.SELECT))
			{
				this.tissueSiteErrStr = "Please select tissue site";
				isValid = false && isValid;
			}	
			else
			{
				this.tissueSiteErrStr = null;
				isValid = true && isValid;
				 	
			}
			if(isNewSpecimen && (this.tissueSide == "" || this.tissueSide == Constants.SELECT))
			{
				this.tissueSideErrStr = "Please select tissue side";
				isValid = false && isValid;
			}	
			else
			{
				this.tissueSideErrStr = null;
				isValid = true && isValid;
				 	
			}
			if(isNewSpecimen && (this.pathologicalStatus == "" || this.pathologicalStatus == Constants.SELECT))
			{
				this.pathologicalStatusErrStr = "Please select pathological status";
				isValid = false && isValid;
			}	
			else
			{
				this.pathologicalStatusErrStr = null;
				isValid = true && isValid;
				 	
			}
			if(this.quantity.toString() == "")
			{
				this.quantityErrStr = "Please enter quantity";
				isValid = false && isValid;
			}	
			else
			{
						
				if(this.specimenClass == Constants.CELL || 
					(this.specimenClass == Constants.TISSUE && 
						( this.specimenType == Constants.FIXED_TISSUE_BLOCK || this.specimenType == Constants.FIXED_TISSUE_SLIDE || 
						this.specimenType == Constants.FROZEN_TISSUE_BLOCK || this.specimenType == Constants.FROZEN_TISSUE_SLIDE || 
						this.specimenType == Constants.MICRODISSECTED || this.specimenType == Constants.NOT_SPECIFIED)))
				{
					if(this.quantity.toString().indexOf(".") != -1)
					{
						this.quantityErrStr = "Please enter valid quantity";
						isValid = false && isValid;
					}
					else
					{
						this.quantityErrStr = null;
						isValid = true && isValid;

					}
				}		
				else
				{														
					this.quantityErrStr = null;
					isValid = true && isValid;
				}
				 	
			}
			var i:int=0;
			if(this.exIdColl != null && this.exIdColl.length !=0)
			{
				for(i=0;i<this.exIdColl.length;i++)
				{

					var exIdBean:ExternalIdentifierBean = exIdColl.getItemAt(i) as ExternalIdentifierBean;
					isValid = exIdBean.validate() && isValid;
				}
			}
			
			if(this.biohazardColl != null && this.biohazardColl.length !=0)
			{
				for(i=0;i<this.biohazardColl.length;i++)
				{
					var biohazardBean:BiohazardBean = biohazardColl.getItemAt(i) as BiohazardBean;
					isValid = biohazardBean.validate() && isValid;
				}
			}
			if(mode == MSPParameter.MODE_PARAM_VAL_ADD&&isNewSpecimen &&this.collectionEvent != null)
			{

				isValid = this.collectionEvent.validate() && isValid;
			}
			if(mode == MSPParameter.MODE_PARAM_VAL_ADD && isNewSpecimen && this.receivedEvent != null)
			{
				isValid = this.receivedEvent.validate() && isValid;
			}
			return isValid;
		}
	}
}