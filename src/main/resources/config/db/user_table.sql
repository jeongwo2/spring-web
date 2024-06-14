create table users(
      username varchar2(50) not null ,
      password varchar2(50) not null,
      enabled  char(1) default '1'
);

alter table users
add constraint pk_users primary key (username);

 create table authorities (
  username varchar2(50)  not null,
  authority varchar2(50) not null
  );

alter table authorities
add constraint fk_authorities_users foreign key (username) references users(username);

create unique index idx_auth_username on authorities (username,authority);

insert into users (username, password) values ('member00','pw00');
insert into users (username, password) values ('admin00','pw00');
insert into users (username, password) values ('user00','pw00');

insert into authorities (username, authority) values ('member00','ROLE_MANAGER');
insert into authorities (username, authority) values ('admin00','ROLE_MANAGER');
insert into authorities (username, authority) values ('admin00','ROLE_ADMIN');
insert into authorities (username, authority) values ('user00','ROLE_USER');

commit;

SELECT * FROM users ;

SELECT * FROM authorities ;


-- ▣ 기존의 테이블을 이용하는 경우

create table tbl_member(
  userid     varchar2(50) not null ,
  userpw     varchar2(100) not null,
  username   varchar2(100) not null,
  regdate    date default sysdate,
  updatedate date default sysdate,
  enabled    char(1) default '1'
);

alter table tbl_member
add constraint pk_tbl_member primary key (userid);

create table tbl_member_auth (
   userid    varchar2(50) not null,
   auth      varchar2(50) not null
);

alter table tbl_member_auth
add constraint fk_member_auth foreign key (userid) references tbl_member(userid);