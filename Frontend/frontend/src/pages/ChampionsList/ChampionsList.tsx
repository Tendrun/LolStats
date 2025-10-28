import ChampionTable from "./ChampionTable.jsx";
import { useQuery } from '@tanstack/react-query';
import { ChampionStats } from '@/components/ChampionMap/ChampionMapping';
import { getAllChampions } from '@/lib/api/ChampionApi';

function Champions() {
  const { data: champions = [], error, isLoading } = useQuery<ChampionStats[]>({
    queryKey: ["champions"],
    queryFn: getAllChampions,
  })

  if (isLoading) return <p>Loading...</p>
  if (error) return <p>Error: {error.message}</p>

  return <ChampionTable champions={champions} />
}

export default Champions;