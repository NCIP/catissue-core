package edu.wustl.catissuecore.util;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.util.global.Constants;


public class SpecimenScriptGeneratorForContainerComponent
{
	final static String VAR="var ";
	final String FUNCTION_NAME="function doOnLoad()";
	final static String OPENING_BRACE="{";
	final static String CLOSING_BRACE="}";
	final static String NEXT_LINE="\n";
	final static String COMMA=",";
	final static String SEMI_COLON=";";
	final static String EMPTY_SPACE=" ";
	final static String CLASS_NAME="className";
	final static String EQUALS="=";
	final static String SPECIMEN_TYPE="specimenType";
	final static String CPID="collectionProtocolId";
	final static String CONTAINER_NAME="containerName";
	final String URL="CatissueCommonAjaxAction.do?type=getStorageContainerList&<%=Constants.CAN_HOLD_SPECIMEN_CLASS%>={0}&specimenType={1}" +
			"&<%=Constants.CAN_HOLD_COLLECTION_PROTOCOL%>={2}";
	

	public StringBuffer generateScript(ViewSpecimenSummaryForm summaryForm, Integer collectionProtocolId)
	{
		String cpId="";
		if(collectionProtocolId!=null && !collectionProtocolId.equals(0))
		{
			cpId=collectionProtocolId.toString();
		}
		StringBuffer scBuffer= new StringBuffer();
		scBuffer.append(FUNCTION_NAME);
		scBuffer.append(NEXT_LINE);
		scBuffer.append(OPENING_BRACE);
		scBuffer.append(NEXT_LINE);
		scBuffer.append(VAR);
		scBuffer.append("className=document.getElementById(\"className\").value").append(SEMI_COLON);
		scBuffer.append(NEXT_LINE);
		scBuffer.append(VAR);
		scBuffer.append("sptype=document.getElementById(\"type\").value").append(SEMI_COLON);
		scBuffer.append(NEXT_LINE);
		scBuffer.append(VAR);
		scBuffer.append(" collectionProtocolId="+collectionProtocolId).append(SEMI_COLON);
		scBuffer.append(NEXT_LINE);
		scBuffer.append(VAR);
		scBuffer.append("url=\"CatissueCommonAjaxAction.do?type=getStorageContainerList&"+Constants.CAN_HOLD_SPECIMEN_CLASS+"="
				+"className&specimenType=sptype&"+Constants.CAN_HOLD_COLLECTION_PROTOCOL+"="+cpId+"\"").append(SEMI_COLON);
		scBuffer.append(NEXT_LINE);

		List<GenericSpecimen> specimenList=summaryForm.getSpecimenList();
		List<GenericSpecimen> aliquotList=summaryForm.getAliquotList();
		List<GenericSpecimen> derivativeList=summaryForm.getAliquotList();

		intialiseComponentForContainer(scBuffer, specimenList);
		intialiseComponentForContainer(scBuffer, aliquotList);
		intialiseComponentForContainer(scBuffer, derivativeList);

		scBuffer.append(CLOSING_BRACE);
		return scBuffer;

	}

