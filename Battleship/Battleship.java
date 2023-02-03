import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;


/**
 * This class is an independent project which is based on the game Battleship. It explores 
 * GUIS in the Java language as well as Multithreading. The player has a 2-dimensional board
 * and must guess all of the coordinates of the enemy's battleships within 40 moves. If the 
 * player is unable to do so, then they lose, otherwise, they win. 
 */
public class GameTry extends JFrame
{
    // Create global vars for access within all methods
    private final static int NUMBER_OF_GAME_SLOTS = 14;
    private JButton[][] coordinates;
    private JButton button;
    private JLabel coordinatePressed;
    private JButton tile;
    private int playerScore;
    private JLabel failure;
    private JLabel ship;
    private JLabel title;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel centerPanel;
    private JLabel score;
    private JLabel coordinate;
    private JLabel hit;
    private String[][] gameBoard;
    private final static int MAXIMUM_SCORE = 40; 
    private final static int MINIMUM_SCORE = 0; 
    private int playerMoves; 
    public maps enemyBoard; 
    public boolean gameOver;

    /**
     * Sets up the components of the GUI
     */
    GameTry() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {  
        initializeStarterVariables();
        
        // MyThread music = new MyThread("music.wav", true);
        // music.start();
        createFrame();
        createPanels();
        createTitleButton();
        createTitleScreenLabels();
        

        // add panels to the JFrame
        this.add(topPanel, BorderLayout.NORTH);
        this.add(bottomPanel, BorderLayout.SOUTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    

    }
    /**
     * Initializes the starting variables. 
     * These will need to be globablly accessed by multiple
     * different methods. 
     */
    private void initializeStarterVariables()
    {
        button = new JButton();
        tile = new JButton();
        failure = new JLabel();
        ship = new JLabel();
        title = new JLabel();
        topPanel = new JPanel();
        bottomPanel = new JPanel();
        centerPanel = new JPanel();
        score = new JLabel();
        coordinate = new JLabel();
        hit = new JLabel();
        playerScore = 0; 
        playerMoves = 40; 
        gameOver = false;
    }
    /**
     * Creates the JFrame 
     */ 
    private void createFrame()
    {
        this.setSize(600,600);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        
    }
    /**
     * Creates the panels 
     */
    private void createPanels()
    {
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(0x184488));
        topPanel.setPreferredSize(new Dimension(100,100));

        bottomPanel.setBackground(new Color(0x184488));
        bottomPanel.setPreferredSize(new Dimension(100,150));
        bottomPanel.setLayout(null);
        
        centerPanel.setBackground(new Color(0x184488));
        centerPanel.setLayout(new BorderLayout());
        
    }
    /**
     * Creates the labels for the screen 
     */
    private void createTitleScreenLabels()
    {   
        // Set main screen icon 
        ImageIcon battleship = new ImageIcon("images/battleship.png");
        ship.setIcon(battleship);
        ship.setVerticalAlignment(JLabel.BOTTOM);
        centerPanel.add(ship);

        // customize text of JLabel title
        title.setText("BATTLE SHIP");
        title.setIcon(new ImageIcon("images/explosion.png"));
        title.setForeground(new Color(0xEEB22F));
        title.setFont(new Font("Wide Latin", Font.BOLD , 40));
        
        // set positioning of JLabel title
        title.setVerticalAlignment(JLabel.CENTER);
        title.setHorizontalAlignment(JLabel.CENTER);
        topPanel.add(title);

        JLabel ocean = new JLabel(new ImageIcon("images/ocean.gif"));
        ocean.setBounds(0,0,600,200);
        bottomPanel.add(ocean);
        
    }
    /**
     * Creates button on home screen 
     */
    private void createTitleButton()
    {
        // set font of button
        JButton button = new JButton();
        button.setText("COMMENCE BATTLE");
        button.setFont(new Font("Baskerville", Font.BOLD, 25));
        button.setForeground(new Color(0x184488));
        button.setOpaque(true);
        button.setFocusable(false);
        
        // set color of the button
        button.setBackground(new Color(0xEEB22F));
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setBounds(150,50,300,50);
        bottomPanel.add(button);
        button.setHorizontalAlignment(JButton.CENTER);
        button.setVerticalAlignment(JButton.CENTER);
        
        /*
         * Adds functionality to the button by implementing 
         * ActionListener interface. 
         */
        button.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                try{
                    
                    MyThread missile = new MyThread("shot.wav", false);
                    missile.start();   
                    nextWindow();
                    
                }
                catch(Exception exc)
                {

                }
            }
        });
    }
    /**
     * Changes the game to the next window 
     * @throws UnsupportedAudioFileException If the audio file is not supported
     * @throws IOException  If the file cannot be opened or found. 
     * @throws LineUnavailableException
     */
    private void nextWindow() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
           this.remove(bottomPanel);
           setEnemyBoard();
           
           topPanel.setLayout(new BorderLayout());
           title.setText("COORDINATES");
           topPanel.remove(title);
           topPanel.add(title, BorderLayout.NORTH);
           score.setText("MOVES REMAINING: " + playerMoves);
           score.setFont(new Font("Baskerville", Font.BOLD, 20));
           score.setForeground(Color.WHITE);
           
           coordinatePressed = new JLabel();
           coordinatePressed.setText("LAST COORDINATE PRESSED: ");
           coordinatePressed.setFont(new Font("Baskerville", Font.BOLD, 20));
           coordinatePressed.setForeground(Color.WHITE);
           failure.setFont(new Font("Helvetica", Font.BOLD, 20));
           coordinatePressed.setLayout(new BorderLayout());
           coordinatePressed.add(failure, BorderLayout.EAST);
           topPanel.add(score, BorderLayout.CENTER);
           topPanel.add(coordinatePressed, BorderLayout.SOUTH);
           
           failure.setVisible(false);
           score.setVerticalAlignment(JLabel.BOTTOM);
           score.setHorizontalAlignment(JLabel.CENTER);
           coordinatePressed.setHorizontalAlignment(JLabel.CENTER);
          
    
           centerPanel.setLayout(new GridLayout(0,11,1,1));
           this.setResizable(true);
           ship.setVisible(false);
           this.setBackground(Color.black);
           createBoard();
        
     }
     /**
      * Creates the game board 
      */
   private void createBoard()
   {
        String[] characters = {"", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String[] coords = {"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
       coordinates = new JButton[11][11];

        for(int i = 0; i < coordinates.length; i++)
        { 
            for(int j = 0; j < coordinates[i].length; j++)
            {
                tile = coordinates[i][j];
                tile = new JButton();
                tile.setText(characters[i]+coords[j]);
                
                if(tile.getText().length() == 1 || tile.getText().equals("10"))
                {
                    tile.setBackground(Color.black);
                    tile.setOpaque(true);
                    tile.setFont(new Font("Wide Latin", Font.BOLD, 20));
                    tile.setForeground(new Color(212,175,55));
                    tile.setBorder(BorderFactory.createLineBorder(Color.white,1));
                    centerPanel.add(tile);
                    tile.setVisible(true);
                }
                else if( tile.getText().length() == 2 || tile.getText().length() == 3 )
                {
                    tile.setBackground(new Color(0,128,128));
                    tile.setOpaque(true);
                    tile.setFont(new Font("Wide Latin", Font.BOLD, 10));
                    tile.setForeground(Color.white);
                    tile.setBorder(BorderFactory.createRaisedBevelBorder());
                    centerPanel.add(tile);
                    tile.setVisible(true);
                    makeButtonClickable(tile, i, j);
                }
                else
                {
                    tile.setVisible(false);
                    tile.setBackground(Color.white);
                }
            }
        }
            
   }
   private void setEnemyBoard()
   {
     enemyBoard = new maps();
     gameBoard = enemyBoard.returnMap();
   }
   /**
    * Makes each button on the game board clickable
    * @param clickable The JButton to make clickable. 
    * @param i The x-coord of button on board.
    * @param j The y-coord of button on board. 
    */
   private void makeButtonClickable(JButton clickable, int i, int j)
   {
        final int row = i;
        final int col = j;
        clickable.addActionListener( new ActionListener()
         {
            public void actionPerformed(ActionEvent e) 
            {
                // cast ref from OBJ -> JButton
                JButton clicked = (JButton)e.getSource();
                clicked.setEnabled(false);
                clicked.setBackground(new Color(1,77,78));
                                
                // add button first ... then set icon
                clicked.add(hit);
                clicked.setLayout(null);

                hit.setIcon(chooseIcon(row, col));
                coordinatePressed.setText("LAST COORDINATE PRESSED: "+ clicked.getActionCommand());
                                
                hit.setBounds(6,2,50,20);
                hit.setVisible(true);
                                
                checkMoves();
                                
                // have button emit sound
                MyThread beep = new MyThread("beep.wav", false);
                beep.start();
                                
            }
        });
                    
   }
   /**
    * Sets the X icon on the board to represent when a button has been
    * pressed. 
    * @param row The row button located in on board.
    * @param col The col button located in on board.
    * @return A green x if it is a hit else, the red icon. 
    */
   private ImageIcon chooseIcon(int row, int col)
   {
        ImageIcon image; 
        failure.setVisible(false);

        if(gameBoard[row-1][col-1].equals("X"))
        {
            playerScore++;
            failure.setText("HIT");
            failure.setForeground(Color.GREEN);
            image = new ImageIcon("images/greenx.png");
        }
        else
        {
            failure.setText("MISS");
            failure.setForeground(Color.RED);
            image =  new ImageIcon("images/redx.png");
        }
        failure.setVisible(true);
        return image;
            
   }
   /**
    * Checks if the game has ended yet by checking player moves. 
    */
   private void checkMoves()
   {
        if(playerMoves >= 0 && !gameOver)
       {
        playerMoves--; 
        score.setText("MOVES REMAINING: " + playerMoves);
        
        if(playerScore == NUMBER_OF_GAME_SLOTS || playerMoves == MINIMUM_SCORE)
        {
            gameOver = true; 
            winGame(playerScore == NUMBER_OF_GAME_SLOTS);
        }
    }
   }
    /**
     * Clears the board so that the next window can be displayed. 
     * @param item The panel to clear. 
     */
   private void clearBoard(JPanel item)
   {
        item.removeAll();
        item.revalidate();
        item.repaint();
        item.setLayout(new BorderLayout());
   }
   /**
    * Displays final stats to player after the game has been won or lost. 
    * @param hasWon A boolean flag to indicate if the game has been won or not 
    */
   private void winGame(boolean hasWon)
   {
        clearBoard(topPanel);
        if(hasWon)
            title.setText("YOU WIN! ");
        else
            title.setText("YOU LOSE!");
        topPanel.setLayout(new BorderLayout());
        score.setText("Total Moves: " + (MAXIMUM_SCORE - playerMoves));
        score.setFont(new Font("Baskerville", Font.BOLD, 25));
        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(score, BorderLayout.SOUTH);
   }
   /**
    * Runs the game. 
    */
    public static void main(String[] args) 
      {
         SwingUtilities.invokeLater(new Runnable() 
         {
             public void run() 
           {
                 try{
                    new GameTry();
                 }
                catch(Exception e)
               {

                 }
            }
       });
    }

}
