import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class WhacAMole {
    int boardWidth = 600;
    int boardHeight = 700; //50 for the text panel on top, 50 for the button panel at the bottom

    JFrame frame = new JFrame("Mario: Whac A Mole");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
	
    JButton[] board = new JButton[9];
    ImageIcon moleIcon;
    ImageIcon plantIcon;

    JButton currMoleTile;
    JButton[] currPlantTile = new JButton[2]; // Array to hold multiple plant tiles

    Random random = new Random();
    Timer setMoleTimer;
    Timer setPlantTimer;
    int plantTimerDelay = 1500; // Delay for plant timer in milliseconds
    int moleTimerDelay = 1000; // Delay for mole timer in milliseconds

    int score = 0;
    int highScore = 0;
    int moleclicks = 0;
    boolean gameOver = false;

    WhacAMole() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.PLAIN, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Score: " + Integer.toString(score) + " Best: " + Integer.toString(highScore));
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);		
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3, 3));
        frame.add(boardPanel);

        Image plantImg = new ImageIcon(getClass().getResource("./piranha.png")).getImage();
        plantIcon = new ImageIcon(plantImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        Image moleImg = new ImageIcon(getClass().getResource("./monty.png")).getImage();
        moleIcon = new ImageIcon(moleImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));
		
        for (int i = 0; i < 9; i++) {
            JButton tile = new JButton();
            board[i] = tile;
            boardPanel.add(tile);
            tile.setFocusable(false);

            tile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton tile = (JButton) e.getSource();
                    if (tile == currMoleTile) {
                        score += 10;
                        if (score > highScore) {
                            highScore = score;
                        }
                        textLabel.setText("Score: " + Integer.toString(score) + " Best: " + Integer.toString(highScore));
                        moleclicks++;
                        if (moleclicks == 5) {
                            moleclicks = 0;
                            if (moleTimerDelay > 100) {
                                moleTimerDelay -= 50; // Decrease mole timer delay
                            }
                            setMoleTimer.setDelay(moleTimerDelay); // Decrease mole timer delay
                            setMoleTimer.restart(); // Restart the timer with the new delay
                            if (plantTimerDelay > 100) {
                                plantTimerDelay -= 50; // Decrease plant timer delay
                            }
                            setPlantTimer.setDelay(plantTimerDelay); // Decrease plant timer delay
                            setPlantTimer.restart(); // Restart the timer with the new delay
                        }
                    }
                    else if (tile == currPlantTile[0] || tile == currPlantTile[1]) {
                        gameOver = true;
                        textLabel.setText("Game Over: " + Integer.toString(score));
                        setMoleTimer.stop();
                        setPlantTimer.stop();
                        for (int i = 0; i < 9; i++) {
                            board[i].setEnabled(false);
                        }
                    }
                }
            });
        }

        setMoleTimer = new Timer(moleTimerDelay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //remove icon from current tile
                if (currMoleTile != null) {
                    currMoleTile.setIcon(null);
                    currMoleTile = null;
                }

                //randomly select another tile
                int num = random.nextInt(9); //0-8
                JButton tile = board[num];

                //if tile is occupied by plant, skip tile for this turn
                if (currPlantTile[0] == tile || currPlantTile[1] == tile) return;

                //set tile to mole
                currMoleTile = tile;
                currMoleTile.setIcon(moleIcon);
            }
        });

        setPlantTimer = new Timer(plantTimerDelay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // set plant #1

                //remove icon from current tile
                if (currPlantTile[0] != null) {
                    currPlantTile[0].setIcon(null);
                    currPlantTile[0] = null;
                }

                //randomly select another tile
                int num = random.nextInt(9); //0-8
                JButton tile = board[num];

                //if tile is occupied by mole, skip tile for this turn
                if (currMoleTile == tile) return;

                //set tile to mole
                currPlantTile[0] = tile;
                currPlantTile[0].setIcon(plantIcon);

                // set plant #2

                //remove icon from current tile
                if (currPlantTile[1] != null) {
                    currPlantTile[1].setIcon(null);
                    currPlantTile[1] = null;
                }

                //randomly select another tile
                int num2 = random.nextInt(9); //0-8
                //make sure the second plant is not on the same tile as the first plant
                while (num2 == num) {
                    num2 = random.nextInt(9); //0-8
                }
                JButton tile2 = board[num2];

                //if tile is occupied by mole, skip tile for this turn
                if (currMoleTile == tile2) return;

                //set tile to mole
                currPlantTile[1] = tile2;
                currPlantTile[1].setIcon(plantIcon);
            }
        });

        buttonPanel.setLayout(new BorderLayout());
        JButton restartButton = new JButton("Restart Game");
        restartButton.setFocusable(false);
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gameOver) {
                    // reset game state except for high score
                    System.out.println("Restarting game...");
                    gameOver = false;
                    score = 0;
                    for (int i = 0; i < 9; i++) {
                        board[i].setEnabled(true);
                    }
                    textLabel.setText("Score: " + Integer.toString(score) + " Best: " + Integer.toString(highScore));
                    moleTimerDelay = 1000; // Reset mole timer delay
                    plantTimerDelay = 1500; // Reset plant timer delay
                    setMoleTimer.setDelay(moleTimerDelay); // Reset mole timer delay
                    setPlantTimer.setDelay(plantTimerDelay); // Reset plant timer delay
                    setMoleTimer.start();
                    setPlantTimer.start();
                }
            }
        });
        buttonPanel.add(restartButton, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        setMoleTimer.start();
        setPlantTimer.start();
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
    }
}