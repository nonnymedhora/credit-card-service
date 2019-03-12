drop database if exists bw_services_secure_test;
create database bw_services_secure_test;

use bw_services_secure_test;

drop table if exists card_audit_log;
drop table if exists customer_card;
drop table if exists id_gen;

create table if not exists customer_card (
	card_id				varchar(128) not null,
	card_number 		mediumtext not null,
	created_by 			varchar(20) not null,
	creation_date 		timestamp not null default current_timestamp,	
	modified_by 		varchar(20),
	modification_date 	timestamp not null default current_timestamp,
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
	modification_date 	timestamp not null default current_timestamp,
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
