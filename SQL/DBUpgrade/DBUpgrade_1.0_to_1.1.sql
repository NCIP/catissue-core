# ------ 'Death date' and 'Vital status' attribute addition to Participant table ------
# ---------- 23 May 2006 -------------
ALTER TABLE catissue_participant ADD COLUMN DEATH_DATE DATE;
ALTER TABLE catissue_participant ADD COLUMN VITAL_STATUS varchar(50);

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



#--------- changes in database for new model of Storage Container
drop table if exists CATISSUE_CONTAINER_SPECIMENCL_REL;
drop table if exists CATISSUE_STORAGETYPE_HOLDS_REL;
drop table if exists CATISSUE_CONTAINER_TYPE_REL;
drop table if exists CATISSUE_CONTAINER_CP_REL;
drop table if exists CATISSUE_CONT_SPECIMENCL_REL;
drop table if exists CATISSUE_TYPE_SPECIMENCL_REL;
drop table if exists CATISSUE_SPECIMEN_CLASS;

#--------dropping Container_name in Storage_container table if exists
alter table CATISSUE_STORAGE_CONTAINER drop column CONTAINER_NAME;

#--------Adding container_name in storage_container table
alter table CATISSUE_STORAGE_CONTAINER add column CONTAINER_NAME varchar (50) unique;

#--------Give values same as identifier to container_name for previouly added containers.
update CATISSUE_STORAGE_CONTAINER set CONTAINER_NAME=IDENTIFIER where CONTAINER_NAME='';

#--------drop the container_number column
alter table CATISSUE_STORAGE_CONTAINER drop column CONTAINER_NUMBER;

#--------Adding Activity Status column in Storage_type table
alter table CATISSUE_STORAGE_TYPE add column ACTIVITY_STATUS varchar(30) default NULL;
#-------- set default Activity status to 'Active ' for all storage types
update CATISSUE_STORAGE_TYPE set ACTIVITY_STATUS='Active';

#-------- updating container Number to container Name
update CATISSUE_INTERFACE_COLUMN_DATA set column_name='CONTAINER_NAME' where IDENTIFIER='240' and TABLE_ID='21';
update CATISSUE_SEARCH_DISPLAY_DATA set display_name='Container Name' where relationship_id='8' and col_id='240';

#-------Creating Catissue_specimen_class table for storing all classes of specimens.
create table CATISSUE_SPECIMEN_CLASS (
   IDENTIFIER BIGINT NOT NULL AUTO_INCREMENT,
   NAME VARCHAR(50),
   ACTIVITY_STATUS VARCHAR(30) default NULL,
   primary key (IDENTIFIER)
);
#-------inserting values in catissye_specimen_class table
insert into CATISSUE_SPECIMEN_CLASS values (1,"Any","Disabled");
insert into CATISSUE_SPECIMEN_CLASS values (2,"FluidSpecimen","Active");
insert into CATISSUE_SPECIMEN_CLASS values (3,"TissueSpecimen","Active");
insert into CATISSUE_SPECIMEN_CLASS values (4,"CellSpecimen","Active");
insert into CATISSUE_SPECIMEN_CLASS values (5,"MolecularSpecimen","Active");


create table CATISSUE_TYPE_SPECIMENCL_REL (
   STORAGE_TYPE_ID BIGINT not null,
   SPECIMEN_CLASS_ID BIGINT not null,
   primary key (STORAGE_TYPE_ID, SPECIMEN_CLASS_ID)
);
alter table CATISSUE_TYPE_SPECIMENCL_REL add index (SPECIMEN_CLASS_ID), add constraint FK79BC0CD43AF944B9 foreign key (SPECIMEN_CLASS_ID) references CATISSUE_SPECIMEN_CLASS (IDENTIFIER);
alter table CATISSUE_TYPE_SPECIMENCL_REL add index (STORAGE_TYPE_ID), add constraint FK79BC0CD459A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);

