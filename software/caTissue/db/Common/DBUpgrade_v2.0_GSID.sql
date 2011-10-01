--  This sql file is used to add a column to the catissue specimen table 
-- in order to integrate GSID with caTissue app.

ALTER TABLE CATISSUE_SPECIMEN ADD GLOBAL_SPECIMEN_IDENTIFIER VARCHAR(50);