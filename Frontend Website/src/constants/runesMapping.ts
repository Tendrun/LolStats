import type { rune } from "@/types/rune";

const RUNESMAPPING: Record<number, rune> = {
 
  // =========================
  // 🎯 PRECISION
  // =========================
  7201: { id: 7201, name: "Precision", src: "/src/assets/runes/Styles/7201_Precision.png" },
  8005: { id: 8005, name: "Press the Attack", src: "/src/assets/runes/Styles/Precision/PressTheAttack/PressTheAttack.png" },
  8008: { id: 8008, name: "Lethal Tempo", src: "/src/assets/runes/Styles/Precision/LethalTempo/LethalTempoTemp.png" },
  8010: { id: 8010, name: "Conqueror", src: "/src/assets/runes/Styles/Precision/Conqueror/Conqueror.png" },
  8014: { id: 8014, name: "Coup de Grace", src: "/src/assets/runes/Styles/Precision/CoupDeGrace/CoupDeGrace.png" },
  8017: { id: 8017, name: "Cut Down", src: "/src/assets/runes/Styles/Precision/CutDown/CutDown.png" },
  8021: { id: 8021, name: "Fleet Footwork", src: "/src/assets/runes/Styles/Precision/FleetFootwork/FleetFootwork.png" },
  8009: { id: 8009, name: "Presence of Mind", src: "/src/assets/runes/Styles/Precision/PresenceOfMind/PresenceOfMind.png" },
  9101: { id: 9101, name: "Absorb Life", src: "/src/assets/runes/Styles/Precision/AbsorbLife/AbsorbLife.png" },
  9103: { id: 9103, name: "Legend: Bloodline", src: "/src/assets/runes/Styles/Precision/LegendBloodline/LegendBloodline.png" },
  9104: { id: 9104, name: "Legend: Alacrity", src: "/src/assets/runes/Styles/Precision/LegendAlacrity/LegendAlacrity.png" },
  9105: { id: 9105, name: "Legend: Haste", src: "/src/assets/runes/Styles/Precision/LegendHaste/LegendHaste.png" },
  9111: { id: 9111, name: "Triumph", src: "/src/assets/runes/Styles/Precision/Triumph.png" },
  8299: { id: 8299, name: "Last Stand", src: "/src/assets/runes/Styles/Precision/LastStand/LastStand.png" },

  // =========================
  // 💀 DOMINATION
  // =========================
  7200: { id: 7200, name: "Domination", src: "/src/assets/runes/Styles/7200_Domination.png" },
  8112: { id: 8112, name: "Electrocute", src: "/src/assets/runes/Styles/Domination/Electrocute/Electrocute.png" },
  8126: { id: 8126, name: "Cheap Shot", src: "/src/assets/runes/Styles/Domination/CheapShot/CheapShot.png" },
  8128: { id: 8128, name: "Dark Harvest", src: "/src/assets/runes/Styles/Domination/DarkHarvest/DarkHarvest.png" },
  8135: { id: 8135, name: "Treasure Hunter", src: "/src/assets/runes/Styles/Domination/TreasureHunter/TreasureHunter.png" },
  8137: { id: 8137, name: "Sixth Sense", src: "/src/assets/runes/Styles/Domination/SixthSense/SixthSense.png" },
  8139: { id: 8139, name: "Taste of Blood", src: "/src/assets/runes/Styles/Domination/TasteOfBlood/GreenTerror_TasteOfBlood.png" },
  8140: { id: 8140, name: "Grisly Mementos", src: "/src/assets/runes/Styles/Domination/GrislyMementos/GrislyMementos.png" },
  8141: { id: 8141, name: "Deep Ward", src: "/src/assets/runes/Styles/Domination/DeepWard/DeepWard.png" },
  8143: { id: 8143, name: "Sudden Impact", src: "/src/assets/runes/Styles/Domination/SuddenImpact/SuddenImpact.png" },
  8105: { id: 8105, name: "Relentless Hunter", src: "/src/assets/runes/Styles/Domination/RelentlessHunter/RelentlessHunter.png" },
  8106: { id: 8106, name: "Ultimate Hunter", src: "/src/assets/runes/Styles/Domination/UltimateHunter/UltimateHunter.png" },
  9923: { id: 9923, name: "Hail of Blades", src: "/src/assets/runes/Styles/Domination/HailOfBlades/HailOfBlades.png" },

  // =========================
  // 🔮 SORCERY
  // =========================
  7202: { id: 7202, name: "Sorcery", src: "/src/assets/runes/Styles/7202_Sorcery.png" },
  8210: { id: 8210, name: "Transcendence", src: "/src/assets/runes/Styles/Sorcery/Transcendence/Transcendence.png" },
  8214: { id: 8214, name: "Summon Aery", src: "/src/assets/runes/Styles/Sorcery/SummonAery/SummonAery.png" },
  8224: { id: 8224, name: "Axiom Arcanist", src: "/src/assets/runes/Styles/Sorcery/NullifyingOrb/Axiom_Arcanist.png" },
  8226: { id: 8226, name: "Manaflow Band", src: "/src/assets/runes/Styles/Sorcery/ManaflowBand/ManaflowBand.png" },
  8229: { id: 8229, name: "Arcane Comet", src: "/src/assets/runes/Styles/Sorcery/ArcaneComet/ArcaneComet.png" },
  8230: { id: 8230, name: "Phase Rush", src: "/src/assets/runes/Styles/Sorcery/PhaseRush/PhaseRush.png" },
  8232: { id: 8232, name: "Waterwalking", src: "/src/assets/runes/Styles/Sorcery/Waterwalking/Waterwalking.png" },
  8233: { id: 8233, name: "Absolute Focus", src: "/src/assets/runes/Styles/Sorcery/AbsoluteFocus/AbsoluteFocus.png" },
  8234: { id: 8234, name: "Celerity", src: "/src/assets/runes/Styles/Sorcery/Celerity/CelerityTemp.png" },
  8236: { id: 8236, name: "Gathering Storm", src: "/src/assets/runes/Styles/Sorcery/GatheringStorm/GatheringStorm.png" },
  8237: { id: 8237, name: "Scorch", src: "/src/assets/runes/Styles/Sorcery/Scorch/Scorch.png" },
  8242: { id: 8242, name: "Unflinching", src: "/src/assets/runes/Styles/Sorcery/Unflinching/Unflinching.png" },
  8275: { id: 8275, name: "Nimbus Cloak", src: "/src/assets/runes/Styles/Sorcery/NimbusCloak/6361.png" },

  // =========================
  // 🛡️ RESOLVE
  // =========================
  7204: { id: 7204, name: "Resolve", src: "/src/assets/runes/Styles/7204_Resolve.png" },
  8401: { id: 8401, name: "Shield Bash", src: "/src/assets/runes/Styles/Resolve/Shield Bash/Shield Bash.png" },
  8429: { id: 8429, name: "Conditioning", src: "/src/assets/runes/Styles/Resolve/Conditioning/Conditioning.png" },
  8437: { id: 8437, name: "Grasp of the Undying", src: "/src/assets/runes/Styles/Resolve/GraspOfTheUndying/GraspOfTheUndying.png" },
  8439: { id: 8439, name: "Aftershock", src: "/src/assets/runes/Styles/Resolve/VeteranAftershock/VeteranAftershock.png" },
  8444: { id: 8444, name: "Second Wind", src: "/src/assets/runes/Styles/Resolve/SecondWind/SecondWind.png" },
  8446: { id: 8446, name: "Demolish", src: "/src/assets/runes/Styles/Resolve/Demolish/Demolish.png" },
  8451: { id: 8451, name: "Overgrowth", src: "/src/assets/runes/Styles/Resolve/Overgrowth/Overgrowth.png" },
  8453: { id: 8453, name: "Revitalize", src: "/src/assets/runes/Styles/Resolve/Revitalize/Revitalize.png" },
  8463: { id: 8463, name: "Font of Life", src: "/src/assets/runes/Styles/Resolve/FontOfLife/FontOfLife.png" },
  8465: { id: 8465, name: "Guardian", src: "/src/assets/runes/Styles/Resolve/Guardian/Guardian.png" },
  8473: { id: 8473, name: "Bone Plating", src: "/src/assets/runes/Styles/Resolve/BonePlating/BonePlating.png" },

  // =========================
  // 💡 INSPIRATION
  // =========================
  7203: { id: 7203, name: "Inspiration", src: "/src/assets/runes/Styles/7203_Inspiration.png" },
  8304: { id: 8304, name: "Magical Footwear", src: "/src/assets/runes/Styles/Inspiration/MagicalFootwear/MagicalFootwear.png" },
  8410: { id: 8410, name: "Approach Velocity", src: "/src/assets/runes/Styles/Inspiration/ApproachVelocity/ApproachVelocity.png" },
  8313: { id: 8313, name: "Triple Tonic", src: "/src/assets/runes/Styles/Inspiration/PerfectTiming/AlchemistCabinet.png" },
  8316: { id: 8316, name: "Jack Of All Trades", src: "/src/assets/runes/Styles/Inspiration/JackOfAllTrades/JackofAllTrades2.png" },
  8321: { id: 8321, name: "Cash Back", src: "/src/assets/runes/Styles/Inspiration/CashBack/CashBack2.png" },
  8345: { id: 8345, name: "Biscuit Delivery", src: "/src/assets/runes/Styles/Inspiration/BiscuitDelivery/BiscuitDelivery.png" },
  8347: { id: 8347, name: "Cosmic Insight", src: "/src/assets/runes/Styles/Inspiration/CosmicInsight/CosmicInsight.png" },
  8351: { id: 8351, name: "Glacial Augment", src: "/src/assets/runes/Styles/Inspiration/GlacialAugment/GlacialAugment.png" },
  8352: { id: 8352, name: "Time Warp Tonic", src: "/src/assets/runes/Styles/Inspiration/TimeWarpTonic/TimeWarpTonic.png" },
  8360: { id: 8360, name: "Unsealed Spellbook", src: "/src/assets/runes/Styles/Inspiration/UnsealedSpellbook/UnsealedSpellbook.png" },
  8369: { id: 8369, name: "First Strike", src: "/src/assets/runes/Styles/Inspiration/FirstStrike/FirstStrike.png" },
  8306: { id: 8369, name: "Hextech Flashtraption", src: "/src/assets/runes/Styles/Inspiration/HextechFlashtraption/HextechFlashtraption.png" },


  // =========================
  // ⚙️ STAT MODS
  // =========================
  5001: { id: 5001, name: "Health Scaling", src: "/src/assets/runes/StatMods/StatModsHealthScalingIcon.png" },
  5005: { id: 5005, name: "Attack Speed", src: "/src/assets/runes/StatMods/StatModsAttackSpeedIcon.png" },
  5007: { id: 5007, name: "CDR", src: "/src/assets/runes/StatMods/StatModsCDRIcon.png" },
  5008: { id: 5008, name: "Adaptive Force", src: "/src/assets/runes/StatMods/StatModsAdaptiveForceIcon.png" },
  5010: { id: 5010, name: "Movement Speed", src: "/src/assets/runes/StatMods/StatModsMovementSpeedIcon.png" },
  5011: { id: 5011, name: "Health", src: "/src/assets/runes/StatMods/StatModsHealthPlusIcon.png" },
  5013: { id: 5013, name: "Tenacity", src: "/src/assets/runes/StatMods/StatModsTenacityIcon.png" },

  // =========================
  // Different
  // =========================
  1: { id: 1, name: "RunesIcon", src: "/src/assets/runes/Styles/src/assets/runesIcon.png" },

};

