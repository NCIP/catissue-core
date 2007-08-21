package valueobjects
{
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	import flash.utils.IExternalizable;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	
	import util.Constants;
	
	[Bindable]
	[RemoteClass(alias="edu.wustl.catissuecore.flex.SpecimenBean")]
	public class SpecimenData implements IExternalizable
	{
		public var specimenTypePVList:ArrayCollection;	
		public var isSelected:Boolean;		
		public var specimenParent:String;
		
		public var spID:Number = -3;
		public var scgName:String = '';
		public var parentSpecimenName:String = '';
		
		public var specimenLabel:String = '';
		public var specimenBarcode:String = '';
		
		public var specimenClass:String = '';
		public var specimenType:String = '';
		
		public var tissueSite:String = '';
		public var tissueSide:String = '';
		public var pathologicalStatus:String = '';
		
		public var creationDate:Date;
		public var quantity:Number;
		public var concentration:Number;
		
		public var comment:String;
		public var unit:String;
		
		public var exIdColl:ArrayCollection = new ArrayCollection();
		
		public function SpecimenData(specimenLabel:String="",specimenBarcode:String="",tissueSide:String="")
		{
			this.specimenParent = 'SCG';
			
			this.scgName = '';
			this.parentSpecimenName = ''
			
			this.specimenLabel = specimenLabel;
			this.specimenBarcode = specimenBarcode;
			
			this.specimenClass = 'Fluid';
			this.specimenType = 'Not Specified';
			
			this.tissueSite = 'Not Specified'
			this.tissueSide = 'Not Specified';
			this.pathologicalStatus = 'Not Specified';
			
			this.creationDate =  new Date();
			this.quantity = new Number();
			this.concentration = new Number();
			
			this.comment = '';
			this.unit = '';	
			
			var exBean:ExternalIdentifierBean = new ExternalIdentifierBean(false,0,'A','B');
			exIdColl.addItem(exBean);
		}
		
		public function copy(spData:SpecimenData):void
		{
			this.specimenTypePVList = spData.specimenTypePVList;
			this.specimenParent = spData.specimenParent;
			
			this.scgName = spData.scgName;
			this.parentSpecimenName = spData.parentSpecimenName;
			
			this.specimenLabel = spData.specimenLabel;
			this.specimenBarcode = spData.specimenBarcode;
			
			this.specimenClass = spData.specimenClass;
			this.specimenType = spData.specimenType;
			
			this.tissueSite = spData.tissueSite;
			this.tissueSide = spData.tissueSide;
			this.pathologicalStatus = spData.pathologicalStatus;
			
			this.creationDate = spData.creationDate;
			this.quantity = spData.quantity;
			this.concentration = spData.concentration;
			
			this.comment = spData.comment;
			this.unit = spData.unit;
			copyExId(spData.exIdColl);
			//this.exIdColl = spData.exIdColl;
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
		
		public function writeExternal(output:IDataOutput) :void 
		{
			Alert.show("CLIENT IN writeExternal");
			output.writeInt(spID);
			output.writeUTF(scgName);
			output.writeUTF(parentSpecimenName);
			output.writeUTF(specimenLabel);
			output.writeUTF(specimenBarcode);
			output.writeUTF(specimenClass);
			output.writeUTF(specimenType);
			output.writeUTF(tissueSite);
			output.writeUTF(tissueSide);
			output.writeUTF(pathologicalStatus);
			output.writeObject(creationDate);
			output.writeDouble(quantity);
			output.writeDouble(concentration);
			output.writeUTF(comment);
			output.writeObject(exIdColl);								
    	}
        
    	public function readExternal(input:IDataInput):void
    	{
    		//Alert.show("CLIENT IN readExternal");
    		
			spID = input.readInt();
			scgName = input.readUTF();
			parentSpecimenName = input.readUTF();
			specimenLabel = input.readUTF();
			specimenBarcode = input.readUTF();
			specimenClass = input.readUTF();
			specimenType = input.readUTF();
			tissueSite = input.readUTF();
			tissueSide = input.readUTF();
			pathologicalStatus = input.readUTF();
			creationDate = input.readObject() as Date;
			quantity = input.readDouble();
			concentration = input.readDouble();
			comment = input.readUTF();
			exIdColl = input.readObject() as ArrayCollection;
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
	}
}