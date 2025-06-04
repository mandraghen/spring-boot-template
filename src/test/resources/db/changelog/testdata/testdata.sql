INSERT INTO address (created, id, updated, version, city, country, postal_code, state, street)
    VALUES ('2025-05-20 11:04:19.622927+00', 1, '2025-05-20 11:04:19.622953+00', 0, 'Casorezzo', 'Italia', '20003', NULL, 'via Piave');
INSERT INTO department (created, id, updated, version, code, name)
    VALUES ('2025-05-20 11:04:19.653678+00', 1, '2025-05-20 11:04:19.653689+00', 0, '123hhoih', 'dip');
INSERT INTO employee (address_id, created, department_id, id, updated, version, email, name, phone_number)
    VALUES (1, '2025-05-20 11:04:19.655633+00', 1, 1, '2025-05-20 11:04:19.67133+00', 1, 'salvo@salvo.it', 'Salvo', '321321231');
INSERT INTO project (created, id, updated, version, code, name)
    VALUES ('2025-05-20 11:04:19.658024+00', 1, '2025-05-20 11:04:19.658034+00', 0, 'pro123', 'project');
INSERT INTO employee_projects (employees_id, projects_id)
    VALUES (1, 1);

INSERT INTO address (created, id, updated, version, city, country, postal_code, state, street)
    VALUES ('2025-05-20 11:04:19.622927+00', 2, '2025-05-20 11:04:19.622953+00', 0, 'Milano', 'Italia', '20100', NULL, 'via Po');
INSERT INTO department (created, id, updated, version, code, name)
    VALUES ('2025-05-20 11:04:19.653678+00', 2, '2025-05-20 11:04:19.653689+00', 0, '1234', 'New Department');
INSERT INTO project (created, id, updated, version, code, name)
    VALUES ('2025-05-20 11:04:19.658024+00', 2, '2025-05-20 11:04:19.658034+00', 0, '1234', 'New Project');

SELECT setval('address_id_seq', 3, true);
--SELECT pg_catalog.setval('address_sequence', 3, false);
SELECT setval('department_id_seq', 3, true);
--SELECT pg_catalog.setval('department_sequence', 3, false);
SELECT setval('employee_id_seq', 2, true);
--SELECT pg_catalog.setval('employee_sequence', 2, false);
SELECT setval('project_id_seq', 3, true);
--SELECT pg_catalog.setval('project_sequence', 3, false);
