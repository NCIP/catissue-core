#---- Ordering system  related alter table script Vaishali

alter table CATISSUE_EXISTING_SP_ORD_ITEM drop foreign key FKF8B855EEBC7298A9;
alter table CATISSUE_EXISTING_SP_ORD_ITEM drop foreign key FKF8B855EE60773DB2;
alter table CATISSUE_PATH_CASE_ORDER_ITEM drop foreign key FKBD5029D5F69249F7;
alter table CATISSUE_PATH_CASE_ORDER_ITEM drop foreign key FKBD5029D5BC7298A9;
alter table CATISSUE_ORDER_ITEM drop foreign key FKB501E88060975C0B;
alter table CATISSUE_ORDER_ITEM drop foreign key FKB501E880783867CC;
alter table CATISSUE_DERIEVED_SP_ORD_ITEM drop foreign key FK3742152BBC7298A9;
alter table CATISSUE_DERIEVED_SP_ORD_ITEM drop foreign key FK3742152B60773DB2;
alter table CATISSUE_ORDER drop foreign key FK543F22B26B1F36E7;
alter table CATISSUE_DISTRIBUTION drop foreign key FK54276680783867CC;
alter table CATISSUE_DISTRIBUTED_ITEM drop foreign key FKA7C3ED4BC4A3C438;
alter table CATISSUE_SP_ARRAY_ORDER_ITEM drop foreign key FKE3823170BC7298A9;
alter table CATISSUE_SP_ARRAY_ORDER_ITEM drop foreign key FKE3823170C4A3C438;
alter table CATISSUE_SPECIMEN_ORDER_ITEM drop foreign key FK48C3B39FBC7298A9;
alter table CATISSUE_SPECIMEN_ORDER_ITEM drop foreign key FK48C3B39F83505A30;
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM drop foreign key FKC5C92CCBCE5FBC3A;
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM drop foreign key FKC5C92CCBBC7298A9;
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM drop foreign key FKC5C92CCBC4A3C438;



#------- ordering system relate tables----


 
drop table if exists CATISSUE_EXISTING_SP_ORD_ITEM;
drop table if exists CATISSUE_PATH_CASE_ORDER_ITEM;
drop table if exists CATISSUE_ORDER_ITEM;
drop table if exists CATISSUE_DERIEVED_SP_ORD_ITEM;
drop table if exists CATISSUE_ORDER;
drop table if exists CATISSUE_SP_ARRAY_ORDER_ITEM;
drop table if exists CATISSUE_SPECIMEN_ORDER_ITEM;
drop table if exists CATISSUE_NEW_SP_AR_ORDER_ITEM;

