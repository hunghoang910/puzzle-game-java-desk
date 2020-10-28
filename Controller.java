/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzlegamefinal;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tvhtr
 */
public class Controller implements ActionListener {

    NewJFrame newJFrame;
    private List<JButton> listButton = new ArrayList<>();
    private int size;
    private int indexSpace, index;
    private int moveCount = 0;
    private boolean check;

//    private Timer t;
    private Thread t1;

    public Controller() {
        newJFrame = new NewJFrame();
        newJFrame.setVisible(true);
        setPanel();
        mixNumber();

        newJFrame.getjButton1().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame();
            }
        });

    }

//    public void countTime() {
//        t = new Timer(1000, new ActionListener() {
//            int time = 0;
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                time++;
//                newJFrame.getLblTime().setText(time + "");
//            }
//        });
//        t.start();
//    }
    public void setPanel() {
        //TODO: Setup Panel 
        newJFrame.getPnLayout().removeAll(); //remove all component
        listButton.removeAll(listButton);
        index = newJFrame.getjComboBox1().getSelectedIndex();
        String str = newJFrame.getjComboBox1().getSelectedItem().toString();
        size = Integer.parseInt(str.substring(0, str.indexOf('x')).trim());
        int heightButton = 60;
        int widthButton = 60;
        //set cho panel dung layout
        newJFrame.getPnLayout().setLayout(new GridLayout(size, size, 10, 10));

        newJFrame.getPnLayout().setPreferredSize(new Dimension(size * widthButton + 10 * (size - 1), size * widthButton + 10 * (size - 1)));
        newJFrame.setResizable(false);
        newJFrame.pack(); //thay doi kich thuoc panel khi thay doi kich thuoc phan tu ben trong 
        if (t1 == null) { //timer is not started
            check = false;
        } else { 
            check = true;
        }
        for (int i = 0; i < size * size - 1; i++) {
            JButton btn = new JButton(i + 1 + "");
            listButton.add(btn);
            newJFrame.getPnLayout().add(btn);
            btn.addActionListener(this);
        }

        JButton btn = new JButton("");
        listButton.add(btn);
        newJFrame.getPnLayout().add(btn);
        btn.addActionListener(this);//take event
    }

    public void newGame() {
        if (t1 != null && !checkWin()) { //t1 has not been started yet and the game is not in win case

            t1.suspend(); 
            int confirm = JOptionPane.showConfirmDialog(null, "Do you want to make new game?", "New Game", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) { //user press yes
                newJFrame.getLblCount().setText("0");
                count();
                setPanel();
                mixNumber();
            } else { // no
                newJFrame.getjComboBox1().setSelectedIndex(index);
                t1.resume();
            }
        } else {// when player won or when the game has not been started yet 
            count();
            setPanel();
            mixNumber();
        }

    }

    public void mixNumber() {
        Random r = new Random();
        indexSpace = listButton.size() - 1;//mac dinh ban dau vi tri cuoi
        for (int i = 0; i < 100 * size; i++) {//so lan mix ko important
            int choose = 1 + r.nextInt(4);//1 up, 2 down, 3 left, 4 right
            
            //if index of space button on the first line
            //choose = 1: up  
            if (indexSpace < size && choose == 1) {
                continue; //ignore the loop: cannot move button up 
            }
            if (indexSpace >= size * size - size && choose == 2) {
                continue;
            }
            if (indexSpace % size == 0 && choose == 3) { 
                continue; 
            }
            if (indexSpace % size == size - 1 && choose == 4) {
                continue;
            }
            
            switch (choose) {
                case 1:
                    swapBtn(indexSpace, indexSpace - size);
                    indexSpace -= size;
                    break;
                case 2:
                    swapBtn(indexSpace, indexSpace + size);
                    indexSpace += size;
                    break;
                case 3:
                    swapBtn(indexSpace, indexSpace - 1);
                    indexSpace -= 1;
                    break;
                case 4:
                    swapBtn(indexSpace, indexSpace + 1);
                    indexSpace += 1;
                    break;
            }
        }

    }

    public void swapBtn(int iFirst, int iSecond) {
        String temp = listButton.get(iFirst).getText(); 
        listButton.get(iFirst).setText(listButton.get(iSecond).getText());
        listButton.get(iSecond).setText(temp);
    }

    public void pressNumber(int i) {//event an nut
        // Check btn can move or not when user press.
        if (i < size * size - size //btn is not on the last row
                && listButton.get(i + size).getText().equals("")) { //btn below it is empty
            swapBtn(i, i + size); //swap 2 btn
            moveCount++;
        } else if (i > size - 1 //btn is not on the first row
                && listButton.get(i - size).getText().equals("")) { //btn above it is empty
            swapBtn(i, i - size);//
            moveCount++;
        } else if (i % size < size - 1 //right 
                && listButton.get(i + 1).getText().equals("")) {
            swapBtn(i, i + 1);
            moveCount++;
        } else if (i % size > 0 
                && listButton.get(i - 1).getText().equals("")) {
            swapBtn(i, i - 1);
            moveCount++;
        }
        newJFrame.getLblCount().setText(moveCount + "");
        if (i == size * size - 1 && listButton.get(i).getText().equals("")) {
            if (checkWin()) {
                t1.stop();
//                check = false;
                JOptionPane.showMessageDialog(null, "You win!");
            }
        }
    }

    public boolean checkWin() {
        for (int i = 0; i < listButton.size() - 1; i++) {
            if (!listButton.get(i).getText().equals(i + 1 + "")) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (check) {
            for (int i = 0; i < listButton.size(); i++) {
                if (e.getSource().equals(listButton.get(i))) {
                    pressNumber(i);//tao event
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Press new game to continue!");
        }
    }

    public void count() {
        moveCount = 0;
        newJFrame.getLblCount().setText("0");
        newJFrame.getLblTime().setText("0");
        t1 = new Thread(() -> {
            int time = 0;
            while (true) {
                try {
                    newJFrame.getLblTime().setText(time + "");
                    time++;
                    t1.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t1.start();
    }

}
