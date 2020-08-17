INSERT INTO test3.t_user (id, email, mobile, name, password, surname, active) VALUES (8, 'ssadmin@gmail.com', '0980640234', 'Oleksandr', '$2a$10$FqXLGtjqfk7l9A5O0IJ6tOORSxQsjSwV.uVuuMYF2qXwkJ2B96e6G', 'Trykopa', true);
INSERT INTO test3.user_role (user_id, roles) VALUES (8, 'ROLE_ADMIN');
INSERT INTO test3.user_role (user_id, roles) VALUES (8, 'ROLE_USER');
INSERT INTO test3.user_role (user_id, roles) VALUES (8, 'ROLE_MANAGER');