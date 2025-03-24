import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./Components/Navbar";
import Champions from "./pages/ChampionsList/ChampionsList.jsx";
import { useEffect, useState } from "react";


function App() {
  return (
  <Router>
  <Navbar /> {/* This is always on top */}
  <div className="main-content">
    <div className="content-container">
      <Routes>
        <Route path="/champions" element={<Champions />} />
        {/* Add other routes here */}
      </Routes>
    </div>
  </div>
</Router>

  );
}

export default App
