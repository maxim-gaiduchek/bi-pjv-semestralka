package com.maxim.gaiduchek.seabattle.entities;

import com.maxim.gaiduchek.seabattle.controllers.App;
import javafx.scene.image.ImageView;

public class OneDeckShip extends Ship {

    public OneDeckShip(Coordinates begin, Coordinates end) {
        super(begin, end);
    }

    @Override
    public int getLength() {
        return 1;
    }

    @Override
    public ImageView getShipPart(int x, int y) {
        return App.getUnaryShipImageView();
    }
}
