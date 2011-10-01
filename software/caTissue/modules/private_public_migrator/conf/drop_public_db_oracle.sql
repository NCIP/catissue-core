
drop user @@PUBLIC_DB_USER_NAME@@ cascade; 
create user @@PUBLIC_DB_USER_NAME@@ identified by @@PUBLIC_DB_PASSWORD@@ default tablespace users temporary tablespace temp; 
grant connect,resource to @@PUBLIC_DB_USER_NAME@@; 
commit;
exit;