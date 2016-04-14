# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table entrant (
  id                            bigint not null,
  name                          varchar(255),
  pool_id                       bigint,
  constraint pk_entrant primary key (id)
);
create sequence entrant_seq;

create table pool (
  id                            bigint not null,
  name                          varchar(255),
  tournament_id                 bigint,
  constraint pk_pool primary key (id)
);
create sequence pool_seq;

create table tournament (
  id                            bigint not null,
  tournament_name               varchar(255),
  game                          varchar(255),
  location                      varchar(255),
  date                          varchar(255),
  t_organizer                   varchar(255),
  platform                      varchar(255),
  tournament_detail             varchar(255),
  tournament_picture            varchar(255),
  owner_id                      bigint,
  constraint pk_tournament primary key (id)
);
create sequence tournament_seq;

create table user (
  id                            bigint not null,
  first_name                    varchar(255),
  last_name                     varchar(255),
  gamer_tag                     varchar(255),
  platform                      varchar(255),
  dob_month                     varchar(255),
  dob_day                       varchar(255),
  dob_year                      varchar(255),
  address                       varchar(255),
  city                          varchar(255),
  state                         varchar(255),
  email                         varchar(255),
  password                      varchar(255),
  icon_url                      varchar(255),
  fav_game                      varchar(255),
  game_team                     varchar(255),
  player_bio                    varchar(156466),
  token                         varchar(255),
  constraint pk_user primary key (id)
);
create sequence user_seq;

alter table entrant add constraint fk_entrant_pool_id foreign key (pool_id) references pool (id) on delete restrict on update restrict;
create index ix_entrant_pool_id on entrant (pool_id);

alter table pool add constraint fk_pool_tournament_id foreign key (tournament_id) references tournament (id) on delete restrict on update restrict;
create index ix_pool_tournament_id on pool (tournament_id);

alter table tournament add constraint fk_tournament_owner_id foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_tournament_owner_id on tournament (owner_id);


# --- !Downs

alter table entrant drop constraint if exists fk_entrant_pool_id;
drop index if exists ix_entrant_pool_id;

alter table pool drop constraint if exists fk_pool_tournament_id;
drop index if exists ix_pool_tournament_id;

alter table tournament drop constraint if exists fk_tournament_owner_id;
drop index if exists ix_tournament_owner_id;

drop table if exists entrant;
drop sequence if exists entrant_seq;

drop table if exists pool;
drop sequence if exists pool_seq;

drop table if exists tournament;
drop sequence if exists tournament_seq;

drop table if exists user;
drop sequence if exists user_seq;

