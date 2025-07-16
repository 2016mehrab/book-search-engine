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
END;
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
