--IF EXISTS(SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'CURATED_PATH') DROP TABLE CURATED_PATH;
--IF EXISTS(SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'CURATED_PATH_TO_PATH') DROP TABLE CURATED_PATH_TO_PATH;
--IF EXISTS(SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'PATH') DROP TABLE PATH;
--IF EXISTS(SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'INTER_MODEL_ASSOCIATION') DROP TABLE INTER_MODEL_ASSOCIATION;
--IF EXISTS(SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'INTRA_MODEL_ASSOCIATION') DROP TABLE INTRA_MODEL_ASSOCIATION;
--IF EXISTS(SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'ASSOCIATION') DROP TABLE ASSOCIATION;
--IF EXISTS(SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'ID_TABLE') DROP TABLE ID_TABLE;

/*INTERMEDIATE_PATH contains  ASSOCIATION(ASSOCIATION_ID) connected by underscore */
create table PATH(
     PATH_ID           bigint         not null,
     FIRST_ENTITY_ID   bigint         null,
     INTERMEDIATE_PATH varchar(1000)  null,
     LAST_ENTITY_ID    bigint         null
);
alter table PATH add constraint PK_PATH_ID primary key (PATH_ID);

CREATE INDEX INDEX1 ON PATH (FIRST_ENTITY_ID,LAST_ENTITY_ID);

/* Possible values for ASSOCIATION_TYPE are 1 and 2
ASSOCIATION_TYPE = 1 represents INTER_MODEL_ASSOCIATION.
ASSOCIATION_TYPE = 2 represents INTRA_MODEL_ASSOCIATION.
*/     
create table ASSOCIATION(
    ASSOCIATION_ID    bigint    not null,
    ASSOCIATION_TYPE  numeric(8)    not null
);
alter table ASSOCIATION add constraint PK_ASSOCIATION_ID primary key (ASSOCIATION_ID);


create table INTER_MODEL_ASSOCIATION(
    ASSOCIATION_ID      bigint  not null references ASSOCIATION(ASSOCIATION_ID),
    LEFT_ENTITY_ID      bigint  not null,
    LEFT_ATTRIBUTE_ID   bigint  not null,
    RIGHT_ENTITY_ID     bigint  not null,
    RIGHT_ATTRIBUTE_ID  bigint  not null
);
alter table INTER_MODEL_ASSOCIATION add constraint PK_INTER_MODEL_ASSO_ID primary key (ASSOCIATION_ID);

create table INTRA_MODEL_ASSOCIATION(
    ASSOCIATION_ID    bigint    not null references ASSOCIATION(ASSOCIATION_ID),
    DE_ASSOCIATION_ID bigint    not null
);
alter table INTRA_MODEL_ASSOCIATION add constraint PK_INTRA_MODEL_ASSO_ID primary key (ASSOCIATION_ID);


create table ID_TABLE(
    NEXT_ASSOCIATION_ID    bigint    not null
);
alter table ID_TABLE add constraint PK_ID_TABLE_NEXT_ASSO_ID primary key (NEXT_ASSOCIATION_ID);


create table CURATED_PATH (
	curated_path_Id BIGINT,
	entity_ids VARCHAR(1000),
	selected bit,
	primary key(curated_path_Id)
);


/*This is mapping table for many-to-many relationship between tables PATH and CURATED_PATH */
create table CURATED_PATH_TO_PATH (
	curated_path_Id BIGINT references CURATED_PATH (curated_path_Id),
	path_id BIGINT  references PATH (path_id),
	primary key(curated_path_Id,path_id)
);

insert into ID_TABLE(NEXT_ASSOCIATION_ID) values(1);