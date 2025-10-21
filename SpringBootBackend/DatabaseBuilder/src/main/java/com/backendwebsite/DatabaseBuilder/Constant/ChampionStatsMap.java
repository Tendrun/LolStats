package com.backendwebsite.DatabaseBuilder.Constant;

import lombok.Getter;

import java.util.*;

public class ChampionStatsMap {

    private static final List<ChampionDetails> CHAMPION_MAP_INTERNAL = new ArrayList<>();
    public List<ChampionDetails> CHAMPION_MAP = new ArrayList<>(CHAMPION_MAP_INTERNAL);

    public int ALL_MATCHES_PLAYED = 0;
    public int ALL_BAN_COUNT = 0;


    static {
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(266, "Aatrox"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(103, "Ahri"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(84, "Akali"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(166, "Akshan"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(12, "Alistar"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(799, "Ambessa"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(32, "Amumu"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(34, "Anivia"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(1, "Annie"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(523, "Aphelios"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(22, "Ashe"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(136, "Aurelion Sol"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(893, "Aurora"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(268, "Azir"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(432, "Bard"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(200, "Bel'Veth"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(53, "Blitzcrank"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(63, "Brand"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(201, "Braum"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(233, "Briar"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(51, "Caitlyn"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(164, "Camille"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(69, "Cassiopeia"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(31, "Cho'Gath"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(42, "Corki"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(122, "Darius"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(131, "Diana"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(119, "Draven"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(36, "Dr. Mundo"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(245, "Ekko"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(60, "Elise"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(28, "Evelynn"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(81, "Ezreal"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(9, "Fiddlesticks"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(114, "Fiora"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(105, "Fizz"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(3, "Galio"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(41, "Gangplank"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(86, "Garen"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(150, "Gnar"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(79, "Gragas"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(104, "Graves"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(887, "Gwen"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(120, "Hecarim"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(74, "Heimerdinger"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(910, "Hwei"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(420, "Illaoi"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(39, "Irelia"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(427, "Ivern"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(40, "Janna"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(59, "Jarvan IV"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(24, "Jax"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(126, "Jayce"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(202, "Jhin"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(222, "Jinx"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(145, "Kai'Sa"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(429, "Kalista"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(43, "Karma"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(30, "Karthus"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(38, "Kassadin"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(55, "Katarina"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(10, "Kayle"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(141, "Kayn"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(85, "Kennen"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(121, "Kha'Zix"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(203, "Kindred"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(240, "Kled"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(96, "Kog'Maw"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(897, "K'Sante"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(7, "LeBlanc"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(64, "Lee Sin"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(89, "Leona"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(876, "Lillia"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(127, "Lissandra"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(236, "Lucian"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(117, "Lulu"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(99, "Lux"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(54, "Malphite"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(90, "Malzahar"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(57, "Maokai"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(11, "Master Yi"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(800, "Mel"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(902, "Milio"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(21, "Miss Fortune"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(62, "Wukong"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(82, "Mordekaiser"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(25, "Morgana"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(950, "Naafiri"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(267, "Nami"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(75, "Nasus"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(111, "Nautilus"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(518, "Neeko"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(76, "Nidalee"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(895, "Nilah"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(56, "Nocturne"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(20, "Nunu & Willump"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(2, "Olaf"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(61, "Orianna"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(516, "Ornn"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(80, "Pantheon"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(78, "Poppy"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(555, "Pyke"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(246, "Qiyana"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(133, "Quinn"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(497, "Rakan"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(33, "Rammus"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(421, "Rek'Sai"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(526, "Rell"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(888, "Renata Glasc"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(58, "Renekton"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(107, "Rengar"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(92, "Riven"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(68, "Rumble"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(13, "Ryze"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(360, "Samira"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(113, "Sejuani"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(235, "Senna"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(147, "Seraphine"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(875, "Sett"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(35, "Shaco"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(98, "Shen"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(102, "Shyvana"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(27, "Singed"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(14, "Sion"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(15, "Sivir"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(72, "Skarner"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(901, "Smolder"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(37, "Sona"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(16, "Soraka"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(50, "Swain"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(517, "Sylas"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(134, "Syndra"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(223, "Tahm Kench"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(163, "Taliyah"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(91, "Talon"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(44, "Taric"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(17, "Teemo"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(412, "Thresh"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(18, "Tristana"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(48, "Trundle"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(23, "Tryndamere"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(4, "Twisted Fate"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(29, "Twitch"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(77, "Udyr"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(6, "Urgot"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(110, "Varus"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(67, "Vayne"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(45, "Veigar"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(161, "Vel'Koz"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(711, "Vex"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(254, "Vi"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(234, "Viego"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(112, "Viktor"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(8, "Vladimir"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(106, "Volibear"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(19, "Warwick"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(498, "Xayah"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(101, "Xerath"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(5, "Xin Zhao"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(157, "Yasuo"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(777, "Yone"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(83, "Yorick"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(804, "Yunara"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(350, "Yuumi"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(154, "Zac"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(238, "Zed"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(221, "Zeri"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(115, "Ziggs"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(26, "Zilean"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(142, "Zoe"));
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(143, "Zyra"));


        /// UNKNOWN SITUATIONS
        CHAMPION_MAP_INTERNAL.add(new ChampionDetails(3151, "Unknown"));
    }

    public static class ChampionDetails {
        public String _id;
        public String _rev;
        public int championId;
        public String name;
        @Getter
        private float winRate;
        @Getter
        private float banRate;
        @Getter
        private float pickRate;
        public int totalMatchesPicked;
        public int wonMatches;
        public int bannedMatches;

        public ChampionDetails(int championId, String name) {
            this.championId = championId;
            this.name = name;
            this.winRate = 0.0f;
            this.banRate = 0.0f;
            this.pickRate = 0.0f;
            this.totalMatchesPicked = 0;
            this.wonMatches = 0;
            this.bannedMatches = 0;
        }

        public ChampionDetails(int championId, String name, float winRate, float banRate, float pickRate,
                               int totalMatchesPicked, int wonMatches, int bannedMatches) {
            this.championId = championId;
            this.name = name;
            this.winRate = winRate;
            this.banRate = banRate;
            this.pickRate = pickRate;
            this.totalMatchesPicked = totalMatchesPicked;
            this.wonMatches = wonMatches;
            this.bannedMatches = bannedMatches;
        }

        public void setWinRate(float winRate) {
            this.winRate = (float) (Math.floor(winRate * 10000) / 100);
        }

        public void setBanRate(float banRate) {
            this.banRate = (float) (Math.floor(banRate * 10000) / 100);
        }

        public void setPickRate(float pickRate) {
            this.pickRate = (float) (Math.floor(pickRate * 10000) / 100);
        }
    }
}

