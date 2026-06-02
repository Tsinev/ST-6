package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Random;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class TicTacToeCellTest {
    @Test
    void constructorStoresCoordinatesAndStartsEmpty() {
        TicTacToeCell cell = new TicTacToeCell(4, 1, 1);

        assertEquals(4, cell.getNum());
        assertEquals(1, cell.getCol());
        assertEquals(1, cell.getRow());
        assertEquals(' ', cell.getMarker());
        assertEquals(" ", cell.getText());
    }

    @Test
    void setMarkerUpdatesStateAndDisablesButton() {
        TicTacToeCell cell = new TicTacToeCell(0, 0, 0);

        cell.setMarker("X");

        assertEquals('X', cell.getMarker());
        assertEquals("X", cell.getText());
        assertFalse(cell.isEnabled());
    }

    @RepeatedTest(10)
    void repeatedConstructionAssignsCorrectCoordinates() {
        int index = new Random(0).nextInt(9);
        TicTacToeCell cell = new TicTacToeCell(index, index % 3, index / 3);

        assertEquals(index, cell.getNum());
        assertEquals(index % 3, cell.getCol());
        assertEquals(index / 3, cell.getRow());
        assertEquals(' ', cell.getMarker());
        assertEquals(" ", cell.getText());
    }

    @RepeatedTest(10)
    void repeatedSetMarkerAcceptsXAndO() {
        TicTacToeCell cell = new TicTacToeCell(0, 0, 0);
        String marker = new Random(1).nextBoolean() ? "X" : "O";

        cell.setMarker(marker);

        assertEquals(marker.charAt(0), cell.getMarker());
        assertEquals(marker, cell.getText());
        assertFalse(cell.isEnabled());
    }
}
