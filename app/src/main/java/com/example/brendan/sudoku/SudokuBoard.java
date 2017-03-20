package com.example.brendan.sudoku;

import java.util.ArrayList;
import java.util.Random;

/*!
 * @author Brendan Beerman, B00592578, br794640@dal.ca
 *
 * @brief SudokuBoard       Generates and keeps track of the users sudoku game
 */

public class SudokuBoard
{
    private int difficulty;
    private int minimunOperations;
    private int maximumOperations;

    private char [][] userBoard;
    private char [][] initialBoard;
    private char [][] answerBoard;
    private int sizeOfBoard;

    // This sudoku was given from the assignment details
    private char [][] baseBoard = new char [][] {{'5','3','4','6','7','8','9','1','2'},
                                                 {'6','7','2','1','9','5','3','4','8'},
                                                 {'1','9','8','3','4','2','5','6','7'},
                                                 {'8','5','9','7','6','1','4','2','3'},
                                                 {'4','2','6','8','5','3','7','9','1'},
                                                 {'7','1','3','9','2','4','8','5','6'},
                                                 {'9','6','1','5','3','7','2','8','4'},
                                                 {'2','8','7','4','1','9','6','3','5'},
                                                 {'3','4','5','2','8','6','1','7','9'}};

    /*!
     * @brief SudokuBoard       A constructor to initialize the board
     */
    SudokuBoard(int difficulty)
    {
        sizeOfBoard = 9;

        minimunOperations = 100;
        maximumOperations = 1000;

        this.difficulty = difficulty;

        initalizeBoards();
    }

    /*!
     * @brief getBoardSize      Returns the length of the board
     *
     * @return                  An int that represents the length of the board
     */
    public int getBoardSize()
    {
        return sizeOfBoard;
    }

    /*!
     * @brief getBoard      Gets one of the boards based on the paramters choice
     *
     * @param boardChoice   An int picks the board, 0 => the base board
     *                                              1 => the answer board
     *                                              2 => the initial board
     *                                              3 => the user board
     *                                              otherwise a blank board is returned
     *
     * @return              An ArrayList of strings that holds the values from the chosen board
     */
    public ArrayList<String> getBoard(int boardChoice)
    {
        ArrayList<String> boardNumbers = new ArrayList<String>();
        char [][] boardToReturn;

        switch(boardChoice)
        {
            case 0:
                boardToReturn = char2dClone(baseBoard);
                break;
            case 1:
                boardToReturn = char2dClone(answerBoard);
                break;
            case 2:
                boardToReturn = char2dClone(initialBoard);
                break;
            case 3:
                boardToReturn = char2dClone(userBoard);
                break;

            default:
                boardToReturn = new char [sizeOfBoard][sizeOfBoard];
                break;
        }

        for (int i = 0; i < sizeOfBoard; i++)
        {
            for (int j = 0; j < sizeOfBoard; j++)
                boardNumbers.add(Character.toString(boardToReturn[i][j]));
        }

        return boardNumbers;
    }

    /*!
     * @brief setUserBoard      Sets the user board based on the SudokuGameActivity boards values
     *
     * @param updatedUserValues An ArrayList of strings of the new user board values
     */
    public void setUserBoard(ArrayList<String> updatedUserValues)
    {
        int currentValue = 0;

        for (int i = 0; i < sizeOfBoard; i++)
        {
            for (int j = 0; j < sizeOfBoard; j++)
            {
                userBoard[i][j] = updatedUserValues.get(currentValue).charAt(0);

                currentValue++;
            }
        }
    }

    /*!
     * @brief resetUsrBoard     Turns the user board back into its initial starting point
     */
    public void resetUserBoard()
    {
        userBoard = char2dClone(initialBoard);
    }

    /*!
     * @brief initializeBoards      The driver to initialize all boards and generate a new game board
     */
    private void initalizeBoards()
    {
        initialBoard = char2dClone(baseBoard);
        generateSudokuBoard();

        answerBoard = char2dClone(initialBoard);

        removeNumbersForUserBoard();
        userBoard = char2dClone(initialBoard);
    }

