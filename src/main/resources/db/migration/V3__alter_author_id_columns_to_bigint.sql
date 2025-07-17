
ALTER  table books_authors DROP constraint books_authors_book_id_fkey,
                           DROP constraint books_authors_author_id_fkey;
ALTER table authors ALTER column author_id type bigint,
    alter column author_id set default nextval('authors_author_id_seq'::regclass);

ALTER TABLE books ALTER COLUMN book_id TYPE BIGINT,
    ALTER COLUMN book_id SET DEFAULT nextval('books_book_id_seq'::regclass);

ALTER table books_authors alter column author_id type bigint,
    alter column  book_id type bigint;

ALTER TABLE books_authors
    add constraint books_authors_book_id_fkey foreign key (book_id) references  books(book_id),
    add constraint  books_authors_author_id_fkey foreign key (author_id) references authors(author_id);