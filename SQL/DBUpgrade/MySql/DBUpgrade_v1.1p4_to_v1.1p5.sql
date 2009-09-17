/* Adding WUSTLKEY column in catissue_user table*/
ALTER TABLE CATISSUE_USER ADD COLUMN WUSTLKEY varchar(100) UNIQUE;