    /*!
     * @brief generateSudokuBoard       Randomly picks an amount of operations to do to edit the sudoku board
     */
    private void generateSudokuBoard()
    {
        Random intGenerator = new Random();

        // between 100 and 1000 operations
        int numberOfOperations = intGenerator.nextInt(maximumOperations - minimunOperations) + minimunOperations;

        for (int i = 0; i < numberOfOperations; i++)
            boardGenerationStep(intGenerator);
    }

    /*!
     * @brief boardGenerationStep       Picks a random step to perform onto the board to edit the board
     *
     * @param intGenerator              A random number generator to produce an int
     */
    private void boardGenerationStep(Random intGenerator)
    {
        switch (intGenerator.nextInt(10))
        {
            case 0:
                swapFromSameRowColumnBlock(intGenerator, true);
                break;

            case 1:
                swapHorizontalVerticalBlocks(intGenerator, true);
                break;

            case 2:
                swapFromSameRowColumnBlock(intGenerator, false);
                break;

            case 3:
                swapHorizontalVerticalBlocks(intGenerator, false);
                break;

            case 4:
                transpose();
                break;

            case 5:
                rotate90Degrees(true);
                break;

            case 6:
                rotate90Degrees(false);
                break;

            case 7:
                rotate180Degrees();
                break;

            case 8:
                rotateSectionsAroundCentre(intGenerator, true);
                break;

            case 9:
                rotateSectionsAroundCentre(intGenerator, false);
                break;
        }
    }

    /*!
     * @brief swapFromSameRowColumnBlock        Swaps two columns from the same block, block is a group of three columns or rows, ie rows 1-3, 4-6, and 7-9 are all blocks
     *
     * @param intGenerator                      A random number generator to produce an int
     * @param isRow                             A boolean to tell if a row or column is being swapped, true => row is being swapped
     *                                                                                                 false => column is being swapped
     */
    private void swapFromSameRowColumnBlock(Random intGenerator, boolean isRow)
    {
        int firstChosenSection = intGenerator.nextInt(9);
        int secondChosenSection;

        // these are all the first columns or rows to pick a second one that belong to it
        if (firstChosenSection == 0 || firstChosenSection == 3 || firstChosenSection == 6)
        {
            if (intGenerator.nextInt(2) == 0)
                secondChosenSection = firstChosenSection + 1;
            else
                secondChosenSection = firstChosenSection + 2;
        }
        else if (firstChosenSection == 1 || firstChosenSection == 4 || firstChosenSection == 7)
        {
            if (intGenerator.nextInt(2) == 0)
                secondChosenSection = firstChosenSection + 1;
            else
                secondChosenSection = firstChosenSection - 1;
        }
        else
        {
            if (intGenerator.nextInt(2) == 0)
                secondChosenSection = firstChosenSection - 1;
            else
                secondChosenSection = firstChosenSection - 2;
        }

        if (isRow)
            swapRows(firstChosenSection, secondChosenSection);
        else
            swapColumns(firstChosenSection, secondChosenSection);
    }

    /*!
     * @brief swapRows          Changes two rows with one another
     *
     * @param firstRowIndex     An int of which row is being swapped
     * @param secondRowIndex    An int of which row is being swapped
     */
    private void swapRows(int firstRowIndex, int secondRowIndex)
    {
        char [] firstRow = initialBoard[firstRowIndex].clone();
        char [] secondRow = initialBoard[secondRowIndex].clone();

        for (int i = 0; i < firstRow.length; i++)
        {
            initialBoard[firstRowIndex][i] = secondRow[i];
            initialBoard[secondRowIndex][i] = firstRow[i];
        }

    }