create table CATISSUE_EXISTING_SP_ORD_ITEM (
   IDENTIFIER bigint not null,
   SPECIMEN_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_PATH_CASE_ORDER_ITEM (
   IDENTIFIER bigint not null,
   PATHOLOGICAL_STATUS varchar(255),
   TISSUE_SITE varchar(255),
   SPECIMEN_CLASS varchar(255),
   SPECIMEN_TYPE varchar(255),
   SPECIMEN_COLL_GROUP_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_ORDER_ITEM (
   IDENTIFIER bigint not null auto_increment,
   DESCRIPTION text,
   DISTRIBUTED_ITEM_ID bigint,
   STATUS varchar(50),
   REQUESTED_QUANTITY double precision,
   ORDER_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_DERIEVED_SP_ORD_ITEM (
   IDENTIFIER bigint not null,
   SPECIMEN_CLASS varchar(255),
   SPECIMEN_TYPE varchar(255),
   SPECIMEN_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_ORDER (
   IDENTIFIER bigint not null auto_increment,
   COMMENTS text,
   DISTRIBUTION_PROTOCOL_ID bigint,
   NAME text,
   REQUESTED_DATE datetime,
   STATUS varchar(50),
   primary key (IDENTIFIER)
);

create table CATISSUE_SPECIMEN_ORDER_ITEM (
   IDENTIFIER bigint not null,
   ARRAY_ORDER_ITEM_ID bigint,
   primary key (IDENTIFIER)
);

#-----------------Array order item tables.
create table CATISSUE_SP_ARRAY_ORDER_ITEM (
   IDENTIFIER bigint not null,
   SPECIMEN_ARRAY_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_NEW_SP_AR_ORDER_ITEM (
   IDENTIFIER bigint not null,
   ARRAY_TYPE_ID bigint,
   NAME varchar(255),
   SPECIMEN_ARRAY_ID bigint,
   primary key (IDENTIFIER)
);


/* extra for catissue_distribution */
 alter table catissue_distribution add column `ORDER_ID` bigint   NULL;
 alter table catissue_distributed_item add column `SPECIMEN_ARRAY_ID` bigint   NULL;  
/* extra finished */
 
alter table CATISSUE_EXISTING_SP_ORD_ITEM add index FKF8B855EEBC7298A9 (IDENTIFIER), add constraint FKF8B855EEBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_EXISTING_SP_ORD_ITEM add index FKF8B855EE60773DB2 (SPECIMEN_ID), add constraint FKF8B855EE60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_PATH_CASE_ORDER_ITEM add index FKBD5029D5F69249F7 (SPECIMEN_COLL_GROUP_ID), add constraint FKBD5029D5F69249F7 foreign key (SPECIMEN_COLL_GROUP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_PATH_CASE_ORDER_ITEM add index FKBD5029D5BC7298A9 (IDENTIFIER), add constraint FKBD5029D5BC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_ORDER_ITEM add index FKB501E88060975C0B (DISTRIBUTED_ITEM_ID), add constraint FKB501E88060975C0B foreign key (DISTRIBUTED_ITEM_ID) references CATISSUE_DISTRIBUTED_ITEM (IDENTIFIER);
alter table CATISSUE_ORDER_ITEM add index FKB501E880783867CC (ORDER_ID), add constraint FKB501E880783867CC foreign key (ORDER_ID) references CATISSUE_ORDER (IDENTIFIER);
alter table CATISSUE_DISTRIBUTION add index FK54276680783867CC (ORDER_ID), add constraint FK54276680783867CC foreign key (ORDER_ID) references CATISSUE_ORDER (IDENTIFIER);
alter table CATISSUE_DISTRIBUTED_ITEM add index FKA7C3ED4BC4A3C438 (SPECIMEN_ARRAY_ID), add constraint FKA7C3ED4BC4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER);
alter table CATISSUE_DERIEVED_SP_ORD_ITEM add index FK3742152BBC7298A9 (IDENTIFIER), add constraint FK3742152BBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_DERIEVED_SP_ORD_ITEM add index FK3742152B60773DB2 (SPECIMEN_ID), add constraint FK3742152B60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_ORDER add index FK543F22B26B1F36E7 (DISTRIBUTION_PROTOCOL_ID), add constraint FK543F22B26B1F36E7 foreign key (DISTRIBUTION_PROTOCOL_ID) references CATISSUE_DISTRIBUTION_PROTOCOL (IDENTIFIER);
alter table CATISSUE_SP_ARRAY_ORDER_ITEM add index FKE3823170BC7298A9 (IDENTIFIER), add constraint FKE3823170BC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_SP_ARRAY_ORDER_ITEM add index FKE3823170C4A3C438 (SPECIMEN_ARRAY_ID), add constraint FKE3823170C4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ORDER_ITEM add index FK48C3B39FBC7298A9 (IDENTIFIER), add constraint FK48C3B39FBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ORDER_ITEM add index FK48C3B39F83505A30 (ARRAY_ORDER_ITEM_ID), add constraint FK48C3B39F83505A30 foreign key (ARRAY_ORDER_ITEM_ID) references CATISSUE_NEW_SP_AR_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add index FKC5C92CCBCE5FBC3A (ARRAY_TYPE_ID), add constraint FKC5C92CCBCE5FBC3A foreign key (ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE (IDENTIFIER);
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add index FKC5C92CCBBC7298A9 (IDENTIFIER), add constraint FKC5C92CCBBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add index FKC5C92CCBC4A3C438 (SPECIMEN_ARRAY_ID), add constraint FKC5C92CCBC4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER)


/* csm_catissuecore.sql */
/*-----Ordering System------*/
INSERT into `CSM_PROTECTION_ELEMENT` select max(PROTECTION_ELEMENT_ID)+1,'Order','Order Object','edu.wustl.catissuecore.domain.OrderDetails',NULL,NULL,1,'2006-11-27' from CSM_PROTECTION_ELEMENT;
INSERT into `CSM_PROTECTION_ELEMENT` select max(PROTECTION_ELEMENT_ID)+1,'OrderItem','OrderItem Object','edu.wustl.catissuecore.domain.OrderItem',NULL,NULL,1,'2006-11-27' from CSM_PROTECTION_ELEMENT;
INSERT into `CSM_PROTECTION_ELEMENT` select max(PROTECTION_ELEMENT_ID)+1,'Derived Specimen Order Item','Derived Specimen Order Item Object','edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem',NULL,NULL,1,'2006-11-27' from CSM_PROTECTION_ELEMENT;
INSERT into `CSM_PROTECTION_ELEMENT` select max(PROTECTION_ELEMENT_ID)+1,'Existing Specimen Array Order Item','Existing Specimen Array Order Item Object','edu.wustl.catissuecore.domain.ExistingSpecimenArrayOrderItem',NULL,NULL,1,'2006-11-27' from CSM_PROTECTION_ELEMENT;
INSERT into `CSM_PROTECTION_ELEMENT` select max(PROTECTION_ELEMENT_ID)+1,'Existing Specimen Order Item','Existing Specimen Order Item Object','edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem',NULL,NULL,1,'2006-11-27' from CSM_PROTECTION_ELEMENT;
INSERT into `CSM_PROTECTION_ELEMENT` select max(PROTECTION_ELEMENT_ID)+1,'New Specimen Array Order Item','New Specimen Array Order Item Object','edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem',NULL,NULL,1,'2006-11-27' from CSM_PROTECTION_ELEMENT;
INSERT into `CSM_PROTECTION_ELEMENT` select max(PROTECTION_ELEMENT_ID)+1,'New Specimen Order Item','New Specimen Order Item Object','edu.wustl.catissuecore.domain.NewSpecimenOrderItem',NULL,NULL,1,'2006-11-27' from CSM_PROTECTION_ELEMENT;
INSERT into `CSM_PROTECTION_ELEMENT` select max(PROTECTION_ELEMENT_ID)+1,'Pathological Case Order Item','Pathological Case Order Item Object','edu.wustl.catissuecore.domain.PathologicalCaseOrderItem',NULL,NULL,1,'2006-11-27' from CSM_PROTECTION_ELEMENT;
INSERT into `CSM_PROTECTION_ELEMENT` select max(PROTECTION_ELEMENT_ID)+1,'Specimen Array Order Item','Specimen Array Order Item Object','edu.wustl.catissuecore.domain.SpecimenArrayOrderItem',NULL,NULL,1,'2006-11-27' from CSM_PROTECTION_ELEMENT;
INSERT into `CSM_PROTECTION_ELEMENT` select max(PROTECTION_ELEMENT_ID)+1,'Specimen Order Item','Specimen Order Item Object','edu.wustl.catissuecore.domain.SpecimenOrderItem',NULL,NULL,1,'2006-11-27' from CSM_PROTECTION_ELEMENT;



#---------------------Ordering System Permissions----Ashish 4/1/06
insert into csm_role(ROLE_ID,ROLE_NAME,ROLE_DESCRIPTION,APPLICATION_ID) values (12,'CREATE_ONLY','Create only role',1);
insert into csm_role_privilege(ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID) values (30,12,1);

insert into csm_protection_group(PROTECTION_GROUP_ID,PROTECTION_GROUP_NAME,APPLICATION_ID) values (44,'SCIENTIST_PROTECTION_GROUP',1);

INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,44,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Order'),'2007-01-04' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,44,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='OrderItem'),'2007-01-04' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,44,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Derived Specimen Order Item'),'2007-01-04' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,44,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Existing Specimen Array Order Item'),'2007-01-04' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,44,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Existing Specimen Order Item'),'2007-01-04' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,44,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='New Specimen Array Order Item'),'2007-01-04' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,44,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='New Specimen Order Item'),'2007-01-04' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,44,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Pathological Case Order Item'),'2007-01-04' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,44,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Specimen Array Order Item'),'2007-01-04' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,44,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Specimen Order Item'),'2007-01-04' from CSM_PG_PE;

