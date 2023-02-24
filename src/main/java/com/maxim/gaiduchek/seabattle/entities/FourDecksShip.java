package com.maxim.gaiduchek.seabattle.entities;

public class FourDecksShip extends Ship {

    public FourDecksShip(Coordinates begin, Coordinates end) {
        super(begin, end);
    }

    @Override
    public int getLength() {
        return 4;
    }
}
