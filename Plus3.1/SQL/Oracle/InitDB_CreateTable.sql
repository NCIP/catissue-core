
CREATE TABLE CATISSUE_QUERY_TABLE_DATA
(
      TABLE_ID number not null, 
      TABLE_NAME varchar(50),
      DISPLAY_NAME varchar(50),
      ALIAS_NAME varchar(50),
      PRIVILEGE_ID number(1,0),
      FOR_SQI number(1,0),                                                                        
      primary key (TABLE_ID)
);

CREATE TABLE CATISSUE_INTERFACE_COLUMN_DATA
(
      IDENTIFIER number(30) not null,
      TABLE_ID number(30) not null,
      COLUMN_NAME varchar(50),
      ATTRIBUTE_TYPE varchar(30),
      primary key (IDENTIFIER)
);


CREATE TABLE CATISSUE_TABLE_RELATION
(
      RELATIONSHIP_ID number(30),
      PARENT_TABLE_ID number(30),
      CHILD_TABLE_ID number(30),
      TABLES_IN_PATH varchar(50),
      primary key (RELATIONSHIP_ID)
);

/*  Name: Shital Lawhale Bug ID: 3549 */
/*  Description : A ATTRIBUTE_ORDER field in CATISSUE_SEARCH_DISPLAY_DATA .*/


CREATE TABLE CATISSUE_SEARCH_DISPLAY_DATA
(
      RELATIONSHIP_ID number(30) not null,
      COL_ID number(30) not null,
      DISPLAY_NAME varchar(50),
      DEFAULT_VIEW_ATTRIBUTE number(1,0),
      ATTRIBUTE_ORDER number(5,0)
);

CREATE TABLE CATISSUE_RELATED_TABLES_MAP
(
      FIRST_TABLE_ID number(30),
      SECOND_TABLE_ID number(30),
      FIRST_TABLE_JOIN_COLUMN varchar(50),
      SECOND_TABLE_JOIN_COLUMN varchar(50)
);
alter table CATISSUE_RELATED_TABLES_MAP add constraint RELATED_TABLES_KEY unique (FIRST_TABLE_ID,SECOND_TABLE_ID);
alter table CATISSUE_SEARCH_DISPLAY_DATA add constraint SEARCH_DATA_KEY unique (RELATIONSHIP_ID,COL_ID);

/*Patch ID: SimpleSearchEdit_14*/
/*Adding Metadata table required for the Simple search edit feature.*/
/*This table contains information about the columns to be hyperlinked for the given table.*/

CREATE TABLE CATISSUE_QUERY_EDITLINK_COLS
(
      TABLE_ID number(30), 
      COL_ID number(30)
);