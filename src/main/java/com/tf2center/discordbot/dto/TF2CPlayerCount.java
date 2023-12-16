package com.tf2center.discordbot.dto;

import java.util.concurrent.atomic.AtomicInteger;

public class TF2CPlayerCount {

    private final AtomicInteger playersCountTotal;
    private final AtomicInteger playersEU;
    private final AtomicInteger playersNA;
    private final AtomicInteger playersOther;


    private TF2CPlayerCount(AtomicInteger playersCountTotal,
                            AtomicInteger playersEU,
                            AtomicInteger playersNA,
                            AtomicInteger playersOther) {
        this.playersCountTotal = playersCountTotal;
        this.playersEU = playersEU;
        this.playersNA = playersNA;
        this.playersOther = playersOther;
    }

    public static TF2CPlayerCount of(AtomicInteger playersCountTotal,
                                     AtomicInteger playersEU,
                                     AtomicInteger playersNA,
                                     AtomicInteger playersOther){
        return new TF2CPlayerCount(playersCountTotal,playersEU,playersNA,playersOther);
    }

    public AtomicInteger getPlayersCountTotal() {
        return playersCountTotal;
    }

    public AtomicInteger getPlayersEU() {
        return playersEU;
    }

    public AtomicInteger getPlayersNA() {
        return playersNA;
    }

    public AtomicInteger getPlayersOther() {
        return playersOther;
    }
}
