package com.backendwebsite.lolstats.Constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChampionStatsMap {

    private static final Map<Integer, ChampionDetails> CHAMPION_MAP_INTERNAL = new HashMap<>();

    public static final Map<Integer, ChampionDetails> CHAMPION_MAP;

    static {
        CHAMPION_MAP_INTERNAL.put(266, new ChampionDetails(266, "Aatrox"));
        CHAMPION_MAP_INTERNAL.put(103, new ChampionDetails(103, "Ahri"));
        CHAMPION_MAP_INTERNAL.put(84, new ChampionDetails(84, "Akali"));
        CHAMPION_MAP_INTERNAL.put(166, new ChampionDetails(166, "Akshan"));
        CHAMPION_MAP_INTERNAL.put(12, new ChampionDetails(12, "Alistar"));
        CHAMPION_MAP_INTERNAL.put(799, new ChampionDetails(799, "Ambessa"));
        CHAMPION_MAP_INTERNAL.put(32, new ChampionDetails(32, "Amumu"));
        CHAMPION_MAP_INTERNAL.put(34, new ChampionDetails(34, "Anivia"));
        CHAMPION_MAP_INTERNAL.put(1, new ChampionDetails(1, "Annie"));
        CHAMPION_MAP_INTERNAL.put(523, new ChampionDetails(523, "Aphelios"));
        CHAMPION_MAP_INTERNAL.put(22, new ChampionDetails(22, "Ashe"));
        CHAMPION_MAP_INTERNAL.put(136, new ChampionDetails(136, "Aurelion Sol"));
        CHAMPION_MAP_INTERNAL.put(893, new ChampionDetails(893, "Aurora"));
        CHAMPION_MAP_INTERNAL.put(268, new ChampionDetails(268, "Azir"));
        CHAMPION_MAP_INTERNAL.put(432, new ChampionDetails(432, "Bard"));
        CHAMPION_MAP_INTERNAL.put(200, new ChampionDetails(200, "Bel'Veth"));
        CHAMPION_MAP_INTERNAL.put(53, new ChampionDetails(53, "Blitzcrank"));
        CHAMPION_MAP_INTERNAL.put(63, new ChampionDetails(63, "Brand"));
        CHAMPION_MAP_INTERNAL.put(201, new ChampionDetails(201, "Braum"));
        CHAMPION_MAP_INTERNAL.put(233, new ChampionDetails(233, "Briar"));
        CHAMPION_MAP_INTERNAL.put(51, new ChampionDetails(51, "Caitlyn"));
        CHAMPION_MAP_INTERNAL.put(164, new ChampionDetails(164, "Camille"));
        CHAMPION_MAP_INTERNAL.put(69, new ChampionDetails(69, "Cassiopeia"));
        CHAMPION_MAP_INTERNAL.put(31, new ChampionDetails(31, "Cho'Gath"));
        CHAMPION_MAP_INTERNAL.put(42, new ChampionDetails(42, "Corki"));
        CHAMPION_MAP_INTERNAL.put(122, new ChampionDetails(122, "Darius"));
        CHAMPION_MAP_INTERNAL.put(131, new ChampionDetails(131, "Diana"));
        CHAMPION_MAP_INTERNAL.put(119, new ChampionDetails(119, "Draven"));
        CHAMPION_MAP_INTERNAL.put(36, new ChampionDetails(36, "Dr. Mundo"));
        CHAMPION_MAP_INTERNAL.put(245, new ChampionDetails(245, "Ekko"));
        CHAMPION_MAP_INTERNAL.put(60, new ChampionDetails(60, "Elise"));
        CHAMPION_MAP_INTERNAL.put(28, new ChampionDetails(28, "Evelynn"));
        CHAMPION_MAP_INTERNAL.put(81, new ChampionDetails(81, "Ezreal"));
        CHAMPION_MAP_INTERNAL.put(9, new ChampionDetails(9, "Fiddlesticks"));
        CHAMPION_MAP_INTERNAL.put(114, new ChampionDetails(114, "Fiora"));
        CHAMPION_MAP_INTERNAL.put(105, new ChampionDetails(105, "Fizz"));
        CHAMPION_MAP_INTERNAL.put(3, new ChampionDetails(3, "Galio"));
        CHAMPION_MAP_INTERNAL.put(41, new ChampionDetails(41, "Gangplank"));
        CHAMPION_MAP_INTERNAL.put(86, new ChampionDetails(86, "Garen"));
        CHAMPION_MAP_INTERNAL.put(150, new ChampionDetails(150, "Gnar"));
        CHAMPION_MAP_INTERNAL.put(79, new ChampionDetails(79, "Gragas"));
        CHAMPION_MAP_INTERNAL.put(104, new ChampionDetails(104, "Graves"));
        CHAMPION_MAP_INTERNAL.put(887, new ChampionDetails(887, "Gwen"));
        CHAMPION_MAP_INTERNAL.put(120, new ChampionDetails(120, "Hecarim"));
        CHAMPION_MAP_INTERNAL.put(74, new ChampionDetails(74, "Heimerdinger"));
        CHAMPION_MAP_INTERNAL.put(910, new ChampionDetails(910, "Hwei"));
        CHAMPION_MAP_INTERNAL.put(420, new ChampionDetails(420, "Illaoi"));
        CHAMPION_MAP_INTERNAL.put(39, new ChampionDetails(39, "Irelia"));
        CHAMPION_MAP_INTERNAL.put(427, new ChampionDetails(427, "Ivern"));
        CHAMPION_MAP_INTERNAL.put(40, new ChampionDetails(40, "Janna"));
        CHAMPION_MAP_INTERNAL.put(59, new ChampionDetails(59, "Jarvan IV"));
        CHAMPION_MAP_INTERNAL.put(24, new ChampionDetails(24, "Jax"));
        CHAMPION_MAP_INTERNAL.put(126, new ChampionDetails(126, "Jayce"));
        CHAMPION_MAP_INTERNAL.put(202, new ChampionDetails(202, "Jhin"));
        CHAMPION_MAP_INTERNAL.put(222, new ChampionDetails(222, "Jinx"));
        CHAMPION_MAP_INTERNAL.put(145, new ChampionDetails(145, "Kai'Sa"));
        CHAMPION_MAP_INTERNAL.put(429, new ChampionDetails(429, "Kalista"));
        CHAMPION_MAP_INTERNAL.put(43, new ChampionDetails(43, "Karma"));
        CHAMPION_MAP_INTERNAL.put(30, new ChampionDetails(30, "Karthus"));
        CHAMPION_MAP_INTERNAL.put(38, new ChampionDetails(38, "Kassadin"));
        CHAMPION_MAP_INTERNAL.put(55, new ChampionDetails(55, "Katarina"));
        CHAMPION_MAP_INTERNAL.put(10, new ChampionDetails(10, "Kayle"));
        CHAMPION_MAP_INTERNAL.put(141, new ChampionDetails(141, "Kayn"));
        CHAMPION_MAP_INTERNAL.put(85, new ChampionDetails(85, "Kennen"));
        CHAMPION_MAP_INTERNAL.put(121, new ChampionDetails(121, "Kha'Zix"));
        CHAMPION_MAP_INTERNAL.put(203, new ChampionDetails(203, "Kindred"));
        CHAMPION_MAP_INTERNAL.put(240, new ChampionDetails(240, "Kled"));
        CHAMPION_MAP_INTERNAL.put(96, new ChampionDetails(96, "Kog'Maw"));
        CHAMPION_MAP_INTERNAL.put(897, new ChampionDetails(897, "K'Sante"));
        CHAMPION_MAP_INTERNAL.put(7, new ChampionDetails(7, "LeBlanc"));
        CHAMPION_MAP_INTERNAL.put(64, new ChampionDetails(64, "Lee Sin"));
        CHAMPION_MAP_INTERNAL.put(89, new ChampionDetails(89, "Leona"));
        CHAMPION_MAP_INTERNAL.put(876, new ChampionDetails(876, "Lillia"));
        CHAMPION_MAP_INTERNAL.put(127, new ChampionDetails(127, "Lissandra"));
        CHAMPION_MAP_INTERNAL.put(236, new ChampionDetails(236, "Lucian"));
        CHAMPION_MAP_INTERNAL.put(117, new ChampionDetails(117, "Lulu"));
        CHAMPION_MAP_INTERNAL.put(99, new ChampionDetails(99, "Lux"));
        CHAMPION_MAP_INTERNAL.put(54, new ChampionDetails(54, "Malphite"));
        CHAMPION_MAP_INTERNAL.put(90, new ChampionDetails(90, "Malzahar"));
        CHAMPION_MAP_INTERNAL.put(57, new ChampionDetails(57, "Maokai"));
        CHAMPION_MAP_INTERNAL.put(11, new ChampionDetails(11, "Master Yi"));
        CHAMPION_MAP_INTERNAL.put(800, new ChampionDetails(800, "Mel"));
        CHAMPION_MAP_INTERNAL.put(902, new ChampionDetails(902, "Milio"));
        CHAMPION_MAP_INTERNAL.put(21, new ChampionDetails(21, "Miss Fortune"));
        CHAMPION_MAP_INTERNAL.put(62, new ChampionDetails(62, "Wukong"));
        CHAMPION_MAP_INTERNAL.put(82, new ChampionDetails(82, "Mordekaiser"));
        CHAMPION_MAP_INTERNAL.put(25, new ChampionDetails(25, "Morgana"));
        CHAMPION_MAP_INTERNAL.put(950, new ChampionDetails(950, "Naafiri"));
        CHAMPION_MAP_INTERNAL.put(267, new ChampionDetails(267, "Nami"));
        CHAMPION_MAP_INTERNAL.put(75, new ChampionDetails(75, "Nasus"));
        CHAMPION_MAP_INTERNAL.put(111, new ChampionDetails(111, "Nautilus"));
        CHAMPION_MAP_INTERNAL.put(518, new ChampionDetails(518, "Neeko"));
        CHAMPION_MAP_INTERNAL.put(76, new ChampionDetails(76, "Nidalee"));
        CHAMPION_MAP_INTERNAL.put(895, new ChampionDetails(895, "Nilah"));
        CHAMPION_MAP_INTERNAL.put(56, new ChampionDetails(56, "Nocturne"));
        CHAMPION_MAP_INTERNAL.put(20, new ChampionDetails(20, "Nunu & Willump"));
        CHAMPION_MAP_INTERNAL.put(2, new ChampionDetails(2, "Olaf"));
        CHAMPION_MAP_INTERNAL.put(61, new ChampionDetails(61, "Orianna"));
        CHAMPION_MAP_INTERNAL.put(516, new ChampionDetails(516, "Ornn"));
        CHAMPION_MAP_INTERNAL.put(80, new ChampionDetails(80, "Pantheon"));
        CHAMPION_MAP_INTERNAL.put(78, new ChampionDetails(78, "Poppy"));
        CHAMPION_MAP_INTERNAL.put(555, new ChampionDetails(555, "Pyke"));
        CHAMPION_MAP_INTERNAL.put(246, new ChampionDetails(246, "Qiyana"));
        CHAMPION_MAP_INTERNAL.put(133, new ChampionDetails(133, "Quinn"));
        CHAMPION_MAP_INTERNAL.put(497, new ChampionDetails(497, "Rakan"));
        CHAMPION_MAP_INTERNAL.put(33, new ChampionDetails(33, "Rammus"));
        CHAMPION_MAP_INTERNAL.put(421, new ChampionDetails(421, "Rek'Sai"));
        CHAMPION_MAP_INTERNAL.put(526, new ChampionDetails(526, "Rell"));
        CHAMPION_MAP_INTERNAL.put(888, new ChampionDetails(888, "Renata Glasc"));
        CHAMPION_MAP_INTERNAL.put(58, new ChampionDetails(58, "Renekton"));
        CHAMPION_MAP_INTERNAL.put(107, new ChampionDetails(107, "Rengar"));
        CHAMPION_MAP_INTERNAL.put(92, new ChampionDetails(92, "Riven"));
        CHAMPION_MAP_INTERNAL.put(68, new ChampionDetails(68, "Rumble"));
        CHAMPION_MAP_INTERNAL.put(13, new ChampionDetails(13, "Ryze"));
        CHAMPION_MAP_INTERNAL.put(360, new ChampionDetails(360, "Samira"));
        CHAMPION_MAP_INTERNAL.put(113, new ChampionDetails(113, "Sejuani"));
        CHAMPION_MAP_INTERNAL.put(235, new ChampionDetails(235, "Senna"));
        CHAMPION_MAP_INTERNAL.put(147, new ChampionDetails(147, "Seraphine"));
        CHAMPION_MAP_INTERNAL.put(875, new ChampionDetails(875, "Sett"));
        CHAMPION_MAP_INTERNAL.put(35, new ChampionDetails(35, "Shaco"));
        CHAMPION_MAP_INTERNAL.put(98, new ChampionDetails(98, "Shen"));
        CHAMPION_MAP_INTERNAL.put(102, new ChampionDetails(102, "Shyvana"));
        CHAMPION_MAP_INTERNAL.put(27, new ChampionDetails(27, "Singed"));
        CHAMPION_MAP_INTERNAL.put(14, new ChampionDetails(14, "Sion"));
        CHAMPION_MAP_INTERNAL.put(15, new ChampionDetails(15, "Sivir"));
        CHAMPION_MAP_INTERNAL.put(72, new ChampionDetails(72, "Skarner"));
        CHAMPION_MAP_INTERNAL.put(901, new ChampionDetails(901, "Smolder"));
        CHAMPION_MAP_INTERNAL.put(37, new ChampionDetails(37, "Sona"));
        CHAMPION_MAP_INTERNAL.put(16, new ChampionDetails(16, "Soraka"));
        CHAMPION_MAP_INTERNAL.put(50, new ChampionDetails(50, "Swain"));
        CHAMPION_MAP_INTERNAL.put(517, new ChampionDetails(517, "Sylas"));
        CHAMPION_MAP_INTERNAL.put(134, new ChampionDetails(134, "Syndra"));
        CHAMPION_MAP_INTERNAL.put(223, new ChampionDetails(223, "Tahm Kench"));
        CHAMPION_MAP_INTERNAL.put(163, new ChampionDetails(163, "Taliyah"));
        CHAMPION_MAP_INTERNAL.put(91, new ChampionDetails(91, "Talon"));
        CHAMPION_MAP_INTERNAL.put(44, new ChampionDetails(44, "Taric"));
        CHAMPION_MAP_INTERNAL.put(17, new ChampionDetails(17, "Teemo"));
        CHAMPION_MAP_INTERNAL.put(412, new ChampionDetails(412, "Thresh"));
        CHAMPION_MAP_INTERNAL.put(18, new ChampionDetails(18, "Tristana"));
        CHAMPION_MAP_INTERNAL.put(48, new ChampionDetails(48, "Trundle"));
        CHAMPION_MAP_INTERNAL.put(23, new ChampionDetails(23, "Tryndamere"));
        CHAMPION_MAP_INTERNAL.put(4, new ChampionDetails(4, "Twisted Fate"));
        CHAMPION_MAP_INTERNAL.put(29, new ChampionDetails(29, "Twitch"));
        CHAMPION_MAP_INTERNAL.put(77, new ChampionDetails(77, "Udyr"));
        CHAMPION_MAP_INTERNAL.put(6, new ChampionDetails(6, "Urgot"));
        CHAMPION_MAP_INTERNAL.put(110, new ChampionDetails(110, "Varus"));
        CHAMPION_MAP_INTERNAL.put(67, new ChampionDetails(67, "Vayne"));
        CHAMPION_MAP_INTERNAL.put(45, new ChampionDetails(45, "Veigar"));
        CHAMPION_MAP_INTERNAL.put(161, new ChampionDetails(161, "Vel'Koz"));
        CHAMPION_MAP_INTERNAL.put(711, new ChampionDetails(711, "Vex"));
        CHAMPION_MAP_INTERNAL.put(254, new ChampionDetails(254, "Vi"));
        CHAMPION_MAP_INTERNAL.put(234, new ChampionDetails(234, "Viego"));
        CHAMPION_MAP_INTERNAL.put(112, new ChampionDetails(112, "Viktor"));
        CHAMPION_MAP_INTERNAL.put(8, new ChampionDetails(8, "Vladimir"));
        CHAMPION_MAP_INTERNAL.put(106, new ChampionDetails(106, "Volibear"));
        CHAMPION_MAP_INTERNAL.put(19, new ChampionDetails(19, "Warwick"));
        CHAMPION_MAP_INTERNAL.put(498, new ChampionDetails(498, "Xayah"));
        CHAMPION_MAP_INTERNAL.put(101, new ChampionDetails(101, "Xerath"));
        CHAMPION_MAP_INTERNAL.put(5, new ChampionDetails(5, "Xin Zhao"));
        CHAMPION_MAP_INTERNAL.put(157, new ChampionDetails(157, "Yasuo"));
        CHAMPION_MAP_INTERNAL.put(777, new ChampionDetails(777, "Yone"));
        CHAMPION_MAP_INTERNAL.put(83, new ChampionDetails(83, "Yorick"));
        CHAMPION_MAP_INTERNAL.put(350, new ChampionDetails(350, "Yuumi"));
        CHAMPION_MAP_INTERNAL.put(154, new ChampionDetails(154, "Zac"));
        CHAMPION_MAP_INTERNAL.put(238, new ChampionDetails(238, "Zed"));
        CHAMPION_MAP_INTERNAL.put(221, new ChampionDetails(221, "Zeri"));
        CHAMPION_MAP_INTERNAL.put(115, new ChampionDetails(115, "Ziggs"));
        CHAMPION_MAP_INTERNAL.put(26, new ChampionDetails(26, "Zilean"));
        CHAMPION_MAP_INTERNAL.put(142, new ChampionDetails(142, "Zoe"));
        CHAMPION_MAP_INTERNAL.put(143, new ChampionDetails(143, "Zyra"));

        CHAMPION_MAP = Collections.unmodifiableMap(CHAMPION_MAP_INTERNAL);
    }

    public static class ChampionDetails {
        public int championID;
        public String championName;
        public float winRate;
        public float banRate;
        public float pickRate;
        public int totalMatches;
        public int wonMatches;
        public int bannedMatches;
        public int pickedMatches;

        public ChampionDetails(int championID, String championName) {
            this.championID = championID;
            this.championName = championName;
            this.winRate = 0.0f;
            this.banRate = 0.0f;
            this.pickRate = 0.0f;
            this.totalMatches = 0;
            this.wonMatches = 0;
            this.bannedMatches = 0;
            this.pickedMatches = 0;
        }

        public static ChampionDetails getChampionOrLog(int championId) {
            ChampionDetails details = CHAMPION_MAP.get(championId);
            if (details == null) {
                System.err.println("❌ Brak championa w CHAMPION_MAP: championId = " + championId);
            }
            return details;
        }

    }
}

