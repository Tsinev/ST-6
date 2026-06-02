package com.mycompany.app;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

enum State {
    PLAYING, OWIN, XWIN, DRAW
}

class GamePlayer {
    final char symbol;

    GamePlayer(char symbol) {
        this.symbol = symbol;
    }
}

class Game {
    static final int INF = 100;
    static final char EMPTY = ' ';

    static final int[][] WINNING_LINES = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
        {0, 4, 8}, {2, 4, 6}
    };

    final GamePlayer player1 = new GamePlayer('X');
    final GamePlayer player2 = new GamePlayer('O');
    final char[] board = {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY};

    private final Random random;
    int q;

    Game() {
        this(new Random());
    }

    Game(Random random) {
        this.random = random;
    }

    State checkState(char[] currentBoard) {
        if (hasWinningLine(currentBoard, player1.symbol)) {
            return State.XWIN;
        }
        if (hasWinningLine(currentBoard, player2.symbol)) {
            return State.OWIN;
        }
        return hasFreeCell(currentBoard) ? State.PLAYING : State.DRAW;
    }

    void generateMoves(char[] currentBoard, List<Integer> moves) {
        collectAvailableMoves(currentBoard, moves);
    }

    void collectAvailableMoves(char[] currentBoard, List<Integer> freeCells) {
        freeCells.clear();
        for (int i = 0; i < currentBoard.length; i++) {
            if (currentBoard[i] == EMPTY) {
                freeCells.add(i);
            }
        }
    }

    int evaluatePosition(char[] currentBoard, GamePlayer player) {
        State state = checkState(currentBoard);
        if (state == State.DRAW) {
            return 0;
        }
        if (state == State.XWIN) {
            return player.symbol == 'X' ? INF : -INF;
        }
        if (state == State.OWIN) {
            return player.symbol == 'O' ? INF : -INF;
        }
        return -1;
    }

    int miniMax(char[] currentBoard, GamePlayer player) {
        return computeBestMove(currentBoard, player);
    }

    int computeBestMove(char[] currentBoard, GamePlayer player) {
        if (evaluatePosition(currentBoard, player) != -1) {
            return 0;
        }
        int currentBest = -INF;
        List<Integer> moves = new ArrayList<Integer>();
        List<Integer> bestMoves = new ArrayList<Integer>();
        collectAvailableMoves(currentBoard, moves);
        for (int move : moves) {
            currentBoard[move] = player.symbol;
            int value = minimaxMin(currentBoard, player);
            currentBoard[move] = EMPTY;
            if (value > currentBest) {
                currentBest = value;
                bestMoves.clear();
                bestMoves.add(move + 1);
            } else if (value == currentBest) {
                bestMoves.add(move + 1);
            }
        }
        return bestMoves.isEmpty() ? 0 : bestMoves.get(random.nextInt(bestMoves.size()));
    }

    int minMove(char[] currentBoard, GamePlayer player) {
        return minimaxMin(currentBoard, player);
    }

    int maxMove(char[] currentBoard, GamePlayer player) {
        return minimaxMax(currentBoard, player);
    }

    int minimaxMin(char[] currentBoard, GamePlayer player) {
        int value = evaluatePosition(currentBoard, player);
        if (value != -1) {
            return value;
        }
        q++;
        int bestValue = INF;
        List<Integer> moves = new ArrayList<Integer>();
        collectAvailableMoves(currentBoard, moves);
        for (int move : moves) {
            currentBoard[move] = opponentFor(player.symbol);
            bestValue = Math.min(bestValue, minimaxMax(currentBoard, player));
            currentBoard[move] = EMPTY;
        }
        return bestValue;
    }

    int minimaxMax(char[] currentBoard, GamePlayer player) {
        int value = evaluatePosition(currentBoard, player);
        if (value != -1) {
            return value;
        }
        q++;
        int bestValue = -INF;
        List<Integer> moves = new ArrayList<Integer>();
        collectAvailableMoves(currentBoard, moves);
        for (int move : moves) {
            currentBoard[move] = player.symbol;
            bestValue = Math.max(bestValue, minimaxMin(currentBoard, player));
            currentBoard[move] = EMPTY;
        }
        return bestValue;
    }

    private boolean hasWinningLine(char[] currentBoard, char symbol) {
        for (int[] line : WINNING_LINES) {
            if (currentBoard[line[0]] == symbol
                    && currentBoard[line[1]] == symbol
                    && currentBoard[line[2]] == symbol) {
                return true;
            }
        }
        return false;
    }

    private boolean hasFreeCell(char[] currentBoard) {
        for (char cell : currentBoard) {
            if (cell == EMPTY) {
                return true;
            }
        }
        return false;
    }

    private char opponentFor(char symbol) {
        return symbol == 'X' ? 'O' : 'X';
    }
}

class TicTacToeCell extends JButton {
    private final int num;
    private final int row;
    private final int col;
    private char marker = ' ';

    TicTacToeCell(int num, int col, int row) {
        this.num = num;
        this.row = row;
        this.col = col;
        setText(String.valueOf(marker));
        setFont(new Font("Arial", Font.PLAIN, 40));
    }

    void setMarker(String marker) {
        this.marker = marker.charAt(0);
        setText(marker);
        setEnabled(false);
    }

    char getMarker() {
        return marker;
    }

    int getRow() {
        return row;
    }

    int getCol() {
        return col;
    }

    int getNum() {
        return num;
    }
}

class TicTacToePanel extends JPanel implements ActionListener {
    private final Game game;
    private final TicTacToeCell[] cells = new TicTacToeCell[9];

    TicTacToePanel(GridLayout layout) {
        this(layout, new Game());
    }

    TicTacToePanel(GridLayout layout, Game game) {
        super(layout);
        this.game = game;
        for (int i = 0; i < cells.length; i++) {
            createCell(i);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        TicTacToeCell cell = (TicTacToeCell) event.getSource();
        applyMove(cell, game.player1.symbol);
        State state = game.checkState(game.board);
        if (state != State.PLAYING) {
            finishGame(state);
            return;
        }
        applyComputerMove();
        state = game.checkState(game.board);
        if (state != State.PLAYING) {
            finishGame(state);
        }
    }

    TicTacToeCell[] getCells() {
        return cells;
    }

    Game getGame() {
        return game;
    }

    String messageFor(State state) {
        switch (state) {
            case XWIN:
                return "X wins";
            case OWIN:
                return "O wins";
            case DRAW:
                return "Draw";
            default:
                return "Game continues";
        }
    }

    void finishGame(State state) {
        JOptionPane.showMessageDialog(this, messageFor(state), "Result", JOptionPane.INFORMATION_MESSAGE);
    }

    private void applyComputerMove() {
        int move = game.computeBestMove(game.board, game.player2);
        if (move > 0) {
            applyMove(cells[move - 1], game.player2.symbol);
        }
    }

    private void applyMove(TicTacToeCell cell, char symbol) {
        cell.setMarker(String.valueOf(symbol));
        game.board[cell.getNum()] = symbol;
    }

    private void createCell(int index) {
        TicTacToeCell cell = new TicTacToeCell(index, index % 3, index / 3);
        cell.addActionListener(this);
        cells[index] = cell;
        add(cell);
    }
}

public class Program {
    static TicTacToePanel createGamePanel() {
        return new TicTacToePanel(new GridLayout(3, 3));
    }

    static JFrame createGameFrame() {
        JFrame frame = new JFrame("Tic-Tac-Toe");
        frame.add(createGamePanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(5, 5, 500, 500);
        return frame;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = createGameFrame();
                frame.setVisible(true);
            }
        });
    }
}
