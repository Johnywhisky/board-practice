use scit;
drop database boards;
create database boards;
use boards;

create table users(
    user_id		varchar(25)		unique not null,
    user_pwd	varchar(255)	not null,
    name	varchar(25)		not null,
    email		varchar(50)		unique,
    roles		varchar(10)		default "ROLE_USER",
    enabled 	char(1)			default "1",
    
    constraint	pk_users_id			primary key (user_id),
    constraint	chk_users_role		check (roles in ("ROLE_USER", "ROLE_ADMIN")),
    constraint	chk_users_enabled	check (enabled in ("0", "1"))
);

create table boards(
	seq_no		bigint			auto_increment,
    writer		varchar(25),
    title		varchar(50)		not null,
    content		varchar(4000)	not null,
    original_file_name varchar(511),
    saved_file_name varchar(511),
    hit_count	int				default	0,
    created_at	datetime		default	current_timestamp,
    updated_at	datetime		default	current_timestamp,

    constraint pk_boards_seq_no	primary key (seq_no)
);

create table replys(
	seq_no			bigint			auto_increment,
    board_seq_no	bigint,
    writer			varchar(50)		not null,
    content			varchar(125)	not null,
    created_at		datetime		default current_timestamp,
    
    constraint	pk_replys_seq_no		primary key (seq_no),
    constraint	fk_replys_board_seq_no	foreign key (board_seq_no) references boards(seq_no) on delete cascade
);

DELIMITER $$
create trigger `auto_hot_board`
before delete on users
for each row
begin
	update boards set writer = '알 수 없는 사용자' where new.writer = old.user_id;
end $$
DELIMITER ;

commit;
select * from users;
select * from boards;
select * from replys;