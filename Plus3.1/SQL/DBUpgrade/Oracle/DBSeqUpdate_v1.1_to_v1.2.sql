declare
    max_number number;
begin
    select max(PROTECTION_GROUP_ID) into max_number from   CSM_PROTECTION_GROUP ;
    max_number := max_number+1;
    execute immediate
    'drop sequence CSM_PROTECTIO_PROTECTION_G_SEQ';

    execute immediate
    'create sequence CSM_PROTECTIO_PROTECTION_G_SEQ start with '||max_number;
end;
