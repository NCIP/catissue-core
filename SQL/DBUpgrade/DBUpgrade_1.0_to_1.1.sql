#------ Begin:25/07/06 Poornima: Bug-1951 -------
# ------ Create unique key for catissue_related_tables_map and catissue_search_display_data----
#--Remove duplicate entries first
delete from CATISSUE_RELATED_TABLES_MAP where FIRST_TABLE_ID=38 and SECOND_TABLE_ID=19;
alter table CATISSUE_RELATED_TABLES_MAP add constraint RELATED_TABLES_KEY unique (FIRST_TABLE_ID,SECOND_TABLE_ID);
alter table CATISSUE_SEARCH_DISPLAY_DATA add constraint SEARCH_DATA_KEY unique (RELATIONSHIP_ID,COL_ID);
#---Insert one entry for the above deleted duplicate entry --
insert into CATISSUE_RELATED_TABLES_MAP values ( 38 , 19 , 'DISTRIBUTION_PROTOCOL_ID','IDENTIFIER');
#------ End:25/07/06 Poornima: Bug-1951 -------

# ------ 'Death date' and 'Vital status' attribute addition to Participant table ------
# ---------- 23 May 2006 -------------
ALTER TABLE catissue_participant ADD COLUMN DEATH_DATE DATE;
ALTER TABLE catissue_participant ADD COLUMN VITAL_STATUS varchar(50);

#-- Participant related data model changes
#--alter table CATISSUE_RACE drop foreign key FKB0242ECD87E5ADC7;
#--drop table if exists CATISSUE_RACE;
create table CATISSUE_RACE (
   PARTICIPANT_ID bigint not null,
   RACE_NAME varchar(50)
);
#--alter table catissue_participant Engine = INNODB;
alter table CATISSUE_RACE add index FKB0242ECD87E5ADC7 (PARTICIPANT_ID), add constraint FKB0242ECD87E5ADC7 foreign key (PARTICIPANT_ID) references CATISSUE_PARTICIPANT (IDENTIFIER);

insert into catissue_race(participant_id,race_name) (select identifier,race from catissue_participant);

alter table catissue_participant drop column race;


# ------ For simple search on Participant 'Death date' and 'Vital status' ---------
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 303, 31, 'DEATH_DATE', 'date');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 304, 31, 'VITAL_STATUS', 'varchar');

# ----- VitalStatus Constant and Permissible value ----- 
INSERT INTO CATISSUE_CDE VALUES ( '2004001','VitalStatus','Vital status of the participant.',1.0,null);
INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES(2638,'Dead',NULL,'2004001');
INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES(2639,'Alive',NULL,'2004001');
INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES(2640,'Unknown',NULL,'2004001');


# ------ Bug 1997:Specimen class not queriable - Added specimen class field entry ------
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (34, 102 , 'Specimen Class');



#-- user paasword changes 
#--alter table CATISSUE_PASSWORD drop foreign key FKDE1F38972206F20F;
#--drop table if exists CATISSUE_PASSWORD;

create table CATISSUE_PASSWORD (
  IDENTIFIER bigint not null auto_increment,
   PASSWORD varchar(50),
   UPDATE_DATE date,
   USER_ID bigint,
   primary key (IDENTIFIER)
);
#--alter table catissue_user Engine = INNODB;

alter table CATISSUE_PASSWORD add index FKDE1F38972206F20F (USER_ID), add constraint FKDE1F38972206F20F foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);

insert into catissue_password (IDENTIFIER,PASSWORD,UPDATE_DATE,USER_ID) (select identifier, password,null,identifier from catissue_user);

alter table catissue_user drop column password;


#-- user  password chnages finish

#--------- changes in database for new model of Storage Container
#--drop table if exists CATISSUE_STOR_TYPE_SPEC_CLASS;
#--drop table if exists CATISSUE_STOR_TYPE_HOLDS_TYPE;
#--drop table if exists CATISSUE_STORAGE_CONT_COLL_PROT_REL;
#--drop table if exists CATISSUE_STOR_CONT_SPEC_CLASS;
#--drop table if exists CATISSUE_STOR_CONT_STOR_TYPE_REL;
drop table if exists CATISSUE_STORAGE_CONT_DETAILS;
alter table CATISSUE_STORAGE_CONTAINER add column CONTAINER_NAME varchar(100) not null;
#--------Adding container_name in storage_container table
#--alter table CATISSUE_CONTAINER add column CONTAINER_NAME varchar (100) NOT NULL;

#--------Give values same as identifier to container_name for previouly added containers.

#--------drop the container_number column
alter table CATISSUE_STORAGE_CONTAINER drop column CONTAINER_NUMBER;

alter table CATISSUE_STORAGE_TYPE add column ACTIVITY_STATUS varchar(30) default NULL;
#-------- set default Activity status to 'Active ' for all storage types
update CATISSUE_STORAGE_TYPE set ACTIVITY_STATUS='Active' where type!='Any';

#-------- copying first row of storage types to temp table and addind "Any" entry and adding first row to the last identifier
drop table if exists catissue_temp_type;


CREATE TABLE catissue_temp_type (                                                                                                      
                         `IDENTIFIER` bigint(20),                                                                                          
                         `TYPE` varchar(50),                                                                                                   
                         `DEFAULT_TEMP_IN_CENTIGRADE` double,                                                                                         
                         `ONE_DIMENSION_LABEL` varchar(50),                                                                                           
                         `TWO_DIMENSION_LABEL` varchar(50),                                                                                           
                         `STORAGE_CONTAINER_CAPACITY_ID` bigint(20),                                                                                  
                         `ACTIVITY_STATUS` varchar(30)
                       );
                                                                                              
insert into catissue_temp_type (select * from catissue_storage_type where identifier=1 and type!='Any');
update catissue_temp_type set identifier=(select max(identifier)+1 from catissue_storage_type);
update catissue_storage_type set type='Any',activity_status='Disabled' where identifier=1;
insert into catissue_storage_type (select * from catissue_temp_type);
update catissue_storage_container set storage_type_id=(select identifier from catissue_temp_type) where storage_type_id=1;

insert into catissue_storage_type (type,activity_status,identifier) values ('Any','Disabled',1);

drop table catissue_temp_type;
#----------Chnages finish



rename table catissue_storage_cont_capacity to CATISSUE_CAPACITY;


#--alter table CATISSUE_CONTAINER_TYPE drop foreign key FKCBBC9954DAC76C0;
#--drop table if exists CATISSUE_CONTAINER_TYPE;

#-- Creating table catissue_container_type
create table CATISSUE_CONTAINER_TYPE (
   IDENTIFIER bigint not null auto_increment,
   CAPACITY_ID bigint ,
   NAME varchar(100),
   ONE_DIMENSION_LABEL varchar(100),
   TWO_DIMENSION_LABEL varchar(100),
   COMMENT text,
   primary key (IDENTIFIER)
   );

#-- altering table catissue_storage_type
#--alter table catissue_capacity Engine = INNODB;
alter table CATISSUE_CONTAINER_TYPE add index FKCBBC9954DAC76C0 (CAPACITY_ID);
alter table CATISSUE_CONTAINER_TYPE add constraint FKCBBC9954DAC76C0 foreign key (CAPACITY_ID) references CATISSUE_CAPACITY (IDENTIFIER);

insert into catissue_container_type(Identifier,capacity_id,name,one_dimension_label,two_dimension_label) (select identifier,storage_container_capacity_id,type,one_dimension_label,two_dimension_label from catissue_storage_type);

alter table CATISSUE_STORAGE_TYPE drop foreign key FKE9A0629A5F7CB0FE ;
alter table catissue_storage_type drop column type;
alter table catissue_storage_type drop column one_dimension_label;
alter table catissue_storage_type drop column two_dimension_label;
alter table catissue_storage_type drop column storage_container_capacity_id;
alter table catissue_storage_type change DEFAULT_TEMP_IN_CENTIGRADE DEFAULT_TEMPERATURE double null; 


#--create rel table for type and speicmen class entries
create table CATISSUE_STOR_TYPE_SPEC_CLASS (
   STORAGE_TYPE_ID bigint not null,
   SPECIMEN_CLASS varchar(50),
   SPECIMEN_CLASS_ID bigint	
);

#-- create rel table for type and type relation
create table CATISSUE_STOR_TYPE_HOLDS_TYPE (
   STORAGE_TYPE_ID BIGINT not null,
   HOLDS_STORAGE_TYPE_ID BIGINT not null,
   primary key (STORAGE_TYPE_ID, HOLDS_STORAGE_TYPE_ID)
);

#-- adding foreign key constraints
#--alter table catissue_storage_type Engine = INNODB;
alter table CATISSUE_STOR_TYPE_HOLDS_TYPE add index (STORAGE_TYPE_ID), add constraint FK185C50B59A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);
alter table CATISSUE_STOR_TYPE_HOLDS_TYPE add index (HOLDS_STORAGE_TYPE_ID), add constraint FK185C50B81236791 foreign key (HOLDS_STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);
alter table CATISSUE_STOR_TYPE_SPEC_CLASS add index FK1BCF33BA59A3CE5C (STORAGE_TYPE_ID), add constraint FK1BCF33BA59A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);

update `csm_protection_element` set `PROTECTION_ELEMENT_ID`='44',`PROTECTION_ELEMENT_NAME`='Capacity',`PROTECTION_ELEMENT_DESCRIPTION`='Capacity Class',`OBJECT_ID`='edu.wustl.catissuecore.domain.Capacity',`ATTRIBUTE`=NULL,`PROTECTION_ELEMENT_TYPE_ID`=NULL,`APPLICATION_ID`='1',`UPDATE_DATE`='0000-00-00' where `PROTECTION_ELEMENT_ID`='44';

 

