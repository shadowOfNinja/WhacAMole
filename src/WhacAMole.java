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
    JButton currPlantTile;

    Random random = new Random();
    Timer setMoleTimer;
    Timer setPlantTimer;
    int score = 0;
    int highScore = 0;
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
                    }
                    else if (tile == currPlantTile) {
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

        setMoleTimer = new Timer(1000, new ActionListener() {
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
                if (currPlantTile == tile) return;

                //set tile to mole
                currMoleTile = tile;
                currMoleTile.setIcon(moleIcon);
            }
        });

        setPlantTimer = new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //remove icon from current tile
                if (currPlantTile != null) {
                    currPlantTile.setIcon(null);
                    currPlantTile = null;
                }

                //randomly select another tile
                int num = random.nextInt(9); //0-8
                JButton tile = board[num];

                //if tile is occupied by mole, skip tile for this turn
                if (currMoleTile == tile) return;

                //set tile to mole
                currPlantTile = tile;
                currPlantTile.setIcon(plantIcon);
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
                    setMoleTimer.start();
                    setPlantTimer.start();
                }
            }
        });
        buttonPanel.add(restartButton, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        setMoleTimer.start();
        setPlantTimer.start();
        frame.setVisible(true);
    }
}


/*
 * Homework
 * - Add Multiple Piranha plants and store them in an Array
 */