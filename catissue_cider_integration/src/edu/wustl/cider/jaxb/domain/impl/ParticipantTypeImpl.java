//
// This file was edu.wustl.cider.jaxb.domain by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// edu.wustl.cider.jaxb.domain on: 2011.06.16 at 07:41:18 GMT
//


package edu.wustl.cider.jaxb.domain.impl;

public class ParticipantTypeImpl implements edu.wustl.cider.jaxb.domain.ParticipantType, com.sun.xml.bind.JAXBObject, edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallableObject, edu.wustl.cider.jaxb.domain.impl.runtime.XMLSerializable, edu.wustl.cider.jaxb.domain.impl.runtime.ValidatableObject
{

    protected edu.wustl.cider.jaxb.domain.CollectionProtocolRegistrationType _CollectionProtocolRegistration;
    protected java.lang.String _Empi;
    public final static java.lang.Class version = (edu.wustl.cider.jaxb.domain.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.wustl.cider.jaxb.domain.ParticipantType.class);
    }

    public edu.wustl.cider.jaxb.domain.CollectionProtocolRegistrationType getCollectionProtocolRegistration() {
        return _CollectionProtocolRegistration;
    }

    public void setCollectionProtocolRegistration(edu.wustl.cider.jaxb.domain.CollectionProtocolRegistrationType value) {
        _CollectionProtocolRegistration = value;
    }

    public java.lang.String getEmpi() {
        return _Empi;
    }

    public void setEmpi(java.lang.String value) {
        _Empi = value;
    }

    public edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.wustl.cider.jaxb.domain.impl.runtime.UnmarshallingContext context) {
        return new edu.wustl.cider.jaxb.domain.impl.ParticipantTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.wustl.cider.jaxb.domain.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_Empi!= null) {
            context.startElement("", "Empi");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text((_Empi), "Empi");
            } catch (java.lang.Exception e) {
                edu.wustl.cider.jaxb.domain.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_CollectionProtocolRegistration!= null) {
            context.startElement("", "CollectionProtocolRegistration");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _CollectionProtocolRegistration), "CollectionProtocolRegistration");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _CollectionProtocolRegistration), "CollectionProtocolRegistration");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _CollectionProtocolRegistration), "CollectionProtocolRegistration");
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
        return (edu.wustl.cider.jaxb.domain.ParticipantType.class);
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
+"\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002"
+"L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000#com.sun.m"
+"sv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com"
+".sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.su"
+"n.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.da"
+"tatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/"
+"lang/String;L\u0000\btypeNameq\u0000~\u0000\u0017L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/dat"
+"atype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/XM"
+"LSchemat\u0000\u0006stringsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProce"
+"ssor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSp"
+"aceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv.grammar.Expression"
+"$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.St"
+"ringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0017L\u0000\fnamespaceURIq\u0000~\u0000\u0017xpq\u0000"
+"~\u0000\u001bq\u0000~\u0000\u001asq\u0000~\u0000\u0006ppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\txq\u0000~\u0000\u0003q\u0000~\u0000\rpsq\u0000~\u0000\u000fppsr\u0000\"com.s"
+"un.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0014q\u0000~\u0000\u001at\u0000\u0005QNames"
+"r\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001dq\u0000~\u0000 sq\u0000~\u0000!q\u0000~\u0000)q\u0000~\u0000\u001asr\u0000#com.sun.msv.grammar.Si"
+"mpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0017L\u0000\fnamespaceURIq\u0000~\u0000"
+"\u0017xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)htt"
+"p://www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.gramma"
+"r.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\f\u0001psq\u0000~\u0000"
+"-t\u0000\u0004Empit\u0000\u0000q\u0000~\u00003sq\u0000~\u0000\u0006ppsq\u0000~\u0000\bq\u0000~\u0000\rp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\bpp\u0000sq\u0000~\u0000\u0006"
+"ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun"
+".msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000\rpsq\u0000~\u0000"
+"$q\u0000~\u0000\rpsr\u00002com.sun.msv.grammar.Expression$AnyStringExpressio"
+"n\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u00004psr\u0000 com.sun.msv.grammar.AnyNameClass"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000.q\u0000~\u00003sq\u0000~\u0000-t\u0000,edu.wustl.cider.jaxb.domain.CollectionProtocolR"
+"egistrationTypet\u0000+http://java.sun.com/jaxb/xjc/dummy-element"
+"ssq\u0000~\u0000\u0006ppsq\u0000~\u0000$q\u0000~\u0000\rpq\u0000~\u0000&q\u0000~\u0000/q\u0000~\u00003sq\u0000~\u0000-t\u0000\u001eCollectionProto"
+"colRegistrationq\u0000~\u00007q\u0000~\u00003sr\u0000\"com.sun.msv.grammar.ExpressionP"
+"ool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionP"
+"ool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$Clos"
+"edHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/su"
+"n/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\t\u0001pq\u0000~\u0000\u0007q\u0000~\u0000:q\u0000~\u0000<q\u0000~\u0000?q\u0000~"
+"\u0000\u000eq\u0000~\u0000#q\u0000~\u0000Hq\u0000~\u00008q\u0000~\u0000\u0005x"));
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
            return edu.wustl.cider.jaxb.domain.impl.ParticipantTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("Empi" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  6 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  4 :
                        if (("CollectionProtocol" == ___local)&&("" == ___uri)) {
                            _CollectionProtocolRegistration = ((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl) spawnChildFromEnterElement((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("SCGCollection" == ___local)&&("" == ___uri)) {
                            _CollectionProtocolRegistration = ((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl) spawnChildFromEnterElement((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        _CollectionProtocolRegistration = ((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl) spawnChildFromEnterElement((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                        return ;
                    case  3 :
                        if (("CollectionProtocolRegistration" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
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
                    case  2 :
                        if (("Empi" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  6 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  5 :
                        if (("CollectionProtocolRegistration" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  4 :
                        _CollectionProtocolRegistration = ((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl) spawnChildFromLeaveElement((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl.class), 5, ___uri, ___local, ___qname));
                        return ;
                    case  3 :
                        state = 6;
                        continue outer;
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
                    case  0 :
                        state = 3;
                        continue outer;
                    case  6 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  4 :
                        _CollectionProtocolRegistration = ((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl) spawnChildFromEnterAttribute((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl.class), 5, ___uri, ___local, ___qname));
                        return ;
                    case  3 :
                        state = 6;
                        continue outer;
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
                    case  0 :
                        state = 3;
                        continue outer;
                    case  6 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  4 :
                        _CollectionProtocolRegistration = ((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl) spawnChildFromLeaveAttribute((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl.class), 5, ___uri, ___local, ___qname));
                        return ;
                    case  3 :
                        state = 6;
                        continue outer;
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
                        case  1 :
                            eatText1(value);
                            state = 2;
                            return ;
                        case  0 :
                            state = 3;
                            continue outer;
                        case  6 :
                            revertToParentFromText(value);
                            return ;
                        case  4 :
                            _CollectionProtocolRegistration = ((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl) spawnChildFromText((edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl.class), 5, value));
                            return ;
                        case  3 :
                            state = 6;
                            continue outer;
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
                _Empi = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