#--alter table CATISSUE_CONTAINER drop foreign key FK49B8DE5DB097B2E;
#--alter table CATISSUE_CONTAINER drop foreign key FK49B8DE5DAC76C0;
drop table if exists CATISSUE_CONTAINER;;
create table CATISSUE_CONTAINER (
   IDENTIFIER bigint not null auto_increment,
   ACTIVITY_STATUS varchar(20),
   BARCODE varchar(100),
   CAPACITY_ID bigint,
   PARENT_CONTAINER_ID bigint,
   COMMENT text,
   FULL bit,
   NAME varchar(100),
   POSITION_DIMENSION_ONE integer,
   POSITION_DIMENSION_TWO integer,
   primary key (IDENTIFIER)
);

#--update catissue_storage_container set container_name = identifier;
insert into CATISSUE_CONTAINER(IDENTIFIER,ACTIVITY_STATUS,BARCODE,CAPACITY_ID,PARENT_CONTAINER_ID,FULL,NAME,POSITION_DIMENSION_ONE,POSITION_DIMENSION_TWO) (SELECT IDENTIFIER,ACTIVITY_STATUS,BARCODE,STORAGE_CONTAINER_CAPACITY_ID,PARENT_CONTAINER_ID,IS_CONTAINER_FULL,CONTAINER_NAME,POSITION_DIMENSION_ONE,POSITION_DIMENSION_TWO FROM CATISSUE_STORAGE_CONTAINER);

alter table CATISSUE_CONTAINER add index FK49B8DE5DB097B2E (PARENT_CONTAINER_ID), add constraint FK49B8DE5DB097B2E foreign key (PARENT_CONTAINER_ID) references CATISSUE_CONTAINER (IDENTIFIER);
alter table CATISSUE_CONTAINER add index FK49B8DE5DAC76C0 (CAPACITY_ID), add constraint FK49B8DE5DAC76C0 foreign key (CAPACITY_ID) references CATISSUE_CAPACITY (IDENTIFIER);


#--insert into catissue_container (IDENTIFIER,ACTIVITY_STATUS,CAPACITY_ID,FULL,NAME)values(1,'Active',2,0,'aa');
ALTER TABLE CATISSUE_STORAGE_CONTAINER DROP foreign key FK28429D015F7CB0FE;
ALTER TABLE CATISSUE_STORAGE_CONTAINER DROP foreign key FK28429D01DB097B2E;

ALTER TABLE CATISSUE_STORAGE_CONTAINER DROP COLUMN IS_CONTAINER_FULL;
ALTER TABLE CATISSUE_STORAGE_CONTAINER DROP COLUMN ACTIVITY_STATUS;
ALTER TABLE CATISSUE_STORAGE_CONTAINER DROP COLUMN BARCODE;
ALTER TABLE CATISSUE_STORAGE_CONTAINER DROP COLUMN PARENT_CONTAINER_ID;
ALTER TABLE CATISSUE_STORAGE_CONTAINER DROP COLUMN STORAGE_CONTAINER_CAPACITY_ID;
ALTER TABLE CATISSUE_STORAGE_CONTAINER DROP COLUMN POSITION_DIMENSION_ONE;
ALTER TABLE CATISSUE_STORAGE_CONTAINER DROP COLUMN POSITION_DIMENSION_TWO;
ALTER TABLE CATISSUE_STORAGE_CONTAINER DROP COLUMN CONTAINER_NAME;
ALTER TABLE CATISSUE_STORAGE_CONTAINER add index FK28429D01BC7298A9 (IDENTIFIER), add constraint FK28429D01BC7298A9 foreign key (IDENTIFIER) references CATISSUE_CONTAINER (IDENTIFIER);


create table CATISSUE_STORAGE_CONT_COLL_PROT_REL (
   STORAGE_CONTAINER_ID bigint not null,
   COLLECTION_PROTOCOL_ID bigint not null,
   primary key (STORAGE_CONTAINER_ID, COLLECTION_PROTOCOL_ID)
);
#--alter table CATISSUE_STORAGE_CONTAINER Engine = INNODB;
#--alter table CATISSUE_COLLECTION_PROTOCOL Engine = INNODB;
alter table CATISSUE_STORAGE_CONT_COLL_PROT_REL add index FK3AE9FCA7B3DFB11D (STORAGE_CONTAINER_ID), add constraint FK3AE9FCA7B3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);
alter table CATISSUE_STORAGE_CONT_COLL_PROT_REL add index FK3AE9FCA748304401 (COLLECTION_PROTOCOL_ID), add constraint FK3AE9FCA748304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);

create table CATISSUE_STOR_CONT_SPEC_CLASS (
   STORAGE_CONTAINER_ID bigint not null,
   SPECIMEN_CLASS varchar(50),
   SPECIMEN_CLASS_ID bigint	
);

alter table CATISSUE_STOR_CONT_SPEC_CLASS add index FKE7F5E8C2B3DFB11D (STORAGE_CONTAINER_ID), add constraint FKE7F5E8C2B3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);

create table CATISSUE_STOR_CONT_STOR_TYPE_REL (
   STORAGE_CONTAINER_ID bigint not null,
   STORAGE_TYPE_ID bigint not null,
   primary key (STORAGE_CONTAINER_ID, STORAGE_TYPE_ID)
);
alter table CATISSUE_STOR_CONT_STOR_TYPE_REL add index FK703B902159A3CE5C (STORAGE_TYPE_ID), add constraint FK703B902159A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);
alter table CATISSUE_STOR_CONT_STOR_TYPE_REL add index FK703B9021B3DFB11D (STORAGE_CONTAINER_ID), add constraint FK703B9021B3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);

update `csm_protection_element` set `PROTECTION_ELEMENT_ID`='184',`PROTECTION_ELEMENT_NAME`='Capacity_systemIdentifier',`PROTECTION_ELEMENT_DESCRIPTION`='Capacity systemIdentifier attribute',`OBJECT_ID`='edu.wustl.catissuecore.domain.Capacity',`ATTRIBUTE`='systemIdentifier',`PROTECTION_ELEMENT_TYPE_ID`=NULL,`APPLICATION_ID`='1',`UPDATE_DATE`='0000-00-00' where `PROTECTION_ELEMENT_ID`='184'
update `csm_protection_element` set `PROTECTION_ELEMENT_ID`='185',`PROTECTION_ELEMENT_NAME`='Capacity_oneDimensionCapacity',`PROTECTION_ELEMENT_DESCRIPTION`='Capacity oneDimensionCapacity attribute',`OBJECT_ID`='edu.wustl.catissuecore.domain.Capacity',`ATTRIBUTE`='oneDimensionCapacity',`PROTECTION_ELEMENT_TYPE_ID`=NULL,`APPLICATION_ID`='1',`UPDATE_DATE`='0000-00-00' where `PROTECTION_ELEMENT_ID`='185'
update `csm_protection_element` set `PROTECTION_ELEMENT_ID`='186',`PROTECTION_ELEMENT_NAME`='Capacity_twoDimensionCapacity',`PROTECTION_ELEMENT_DESCRIPTION`='Capacity twoDimensionCapacity attribute',`OBJECT_ID`='edu.wustl.catissuecore.domain.Capacity',`ATTRIBUTE`='twoDimensionCapacity',`PROTECTION_ELEMENT_TYPE_ID`=NULL,`APPLICATION_ID`='1',`UPDATE_DATE`='0000-00-00' where `PROTECTION_ELEMENT_ID`='186'



#-- changes finish for storage type and storage container



#-----Start---Bug 2088: changes done on:19\07\2006--------

#--------Adding Specimen Collection Group Name in CATISSUE_SPECIMEN_COLL_GROUP table
alter table CATISSUE_SPECIMEN_COLL_GROUP add column NAME varchar(50) NOT NULL;


#--------Give values to group_name for previouly added specimen collection groups.
update CATISSUE_SPECIMEN_COLL_GROUP set NAME=IDENTIFIER where NAME='';
alter table CATISSUE_SPECIMEN_COLL_GROUP add constraint NAME unique (NAME);
INSERT INTO CATISSUE_INTERFACE_COLUMN_DATA (IDENTIFIER,TABLE_ID,COLUMN_NAME,ATTRIBUTE_TYPE) VALUES (305 ,35 , 'NAME','varchar');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (30, 305 , 'Specimen Collection Group Name');
alter table CATISSUE_SPECIMEN_COLL_GROUP change column NAME NAME varchar(55);
#-----End---Bug 2088: changes done on:19\07\2006--------

#-- poornima's file
#--Changes in Specimen Requirement table 
create table CATISSUE_QUANTITY (
   IDENTIFIER bigint not null auto_increment,
   QUANTITY double precision,
   primary key (IDENTIFIER)
);

insert into catissue_quantity(identifier,quantity) (select identifier,quantity from catissue_specimen_requirement);

alter table catissue_specimen_requirement drop column QUANTITY,add column QUANTITY_ID bigint (20);   

alter table catissue_specimen_requirement add foreign key (QUANTITY_ID) references catissue_quantity (IDENTIFIER) on delete cascade on update cascade ;

insert into catissue_specimen_requirement(quantity_id) (select identifier from catissue_quantity);

