package com.maxim.gaiduchek.seabattle.factories;

import com.maxim.gaiduchek.seabattle.entities.*;

public class ShipFactory {

    private ShipFactory() {
    }

    public static Ship createShip(Coordinates begin, Coordinates end) {
        int length = Math.abs(begin.y() == end.y() ? begin.x() - end.x() : begin.y() - end.y()) + 1;
        switch (length) {
            case 1:
                return new OneDeckShip(begin, end);
            case 2:
                return new TwoDecksShip(begin, end);
            case 3:
                return new ThreeDecksShip(begin, end);
            case 4:
                return new FourDecksShip(begin, end);
            case 5:
                return new FiveDecksShip(begin, end);
            default:
                throw new IllegalArgumentException("Coordinates do not match the ship's length");
        }
    }
}
