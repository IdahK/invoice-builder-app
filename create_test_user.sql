-- Create a test user for authentication testing
INSERT INTO "user" (user_id, user_email, user_password_hash, user_display_name, user_status, user_created_at, user_account_id)
VALUES (
    gen_random_uuid(),
    'test@example.com',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj6ukx.LrUpm', -- password: TestPass123!
    'Test User',
    'ACTIVE',
    NOW(),
    (SELECT account_id FROM accounts LIMIT 1)
);

-- Assign OWNER role to the test user
INSERT INTO user_roles (user_id, role_id)
SELECT 
    u.user_id,
    r.role_id
FROM "user" u, roles r
WHERE u.user_email = 'test@example.com' AND r.role_name = 'OWNER';
