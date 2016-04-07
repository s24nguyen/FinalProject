# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table user (
  id                            bigint not null,
  first_name                    varchar(255),
  last_name                     varchar(255),
  gamer_tag                     varchar(255),
  dob_month                     varchar(255),
  dob_day                       varchar(255),
  dob_year                      varchar(255),
  address                       varchar(255),
  city                          varchar(255),
  state                         varchar(255),
  email                         varchar(255),
  password                      varchar(255),
  token                         varchar(255),
  constraint pk_user primary key (id)
);
create sequence user_seq;


# --- !Downs

drop table if exists user;
drop sequence if exists user_seq;

