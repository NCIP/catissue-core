
/************Vijay Pande: column added since Clinical Report is now removed from System ***********/ 
ALTER TABLE CATISSUE_SPECIMEN_COLL_GROUP ADD COLUMN SURGICAL_PATHOLOGY_NUMBER varchar(50); 

/*----Ashish: Ordering System-----*/
insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 76, 'CATISSUE_ORDER', 'Order', 'OrderDetails', 2);

	/*******CDE Ordering System******/
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

/*--------Consent Tracking-------*/
INSERT into `CSM_PROTECTION_ELEMENT` select max(PROTECTION_ELEMENT_ID)+1,'Consent Tier','ConsentTier Object','edu.wustl.catissuecore.domain.ConsentTier',NULL,NULL,1,'2006-11-27' from CSM_PROTECTION_ELEMENT;
INSERT into `CSM_PROTECTION_ELEMENT` select max(PROTECTION_ELEMENT_ID)+1,'Consent Tier Response','Consent Tier Response Object','edu.wustl.catissuecore.domain.ConsentTierResponse',NULL,NULL,1,'2006-11-27' from CSM_PROTECTION_ELEMENT;
INSERT into `CSM_PROTECTION_ELEMENT` select max(PROTECTION_ELEMENT_ID)+1,'Consent Tier Status','Consent Tier Status Object','edu.wustl.catissuecore.domain.ConsentTierStatus',NULL,NULL,1,'2006-11-27' from CSM_PROTECTION_ELEMENT;


	/*-------Ordering System---------*/
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

/*-------Consent Tracking------ */
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier Response'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier Response'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier Response'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier Status'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier Status'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier Status'),'2006-11-27' from CSM_PG_PE;

	
	#---- Ordering system  related alter table script
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
alter table CATISSUE_SP_ARRAY_ORDER_ITEM drop foreign key FKE3823170BC7298A9;
alter table CATISSUE_SP_ARRAY_ORDER_ITEM drop foreign key FKE3823170C4A3C438;
alter table CATISSUE_SPECIMEN_ORDER_ITEM drop foreign key FK48C3B39FBC7298A9;
alter table CATISSUE_SPECIMEN_ORDER_ITEM drop foreign key FK48C3B39F83505A30;
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM drop foreign key FKC5C92CCBCE5FBC3A;
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM drop foreign key FKC5C92CCBBC7298A9;


	#-------- Consent Tracking alter scripts
alter table CATISSUE_CONSENT_TIER_RESPONSE drop foreign key FKFB1995FD4AD77FCB;
alter table CATISSUE_CONSENT_TIER_RESPONSE drop foreign key FKFB1995FD17B9953;
alter table CATISSUE_CONSENT_TIER_STATUS drop foreign key FKF74E94AEF69249F7;
alter table CATISSUE_CONSENT_TIER_STATUS drop foreign key FKF74E94AE60773DB2;
alter table CATISSUE_CONSENT_TIER_STATUS drop foreign key FKF74E94AE17B9953;
alter table CATISSUE_CONSENT_TIER drop foreign key FK51725303E36A4B4F;

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
   DESCRIPTION varchar(200),
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
   primary key (IDENTIFIER)
);
 
