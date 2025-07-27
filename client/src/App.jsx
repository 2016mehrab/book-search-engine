import { BrowserRouter,Routes,Route } from "react-router";
import Home from "./pages/Home";
import Book from "./pages/Book";

const App = () => {
  return (
    <BrowserRouter basename="/booksearch/">
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/books/:isbn" element={<Book />} />
      </Routes>
    </BrowserRouter>
  );
};

export default App;
