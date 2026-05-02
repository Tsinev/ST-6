package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
}
