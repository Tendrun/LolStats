import ChampionTable from "./ChampionTable.jsx";
import { useQuery } from '@tanstack/react-query';
import { mapChampionsWithImages, ChampionStats } from "../../components/ChampionMap/ChampionMapping";

function Champions() {
  const { data: champions = [], error, isLoading } = useQuery<ChampionStats[]>({
    queryKey: ["champions"],
    queryFn: async (): Promise<ChampionStats[]> => {
      const res = await fetch("http://localhost:4021/api/data/getChampions");
      if (!res.ok) throw new Error('Network response was not ok')
        
      const raw = await res.json();
      return mapChampionsWithImages(raw);
    },
  })

  if (isLoading) return <p>Loading...</p>
  if (error) return <p>Error: {error.message}</p>

  return <ChampionTable champions={champions} />
}

export default Champions;