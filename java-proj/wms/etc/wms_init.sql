-- Create permissions

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
call createPermission('PERM_CREATE_LOTTERY');
call createPermission('PERM_ADMIN_IHOST');

-- Create roles

call createRole('ROLE_USER', @role_user);
call roleHasPermission(@role_user, 'PERM_READ_ACCOUNTS');

call createRole('ROLE_MANAGER', @role_manager);
call roleHasPermission(@role_manager, 'PERM_CREATE_ACCOUNTS');
call roleHasPermission(@role_manager, 'PERM_READ_ACCOUNTS');
call roleHasPermission(@role_manager, 'PERM_UPDATE_ACCOUNTS');
call roleHasPermission(@role_manager, 'PERM_DELETE_ACCOUNTS');
call roleHasPermission(@role_manager, 'PERM_CREATE_TOKEN');
call roleHasPermission(@role_manager, 'PERM_CREATE_LOTTERY');

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

call createRole('ROLE_ADMIN', @role_admin);
call roleHasPermission(@role_admin, 'PERM_ADMIN_IHOST');
