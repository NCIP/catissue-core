create or replace
procedure caller Is


      v_code  NUMBER;
      v_errm  VARCHAR2(1000);
begin 
         Begin
          FIXED_EVENT_MIGRATE('FixedEventParameters');
          NULL;
          EXCEPTION WHEN OTHERS THEN
          v_code := SQLCODE;
          v_errm := SUBSTR(SQLERRM, 1, 1000);
          DBMS_OUTPUT.PUT_LINE('exception occer''Error code ' || v_code ||' '||v_errm );
        end;
 commit;
            Begin
              Frozen_MIGRATE_CALL('FrozenEventParameters');
              NULL;
              EXCEPTION WHEN OTHERS THEN
              v_code := SQLCODE;
              v_errm := SUBSTR(SQLERRM, 1, 1000);
              DBMS_OUTPUT.PUT_LINE('exception occer''Error code ' || v_code ||' '||v_errm );
              end;
  commit;
             Begin  
                IN_OUT_EVENT_MIGRATE('CheckInCheckOutEventParameter');
                NULL;
                EXCEPTION WHEN OTHERS THEN
                v_code := SQLCODE;
                v_errm := SUBSTR(SQLERRM, 1, 1000);
                DBMS_OUTPUT.PUT_LINE('exception occer''Error code ' || v_code ||' '||v_errm );
               end;
 
  commit;
             Begin
              MOL_MIGRATE('MolecularSpecimenReviewParameters');
              NULL;
              EXCEPTION WHEN OTHERS THEN
              v_code := SQLCODE;
              v_errm := SUBSTR(SQLERRM, 1, 1000);
              DBMS_OUTPUT.PUT_LINE('exception occer''Error code ' || v_code ||' '||v_errm );
            end;
 
  commit;
  Begin
 PROCEDURE_EVENT_MIGRATE('ProcedureEventParameters');
 NULL;
              EXCEPTION WHEN OTHERS THEN
              v_code := SQLCODE;
              v_errm := SUBSTR(SQLERRM, 1, 1000);
              DBMS_OUTPUT.PUT_LINE('exception occer''Error code ' || v_code ||' '||v_errm );
            end;
commit ;
Begin
 RECIVED_EVENT_MIGRATE('ReceivedEventParameters');
 NULL;
              EXCEPTION WHEN OTHERS THEN
              v_code := SQLCODE;
              v_errm := SUBSTR(SQLERRM, 1, 1000);
              DBMS_OUTPUT.PUT_LINE('exception occer''Error code ' || v_code ||' '||v_errm );
            end;
 commit;
 Begin
 SPUN_EVENT_MIGRATE('SpunEventParameters');
 NULL;
              EXCEPTION WHEN OTHERS THEN
              v_code := SQLCODE;
              v_errm := SUBSTR(SQLERRM, 1, 1000);
              DBMS_OUTPUT.PUT_LINE('exception occer''Error code ' || v_code ||' '||v_errm );
            end;
 commit;
 Begin
 THAW_EVENT_MIGRATE('ThawEventParameters');
 NULL;
              EXCEPTION WHEN OTHERS THEN
              v_code := SQLCODE;
              v_errm := SUBSTR(SQLERRM, 1, 1000);
              DBMS_OUTPUT.PUT_LINE('exception occer''Error code ' || v_code ||' '||v_errm );
            end;
 commit;
 Begin
 TISSUE_SPECIMEN_EVENT_MIGRATE('TissueSpecimenReviewEventParameters');
 NULL;
              EXCEPTION WHEN OTHERS THEN
              v_code := SQLCODE;
              v_errm := SUBSTR(SQLERRM, 1, 1000);
              DBMS_OUTPUT.PUT_LINE('exception occer''Error code ' || v_code ||' '||v_errm );
            end;
 commit;
 Begin
 
 EMBEDDED_EVENT_MIGRATE('EmbeddedEventParameters');
 NULL;
              EXCEPTION WHEN OTHERS THEN
              v_code := SQLCODE;
              v_errm := SUBSTR(SQLERRM, 1, 1000);
              DBMS_OUTPUT.PUT_LINE('exception occer''Error code ' || v_code ||' '||v_errm );
            end;
 commit;
 Begin
 COLLECTION_EVENT_MIGRATE('CollectionEventParameters');
 NULL;
              EXCEPTION WHEN OTHERS THEN
              v_code := SQLCODE;
              v_errm := SUBSTR(SQLERRM, 1, 1000);
              DBMS_OUTPUT.PUT_LINE('exception occer''Error code ' || v_code ||' '||v_errm );
            end;
 commit;
 Begin
 CELL_EVENT_MIGRATE('CellSpecimenReviewParameters');
 NULL;
              EXCEPTION WHEN OTHERS THEN
              v_code := SQLCODE;
              v_errm := SUBSTR(SQLERRM, 1, 1000);
              DBMS_OUTPUT.PUT_LINE('exception occer''Error code ' || v_code ||' '||v_errm );
            end;
  commit;
  Begin
 Fluid_Event_migrate('FluidSpecimenReviewEventParameters');
 NULL;
              EXCEPTION WHEN OTHERS THEN
              v_code := SQLCODE;
              v_errm := SUBSTR(SQLERRM, 1, 1000);
              DBMS_OUTPUT.PUT_LINE('exception occer''Error code ' || v_code ||' '||v_errm );
            end;
  commit;
end;