-- =====================================================================================================================
-- Procedures
-- =====================================================================================================================

delimiter //

drop procedure if exists createPermission;
drop procedure  if exists createRole;
drop procedure  if exists roleHasPermission;
drop procedure  if exists createAccount;
drop procedure  if exists accountHasRole;
drop procedure  if exists macHasAccount;


create procedure createPermission($name varchar(50))
begin
    insert into permission (name) values ($name);
end //

create procedure createRole($name varchar(50), out $id smallint)
begin
    insert into role (name) values ($name);
    set $id := last_insert_id();
end //

create procedure roleHasPermission($role_id smallint, $perm_name varchar(50))
begin
    declare _perm_id int;
    select id from permission where name = $perm_name into _perm_id;
    insert into role_permission (role_id, permission_id) values ($role_id, _perm_id);
end //

create procedure createAccount($id varchar(36), $phone varchar(30), $hint varchar(30))
begin
    insert into account (account_id, phone, hint, enabled, create_t) values ($id, $phone, $hint, 1, now());
    set $id := last_insert_id();
end //

create procedure accountHasRole($account_id varchar(36), $role_id smallint)
begin
    insert into account_role (account_id, role_id) values ($account_id, $role_id);
end //

create procedure createMac($mac bigint(20), out $id int)
begin
	declare _uuid varchar(36);
	select uuid() into _uuid;
    insert into mac (mac, password, create_t) values ($mac, _uuid, now());
    set $id := last_insert_id();
end //

create procedure macHasAccount($mac_id int, $account_id varchar(36))
begin
    insert into mac_account (mac_id, account_id, create_t) values ($mac_id, $account_id, now());
end //
