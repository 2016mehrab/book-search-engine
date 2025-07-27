import { API } from "../configs/constants";

class BookService {
  static #instance = null;
  constructor(baseUrl) {
    if (BookService.#instance) {
      throw new Error("Cannot create multiple instance");
    }
    this.baseUrl = baseUrl;
  }
  static getInstance() {
    if (!BookService.#instance) {
      BookService.#instance = new BookService(API.URL);
    }
    return BookService.#instance;
  }

  async searchBooks(query = "", page = 0, size = 5) {
    try {
      let url;
      url = `${this.baseUrl}/books`;
      if (query.length >= 0) {
        url += `?search=${encodeURIComponent(query)}`;
      }
      if (page !== 0) url += `&page=${encodeURIComponent(page)}`;
      if (size !== 5) url += `&pageSize=${encodeURIComponent(size)}`;

      const res = await fetch(url);
      if (!res.ok) {
        console.warn(res);
        throw new Error("Server Error");
      }
      const data = await res.json();
      return data;
    } catch (e) {
      console.error("error occured", e);
      throw e;
    }
  }
  async getBookByIsbn(isbn){
    try {
      const url = `${this.baseUrl}/books/${isbn}`;
      const res = await fetch(url);

      if(!res.ok){
        console.error(res);
        throw new Error(`Failed to fetch book with isbn: ${isbn}`);

      }
      const data = await res.json();

      console.log(data);
      return data;
      
    } catch (error) {
      console.error("error occured when fetching books", error.message);
      throw error;
      
    }
  }
}
export default BookService;
