package gov.nih.nci.codegen.util;

import gov.nih.nci.codegen.GenerationException;
import gov.nih.nci.codegen.validator.ValidatorAttribute;
import gov.nih.nci.codegen.validator.ValidatorClass;
import gov.nih.nci.codegen.validator.ValidatorModel;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociation;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociationEnd;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAttribute;
import gov.nih.nci.ncicb.xmiinout.domain.UMLClass;
import gov.nih.nci.ncicb.xmiinout.domain.UMLDatatype;
import gov.nih.nci.ncicb.xmiinout.domain.UMLDependency;
import gov.nih.nci.ncicb.xmiinout.domain.UMLGeneralization;
import gov.nih.nci.ncicb.xmiinout.domain.UMLInterface;
import gov.nih.nci.ncicb.xmiinout.domain.UMLModel;
import gov.nih.nci.ncicb.xmiinout.domain.UMLPackage;
import gov.nih.nci.ncicb.xmiinout.domain.UMLTaggableElement;
import gov.nih.nci.ncicb.xmiinout.domain.UMLTaggedValue;
import gov.nih.nci.ncicb.xmiinout.util.ModelUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class TransformerUtils
{
	private static Logger log = Logger.getLogger(TransformerUtils.class);
	private String BASE_PKG_LOGICAL_MODEL;
	private String BASE_PKG_DATA_MODEL;
	private String INCLUDE_PACKAGE;
	private String EXCLUDE_PACKAGE;
	private String EXCLUDE_NAME;
	private String EXCLUDE_NAMESPACE;
	private String IDENTITY_GENERATOR_TAG;
	private Set<String> INCLUDE_PACKAGE_PATTERNS = new HashSet<String>();
	private Set<String> EXCLUDE_PACKAGE_PATTERNS = new HashSet<String>();
	private Set<String> EXCLUDE_CLASS_PATTERNS = new HashSet<String>();
	private Set<String> EXCLUDE_NAMESPACE_PATTERNS = new HashSet<String>();
	private String DATABASE_TYPE;
	private Map<String,String> CASCADE_STYLES = new HashMap<String,String>();
	private ValidatorModel vModel;
	private ValidatorModel vModelExtension;
	
	private static final String TV_ID_ATTR_COLUMN = "id-attribute";
	private static final String TV_MAPPED_ATTR_COLUMN = "mapped-attributes";
	private static final String TV_ASSOC_COLUMN = "implements-association";
	private static final String TV_INVERSE_ASSOC_COLUMN = "inverse-of";
	private static final String TV_DISCR_COLUMN = "discriminator";
	private static final String TV_CORRELATION_TABLE = "correlation-table";
	private static final String TV_DOCUMENTATION = "documentation";
	private static final String TV_DESCRIPTION = "description";
	private static final String TV_LAZY_LOAD = "lazy-load";
	private static final String TV_TYPE="type";
	private static final String TV_MAPPED_COLLECTION_TABLE = "mapped-collection-table";
	private static final String TV_MAPPED_ELEMENT_COLUMN = "mapped-element";
	private static final String TV_CADSR_PUBLICID = "CADSR_ConceptualDomainPublicID";
	private static final String TV_CADSR_VERSION = "CADSR_ConceptualDomainVersion";
	private static final String TV_NCI_CASCADE_ASSOCIATION = "NCI_CASCADE_ASSOCIATION";
	private static final String TV_NCI_EAGER_LOAD = "NCI_EAGER_LOAD";
	public static final String  TV_PK_GENERATOR = "NCI_GENERATOR.";
	public static final String  TV_PK_GENERATOR_PROPERTY = "NCI_GENERATOR_PROPERTY";
	
	//Global Model Exchange (GME) Project Tag Value Constants; see: https://wiki.nci.nih.gov/display/caCORE/GME+Namespace
	public static final String  TV_NCI_GME_XML_NAMESPACE = "NCI_GME_XML_NAMESPACE"; //Used for projects, Packages, Classes
	public static final String  TV_NCI_GME_XML_ELEMENT = "NCI_GME_XML_ELEMENT"; //Used for Classes
	public static final String  TV_NCI_GME_XML_LOC_REF = "NCI_GME_XML_LOC_REF"; //Used for Attributes
	public static final String  TV_NCI_GME_SOURCE_XML_LOC_REF = "NCI_GME_SOURCE_XML_LOC_REF"; //Used for Associations
	public static final String  TV_NCI_GME_TARGET_XML_LOC_REF = "NCI_GME_TARGET_XML_LOC_REF"; //Used for Associations
	
	private static final String STEREO_TYPE_TABLE = "table";
	private static final String STEREO_TYPE_DATASOURCE_DEPENDENCY = "DataSource";
	
	public static final String  PK_GENERATOR_SYSTEMWIDE = "NCI_GENERATOR_SYSTEMWIDE.";

	
	public TransformerUtils(Properties umlModelFileProperties,List cascadeStyles, ValidatorModel vModel, ValidatorModel vModelExtension) {
			BASE_PKG_LOGICAL_MODEL = umlModelFileProperties.getProperty("Logical Model") == null ? "" :umlModelFileProperties.getProperty("Logical Model").trim();
			BASE_PKG_DATA_MODEL = umlModelFileProperties.getProperty("Data Model")==null ? "" : umlModelFileProperties.getProperty("Data Model").trim();
			
			EXCLUDE_PACKAGE = umlModelFileProperties.getProperty("Exclude Package")==null ? "" : umlModelFileProperties.getProperty("Exclude Package").trim();
			INCLUDE_PACKAGE = umlModelFileProperties.getProperty("Include Package")==null ? "" : umlModelFileProperties.getProperty("Include Package").trim();
			EXCLUDE_NAME = umlModelFileProperties.getProperty("Exclude Name")==null ? "" : umlModelFileProperties.getProperty("Exclude Name").trim();
			EXCLUDE_NAMESPACE = umlModelFileProperties.getProperty("Exclude Namespace")==null ? "" : umlModelFileProperties.getProperty("Exclude Namespace").trim();
			
			for(String excludeToken:EXCLUDE_PACKAGE.split(","))
				EXCLUDE_PACKAGE_PATTERNS.add(excludeToken.trim());
			for(String includeToken:INCLUDE_PACKAGE.split(","))
				INCLUDE_PACKAGE_PATTERNS.add(includeToken.trim());
			for(String excludeToken:EXCLUDE_NAME.split(","))
				EXCLUDE_CLASS_PATTERNS.add(excludeToken.trim());
			for(String excludeToken:EXCLUDE_NAMESPACE.split(","))
				EXCLUDE_NAMESPACE_PATTERNS.add(excludeToken.trim());
			
			IDENTITY_GENERATOR_TAG = umlModelFileProperties.getProperty("Identity Generator Tag") == null ? "": umlModelFileProperties.getProperty("Identity Generator Tag").trim();
			DATABASE_TYPE = umlModelFileProperties.getProperty("Database Type") == null ? "": umlModelFileProperties.getProperty("Database Type").trim();
			
			for (Object cascadeStyle : cascadeStyles){
				CASCADE_STYLES.put((String) cascadeStyle, (String)cascadeStyle);
			}
			
			this.vModel = vModel;
			log.debug("ValidatorModel: " + vModel);
			
			this.vModelExtension = vModelExtension;
			log.debug("ValidatorModel Extension: " + vModelExtension);

		}
	
	public String getDatabaseType() {
		return DATABASE_TYPE;
	}
	
	public boolean isIncluded(UMLClass klass) throws GenerationException
	{
		String fqcn = getFQCN(klass);
		
		return isIncluded(fqcn);
	}
	
	public boolean isIncluded(UMLInterface interfaze) throws GenerationException
	{
		String fqcn = getFQCN(interfaze);
		
		return isIncluded(fqcn);
	}
	
	public boolean isIncluded(String fqcn)
	{

		log.debug("isIncluded(String fqcn) for fqcn: "+fqcn);

		for (String excludePkgPattern:EXCLUDE_PACKAGE_PATTERNS)
			if (Pattern.matches(excludePkgPattern, fqcn))
				return false;


		for (String excludeClassPattern:EXCLUDE_CLASS_PATTERNS){
			if (Pattern.matches(excludeClassPattern, fqcn))
				return false;
		}

		for(String includePkgPattern: INCLUDE_PACKAGE_PATTERNS){
			log.debug("includePkgPattern: "+includePkgPattern+"; fqcn: "+fqcn);
			if(Pattern.matches(includePkgPattern, fqcn))
				return true;
		}

		return false;
	}
	
	public boolean isIncluded(UMLPackage pkg) throws GenerationException
	{
		String fullPkgName = getFullPackageName(pkg);
		log.debug("isIncluded(UMLPackage pkg) for fullPkgName: "+fullPkgName);

		for(String excludePkgPattern: EXCLUDE_PACKAGE_PATTERNS)
			if (Pattern.matches(excludePkgPattern, fullPkgName))
				return false;

		for(String includePkgPattern: INCLUDE_PACKAGE_PATTERNS)
			if (Pattern.matches(includePkgPattern, fullPkgName))
				return true;
		
		return true;
	}
	
	public boolean isNamespaceIncluded(UMLClass klass, String defaultNamespacePrefix) throws GenerationException
	{
		String pkgNamespace=null;
		
		try {
			pkgNamespace = getGMENamespace(klass);
		} catch (GenerationException ge) {
			log.error("ERROR: ", ge);
			throw new GenerationException("Error getting the GME Namespace tag value for: " + getFullPackageName(klass.getPackage()), ge);
		}
		
		if (pkgNamespace==null) //use default namespace
			pkgNamespace = defaultNamespacePrefix+getFullPackageName(klass);
		
		log.debug("* * * * * pkgNamespace:"+pkgNamespace);
		
		for(String excludePkgNamespacePattern: EXCLUDE_NAMESPACE_PATTERNS)
			if(Pattern.matches(excludePkgNamespacePattern,pkgNamespace)){
				return false;
			}

		return true;
	}
	
	public String getEmptySpace(Integer count)
	{
		String spaces = "";
		
		for(Integer i=0;i<count;i++)
			spaces +="\t";
		
		return spaces;
	}
	
	public String getFQEN(UMLTaggableElement elt) throws GenerationException {
		if (elt instanceof UMLClass)
			return getFQCN((UMLClass)elt);
		if (elt instanceof UMLPackage)
			return getFullPackageName((UMLPackage)elt);
		
		throw new GenerationException("Error getting fully qualified element name.  Supported taggable element types include UMLClass and UMLPackage; element is neither");
	}
	
	public String getFQCN(UMLClass klass)
	{
		return removeBasePackage(ModelUtil.getFullName(klass));
	}

	public String getFQCN(UMLInterface interfaze)
	{
		return removeBasePackage(ModelUtil.getFullName(interfaze));
	}

	public String getFullPackageName(UMLTaggableElement te)
	{
		if (te instanceof UMLClass)
			return removeBasePackage(ModelUtil.getFullPackageName((UMLClass)te));
		if (te instanceof UMLInterface)
			return removeBasePackage(ModelUtil.getFullPackageName((UMLInterface)te));
		if (te instanceof UMLPackage)
			return removeBasePackage(ModelUtil.getFullPackageName((UMLPackage)te));
		
		return "";
	}

	private String removeBasePackage(String path)
	{
		if(path.startsWith(BASE_PKG_LOGICAL_MODEL+"."))
			return path.substring(BASE_PKG_LOGICAL_MODEL.length()+1);
		else if(path.startsWith(BASE_PKG_DATA_MODEL+"."))
			return path.substring(BASE_PKG_DATA_MODEL.length()+1);
		else
			return path;
	}
	
	public String getBasePkgLogicalModel(){
		return BASE_PKG_LOGICAL_MODEL;
	}

	public UMLClass getSuperClass(UMLClass klass) throws GenerationException
	{
		UMLClass[] superClasses = ModelUtil.getSuperclasses(klass);

		if(superClasses.length == 0) {
			log.debug("*** Getting superclass for class " + klass.getName() + ": " + null);
			return null;
		}

		if(superClasses.length > 1)
			throw new GenerationException("Class can not have more than one super class");
		
		log.debug("*** Getting superclass for class " + klass.getName() + ": " + superClasses[0].getName());
		
		return superClasses[0];
	}

	public String getSuperClassString(UMLClass klass) throws GenerationException
	{
		UMLClass superClass = getSuperClass(klass);
		if(superClass == null) 
			return "";
		else 
			return "extends " + superClass.getName();
	}
	
	public UMLInterface[] getSuperInterface(UMLInterface interfaze) throws GenerationException
	{
		UMLInterface[] superInterfaces = ModelUtil.getSuperInterfaces(interfaze);

		if(superInterfaces.length == 0) {
			log.debug("*** Getting superinterface for interface " + interfaze.getName() + ": " + null);
			return null;
		}

		log.debug("*** Getting superinterface for interface " + interfaze.getName() + ": " + superInterfaces[0].getName());
		
		return superInterfaces;
	}	
	
	public String getSuperInterfaceString(UMLInterface interfaze) throws GenerationException
	{
		String superInterfaceStr = "extends ";
		UMLInterface[] superInterfaces = getSuperInterface(interfaze);
		if(superInterfaces == null) 
			return "";
		else {
			superInterfaceStr += superInterfaces[0].getName();

			for (int i = 1; i < superInterfaces.length; i++){
				superInterfaceStr += ", " + superInterfaces[i].getName();
			}

		}
		return superInterfaceStr;
	}
	
	public UMLInterface[] getInterfaces(UMLClass klass) throws GenerationException
	{
		UMLInterface[] interfaces = ModelUtil.getInterfaces(klass);

		if(interfaces.length == 0) {
			log.debug("*** Getting interface for class " + klass.getName() + ": " + null);
			return null;
		}

		log.debug("*** Getting superclass for class " + klass.getName() + ": " + interfaces[0].getName());
		
		return interfaces;
	}	
	
	public String getInterfaceString(UMLClass klass) throws GenerationException
	{
		UMLInterface[] interfaces = getInterfaces(klass);
		if(interfaces == null) 
			return "";
		else {
			String interfaceStr = "";
			for (UMLInterface interfaze : interfaces){
				interfaceStr += ", " + interfaze.getName();
			}
			return interfaceStr;
		}
	}
	
	public String getInterfaceImports(UMLInterface interfaze) throws GenerationException
	{
		StringBuilder sb = new StringBuilder();
		Set<String> importList = new HashSet<String>();
		UMLInterface[] interfaces = ModelUtil.getSuperInterfaces(interfaze);

		String pkgName = getFullPackageName(interfaze);
		
		for (UMLInterface superInterfaze : interfaces) {
			String superInterfacePkg = getFullPackageName(superInterfaze);
			if (!pkgName.equals(superInterfacePkg))
				importList.add(getFQCN(superInterfaze));
		}
		
		for(String importClass:importList)
			sb.append("import ").append(importClass).append(";\n");

		return sb.toString();
	}
	
	public String getImports(UMLClass klass) throws GenerationException
	{
		StringBuilder sb = new StringBuilder();
		Set<String> importList = new HashSet<String>();
		UMLClass[] superClasses = ModelUtil.getSuperclasses(klass);
		UMLInterface[] interfaces = ModelUtil.getInterfaces(klass);

		if(superClasses.length>1)
			throw new GenerationException("Class can not have more than one super classes");

		String pkgName = getFullPackageName(klass);
		if(superClasses.length == 1)
		{ 
			String superPkg = getFullPackageName(superClasses[0]);
			if(!pkgName.equals(superPkg))
				importList.add(getFQCN(superClasses[0]));
		}
		
		for (UMLInterface interfaze : interfaces) {
			String interfacePkg = getFullPackageName(interfaze);
			if (!pkgName.equals(interfacePkg))
				importList.add(getFQCN(interfaze));
		}
		
		for(UMLAttribute attr: klass.getAttributes())
		{
			if(getDataType(attr).startsWith("Collection") && !importList.contains("java.util.Collection"))
			{
				importList.add("java.util.Collection");
				break;
			}
		}
		for(UMLAssociation association: klass.getAssociations())
		{
			List<UMLAssociationEnd> assocEnds = association.getAssociationEnds();
			UMLAssociationEnd otherEnd = getOtherEnd(klass,assocEnds);
			
			String assocKlass = getFQCN ((UMLClass)otherEnd.getUMLElement());
			if(!pkgName.equals(getFullPackageName ((UMLClass)otherEnd.getUMLElement())) && !importList.contains(assocKlass))
				importList.add(assocKlass);
			if(isAssociationEndMany(otherEnd) && otherEnd.isNavigable()&& !importList.contains("java.util.Collection"))
				importList.add("java.util.Collection");
		}
		
		importList.addAll(getHibernateValidatorConstraintImports(klass));
		
		for(String importClass:importList)
			sb.append("import ").append(importClass).append(";\n");

		return sb.toString();
	}

	public String getDataType(UMLAttribute attr)
	{
		UMLDatatype dataType = attr.getDatatype();
		String name = dataType.getName();
		if(dataType instanceof UMLClass)
			name = getFQCN((UMLClass)dataType);
		
		if(name.startsWith("java.lang."))
			name = name.substring("java.lang.".length());

		if("int".equalsIgnoreCase(name) || "integer".equalsIgnoreCase(name))
			return "Integer";
		if("double".equalsIgnoreCase(name))
			return "Double";
		if("float".equalsIgnoreCase(name))
			return "Float";
		if("long".equalsIgnoreCase(name))
			return "Long";
		if("string".equalsIgnoreCase(name))
			return "String";
		if("char".equalsIgnoreCase(name) || "character".equalsIgnoreCase(name))
			return "Character";
		if("boolean".equalsIgnoreCase(name) )
			return "Boolean";
		if("byte".equalsIgnoreCase(name) )
			return "Byte";
		if("short".equalsIgnoreCase(name) )
			return "Short";

		if("date".equalsIgnoreCase(name) || "java.util.date".equalsIgnoreCase(name))
			return "java.util.Date";

		
		if("collection<int>".equalsIgnoreCase(name) || "collection<integer>".equalsIgnoreCase(name))
			return "Collection<Integer>";
		if("collection<double>".equalsIgnoreCase(name))
			return "Collection<Double>";
		if("collection<float>".equalsIgnoreCase(name))
			return "Collection<Float>";
		if("collection<long>".equalsIgnoreCase(name))
			return "Collection<Long>";
		if("collection<string>".equalsIgnoreCase(name))
			return "Collection<String>";
		if("collection<boolean>".equalsIgnoreCase(name))
			return "Collection<Boolean>";
		if("collection<byte>".equalsIgnoreCase(name))
			return "Collection<Byte>";
		if("collection<short>".equalsIgnoreCase(name))
			return "Collection<Short>";
		if("collection<char>".equalsIgnoreCase(name) || "collection<character>".equalsIgnoreCase(name))
			return "Collection<Character>";
		
		log.error("Unknown data type = "+name);
		
		return name;
	}

	public HashMap<String, String> getPKGeneratorTags(UMLClass table,String fqcn,UMLAttribute classIdAttr) throws GenerationException {
		HashMap<String, String> pkTags = new HashMap<String, String>();
		String pkgenClassKey = TV_PK_GENERATOR + DATABASE_TYPE;

		UMLAttribute tableIdAttribute=getMappedColumn(table,fqcn+"."+classIdAttr.getName());
		Collection<UMLTaggedValue> tableTaggedValues = tableIdAttribute.getTaggedValues();
		String pkGeneratorClass = getTagValue(tableTaggedValues,pkgenClassKey, 1);
		
		if (pkGeneratorClass != null && !("".equals(pkGeneratorClass))) {
			for (int i = 1; i <= tableTaggedValues.size(); i++) {
				String pkgenProp = TV_PK_GENERATOR_PROPERTY + i + "."+ DATABASE_TYPE;
				String pkParam = getTagValue(tableTaggedValues, pkgenProp, 1);
				StringTokenizer tokenizer = new StringTokenizer(pkParam, ":");
				if(tokenizer.hasMoreTokens()){
					pkTags.put(tokenizer.nextToken(), tokenizer.nextToken());
				}
			}
			pkTags.put(pkgenClassKey, pkGeneratorClass);
		} else {
			pkTags.put(PK_GENERATOR_SYSTEMWIDE+DATABASE_TYPE, IDENTITY_GENERATOR_TAG);
		}		
		return pkTags;
	}


	public String getHibernateDataType(UMLClass klass, UMLAttribute attr) throws GenerationException
	{
		log.debug("getHibernateDataType for klass: " + klass.getName() + ", attr: " + attr.getName());
		String fqcn = getFQCN(klass);
		UMLClass table = getTable(klass);
		UMLAttribute col = getMappedColumn(table,fqcn+"."+attr.getName());
		
		Boolean isClob = "CLOB".equalsIgnoreCase(getTagValue(col.getTaggedValues(),TV_TYPE, 1));
		
		UMLDatatype dataType = attr.getDatatype();
		String name = dataType.getName();
		if(dataType instanceof UMLClass)
			name = getFQCN((UMLClass)dataType);
		
		if(name.startsWith("java.lang."))
			name = name.substring("java.lang.".length());

		if(isClob && "string".equalsIgnoreCase(name))
			return "text";
		if(isClob && !"string".equalsIgnoreCase(name))
			throw new GenerationException("Can not map CLOB to anything other than String");
		if("int".equalsIgnoreCase(name) || "integer".equalsIgnoreCase(name))
			return "integer";
		if("double".equalsIgnoreCase(name))
			return "double";
		if("float".equalsIgnoreCase(name))
			return "float";
		if("long".equalsIgnoreCase(name))
			return "long";
		if("string".equalsIgnoreCase(name))
			return "string";
		if("char".equalsIgnoreCase(name) || "character".equalsIgnoreCase(name))
			return "character";
		if("boolean".equalsIgnoreCase(name) )
			return "boolean";
		if("byte".equalsIgnoreCase(name) )
			return "byte";
		if("short".equalsIgnoreCase(name) )
			return "short";

		if("date".equalsIgnoreCase(name) || "java.util.date".equalsIgnoreCase(name))
			return "java.util.Date";

		log.info("Type = "+name);
		
		return name;
	}	
	
	public String getGetterMethodName(UMLAttribute attr)
	{
		String name = attr.getName(); 
		return "get"+name.substring(0,1).toUpperCase()+name.substring(1,name.length());
	}	

	public String getSetterMethodName(UMLAttribute attr)
	{
		String name = attr.getName(); 
		return "set"+name.substring(0,1).toUpperCase()+name.substring(1,name.length());
	}

	public UMLAssociationEnd getThisEnd(UMLClass klass, List<UMLAssociationEnd>assocEnds) throws GenerationException
	{
		UMLAssociationEnd end1 = assocEnds.get(0);
		UMLAssociationEnd end2 = assocEnds.get(1);
		
		if(end1.getUMLElement().equals(klass))
			return end1;
		else if(end2.getUMLElement().equals(klass))
			return end2;
		else
			throw new GenerationException("Could not figureout this end");
	}

	public UMLAssociationEnd getOtherEnd(UMLClass klass, List<UMLAssociationEnd>assocEnds) throws GenerationException
	{
		UMLAssociationEnd end1 = assocEnds.get(0);
		UMLAssociationEnd end2 = assocEnds.get(1);
		if(end1.getUMLElement().equals(klass))
			return end2;
		else if(end2.getUMLElement().equals(klass))
			return end1;
		else
			throw new GenerationException("Could not figureout other end" );
	}

	public Boolean isAssociationEndMany(UMLAssociationEnd assocEnd)
	{
		if((assocEnd.getHighMultiplicity()<0)||(assocEnd.getLowMultiplicity()<0)) 
			return true;
		else
			return false;
	}
	
	public Boolean isImplicitParent(UMLAssociationEnd assocEnd)
	{
		return isImplicitParent((UMLClass)assocEnd.getUMLElement());
	}

	public String getGetterMethodName(UMLAssociationEnd assocEnd)
	{
		String name = assocEnd.getRoleName(); 
		return "get"+name.substring(0,1).toUpperCase()+name.substring(1,name.length());
	}	

	public String getSetterMethodName(UMLAssociationEnd assocEnd)
	{
		String name = assocEnd.getRoleName(); 
		return "set"+name.substring(0,1).toUpperCase()+name.substring(1,name.length());
	}

	public Boolean isSelfAssociation(UMLAssociationEnd assocEnd1,UMLAssociationEnd assocEnd2)
	{
		return assocEnd1.getUMLElement().equals(assocEnd2.getUMLElement());
	}

	public String getClassIdGetterMthod(UMLClass klass) throws GenerationException
	{
		String idAttrName = getClassIdAttrName(klass);
		if (idAttrName == null) return null;
		return "get"+firstCharUpper(getClassIdAttrName(klass));
	}

	private String firstCharUpper(String data)
	{
		if(data == null || data.length() == 0) return data;
		return Character.toUpperCase(data.charAt(0)) + data.substring(1); 
	}
	
	public String getClassIdAttrName(UMLClass klass) throws GenerationException
	{
		UMLAttribute idAttr = getClassIdAttr(klass);
		if (idAttr == null) return null;
		
		return getClassIdAttr(klass).getName();
	}

	public UMLAttribute getClassIdAttr(UMLClass klass) throws GenerationException
	{
		
		String fqcn = getFQCN(klass);
		
		UMLAttribute idAttr = getColumn(klass,TV_ID_ATTR_COLUMN, fqcn,true,0,1);
		
		if(idAttr !=null) return idAttr;
		
		String idAttrName = "id";
		for(UMLAttribute attribute:klass.getAttributes())
			if(idAttrName.equals(attribute.getName()))
				return attribute;

		for(UMLGeneralization gen: klass.getGeneralizations())
		{
			if(gen.getSubtype() == klass && gen.getSupertype() != klass)
			{
				UMLAttribute superId = getClassIdAttr((UMLClass)gen.getSupertype());
				if(superId != null)
					return superId;
			}
		}
		
		return null;
		//throw new GenerationException("No attribute found that maps to the primary key identifier for class : "+fqcn);
	}
	
	public Boolean isCollection(UMLClass klass, UMLAttribute attr ) throws GenerationException
	{
		if(getDataType(attr).startsWith("Collection")) 
			return true;
		return false;
	}
	
	public boolean isStatic(UMLAttribute att){
		
		UMLTaggedValue tValue = att.getTaggedValue("static");
		
		if (tValue == null) {
			return false;
		}
		
		log.debug("UMLAttribute 'static' Tagged Value: " + tValue.getValue());
		return ("1".equalsIgnoreCase(tValue.getValue()));
	}
	
	public boolean isAbstract(UMLClass klass){	
		return klass.getAbstractModifier().isAbstract();
	}
	
	public String getType(UMLAssociationEnd assocEnd){
		
		UMLTaggedValue tValue = assocEnd.getTaggedValue("type");
		
		if (tValue == null) {
			return "";
		}
		
		log.debug("UMLAttribute Type Tagged Value: " + tValue.getValue());
		return tValue.getValue();
	}		
	
	public UMLAssociationEnd getOtherAssociationEnd(UMLAssociationEnd assocEnd) {
		UMLAssociationEnd otherAssocEnd = null;
		for (Iterator i = assocEnd.getOwningAssociation().getAssociationEnds().iterator(); i
				.hasNext();) {
			UMLAssociationEnd ae = (UMLAssociationEnd) i.next();
			if (ae != assocEnd) {
				otherAssocEnd = ae;
				break;
			}
		}
		return otherAssocEnd;
	}	
	
	public String getUpperBound(UMLAssociationEnd otherEnd) {

		int multiplicity = otherEnd.getHighMultiplicity();
		String finalMultiplicity = new String();
		if (multiplicity == -1) {
			finalMultiplicity = "unbounded";
		} else {
			Integer x = new Integer(multiplicity);
			finalMultiplicity = x.toString();
		}
		return finalMultiplicity;
	}	
	
	public String getLowerBound(UMLAssociationEnd otherEnd) {

		int multiplicity = otherEnd.getLowMultiplicity();
		String finalMultiplicity = new String();
		if (multiplicity == -1) {
			finalMultiplicity = "unbounded";
		} else {
			Integer x = new Integer(multiplicity);
			
			finalMultiplicity = x.toString();
		}
		return finalMultiplicity;
	}	
	
	/**
	 * @param thisEnd
	 * @param otherEnd
	 * @return
	 */
	public boolean isMany2One(UMLAssociationEnd thisEnd, UMLAssociationEnd otherEnd) {
		return isAssociationEndMany(thisEnd) && !isAssociationEndMany(otherEnd);
	}
	
	/**
	 * @param thisEnd
	 * @param otherEnd
	 * @return
	 */
	public boolean isAny(UMLAssociationEnd thisEnd,UMLAssociationEnd otherEnd) {
		return isAssociationEndMany(thisEnd) && !isAssociationEndMany(otherEnd) && isImplicitParent(otherEnd);
	}
	
	/**
	 * @param thisEnd
	 * @param otherEnd
	 * @return
	 */
	public boolean isOne2Many(UMLAssociationEnd thisEnd,UMLAssociationEnd otherEnd) {
		return !isAssociationEndMany(thisEnd) && isAssociationEndMany(otherEnd);
	}
	
	/**
	 * @param thisEnd
	 * @param otherEnd
	 * @return
	 */
	public boolean isMany2Many(UMLAssociationEnd thisEnd,UMLAssociationEnd otherEnd) {
		return isAssociationEndMany(thisEnd) && isAssociationEndMany(otherEnd);
	}	
	
	/**
	 * @param thisEnd
	 * @param otherEnd
	 * @return
	 */
	public boolean isMany2Any(UMLAssociationEnd thisEnd,UMLAssociationEnd otherEnd) {
		return isAssociationEndMany(thisEnd) && isAssociationEndMany(otherEnd) && isImplicitParent(otherEnd);
	}
	
	/**
	 * @param thisEnd
	 * @param otherEnd
	 * @return
	 */
	public boolean isOne2One(UMLAssociationEnd thisEnd,UMLAssociationEnd otherEnd) {
		return !isAssociationEndMany(thisEnd) && !isAssociationEndMany(otherEnd);
	}	
		
	
	public Collection getAssociationEnds(UMLClass klass) {
		return getAssociationEnds(klass, false);
	}

	public Collection getAssociationEnds(UMLClass klass,
			boolean includeInherited) {
		log.debug("class = " + klass.getName() + ", includeInherited = "
				+ includeInherited);
		List<UMLAssociationEnd> assocEndsList = new ArrayList<UMLAssociationEnd>();
		UMLClass superClass = klass;
		while (superClass != null) {
			Collection assocs = superClass.getAssociations();
			log.debug( superClass.getName() + " association collection size(): " + assocs.size());
			
			for (Iterator i = assocs.iterator(); i.hasNext();) {
				UMLAssociation assoc = (UMLAssociation) i.next();
				
				for (UMLAssociationEnd ae:assoc.getAssociationEnds()){
					UMLAssociationEnd otherEnd = getOtherAssociationEnd(ae);
					String id = ((UMLClass)(otherEnd.getUMLElement())).getName() + Constant.LEFT_BRACKET
							+ getFQCN((UMLClass)(otherEnd.getUMLElement())) + Constant.RIGHT_BRACKET;
					
					log.debug("id (otherEnd): " + id);
					log.debug("superClass: " + superClass.getName());
					
					if ((UMLClass)ae.getUMLElement() == superClass) {
						log.debug("adding association: " + id + " for class " + superClass.getName());
						assocEndsList.add(ae);
					}
				}
			}
			if (includeInherited) {
				// TODO :: Implement includeInherited
//				Collection gens = superClass.getGeneralization();
//				if (gens.size() > 0) {
//					superClass = (Classifier) ((Generalization) gens.iterator()
//							.next()).getParent();
//				} else {
//					superClass = null;
//				}
				log.debug("Need to implement includeInherited");

			} else {
				superClass = null;
			}
		}	
		
		return assocEndsList;
	}
	
	public void collectPackages(Collection<UMLPackage> nextLevelPackages, Hashtable<String, Collection<UMLClass>> pkgColl) throws GenerationException
	{
		for(UMLPackage pkg:nextLevelPackages){

			if (isIncluded(pkg)){
				String pkgName=getFullPackageName(pkg);
				log.debug("including package: " + pkgName);
			
				Collection<UMLClass> pkgClasses = pkg.getClasses();

				if (pkgClasses != null && pkgClasses.size() > 0){
					for (UMLClass klass:pkgClasses){
						if(!STEREO_TYPE_TABLE.equalsIgnoreCase(klass.getStereotype()) && isIncluded(klass)) { 
							if(!pkgColl.containsKey(pkgName)) {
								List<UMLClass> classes = new ArrayList<UMLClass>();
								classes.add(klass);
								pkgColl.put(pkgName, classes);
							} else {
								Collection<UMLClass> existingCollection = pkgColl.get(pkgName);
								existingCollection.add(klass);
							}					
						}
					}
				}
			} else{
				log.debug("excluding package: " + pkg.getName());
			}
			collectPackages(pkg.getPackages(), pkgColl);	
		}
	}
	
	public void collectPackages(Collection<UMLClass> allClasses, Hashtable<String, Collection<UMLClass>> pkgColl,String defaultNamespacePrefix)
	throws GenerationException {

		String pkgName=null;
		String pkgNamespace=null;
		for(UMLClass klass:allClasses){
			pkgName = getGMEPackageName(klass);
			if (pkgName == null)
				pkgName=getFullPackageName(klass);
			log.debug("processing klass: " + klass.getName() + " of package " + pkgName);
			if (isNamespaceIncluded(klass,defaultNamespacePrefix)){
				log.debug("including package: " + pkgName);

				if(!STEREO_TYPE_TABLE.equalsIgnoreCase(klass.getStereotype()) && isIncluded(klass)) { //No longer using GME ClassName; e.g., no longer using isIncluded(pkgName+"."+getClassName(klass))) { 
					pkgNamespace=getGMENamespace(klass);
					
					if (pkgNamespace !=null && (pkgNamespace.endsWith("/") || !pkgNamespace.endsWith(pkgName)))
						pkgNamespace=pkgNamespace+pkgName;
					log.debug("pkgNamespace: " + pkgNamespace);
					if(!pkgColl.containsKey(pkgNamespace)) {
						List<UMLClass> classes = new ArrayList<UMLClass>();
						classes.add(klass);
						pkgColl.put(pkgNamespace, classes);
					} else {
						Collection<UMLClass> existingCollection = pkgColl.get(pkgNamespace);
						existingCollection.add(klass);
					}					
				}
			} else{
				log.debug("excluding class: " +klass.getName()+" with package: " + pkgName);
			}
		}

	}
	
	public String getGMEPackageName(UMLClass klass) throws GenerationException{
		String namespacePkgName = null;
		try {
			namespacePkgName = getNamespacePackageName(klass);
			if (namespacePkgName!=null && namespacePkgName.length()>0)
				return namespacePkgName;
		} catch(GenerationException ge) {
			log.error("ERROR: ", ge);
			throw new GenerationException("Error getting the GME package name for: " + getFQEN(klass), ge);
		}
		
		namespacePkgName=getGMEPackageName(klass.getPackage());
		if (namespacePkgName!=null && namespacePkgName.length()>0)
			return namespacePkgName;
		
		log.debug("GME Package name not found for: "+getFullPackageName(klass)+". Returning null");
		return null;
	}
	
	public String getGMEPackageName(UMLPackage pkg) throws GenerationException{
		if (pkg==null)
			return null;
		
		log.debug("Getting Package Name for: " +pkg.getName());
		String namespacePkgName = getNamespacePackageName(pkg);
		if (namespacePkgName!=null && namespacePkgName.length()>0)
			return namespacePkgName;

		return getGMEPackageName(pkg.getParent());
	}
	
	private String getClassName(UMLClass klass)throws GenerationException{
		try {
			String klassName = getXMLClassName(klass);
			if (klassName!=null)
				return klassName;
		} catch(GenerationException ge) {
			log.error("ERROR: ", ge);
			throw new GenerationException("Error getting the GME Class (XML Element) name for klass: " + getFQCN(klass));
		}
		return klass.getName();
	}
	
	/**
	 * Returns all the classes (not the tables) in the XMI file which do not belong to java.lang or java.util package 
	 * @param model
	 * @return
	 */
	public Collection<UMLClass> getAllClasses(UMLModel model) throws GenerationException
	{
		Collection<UMLClass> classes = null;
		try {
			classes = new HashSet<UMLClass>();
			getAllClasses(model.getPackages(),classes);
		} catch(Exception e){
			log.error("Unable to retrieve classes from model: ", e);
			throw new GenerationException("Unable to retrieve classes from model: ", e);
		}
		return classes;
	}
	
	private void getAllClasses(Collection<UMLPackage> pkgCollection,Collection<UMLClass> classes)throws GenerationException
	{
		for(UMLPackage pkg:pkgCollection)
			getAllClasses(pkg,classes);
	}
	
	private void getAllClasses(UMLPackage rootPkg,Collection<UMLClass> classes) throws GenerationException
	{
		if(isIncluded(rootPkg))
		{
			for(UMLClass klass:rootPkg.getClasses())
			{
				if(!STEREO_TYPE_TABLE.equalsIgnoreCase(klass.getStereotype()) && isIncluded(klass))
					classes.add(klass);
			}
		}
		getAllClasses(rootPkg.getPackages(),classes);
	}	
	
	/**
	 * Returns all the interfaces in the XMI file which do not belong to java.lang or java.util package 
	 * @param model
	 * @return
	 */
	public Collection<UMLInterface> getAllInterfaces(UMLModel model) throws GenerationException
	{
		Collection<UMLInterface> interfaces = null;
		try {
			interfaces = new HashSet<UMLInterface>();
			getAllInterfaces(model.getPackages(),interfaces);
		} catch(Exception e){
			log.error("Unable to retrieve interfaces from model: ", e);
			throw new GenerationException("Unable to retrieve interfaces from model: ", e);
		}
		return interfaces;
	}
	
	private void getAllInterfaces(Collection<UMLPackage> pkgCollection,Collection<UMLInterface> interfaces)throws GenerationException
	{
		for(UMLPackage pkg:pkgCollection)
			getAllInterfaces(pkg,interfaces);
	}

	private void getAllInterfaces(UMLPackage rootPkg,Collection<UMLInterface> interfaces) throws GenerationException
	{
		if(isIncluded(rootPkg))
		{
			for(UMLInterface interfaze:rootPkg.getInterfaces())
			{
				if(!STEREO_TYPE_TABLE.equalsIgnoreCase(interfaze.getStereotype()) && isIncluded(interfaze))
					interfaces.add(interfaze);
			}
		}
		getAllInterfaces(rootPkg.getPackages(),interfaces);
	}	
	
	public Collection<UMLClass> getAllHibernateClasses(UMLModel model) throws GenerationException
	{
		Collection<UMLClass> allHibernateClasses = getAllParentClasses(model);
		allHibernateClasses.addAll(getAllImplicitParentLeafClasses(model));
		
		return allHibernateClasses;	
	}	
	
	/**
	 * Returns all the classes (not the tables) in the XMI file which do not belong to java.lang or java.util package.
	 * The class also have to be the root class in the inheritnace hierarchy to be included in the final list 
	 * @param model
	 * @return
	 */
	public Collection<UMLClass> getAllParentClasses(UMLModel model) throws GenerationException
	{
		Collection<UMLClass> classes = new ArrayList<UMLClass>();
		getAllParentClasses(model.getPackages(),classes);
		return classes;
	}
	
	private void getAllParentClasses(Collection<UMLPackage> pkgCollection,Collection<UMLClass> classes) throws GenerationException
	{
		for(UMLPackage pkg:pkgCollection)
			getAllParentClasses(pkg,classes);
	}

	
	private void getAllParentClasses(UMLPackage rootPkg,Collection<UMLClass> classes)throws GenerationException
	{
		if(isIncluded(rootPkg))
		{
			for(UMLClass klass:rootPkg.getClasses())
			{
				if(!STEREO_TYPE_TABLE.equalsIgnoreCase(klass.getStereotype()) && isIncluded(klass) && ModelUtil.getSuperclasses(klass).length == 0 && !isImplicitParent(klass))
					classes.add(klass);
			}
		}
		getAllParentClasses(rootPkg.getPackages(),classes);
	}
	
	/**
	 * Returns all the classes (not the tables) in the XMI file which do not belong to java.lang or java.util package.
	 * The class also has to be an implicit parent (parent class with no table mapping) in the inheritance hierarchy to be included in the final list 
	 * @param model
	 * @return
	 */
	public Collection<UMLClass> getAllImplicitParentLeafClasses(UMLModel model) throws GenerationException
	{
		Collection<UMLClass> classes = new ArrayList<UMLClass>();
		getAllImplicitParentLeafClasses(model.getPackages(),classes);
		return classes;
	}
	
	private void getAllImplicitParentLeafClasses(Collection<UMLPackage> pkgCollection,Collection<UMLClass> classes) throws GenerationException
	{
		for(UMLPackage pkg:pkgCollection)
			getAllImplicitParentLeafClasses(pkg,classes);
	}
	
	private void getAllImplicitParentLeafClasses(UMLPackage rootPkg,Collection<UMLClass> classes) throws GenerationException
	{
		if(isIncluded(rootPkg))
		{
			for(UMLClass klass:rootPkg.getClasses())
			{
				try {
					if(!STEREO_TYPE_TABLE.equalsIgnoreCase(klass.getStereotype()) && isIncluded(klass) && isImplicitParent(getSuperClass(klass)) && !isImplicitParent(klass))
						classes.add(klass);
				} catch(GenerationException e){
					continue;
				}
			}
		}
		getAllImplicitParentLeafClasses(rootPkg.getPackages(),classes);
	}
	

	/**
	 * Retrieves the table corresponding to the Dependency link between class and a table. 
	 * If there are no Dependencies that links the class to table or there is more than
	 * one Dependency then the method throws an exception
	 *  
	 * @param klass
	 * @return
	 * @throws GenerationException
	 */
	public UMLClass getTable(UMLClass klass) throws GenerationException
	{
		Set<UMLDependency> dependencies = klass.getDependencies();
		Map<String,UMLClass> clientMap = new HashMap<String,UMLClass>();
		int count = 0;
		UMLClass result = null; 
		for(UMLDependency dependency:dependencies)
		{
			UMLClass client = (UMLClass) dependency.getClient();
			
			log.debug("getTable: klass: " + klass.getName() + "Client stereotype: " +client.getStereotype() + "; dependency stereotype: " + dependency.getStereotype());
			if(STEREO_TYPE_TABLE.equalsIgnoreCase(client.getStereotype()) && STEREO_TYPE_DATASOURCE_DEPENDENCY.equalsIgnoreCase(dependency.getStereotype()))
			{
				log.debug("* * * client.getName(): " + client.getName());
				clientMap.put(client.getName(), client); 
				result = client;
			}
		}
		
		count = clientMap.size();

		if(count!=1){
			log.debug("getTable: klass: " +klass.getName()+"; count: " + count);
			throw new GenerationException("No table found for : "+getFQCN(klass)+".  Make sure the corresponding Data Model table (class) has a 'table' Stereotype assigned, and the Dependency between the Data Model table and Logical Model class has a 'DataSource' Stereotype assigned.");
		}

		
		return result;
	}
	
	/**
	 * Determines whether the input class is an implicit superclass.  Used 
	 * by the code generator to determine whether an implicit inheritance 
	 * hibernate mapping should be created for the input class
	 * @param klass
	 * @return
	 * @throws GenerationException
	 */
	public boolean isImplicitParent(UMLClass klass)
	{
		if (klass != null)
			log.debug("isImplicitClass " + klass.getName()+": " + (isSuperclass(klass) && hasNoTableMapping(klass)));
		
		return (isSuperclass(klass) && hasNoTableMapping(klass));
	}
	
	public boolean hasImplicitParent(UMLClass klass){
		UMLClass superclass = klass;
		do {
			try {
				superclass = getSuperClass(superclass);

				if(isImplicitParent(superclass)){
					return true;
				}
			} catch (GenerationException e) {
				log.error("ERROR encountered checking if class " +klass.getName() + " has an implicit parent: ", e);
				return false;
			}

		} while (!(superclass==null) && !(superclass.getName().equalsIgnoreCase("java.lang.Object")));

		return false;
	}
	
	/**
	 * Determines whether the input class is a superclass
	 * @param klass
	 * @return
	 */
	private boolean isSuperclass(UMLClass klass)
	{
		boolean isSuperClass = false;

		if (klass != null)
			for(UMLGeneralization gen:klass.getGeneralizations())
			{
				if(gen.getSupertype() instanceof UMLClass && ((UMLClass)gen.getSupertype()) == klass) 
					return true;
			}

		return isSuperClass;
	}
	
	/**
	 * Determines whether the input class is a superclass
	 * @param klass
	 * @return
	 */
	private boolean hasNoTableMapping(UMLClass klass)
	{
		try {
			getTable(klass);
		} catch (GenerationException e){
			return true;
		}
		return false;
	}	
	
	/**
	 * Scans the tag values of the association to determine which JOIN table the association is using. 
	 * 
	 * @param association
	 * @param model
	 * @param klass
	 * @return
	 * @throws GenerationException
	 */
	public UMLClass findCorrelationTable(UMLAssociation association, UMLModel model, UMLClass klass) throws GenerationException
	{
		return findCorrelationTable(association, model, klass, true);
	}

	public UMLClass findCorrelationTable(UMLAssociation association, UMLModel model, UMLClass klass, boolean throwException) throws GenerationException
	{
		int minReq = throwException ? 1:0;
		String tableName = getTagValue(klass, association,TV_CORRELATION_TABLE, null,minReq,1);

		if(!throwException && (tableName == null || tableName.length() ==0)) return null;
		
		UMLClass correlationTable = ModelUtil.findClass(model,BASE_PKG_DATA_MODEL+"."+tableName);
		if(correlationTable == null) throw new GenerationException("No correlation table found named : \""+tableName+"\"");
		
		return correlationTable;
	}

	public String getMappedColumnName(UMLClass table, String fullyQualifiedAttrName) throws GenerationException
	{
		return getColumnName(table,TV_MAPPED_ATTR_COLUMN,fullyQualifiedAttrName,false,1,1);
	}
	
	public UMLAttribute getMappedColumn(UMLClass table, String fullyQualifiedAttrName) throws GenerationException
	{
		return getColumn(table,TV_MAPPED_ATTR_COLUMN,fullyQualifiedAttrName,false,1,1);
	}

	/**
	 * @param tgElt The TaggableElement (UMLClass, UMLAttribute)
	 * @return		String containing a concatenation of any, all caDSR 
	 * 				tag values
	 */
	public String getCaDSRAnnotationContent(UMLTaggableElement tgElt)
	{
		String publicID = getTagValue(tgElt, TV_CADSR_PUBLICID);
		String version = getTagValue(tgElt, TV_CADSR_VERSION);		

		if (publicID == null && version == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		
		if (publicID != null)
			sb.append(TV_CADSR_PUBLICID).append("=\"").append(publicID).append("\"; ");
		
		if (version != null)
			sb.append(TV_CADSR_VERSION).append("=\"").append(version).append("\"");
	
		return sb.toString();

	}	
	
	public String findAssociatedColumn(UMLClass table,UMLClass klass, UMLAssociationEnd otherEnd, UMLClass assocKlass, UMLAssociationEnd thisEnd, Boolean throwException, Boolean isJoin) throws GenerationException
	{

		String col1 = getColumnName(table,TV_ASSOC_COLUMN,getFQCN(klass) +"."+ otherEnd.getRoleName(),false,0,1);
		String col2 = getColumnName(table,TV_ASSOC_COLUMN,getFQCN(assocKlass) +"."+ thisEnd.getRoleName(),false,0,1);
		String col3 = getColumnName(table,TV_INVERSE_ASSOC_COLUMN,getFQCN(assocKlass) +"."+ thisEnd.getRoleName(),false,0,1);
		
		log.debug("***** col1: " + col1 + "; col2: " + col2 + "; col3: " + col3);
		
		if("".equals(col1)) col1=null;
		if("".equals(col2)) col2=null;
		if("".equals(col3)) col3=null;
		
		if((col1==null && col3==null && isJoin && throwException) || (col1==null && col2==null && !isJoin && throwException)){
			log.debug("***** col1: " + col1 + "; col2: " + col2 + "; col3: " + col3);
			log.debug("klass: " + klass.getName());
			log.debug("assocKlass: " + assocKlass.getName());
			log.debug("table: " + table.getName());
			log.debug("isJoin: " + isJoin);
			log.debug("otherEnd.getRoleName(): " +otherEnd.getRoleName());
			log.debug("thisEnd.getRoleName(): " +thisEnd.getRoleName());	
			throw new GenerationException("Could not determine the column for the association between "+getFQCN(klass)+" and "+getFQCN(assocKlass) +". Check for missing implements-association/inverse-of/correlation-table tag(s), where appropriate");
		}
		/*if(col1!=null && col2!=null && !col1.equals(col2))
			throw new GenerationException("More than one column found for the association between "+getFQCN(klass)+" and "+getFQCN(assocKlass));
		if(col1!=null && col3!=null && !col1.equals(col3))
			throw new GenerationException("More than one column found for the association between "+getFQCN(klass)+" and "+getFQCN(assocKlass));
		if(col2!=null && col3!=null && !col2.equals(col3))
			throw new GenerationException("More than one column found for the association between "+getFQCN(klass)+" and "+getFQCN(assocKlass));
		*/
		if(isJoin)
		{
			return col1==null ? col3 : col1;
		}
		else
		{
			return col1==null ? col2 : col1;
			
		}
/*		if(col1!=null) return col1;
		else if (col3!=null) return col3;
		else return col2;
*/	}

	public String findAssociatedColumn(UMLClass table,UMLClass klass, UMLAssociationEnd otherEnd, UMLClass assocKlass, UMLAssociationEnd thisEnd, Boolean isJoin) throws GenerationException
	{
		return findAssociatedColumn(table,klass, otherEnd, assocKlass, thisEnd, true, isJoin);
	}

	public String findInverseColumnValue(UMLClass table,UMLClass klass, UMLAssociationEnd thisEnd) throws GenerationException
	{
		return getColumnName(table,TV_INVERSE_ASSOC_COLUMN,getFQCN(klass) +"."+ thisEnd.getRoleName(),false,0,1);
	}
	
	public String findDiscriminatingColumnName(UMLClass klass) throws GenerationException
	{
		UMLClass superKlass = klass;
		
		UMLClass temp = klass;
		while ((temp = getSuperClass(temp))!=null && !isImplicitParent(temp))
			superKlass = temp;
		
		UMLClass table = getTable(superKlass);
		String fqcn = getFQCN(superKlass);
		return getColumnName(table,TV_DISCR_COLUMN,fqcn,false,0,1);
	}

	public String getDiscriminatorValue(UMLClass klass) throws GenerationException
	{
		return getTagValue(klass,TV_DISCR_COLUMN,null, 1,1);
	}
	
	public String getRootDiscriminatorValue(UMLClass klass) throws GenerationException
	{
		return getTagValue(klass,TV_DISCR_COLUMN,null,0,1);
	}
	
	public String getImplicitDiscriminatorColumn(UMLClass table, UMLClass klass, String roleName) throws GenerationException
	{
		log.debug("**** getImplicitDiscriminator: table: " + table.getName() +"; klass: " + klass.getName() +"; roleName: " + roleName);
		return getColumnName(table,TV_DISCR_COLUMN,getFQCN(klass)+"."+roleName,false,1,1);
	}
	
	public String getImplicitIdColumn(UMLClass table, UMLClass klass, String roleName) throws GenerationException
	{
		return getColumnName(table,TV_ASSOC_COLUMN,getFQCN(klass)+"."+roleName,false,1,1);
	}

	public boolean isLazyLoad(UMLClass klass, UMLAssociation association) throws GenerationException
	{
		String temp = getTagValue(klass,association, TV_LAZY_LOAD,null, 0,1);
		
		if (temp != null)
			throw new GenerationException("Invalid Tag Value found:  The '" + TV_LAZY_LOAD + "' Tag Value which is attached to the association link has been replaced with the '" + TV_NCI_EAGER_LOAD + "' Tag Value.  Also, it's value must now conform to the following pattern:  "+TV_NCI_EAGER_LOAD+"#<fully qualified class name>.<role name>.  The value of the tag continues to be 'yes' or 'no'.  Please update your model accordingly" );

		return true;
	}
	
	private String getTagValue(UMLTaggableElement elt, String key, String value, int minOccurrence, int maxOccurrence) throws GenerationException
	{
		String result = null;
		int count = 0;
		for(UMLTaggedValue tv: elt.getTaggedValues())
		{
			if (key.equals(tv.getName()))
			{
				String tvValue = tv.getValue();
				String[] tvValues = tvValue.split(",");
				for(String val:tvValues)
				{
					if(value==null)
					{
						count++;
						result = val;
					}
					else if(value.equals(val))
					{
						count++;
						result = val;
					}
				}
			}
		}
		
		if(count < minOccurrence || (minOccurrence>0 && (result == null || result.trim().length() == 0))) throw new GenerationException("No value found for "+key+" tag in : "+getFQEN(elt));
		if(count > maxOccurrence) throw new GenerationException("More than one value found for "+key+" tag in : "+getFQEN(elt));
		
		return result;
	}
	
	private String getTagValue(UMLTaggableElement tgElt, String key) 
	{

		for(UMLTaggedValue tv: tgElt.getTaggedValues())
		{
			if (key.equals(tv.getName()))
			{
				return tv.getValue();
			}
		}
		
		return null;
	}	
	
	private List<String> getTagValues(UMLTaggableElement tgElt, String key) 
	{

		List<String> tagValues = new ArrayList<String>();
		for(UMLTaggedValue tv: tgElt.getTaggedValues())
		{
			if (key.equals(tv.getName()))
			{
				log.debug(tv.getName() + ": " + tv.getValue());
				tagValues.add(tv.getValue());
			}
		}
		
		return tagValues;
	}
	

	private String getColumnName(UMLClass klass, String key, String value,  boolean isValuePrefix, int minOccurrence, int maxOccurrence) throws GenerationException
	{
		UMLAttribute attr = getColumn(klass,key,value,isValuePrefix,minOccurrence,maxOccurrence);
		return (attr==null) ? "" : attr.getName();
	}

	private UMLAttribute getColumn(UMLClass klass, String key, String value, boolean isValuePrefix, int minOccurrence, int maxOccurrence) throws GenerationException
	{
	
		UMLAttribute result = null;
		int count = 0;
		for(UMLAttribute attr: klass.getAttributes())
		{
			for(UMLTaggedValue tv: attr.getTaggedValues())
			{
				if (key.equals(tv.getName()))
				{
					String tvValue = tv.getValue();
					String[] tvValues = tvValue.split(",");
					for(String val:tvValues)
					{
						if(value==null)
						{
							count++;
							result = attr;
						}
						else if(isValuePrefix && val.startsWith(value))
						{
							count++;
							result = attr;
						}
						else if(!isValuePrefix && val.equals(value))
						{
							count++;
							result = attr;
						}
					}
				}
			}
		}
		if(count < minOccurrence) throw new GenerationException("No value of "+value+" found for "+key+" tag in class : "+getFQCN(klass));
		if(count > maxOccurrence) throw new GenerationException("More than one values found for "+key+" tag in class : "+getFQCN(klass));
		
		return result;
	}
	
	private String getTagValue(UMLClass klass, UMLAttribute attribute, String key, String value, Boolean isValuePrefix, int minOccurrence, int maxOccurrence) throws GenerationException
	{
		String result = null;
		int count = 0;
		for(UMLTaggedValue tv: attribute.getTaggedValues())
		{
			log.debug("Processing tv: " + tv.getName());
			if (key.equals(tv.getName()))
			{
				String tvValue = tv.getValue();
				log.debug("Key equals tv.  TV value is: " + tv.getValue());
				String[] tvValues = tvValue.split(",");
				for(String val:tvValues)
				{
					if(value==null)
					{
						count++;
						result = val;
					}
					else if(isValuePrefix && val.startsWith(value))
					{
						count++;
						result = val;
					}
					else if(!isValuePrefix && val.equals(value))
					{
						count++;
						result = val;
					}
				}
			}
		}
		
		if(count < minOccurrence) throw new GenerationException("No value of "+value+" found for "+key+" tag in class : "+getFQCN(klass));
		if(count > maxOccurrence) throw new GenerationException("More than one values found for "+key+" tag in class : "+getFQCN(klass));
	
		return result;
	}	
		
	private String getTagValue(UMLClass klass, UMLAssociation association, String key, String value, Boolean isValuePrefix, int minOccurrence, int maxOccurrence) throws GenerationException
	{
		
		List <UMLAssociationEnd>ends = association.getAssociationEnds();
		UMLAssociationEnd thisEnd = getThisEnd(klass, ends);
		UMLAssociationEnd otherEnd = getOtherEnd(klass, ends);
		String thisClassName = getFQCN(((UMLClass)thisEnd.getUMLElement()));
		String otherClassName = getFQCN(((UMLClass)otherEnd.getUMLElement()));
		
		String result = null;
		int count = 0;
		for(UMLTaggedValue tv: association.getTaggedValues())
		{
			if (key.equals(tv.getName()))
			{
				String tvValue = tv.getValue();
				String[] tvValues = tvValue.split(",");
				for(String val:tvValues)
				{
					if(value==null)
					{
						count++;
						result = val;
					}
					else if(isValuePrefix && val.startsWith(value))
					{
						count++;
						result = val;
					}
					else if(!isValuePrefix && val.equals(value))
					{
						count++;
						result = val;
				}
			}
		}
		}
		
		
		if(count < minOccurrence || (minOccurrence >0 && (result == null || result.trim().length() == 0))) throw new GenerationException("No tag value of "+key+" found for the association between "+thisClassName +" and "+ otherClassName +":"+count+":"+result);
		if(count > maxOccurrence) throw new GenerationException("More than the expected maximum number (" + maxOccurrence + ") of tag value occurrences  for "+key+" found for the association between "+thisClassName +" and "+ otherClassName);
		
		return result;
	}	
	
	private String getTagValue(UMLClass klass, UMLAssociation association, String key, String value, int minOccurrence, int maxOccurrence) throws GenerationException
	{
		return getTagValue(klass, association, key, value,false, minOccurrence, maxOccurrence);
	}	
	
	public String getTagValue(Collection<UMLTaggedValue> tagValues, String key, int maxOccurrence) throws GenerationException
	{
		StringBuilder temp = new StringBuilder();
		
		for(int i=0;i<maxOccurrence;i++)
		{
			String searchKey = i==0 ? key : key + (i+1);
			for(UMLTaggedValue tv:tagValues)
			{
				if(searchKey.equals(tv.getName()))
				{
					temp.append(tv.getValue());
				}
			}
			
		}
		return temp.toString();
	}
	
	private String getJavaDocs(Collection<UMLTaggedValue> tagValues) throws GenerationException
	{
		String documentation = getTagValue(tagValues, TV_DOCUMENTATION, 8);
		String description = getTagValue(tagValues, TV_DESCRIPTION, 8);		
		
		String temp = documentation == null || documentation.trim().length()==0 ? description : documentation; 
		StringBuilder doc = new StringBuilder();
		doc.append("/**");
		doc.append("\n\t* ").append(temp);
		doc.append("\n\t**/");
		return doc.toString();

	}
	
	public String getJavaDocs(UMLInterface interfaze) throws GenerationException 
	{
		return getJavaDocs(interfaze.getTaggedValues());
	}
	
	public String getJavaDocs(UMLClass klass) throws GenerationException 
	{
		return getJavaDocs(klass.getTaggedValues());
	}

	public String getJavaDocs(UMLAttribute attr) throws GenerationException 
	{
		return getJavaDocs(attr.getTaggedValues());
	}

	public String getJavaDocs(UMLClass klass, UMLAssociation assoc) throws GenerationException 
	{
		UMLAssociationEnd otherEnd = getOtherEnd(klass, assoc.getAssociationEnds());
		StringBuilder doc = new StringBuilder();
		doc.append("/**");
		doc.append("\n	* An associated "+getFQCN(((UMLClass)otherEnd.getUMLElement()))+" object");
		if(isAssociationEndMany(otherEnd))
			doc.append("'s collection ");
		doc.append("\n	**/\n");
		return doc.toString();
	}

	public String getGetterMethodJavaDocs(UMLAttribute attr) {
		StringBuilder doc = new StringBuilder();
		doc.append("/**");
		doc.append("\n	* Retrieves the value of the "+attr.getName()+" attribute");
		doc.append("\n	* @return ").append(attr.getName());
		doc.append("\n	**/\n");
		return doc.toString();
	}

	public String getSetterMethodJavaDocs(UMLAttribute attr) {
		StringBuilder doc = new StringBuilder();
		doc.append("/**");
		doc.append("\n	* Sets the value of "+attr.getName()+" attribute");
		doc.append("\n	**/\n");
		return doc.toString();
	}

	public String getGetterMethodJavaDocs(UMLClass klass, UMLAssociation assoc) throws GenerationException {
		UMLAssociationEnd otherEnd = getOtherEnd(klass, assoc.getAssociationEnds());
		StringBuilder doc = new StringBuilder();
		doc.append("/**");
		doc.append("\n	* Retrieves the value of the "+otherEnd.getRoleName()+" attribute");
		doc.append("\n	* @return ").append(otherEnd.getRoleName());
		doc.append("\n	**/\n");
		return doc.toString();
	}

	public String getSetterMethodJavaDocs(UMLClass klass, UMLAssociation assoc) throws GenerationException {
		UMLAssociationEnd otherEnd = getOtherEnd(klass, assoc.getAssociationEnds());
		StringBuilder doc = new StringBuilder();
		doc.append("/**");
		doc.append("\n	* Sets the value of "+otherEnd.getRoleName()+" attribute");
		doc.append("\n	**/\n");
		return doc.toString();
	}	
	
	public String reversePackageName(String s) {
		StringTokenizer st = new StringTokenizer(s,".");
		Vector<String> myVector = new Vector<String>();
		StringBuilder myStringBuilder = new StringBuilder();
		while (st.hasMoreTokens()) {
			     String t = st.nextToken();
			     myVector.add(t);

	    }

        for (int i = myVector.size(); i>0; i--) {
			  myStringBuilder.append(myVector.elementAt(i-1));
			  myStringBuilder.append(Constant.DOT);

	    }
	    int length1 = myStringBuilder.length();
	    String finalString1 = myStringBuilder.substring(0,length1-1);
        return finalString1;
    }
	
	
	
	public String getWSDDServiceValue(Collection<UMLClass> classColl)throws GenerationException{
        StringBuilder nn1 = new StringBuilder();
        for(UMLClass klass:classColl){
			String pkgName = getFullPackageName(klass);
			nn1.append(pkgName)
			   .append(Constant.DOT)
			   .append(klass.getName())
			   .append(Constant.COMMA);
		}
        
        // remove last Comma
        return nn1.substring(0, nn1.length()-1);

	}
	

	public UMLClass findCollectionTable(UMLAttribute attr, UMLModel model) throws GenerationException
	{
		String tableName = getTagValue(attr.getTaggedValues(),TV_MAPPED_COLLECTION_TABLE, 1);

		UMLClass collectionTable = ModelUtil.findClass(model,BASE_PKG_DATA_MODEL+"."+tableName);
		if(collectionTable == null) throw new GenerationException("No collection table found named : \""+tableName+"\"");
		
		return collectionTable;
	}	
	
	
	public String getCollectionKeyColumnName(UMLClass table,UMLClass klass, UMLAttribute attr) throws GenerationException
	{
		return getColumnName(table,TV_MAPPED_ATTR_COLUMN,getFQCN(klass) +"."+ attr.getName(),false,1,1);
	}

	public String getCollectionElementColumnName(UMLClass table,UMLClass klass, UMLAttribute attr) throws GenerationException
	{
		return getColumnName(table,TV_MAPPED_ELEMENT_COLUMN,getFQCN(klass) +"."+ attr.getName(),false,1,1);
	}

    public String lowerCase(String s) {
        return s.substring(0, 1).toLowerCase().concat(s.substring(1));
    }

    public String upperCase(String s) {
        return s.substring(0, 1).toUpperCase().concat(s.substring(1));
    }

	public String getCollectionElementHibernateType(UMLClass klass, UMLAttribute attr) throws GenerationException
	{
		String name = getDataType(attr);
		if(name.startsWith("Collection<"))
		{
			name = name.substring("Collection<".length());
			name = name.substring(0,name.length()-1);
			
			if("int".equalsIgnoreCase(name) || "integer".equalsIgnoreCase(name))
				return "integer";
			if("double".equalsIgnoreCase(name))
				return "double";
			if("float".equalsIgnoreCase(name))
				return "float";
			if("long".equalsIgnoreCase(name))
				return "long";
			if("string".equalsIgnoreCase(name))
				return "string";
			if("char".equalsIgnoreCase(name) || "character".equalsIgnoreCase(name))
				return "character";
			if("boolean".equalsIgnoreCase(name) )
				return "boolean";
			if("byte".equalsIgnoreCase(name) )
				return "byte";
			if("short".equalsIgnoreCase(name) )
				return "short";	
		}
		return name;
	}
	
	public List<UMLClass> getNonImplicitSubclasses(UMLClass implicitKlass){
		
		ArrayList<UMLClass> nonImplicitSubclasses = new ArrayList<UMLClass>();
		
		getNonImplicitSubclasses(implicitKlass, nonImplicitSubclasses);
		
		return nonImplicitSubclasses;
	}
	
	private void getNonImplicitSubclasses(UMLClass klass, ArrayList<UMLClass> nonImplicitSubclasses){
		for(UMLGeneralization gen:klass.getGeneralizations()){
			UMLClass subKlass = (UMLClass)gen.getSubtype();
			if(subKlass!=klass && !isImplicitParent(subKlass)){
				nonImplicitSubclasses.add(subKlass);
			}
			
			if(subKlass!=klass)
				getNonImplicitSubclasses(subKlass, nonImplicitSubclasses);
		}
	}
	
	/**
	 * Scans the tag values of the association to determine the cascade-style 
	 * 
	 * @param association
	 * @param model
	 * @param klass
	 * @return
	 * @throws GenerationException
	 */
	public String findCascadeStyle(UMLClass klass, String roleName, UMLAssociation association) throws GenerationException
	{
		for (String cascadeStyles : getTagValues(association, TV_NCI_CASCADE_ASSOCIATION + "#" + getFQCN(klass)+"."+roleName)){

				List<String> validCascadeStyles = new ArrayList<String>();
				
				for(String cascadeStyle:cascadeStyles.split(",")){
					validCascadeStyles.add(cascadeStyle.trim());
				}
				
				StringBuilder validCascadeStyleSB = new StringBuilder();
				validCascadeStyleSB.append(validCascadeStyles.get(0));
				for (int i = 1; i <validCascadeStyles.size(); i++ ){
					validCascadeStyleSB.append(",").append(validCascadeStyles.get(i));
				}

				return validCascadeStyleSB.toString();
		}
		
		return "none";
	}

	public String isFKAttributeNull(UMLAssociationEnd otherEnd) {
		if (otherEnd.getLowMultiplicity() == 0) {
			return "false";
		}
		return "true";
	}

	/**
	 * Scans the tag values of the association to determine the cascade-style 
	 * 
	 * @param klass
	 * @param roleName
	 * @param association
	 * @return
	 * @throws GenerationException
	 */
	public boolean isLazyLoad(UMLClass klass, String roleName, UMLAssociation association) throws GenerationException
	{
		for( String eagerLoadValue : getTagValues(association, TV_NCI_EAGER_LOAD + "#" +getFQCN(klass)+"."+roleName)){
			if ("true".equalsIgnoreCase(eagerLoadValue) || "yes".equalsIgnoreCase(eagerLoadValue) ){
				return false;
			}
		}

		return true;
	}

	/**
	 * Scans the tag values of the association to determine the cascade-style 
	 * 
	 * @param association
	 * @param model
	 * @param klass
	 * @return
	 * @throws GenerationException
	 */
	public Map<String,String> getValidCascadeStyles(){
		return CASCADE_STYLES;
	}
	
	/**
	 * Scans the tag values of the association to determine whether or not an inverse-of tag 
	 * is present in any of the table columns 
	 * 
	 * @param klass
	 * @param key
	 * @return
	 * @throws GenerationException
	 */
	public List findInverseSettingColumns(UMLClass klass) throws GenerationException
	{
		List<String> attrs = new ArrayList<String>();
		for(UMLAttribute attr: klass.getAttributes())
		{
			for(UMLTaggedValue tv: attr.getTaggedValues())
			{
				if (TV_INVERSE_ASSOC_COLUMN.equals(tv.getName()))
				{
					attrs.add(attr.getName());
				}
			}
		}
		
		return attrs;
	}
	
	public String getHibernateValidatorConstraints(UMLClass klass){
		
		ValidatorClass vClass = vModel.getClass(getFQCN(klass));
		ValidatorClass vClassExtension = vModelExtension.getClass(getFQCN(klass));
		
		String constraintAnnotationString="";
		
		if (vClass != null) 
			constraintAnnotationString = "\t" + vClass.getConstraintAnnotationString()+"\n";
		
		if (vClassExtension != null) 
			constraintAnnotationString += "\t" + vClassExtension.getConstraintAnnotationString()+"\n";
		
		return constraintAnnotationString;

	}
	
	public String getHibernateValidatorConstraints(UMLClass klass,UMLAttribute attr){
		
		ValidatorClass vClass = vModel.getClass(getFQCN(klass));
		ValidatorClass vClassExtension = vModelExtension.getClass(getFQCN(klass));
		
		List<String> cadsrConstraintAnnotations=new ArrayList<String>();
		List<String> userConstraintAnnotations=new ArrayList<String>();
		ValidatorAttribute vAttr=null;
		if (vClass != null) 
			vAttr=vClass.getAttribute(attr.getName());
		
		if (vAttr!=null)
			cadsrConstraintAnnotations.addAll(vAttr.getConstraintAnnotations());
		
		ValidatorAttribute vAttrExtension=null;
		if (vClassExtension != null) 
			vAttrExtension=vClassExtension.getAttribute(attr.getName());
		
		if (vAttrExtension!=null)
			userConstraintAnnotations.addAll(vAttrExtension.getConstraintAnnotations());
		
		//remove duplicates - user constraints override caDSR constraints
		List<String> constraintAnnotations=new ArrayList<String>();
		for(String cadsrConstraintAnnotation : cadsrConstraintAnnotations){
			String cadsrConstraintPrefix = cadsrConstraintAnnotation.indexOf("(") > 0 ? cadsrConstraintAnnotation.substring(0, cadsrConstraintAnnotation.indexOf("(")) : cadsrConstraintAnnotation;
			boolean duplicateConstraint = false;
			for(String userConstraintAnnotation : userConstraintAnnotations){
				if (userConstraintAnnotation.startsWith(cadsrConstraintPrefix)){
					duplicateConstraint = true;
					break;
				}
			}
			if (!duplicateConstraint)
				constraintAnnotations.add(cadsrConstraintAnnotation);
		}
		
		constraintAnnotations.addAll(userConstraintAnnotations);
		
		//Handle special @Patterns scenario
		List<String> patternConstraintAnnotations=new ArrayList<String>();
		for(String constraintAnnotation : constraintAnnotations){
			if (constraintAnnotation.indexOf("Pattern")>0){
				patternConstraintAnnotations.add(constraintAnnotation);
			}
		}
		
		StringBuilder sb;
		if (!patternConstraintAnnotations.isEmpty()){
			sb = new StringBuilder();
			constraintAnnotations.removeAll(patternConstraintAnnotations);

			sb.append(patternConstraintAnnotations.remove(0));
			for (String patternConstraintAnnotation:patternConstraintAnnotations){
				sb.append(",").append(patternConstraintAnnotation);
			}

			constraintAnnotations.add("@Patterns({"+sb.toString()+"})");
		}

		sb = new StringBuilder();
		for(String constraintAnnotation: constraintAnnotations){
			sb.append("\n\t").append(constraintAnnotation);
		}
		
		return sb.toString();
	}
	
	public Collection<String> getXSDRestrictionValues(UMLClass klass,UMLAttribute attr){
		
		ValidatorClass vClass = vModel.getClass(getFQCN(klass));
		ValidatorClass vClassExtension = vModelExtension.getClass(getFQCN(klass));
		
		ArrayList<String> permissibleValues = new ArrayList<String>();
		
		//get user supplied permissible value collection from validator extension file
		ValidatorAttribute vAttrExtension=null;
		if (vClassExtension != null)
			vAttrExtension =  vClassExtension.getAttribute(attr.getName());
		
		if (vAttrExtension != null) 
			permissibleValues.addAll(vAttrExtension.getXSDRestrictionCollection());
		
		//user supplied constraints override caDSR constraints, so only retrieve
		//caDSR constraints if user did not supply any constraints 
		if (permissibleValues.isEmpty()){
			ValidatorAttribute vAttr=null;
			if (vClass != null)
				vAttr =  vClass.getAttribute(attr.getName());
			
			if (vAttr != null) 
				permissibleValues.addAll(vAttr.getXSDRestrictionCollection());
		}
		
		return permissibleValues;
	}
	
	private Collection<String> getHibernateValidatorConstraintImports(UMLClass klass){
		
		ValidatorClass vClass = vModel.getClass(getFQCN(klass));
		ValidatorClass vClassExtension = vModelExtension.getClass(getFQCN(klass));
		
		Collection<String> constraintImports = new HashSet<String>();
		
		if (vClass != null)
			constraintImports.addAll(vClass.getConstraintImports());
		
		if (vClassExtension != null)
			constraintImports.addAll(vClassExtension.getConstraintImports());
		
		if (constraintImports.contains("org.hibernate.validator.Pattern"))
			constraintImports.add("org.hibernate.validator.Patterns");
		
		return constraintImports;
	}
	
	
	public String getNamespace(UMLTaggableElement te) throws GenerationException {
		String gmeNamespacePrefix = null;
		try {
			gmeNamespacePrefix = getTagValue(te,TV_NCI_GME_XML_NAMESPACE,null,0,1);
		} catch(GenerationException ge) {
			log.error("ERROR: ", ge);
			throw new GenerationException("Error getting the GME 'NCI_GME_XML_NAMESPACE' tag value for element", ge);
		}
		
		return gmeNamespacePrefix;
	}
	
	public String getGMENamespace(UMLClass klass) throws GenerationException{
		String gmeNamespace = null;
		try {
			gmeNamespace = getNamespace(klass);
			if (gmeNamespace!=null && gmeNamespace.length()>0)
				return gmeNamespace;
		} catch(GenerationException ge) {
			log.error("ERROR: ", ge);
			throw new GenerationException("Error getting the GME namespace for: " + getFQEN(klass), ge);
		}
		
		gmeNamespace=getGMENamespace(klass.getPackage());
		if (gmeNamespace!=null && gmeNamespace.length()>0)
			return gmeNamespace;
		
		log.error("GME Namespace name not found for: "+getFullPackageName(klass)+". Returning null");
		return null;
	}
	
	public String getGMENamespace(UMLPackage pkg) throws GenerationException{
		if (pkg==null)
			return null;
		
		log.debug("Getting Package Namespace for: " +pkg.getName());
		String gmeNamespace = getNamespace(pkg);
		if (gmeNamespace!=null && gmeNamespace.length()>0)
			return gmeNamespace;

		return getGMENamespace(pkg.getParent());
	}
	
	public boolean hasGMEXMLNamespaceTag(UMLTaggableElement te){
		try {
			getTagValue(te,TV_NCI_GME_XML_NAMESPACE,null,0,0);
		} catch (GenerationException e) {
			return true;
		}
		return false;
	}
	
	private String getNamespacePackageName(UMLTaggableElement te) throws GenerationException {
		String gmeNamespace = null;
		try {
			gmeNamespace = getTagValue(te,TV_NCI_GME_XML_NAMESPACE,null,0,1);
		} catch(GenerationException ge) {
			log.error("ERROR: ", ge);
			throw new GenerationException("Error getting the GME 'NCI_GME_XML_NAMESPACE' tag value for: " + getFQEN(te), ge);
		}
		
		if (gmeNamespace != null && gmeNamespace.lastIndexOf('/')<0)
			throw new GenerationException("Invalid GME Namespace found for:" + getFQEN(te)+": "+gmeNamespace);

		if (gmeNamespace!=null){
			return gmeNamespace.substring(gmeNamespace.lastIndexOf('/')+1, gmeNamespace.length());
		}
		
		return null;
	}
	
	public String getNamespacePrefix(UMLPackage pkg) throws GenerationException {
		String gmeNamespace = null;
		try {
			gmeNamespace = getTagValue(pkg,TV_NCI_GME_XML_NAMESPACE,null,0,1);
		} catch(GenerationException ge) {
			log.error("ERROR: ", ge);
			throw new GenerationException("Error getting the GME 'NCI_GME_XML_NAMESPACE' tag value for UML package: " + getFullPackageName(pkg), ge);
		}

		if (gmeNamespace != null && gmeNamespace.lastIndexOf('/')<0)
			throw new GenerationException("Invalid GME Namespace found for UML package " + getFullPackageName(pkg)+": "+gmeNamespace);

		if (gmeNamespace!=null){
			return gmeNamespace.substring(0,gmeNamespace.lastIndexOf('/'));
		}
		
		return null;
	}
	
	public String getXMLClassName(UMLClass klass) throws GenerationException {
		try {
			return getTagValue(klass,TV_NCI_GME_XML_ELEMENT,null,0,1);
		} catch(GenerationException ge) {
			log.error("ERROR: ", ge);
			throw new GenerationException("Error getting the GME 'NCI_GME_XML_ELEMENT' tag value for klass: " + klass.getName(), ge);
		}
	}
	
	public boolean hasGMEXMLClassTag(UMLTaggableElement te){
		try {
			getTagValue(te,TV_NCI_GME_XML_ELEMENT,null,0,0);
		} catch (GenerationException e) {
			return true;
		}
		return false;
	}
	
	public String getXMLAttributeName(UMLAttribute attr)throws GenerationException{
		try {
			  String attributeName = getTagValue(attr,TV_NCI_GME_XML_LOC_REF,null,0,1);
			  
			  if (attributeName !=null && attributeName.length()>0 && (attributeName.startsWith("@")))
				  attributeName=attributeName.substring(1); //remove leading '@' character
			  
			  return attributeName;
		} catch(GenerationException ge) {
			log.error("ERROR: ", ge);
			throw new GenerationException("Error getting the GME 'NCI_GME_XML_LOC_REF' tag value for attribute: " + attr.getName(), ge);
		}
	}
	
	public boolean generateXMLAttributeAsElement(UMLAttribute attr)throws GenerationException{
		try {
			  String attributeName = getTagValue(attr,TV_NCI_GME_XML_LOC_REF,null,0,1);
			  
			  if (attributeName !=null && attributeName.length()>0 && !(attributeName.startsWith("@")))
				  return true;
			  
			  return false;
		} catch(GenerationException ge) {
			log.error("ERROR: ", ge);
			throw new GenerationException("Error getting the GME 'NCI_GME_XML_LOC_REF' tag value for attribute: " + attr.getName(), ge);
		}
	}
	
	public boolean hasGMEXMLAttributeTag(UMLTaggableElement te){
		try {
			getTagValue(te,TV_NCI_GME_XML_LOC_REF,null,0,0);
		} catch (GenerationException e) {
			return true;
		}
		return false;
	}
	
	public String getXMLLocRef(UMLAssociationEnd assocEnd, String klassName)throws GenerationException
	{
		try {
			return getGmeLocRef(assocEnd.getOwningAssociation(),klassName);
		} catch(GenerationException ge) {
			log.error("ERROR: ", ge);
			throw new GenerationException("Error getting the GME 'NCI_GME_SOURCE_XML_LOC_REF' or 'NCI_GME_TARGET_XML_LOC_REF' tag value for association roleName: " + assocEnd.getRoleName(), ge);
		}
	}
	
	private String getGmeLocRef(UMLAssociation assoc,String klassName) throws GenerationException
	{
		String tv = getTagValue(assoc,TV_NCI_GME_SOURCE_XML_LOC_REF,null,0,1);
		if (tv !=null && tv.endsWith("/"+klassName)){
			return tv.substring(0, tv.lastIndexOf('/'));
		}
		
		tv = getTagValue(assoc,TV_NCI_GME_TARGET_XML_LOC_REF,null,0,1);
		if (tv !=null && tv.endsWith("/"+klassName)){
			return tv.substring(0, tv.lastIndexOf('/'));
		}
		
		return null;
	}
	
	public String getGmeSourceLocRef(UMLAssociation assoc) throws GenerationException
	{
		return getTagValue(assoc,TV_NCI_GME_SOURCE_XML_LOC_REF,null,0,1);
	}
	
	public String getGmeTargetLocRef(UMLAssociation assoc) throws GenerationException
	{
		return getTagValue(assoc,TV_NCI_GME_TARGET_XML_LOC_REF,null,0,1);
	}
	
	public boolean hasGMELocRefTag(UMLTaggableElement te){
		try {
			getTagValue(te,TV_NCI_GME_SOURCE_XML_LOC_REF,null,0,0);
			getTagValue(te,TV_NCI_GME_TARGET_XML_LOC_REF,null,0,0);
		} catch (GenerationException e) {
			return true;
		}
		return false;
	}
}