create table CATISSUE_STORAGETYPE_HOLDS_REL (
   STORAGE_TYPE_ID BIGINT not null,
   STORAGE_TYPE_HOLD_ID BIGINT not null,
   primary key (STORAGE_TYPE_ID, STORAGE_TYPE_HOLD_ID)
);
alter table CATISSUE_STORAGETYPE_HOLDS_REL add index (STORAGE_TYPE_ID), add constraint FK3239D0859A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);
alter table CATISSUE_STORAGETYPE_HOLDS_REL add index (STORAGE_TYPE_HOLD_ID), add constraint FK3239D0839B92FFA foreign key (STORAGE_TYPE_HOLD_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);

create table CATISSUE_CONTAINER_TYPE_REL (
   STORAGE_CONTAINER_ID BIGINT not null,
   STORAGE_TYPE_ID BIGINT not null,
   primary key (STORAGE_TYPE_ID, STORAGE_CONTAINER_ID)
);
alter table CATISSUE_CONTAINER_TYPE_REL add index (STORAGE_CONTAINER_ID), add constraint FKFE943B0EB3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);
alter table CATISSUE_CONTAINER_TYPE_REL add index (STORAGE_TYPE_ID), add constraint FKFE943B0E59A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);

create table CATISSUE_CONTAINER_CP_REL (
   COLLECTION_PROTOCOL_ID BIGINT not null,
   STORAGE_CONTAINER_ID BIGINT not null,
   primary key (STORAGE_CONTAINER_ID, COLLECTION_PROTOCOL_ID)
);
alter table CATISSUE_CONTAINER_CP_REL add index (STORAGE_CONTAINER_ID), add constraint FKB1816941B3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);
alter table CATISSUE_CONTAINER_CP_REL add index (COLLECTION_PROTOCOL_ID), add constraint FKB181694148304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);

create table CATISSUE_CONT_SPECIMENCL_REL (
   SPECIMEN_CLASS_ID BIGINT not null,
   STORAGE_CONTAINER_ID BIGINT not null,
   primary key (STORAGE_CONTAINER_ID, SPECIMEN_CLASS_ID)
);
alter table CATISSUE_CONT_SPECIMENCL_REL add index (STORAGE_CONTAINER_ID), add constraint FKB9F0FDDCB3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);
alter table CATISSUE_CONT_SPECIMENCL_REL add index (SPECIMEN_CLASS_ID), add constraint FKB9F0FDDC3AF944B9 foreign key (SPECIMEN_CLASS_ID) references CATISSUE_SPECIMEN_CLASS (IDENTIFIER);

delete from CATISSUE_QUERY_TABLE_DATA where TABLE_ID='22';
delete from CATISSUE_RELATED_TABLES_MAP where FIRST_TABLE_ID='22';
delete from CATISSUE_INTERFACE_COLUMN_DATA where TABLE_ID='22';
delete from CATISSUE_TABLE_RELATION where CHILD_TABLE_ID='22';
delete from CATISSUE_SEARCH_DISPLAY_DATA where RELATIONSHIP_ID='39';
drop table CATISSUE_STORAGE_CONT_DETAILS;
delete from csm_protection_element where PROTECTION_ELEMENT_ID='43';


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

#--------Adding Group_name in CATISSUE_SPECIMEN_COLL_GROUP table
alter table CATISSUE_SPECIMEN_COLL_GROUP add column NAME varchar(50) NOT NULL;


#--------Give values to group_name for previouly added specimen collection groups.
update CATISSUE_SPECIMEN_COLL_GROUP set NAME=IDENTIFIER where NAME='';
alter table CATISSUE_SPECIMEN_COLL_GROUP add constraint NAME unique (NAME);
INSERT INTO CATISSUE_INTERFACE_COLUMN_DATA (IDENTIFIER,TABLE_ID,COLUMN_NAME,ATTRIBUTE_TYPE) VALUES (305 ,35 , 'NAME','varchar');
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES (30, 305 , 'Specimen Collection Group Name');