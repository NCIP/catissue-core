/*Added by Preeti : This is requird to be done when catissuecore metadata is dumped. We need to set
isPrimaryKey = 1 for all id attributes. */

update dyextn_primitive_attribute set IS_PRIMARY_KEY ='1' where
identifier = (select IDENTIFIER from dyextn_abstract_metadata where name='id' and dyextn_abstract_metadata.identifier = dyextn_primitive_attribute.identifier)