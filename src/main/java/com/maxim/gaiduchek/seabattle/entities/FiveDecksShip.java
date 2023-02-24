package com.maxim.gaiduchek.seabattle.entities;

public class FiveDecksShip extends Ship {

    public FiveDecksShip(Coordinates begin, Coordinates end) {
        super(begin, end);
    }

    @Override
    public int getLength() {
        return 5;
    }
}
