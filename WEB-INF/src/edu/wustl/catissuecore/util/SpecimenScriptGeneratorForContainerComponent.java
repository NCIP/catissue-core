
package edu.wustl.catissuecore.util;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;

public class SpecimenScriptGeneratorForContainerComponent
{

	final static String VAR = "var ";
	final static String FUNCTION_NAME = "function doOnLoad()";
	final static String OPENING_BRACE = "{";
	final static String CLOSING_BRACE = "}";
	final static String NEXT_LINE = "\n";
	final static String COMMA = ",";
	final static String SEMI_COLON = ";";
	final static String EMPTY_SPACE = " ";
	final static String CLASS_NAME = "className";
	final static String EQUALS = "=";
	final static String SPECIMEN_TYPE = "specimenType";
	final static String CPID = "collectionProtocolId";
	final static String CONTAINER_NAME = "containerName";
	//final String URL = "CatissueCommonAjaxAction.do?type=getStorageContainerList&<%=Constants.CAN_HOLD_SPECIMEN_CLASS%>={0}&specimenType={1}"
		//	+ "&<%=Constants.CAN_HOLD_COLLECTION_PROTOCOL%>={2}";

	public static String generateScript(ViewSpecimenSummaryForm summaryForm,
			String collectionProtocolId)
	{
		String cpId = "";
		if (collectionProtocolId != null && !collectionProtocolId.equals(0))
		{
			cpId = collectionProtocolId.toString();
		}
		StringBuffer scBuffer = new StringBuffer();
		scBuffer.append(FUNCTION_NAME);
		scBuffer.append(NEXT_LINE);
		scBuffer.append(OPENING_BRACE);
		scBuffer.append(NEXT_LINE);
		/*scBuffer.append(VAR);
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
		scBuffer.append(NEXT_LINE);*/

		List<GenericSpecimen> specimenList = summaryForm.getSpecimenList();
		List<GenericSpecimen> aliquotList = summaryForm.getAliquotList();
		List<GenericSpecimen> derivativeList = summaryForm.getDerivedList();

		intialiseComponentForContainer(scBuffer, specimenList,cpId);
		intialiseComponentForContainer(scBuffer, aliquotList,cpId);
		intialiseComponentForContainer(scBuffer, derivativeList,cpId);

		scBuffer.append(CLOSING_BRACE);
		return scBuffer.toString();

	}

