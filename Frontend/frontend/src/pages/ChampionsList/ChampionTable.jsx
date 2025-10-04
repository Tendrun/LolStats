import { useState } from "react";
import './ChampionTable.css';

export default function ChampionTable({ champions }) {
  const [sortConfig, setSortConfig] = useState({ key: null, direction: "asc" });

  const sortedChampions = [...champions].sort((a, b) => {
    if (!sortConfig.key) return 0;

    let aValue = a[sortConfig.key];
    let bValue = b[sortConfig.key];

    // Normalize strings and handle missing values
    if (typeof aValue === "string") aValue = aValue.toLowerCase();
    if (typeof bValue === "string") bValue = bValue.toLowerCase();

    if (aValue === undefined || aValue === null) return 1;
    if (bValue === undefined || bValue === null) return -1;

    if (aValue < bValue) return sortConfig.direction === "asc" ? -1 : 1;
    if (aValue > bValue) return sortConfig.direction === "asc" ? 1 : -1;
    return 0;
  });

  const handleSort = (key) => {
    let direction = "asc";
    if (sortConfig.key === key && sortConfig.direction === "asc") {
      direction = "desc";
    }
    setSortConfig({ key, direction });
  };

  const getSortIndicator = (key) => {
    if (sortConfig.key !== key) return "";
    return sortConfig.direction === "asc" ? " ↑" : " ↓";
  };
  

  return (
    <div className="table-wrapper">
      <table className="champion-table">
      <thead>
        <tr>
          <th onClick={() => handleSort("index")}>#</th>
          <th onClick={() => handleSort("name")}>Champion{getSortIndicator("name")}</th>
          <th onClick={() => handleSort("role")}>Role{getSortIndicator("role")}</th>
          <th onClick={() => handleSort("tier")}>Tier{getSortIndicator("tier")}</th>
          <th onClick={() => handleSort("winRate")}>Win Rate{getSortIndicator("winRate")}</th>
          <th onClick={() => handleSort("pickRate")}>Pick Rate{getSortIndicator("pickRate")}</th>
          <th onClick={() => handleSort("banRate")}>Ban Rate{getSortIndicator("banRate")}</th>
          <th onClick={() => handleSort("counterPicks")}>Counter Picks{getSortIndicator("counterPicks")}</th>
          <th onClick={() => handleSort("matches")}>Matches{getSortIndicator("matches")}</th>
        </tr>
      </thead>
        <tbody>
          {sortedChampions.map((champ, idx) => (
            <tr key={idx}>
              <td>{idx + 1}</td>
              <td>
                <img
                  src={`/ChampionIcons/${champ.image}`}
                  alt={champ.name}
                  className="champion-icon"
                />
                {champ.name}
              </td>
              <td>{champ.role || "-"}</td>
              <td>{champ.tier || "-"}</td>
              <td>{champ.winRate || "-"}</td>
              <td>{champ.pickRate || "-"}</td>
              <td>{champ.banRate || "-"}</td>
              <td>{champ.counterPicks?.join(", ") || "-"}</td>
              <td>{champ.matches || 0}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
