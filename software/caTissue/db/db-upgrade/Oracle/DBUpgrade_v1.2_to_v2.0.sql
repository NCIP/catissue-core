-- To add unique constraint for Bugid:19485
-- alter table CSM_MIGRATE_USER add constraint MIG_USR_KEY unique (TARGET_IDP_NAME, MIGRATED_LOGIN_NAME)
/

-- Removed these columns as they are not required for more info refer bug 19643
ALTER TABLE CATISSUE_SPECIMEN drop column POSITION_DIMENSION_ONE
/
ALTER TABLE CATISSUE_SPECIMEN drop column POSITION_DIMENSION_TWO
/
ALTER TABLE CATISSUE_SPECIMEN drop column STORAGE_CONTAINER_IDENTIFIER
/
ALTER TABLE CATISSUE_CONTAINER drop column POSITION_DIMENSION_ONE
/
ALTER TABLE CATISSUE_CONTAINER drop column POSITION_DIMENSION_TWO
/
ALTER TABLE CATISSUE_CONTAINER drop column PARENT_CONTAINER_ID
/
ALTER TABLE CATISSUE_SPECIMEN_ARRAY drop column STORAGE_CONTAINER_ID
/

-- Required for model changes
alter table CATISSUE_PATHOLOGY_REPORT add SCG_ID NUMBER(19)
/
alter table CATISSUE_PATHOLOGY_REPORT add constraint fk_scgid foreign key(SCG_ID) references CATISSUE_SPECIMEN_COLL_GROUP(IDENTIFIER)
/
alter table CATISSUE_SPECIMEN_COLL_GROUP add DSPR_ID NUMBER(19)
/
alter table CATISSUE_SPECIMEN_COLL_GROUP add ISPR_ID NUMBER(19)
/
alter table CATISSUE_SPECIMEN_COLL_GROUP add constraint fk_dspr_id foreign key(DSPR_ID) references CATISSUE_DEIDENTIFIED_REPORT(IDENTIFIER)
/
alter table CATISSUE_SPECIMEN_COLL_GROUP add constraint fk_ispr_id foreign key(ISPR_ID) references CATISSUE_IDENTIFIED_REPORT(IDENTIFIER)
/
create or replace PROCEDURE CACORE_UPGRADE_PROC AS
BEGIN
  DECLARE
  CURSOR ispr_cur IS
	SELECT scg_id, ispr.IDENTIFIER
	FROM catissue_identified_report ispr, catissue_specimen_coll_group scg
	WHERE scg.IDENTIFIER= ispr.scg_id;

  CURSOR dspr_cur IS
	SELECT scg_id, dspr.IDENTIFIER
	FROM catissue_deidentified_report dspr, catissue_specimen_coll_group scg
	WHERE scg.IDENTIFIER=dspr.scg_id;

	scgId NUMBER;
	isprId NUMBER;
	dsprId NUMBER;

	BEGIN
	OPEN ispr_cur;
		FETCH ispr_cur INTO scgId, isprId;
		LOOP
			IF ispr_cur%NOTFOUND THEN
				EXIT;
			END IF;

			UPDATE catissue_pathology_report
			SET SCG_ID = scgId
			WHERE IDENTIFIER = isprId;

			UPDATE catissue_specimen_coll_group
			SET ISPR_ID = isprId
			WHERE IDENTIFIER = scgId;

			FETCH ispr_cur into scgId, isprId;
		END LOOP;
	CLOSE ispr_cur;

	OPEN dspr_cur;
		FETCH dspr_cur INTO scgId, dsprId;
		LOOP
			IF dspr_cur%NOTFOUND THEN
				EXIT;
			END IF;

			UPDATE catissue_pathology_report
			SET SCG_ID = scgId
			WHERE IDENTIFIER = dsprId;

			UPDATE catissue_specimen_coll_group
			SET DSPR_ID = dsprId
			WHERE IDENTIFIER = scgId;

			FETCH dspr_cur into scgId, dsprId;
		END LOOP;
	CLOSE dspr_cur;

	commit;
	END;
END CACORE_UPGRADE_PROC;
/
begin
 cacore_upgrade_proc();
end;
/
drop procedure "CACORE_UPGRADE_PROC"
/
alter table catissue_identified_report drop column "SCG_ID"
/
alter table catissue_deidentified_report drop column "SCG_ID"
/