	private void intialiseComponentForContainer(StringBuffer scBuffer, List<GenericSpecimen> specimenList)
	{
		if(specimenList!=null)
		{
			Iterator<GenericSpecimen> iter=specimenList.iterator();
			while(iter.hasNext())
			{
				GenericSpecimen specimen=iter.next();
				String specimenId=String.valueOf(specimen.getId());
				
				String specimenType="S_";
				String specimenIdSuffix = specimenType+specimen.getId();
				
				StringBuffer storageContainerGrid=new StringBuffer("selectedContainerName_").append(specimenIdSuffix);
				StringBuffer storageContainer=new StringBuffer("storageContainer_").append(specimenIdSuffix);
				StringBuffer storageContainerDropDown=new StringBuffer("storageContainerDropDown_").append(specimenIdSuffix);
				StringBuffer storageContainerPagingArea=new StringBuffer("storageContainerPagingArea_").append(specimenIdSuffix);
				StringBuffer storageContainerInfoArea=new StringBuffer("storageContainerInfoArea_").append(specimenIdSuffix);
				StringBuffer containerDropDownInfo=new StringBuffer("containerDropDownInfo_").append(specimenIdSuffix);
				StringBuffer scGrid=new StringBuffer("scGrid_").append(specimenIdSuffix);
				StringBuffer scGridVisible=new StringBuffer("scGridVisible_").append(specimenIdSuffix);
				StringBuffer selectedContainerName=new StringBuffer("selectedContainerName_").append(specimenIdSuffix);
				
				
				StringBuffer variableDec=new StringBuffer();
				variableDec.append(VAR).append(EMPTY_SPACE);
				StringBuffer variableClassName=new StringBuffer(CLASS_NAME).append(specimenId);
				StringBuffer valueClassName=new StringBuffer("document.getElementById(\"className_S_").append(specimenId).append(").value;");
				variableDec.append(EQUALS).append(valueClassName);
				variableDec.append(NEXT_LINE);
				
				variableDec.append(VAR).append(EMPTY_SPACE);
				StringBuffer variableSpecimenType=new StringBuffer(SPECIMEN_TYPE).append(specimenId);
				StringBuffer valueSpecimenType=new StringBuffer("document.getElementById(\"className_S_").append(specimenId).append(").value;");
				variableDec.append(EQUALS).append(valueSpecimenType);
				variableDec.append(NEXT_LINE);
				
				variableDec.append(VAR).append(EMPTY_SPACE);
				StringBuffer variableCollectionProtocol=new StringBuffer(CPID).append(specimenId);
				StringBuffer valueCollectionProtocol=new StringBuffer("<%=collectionProtocolId%>");
				variableDec.append(EQUALS).append(valueCollectionProtocol);
				variableDec.append(NEXT_LINE);
						
						
				variableDec.append(VAR).append(EMPTY_SPACE);
				StringBuffer variableContainerName=new StringBuffer(CONTAINER_NAME);
				StringBuffer valueContainerName=new StringBuffer("document.getElementById(\""+storageContainerDropDown+"\").value;");
				variableDec.append(EQUALS).append(valueContainerName);
				variableDec.append(NEXT_LINE);
				
				Object params[]=new Object[]{variableClassName,variableSpecimenType,variableCollectionProtocol};
				
				variableDec.append(VAR).append(EMPTY_SPACE).append("url").append(specimenId).append(EQUALS).append(MessageFormat.format(URL, params));
				
					
				//if(specimen.get)

				scBuffer.append(NEXT_LINE);
				scBuffer.append("//Drop Down components information");
				scBuffer.append(NEXT_LINE);
				scBuffer.append(containerDropDownInfo +"= {gridObj:\""+storageContainerGrid+"\", gridDiv:\""+storageContainer+"\"" +
						", dropDownId:\""+storageContainerDropDown+"\"" +
						", pagingArea:\""+storageContainerPagingArea+"\"," +
						" infoArea:\""+storageContainerInfoArea+"\"" +
						", onOptionSelect:" +
						"function (id,ind)"
						+"{"
							+"document.getElementsByName('"+selectedContainerName+"')[0].value ="+ valueContainerName+";"
							+"document.getElementById("+containerDropDownInfo+"['dropDownId']).value = "+scGrid+".cellById(id,ind).getValue()"
							+"hideGrid("+containerDropDownInfo+"['gridDiv']);"
							+scGridVisible+"= false;"
							+"}"
							+", actionToDo:url, callBackAction:"
							+"function()" 
							+"{"
							+	"var containerName= document.getElementsByName('"+selectedContainerName+"')[0].value;"
							+	"if(containerName != \"\" && containerName != 0 && containerName != null)"
							+	"{"
							+		"document.getElementsByName('"+selectedContainerName+"')[0].value ="+ valueContainerName+";"
							+		"document.getElementById("+containerDropDownInfo+"['dropDownId']).value = "+scGrid+".cellById(id,ind).getValue()"
							+		"hideGrid("+containerDropDownInfo+"['gridDiv']);"
							+		scGridVisible+"= false;"
							+	"}" 
							+"}"
							+", visibilityStatusVariable:"+scGridVisible+"};");
				scBuffer.append(NEXT_LINE);
				scBuffer.append("// initialising grid");
				scBuffer.append(scGrid+"= initDropDownGrid("+containerDropDownInfo+",5,0);"); 
			}
		}
	}
	

