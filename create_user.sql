-- Create user with BCrypt password (password: 'password123')
INSERT INTO "user" (user_id, user_email, user_password_hash, user_email_verified, user_status, user_account_id, user_created_at) 
VALUES (
    gen_random_uuid(), 
    'test@example.com', 
    '$2a$12$dgMFbgJ7F84zENOhpwIsFulOCnIuAyEq9y1W6.hFn5GhscHO6LRfK',
    true, 
    'ACTIVE', 
    '9e5f8a5e-def4-4617-a176-494113af4b0b',
    NOW()
) RETURNING user_id;

-- Assign USER role to the user
INSERT INTO user_roles (user_id, role_id) 
SELECT u.user_id, r.role_id 
FROM "user" u, roles r 
WHERE u.user_email = 'test@example.com' AND r.role_name = 'USER';
