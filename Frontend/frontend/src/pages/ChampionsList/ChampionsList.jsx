import ChampionTable from "./ChampionTable";
import { useQuery } from '@tanstack/react-query';

function Champions() {
  const { data: champions = [], error, isLoading } = useQuery({
    queryKey: ['champions'],
    queryFn: async () => {
      const res = await fetch('http://localhost:4021/api/data/getChampions')
      if (!res.ok) throw new Error('Network response was not ok')
      return res.json()
    },
  })

  if (isLoading) return <p>Loading...</p>
  if (error) return <p>Error: {error.message}</p>

  return <ChampionTable champions={champions} />
}

export default Champions;