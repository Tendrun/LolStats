import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar/Navbar.jsx";
import Champions from "./pages/ChampionsList/ChampionsList.jsx";
import CampionDetail from "./pages/ChampionDetail/[index].tsx";
import { useEffect, useState } from "react";


function App() {
  return (
  <Router>
  <Navbar /> {/* This is always on top */}
  <div className="main-content">
    <div className="content-container">
      <Routes>
        <Route path="/Champions" element={<Champions />} />
        <Route path="/ChampionDetail/:id" element={<CampionDetail />} />
      </Routes>
    </div>
  </div>
</Router>

  );
}

export default App
