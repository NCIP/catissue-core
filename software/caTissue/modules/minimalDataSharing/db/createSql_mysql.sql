SET storage_engine=InnoDB;
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS collection_protocol;
DROP TABLE IF EXISTS number_distributed;
DROP TABLE IF EXISTS reporting_period;
DROP TABLE IF EXISTS specimen_inventory;
DROP TABLE IF EXISTS specimen_type;

create table collection_protocol 
  (
        id bigint not null,
        name varchar(255),
        patients_enrolled bigint,
        patients_planned bigint,
		principanInv varchar(255),
        primary key (id)
    );

    create table number_distributed (
        id bigint not null   AUTO_INCREMENT,
        number_distributed bigint,
        specimen_type_id bigint not null,
        collection_protocol_id bigint not null,
        report_period_id bigint not null,
        primary key (id)
    );

    create table reporting_period (
        id bigint not null   AUTO_INCREMENT,
        start_date date,
        end_date date,
        primary key (id)
    );

    create table specimen_inventory (
        id bigint not null  AUTO_INCREMENT,
        specimen_count bigint,
        specimen_type_id bigint not null,
        collection_protocol_id bigint not null,
        primary key (id)
    );

    create table specimen_type (
        id bigint not null,
        name varchar(255),
        primary key (id)
    );

    alter table number_distributed
        add index FKC442065D44C9BB33 (report_period_id), 
        add constraint FKC442065D44C9BB33 
        foreign key (report_period_id) 
        references reporting_period (id);

    alter table number_distributed
        add index FKC442065D4F4DCD75 (specimen_type_id), 
        add constraint FKC442065D4F4DCD75 
        foreign key (specimen_type_id) 
        references specimen_type (id);

    alter table number_distributed
        add index FKC442065DE43A88E1 (collection_protocol_id), 
        add constraint FKC442065DE43A88E1 
        foreign key (collection_protocol_id) 
        references collection_protocol (id);

    alter table specimen_inventory 
        add index FKBA2DECE54F4DCD75 (specimen_type_id), 
        add constraint FKBA2DECE54F4DCD75 
        foreign key (specimen_type_id) 
        references specimen_type (id);

    alter table specimen_inventory 
        add index FKBA2DECE5E43A88E1 (collection_protocol_id), 
        add constraint FKBA2DECE5E43A88E1 
        foreign key (collection_protocol_id) 
        references collection_protocol (id);
	
	insert into reporting_period 
	select distinct concat(EXTRACT(YEAR from event_timestamp),ceil(EXTRACT(MONTH from event_timestamp)/3)) as year,
	'0000-00-00','0000-00-00' from @@sourcedatabase@@.catissue_distribution;

	SET FOREIGN_KEY_CHECKS=1;