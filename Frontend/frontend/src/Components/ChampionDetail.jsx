// Components/ChampionDetail.jsx
import { useParams } from "react-router-dom";

export default function ChampionDetail({ champions }) {
  const { championName } = useParams();

  const champ = champions.find(
    (c) => c.name.toLowerCase().replace(/\s+/g, "-") === championName
  );

  if (!champ) return <div>Champion not found</div>;

  return (
    <div>
      <h1>{champ.name}</h1>
      <img src={`/ChampionIcons/${champ.image}`} alt={champ.name} />
      <p>Role: {champ.role}</p>
      <p>Tier: {champ.tier}</p>
      <p>Win Rate: {champ.winRate}</p>
      <p>Pick Rate: {champ.pickRate}</p>
      <p>Ban Rate: {champ.banRate}</p>
      <p>Matches: {champ.matches}</p>
    </div>
  );
}
