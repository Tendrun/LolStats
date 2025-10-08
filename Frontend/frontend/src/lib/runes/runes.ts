import type { runeMapping } from "@/constants/runesMapping";
import type { rune } from "@/types/rune";

export function getRunesByIds(runeIds: number[], runesMapping: Record<number, { id: number; name: string; src: string }>) {
    return runeIds.map((id) => runesMapping[id]).filter((rune) => rune !== undefined);
}

function getPrimaryRuneStyle(runeIds: number[], runesMapping: runeMapping) {
    const primaryRuneId = runeIds[0];
    return runesMapping[primaryRuneId];
}

function getSecondaryRuneStyle(runeIds: number[], runesMapping: runeMapping) {
    const secondaryRuneId = runeIds[4];
    return runesMapping[secondaryRuneId];
}

function getStatRunes(runeIds: number[], runesMapping: runeMapping) {
    const statRuneIds = runeIds.slice(5, 8);
    return statRuneIds.map((id) => runesMapping[id]).filter((rune) => rune !== undefined);
}