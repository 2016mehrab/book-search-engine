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
      console.log("handleClickOutside", e.target);
      if (
        searchWrapperRef.current &&
        !searchWrapperRef.current.contains(e.target)
      ) {
        console.log("Inside condition");
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
      <div className="p-4 m-auto pt-4 w-2/3 ">
      <ErrorBoundary fallback={<ErrorFallback/>}>
        <Search ref={searchWrapperRef} openResults={openResults} open={open} /></ErrorBoundary>
      </div>
    </div>
  );
};

export default Home;
