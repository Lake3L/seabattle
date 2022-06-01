package com.example.seabattle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;

//interface of board and ships
public class Interface extends SurfaceView {
    //background
    private Bitmap bitmap;
    private Paint paint;

    private boolean turn = true; //если ходит компьютер, то false, а если игрок - true.

    //1 if player won
    //2 if enemy won,
    //-1 if nobody have won yet.
    private int winner = -1;
    private static  int cellSize; //for different screens
    private int xStart = 0, yStart = 0;
    private int endEnemyFieldY;
    private  int startEnemyFieldX;

    private int[][] player;//updating models
    private int[][] enemy;

    public Interface(Context context) {
        super(context);
        paint = new Paint();
        setWillNotDraw(false);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.battle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0,0,null);
        canvas.save();
        drawBoard(canvas);
        canvas.restore();
        canvas.save();
        drawShips(canvas);
        canvas.restore();
        checkWinner(canvas);
    }

    private boolean checkWinner(Canvas canvas) {
        if(winner != -1) {
            paint.setColor(Color.MAGENTA);
            paint.setTextSize(72);
            //canvas.drawRect(150, 150, getWidth() * 0.7f, getHeight() * 0.7f, paint);
            if(winner == 1)
                canvas.drawText("You won!",getWidth()/2,
                        getHeight()/2,paint );
            else canvas.drawText("Game over!",getWidth()/2,
                    getHeight()/2, paint );
            return true;
        }
        return false;
    }



    private void drawBoard(Canvas canvas){
        cellSize = getWidth()/(2*PositionAndMove.N ) - 8;
        paint.setColor(Color.WHITE);

        int plr = PositionAndMove.N * cellSize;

        //vertical lines
        for(int i = 0; i <= plr; i += cellSize)
            canvas.drawLine(i, xStart, i, plr, paint );
        //horizontal lines
        for(int i = 0; i <= plr; i += cellSize)
            canvas.drawLine(yStart, i, plr, i, paint );

        //draw indicator
        if(turn)
            paint.setColor(Color.GREEN);
        else
            paint.setColor(Color.RED);
        canvas.drawCircle(plr+75, getHeight()/2,10,paint);

        startEnemyFieldX = plr + getWidth()/5;
        endEnemyFieldY = cellSize * PositionAndMove.N;
        paint.setColor(Color.BLACK);

        //vertical lines
        for(int i = 0; i <= plr; i += cellSize)
            canvas.drawLine(i, yStart, i, plr, paint );
        //horizontal lines
        for(int i = 0; i <= plr; i += cellSize)
            canvas.drawLine(xStart, i, plr, i, paint );
    }

    public void setBoard(int[][] player, int[][] enemy){
        this.player = new int[PositionAndMove.N][PositionAndMove.N];
        this.enemy = new int[PositionAndMove.N][PositionAndMove.N];
        for(int i = 0; i <PositionAndMove.N; ++i)
            for(int j = 0; j <PositionAndMove.N; ++j){
                this.player[i][j] = player[i][j];
                this.enemy[i][j] = enemy[i][j];
            }
    }

    private void drawShips(Canvas canvas){
        Paint paint = new Paint();
        //your step
        for(int i = 0; i < PositionAndMove.N; ++i)
            for(int j = 0; j < PositionAndMove.N;++j) {
                if (player[i][j] == PositionAndMove.SHIP) {
                    paint.setARGB(150, 0, 150, 100);
                    canvas.drawRect(j * cellSize, i * cellSize,
                            (j + 1) * cellSize, (i + 1) * cellSize, paint);
                } else if (player[i][j] == PositionAndMove.ATTACKED) {
                    paint.setARGB(150, 0, 0, 100);
                    canvas.drawRect(j * cellSize, i * cellSize,
                            (j + 1) * cellSize, (i + 1) * cellSize, paint);
                } else if (player[i][j] == PositionAndMove.ATTACKED_SHIP) {
                    paint.setARGB(150, 250, 0, 0);
                    canvas.drawRect(j * cellSize, i * cellSize,
                            (j + 1) * cellSize, (i + 1) * cellSize, paint);
                }
            }
        //enemy step
        canvas.translate(startEnemyFieldX, 0);
        for(int i = 0; i < PositionAndMove.N; ++i)
            for(int j = 0; j < PositionAndMove.N;++j) {
                if (player[i][j] == PositionAndMove.SHIP) {
                    paint.setARGB(150, 0, 150, 100);
                    canvas.drawRect(j * cellSize, i * cellSize,
                            (j + 1) * cellSize, (i + 1) * cellSize, paint);
                } else if (player[i][j] == PositionAndMove.ATTACKED_SHIP) {
                    paint.setARGB(150, 250, 0, 0);
                    canvas.drawRect(j * cellSize, i * cellSize,
                            (j + 1) * cellSize, (i + 1) * cellSize, paint);
                }
            }

    }


    public void setCell(boolean player){
        int[][] field;
        if(player)
            field = this.player;
        else field = this.enemy;

    }

    public void setTurn(boolean t) {
        this.turn = t;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public int getEndEnemyFieldY() {
        return endEnemyFieldY;
    }

    public int getCellSize() {
        return cellSize;
    }

    public int getStartEnemyFieldX() {
        return startEnemyFieldX;
    }
}
