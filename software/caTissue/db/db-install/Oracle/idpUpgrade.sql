/** This table is to support WUSTL Key migration */
CREATE TABLE csm_migrate_user
(
 LOGIN_NAME varchar2(100 Byte) NOT NULL,
 TARGET_IDP_NAME varchar2(100 Byte),
 MIGRATED_LOGIN_NAME varchar2(100 Byte) default NULL,
 MIGRATION_STATUS varchar2(100 Byte) NOT NULL,
 primary key (LOGIN_NAME)
);