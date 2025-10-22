import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar/Navbar.jsx";
import "./App.css";
import Champions from "./pages/ChampionsList/ChampionsList.jsx";
import CampionDetail from "./pages/ChampionDetail/[index].tsx";
import {
  QueryClient,
  QueryClientProvider,
} from '@tanstack/react-query'

const queryClient = new QueryClient()

function App() {
  return (
  <QueryClientProvider client={queryClient}>
    <Router>
    <Navbar />
    <div className="main-content">
      <div className="content-container">
        <Routes>
          <Route path="/Champions" element={<Champions />} />
          <Route path="/ChampionDetail/:id" element={<CampionDetail />} />
        </Routes>
      </div>
    </div>
    </Router>
</QueryClientProvider>

  );
}

export default App
