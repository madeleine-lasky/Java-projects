import java.util.Random;
/*
 * An enummerated class which initializes the different types of ships.
 * Different types of ships occupy a certain number of spaces on the
 * board.
 *      AIRCRAFT CARRIER = 5 spaces
 *      BATTLESHIP = 4 spaces
 *      SUBMARINE = 3 spaces
 *      DESTROYER = 2 spaces
 */
enum Ships {
    // declare ship types and enact constructor to set number of spaces
    AIRCRAFT_CARRIER(5), BATTLESHIP(4), SUBMARINE(3), DESTROYER(2);

    private int spaces;     // field to indicate space for each type 

    /**
     * Constructor to initialze ship with number of spaces.
     * @param spaces The number of spaces that a ship takes up. 
     */
    Ships(int spaces)
    {
        this.spaces = spaces;
    }
    
    /**
     * Returns the number of spaces for the enumerated type.
     * @return the number of spaces occupied by the ship 
     */
    int getSpaces()
    {
        return spaces;
    }
}
/*
 * This class creates a valid board for battleship.
 * It assigns a valid placement for each of the ship types in
 * the enummerated list. Rules for the board include that ships 
 * may not share the same coordinates and the board DOES NOT
 * wrap around in terms of the coordinates. 
 */
public class maps
{
    public final int BOARD_DIMENSIONS = 10;
    public String[][] map;
    private Ships[] pieces; 
    private int count;
    private int totalPlaced;
    private Random choice;
    private final int MAX_SPACE = 14;
    private boolean integrityOK = false;
    // private Entry[] shipCoordinates;

    /**
     * A constructor to initialize the board. 
     */
    public maps()
    {
        // Initialize data fields
        pieces = Ships.values();
        count = 0;
        totalPlaced = 0; 
        choice = new Random();
        initializeMap();
        // shipCoordinates = new Entry[4];
        integrityOK = true;

        // Assign a placement for each
        // piece within the confines of the board 
        while(count != 4)
        {
            placePiece();
        }

        // Debugging: check the placement 
        displayBoards();
    }
    /**
     * Initializes the game board.
     */
    private void initializeMap()
    {
        map = new String[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
        
        for(int i = 0; i < map.length; i++)
            for(int j = 0; j < map[i].length; j++)
            {
                map[i][j] = "0";

            }

    }
    /** 
     * Checks to ensure if the game board is in a valid state.
     * @throws IllegalStateException If the constructor has not been called. 
     */
    public void checkValidity()
    {
        if(!integrityOK)
            throw new IllegalStateException("Board is not in valid state");
    }
    /**
     * Places ships onto the board using a random generator. 
     * If the random number generated == 0, then the next
     * ship piece is placed vertically. Otherwise, it is placed
     * horizontally. 
     */
    public void placePiece()
    {
        checkValidity();
        int placement = choice.nextInt(2);
        
        int col = choice.nextInt(10);
        int row = choice.nextInt(10);
        boolean direction = placement ==  0;
        
        boolean validity = false;

        while(!validity || (row + pieces[count].getSpaces() > 9) || (col + pieces[count].getSpaces() > 9))
        {
            row = choice.nextInt(10);
            col = choice.nextInt(10);
            validity = isValidPlacement(row , col, direction);
            
        }
        
        if(direction)
        {
            verticalPlacement(row, col);
            // shipCoordinates[count] = new Entry(row, col, true, pieces[count]);
        }
        else
        {
            horizontalPlacement(row, col);
            // shipCoordinates[count] = new Entry(row, col, false, pieces[count]);
        }   
        
        // Assertion: the piece has been placed onto the map 
        count++;
    }
    /**
     * Places a ship piece vertically on the board.
     * Precondition: checkValidity has already been called. 
     * @param row The starting row on the map to place piece( this is constant ).
     * @param col The column on the map to place piece. 
     */
    private void verticalPlacement(int row, int col)
    {
        for(int i = 0; i < pieces[count].getSpaces(); i++)
        {   
    
            if(totalPlaced == MAX_SPACE)
                break;
            else
            {
                map[row + i][col] = "X";
                totalPlaced++;
            }
        }
    }
    /**
     * Places a ship piece horizontally on the board.
     * Precondition: checkValidity has already been called.
     * @param row The row on the map to place piece ( this is constant ).
     * @param col The starting column on the map to place piece. 
     */
    private void horizontalPlacement(int row, int col)
    {
        for(int i = 0; i < pieces[count].getSpaces(); i++)
        {
            if(totalPlaced == MAX_SPACE)
                break;
            else   
            {
                map[row][col + i] = "X";
                totalPlaced++;
            }
        }
    }
    /**
     * Checks if the starting row and column is valid for the
     * current piece being placed.
     * Precondition: integrityOK has already been called 
     * @param row The row on the board to start placing piece.
     *      If checkRow == true, then increments of row will be checked.
     * @param col The column on the board to start placing the piece.
     *      If checkRow == false, then increments of column will be checked.
     * @param changeRow True if placement checked is for vertical placement else false. 
     * @return True if this starting position is valid, else false. 
     */
    private boolean isValidPlacement(int row, int col, boolean changeRow)
    {
        boolean validity = true;
        int countNum = 0;
        int increment = 0;
        while(validity && ( countNum !=  pieces[count].getSpaces() ) )
        {
            if( ( row + increment >= BOARD_DIMENSIONS ) || ( col + increment >= BOARD_DIMENSIONS ))
                validity = false;

            if(changeRow && (row + increment < BOARD_DIMENSIONS))
            {
                if(map[row + increment][col].equals("X"))
                {
                    validity = false;
                    break;
                }
            }
            else if(!changeRow && ( col + increment < BOARD_DIMENSIONS))
            {
                if(map[row][col+increment].equals("X"))
                {
                    validity = false;
                    break;
                }
            }
         
            increment++;
            countNum++;
        
        }
        return validity;
    }
    /**
     * A debugging print statement to show the coordinates 
     * of the board. 
     */
    public void displayBoards()
    {
        for(int i = 0; i < map.length; i++)
        {
            System.out.println();
            for(int j = 0; j < map[i].length; j++)
            {
                System.out.print(map[i][j] + " ");

            }
        }
        System.out.println();
    }
    public String[][] returnMap(){
        return this.map; 
    }

}