-- These SQL's are for creating SPP related tables and coresponding changes in the model for SPP
create table catissue_spp (IDENTIFIER number(19,0), NAME varchar(50) unique, BARCODE varchar(50) unique,spp_template_xml CLOB,  primary key (IDENTIFIER))
/
create table catissue_abstract_application (IDENTIFIER number(19,0), REASON_DEVIATION varchar(4000), TIMESTAMP timestamp, USER_DETAILS number(19,0), COMMENTS varchar(4000), primary key (IDENTIFIER), foreign key (USER_DETAILS) references catissue_user (IDENTIFIER))
/
create table catissue_default_action (IDENTIFIER number(19,0), PRIMARY KEY (IDENTIFIER), foreign key (IDENTIFIER) references dyextn_abstract_form_context (IDENTIFIER))
/
create table catissue_spp_application (IDENTIFIER number(19,0), SPP_IDENTIFIER number(19,0), SCG_IDENTIFIER number(19,0), primary key (IDENTIFIER), foreign key (IDENTIFIER) references  catissue_abstract_application (IDENTIFIER) ,foreign key (SPP_IDENTIFIER) references catissue_spp (IDENTIFIER), foreign key (SCG_IDENTIFIER) references catissue_specimen_coll_group (IDENTIFIER))
/
create table catissue_action_application (IDENTIFIER number(19,0), SPP_APP_IDENTIFIER number(19,0), SPECIMEN_ID number(19,0), SCG_ID number(19,0), primary key (IDENTIFIER), foreign key (IDENTIFIER) references catissue_abstract_application (IDENTIFIER), foreign key (SPP_APP_IDENTIFIER) references catissue_spp_application (IDENTIFIER), foreign key (SPECIMEN_ID) references catissue_specimen (IDENTIFIER), foreign key (SCG_ID) references catissue_specimen_coll_group (IDENTIFIER))
/
create table catissue_action_app_rcd_entry (IDENTIFIER number(19,0), ACTION_APP_ID number(19,0), primary key (IDENTIFIER), foreign key (ACTION_APP_ID) references catissue_action_application (IDENTIFIER))
/
create table catissue_action (IDENTIFIER number(19,0), BARCODE varchar(50), ACTION_ORDER number(19,0), ACTION_APP_RECORD_ENTRY_ID number(19,0), SPP_IDENTIFIER number(19,0), UNIQUE_ID varchar(50) not null, IS_SKIPPED number(1,0) default 0, primary key (IDENTIFIER), foreign key (ACTION_APP_RECORD_ENTRY_ID) references catissue_action_app_rcd_entry (IDENTIFIER), foreign key (SPP_IDENTIFIER) references catissue_spp (IDENTIFIER))
/
ALTER TABLE catissue_action ADD CONSTRAINT spp_unique_id UNIQUE (SPP_IDENTIFIER,UNIQUE_ID)
/
create table catissue_cpe_spp (cpe_identifier number(19,0), spp_identifier number(19,0),CONSTRAINT catissue_cpe_spp_1 FOREIGN KEY (cpe_identifier) REFERENCES catissue_coll_prot_event (IDENTIFIER),CONSTRAINT catissue_cpe_spp_2 FOREIGN KEY (spp_identifier) REFERENCES catissue_spp (IDENTIFIER))
/
alter table catissue_action_application add (ACTION_IDENTIFIER number(19,0), ACTION_APP_RECORD_ENTRY_ID number(19,0), foreign key (ACTION_IDENTIFIER) references catissue_action (IDENTIFIER), foreign key (ACTION_APP_RECORD_ENTRY_ID) references catissue_action_app_rcd_entry (IDENTIFIER))
/
alter table catissue_cp_req_specimen add (SPP_IDENTIFIER number(19,0), ACTION_IDENTIFIER number(19,0), foreign key (SPP_IDENTIFIER) references catissue_spp (IDENTIFIER), foreign key (ACTION_IDENTIFIER) references catissue_action (IDENTIFIER))
/
alter table catissue_specimen add (SPP_APPLICATION_ID number(19,0), ACTION_APPLICATION_ID number(19,0), foreign key (SPP_APPLICATION_ID) references catissue_spp_application (IDENTIFIER), foreign key (ACTION_APPLICATION_ID) references catissue_action_application (IDENTIFIER))
/
create sequence CATISSUE_ABS_APPL_SEQ
/
create sequence CATISSUE_SPP_SEQ
/
-- SQL's for SPP tables creation end

