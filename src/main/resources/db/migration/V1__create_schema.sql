CREATE SCHEMA public;

create table books(
                      book_id serial primary key,
                      title varchar(255) not null,
                      rating decimal(2,1) not null,
                      description text not null,
                      language varchar(255) not null,
                      isbn varchar(255) not null,
                      book_format varchar(255) not null,
                      edition varchar(255) not null,
                      pages int not null,
                      publisher varchar(255) not null,
                      publish_date date not null,
                      first_publish_date date not null,
                      liked_percent decimal(5,2) not null,
                      price decimal(5,2) not null
);

create table authors(
                        author_id serial primary key,
                        name varchar(255) not null unique
);


create table books_authors (
                               author_id int not null,
                               book_id int not null,
                               primary key (book_id, author_id),
                               foreign key (book_id) references books (book_id),
                               foreign key (author_id) references authors (author_id)
);
