-- Create accounts

-- manager account
call createAccount('90b1e606-dfd3-11e4-90f4-000c29c5df73', '1382188318', 'manager bootstrap');
call accountHasRole('90b1e606-dfd3-11e4-90f4-000c29c5df73', 2)
call createMac(1096068950523, @id);
call macHasAccount(@id, '90b1e606-dfd3-11e4-90f4-000c29c5df73')
