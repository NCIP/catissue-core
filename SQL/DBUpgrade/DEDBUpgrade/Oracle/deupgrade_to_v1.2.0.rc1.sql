CREATE TABLE DYEXTN_LABEL (
    IDENTIFIER number(19,0) not null, 
    primary key (IDENTIFIER)
);
ALTER TABLE DYEXTN_CONTROL ADD SHOW_LABEL number(1,0);
ALTER TABLE DYEXTN_CONTROL ADD yPosition number(10,0);
UPDATE DYEXTN_CONTROL set SHOW_LABEL=1;
UPDATE DYEXTN_CONTROL set YPOSITION=0;
CREATE sequence DYEXTN_FORMULA_SEQ;
CREATE TABLE DYEXTN_FORMULA (
   IDENTIFIER number(19,0) not null,
   EXPRESSION varchar(255), 
   CATEGORY_ATTRIBUTE_ID number(19,0),
   primary key (IDENTIFIER)
);
ALTER TABLE  DYEXTN_CONTROL ADD(IS_CALCULATED number(1,0) default 0);
ALTER TABLE  DYEXTN_CATEGORY_ATTRIBUTE ADD(IS_CAL_ATTRIBUTE number(1,0) default 0);
ALTER TABLE  DYEXTN_CATEGORY_ATTRIBUTE ADD(CAL_CATEGORY_ATTR_ID number(19,0));
ALTER TABLE  DYEXTN_CATEGORY_ATTRIBUTE ADD(CAL_DEPENDENT_CATEGORY_ATTR_ID number(19,0));
ALTER TABLE DYEXTN_FORMULA add constraint FK7A0DA06B41D885B234 foreign key (CATEGORY_ATTRIBUTE_ID) references DYEXTN_CATEGORY_ATTRIBUTE (IDENTIFIER);
ALTER TABLE DYEXTN_CATEGORY_ATTRIBUTE add constraint FKEF3B77585C7C8694E foreign key (CAL_CATEGORY_ATTR_ID) references DYEXTN_CATEGORY_ATTRIBUTE (IDENTIFIER);
ALTER TABLE DYEXTN_CATEGORY_ATTRIBUTE add constraint FK7A0DA606B41D885B234 foreign key (CAL_DEPENDENT_CATEGORY_ATTR_ID) references DYEXTN_CATEGORY_ATTRIBUTE (IDENTIFIER);
ALTER TABLE DYEXTN_CONTAINER ADD (PARENT_CONTAINER_ID number(19,0));
ALTER TABLE DYEXTN_LIST_BOX ADD(USE_AUTOCOMPLETE NUMBER(1,0));




/*DE metadata .*/

update DYEXTN_ABSTRACT_METADATA 
set name = 'Deprecated_EventParameters'
where name = 'edu.wustl.catissuecore.domain.EventParameters';

update DYEXTN_ABSTRACT_METADATA 
set name = 'Deprecated_AuditEventDetails'
where name = 'edu.wustl.catissuecore.domain.AuditEventDetails';


update DYEXTN_ABSTRACT_METADATA 
set name = 'Deprecated_AuditEvent'
where name = 'edu.wustl.catissuecore.domain.AuditEvent';

update DYEXTN_ABSTRACT_METADATA 
set name = 'Deprecated_AuditEventLog'
where name = 'edu.wustl.catissuecore.domain.AuditEventLog';