#-- Changes for Specimen Array functionality
create table CATISSUE_SPECIMEN_ARRAY (
   IDENTIFIER bigint not null,
   CREATED_BY_ID bigint,
   SPECIMEN_ARRAY_TYPE_ID bigint,
   STORAGE_CONTAINER_ID bigint,
   DISTRIBUTION_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN_ARRAY_TYPE (
   IDENTIFIER bigint not null,
   SPECIMEN_CLASS varchar(50),
   primary key (IDENTIFIER)
);

create table CATISSUE_SPECIMEN_ARRAY_CONTENT (
   IDENTIFIER bigint not null auto_increment,
   CONC_IN_MICROGM_PER_MICROLTR double precision,
   INITIAL_QUANTITY_ID bigint,
   POSITION_DIMENSION_ONE integer,
   POSITION_DIMENSION_TWO integer,
   SPECIMEN_ID bigint,
   SPECIMEN_ARRAY_ID bigint,
   primary key (IDENTIFIER)
);


create table CATISSUE_SPECIMEN_TYPE (
   SPECIMEN_ARRAY_TYPE_ID bigint not null,
   SPECIMEN_TYPE varchar(50)
);



alter table CATISSUE_SPECIMEN_ARRAY_CONTENT add index FKBEA9D458C4A3C438 (SPECIMEN_ARRAY_ID), add constraint FKBEA9D458C4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER);
#--alter table CATISSUE_SPECIMEN Engine = INNODB;
alter table CATISSUE_SPECIMEN_ARRAY_CONTENT add index FKBEA9D45860773DB2 (SPECIMEN_ID), add constraint FKBEA9D45860773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ARRAY_CONTENT add index FKBEA9D45892AB74B4 (INITIAL_QUANTITY_ID), add constraint FKBEA9D45892AB74B4 foreign key (INITIAL_QUANTITY_ID) references CATISSUE_QUANTITY (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ARRAY add index FKECBF8B3E64B129CC (CREATED_BY_ID), add constraint FKECBF8B3E64B129CC foreign key (CREATED_BY_ID) references CATISSUE_USER (IDENTIFIER);
#--alter table CATISSUE_DISTRIBUTION Engine = INNODB;
alter table CATISSUE_SPECIMEN_ARRAY add index FKECBF8B3EF8278B6 (DISTRIBUTION_ID), add constraint FKECBF8B3EF8278B6 foreign key (DISTRIBUTION_ID) references CATISSUE_DISTRIBUTION (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ARRAY add index FKECBF8B3EBC7298A9 (IDENTIFIER), add constraint FKECBF8B3EBC7298A9 foreign key (IDENTIFIER) references CATISSUE_CONTAINER (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ARRAY add index FKECBF8B3EB3DFB11D (STORAGE_CONTAINER_ID), add constraint FKECBF8B3EB3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ARRAY add index FKECBF8B3EECE89343 (SPECIMEN_ARRAY_TYPE_ID), add constraint FKECBF8B3EECE89343 foreign key (SPECIMEN_ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ARRAY_TYPE add index FKD36E0B9BBC7298A9 (IDENTIFIER), add constraint FKD36E0B9BBC7298A9 foreign key (IDENTIFIER) references CATISSUE_CONTAINER_TYPE (IDENTIFIER);
alter table CATISSUE_SPECIMEN_TYPE add index FKFF69C195ECE89343 (SPECIMEN_ARRAY_TYPE_ID), add constraint FKFF69C195ECE89343 foreign key (SPECIMEN_ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE (IDENTIFIER);
#-- poornima's file end



# ------ CSM update for Similar Containers Action --------
# ------ chetan 04-07-2006 ---------
# ------ Note: The values which are used for Identifiers like PG_PE_ID, PROTECTION_ELEMENT_ID, should be replaced by appropriate values (Max(Indetifier)+1) by doing a
# ------ Select Max(<Indetifier>) from <corresponding-table>. It is assumed that "17" is the Identifier for PROTECTION_GROUP "Adminstrative".
INSERT INTO `CSM_PROTECTION_ELEMENT` (`PROTECTION_ELEMENT_ID`,`PROTECTION_ELEMENT_NAME`,`PROTECTION_ELEMENT_DESCRIPTION`,`OBJECT_ID`,`ATTRIBUTE`,`PROTECTION_ELEMENT_TYPE_ID`,`APPLICATION_ID`,`UPDATE_DATE`) VALUES (9999,'edu.wustl.catissuecore.action.SimilarContainersAction','edu.wustl.catissuecore.action.SimilarContainersAction','edu.wustl.catissuecore.action.SimilarContainersAction',NULL,NULL,1,'2006-07-04');
INSERT INTO `CSM_PG_PE` (`PG_PE_ID`,`PROTECTION_GROUP_ID`,`PROTECTION_ELEMENT_ID`,`UPDATE_DATE`) VALUES (10001,17,9999,'0000-00-00');



#--------------- bug 2058 need Male  and Female values instead of Male gender and Female gender
update catissue_permissible_value set value='Male' where identifier=59;
update catissue_permissible_value set value='Female' where identifier=61;
#-------- finish

#------- Aniruddha : Changes for specimen aliquoting
ALTER TABLE CATISSUE_SPECIMEN ADD COLUMN LABEL varchar(50);
ALTER TABLE CATISSUE_SPECIMEN ADD COLUMN LINEAGE varchar(50);
ALTER TABLE CATISSUE_SPECIMEN ADD COLUMN PATHOLOGICAL_STATUS varchar(50);

ALTER TABLE CATISSUE_SPECIMEN_CHAR drop column PATHOLOGICAL_STATUS;


#---------------- Query Interface Search Related changes ----------
delete from CATISSUE_RELATED_TABLES_MAP;
delete from CATISSUE_TABLE_RELATION;
delete from CATISSUE_SEARCH_DISPLAY_DATA;
delete from CATISSUE_INTERFACE_COLUMN_DATA;
delete from CATISSUE_QUERY_TABLE_DATA;

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 4, 'CATISSUE_ADDRESS', 'Address', 'Address', 2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 1, 4, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 2, 4, 'STREET', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 3, 4, 'CITY', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 4, 4, 'STATE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 5, 4, 'COUNTRY', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 6, 4, 'ZIPCODE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 7, 4, 'PHONE_NUMBER', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 8, 4, 'FAX_NUMBER', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 39, 'CATISSUE_AUDIT_EVENT', 'Audit Event', 'AuditEvent',1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 9, 39, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 10, 39, 'IP_ADDRESS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 11, 39, 'EVENT_TIMESTAMP', 'timestamp');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 12, 39, 'USER_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 13, 39, 'COMMENTS', 'text');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 40, 'CATISSUE_AUDIT_EVENT_DETAILS', 'Audit Event Details', 'AuditEventDetails',1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 14, 40, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 15, 40, 'ELEMENT_NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 16, 40, 'PREVIOUS_VALUE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 17, 40, 'CURRENT_VALUE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 18, 40, 'AUDIT_EVENT_LOG_ID', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 41, 'CATISSUE_AUDIT_EVENT_LOG', 'Audit Event Log', 'AuditEventLog',1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 19, 41, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 20, 41, 'OBJECT_IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 21, 41, 'OBJECT_NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 22, 41, 'EVENT_TYPE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 23, 41, 'AUDIT_EVENT_ID', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 8, 'CATISSUE_BIOHAZARD', 'Biohazard', 'Biohazard',1,1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 24, 8, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 25, 8, 'NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 26, 8, 'COMMENTS', 'text');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 27, 8, 'TYPE', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 7, 'CATISSUE_CANCER_RESEARCH_GROUP', 'Cancer Research Group', 'CancerResearchGroup',1,1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 28, 7, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 29, 7, 'NAME', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 42, 'CATISSUE_CDE', 'Cde', 'Cde',0);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 30, 42, 'PUBLIC_ID', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 31, 42, 'LONG_NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 32, 42, 'DEFINITION', 'text');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 33, 42, 'VERSION', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 43, 'CATISSUE_CELL_SPE_REVIEW_PARAM', 'Cell Specimen Review Parameters', 'CellSpecimenParam',2,2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 39, 43, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 40, 43, 'NEOPLASTIC_CELLULARITY_PER', 'double');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 41, 43, 'VIABLE_CELL_PERCENTAGE', 'double');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 44, 'CATISSUE_IN_OUT_EVENT_PARAM', 'Checkin Checkout Event Parameter', 'CheckinoutEventParam',2,2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 42, 44, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 43, 44, 'STORAGE_STATUS', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 26, 'CATISSUE_CLINICAL_REPORT', 'Clinical Report', 'ClinicalReport',2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 44, 26, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 45, 26, 'SURGICAL_PATHOLOGICAL_NUMBER', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 46, 26, 'PARTICIPENT_MEDI_IDENTIFIER_ID', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 17, 'CATISSUE_COLL_COORDINATORS', 'Collection Coordinators', 'CollectionCoordinators',2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 47, 17, 'COLLECTION_PROTOCOL_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 48, 17, 'USER_ID', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 45, 'CATISSUE_COLL_DISTRIBUTION_REL', 'Collection Distribution Relation', 'CollectionDistributionRel',2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 49, 45, 'COLLECTION_PROTOCOL_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 50, 45, 'DISTRIBUTION_PROTOCOL_ID', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 46, 'CATISSUE_COLL_EVENT_PARAM', 'Collection Event Parameters', 'CollectionEventParam',2,2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 51, 46, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 52, 46, 'COLLECTION_PROCEDURE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 53, 46, 'CONTAINER', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 10, 'CATISSUE_COLLECTION_PROTOCOL', 'Collection Protocol', 'CollectionProtocol',2,1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 54, 10, 'IDENTIFIER', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 11, 'CATISSUE_COLL_PROT_EVENT', 'Collection Protocol Event', 'CollectionProtocolEvent',2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 55, 11, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 56, 11, 'CLINICAL_STATUS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 57, 11, 'STUDY_CALENDAR_EVENT_POINT', 'double');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 58, 11, 'COLLECTION_PROTOCOL_ID', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 27, 'CATISSUE_COLL_PROT_REG', 'Collection Protocol Registration', 'CollectionProtReg',2,1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 59, 27, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 60, 27, 'PROTOCOL_PARTICIPANT_ID', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 61, 27, 'REGISTRATION_DATE', 'date');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 62, 27, 'PARTICIPANT_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 63, 27, 'COLLECTION_PROTOCOL_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 64, 27, 'ACTIVITY_STATUS', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 18, 'CATISSUE_COLL_SPECIMEN_REQ', 'Collection Specimen Requirement', 'CollectionSpecReq',2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 65, 18, 'COLLECTION_PROTOCOL_EVENT_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 66, 18, 'SPECIMEN_REQUIREMENT_ID', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 5, 'CATISSUE_DEPARTMENT', 'Department', 'Department',1, 1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 67, 5, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 68, 5, 'NAME', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 47, 'CATISSUE_DISPOSAL_EVENT_PARAM', 'Disposal Event Parameters', 'DisposalEventParam',2,2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 69, 47, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 70, 47, 'REASON', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 37, 'CATISSUE_DISTRIBUTED_ITEM', 'Distributed Item', 'DistributedItem',1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 71, 37, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 72, 37, 'QUANTITY', 'double');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 73, 37, 'SPECIMEN_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 74, 37, 'DISTRIBUTION_ID', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 38, 'CATISSUE_DISTRIBUTION', 'Distribution', 'Distribution', 2, 1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 75, 38, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 76, 38, 'TO_SITE_ID', 'bigint');
/*insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 77, 38, 'FROM_SITE_ID', 'bigint');*/
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 78, 38, 'DISTRIBUTION_PROTOCOL_ID', 'bigint');
/* Kapil: Added for distribution */
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 297, 38, 'ACTIVITY_STATUS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 298, 38, 'EVENT_TIMESTAMP', 'timestampdate');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 299, 38, 'USER_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 300, 38, 'COMMENTS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 301, 38, 'SPECIMEN_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 302, 38, 'EVENT_TIMESTAMP', 'timestamptime');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 19, 'CATISSUE_DISTRIBUTION_PROTOCOL', 'Distribution Protocol', 'DistributionProtocol',2, 1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 79, 19, 'IDENTIFIER', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 20, 'CATISSUE_DISTRIBUTION_SPE_REQ', 'Distribution Specimen Requirement', 'DistributionSpecReq',1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 80, 20, 'DISTRIBUTION_PROTOCOL_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 81, 20, 'SPECIMEN_REQUIREMENT_ID', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 48, 'CATISSUE_EMBEDDED_EVENT_PARAM', 'Embedded Event Parameters', 'EmbeddedEventParam',2,2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 82, 48, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 83, 48, 'EMBEDDING_MEDIUM', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 28, 'CATISSUE_EXTERNAL_IDENTIFIER', 'External Identifier', 'ExternalIdentifier',2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 84, 28, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 85, 28, 'NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 86, 28, 'VALUE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 87, 28, 'SPECIMEN_ID', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 49, 'CATISSUE_FIXED_EVENT_PARAM', 'Fixed Event Parameters', 'FixedEventParameters',2,2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 88, 49, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 89, 49, 'FIXATION_TYPE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 90, 49, 'DURATION_IN_MINUTES', 'integer');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 50, 'CATISSUE_FLUID_SPE_EVENT_PARAM', 'Fluid Specimen Review Event Parameters', 'FluidSpecEventParam',2,2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 96, 50, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 97, 50, 'CELL_COUNT', 'double');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 51, 'CATISSUE_FROZEN_EVENT_PARAM', 'Frozen Event Parameters', 'FrozenEventParameters',2,2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 98, 51, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 99, 51, 'METHOD', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 6, 'CATISSUE_INSTITUTION', 'Institution', 'Institution',1, 1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 100, 6, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 101, 6, 'NAME', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 52, 'CATISSUE_MOL_SPE_REVIEW_PARAM', 'Molecular Specimen Review Parameters', 'MolecularSpecParam',2,2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 108, 52, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 109, 52, 'GEL_IMAGE_URL', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 110, 52, 'QUALITY_INDEX', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 111, 52, 'LANE_NUMBER', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 112, 52, 'GEL_NUMBER', 'integer');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 113, 52, 'ABSORBANCE_AT_260', 'double');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 114, 52, 'ABSORBANCE_AT_280', 'double');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 115, 52, 'RATIO_28S_TO_18S', 'double');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 31, 'CATISSUE_PARTICIPANT', 'Participant', 'Participant',2, 1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 116, 31, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 117, 31, 'LAST_NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 118, 31, 'FIRST_NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 119, 31, 'MIDDLE_NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 120, 31, 'BIRTH_DATE', 'date');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 303, 31, 'DEATH_DATE', 'date');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 304, 31, 'VITAL_STATUS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 121, 31, 'GENDER', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 122, 31, 'GENOTYPE', 'varchar');
/*insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 123, 31, 'RACE', 'varchar');*/
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 124, 31, 'ETHNICITY', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 125, 31, 'SOCIAL_SECURITY_NUMBER', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 126, 31, 'ACTIVITY_STATUS', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 73, 'CATISSUE_RACE', 'Race', 'Race',0);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 316, 73, 'PARTICIPANT_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 317, 73, 'RACE_NAME', 'varchar');


insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 32, 'CATISSUE_PART_MEDICAL_ID', 'Participant Medical Identifier', 'ParticipantMedicalId',2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 127, 32, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 128, 32, 'MEDICAL_RECORD_NUMBER', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 129, 32, 'SITE_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 130, 32, 'PARTICIPANT_ID', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 53, 'CATISSUE_PERMISSIBLE_VALUE', 'Permissible Value', 'PermissibleValue',0);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 131, 53, 'IDENTIFIER', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 132, 53, 'CONCEPT_CODE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 133, 53, 'DEFINITION', 'text');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 134, 53, 'EVS_CODE', 'text');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 135, 53, 'PARENT_IDENTIFIER', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 136, 53, 'VALUE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 137, 53, 'PUBLIC_ID', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 54, 'CATISSUE_PROCEDURE_EVENT_PARAM', 'Procedure Event Parameters', 'ProcedureEventParam',2,2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 138, 54, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 139, 54, 'URL', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 140, 54, 'NAME', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 55, 'CATISSUE_INTERFACE_COLUMN_DATA', 'Query Interface Column Data', 'QueryInterfaceColumnData',0);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 141, 55, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 142, 55, 'TABLE_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 143, 55, 'COLUMN_NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 144, 55, 'DISPLAY_NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 145, 55, 'ATTRIBUTE_TYPE', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 56, 'CATISSUE_QUERY_TABLE_DATA', 'Query Interface Table Data', 'QueryInterfaceTableData',0);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 146, 56, 'TABLE_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 147, 56, 'DISPLAY_NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 148, 56, 'TABLE_NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 149, 56, 'ALIAS_NAME', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 57, 'CATISSUE_RECEIVED_EVENT_PARAM', 'Received Event Parameters', 'ReceivedEventParam',2,2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 150, 57, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 151, 57, 'RECEIVED_QUALITY', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 58, 'CATISSUE_RELATED_TABLES_MAP', 'Related Tables Map', 'RelatedTablesMap',0);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 152, 58, 'FIRST_TABLE_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 153, 58, 'SECOND_TABLE_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 154, 58, 'FIRST_TABLE_JOIN_COLUMN', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 155, 58, 'SECOND_TABLE_JOIN_COLUMN', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 59, 'CATISSUE_REPORTED_PROBLEM', 'Reported Problem', 'ReportedProblem',1, 1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 156, 59, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 157, 59, 'AFFILIATION', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 158, 59, 'NAME_OF_REPORTER', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 159, 59, 'REPORTERS_EMAIL_ID', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 160, 59, 'MESSAGE_BODY', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 161, 59, 'SUBJECT', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 162, 59, 'REPORTED_DATE', 'date');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 163, 59, 'ACTIVITY_STATUS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 164, 59, 'COMMENTS', 'text');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 60, 'CATISSUE_EVENT_PARAM', 'Review Event Parameters', 'ReviewEventParameters',2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 165, 60, 'IDENTIFIER', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 61, 'CATISSUE_SIGNUP_USER', 'Signup User', 'SignupUser',2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 166, 61, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 167, 61, 'FIRST_NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 168, 61, 'LAST_NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 169, 61, 'LOGIN_NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 170, 61, 'EMAIL_ADDRESS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 171, 61, 'START_DATE', 'date');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 172, 61, 'ACTIVITY_STATUS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 173, 61, 'DEPARTMENT_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 174, 61, 'STREET', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 175, 61, 'CITY', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 176, 61, 'STATE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 177, 61, 'COUNTRY', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 178, 61, 'ZIPCODE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 179, 61, 'PHONE_NUMBER', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 180, 61, 'FAX_NUMBER', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 181, 61, 'CANCER_RESEARCH_GROUP_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 182, 61, 'INSTITUTION_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 183, 61, 'STATUS_COMMENT', 'text');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 3, 'CATISSUE_SITE', 'Site', 'Site',2, 1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 184, 3, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 185, 3, 'NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 186, 3, 'TYPE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 187, 3, 'EMAIL_ADDRESS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 188, 3, 'USER_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 189, 3, 'ACTIVITY_STATUS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 190, 3, 'ADDRESS_ID', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID,FOR_SQI) values ( 33, 'CATISSUE_SPECIMEN', 'Specimen', 'Specimen',2,1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 191, 33, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 192, 33, 'TYPE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 193, 33, 'AVAILABLE', 'tinyint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 194, 33, 'POSITION_DIMENSION_ONE', 'integer');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 195, 33, 'POSITION_DIMENSION_TWO', 'integer');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 196, 33, 'BARCODE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 197, 33, 'COMMENTS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 198, 33, 'ACTIVITY_STATUS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 199, 33, 'PARENT_SPECIMEN_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 200, 33, 'STORAGE_CONTAINER_IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 201, 33, 'SPECIMEN_COLLECTION_GROUP_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 202, 33, 'SPECIMEN_CHARACTERISTICS_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 208, 33, 'PATHOLOGICAL_STATUS', 'varchar');

insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 102, 33, 'SPECIMEN_CLASS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 103, 33, 'QUANTITY_ID', 'double');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 104, 33, 'AVAILABLE_QUANTITY_ID', 'double');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 105, 33, 'CONCENTRATION', 'double');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID,FOR_SQI) values ( 74, 'CATISSUE_QUANTITY', 'Quantity', 'Quantity',2,1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 318, 74, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 319, 74, 'QUANTITY', 'double');


insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 62, 'CATISSUE_SPECIMEN_BIOHZ_REL', 'Biohazard', 'SpecimenBiohazardRel',2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 203, 62, 'BIOHAZARD_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 204, 62, 'SPECIMEN_ID', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 34, 'CATISSUE_SPECIMEN_CHAR', 'Specimen Characteristics', 'SpecimenCharacteristics',2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 205, 34, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 206, 34, 'TISSUE_SITE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 207, 34, 'TISSUE_SIDE', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 35, 'CATISSUE_SPECIMEN_COLL_GROUP', 'Specimen Collection Group', 'SpecimenCollectionGroup',2,1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 209, 35, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 210, 35, 'CLINICAL_DIAGNOSIS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 211, 35, 'CLINICAL_STATUS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 212, 35, 'ACTIVITY_STATUS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 213, 35, 'SITE_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 214, 35, 'COLLECTION_PROTOCOL_EVENT_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 215, 35, 'CLINICAL_REPORT_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 216, 35, 'COLLECTION_PROTOCOL_REG_ID', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 63, 'CATISSUE_SPECIMEN_EVENT_PARAM', 'Specimen Event Parameters', 'SpecimenEventParameters',2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 217, 63, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 218, 63, 'SPECIMEN_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 219, 63, 'EVENT_TIMESTAMP', 'timestamptime');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 220, 63, 'USER_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 221, 63, 'COMMENTS', 'text');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 296, 63, 'EVENT_TIMESTAMP', 'timestampdate');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 9, 'CATISSUE_SPECIMEN_PROTOCOL', 'Specimen Protocol', 'SpecimenProtocol',2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 222, 9, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 223, 9, 'PRINCIPAL_INVESTIGATOR_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 224, 9, 'TITLE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 225, 9, 'SHORT_TITLE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 226, 9, 'IRB_IDENTIFIER', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 227, 9, 'START_DATE', 'date');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 228, 9, 'END_DATE', 'date');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 229, 9, 'ENROLLMENT', 'integer');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 230, 9, 'DESCRIPTION_URL', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 231, 9, 'ACTIVITY_STATUS', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 12, 'CATISSUE_SPECIMEN_REQUIREMENT', 'Specimen Requirement', 'SpecimenRequirement',2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 232, 12, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 233, 12, 'SPECIMEN_TYPE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 234, 12, 'TISSUE_SITE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 235, 12, 'PATHOLOGY_STATUS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 37, 12, 'QUANTITY_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 38, 12, 'SPECIMEN_CLASS', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 64, 'CATISSUE_SPUN_EVENT_PARAMETERS', 'Spun Event Parameters', 'SpunEventParameters',2,2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 236, 64, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 237, 64, 'GFORCE', 'double');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 238, 64, 'DURATION_IN_MINUTES', 'integer');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 21, 'CATISSUE_STORAGE_CONTAINER', 'Storage Container', 'StorageContainer',2,1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 239, 21, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 241, 21, 'TEMPERATURE', 'double');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 245, 21, 'STORAGE_TYPE_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 246, 21, 'SITE_ID', 'bigint');


insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 70, 'CATISSUE_CONTAINER', 'Container', 'Container',2,1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 309, 70, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 240, 70, 'NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 242, 70, 'FULL', 'tinyint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 243, 70, 'BARCODE', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 244, 70, 'ACTIVITY_STATUS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 247, 70, 'PARENT_CONTAINER_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 248, 70, 'CAPACITY_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 249, 70, 'POSITION_DIMENSION_ONE', 'integer');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 250, 70, 'POSITION_DIMENSION_TWO', 'integer');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 310, 70, 'COMMENT', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 2, 'CATISSUE_CAPACITY', 'Storage Container Capacity', 'StorageContainerCapacity',2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 251, 2, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 252, 2, 'ONE_DIMENSION_CAPACITY', 'integer');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 253, 2, 'TWO_DIMENSION_CAPACITY', 'integer');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 1, 'CATISSUE_STORAGE_TYPE', 'Storage Type', 'StorageType',1,1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 258, 1, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 260, 1, 'DEFAULT_TEMPERATURE', 'double');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 305, 1, 'ACTIVITY_STATUS', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 69, 'CATISSUE_CONTAINER_TYPE', 'Container Type', 'ContainerType',1,1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 306, 69, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 307, 69, 'NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 261, 69, 'ONE_DIMENSION_LABEL', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 262, 69, 'TWO_DIMENSION_LABEL', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 263, 69, 'CAPACITY_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 308, 69, 'COMMENT', 'varchar');


insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 65, 'CATISSUE_TABLE_RELATION', 'Table Relation', 'TableRelation',0);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 264, 65, 'PARENT_TABLE_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 265, 65, 'CHILD_TABLE_ID', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 66, 'CATISSUE_THAW_EVENT_PARAMETERS', 'Thaw Event Parameters', 'ThawEventParameters',2,2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 266, 66, 'IDENTIFIER', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 67, 'CATISSUE_TIS_SPE_EVENT_PARAM', 'Tissue Specimen Review Event Parameters', 'TissueSpecEventParam',2,2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 272, 67, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 273, 67, 'NEOPLASTIC_CELLULARITY_PER', 'double');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 274, 67, 'NECROSIS_PERCENTAGE', 'double');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 275, 67, 'LYMPHOCYTIC_PERCENTAGE', 'double');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 276, 67, 'TOTAL_CELLULARITY_PERCENTAGE', 'double');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 277, 67, 'HISTOLOGICAL_QUALITY', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 68, 'CATISSUE_TRANSFER_EVENT_PARAM', 'Transfer Event Parameter', 'TransferEventParameter',2,2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 278, 68, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 279, 68, 'FROM_POSITION_DIMENSION_ONE', 'integer');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 280, 68, 'FROM_POSITION_DIMENSION_TWO', 'integer');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 281, 68, 'TO_POSITION_DIMENSION_ONE', 'integer');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 282, 68, 'TO_POSITION_DIMENSION_TWO', 'integer');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 283, 68, 'TO_STORAGE_CONTAINER_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 284, 68, 'FROM_STORAGE_CONTAINER_ID', 'bigint');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 23, 'CATISSUE_USER', 'User', 'User',2,1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 285, 23, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 286, 23, 'ACTIVITY_STATUS', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 287, 23, 'DEPARTMENT_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 288, 23, 'CANCER_RESEARCH_GROUP_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 289, 23, 'INSTITUTION_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 290, 23, 'ADDRESS_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 291, 23, 'STATUS_COMMENT', 'text');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 292, 23, 'LAST_NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 293, 23, 'FIRST_NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 294, 23, 'LOGIN_NAME', 'varchar');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 295, 23, 'START_DATE', 'date');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 71, 'CATISSUE_SPECIMEN_ARRAY_TYPE', 'Specimen Array Type', 'SpecimenArrayType',2,1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 311, 71, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 312, 71, 'SPECIMEN_CLASS', 'varchar');

insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 72, 'CATISSUE_SPECIMEN_ARRAY', 'Specimen Array', 'SpecimenArray',2,1);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 313, 21, 'IDENTIFIER', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 314, 21, 'CREATED_BY_ID', 'bigint');
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 315, 21, 'SPECIMEN_ARRAY_TYPE_ID', 'bigint');


INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (1,1,1);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (2,6,6);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (3,5,5);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (4,7,7);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (5,8,8);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (6,3,3);

INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (7,3,4);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (12,10,10);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (13,10,9);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (14,10,11);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (15,10,12,'11:18');
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (119,10,74,'11:18:12');
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (16,10,18,'11');
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (17,19,19);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (18,19,9);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (19,19,20);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (20,19,12,'20');
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (21,23,23);
/*INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (22,23,24);*/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (23,23,4);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (24,23,5);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (25,23,6);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (26,23,7);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (27,31,31);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (28,31,32);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (29,27,27);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (30,35,35);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (31,35,11);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (32,35,3);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (33,35,27);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (34,33,33);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (35,33,34);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (36,38,38);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (37,38,37);

INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (40,10,23,'9');
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (41,10,17);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (43,19,23,'9');
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (44,31,3,'32');
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (45,27,9,'10');
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (46,27,31);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (47,35,26);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (48,35,9,'27:10');
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (50,33,28);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (51,38,23);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (52,38,33,'37');
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (53,38,9,'19');
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (55,38,34);

INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (54,35,31,'27');

