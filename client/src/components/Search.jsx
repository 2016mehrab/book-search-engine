import { forwardRef, useRef, useState } from "react";
import BookService from "../services/BookService";
import { Link } from "react-router";
import Button from "./Button";

const Search = forwardRef(({ open, openResults }, ref) => {
  const [query, setQuery] = useState("");
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [pageNum, setPageNum] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [hasSearched, setHasSearched] = useState(false);
  const scrollContainerRef = useRef(null);

  const fetchBooks = async (page = 0) => {
    try {
      setLoading(true);
      setError("");
      setHasSearched(false);
      const service = BookService.getInstance();
      const result = await service.searchBooks(query, page);
      setHasSearched(true);
      return result;
    } catch (e) {
      setError(e.message || "Something went wrong.");
      return null;
    } finally {
      setLoading(false);
    }
  };

  async function handleSubmit(e) {
    e.preventDefault();
    const result = await fetchBooks();
    if (result?.content) {
      setBooks(result.content);
      setTotalPages(result?.page?.totalPages);
      setPageNum(result?.page?.number || 0);
    } else {
      setBooks([]);
    }
    console.info(books);
    openResults();
  }

  let timeout;
  async function handleScroll() {
    clearTimeout(timeout);
    timeout = setTimeout(async () => {
      const { scrollTop, scrollHeight, clientHeight } =
        scrollContainerRef.current;
      const scrollThreshold = 150;

      if (scrollTop + clientHeight >= scrollHeight - scrollThreshold) {
        if (pageNum + 1 <= totalPages && !loading) {
          const nextPage = pageNum + 1;
          const result = await fetchBooks(nextPage);
          if (result?.content) {
            setBooks((prev) => [...prev, ...result.content]);
            setPageNum(nextPage);
          }
        }
      }
    }, 300);
  }

  console.log("page-", pageNum);

  return (
    <div className="pt-8" ref={ref}>
      <form onSubmit={handleSubmit} className=" flex items-center gap-2">
        <label htmlFor="bookSearch">Search:</label>
        <div className="relative  flex-1 max-w-2xl">
          <input
            className="pl-2 py-1 border-1 rounded-sm  w-full outline-transparent"
            type="search"
            id="bookSearch"
            placeholder="Search books using title, isbn or description..."
            value={query}
            onChange={(e) => {
              setQuery(e.target.value);
              setHasSearched(false);
              setBooks([])
            }}
          />
          <section className="absolute  w-full">
            <ul
              className=" overflow-scroll max-h-96 shadow-md"
              ref={scrollContainerRef}
              onScroll={handleScroll}
            >
              {loading && <div>Loading</div>}
              {error && <div>{error}</div>}
              {books?.length > 0 && open ? (
                books.map((b) => {
                  return (
                    <Link to={`/books/${encodeURIComponent(b.isbn)}`}>
                      <li
                        key={b.id}
                        className=" hover:bg-blue-100 border-gray-300 shadow-sm p-2 "
                      >
                        <h4
                          className="font-semibold  book-highlight-container"
                          dangerouslySetInnerHTML={{ __html: b.title }}
                        ></h4>
                        <p
                          className="text-[.9rem] book-highlight-container"
                          dangerouslySetInnerHTML={{ __html: b.description }}
                        ></p>
                        <span
                          className="text-[0.95rem] book-highlight-container"
                          dangerouslySetInnerHTML={{ __html: b.isbn }}
                        ></span>
                      </li>
                    </Link>
                  );
                })
              ) : hasSearched && (query.length > 0) && open ? (
                <div className="p-2">No books match.</div>
              ) : null}
            </ul>
          </section>
        </div>

        <Button styles={""} label={"Go"} type="submit" />
      </form>
    </div>
  );
});

Search.displayName = "Search";

export default Search;
