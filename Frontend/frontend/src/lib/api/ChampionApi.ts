import { mapChampionsWithImages, ChampionStats } from "@/components/ChampionMap/ChampionMapping";

export async function getAllChampions(): Promise<ChampionStats[]> {
  
  const res = await fetch("http://localhost:4021/api/data/getChampions");
  if (!res.ok) throw new Error("Network response was not ok");

  const raw = await res.json();
  return mapChampionsWithImages(raw);
}