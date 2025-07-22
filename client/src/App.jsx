import { useEffect, useRef, useState } from "react";
import Search from "./components/Search";

const App = () => {
  const [open, SetOpen] = useState(false);
  const searchWrapperRef = useRef(null);
  function handleSearchResultOpen(e) {
    SetOpen(false);
    console.log(e.target);
  }
  function openResults() {
    SetOpen(true);
  }
  useEffect(()=>{
    function handleClickOutside(e){
      console.log("handleClickOutside",e.target)
      if(searchWrapperRef.current && !searchWrapperRef.current.contains(e.target)){
      console.log("Inside condition")
        SetOpen(false);
      }
    }
     document.addEventListener('mousedown', handleClickOutside); 

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  },[open])

  return (
    <div
    
      // onClick={handleSearchResultOpen}
      className="   w-full h-screen"
    >
      <div className="p-4 m-auto pt-4 w-2/3 ">
        <Search ref={searchWrapperRef} openResults={openResults} open={open} />
      </div>
    </div>
  );
};

export default App;
