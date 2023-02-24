package com.maxim.gaiduchek.seabattle.entities;

public class TwoDecksShip extends Ship {

    public TwoDecksShip(Coordinates begin, Coordinates end) {
        super(begin, end);
    }

    @Override
    public int getLength() {
        return 2;
    }
}
