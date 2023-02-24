package com.maxim.gaiduchek.seabattle.factories;

import com.maxim.gaiduchek.seabattle.entities.Grid;
import com.maxim.gaiduchek.seabattle.entities.IGrid;

public class GridFactory {

    private GridFactory() {
    }

    public static IGrid createDefault() {
        return new Grid();
    }

    public static IGrid generateDefault() {
        return Grid.generate();
    }
}