insert into csm_user_group_role_pg(USER_GROUP_ROLE_PG_ID,GROUP_ID,ROLE_ID,PROTECTION_GROUP_ID) values (102,4,11,44);

INSERT into `CSM_PROTECTION_ELEMENT` select max(PROTECTION_ELEMENT_ID)+1,'edu.wustl.catissuecore.action.RequestListAction','edu.wustl.catissuecore.action.RequestListAction','edu.wustl.catissuecore.action.RequestListAction',NULL,NULL,1,'2007-01-04' from CSM_PROTECTION_ELEMENT;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='edu.wustl.catissuecore.action.RequestListAction'),'2007-01-04' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='edu.wustl.catissuecore.action.RequestListAction'),'2007-01-04' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='edu.wustl.catissuecore.action.RequestListAction'),'2007-01-04' from CSM_PG_PE;

INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Order'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Order'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Order'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='OrderItem'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='OrderItem'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='OrderItem'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Derived Specimen Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Derived Specimen Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Derived Specimen Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Existing Specimen Array Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Existing Specimen Array Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Existing Specimen Array Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Existing Specimen Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Existing Specimen Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Existing Specimen Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='New Specimen Array Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='New Specimen Array Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='New Specimen Array Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='New Specimen Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='New Specimen Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='New Specimen Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Pathological Case Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Pathological Case Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Pathological Case Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Specimen Array Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Specimen Array Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Specimen Array Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Specimen Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Specimen Order Item'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Specimen Order Item'),'2006-11-27' from CSM_PG_PE;


