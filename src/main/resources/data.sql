

drop table if exists CARD;
drop table if exists BANK_ACCOUNT;

create table BANK_ACCOUNT
(
    ACCOUNT_ID INT auto_increment,
    NUMBER     VARCHAR(20) not null,
    BALANCE      NUMERIC(14, 2) default 0
);

create unique index BANK_ACCOUNT_ACCOUNT_ID_UINDEX
    on BANK_ACCOUNT (ACCOUNT_ID);

alter table BANK_ACCOUNT
    add constraint BANK_ACCOUNT_PK
        primary key (ACCOUNT_ID);


create table CARD
(
    CARD_ID      INT auto_increment,
    NUMBER       VARCHAR(20) not null,
    BANK_ACCOUNT INT,
    constraint CARD_BANK_ACCOUNT_ACCOUNT_ID_FK
        foreign key (BANK_ACCOUNT) references BANK_ACCOUNT (ACCOUNT_ID)
);

create unique index CARD_CARD_ID_UINDEX
    on CARD (CARD_ID);

alter table CARD
    add constraint CARD_PK
        primary key (CARD_ID);

