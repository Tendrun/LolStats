import { useEffect, useState } from "react";
import ChampionTable from "./ChampionTable";


  function Champions() {
    const [champions, setChampions] = useState([]);

    useEffect(() => {
      fetch("/champions.json")
      .then((res) => res.json())
      .then((data) => setChampions(data))
      .catch((err) => console.error("Failed to load champions:", err));
  }, []);

  return (
    <ChampionTable champions={champions} />
  );
}

  
export default Champions;
  