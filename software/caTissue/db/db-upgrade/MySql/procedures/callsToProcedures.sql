drop procedure if exists  caller;
//
create procedure  caller()
begin 
call  cell_call_parameter();
select'done cell_call_parameter';
call  Collection_Event_migrate();
select'done Collection_Event_migrate';
call  Embedded_call_parameter();
select'done Embedded_call_parameter';
call  fix_call_parameter();
select'done fix_call_parameter';
call  Fluid_call_parameter();
select'done Fluid_call_parameter';
call  Frozen_call_parameter();
select'done Frozen_call_parameter';
call  in_out_call_parameter();
select'done in_out_call_parameter';
call  mole_Event_migrate();
select'done mole_Event_migrate';
call  Procedure_Event_migrate();
select'done Procedure_Event_migrate';
call  Received_call_parameter();
select'done Received_call_parameter';
call  spun_Event_migrate();
select'done spun_Event_migrate';
call  thaw_call_parameter();
select'done thaw_call_parameter';
call  Tissue_Event_migrate();
select'done Tissue_Event_migrate';
end;
//