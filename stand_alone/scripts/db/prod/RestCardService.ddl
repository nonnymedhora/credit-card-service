drop database if exists bw_services_secure_prod;
create database bw_services_secure_prod;

use bw_services_secure_prod;

drop table if exists card_audit_log;
drop table if exists customer_card;
drop table if exists id_gen;

drop trigger if exists trg_update_customer_card;
drop trigger if exists trg_update_card_audit_log;

drop trigger if exists trg_insert_customer_card;
drop trigger if exists trg_insert_card_audit_log;

create table if not exists customer_card (
	card_id				varchar(128) not null,
	card_number 		mediumtext not null,
	created_by 			varchar(20) not null,
	creation_date 		timestamp not null default current_timestamp,	
	modified_by 		varchar(20),
	modification_date 	timestamp not null,
	primary key (card_id)
) engine=innodb;

create table card_audit_log (
	audit_id			integer(9) not null auto_increment,
	session_id			varchar(128) not null,
	user_id				integer(9) not null,
	card_id				varchar(128) not null,
	client_id			integer(9) not null,
	sub_client_id		integer(9),
	concierge_id		integer(9),
	customer_id			integer,
	request_type		char not null,
	created_by 			varchar(20) not null,
	creation_date 		timestamp not null default current_timestamp,	
	modified_by 		varchar(20),
	modification_date 	timestamp not null,
	primary key (audit_id),
	index card_audit_ind1 (card_id),
	index card_audit_ind2 (customer_id),
	index card_audit_ind3 (session_id),
	constraint fk_card_audit_card
	foreign key (card_id) references customer_card(card_id)
) engine=innodb;


create table id_gen (
	gen_name		varchar(128) not null,
	gen_value		integer(9) not null,
	primary key (gen_name)
) engine=innodb;

create trigger trg_insert_customer_card
before insert on customer_card for each row set new.modification_date = now();

create trigger trg_update_customer_card
before update on customer_card for each row set new.modification_date = now();

create trigger trg_insert_audit_log
before insert on card_audit_log for each row set new.modification_date = now();

create trigger trg_card_audit_log
before update on card_audit_log for each row set new.modification_date = now();