/* ************************************** QUERIES FOR SITE ***********************************************************/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (6, 184, 'Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (6, 185, 'Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (6, 186, 'Type');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (6, 187, 'Email Address');
/*INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (6, 189, 'Activity Status');*/

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (7, 2, 'Street');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (7, 3, 'City');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (7, 4, 'State');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (7, 5, 'Country');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (7, 6, 'Zip Code');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (7, 7, 'Phone Number');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (7, 8, 'Fax Number');

/* ************************************** QUERIES FOR INSTITUTION ***********************************************************/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (2, 100, 'Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (2, 101, 'Name');

/* ************************************** QUERIES FOR DEPARTMENT ***********************************************************/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (3, 67, 'Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (3, 68, 'Name');

/* ************************************** QUERIES FOR CRG ******************************************************************/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (4, 28, 'Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (4, 29, 'Name');

/* ************************************** QUERIES FOR BIOHAZARD ***********************************************************/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (5, 24, 'Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (5, 25, 'Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (5, 26, 'Comments');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (5, 27, 'Type');

/* ************************************** QUERIES FOR STORAGE TYPE *********************************************************/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (1, 258, 'Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (1, 260, 'Default Temperature');

INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (108,1,69);
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (108, 307, 'Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (108, 261, 'One Dimension Label');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (108, 262, 'Two Dimension Label');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (108, 308, 'Comments');

/* ************************************** QUERIES FOR STORAGE CONTAINER *****************************************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (8,21,21);
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (8, 239, 'Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (8, 241, 'Temperature');

INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (9,21,2,'70');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (9, 252, 'One Dimension Capacity');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (9, 253, 'Two Dimension Capacity');


INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (10,21,69,'1');
/*INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (10, 259, 'Type');*/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (10, 307, 'Container Type');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (10, 261, 'One Dimension Label');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (10, 262, 'Two Dimension Label');

INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (11,21,3);
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (11, 185, 'Site Name');

INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (110,21,70);
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (110, 240, 'Container Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (110, 242, 'Is Container Full');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (110, 243, 'Barcode');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (110, 247, 'Parent Container Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (110, 249, 'Position Dimension One');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (110, 250, 'Position Dimension Two');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (110, 310, 'Comments');




/*INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (39, 255, 'Parameter Name');*/
/*INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (39, 256, 'Parameter Value');*/

/* ************************************** QUERIES FOR SPECIMEN ARRAY TYPE *********************************************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (111,71,71);
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (111, 311, 'Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (111, 312, 'Specimen Class');

INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (112,71,69);
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (112, 307, 'Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (112, 261, 'One Dimension Label');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (112, 262, 'Two Dimension Label');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (112, 308, 'Comments');

/* ************************************** QUERIES FOR SPECIMEN ARRAY *****************************************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (113,72,72);
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (113, 313, 'Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (113, 314, 'Creator User ID');

INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (114,72,70);
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (114, 240, 'Container Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (114, 242, 'Is Container Full');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (114, 243, 'Barcode');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (114, 247, 'Parent Container Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (114, 249, 'Position Dimension One');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (114, 250, 'Position Dimension Two');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (114, 310, 'Comments');

INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (115,72,2,'70');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (115, 252, 'One Dimension Capacity');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (115, 253, 'Two Dimension Capacity');


INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (116,72,69,'71');
/*INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (116, 259, 'Type');*/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (116, 307, 'Container Type');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (116, 261, 'One Dimension Label');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (116, 262, 'Two Dimension Label');

/* ************************************** QUERIES FOR COLLECTION PROTOCOL ***************************************************/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (12, 54, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (40, 292, 'Principal Investigator Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (40, 293, 'Principal Investigator First Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (42, 292, 'Protocol Coordinator Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (42, 293, 'Protocol Coordinator First Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (13, 224, 'Title');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (13, 225, 'Short Title');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (13, 226, 'IRB Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (13, 227, 'Start Date');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (13, 228, 'End Date');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (13, 229, 'Enrollment');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (13, 230, 'Description URL');
/*INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (13, 231, 'Activity Status');*/

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (14, 56, 'Clinical Status');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (14, 57, 'Study Calendar Event Point');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (15, 233, 'Specimen Type');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (15, 234, 'Tissue Site');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (15, 235, 'Pathological Status');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (16, 65, 'Collection Protocol Event Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (119, 319, 'Required Specimen Quantity');


/* ************************************** QUERIES FOR DISTRIBUTION PROTOCOL *************************************************/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (17, 79, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (18, 224, 'Title');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (18, 225, 'Short Title');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (18, 226, 'IRB Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (18, 227, 'Start Date');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (18, 228, 'End Date');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (18, 229, 'Enrollment');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (18, 230, 'Description URL');
/*INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (18, 231, 'Activity Status');*/

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (20, 233, 'Specimen Type');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (20, 234, 'Tissue Site');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (20, 235, 'Pathological Status');

/*INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (20, 37, 'Required Specimen Quantity');*/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (120,19,74,'12');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (120, 319, 'Required Specimen Quantity');


INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (43, 292, 'Principal Investigator Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (43, 293, 'Principal Investigator First Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (19, 80, 'Distribution Protocol Identifier');

/* ************************************** QUERIES FOR USER ******************************************************************/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (21, 285, 'Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (21, 292, 'Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (21, 293, 'First Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (21, 294, 'Login Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (21, 295, 'Date');
/*INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (21, 286, 'Activity Status');*/

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (23, 2, 'Street');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (23, 3, 'City');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (23, 4, 'State');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (23, 5, 'Country');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (23, 6, 'Zip Code');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (23, 7, 'Phone Number');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (23, 8, 'Fax Number');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (25, 101, 'Institution Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (24, 68, 'Department Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (26, 29, 'Cancer Research Group Name');

/* ************************************** QUERIES FOR PARTICIPANT ***********************************************************/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (27, 116, 'Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (27, 117, 'Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (27, 118, 'First Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (27, 119, 'Middle Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (27, 120, 'Birth Date');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (27, 303, 'Death Date');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (27, 304, 'Vital Status');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (27, 121, 'Gender');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (27, 122, 'Genotype');
/*INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (27, 123, 'Race');*/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (27, 124, 'Ethnicity');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (27, 125, 'Social Security Number');
/*INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (27, 126, 'Activity Status');*/

/*INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (44, 185, 'Medical Record Number Source');*/

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (28, 128, 'Medical Record Number');

INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (117,31,73);
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (117, 317, 'Race Name');

/* ************************************** QUERIES FOR COLLECTION PROTOCOL REGISTRATION ***************************************/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (29, 59, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (45, 224, 'Protocol Title');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (29, 60, 'Participant Protocol Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (29, 61, 'Registration Date');
/*INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (29, 64, 'Activity Status');*/

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (46, 117, 'Participant Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (46, 118, 'Participant First Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (46, 119, 'Participant Middle Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (46, 120, 'Participant Birth Date');

/* ************************************** QUERIES FOR SPECIMEN COLLECTION GROUP **********************************************/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (30, 209, 'Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (30, 210, 'Clinical Diagnosis');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (30, 211, 'Clinical Status');
/*INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (30, 212, 'Activity Status');*/

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (48, 224, 'Collection Protocol Title');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (31, 57, 'Study Calendar Event Point');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (32, 185, 'Site Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (33, 60, 'Protocol Participant Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (47, 45, 'Surgical Pathology Number');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (47, 46, 'Medical Record Number');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (54, 117, 'Participant Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (54, 118, 'Participant First Name');

/* ************************************** QUERIES FOR SPECIMEN ***************************************************************/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (34, 191, 'Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (34, 201, 'Specimen Collection Group Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (34, 199, 'Parent Specimen Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (34, 192, 'Type');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (34, 208, 'Pathological Status');

/*INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (34, 193, 'Is Available');*/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (34, 194, 'Position Dimension One');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (34, 195, 'Position Dimension Two');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (34, 196, 'Barcode');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (34, 105, 'Concentration');

INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (118,33,74);
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (118, 319, 'Quantity');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (35, 206, 'Tissue Site');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (35, 207, 'Tissue Side');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (35, 208, 'Pathological Status'); */

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (50, 85, 'External Identifier Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (50, 86, 'External Identifier Value');

/* ************************************** QUERIES FOR DISTRIBUTION ************************************************************/

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (36, 75, 'Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (36, 298, 'Distribution Date');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (36, 302, 'Distribution Time');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (37, 72, 'Distributed Quantity');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (51, 292, 'Principal Investigator Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (51, 293, 'Principal Investigator First Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (52, 191, 'Distributed Specimen Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (52, 192, 'Distributed Specimen Type');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (55, 206, 'Distributed Specimen Tissue Site');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (55, 207, 'Distributed Specimen Tissue Side');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (55, 208, 'Distributed Specimen Pathological Status');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (53, 224, 'Distribution Protocol Title');



/*TABLE MAP DATA*/
insert into CATISSUE_RELATED_TABLES_MAP values ( 10 , 11 , 'IDENTIFIER','COLLECTION_PROTOCOL_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 5 , 23 , 'IDENTIFIER','DEPARTMENT_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 6 , 23 , 'IDENTIFIER','INSTITUTION_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 7 , 23 , 'IDENTIFIER','CANCER_RESEARCH_GROUP_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 4 , 23 , 'IDENTIFIER','ADDRESS_ID');

/*insert into CATISSUE_RELATED_TABLES_MAP values ( 23 , 3 , 'IDENTIFIER','USER_ID');*/
insert into CATISSUE_RELATED_TABLES_MAP values ( 4 , 3 , 'IDENTIFIER','ADDRESS_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 9 , 10 , 'IDENTIFIER','IDENTIFIER');
insert into CATISSUE_RELATED_TABLES_MAP values ( 23 , 9 , 'IDENTIFIER','PRINCIPAL_INVESTIGATOR_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 23 , 17 , 'IDENTIFIER','USER_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 10 , 17 , 'IDENTIFIER','COLLECTION_PROTOCOL_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 12 , 18, 'IDENTIFIER','SPECIMEN_REQUIREMENT_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 11 , 18 , 'IDENTIFIER','COLLECTION_PROTOCOL_EVENT_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 9 , 19 , 'IDENTIFIER','IDENTIFIER');
insert into CATISSUE_RELATED_TABLES_MAP values ( 12 , 20 , 'IDENTIFIER','SPECIMEN_REQUIREMENT_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 19 , 20 , 'IDENTIFIER','DISTRIBUTION_PROTOCOL_ID');

insert into CATISSUE_RELATED_TABLES_MAP values ( 12 , 74 , 'QUANTITY_ID','IDENTIFIER');

insert into CATISSUE_RELATED_TABLES_MAP values ( 23 , 24 , 'IDENTIFIER','USER_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 31 , 32 , 'IDENTIFIER','PARTICIPANT_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 31 , 73 , 'IDENTIFIER','PARTICIPANT_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 10 , 27 , 'IDENTIFIER','COLLECTION_PROTOCOL_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 31 , 27 , 'IDENTIFIER','PARTICIPANT_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 3 , 35 , 'IDENTIFIER','SITE_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 11 , 35 , 'IDENTIFIER','COLLECTION_PROTOCOL_EVENT_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 26 , 35 , 'IDENTIFIER','CLINICAL_REPORT_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 27 , 35 , 'IDENTIFIER','COLLECTION_PROTOCOL_REG_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 21 , 33 , 'IDENTIFIER','STORAGE_CONTAINER_IDENTIFIER');
insert into CATISSUE_RELATED_TABLES_MAP values ( 35 , 33 , 'IDENTIFIER','SPECIMEN_COLLECTION_GROUP_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 34 , 33 , 'IDENTIFIER','SPECIMEN_CHARACTERISTICS_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 33 , 33 , 'IDENTIFIER','PARENT_SPECIMEN_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 33 , 37 , 'IDENTIFIER','SPECIMEN_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 38 , 37 , 'IDENTIFIER','DISTRIBUTION_ID');
/*insert into CATISSUE_RELATED_TABLES_MAP values ( 38 , 19 , 'DISTRIBUTION_PROTOCOL_ID','IDENTIFIER');*/


/*insert into CATISSUE_RELATED_TABLES_MAP values ( 32 , 3 , 'SITE_ID','IDENTIFIER');*/
insert into CATISSUE_RELATED_TABLES_MAP values ( 38 , 19 , 'DISTRIBUTION_PROTOCOL_ID','IDENTIFIER');

insert into CATISSUE_RELATED_TABLES_MAP values ( 33, 62 , 'IDENTIFIER' , 'SPECIMEN_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 8 , 62 , 'IDENTIFIER' , 'BIOHAZARD_ID');

insert into CATISSUE_RELATED_TABLES_MAP values ( 33, 28 , 'IDENTIFIER','SPECIMEN_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 33, 118 , 'QUANTITY_ID','IDENTIFIER');

INSERT INTO CATISSUE_RELATED_TABLES_MAP VALUES(33,63,'IDENTIFIER','SPECIMEN_ID');

/* ***************** RELATIONSHIP QUERIES FOR STORAGE TYPE ********************************************/
INSERT INTO CATISSUE_RELATED_TABLES_MAP VALUES(1,69,'IDENTIFIER','IDENTIFIER');
/*INSERT INTO CATISSUE_RELATED_TABLES_MAP VALUES(2,69,'IDENTIFIER','CAPACITY_ID');*/

/* ***************** RELATIONSHIP QUERIES FOR STORAGE CONTAINER ********************************************/
insert into CATISSUE_RELATED_TABLES_MAP values ( 3 , 21 , 'IDENTIFIER','SITE_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 2 , 70 , 'IDENTIFIER','CAPACITY_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 1 , 21 , 'IDENTIFIER','STORAGE_TYPE_ID');
insert into CATISSUE_RELATED_TABLES_MAP values ( 70 , 21 , 'IDENTIFIER','IDENTIFIER');
/*insert into CATISSUE_RELATED_TABLES_MAP values ( 22 , 21 , 'STORAGE_CONTAINER_ID','IDENTIFIER');*/

/* ***************** RELATIONSHIP QUERIES FOR SPECIMEN ARRAY TYPE ********************************************/
INSERT INTO CATISSUE_RELATED_TABLES_MAP VALUES(71,69,'IDENTIFIER','IDENTIFIER');

/* ***************** RELATIONSHIP QUERIES FOR SPECIMEN ARRAY  ********************************************/
insert into CATISSUE_RELATED_TABLES_MAP values ( 72 , 71 , 'SPECIMEN_ARRAY_TYPE_ID', 'IDENTIFIER');
insert into CATISSUE_RELATED_TABLES_MAP values ( 72 , 70 , 'IDENTIFIER','IDENTIFIER');

/* ***************** RELATIONSHIP QUERIES FOR CELL SPECIMEN REVIEW EVENT PARAMETERS ********************************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (57, 43, 43);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (58, 43, 63);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (59, 43, 23);

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (57, 39, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (58, 218, 'Specimen Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (58, 219, 'Event Timestamp');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (58, 221, 'Comments');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (59, 292, 'User Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (59, 293, 'User First Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (57, 40, 'Neoplastic Cellularity Percentage');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (57, 41, 'Viable Cell Percentage');

/* ***************** RELATIONSHIP QUERIES FOR CHECK IN CHECK OUT EVENT PARAMETERS ********************************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (60, 44, 44);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (61, 44, 63);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (62, 44, 23);

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (60, 42, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (61, 218, 'Specimen Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (61, 219, 'Event Timestamp');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (61, 221, 'Comments');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (62, 292, 'User Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (62, 293, 'User First Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (60, 43, 'Storage Status');

/* ************************* RELATIONSHIP QUERIES FOR COLLECTION EVENT PARAMETERS ********************************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (63, 46, 46);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (64, 46, 63);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (65, 46, 23);

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (63, 51, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (64, 218, 'Specimen Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (64, 219, 'Event Timestamp');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (64, 221, 'Comments');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (65, 292, 'User Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (65, 293, 'User First Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (63, 52, 'Collection Procedure');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (63, 53, 'Container');

/* ************************* RELATIONSHIP QUERIES FOR DISPOSAL EVENT PARAMETERS ********************************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (66, 47, 47);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (67, 47, 63);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (68, 47, 23);

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (66, 69, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (67, 218, 'Specimen Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (67, 219, 'Event Timestamp');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (67, 221, 'Comments');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (68, 292, 'User Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (68, 293, 'User First Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (66, 70, 'Reason');

/* ************************* RELATIONSHIP QUERIES FOR EMBEDDED EVENT PARAMETERS ********************************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (69, 48, 48);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (70, 48, 63);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (71, 48, 23);

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (69, 82, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (70, 218, 'Specimen Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (70, 219, 'Event Timestamp');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (70, 221, 'Comments');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (71, 292, 'User Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (71, 293, 'User First Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (69, 83, 'Embedding Medium');

/* ************************* RELATIONSHIP QUERIES FOR FIXED EVENT PARAMETERS ***********************************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (72, 49, 49);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (73, 49, 63);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (74, 49, 23);

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (72, 88, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (73, 218, 'Specimen Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (73, 219, 'Event Timestamp');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (73, 221, 'Comments');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (74, 292, 'User Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (74, 293, 'User First Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (72, 89, 'Fixation Type');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (72, 90, 'Duration In Minutes');

/* ************************* RELATIONSHIP QUERIES FOR FLUID SPECIMEN REVIEW EVENT PARAMETERS *******************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (75, 50, 50);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (76, 50, 63);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (77, 50, 23);

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (75, 96, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (76, 218, 'Specimen Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (76, 219, 'Event Timestamp');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (76, 221, 'Comments');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (77, 292, 'User Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (77, 293, 'User First Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (75, 97, 'Cell Count');

/* ************************* RELATIONSHIP QUERIES FOR FROZEN  EVENT PARAMETERS *******************************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (78, 51, 51);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (79, 51, 63);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (80, 51, 23);

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (78, 98, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (79, 218, 'Specimen Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (79, 219, 'Event Timestamp');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (79, 221, 'Comments');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (80, 292, 'User Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (80, 293, 'User First Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (78, 99, 'Method');

/* ************************* RELATIONSHIP QUERIES FOR MOLECULAR SPECIMEN EVENT PARAMETERS ********************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (81, 52, 52);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (82, 52, 63);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (83, 52, 23);

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (81, 108, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (82, 218, 'Specimen Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (82, 219, 'Event Timestamp');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (82, 221, 'Comments');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (83, 292, 'User Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (83, 293, 'User First Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (81, 109, 'Gel Image URL');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (81, 110, 'Quality Index');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (81, 111, 'Lane Number');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (81, 112, 'Gel Number');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (81, 113, 'Absorbance At 260');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (81, 114, 'Absorbance At 280');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (81, 115, 'Ratio 28S to 18S');

/* ************************* RELATIONSHIP QUERIES FOR PROCEDURE EVENT PARAMETERS ****************************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (84, 54, 54);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (85, 54, 63);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (86, 54, 23);

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (84, 138, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (85, 218, 'Specimen Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (85, 219, 'Event Timestamp');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (85, 221, 'Comments');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (86, 292, 'User Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (86, 293, 'User First Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (84, 139, 'URL');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (84, 140, 'Name');

/* ************************* RELATIONSHIP QUERIES FOR RECEIVED EVENT PARAMETERS ****************************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (87, 57, 57);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (88, 57, 63);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (89, 57, 23);

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (87, 150, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (88, 218, 'Specimen Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (88, 219, 'Event Timestamp');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (88, 221, 'Comments');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (89, 292, 'User Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (89, 293, 'User First Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (87, 151, 'Received Quality');

/* ************************* RELATIONSHIP QUERIES FOR SPUN EVENT PARAMETERS ********************************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (90, 64, 64);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (91, 64, 63);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (92, 64, 23);

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (90, 236, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (91, 218, 'Specimen Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (91, 219, 'Event Timestamp');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (91, 221, 'Comments');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (92, 292, 'User Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (92, 293, 'User First Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (91, 237, 'GForce');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (91, 238, 'Duration In Minutes');

/* ************************* RELATIONSHIP QUERIES FOR THAW EVENT PARAMETERS ********************************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (93, 66, 66);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (94, 66, 63);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (95, 66, 23);

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (93, 266, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (94, 218, 'Specimen Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (94, 219, 'Event Timestamp');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (94, 221, 'Comments');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (95, 292, 'User Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (95, 293, 'User First Name');

/* ************************* RELATIONSHIP QUERIES FOR TISSUE SPECIMEN REVIEW EVENT PARAMETERS **************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (96, 67, 67);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (97, 67, 63);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (98, 67, 23);

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (96, 272, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (97, 218, 'Specimen Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (97, 219, 'Event Timestamp');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (97, 221, 'Comments');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (98, 292, 'User Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (98, 293, 'User First Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (96, 273, 'Neo Plastic Cellularity Percentage');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (96, 274, 'Necrosis Percentage');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (96, 275, 'Lymphocytic Percentage');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (96, 276, 'Total Cellularity Percentage');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (96, 277, 'Histological Quality');

/* ************************* RELATIONSHIP QUERIES FOR TRANSFER EVENT PARAMETERS ******************************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (99, 68, 68);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (100, 68, 63);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES (101, 68, 23);

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (99, 278, 'Identifier');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (100, 218, 'Specimen Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (100, 219, 'Event Timestamp');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (100, 221, 'Comments');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (101, 292, 'User Last Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (101, 293, 'User First Name');

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (99, 284, 'From Storage Container Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (99, 283, 'To Storage Container Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (99, 279, 'From Position Dimension One');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (99, 280, 'From Position Dimension Two');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (99, 281, 'To Position Dimension One');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (99, 282, 'To Position Dimension Two');

/* ************************* RELATIONSHIP QUERIES FOR REPORTED PROBLEMS ******************************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (102,59,59);

INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (102, 156, 'Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (102, 157, 'Affiliation');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (102, 158, 'Name of reporter');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (102, 159, 'Reporters email id');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (102, 160, 'Message Body');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (102, 161, 'Subject');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (102, 162, 'Reported Date');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (102, 163, 'Activity Status');

/* ******************************* MAPPING QUERIES FOR EVENT PARAMETERS *************************************************/
INSERT INTO CATISSUE_RELATED_TABLES_MAP(FIRST_TABLE_ID,SECOND_TABLE_ID,FIRST_TABLE_JOIN_COLUMN,SECOND_TABLE_JOIN_COLUMN) VALUES(63,43,'IDENTIFIER','IDENTIFIER');
INSERT INTO CATISSUE_RELATED_TABLES_MAP(FIRST_TABLE_ID,SECOND_TABLE_ID,FIRST_TABLE_JOIN_COLUMN,SECOND_TABLE_JOIN_COLUMN) VALUES(63,44,'IDENTIFIER','IDENTIFIER');
INSERT INTO CATISSUE_RELATED_TABLES_MAP(FIRST_TABLE_ID,SECOND_TABLE_ID,FIRST_TABLE_JOIN_COLUMN,SECOND_TABLE_JOIN_COLUMN) VALUES(63,46,'IDENTIFIER','IDENTIFIER');
INSERT INTO CATISSUE_RELATED_TABLES_MAP(FIRST_TABLE_ID,SECOND_TABLE_ID,FIRST_TABLE_JOIN_COLUMN,SECOND_TABLE_JOIN_COLUMN) VALUES(63,47,'IDENTIFIER','IDENTIFIER');
INSERT INTO CATISSUE_RELATED_TABLES_MAP(FIRST_TABLE_ID,SECOND_TABLE_ID,FIRST_TABLE_JOIN_COLUMN,SECOND_TABLE_JOIN_COLUMN) VALUES(63,48,'IDENTIFIER','IDENTIFIER');
INSERT INTO CATISSUE_RELATED_TABLES_MAP(FIRST_TABLE_ID,SECOND_TABLE_ID,FIRST_TABLE_JOIN_COLUMN,SECOND_TABLE_JOIN_COLUMN) VALUES(63,49,'IDENTIFIER','IDENTIFIER');
INSERT INTO CATISSUE_RELATED_TABLES_MAP(FIRST_TABLE_ID,SECOND_TABLE_ID,FIRST_TABLE_JOIN_COLUMN,SECOND_TABLE_JOIN_COLUMN) VALUES(63,50,'IDENTIFIER','IDENTIFIER');
INSERT INTO CATISSUE_RELATED_TABLES_MAP(FIRST_TABLE_ID,SECOND_TABLE_ID,FIRST_TABLE_JOIN_COLUMN,SECOND_TABLE_JOIN_COLUMN) VALUES(63,51,'IDENTIFIER','IDENTIFIER');
INSERT INTO CATISSUE_RELATED_TABLES_MAP(FIRST_TABLE_ID,SECOND_TABLE_ID,FIRST_TABLE_JOIN_COLUMN,SECOND_TABLE_JOIN_COLUMN) VALUES(63,52,'IDENTIFIER','IDENTIFIER');
INSERT INTO CATISSUE_RELATED_TABLES_MAP(FIRST_TABLE_ID,SECOND_TABLE_ID,FIRST_TABLE_JOIN_COLUMN,SECOND_TABLE_JOIN_COLUMN) VALUES(63,54,'IDENTIFIER','IDENTIFIER');
INSERT INTO CATISSUE_RELATED_TABLES_MAP(FIRST_TABLE_ID,SECOND_TABLE_ID,FIRST_TABLE_JOIN_COLUMN,SECOND_TABLE_JOIN_COLUMN) VALUES(63,57,'IDENTIFIER','IDENTIFIER');
INSERT INTO CATISSUE_RELATED_TABLES_MAP(FIRST_TABLE_ID,SECOND_TABLE_ID,FIRST_TABLE_JOIN_COLUMN,SECOND_TABLE_JOIN_COLUMN) VALUES(63,64,'IDENTIFIER','IDENTIFIER');
INSERT INTO CATISSUE_RELATED_TABLES_MAP(FIRST_TABLE_ID,SECOND_TABLE_ID,FIRST_TABLE_JOIN_COLUMN,SECOND_TABLE_JOIN_COLUMN) VALUES(63,66,'IDENTIFIER','IDENTIFIER');
INSERT INTO CATISSUE_RELATED_TABLES_MAP(FIRST_TABLE_ID,SECOND_TABLE_ID,FIRST_TABLE_JOIN_COLUMN,SECOND_TABLE_JOIN_COLUMN) VALUES(63,67,'IDENTIFIER','IDENTIFIER');
INSERT INTO CATISSUE_RELATED_TABLES_MAP(FIRST_TABLE_ID,SECOND_TABLE_ID,FIRST_TABLE_JOIN_COLUMN,SECOND_TABLE_JOIN_COLUMN) VALUES(63,68,'IDENTIFIER','IDENTIFIER');

/* ************************* RELATIONSHIP QUERIES FOR ADVANCE QUERY SEARCH *******************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (103,31,10,'27');
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (104,10,35,'27');

/* ************************* RELATIONSHIP QUERIES FOR SPECIMEN BIOHAZARD RELATIONSHIP ******************************************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (105,62,8);
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID, TABLES_IN_PATH) values (106,33,8,'62');

/* Biohazard */
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (105, 24, 'Identifier');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (105, 25, 'Name');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (105, 26, 'Comments');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (105, 27, 'Type');

/*************** RELATIONSHIP QUERIES FOR SpecimenEventParameters and User RELATIONSHIP *******************/
INSERT INTO CATISSUE_TABLE_RELATION (RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values (107,23,63);
INSERT INTO CATISSUE_RELATED_TABLES_MAP(FIRST_TABLE_ID,SECOND_TABLE_ID,FIRST_TABLE_JOIN_COLUMN,SECOND_TABLE_JOIN_COLUMN) VALUES(23,63,'IDENTIFIER','USER_ID');

commit;
#-------- finish


#------Start:- Jitendra: Bug-1678 -------

alter table catissue_reported_problem modify MESSAGE_BODY varchar(500) NOT NULL;
alter table CATISSUE_AUDIT_EVENT_DETAILS modify CURRENT_VALUE varchar(500) NOT NULL;


#------End: Jitendra: Bug-1678 -------

alter table catissue_container_type add column activity_status varchar(30);
update catissue_container_type a set activity_status = (select activity_status from catissue_storage_type b where a.identifier = b.identifier);
alter table catissue_storage_type drop column activity_status;

create table CATISSUE_STORTY_HOLDS_SPARRTY (
   STORAGE_TYPE_ID BIGINT not null,
   SPECIMEN_ARRAY_TYPE_ID BIGINT not null,
   primary key (STORAGE_TYPE_ID, SPECIMEN_ARRAY_TYPE_ID)
);
alter table CATISSUE_STORTY_HOLDS_SPARRTY add index (STORAGE_TYPE_ID), add constraint FK70F57E4459A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);
alter table CATISSUE_STORTY_HOLDS_SPARRTY add index (SPECIMEN_ARRAY_TYPE_ID), add constraint FK70F57E44ECE89343 foreign key (SPECIMEN_ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE (IDENTIFIER);

create table CATISSUE_CONT_HOLDS_SPARRTYPE (
   STORAGE_CONTAINER_ID BIGINT not null,
   SPECIMEN_ARRAY_TYPE_ID BIGINT not null,
   primary key (STORAGE_CONTAINER_ID, SPECIMEN_ARRAY_TYPE_ID)
);
alter table CATISSUE_CONT_HOLDS_SPARRTYPE add index (SPECIMEN_ARRAY_TYPE_ID), add constraint FKDC7E31E2ECE89343 foreign key (SPECIMEN_ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE (IDENTIFIER);
alter table CATISSUE_CONT_HOLDS_SPARRTYPE add index (STORAGE_CONTAINER_ID), add constraint FKDC7E31E2B3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);
