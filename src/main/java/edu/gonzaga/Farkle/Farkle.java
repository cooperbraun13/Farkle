package edu.gonzaga.Farkle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class Farkle {
    Meld meld;
    Hand hand;
    private Player player;

    // Main game GUI window and two main panels (left & right)
    JFrame mainWindowFrame;
    JPanel controlPanel;
    JPanel scorecardPanel;

    // Dice view, user input, reroll status, and reroll button
    JTextField diceValuesTextField;
    JTextField diceKeepStringTextField;
    JButton diceRerollBtn;
    JTextField rerollsLeftTextField;

    // Player name - set it to your choice
    JTextField playerNameTextField = new JTextField();

    // Buttons for showing dice and checkboxes for meld include/exclude
    ArrayList<JButton> diceButtons = new ArrayList<>();
    ArrayList<JCheckBox> meldCheckboxes = new ArrayList<>();

    JButton rerollUnmeldedButton = new JButton("Reroll Unmelded Dice");
    JButton bankPointsButton = new JButton("Bank Points");
    JTextField diceDebugLabel = new JTextField();
    JLabel meldScoreTextLabel = new JLabel();
    JLabel totalScoreTextLabel = new JLabel();
    JButton rollButton = new JButton();

    JPanel playerInfoPanel = new JPanel();
    JPanel diceControlPanel = new JPanel();
    JPanel meldControlPanel = new JPanel();

    DiceImages diceImages = new DiceImages("media/");

    public static void main(String[] args) {
        Farkle app = new Farkle();    // Create, then run GUI
        app.runGUI();
    }

    // Constructor for the actual Farkle object
    public Farkle() {
        hand = new Hand(6);
        meld = new Meld(); // initialize meld object
    }

    private void animatedRoll() {
        Timer timer = new Timer(100, null); // 100 ms delay
        int animationDuration = 1000; // milliseconds
        long startTime = System.currentTimeMillis();
    
        // actionlistener for the timer
        ActionListener animationAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - startTime >= animationDuration) {
                    // stop the animation then roll the dice for real
                    ((Timer) e.getSource()).stop();
                    hand.roll();
                    updateDiceDisplay();
                } else {
                    // display random values to simulate dice rolling
                    for (int i = 0; i < diceButtons.size(); i++) {
                        int randomValue = (int) (Math.random() * 6) + 1;
                        diceButtons.get(i).setIcon(diceImages.getDieImage(randomValue)); // sets random dice image
                    }
                }
            }
        };
    
        timer.addActionListener(animationAction);
        timer.start(); // Start the animation timer
    }
    

    // Sets up the full Swing GUI, but does not do any callback code
    void setupGUI() {
        this.mainWindowFrame = new JFrame("Cooper's GUI Farkle");
        this.mainWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainWindowFrame.setLocation(100, 100);

        // Player info and roll button panel
        this.playerInfoPanel = genPlayerInfoPanel();

        // Dice status and checkboxes to show the hand and which to include in the meld
        this.diceControlPanel = genDiceControlPanel();

        // The bottom Meld control panel
        this.meldControlPanel = genMeldControlPanel();

        mainWindowFrame.getContentPane().add(BorderLayout.NORTH, this.playerInfoPanel);
        mainWindowFrame.getContentPane().add(BorderLayout.CENTER, this.diceControlPanel);
        mainWindowFrame.getContentPane().add(BorderLayout.SOUTH, this.meldControlPanel);
        mainWindowFrame.pack();
    }

    private JPanel genMeldControlPanel() {
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new FlowLayout());

        newPanel.add(rerollUnmeldedButton);
        newPanel.add(bankPointsButton);
        newPanel.add(meldScoreTextLabel);
        newPanel.add(totalScoreTextLabel);

        return newPanel;
    }

    private JPanel genDiceControlPanel() {
        JPanel newPanel = new JPanel();
        newPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        newPanel.setLayout(new GridLayout(3, 7, 1, 1));
        JLabel diceLabel = new JLabel("Dice Vals:");
        JLabel meldBoxesLabel = new JLabel("Meld 'em:");

        newPanel.add(new Panel()); 
        for (Integer index = 0; index < 6; index++) {
            JLabel colLabel = new JLabel(Character.toString('A' + index), SwingConstants.CENTER);
            newPanel.add(colLabel);
        }
        newPanel.add(diceLabel);

        for (Integer index = 0; index < 6; index++) {
            JButton diceStatusButton = new JButton();
            diceStatusButton.setPreferredSize(new Dimension(40,50));
            this.diceButtons.add(diceStatusButton);
            newPanel.add(diceStatusButton);
        }

        newPanel.add(meldBoxesLabel);
        // add checkboxes with actionlistener to recalculate meld score when checked
        for (Integer index = 0; index < 6; index++) {
            JCheckBox meldCheckbox = new JCheckBox();
            meldCheckbox.setHorizontalAlignment(SwingConstants.CENTER);
            this.meldCheckboxes.add(meldCheckbox);
            
            // add ActionListener to each checkbox
            meldCheckbox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Reset the meld and add selected dice
                    meld.reset(); 
                    for (int i = 0; i < meldCheckboxes.size(); i++) {
                        if (meldCheckboxes.get(i).isSelected()) {
                            int dieValue = Integer.parseInt(diceButtons.get(i).getText());
                            meld.setMeldDie(i, dieValue); // Set the die value in the meld
                        }
                    }

                    // Calculate the score after adding the dice to the meld
                    int score = meld.calculateMeldScore(); 
                    meldScoreTextLabel.setText("Meld Score: " + score); // Update the score label
                }
            });

            newPanel.add(meldCheckbox);
        }

        return newPanel;
    }

    private JPanel genPlayerInfoPanel() {
        JPanel newPanel = new JPanel();
        newPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        newPanel.setLayout(new FlowLayout());    // Left to right

        JLabel playerNameLabel = new JLabel("Player name:");
        playerNameTextField.setColumns(20);
        diceDebugLabel.setColumns(6);
        diceDebugLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the text
        rollButton.setText("Roll Dice");

        newPanel.add(playerNameLabel);   // Add our player label
        newPanel.add(playerNameTextField); // Add our player text field
        newPanel.add(rollButton);        // Put the roll button on there
        newPanel.add(this.diceDebugLabel);

        return newPanel;
    }

    private void addRollDiceCallback() {
        this.rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animatedRoll();
                // hand.roll(); // Roll the dice
                // updateDiceDisplay();
            }
        });
    }

    private void updateDiceDisplay() {
        StringBuilder debugText = new StringBuilder();
        for (int i = 0; i < hand.getDiceCount(); i++) {
            int value = hand.getDie(i).getSideUp();
            debugText.append(value);
            diceButtons.get(i).setText(String.valueOf(value)); // Update button text
            ImageIcon diceImage = diceImages.getDieImage(value);
            diceButtons.get(i).setIcon(diceImage);
        }
        diceDebugLabel.setText(debugText.toString()); // Update debug text field
    }

    private void putDemoDefaultValuesInGUI() {
        // Example setting of player name
        this.playerNameTextField.setText("McLovin");

        // Example Dice debug output
        this.diceDebugLabel.setText("");
    }

    private void addDemoButtonCallbackHandlers() {
        this.rerollUnmeldedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reroll only the dice that are not in the meld
                //player.reroll(dice);
                for (int i = 0; i < hand.getDiceCount(); i++) {
                    if (!meldCheckboxes.get(i).isSelected()) {
                        hand.getDie(i).roll(); // Reroll the die
                    }
                }
                updateDiceDisplay(); // Update the dice display
            }
        });

        this.bankPointsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Bank points and reset the meld
                int meldScore = meld.calculateMeldScore();

                // Initialize the player if not already done
                if (player == null) {
                player = new Player(playerNameTextField.getText());
                }

                // Add the meld score to the player's total score
                player.addScore(meldScore);
                meld.reset(); // Reset the meld for the next round
                meldScoreTextLabel.setText("Meld Score: 0"); 
                totalScoreTextLabel.setText("Total Score: " + player.getTotalScore());

                for (JCheckBox checkbox : meldCheckboxes) {
                    checkbox.setSelected(false);
                }
            }
        });
    }

    public void runGUI() {
        System.out.println("Starting GUI app");
        this.setupGUI();

        this.putDemoDefaultValuesInGUI();
        this.addRollDiceCallback();
        this.addDemoButtonCallbackHandlers();

        this.mainWindowFrame.setVisible(true);

        System.out.println("Done in GUI app");
    }
}