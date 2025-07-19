import { useEffect, useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./Components/Navbar";
import Champions from "./pages/ChampionsList/ChampionsList.jsx";
import ChampionDetail from "./Components/ChampionDetail.jsx";
import ChampionTable from "./pages/ChampionsList/ChampionTable.jsx";


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
