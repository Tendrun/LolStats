import { useQuery } from "@tanstack/react-query";
import { mapChampionsWithImages } from '@/components/ChampionMap/ChampionMapping';

export const championsApi = {
  async getAll() {
    return useQuery({
    queryKey: ["champions"],
    queryFn: async () => {
        const res = await fetch("http://localhost:4021/api/data/getChampions");
        if (!res.ok) throw new Error("Network response was not ok");
        const raw = await res.json();
        return mapChampionsWithImages(raw);
    },
    });
    }
}
