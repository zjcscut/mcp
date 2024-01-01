drop table if exists customer;
create table customer
(
    id            int primary key auto_increment,
    customer_name varchar,
    phone         varchar,
    create_time   datetime
);
drop table if exists foo;
create table foo
(
    id   int primary key auto_increment,
    name varchar
);