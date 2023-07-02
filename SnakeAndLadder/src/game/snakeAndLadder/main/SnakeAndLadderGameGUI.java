package game.snakeAndLadder.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SnakeAndLadderGameGUI extends JFrame implements ActionListener {
    
    // game constants
    private static final int BOARD_SIZE = 100;
    private static final int NUM_SNAKES = 8;
    private static final int NUM_LADDERS = 7;
    private static final int WINNING_POSITION = 100;
    
    // array to store the snakes and ladders
    private static final int[] snakes = {27,40,43,54,66,89,95,99};
    private static final int[] snakesEnd = {5,3,18,31,45,53,77,41} ;
    private static final int[] ladders = {4,13,33,42,50,62,74};
    private static final int[] laddersEnd = {25,46,49,63,69,81,92} ;
    
    // GUI components
    private JLabel[] playerLabels;
    private JLabel[] positionLabels;
    private JButton rollButton;
    private JTextArea logArea;
    
    // game state
    private int numPlayers;
    private int[] playerPositions;
    private int currentPlayer;
    private boolean gameOver;
    
    public SnakeAndLadderGameGUI() {
        super("Snake and Ladder Game");
        System.out.print("Enter the number of players(2-4): ");
    	Scanner sc = new Scanner(System.in);
    	numPlayers = sc.nextInt();
        // set up main window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // set up player information panel
        JPanel playerPanel = new JPanel(new GridLayout(0, 2));
        add(playerPanel, BorderLayout.WEST);
        playerLabels = new JLabel[numPlayers];
        positionLabels = new JLabel[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            playerLabels[i] = new JLabel("Player " + (i+1) + ": ");
            playerPanel.add(playerLabels[i]);
            positionLabels[i] = new JLabel("1");
            playerPanel.add(positionLabels[i]);
        }
        
        // set up game board panel
        JPanel boardPanel = new JPanel(new GridLayout(10, 10));
        add(boardPanel, BorderLayout.CENTER);
        for (int i = 1; i <= BOARD_SIZE; i++) {
            JPanel squarePanel = new JPanel();
            squarePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            squarePanel.setPreferredSize(new Dimension(50, 50));
            JLabel squareLabel = new JLabel(Integer.toString(i));
            squarePanel.add(squareLabel);
            for (int j= 0; j< snakes.length; j++) {
            	if (i == snakes[j]) {
            		squarePanel.setBackground(Color.RED);
            	}
            }
            for (int k= 0; k< ladders.length; k++) {
            	if (i == ladders[k]) {
            		squarePanel.setBackground(Color.GREEN);
            	}
            }
            
            boardPanel.add(squarePanel);
        }
        
        // set up control panel
        JPanel controlPanel = new JPanel(new BorderLayout());
        add(controlPanel, BorderLayout.SOUTH);
        rollButton = new JButton("Player 1");
        rollButton.addActionListener(this);
        controlPanel.add(rollButton, BorderLayout.WEST);
        logArea = new JTextArea(5, 20);
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);
        controlPanel.add(logScrollPane, BorderLayout.CENTER);
        
        // initialize game state
        playerPositions = new int[numPlayers];
        currentPlayer = 0;
        gameOver = false;
        
        // show the window
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    // main method to start the game
    public static void main(String[] args) {
        new SnakeAndLadderGameGUI();
    }
    
    // event handler for roll dice button
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return;
        }
        
        // roll the dice
        int roll = rollDice();
        logArea.append("Player " + (currentPlayer+1) + " rolled a " + roll + "\n");
        
        // move the player
        if(playerPositions[currentPlayer] + roll > BOARD_SIZE) {
        	logArea.append("Sorry! you are exceeded the target.\n");
        	currentPlayer = (currentPlayer + 1) % numPlayers;
        	rollButton.setText("Player " + (currentPlayer + 1));
        	return ;
        }
        int newPosition = playerPositions[currentPlayer] + roll;
        if (newPosition > BOARD_SIZE) {
            newPosition = BOARD_SIZE - (newPosition - BOARD_SIZE);
        }
        playerPositions[currentPlayer] = newPosition;
        positionLabels[currentPlayer].setText(Integer.toString(newPosition));
        boolean flag = true ;
        if(roll == 6)
        	flag = false ;
        // check for snakes and ladders
        for (int i = 0; i < NUM_SNAKES; i++) {
            if (newPosition == snakes[i]) {
                int oldPosition = newPosition;
                newPosition = snakesEnd[i];
                playerPositions[currentPlayer] = newPosition;
                positionLabels[currentPlayer].setText(Integer.toString(newPosition));
                logArea.append("Player " + (currentPlayer+1) + " landed on a snake at " + oldPosition + " and slid down to " + newPosition + "\n");
                break;
            }
        }
        for (int i = 0; i < NUM_LADDERS; i++) {
            if (newPosition == ladders[i]) {
            	flag = false ;
                int oldPosition = newPosition;
                newPosition = laddersEnd[i];
                playerPositions[currentPlayer] = newPosition;
                positionLabels[currentPlayer].setText(Integer.toString(newPosition));
                logArea.append("Player " + (currentPlayer+1) + " landed on a ladder at " + oldPosition + " and climbed up to " + newPosition + "\n");
                break;
            }
        }
        
        // check for a win
        if (newPosition == WINNING_POSITION) {
            gameOver = true;
            logArea.append("Player " + (currentPlayer+1) + " wins!\n");
            rollButton.setEnabled(false);
        }
        
        // switch to the next player
        if(flag)
        	currentPlayer = (currentPlayer + 1) % numPlayers;
        rollButton.setText("Player " + (currentPlayer + 1));
    }

    // helper method to roll the dice
    private int rollDice() {
        return (int)(Math.random() * 6) + 1;
    }
}