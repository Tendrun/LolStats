import React, { useMemo, useState } from "react";
import { motion } from "framer-motion";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  ResponsiveContainer,
  Line,
  LineChart,
  CartesianGrid,
  Radar,
  RadarChart,
  PolarGrid,
  PolarAngleAxis,
  PolarRadiusAxis,
} from "recharts";

// --- Mock data (replace with your API data) ---
const champions = [
  { name: "Ahri", win: 52.1, pick: 8.2, ban: 2.3, kda: 3.4, dmg: 7.1, obj: 6.3 },
  { name: "Yasuo", win: 49.3, pick: 6.7, ban: 15.2, kda: 2.1, dmg: 8.0, obj: 5.0 },
  { name: "Lux", win: 53.9, pick: 7.5, ban: 1.8, kda: 3.7, dmg: 6.4, obj: 6.0 },
  { name: "Zed", win: 47.8, pick: 5.9, ban: 12.3, kda: 2.3, dmg: 8.6, obj: 4.7 },
  { name: "Garen", win: 51.0, pick: 5.2, ban: 3.3, kda: 2.9, dmg: 5.1, obj: 7.1 },
  { name: "Kai'Sa", win: 50.6, pick: 11.2, ban: 4.1, kda: 3.1, dmg: 7.4, obj: 6.8 },
  { name: "Jinx", win: 51.8, pick: 10.4, ban: 2.5, kda: 3.2, dmg: 7.0, obj: 6.6 },
  { name: "Darius", win: 49.9, pick: 7.9, ban: 7.2, kda: 2.2, dmg: 6.9, obj: 6.2 },
  { name: "Leblanc", win: 48.2, pick: 4.4, ban: 9.5, kda: 2.6, dmg: 8.1, obj: 4.3 },
  { name: "Morgana", win: 52.7, pick: 6.0, ban: 1.1, kda: 3.5, dmg: 6.2, obj: 6.7 },
];

const matchesPerDay = [
  { day: "Mon", matches: 820 },
  { day: "Tue", matches: 940 },
  { day: "Wed", matches: 1010 },
  { day: "Thu", matches: 880 },
  { day: "Fri", matches: 1150 },
  { day: "Sat", matches: 1380 },
  { day: "Sun", matches: 1240 },
];

const regions = ["EUNE", "EUW", "NA", "KR", "BR"];
const ranks = ["All", "Iron", "Bronze", "Silver", "Gold", "Platinum", "Diamond", "Master+ "];

function KpiCard({ title, value, hint }) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.25 }}
      className="rounded-2xl shadow p-4 bg-white/80 dark:bg-neutral-900/80 backdrop-blur border border-neutral-200 dark:border-neutral-800"
    >
      <div className="text-sm text-neutral-500 dark:text-neutral-400">{title}</div>
      <div className="text-3xl font-semibold mt-1">{value}</div>
      {hint && <div className="text-xs text-neutral-400 mt-2">{hint}</div>}
    </motion.div>
  );
}

function Select({ label, value, onChange, options }) {
  return (
    <label className="flex items-center gap-2 text-sm text-neutral-600 dark:text-neutral-300">
      <span className="min-w-16">{label}</span>
      <select
        className="px-3 py-2 rounded-xl border border-neutral-200 dark:border-neutral-700 bg-white dark:bg-neutral-900"
        value={value}
        onChange={(e) => onChange(e.target.value)}
      >
        {options.map((opt) => (
          <option key={opt} value={opt}>
            {opt}
          </option>
        ))}
      </select>
    </label>
  );
}

