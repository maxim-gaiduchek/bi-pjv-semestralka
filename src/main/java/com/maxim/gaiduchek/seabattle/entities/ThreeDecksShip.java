package com.maxim.gaiduchek.seabattle.entities;

public class ThreeDecksShip extends Ship {

    public ThreeDecksShip(Coordinates begin, Coordinates end) {
        super(begin, end);
    }

    @Override
    public int getLength() {
        return 3;
    }
}
