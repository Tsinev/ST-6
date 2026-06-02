package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import org.junit.jupiter.api.Test;

class ProgramTest {
    @Test
    void createGamePanelReturnsPanelWithNineCells() {
        TicTacToePanel panel = Program.createGamePanel();

        assertNotNull(panel);
        assertEquals(9, panel.getCells().length);
        for (TicTacToeCell cell : panel.getCells()) {
            assertNotNull(cell);
        }
    }

    @Test
    void createGameFrameReturnsFrameReadyForDisplay() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Headless mode: skip frame creation test");

        JFrame frame = Program.createGameFrame();

        assertNotNull(frame);
        assertEquals("Tic-Tac-Toe", frame.getTitle());
        assertEquals(1, frame.getContentPane().getComponentCount());
        assertFalse(frame.isVisible());
    }
}
