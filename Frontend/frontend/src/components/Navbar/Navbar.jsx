import { Link } from "react-router-dom";
import "./Navbar.css";

export default function Navbar() {
  return (
    <nav className="navbar">
      <div className="nav-container">
        <Link to="/" className="nav-logo">LoL Stats</Link>
        <ul className="nav-links">
          <li><Link to="/champions">Champions</Link></li>
          <li><Link to="/ChampionDashboard">Champion Dashboard</Link></li>
        </ul>
      </div>
    </nav>
  );
}
