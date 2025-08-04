import { useEffect, useRef, useState } from "react";
import Search from '../components/Search';
import ErrorBoundary from "../components/ErrorBoundary";
import ErrorFallback from "../components/ErrorFallback";

const Home = () => {
  const [open, SetOpen] = useState(false);
  const searchWrapperRef = useRef(null);

  function openResults() {
    SetOpen(true);
  }

  useEffect(() => {
    function handleClickOutside(e) {
      if (
        searchWrapperRef.current &&
        !searchWrapperRef.current.contains(e.target)
      ) {
        SetOpen(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [open]);
  return (
    <div
      className="   w-full h-screen"
    >
      <div className="p-4 m-auto pt-4 max-w-3xl">
      <ErrorBoundary fallback={<ErrorFallback/>}>
        <Search ref={searchWrapperRef} openResults={openResults} open={open} /></ErrorBoundary>
      </div>
    </div>
  );
};

export default Home;