	private static void intialiseComponentForContainer(StringBuffer scBuffer, List<GenericSpecimen> specimenList,String cpId)
	{
		if(specimenList!=null)
		{
			Iterator<GenericSpecimen> iter=specimenList.iterator();
			while(iter.hasNext())
			{
				GenericSpecimen specimen=iter.next();
				String specimenSuffix = getSpecimenSuffix(specimen);
				if(specimen.getCollectionStatus()== null || "null".equals(specimen.getCollectionStatus()) || "Pending".equals(specimen.getCollectionStatus()))
				{
					StringBuffer storageContainerGrid=new StringBuffer("storageContainerGrid_").append(specimenSuffix);
					StringBuffer storageContainer=new StringBuffer("storageContainer_").append(specimenSuffix);
					StringBuffer storageContainerDropDown=new StringBuffer("storageContainerDropDown_").append(specimenSuffix);
					StringBuffer storageContainerPagingArea=new StringBuffer("storageContainerPagingArea_").append(specimenSuffix);
					StringBuffer storageContainerInfoArea=new StringBuffer("storageContainerInfoArea_").append(specimenSuffix);
					StringBuffer containerDropDownInfo=new StringBuffer("containerDropDownInfo_").append(specimenSuffix);
					StringBuffer scGrid=new StringBuffer("scGrid_").append(specimenSuffix);
					StringBuffer scGridVisible=new StringBuffer("scGridVisible_").append(specimenSuffix);
					StringBuffer selectedContainerName=new StringBuffer("selectedContainerName_").append(specimenSuffix);

					//StringBuffer scBuffer=new StringBuffer();
					
					
					scBuffer.append(VAR).append(EMPTY_SPACE);
					StringBuffer variableClassName=new StringBuffer(CLASS_NAME).append(specimenSuffix);
					StringBuffer valueClassName=new StringBuffer("document.getElementById(\"className_").append(specimenSuffix).append("\").value;");
					scBuffer.append(variableClassName);
					scBuffer.append(EQUALS).append(valueClassName);
					scBuffer.append(NEXT_LINE);

					scBuffer.append(VAR).append(EMPTY_SPACE);
					StringBuffer variableSpecimenType=new StringBuffer(SPECIMEN_TYPE).append(specimenSuffix);
					StringBuffer valueSpecimenType=new StringBuffer("document.getElementById(\"type_").append(specimenSuffix).append("\").value;");
					scBuffer.append(variableSpecimenType);
					scBuffer.append(EQUALS).append(valueSpecimenType);
					scBuffer.append(NEXT_LINE);

					final Long collectionProtocolId = specimen.getCollectionProtocolId();
					scBuffer.append(VAR).append(EMPTY_SPACE);
					StringBuffer variableCollectionProtocol=new StringBuffer(CPID);
					StringBuffer valueCollectionProtocol=new StringBuffer(AppUtility.getFormattedValue(collectionProtocolId.toString()));
					scBuffer.append(variableCollectionProtocol);
					scBuffer.append(EQUALS).append("\"").append(valueCollectionProtocol).append("\"").append(";");
					scBuffer.append(NEXT_LINE);


					scBuffer.append(VAR).append(EMPTY_SPACE);
					StringBuffer variableContainerName=new StringBuffer(CONTAINER_NAME);
					StringBuffer valueContainerName=new StringBuffer("document.getElementById(\"storageContainerDropDown_"+specimenSuffix+"\").value;");
					scBuffer.append(variableContainerName);
					scBuffer.append(EQUALS).append(valueContainerName);
					scBuffer.append(NEXT_LINE);

					//Object params[]=new Object[]{variableClassName,variableSpecimenType,variableCollectionProtocol};

					//scBuffer.append(VAR).append(EMPTY_SPACE).append("url").append(specimenSuffix).append(EQUALS).append(MessageFormat.format(URL, params));
					scBuffer.append(VAR).append(EMPTY_SPACE).append("url").append(EQUALS).append("\"CatissueCommonAjaxAction.do?type=getStorageContainerList&")
					.append(Constants.CAN_HOLD_SPECIMEN_CLASS).append(EQUALS).append("\"+")
					.append(variableClassName).append("+\"").append("&specimenType=").append("\"+").append(variableSpecimenType).append("+\"")
					.append("&").append(Constants.CAN_HOLD_COLLECTION_PROTOCOL).append(EQUALS).append(valueCollectionProtocol).append("\";");


					//if(specimen.get)

					scBuffer.append(NEXT_LINE);
					scBuffer.append("//Drop Down components information");
					scBuffer.append(NEXT_LINE);
					scBuffer.append(containerDropDownInfo +"= {\ngridObj:\""+storageContainerGrid+"\", \ngridDiv:\""+storageContainer+"\"" +
							",\n dropDownId:\""+storageContainerDropDown+"\"" +
							",\npagingArea:\""+storageContainerPagingArea+"\"," +
							" \ninfoArea:\""+storageContainerInfoArea+"\"" +
							",\n onOptionSelect:" +
							"\nfunction (id,ind)"
							+"\n{"
							+"\n\tdocument.getElementsByName('"+selectedContainerName+"')[0].value ="+ valueContainerName
							+"\n\tdocument.getElementById("+containerDropDownInfo+"['dropDownId']).value = "+scGrid+".cellById(id,ind).getValue();"
							+"\n\thideGrid("+containerDropDownInfo+"['gridDiv']);"
							+"\n\tdocument.getElementById(\"positionDimensionOne_"+specimenSuffix+"\").value=\"\";"
							+"\n\tdocument.getElementById(\"positionDimensionTwo_"+specimenSuffix+"\").value=\"\";\n\t"
							+scGridVisible+"= false;"
							+"\n}"
							+"\n, actionToDo:url, callBackAction:"
							+"\nfunction()" 
							+"\n{"
							+	"\n\tvar containerName= document.getElementsByName('"+selectedContainerName+"')[0].value;"
							+	"\n\tif(containerName != \"\" && containerName != 0 && containerName != null)"
							+	"\n\t{"
							+		"\n\tdocument.getElementsByName('"+selectedContainerName+"')[0].value ="+ valueContainerName
							+		"\n\tdocument.getElementById("+containerDropDownInfo+"['dropDownId']).value = "+scGrid+".cellById(id,ind).getValue();"
							+		"\n\thideGrid("+containerDropDownInfo+"['gridDiv']);\n\t"
							+		scGridVisible+"= false;"
							+	"\n}" 
							+"\n}"
							+"\n, visibilityStatusVariable:"+scGridVisible+", propertyId:'selectedContainerName_"+specimenSuffix+"'};");
					scBuffer.append(NEXT_LINE);
					scBuffer.append("// initialising grid");
					scBuffer.append(NEXT_LINE);
					scBuffer.append(scGrid+"= initDropDownGridForSummary("+containerDropDownInfo+",false);"); 
					scBuffer.append(NEXT_LINE);
					scBuffer.append(NEXT_LINE);
				}
			}
		}
	}

