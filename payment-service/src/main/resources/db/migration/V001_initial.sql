CREATE TABLE payments(
    id serial not null,
    order_id int not null,
    user_id int NOT NULL
)