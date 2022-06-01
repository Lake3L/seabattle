package com.example.seabattle;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
private Interface sBattle;
private Game sGame;

    volatile boolean playerTurn = true;
    volatile boolean enemyTurn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initApp();
    }

    private void initApp() {
        setContentView(R.layout.activity_main);
    }

    public void battleStart(View v){
       sBattle = new Interface(this);
       sBattle.setOnTouchListener(this);
       setContentView(sBattle);
       sGame = new Game();
       //updating model
        sBattle.setBoard(sGame.getPlayerPlacement(), sGame.getEnemyPlacement());
        sBattle.invalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int cellSize = sBattle.getCellSize();
        int startBoardX = sBattle.getStartEnemyFieldX();
        int endBoardX = cellSize * PositionAndMove.N + startBoardX;
        int endBoardY = sBattle.getEndEnemyFieldY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //click only over enemy board to hit
                if (x >= startBoardX && y <= endBoardY &&
                        x <= endBoardX) {
                    int ip = y / cellSize;
                    int jp = (x - startBoardX) / cellSize;
                    return playerAttackEnemy(ip, jp);
                }

            case MotionEvent.ACTION_UP:
                enemyAttackPlayer();
            case MotionEvent.ACTION_MOVE:
                //user incorrect touch
        }
        return true;
    }


//      Attack enemy's ships.
//      @param i row position of cell
//      @param j column position of cell
//      @return True, that means end of player's attack
//               and then enemy will hit, cause ACTION.UP called after
//               ACTION.DOW return true;
//               False, if attack was successul and player will attack
//               again, cause of game rules.

    private boolean playerAttackEnemy(int i, int j){
        sBattle.setTurn(playerTurn);
        sBattle.invalidate();
        if (playerTurn) {
            if (!sGame.attackEnemy(i, j)) {
                //unsuccessful
                sBattle.setTurn(false);
                sBattle.setBoard(sGame.getPlayerPlacement(),
                        sGame.getEnemyPlacement());
                //in fact draw red circle that means enemy turn now
                sBattle.invalidate();
                //turn enemy move
                enemyTurn = true;
                playerTurn = false;
                return true;
            }
            //check winner
            int winner = sGame.getWinner();
            if (winner != -1) {
                sBattle.setWinner(winner);
                sBattle.setBoard(sGame.getPlayerPlacement(),
                        sGame.getEnemyPlacement());
                sBattle.invalidate();
                return false;
            }
            sBattle.setTurn(true);
            sBattle.setBoard(sGame.getPlayerPlacement(),
                    sGame.getEnemyPlacement());
            sBattle.invalidate();
            return false;
        }
        return false;
    }

    private boolean enemyAttackPlayer() {
        if (enemyTurn) {
            sBattle.setTurn(false);
            sBattle.invalidate();
            //while enemy is attacking only player's ships
            while (sGame.attackPlayer()) {
                sBattle.setBoard(sGame.getPlayerPlacement(),
                        sGame.getEnemyPlacement());
                //wait and give info to player that "now enemy is thinking"
                try {
                    sleep(500);
                } catch (Exception ie) {}
                int winner = sGame.getWinner();
                if (winner != -1) {
                    sBattle.setWinner(winner);
                    sBattle.setBoard(sGame.getPlayerPlacement(),
                            sGame.getEnemyPlacement());
                    sBattle.invalidate();
                    return false;
                }
            }
            //hitting finished, cause enemy fluffed
            sBattle.setBoard(sGame.getPlayerPlacement(),
                    sGame.getEnemyPlacement());
            playerTurn = true;
            sBattle.setTurn(true);
            sBattle.invalidate();
            enemyTurn = false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Can restart the game;
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                initApp();
                return true;
        }
        return true;
    }
}