export enum runeCategory {
  Precision = "Precision",
  Domination = "Domination",
  Sorcery = "Sorcery",
  Resolve = "Resolve",
  Inspiration = "Inspiration",
  StatMods = "StatMods",
}

export enum runeSubCategory {
  Icon = "Icon",
  Primary = "Primary",
  Secondary = "Secondary",
  StatRunes = "Stat Runes",
}


export const RUNESCATEGORIES: Record<
  runeCategory, 
  Record<runeSubCategory, rune[]> 
  > = {
  [runeCategory.Precision]: {
    [runeSubCategory.Icon]: [RUNESMAPPING[7201]],

    [runeSubCategory.Primary]: [
      RUNESMAPPING[8005],
      RUNESMAPPING[8008],
      RUNESMAPPING[8021],
      RUNESMAPPING[8010],
    ],
    [runeSubCategory.Secondary]: [
      RUNESMAPPING[9101],
      RUNESMAPPING[9111],
      RUNESMAPPING[8009],
      RUNESMAPPING[9104],
      RUNESMAPPING[9105],
      RUNESMAPPING[9103],      
      RUNESMAPPING[8014],
      RUNESMAPPING[8017],
      RUNESMAPPING[8299],
    ],
    [runeSubCategory.StatRunes]: [],
  },

  [runeCategory.Domination]: {
    [runeSubCategory.Icon]: [RUNESMAPPING[7200]],

    [runeSubCategory.Primary]: [
      RUNESMAPPING[8112],
      RUNESMAPPING[8128],
      RUNESMAPPING[9923],
    ],
    [runeSubCategory.Secondary]: [
      RUNESMAPPING[8126],
      RUNESMAPPING[8139],
      RUNESMAPPING[8143],
      RUNESMAPPING[8137],
      RUNESMAPPING[8140],
      RUNESMAPPING[8141],
      RUNESMAPPING[8135],
      RUNESMAPPING[8105],
      RUNESMAPPING[8106],
    ],
    [runeSubCategory.StatRunes]: [],
  },

  [runeCategory.Sorcery]: {
    [runeSubCategory.Icon]: [RUNESMAPPING[7202]],

    [runeSubCategory.Primary]: [
      RUNESMAPPING[8214],
      RUNESMAPPING[8229],
      RUNESMAPPING[8230],
    ],
    [runeSubCategory.Secondary]: [
      RUNESMAPPING[8224],
      RUNESMAPPING[8226],
      RUNESMAPPING[8275],
      RUNESMAPPING[8210],
      RUNESMAPPING[8234],
      RUNESMAPPING[8233],
      RUNESMAPPING[8237],
      RUNESMAPPING[8232],
      RUNESMAPPING[8236],
    ],
    [runeSubCategory.StatRunes]: [],
  },

  [runeCategory.Resolve]: {
    [runeSubCategory.Icon]: [RUNESMAPPING[7204]],

    [runeSubCategory.Primary]: [
      RUNESMAPPING[8437],
      RUNESMAPPING[8439],
      RUNESMAPPING[8465],
    ],
    [runeSubCategory.Secondary]: [
      RUNESMAPPING[8446],
      RUNESMAPPING[8463],
      RUNESMAPPING[8401],
      RUNESMAPPING[8429],
      RUNESMAPPING[8444],
      RUNESMAPPING[8473],
      RUNESMAPPING[8451],
      RUNESMAPPING[8453],
      RUNESMAPPING[8242],
    ],
    [runeSubCategory.StatRunes]: [],
  },

  [runeCategory.Inspiration]: {
    [runeSubCategory.Icon]: [RUNESMAPPING[7203]],

    [runeSubCategory.Primary]: [
      RUNESMAPPING[8351],
      RUNESMAPPING[8360],
      RUNESMAPPING[8369],
    ],
    [runeSubCategory.Secondary]: [
      RUNESMAPPING[8306],
      RUNESMAPPING[8304],
      RUNESMAPPING[8321],
      RUNESMAPPING[8313],
      RUNESMAPPING[8352],
      RUNESMAPPING[8345],
      RUNESMAPPING[8347],
      RUNESMAPPING[8410],
      RUNESMAPPING[8316],
    ],
    [runeSubCategory.StatRunes]: [],
  },

  [runeCategory.StatMods]: {
    [runeSubCategory.Icon]: [],
    [runeSubCategory.Primary]: [],
    [runeSubCategory.Secondary]: [],
    [runeSubCategory.StatRunes]: [
      RUNESMAPPING[5008],
      RUNESMAPPING[5005],
      RUNESMAPPING[5007],
      RUNESMAPPING[5008],
      RUNESMAPPING[5010],
      RUNESMAPPING[5001],
      RUNESMAPPING[5011],
      RUNESMAPPING[5013],
      RUNESMAPPING[5001],
    ],
  },
};