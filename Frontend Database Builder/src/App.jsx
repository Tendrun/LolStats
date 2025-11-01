import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar/Navbar.jsx";
import "./App.css";
import AdminPanel from "./pages/AdminPanel/AdminPanel";
import {
  QueryClient,
  QueryClientProvider,
} from '@tanstack/react-query';
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import DatabaseBuilder from "./pages/AdminPanel/DatabaseBuilder/DatabaseBuilder.jsx";

const queryClient = new QueryClient()

function App() {
  return (
  <QueryClientProvider client={queryClient}>
    <Router>
    <Navbar />
    <div className="main-content">
      <div className="content-container">
        <Routes>
          <Route path="/admin-panel" element={<AdminPanel />} />
          <Route path="/admin-panel/database-builder" element={<DatabaseBuilder />} />
        </Routes>
      </div>
    </div>
    </Router>
</QueryClientProvider>

  );
}

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>
)

export default App
