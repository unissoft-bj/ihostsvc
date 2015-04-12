call createPermission('PERM_CREATE_ACCOUNTS');
call createPermission('PERM_READ_ACCOUNTS');
call createPermission('PERM_UPDATE_ACCOUNTS');
call createPermission('PERM_DELETE_ACCOUNTS');
call createPermission('PERM_ADMIN_ACCOUNTS');

call createPermission('PERM_CREATE_RECEPTIONS');
call createPermission('PERM_READ_RECEPTIONS');
call createPermission('PERM_UPDATE_RECEPTIONS');
call createPermission('PERM_DELETE_RECEPTIONS');
call createPermission('PERM_ADMIN_RECEPTIONS');

call createPermission('PERM_CREATE_TOKEN');

-- Create roles

call createRole('ROLE_USER', @role_user);
call roleHasPermission(@role_user, 'PERM_READ_ACCOUNTS');

call createRole('ROLE_MANAGER', @role_manager);
call roleHasPermission(@role_manager, 'PERM_CREATE_ACCOUNTS');
call roleHasPermission(@role_manager, 'PERM_READ_ACCOUNTS');
call roleHasPermission(@role_manager, 'PERM_UPDATE_ACCOUNTS');
call roleHasPermission(@role_manager, 'PERM_DELETE_ACCOUNTS');
call roleHasPermission(@role_manager, 'PERM_CREATE_TOKEN');

call createRole('ROLE_SALES', @role_sales);
call roleHasPermission(@role_sales, 'PERM_CREATE_RECEPTIONS');
call roleHasPermission(@role_sales, 'PERM_READ_RECEPTIONS');
call roleHasPermission(@role_sales, 'PERM_UPDATE_RECEPTIONS');
call roleHasPermission(@role_sales, 'PERM_DELETE_RECEPTIONS');


call createRole('ROLE_SERVICE', @role_service);
call roleHasPermission(@role_service, 'PERM_CREATE_RECEPTIONS');
call roleHasPermission(@role_service, 'PERM_READ_RECEPTIONS');
call roleHasPermission(@role_service, 'PERM_UPDATE_RECEPTIONS');
call roleHasPermission(@role_service, 'PERM_DELETE_RECEPTIONS');

-- Create accounts

-- manager account
call createAccount('90b1e606-dfd3-11e4-90f4-000c29c5df73', '1382188318', 'manager bootstrap');
call accountHasRole('90b1e606-dfd3-11e4-90f4-000c29c5df73', 2)
call createMac(1096068950523, @id);
call macHasAccount(@id, '90b1e606-dfd3-11e4-90f4-000c29c5df73')




