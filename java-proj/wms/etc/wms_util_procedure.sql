-- =====================================================================================================================
-- Procedures
-- =====================================================================================================================

delimiter //

drop procedure if exists createPermission;
drop procedure  if exists createRole;
drop procedure  if exists roleHasPermission;
drop procedure  if exists createAccount;
drop procedure  if exists accountHasRole;


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
    insert into account (id, phone, hint, create_t) values ($id, $phone, $hint, now());
    set $id := last_insert_id();
end //

create procedure accountHasRole($account_id int, $role_id smallint)
begin
    insert into account_role (account_id, role_id) values ($account_id, $role_id);
end //
