INSERT INTO public.address (created, id, updated, version, city, country, postal_code, state, street)
    VALUES ('2025-05-20 11:04:19.622927+00', 6, '2025-05-20 11:04:19.622953+00', 0, 'Casorezzo', 'Italia', '20003', NULL, 'via Piave');
INSERT INTO public.department (created, id, updated, version, code, name)
    VALUES ('2025-05-20 11:04:19.653678+00', 1, '2025-05-20 11:04:19.653689+00', 0, '123hhoih', 'dip');
INSERT INTO public.employee (address_id, created, department_id, id, updated, version, email, name, phone_number)
    VALUES (6, '2025-05-20 11:04:19.655633+00', 1, 6, '2025-05-20 11:04:19.67133+00', 1, 'salvo@salvo.it', 'Salvo', '321321231');
INSERT INTO public.project (created, id, updated, version, code, name)
    VALUES ('2025-05-20 11:04:19.658024+00', 1, '2025-05-20 11:04:19.658034+00', 0, 'pro123', 'project');
INSERT INTO public.employee_projects (employees_id, projects_id)
    VALUES (6, 1);

SELECT pg_catalog.setval('public.address_id_seq', 6, true);
SELECT pg_catalog.setval('public.address_sequence', 1, false);
SELECT pg_catalog.setval('public.department_id_seq', 1, true);
SELECT pg_catalog.setval('public.department_sequence', 1, false);
SELECT pg_catalog.setval('public.employee_id_seq', 6, true);
SELECT pg_catalog.setval('public.employee_sequence', 1, false);
SELECT pg_catalog.setval('public.project_id_seq', 1, true);
SELECT pg_catalog.setval('public.project_sequence', 1, false);
