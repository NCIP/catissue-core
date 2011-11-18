ALTER TABLE CSM_PROTECTION_ELEMENT ADD ( ATTRIBUTE_VALUE VARCHAR2(100) );
ALTER TABLE CSM_PROTECTION_ELEMENT DROP CONSTRAINT UQ_PE_OBJ_ATT_APP_ID;

ALTER TABLE CSM_PROTECTION_ELEMENT ADD CONSTRAINT UQ_PE_OBJ_ATT_VAL_APP_ID
UNIQUE (OBJECT_ID, ATTRIBUTE, ATTRIBUTE_VALUE, APPLICATION_ID)
;

ALTER TABLE CSM_USER ADD (MIGRATED_FLAG NUMBER(1) DEFAULT 0);
ALTER TABLE CSM_USER MODIFY ( MIGRATED_FLAG NUMBER(1) NOT NULL);
ALTER TABLE CSM_USER ADD ( PREMGRT_LOGIN_NAME VARCHAR(100));
ALTER TABLE CSM_PG_PE MODIFY ( UPDATE_DATE DATE DEFAULT sysdate);
ALTER TABLE CSM_ROLE_PRIVILEGE DROP (UPDATE_DATE);
ALTER TABLE CSM_USER_PE DROP ( UPDATE_DATE );
ALTER TABLE CSM_FILTER_CLAUSE ADD ( GENERATED_SQL_GROUP VARCHAR2(4000) NOT NULL);

ALTER TABLE CSM_MAPPING ADD CONSTRAINT FK_PE_APP
FOREIGN KEY (APPLICATION_ID) REFERENCES CSM_APPLICATION (APPLICATION_ID)
ON DELETE CASCADE
;

ALTER TABLE CSM_MAPPING
ADD CONSTRAINT UQ_MP_OBJ_NAME_ATT_NAME_APP_ID UNIQUE (OBJECT_NAME,ATTRIBUTE_NAME,APPLICATION_ID)
;

ALTER TABLE CSM_APPLICATION ADD ( CSM_VERSION VARCHAR2(20));

DROP TRIGGER SET_CSM_ROLE_PRIV_UPDATE_DATE;
DROP TRIGGER SET_CSM_USER_PE_UPDATE_DATE;