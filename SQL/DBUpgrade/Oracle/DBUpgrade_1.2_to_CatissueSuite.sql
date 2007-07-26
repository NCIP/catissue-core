
# ----------------------ordering changes  - vaishali 
/* catissuecore.sql */
#ordering

#---- Ordering system  related alter table script


/* ordering */
create sequence CATISSUE_ORDER_SEQ;
create sequence CATISSUE_ORDER_ITEM_SEQ;
/* ordering finish */

alter table CATISSUE_EXISTING_SP_ORD_ITEM drop constraint FKF8B855EEBC7298A9;
alter table CATISSUE_EXISTING_SP_ORD_ITEM drop constraint FKF8B855EE60773DB2;
alter table CATISSUE_PATH_CASE_ORDER_ITEM drop constraint FKBD5029D5F69249F7;
alter table CATISSUE_PATH_CASE_ORDER_ITEM drop constraint FKBD5029D5BC7298A9;
alter table CATISSUE_ORDER_ITEM drop constraint FKB501E88060975C0B;
alter table CATISSUE_ORDER_ITEM drop constraint FKB501E880783867CC;
alter table CATISSUE_DERIEVED_SP_ORD_ITEM drop constraint FK3742152BBC7298A9;
alter table CATISSUE_DERIEVED_SP_ORD_ITEM drop constraint FK3742152B60773DB2;
alter table CATISSUE_ORDER drop constraint FK543F22B26B1F36E7;
alter table CATISSUE_DISTRIBUTION drop constraint FK54276680783867CC;
alter table CATISSUE_SP_ARRAY_ORDER_ITEM drop constraint FKE3823170BC7298A9;
alter table CATISSUE_SP_ARRAY_ORDER_ITEM drop constraint FKE3823170C4A3C438;
alter table CATISSUE_SPECIMEN_ORDER_ITEM drop constraint FK48C3B39FBC7298A9;
alter table CATISSUE_SPECIMEN_ORDER_ITEM drop constraint FK48C3B39F83505A30;
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM drop constraint FKC5C92CCBCE5FBC3A;
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM drop constraint FKC5C92CCBBC7298A9;




#------- ordering system relate tables----

drop table CATISSUE_EXISTING_SP_ORD_ITEM cascade constraints;
drop table CATISSUE_PATH_CASE_ORDER_ITEM cascade constraints;
drop table CATISSUE_ORDER_ITEM cascade constraints;
drop table CATISSUE_DERIEVED_SP_ORD_ITEM cascade constraints;
drop table CATISSUE_ORDER cascade constraints;
drop table CATISSUE_SP_ARRAY_ORDER_ITEM cascade constraints;
drop table CATISSUE_SPECIMEN_ORDER_ITEM cascade constraints;
drop table CATISSUE_NEW_SP_AR_ORDER_ITEM cascade constraints;

