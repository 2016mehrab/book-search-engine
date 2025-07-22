import { forwardRef, useRef, useState } from "react";
import { API } from "../configs/constants";
import BookService from "../services/BookService";

const Search = forwardRef(({ open, openResults }, ref) => {
  const [query, setQuery] = useState("");
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [pageNum, setPageNum] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const scrollContainerRef = useRef(null);

  const fetchBooks = async (page = 0) => {
    try {
      setLoading(true);
      setError("");
      const service = BookService.getInstance(API.URL);
      const result = await service.searchBooks(query, page);
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
  async function handleScroll(e) {
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
    <div className=" " ref={ref}>
      <form
        onSubmit={handleSubmit}
        className="flex items-center gap-2"
      >
        <label htmlFor="bookSearch">Search:</label>
        <div className="relative  flex-1 ">
          <input
            className="pl-2 py-1 border-1 rounded-sm w-full outline-transparent"
            type="search"
            id="bookSearch"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
          <section className="absolute  w-full">
            <ul
              className="p-2 overflow-scroll max-h-96 shadow-md"
              ref={scrollContainerRef}
              onScroll={handleScroll}
            >
              {loading && <div>Loading</div>}
              {error && <div>{error}</div>}
              {books?.length > 0 && open ? (
                books.map((b) => {
                  return (
                    <li key={b.id} className=" border-gray-300 shadow-sm p-2 ">
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
                  );
                })
              ) : query.length > 0 && open ? (
                <div className="">No books match.</div>
              ) : null}
            </ul>
          </section>
        </div>

        <button
          type="submit"
          className="rounded-sm border-1 border-blue-400 px-2 py-1 hover:bg-blue-400 hover:text-white cursor-pointer duration-300"
        >
          Go
        </button>
      </form>
    </div>
  );
});

export default Search;
