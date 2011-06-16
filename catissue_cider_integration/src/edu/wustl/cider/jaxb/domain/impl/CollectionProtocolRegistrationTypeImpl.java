//
// This file was edu.wustl.cider.jaxb.domain by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// edu.wustl.cider.jaxb.domain on: 2011.06.16 at 07:41:18 GMT
//


package edu.wustl.cider.jaxb.domain.impl;

public class CollectionProtocolRegistrationTypeImpl implements edu.wustl.cider.jaxb.domain.CollectionProtocolRegistrationType, com.sun.xml.bind.JAXBObject, edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallableObject, edu.wustl.cider.jaxb.domain.impl.runtime.XMLSerializable, edu.wustl.cider.jaxb.domain.impl.runtime.ValidatableObject
{

    protected edu.wustl.cider.jaxb.domain.CollectionProtocolType _CollectionProtocol;
    protected edu.wustl.cider.jaxb.domain.SCGCollectionType _SCGCollection;
    public final static java.lang.Class version = (edu.wustl.cider.jaxb.domain.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.wustl.cider.jaxb.domain.CollectionProtocolRegistrationType.class);
    }

    public edu.wustl.cider.jaxb.domain.CollectionProtocolType getCollectionProtocol() {
        return _CollectionProtocol;
    }

    public void setCollectionProtocol(edu.wustl.cider.jaxb.domain.CollectionProtocolType value) {
        _CollectionProtocol = value;
    }

    public edu.wustl.cider.jaxb.domain.SCGCollectionType getSCGCollection() {
        return _SCGCollection;
    }

    public void setSCGCollection(edu.wustl.cider.jaxb.domain.SCGCollectionType value) {
        _SCGCollection = value;
    }

    public edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallingContext context) {
        return new edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.wustl.cider.jaxb.domain.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_CollectionProtocol!= null) {
            context.startElement("", "CollectionProtocol");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _CollectionProtocol), "CollectionProtocol");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _CollectionProtocol), "CollectionProtocol");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _CollectionProtocol), "CollectionProtocol");
            context.endElement();
        }
        if (_SCGCollection!= null) {
            context.startElement("", "SCGCollection");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _SCGCollection), "SCGCollection");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _SCGCollection), "SCGCollection");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _SCGCollection), "SCGCollection");
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
        return (edu.wustl.cider.jaxb.domain.CollectionProtocolRegistrationType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom."
+"sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttr"
+"ibutesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa"
+"\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\bpp\u0000sq\u0000~\u0000\u0006ppsr\u0000 com.sun.msv.gr"
+"ammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryEx"
+"p\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000\rpsr\u0000 com.sun.msv.grammar.A"
+"ttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\txq\u0000~\u0000\u0003q\u0000~\u0000"
+"\rpsr\u00002com.sun.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\f\u0001psr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000co"
+"m.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000"
+"~\u0000\u0003q\u0000~\u0000\u0018psr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L"
+"\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000\u001fxq\u0000~\u0000\u001at\u0000"
+" edu.wustl.cider.jaxb.domain.CollectionProtocolTypet\u0000+http://java.sun.com/jaxb"
+"/xjc/dummy-elementssq\u0000~\u0000\u0006ppsq\u0000~\u0000\u0014q\u0000~\u0000\rpsr\u0000\u001bcom.sun.msv.gramm"
+"ar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;"
+"L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003p"
+"psr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.su"
+"n.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.m"
+"sv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datat"
+"ype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u001fL\u0000\btype"
+"Nameq\u0000~\u0000\u001fL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpac"
+"eProcessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005"
+"com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000"
+"\tlocalNameq\u0000~\u0000\u001fL\u0000\fnamespaceURIq\u0000~\u0000\u001fxpq\u0000~\u00000q\u0000~\u0000/sq\u0000~\u0000\u001et\u0000\u0004type"
+"t\u0000)http://www.w3.org/2001/XMLSchema-instanceq\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u0000\u0012Co"
+"llectionProtocolt\u0000\u0000q\u0000~\u0000\u001dsq\u0000~\u0000\u0006ppsq\u0000~\u0000\bq\u0000~\u0000\rp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\bp"
+"p\u0000sq\u0000~\u0000\u0006ppsq\u0000~\u0000\u0011q\u0000~\u0000\rpsq\u0000~\u0000\u0014q\u0000~\u0000\rpq\u0000~\u0000\u0017q\u0000~\u0000\u001bq\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u0000\u001bge"
+"nerated.SCGCollectionTypeq\u0000~\u0000\"sq\u0000~\u0000\u0006ppsq\u0000~\u0000\u0014q\u0000~\u0000\rpq\u0000~\u0000(q\u0000~\u00008"
+"q\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u0000\rSCGCollectionq\u0000~\u0000=q\u0000~\u0000\u001dsr\u0000\"com.sun.msv.grammar"
+".ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar"
+"/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.Express"
+"ionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006pare"
+"ntt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u000b\u0001pq\u0000~\u0000\u0005q\u0000~\u0000\u000eq"
+"\u0000~\u0000@q\u0000~\u0000\u0010q\u0000~\u0000\u0013q\u0000~\u0000Cq\u0000~\u0000Bq\u0000~\u0000#q\u0000~\u0000Gq\u0000~\u0000\u0007q\u0000~\u0000>x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.wustl.cider.jaxb.domain.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallingContext context) {
            super(context, "-------");
        }

        protected Unmarshaller(edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("SCGCollection" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  1 :
                        if (("PrincipalInvestigator" == ___local)&&("" == ___uri)) {
                            _CollectionProtocol = ((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolTypeImpl) spawnChildFromEnterElement((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("Title" == ___local)&&("" == ___uri)) {
                            _CollectionProtocol = ((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolTypeImpl) spawnChildFromEnterElement((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  0 :
                        if (("CollectionProtocol" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  4 :
                        if (("SpecimenCollectionGroup" == ___local)&&("" == ___uri)) {
                            _SCGCollection = ((edu.wustl.cider.jaxb.domain.impl.SCGCollectionTypeImpl) spawnChildFromEnterElement((edu.wustl.cider.jaxb.domain.impl.SCGCollectionTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        _SCGCollection = ((edu.wustl.cider.jaxb.domain.impl.SCGCollectionTypeImpl) spawnChildFromEnterElement((edu.wustl.cider.jaxb.domain.impl.SCGCollectionTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                        return ;
                    case  6 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
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
                    case  5 :
                        if (("SCGCollection" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  2 :
                        if (("CollectionProtocol" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  4 :
                        _SCGCollection = ((edu.wustl.cider.jaxb.domain.impl.SCGCollectionTypeImpl) spawnChildFromLeaveElement((edu.wustl.cider.jaxb.domain.impl.SCGCollectionTypeImpl.class), 5, ___uri, ___local, ___qname));
                        return ;
                    case  6 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
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
                    case  4 :
                        _SCGCollection = ((edu.wustl.cider.jaxb.domain.impl.SCGCollectionTypeImpl) spawnChildFromEnterAttribute((edu.wustl.cider.jaxb.domain.impl.SCGCollectionTypeImpl.class), 5, ___uri, ___local, ___qname));
                        return ;
                    case  6 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
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
                    case  4 :
                        _SCGCollection = ((edu.wustl.cider.jaxb.domain.impl.SCGCollectionTypeImpl) spawnChildFromLeaveAttribute((edu.wustl.cider.jaxb.domain.impl.SCGCollectionTypeImpl.class), 5, ___uri, ___local, ___qname));
                        return ;
                    case  6 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
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
                        case  0 :
                            state = 3;
                            continue outer;
                        case  4 :
                            _SCGCollection = ((edu.wustl.cider.jaxb.domain.impl.SCGCollectionTypeImpl) spawnChildFromText((edu.wustl.cider.jaxb.domain.impl.SCGCollectionTypeImpl.class), 5, value));
                            return ;
                        case  6 :
                            revertToParentFromText(value);
                            return ;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

    }

}