	private static String getSpecimenSuffix(GenericSpecimen specimen)
	{
		String lineage = AppUtility.getLineageSubString(specimen);
		String idToAppend = String.valueOf((long) specimen.getId());
		String specimenIdSuffix = null;
		if (-1 == Long.valueOf(specimen.getId()))
		{
			idToAppend =specimen.getUniqueIdentifier().toString();
			specimenIdSuffix = idToAppend;
		}
		else
		{
			specimenIdSuffix = lineage + "_" + specimen.getId();
		}
		return specimenIdSuffix;
	}

	public static String generateVariableNames(ViewSpecimenSummaryForm summaryForm)
	{
		StringBuffer scBuffer = new StringBuffer();
		List<GenericSpecimen> specimenList = summaryForm.getSpecimenList();
		List<GenericSpecimen> aliquotList = summaryForm.getAliquotList();
		List<GenericSpecimen> derivativeList = summaryForm.getDerivedList();
		declareVariables(scBuffer, specimenList);
		declareVariables(scBuffer, aliquotList);
		declareVariables(scBuffer, derivativeList);
		return scBuffer.toString();

	}

	private static void declareVariables(StringBuffer scBuffer, List<GenericSpecimen> specimenList)
	{
		if (specimenList != null)
		{
			Iterator<GenericSpecimen> iter = specimenList.iterator();
			while (iter.hasNext())
			{
				GenericSpecimen specimen = iter.next();

				String specimenControlSuffix = getSpecimenSuffix(specimen);
				StringBuffer containerDropDownInfo = new StringBuffer("containerDropDownInfo_")
						.append(specimenControlSuffix);
				StringBuffer scGrid = new StringBuffer("scGrid_").append(specimenControlSuffix);
				StringBuffer scGridVisible = new StringBuffer("scGridVisible_").append(
						specimenControlSuffix).append("=false");
				StringBuffer gridDivObject = new StringBuffer("gridDivObject_")
						.append(specimenControlSuffix);

				scBuffer.append(VAR).append(EMPTY_SPACE);
				scBuffer.append(containerDropDownInfo).append(COMMA).append(scGrid)
						.append(SEMI_COLON);
				scBuffer.append(NEXT_LINE);
				scBuffer.append(VAR).append(EMPTY_SPACE);
				scBuffer.append(scGridVisible).append(SEMI_COLON);
				scBuffer.append(NEXT_LINE);
				scBuffer.append(VAR).append(EMPTY_SPACE);
				scBuffer.append(gridDivObject).append(SEMI_COLON);
				scBuffer.append(NEXT_LINE);

			}
		}
	}

	public static void setContainerValues(List<GenericSpecimen> specimenList)
	{
		if (specimenList != null)
		{
			Iterator<GenericSpecimen> iter = specimenList.iterator();
			while (iter.hasNext())
			{
				GenericSpecimen specimen = iter.next();
				String specimenType = "S_";
				String specimenIdSuffix = specimenType + specimen.getId();
				StringBuffer containerDropDownInfo = new StringBuffer("containerDropDownInfo_")
						.append(specimenIdSuffix);
				StringBuffer positionDimensionOne = new StringBuffer("positionDimensionOne_")
						.append(specimenIdSuffix);
				StringBuffer positionDimensionTwo = new StringBuffer("positionDimensionTwo_")
						.append(specimenIdSuffix);

				if (null != specimen.getSelectedContainerName()
						&& !"".equalsIgnoreCase(specimen.getSelectedContainerName()))
				{
					StringBuffer stringBuffer = new StringBuffer();
					stringBuffer.append("document.getElementById(").append(containerDropDownInfo)
							.append("['dropDownId']).value=")
							.append(specimen.getSelectedContainerName()).append(SEMI_COLON);
					stringBuffer.append(NEXT_LINE);
					stringBuffer.append("document.getElementById(").append(positionDimensionOne)
							.append(".value=").append(specimen.getPositionDimensionOne())
							.append(SEMI_COLON);
					stringBuffer.append(NEXT_LINE);
					stringBuffer.append("document.getElementById(").append(positionDimensionTwo)
							.append(".value=").append(specimen.getPositionDimensionTwo())
							.append(SEMI_COLON);
					stringBuffer.append(NEXT_LINE);
				}
			}
		}
	}

}
