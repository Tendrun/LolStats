import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
} from "chart.js";
import { Bar } from "react-chartjs-2";
import { getAllChampions } from "@/lib/api/ChampionApi";
import { useEffect, useState, useMemo } from "react";
import { ChampionStats } from "@/components/ChampionMap/ChampionMapping";

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const options = {
  responsive: true,
  scales: { y: { beginAtZero: true } }
};

export default function ChampionDashboard() {
  const [champions, setChampions] = useState<ChampionStats[]>();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const controller = new AbortController();
    setLoading(true);
    setError(null);

    getAllChampions() // only if your API accepts signal; otherwise omit
      .then((data) => {
        setChampions(data);
      })
      .catch((err) => {
        if (err.name !== "AbortError") {
          setError(err.message || "Failed to fetch champions");
        }
      })
      .finally(() => {
        setLoading(false);
      });

    return () => {
      controller.abort(); // cancel fetch on unmount
    };
  }, []);

  // derive chart data from champions, memoized so it only recomputes when champions change
  const chartData = useMemo(() => {
    if (!champions || champions.length === 0) {
      // fallback sample data while loading / if no data
      return {
        ["empty labels"]: [],
        datasets: [
          {
            label: "Brak danych",
            data: [0, 0, 0, 0, 0, 0, 0],
            backgroundColor: "rgba(0,0,0,0.1)",
            borderColor: "rgba(0,0,0,0.2)",
            borderWidth: 1
          }
        ]
      };
    }

    // Use a numeric champion metric for the chart. `ChampionStats` exposes `totalMatchesPicked` (number).
    // Build labels from champion names and data from totalMatchesPicked so types line up with Chart.js (number[]).
    const champLabels = champions.map((c) => c.name);
    const values: number[] = champions.map((c) => Number(c.totalMatchesPicked ?? 0));

    // simple color palette (repeat if needed)
    const colors = champions.map((_, i) => {
      const base = [54, 162, 235];
      const alpha = 0.6 - Math.min(i * 0.01, 0.45);
      return `rgba(${base[0]},${base[1]},${base[2]},${alpha})`;
    });

    return {
      labels: champLabels,
      datasets: [
        {
          label: "Total Matches Picked",
          data: values,
          backgroundColor: colors,
          borderColor: "rgb(54,162,235)",
          borderWidth: 1
        }
      ]
    };
  }, [champions]);

  if (loading) return <div className="p-6">Loading champions…</div>;
  if (error) return <div className="p-6 text-red-600">Error: {error}</div>;

  return (
    <div className="p-6 max-w-lg mx-auto">
      <h2 className="text-xl font-semibold mb-4">Champion Dashboard</h2>
      <div style={{ width: 1420, height: 420 }}>
        <Bar data={chartData} options={{ ...options, maintainAspectRatio: false }} />
      </div>
    </div>
  );
}