    /*!
     * @brief swapHorizontalVerticalBlocks      Swaps vertical or horizontal blocks with one another
     *
     * @param intGenerator                      A random number generator to produce an int
     * @param isHorizontal                      A boolean to tell if rows or columns are being swapped, true => rows being swapped
     *                                                                                                  false => columns are being swapped
     */
    private void swapHorizontalVerticalBlocks(Random intGenerator, boolean isHorizontal)
    {
        int firstChosenBlock = intGenerator.nextInt(3);
        int secondChosenBlock;

        while ((secondChosenBlock = intGenerator.nextInt(3)) == firstChosenBlock);

        int [] firstBlockSections;

        if (firstChosenBlock == 0)
            firstBlockSections = new int[]{0, 1, 2};
        else if (firstChosenBlock == 1)
            firstBlockSections = new int[]{3, 4, 5};
        else
            firstBlockSections = new int[]{6, 7, 8};

        int [] secondBlockSections;

        if (secondChosenBlock == 0)
            secondBlockSections = new int[]{0, 1, 2};
        else if (secondChosenBlock == 1)
            secondBlockSections = new int[]{3, 4, 5};
        else
            secondBlockSections = new int[]{6, 7, 8};

        for (int i = 0; i < firstBlockSections.length; i++)
        {
            if (isHorizontal)
                swapRows(firstBlockSections[i], secondBlockSections[i]);
            else
                swapColumns(firstBlockSections[i], secondBlockSections[i]);
        }
    }

    /*!
     * @brief swapColumns               A function to change two columns with one another
     *
     * @params firstChosenColumn        An int of the first column's index
     * @params secondChosenColumn       An int of the second column's index
     */
    private void swapColumns(int firstChosenColumn, int secondChosenColumn)
    {
        char [] firstColumn = getBoardColumn(firstChosenColumn);
        char [] secondColumn = getBoardColumn(secondChosenColumn);

        for (int i = 0; i < firstColumn.length; i++)
        {
            initialBoard[i][firstChosenColumn] = secondColumn[i];
            initialBoard[i][secondChosenColumn] = firstColumn[i];
        }
    }

    /*!
     * @brief getBoardColumn        Gets a given column from the sudoku board
     *
     * @param chosenColumn          The column to collect its values
     *
     * @return                      A char array of the values of that column
     */
    private char [] getBoardColumn(int chosenColumn)
    {
        char [] column = new char[sizeOfBoard];

        for (int i = 0; i < column.length; i++)
            column[i] = initialBoard[i][chosenColumn];

        return column;
    }

    /*!
     * @brief tranpose      Swaps the rows with the columns of the sudoku board
     */
    private void transpose()
    {
        char [][] copyOfInitalBoard = char2dClone(initialBoard);

        for (int i = 0; i < sizeOfBoard; i++)
        {
            for (int j = 0; j < sizeOfBoard; j++)
                initialBoard[i][j] = copyOfInitalBoard[j][i];
        }
    }

    /*!
     * @brief rotate90Degrees       Rotates the board 90 degrees to the right or left, by transposing and reversing the rows
     *
     * @param rotateRight           A boolean to tell if the board will rotate left or right, true => rotate right
     *                                                                                        false => rotate left
     */
    private void rotate90Degrees(boolean rotateRight)
    {
        // transposing first makes the board rotate right
        if (rotateRight)
            transpose();

        char [] currentRow;

        for (int i = 0; i < sizeOfBoard; i++)
        {
            currentRow = initialBoard[i].clone();

            for (int j = 0; j < sizeOfBoard; j++)
                initialBoard[i][j] = currentRow[currentRow.length - j - 1];
        }

        if (!rotateRight)
            transpose();
    }

    /*!
     * @brief roate180Degrees       Rotates the board 90 degrees to the right twice
     */
    private void rotate180Degrees()
    {
        rotate90Degrees(true);
        rotate90Degrees(true);
    }

    /*!
     * @brief rotateSectionsAroundCentre        Rotates a pair of rows around the center row or columns, ie row/column (1, 9), (2, 8), (3, 7), or (4, 6) can all be swapped with one another
     *
     * @param intGenerator                      A random number generator to produce an int
     * @param rotateRows                        A boolean to tell if the rows or columns are being rotated, true => swapping rows
     *                                                                                                      false => swapping columns
     */
    private void rotateSectionsAroundCentre(Random intGenerator, boolean rotateRows)
    {
        int chosenSection;

        while ((chosenSection = intGenerator.nextInt(9)) == 4);

        if (rotateRows)
            swapRows(chosenSection, sizeOfBoard - chosenSection - 1);
        else
            swapColumns(chosenSection, sizeOfBoard - chosenSection - 1);
    }

