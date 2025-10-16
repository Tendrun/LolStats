package com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchDTO {
    public Metadata metadata;
    public Info info;

    public MatchDTO(Metadata metadata, Info info) {
        this.metadata = metadata;
        this.info = info;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Metadata {
        public String dataVersion;
        public String matchId;
        public List<String> participants;
        public Metadata(String dataVersion, String matchId, List<String> participants) {
            this.dataVersion = dataVersion;
            this.matchId = matchId;
            this.participants = participants;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Info {
        public String gameVersion;
        public List<Participant> participants;
        public String platformId;
        public Integer queueId;
        public Info(String gameVersion, List<Participant> participants, String platformId, Integer queueId) {
            this.gameVersion = gameVersion;
            this.participants = participants;
            this.platformId = platformId;
            this.queueId = queueId;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Participant {
        public Integer championId;
        public String championName;

        public Integer item0;
        public Integer item1;
        public Integer item2;
        public Integer item3;
        public Integer item4;
        public Integer item5;
        public Integer item6;

        public String teamPosition;
        public Perks perks;
        public String puuid;
        public Boolean win;
        public Participant(Integer championId, String championName,
                           Integer item0, Integer item1, Integer item2, Integer item3,
                           Integer item4, Integer item5, Integer item6,
                           String teamPosition, Perks perks,
                           String puuid, Boolean win) {
            this.championId = championId;
            this.championName = championName;
            this.item0 = item0;
            this.item1 = item1;
            this.item2 = item2;
            this.item3 = item3;
            this.item4 = item4;
            this.item5 = item5;
            this.item6 = item6;
            this.teamPosition = teamPosition;
            this.perks = perks;
            this.puuid = puuid;
            this.win = win;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Perks {
        public StatPerks statPerks;
        public List<Style> styles;
        public Perks(StatPerks statPerks, List<Style> styles) {
            this.statPerks = statPerks;
            this.styles = styles;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StatPerks {
        public Integer defense;
        public Integer flex;
        public Integer offense;
        public StatPerks(Integer defense, Integer flex, Integer offense) {
            this.defense = defense;
            this.flex = flex;
            this.offense = offense;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Style {
        public String description;
        public List<Selection> selections;
        public Integer style;
        public Style(String description, List<Selection> selections, Integer style) {
            this.description = description;
            this.selections = selections;
            this.style = style;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Selection {
        public Integer perk;
        public Integer var1;
        public Integer var2;
        public Integer var3;
        public Selection(Integer perk, Integer var1, Integer var2, Integer var3) {
            this.perk = perk;
            this.var1 = var1;
            this.var2 = var2;
            this.var3 = var3;
        }
    }
}
