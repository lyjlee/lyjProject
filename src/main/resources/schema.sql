SET search_path To mydb;


drop table if exists Account;
drop table if exists Board;
drop table if exists persistent_logins;
drop table if exists Reply;



create table Account
(
    account_id serial primary key ,
    user_id varchar(50) unique,
    user_password varchar(80),
    user_name varchar(50),
    user_email varchar(50),
    user_emailVerified boolean DEFAULT FALSE,
    user_emailCheckToken varchar(80) DEFAULT NULL,
    user_joinedAt DATE DEFAULT NULL
);

CREATE TABLE persistent_logins
(
     series varchar(64) NOT NULL primary key ,
     username varchar(64) NOT NULL,
     token varchar(64) NOT NULL,
     last_used timestamp without time zone NOT NULL default current_timestamp
);

CREATE TABLE Board
(
    board_no serial NOT NULL,
    board_title varchar(80),
    board_des text,
    user_name varchar(50),
    board_submittedAt timestamp without time zone NOT NULL
);

CREATE TABLE Reply
(
    reply_no serial NOT NULL,
    reply_level int default 1,
    reply_parent_no int default 0,
    board_no int,
    user_name varchar(50),
    reply_content varchar(64),
    reply_submittedAt timestamp without time zone NOT NULL
);