export default function ChampionDashboard() {
  const [region, setRegion] = useState("EUNE");
  const [rank, setRank] = useState("All");
  const [selectedChampion, setSelectedChampion] = useState(champions[0].name);

  // In real app, useQuery(...) with [region, rank] and fetch your API.
  const kpis = useMemo(() => {
    const totalMatches = matchesPerDay.reduce((a, b) => a + b.matches, 0);
    const avgWin = (
      champions.reduce((a, b) => a + b.win, 0) / champions.length
    ).toFixed(1);
    return [
      { title: "Matches processed (7d)", value: totalMatches.toLocaleString() },
      { title: "Champions tracked", value: champions.length },
      { title: "Avg. Win Rate", value: `${avgWin}%` },
      { title: "Regions", value: regions.length },
    ];
  }, []);

  const topByWin = useMemo(() => {
    return [...champions]
      .sort((a, b) => b.win - a.win)
      .slice(0, 7);
  }, []);

  const selected = champions.find((c) => c.name === selectedChampion) || champions[0];
  const radarData = [
    { metric: "KDA", value: selected.kda },
    { metric: "Damage", value: selected.dmg },
    { metric: "Objectives", value: selected.obj },
    { metric: "Pick%", value: selected.pick },
    { metric: "Ban%", value: selected.ban },
  ];

  return (
    <div className="min-h-screen w-full bg-gradient-to-b from-neutral-50 to-white dark:from-neutral-950 dark:to-neutral-900 text-neutral-900 dark:text-neutral-100 p-6">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-6">
        <div>
          <h1 className="text-2xl md:text-3xl font-bold">LOL Stats – Admin Dashboard</h1>
          <p className="text-neutral-500 dark:text-neutral-400">Region & Rank scoped insights</p>
        </div>
        <div className="flex items-center gap-3">
          <Select label="Region" value={region} onChange={setRegion} options={regions} />
          <Select label="Rank" value={rank} onChange={setRank} options={ranks} />
        </div>
      </div>

      {/* KPI cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
        {kpis.map((k) => (
          <KpiCard key={k.title} title={k.title} value={k.value} hint={k.hint} />
        ))}
      </div>

      {/* Charts Grid */}
      <div className="grid grid-cols-1 xl:grid-cols-3 gap-6">
        {/* Top by Win Rate */}
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.25, delay: 0.05 }}
          className="col-span-1 xl:col-span-2 rounded-2xl shadow bg-white/80 dark:bg-neutral-900/80 backdrop-blur border border-neutral-200 dark:border-neutral-800 p-4"
        >
          <div className="flex items-center justify-between mb-3">
            <h2 className="text-lg font-semibold">Top Champions by Win Rate</h2>
            <span className="text-sm text-neutral-500">(mock data)</span>
          </div>
          <div className="h-72">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={topByWin} margin={{ top: 10, right: 10, left: 0, bottom: 0 }}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis unit="%" />
                <Tooltip />
                <Legend />
                <Bar dataKey="win" name="Win Rate" />
                <Bar dataKey="pick" name="Pick Rate" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </motion.div>

        {/* Matches per day */}
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.25, delay: 0.1 }}
          className="rounded-2xl shadow bg-white/80 dark:bg-neutral-900/80 backdrop-blur border border-neutral-200 dark:border-neutral-800 p-4"
        >
          <div className="flex items-center justify-between mb-3">
            <h2 className="text-lg font-semibold">Matches processed per day</h2>
            <span className="text-sm text-neutral-500">(last 7 days)</span>
          </div>
          <div className="h-72">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={matchesPerDay} margin={{ top: 10, right: 10, left: 0, bottom: 0 }}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="day" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line type="monotone" dataKey="matches" name="Matches" dot={false} />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </motion.div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mt-6">
        {/* Champion selector + Radar */}
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.25, delay: 0.15 }}
          className="rounded-2xl shadow bg-white/80 dark:bg-neutral-900/80 backdrop-blur border border-neutral-200 dark:border-neutral-800 p-4 lg:col-span-2"
        >
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 mb-3">
            <h2 className="text-lg font-semibold">Champion radar – relative metrics</h2>
            <select
              className="px-3 py-2 rounded-xl border border-neutral-200 dark:border-neutral-700 bg-white dark:bg-neutral-900"
              value={selectedChampion}
              onChange={(e) => setSelectedChampion(e.target.value)}
            >
              {champions.map((c) => (
                <option key={c.name} value={c.name}>
                  {c.name}
                </option>
              ))}
            </select>
          </div>

          <div className="h-80">
            <ResponsiveContainer width="100%" height="100%">
              <RadarChart data={radarData} outerRadius={110}>
                <PolarGrid />
                <PolarAngleAxis dataKey="metric" />
                <PolarRadiusAxis angle={30} domain={[0, 12]} />
                <Radar name={selectedChampion} dataKey="value" />
                <Legend />
                <Tooltip />
              </RadarChart>
            </ResponsiveContainer>
          </div>
        </motion.div>

        {/* Notes / Audit */}
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.25, delay: 0.2 }}
          className="rounded-2xl shadow bg-white/80 dark:bg-neutral-900/80 backdrop-blur border border-neutral-200 dark:border-neutral-800 p-4"
        >
          <h2 className="text-lg font-semibold mb-2">Notes / Audit</h2>
          <ul className="space-y-2 text-sm text-neutral-600 dark:text-neutral-300 list-disc pl-5">
            <li>Filters are mock; wire them to your API (React Query).</li>
            <li>Expose metrics per patch, region, rank for better insights.</li>
            <li>Add RBAC: ADMIN vs READONLY for safe access.</li>
            <li>Log all changes to featured data for reproducibility.</li>
          </ul>
        </motion.div>
      </div>
    </div>
  );
}
