import { useParams } from "react-router-dom";

export default function ChampionDetail() {
    const { id } = useParams();
    return <div>Champion Detail Page for Champion ID: {id}</div>;
}