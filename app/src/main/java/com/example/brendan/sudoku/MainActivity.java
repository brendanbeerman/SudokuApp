/*!
 * @author Brendan Beerman, B00592578, br794640@dal.ca
 *
 * @brief MainActivity      The MainActivity of the application that lets the user pick the difficulty of their sudoku puzzle
 *
 */

package com.example.brendan.sudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity
{
    /*!
     * @brief onCreate      The initial method called to kick off the start of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*!
     * @brief playGame      When a difficulty is selected the SudokuGameActivity needs to know what type of board to create
     */
    public void playGame(View view)
    {
        Intent intent;

        switch (view.getId())
        {
            case R.id.easyButton:
                intent = new Intent(MainActivity.this, SudokuGameActivity.class);
                intent.putExtra("Difficulty", 0);
                startActivity(intent);
                break;

            case R.id.mediumButton:
                intent = new Intent(MainActivity.this, SudokuGameActivity.class);
                intent.putExtra("Difficulty", 1);
                startActivity(intent);
                break;

            case R.id.hardButton:
                intent = new Intent(MainActivity.this, SudokuGameActivity.class);
                intent.putExtra("Difficulty", 2);
                startActivity(intent);
                break;
        }
    }
}