alter table CATISSUE_EXISTING_SP_ORD_ITEM add index FKF8B855EEBC7298A9 (IDENTIFIER), add constraint FKF8B855EEBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_EXISTING_SP_ORD_ITEM add index FKF8B855EE60773DB2 (SPECIMEN_ID), add constraint FKF8B855EE60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_PATH_CASE_ORDER_ITEM add index FKBD5029D5F69249F7 (SPECIMEN_COLL_GROUP_ID), add constraint FKBD5029D5F69249F7 foreign key (SPECIMEN_COLL_GROUP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_PATH_CASE_ORDER_ITEM add index FKBD5029D5BC7298A9 (IDENTIFIER), add constraint FKBD5029D5BC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_ORDER_ITEM add index FKB501E88060975C0B (DISTRIBUTED_ITEM_ID), add constraint FKB501E88060975C0B foreign key (DISTRIBUTED_ITEM_ID) references CATISSUE_DISTRIBUTED_ITEM (IDENTIFIER);
alter table CATISSUE_ORDER_ITEM add index FKB501E880783867CC (ORDER_ID), add constraint FKB501E880783867CC foreign key (ORDER_ID) references CATISSUE_ORDER (IDENTIFIER);
alter table CATISSUE_DISTRIBUTION add index FK54276680783867CC (ORDER_ID), add constraint FK54276680783867CC foreign key (ORDER_ID) references CATISSUE_ORDER (IDENTIFIER);
alter table CATISSUE_DERIEVED_SP_ORD_ITEM add index FK3742152BBC7298A9 (IDENTIFIER), add constraint FK3742152BBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_DERIEVED_SP_ORD_ITEM add index FK3742152B60773DB2 (SPECIMEN_ID), add constraint FK3742152B60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_ORDER add index FK543F22B26B1F36E7 (DISTRIBUTION_PROTOCOL_ID), add constraint FK543F22B26B1F36E7 foreign key (DISTRIBUTION_PROTOCOL_ID) references CATISSUE_DISTRIBUTION_PROTOCOL (IDENTIFIER);
alter table CATISSUE_SP_ARRAY_ORDER_ITEM add index FKE3823170BC7298A9 (IDENTIFIER), add constraint FKE3823170BC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_SP_ARRAY_ORDER_ITEM add index FKE3823170C4A3C438 (SPECIMEN_ARRAY_ID), add constraint FKE3823170C4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ORDER_ITEM add index FK48C3B39FBC7298A9 (IDENTIFIER), add constraint FK48C3B39FBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ORDER_ITEM add index FK48C3B39F83505A30 (ARRAY_ORDER_ITEM_ID), add constraint FK48C3B39F83505A30 foreign key (ARRAY_ORDER_ITEM_ID) references CATISSUE_NEW_SP_AR_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add index FKC5C92CCBCE5FBC3A (ARRAY_TYPE_ID), add constraint FKC5C92CCBCE5FBC3A foreign key (ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE (IDENTIFIER);
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add index FKC5C92CCBBC7298A9 (IDENTIFIER), add constraint FKC5C92CCBBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);

	#------ Consent Tracking related drop, create and add foreign key scripts.
drop table if exists CATISSUE_CONSENT_TIER_RESPONSE;
drop table if exists CATISSUE_CONSENT_TIER_STATUS;
drop table if exists CATISSUE_CONSENT_TIER;

create table CATISSUE_CONSENT_TIER_RESPONSE (
   IDENTIFIER bigint not null auto_increment,
   RESPONSE varchar(20),
   CONSENT_TIER_ID bigint,
   COLL_PROT_REG_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_CONSENT_TIER_STATUS (
   IDENTIFIER bigint not null auto_increment,
   CONSENT_TIER_ID bigint,
   STATUS varchar(255),
   SPECIMEN_ID bigint,
   SPECIMEN_COLL_GROUP_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_CONSENT_TIER (
   IDENTIFIER bigint not null auto_increment,
   STATEMENT text,
   COLL_PROTOCOL_ID bigint,
   primary key (IDENTIFIER)
);

alter table CATISSUE_CONSENT_TIER_RESPONSE add index FKFB1995FD4AD77FCB (COLL_PROT_REG_ID), add constraint FKFB1995FD4AD77FCB foreign key (COLL_PROT_REG_ID) references CATISSUE_COLL_PROT_REG (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER_RESPONSE add index FKFB1995FD17B9953 (CONSENT_TIER_ID), add constraint FKFB1995FD17B9953 foreign key (CONSENT_TIER_ID) references CATISSUE_CONSENT_TIER (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER_STATUS add index FKF74E94AEF69249F7 (SPECIMEN_COLL_GROUP_ID), add constraint FKF74E94AEF69249F7 foreign key (SPECIMEN_COLL_GROUP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER_STATUS add index FKF74E94AE60773DB2 (SPECIMEN_ID), add constraint FKF74E94AE60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER_STATUS add index FKF74E94AE17B9953 (CONSENT_TIER_ID), add constraint FKF74E94AE17B9953 foreign key (CONSENT_TIER_ID) references CATISSUE_CONSENT_TIER (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER add index FK51725303E36A4B4F (COLL_PROTOCOL_ID), add constraint FK51725303E36A4B4F foreign key (COLL_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);

	/*----caTissue tables changed for ordering system and consent tracking----- */
alter table CATISSUE_DISTRIBUTION drop foreign key FK54276680783867CC;	
alter table CATISSUE_DISTRIBUTED_ITEM drop foreign key FKA7C3ED4BC4A3C438;	
alter table CATISSUE_COLL_PROT_REG drop foreign key FK5EB25F13A0FF79D4;
	
alter table CATISSUE_DISTRIBUTION add column ORDER_ID bigint;
alter table CATISSUE_DISTRIBUTED_ITEM add column SPECIMEN_ARRAY_ID bigint;
alter table CATISSUE_COLLECTION_PROTOCOL add column UNSIGNED_CONSENT_DOC_URL text;
alter table CATISSUE_COLL_PROT_REG add column (CONSENT_SIGN_DATE datetime,CONSENT_DOC_URL text,CONSENT_WITNESS bigint);

alter table CATISSUE_DISTRIBUTION add index FK54276680783867CC (ORDER_ID), add constraint FK54276680783867CC foreign key (ORDER_ID) references CATISSUE_ORDER (IDENTIFIER);
alter table CATISSUE_COLL_PROT_REG add index FK5EB25F13A0FF79D4 (CONSENT_WITNESS), add constraint FK5EB25F13A0FF79D4 foreign key (CONSENT_WITNESS) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_DISTRIBUTED_ITEM add index FKA7C3ED4BC4A3C438 (SPECIMEN_ARRAY_ID), add constraint FKA7C3ED4BC4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER);
/*-----END Ashish: Ordering System----*/

/*---bug 3003-----Ashish ---7/12/06-----*/
alter table catissue_order_item modify DESCRIPTION text;

/*-----Added association ---Ashish 7/12/06 -----*/
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM drop foreign key FKC5C92CCBC4A3C438;
alter table catissue_new_sp_ar_order_item add column SPECIMEN_ARRAY_ID bigint;
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add index FKC5C92CCBC4A3C438 (SPECIMEN_ARRAY_ID), add constraint FKC5C92CCBC4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER);

/****caTIES Realated Tables - start**********/

alter table CATISSUE_REPORT_TEXTCONTENT drop foreign key FKD74882FD91092806;
alter table CATISSUE_REPORT_TEXTCONTENT drop foreign key FKD74882FDBC7298A9;
alter table CATISSUE_IDENTIFIED_REPORT drop foreign key FK6A2246DCBC7298A9;
alter table CATISSUE_IDENTIFIED_REPORT drop foreign key FK6A2246DC752DD177;
alter table CATISSUE_IDENTIFIED_REPORT drop foreign key FK6A2246DC91741663;
alter table CATISSUE_CONCEPT_REFERENT drop foreign key FK799CCA7E9F96B363;
alter table CATISSUE_REVIEW_PARAMS drop foreign key FK5311FFF62206F20F;
alter table CATISSUE_REVIEW_PARAMS drop foreign key FK5311FFF691092806;
alter table CATISSUE_REPORT_BICONTENT drop foreign key FK8A9A4EE391092806;
alter table CATISSUE_REPORT_BICONTENT drop foreign key FK8A9A4EE3BC7298A9;
alter table CATISSUE_DEIDENTIFIED_REPORT drop foreign key FKCDD0DF7BBC7298A9;
alter table CATISSUE_DEIDENTIFIED_REPORT drop foreign key FKCDD0DF7B91741663;
alter table CATISSUE_QUARANTINE_PARAMS drop foreign key FK3C12AE3B2206F20F;
alter table CATISSUE_QUARANTINE_PARAMS drop foreign key FK3C12AE3B3EEC14E3;
alter table CATISSUE_PATHOLOGY_REPORT drop foreign key FK904EC9F040DCD7BF;
alter table CATISSUE_REPORT_XMLCONTENT drop foreign key FK4597C9F1BC7298A9;
alter table CATISSUE_REPORT_XMLCONTENT drop foreign key FK4597C9F191092806;
/*Ashish --8 march 07 */
alter table CATISSUE_REPORT_QUEUE drop foreign key FK214246228CA560D1;
alter table CATISSUE_CONCEPT_REFERENT drop foreign key FK799CCA7EA9816272;
alter table CATISSUE_CONCEPT_REFERENT drop foreign key FK799CCA7E72C371DD;

drop table if exists CATISSUE_REPORT_TEXTCONTENT;
drop table if exists CATISSUE_IDENTIFIED_REPORT;
drop table if exists CATISSUE_CONCEPT_REFERENT;
drop table if exists CATISSUE_REPORT_CONTENT;
drop table if exists CATISSUE_REVIEW_PARAMS;
drop table if exists CATISSUE_REPORT_BICONTENT;
drop table if exists CATISSUE_REPORT_SECTION;
drop table if exists CATISSUE_DEIDENTIFIED_REPORT;
drop table if exists CATISSUE_PATHOLOGY_REPORT;
drop table if exists CATISSUE_REPORT_XMLCONTENT;
drop table if exists CATISSUE_REPORT_QUEUE;
drop table if exists CATISSUE_REPORT_PARTICIP_REL;


create table CATISSUE_REPORT_TEXTCONTENT (
   IDENTIFIER bigint not null,
   REPORT_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_IDENTIFIED_REPORT (
   IDENTIFIER bigint not null,
   DEID_REPORT bigint,
   SCG_ID bigint,
   primary key (IDENTIFIER)
);
/* Ashish 8/3/07*/
create table CATISSUE_CONCEPT_REFERENT (
   IDENTIFIER bigint not null auto_increment,
   CONCEPT_ID bigint,
   CONCEPT_CLASSIFICATION_ID bigint,
   DEIDENTIFIED_REPORT_ID bigint,
   END_OFFSET bigint,
   IS_MODIFIER bit,
   IS_NEGATED bit,
   START_OFFSET bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_REPORT_CONTENT (
   IDENTIFIER bigint not null auto_increment,
   REPORT_DATA text,
   primary key (IDENTIFIER)
);
create table CATISSUE_REVIEW_PARAMS (
   IDENTIFIER bigint not null auto_increment,
   REVIEWER_ROLE varchar(100),
   REPORT_ID bigint,
   EVENT_TIMESTAMP datetime,
   USER_ID bigint,
   COMMENTS text,
   STATUS varchar(100),
   primary key (IDENTIFIER)
);
create table CATISSUE_REPORT_BICONTENT (
   IDENTIFIER bigint not null,
   REPORT_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_REPORT_SECTION (
   IDENTIFIER bigint not null auto_increment,
   DOCUMENT_FRAGMENT text,
   END_OFFSET integer,
   NAME varchar(100),
   START_OFFSET integer,
   TEXT_CONTENT_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_DEIDENTIFIED_REPORT (
   IDENTIFIER bigint not null,
   STATUS varchar(100),
   SCG_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_QUARANTINE_PARAMS (
   IDENTIFIER bigint not null auto_increment,
   DEID_REPORT_ID bigint,
   IS_QUARANTINED bit,
   EVENT_TIMESTAMP datetime,
   USER_ID bigint,
   COMMENTS text,
   STATUS varchar(100),
   primary key (IDENTIFIER)
);
create table CATISSUE_PATHOLOGY_REPORT (
   IDENTIFIER bigint not null auto_increment,
   ACTIVITY_STATUS varchar(100),
   REVIEW_FLAG bit,
   SOURCE_ID bigint,
   REPORT_STATUS varchar(100),
   COLLECTION_DATE_TIME date,
   primary key (IDENTIFIER)
);

create table CATISSUE_REPORT_XMLCONTENT (
   IDENTIFIER bigint not null,
   REPORT_ID bigint,
   primary key (IDENTIFIER)
);
/* Ashish 8/3/07*/
create table CATISSUE_REPORT_QUEUE (
   IDENTIFIER bigint not null auto_increment,
   STATUS varchar(50),
   REPORT_TEXT text,
   SPECIMEN_COLL_GRP_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_REPORT_PARTICIP_REL(
   PARTICIPANT_ID bigint,
   REPORT_ID bigint
);

alter table CATISSUE_REPORT_TEXTCONTENT add index FKD74882FD91092806 (REPORT_ID), add constraint FKD74882FD91092806 foreign key (REPORT_ID) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);
alter table CATISSUE_REPORT_TEXTCONTENT add index FKD74882FDBC7298A9 (IDENTIFIER), add constraint FKD74882FDBC7298A9 foreign key (IDENTIFIER) references CATISSUE_REPORT_CONTENT (IDENTIFIER);
alter table CATISSUE_IDENTIFIED_REPORT add index FK6A2246DCBC7298A9 (IDENTIFIER), add constraint FK6A2246DCBC7298A9 foreign key (IDENTIFIER) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);
alter table CATISSUE_IDENTIFIED_REPORT add index FK6A2246DC752DD177 (DEID_REPORT), add constraint FK6A2246DC752DD177 foreign key (DEID_REPORT) references CATISSUE_DEIDENTIFIED_REPORT (IDENTIFIER);
alter table CATISSUE_IDENTIFIED_REPORT add index FK6A2246DC91741663 (SCG_ID), add constraint FK6A2246DC91741663 foreign key (SCG_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_CONCEPT_REFERENT add index FK799CCA7E9F96B363 (DEIDENTIFIED_REPORT_ID), add constraint FK799CCA7E9F96B363 foreign key (DEIDENTIFIED_REPORT_ID) references CATISSUE_DEIDENTIFIED_REPORT (IDENTIFIER);
alter table CATISSUE_REVIEW_PARAMS add index FK5311FFF62206F20F (USER_ID), add constraint FK5311FFF62206F20F foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_REVIEW_PARAMS add index FK5311FFF691092806 (REPORT_ID), add constraint FK5311FFF691092806 foreign key (REPORT_ID) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);
alter table CATISSUE_REPORT_BICONTENT add index FK8A9A4EE391092806 (REPORT_ID), add constraint FK8A9A4EE391092806 foreign key (REPORT_ID) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);
alter table CATISSUE_REPORT_BICONTENT add index FK8A9A4EE3BC7298A9 (IDENTIFIER), add constraint FK8A9A4EE3BC7298A9 foreign key (IDENTIFIER) references CATISSUE_REPORT_CONTENT (IDENTIFIER);
alter table CATISSUE_DEIDENTIFIED_REPORT add index FKCDD0DF7BBC7298A9 (IDENTIFIER), add constraint FKCDD0DF7BBC7298A9 foreign key (IDENTIFIER) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);
alter table CATISSUE_DEIDENTIFIED_REPORT add index FKCDD0DF7B91741663 (SCG_ID), add constraint FKCDD0DF7B91741663 foreign key (SCG_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_QUARANTINE_PARAMS add index FK3C12AE3B2206F20F (USER_ID), add constraint FK3C12AE3B2206F20F foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_QUARANTINE_PARAMS add index FK3C12AE3B3EEC14E3 (DEID_REPORT_ID), add constraint FK3C12AE3B3EEC14E3 foreign key (DEID_REPORT_ID) references CATISSUE_DEIDENTIFIED_REPORT (IDENTIFIER);
alter table CATISSUE_PATHOLOGY_REPORT add index FK904EC9F040DCD7BF (SOURCE_ID), add constraint FK904EC9F040DCD7BF foreign key (SOURCE_ID) references CATISSUE_SITE (IDENTIFIER);
alter table CATISSUE_REPORT_XMLCONTENT add index FK4597C9F1BC7298A9 (IDENTIFIER), add constraint FK4597C9F1BC7298A9 foreign key (IDENTIFIER) references CATISSUE_REPORT_CONTENT (IDENTIFIER);
alter table CATISSUE_REPORT_XMLCONTENT add index FK4597C9F191092806 (REPORT_ID), add constraint FK4597C9F191092806 foreign key (REPORT_ID) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);

/*Ashish 8/3/07*/
alter table CATISSUE_REPORT_QUEUE add index FK214246228CA560D1 (SPECIMEN_COLL_GRP_ID), add constraint FK214246228CA560D1 foreign key (SPECIMEN_COLL_GRP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);

/* caTies tables for Concept HighLighting  -- Ashish -- 8 March,07 */

alter table CATISSUE_CONCEPT drop foreign key FKC1A3C8CC7F0C2C7;

drop table if exists CATISSUE_CONCEPT;
drop table if exists CATISSUE_SEMANTIC_TYPE;
drop table if exists CATISSUE_CONCEPT_CLASSIFICATN;

create table CATISSUE_CONCEPT (
   IDENTIFIER bigint not null auto_increment,
   CONCEPT_UNIQUE_ID varchar(30),
   NAME text,
   SEMANTIC_TYPE_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_SEMANTIC_TYPE (
   IDENTIFIER bigint not null auto_increment,
   LABEL text,
   primary key (IDENTIFIER)
);

create table CATISSUE_CONCEPT_CLASSIFICATN (
   IDENTIFIER bigint not null auto_increment,
   NAME text,
   primary key (IDENTIFIER)
);

alter table CATISSUE_CONCEPT add index FKC1A3C8CC7F0C2C7 (SEMANTIC_TYPE_ID), add constraint FKC1A3C8CC7F0C2C7 foreign key (SEMANTIC_TYPE_ID) references CATISSUE_SEMANTIC_TYPE (IDENTIFIER);
alter table CATISSUE_CONCEPT_REFERENT add index FK799CCA7EA9816272 (CONCEPT_ID), add constraint FK799CCA7EA9816272 foreign key (CONCEPT_ID) references CATISSUE_CONCEPT (IDENTIFIER);
alter table CATISSUE_CONCEPT_REFERENT add index FK799CCA7E72C371DD (CONCEPT_CLASSIFICATION_ID), add constraint FK799CCA7E72C371DD foreign key (CONCEPT_CLASSIFICATION_ID) references CATISSUE_CONCEPT_CLASSIFICATN (IDENTIFIER);


INSERT INTO CSM_PROTECTION_ELEMENT select max(PROTECTION_ELEMENT_ID)+1,'Review Comments','PathologyReportReviewParameter Object','edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter',NULL,NULL,1,'2006-11-27' from CSM_PROTECTION_ELEMENT;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,20,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Review Comments'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CSM_PROTECTION_ELEMENT select max(PROTECTION_ELEMENT_ID)+1,'Quarantine Comments','QuarantineEventParameter Object','edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter',NULL,NULL,1,'2006-11-27' from CSM_PROTECTION_ELEMENT;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,20,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Quarantine Comments'),'2006-11-27' from CSM_PG_PE;
INSERT INTO CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 77, 'CATISSUE_REVIEW_PARAMS', 'Review Comment', 'PathologyReportReviewParameter', 1);
INSERT INTO CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values ( 78, 'CATISSUE_REVIEW_PARAMS', 'Quarantine Comment', 'QuarantineEventParameter', 1);


/****caTIES Realated Tables - end**********/

#---------------------Ordering System Permissions----Ashish 4/1/06
insert into csm_role(ROLE_ID,ROLE_NAME,ROLE_DESCRIPTION,APPLICATION_ID) values (11,'CREATE_ONLY','Create only role',1);
insert into csm_role_privilege(ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID) values (28,11,1);

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

#------------------------New Table entry For ConsentWithdrawal ---------- Mandar : 18-Jan-07 -------------start
insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 79, 'CATISSUE_RETURN_EVENT_PARAM', 'Return Event Parameters', 'ReturnEventParameters',2,2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 328, 79, 'IDENTIFIER', 'bigint');

create table CATISSUE_RETURN_EVENT_PARAM (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);

alter table CATISSUE_RETURN_EVENT_PARAM add index FKD8890A48BC7298A91 (IDENTIFIER), add constraint FKD8890A48BC7298A91 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);

INSERT into `CSM_PROTECTION_ELEMENT` select max(PROTECTION_ELEMENT_ID)+1,'ReturnEventParameters','ReturnEventParameters Class','edu.wustl.catissuecore.domain.ReturnEventParameters',NULL,NULL,1,'2007-01-17' from CSM_PROTECTION_ELEMENT;
INSERT INTO CSM_PG_PE select max(PG_PE_ID)+1,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='ReturnEventParameters'),'2006-11-27' from CSM_PG_PE;
#------------------------New Table entry For ConsentWithdrawal ---------- Mandar : 18-Jan-07 -------------end

insert into csm_protection_element (PROTECTION_ELEMENT_NAME,PROTECTION_ELEMENT_DESCRIPTION,OBJECT_ID,APPLICATION_ID)values('edu.wustl.catissuecore.action.querysuite.AddToLimitSetAction','edu.wustl.catissuecore.action.querysuite.AddToLimitSetAction','edu.wustl.catissuecore.action.querysuite.AddToLimitSetAction',1);
insert into csm_pg_pe (PROTECTION_GROUP_ID,PROTECTION_ELEMENT_ID) values (24,(select MAX(PROTECTION_ELEMENT_ID) from csm_protection_element where OBJECT_ID='edu.wustl.catissuecore.action.querysuite.AddToLimitSetAction'));

insert into csm_protection_element (PROTECTION_ELEMENT_NAME,PROTECTION_ELEMENT_DESCRIPTION,OBJECT_ID,APPLICATION_ID)values('edu.wustl.catissuecore.action.querysuite.ViewSearchResultsAction','edu.wustl.catissuecore.action.querysuite.ViewSearchResultsAction','edu.wustl.catissuecore.action.querysuite.ViewSearchResultsAction',1);
insert into csm_pg_pe (PROTECTION_GROUP_ID,PROTECTION_ELEMENT_ID) values (24,(select MAX(PROTECTION_ELEMENT_ID) from csm_protection_element where OBJECT_ID='edu.wustl.catissuecore.action.querysuite.ViewSearchResultsAction'));

insert into csm_protection_element (PROTECTION_ELEMENT_NAME,PROTECTION_ELEMENT_DESCRIPTION,OBJECT_ID,APPLICATION_ID)values('edu.wustl.catissuecore.action.querysuite.PathFinderAction','edu.wustl.catissuecore.action.querysuite.PathFinderAction','edu.wustl.catissuecore.action.querysuite.PathFinderAction',1);
insert into csm_pg_pe (PROTECTION_GROUP_ID,PROTECTION_ELEMENT_ID) values (24,(select MAX(PROTECTION_ELEMENT_ID) from csm_protection_element where OBJECT_ID='edu.wustl.catissuecore.action.querysuite.PathFinderAction'));

insert into csm_protection_element (PROTECTION_ELEMENT_NAME,PROTECTION_ELEMENT_DESCRIPTION,OBJECT_ID,APPLICATION_ID)values('edu.wustl.catissuecore.action.querysuite.GetDagViewDataAction','edu.wustl.catissuecore.action.querysuite.GetDagViewDataAction','edu.wustl.catissuecore.action.querysuite.GetDagViewDataAction',1);
insert into csm_pg_pe (PROTECTION_GROUP_ID,PROTECTION_ELEMENT_ID) values (24,(select MAX(PROTECTION_ELEMENT_ID) from csm_protection_element where OBJECT_ID='edu.wustl.catissuecore.action.querysuite.GetDagViewDataAction'));

#------------------------New Column Entry For ConsentWaived ---------- Mandar : 25-Jan-07 -------------start
alter table CATISSUE_COLLECTION_PROTOCOL add column CONSENTS_WAIVED bit;
update catissue_collection_protocol set  CONSENTS_WAIVED='0' where  CONSENTS_WAIVED is null;
#------------------------New Column Entry For ConsentWaived ---------- Mandar : 25-Jan-07 -------------end
#------------------------New Column Entry For Participant ---------- Sachin : 13-Mar-07 -------------start
alter table CATISSUE_PARTICIPANT add column MARITAL_STATUS varchar(50);
#------------------------New Column Entry For Participant ---------- Sachin : 13-Mar-07 -------------end


create table CURATED_PATH (
	curated_path_Id BIGINT,
	entity_ids VARCHAR(1000),
	selected boolean,
	primary key (curated_path_Id)
);

/*This is mapping table for many-to-many relationship between tables PATH and CURATED_PATH */
create table CURATED_PATH_TO_PATH (
	curated_path_Id BIGINT references CURATED_PATH (curated_path_Id),
	path_id BIGINT  references PATH (path_id),
	primary key (curated_path_Id,path_id)
);