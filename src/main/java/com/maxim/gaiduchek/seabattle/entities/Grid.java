package com.maxim.gaiduchek.seabattle.entities;

import com.maxim.gaiduchek.seabattle.controllers.App;
import com.maxim.gaiduchek.seabattle.factories.ShipFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Grid implements IGrid, Externalizable {

    private static final long serialVersionUID = 7763048505717776650L;

    private final Cell[][] cells = new Cell[MAX_Y + 1][MAX_X + 1];
    private final int[] shipsCount = new int[MAX_SHIP_LENGTH];

    public Grid() {
        IGrid.forEachCoordinate((x, y) -> cells[y][x] = new Cell());
        IGrid.forEachShipLength(length -> shipsCount[length - 1] = 0);
    }

    public static Grid generate() {
        Grid grid = new Grid();
        Random random = new Random();

        for (int length = MAX_SHIP_LENGTH; length >= 1; length--) {
            for (int count = MAX_SHIP_LENGTH - length + 1; count >= 1; count--) {
                if (length == 2 && !IGrid.validateLength2(grid) || length == 1 && !IGrid.validateLength1(grid)) {
                    length = MAX_SHIP_LENGTH + 1;
                    grid = new Grid();
                    break;
                }

                Coordinates begin, end;

                while (true) {
                    if (random.nextBoolean()) {
                        int x = random.nextInt(MAX_X - length + 2), y = random.nextInt(MAX_Y + 1);

                        begin = new Coordinates(x, y);
                        end = new Coordinates(x + length - 1, y);
                    } else {
                        int x = random.nextInt(MAX_X + 1), y = random.nextInt(MAX_Y - length + 2);

                        begin = new Coordinates(x, y);
                        end = new Coordinates(x, y + length - 1);
                    }

                    boolean toBreak = true;

                    for (int x = begin.x(); x <= end.x() && toBreak; x++) {
                        for (int y = begin.y(); y <= end.y() && toBreak; y++) {
                            if (grid.hasShipNearby(x, y)) {
                                toBreak = false;
                            }
                        }
                    }

                    grid.outputGrid();

                    if (toBreak) {
                        break;
                    }
                }
                grid.addShip(ShipFactory.createShip(begin, end));
                grid.outputGrid();
            }
        }

        System.out.println("Randomly generated grid:");
        grid.outputGrid();

        return grid;
    }

    // getters

    private Cell getCell(Coordinates coordinates) {
        return getCell(coordinates.x(), coordinates.y());
    }

    private Cell getCell(int x, int y) {
        return cells[y][x];
    }

    @Override
    public Ship getShip(Coordinates coordinates) {
        return getShip(coordinates.x(), coordinates.y());
    }

    @Override
    public Ship getShip(int x, int y) {
        return getCell(x, y).getShip();
    }

    @Override
    public int getShipsCount(int shipLength) {
        return shipsCount[shipLength - 1];
    }

    // booleans

    @Override
    public boolean isLengthNotCompleted(int shipLength) {
        return shipsCount[shipLength - 1] != MAX_SHIP_LENGTH - shipLength + 1;
    }

    @Override
    public boolean isFullyCompleted() {
        for (int length = 1; length <= MAX_SHIP_LENGTH; length++) {
            if (isLengthNotCompleted(length)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isCellFree(int x, int y) {
        return getCell(x, y).isFree() && !hasShipNearby(x, y);
    }

    @Override
    public boolean hasShip(int x, int y) {
        return getShip(x, y) != null;
    }

    @Override
    public boolean hasShip(Coordinates coordinates) {
        return hasShip(coordinates.x(), coordinates.y());
    }

    @Override
    public boolean isShotted(int x, int y) {
        return getCell(x, y).isShotted();
    }

    @Override
    public boolean isDestroyed(int x, int y) {
        return isDestroyed(getShip(x, y));
    }

    private boolean isDestroyed(Ship ship) {
        for (int x = ship.getBeginX(); x <= ship.getEndX(); x++) {
            for (int y = ship.getBeginY(); y <= ship.getEndY(); y++) {
                if (!isShotted(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean hasShipNearby(int cx, int cy) {
        for (int x = Math.max(cx - 1, 0); x <= Math.min(cx + 1, MAX_X); x++) {
            for (int y = Math.max(cy - 1, 0); y <= Math.min(cy + 1, MAX_Y); y++) {
                if (hasShip(x, y)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isDefeated() {
        for (int x = 0; x <= MAX_X; x++) {
            for (int y = 0; y <= MAX_Y; y++) {
                if (hasShip(x, y) && !isShotted(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    // setters

    @Override
    public void addShip(Ship ship) {
        ship.forEachCoordinate((x, y) -> getCell(x, y).setShip(ship));
        shipsCount[ship.getLength() - 1]++;
    }

    @Override
    public void removeShip(Coordinates coordinates) {
        Ship ship = getCell(coordinates).getShip();

        ship.forEachCoordinate((x, y) -> getCell(x, y).setShip(null));
        shipsCount[ship.getLength() - 1]--;
    }

    private void setShotted(int x, int y) {
        getCell(x, y).setShotted();
    }

    @Override
    public void decrementShipsCount(int shipLength) {
        shipsCount[shipLength - 1]--;
    }

    // other

    @Override
    public boolean shot(GridPane gridPane, int x, int y, boolean isEnemy) {
        if (!isShotted(x, y)) {
            setShotted(x, y);

            if (hasShip(x, y)) {
                Ship ship = getShip(x, y);

                if (isDestroyed(ship)) {
                    Coordinates begin = ship.getBegin(), end = ship.getEnd();

                    for (int cx = Math.max(begin.x() - 1, 0); cx <= Math.min(end.x() + 1, MAX_X); cx++) {
                        for (int cy = Math.max(begin.y() - 1, 0); cy <= Math.min(end.y() + 1, MAX_Y); cy++) {
                            if (hasShip(cx, cy)) {
                                gridPane.add(ship.getShipPart(cx, cy), cx, cy);
                                gridPane.add(App.getHitShotImageView(), cx, cy);
                            } else if (!isShotted(cx, cy)) {
                                gridPane.add(isEnemy ? App.getEnemyMissedShotImageView() : App.getMissedShotImageView(),
                                        cx, cy);
                            }
                            setShotted(cx, cy);
                        }
                    }
                }

                ImageView hitShot = App.getHitShotImageView();

                gridPane.add(hitShot, x, y);
                App.playAppearingAnimation(hitShot);

                return true;
            } else {
                ImageView missedShot = isEnemy ? App.getEnemyMissedShotImageView() : App.getMissedShotImageView();

                gridPane.add(missedShot, x, y);
                App.playAppearingAnimation(missedShot);

                return false;
            }
        }

        return false;
    }

    private void outputGrid() {
        for (int y = 0; y <= MAX_Y; y++) {
            for (int x = 0; x <= MAX_X; x++) {
                System.out.print((hasShip(x, y) ? "0" : ".") + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    // externalize

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        Set<Ship> ships = new HashSet<>();

        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (cell.hasShip()) {
                    ships.add(cell.getShip());
                }
                out.writeBoolean(cell.isShotted());
            }
        }
        for (Ship ship : ships) {
            out.writeByte(ship.getBeginX());
            out.writeByte(ship.getBeginY());
            out.writeByte(ship.getEndX());
            out.writeByte(ship.getEndY());
        }
    }

    @Override
    public void readExternal(ObjectInput in) {
        for (int y = 0; y <= MAX_Y; y++) {
            for (int x = 0; x <= MAX_X; x++) {
                try {
                    cells[y][x] = new Cell(in.readBoolean());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        IGrid.forEachShipLength(length -> {
            for (int i = MAX_SHIP_LENGTH - length + 1; i >= 1; i--) {
                try {
                    Coordinates begin = new Coordinates(in.readByte(), in.readByte());
                    Coordinates end = new Coordinates(in.readByte(), in.readByte());

                    addShip(ShipFactory.createShip(begin, end));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
