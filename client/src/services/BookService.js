class BookService {
  static #instance = null;
  constructor(baseUrl) {
    if (BookService.#instance) {
      throw new Error("Cannot create multiple instance");
    }
    this.baseUrl = baseUrl;
  }
  static getInstance(baseUrl) {
    if (!BookService.#instance) {
      BookService.#instance = new BookService(baseUrl);
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
}
export default BookService;
