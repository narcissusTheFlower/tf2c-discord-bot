package com.tf2center.discordbot.dto.json.tf2cpreview;

import com.fasterxml.jackson.annotation.JsonProperty;


public final class TF2CLobbyPreview {

    @JsonProperty("no")
    private int lobbyId;
    private long leaderSteamId;
    private String region;
    @JsonProperty("mumbleRequired")
    private boolean voiceCommunicationRequired;
    private String gameType;
    private boolean inReadyUpMode;
    private String map;
    private String thumbnailUrl;
    private int playersInLobby;
    private int playersForGame;
    private String restrictionsText;
    private boolean regionLock;
    @JsonProperty("useBalancer")
    private boolean balancedLobby;
    private boolean advanced;
    private TF2CSlot[] slots;

    public int getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    public long getLeaderSteamId() {
        return leaderSteamId;
    }

    public void setLeaderSteamId(long leaderSteamId) {
        this.leaderSteamId = leaderSteamId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isVoiceCommunicationRequired() {
        return voiceCommunicationRequired;
    }

    public void setVoiceCommunicationRequired(boolean voiceCommunicationRequired) {
        this.voiceCommunicationRequired = voiceCommunicationRequired;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public boolean isInReadyUpMode() {
        return inReadyUpMode;
    }

    public void setInReadyUpMode(boolean inReadyUpMode) {
        this.inReadyUpMode = inReadyUpMode;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getPlayersInLobby() {
        return playersInLobby;
    }

    public void setPlayersInLobby(int playersInLobby) {
        this.playersInLobby = playersInLobby;
    }

    public int getPlayersForGame() {
        return playersForGame;
    }

    public void setPlayersForGame(int playersForGame) {
        this.playersForGame = playersForGame;
    }

    public String getRestrictionsText() {
        return restrictionsText;
    }

    public void setRestrictionsText(String restrictionsText) {
        this.restrictionsText = restrictionsText;
    }

    public boolean isRegionLocked() {
        return regionLock;
    }

    public void setRegionLock(boolean regionLock) {
        this.regionLock = regionLock;
    }

    public boolean isBalancedLobby() {
        return balancedLobby;
    }

    public void setBalancedLobby(boolean balancedLobby) {
        this.balancedLobby = balancedLobby;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }

    public TF2CSlot[] getSlots() {
        return slots;
    }

    public void setSlots(TF2CSlot[] slots) {
        this.slots = slots;
    }


//    EmbedCreateSpec lobbyPreview = EmbedCreateSpec.builder()
//            .color(Color.GREEN)
//            //.author("Current players", "https://tf2center.com/lobbies", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/48/Team_Fortress_2_style_logo.svg/1024px-Team_Fortress_2_style_logo.svg.png")
//            .title("Team Fortress 2 Center")
//            .url("https://tf2center.com/lobbies")
//            .description("Current players: EU 60 | NA 20 | Other 5")
//            .thumbnail("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQK4BPeNhcFv9bND-c9B5wbQUT8t5FNz-A1mo7D-td9Tw&s")
//
//            .addField("", "----------------------------------------------------", false)
//            .addField("cp_process_final_f5 | #1252268 | 6v6", "Has not started yet \uD83D\uDD56 \n" +
//                    "Discord/Mumble required: ✅ \n" +
//                    "Region lock: ✅ \n" +
//                    "Offclassing: ✅ \n" +
//                    "Balancing: \uD83C\uDDFD ", false)
//            .addField("Roamer scout", "[Join RED](https://tf2center.com/lobbies)\n" +
//                    "[Join BLU](https://tf2center.com/lobbies)", true)
//            .addField("Poket scout", "clockwork\n" +
//                    "highfive", true)
//            .addField("Roamer soldier ", "blaze\n" +
//                    "soapymeister", true)
//            .addField("Poket soldier ", "rando \n" +
//                    "dingo", true)
//            .addField("Demoman", "kaidus \n" +
//                    "duwatna", true)
//            .addField("Medic", "[Join RED](https://tf2center.com/lobbies)\n" +
//                    "[Join BLU](https://tf2center.com/lobbies)", true)
//            //-----------------------------------------------------
//            .addField("\u200B", "\u200B", false)
//            .addField("koth_product_final | #1252270 | 9v9", "Ready UP! ⚡ \n" +
//                    "Discord/Mumble required: \uD83C\uDDFD \n" +
//                    "Region lock: \uD83C\uDDFD \n" +
//                    "Offclassing: \uD83C\uDDFD \n" +
//                    "Balancing: \uD83C\uDDFD ", false)
//            .addField("Scout", "arek\n" +
//                    "[Join BLU \uD83D\uDD10](https://tf2center.com/lobbies)", true)
//            .addField("Soldier", "sandblast\n" +
//                    "[Join BLU](https://tf2center.com/lobbies)", true)
//            .addField("Pyro ", "marmaloo\n" +
//                    "[Join BLU \uD83D\uDD10](https://tf2center.com/lobbies)", true)
//            .addField("Demoman", "b4nny\n" +
//                    "[Join BLU](https://tf2center.com/lobbies)", true)
//            .addField("Heavyweapons", "[Join RED](https://tf2center.com/lobbies) \n" +
//                    "[Join BLU](https://tf2center.com/lobbies)" , true)
//            .addField("Engineer", "skeez \n" +
//                    "[Join BLU](https://tf2center.com/lobbies)", true)
//            .addField("Medic", "skeez\n" +
//                    "[Join BLU](https://tf2center.com/lobbies)", true)
//            .addField("Sniper", "skeez\n" +
//                    "[Join BLU](https://tf2center.com/lobbies)", true)
//            .addField("Spy", "[Join RED](https://tf2center.com/lobbies)\n" +
//                    "[Join BLU](https://tf2center.com/lobbies)", true)
//            //-----------------------------------------------------
//            .addField("\u200B", "\u200B", false)
//
//            .addField("koth_ultiduo | #1252271 | 2v2", "Has not started yet \uD83D\uDD56 \n" +
//                    "Discord/Mumble required: \uD83C\uDDFD \n" +
//                    "Region lock: ✅ \n" +
//                    "Offclassing: \uD83C\uDDFD \n" +
//                    "Balancing: \uD83C\uDDFD ", false)
//            .addField("Soldier", "[Join RED](https://tf2center.com/lobbies) \uD83D\uDD10 \n" +
//                    "[Join BLU](https://tf2center.com/lobbies) ", true)
//            .addField("Medic", "[Join RED](https://tf2center.com/lobbies) \uD83D\uDD10 \n" +
//                    "[Join BLU](https://tf2center.com/lobbies)", true)
//            //-----------------------------------------------------
//            //.image("https://wiki.teamfortress.com/w/images/8/82/Cp_process_middle_point.jpeg")
//            .timestamp(Instant.now())
//            .footer(" \uD83D\uDD56 Last updated", "")  //https://upload.wikimedia.org/wikipedia/commons/thumb/4/48/Team_Fortress_2_style_logo.svg/1024px-Team_Fortress_2_style_logo.svg.png
//            .build();

}
