# --- First database schema

# --- !Ups

create table "ITEM" (
  "ID" number AUTO_INCREMENT primary key,
  "MONEY" number not null,
  "KIND" text,
  "LOCATION" text
);

# --- !Downs

drop table if exists "ITEM";