	public StringBuffer generateVariableNames(ViewSpecimenSummaryForm summaryForm)
	{
		StringBuffer scBuffer=new StringBuffer();
		List<GenericSpecimen> specimenList=summaryForm.getSpecimenList();
		List<GenericSpecimen> aliquotList=summaryForm.getAliquotList();
		List<GenericSpecimen> derivativeList=summaryForm.getAliquotList();
		declareVariables(scBuffer, specimenList);
		declareVariables(scBuffer, aliquotList);
		declareVariables(scBuffer, derivativeList);
		return scBuffer;

	}

	private void declareVariables(StringBuffer scBuffer, List<GenericSpecimen> specimenList)
	{
		if(specimenList!=null)
		{
			Iterator<GenericSpecimen> iter=specimenList.iterator();
			while(iter.hasNext())
			{
				GenericSpecimen specimen=iter.next();

				String specimenType="S_";
				String specimenIdSuffix=specimenType+specimen.getId();
				StringBuffer containerDropDownInfo=new StringBuffer("containerDropDownInfo_").append(specimenIdSuffix);
				StringBuffer scGrid=new StringBuffer("scGrid_").append(specimenIdSuffix);
				StringBuffer scGridVisible=new StringBuffer("scGridVisible_").append(specimenIdSuffix).append("=false");
				StringBuffer gridDivObject=new StringBuffer("gridDivObject_").append(specimenIdSuffix);
				
				scBuffer.append(VAR);
				scBuffer.append(containerDropDownInfo).append(COMMA).append(scGrid).append(SEMI_COLON);
				scBuffer.append(NEXT_LINE);
				scBuffer.append(VAR);
				scBuffer.append(scGridVisible).append(SEMI_COLON);
				scBuffer.append(NEXT_LINE);
				scBuffer.append(VAR);
				scBuffer.append(gridDivObject).append(SEMI_COLON);
				scBuffer.append(NEXT_LINE);

			}
		}
	}
	
	public static void setContainerValues(List<GenericSpecimen> specimenList)
	{
		if(specimenList!=null)
		{
			Iterator<GenericSpecimen> iter=specimenList.iterator();
			while(iter.hasNext())
			{
				GenericSpecimen specimen=iter.next();
				String specimenType="S_";
				String specimenIdSuffix=specimenType+specimen.getId();
				StringBuffer containerDropDownInfo=new StringBuffer("containerDropDownInfo_").append(specimenIdSuffix);
				StringBuffer positionDimensionOne=new StringBuffer("positionDimensionOne_").append(specimenIdSuffix);
				StringBuffer positionDimensionTwo=new StringBuffer("positionDimensionTwo_").append(specimenIdSuffix);
				
				if(null!=specimen.getSelectedContainerName() && !"".equalsIgnoreCase(specimen.getSelectedContainerName())) 
				{
					StringBuffer stringBuffer=new StringBuffer();
					stringBuffer.append("document.getElementById(").append(containerDropDownInfo).append("['dropDownId']).value=")
						.append(specimen.getSelectedContainerName()).append(SEMI_COLON);
					stringBuffer.append(NEXT_LINE);
					stringBuffer.append("document.getElementById(").append(positionDimensionOne).append(".value=").append(specimen.getPositionDimensionOne()).append(SEMI_COLON);
					stringBuffer.append(NEXT_LINE);
					stringBuffer.append("document.getElementById(").append(positionDimensionTwo).append(".value=").append(specimen.getPositionDimensionTwo()).append(SEMI_COLON);
					stringBuffer.append(NEXT_LINE);
				}
			}
		}
	}
	

}
