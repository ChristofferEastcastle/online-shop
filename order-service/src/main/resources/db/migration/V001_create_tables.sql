CREATE TABLE users
(
    id       serial,
    email    varchar(100),
    password varchar(100)
);

INSERT INTO users
VALUES (default, 'test1@email.com', '$2a$12$NlF0t7nGIzcgnqQqGA1fH.9m3aSdt7uoW9uTzYsdlzv44ekechHm.')