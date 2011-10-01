SET storage_engine=InnoDB;
SET FOREIGN_KEY_CHECKS=0;
drop table if exists CURATED_PATH; 
drop table if exists CURATED_PATH_TO_PATH;
drop table if exists PATH;
drop table if exists INTER_MODEL_ASSOCIATION;
drop table if exists INTRA_MODEL_ASSOCIATION;
drop table if exists ASSOCIATION;
drop table if exists ID_TABLE;

SET FOREIGN_KEY_CHECKS=1;
/*INTERMEDIATE_PATH contains  ASSOCIATION(ASSOCIATION_ID) connected by underscore */
create table PATH(
     PATH_ID           bigint         not null,
     FIRST_ENTITY_ID   bigint         null,
     INTERMEDIATE_PATH varchar(1000)  null,
     LAST_ENTITY_ID    bigint         null,
     primary key (PATH_ID),
     index INDEX1 (FIRST_ENTITY_ID,LAST_ENTITY_ID)
);
/* Possible values for ASSOCIATION_TYPE are 1 and 2
ASSOCIATION_TYPE = 1 represents INTER_MODEL_ASSOCIATION.
ASSOCIATION_TYPE = 2 represents INTRA_MODEL_ASSOCIATION.
*/     
create table ASSOCIATION(
    ASSOCIATION_ID    bigint    not null,
    ASSOCIATION_TYPE  INT(8)    not null ,
    primary key (ASSOCIATION_ID)
);

create table INTER_MODEL_ASSOCIATION(
    ASSOCIATION_ID      bigint  not null references ASSOCIATION(ASSOCIATION_ID),
    LEFT_ENTITY_ID      bigint  not null,
    LEFT_ATTRIBUTE_ID   bigint  not null,
    RIGHT_ENTITY_ID     bigint  not null,
    RIGHT_ATTRIBUTE_ID  bigint  not null,
    primary key (ASSOCIATION_ID) 
);
create table INTRA_MODEL_ASSOCIATION(
    ASSOCIATION_ID    bigint    not null references ASSOCIATION(ASSOCIATION_ID),
    DE_ASSOCIATION_ID bigint    not null,
    primary key (ASSOCIATION_ID) 
);
create table ID_TABLE(
    NEXT_ASSOCIATION_ID    bigint    not null,
    primary key (NEXT_ASSOCIATION_ID)
);
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
insert into ID_TABLE(NEXT_ASSOCIATION_ID) value(1);