package com.magareto.yorick.models.coinflip;

public enum CoinFlip {
    EVEN ("even"),
    HEADS ("heads"),
    TAILS("tails");

    String flipName;

    CoinFlip(String flipName) {
        this.flipName = flipName;
    }

    public static CoinFlip getCoinFlip(String name) {
        for(CoinFlip coinFlip: values()) {
            if(coinFlip.flipName.equals(name)) {
                return coinFlip;
            }
        }
        return null;
    }


}
