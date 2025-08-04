import { BrowserRouter, Routes, Route } from "react-router";
import Home from "./pages/Home";
import Book from "./pages/Book";
import ErrorBoundary from "./components/ErrorBoundary";
import ErrorFallback from "./components/ErrorFallback";

const App = () => {
  return (
    <BrowserRouter >
      <Routes>
        <Route path="/" element={<Home />} />
        <Route
          path="/books/:isbn"
          element={
            <ErrorBoundary fallback={<ErrorFallback />}>
                <Book />
            </ErrorBoundary>
          }
        />
      </Routes>
    </BrowserRouter>
  );
};

export default App;
