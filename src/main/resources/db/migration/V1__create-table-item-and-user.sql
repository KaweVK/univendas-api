create table "user"(
    id uuid not null primary key,
    name varchar(100) not null,
    email varchar(100) not null,
    password varchar(255) not null,
    phone_number varchar(15),
    city varchar(50),
    register_date timestamp,
    update_date timestamp
);

create table item(
    id uuid not null primary key,
    name varchar(100) not null,
    description text not null,
    amount bigint not null,
    price numeric(18,2),
    category varchar(50) not null,
    id_user uuid not null references "user"(id),
    register_date timestamp,
    update_date timestamp
);