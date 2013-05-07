DROP SEQUENCE collection_protocol_SEQ;
DROP SEQUENCE specimen_inventory_SEQ;
DROP SEQUENCE number_distributed_SEQ;
DROP SEQUENCE specimen_type_SEQ;
DROP SEQUENCE REPORTING_PERIOD_SEQ;

DROP TABLE COLLECTION_PROTOCOL cascade constraints;
DROP TABLE number_distributed cascade constraints;
DROP TABLE specimen_inventory cascade constraints;
DROP TABLE specimen_type cascade constraints;
DROP TABLE reporting_period cascade constraints;

create table collection_protocol 
  (
        id number(19,0) not null,
        name varchar(255),
        patients_enrolled number(19,0),
        patients_planned number(19,0),
		principanInv varchar(255),
        primary key (id)
    );

    create table number_distributed (
        id number(19,0) not null,
        number_distributed number(19,0),
        specimen_type_id number(19,0) not null,
        collection_protocol_id number(19,0) not null,
        report_period_id number(19,0) not null,
        primary key (id)
    );

    create table reporting_period (
        id number(19,0) not null,
        start_date date,
        end_date date,
        primary key (id)
    );

    create table specimen_inventory (
		id number(19,0) not null,
        specimen_count number(19,0),
        specimen_type_id number(19,0) not null,
        collection_protocol_id number(19,0) not null,
        primary key (id)
    );

    create table specimen_type (
        id number(19,0) not null,
        name varchar(255),
        primary key (id)
    );

	ALTER TABLE NUMBER_DISTRIBUTED
	ADD constraint FKC442065D44C9BB33      
    foreign key (report_period_id) references REPORTING_PERIOD (id);

    alter table number_distributed
    add constraint FKC442065D4F4DCD75 
    foreign key (specimen_type_id) references specimen_type (id);

    alter table number_distributed
    add constraint FKC442065DE43A88E1 
    foreign key (collection_protocol_id) 
    references collection_protocol (id);

    alter table specimen_inventory 
    add constraint FKBA2DECE54F4DCD75 
    foreign key (specimen_type_id) 
    references specimen_type (id);

    alter table specimen_inventory 
    add constraint FKBA2DECE5E43A88E1 
    foreign key (collection_protocol_id) 
    references collection_protocol (id);
	
	create sequence collection_protocol_SEQ;
	create sequence specimen_inventory_SEQ;
	create sequence number_distributed_SEQ;
	create sequence specimen_type_SEQ;
	create sequence REPORTING_PERIOD_SEQ;

	INSERT INTO reporting_period SELECT DISTINCT CONCAT(EXTRACT(YEAR FROM event_timestamp),CEIL(EXTRACT(MONTH FROM event_timestamp)/3)),
	TO_DATE('2008-01-01','YYYY-MM-DD'),TO_DATE('2008-01-01','YYYY-MM-DD') FROM @@sourcedatabase@@.catissue_distribution;	