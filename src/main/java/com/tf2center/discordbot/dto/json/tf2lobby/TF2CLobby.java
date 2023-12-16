package com.tf2center.discordbot.dto.json.tf2lobby;


//@Component
public class TF2CLobby {

    /*
    - Map done
    - Format (6s, 9v9, 4v4, ultiduo, bbal) done
    - Current players in the lobby ie. 7/12 or 15/18 done
    - Region (EU, USA, else) done
    - Id done
    - Vc requirement
    - Region lock
    - Offclassing
    - Balancing
    -
     */

    private final Integer lobbyId;
    private final Long lobbyLeaderId;
    private final String region;
    //private final String serverLocation;
    private final Boolean voiceRequiered;
    private final Boolean inReadyUpMode;
    private final String map;
    private final Integer maxPlayers;
    private final Integer numOfcurrentPlayers;
//    private final TF2CTeam<TF2TeamSide.RED> redTeam;
//    private final TF2CTeam<TF2TeamSide.BLU> bluTeam;

//    private final Set<TF2CPlayer> players;
//    private final Set<TF2CPlayer> spectators;
    private final Boolean regionLock;
    private final Boolean balancedLobby;
    private final Boolean advancedLobby;
    //private final String config;

    public TF2CLobby(Integer lobbyId, Long lobbyLeaderId, String region, Boolean voiceRequiered, Boolean inReadyUpMode, String map, Integer maxPlayers, Integer numOfcurrentPlayers, Boolean regionLock, Boolean balancedLobby, Boolean advancedLobby) {
        this.lobbyId = lobbyId;
        this.lobbyLeaderId = lobbyLeaderId;
        this.region = region;
        this.voiceRequiered = voiceRequiered;
        this.inReadyUpMode = inReadyUpMode;
        this.map = map;
        this.maxPlayers = maxPlayers;
        this.numOfcurrentPlayers = numOfcurrentPlayers;
        this.regionLock = regionLock;
        this.balancedLobby = balancedLobby;
        this.advancedLobby = advancedLobby;
    }

   // private enum TF2CLobby
   enum Kind {RED,BLU}

}
