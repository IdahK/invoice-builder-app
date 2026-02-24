SELECT u.user_email, u.user_status, a.account_name, r.role_name 
FROM "user" u 
JOIN accounts a ON u.user_account_id = a.account_id 
JOIN user_roles ur ON u.user_id = ur.user_id 
JOIN roles r ON ur.role_id = r.role_id 
WHERE u.user_email = 'test@example.com';
