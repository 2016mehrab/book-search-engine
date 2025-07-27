import {  useEffect , useState } from "react";
import { Link, useParams } from "react-router";
import BookService from "../services/BookService";
import Button from "../components/Button";

const Detail = ({ label, value }) => (
  <p>
    <span className="font-medium text-gray-800">{label}:</span>{" "}
    <span className="text-gray-600">{value}</span>
  </p>
);

const Book = () => {
  const { isbn } = useParams();

  const [book, setBook] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const bookService = BookService.getInstance();



  useEffect(() => {
    const fetchBookDetails = async () => {
      setLoading(true);
      setError(null);
      try {
        const result = await bookService.getBookByIsbn(isbn);
        setBook(result);
      } catch (e) {
        console.error(e);
        setError(e.message);
      } finally {
        setLoading(false);
      }
    };
    if (isbn) fetchBookDetails();
  }, [isbn, bookService]);

  if (loading) return <div className="p-4 text-center">Loading book details</div>;
  if (error) return <div className="p-4 text-red-500">{error}</div>;
  if (!book) {
    return (
      <div className="p-4 text-center">Book with ISBN "{isbn}" not found.</div>
    );
  }

  console.info("book", book);

  return (
    <>
      <div className="container mx-auto p-6 max-w-3xl bg-white shadow-xl rounded-xl mt-8 border border-gray-100">
        <Link to={"/"}>
          <Button label={"Back to search"} styles={"mt-2 mb-5"} />
        </Link>
        <div className="flex  items-center justify-between flex-wrap mb-4">
          <h2 className="text-4xl font-extrabold text-gray-900">
            {book.title}
          </h2>
          {book.rating && (
            <span className="text-sm bg-yellow-100 text-yellow-800 px-3 py-1 rounded-full font-medium whitespace-nowrap">
              ⭐ Rating: {book.rating}
            </span>
          )}
        </div>

        {book.authors && (
          <p className="text-lg text-gray-700 mb-4">
            <strong className="text-gray-800">Author(s): </strong>
            {book.authors.map((a, i) => (
              <span key={a.id}>
                {a.name}
                {i < book.authors.length - 1 && ", "}
              </span>
            ))}
          </p>
        )}

        <p className="text-gray-800 mb-4 leading-relaxed">
          <strong className="block text-base text-gray-900 mb-1">
            Description:
          </strong>
          <span className="text-gray-700 text-sm">
            {book.description || "No description available."}
          </span>
        </p>

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-8 gap-y-3 text-sm text-gray-700 mb-4">
          <Detail label="ISBN" value={book.isbn} />
          {book.language && <Detail label="Language" value={book.language} />}
          {book.pageCount && <Detail label="Pages" value={book.pageCount} />}
          {book.bookFormat && <Detail label="Format" value={book.bookFormat} />}
          {book.publisher && (
            <Detail label="Publisher" value={book.publisher} />
          )}
          {book.edition && <Detail label="Edition" value={book.edition} />}
          {book.publishDate && (
            <Detail
              label="Published"
              value={new Date(book.publishDate).toLocaleDateString("en-US", {
                year: "numeric",
                month: "short",
                day: "numeric",
              })}
            />
          )}
          {book.price && <Detail label="Price" value={`$${book.price}`} />}
          {book.likedPercent && (
            <Detail label="Liked" value={`${book.likedPercent}%`} />
          )}
        </div>
      </div>
    </>
  );
};

export default Book;
