
drop user @@STAGING_DB_USER_NAME@@ cascade; 
create user @@STAGING_DB_USER_NAME@@ identified by @@STAGING_DB_PASSWORD@@ default tablespace users temporary tablespace temp; 
grant connect,resource to @@STAGING_DB_USER_NAME@@; 
commit;
exit;