create table CATISSUE_EXISTING_SP_ORD_ITEM (
   IDENTIFIER number(19,0) not null,
   SPECIMEN_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_PATH_CASE_ORDER_ITEM (
   IDENTIFIER number(19,0) not null,
   PATHOLOGICAL_STATUS varchar(255),
   TISSUE_SITE varchar(255),
   SPECIMEN_CLASS varchar(255),
   SPECIMEN_TYPE varchar(255),
   SPECIMEN_COLL_GROUP_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_ORDER_ITEM (
   IDENTIFIER number(19,0) not null,
   DESCRIPTION varchar2(500),
   DISTRIBUTED_ITEM_ID number(19,0),
   STATUS varchar(50),
   REQUESTED_QUANTITY double precision,
   ORDER_ID number(19,0),
   primary key (IDENTIFIER)
);

create table CATISSUE_DERIEVED_SP_ORD_ITEM (
   IDENTIFIER number(19,0) not null,
   SPECIMEN_CLASS varchar(255),
   SPECIMEN_TYPE varchar(255),
   SPECIMEN_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_ORDER (
   IDENTIFIER number(19,0) not null,
   COMMENTS varchar2(500),
   DISTRIBUTION_PROTOCOL_ID number(19,0),
   NAME varchar2(500),
   REQUESTED_DATE date,
   STATUS varchar(50),
   primary key (IDENTIFIER)
);

create table CATISSUE_SPECIMEN_ORDER_ITEM (
   IDENTIFIER number(19,0) not null,
   ARRAY_ORDER_ITEM_ID number(19,0),
   primary key (IDENTIFIER)
);

create table CATISSUE_SP_ARRAY_ORDER_ITEM (
   IDENTIFIER number(19,0) not null,
   SPECIMEN_ARRAY_ID number(19,0),
   primary key (IDENTIFIER)
);

create table CATISSUE_NEW_SP_AR_ORDER_ITEM (
   IDENTIFIER number(19,0) not null,
   ARRAY_TYPE_ID number(19,0),
   NAME varchar(255),
   SPECIMEN_ARRAY_ID number(19,0),
   primary key (IDENTIFIER)
);
alter table CATISSUE_EXISTING_SP_ORD_ITEM add constraint FKF8B855EEBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_EXISTING_SP_ORD_ITEM add constraint FKF8B855EE60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_PATH_CASE_ORDER_ITEM add constraint FKBD5029D5F69249F7 foreign key (SPECIMEN_COLL_GROUP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_PATH_CASE_ORDER_ITEM add constraint FKBD5029D5BC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_ORDER_ITEM add constraint FKB501E88060975C0B foreign key (DISTRIBUTED_ITEM_ID) references CATISSUE_DISTRIBUTED_ITEM (IDENTIFIER);
alter table CATISSUE_ORDER_ITEM add constraint FKB501E880783867CC foreign key (ORDER_ID) references CATISSUE_ORDER (IDENTIFIER);
alter table CATISSUE_DISTRIBUTION add constraint FK54276680783867CC foreign key (ORDER_ID) references CATISSUE_ORDER (IDENTIFIER);
alter table CATISSUE_DERIEVED_SP_ORD_ITEM add constraint FK3742152BBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_DERIEVED_SP_ORD_ITEM add constraint FK3742152B60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_ORDER add constraint FK543F22B26B1F36E7 foreign key (DISTRIBUTION_PROTOCOL_ID) references CATISSUE_DISTRIBUTION_PROTOCOL (IDENTIFIER);
alter table CATISSUE_SP_ARRAY_ORDER_ITEM add constraint FKE3823170BC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_SP_ARRAY_ORDER_ITEM add constraint FKE3823170C4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ORDER_ITEM add constraint FK48C3B39FBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ORDER_ITEM add constraint FK48C3B39F83505A30 foreign key (ARRAY_ORDER_ITEM_ID) references CATISSUE_NEW_SP_AR_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add constraint FKC5C92CCBCE5FBC3A foreign key (ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE (IDENTIFIER);
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add constraint FKC5C92CCBBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add constraint FKC5C92CCBC4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER);


/* csm_inserData_Oracle.sql */
/*-----Ordering System------*/
INSERT into CSM_PROTECTION_ELEMENT values (CSM_PROTECTIO_PROTECTION_E_SEQ.NEXTVAL,'Order','Order Object','edu.wustl.catissuecore.domain.OrderDetails',NULL,NULL,1,to_date('2006-11-27','yyyy-mm-dd'));
INSERT into CSM_PROTECTION_ELEMENT values (CSM_PROTECTIO_PROTECTION_E_SEQ.NEXTVAL,'OrderItem','OrderItem Object','edu.wustl.catissuecore.domain.OrderItem',NULL,NULL,1,to_date('2006-11-27','yyyy-mm-dd'));
INSERT into CSM_PROTECTION_ELEMENT values (CSM_PROTECTIO_PROTECTION_E_SEQ.NEXTVAL,'Derived Specimen Order Item','Derived Specimen Order Item Object','edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem',NULL,NULL,1,to_date('2006-11-27','yyyy-mm-dd'));
INSERT into CSM_PROTECTION_ELEMENT values (CSM_PROTECTIO_PROTECTION_E_SEQ.NEXTVAL,'Existing Specimen Array Order Item','Existing Specimen Array Order Item Object','edu.wustl.catissuecore.domain.ExistingSpecimenArrayOrderItem',NULL,NULL,1,to_date('2006-11-27','yyyy-mm-dd'));
INSERT into CSM_PROTECTION_ELEMENT values (CSM_PROTECTIO_PROTECTION_E_SEQ.NEXTVAL,'Existing Specimen Order Item','Existing Specimen Order Item Object','edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem',NULL,NULL,1,to_date('2006-11-27','yyyy-mm-dd'));
INSERT into CSM_PROTECTION_ELEMENT values (CSM_PROTECTIO_PROTECTION_E_SEQ.NEXTVAL,'New Specimen Array Order Item','New Specimen Array Order Item Object','edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem',NULL,NULL,1,to_date('2006-11-27','yyyy-mm-dd'));
INSERT into CSM_PROTECTION_ELEMENT values (CSM_PROTECTIO_PROTECTION_E_SEQ.NEXTVAL,'New Specimen Order Item','New Specimen Order Item Object','edu.wustl.catissuecore.domain.NewSpecimenOrderItem',NULL,NULL,1,to_date('2006-11-27','yyyy-mm-dd'));
INSERT into CSM_PROTECTION_ELEMENT values (CSM_PROTECTIO_PROTECTION_E_SEQ.NEXTVAL,'Pathological Case Order Item','Pathological Case Order Item Object','edu.wustl.catissuecore.domain.PathologicalCaseOrderItem',NULL,NULL,1,to_date('2006-11-27','yyyy-mm-dd'));
INSERT into CSM_PROTECTION_ELEMENT values (CSM_PROTECTIO_PROTECTION_E_SEQ.NEXTVAL,'Specimen Array Order Item','Specimen Array Order Item Object','edu.wustl.catissuecore.domain.SpecimenArrayOrderItem',NULL,NULL,1,to_date('2006-11-27','yyyy-mm-dd'));
INSERT into CSM_PROTECTION_ELEMENT values (CSM_PROTECTIO_PROTECTION_E_SEQ.NEXTVAL,'Specimen Order Item','Specimen Order Item Object','edu.wustl.catissuecore.domain.SpecimenOrderItem',NULL,NULL,1,to_date('2006-11-27','yyyy-mm-dd'));


INSERT into CSM_PROTECTION_ELEMENT select max(PROTECTION_ELEMENT_ID)+1,'edu.wustl.catissuecore.action.RequestListAction','edu.wustl.catissuecore.action.RequestListAction','edu.wustl.catissuecore.action.RequestListAction',NULL,NULL,1,to_date('2007-01-04','yyyy-mm-dd') from CSM_PROTECTION_ELEMENT;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='edu.wustl.catissuecore.action.RequestListAction'),to_date('2007-01-04','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='edu.wustl.catissuecore.action.RequestListAction'),to_date('2007-01-04','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='edu.wustl.catissuecore.action.RequestListAction'),to_date('2007-01-04','yyyy-mm-dd') from dual;


	/*-------Ordering System---------*/	
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Order'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Order'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Order'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='OrderItem'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='OrderItem'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='OrderItem'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Derived Specimen Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Derived Specimen Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Derived Specimen Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Existing Specimen Array Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Existing Specimen Array Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Existing Specimen Array Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Existing Specimen Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Existing Specimen Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Existing Specimen Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='New Specimen Array Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='New Specimen Array Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='New Specimen Array Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='New Specimen Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='New Specimen Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='New Specimen Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Pathological Case Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Pathological Case Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Pathological Case Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Specimen Array Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Specimen Array Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Specimen Array Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Specimen Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Specimen Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Specimen Order Item'),to_date('2006-11-27','yyyy-mm-dd') from dual;


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
/* order system finish */

/* extra for catissue_distribution */
 alter table catissue_distribution add ORDER_ID number(19,0);
  alter table catissue_distributed_item add  SPECIMEN_ARRAY_ID number(19,0);  
  alter table CATISSUE_DISTRIBUTION add constraint FK54276680783867CC foreign key (ORDER_ID) references CATISSUE_ORDER (IDENTIFIER);
  alter table CATISSUE_DISTRIBUTED_ITEM add constraint FKA7C3ED4BC4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY;
  
/* extra finished */
------------------------ordering changes finish


/*--------Consent Tracking-------*/
INSERT into CSM_PROTECTION_ELEMENT select max(PROTECTION_ELEMENT_ID)+1,'Consent Tier','ConsentTier Object','edu.wustl.catissuecore.domain.ConsentTier',NULL,NULL,1,to_date('2006-11-27','yyyy-mm-dd') from CSM_PROTECTION_ELEMENT;
INSERT into CSM_PROTECTION_ELEMENT select max(PROTECTION_ELEMENT_ID)+1,'Consent Tier Response','Consent Tier Response Object','edu.wustl.catissuecore.domain.ConsentTierResponse',NULL,NULL,1,to_date('2006-11-27','yyyy-mm-dd') from CSM_PROTECTION_ELEMENT;
INSERT into CSM_PROTECTION_ELEMENT select max(PROTECTION_ELEMENT_ID)+1,'Consent Tier Status','Consent Tier Status Object','edu.wustl.catissuecore.domain.ConsentTierStatus',NULL,NULL,1,to_date('2006-11-27','yyyy-mm-dd') from CSM_PROTECTION_ELEMENT;
	
/*--------Consent Tracking-------*/
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier Response'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier Response'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier Response'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,1,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier Status'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,2,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier Status'),to_date('2006-11-27','yyyy-mm-dd') from dual;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='Consent Tier Status'),to_date('2006-11-27','yyyy-mm-dd') from dual;

/*Consent Tracking related drop, create and add foreign key scripts.*/
drop table CATISSUE_CONSENT_TIER_RESPONSE;
drop table CATISSUE_CONSENT_TIER_STATUS;
drop table CATISSUE_CONSENT_TIER;

create table CATISSUE_CONSENT_TIER_RESPONSE (
   IDENTIFIER number(19,0) not null,
   RESPONSE varchar(255),
   CONSENT_TIER_ID number(19,0),
   COLL_PROT_REG_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_CONSENT_TIER_STATUS (
   IDENTIFIER number(19,0) not null,
   CONSENT_TIER_ID number(19,0),
   STATUS varchar(255),
   SPECIMEN_ID number(19,0),
   SPECIMEN_COLL_GROUP_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_CONSENT_TIER (
   IDENTIFIER number(19,0) not null,
   STATEMENT varchar2(500),
   COLL_PROTOCOL_ID number(19,0),
   primary key (IDENTIFIER)
);

alter table CATISSUE_CONSENT_TIER_RESPONSE  add constraint FKFB1995FD4AD77FCB foreign key (COLL_PROT_REG_ID) references CATISSUE_COLL_PROT_REG (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER_RESPONSE  add constraint FKFB1995FD17B9953 foreign key (CONSENT_TIER_ID) references CATISSUE_CONSENT_TIER (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER_STATUS  add constraint FKF74E94AEF69249F7 foreign key (SPECIMEN_COLL_GROUP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER_STATUS  add constraint FKF74E94AE60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER_STATUS  add constraint FKF74E94AE17B9953 foreign key (CONSENT_TIER_ID) references CATISSUE_CONSENT_TIER (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER  add constraint FK51725303E36A4B4F foreign key (COLL_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);

create sequence CATISSUE_CONSENT_TIER_RES_SEQ;
create sequence CATISSUE_CONSENT_TIER_STAT_SEQ;
create sequence CATISSUE_CONSENT_TIER_SEQ;
create sequence CATISSUE_ORDER_SEQ;
create sequence CATISSUE_ORDER_ITEM_SEQ;

/*----caTissue tables changed for and consent tracking----- */
alter table CATISSUE_COLL_PROT_REG drop constraint FK5EB25F13A0FF79D4;
alter table CATISSUE_COLLECTION_PROTOCOL add  UNSIGNED_CONSENT_DOC_URL varchar2(500);
alter table CATISSUE_COLL_PROT_REG add  (CONSENT_SIGN_DATE date,CONSENT_DOC_URL varchar2(500),CONSENT_WITNESS number(19,0));
alter table CATISSUE_COLL_PROT_REG add constraint FK5EB25F13A0FF79D4 foreign key (CONSENT_WITNESS) references CATISSUE_USER (IDENTIFIER);


/* ------------------------New Table entry For ConsentWithdrawal ---------- Mandar : 18-Jan-07 -------------start */
insert into CATISSUE_QUERY_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI) values ( 79, 'CATISSUE_RETURN_EVENT_PARAM', 'Return Event Parameters', 'ReturnEventParameters',2,2);
insert into CATISSUE_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) values ( 328, 79, 'IDENTIFIER', 'bigint');

create table CATISSUE_RETURN_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);

alter table CATISSUE_RETURN_EVENT_PARAM add constraint FKD8890A48BC7298A91 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);

INSERT into CSM_PROTECTION_ELEMENT select max(PROTECTION_ELEMENT_ID)+1,'ReturnEventParameters','ReturnEventParameters Class','edu.wustl.catissuecore.domain.ReturnEventParameters',NULL,NULL,1,to_date('2007-01-18','yyyy-mm-dd') from CSM_PROTECTION_ELEMENT;
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,3,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='ReturnEventParameters'),to_date('2006-01-18','yyyy-mm-dd') from dual;

/* ------------------------New Table entry For ConsentWithdrawal ---------- Mandar : 18-Jan-07 -------------end	 */

#------------------------New Column Entry For ConsentWaived ---------- Mandar : 25-Jan-07 -------------start
alter table CATISSUE_COLLECTION_PROTOCOL add CONSENTS_WAIVED number(1,0);
update CATISSUE_COLLECTION_PROTOCOL set CONSENTS_WAIVED=0 where CONSENTS_WAIVED is null;
#------------------------New Column Entry For ConsentWaived ---------- Mandar : 25-Jan-07 -------------end

/****Participant add column- start**********/
alter table CATISSUE_PARTICIPANT add  MARITAL_STATUS varchar2(50);
/****Participant add column- end**********/