/* cde_dummydata_common.sql */


/*******Ordering System******/
INSERT INTO CATISSUE_CDE VALUES ( '4284','Request Status','Statuses for the ordered requests',1.0,null);
INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES(5000,'All',NULL,'4284');
INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES(5001,'New',NULL,'4284');
INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES(5002,'Pending',NULL,'4284');

INSERT INTO CATISSUE_CDE VALUES ( '4285','Requested Items Status','Statuses for the individual elements in the ordered request',1.0,null);
INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES(5005,'New',NULL,'4285');
INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES(5006,'Pending - Protocol Review',NULL,'4285');
INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES(5007,'Pending - For Distribution',NULL,'4285');
INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES(5012,'Distributed',NULL,'4285');
INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES(5008,'Pending - Specimen Preparation',NULL,'4285');
INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES(5009,'Rejected - Inappropriate Request',NULL,'4285');
INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES(5010,'Rejected - Specimen Unavailable',NULL,'4285');
INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES(5011,'Rejected - Unable to Create',NULL,'4285');


/* initDB_inser_Common.sql */
/*Ordering System*/
insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 76, 'CATISSUE_ORDER', 'Order', 'OrderDetails', 2);



/* extra for catissue_distribution */
 alter table catissue_distribution add column `ORDER_ID` bigint   NULL;
 alter table catissue_distributed_item add column `SPECIMEN_ARRAY_ID` bigint   NULL;  
/* extra finished */
#-----------------------ordering changes finish