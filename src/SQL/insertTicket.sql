INSERT INTO _ticket (subject, description, createdAt,  user_id, status, level) 
VALUES 
    ('Server Down Issue', 'The main server is not responding.', '2025-01-27 10:00:00', 2, 'OPEN', 'HIGH'),
    ('Database Connection Failure', 'The application cannot connect to the database.', '2025-01-26 09:30:00',  3, 'OPEN', 'MEDIUM'),
    ('Email Not Sending', 'Outgoing emails are not being sent to customers.', '2025-01-25 14:45:00',   4, 'CLOSED', 'LOW'),
    ('Network Latency Issue', 'Users are experiencing slow loading times.', '2025-01-24 07:10:00',  5, 'OPEN', 'MEDIUM'),
    ('Bug in Payment Processing', 'Customers report failed transactions during checkout.', '2025-01-23 19:00:00', 6, 'OPEN', 'HIGH');