    /*!
     * @brief removeNumbersForUserBoard     Removes numbers from the board based on the difficulty
     */
    private void removeNumbersForUserBoard()
    {
        int amountToClear = setAmountOfNumbersToClear();
        int amountRemovedSoFar = 0;

        int emptyRowsColumnsSoFar = 0;
        int emptyRowsColumnsAllowed = difficulty + difficulty;

        Random intGenerator = new Random();
        int rowPosition;
        int columnPosition;

        while (amountRemovedSoFar != amountToClear)
        {
            rowPosition = intGenerator.nextInt(9);
            columnPosition = intGenerator.nextInt(9);

            if (initialBoard[rowPosition][columnPosition] == ' ')
                continue;
            else if (emptyRowsColumnsSoFar == emptyRowsColumnsAllowed && makesRowOrColumnEmpty(rowPosition, columnPosition))
                continue;
            else if (emptyRowsColumnsSoFar != emptyRowsColumnsAllowed && makesRowOrColumnEmpty(rowPosition, columnPosition))
                emptyRowsColumnsSoFar++;

            initialBoard[rowPosition][columnPosition] = ' ';

            amountRemovedSoFar++;
        }
    }

    /*!
     * @brief setAmountOfNumbersToClear     Sets the amount of numbers to remove from the sudoku board based on the difficulty
     */
    private int setAmountOfNumbersToClear()
    {
        Random intGenerator = new Random();

        if (difficulty == 0)
            return intGenerator.nextInt(51 - 46) + 46;
        else if (difficulty == 1)
            return intGenerator.nextInt(56 - 52) + 52;
        else
            return intGenerator.nextInt(61 - 57) + 57;
    }

    /*!
     * @brief makesRowOrColumnEmpty     A driver that checks if removing a number makes a row or a column empty
     *
     * @param row                       An int of which row to check
     * @param column                    An int of which column to check
     *
     * @return                          A boolean if either the row or column will become empty, true => the row or column becomes empty
     *                                                                                           false => neither become empty
     */
    private boolean makesRowOrColumnEmpty(int row, int column)
    {
        return makesRowColumnEmptyCheck(row, true) || makesRowColumnEmptyCheck(column, false);
    }

    /*!
     * @brief makesRowColumnEmptyCheck      Checks a give section will become completely empty
     *
     * @param section                       An int of which column or row to check
     * @param isRow                         A boolean to tell if a row or column is being checked, true => is a row
     *                                                                                             false => is a column
     *
     * @return                              A boolean to tell if the row or column will become empty, true => the section will become empty
     *                                                                                                 false => the section will become empty
     */
    private boolean makesRowColumnEmptyCheck(int section, boolean isRow)
    {
        int amountOfEmptyCells = 0;

        for (int i = 0; i < sizeOfBoard; i++)
        {
            if (isRow)
            {
                if (initialBoard[section][i] == ' ')
                    amountOfEmptyCells++;
            }
            else
            {
                if (initialBoard[i][section] == ' ')
                    amountOfEmptyCells++;
            }
        }

        if (amountOfEmptyCells == 8)
            return true;
        else
            return false;
    }

    /*!
     * @brief validSolution     Checks if the user board matches the answer board
     *
     * @return                  A boolean to tell if the solution is valid or not, true => the boards match
     *                                                                             false => the boards do not match
     */
    public boolean validSolution()
    {
        boolean areDifferences = false;

        for (int i = 0; i < sizeOfBoard; i++)
        {
            for (int j = 0; j < sizeOfBoard; j++)
            {
                if (userBoard[i][j] != answerBoard[i][j])
                    return false;
            }
        }

        return true;
    }

    /*!
     * @brief char2dClone       Copies a 2D char array from one to another
     *
     * @param toClone           The 2D char array to copy
     *
     * @return                  A 2D char array that is a copy of the original
     */
    private char [][] char2dClone(char [][] toClone)
    {
        char [][] copy = new char [sizeOfBoard][];

        for (int i = 0; i < sizeOfBoard; i++)
            copy[i] = toClone[i].clone();

        return copy;
    }

}
