
set classpath=.\classes;.\lib\activation.jar;.\lib\ant-antlr-1.6.3.jar;.\lib\antlr-2.7.5H3.jar;.\lib\ant-optional-1.5.3.jar;.\lib\asm.jar;.\lib\asm-attrs.jar;.\lib\c3p0-0.8.4.5.jar;.\lib\c3p0-0.8.5.2.jar;.\lib\caTies.jar;.\lib\cglib-2.1.jar;.\lib\client.jar;.\lib\commonpackage.jar;.\lib\commons-beanutils.jar;.\lib\commons-codec-1.3.jar;.\lib\commons-collections-2.1.1.jar;.\lib\commons-digester.jar;.\lib\commons-fileupload.jar;.\lib\commons-lang.jar;.\lib\commons-logging-1.0.4.jar;.\lib\commons-validator.jar;.\lib\csmapi.jar;.\lib\dom4j-1.6.jar;.\lib\ehcache-1.1.jar;.\lib\ehcache-1.2.3.jar;.\lib\hibernate2.jar;.\lib\hibernate2.1.7c.jar;.\lib\jaas.jar;.\lib\jakarta-oro.jar;.\lib\jaws.jar;.\lib\jaxb-api.jar;.\lib\jaxb-impl.jar;.\lib\jaxb-libs.jar;.\lib\jaxb-xjc.jar;.\lib\jax-qname.jar;.\lib\jcs-1.0-dev.jar;.\lib\jdom.jar;.\lib\jta.jar;.\lib\junit-3.8.1.jar;.\lib\log4j-1.2.9.jar;.\lib\mail.jar;.\lib\mockobjects-core-0.09.jar;.\lib\mysql-connector-java-3.0.16-ga-bin.jar;.\lib\namespace.jar;.\lib\odmg.jar;.\lib\oracleDriver.jar;.\lib\relaxngDatatype.jar;.\lib\sdkClient.jar;.\lib\servlet.jar;.\lib\smtp.jar;.\lib\struts.jar;.\lib\struts-legacy.jar;.\lib\saxpath.jar;.\lib\jaxen-core.jar;.\lib\jaxen-jdom.jar;.\lib\xalan-2.4.0.jar;.\lib\xml-apis.jar;.\lib\xsdlib.jar;./reportloader.jar;.;./deid.jar;

echo %classpath%

rem java -jar deid.jar

java edu.wustl.catissuecore.deid.DeIDPipelineManager

set classpath=""