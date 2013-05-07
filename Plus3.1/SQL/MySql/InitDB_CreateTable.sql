SET storage_engine=InnoDB;
drop table if exists CATISSUE_INTERFACE_COLUMN_DATA;
CREATE TABLE CATISSUE_INTERFACE_COLUMN_DATA
(
      IDENTIFIER bigint not null auto_increment,
      TABLE_ID bigint not null,
      COLUMN_NAME varchar(50),
      ATTRIBUTE_TYPE varchar(30),
      primary key (IDENTIFIER)
);

drop table if exists CATISSUE_QUERY_TABLE_DATA;
CREATE TABLE CATISSUE_QUERY_TABLE_DATA
(
      TABLE_ID bigint not null auto_increment, 
      TABLE_NAME varchar(50),
      DISPLAY_NAME varchar(50),
      ALIAS_NAME varchar(50),
      PRIVILEGE_ID int(1),
      FOR_SQI tinyint(1) default NULL,                                                                 
      primary key (TABLE_ID)
);

drop TABLE if exists CATISSUE_TABLE_RELATION;
CREATE TABLE CATISSUE_TABLE_RELATION
(
      RELATIONSHIP_ID bigint auto_increment,
      PARENT_TABLE_ID bigint,
      CHILD_TABLE_ID bigint,
      TABLES_IN_PATH varchar(50),
      primary key (RELATIONSHIP_ID)
);


/*  Name: Shital Lawhale Bug ID: 3549 */
/*  Description : A ATTRIBUTE_ORDER field in CATISSUE_SEARCH_DISPLAY_DATA .*/

drop table if exists CATISSUE_SEARCH_DISPLAY_DATA;
CREATE TABLE CATISSUE_SEARCH_DISPLAY_DATA
(
      RELATIONSHIP_ID bigint not null,
      COL_ID bigint not null,
      DISPLAY_NAME varchar(50),
      DEFAULT_VIEW_ATTRIBUTE bit DEFAULT 0,
      ATTRIBUTE_ORDER int(5)
);

drop table if exists CATISSUE_RELATED_TABLES_MAP;
CREATE TABLE CATISSUE_RELATED_TABLES_MAP
(
      FIRST_TABLE_ID bigint,      
      SECOND_TABLE_ID bigint,
      FIRST_TABLE_JOIN_COLUMN varchar(50),
      SECOND_TABLE_JOIN_COLUMN varchar(50)
);
/*Patch ID: SimpleSearchEdit_13*/
/*Adding Metadata table required for the Simple search edit feature.*/
/*This table contains information about the columns to be hyperlinked for the given table.*/
drop table if exists CATISSUE_QUERY_EDITLINK_COLS;
CREATE TABLE CATISSUE_QUERY_EDITLINK_COLS
(
      TABLE_ID bigint not null, 
      COL_ID bigint not null
);