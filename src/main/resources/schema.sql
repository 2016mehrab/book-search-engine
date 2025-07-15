DROP SCHEMA public CASCADE;
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

-- create index on books(isbn);
CREATE UNIQUE INDEX idx_books_isbn ON books (isbn);

alter table books add column search_vector tsvector;
-- Function to update the search_vector
CREATE OR REPLACE FUNCTION update_books_search_vector() RETURNS TRIGGER AS $$
BEGIN
    NEW.search_vector :=
            setweight(to_tsvector('english', coalesce(NEW.title, '')), 'A') ||
            setweight(to_tsvector('english', coalesce(NEW.description, '')), 'B') ||
            setweight(to_tsvector('english', coalesce(NEW.isbn, '')), 'C');
    RETURN NEW;
END
$$ LANGUAGE plpgsql;

-- Trigger to call the function before insert or update on books table
CREATE TRIGGER trg_books_search_vector_update
    BEFORE INSERT OR UPDATE OF title, description, isbn ON books
    FOR EACH ROW EXECUTE FUNCTION update_books_search_vector();

update books set search_vector =
                     setweight(to_tsvector('english' ,coalesce(title,'')),'A' ) ||
                     setweight(to_tsvector('english' ,coalesce(isbn,'')),'C' ) ||
                     setweight(to_tsvector('english' ,coalesce(description,'')),'B' ) ;

create index gin_idx on books using GIN(search_vector);
-- searching
-- select title , ts_rank(search_vector, to_tsquery('english','algorithms')) as rank
-- from books
-- where search_vector @@ to_tsquery('english','algorithms')
-- order by rank desc;
-- check the GIN
-- SELECT
--     lexeme,
--     array_agg(ctid ORDER BY ctid) AS tids
-- FROM (
--          SELECT
--              ctid,
--              (unnest(search_vector)).lexeme AS lexeme
--          FROM books
--      ) AS sub
-- GROUP BY lexeme
-- ORDER BY lexeme;
