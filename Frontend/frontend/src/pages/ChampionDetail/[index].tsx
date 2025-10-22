import { useParams } from "react-router-dom";
import { RUNESCATEGORIES } from "../../constants/runesMapping";
import './ChampionDetail.css';
import { useQuery } from "@tanstack/react-query";


export default function ChampionDetail() {
  const { id } = useParams();

  const { data, isLoading, isError } = useQuery({
    queryKey: ["champion", id],
    queryFn: fetchChampion,
    refetchOnWindowFocus: true,
    retry: 1,
  });

  return (
    <div>
      <h2>Champion Detail Page for Champion Name: {id}</h2>

      <div className="ContainerWrapper">
        <div className="StatContainer">📊 Stat 1</div>
        <div className="StatContainer">⚔️ Stat 2</div>
        <div className="StatContainer">🏆 Stat 3</div>
      </div>


      {Object.entries(RUNESCATEGORIES).map(([categoryKey, subcategories]) => (
        <div key={categoryKey} style={{ marginBottom: "40px" }}>
          <h2>🔹 {categoryKey}</h2>

          {Object.entries(subcategories).map(([subKey, runes]) => (
            <div key={subKey} style={{ marginLeft: "20px", marginBottom: "20px" }}>
              <h3>• {subKey}</h3>

              <div
                style={{
                  display: "grid",
                  gridTemplateColumns: "repeat(auto-fill, 120px)",
                  gap: "16px",
                  marginTop: "8px",
                }}
              >
                {runes.length > 0 ? (
                  runes.map((rune) => (
                    <div
                      key={rune.id}
                      style={{
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "center",
                        border: "1px solid #ccc",
                        borderRadius: "8px",
                        padding: "8px",
                      }}
                    >
                      <img
                        src={rune.src}
                        alt={rune.name}
                        style={{
                          width: "64px",
                          height: "64px",
                          objectFit: "contain",
                        }}
                      />
                      <span style={{ fontSize: "12px", marginTop: "6px" }}>
                        {rune.name}
                      </span>
                      <span style={{ fontSize: "10px", color: "#666" }}>
                        #{rune.id}
                      </span>
                    </div>
                  ))
                ) : (
                  <p style={{ color: "#999" }}>No runes in this category.</p>
                )}
              </div>
            </div>
          ))}
        </div>
      ))}
    </div>
  );
}