--SQL's for inserting SPP metadata for simple search
INSERT INTO CATISSUE_QUERY_TABLE_DATA(TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID, FOR_SQI)  VALUES(103,'catissue_spp','Specimen Processing Procedure','SpecimenProcessingProcedure',2,1)
/
INSERT INTO CATISSUE_TABLE_RELATION( RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) VALUES(148,103,103)
/
INSERT INTO CATISSUE_INTERFACE_COLUMN_DATA( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) VALUES(346,103,'IDENTIFIER', 'bigint')
/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME, DEFAULT_VIEW_ATTRIBUTE, ATTRIBUTE_ORDER) VALUES ( (select max(RELATIONSHIP_ID) FROM CATISSUE_TABLE_RELATION), (SELECT max(IDENTIFIER) FROM CATISSUE_INTERFACE_COLUMN_DATA), 'Identifier',1,1)
/
INSERT INTO CATISSUE_INTERFACE_COLUMN_DATA( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) VALUES(347,103,'NAME', 'varchar')
/
INSERT INTO CATISSUE_QUERY_EDITLINK_COLS VALUES((SELECT max(TABLE_ID) FROM CATISSUE_QUERY_TABLE_DATA), (SELECT max(IDENTIFIER) FROM CATISSUE_INTERFACE_COLUMN_DATA))
/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME, DEFAULT_VIEW_ATTRIBUTE, ATTRIBUTE_ORDER) VALUES ((select max(RELATIONSHIP_ID) FROM CATISSUE_TABLE_RELATION), (SELECT max(IDENTIFIER) FROM CATISSUE_INTERFACE_COLUMN_DATA), 'SPP Name',1,2)
/
INSERT INTO CATISSUE_INTERFACE_COLUMN_DATA( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) VALUES(348,103,'BARCODE', 'varchar')
/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME, DEFAULT_VIEW_ATTRIBUTE, ATTRIBUTE_ORDER) VALUES ((select max(RELATIONSHIP_ID) FROM CATISSUE_TABLE_RELATION), (SELECT max(IDENTIFIER) FROM CATISSUE_INTERFACE_COLUMN_DATA), 'Barcode',1,3)
/
-- SQLs for Grid Grouper integration
create table CATISSUE_CP_GRID_PRVG (
   IDENTIFIER NUMBER(20) NOT NULL,
   GROUP_NAME VARCHAR2(255) NOT NULL,
   STEM_NAME VARCHAR2(255) NOT NULL,
   PRIVILEGES_STRING VARCHAR2(255),
   STATUS VARCHAR2(255),
   COLLECTION_PROTOCOL_ID number(19,0) NOT NULL,
   primary key (IDENTIFIER),
   CONSTRAINT FK_GRID_GRP_COLPROT FOREIGN KEY (COLLECTION_PROTOCOL_ID) REFERENCES CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER)
)
/
create sequence CATISSUE_CP_GRID_PRVG_SEQ
/
-- SQLs for Grid Grouper integration end
-- SQLs to make GSID querieable
INSERT INTO CATISSUE_INTERFACE_COLUMN_DATA( IDENTIFIER, TABLE_ID, COLUMN_NAME , ATTRIBUTE_TYPE ) VALUES(349,33,'GLOBAL_SPECIMEN_IDENTIFIER', 'varchar')
/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME, DEFAULT_VIEW_ATTRIBUTE, ATTRIBUTE_ORDER) VALUES ((select RELATIONSHIP_ID FROM CATISSUE_TABLE_RELATION WHERE PARENT_TABLE_ID = (SELECT TABLE_ID FROM CATISSUE_QUERY_TABLE_DATA WHERE TABLE_NAME = 'CATISSUE_SPECIMEN') AND CHILD_TABLE_ID = (SELECT TABLE_ID FROM CATISSUE_QUERY_TABLE_DATA WHERE TABLE_NAME = 'CATISSUE_SPECIMEN')), (SELECT max(IDENTIFIER) FROM CATISSUE_INTERFACE_COLUMN_DATA), 'GSID',1,3)
/
-- SQLs to make GSID querieable ends
/
-- Changes For SCG Age AT Collection 
ALTER TABLE catissue_specimen_coll_group ADD (AGE_AT_COLLECTION NUMBER(6,2))
/
-- For Update Encounter Time Stamp:
	UPDATE catissue_specimen_coll_group scg 
	set ENCOUNTER_TIMESTAMP =
	(select max(EVENT_TIMESTAMP) from catissue_specimen_event_param sep,
	catissue_coll_event_param cep
	where sep.SPECIMEN_COLL_GRP_ID = scg.identifier and
	sep.identifier = cep.identifier
	)
/
-- For Update Encounter Time Stamp:
	UPDATE catissue_specimen_coll_group scg
	set AGE_AT_COLLECTION=(select round(((scg.ENCOUNTER_TIMESTAMP-part.BIRTH_DATE)/365),0)
		from catissue_coll_prot_reg cpr ,catissue_participant part
		where scg.COLLECTION_PROTOCOL_REG_ID=cpr.identifier
			AND
		part.identifier=cpr.PARTICIPANT_ID
		AND
		part.BIRTH_DATE IS NOT NULL
		AND
		scg.ENCOUNTER_TIMESTAMP IS NOT NULL
	)
/
Alter table QUERY_PARAMETERIZED_QUERY add SHOW_TREE number(1,0) default 0
/
 Insert into catissue_permissible_value(Identifier,PARENT_IDENTIFIER,VALUE) select (select max(identifier)+1 from catissue_permissible_value), 3,'Buffy Coat' from dual where not exists (select * from catissue_permissible_value where PARENT_IDENTIFIER=3 and (Value like 'Buffy Coat' or Value like 'buffy coat'))
/