package com.maxim.gaiduchek.seabattle.entities;

import javafx.scene.layout.GridPane;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IGrid {

    int MAX_SHIP_LENGTH = 5;
    int MAX_X = 10, MAX_Y = 10;

    // static

    static boolean getFreeCellNearby(Grid grid, int x, int y) {
        if (0 <= x - 1 && !grid.isShotted(x - 1, y)) {
            return true;
        }
        if (x + 1 <= MAX_X && !grid.isShotted(x + 1, y)) {
            return true;
        }
        if (0 <= y - 1 && !grid.isShotted(x, y - 1)) {
            return true;
        }
        return y + 1 <= MAX_Y && !grid.isShotted(x, y + 1);
    }

    static boolean validateLength1(Grid grid) {
        for (int x = 0; x <= MAX_X; x++) {
            for (int y = 0; y <= MAX_Y; y++) {
                if (grid.isCellFree(x, y)) {
                    return true;
                }
            }
        }

        return false;
    }

    static boolean validateLength2(Grid grid) {
        for (int x = 0; x <= MAX_X; x++) {
            for (int y = 0; y <= MAX_Y; y++) {
                if (grid.isCellFree(x, y) && getFreeCellNearby(grid, x, y)) {
                    return true;
                }
            }
        }

        return false;
    }

    static void forEachShipLength(Consumer<Integer> function) {
        for (int length = MAX_SHIP_LENGTH; length >= 1; length--) {
            function.accept(length);
        }
    }

    static void forEachCoordinate(BiConsumer<Integer, Integer> function) {
        for (int x = 0; x <= MAX_X; x++) {
            for (int y = 0; y <= MAX_Y; y++) {
                function.accept(x, y);
            }
        }
    }

    Ship getShip(Coordinates coordinates);

    Ship getShip(int x, int y);

    int getShipsCount(int shipLength);

    boolean isLengthNotCompleted(int shipLength);

    boolean isFullyCompleted();

    boolean isCellFree(int x, int y);

    boolean hasShip(int x, int y);

    boolean hasShip(Coordinates coordinates);

    boolean isShotted(int x, int y);

    boolean isDestroyed(int x, int y);

    boolean hasShipNearby(int cx, int cy);

    boolean isDefeated();

    void addShip(Ship ship);

    void removeShip(Coordinates coordinates);

    void decrementShipsCount(int shipLength);

    boolean shot(GridPane gridPane, int x, int y, boolean isEnemy);
}
