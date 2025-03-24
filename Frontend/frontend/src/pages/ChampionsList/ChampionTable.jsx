import './ChampionTable.css';


export default function ChampionTable({ champions }) {
    return (
    <div className="table-wrapper">
      <table className="champion-table">
        <thead>
          <tr>
            <th>#</th>
            <th>Champion</th>
            <th>Role</th>
            <th>Tier</th>
            <th>Win Rate</th>
            <th>Pick Rate</th>
            <th>Ban Rate</th>
            <th>Counter Picks</th>
            <th>Matches</th>
          </tr>
        </thead>
        <tbody>
          {champions.map((champ, idx) => (
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
  