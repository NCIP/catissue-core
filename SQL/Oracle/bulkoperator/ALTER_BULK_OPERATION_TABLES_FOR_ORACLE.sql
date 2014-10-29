/*L
   Copyright Washington University in St. Louis
   Copyright SemanticBits
   Copyright Persistent Systems
   Copyright Krishagni

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
L*/

ALTER TABLE  catissue_bulk_operation ADD(CSV_TEMPLATE_TEMP CLOB);
UPDATE catissue_bulk_operation set CSV_TEMPLATE_TEMP=CSV_TEMPLATE;
ALTER TABLE  catissue_bulk_operation DROP column  CSV_TEMPLATE;
ALTER TABLE  catissue_bulk_operation ADD(CSV_TEMPLATE CLOB);
UPDATE catissue_bulk_operation set CSV_TEMPLATE=CSV_TEMPLATE_TEMP;
ALTER TABLE  catissue_bulk_operation DROP column  CSV_TEMPLATE_TEMP;
commit;

ALTER TABLE  catissue_bulk_operation ADD(XML_TEMPALTE_TEMP CLOB );
UPDATE catissue_bulk_operation set XML_TEMPALTE_TEMP=XML_TEMPALTE;
alter TABLE  catissue_bulk_operation DROP column  XML_TEMPALTE;
ALTER TABLE  catissue_bulk_operation ADD(XML_TEMPALTE CLOB);
UPDATE catissue_bulk_operation set XML_TEMPALTE=XML_TEMPALTE_TEMP;
ALTER TABLE  catissue_bulk_operation DROP column  XML_TEMPALTE_TEMP;
commit;