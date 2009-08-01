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