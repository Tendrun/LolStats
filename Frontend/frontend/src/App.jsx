import { useEffect, useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar.jsx";
import Champions from "./pages/championsList/ChampionsList.jsx";
import ChampionDetail from "./components/ChampionDetail.jsx";
import ChampionTable from "./pages/championsList/ChampionTable.jsx";


function App() {
  const [champions, setChampions] = useState([]);

  useEffect(() => {
    fetch("/champions.json")
      .then((res) => res.json())
      .then((data) => setChampions(data))
      .catch((err) => console.error("Error loading champions:", err));
  }, []);

  return (
    <Router>
      <Navbar />
      <div className="main-content">
        <div className="content-container">
          <Routes>
            <Route path="/" element={<ChampionTable />} />
            <Route path="/champions" element={<Champions champions={champions} />} />
            <Route path="/champions/:championName" element={<ChampionDetail champions={champions} />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
