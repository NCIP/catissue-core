/*
SQLyog - Free MySQL GUI v5.16
Host - 4.1.21-community-nt : Database - catissuecore
*********************************************************************
Server version : 4.1.21-community-nt
*/

SET NAMES utf8;

SET SQL_MODE='';

SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO';

/*Table structure for table `association` */

DROP TABLE IF EXISTS `association`;

CREATE TABLE `association` (
  `ASSOCIATION_ID` bigint(20) NOT NULL default '0',
  `ASSOCIATION_TYPE` int(8) NOT NULL default '0',
  PRIMARY KEY  (`ASSOCIATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `categorial_attribute` */

DROP TABLE IF EXISTS `categorial_attribute`;

CREATE TABLE `categorial_attribute` (
  `ID` bigint(20) NOT NULL auto_increment,
  `CATEGORIAL_CLASS_ID` bigint(20) NOT NULL default '0',
  `DE_CATEGORY_ATTRIBUTE_ID` bigint(20) NOT NULL default '0',
  `DE_SOURCE_CLASS_ATTRIBUTE_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`ID`),
  KEY `FK31F77B56E8CBD948` (`CATEGORIAL_CLASS_ID`),
  CONSTRAINT `FK31F77B56E8CBD948` FOREIGN KEY (`CATEGORIAL_CLASS_ID`) REFERENCES `categorial_class` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `categorial_class` */

DROP TABLE IF EXISTS `categorial_class`;

CREATE TABLE `categorial_class` (
  `ID` bigint(20) NOT NULL auto_increment,
  `DE_ENTITY_ID` bigint(20) default NULL,
  `PATH_FROM_PARENT_ID` bigint(20) default NULL,
  `PARENT_CATEGORIAL_CLASS_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK9651EF32D8D56A33` (`PARENT_CATEGORIAL_CLASS_ID`),
  CONSTRAINT `FK9651EF32D8D56A33` FOREIGN KEY (`PARENT_CATEGORIAL_CLASS_ID`) REFERENCES `categorial_class` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `category` */

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `ID` bigint(20) NOT NULL auto_increment,
  `DE_ENTITY_ID` bigint(20) NOT NULL default '0',
  `PARENT_CATEGORY_ID` bigint(20) default NULL,
  `ROOT_CATEGORIAL_CLASS_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `ROOT_CATEGORIAL_CLASS_ID` (`ROOT_CATEGORIAL_CLASS_ID`),
  KEY `FK31A8ACFE2D0F63E7` (`PARENT_CATEGORY_ID`),
  KEY `FK31A8ACFE211D9A6B` (`ROOT_CATEGORIAL_CLASS_ID`),
  CONSTRAINT `FK31A8ACFE211D9A6B` FOREIGN KEY (`ROOT_CATEGORIAL_CLASS_ID`) REFERENCES `categorial_class` (`ID`),
  CONSTRAINT `FK31A8ACFE2D0F63E7` FOREIGN KEY (`PARENT_CATEGORY_ID`) REFERENCES `category` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `de_coll_attr_record_values` */

DROP TABLE IF EXISTS `de_coll_attr_record_values`;

CREATE TABLE `de_coll_attr_record_values` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `RECORD_VALUE` text,
  `COLLECTION_ATTR_RECORD_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK847DA577355836BC` (`COLLECTION_ATTR_RECORD_ID`),
  CONSTRAINT `FK847DA577355836BC` FOREIGN KEY (`COLLECTION_ATTR_RECORD_ID`) REFERENCES `dyextn_attribute_record` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `de_file_attr_record_values` */

DROP TABLE IF EXISTS `de_file_attr_record_values`;

CREATE TABLE `de_file_attr_record_values` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `CONTENT_TYPE` varchar(255) default NULL,
  `FILE_CONTENT` blob,
  `FILE_NAME` varchar(255) default NULL,
  `RECORD_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKE68334E7E150DFC9` (`RECORD_ID`),
  CONSTRAINT `FKE68334E7E150DFC9` FOREIGN KEY (`RECORD_ID`) REFERENCES `dyextn_attribute_record` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_abstract_metadata` */

DROP TABLE IF EXISTS `dyextn_abstract_metadata`;

CREATE TABLE `dyextn_abstract_metadata` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `CREATED_DATE` date default NULL,
  `DESCRIPTION` text,
  `LAST_UPDATED` date default NULL,
  `NAME` text,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_asso_display_attr` */

DROP TABLE IF EXISTS `dyextn_asso_display_attr`;

CREATE TABLE `dyextn_asso_display_attr` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `SEQUENCE_NUMBER` int(11) default NULL,
  `DISPLAY_ATTRIBUTE_ID` bigint(20) default NULL,
  `SELECT_CONTROL_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKD12FD3823B3AAE3B` (`DISPLAY_ATTRIBUTE_ID`),
  KEY `FKD12FD382F7AA8E80` (`SELECT_CONTROL_ID`),
  CONSTRAINT `FKD12FD382F7AA8E80` FOREIGN KEY (`SELECT_CONTROL_ID`) REFERENCES `dyextn_select_control` (`IDENTIFIER`),
  CONSTRAINT `FKD12FD3823B3AAE3B` FOREIGN KEY (`DISPLAY_ATTRIBUTE_ID`) REFERENCES `dyextn_primitive_attribute` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_association` */

DROP TABLE IF EXISTS `dyextn_association`;

CREATE TABLE `dyextn_association` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `DIRECTION` varchar(255) default NULL,
  `TARGET_ENTITY_ID` bigint(20) default NULL,
  `SOURCE_ROLE_ID` bigint(20) default NULL,
  `TARGET_ROLE_ID` bigint(20) default NULL,
  `IS_SYSTEM_GENERATED` tinyint(1) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK104684243AC5160` (`SOURCE_ROLE_ID`),
  KEY `FK10468424F60C84D6` (`TARGET_ROLE_ID`),
  KEY `FK104684246315C5C9` (`TARGET_ENTITY_ID`),
  KEY `FK10468424BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK10468424BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute` (`IDENTIFIER`),
  CONSTRAINT `FK104684243AC5160` FOREIGN KEY (`SOURCE_ROLE_ID`) REFERENCES `dyextn_role` (`IDENTIFIER`),
  CONSTRAINT `FK104684246315C5C9` FOREIGN KEY (`TARGET_ENTITY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`),
  CONSTRAINT `FK10468424F60C84D6` FOREIGN KEY (`TARGET_ROLE_ID`) REFERENCES `dyextn_role` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_attribute` */

DROP TABLE IF EXISTS `dyextn_attribute`;

CREATE TABLE `dyextn_attribute` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `ENTIY_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK37F1E2FFB15CD09F` (`ENTIY_ID`),
  KEY `FK37F1E2FFBC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK37F1E2FFBC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_abstract_metadata` (`IDENTIFIER`),
  CONSTRAINT `FK37F1E2FFB15CD09F` FOREIGN KEY (`ENTIY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_attribute_record` */

DROP TABLE IF EXISTS `dyextn_attribute_record`;

CREATE TABLE `dyextn_attribute_record` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `ENTITY_ID` bigint(20) default NULL,
  `ATTRIBUTE_ID` bigint(20) default NULL,
  `RECORD_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK9B20ED9179F466F7` (`ENTITY_ID`),
  KEY `FK9B20ED914D87D1BE` (`ATTRIBUTE_ID`),
  CONSTRAINT `FK9B20ED914D87D1BE` FOREIGN KEY (`ATTRIBUTE_ID`) REFERENCES `dyextn_primitive_attribute` (`IDENTIFIER`),
  CONSTRAINT `FK9B20ED9179F466F7` FOREIGN KEY (`ENTITY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_attribute_type_info` */

DROP TABLE IF EXISTS `dyextn_attribute_type_info`;

CREATE TABLE `dyextn_attribute_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `PRIMITIVE_ATTRIBUTE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK62596D53B4C15A36` (`PRIMITIVE_ATTRIBUTE_ID`),
  CONSTRAINT `FK62596D53B4C15A36` FOREIGN KEY (`PRIMITIVE_ATTRIBUTE_ID`) REFERENCES `dyextn_primitive_attribute` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_barr_concept_value` */

DROP TABLE IF EXISTS `dyextn_barr_concept_value`;

CREATE TABLE `dyextn_barr_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK89D27DF7BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK89D27DF7BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_boolean_concept_value` */

DROP TABLE IF EXISTS `dyextn_boolean_concept_value`;

CREATE TABLE `dyextn_boolean_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `VALUE` tinyint(1) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK57B6C4A6BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK57B6C4A6BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_boolean_type_info` */

DROP TABLE IF EXISTS `dyextn_boolean_type_info`;

CREATE TABLE `dyextn_boolean_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK28F1809FBC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK28F1809FBC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_byte_array_type_info` */

DROP TABLE IF EXISTS `dyextn_byte_array_type_info`;

CREATE TABLE `dyextn_byte_array_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `CONTENT_TYPE` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK18BDA73BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK18BDA73BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_cadsrde` */

DROP TABLE IF EXISTS `dyextn_cadsrde`;

CREATE TABLE `dyextn_cadsrde` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `PUBLIC_ID` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK588A2509BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK588A2509BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_data_element` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_check_box` */

DROP TABLE IF EXISTS `dyextn_check_box`;

CREATE TABLE `dyextn_check_box` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK4EFF9257BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK4EFF9257BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_column_properties` */

DROP TABLE IF EXISTS `dyextn_column_properties`;

CREATE TABLE `dyextn_column_properties` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `PRIMITIVE_ATTRIBUTE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK8FCE2B3FBC7298A9` (`IDENTIFIER`),
  KEY `FK8FCE2B3FB4C15A36` (`PRIMITIVE_ATTRIBUTE_ID`),
  CONSTRAINT `FK8FCE2B3FB4C15A36` FOREIGN KEY (`PRIMITIVE_ATTRIBUTE_ID`) REFERENCES `dyextn_primitive_attribute` (`IDENTIFIER`),
  CONSTRAINT `FK8FCE2B3FBC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_database_properties` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_combobox` */

DROP TABLE IF EXISTS `dyextn_combobox`;

CREATE TABLE `dyextn_combobox` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKABBC649ABC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FKABBC649ABC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_select_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_constraint_properties` */

DROP TABLE IF EXISTS `dyextn_constraint_properties`;

CREATE TABLE `dyextn_constraint_properties` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `SOURCE_ENTITY_KEY` varchar(255) default NULL,
  `TARGET_ENTITY_KEY` varchar(255) default NULL,
  `ASSOCIATION_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK82886CD8BC7298A9` (`IDENTIFIER`),
  KEY `FK82886CD8927B15B9` (`ASSOCIATION_ID`),
  CONSTRAINT `FK82886CD8927B15B9` FOREIGN KEY (`ASSOCIATION_ID`) REFERENCES `dyextn_association` (`IDENTIFIER`),
  CONSTRAINT `FK82886CD8BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_database_properties` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_container` */

DROP TABLE IF EXISTS `dyextn_container`;

CREATE TABLE `dyextn_container` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `BUTTON_CSS` varchar(255) default NULL,
  `CAPTION` varchar(255) default NULL,
  `ENTITY_ID` bigint(20) default NULL,
  `MAIN_TABLE_CSS` varchar(255) default NULL,
  `REQUIRED_FIELD_INDICATOR` varchar(255) default NULL,
  `REQUIRED_FIELD_WARNING_MESSAGE` varchar(255) default NULL,
  `TITLE_CSS` varchar(255) default NULL,
  `BASE_CONTAINER_ID` bigint(20) default NULL,
  `ENTITY_GROUP_ID` bigint(20) default NULL,
  `VIEW_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK1EAB84E4A1257067` (`BASE_CONTAINER_ID`),
  KEY `FK1EAB84E479F466F7` (`ENTITY_ID`),
  KEY `FK1EAB84E4992A67D7` (`ENTITY_GROUP_ID`),
  KEY `FK1EAB84E445DEFCF5` (`VIEW_ID`),
  CONSTRAINT `FK1EAB84E445DEFCF5` FOREIGN KEY (`VIEW_ID`) REFERENCES `dyextn_view` (`IDENTIFIER`),
  CONSTRAINT `FK1EAB84E479F466F7` FOREIGN KEY (`ENTITY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`),
  CONSTRAINT `FK1EAB84E4992A67D7` FOREIGN KEY (`ENTITY_GROUP_ID`) REFERENCES `dyextn_entity_group` (`IDENTIFIER`),
  CONSTRAINT `FK1EAB84E4A1257067` FOREIGN KEY (`BASE_CONTAINER_ID`) REFERENCES `dyextn_container` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_containment_control` */

DROP TABLE IF EXISTS `dyextn_containment_control`;

CREATE TABLE `dyextn_containment_control` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `DISPLAY_CONTAINER_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK3F9D4AD3F7798636` (`DISPLAY_CONTAINER_ID`),
  KEY `FK3F9D4AD3BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK3F9D4AD3BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`),
  CONSTRAINT `FK3F9D4AD3F7798636` FOREIGN KEY (`DISPLAY_CONTAINER_ID`) REFERENCES `dyextn_container` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_control` */

DROP TABLE IF EXISTS `dyextn_control`;

CREATE TABLE `dyextn_control` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `CAPTION` varchar(255) default NULL,
  `CSS_CLASS` varchar(255) default NULL,
  `HIDDEN` tinyint(1) default NULL,
  `NAME` varchar(255) default NULL,
  `SEQUENCE_NUMBER` int(11) default NULL,
  `TOOLTIP` varchar(255) default NULL,
  `ABSTRACT_ATTRIBUTE_ID` bigint(20) default NULL,
  `CONTAINER_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK70FB5E80A67822BB` (`ABSTRACT_ATTRIBUTE_ID`),
  KEY `FK70FB5E809C6A9B9` (`CONTAINER_ID`),
  CONSTRAINT `FK70FB5E809C6A9B9` FOREIGN KEY (`CONTAINER_ID`) REFERENCES `dyextn_container` (`IDENTIFIER`),
  CONSTRAINT `FK70FB5E80A67822BB` FOREIGN KEY (`ABSTRACT_ATTRIBUTE_ID`) REFERENCES `dyextn_attribute` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_data_element` */

DROP TABLE IF EXISTS `dyextn_data_element`;

CREATE TABLE `dyextn_data_element` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `ATTRIBUTE_TYPE_INFO_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKB1153E48C8D972A` (`ATTRIBUTE_TYPE_INFO_ID`),
  CONSTRAINT `FKB1153E48C8D972A` FOREIGN KEY (`ATTRIBUTE_TYPE_INFO_ID`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_data_grid` */

DROP TABLE IF EXISTS `dyextn_data_grid`;

CREATE TABLE `dyextn_data_grid` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK233EB73EBC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK233EB73EBC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_database_properties` */

DROP TABLE IF EXISTS `dyextn_database_properties`;

CREATE TABLE `dyextn_database_properties` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_date_concept_value` */

DROP TABLE IF EXISTS `dyextn_date_concept_value`;

CREATE TABLE `dyextn_date_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `VALUE` date default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK45F598A6BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK45F598A6BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_date_type_info` */

DROP TABLE IF EXISTS `dyextn_date_type_info`;

CREATE TABLE `dyextn_date_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `FORMAT` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKFBA549FBC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FKFBA549FBC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_datepicker` */

DROP TABLE IF EXISTS `dyextn_datepicker`;

CREATE TABLE `dyextn_datepicker` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKFEADD199BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FKFEADD199BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_double_concept_value` */

DROP TABLE IF EXISTS `dyextn_double_concept_value`;

CREATE TABLE `dyextn_double_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `VALUE` double default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKB94E6449BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FKB94E6449BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_double_type_info` */

DROP TABLE IF EXISTS `dyextn_double_type_info`;

CREATE TABLE `dyextn_double_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKC83869C2BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FKC83869C2BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_numeric_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_entity` */

DROP TABLE IF EXISTS `dyextn_entity`;

CREATE TABLE `dyextn_entity` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `DATA_TABLE_STATE` int(11) default NULL,
  `IS_ABSTRACT` tinyint(1) default NULL,
  `PARENT_ENTITY_ID` bigint(20) default NULL,
  `INHERITANCE_STRATEGY` int(11) default NULL,
  `DISCRIMINATOR_COLUMN_NAME` varchar(255) default NULL,
  `DISCRIMINATOR_VALUE` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK8B243640450711A2` (`PARENT_ENTITY_ID`),
  KEY `FK8B243640BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK8B243640BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_abstract_metadata` (`IDENTIFIER`),
  CONSTRAINT `FK8B243640450711A2` FOREIGN KEY (`PARENT_ENTITY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_entity_group` */

DROP TABLE IF EXISTS `dyextn_entity_group`;

CREATE TABLE `dyextn_entity_group` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `LONG_NAME` varchar(255) default NULL,
  `SHORT_NAME` varchar(255) default NULL,
  `VERSION` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK105DE7A0BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK105DE7A0BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_abstract_metadata` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_entity_group_rel` */

DROP TABLE IF EXISTS `dyextn_entity_group_rel`;

CREATE TABLE `dyextn_entity_group_rel` (
  `ENTITY_GROUP_ID` bigint(20) NOT NULL default '0',
  `ENTITY_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`ENTITY_ID`,`ENTITY_GROUP_ID`),
  KEY `FK5A0D835A992A67D7` (`ENTITY_GROUP_ID`),
  KEY `FK5A0D835A79F466F7` (`ENTITY_ID`),
  CONSTRAINT `FK5A0D835A79F466F7` FOREIGN KEY (`ENTITY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`),
  CONSTRAINT `FK5A0D835A992A67D7` FOREIGN KEY (`ENTITY_GROUP_ID`) REFERENCES `dyextn_entity_group` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_file_extensions` */

DROP TABLE IF EXISTS `dyextn_file_extensions`;

CREATE TABLE `dyextn_file_extensions` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `FILE_EXTENSION` varchar(255) default NULL,
  `ATTRIBUTE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKD49834FA4D87D1BE` (`ATTRIBUTE_ID`),
  CONSTRAINT `FKD49834FA4D87D1BE` FOREIGN KEY (`ATTRIBUTE_ID`) REFERENCES `dyextn_file_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_file_type_info` */

DROP TABLE IF EXISTS `dyextn_file_type_info`;

CREATE TABLE `dyextn_file_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `MAX_FILE_SIZE` float default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKA00F0EDBC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FKA00F0EDBC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_file_upload` */

DROP TABLE IF EXISTS `dyextn_file_upload`;

CREATE TABLE `dyextn_file_upload` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `NO_OF_COLUMNS` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK2FAD41E7BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK2FAD41E7BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_float_concept_value` */

DROP TABLE IF EXISTS `dyextn_float_concept_value`;

CREATE TABLE `dyextn_float_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `VALUE` float default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK6785309ABC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK6785309ABC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_float_type_info` */

DROP TABLE IF EXISTS `dyextn_float_type_info`;

CREATE TABLE `dyextn_float_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK7E1C0693BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK7E1C0693BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_numeric_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_integer_concept_value` */

DROP TABLE IF EXISTS `dyextn_integer_concept_value`;

CREATE TABLE `dyextn_integer_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `VALUE` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKFBA33B3CBC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FKFBA33B3CBC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_integer_type_info` */

DROP TABLE IF EXISTS `dyextn_integer_type_info`;

CREATE TABLE `dyextn_integer_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK5F9CB235BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK5F9CB235BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_numeric_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_list_box` */

DROP TABLE IF EXISTS `dyextn_list_box`;

CREATE TABLE `dyextn_list_box` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `MULTISELECT` tinyint(1) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK208395A7BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK208395A7BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_select_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_long_concept_value` */

DROP TABLE IF EXISTS `dyextn_long_concept_value`;

CREATE TABLE `dyextn_long_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `VALUE` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK3E1A6EF4BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK3E1A6EF4BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_long_type_info` */

DROP TABLE IF EXISTS `dyextn_long_type_info`;

CREATE TABLE `dyextn_long_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK257281EDBC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK257281EDBC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_numeric_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_numeric_type_info` */

DROP TABLE IF EXISTS `dyextn_numeric_type_info`;

CREATE TABLE `dyextn_numeric_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `MEASUREMENT_UNITS` varchar(255) default NULL,
  `DECIMAL_PLACES` int(11) default NULL,
  `NO_DIGITS` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK4DEC9544BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK4DEC9544BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_permissible_value` */

DROP TABLE IF EXISTS `dyextn_permissible_value`;

CREATE TABLE `dyextn_permissible_value` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `DESCRIPTION` varchar(255) default NULL,
  `ATTRIBUTE_TYPE_INFO_ID` bigint(20) default NULL,
  `USER_DEF_DE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK136264E08C8D972A` (`ATTRIBUTE_TYPE_INFO_ID`),
  KEY `FK136264E03D51114B` (`USER_DEF_DE_ID`),
  CONSTRAINT `FK136264E03D51114B` FOREIGN KEY (`USER_DEF_DE_ID`) REFERENCES `dyextn_userdefined_de` (`IDENTIFIER`),
  CONSTRAINT `FK136264E08C8D972A` FOREIGN KEY (`ATTRIBUTE_TYPE_INFO_ID`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_primitive_attribute` */

DROP TABLE IF EXISTS `dyextn_primitive_attribute`;

CREATE TABLE `dyextn_primitive_attribute` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `IS_COLLECTION` tinyint(1) default NULL,
  `IS_IDENTIFIED` tinyint(1) default NULL,
  `IS_PRIMARY_KEY` tinyint(1) default NULL,
  `IS_NULLABLE` tinyint(1) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKA9F765C7BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FKA9F765C7BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_radiobutton` */

DROP TABLE IF EXISTS `dyextn_radiobutton`;

CREATE TABLE `dyextn_radiobutton` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK16F5BA90BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK16F5BA90BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_role` */

DROP TABLE IF EXISTS `dyextn_role`;

CREATE TABLE `dyextn_role` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `ASSOCIATION_TYPE` varchar(255) default NULL,
  `MAX_CARDINALITY` int(11) default NULL,
  `MIN_CARDINALITY` int(11) default NULL,
  `NAME` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_rule` */

DROP TABLE IF EXISTS `dyextn_rule`;

CREATE TABLE `dyextn_rule` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  `ATTRIBUTE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKC27E0994D87D1BE` (`ATTRIBUTE_ID`),
  CONSTRAINT `FKC27E0994D87D1BE` FOREIGN KEY (`ATTRIBUTE_ID`) REFERENCES `dyextn_attribute` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_rule_parameter` */

DROP TABLE IF EXISTS `dyextn_rule_parameter`;

CREATE TABLE `dyextn_rule_parameter` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  `VALUE` varchar(255) default NULL,
  `RULE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK22567363871AAD3E` (`RULE_ID`),
  CONSTRAINT `FK22567363871AAD3E` FOREIGN KEY (`RULE_ID`) REFERENCES `dyextn_rule` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_select_control` */

DROP TABLE IF EXISTS `dyextn_select_control`;

CREATE TABLE `dyextn_select_control` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `SEPARATOR_STRING` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKDFEBB657BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FKDFEBB657BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_semantic_property` */

DROP TABLE IF EXISTS `dyextn_semantic_property`;

CREATE TABLE `dyextn_semantic_property` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `CONCEPT_CODE` varchar(255) default NULL,
  `TERM` varchar(255) default NULL,
  `THESAURAS_NAME` varchar(255) default NULL,
  `SEQUENCE_NUMBER` int(11) default NULL,
  `ABSTRACT_METADATA_ID` bigint(20) default NULL,
  `ABSTRACT_VALUE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKD2A0B5B13BAB5E46` (`ABSTRACT_VALUE_ID`),
  KEY `FKD2A0B5B17D7A9B8E` (`ABSTRACT_METADATA_ID`),
  CONSTRAINT `FKD2A0B5B17D7A9B8E` FOREIGN KEY (`ABSTRACT_METADATA_ID`) REFERENCES `dyextn_abstract_metadata` (`IDENTIFIER`),
  CONSTRAINT `FKD2A0B5B13BAB5E46` FOREIGN KEY (`ABSTRACT_VALUE_ID`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_short_concept_value` */

DROP TABLE IF EXISTS `dyextn_short_concept_value`;

CREATE TABLE `dyextn_short_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `VALUE` smallint(6) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKC1945ABABC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FKC1945ABABC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_short_type_info` */

DROP TABLE IF EXISTS `dyextn_short_type_info`;

CREATE TABLE `dyextn_short_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK99540B3BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK99540B3BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_numeric_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_string_concept_value` */

DROP TABLE IF EXISTS `dyextn_string_concept_value`;

CREATE TABLE `dyextn_string_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `VALUE` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKADE7D889BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FKADE7D889BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_string_type_info` */

DROP TABLE IF EXISTS `dyextn_string_type_info`;

CREATE TABLE `dyextn_string_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `MAX_SIZE` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKDA35FE02BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FKDA35FE02BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_table_properties` */

DROP TABLE IF EXISTS `dyextn_table_properties`;

CREATE TABLE `dyextn_table_properties` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `ENTITY_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKE608E08179F466F7` (`ENTITY_ID`),
  KEY `FKE608E081BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FKE608E081BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_database_properties` (`IDENTIFIER`),
  CONSTRAINT `FKE608E08179F466F7` FOREIGN KEY (`ENTITY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_tagged_value` */

DROP TABLE IF EXISTS `dyextn_tagged_value`;

CREATE TABLE `dyextn_tagged_value` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `T_KEY` varchar(255) default NULL,
  `T_VALUE` varchar(255) default NULL,
  `ABSTRACT_METADATA_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKF79D055B7D7A9B8E` (`ABSTRACT_METADATA_ID`),
  CONSTRAINT `FKF79D055B7D7A9B8E` FOREIGN KEY (`ABSTRACT_METADATA_ID`) REFERENCES `dyextn_abstract_metadata` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_textarea` */

DROP TABLE IF EXISTS `dyextn_textarea`;

CREATE TABLE `dyextn_textarea` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `TEXTAREA_COLUMNS` int(11) default NULL,
  `TEXTAREA_ROWS` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK946EE257BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK946EE257BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_textfield` */

DROP TABLE IF EXISTS `dyextn_textfield`;

CREATE TABLE `dyextn_textfield` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `NO_OF_COLUMNS` int(11) default NULL,
  `IS_PASSWORD` tinyint(1) default NULL,
  `IS_URL` tinyint(1) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKF9AFC850BC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FKF9AFC850BC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_userdefined_de` */

DROP TABLE IF EXISTS `dyextn_userdefined_de`;

CREATE TABLE `dyextn_userdefined_de` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK630761FFBC7298A9` (`IDENTIFIER`),
  CONSTRAINT `FK630761FFBC7298A9` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_data_element` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dyextn_view` */

DROP TABLE IF EXISTS `dyextn_view`;

CREATE TABLE `dyextn_view` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `id_table` */

DROP TABLE IF EXISTS `id_table`;

CREATE TABLE `id_table` (
  `NEXT_ASSOCIATION_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`NEXT_ASSOCIATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `inter_model_association` */

DROP TABLE IF EXISTS `inter_model_association`;

CREATE TABLE `inter_model_association` (
  `ASSOCIATION_ID` bigint(20) NOT NULL default '0',
  `LEFT_ENTITY_ID` bigint(20) NOT NULL default '0',
  `LEFT_ATTRIBUTE_ID` bigint(20) NOT NULL default '0',
  `RIGHT_ENTITY_ID` bigint(20) NOT NULL default '0',
  `RIGHT_ATTRIBUTE_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`ASSOCIATION_ID`),
  CONSTRAINT `inter_model_association_ibfk_1` FOREIGN KEY (`ASSOCIATION_ID`) REFERENCES `association` (`ASSOCIATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `intra_model_association` */

DROP TABLE IF EXISTS `intra_model_association`;

CREATE TABLE `intra_model_association` (
  `ASSOCIATION_ID` bigint(20) NOT NULL default '0',
  `DE_ASSOCIATION_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`ASSOCIATION_ID`),
  CONSTRAINT `intra_model_association_ibfk_1` FOREIGN KEY (`ASSOCIATION_ID`) REFERENCES `association` (`ASSOCIATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `path` */

DROP TABLE IF EXISTS `path`;

CREATE TABLE `path` (
  `PATH_ID` bigint(20) NOT NULL auto_increment,
  `FIRST_ENTITY_ID` bigint(20) default NULL,
  `INTERMEDIATE_PATH` text,
  `LAST_ENTITY_ID` bigint(20) default NULL,
  PRIMARY KEY  (`PATH_ID`),
  KEY `INDEX1` (`FIRST_ENTITY_ID`,`LAST_ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
