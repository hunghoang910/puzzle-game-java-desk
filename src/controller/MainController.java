/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import view.NumberPuzzleGame;

/**
 *
 * @author Hung Trinh
 */
public class MainController {

    private final NumberPuzzleGame view;
    private int sizeOfMatrix;
    private JButton[][] matrix;
    private final int level = 1000;
    private boolean isGameStarting;
    private Timer time;
    private int numbersOfMoveCount = 0;

    public MainController(NumberPuzzleGame view) {
        this.view = view;
    }

    private void control() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
        view.setResizable(false);
        isGameStarting = false;
        startGame();
        spawnButton();
    }

    private void startGame() {
        view.getBtnNewGame().addActionListener((evt) -> {
            if (isGameStarting) {
                time.stop();
                int confirm = JOptionPane.showConfirmDialog(null, "Do you want to make a new game?", "New Game", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    countTime();
                    spawnButton();
                    isGameStarting = true;
                    numbersOfMoveCount = 0;
                    view.getLblMoveCount().setText("0");
                } else if (confirm == JOptionPane.NO_OPTION) {
                    time.stop();
                }

            } else {
                countTime();
                spawnButton();
                isGameStarting = true;
                numbersOfMoveCount = 0;
                view.getLblMoveCount().setText("0");
            }
        });
    }

    private void countTime() {
        view.getLblTimeCount().setText("0" + "s");
        time = new Timer(1000, new ActionListener() {
            int seconds = 0;
            @Override
            public void actionPerformed(ActionEvent evt) {
                seconds++;
                view.getLblTimeCount().setText(seconds + "s");
            }
        });
        time.start();
    }

    private void spawnButton() {
        sizeOfMatrix = view.getCbbPuzzleSize().getSelectedIndex() + 3;
        view.getPnlPlaySpace().removeAll();
        view.getPnlPlaySpace().setLayout(new GridLayout(sizeOfMatrix, sizeOfMatrix, 10, 10));
        view.getPnlPlaySpace().setPreferredSize(new Dimension(sizeOfMatrix * 60, sizeOfMatrix * 60));
        matrix = new JButton[sizeOfMatrix][sizeOfMatrix];
        for (int i = 0; i < sizeOfMatrix; i++) {
            for (int j = 0; j < sizeOfMatrix; j++) {
                JButton btn = new JButton(i * sizeOfMatrix + j + 1 + "");
                matrix[i][j] = btn;
                view.getPnlPlaySpace().add(btn);
                swapButton(btn);
            }
        }
        matrix[sizeOfMatrix - 1][sizeOfMatrix - 1].setText("");
        randomButton();
        view.pack();
    }

    private void swapButton(JButton btn) {
        btn.addActionListener((evt) -> {
            if (isGameStarting) {
                if (canMove(btn)) {
                    moveButton(btn);
                    numbersOfMoveCount++;
                    view.getLblMoveCount().setText(numbersOfMoveCount + "");
                    if (isWon()) {
                        isGameStarting = false;
                        JOptionPane.showMessageDialog(null, "You won!");
                        time.stop();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Press New Game to start");
            }
        });
    }

    private boolean canMove(JButton btnPicked) {
        if (btnPicked.getText().equals("")) {
            return false;
        }
        Point pBlank = getPositionOfBlankButton();
        Point pPicked = null;
        for (int i = 0; i < sizeOfMatrix; i++) {
            for (int j = 0; j < sizeOfMatrix; j++) {
                if (matrix[i][j].getText().equals(btnPicked.getText())) {
                    pPicked = new Point(i, j);
                }
            }
        }
        if (pBlank.x == pPicked.x && Math.abs(pBlank.y - pPicked.y) == 1) {
            return true;
        }
        if (pBlank.y == pPicked.y && Math.abs(pBlank.x - pPicked.x) == 1) {
            return true;
        }
        return false;
    }

    private void moveButton(JButton btnPicked) {
        Point pBlank = getPositionOfBlankButton();
        matrix[pBlank.x][pBlank.y].setText(btnPicked.getText());
        btnPicked.setText("");
    }

    private void randomButton() {
        for (int i = 0; i < level; i++) {
            Point p = this.getPositionOfBlankButton();
            int x = p.x;
            int y = p.y;
            Random r = new Random();
            int n = r.nextInt(4);
            switch (n) {
                case 0: //swap left
                    if (y > 0) {
                        matrix[x][y].setText(matrix[x][y - 1].getText());
                        matrix[x][y - 1].setText("");
                    }
                    break;
                case 1: //swap right
                    if (y < sizeOfMatrix - 1) {
                        matrix[x][y].setText(matrix[x][y + 1].getText());
                        matrix[x][y + 1].setText("");
                    }
                    break;
                case 2: //swap up
                    if (x > 0) {
                        matrix[x][y].setText(matrix[x - 1][y].getText());
                        matrix[x - 1][y].setText("");
                    }
                    break;
                case 3: //swap down
                    if (x < sizeOfMatrix - 1) {
                        matrix[x][y].setText(matrix[x + 1][y].getText());
                        matrix[x + 1][y].setText("");
                    }
                    break;
            }
        }
    }

    private boolean isWon() {
        if (!matrix[sizeOfMatrix - 1][sizeOfMatrix - 1].getText().equals("")) {
            return false;
        }
        for (int i = 0; i < sizeOfMatrix; i++) {
            for (int j = 0; j < sizeOfMatrix; j++) {
                if ((i == sizeOfMatrix - 1) && (j == sizeOfMatrix - 1)) {
                    return true;
                } else if (!matrix[i][j].getText().equals(i * sizeOfMatrix + j + 1 + "")) { //neu matrix k co gia tri la a++ va nut blank o cuoi
                    return false;
                }
            }
        }
        return true;
    }

    private Point getPositionOfBlankButton() {
        for (int i = 0; i < sizeOfMatrix; i++) {
            for (int j = 0; j < sizeOfMatrix; j++) {
                if (matrix[i][j].getText().equals("")) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        new MainController(new NumberPuzzleGame()).control();
    }
}
