package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.GridLayout;
import java.util.Random;
import org.junit.jupiter.api.Test;

class TicTacToePanelTest {
    @Test
    void constructorCreatesNineCells() {
        TestPanel panel = new TestPanel(new Game(new Random(0)));

        assertEquals(9, panel.getCells().length);
        for (TicTacToeCell cell : panel.getCells()) {
            assertNotNull(cell);
        }
    }

    @Test
    void actionPerformedAppliesHumanAndComputerMoves() {
        TestPanel panel = new TestPanel(new Game(new Random(0)));

        panel.getCells()[0].doClick();

        assertEquals('X', panel.getCells()[0].getMarker());
        assertEquals('X', panel.getGame().board[0]);
        assertEquals(1, count(panel.getGame().board, 'X'));
        assertEquals(1, count(panel.getGame().board, 'O'));
        assertNull(panel.finishedState);
    }

    @Test
    void actionPerformedReportsHumanWin() {
        TestPanel panel = new TestPanel(new Game(new Random(0)));
        preset(panel, 0, 'X');
        preset(panel, 1, 'X');
        preset(panel, 3, 'O');
        preset(panel, 4, 'O');

        panel.getCells()[2].doClick();

        assertEquals(State.XWIN, panel.finishedState);
        assertEquals(2, count(panel.getGame().board, 'O'));
    }

    @Test
    void actionPerformedReportsDraw() {
        TestPanel panel = new TestPanel(new Game(new Random(0)));
        preset(panel, 0, 'X');
        preset(panel, 1, 'O');
        preset(panel, 2, 'X');
        preset(panel, 3, 'X');
        preset(panel, 4, 'O');
        preset(panel, 5, 'O');
        preset(panel, 6, 'O');
        preset(panel, 7, 'X');

        panel.getCells()[8].doClick();

        assertEquals(State.DRAW, panel.finishedState);
    }

    @Test
    void messageForMapsStatesToReadableText() {
        TestPanel panel = new TestPanel(new Game(new Random(0)));

        assertEquals("X wins", panel.messageFor(State.XWIN));
        assertEquals("O wins", panel.messageFor(State.OWIN));
        assertEquals("Draw", panel.messageFor(State.DRAW));
        assertEquals("Game continues", panel.messageFor(State.PLAYING));
    }

    private static int count(char[] board, char marker) {
        int total = 0;
        for (char cell : board) {
            if (cell == marker) {
                total++;
            }
        }
        return total;
    }

    private static void preset(TestPanel panel, int index, char marker) {
        panel.getCells()[index].setMarker(String.valueOf(marker));
        panel.getGame().board[index] = marker;
    }

    private static class TestPanel extends TicTacToePanel {
        private State finishedState;

        TestPanel(Game game) {
            super(new GridLayout(3, 3), game);
        }

        @Override
        void finishGame(State state) {
            finishedState = state;
        }
    }
}
