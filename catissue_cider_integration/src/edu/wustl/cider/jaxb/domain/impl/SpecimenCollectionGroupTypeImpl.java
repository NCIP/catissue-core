//
// This file was edu.wustl.cider.jaxb.domain by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// edu.wustl.cider.jaxb.domain on: 2011.06.16 at 07:41:18 GMT
//


package edu.wustl.cider.jaxb.domain.impl;

public class SpecimenCollectionGroupTypeImpl implements edu.wustl.cider.jaxb.domain.SpecimenCollectionGroupType, com.sun.xml.bind.JAXBObject, edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallableObject, edu.wustl.cider.jaxb.domain.impl.runtime.XMLSerializable, edu.wustl.cider.jaxb.domain.impl.runtime.ValidatableObject
{

    protected java.lang.String _AccessionNumber;
    protected java.lang.String _ClinicalDiagnosis;
    protected java.lang.String _ClinicalStatus;
    protected edu.wustl.cider.jaxb.domain.SpecimenCollectionType _SpecimenCollection;
    public final static java.lang.Class version = (edu.wustl.cider.jaxb.domain.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.wustl.cider.jaxb.domain.SpecimenCollectionGroupType.class);
    }

    public java.lang.String getAccessionNumber() {
        return _AccessionNumber;
    }

    public void setAccessionNumber(java.lang.String value) {
        _AccessionNumber = value;
    }

    public java.lang.String getClinicalDiagnosis() {
        return _ClinicalDiagnosis;
    }

    public void setClinicalDiagnosis(java.lang.String value) {
        _ClinicalDiagnosis = value;
    }

    public java.lang.String getClinicalStatus() {
        return _ClinicalStatus;
    }

    public void setClinicalStatus(java.lang.String value) {
        _ClinicalStatus = value;
    }

    public edu.wustl.cider.jaxb.domain.SpecimenCollectionType getSpecimenCollection() {
        return _SpecimenCollection;
    }

    public void setSpecimenCollection(edu.wustl.cider.jaxb.domain.SpecimenCollectionType value) {
        _SpecimenCollection = value;
    }

    public edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallingContext context) {
        return new edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionGroupTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.wustl.cider.jaxb.domain.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_AccessionNumber!= null) {
            context.startElement("", "AccessionNumber");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text((_AccessionNumber), "AccessionNumber");
            } catch (java.lang.Exception e) {
                edu.wustl.cider.jaxb.domain.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_ClinicalDiagnosis!= null) {
            context.startElement("", "ClinicalDiagnosis");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text((_ClinicalDiagnosis), "ClinicalDiagnosis");
            } catch (java.lang.Exception e) {
                edu.wustl.cider.jaxb.domain.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_ClinicalStatus!= null) {
            context.startElement("", "ClinicalStatus");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text((_ClinicalStatus), "ClinicalStatus");
            } catch (java.lang.Exception e) {
                edu.wustl.cider.jaxb.domain.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_SpecimenCollection!= null) {
            context.startElement("", "SpecimenCollection");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _SpecimenCollection), "SpecimenCollection");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _SpecimenCollection), "SpecimenCollection");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _SpecimenCollection), "SpecimenCollection");
            context.endElement();
        }
    }

    public void serializeAttributes(edu.wustl.cider.jaxb.domain.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.wustl.cider.jaxb.domain.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.wustl.cider.jaxb.domain.SpecimenCollectionGroupType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\u001dcom.sun.msv.grammar."
+"ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000\'com.sun.msv.grammar.trex.Ele"
+"mentPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/Na"
+"meClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aigno"
+"reUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lan"
+"g.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.gra"
+"mmar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatyp"
+"e;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000"
+"\u0003ppsr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAl"
+"waysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr"
+"\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnames"
+"paceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u0019L\u0000\nwhiteSpacet\u0000."
+"Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://ww"
+"w.w3.org/2001/XMLSchemat\u0000\u0006stringsr\u00005com.sun.msv.datatype.xsd"
+".WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.data"
+"type.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv.gr"
+"ammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom"
+".sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0019L\u0000\fnames"
+"paceURIq\u0000~\u0000\u0019xpq\u0000~\u0000\u001dq\u0000~\u0000\u001csq\u0000~\u0000\bppsr\u0000 com.sun.msv.grammar.Attr"
+"ibuteExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u000bxq\u0000~\u0000\u0003q\u0000~\u0000\u000fps"
+"q\u0000~\u0000\u0011ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~"
+"\u0000\u0016q\u0000~\u0000\u001ct\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProces"
+"sor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001fq\u0000~\u0000\"sq\u0000~\u0000#q\u0000~\u0000+q\u0000~\u0000\u001csr\u0000#com.su"
+"n.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0019L\u0000\f"
+"namespaceURIq\u0000~\u0000\u0019xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u00000co"
+"m.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000"
+"~\u0000\u0003sq\u0000~\u0000\u000e\u0001psq\u0000~\u0000/t\u0000\u000fAccessionNumbert\u0000\u0000q\u0000~\u00005sq\u0000~\u0000\bppsq\u0000~\u0000\nq\u0000~"
+"\u0000\u000fp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0014sq\u0000~\u0000\bppsq\u0000~\u0000&q\u0000~\u0000\u000fpq\u0000~\u0000(q\u0000~\u00001q\u0000~\u00005sq\u0000~\u0000/t\u0000"
+"\u0011ClinicalDiagnosisq\u0000~\u00009q\u0000~\u00005sq\u0000~\u0000\bppsq\u0000~\u0000\nq\u0000~\u0000\u000fp\u0000sq\u0000~\u0000\u0000ppq\u0000~"
+"\u0000\u0014sq\u0000~\u0000\bppsq\u0000~\u0000&q\u0000~\u0000\u000fpq\u0000~\u0000(q\u0000~\u00001q\u0000~\u00005sq\u0000~\u0000/t\u0000\u000eClinicalStatus"
+"q\u0000~\u00009q\u0000~\u00005sq\u0000~\u0000\bppsq\u0000~\u0000\nq\u0000~\u0000\u000fp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\npp\u0000sq\u0000~\u0000\bppsr\u0000 "
+"com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.g"
+"rammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000\u000fpsq\u0000~\u0000&q\u0000~\u0000\u000f"
+"psr\u00002com.sun.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u00006psr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00000q\u0000~\u00005sq\u0000~\u0000/t\u0000 edu.wustl.cider.jaxb.domain.SpecimenCollectionTypet\u0000+"
+"http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\bppsq\u0000~\u0000&q\u0000~"
+"\u0000\u000fpq\u0000~\u0000(q\u0000~\u00001q\u0000~\u00005sq\u0000~\u0000/t\u0000\u0012SpecimenCollectionq\u0000~\u00009q\u0000~\u00005sr\u0000\"c"
+"om.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lc"
+"om/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.m"
+"sv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rst"
+"reamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;x"
+"p\u0000\u0000\u0000\u0011\u0001pq\u0000~\u0000\tq\u0000~\u0000:q\u0000~\u0000Aq\u0000~\u0000Jq\u0000~\u0000\u0006q\u0000~\u0000Lq\u0000~\u0000Oq\u0000~\u0000\u0010q\u0000~\u0000<q\u0000~\u0000Cq\u0000~"
+"\u0000\u0005q\u0000~\u0000\u0007q\u0000~\u0000%q\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Xq\u0000~\u0000Hx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.wustl.cider.jaxb.domain.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------");
        }

        protected Unmarshaller(edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionGroupTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("ClinicalDiagnosis" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  0 :
                        if (("AccessionNumber" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  6 :
                        if (("ClinicalStatus" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  9 :
                        if (("SpecimenCollection" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 10;
                            return ;
                        }
                        state = 12;
                        continue outer;
                    case  12 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  10 :
                        if (("Specimen" == ___local)&&("" == ___uri)) {
                            _SpecimenCollection = ((edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionTypeImpl) spawnChildFromEnterElement((edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionTypeImpl.class), 11, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        _SpecimenCollection = ((edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionTypeImpl) spawnChildFromEnterElement((edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionTypeImpl.class), 11, ___uri, ___local, ___qname, __atts));
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
                    case  3 :
                        state = 6;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  8 :
                        if (("ClinicalStatus" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  11 :
                        if (("SpecimenCollection" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  2 :
                        if (("AccessionNumber" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  12 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  5 :
                        if (("ClinicalDiagnosis" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  10 :
                        _SpecimenCollection = ((edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionTypeImpl) spawnChildFromLeaveElement((edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionTypeImpl.class), 11, ___uri, ___local, ___qname));
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
                    case  3 :
                        state = 6;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  12 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  10 :
                        _SpecimenCollection = ((edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionTypeImpl) spawnChildFromEnterAttribute((edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionTypeImpl.class), 11, ___uri, ___local, ___qname));
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
                    case  3 :
                        state = 6;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  12 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  10 :
                        _SpecimenCollection = ((edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionTypeImpl) spawnChildFromLeaveAttribute((edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionTypeImpl.class), 11, ___uri, ___local, ___qname));
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
                        case  3 :
                            state = 6;
                            continue outer;
                        case  1 :
                            eatText1(value);
                            state = 2;
                            return ;
                        case  0 :
                            state = 3;
                            continue outer;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  7 :
                            eatText2(value);
                            state = 8;
                            return ;
                        case  9 :
                            state = 12;
                            continue outer;
                        case  12 :
                            revertToParentFromText(value);
                            return ;
                        case  4 :
                            eatText3(value);
                            state = 5;
                            return ;
                        case  10 :
                            _SpecimenCollection = ((edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionTypeImpl) spawnChildFromText((edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionTypeImpl.class), 11, value));
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
                _AccessionNumber = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ClinicalStatus = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ClinicalDiagnosis = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
