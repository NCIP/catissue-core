//
// This file was edu.wustl.cider.jaxb.domain by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// edu.wustl.cider.jaxb.domain on: 2011.06.16 at 07:41:18 GMT
//


package edu.wustl.cider.jaxb.domain.impl;

public class SpecimenTypeImpl implements edu.wustl.cider.jaxb.domain.SpecimenType, com.sun.xml.bind.JAXBObject, edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallableObject, edu.wustl.cider.jaxb.domain.impl.runtime.XMLSerializable, edu.wustl.cider.jaxb.domain.impl.runtime.ValidatableObject
{

    protected java.lang.String _Type;
    protected edu.wustl.cider.jaxb.domain.SpecimenPositionType _SpecimenPosition;
    protected java.lang.String _PathologicalStatus;
    protected edu.wustl.cider.jaxb.domain.SpecimenEventsType _SpecimenEvents;
    protected boolean has_IsAvailable;
    protected boolean _IsAvailable;
    protected java.lang.String _ClassName;
    protected boolean has_Quantity;
    protected double _Quantity;
    protected boolean has_Id;
    protected long _Id;
    protected edu.wustl.cider.jaxb.domain.SpecimenCharacteristicsType _SpecimenCharacteristics;
    public final static java.lang.Class version = (edu.wustl.cider.jaxb.domain.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.wustl.cider.jaxb.domain.SpecimenType.class);
    }

    public java.lang.String getType() {
        return _Type;
    }

    public void setType(java.lang.String value) {
        _Type = value;
    }

    public edu.wustl.cider.jaxb.domain.SpecimenPositionType getSpecimenPosition() {
        return _SpecimenPosition;
    }

    public void setSpecimenPosition(edu.wustl.cider.jaxb.domain.SpecimenPositionType value) {
        _SpecimenPosition = value;
    }

    public java.lang.String getPathologicalStatus() {
        return _PathologicalStatus;
    }

    public void setPathologicalStatus(java.lang.String value) {
        _PathologicalStatus = value;
    }

    public edu.wustl.cider.jaxb.domain.SpecimenEventsType getSpecimenEvents() {
        return _SpecimenEvents;
    }

    public void setSpecimenEvents(edu.wustl.cider.jaxb.domain.SpecimenEventsType value) {
        _SpecimenEvents = value;
    }

    public boolean isIsAvailable() {
        return _IsAvailable;
    }

    public void setIsAvailable(boolean value) {
        _IsAvailable = value;
        has_IsAvailable = true;
    }

    public java.lang.String getClassName() {
        return _ClassName;
    }

    public void setClassName(java.lang.String value) {
        _ClassName = value;
    }

    public double getQuantity() {
        return _Quantity;
    }

    public void setQuantity(double value) {
        _Quantity = value;
        has_Quantity = true;
    }

    public long getId() {
        return _Id;
    }

    public void setId(long value) {
        _Id = value;
        has_Id = true;
    }

    public edu.wustl.cider.jaxb.domain.SpecimenCharacteristicsType getSpecimenCharacteristics() {
        return _SpecimenCharacteristics;
    }

    public void setSpecimenCharacteristics(edu.wustl.cider.jaxb.domain.SpecimenCharacteristicsType value) {
        _SpecimenCharacteristics = value;
    }

    public edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallingContext context) {
        return new edu.wustl.cider.jaxb.domain.impl.SpecimenTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.wustl.cider.jaxb.domain.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_IsAvailable) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "IsAvailable"));
        }
        if (!has_Quantity) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Quantity"));
        }
        if (!has_Id) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Id"));
        }
        context.startElement("", "Id");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printLong((_Id)), "Id");
        } catch (java.lang.Exception e) {
            edu.wustl.cider.jaxb.domain.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "ClassName");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text((_ClassName), "ClassName");
        } catch (java.lang.Exception e) {
            edu.wustl.cider.jaxb.domain.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "Type");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text((_Type), "Type");
        } catch (java.lang.Exception e) {
            edu.wustl.cider.jaxb.domain.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "PathologicalStatus");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text((_PathologicalStatus), "PathologicalStatus");
        } catch (java.lang.Exception e) {
            edu.wustl.cider.jaxb.domain.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "Quantity");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printDouble((_Quantity)), "Quantity");
        } catch (java.lang.Exception e) {
            edu.wustl.cider.jaxb.domain.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "IsAvailable");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printBoolean((_IsAvailable)), "IsAvailable");
        } catch (java.lang.Exception e) {
            edu.wustl.cider.jaxb.domain.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        if (_SpecimenCharacteristics!= null) {
            context.startElement("", "SpecimenCharacteristics");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _SpecimenCharacteristics), "SpecimenCharacteristics");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _SpecimenCharacteristics), "SpecimenCharacteristics");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _SpecimenCharacteristics), "SpecimenCharacteristics");
            context.endElement();
        }
        if (_SpecimenPosition!= null) {
            context.startElement("", "SpecimenPosition");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _SpecimenPosition), "SpecimenPosition");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _SpecimenPosition), "SpecimenPosition");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _SpecimenPosition), "SpecimenPosition");
            context.endElement();
        }
        if (_SpecimenEvents!= null) {
            context.startElement("", "SpecimenEvents");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _SpecimenEvents), "SpecimenEvents");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _SpecimenEvents), "SpecimenEvents");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _SpecimenEvents), "SpecimenEvents");
            context.endElement();
        }
    }

    public void serializeAttributes(edu.wustl.cider.jaxb.domain.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_IsAvailable) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "IsAvailable"));
        }
        if (!has_Quantity) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Quantity"));
        }
        if (!has_Id) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Id"));
        }
    }

    public void serializeURIs(edu.wustl.cider.jaxb.domain.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_IsAvailable) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "IsAvailable"));
        }
        if (!has_Quantity) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Quantity"));
        }
        if (!has_Id) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Id"));
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.wustl.cider.jaxb.domain.SpecimenType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000pp"
+"sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001e"
+"com.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclared"
+"AttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.m"
+"sv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/D"
+"atatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair"
+";xq\u0000~\u0000\u0003ppsr\u0000!com.sun.msv.datatype.xsd.LongType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000"
+"+com.sun.msv.datatype.xsd.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nba"
+"seFacetst\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;xr\u0000*com."
+"sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun"
+".msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.dat"
+"atype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/l"
+"ang/String;L\u0000\btypeNameq\u0000~\u0000\u001cL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/data"
+"type/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/XML"
+"Schemat\u0000\u0004longsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcesso"
+"r$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpace"
+"Processor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u0000*com.sun.msv.datatype.xsd.MaxInclus"
+"iveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.RangeFacet\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012Ljava/lang/Object;xr\u00009com.sun.msv.d"
+"atatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*c"
+"om.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFac"
+"etFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypeq\u0000~\u0000\u0018L\u0000\fconcreteTypet"
+"\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000\u001cxq"
+"\u0000~\u0000\u001bppq\u0000~\u0000#\u0000\u0001sr\u0000*com.sun.msv.datatype.xsd.MinInclusiveFacet\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000%ppq\u0000~\u0000#\u0000\u0000sr\u0000$com.sun.msv.datatype.xsd.Intege"
+"rType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0017q\u0000~\u0000\u001ft\u0000\u0007integerq\u0000~\u0000#sr\u0000,com.sun.msv.da"
+"tatype.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun"
+".msv.datatype.xsd.DataTypeWithLexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea"
+"\u0002\u0000\u0000xq\u0000~\u0000(ppq\u0000~\u0000#\u0001\u0000sr\u0000#com.sun.msv.datatype.xsd.NumberType\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0019q\u0000~\u0000\u001ft\u0000\u0007decimalq\u0000~\u0000#q\u0000~\u00004t\u0000\u000efractionDigits\u0000\u0000\u0000\u0000"
+"q\u0000~\u0000.t\u0000\fminInclusivesr\u0000\u000ejava.lang.Long;\u008b\u00e4\u0090\u00cc\u008f#\u00df\u0002\u0000\u0001J\u0000\u0005valuexr\u0000"
+"\u0010java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xp\u0080\u0000\u0000\u0000\u0000\u0000\u0000\u0000q\u0000~\u0000.t\u0000\fmaxInclusivesq"
+"\u0000~\u00008\u007f\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ffsr\u00000com.sun.msv.grammar.Expression$NullSetExpres"
+"sion\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f"
+"\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001cL\u0000\fnamespaceURIq\u0000~\u0000\u001cxpq\u0000~\u0000 q\u0000~\u0000\u001fsr\u0000\u001dco"
+"m.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.ms"
+"v.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000"
+"\u000exq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\u0012pps"
+"r\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0019q\u0000~\u0000\u001ft"
+"\u0000\u0005QNameq\u0000~\u0000#q\u0000~\u0000>sq\u0000~\u0000?q\u0000~\u0000Jq\u0000~\u0000\u001fsr\u0000#com.sun.msv.grammar.Sim"
+"pleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001cL\u0000\fnamespaceURIq\u0000~\u0000\u001c"
+"xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http"
+"://www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.grammar"
+".Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000E\u0001psq\u0000~\u0000L"
+"t\u0000\u0002Idt\u0000\u0000sq\u0000~\u0000\rpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0012ppsr\u0000#com.sun.msv.datatype.xs"
+"d.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000\u0019q\u0000~\u0000\u001ft\u0000\u0006strings"
+"r\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\"\u0001q\u0000~\u0000>sq\u0000~\u0000?q\u0000~\u0000\\q\u0000~\u0000\u001fsq\u0000~\u0000Appsq\u0000~\u0000Cq\u0000~\u0000Fpq\u0000~\u0000G"
+"q\u0000~\u0000Nq\u0000~\u0000Rsq\u0000~\u0000Lt\u0000\tClassNameq\u0000~\u0000Vsq\u0000~\u0000\rpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000Ysq\u0000~\u0000"
+"Appsq\u0000~\u0000Cq\u0000~\u0000Fpq\u0000~\u0000Gq\u0000~\u0000Nq\u0000~\u0000Rsq\u0000~\u0000Lt\u0000\u0004Typeq\u0000~\u0000Vsq\u0000~\u0000\rpp\u0000sq\u0000"
+"~\u0000\u0000ppq\u0000~\u0000Ysq\u0000~\u0000Appsq\u0000~\u0000Cq\u0000~\u0000Fpq\u0000~\u0000Gq\u0000~\u0000Nq\u0000~\u0000Rsq\u0000~\u0000Lt\u0000\u0012Pathol"
+"ogicalStatusq\u0000~\u0000Vsq\u0000~\u0000\rpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0012ppsr\u0000#com.sun.msv.da"
+"tatype.xsd.DoubleType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.datatype.xsd"
+".FloatingNumberType\u00fc\u00e3\u00b6\u0087\u008c\u00a8|\u00e0\u0002\u0000\u0000xq\u0000~\u0000\u0019q\u0000~\u0000\u001ft\u0000\u0006doubleq\u0000~\u0000#q\u0000~\u0000>"
+"sq\u0000~\u0000?q\u0000~\u0000vq\u0000~\u0000\u001fsq\u0000~\u0000Appsq\u0000~\u0000Cq\u0000~\u0000Fpq\u0000~\u0000Gq\u0000~\u0000Nq\u0000~\u0000Rsq\u0000~\u0000Lt\u0000\b"
+"Quantityq\u0000~\u0000Vsq\u0000~\u0000\rpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0012ppsr\u0000$com.sun.msv.dataty"
+"pe.xsd.BooleanType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0019q\u0000~\u0000\u001ft\u0000\u0007booleanq\u0000~\u0000#q\u0000~\u0000>"
+"sq\u0000~\u0000?q\u0000~\u0000\u0081q\u0000~\u0000\u001fsq\u0000~\u0000Appsq\u0000~\u0000Cq\u0000~\u0000Fpq\u0000~\u0000Gq\u0000~\u0000Nq\u0000~\u0000Rsq\u0000~\u0000Lt\u0000\u000b"
+"IsAvailableq\u0000~\u0000Vsq\u0000~\u0000Appsq\u0000~\u0000\rq\u0000~\u0000Fp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\rpp\u0000sq\u0000~\u0000A"
+"ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun"
+".msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000Fpsq\u0000~\u0000"
+"Cq\u0000~\u0000Fpsr\u00002com.sun.msv.grammar.Expression$AnyStringExpressio"
+"n\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000Spsr\u0000 com.sun.msv.grammar.AnyNameClass"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Mq\u0000~\u0000Rsq\u0000~\u0000Lt\u0000%edu.wustl.cider.jaxb.domain.SpecimenCharacteris"
+"ticsTypet\u0000+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000A"
+"ppsq\u0000~\u0000Cq\u0000~\u0000Fpq\u0000~\u0000Gq\u0000~\u0000Nq\u0000~\u0000Rsq\u0000~\u0000Lt\u0000\u0017SpecimenCharacteristic"
+"sq\u0000~\u0000Vq\u0000~\u0000Rsq\u0000~\u0000Appsq\u0000~\u0000\rq\u0000~\u0000Fp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\rpp\u0000sq\u0000~\u0000Appsq\u0000"
+"~\u0000\u008cq\u0000~\u0000Fpsq\u0000~\u0000Cq\u0000~\u0000Fpq\u0000~\u0000\u0091q\u0000~\u0000\u0093q\u0000~\u0000Rsq\u0000~\u0000Lt\u0000\u001eedu.wustl.cider.jaxb.domain.Speci"
+"menPositionTypeq\u0000~\u0000\u0096sq\u0000~\u0000Appsq\u0000~\u0000Cq\u0000~\u0000Fpq\u0000~\u0000Gq\u0000~\u0000Nq\u0000~\u0000Rsq\u0000~\u0000"
+"Lt\u0000\u0010SpecimenPositionq\u0000~\u0000Vq\u0000~\u0000Rsq\u0000~\u0000Appsq\u0000~\u0000\rq\u0000~\u0000Fp\u0000sq\u0000~\u0000\u0000pps"
+"q\u0000~\u0000\rpp\u0000sq\u0000~\u0000Appsq\u0000~\u0000\u008cq\u0000~\u0000Fpsq\u0000~\u0000Cq\u0000~\u0000Fpq\u0000~\u0000\u0091q\u0000~\u0000\u0093q\u0000~\u0000Rsq\u0000~\u0000"
+"Lt\u0000\u001cedu.wustl.cider.jaxb.domain.SpecimenEventsTypeq\u0000~\u0000\u0096sq\u0000~\u0000Appsq\u0000~\u0000Cq\u0000~\u0000Fpq\u0000~"
+"\u0000Gq\u0000~\u0000Nq\u0000~\u0000Rsq\u0000~\u0000Lt\u0000\u000eSpecimenEventsq\u0000~\u0000Vq\u0000~\u0000Rsr\u0000\"com.sun.msv"
+".grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv"
+"/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar"
+".ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersio"
+"nL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000#\u0001pq\u0000~"
+"\u0000}q\u0000~\u0000\tq\u0000~\u0000\u0089q\u0000~\u0000\u009dq\u0000~\u0000\u00aaq\u0000~\u0000\u008bq\u0000~\u0000\u008eq\u0000~\u0000\u00a0q\u0000~\u0000\u009fq\u0000~\u0000\u00adq\u0000~\u0000\u00acq\u0000~\u0000\u000bq\u0000~"
+"\u0000Xq\u0000~\u0000eq\u0000~\u0000kq\u0000~\u0000\u0011q\u0000~\u0000\bq\u0000~\u0000\u0007q\u0000~\u0000qq\u0000~\u0000\u0005q\u0000~\u0000\u0006q\u0000~\u0000\fq\u0000~\u0000Bq\u0000~\u0000`q\u0000~"
+"\u0000fq\u0000~\u0000lq\u0000~\u0000xq\u0000~\u0000\u0083q\u0000~\u0000\nq\u0000~\u0000\u0097q\u0000~\u0000\u00a4q\u0000~\u0000\u00b1q\u0000~\u0000\u0087q\u0000~\u0000\u009bq\u0000~\u0000\u00a8x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.wustl.cider.jaxb.domain.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallingContext context) {
            super(context, "----------------------------");
        }

        protected Unmarshaller(edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.wustl.cider.jaxb.domain.impl.SpecimenTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  6 :
                        if (("Type" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        break;
                    case  24 :
                        if (("SpecimenEvents" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 25;
                            return ;
                        }
                        state = 27;
                        continue outer;
                    case  9 :
                        if (("PathologicalStatus" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        break;
                    case  18 :
                        if (("SpecimenCharacteristics" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 19;
                            return ;
                        }
                        state = 21;
                        continue outer;
                    case  15 :
                        if (("IsAvailable" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 16;
                            return ;
                        }
                        break;
                    case  27 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  12 :
                        if (("Quantity" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        break;
                    case  3 :
                        if (("ClassName" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        break;
                    case  19 :
                        if (("TissueSite" == ___local)&&("" == ___uri)) {
                            _SpecimenCharacteristics = ((edu.wustl.cider.jaxb.domain.impl.SpecimenCharacteristicsTypeImpl) spawnChildFromEnterElement((edu.wustl.cider.jaxb.domain.impl.SpecimenCharacteristicsTypeImpl.class), 20, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  0 :
                        if (("Id" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        break;
                    case  21 :
                        if (("SpecimenPosition" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 22;
                            return ;
                        }
                        state = 24;
                        continue outer;
                    case  25 :
                        if (("CollectionEvent" == ___local)&&("" == ___uri)) {
                            _SpecimenEvents = ((edu.wustl.cider.jaxb.domain.impl.SpecimenEventsTypeImpl) spawnChildFromEnterElement((edu.wustl.cider.jaxb.domain.impl.SpecimenEventsTypeImpl.class), 26, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        _SpecimenEvents = ((edu.wustl.cider.jaxb.domain.impl.SpecimenEventsTypeImpl) spawnChildFromEnterElement((edu.wustl.cider.jaxb.domain.impl.SpecimenEventsTypeImpl.class), 26, ___uri, ___local, ___qname, __atts));
                        return ;
                    case  22 :
                        if (("StorageContainer" == ___local)&&("" == ___uri)) {
                            _SpecimenPosition = ((edu.wustl.cider.jaxb.domain.impl.SpecimenPositionTypeImpl) spawnChildFromEnterElement((edu.wustl.cider.jaxb.domain.impl.SpecimenPositionTypeImpl.class), 23, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        _SpecimenPosition = ((edu.wustl.cider.jaxb.domain.impl.SpecimenPositionTypeImpl) spawnChildFromEnterElement((edu.wustl.cider.jaxb.domain.impl.SpecimenPositionTypeImpl.class), 23, ___uri, ___local, ___qname, __atts));
                        return ;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  8 :
                        if (("Type" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  24 :
                        state = 27;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  27 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  5 :
                        if (("ClassName" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  14 :
                        if (("Quantity" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  26 :
                        if (("SpecimenEvents" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 27;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("Id" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  20 :
                        if (("SpecimenCharacteristics" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 21;
                            return ;
                        }
                        break;
                    case  17 :
                        if (("IsAvailable" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 18;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("PathologicalStatus" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  23 :
                        if (("SpecimenPosition" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 24;
                            return ;
                        }
                        break;
                    case  21 :
                        state = 24;
                        continue outer;
                    case  25 :
                        _SpecimenEvents = ((edu.wustl.cider.jaxb.domain.impl.SpecimenEventsTypeImpl) spawnChildFromLeaveElement((edu.wustl.cider.jaxb.domain.impl.SpecimenEventsTypeImpl.class), 26, ___uri, ___local, ___qname));
                        return ;
                    case  22 :
                        _SpecimenPosition = ((edu.wustl.cider.jaxb.domain.impl.SpecimenPositionTypeImpl) spawnChildFromLeaveElement((edu.wustl.cider.jaxb.domain.impl.SpecimenPositionTypeImpl.class), 23, ___uri, ___local, ___qname));
                        return ;
                }
                super.leaveElement(___uri, ___local, ___qname);
                break;
            }
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  24 :
                        state = 27;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  27 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  21 :
                        state = 24;
                        continue outer;
                    case  25 :
                        _SpecimenEvents = ((edu.wustl.cider.jaxb.domain.impl.SpecimenEventsTypeImpl) spawnChildFromEnterAttribute((edu.wustl.cider.jaxb.domain.impl.SpecimenEventsTypeImpl.class), 26, ___uri, ___local, ___qname));
                        return ;
                    case  22 :
                        _SpecimenPosition = ((edu.wustl.cider.jaxb.domain.impl.SpecimenPositionTypeImpl) spawnChildFromEnterAttribute((edu.wustl.cider.jaxb.domain.impl.SpecimenPositionTypeImpl.class), 23, ___uri, ___local, ___qname));
                        return ;
                }
                super.enterAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  24 :
                        state = 27;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  27 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  21 :
                        state = 24;
                        continue outer;
                    case  25 :
                        _SpecimenEvents = ((edu.wustl.cider.jaxb.domain.impl.SpecimenEventsTypeImpl) spawnChildFromLeaveAttribute((edu.wustl.cider.jaxb.domain.impl.SpecimenEventsTypeImpl.class), 26, ___uri, ___local, ___qname));
                        return ;
                    case  22 :
                        _SpecimenPosition = ((edu.wustl.cider.jaxb.domain.impl.SpecimenPositionTypeImpl) spawnChildFromLeaveAttribute((edu.wustl.cider.jaxb.domain.impl.SpecimenPositionTypeImpl.class), 23, ___uri, ___local, ___qname));
                        return ;
                }
                super.leaveAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void handleText(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                try {
                    switch (state) {
                        case  16 :
                            eatText1(value);
                            state = 17;
                            return ;
                        case  13 :
                            eatText2(value);
                            state = 14;
                            return ;
                        case  24 :
                            state = 27;
                            continue outer;
                        case  18 :
                            state = 21;
                            continue outer;
                        case  7 :
                            eatText3(value);
                            state = 8;
                            return ;
                        case  27 :
                            revertToParentFromText(value);
                            return ;
                        case  10 :
                            eatText4(value);
                            state = 11;
                            return ;
                        case  4 :
                            eatText5(value);
                            state = 5;
                            return ;
                        case  21 :
                            state = 24;
                            continue outer;
                        case  1 :
                            eatText6(value);
                            state = 2;
                            return ;
                        case  25 :
                            _SpecimenEvents = ((edu.wustl.cider.jaxb.domain.impl.SpecimenEventsTypeImpl) spawnChildFromText((edu.wustl.cider.jaxb.domain.impl.SpecimenEventsTypeImpl.class), 26, value));
                            return ;
                        case  22 :
                            _SpecimenPosition = ((edu.wustl.cider.jaxb.domain.impl.SpecimenPositionTypeImpl) spawnChildFromText((edu.wustl.cider.jaxb.domain.impl.SpecimenPositionTypeImpl.class), 23, value));
                            return ;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

        private void eatText1(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _IsAvailable = javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_IsAvailable = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Quantity = javax.xml.bind.DatatypeConverter.parseDouble(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_Quantity = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Type = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _PathologicalStatus = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ClassName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Id = javax.xml.bind.DatatypeConverter.parseLong(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_Id = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
