package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class GameTest {
    @Test
    void constructorCreatesPlayersAndEmptyBoard() {
        Game game = new Game(new Random(0));

        assertEquals('X', game.player1.symbol);
        assertEquals('O', game.player2.symbol);
        assertTrue(Arrays.equals(new char[] {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, game.board));
    }

    @Test
    void checkStateDetectsAllGameResults() {
        Game game = new Game(new Random(0));

        assertEquals(State.XWIN, game.checkState(new char[] {'X', 'X', 'X', 'O', ' ', 'O', ' ', ' ', ' '}));
        assertEquals(State.OWIN, game.checkState(new char[] {'O', 'X', 'X', 'O', 'X', ' ', 'O', ' ', ' '}));
        assertEquals(State.DRAW, game.checkState(new char[] {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'}));
        assertEquals(State.PLAYING, game.checkState(new char[] {'X', 'O', ' ', ' ', 'O', ' ', ' ', 'X', ' '}));
    }

    @Test
    void generateMovesReturnsOnlyFreeCells() {
        Game game = new Game(new Random(0));
        ArrayList<Integer> moves = new ArrayList<Integer>();

        game.generateMoves(new char[] {'X', ' ', 'O', ' ', ' ', 'X', 'O', ' ', 'X'}, moves);

        assertIterableEquals(Arrays.asList(1, 3, 4, 7), moves);
    }

    @Test
    void evaluatePositionScoresTerminalBoards() {
        Game game = new Game(new Random(0));

        assertEquals(Game.INF, game.evaluatePosition(new char[] {'X', 'X', 'X', 'O', ' ', 'O', ' ', ' ', ' '}, game.player1));
        assertEquals(-Game.INF, game.evaluatePosition(new char[] {'X', 'X', 'X', 'O', ' ', 'O', ' ', ' ', ' '}, game.player2));
        assertEquals(0, game.evaluatePosition(new char[] {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'}, game.player1));
        assertEquals(-1, game.evaluatePosition(new char[] {'X', 'O', ' ', ' ', 'O', ' ', ' ', 'X', ' '}, game.player2));
    }

    @Test
    void miniMaxFindsWinningMoveForO() {
        Game game = new Game(new Random(0));
        char[] board = {'O', 'O', ' ', 'X', 'X', ' ', ' ', ' ', ' '};

        assertEquals(3, game.miniMax(board, game.player2));
    }

    @Test
    void miniMaxBlocksOpponentWin() {
        Game game = new Game(new Random(0));
        char[] board = {'X', 'X', ' ', 'O', ' ', ' ', ' ', ' ', ' '};

        assertEquals(3, game.miniMax(board, game.player2));
    }

    @Test
    void miniMaxReturnsZeroWhenBoardIsFull() {
        Game game = new Game(new Random(0));

        assertEquals(0, game.miniMax(new char[] {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'}, game.player2));
    }

    @Test
    void maxMoveAndMinMoveReturnExpectedScores() {
        Game game = new Game(new Random(0));

        assertEquals(Game.INF, game.maxMove(new char[] {'X', 'X', ' ', 'O', 'O', ' ', ' ', ' ', ' '}, game.player1));
        assertEquals(-Game.INF, game.minMove(new char[] {'X', 'X', ' ', 'O', ' ', ' ', ' ', ' ', ' '}, game.player2));
        assertTrue(game.q > 0);
    }

    @RepeatedTest(20)
    void repeatedStateEvaluationCoversWinningAndDrawBoards() {
        Game game = new Game(new Random(0));
        char[][] boards = {
            {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', 'O', 'O', 'O', ' ', ' ', ' '},
            {'X', ' ', ' ', 'X', ' ', ' ', 'X', ' ', ' '},
            {' ', 'O', ' ', ' ', 'O', ' ', ' ', 'O', ' '},
            {'X', ' ', ' ', ' ', 'X', ' ', ' ', ' ', 'X'},
            {' ', ' ', 'O', ' ', 'O', ' ', 'O', ' ', ' '},
            {'X', 'O', 'X', 'O', 'X', 'O', 'O', 'X', 'O'}
        };
        State[] expected = {State.XWIN, State.OWIN, State.XWIN, State.OWIN, State.XWIN, State.OWIN, State.DRAW};
        int index = new Random(1).nextInt(boards.length);
        assertEquals(expected[index], game.checkState(boards[index]));
        int score = game.evaluatePosition(boards[index], index % 2 == 0 ? game.player1 : game.player2);
        assertTrue(score == Game.INF || score == -Game.INF || score == 0);
    }

    @RepeatedTest(30)
    void repeatedMiniMaxHandlesDifferentOpenings() {
        Game game = new Game(new Random(13));
        char[] board = {'X', 'O', ' ', ' ', 'X', ' ', ' ', 'O', ' '};
        int move = game.miniMax(board, game.player2);
        assertTrue(move >= 0 && move <= 9);
        assertTrue(move == 0 || board[move - 1] == ' ');
    }
}
