package sliderpuzzle;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SliderPuzzle {

    //TODO add a timer?
    private final int NUM_ROWS = 4;
    private final int NUM_COLUMNS = 4;
    private final int TOTAL_SQUARES = NUM_ROWS * NUM_COLUMNS;
    private final int BUTTON_SIZE = 65;
    private final static SliderPuzzle INSTANCE = new SliderPuzzle();
    private JButton[][] squares;
    private List<Integer> availableNumbers; //List of numbers for use in buttons
    private JFrame frame;

    public static SliderPuzzle getInstance() {
        return INSTANCE;
    }

    //Constructor for SliderPuzzle
    private SliderPuzzle() {
        frame = new JFrame("Slider Puzzle");
        frame.getContentPane().add(createNewGame());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null); //Centers frame. Must follow pack
        frame.setVisible(true);
    }

    private JPanel createNewGame() {
        //TODO add a custom dialog to enter the rows and colums min 3 max 8, min 3 max 10
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(NUM_ROWS, NUM_COLUMNS));
        squares = new JButton[NUM_ROWS][NUM_COLUMNS];
        JButton tempButton;
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLUMNS; j++) {
                tempButton = new JButton("");
                tempButton.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
                tempButton.setFocusable(false);
                tempButton.addActionListener(new ButtonEventHandler());
                squares[i][j] = tempButton;
                panel.add(tempButton);
            }
        }
        squares[NUM_ROWS - 1][NUM_COLUMNS - 1].setVisible(false);
        availableNumbers = new ArrayList<>();
        randomizeNumbers();
        addNumbersToButtons();
        return panel;
    }

    private class ButtonEventHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton neighbor, source = (JButton) e.getSource();
            String sourceText = source.getText();
            for (int k = 0; k < NUM_ROWS; k++) {
                for (int l = 0; l < NUM_COLUMNS; l++) {
                    if (source == squares[k][l]) {
                        neighbor = checkNeighbors(k, l);
                        source.setText(neighbor.getText());
                        source.setVisible(false);
                        neighbor.setText(sourceText);
                        neighbor.setVisible(true);
                        checkForWin();
                        break;
                    }
                }
            }
        }

        /*  Returns original square or the neighboring square (top, bottom, left or right) that is blank. */
        private JButton checkNeighbors(int rowIndex, int colIndex) {
            JButton temp;
            String lastSquare = Integer.toString(TOTAL_SQUARES);
            //Top neighbor
            temp = squares[Math.max(0, rowIndex - 1)][colIndex];
            if (temp.getText().equals(lastSquare)) {
                return temp;
            }
            //Bottom neighbor
            temp = squares[Math.min(NUM_ROWS - 1, rowIndex + 1)][colIndex];
            if (temp.getText().equals(lastSquare)) {
                return temp;
            }
            //Left neighbor
            temp = squares[rowIndex][Math.max(0, colIndex - 1)];
            if (temp.getText().equals(lastSquare)) {
                return temp;
            }
            //Right neighbor
            temp = squares[rowIndex][Math.min(NUM_COLUMNS - 1, colIndex + 1)];
            if (temp.getText().equals(lastSquare)) {
                return temp;
            }
            return squares[rowIndex][colIndex];
        }
    }

    private void checkForWin() {
        int counter = 1;
        for (JButton[] jButtons : squares) {
            for (JButton jButton : jButtons) {
                //If the buttons are in the correct order, their text will
                //coincide with the counter
                if (!jButton.getText().equals(Integer.toString(counter++))) {
                    return;
                }
            }
        }

        Object[] options = {"New Game",
            "Quit"};
        int n = JOptionPane.showOptionDialog(frame,
                "You Win!",
                null,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options, //the titles of buttons
                options[0]); //default button title

        if (n == JOptionPane.YES_OPTION) {
            randomizeNumbers();
            addNumbersToButtons();
        } else if (n == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    private void randomizeNumbers() {
        availableNumbers.clear();
        for (int a = 1; a < NUM_ROWS * NUM_COLUMNS; a++) {
            availableNumbers.add(a);
        }
        Collections.shuffle(availableNumbers);
        availableNumbers.add(TOTAL_SQUARES);
        checkPolarity(availableNumbers);
    }

    private void checkPolarity(List<Integer> availableNumbers) {
        int inversions = 0;
        for (int i = 0; i < TOTAL_SQUARES; i++) {
            for (int j = i + 1; j < TOTAL_SQUARES; j++) {
                if (availableNumbers.get(i) > availableNumbers.get(j)) {
                    inversions++;
                }
            }
        }

        if (0 != inversions % 2) {
            Integer temp = availableNumbers.get(0);
            availableNumbers.set(0, availableNumbers.get(1));
            availableNumbers.set(1, temp);
        }
    }

    private void addNumbersToButtons() {
        int counter = 0;
        for (JButton[] jButtons : squares) {
            for (JButton jButton : jButtons) {
                jButton.setText(availableNumbers.get(counter++).toString());
            }
        }
    }
}
