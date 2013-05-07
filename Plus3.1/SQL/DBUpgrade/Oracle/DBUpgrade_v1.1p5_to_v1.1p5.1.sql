alter table CATISSUE_SPECIMEN_PROTOCOL add (LABEL_FORMAT varchar(255) default null);

alter table CATISSUE_SPECIMEN_PROTOCOL add (DERIV_LABEL_FORMAT varchar(255) default null);
alter table CATISSUE_SPECIMEN_PROTOCOL add (ALIQUOT_LABEL_FORMAT varchar(255) default null);

CREATE INDEX INDX_COLL_PROT_ID ON CATISSUE_COLL_PROT_REG (COLLECTION_PROTOCOL_ID);

CREATE TABLE KEY_SEQ_GENERATOR
   (	IDENTIFIER NUMBER(19,0) NOT NULL ENABLE,
	KEY_VALUE VARCHAR2(50 BYTE) NOT NULL ENABLE,
	KEY_SEQUENCE_ID VARCHAR2(50 BYTE) NOT NULL ENABLE,
	KEY_TYPE VARCHAR2(255 BYTE) NOT NULL ENABLE,
	 PRIMARY KEY (IDENTIFIER)
	 );
create sequence KEY_GENERATOR_SEQ;
CREATE INDEX KEY_SEQ_GENERATOR_INDEX ON KEY_SEQ_GENERATOR (KEY_VALUE);
CREATE INDEX KEY_GENERATOR_KEY_TYPE_INDEX ON KEY_SEQ_GENERATOR (KEY_TYPE);

update dyextn_role set ASSOCIATION_TYPE = 'CONTAINTMENT'
where identifier in
(
	select source_role_id from dyextn_association where identifier in
	(
		select de_association_id from intra_model_association where association_id =
		(
			select intermediate_path from path where first_entity_id =
			(
				select identifier from dyextn_abstract_metadata
				where name = 'edu.wustl.catissuecore.domain.SpecimenArrayContent'
			)
			and last_entity_id =
			(
				select identifier from dyextn_abstract_metadata
				where name = 'edu.wustl.catissuecore.domain.SpecimenArray'
			)
		)
	)
);