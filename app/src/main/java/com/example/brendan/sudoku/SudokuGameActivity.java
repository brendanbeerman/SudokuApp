/*!
 * @author Brendan Beerman, B00592578, br794640@dal.ca
 *
 * @brief SudokuGameActivity        The GUI for playing the sudoku game
 *
 */

package com.example.brendan.sudoku;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class SudokuGameActivity extends AppCompatActivity
{
    private String currentCharacter = "e";
    private ArrayList<Button> boardButtons;
    private SudokuBoard sudoku;

    /*!
     * @brief onCreate      Creates the layout, sets the difficulty, and starts the set up for the board
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_game);

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            int difficultyValue = extras.getInt("Difficulty");

            sudoku = new SudokuBoard(difficultyValue);

            initialBoardSetUp();
        }
    }

    /*!
     * @brief initialBoardSetUp     Creates the buttons for the sudoku board and adds them to the grid layout
     */
    private void initialBoardSetUp()
    {
        createBoardButtons(81);

        ArrayList<String> buttonValues = sudoku.getBoard(3);
        updateButtonValues(buttonValues);

        addBoardButtonsToGrid();
    }

    /*!
     * @brief createBoardButtons        Sets up the buttons layouts and creates their onClick functionality
     *
     * @param boardSize                 An int that tells the function about how many buttons will need to be made
     */
    private void createBoardButtons(int boardSize)
    {
        boardButtons = new ArrayList<Button>();
        Button currentButton;

        for (int i = 0; i < boardSize; i++)
        {
            currentButton = new Button(this);

            currentButton.setText(" ");
            currentButton.setWidth(35);
            currentButton.setHeight(35);
            currentButton.setBackgroundColor(Color.rgb(0, 0, 0));
            currentButton.setBackground(getButtonShape());
            currentButton.setId(i);

            // When clicked it sets the text to that off the value of the 'currentCharacter' variable
            currentButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (currentCharacter != "e")
                    {
                        int counter = 0;

                        for (Button currentButton : boardButtons)
                        {
                            if (view.getId() == currentButton.getId())
                            {
                                if (currentCharacter == "c")
                                    currentButton.setText(" ");
                                else
                                    currentButton.setText(currentCharacter);

                                updateUserBoard(counter);

                                break;
                            }

                            counter++;
                        }
                    }
                }
            });

            boardButtons.add(currentButton);
        }
    }

    /*!
     * @brief updateUserBoard       When the user changes the text of a button the users board in the SudokuBoard class gets updated
     *
     * @param index                 An int that represents the number that was clicked
     */
    private void updateUserBoard(int index)
    {
        ArrayList<String> buttonValues = new ArrayList<String>();

        for (Button currentButton : boardButtons)
            buttonValues.add(currentButton.getText() + "");

        sudoku.setUserBoard(buttonValues);
    }

    /*!
     * @brief getButtonShape        Sets the shape and other variables for the buttons
     *
     * @return                      A ShapeDrawable object for the buttons
     */
    private ShapeDrawable getButtonShape()
    {
        ShapeDrawable buttonShape = new ShapeDrawable();
        buttonShape.setShape(new RectShape());
        buttonShape.getPaint().setColor(Color.WHITE);
        buttonShape.getPaint().setStrokeWidth(15f);

        return buttonShape;
    }

    /*!
     * @brief updateButtonValues        Updates the button text values
     *
     * @param buttonValues              An ArrayList of strings with the new values for the buttons
     */
    private void updateButtonValues(ArrayList<String> buttonValues)
    {
        ArrayList<Button> updatedButtons = new ArrayList<Button>();
        Button currentButton;

        for (int i = 0; i < boardButtons.size(); i++)
        {
            currentButton = boardButtons.get(i);
            currentButton.setText(buttonValues.get(i));

            updatedButtons.add(currentButton);
        }

        boardButtons = updatedButtons;
    }

    /*!
     * @brief addButtonsToGrid      Adds the buttons to the GUI
     */
    private void addBoardButtonsToGrid()
    {
        int totalColumns = 9;
        int currentRow = 0;
        int currentColumn = 0;

        GridLayout sudokuBoard = (GridLayout) findViewById(R.id.sudokuLayout);
        sudokuBoard.removeAllViews();

        for (Button currentButton : boardButtons)
        {
            if (currentColumn == totalColumns)
            {
                currentColumn = 0;
                currentRow++;
            }

            currentButton.setLayoutParams(getLayoutParams(currentRow, currentColumn));

            sudokuBoard.addView(currentButton);

            currentColumn++;
        }
    }

    /*!
     * @brief getLayoutParams       Makes a set of layout parameters for setting up a button on the grid layout
     *
     * @param row                   An int for which row the button will be placed in
     * @param column                An int for which column the button will be place in
     *
     * @return                      A GridLayout.LayoutParams object to set up variables for the placement of the button
     */
    private GridLayout.LayoutParams getLayoutParams(int row, int column)
    {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();

        params.width = 105;
        params.height = 105;
        params.topMargin = 1;
        params.rightMargin = 1;
        params.setGravity(Gravity.CENTER);
        params.rowSpec = GridLayout.spec(row);
        params.columnSpec = GridLayout.spec(column);

        return params;
    }

    /*!
     * @brief setCurrentCharacter       When a button from the keyboard grid layout is clicked the currentCharacter variable is updated to the buttons value
     */
    public void setCurrentCharacter(View view)
    {
        switch (view.getId())
        {
            case R.id.oneButton:
                currentCharacter = "1";
                break;

            case R.id.twoButton:
                currentCharacter = "2";
                break;

            case R.id.threeButton:
                currentCharacter = "3";
                break;

            case R.id.fourButton:
                currentCharacter = "4";
                break;

            case R.id.fiveButton:
                currentCharacter = "5";
                break;

            case R.id.sixButton:
                currentCharacter = "6";
                break;

            case R.id.sevenButton:
                currentCharacter = "7";
                break;

            case R.id.eightButton:
                currentCharacter = "8";
                break;

            case R.id.nineButton:
                currentCharacter = "9";
                break;

            case R.id.clearButton:
                currentCharacter = "c";
                break;
        }
    }

    /*!
     * @brief resetBoard        Makes the button values return to their original ones
     */
    public void resetBoard(View view)
    {
        sudoku.resetUserBoard();

        ArrayList<String> buttonValues = sudoku.getBoard(2);

        Button currentButton;

        for (int i = 0; i < boardButtons.size(); i++)
        {
            currentButton = (Button) findViewById(boardButtons.get(i).getId());
            currentButton.setText(buttonValues.get(i));
        }
    }

    /*!
     * @brief checkAnswer       Checks if the user's solution is correct or not
     */
    public void checkAnswer(View view)
    {
        // makes a toast if correct or incorrect, returns to main activity if it is correct
        if (sudoku.validSolution())
        {
            Toast victoryToast = Toast.makeText(getApplicationContext(), "  You have completed the sudoku!  ", Toast.LENGTH_SHORT);
            victoryToast.setGravity(Gravity.CENTER | Gravity.TOP, 25, 50);
            victoryToast.show();

            finish();
        }
        else
        {
            Toast errorToast = Toast.makeText(getApplicationContext(), "  There are errors in the sudoku  ", Toast.LENGTH_SHORT);
            errorToast.getView().setBackgroundColor(Color.RED);
            errorToast.setGravity(Gravity.CENTER | Gravity.TOP, 25, 50);
            errorToast.show();
        }
    }
}
